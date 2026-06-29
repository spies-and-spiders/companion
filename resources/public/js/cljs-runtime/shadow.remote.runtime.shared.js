goog.provide('shadow.remote.runtime.shared');
shadow.remote.runtime.shared.init_state = (function shadow$remote$runtime$shared$init_state(client_info){
return new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"extensions","extensions",-1103629196),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"ops","ops",1237330063),cljs.core.PersistentArrayMap.EMPTY,new cljs.core.Keyword(null,"client-info","client-info",1958982504),client_info,new cljs.core.Keyword(null,"call-id-seq","call-id-seq",-1679248218),(0),new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),cljs.core.PersistentArrayMap.EMPTY], null);
});
shadow.remote.runtime.shared.now = (function shadow$remote$runtime$shared$now(){
return Date.now();
});
shadow.remote.runtime.shared.get_client_id = (function shadow$remote$runtime$shared$get_client_id(p__29964){
var map__29965 = p__29964;
var map__29965__$1 = cljs.core.__destructure_map(map__29965);
var runtime = map__29965__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29965__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var or__5162__auto__ = new cljs.core.Keyword(null,"client-id","client-id",-464622140).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref));
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("runtime has no assigned runtime-id",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"runtime","runtime",-1331573996),runtime], null));
}
});
shadow.remote.runtime.shared.relay_msg = (function shadow$remote$runtime$shared$relay_msg(runtime,msg){
var self_id_30178 = shadow.remote.runtime.shared.get_client_id(runtime);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"to","to",192099007).cljs$core$IFn$_invoke$arity$1(msg),self_id_30178)){
shadow.remote.runtime.api.relay_msg(runtime,msg);
} else {
Promise.resolve((1)).then((function (){
var G__29968 = runtime;
var G__29969 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"from","from",1815293044),self_id_30178);
return (shadow.remote.runtime.shared.process.cljs$core$IFn$_invoke$arity$2 ? shadow.remote.runtime.shared.process.cljs$core$IFn$_invoke$arity$2(G__29968,G__29969) : shadow.remote.runtime.shared.process.call(null,G__29968,G__29969));
}));
}

return msg;
});
shadow.remote.runtime.shared.reply = (function shadow$remote$runtime$shared$reply(runtime,p__29972,res){
var map__29973 = p__29972;
var map__29973__$1 = cljs.core.__destructure_map(map__29973);
var call_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29973__$1,new cljs.core.Keyword(null,"call-id","call-id",1043012968));
var from = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29973__$1,new cljs.core.Keyword(null,"from","from",1815293044));
var res__$1 = (function (){var G__29976 = res;
var G__29976__$1 = (cljs.core.truth_(call_id)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__29976,new cljs.core.Keyword(null,"call-id","call-id",1043012968),call_id):G__29976);
if(cljs.core.truth_(from)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__29976__$1,new cljs.core.Keyword(null,"to","to",192099007),from);
} else {
return G__29976__$1;
}
})();
return shadow.remote.runtime.api.relay_msg(runtime,res__$1);
});
shadow.remote.runtime.shared.call = (function shadow$remote$runtime$shared$call(var_args){
var G__29983 = arguments.length;
switch (G__29983) {
case 3:
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$3 = (function (runtime,msg,handlers){
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$4(runtime,msg,handlers,(0));
}));

(shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$4 = (function (p__29987,msg,handlers,timeout_after_ms){
var map__29989 = p__29987;
var map__29989__$1 = cljs.core.__destructure_map(map__29989);
var runtime = map__29989__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29989__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
if(cljs.core.map_QMARK_(msg)){
} else {
throw (new Error("Assert failed: (map? msg)"));
}

if(cljs.core.map_QMARK_(handlers)){
} else {
throw (new Error("Assert failed: (map? handlers)"));
}

if(cljs.core.nat_int_QMARK_(timeout_after_ms)){
} else {
throw (new Error("Assert failed: (nat-int? timeout-after-ms)"));
}

var call_id = new cljs.core.Keyword(null,"call-id-seq","call-id-seq",-1679248218).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(state_ref,cljs.core.update,new cljs.core.Keyword(null,"call-id-seq","call-id-seq",-1679248218),cljs.core.inc);

cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(state_ref,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),call_id], null),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"handlers","handlers",79528781),handlers,new cljs.core.Keyword(null,"called-at","called-at",607081160),shadow.remote.runtime.shared.now(),new cljs.core.Keyword(null,"msg","msg",-1386103444),msg,new cljs.core.Keyword(null,"timeout","timeout",-318625318),timeout_after_ms], null));

return shadow.remote.runtime.api.relay_msg(runtime,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"call-id","call-id",1043012968),call_id));
}));

(shadow.remote.runtime.shared.call.cljs$lang$maxFixedArity = 4);

shadow.remote.runtime.shared.trigger_BANG_ = (function shadow$remote$runtime$shared$trigger_BANG_(var_args){
var args__5903__auto__ = [];
var len__5897__auto___30209 = arguments.length;
var i__5898__auto___30210 = (0);
while(true){
if((i__5898__auto___30210 < len__5897__auto___30209)){
args__5903__auto__.push((arguments[i__5898__auto___30210]));

var G__30213 = (i__5898__auto___30210 + (1));
i__5898__auto___30210 = G__30213;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((2) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((2)),(0),null)):null);
return shadow.remote.runtime.shared.trigger_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__5904__auto__);
});

(shadow.remote.runtime.shared.trigger_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (p__30001,ev,args){
var map__30005 = p__30001;
var map__30005__$1 = cljs.core.__destructure_map(map__30005);
var runtime = map__30005__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30005__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var seq__30007 = cljs.core.seq(cljs.core.vals(new cljs.core.Keyword(null,"extensions","extensions",-1103629196).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref))));
var chunk__30012 = null;
var count__30013 = (0);
var i__30014 = (0);
while(true){
if((i__30014 < count__30013)){
var ext = chunk__30012.cljs$core$IIndexed$_nth$arity$2(null,i__30014);
var ev_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(ext,ev);
if(cljs.core.truth_(ev_fn)){
cljs.core.apply.cljs$core$IFn$_invoke$arity$2(ev_fn,args);


var G__30229 = seq__30007;
var G__30230 = chunk__30012;
var G__30231 = count__30013;
var G__30232 = (i__30014 + (1));
seq__30007 = G__30229;
chunk__30012 = G__30230;
count__30013 = G__30231;
i__30014 = G__30232;
continue;
} else {
var G__30233 = seq__30007;
var G__30234 = chunk__30012;
var G__30235 = count__30013;
var G__30236 = (i__30014 + (1));
seq__30007 = G__30233;
chunk__30012 = G__30234;
count__30013 = G__30235;
i__30014 = G__30236;
continue;
}
} else {
var temp__5823__auto__ = cljs.core.seq(seq__30007);
if(temp__5823__auto__){
var seq__30007__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__30007__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__30007__$1);
var G__30239 = cljs.core.chunk_rest(seq__30007__$1);
var G__30240 = c__5694__auto__;
var G__30241 = cljs.core.count(c__5694__auto__);
var G__30242 = (0);
seq__30007 = G__30239;
chunk__30012 = G__30240;
count__30013 = G__30241;
i__30014 = G__30242;
continue;
} else {
var ext = cljs.core.first(seq__30007__$1);
var ev_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(ext,ev);
if(cljs.core.truth_(ev_fn)){
cljs.core.apply.cljs$core$IFn$_invoke$arity$2(ev_fn,args);


var G__30247 = cljs.core.next(seq__30007__$1);
var G__30248 = null;
var G__30249 = (0);
var G__30250 = (0);
seq__30007 = G__30247;
chunk__30012 = G__30248;
count__30013 = G__30249;
i__30014 = G__30250;
continue;
} else {
var G__30251 = cljs.core.next(seq__30007__$1);
var G__30252 = null;
var G__30253 = (0);
var G__30254 = (0);
seq__30007 = G__30251;
chunk__30012 = G__30252;
count__30013 = G__30253;
i__30014 = G__30254;
continue;
}
}
} else {
return null;
}
}
break;
}
}));

(shadow.remote.runtime.shared.trigger_BANG_.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(shadow.remote.runtime.shared.trigger_BANG_.cljs$lang$applyTo = (function (seq29994){
var G__29995 = cljs.core.first(seq29994);
var seq29994__$1 = cljs.core.next(seq29994);
var G__29996 = cljs.core.first(seq29994__$1);
var seq29994__$2 = cljs.core.next(seq29994__$1);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__29995,G__29996,seq29994__$2);
}));

shadow.remote.runtime.shared.welcome = (function shadow$remote$runtime$shared$welcome(p__30052,p__30053){
var map__30054 = p__30052;
var map__30054__$1 = cljs.core.__destructure_map(map__30054);
var runtime = map__30054__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30054__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var map__30055 = p__30053;
var map__30055__$1 = cljs.core.__destructure_map(map__30055);
var msg = map__30055__$1;
var client_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30055__$1,new cljs.core.Keyword(null,"client-id","client-id",-464622140));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(state_ref,cljs.core.assoc,new cljs.core.Keyword(null,"client-id","client-id",-464622140),client_id,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"welcome","welcome",-578152123),true], 0));

var map__30056 = cljs.core.deref(state_ref);
var map__30056__$1 = cljs.core.__destructure_map(map__30056);
var client_info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30056__$1,new cljs.core.Keyword(null,"client-info","client-info",1958982504));
var extensions = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30056__$1,new cljs.core.Keyword(null,"extensions","extensions",-1103629196));
shadow.remote.runtime.shared.relay_msg(runtime,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"hello","hello",-245025397),new cljs.core.Keyword(null,"client-info","client-info",1958982504),client_info], null));

return shadow.remote.runtime.shared.trigger_BANG_(runtime,new cljs.core.Keyword(null,"on-welcome","on-welcome",1895317125));
});
shadow.remote.runtime.shared.ping = (function shadow$remote$runtime$shared$ping(runtime,msg){
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"pong","pong",-172484958)], null));
});
shadow.remote.runtime.shared.request_supported_ops = (function shadow$remote$runtime$shared$request_supported_ops(p__30062,msg){
var map__30064 = p__30062;
var map__30064__$1 = cljs.core.__destructure_map(map__30064);
var runtime = map__30064__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30064__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"supported-ops","supported-ops",337914702),new cljs.core.Keyword(null,"ops","ops",1237330063),cljs.core.disj.cljs$core$IFn$_invoke$arity$variadic(cljs.core.set(cljs.core.keys(new cljs.core.Keyword(null,"ops","ops",1237330063).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref)))),new cljs.core.Keyword(null,"welcome","welcome",-578152123),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"unknown-relay-op","unknown-relay-op",170832753),new cljs.core.Keyword(null,"unknown-op","unknown-op",1900385996),new cljs.core.Keyword(null,"request-supported-ops","request-supported-ops",-1034994502),new cljs.core.Keyword(null,"tool-disconnect","tool-disconnect",189103996)], 0))], null));
});
shadow.remote.runtime.shared.unknown_relay_op = (function shadow$remote$runtime$shared$unknown_relay_op(msg){
return console.warn("unknown-relay-op",msg);
});
shadow.remote.runtime.shared.unknown_op = (function shadow$remote$runtime$shared$unknown_op(msg){
return console.warn("unknown-op",msg);
});
shadow.remote.runtime.shared.add_extension_STAR_ = (function shadow$remote$runtime$shared$add_extension_STAR_(p__30072,key,p__30073){
var map__30074 = p__30072;
var map__30074__$1 = cljs.core.__destructure_map(map__30074);
var state = map__30074__$1;
var extensions = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30074__$1,new cljs.core.Keyword(null,"extensions","extensions",-1103629196));
var map__30075 = p__30073;
var map__30075__$1 = cljs.core.__destructure_map(map__30075);
var spec = map__30075__$1;
var ops = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30075__$1,new cljs.core.Keyword(null,"ops","ops",1237330063));
var transit_write_handlers = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30075__$1,new cljs.core.Keyword(null,"transit-write-handlers","transit-write-handlers",1886308716));
if(cljs.core.contains_QMARK_(extensions,key)){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("extension already registered",new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"spec","spec",347520401),spec], null));
} else {
}

return cljs.core.reduce_kv((function (state__$1,op_kw,op_handler){
if(cljs.core.truth_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063),op_kw], null)))){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("op already registered",new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"key","key",-1516042587),key,new cljs.core.Keyword(null,"op","op",-1882987955),op_kw], null));
} else {
}

return cljs.core.assoc_in(state__$1,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063),op_kw], null),op_handler);
}),cljs.core.assoc_in(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"extensions","extensions",-1103629196),key], null),spec),ops);
});
shadow.remote.runtime.shared.add_extension = (function shadow$remote$runtime$shared$add_extension(p__30083,key,spec){
var map__30084 = p__30083;
var map__30084__$1 = cljs.core.__destructure_map(map__30084);
var runtime = map__30084__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30084__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(state_ref,shadow.remote.runtime.shared.add_extension_STAR_,key,spec);

var temp__5827__auto___30295 = new cljs.core.Keyword(null,"on-welcome","on-welcome",1895317125).cljs$core$IFn$_invoke$arity$1(spec);
if((temp__5827__auto___30295 == null)){
} else {
var on_welcome_30296 = temp__5827__auto___30295;
if(cljs.core.truth_(new cljs.core.Keyword(null,"welcome","welcome",-578152123).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref)))){
(on_welcome_30296.cljs$core$IFn$_invoke$arity$0 ? on_welcome_30296.cljs$core$IFn$_invoke$arity$0() : on_welcome_30296.call(null));
} else {
}
}

return runtime;
});
shadow.remote.runtime.shared.add_defaults = (function shadow$remote$runtime$shared$add_defaults(runtime){
return shadow.remote.runtime.shared.add_extension(runtime,new cljs.core.Keyword("shadow.remote.runtime.shared","defaults","shadow.remote.runtime.shared/defaults",-1821257543),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ops","ops",1237330063),new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"welcome","welcome",-578152123),(function (p1__30095_SHARP_){
return shadow.remote.runtime.shared.welcome(runtime,p1__30095_SHARP_);
}),new cljs.core.Keyword(null,"unknown-relay-op","unknown-relay-op",170832753),(function (p1__30096_SHARP_){
return shadow.remote.runtime.shared.unknown_relay_op(p1__30096_SHARP_);
}),new cljs.core.Keyword(null,"unknown-op","unknown-op",1900385996),(function (p1__30097_SHARP_){
return shadow.remote.runtime.shared.unknown_op(p1__30097_SHARP_);
}),new cljs.core.Keyword(null,"ping","ping",-1670114784),(function (p1__30098_SHARP_){
return shadow.remote.runtime.shared.ping(runtime,p1__30098_SHARP_);
}),new cljs.core.Keyword(null,"request-supported-ops","request-supported-ops",-1034994502),(function (p1__30099_SHARP_){
return shadow.remote.runtime.shared.request_supported_ops(runtime,p1__30099_SHARP_);
})], null)], null));
});
shadow.remote.runtime.shared.del_extension_STAR_ = (function shadow$remote$runtime$shared$del_extension_STAR_(state,key){
var ext = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"extensions","extensions",-1103629196),key], null));
if(cljs.core.not(ext)){
return state;
} else {
return cljs.core.reduce_kv((function (state__$1,op_kw,op_handler){
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(state__$1,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063)], null),cljs.core.dissoc,op_kw);
}),cljs.core.update.cljs$core$IFn$_invoke$arity$4(state,new cljs.core.Keyword(null,"extensions","extensions",-1103629196),cljs.core.dissoc,key),new cljs.core.Keyword(null,"ops","ops",1237330063).cljs$core$IFn$_invoke$arity$1(ext));
}
});
shadow.remote.runtime.shared.del_extension = (function shadow$remote$runtime$shared$del_extension(p__30101,key){
var map__30102 = p__30101;
var map__30102__$1 = cljs.core.__destructure_map(map__30102);
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30102__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(state_ref,shadow.remote.runtime.shared.del_extension_STAR_,key);
});
shadow.remote.runtime.shared.unhandled_call_result = (function shadow$remote$runtime$shared$unhandled_call_result(call_config,msg){
return console.warn("unhandled call result",msg,call_config);
});
shadow.remote.runtime.shared.unhandled_client_not_found = (function shadow$remote$runtime$shared$unhandled_client_not_found(p__30103,msg){
var map__30104 = p__30103;
var map__30104__$1 = cljs.core.__destructure_map(map__30104);
var runtime = map__30104__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30104__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
return shadow.remote.runtime.shared.trigger_BANG_.cljs$core$IFn$_invoke$arity$variadic(runtime,new cljs.core.Keyword(null,"on-client-not-found","on-client-not-found",-642452849),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([msg], 0));
});
shadow.remote.runtime.shared.reply_unknown_op = (function shadow$remote$runtime$shared$reply_unknown_op(runtime,msg){
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"unknown-op","unknown-op",1900385996),new cljs.core.Keyword(null,"msg","msg",-1386103444),msg], null));
});
shadow.remote.runtime.shared.process = (function shadow$remote$runtime$shared$process(p__30111,p__30112){
var map__30113 = p__30111;
var map__30113__$1 = cljs.core.__destructure_map(map__30113);
var runtime = map__30113__$1;
var state_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30113__$1,new cljs.core.Keyword(null,"state-ref","state-ref",2127874952));
var map__30114 = p__30112;
var map__30114__$1 = cljs.core.__destructure_map(map__30114);
var msg = map__30114__$1;
var op = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30114__$1,new cljs.core.Keyword(null,"op","op",-1882987955));
var call_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30114__$1,new cljs.core.Keyword(null,"call-id","call-id",1043012968));
var state = cljs.core.deref(state_ref);
var op_handler = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ops","ops",1237330063),op], null));
if(cljs.core.truth_(call_id)){
var cfg = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(state,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),call_id], null));
var call_handler = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(cfg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"handlers","handlers",79528781),op], null));
if(cljs.core.truth_(call_handler)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(state_ref,cljs.core.update,new cljs.core.Keyword(null,"call-handlers","call-handlers",386605551),cljs.core.dissoc,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([call_id], 0));

return (call_handler.cljs$core$IFn$_invoke$arity$1 ? call_handler.cljs$core$IFn$_invoke$arity$1(msg) : call_handler.call(null,msg));
} else {
if(cljs.core.truth_(op_handler)){
return (op_handler.cljs$core$IFn$_invoke$arity$1 ? op_handler.cljs$core$IFn$_invoke$arity$1(msg) : op_handler.call(null,msg));
} else {
return shadow.remote.runtime.shared.unhandled_call_result(cfg,msg);

}
}
} else {
if(cljs.core.truth_(op_handler)){
return (op_handler.cljs$core$IFn$_invoke$arity$1 ? op_handler.cljs$core$IFn$_invoke$arity$1(msg) : op_handler.call(null,msg));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-not-found","client-not-found",-1754042614),op)){
return shadow.remote.runtime.shared.unhandled_client_not_found(runtime,msg);
} else {
return shadow.remote.runtime.shared.reply_unknown_op(runtime,msg);

}
}
}
});
shadow.remote.runtime.shared.run_on_idle = (function shadow$remote$runtime$shared$run_on_idle(state_ref){
var seq__30133 = cljs.core.seq(cljs.core.vals(new cljs.core.Keyword(null,"extensions","extensions",-1103629196).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(state_ref))));
var chunk__30135 = null;
var count__30136 = (0);
var i__30137 = (0);
while(true){
if((i__30137 < count__30136)){
var map__30152 = chunk__30135.cljs$core$IIndexed$_nth$arity$2(null,i__30137);
var map__30152__$1 = cljs.core.__destructure_map(map__30152);
var on_idle = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30152__$1,new cljs.core.Keyword(null,"on-idle","on-idle",2044706602));
if(cljs.core.truth_(on_idle)){
(on_idle.cljs$core$IFn$_invoke$arity$0 ? on_idle.cljs$core$IFn$_invoke$arity$0() : on_idle.call(null));


var G__30370 = seq__30133;
var G__30371 = chunk__30135;
var G__30372 = count__30136;
var G__30373 = (i__30137 + (1));
seq__30133 = G__30370;
chunk__30135 = G__30371;
count__30136 = G__30372;
i__30137 = G__30373;
continue;
} else {
var G__30374 = seq__30133;
var G__30375 = chunk__30135;
var G__30376 = count__30136;
var G__30377 = (i__30137 + (1));
seq__30133 = G__30374;
chunk__30135 = G__30375;
count__30136 = G__30376;
i__30137 = G__30377;
continue;
}
} else {
var temp__5823__auto__ = cljs.core.seq(seq__30133);
if(temp__5823__auto__){
var seq__30133__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__30133__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__30133__$1);
var G__30386 = cljs.core.chunk_rest(seq__30133__$1);
var G__30387 = c__5694__auto__;
var G__30388 = cljs.core.count(c__5694__auto__);
var G__30389 = (0);
seq__30133 = G__30386;
chunk__30135 = G__30387;
count__30136 = G__30388;
i__30137 = G__30389;
continue;
} else {
var map__30153 = cljs.core.first(seq__30133__$1);
var map__30153__$1 = cljs.core.__destructure_map(map__30153);
var on_idle = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__30153__$1,new cljs.core.Keyword(null,"on-idle","on-idle",2044706602));
if(cljs.core.truth_(on_idle)){
(on_idle.cljs$core$IFn$_invoke$arity$0 ? on_idle.cljs$core$IFn$_invoke$arity$0() : on_idle.call(null));


var G__30395 = cljs.core.next(seq__30133__$1);
var G__30396 = null;
var G__30397 = (0);
var G__30398 = (0);
seq__30133 = G__30395;
chunk__30135 = G__30396;
count__30136 = G__30397;
i__30137 = G__30398;
continue;
} else {
var G__30399 = cljs.core.next(seq__30133__$1);
var G__30400 = null;
var G__30401 = (0);
var G__30402 = (0);
seq__30133 = G__30399;
chunk__30135 = G__30400;
count__30136 = G__30401;
i__30137 = G__30402;
continue;
}
}
} else {
return null;
}
}
break;
}
});

//# sourceMappingURL=shadow.remote.runtime.shared.js.map
