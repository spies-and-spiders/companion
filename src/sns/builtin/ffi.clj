(ns sns.builtin.ffi
  "Adapter for `:ffi` plugins: call a C-ABI symbol in a shared library
   (.so/.dylib/.dll) with the request as a JSON C string and read a *friendly*
   JSON view-model back. Unlike `:jar` (which loads JVM bytecode and so cannot run
   in a native image), this works in both the uberjar and the native image.

   The library exports `(char* request_json) -> char* output_json`, sharing the
   friendly contract with `:cli` (see sns.builtin.plugin-io). Generation passes
   `{:inputs :session}`, an action passes `{:action :params :session}`; the
   presence of `action` tells the plugin which it is. If `free-symbol` is given it
   is called on the returned pointer once its bytes are copied out; otherwise the
   library owns that memory (e.g. a reused static buffer).

   Native image needs the two downcall descriptors registered at build time via
   the `foreign` section of reachability-metadata.json (the signatures are fixed,
   so one entry each covers every :ffi plugin) and `--enable-native-access`."
  (:require
    [sns.builtin.plugin-io :as io]
    [sns.sdk.protocols :as p])
  (:import
    (java.lang.foreign Arena FunctionDescriptor Linker Linker$Option MemoryLayout MemorySegment SymbolLookup ValueLayout)
    (java.lang.invoke MethodHandle)
    (java.util.concurrent ConcurrentHashMap)
    (java.util.function Function)))

;; One SymbolLookup per library path, so several :ffi plugins bound to different
;; symbols of the same library open (dlopen) it once and share the handle. Empty
;; at native-image build time (no foreign state baked into the image heap);
;; populated at run time. computeIfAbsent loads at most once per key, even racing.
(defonce ^:private lookups (ConcurrentHashMap.))

(defn- library-lookup
  ^SymbolLookup [^String library]
  (.computeIfAbsent ^ConcurrentHashMap lookups library
                    (reify Function
                      (apply [_ lib]
                        (SymbolLookup/libraryLookup ^String lib (Arena/global))))))

(defn- downcall
  "Bind `sym` in `lookup` as a `MethodHandle` for function descriptor `desc`."
  ^MethodHandle [^Linker linker ^SymbolLookup lookup ^String sym ^FunctionDescriptor desc]
  (let [^MemorySegment addr (.orElseThrow (.find lookup sym))]
    (.downcallHandle linker addr desc (make-array Linker$Option 0))))

(defn- call
  "Invoke `handle` with `ctx` encoded as a JSON C string, read the returned C
   string, free it via `free` (when given), and map it to a view-model."
  [id ^MethodHandle handle ^MethodHandle free ctx]
  (let [arena (Arena/ofConfined)]
    (try
      (let [^MemorySegment req (.allocateFrom arena ^String (io/encode-request ctx))
            ^MemorySegment res (.invokeWithArguments handle (object-array [req]))
            out                (.getString (.reinterpret res Long/MAX_VALUE) 0)]
        (when free
          (.invokeWithArguments free (object-array [res])))
        (io/read-output id out))
      (finally
        (.close arena)))))

;; Every :ffi symbol has the same fixed C signature, so the two descriptors are
;; built once per plugin at startup (never at native-image build time — the
;; foreign machinery is initialised at run time) and registered for downcall via
;; the `foreign` section of reachability-metadata.json.
(defn- ptr->ptr []
  (FunctionDescriptor/of ValueLayout/ADDRESS (into-array MemoryLayout [ValueLayout/ADDRESS])))

(defn- ptr->void []
  (FunctionDescriptor/ofVoid (into-array MemoryLayout [ValueLayout/ADDRESS])))

(defn generator
  "Build a `LootGenerator`/`LootAction` bound to `symbol` (and optional
   `free-symbol`) in the shared library at `library`. The library stays loaded for
   the app's lifetime (a global arena). `utility?` marks a session tool rather
   than loot."
  ([id library symbol free-symbol] (generator id library symbol free-symbol false))
  ([id library symbol free-symbol utility?]
   (let [linker (Linker/nativeLinker)
         lookup (library-lookup (str library))
         handle (downcall linker lookup (str symbol) (ptr->ptr))
         free   (when free-symbol
                  (downcall linker lookup (str free-symbol) (ptr->void)))]
     (reify
       p/LootGenerator
       (loot-spec [_]
         (cond-> {:id id :label (name id)}
                 utility? (assoc :utility? true)))
       (generate [_ {:keys [inputs session]}]
         (call id handle free {:inputs inputs :session session}))
       p/LootAction
       (handle-action [_ {:keys [session]} action params]
         (call id handle free {:action action :params params :session session}))))))
