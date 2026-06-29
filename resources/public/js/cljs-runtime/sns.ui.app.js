goog.provide('sns.ui.app');
sns.ui.app.current_spec = (function sns$ui$app$current_spec(p__40206){
var map__40207 = p__40206;
var map__40207__$1 = cljs.core.__destructure_map(map__40207);
var loot_types = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__40207__$1,new cljs.core.Keyword(null,"loot-types","loot-types",417124260));
var selected = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__40207__$1,new cljs.core.Keyword(null,"selected","selected",574897764));
return cljs.core.some((function (p1__40205_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(selected,new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(p1__40205_SHARP_))){
return p1__40205_SHARP_;
} else {
return null;
}
}),loot_types);
});
sns.ui.app.view = (function sns$ui$app$view(state){
var spec = sns.ui.app.current_spec(state);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.app","div.app",-99849286),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"header.topbar","header.topbar",-1061608070),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.brand","div.brand",-1698268131),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.brand__mark","span.brand__mark",1840525904),"\u2726"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.brand__name","span.brand__name",1601168250),"sns-companion"], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.topbar__tag","p.topbar__tag",130663225),"loot at the table"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.stage","div.stage",-1393965828),sns.ui.render.picker(state),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"main.workbench","main.workbench",-146645268),(cljs.core.truth_(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(state))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.notice.notice--error","p.notice.notice--error",-2112025884),new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(state)], null):null),(cljs.core.truth_(spec)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section.summon","section.summon",-54007066),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.summon__eyebrow","p.summon__eyebrow",-1944746616),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(spec)], null),sns.ui.render.input_form(spec,new cljs.core.Keyword(null,"inputs","inputs",865803858).cljs$core$IFn$_invoke$arity$1(state)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.generate","button.generate",-697997278),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click","click",1912301393),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","generate","ui/generate",-163449114)], null)], null)], null)], null),(cljs.core.truth_(new cljs.core.Keyword(null,"loading?","loading?",1905707049).cljs$core$IFn$_invoke$arity$1(state))?"Summoning\u2026":(""+"Generate "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(spec))))], null)], null):null),sns.ui.render.result(new cljs.core.Keyword(null,"result","result",1415092211).cljs$core$IFn$_invoke$arity$1(state)),(((((new cljs.core.Keyword(null,"result","result",1415092211).cljs$core$IFn$_invoke$arity$1(state) == null)) && ((spec == null))))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.empty","div.empty",47401225),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.empty__line","p.empty__line",2111202841),"Choose a discipline, or roll the hoard."], null)], null):null)], null)], null)], null);
});
sns.ui.app.render_BANG_ = (function sns$ui$app$render_BANG_(state){
return replicant.dom.render(document.getElementById("app"),sns.ui.app.view(state));
});
sns.ui.app.init_BANG_ = (function sns$ui$app$init_BANG_(){
replicant.dom.set_dispatch_BANG_((function (event_data,actions){
return nexus.registry.dispatch(sns.ui.state.store,event_data,actions);
}));

cljs.core.add_watch(sns.ui.state.store,new cljs.core.Keyword("sns.ui.app","render","sns.ui.app/render",797687379),(function (_,___$1,___$2,state){
return sns.ui.app.render_BANG_(state);
}));

nexus.registry.dispatch(sns.ui.state.store,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("fx","load-loot-types","fx/load-loot-types",1855184156)], null)], null));

return sns.ui.app.render_BANG_(cljs.core.deref(sns.ui.state.store));
});

//# sourceMappingURL=sns.ui.app.js.map
