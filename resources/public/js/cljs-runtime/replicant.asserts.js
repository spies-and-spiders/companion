goog.provide('replicant.asserts');
replicant.asserts.camel__GT_dash = (function replicant$asserts$camel__GT_dash(s){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("-",cljs.core.map.cljs$core$IFn$_invoke$arity$2(clojure.string.lower_case,cljs.core.re_seq(/[A-Z][a-z0-9]*|[a-z0-9]+/,s)));
});
replicant.asserts.camel__GT_dash_k = (function replicant$asserts$camel__GT_dash_k(k){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(replicant.asserts.camel__GT_dash(cljs.core.name(k)));
});
replicant.asserts.has_bad_conditional_attrs_QMARK_ = (function replicant$asserts$has_bad_conditional_attrs_QMARK_(vdom,headers){
if(((((0) < cljs.core.count((headers[(5)])))) || (((0) < cljs.core.count((vdom[(4)])))))){
var vec__37960 = (headers[(7)]);
var new_selector = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37960,(0),null);
var new_attrs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37960,(1),null);
var vec__37963 = (vdom[(7)]);
var old_selector = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37963,(0),null);
var old_attrs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37963,(1),null);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new_selector,old_selector)){
return false;
} else {
if((new_attrs == null)){
return cljs.core.map_QMARK_(old_attrs);
} else {
if(cljs.core.map_QMARK_(new_attrs)){
return (old_attrs == null);
} else {
return false;

}
}
}
} else {
return false;
}
});
replicant.asserts.abbreviate_map = (function replicant$asserts$abbreviate_map(m,n){
return (""+"{"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$2(", ",cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p__37971){
var vec__37972 = p__37971;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37972,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37972,(1),null);
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(k)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core.map_QMARK_(v))?(replicant.asserts.abbreviate_map.cljs$core$IFn$_invoke$arity$2 ? replicant.asserts.abbreviate_map.cljs$core$IFn$_invoke$arity$2(v,n) : replicant.asserts.abbreviate_map.call(null,v,n)):cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([v], 0)))));
}),cljs.core.take.cljs$core$IFn$_invoke$arity$2(n,m))))+cljs.core.str.cljs$core$IFn$_invoke$arity$1((((n < cljs.core.count(m)))?" ,,,":null))+"}");
});
replicant.asserts.abbreviate_node = (function replicant$asserts$abbreviate_node(x){
if(replicant.hiccup.hiccup_QMARK_(x)){
return (""+"["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first(x))+" ,,,]");
} else {
if((x == null)){
return "nil";
} else {
if(typeof x === 'string'){
if(((20) < ((x).length))){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.take.cljs$core$IFn$_invoke$arity$2((20),x)))+"...");
} else {
return x;
}
} else {
if(cljs.core.coll_QMARK_(x)){
return (""+"(,,,)");
} else {
return cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([x], 0));

}
}
}
}
});
replicant.asserts.format_hiccup_part = (function replicant$asserts$format_hiccup_part(x){
if(cljs.core.map_QMARK_(x)){
return replicant.asserts.abbreviate_map(x,(2));
} else {
if(replicant.hiccup.hiccup_QMARK_(x)){
var s = cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([x], 0));
if((((s).length) < (20))){
return s;
} else {
return replicant.asserts.abbreviate_node(x);
}
} else {
if(cljs.core.coll_QMARK_(x)){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),cljs.core.count(x))){
return (""+"("+cljs.core.str.cljs$core$IFn$_invoke$arity$1(replicant.asserts.abbreviate_node(cljs.core.first(x)))+")");
} else {
return (""+"("+cljs.core.str.cljs$core$IFn$_invoke$arity$1(replicant.asserts.abbreviate_node(cljs.core.first(x)))+" ,,,)");
}
} else {
return cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([x], 0));

}
}
}
});
replicant.asserts.convey_bad_conditional_attributes = (function replicant$asserts$convey_bad_conditional_attributes(vdom,headers){
var vec__37981 = cljs.core.first((function (){var or__5162__auto__ = cljs.core.not_empty((vdom[(3)]));
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.not_empty((headers[(4)]));
}
})());
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37981,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__37981,(1),null);
return (""+"Replicant treats nils as hints of nodes that come and go. Wrapping "+"the entire attribute map in a conditional such that what used to be "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(replicant.asserts.format_hiccup_part(cljs.core.second((vdom[(7)]))))+" is now "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(replicant.asserts.format_hiccup_part(cljs.core.second((headers[(7)]))))+" can impair how well Replicant can match up child nodes without keys, and "+"may lead to undesirable behavior for life-cycle events and transitions.\n\n"+"Instead of:\n["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first((headers[(7)])))+" (when something? {"+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(k)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(k)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([v], 0)))):null))+"}) ,,,]\n\nConsider:\n["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first((headers[(7)])))+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(k)?(""+"\n  "+"(cond-> {}\n    something? (assoc "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(k)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([v], 0)))+"))\n"):" {}"))+" ,,,]");
});

//# sourceMappingURL=replicant.asserts.js.map
