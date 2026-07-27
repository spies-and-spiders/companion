// Example :ffi loot plugin in Go. cgo compiles this to a C-ABI shared library:
//
//   macOS: go build -buildmode=c-shared -o libloot.dylib loot.go
//   Linux: go build -buildmode=c-shared -o libloot.so   loot.go
//
//   {:type :ffi :id :ffi-loot :library "libloot.dylib"
//    :symbol "generate" :free-symbol "loot_free"}
//
// `generate` receives the request JSON ({"inputs",...} for a roll,
// {"action","params",...} for a follow-up) and returns a C string the engine
// reads and then frees via `loot_free`. Unlike the C example, this one actually
// parses the request (encoding/json is in the standard library) and builds the
// output from typed structs — the shape a real plugin would use.
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
	Inputs map[string]any `json:"inputs"`
	Action string         `json:"action"`
	Params map[string]any `json:"params"`
}

type item struct {
	Title    string   `json:"title,omitempty"`
	Body     string   `json:"body"`
	Metadata []string `json:"metadata,omitempty"`
}

type section struct {
	Heading string `json:"heading,omitempty"`
	Items   []item `json:"items"`
}

type action struct {
	Label  string         `json:"label"`
	Action string         `json:"action"`
	Params map[string]any `json:"params,omitempty"`
}

type output struct {
	Title    string    `json:"title"`
	Subtitle string    `json:"subtitle,omitempty"`
	Sections []section `json:"sections,omitempty"`
	Actions  []action  `json:"actions,omitempty"`
}

func roll() output {
	return output{
		Title: "Rusty Dagger",
		Sections: []section{{
			Heading: "Loot",
			Items:   []item{{Title: "Rusty Dagger", Body: "A worn blade.", Metadata: []string{"common"}}},
		}},
		Actions: []action{{Label: "Sharpen", Action: "sharpen", Params: map[string]any{"by": 1}}},
	}
}

func sharpen() output {
	return output{
		Title:    "Sharpened Blade",
		Sections: []section{{Items: []item{{Body: "The blade is now +1 keener."}}}},
	}
}

//export generate
func generate(request_json *C.char) *C.char {
	var req request
	// A malformed request is a programming error on the engine side; the zero
	// value (no action) then simply rolls, which is a safe default.
	_ = json.Unmarshal([]byte(C.GoString(request_json)), &req)

	out := roll()
	if req.Action != "" {
		out = sharpen()
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
