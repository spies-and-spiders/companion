goog.provide('nexus.registry');
nexus.registry._BANG_registry = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
nexus.registry.register_system__GT_state_BANG_ = (function nexus$registry$register_system__GT_state_BANG_(f){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(nexus.registry._BANG_registry,cljs.core.assoc,new cljs.core.Keyword("nexus","system->state","nexus/system->state",-142405750),f);
});
nexus.registry.register_action_BANG_ = (function nexus$registry$register_action_BANG_(action_k,f){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(nexus.registry._BANG_registry,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("nexus","actions","nexus/actions",-707386605),action_k], null),f);
});
nexus.registry.register_effect_BANG_ = (function nexus$registry$register_effect_BANG_(effect_k,f){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(nexus.registry._BANG_registry,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("nexus","effects","nexus/effects",-865511691),effect_k], null),f);
});
nexus.registry.register_placeholder_BANG_ = (function nexus$registry$register_placeholder_BANG_(placeholder_k,f){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(nexus.registry._BANG_registry,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("nexus","placeholders","nexus/placeholders",1656029064),placeholder_k], null),f);
});
nexus.registry.register_interceptor_BANG_ = (function nexus$registry$register_interceptor_BANG_(var_args){
var G__36459 = arguments.length;
switch (G__36459) {
case 2:
return nexus.registry.register_interceptor_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return nexus.registry.register_interceptor_BANG_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(nexus.registry.register_interceptor_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (phase,f){
return nexus.registry.register_interceptor_BANG_.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.createAsIfByAssoc([phase,f]));
}));

(nexus.registry.register_interceptor_BANG_.cljs$core$IFn$_invoke$arity$1 = (function (interceptor){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(nexus.registry._BANG_registry,cljs.core.update,new cljs.core.Keyword("nexus","interceptors","nexus/interceptors",-569564390),cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core.conj,cljs.core.PersistentVector.EMPTY),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([interceptor], 0));
}));

(nexus.registry.register_interceptor_BANG_.cljs$lang$maxFixedArity = 2);

nexus.registry.get_interceptors = (function nexus$registry$get_interceptors(){
return new cljs.core.Keyword("nexus","interceptors","nexus/interceptors",-569564390).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(nexus.registry._BANG_registry));
});
nexus.registry.get_registry = (function nexus$registry$get_registry(){
return cljs.core.deref(nexus.registry._BANG_registry);
});
nexus.registry.on_error = (function nexus$registry$on_error(f){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(nexus.registry._BANG_registry,cljs.core.assoc,new cljs.core.Keyword("nexus","on-error","nexus/on-error",1690599455),f);
});
nexus.registry.dispatch = (function nexus$registry$dispatch(system,dispatch_data,actions){
return nexus.core.dispatch(nexus.registry.get_registry(),system,dispatch_data,actions);
});

//# sourceMappingURL=nexus.registry.js.map
