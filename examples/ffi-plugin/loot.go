// Example :ffi loot plugin in Go. cgo compiles this to a C-ABI shared library:
//
//   macOS: go build -buildmode=c-shared -o libloot.dylib loot.go
//   Linux: go build -buildmode=c-shared -o libloot.so   loot.go
//
//   {:type :ffi :id :ffi-loot :library "libloot.dylib"
//    :symbol "generate" :free-symbol "loot_free"}
//
// `generate` receives the request JSON ({"inputs",...} for a roll,
// {"action","params","view-model",...} for a follow-up) and returns a C string
// holding a view-model — the same one a :jar plugin returns — which the engine
// reads and then frees via `loot_free`. Unlike the C example, this one actually
// parses the request (encoding/json is in the standard library) and builds the
// output from typed structs — the shape a real plugin would use.
//
// It also shows `item/vars`: `item/body` is a *template* rendered in the
// browser against them, so the DM edits the damage as a number rather than by
// retyping prose, and the edited value comes back on `view-model` when the
// action fires.
package main

/*
#include <stdlib.h>
*/
import "C"

import (
	"encoding/json"
	"unsafe"
)

type request struct {
	Inputs    map[string]any `json:"inputs"`
	Action    string         `json:"action"`
	Params    map[string]any `json:"params"`
	ViewModel *output        `json:"view-model"`
}

// A resolved variable an item's template interpolates. `type` tells the browser
// which control to render and how to parse what the DM types back.
type itemVar struct {
	Value any    `json:"value"`
	Type  string `json:"type,omitempty"`
}

type item struct {
	Title    string             `json:"item/title,omitempty"`
	Body     string             `json:"item/body"`
	Metadata []string           `json:"item/metadata,omitempty"`
	Vars     map[string]itemVar `json:"item/vars,omitempty"`
}

type section struct {
	Heading string `json:"section/heading,omitempty"`
	Items   []item `json:"section/items"`
}

// `label`/`action`/`params`, not a built `:action/event`: the engine fills in
// the id this plugin was registered under and routes the click back here.
type action struct {
	Label  string         `json:"label"`
	Action string         `json:"action"`
	Params map[string]any `json:"params,omitempty"`
}

type output struct {
	Title    string    `json:"loot/title"`
	Subtitle string    `json:"loot/subtitle,omitempty"`
	Sections []section `json:"loot/sections,omitempty"`
	Actions  []action  `json:"loot/actions,omitempty"`
}

func blade(title string, keen int) output {
	return output{
		Title: title,
		Sections: []section{{
			Heading: "Loot",
			Items: []item{{
				Title:    "Rusty Dagger",
				Body:     "A worn blade, +{{ keen }} keener.",
				Metadata: []string{"common"},
				Vars:     map[string]itemVar{"keen": {Value: keen, Type: "int"}},
			}},
		}},
		Actions: []action{{Label: "Sharpen", Action: "sharpen", Params: map[string]any{"by": 1}}},
	}
}

// keenness reads the displayed value back off the view-model the UI returned, so
// a DM edit is what the next sharpening builds on.
func keenness(vm *output) int {
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

//export generate
func generate(request_json *C.char) *C.char {
	var req request
	// A malformed request is a programming error on the engine side; the zero
	// value (no action) then simply rolls, which is a safe default.
	_ = json.Unmarshal([]byte(C.GoString(request_json)), &req)

	out := blade("Rusty Dagger", 0)
	if req.Action != "" {
		out = blade("Sharpened Blade", keenness(req.ViewModel)+1)
	}

	body, _ := json.Marshal(out)
	// C.CString malloc's a copy the engine owns until it calls loot_free.
	return C.CString(string(body))
}

//export loot_free
func loot_free(p *C.char) {
	C.free(unsafe.Pointer(p))
}

func main() {}
