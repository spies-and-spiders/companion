goog.provide('nexus.core');
nexus.core.conjv = cljs.core.fnil.cljs$core$IFn$_invoke$arity$2(cljs.core.conj,cljs.core.PersistentVector.EMPTY);
nexus.core.action_QMARK_ = (function nexus$core$action_QMARK_(data){
return ((cljs.core.vector_QMARK_(data)) && ((cljs.core.first(data) instanceof cljs.core.Keyword)));
});
nexus.core.actions_QMARK_ = (function nexus$core$actions_QMARK_(data){
return ((cljs.core.sequential_QMARK_(data)) && (cljs.core.every_QMARK_(nexus.core.action_QMARK_,data)));
});
nexus.core.log_error = (function nexus$core$log_error(nexus__$1,ctx,error){
var temp__5823__auto___36178 = new cljs.core.Keyword("nexus","on-error","nexus/on-error",1690599455).cljs$core$IFn$_invoke$arity$1(nexus__$1);
if(cljs.core.truth_(temp__5823__auto___36178)){
var on_error_36179 = temp__5823__auto___36178;
try{var G__35933_36181 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(ctx,new cljs.core.Keyword(null,"stack","stack",-793405930),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"queue","queue",1455835879)], 0));
var G__35934_36182 = error;
(on_error_36179.cljs$core$IFn$_invoke$arity$2 ? on_error_36179.cljs$core$IFn$_invoke$arity$2(G__35933_36181,G__35934_36182) : on_error_36179.call(null,G__35933_36181,G__35934_36182));
}catch (e35932){var __36184 = e35932;
}} else {
}

return error;
});
nexus.core.run_interceptors = (function nexus$core$run_interceptors(ctx,interceptors,p__35940){
var vec__35941 = p__35940;
var before = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35941,(0),null);
var after = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35941,(1),null);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35941,(2),null);
var invoke = (function nexus$core$run_interceptors_$_invoke(f,state,phase,interceptor){
try{var G__35982 = state;
if(cljs.core.ifn_QMARK_(f)){
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(G__35982) : f.call(null,G__35982));
} else {
return G__35982;
}
}catch (e35971){var e = e35971;
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(state,new cljs.core.Keyword(null,"errors","errors",-908790718),nexus.core.conjv,nexus.core.log_error(new cljs.core.Keyword(null,"nexus","nexus",-586700349).cljs$core$IFn$_invoke$arity$1(ctx),ctx,cljs.core.into.cljs$core$IFn$_invoke$arity$2((function (){var G__35973 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"phase","phase",575722892),phase,new cljs.core.Keyword(null,"err","err",-2089457205),e,new cljs.core.Keyword(null,"trace","trace",-1082747415),new cljs.core.Keyword(null,"trace","trace",-1082747415).cljs$core$IFn$_invoke$arity$1(state)], null);
if(cljs.core.truth_(k)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35973,k,cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k));
} else {
return G__35973;
}
})(),cljs.core.select_keys(interceptor,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"id","id",-1388402092)], null)))));
}});
var state = (function (){var G__35999 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(ctx,new cljs.core.Keyword(null,"queue","queue",1455835879),interceptors,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"stack","stack",-793405930),cljs.core.List.EMPTY], 0));
if(cljs.core.truth_(k)){
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__35999,new cljs.core.Keyword(null,"trace","trace",-1082747415),nexus.core.conjv,cljs.core.get.cljs$core$IFn$_invoke$arity$2(ctx,k));
} else {
return G__35999;
}
})();
while(true){
if(cljs.core.truth_(new cljs.core.Keyword(null,"queue","queue",1455835879).cljs$core$IFn$_invoke$arity$1(state))){
var interceptor = cljs.core.first(new cljs.core.Keyword(null,"queue","queue",1455835879).cljs$core$IFn$_invoke$arity$1(state));
var state__$1 = cljs.core.update.cljs$core$IFn$_invoke$arity$4(cljs.core.update.cljs$core$IFn$_invoke$arity$3(state,new cljs.core.Keyword(null,"queue","queue",1455835879),cljs.core.next),new cljs.core.Keyword(null,"stack","stack",-793405930),cljs.core.conj,interceptor);
var G__36204 = invoke(cljs.core.get.cljs$core$IFn$_invoke$arity$2(interceptor,before),state__$1,(function (){var or__5162__auto__ = new cljs.core.Keyword(null,"phase","phase",575722892).cljs$core$IFn$_invoke$arity$1(interceptor);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return before;
}
})(),interceptor);
state = G__36204;
continue;
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"stack","stack",-793405930).cljs$core$IFn$_invoke$arity$1(state))){
var interceptor = cljs.core.first(new cljs.core.Keyword(null,"stack","stack",-793405930).cljs$core$IFn$_invoke$arity$1(state));
var state__$1 = cljs.core.update.cljs$core$IFn$_invoke$arity$3(state,new cljs.core.Keyword(null,"stack","stack",-793405930),cljs.core.next);
var G__36208 = invoke(cljs.core.get.cljs$core$IFn$_invoke$arity$2(interceptor,after),state__$1,after,interceptor);
state = G__36208;
continue;
} else {
return state;

}
}
break;
}
});
nexus.core.interpolate_walk = (function nexus$core$interpolate_walk(placeholders,interpolations,dispatch_data,x){
if(cljs.core.truth_(new cljs.core.Keyword("nexus","skip-interpolation","nexus/skip-interpolation",-1358683772).cljs$core$IFn$_invoke$arity$1(cljs.core.meta(x)))){
return x;
} else {
var x_SINGLEQUOTE_ = ((cljs.core.coll_QMARK_(x))?clojure.walk.walk((function (p1__36003_SHARP_){
return (nexus.core.interpolate_walk.cljs$core$IFn$_invoke$arity$4 ? nexus.core.interpolate_walk.cljs$core$IFn$_invoke$arity$4(placeholders,interpolations,dispatch_data,p1__36003_SHARP_) : nexus.core.interpolate_walk.call(null,placeholders,interpolations,dispatch_data,p1__36003_SHARP_));
}),cljs.core.identity,x):x);
var temp__5821__auto__ = ((cljs.core.vector_QMARK_(x_SINGLEQUOTE_))?cljs.core.get.cljs$core$IFn$_invoke$arity$2(placeholders,cljs.core.first(x_SINGLEQUOTE_)):null);
if(cljs.core.truth_(temp__5821__auto__)){
var f = temp__5821__auto__;
var resolution = cljs.core.apply.cljs$core$IFn$_invoke$arity$3(f,dispatch_data,cljs.core.next(x_SINGLEQUOTE_));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(interpolations,cljs.core.conj,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"placeholder","placeholder",-104873083),x_SINGLEQUOTE_,new cljs.core.Keyword(null,"resolution","resolution",-756075601),resolution], null));

return resolution;
} else {
return x_SINGLEQUOTE_;
}
}
});
nexus.core.interpolate_1 = (function nexus$core$interpolate_1(p__36019,dispatch_data,action){
var map__36021 = p__36019;
var map__36021__$1 = cljs.core.__destructure_map(map__36021);
var placeholders = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36021__$1,new cljs.core.Keyword("nexus","placeholders","nexus/placeholders",1656029064));
var interpolations = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
var interpolated = nexus.core.interpolate_walk(placeholders,interpolations,dispatch_data,action);
var G__36026 = interpolated;
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(interpolated,action)){
return cljs.core.with_meta(G__36026,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("nexus","action","nexus/action",-1495019145),action,new cljs.core.Keyword("nexus","interpolations","nexus/interpolations",-2139726909),cljs.core.deref(interpolations)], null));
} else {
return G__36026;
}
});
/**
 * Walks `actions`, and replaces any forms matching a registered placeholder with
 *   the value of calling the corresponding function with `dispatch-data`. Returns
 *   interpolated `actions`.
 */
nexus.core.interpolate = (function nexus$core$interpolate(nexus__$1,dispatch_data,actions){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p1__36027_SHARP_){
return nexus.core.interpolate_1(nexus__$1,dispatch_data,p1__36027_SHARP_);
}),actions);
});
nexus.core.get_state = (function nexus$core$get_state(nexus__$1,ctx){
var or__5162__auto__ = (function (){var temp__5823__auto__ = new cljs.core.Keyword("nexus","system+dispatch-data->state","nexus/system+dispatch-data->state",-365410147).cljs$core$IFn$_invoke$arity$1(nexus__$1);
if(cljs.core.truth_(temp__5823__auto__)){
var f = temp__5823__auto__;
var G__36041 = new cljs.core.Keyword(null,"system","system",-29381724).cljs$core$IFn$_invoke$arity$1(ctx);
var G__36042 = new cljs.core.Keyword(null,"dispatch-data","dispatch-data",658842324).cljs$core$IFn$_invoke$arity$1(ctx);
return (f.cljs$core$IFn$_invoke$arity$2 ? f.cljs$core$IFn$_invoke$arity$2(G__36041,G__36042) : f.call(null,G__36041,G__36042));
} else {
return null;
}
})();
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
var temp__5823__auto__ = new cljs.core.Keyword("nexus","system->state","nexus/system->state",-142405750).cljs$core$IFn$_invoke$arity$1(nexus__$1);
if(cljs.core.truth_(temp__5823__auto__)){
var f = temp__5823__auto__;
var G__36044 = new cljs.core.Keyword(null,"system","system",-29381724).cljs$core$IFn$_invoke$arity$1(ctx);
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(G__36044) : f.call(null,G__36044));
} else {
return null;
}
}
});
nexus.core.reset_ctx_nesting = (function nexus$core$reset_ctx_nesting(p__36054,ctx){
var map__36055 = p__36054;
var map__36055__$1 = cljs.core.__destructure_map(map__36055);
var queue = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36055__$1,new cljs.core.Keyword(null,"queue","queue",1455835879));
var stack = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36055__$1,new cljs.core.Keyword(null,"stack","stack",-793405930));
var trace = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36055__$1,new cljs.core.Keyword(null,"trace","trace",-1082747415));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(ctx,new cljs.core.Keyword(null,"queue","queue",1455835879),queue,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"stack","stack",-793405930),stack,new cljs.core.Keyword(null,"trace","trace",-1082747415),trace], 0));
});
nexus.core.dispatch_action = (function nexus$core$dispatch_action(nexus__$1,dispatch_BANG_,ctx,action_f,action){
return nexus.core.run_interceptors(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ctx,new cljs.core.Keyword(null,"action","action",-811238024),action),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("nexus","interceptors","nexus/interceptors",-569564390).cljs$core$IFn$_invoke$arity$1(nexus__$1),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"expand-action","expand-action",-565829596),new cljs.core.Keyword(null,"before-action","before-action",1841179242),(function (ctx_STAR_){
var actions = cljs.core.apply.cljs$core$IFn$_invoke$arity$3(action_f,new cljs.core.Keyword(null,"state","state",-1988618099).cljs$core$IFn$_invoke$arity$1(ctx_STAR_),cljs.core.next(new cljs.core.Keyword(null,"action","action",-811238024).cljs$core$IFn$_invoke$arity$1(ctx_STAR_)));
if(cljs.core.empty_QMARK_(actions)){
return nexus.core.reset_ctx_nesting(ctx,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ctx_STAR_,new cljs.core.Keyword(null,"actions","actions",-812656882),cljs.core.PersistentVector.EMPTY));
} else {
if((!(nexus.core.actions_QMARK_(actions)))){
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(ctx_STAR_,new cljs.core.Keyword(null,"errors","errors",-908790718),nexus.core.conjv,nexus.core.log_error(nexus__$1,ctx,new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"action","action",-811238024),action,new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"expand-action","expand-action",-565829596),new cljs.core.Keyword(null,"trace","trace",-1082747415),new cljs.core.Keyword(null,"trace","trace",-1082747415).cljs$core$IFn$_invoke$arity$1(ctx_STAR_),new cljs.core.Keyword(null,"err","err",-2089457205),cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.first(action))+" should expand to a collection of actions"),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"res","res",-1395007879),actions,new cljs.core.Keyword(null,"action","action",-811238024),action], null))], null)));
} else {
return nexus.core.reset_ctx_nesting(ctx,(function (){var G__36077 = nexus__$1;
var G__36078 = dispatch_BANG_;
var G__36079 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ctx_STAR_,new cljs.core.Keyword(null,"actions","actions",-812656882),actions);
return (nexus.core.dispatch_actions.cljs$core$IFn$_invoke$arity$3 ? nexus.core.dispatch_actions.cljs$core$IFn$_invoke$arity$3(G__36077,G__36078,G__36079) : nexus.core.dispatch_actions.call(null,G__36077,G__36078,G__36079));
})());

}
}
})], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"before-action","before-action",1841179242),new cljs.core.Keyword(null,"after-action","after-action",-22240574),new cljs.core.Keyword(null,"action","action",-811238024)], null));
});
nexus.core.__GT_execute_ctx = (function nexus$core$__GT_execute_ctx(ctx){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(ctx,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),(function (dispatch){
return (function() { 
var G__36290__delegate = function (actions,p__36084){
var vec__36085 = p__36084;
var dispatch_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36085,(0),null);
return cljs.core.select_keys((function (){var G__36088 = actions;
var G__36089 = dispatch_data;
var G__36090 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(ctx,new cljs.core.Keyword(null,"action","action",-811238024),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"actions","actions",-812656882),new cljs.core.Keyword(null,"effect","effect",347343289),new cljs.core.Keyword(null,"stack","stack",-793405930),new cljs.core.Keyword(null,"queue","queue",1455835879)], 0));
return (dispatch.cljs$core$IFn$_invoke$arity$3 ? dispatch.cljs$core$IFn$_invoke$arity$3(G__36088,G__36089,G__36090) : dispatch.call(null,G__36088,G__36089,G__36090));
})(),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"results","results",-1134170113),new cljs.core.Keyword(null,"errors","errors",-908790718)], null));
};
var G__36290 = function (actions,var_args){
var p__36084 = null;
if (arguments.length > 1) {
var G__36294__i = 0, G__36294__a = new Array(arguments.length -  1);
while (G__36294__i < G__36294__a.length) {G__36294__a[G__36294__i] = arguments[G__36294__i + 1]; ++G__36294__i;}
  p__36084 = new cljs.core.IndexedSeq(G__36294__a,0,null);
} 
return G__36290__delegate.call(this,actions,p__36084);};
G__36290.cljs$lang$maxFixedArity = 1;
G__36290.cljs$lang$applyTo = (function (arglist__36295){
var actions = cljs.core.first(arglist__36295);
var p__36084 = cljs.core.rest(arglist__36295);
return G__36290__delegate(actions,p__36084);
});
G__36290.cljs$core$IFn$_invoke$arity$variadic = G__36290__delegate;
return G__36290;
})()
;
}));
});
nexus.core.execute_effect = (function nexus$core$execute_effect(nexus__$1,dispatch_BANG_,ctx,effect_f,effect){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(nexus.core.run_interceptors(cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(ctx,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),dispatch_BANG_,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"effect","effect",347343289),effect], 0)),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("nexus","interceptors","nexus/interceptors",-569564390).cljs$core$IFn$_invoke$arity$1(nexus__$1),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"execute-effect","execute-effect",440233016),new cljs.core.Keyword(null,"before-effect","before-effect",-1455186134),(function (p__36097){
var map__36098 = p__36097;
var map__36098__$1 = cljs.core.__destructure_map(map__36098);
var ctx_STAR_ = map__36098__$1;
var system = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36098__$1,new cljs.core.Keyword(null,"system","system",-29381724));
var effect__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36098__$1,new cljs.core.Keyword(null,"effect","effect",347343289));
var result = cljs.core.apply.cljs$core$IFn$_invoke$arity$4(effect_f,nexus.core.__GT_execute_ctx(ctx_STAR_),system,cljs.core.next(effect__$1));
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(ctx_STAR_,new cljs.core.Keyword(null,"res","res",-1395007879),result,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"state","state",-1988618099),nexus.core.get_state(nexus__$1,ctx_STAR_)], 0)),new cljs.core.Keyword(null,"results","results",-1134170113),nexus.core.conjv,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"effect","effect",347343289),effect__$1,new cljs.core.Keyword(null,"res","res",-1395007879),result], null));
})], null)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"before-effect","before-effect",-1455186134),new cljs.core.Keyword(null,"after-effect","after-effect",933600904),new cljs.core.Keyword(null,"effect","effect",347343289)], null)),new cljs.core.Keyword(null,"effect","effect",347343289),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"res","res",-1395007879)], 0));
});
nexus.core.dispatch_actions = (function nexus$core$dispatch_actions(nexus__$1,dispatch_BANG_,p__36102){
var map__36103 = p__36102;
var map__36103__$1 = cljs.core.__destructure_map(map__36103);
var ctx = map__36103__$1;
var queue = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36103__$1,new cljs.core.Keyword(null,"queue","queue",1455835879));
var stack = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36103__$1,new cljs.core.Keyword(null,"stack","stack",-793405930));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ctx__$1,action){
var vec__36104 = nexus.core.interpolate_1(nexus__$1,new cljs.core.Keyword(null,"dispatch-data","dispatch-data",658842324).cljs$core$IFn$_invoke$arity$1(ctx__$1),action);
var action_k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36104,(0),null);
var action__$1 = vec__36104;
var or__5162__auto__ = (function (){var temp__5823__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(nexus__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("nexus","actions","nexus/actions",-707386605),action_k], null));
if(cljs.core.truth_(temp__5823__auto__)){
var action_f = temp__5823__auto__;
return nexus.core.dispatch_action(nexus__$1,dispatch_BANG_,ctx__$1,action_f,action__$1);
} else {
return null;
}
})();
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
var or__5162__auto____$1 = (function (){var temp__5823__auto__ = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(nexus__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("nexus","effects","nexus/effects",-865511691),action_k], null));
if(cljs.core.truth_(temp__5823__auto__)){
var effect_f = temp__5823__auto__;
return nexus.core.execute_effect(nexus__$1,dispatch_BANG_,ctx__$1,effect_f,action__$1);
} else {
return null;
}
})();
if(cljs.core.truth_(or__5162__auto____$1)){
return or__5162__auto____$1;
} else {
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(ctx__$1,new cljs.core.Keyword(null,"errors","errors",-908790718),nexus.core.conjv,nexus.core.log_error(nexus__$1,ctx__$1,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"execute-effect","execute-effect",440233016),new cljs.core.Keyword(null,"effect-k","effect-k",137284057),action_k,new cljs.core.Keyword(null,"err","err",-2089457205),cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("No such effect",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"available-effects","available-effects",2081495292),cljs.core.keys(new cljs.core.Keyword("nexus","effects","nexus/effects",-865511691).cljs$core$IFn$_invoke$arity$1(nexus__$1))], null))], null)));
}
}
}),cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(ctx,new cljs.core.Keyword(null,"state","state",-1988618099),nexus.core.get_state(nexus__$1,ctx)),new cljs.core.Keyword(null,"actions","actions",-812656882).cljs$core$IFn$_invoke$arity$1(ctx)),new cljs.core.Keyword(null,"queue","queue",1455835879),queue,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"stack","stack",-793405930),stack], 0));
});
nexus.core.dispatch = (function nexus$core$dispatch(nexus__$1,system,dispatch_data,actions){
if(cljs.core.truth_(new cljs.core.Keyword("nexus","actions","nexus/actions",-707386605).cljs$core$IFn$_invoke$arity$1(nexus__$1))){
if(((cljs.core.ifn_QMARK_(new cljs.core.Keyword("nexus","system->state","nexus/system->state",-142405750).cljs$core$IFn$_invoke$arity$1(nexus__$1))) || (cljs.core.ifn_QMARK_(new cljs.core.Keyword("nexus","system+dispatch-data->state","nexus/system+dispatch-data->state",-365410147).cljs$core$IFn$_invoke$arity$1(nexus__$1))))){
} else {
throw (new Error((""+"Assert failed: "+"Either :nexus/system+dispatch-data->state or :nexus/system->state must be a function"+"\n"+"(or (ifn? (:nexus/system->state nexus)) (ifn? (:nexus/system+dispatch-data->state nexus)))")));
}
} else {
}

var nexus__$2 = cljs.core.update.cljs$core$IFn$_invoke$arity$3(nexus__$1,new cljs.core.Keyword("nexus","interceptors","nexus/interceptors",-569564390),cljs.core.vec);
var dispatch_BANG_ = (function() { 
var nexus$core$dispatch_$_dispatch_BANG___delegate = function (actions__$1,p__36119){
var vec__36127 = p__36119;
var disp_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36127,(0),null);
var parent_ctx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36127,(1),null);
var handler = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"action-dispatch","action-dispatch",-1732769405),new cljs.core.Keyword(null,"before-dispatch","before-dispatch",395669799),cljs.core.partial.cljs$core$IFn$_invoke$arity$3(nexus.core.dispatch_actions,nexus__$2,nexus$core$dispatch_$_dispatch_BANG_)], null);
return nexus.core.run_interceptors(cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(parent_ctx,new cljs.core.Keyword(null,"nexus","nexus",-586700349),nexus__$2,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"system","system",-29381724),system,new cljs.core.Keyword(null,"dispatch-data","dispatch-data",658842324),cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([dispatch_data,disp_data], 0)),new cljs.core.Keyword(null,"actions","actions",-812656882),actions__$1], 0)),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("nexus","interceptors","nexus/interceptors",-569564390).cljs$core$IFn$_invoke$arity$1(nexus__$2),handler),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"before-dispatch","before-dispatch",395669799),new cljs.core.Keyword(null,"after-dispatch","after-dispatch",183497585)], null));
};
var nexus$core$dispatch_$_dispatch_BANG_ = function (actions__$1,var_args){
var p__36119 = null;
if (arguments.length > 1) {
var G__36345__i = 0, G__36345__a = new Array(arguments.length -  1);
while (G__36345__i < G__36345__a.length) {G__36345__a[G__36345__i] = arguments[G__36345__i + 1]; ++G__36345__i;}
  p__36119 = new cljs.core.IndexedSeq(G__36345__a,0,null);
} 
return nexus$core$dispatch_$_dispatch_BANG___delegate.call(this,actions__$1,p__36119);};
nexus$core$dispatch_$_dispatch_BANG_.cljs$lang$maxFixedArity = 1;
nexus$core$dispatch_$_dispatch_BANG_.cljs$lang$applyTo = (function (arglist__36346){
var actions__$1 = cljs.core.first(arglist__36346);
var p__36119 = cljs.core.rest(arglist__36346);
return nexus$core$dispatch_$_dispatch_BANG___delegate(actions__$1,p__36119);
});
nexus$core$dispatch_$_dispatch_BANG_.cljs$core$IFn$_invoke$arity$variadic = nexus$core$dispatch_$_dispatch_BANG___delegate;
return nexus$core$dispatch_$_dispatch_BANG_;
})()
;
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(dispatch_BANG_(actions),new cljs.core.Keyword(null,"nexus","nexus",-586700349),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"system","system",-29381724),new cljs.core.Keyword(null,"state","state",-1988618099),new cljs.core.Keyword(null,"trace","trace",-1082747415),new cljs.core.Keyword(null,"queue","queue",1455835879),new cljs.core.Keyword(null,"stack","stack",-793405930),new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),new cljs.core.Keyword(null,"dispatch-data","dispatch-data",658842324),new cljs.core.Keyword(null,"action","action",-811238024),new cljs.core.Keyword(null,"actions","actions",-812656882)], 0));
});

//# sourceMappingURL=nexus.core.js.map
