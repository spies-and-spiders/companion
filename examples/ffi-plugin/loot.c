/*
 * Example :ffi loot plugin. Build a shared library and point config at it:
 *
 *   macOS: cc -shared -fPIC -o libloot.dylib loot.c
 *   Linux: cc -shared -fPIC -o libloot.so   loot.c
 *
 *   {:type :ffi :id :ffi-loot :library "libloot.dylib"
 *    :symbol "generate" :free-symbol "loot_free"}
 *
 * `generate` receives the request JSON ({"inputs",...} for a roll,
 * {"action","params",...} for a follow-up) and returns a malloc'd, friendly
 * output JSON that the engine reads and then frees via `loot_free`. A real
 * plugin would parse the request; this example only branches on whether an
 * action is present, to keep it dependency-free.
 */
#include <stdlib.h>
#include <string.h>

char *generate(const char *request) {
    const char *body =
        strstr(request, "\"action\"") != NULL
            ? "{\"title\":\"Sharpened Blade\",\"sections\":[{\"items\":["
              "{\"body\":\"The blade is now +1 keener.\"}]}]}"
            : "{\"title\":\"Rusty Dagger\","
              "\"sections\":[{\"heading\":\"Loot\",\"items\":["
              "{\"title\":\"Rusty Dagger\",\"body\":\"A worn blade.\","
              "\"metadata\":[\"common\"]}]}],"
              "\"actions\":[{\"label\":\"Sharpen\",\"action\":\"sharpen\","
              "\"params\":{\"by\":1}}]}";
    char *out = (char *)malloc(strlen(body) + 1);
    strcpy(out, body);
    return out;
}

void loot_free(char *p) { free(p); }
