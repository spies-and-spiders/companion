goog.provide('sns.ui.nexus');
nexus.registry.register_system__GT_state_BANG_(cljs.core.deref);
nexus.registry.register_placeholder_BANG_(new cljs.core.Keyword("event.target","value","event.target/value",857030372),(function (p__38009){
var map__38010 = p__38009;
var map__38010__$1 = cljs.core.__destructure_map(map__38010);
var dom_event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38010__$1,new cljs.core.Keyword("replicant","dom-event","replicant/dom-event",-1613182512));
var G__38011 = dom_event;
var G__38011__$1 = (((G__38011 == null))?null:G__38011.target);
if((G__38011__$1 == null)){
return null;
} else {
return G__38011__$1.value;
}
}));
nexus.registry.register_placeholder_BANG_(new cljs.core.Keyword("event.target","checked","event.target/checked",1556027292),(function (p__38013){
var map__38014 = p__38013;
var map__38014__$1 = cljs.core.__destructure_map(map__38014);
var dom_event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38014__$1,new cljs.core.Keyword("replicant","dom-event","replicant/dom-event",-1613182512));
var G__38015 = dom_event;
var G__38015__$1 = (((G__38015 == null))?null:G__38015.target);
if((G__38015__$1 == null)){
return null;
} else {
return G__38015__$1.checked;
}
}));
nexus.registry.register_effect_BANG_(new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),(function (_ctx,system,path,v){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(system,cljs.core.assoc_in,path,v);
}));
nexus.registry.register_effect_BANG_(new cljs.core.Keyword("fx","load-loot-types","fx/load-loot-types",1855184156),(function (p__38016,_system){
var map__38017 = p__38016;
var map__38017__$1 = cljs.core.__destructure_map(map__38017);
var dispatch = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38017__$1,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009));
return sns.ui.api.request(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"url","url",276297046),"/api/loot-types"], null),(function (types){
var G__38018 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"loot-types","loot-types",417124260)], null),types], null)], null);
return (dispatch.cljs$core$IFn$_invoke$arity$1 ? dispatch.cljs$core$IFn$_invoke$arity$1(G__38018) : dispatch.call(null,G__38018));
}),(function (err){
var G__38019 = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(err)], null)], null);
return (dispatch.cljs$core$IFn$_invoke$arity$1 ? dispatch.cljs$core$IFn$_invoke$arity$1(G__38019) : dispatch.call(null,G__38019));
}));
}));
sns.ui.nexus.result_effect = (function sns$ui$nexus$result_effect(p__38023,req){
var map__38024 = p__38023;
var map__38024__$1 = cljs.core.__destructure_map(map__38024);
var dispatch = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38024__$1,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009));
var G__38028_38052 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),true], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),null], null)], null);
(dispatch.cljs$core$IFn$_invoke$arity$1 ? dispatch.cljs$core$IFn$_invoke$arity$1(G__38028_38052) : dispatch.call(null,G__38028_38052));

return sns.ui.api.request(req,(function (vm){
var G__38037 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"result","result",1415092211)], null),vm], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false], null)], null);
return (dispatch.cljs$core$IFn$_invoke$arity$1 ? dispatch.cljs$core$IFn$_invoke$arity$1(G__38037) : dispatch.call(null,G__38037));
}),(function (err){
var G__38040 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"error","error",-978969032)], null),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(err)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"loading?","loading?",1905707049)], null),false], null)], null);
return (dispatch.cljs$core$IFn$_invoke$arity$1 ? dispatch.cljs$core$IFn$_invoke$arity$1(G__38040) : dispatch.call(null,G__38040));
}));
});
nexus.registry.register_effect_BANG_(new cljs.core.Keyword("fx","generate","fx/generate",-163455704),(function (ctx,_system,id,inputs){
return sns.ui.nexus.result_effect(ctx,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"post","post",269697687),new cljs.core.Keyword(null,"url","url",276297046),"/api/generate",new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"id","id",-1388402092),id,new cljs.core.Keyword(null,"inputs","inputs",865803858),inputs], null)], null));
}));
nexus.registry.register_effect_BANG_(new cljs.core.Keyword("fx","roll","fx/roll",11270245),(function (ctx,_system,inputs){
return sns.ui.nexus.result_effect(ctx,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"post","post",269697687),new cljs.core.Keyword(null,"url","url",276297046),"/api/roll",new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"inputs","inputs",865803858),inputs], null)], null));
}));
nexus.registry.register_effect_BANG_(new cljs.core.Keyword("fx","action","fx/action",-811235302),(function (ctx,_system,id,action,params){
return sns.ui.nexus.result_effect(ctx,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"method","method",55703592),new cljs.core.Keyword(null,"post","post",269697687),new cljs.core.Keyword(null,"url","url",276297046),"/api/action",new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"id","id",-1388402092),id,new cljs.core.Keyword(null,"action","action",-811238024),action,new cljs.core.Keyword(null,"params","params",710516235),params], null)], null));
}));
nexus.registry.register_action_BANG_(new cljs.core.Keyword("ui","select-type","ui/select-type",-1022679010),(function (_state,id){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"selected","selected",574897764)], null),id], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"inputs","inputs",865803858)], null),cljs.core.PersistentArrayMap.EMPTY], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"result","result",1415092211)], null),null], null)], null);
}));
nexus.registry.register_action_BANG_(new cljs.core.Keyword("ui","set-input","ui/set-input",212729820),(function (_state,field,value){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","assoc-in","fx/assoc-in",-1754128836),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"inputs","inputs",865803858),field], null),value], null)], null);
}));
nexus.registry.register_action_BANG_(new cljs.core.Keyword("ui","generate","ui/generate",-163449114),(function (state){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","generate","fx/generate",-163455704),new cljs.core.Keyword(null,"selected","selected",574897764).cljs$core$IFn$_invoke$arity$1(state),new cljs.core.Keyword(null,"inputs","inputs",865803858).cljs$core$IFn$_invoke$arity$1(state)], null)], null);
}));
nexus.registry.register_action_BANG_(new cljs.core.Keyword("ui","roll","ui/roll",11270435),(function (state){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","roll","fx/roll",11270245),new cljs.core.Keyword(null,"inputs","inputs",865803858).cljs$core$IFn$_invoke$arity$1(state)], null)], null);
}));
nexus.registry.register_action_BANG_(new cljs.core.Keyword("loot","action","loot/action",-1394033200),(function (_state,p__38047){
var map__38048 = p__38047;
var map__38048__$1 = cljs.core.__destructure_map(map__38048);
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38048__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var action = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38048__$1,new cljs.core.Keyword(null,"action","action",-811238024));
var params = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38048__$1,new cljs.core.Keyword(null,"params","params",710516235));
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","action","fx/action",-811235302),id,action,params], null)], null);
}));

//# sourceMappingURL=sns.ui.nexus.js.map
