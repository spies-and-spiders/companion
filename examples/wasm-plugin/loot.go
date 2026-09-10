// Example sns-companion :wasm loot plugin: a WASI command module.
//
//	GOOS=wasip1 GOARCH=wasm go build -o loot.wasm loot.go
//
//	{:type :wasm :id :wasm-loot :wasm {:module "loot.wasm"}}
//
// The request arrives as JSON on stdin and a view-model goes out on stdout —
// the same contract :cli speaks, so one source can serve both. Unlike :ffi this
// needs no C toolchain and no per-platform build: one module runs everywhere,
// sandboxed, and in the native image too.
//
// It shows the two things the view-model carries that finished prose cannot:
// "item/vars", so the DM edits the number rather than retyping the body, and
// "loot/state", opaque bookkeeping the engine hands back with the next action.
package main

import (
	"encoding/json"
	"fmt"
	"os"
)

type itemVar struct {
	Value any    `json:"value"`
	Type  string `json:"type,omitempty"`
}

type item struct {
	Title string             `json:"item/title,omitempty"`
	Body  string             `json:"item/body"`
	Vars  map[string]itemVar `json:"item/vars,omitempty"`
}

type section struct {
	Items []item `json:"section/items"`
}

// label/action/params rather than a built ":action/event": the engine fills in
// the id this module was registered under and routes the click back here.
type action struct {
	Label  string         `json:"label"`
	Action string         `json:"action"`
	Params map[string]any `json:"params,omitempty"`
}

type state struct {
	Tier int `json:"tier"`
}

type viewModel struct {
	Title    string    `json:"loot/title"`
	Sections []section `json:"loot/sections,omitempty"`
	Actions  []action  `json:"loot/actions,omitempty"`
	State    *state    `json:"loot/state,omitempty"`
}

// A generate call carries "inputs"; an action carries "action" and the
// view-model the UI had on screen, DM edits included.
type request struct {
	Action    string     `json:"action"`
	ViewModel *viewModel `json:"view-model"`
}

func blade(keen, tier int) viewModel {
	return viewModel{
		Title: "Whetstone Blade",
		Sections: []section{{Items: []item{{
			Title: "Whetstone Blade",
			Body:  "A plain blade, +{{ keen }} keener.",
			Vars:  map[string]itemVar{"keen": {Value: keen, Type: "int"}},
		}}}},
		Actions: []action{{Label: "Sharpen", Action: "sharpen", Params: map[string]any{"by": 1}}},
		State:   &state{Tier: tier},
	}
}

// keenness reads the displayed value back off the view-model the UI returned,
// so a DM edit is what the next sharpening builds on.
func keenness(vm *viewModel) int {
	if vm == nil || len(vm.Sections) == 0 || len(vm.Sections[0].Items) == 0 {
		return 0
	}
	if v, ok := vm.Sections[0].Items[0].Vars["keen"]; ok {
		if n, ok := v.Value.(float64); ok {
			return int(n)
		}
	}
	return 0
}

func tier(vm *viewModel) int {
	if vm == nil || vm.State == nil {
		return 0
	}
	return vm.State.Tier
}

func main() {
	var req request
	// The engine always writes a request, so a decode failure is a contract
	// breach; the zero value then simply generates, which is a safe default.
	_ = json.NewDecoder(os.Stdin).Decode(&req)

	out := blade(0, 1)
	if req.Action == "sharpen" {
		out = blade(keenness(req.ViewModel)+1, tier(req.ViewModel)+1)
	}

	if err := json.NewEncoder(os.Stdout).Encode(out); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(1)
	}
}
