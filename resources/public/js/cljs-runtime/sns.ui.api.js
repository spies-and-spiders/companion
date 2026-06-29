goog.provide('sns.ui.api');
sns.ui.api.reader = cognitect.transit.reader.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"json","json",1279968570));
sns.ui.api.writer = cognitect.transit.writer.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"json","json",1279968570));
/**
 * Issue a transit request to `url`. On success calls `(on-ok decoded-body)`;
 * on any error calls `(on-err {:error msg ...})`.
 */
sns.ui.api.request = (function sns$ui$api$request(p__37955,on_ok,on_err){
var map__37956 = p__37955;
var map__37956__$1 = cljs.core.__destructure_map(map__37956);
var method = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37956__$1,new cljs.core.Keyword(null,"method","method",55703592));
var url = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37956__$1,new cljs.core.Keyword(null,"url","url",276297046));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37956__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var init = (function (){var G__37957 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"method","method",55703592),clojure.string.upper_case(cljs.core.name((function (){var or__5162__auto__ = method;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return new cljs.core.Keyword(null,"get","get",1683182755);
}
})())),new cljs.core.Keyword(null,"headers","headers",-835030129),new cljs.core.PersistentArrayMap(null, 1, ["Accept","application/transit+json"], null)], null);
if(cljs.core.truth_(body)){
return cljs.core.assoc_in(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__37957,new cljs.core.Keyword(null,"body","body",-2049205669),cognitect.transit.write(sns.ui.api.writer,body)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"headers","headers",-835030129),"Content-Type"], null),"application/transit+json");
} else {
return G__37957;
}
})();
return fetch(url,cljs.core.clj__GT_js(init)).then((function (res){
return res.text().then((function (text){
return [res,text];
}));
})).then((function (pair){
var res = (pair[(0)]);
var text = (pair[(1)]);
var data = ((cljs.core.seq(text))?cognitect.transit.read(sns.ui.api.reader,text):null);
if(cljs.core.truth_(res.ok)){
return (on_ok.cljs$core$IFn$_invoke$arity$1 ? on_ok.cljs$core$IFn$_invoke$arity$1(data) : on_ok.call(null,data));
} else {
var G__37966 = ((cljs.core.map_QMARK_(data))?data:new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),(""+"HTTP "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(res.status))], null));
return (on_err.cljs$core$IFn$_invoke$arity$1 ? on_err.cljs$core$IFn$_invoke$arity$1(G__37966) : on_err.call(null,G__37966));
}
})).catch((function (err){
var G__37967 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"error","error",-978969032),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(err))], null);
return (on_err.cljs$core$IFn$_invoke$arity$1 ? on_err.cljs$core$IFn$_invoke$arity$1(G__37967) : on_err.call(null,G__37967));
}));
});

//# sourceMappingURL=sns.ui.api.js.map
