goog.provide('shadow.remote.runtime.tap_support');
shadow.remote.runtime.tap_support.tap_subscribe = (function shadow$remote$runtime$tap_support$tap_subscribe(p__36598,p__36599){
var map__36601 = p__36598;
var map__36601__$1 = cljs.core.__destructure_map(map__36601);
var svc = map__36601__$1;
var subs_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36601__$1,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911));
var obj_support = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36601__$1,new cljs.core.Keyword(null,"obj-support","obj-support",1522559229));
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36601__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
var map__36602 = p__36599;
var map__36602__$1 = cljs.core.__destructure_map(map__36602);
var msg = map__36602__$1;
var from = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36602__$1,new cljs.core.Keyword(null,"from","from",1815293044));
var summary = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36602__$1,new cljs.core.Keyword(null,"summary","summary",380847952));
var history__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36602__$1,new cljs.core.Keyword(null,"history","history",-247395220));
var num = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__36602__$1,new cljs.core.Keyword(null,"num","num",1985240673),(10));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(subs_ref,cljs.core.assoc,from,msg);

if(cljs.core.truth_(history__$1)){
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap-subscribed","tap-subscribed",-1882247432),new cljs.core.Keyword(null,"history","history",-247395220),cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (oid){
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"oid","oid",-768692334),oid,new cljs.core.Keyword(null,"summary","summary",380847952),shadow.remote.runtime.obj_support.obj_describe_STAR_(obj_support,oid)], null);
}),shadow.remote.runtime.obj_support.get_tap_history(obj_support,num)))], null));
} else {
return null;
}
});
shadow.remote.runtime.tap_support.tap_unsubscribe = (function shadow$remote$runtime$tap_support$tap_unsubscribe(p__36627,p__36628){
var map__36631 = p__36627;
var map__36631__$1 = cljs.core.__destructure_map(map__36631);
var subs_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36631__$1,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911));
var map__36632 = p__36628;
var map__36632__$1 = cljs.core.__destructure_map(map__36632);
var from = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36632__$1,new cljs.core.Keyword(null,"from","from",1815293044));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(subs_ref,cljs.core.dissoc,from);
});
shadow.remote.runtime.tap_support.request_tap_history = (function shadow$remote$runtime$tap_support$request_tap_history(p__36648,p__36649){
var map__36651 = p__36648;
var map__36651__$1 = cljs.core.__destructure_map(map__36651);
var obj_support = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36651__$1,new cljs.core.Keyword(null,"obj-support","obj-support",1522559229));
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36651__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
var map__36652 = p__36649;
var map__36652__$1 = cljs.core.__destructure_map(map__36652);
var msg = map__36652__$1;
var num = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__36652__$1,new cljs.core.Keyword(null,"num","num",1985240673),(10));
var tap_ids = shadow.remote.runtime.obj_support.get_tap_history(obj_support,num);
return shadow.remote.runtime.shared.reply(runtime,msg,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap-history","tap-history",-282803347),new cljs.core.Keyword(null,"oids","oids",-1580877688),tap_ids], null));
});
shadow.remote.runtime.tap_support.tool_disconnect = (function shadow$remote$runtime$tap_support$tool_disconnect(p__36669,tid){
var map__36675 = p__36669;
var map__36675__$1 = cljs.core.__destructure_map(map__36675);
var svc = map__36675__$1;
var subs_ref = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36675__$1,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911));
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(subs_ref,cljs.core.dissoc,tid);
});
shadow.remote.runtime.tap_support.start = (function shadow$remote$runtime$tap_support$start(runtime,obj_support){
var subs_ref = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var tap_fn = (function shadow$remote$runtime$tap_support$start_$_runtime_tap(obj){
if((!((obj == null)))){
var oid = shadow.remote.runtime.obj_support.register(obj_support,obj,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"from","from",1815293044),new cljs.core.Keyword(null,"tap","tap",-1086702463)], null));
var seq__36696 = cljs.core.seq(cljs.core.deref(subs_ref));
var chunk__36697 = null;
var count__36698 = (0);
var i__36699 = (0);
while(true){
if((i__36699 < count__36698)){
var vec__36737 = chunk__36697.cljs$core$IIndexed$_nth$arity$2(null,i__36699);
var tid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36737,(0),null);
var tap_config = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36737,(1),null);
shadow.remote.runtime.api.relay_msg(runtime,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap","tap",-1086702463),new cljs.core.Keyword(null,"to","to",192099007),tid,new cljs.core.Keyword(null,"oid","oid",-768692334),oid], null));


var G__36795 = seq__36696;
var G__36796 = chunk__36697;
var G__36797 = count__36698;
var G__36798 = (i__36699 + (1));
seq__36696 = G__36795;
chunk__36697 = G__36796;
count__36698 = G__36797;
i__36699 = G__36798;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__36696);
if(temp__5823__auto__){
var seq__36696__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__36696__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__36696__$1);
var G__36799 = cljs.core.chunk_rest(seq__36696__$1);
var G__36800 = c__5694__auto__;
var G__36801 = cljs.core.count(c__5694__auto__);
var G__36802 = (0);
seq__36696 = G__36799;
chunk__36697 = G__36800;
count__36698 = G__36801;
i__36699 = G__36802;
continue;
} else {
var vec__36749 = cljs.core.first(seq__36696__$1);
var tid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36749,(0),null);
var tap_config = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__36749,(1),null);
shadow.remote.runtime.api.relay_msg(runtime,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"tap","tap",-1086702463),new cljs.core.Keyword(null,"to","to",192099007),tid,new cljs.core.Keyword(null,"oid","oid",-768692334),oid], null));


var G__36804 = cljs.core.next(seq__36696__$1);
var G__36805 = null;
var G__36806 = (0);
var G__36807 = (0);
seq__36696 = G__36804;
chunk__36697 = G__36805;
count__36698 = G__36806;
i__36699 = G__36807;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
});
var svc = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"runtime","runtime",-1331573996),runtime,new cljs.core.Keyword(null,"obj-support","obj-support",1522559229),obj_support,new cljs.core.Keyword(null,"tap-fn","tap-fn",1573556461),tap_fn,new cljs.core.Keyword(null,"subs-ref","subs-ref",-1355989911),subs_ref], null);
shadow.remote.runtime.api.add_extension(runtime,new cljs.core.Keyword("shadow.remote.runtime.tap-support","ext","shadow.remote.runtime.tap-support/ext",1019069674),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"ops","ops",1237330063),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"tap-subscribe","tap-subscribe",411179050),(function (p1__36685_SHARP_){
return shadow.remote.runtime.tap_support.tap_subscribe(svc,p1__36685_SHARP_);
}),new cljs.core.Keyword(null,"tap-unsubscribe","tap-unsubscribe",1183890755),(function (p1__36686_SHARP_){
return shadow.remote.runtime.tap_support.tap_unsubscribe(svc,p1__36686_SHARP_);
}),new cljs.core.Keyword(null,"request-tap-history","request-tap-history",-670837812),(function (p1__36687_SHARP_){
return shadow.remote.runtime.tap_support.request_tap_history(svc,p1__36687_SHARP_);
})], null),new cljs.core.Keyword(null,"on-tool-disconnect","on-tool-disconnect",693464366),(function (p1__36688_SHARP_){
return shadow.remote.runtime.tap_support.tool_disconnect(svc,p1__36688_SHARP_);
})], null));

cljs.core.add_tap(tap_fn);

return svc;
});
shadow.remote.runtime.tap_support.stop = (function shadow$remote$runtime$tap_support$stop(p__36770){
var map__36771 = p__36770;
var map__36771__$1 = cljs.core.__destructure_map(map__36771);
var svc = map__36771__$1;
var tap_fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36771__$1,new cljs.core.Keyword(null,"tap-fn","tap-fn",1573556461));
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__36771__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
cljs.core.remove_tap(tap_fn);

return shadow.remote.runtime.api.del_extension(runtime,new cljs.core.Keyword("shadow.remote.runtime.tap-support","ext","shadow.remote.runtime.tap-support/ext",1019069674));
});

//# sourceMappingURL=shadow.remote.runtime.tap_support.js.map
