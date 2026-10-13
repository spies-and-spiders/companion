(ns sns.builtin.ffi
  "Adapter for `:ffi` plugins: calls a `char* (char* request_json)` symbol in a
   shared library. See `sns.builtin.plugin-io`.

   The native image needs `--enable-native-access` and both downcall
   descriptors registered in reachability-metadata.json's `foreign` section."
  (:require
    [sns.builtin.plugin-io :as io]
    [sns.sdk.protocols :as p])
  (:import
    (java.lang.foreign Arena FunctionDescriptor Linker Linker$Option MemoryLayout MemorySegment SymbolLookup ValueLayout)
    (java.lang.invoke MethodHandle)
    (java.util.concurrent ConcurrentHashMap)
    (java.util.function Function)))

;; Per library path, so plugins sharing a library load it once.
(defonce ^:private lookups (ConcurrentHashMap.))

(defn- library-lookup
  ^SymbolLookup [^String library]
  (.computeIfAbsent ^ConcurrentHashMap lookups library
                    (reify Function
                      (apply [_ lib]
                        (SymbolLookup/libraryLookup ^String lib (Arena/global))))))

(defn- downcall
  ^MethodHandle [^Linker linker ^SymbolLookup lookup ^String sym ^FunctionDescriptor desc]
  (let [^MemorySegment addr (.orElseThrow (.find lookup sym))]
    (.downcallHandle linker addr desc (make-array Linker$Option 0))))

(defn- call
  "Call `handle` on `ctx`, freeing the result with `free` if given."
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

;; Functions rather than values, so they're never built at native-image build time.
(defn- ptr->ptr []
  (FunctionDescriptor/of ValueLayout/ADDRESS (into-array MemoryLayout [ValueLayout/ADDRESS])))

(defn- ptr->void []
  (FunctionDescriptor/ofVoid (into-array MemoryLayout [ValueLayout/ADDRESS])))

(defn generator
  "Build a `Generator`/`Action` from an `:ffi` plugin config entry. The library
   stays loaded for the app's lifetime."
  [{:keys                                     [id]
    {:keys [library free-symbol] sym :symbol} :ffi
    :as                                       plugin}]
  (let [linker (Linker/nativeLinker)
        lookup (library-lookup (str library))
        handle (downcall linker lookup (str sym) (ptr->ptr))
        free   (when free-symbol
                 (downcall linker lookup (str free-symbol) (ptr->void)))
        colls  (io/collections plugin)]
    (reify
      p/Generator
      (loot-spec [_] {})
      (generate [_ ctx]
        (call id handle free (io/with-state ctx colls {:inputs (:inputs ctx)})))
      p/Action
      (handle-action [_ ctx action params]
        (call id handle free (io/with-state ctx colls (io/action-request ctx action params)))))))
