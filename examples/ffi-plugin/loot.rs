// Example :ffi loot plugin in Rust, compiled to a C-ABI shared library:
//
//   macOS: rustc --crate-type cdylib -o libloot.dylib loot.rs
//   Linux: rustc --crate-type cdylib -o libloot.so   loot.rs
//
//   {:type :ffi :id :ffi-loot :library "libloot.dylib"
//    :symbol "generate" :free-symbol "loot_free"}
//
// `generate` receives the request JSON ({"inputs",...} for a roll,
// {"action","params",...} for a follow-up) and returns a Rust-allocated C string
// the engine reads and then frees via `loot_free`. Like the C example this stays
// dependency-free and only branches on whether an action is present; a real
// plugin would parse the request and build the output with serde_json.

use std::ffi::{c_char, CStr, CString};

/// # Safety
/// `request` must be a valid, NUL-terminated C string, as the engine always
/// passes. The returned pointer is owned by the caller until handed to
/// `loot_free`.
#[no_mangle]
pub unsafe extern "C" fn generate(request: *const c_char) -> *mut c_char {
    let request = CStr::from_ptr(request).to_string_lossy();
    let body = if request.contains("\"action\"") {
        r#"{"title":"Sharpened Blade","sections":[{"items":[{"body":"The blade is now +1 keener."}]}]}"#
    } else {
        r#"{"title":"Rusty Dagger","sections":[{"heading":"Loot","items":[{"title":"Rusty Dagger","body":"A worn blade.","metadata":["common"]}]}],"actions":[{"label":"Sharpen","action":"sharpen","params":{"by":1}}]}"#
    };
    // into_raw hands ownership to the engine; loot_free reclaims it below.
    CString::new(body).unwrap().into_raw()
}

/// # Safety
/// `p` must be a pointer previously returned by `generate` and not yet freed.
#[no_mangle]
pub unsafe extern "C" fn loot_free(p: *mut c_char) {
    if !p.is_null() {
        drop(CString::from_raw(p));
    }
}
