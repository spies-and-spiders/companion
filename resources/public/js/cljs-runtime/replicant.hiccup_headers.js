goog.provide('replicant.hiccup_headers');
/**
 * Key for a keyed node. A [tag k] tuple, except under squint where JS Set and
 *   Map compare composite keys by reference, so a length-prefixed string is used.
 */
replicant.hiccup_headers.make_key = (function replicant$hiccup_headers$make_key(tag,k){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tag,k], null);
});

//# sourceMappingURL=replicant.hiccup_headers.js.map
