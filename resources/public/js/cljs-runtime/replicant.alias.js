goog.provide('replicant.alias');
replicant.alias.aliases = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
/**
 * Register an alias. Associates the alias key `k` with the function `f`:
 * 
 * ```clj
 * (replicant.alias/register! :ui/a custom-link)
 * ```
 */
replicant.alias.register_BANG_ = (function replicant$alias$register_BANG_(k,f){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(replicant.alias.aliases,cljs.core.assoc,k,f);
});
/**
 * Returns globally registered aliases
 */
replicant.alias.get_registered_aliases = (function replicant$alias$get_registered_aliases(){
return cljs.core.deref(replicant.alias.aliases);
});
replicant.alias.__GT_hiccup = (function replicant$alias$__GT_hiccup(headers){
if(cljs.core.truth_(headers)){
var or__5162__auto__ = (headers[(8)]);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1((headers[(0)])),(function (){var attrs = replicant.core.get_attrs(headers);
var G__40138 = (headers[(4)]);
var G__40138__$1 = (cljs.core.truth_(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(attrs))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__40138,new cljs.core.Keyword(null,"id","id",-1388402092),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(attrs)):G__40138);
if(cljs.core.truth_(new cljs.core.Keyword(null,"classes","classes",2037804510).cljs$core$IFn$_invoke$arity$1(attrs))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__40138__$1,new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.set(new cljs.core.Keyword(null,"classes","classes",2037804510).cljs$core$IFn$_invoke$arity$1(attrs)));
} else {
return G__40138__$1;
}
})()], null),replicant.core.flatten_seqs((headers[(5)])));
}
} else {
return null;
}
});
replicant.alias.alias_hiccup_QMARK_ = (function replicant$alias$alias_hiccup_QMARK_(x){
return ((replicant.hiccup.hiccup_QMARK_(x)) && (cljs.core.qualified_keyword_QMARK_(cljs.core.first(x))));
});
replicant.alias.expand_aliased_hiccup = (function replicant$alias$expand_aliased_hiccup(x,opt){
if(replicant.alias.alias_hiccup_QMARK_(x)){
var headers = replicant.core.get_hiccup_headers(null,x);
var defined_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"aliases","aliases",1346874714).cljs$core$IFn$_invoke$arity$1(opt),(headers[(0)]));
if(((cljs.core.not(defined_QMARK_)) && (cljs.core.get.cljs$core$IFn$_invoke$arity$3(opt,new cljs.core.Keyword(null,"ignore-missing-alias?","ignore-missing-alias?",1175846938),true) === false))){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2((""+"Tried to expand undefined alias "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((headers[(0)]))),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"alias","alias",-2039751630),(headers[(0)])], null));
} else {
}

var G__40139 = headers;
var G__40139__$1 = (cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"aliases","aliases",1346874714).cljs$core$IFn$_invoke$arity$1(opt),(headers[(0)])))?replicant.core.get_alias_headers(opt,G__40139):G__40139);
return replicant.alias.__GT_hiccup(G__40139__$1);

} else {
return x;
}
});
replicant.alias.get_opts = (function replicant$alias$get_opts(opt){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(opt,new cljs.core.Keyword(null,"aliases","aliases",1346874714),(function (p1__40140_SHARP_){
var or__5162__auto__ = p1__40140_SHARP_;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return replicant.alias.get_registered_aliases();
}
}));
});
/**
 * Expand the first level of aliases in `hiccup`. The result may contain aliases
 *   if returned by the top-level aliases. If using aliases that are not in the
 *   global registry, pass `:aliases` in `opt`.
 */
replicant.alias.expand_1 = (function replicant$alias$expand_1(var_args){
var args__5903__auto__ = [];
var len__5897__auto___40155 = arguments.length;
var i__5898__auto___40156 = (0);
while(true){
if((i__5898__auto___40156 < len__5897__auto___40155)){
args__5903__auto__.push((arguments[i__5898__auto___40156]));

var G__40157 = (i__5898__auto___40156 + (1));
i__5898__auto___40156 = G__40157;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((1) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((1)),(0),null)):null);
return replicant.alias.expand_1.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5904__auto__);
});

(replicant.alias.expand_1.cljs$core$IFn$_invoke$arity$variadic = (function (hiccup,p__40144){
var vec__40145 = p__40144;
var opt = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__40145,(0),null);
var opt__$1 = replicant.alias.get_opts(opt);
return clojure.walk.postwalk((function (p1__40141_SHARP_){
return replicant.alias.expand_aliased_hiccup(p1__40141_SHARP_,opt__$1);
}),hiccup);
}));

(replicant.alias.expand_1.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(replicant.alias.expand_1.cljs$lang$applyTo = (function (seq40142){
var G__40143 = cljs.core.first(seq40142);
var seq40142__$1 = cljs.core.next(seq40142);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__40143,seq40142__$1);
}));

/**
 * Recursively expand all aliases in `hiccup`. The result will not contain
 *   aliases. If using aliases that are not in the global registry, pass `:aliases`
 *   in `opt`.
 */
replicant.alias.expand = (function replicant$alias$expand(var_args){
var args__5903__auto__ = [];
var len__5897__auto___40158 = arguments.length;
var i__5898__auto___40159 = (0);
while(true){
if((i__5898__auto___40159 < len__5897__auto___40158)){
args__5903__auto__.push((arguments[i__5898__auto___40159]));

var G__40160 = (i__5898__auto___40159 + (1));
i__5898__auto___40159 = G__40160;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((1) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((1)),(0),null)):null);
return replicant.alias.expand.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5904__auto__);
});

(replicant.alias.expand.cljs$core$IFn$_invoke$arity$variadic = (function (hiccup,p__40151){
var vec__40152 = p__40151;
var opt = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__40152,(0),null);
var opt__$1 = replicant.alias.get_opts(opt);
return clojure.walk.prewalk((function (p1__40148_SHARP_){
return replicant.alias.expand_aliased_hiccup(p1__40148_SHARP_,opt__$1);
}),hiccup);
}));

(replicant.alias.expand.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(replicant.alias.expand.cljs$lang$applyTo = (function (seq40149){
var G__40150 = cljs.core.first(seq40149);
var seq40149__$1 = cljs.core.next(seq40149);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__40150,seq40149__$1);
}));


//# sourceMappingURL=replicant.alias.js.map
