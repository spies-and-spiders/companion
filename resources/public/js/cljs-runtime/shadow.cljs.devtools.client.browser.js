goog.provide('shadow.cljs.devtools.client.browser');
shadow.cljs.devtools.client.browser.devtools_msg = (function shadow$cljs$devtools$client$browser$devtools_msg(var_args){
var args__5903__auto__ = [];
var len__5897__auto___38475 = arguments.length;
var i__5898__auto___38476 = (0);
while(true){
if((i__5898__auto___38476 < len__5897__auto___38475)){
args__5903__auto__.push((arguments[i__5898__auto___38476]));

var G__38477 = (i__5898__auto___38476 + (1));
i__5898__auto___38476 = G__38477;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((1) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((1)),(0),null)):null);
return shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5904__auto__);
});

(shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic = (function (msg,args){
if(shadow.cljs.devtools.client.env.log){
if(cljs.core.seq(shadow.cljs.devtools.client.env.log_style)){
return console.log.apply(console,cljs.core.into_array.cljs$core$IFn$_invoke$arity$1(cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(""+"%cshadow-cljs: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg)),shadow.cljs.devtools.client.env.log_style], null),args)));
} else {
return console.log.apply(console,cljs.core.into_array.cljs$core$IFn$_invoke$arity$1(cljs.core.into.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [(""+"shadow-cljs: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg))], null),args)));
}
} else {
return null;
}
}));

(shadow.cljs.devtools.client.browser.devtools_msg.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(shadow.cljs.devtools.client.browser.devtools_msg.cljs$lang$applyTo = (function (seq37951){
var G__37952 = cljs.core.first(seq37951);
var seq37951__$1 = cljs.core.next(seq37951);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__37952,seq37951__$1);
}));

shadow.cljs.devtools.client.browser.script_eval = (function shadow$cljs$devtools$client$browser$script_eval(code){
return goog.globalEval(code);
});
shadow.cljs.devtools.client.browser.do_js_load = (function shadow$cljs$devtools$client$browser$do_js_load(sources){
var seq__37975 = cljs.core.seq(sources);
var chunk__37976 = null;
var count__37977 = (0);
var i__37978 = (0);
while(true){
if((i__37978 < count__37977)){
var map__37991 = chunk__37976.cljs$core$IIndexed$_nth$arity$2(null,i__37978);
var map__37991__$1 = cljs.core.__destructure_map(map__37991);
var src = map__37991__$1;
var resource_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37991__$1,new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582));
var output_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37991__$1,new cljs.core.Keyword(null,"output-name","output-name",-1769107767));
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37991__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37991__$1,new cljs.core.Keyword(null,"js","js",1768080579));
$CLJS.SHADOW_ENV.setLoaded(output_name);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load JS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([resource_name], 0));

shadow.cljs.devtools.client.env.before_load_src(src);

try{shadow.cljs.devtools.client.browser.script_eval((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js)+"\n//# sourceURL="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($CLJS.SHADOW_ENV.scriptBase)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(output_name)));
}catch (e37992){var e_38481 = e37992;
if(shadow.cljs.devtools.client.env.log){
console.error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)),e_38481);
} else {
}

throw (new Error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)+": "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(e_38481.message))));
}

var G__38482 = seq__37975;
var G__38483 = chunk__37976;
var G__38484 = count__37977;
var G__38485 = (i__37978 + (1));
seq__37975 = G__38482;
chunk__37976 = G__38483;
count__37977 = G__38484;
i__37978 = G__38485;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__37975);
if(temp__5823__auto__){
var seq__37975__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__37975__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__37975__$1);
var G__38486 = cljs.core.chunk_rest(seq__37975__$1);
var G__38487 = c__5694__auto__;
var G__38488 = cljs.core.count(c__5694__auto__);
var G__38489 = (0);
seq__37975 = G__38486;
chunk__37976 = G__38487;
count__37977 = G__38488;
i__37978 = G__38489;
continue;
} else {
var map__37996 = cljs.core.first(seq__37975__$1);
var map__37996__$1 = cljs.core.__destructure_map(map__37996);
var src = map__37996__$1;
var resource_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37996__$1,new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582));
var output_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37996__$1,new cljs.core.Keyword(null,"output-name","output-name",-1769107767));
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37996__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__37996__$1,new cljs.core.Keyword(null,"js","js",1768080579));
$CLJS.SHADOW_ENV.setLoaded(output_name);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load JS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([resource_name], 0));

shadow.cljs.devtools.client.env.before_load_src(src);

try{shadow.cljs.devtools.client.browser.script_eval((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js)+"\n//# sourceURL="+cljs.core.str.cljs$core$IFn$_invoke$arity$1($CLJS.SHADOW_ENV.scriptBase)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(output_name)));
}catch (e37999){var e_38493 = e37999;
if(shadow.cljs.devtools.client.env.log){
console.error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)),e_38493);
} else {
}

throw (new Error((""+"Failed to load "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name)+": "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(e_38493.message))));
}

var G__38494 = cljs.core.next(seq__37975__$1);
var G__38495 = null;
var G__38496 = (0);
var G__38497 = (0);
seq__37975 = G__38494;
chunk__37976 = G__38495;
count__37977 = G__38496;
i__37978 = G__38497;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.do_js_reload = (function shadow$cljs$devtools$client$browser$do_js_reload(msg,sources,complete_fn,failure_fn){
return shadow.cljs.devtools.client.env.do_js_reload.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc.cljs$core$IFn$_invoke$arity$variadic(msg,new cljs.core.Keyword(null,"log-missing-fn","log-missing-fn",732676765),(function (fn_sym){
return null;
}),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"log-call-async","log-call-async",183826192),(function (fn_sym){
return shadow.cljs.devtools.client.browser.devtools_msg((""+"call async "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym)));
}),new cljs.core.Keyword(null,"log-call","log-call",412404391),(function (fn_sym){
return shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym)));
})], 0)),(function (next){
shadow.cljs.devtools.client.browser.do_js_load(sources);

return (next.cljs$core$IFn$_invoke$arity$0 ? next.cljs$core$IFn$_invoke$arity$0() : next.call(null));
}),complete_fn,failure_fn);
});
/**
 * when (require '["some-str" :as x]) is done at the REPL we need to manually call the shadow.js.require for it
 * since the file only adds the shadow$provide. only need to do this for shadow-js.
 */
shadow.cljs.devtools.client.browser.do_js_requires = (function shadow$cljs$devtools$client$browser$do_js_requires(js_requires){
var seq__38005 = cljs.core.seq(js_requires);
var chunk__38006 = null;
var count__38007 = (0);
var i__38008 = (0);
while(true){
if((i__38008 < count__38007)){
var js_ns = chunk__38006.cljs$core$IIndexed$_nth$arity$2(null,i__38008);
var require_str_38498 = (""+"var "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+" = shadow.js.require(\""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+"\");");
shadow.cljs.devtools.client.browser.script_eval(require_str_38498);


var G__38499 = seq__38005;
var G__38500 = chunk__38006;
var G__38501 = count__38007;
var G__38502 = (i__38008 + (1));
seq__38005 = G__38499;
chunk__38006 = G__38500;
count__38007 = G__38501;
i__38008 = G__38502;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__38005);
if(temp__5823__auto__){
var seq__38005__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__38005__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__38005__$1);
var G__38503 = cljs.core.chunk_rest(seq__38005__$1);
var G__38504 = c__5694__auto__;
var G__38505 = cljs.core.count(c__5694__auto__);
var G__38506 = (0);
seq__38005 = G__38503;
chunk__38006 = G__38504;
count__38007 = G__38505;
i__38008 = G__38506;
continue;
} else {
var js_ns = cljs.core.first(seq__38005__$1);
var require_str_38507 = (""+"var "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+" = shadow.js.require(\""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(js_ns)+"\");");
shadow.cljs.devtools.client.browser.script_eval(require_str_38507);


var G__38508 = cljs.core.next(seq__38005__$1);
var G__38509 = null;
var G__38510 = (0);
var G__38511 = (0);
seq__38005 = G__38508;
chunk__38006 = G__38509;
count__38007 = G__38510;
i__38008 = G__38511;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.cljs.devtools.client.browser.handle_build_complete = (function shadow$cljs$devtools$client$browser$handle_build_complete(runtime,p__38027){
var map__38029 = p__38027;
var map__38029__$1 = cljs.core.__destructure_map(map__38029);
var msg = map__38029__$1;
var info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38029__$1,new cljs.core.Keyword(null,"info","info",-317069002));
var reload_info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38029__$1,new cljs.core.Keyword(null,"reload-info","reload-info",1648088086));
var warnings = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.distinct.cljs$core$IFn$_invoke$arity$1((function (){var iter__5649__auto__ = (function shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__38030(s__38031){
return (new cljs.core.LazySeq(null,(function (){
var s__38031__$1 = s__38031;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__38031__$1);
if(temp__5823__auto__){
var xs__6383__auto__ = temp__5823__auto__;
var map__38038 = cljs.core.first(xs__6383__auto__);
var map__38038__$1 = cljs.core.__destructure_map(map__38038);
var src = map__38038__$1;
var resource_name = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38038__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
var warnings = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38038__$1,new cljs.core.Keyword(null,"warnings","warnings",-735437651));
if(cljs.core.not(new cljs.core.Keyword(null,"from-jar","from-jar",1050932827).cljs$core$IFn$_invoke$arity$1(src))){
var iterys__5645__auto__ = ((function (s__38031__$1,map__38038,map__38038__$1,src,resource_name,warnings,xs__6383__auto__,temp__5823__auto__,map__38029,map__38029__$1,msg,info,reload_info){
return (function shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__38030_$_iter__38033(s__38034){
return (new cljs.core.LazySeq(null,((function (s__38031__$1,map__38038,map__38038__$1,src,resource_name,warnings,xs__6383__auto__,temp__5823__auto__,map__38029,map__38029__$1,msg,info,reload_info){
return (function (){
var s__38034__$1 = s__38034;
while(true){
var temp__5823__auto____$1 = cljs.core.seq(s__38034__$1);
if(temp__5823__auto____$1){
var s__38034__$2 = temp__5823__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__38034__$2)){
var c__5647__auto__ = cljs.core.chunk_first(s__38034__$2);
var size__5648__auto__ = cljs.core.count(c__5647__auto__);
var b__38036 = cljs.core.chunk_buffer(size__5648__auto__);
if((function (){var i__38035 = (0);
while(true){
if((i__38035 < size__5648__auto__)){
var warning = cljs.core._nth(c__5647__auto__,i__38035);
cljs.core.chunk_append(b__38036,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(warning,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100),resource_name));

var G__38512 = (i__38035 + (1));
i__38035 = G__38512;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38036),shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__38030_$_iter__38033(cljs.core.chunk_rest(s__38034__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38036),null);
}
} else {
var warning = cljs.core.first(s__38034__$2);
return cljs.core.cons(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(warning,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100),resource_name),shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__38030_$_iter__38033(cljs.core.rest(s__38034__$2)));
}
} else {
return null;
}
break;
}
});})(s__38031__$1,map__38038,map__38038__$1,src,resource_name,warnings,xs__6383__auto__,temp__5823__auto__,map__38029,map__38029__$1,msg,info,reload_info))
,null,null));
});})(s__38031__$1,map__38038,map__38038__$1,src,resource_name,warnings,xs__6383__auto__,temp__5823__auto__,map__38029,map__38029__$1,msg,info,reload_info))
;
var fs__5646__auto__ = cljs.core.seq(iterys__5645__auto__(warnings));
if(fs__5646__auto__){
return cljs.core.concat.cljs$core$IFn$_invoke$arity$2(fs__5646__auto__,shadow$cljs$devtools$client$browser$handle_build_complete_$_iter__38030(cljs.core.rest(s__38031__$1)));
} else {
var G__38516 = cljs.core.rest(s__38031__$1);
s__38031__$1 = G__38516;
continue;
}
} else {
var G__38517 = cljs.core.rest(s__38031__$1);
s__38031__$1 = G__38517;
continue;
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5649__auto__(new cljs.core.Keyword(null,"sources","sources",-321166424).cljs$core$IFn$_invoke$arity$1(info));
})()));
if(shadow.cljs.devtools.client.env.log){
var seq__38042_38519 = cljs.core.seq(warnings);
var chunk__38043_38520 = null;
var count__38044_38521 = (0);
var i__38045_38522 = (0);
while(true){
if((i__38045_38522 < count__38044_38521)){
var map__38051_38523 = chunk__38043_38520.cljs$core$IIndexed$_nth$arity$2(null,i__38045_38522);
var map__38051_38524__$1 = cljs.core.__destructure_map(map__38051_38523);
var w_38525 = map__38051_38524__$1;
var msg_38526__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38051_38524__$1,new cljs.core.Keyword(null,"msg","msg",-1386103444));
var line_38527 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38051_38524__$1,new cljs.core.Keyword(null,"line","line",212345235));
var column_38528 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38051_38524__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var resource_name_38529 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38051_38524__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
console.warn((""+"BUILD-WARNING in "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name_38529)+" at ["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_38527)+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(column_38528)+"]\n\t"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg_38526__$1)));


var G__38530 = seq__38042_38519;
var G__38531 = chunk__38043_38520;
var G__38532 = count__38044_38521;
var G__38533 = (i__38045_38522 + (1));
seq__38042_38519 = G__38530;
chunk__38043_38520 = G__38531;
count__38044_38521 = G__38532;
i__38045_38522 = G__38533;
continue;
} else {
var temp__5823__auto___38534 = cljs.core.seq(seq__38042_38519);
if(temp__5823__auto___38534){
var seq__38042_38535__$1 = temp__5823__auto___38534;
if(cljs.core.chunked_seq_QMARK_(seq__38042_38535__$1)){
var c__5694__auto___38536 = cljs.core.chunk_first(seq__38042_38535__$1);
var G__38537 = cljs.core.chunk_rest(seq__38042_38535__$1);
var G__38538 = c__5694__auto___38536;
var G__38539 = cljs.core.count(c__5694__auto___38536);
var G__38540 = (0);
seq__38042_38519 = G__38537;
chunk__38043_38520 = G__38538;
count__38044_38521 = G__38539;
i__38045_38522 = G__38540;
continue;
} else {
var map__38053_38541 = cljs.core.first(seq__38042_38535__$1);
var map__38053_38542__$1 = cljs.core.__destructure_map(map__38053_38541);
var w_38543 = map__38053_38542__$1;
var msg_38544__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38053_38542__$1,new cljs.core.Keyword(null,"msg","msg",-1386103444));
var line_38545 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38053_38542__$1,new cljs.core.Keyword(null,"line","line",212345235));
var column_38546 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38053_38542__$1,new cljs.core.Keyword(null,"column","column",2078222095));
var resource_name_38547 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38053_38542__$1,new cljs.core.Keyword(null,"resource-name","resource-name",2001617100));
console.warn((""+"BUILD-WARNING in "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(resource_name_38547)+" at ["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(line_38545)+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(column_38546)+"]\n\t"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(msg_38544__$1)));


var G__38548 = cljs.core.next(seq__38042_38535__$1);
var G__38549 = null;
var G__38550 = (0);
var G__38551 = (0);
seq__38042_38519 = G__38548;
chunk__38043_38520 = G__38549;
count__38044_38521 = G__38550;
i__38045_38522 = G__38551;
continue;
}
} else {
}
}
break;
}
} else {
}

if((!(shadow.cljs.devtools.client.env.autoload))){
return shadow.cljs.devtools.client.hud.load_end_success();
} else {
if(((cljs.core.empty_QMARK_(warnings)) || (shadow.cljs.devtools.client.env.ignore_warnings))){
var sources_to_get = shadow.cljs.devtools.client.env.filter_reload_sources(info,reload_info);
if(cljs.core.not(cljs.core.seq(sources_to_get))){
return shadow.cljs.devtools.client.hud.load_end_success();
} else {
if(cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"after-load","after-load",-1278503285)], null)))){
} else {
shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("reloading code but no :after-load hooks are configured!",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["https://shadow-cljs.github.io/docs/UsersGuide.html#_lifecycle_hooks"], 0));
}

return shadow.cljs.devtools.client.shared.load_sources(runtime,sources_to_get,(function (p1__38025_SHARP_){
return shadow.cljs.devtools.client.browser.do_js_reload(msg,p1__38025_SHARP_,shadow.cljs.devtools.client.hud.load_end_success,shadow.cljs.devtools.client.hud.load_failure);
}));
}
} else {
return null;
}
}
});
shadow.cljs.devtools.client.browser.page_load_uri = (cljs.core.truth_(goog.global.document)?goog.Uri.parse(document.location.href):null);
shadow.cljs.devtools.client.browser.match_paths = (function shadow$cljs$devtools$client$browser$match_paths(old,new$){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("file",shadow.cljs.devtools.client.browser.page_load_uri.getScheme())){
var rel_new = cljs.core.subs.cljs$core$IFn$_invoke$arity$2(new$,(1));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(old,rel_new)) || (clojure.string.starts_with_QMARK_(old,(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(rel_new)+"?"))))){
return rel_new;
} else {
return null;
}
} else {
var node_uri = goog.Uri.parse(old);
var node_uri_resolved = shadow.cljs.devtools.client.browser.page_load_uri.resolve(node_uri);
var node_abs = node_uri_resolved.getPath();
var and__5160__auto__ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$1(shadow.cljs.devtools.client.browser.page_load_uri.hasSameDomainAs(node_uri))) || (cljs.core.not(node_uri.hasDomain())));
if(and__5160__auto__){
var and__5160__auto____$1 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(node_abs,new$);
if(and__5160__auto____$1){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var G__38056 = node_uri;
G__38056.setQuery(null);

G__38056.setPath(new$);

return G__38056;
})()));
} else {
return and__5160__auto____$1;
}
} else {
return and__5160__auto__;
}
}
});
shadow.cljs.devtools.client.browser.handle_asset_update = (function shadow$cljs$devtools$client$browser$handle_asset_update(p__38057){
var map__38058 = p__38057;
var map__38058__$1 = cljs.core.__destructure_map(map__38058);
var msg = map__38058__$1;
var updates = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38058__$1,new cljs.core.Keyword(null,"updates","updates",2013983452));
var reload_info = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38058__$1,new cljs.core.Keyword(null,"reload-info","reload-info",1648088086));
var seq__38059 = cljs.core.seq(updates);
var chunk__38061 = null;
var count__38062 = (0);
var i__38063 = (0);
while(true){
if((i__38063 < count__38062)){
var path = chunk__38061.cljs$core$IIndexed$_nth$arity$2(null,i__38063);
if(clojure.string.ends_with_QMARK_(path,"css")){
var seq__38271_38556 = cljs.core.seq(cljs.core.array_seq.cljs$core$IFn$_invoke$arity$1(document.querySelectorAll("link[rel=\"stylesheet\"]")));
var chunk__38275_38557 = null;
var count__38276_38558 = (0);
var i__38277_38559 = (0);
while(true){
if((i__38277_38559 < count__38276_38558)){
var node_38560 = chunk__38275_38557.cljs$core$IIndexed$_nth$arity$2(null,i__38277_38559);
if(cljs.core.not(node_38560.shadow$old)){
var path_match_38561 = shadow.cljs.devtools.client.browser.match_paths(node_38560.getAttribute("href"),path);
if(cljs.core.truth_(path_match_38561)){
var new_link_38562 = (function (){var G__38308 = node_38560.cloneNode(true);
G__38308.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_38561)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__38308;
})();
(node_38560.shadow$old = true);

(new_link_38562.onload = ((function (seq__38271_38556,chunk__38275_38557,count__38276_38558,i__38277_38559,seq__38059,chunk__38061,count__38062,i__38063,new_link_38562,path_match_38561,node_38560,path,map__38058,map__38058__$1,msg,updates,reload_info){
return (function (e){
var seq__38309_38563 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__38311_38564 = null;
var count__38312_38565 = (0);
var i__38313_38566 = (0);
while(true){
if((i__38313_38566 < count__38312_38565)){
var map__38319_38567 = chunk__38311_38564.cljs$core$IIndexed$_nth$arity$2(null,i__38313_38566);
var map__38319_38568__$1 = cljs.core.__destructure_map(map__38319_38567);
var task_38569 = map__38319_38568__$1;
var fn_str_38570 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38319_38568__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38571 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38319_38568__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38572 = goog.getObjectByName(fn_str_38570,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38571)));

(fn_obj_38572.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38572.cljs$core$IFn$_invoke$arity$2(path,new_link_38562) : fn_obj_38572.call(null,path,new_link_38562));


var G__38573 = seq__38309_38563;
var G__38574 = chunk__38311_38564;
var G__38575 = count__38312_38565;
var G__38576 = (i__38313_38566 + (1));
seq__38309_38563 = G__38573;
chunk__38311_38564 = G__38574;
count__38312_38565 = G__38575;
i__38313_38566 = G__38576;
continue;
} else {
var temp__5823__auto___38577 = cljs.core.seq(seq__38309_38563);
if(temp__5823__auto___38577){
var seq__38309_38581__$1 = temp__5823__auto___38577;
if(cljs.core.chunked_seq_QMARK_(seq__38309_38581__$1)){
var c__5694__auto___38582 = cljs.core.chunk_first(seq__38309_38581__$1);
var G__38583 = cljs.core.chunk_rest(seq__38309_38581__$1);
var G__38584 = c__5694__auto___38582;
var G__38585 = cljs.core.count(c__5694__auto___38582);
var G__38586 = (0);
seq__38309_38563 = G__38583;
chunk__38311_38564 = G__38584;
count__38312_38565 = G__38585;
i__38313_38566 = G__38586;
continue;
} else {
var map__38321_38587 = cljs.core.first(seq__38309_38581__$1);
var map__38321_38588__$1 = cljs.core.__destructure_map(map__38321_38587);
var task_38589 = map__38321_38588__$1;
var fn_str_38590 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38321_38588__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38591 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38321_38588__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38592 = goog.getObjectByName(fn_str_38590,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38591)));

(fn_obj_38592.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38592.cljs$core$IFn$_invoke$arity$2(path,new_link_38562) : fn_obj_38592.call(null,path,new_link_38562));


var G__38593 = cljs.core.next(seq__38309_38581__$1);
var G__38594 = null;
var G__38595 = (0);
var G__38596 = (0);
seq__38309_38563 = G__38593;
chunk__38311_38564 = G__38594;
count__38312_38565 = G__38595;
i__38313_38566 = G__38596;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_38560);
});})(seq__38271_38556,chunk__38275_38557,count__38276_38558,i__38277_38559,seq__38059,chunk__38061,count__38062,i__38063,new_link_38562,path_match_38561,node_38560,path,map__38058,map__38058__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_38561], 0));

goog.dom.insertSiblingAfter(new_link_38562,node_38560);


var G__38597 = seq__38271_38556;
var G__38598 = chunk__38275_38557;
var G__38599 = count__38276_38558;
var G__38600 = (i__38277_38559 + (1));
seq__38271_38556 = G__38597;
chunk__38275_38557 = G__38598;
count__38276_38558 = G__38599;
i__38277_38559 = G__38600;
continue;
} else {
var G__38601 = seq__38271_38556;
var G__38602 = chunk__38275_38557;
var G__38603 = count__38276_38558;
var G__38604 = (i__38277_38559 + (1));
seq__38271_38556 = G__38601;
chunk__38275_38557 = G__38602;
count__38276_38558 = G__38603;
i__38277_38559 = G__38604;
continue;
}
} else {
var G__38605 = seq__38271_38556;
var G__38606 = chunk__38275_38557;
var G__38607 = count__38276_38558;
var G__38608 = (i__38277_38559 + (1));
seq__38271_38556 = G__38605;
chunk__38275_38557 = G__38606;
count__38276_38558 = G__38607;
i__38277_38559 = G__38608;
continue;
}
} else {
var temp__5823__auto___38609 = cljs.core.seq(seq__38271_38556);
if(temp__5823__auto___38609){
var seq__38271_38610__$1 = temp__5823__auto___38609;
if(cljs.core.chunked_seq_QMARK_(seq__38271_38610__$1)){
var c__5694__auto___38614 = cljs.core.chunk_first(seq__38271_38610__$1);
var G__38615 = cljs.core.chunk_rest(seq__38271_38610__$1);
var G__38616 = c__5694__auto___38614;
var G__38617 = cljs.core.count(c__5694__auto___38614);
var G__38618 = (0);
seq__38271_38556 = G__38615;
chunk__38275_38557 = G__38616;
count__38276_38558 = G__38617;
i__38277_38559 = G__38618;
continue;
} else {
var node_38619 = cljs.core.first(seq__38271_38610__$1);
if(cljs.core.not(node_38619.shadow$old)){
var path_match_38620 = shadow.cljs.devtools.client.browser.match_paths(node_38619.getAttribute("href"),path);
if(cljs.core.truth_(path_match_38620)){
var new_link_38621 = (function (){var G__38323 = node_38619.cloneNode(true);
G__38323.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_38620)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__38323;
})();
(node_38619.shadow$old = true);

(new_link_38621.onload = ((function (seq__38271_38556,chunk__38275_38557,count__38276_38558,i__38277_38559,seq__38059,chunk__38061,count__38062,i__38063,new_link_38621,path_match_38620,node_38619,seq__38271_38610__$1,temp__5823__auto___38609,path,map__38058,map__38058__$1,msg,updates,reload_info){
return (function (e){
var seq__38324_38622 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__38326_38623 = null;
var count__38327_38624 = (0);
var i__38328_38625 = (0);
while(true){
if((i__38328_38625 < count__38327_38624)){
var map__38334_38626 = chunk__38326_38623.cljs$core$IIndexed$_nth$arity$2(null,i__38328_38625);
var map__38334_38627__$1 = cljs.core.__destructure_map(map__38334_38626);
var task_38628 = map__38334_38627__$1;
var fn_str_38629 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38334_38627__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38630 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38334_38627__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38631 = goog.getObjectByName(fn_str_38629,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38630)));

(fn_obj_38631.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38631.cljs$core$IFn$_invoke$arity$2(path,new_link_38621) : fn_obj_38631.call(null,path,new_link_38621));


var G__38632 = seq__38324_38622;
var G__38633 = chunk__38326_38623;
var G__38634 = count__38327_38624;
var G__38635 = (i__38328_38625 + (1));
seq__38324_38622 = G__38632;
chunk__38326_38623 = G__38633;
count__38327_38624 = G__38634;
i__38328_38625 = G__38635;
continue;
} else {
var temp__5823__auto___38636__$1 = cljs.core.seq(seq__38324_38622);
if(temp__5823__auto___38636__$1){
var seq__38324_38637__$1 = temp__5823__auto___38636__$1;
if(cljs.core.chunked_seq_QMARK_(seq__38324_38637__$1)){
var c__5694__auto___38638 = cljs.core.chunk_first(seq__38324_38637__$1);
var G__38639 = cljs.core.chunk_rest(seq__38324_38637__$1);
var G__38640 = c__5694__auto___38638;
var G__38641 = cljs.core.count(c__5694__auto___38638);
var G__38642 = (0);
seq__38324_38622 = G__38639;
chunk__38326_38623 = G__38640;
count__38327_38624 = G__38641;
i__38328_38625 = G__38642;
continue;
} else {
var map__38336_38643 = cljs.core.first(seq__38324_38637__$1);
var map__38336_38644__$1 = cljs.core.__destructure_map(map__38336_38643);
var task_38645 = map__38336_38644__$1;
var fn_str_38646 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38336_38644__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38647 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38336_38644__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38648 = goog.getObjectByName(fn_str_38646,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38647)));

(fn_obj_38648.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38648.cljs$core$IFn$_invoke$arity$2(path,new_link_38621) : fn_obj_38648.call(null,path,new_link_38621));


var G__38649 = cljs.core.next(seq__38324_38637__$1);
var G__38650 = null;
var G__38651 = (0);
var G__38652 = (0);
seq__38324_38622 = G__38649;
chunk__38326_38623 = G__38650;
count__38327_38624 = G__38651;
i__38328_38625 = G__38652;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_38619);
});})(seq__38271_38556,chunk__38275_38557,count__38276_38558,i__38277_38559,seq__38059,chunk__38061,count__38062,i__38063,new_link_38621,path_match_38620,node_38619,seq__38271_38610__$1,temp__5823__auto___38609,path,map__38058,map__38058__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_38620], 0));

goog.dom.insertSiblingAfter(new_link_38621,node_38619);


var G__38653 = cljs.core.next(seq__38271_38610__$1);
var G__38654 = null;
var G__38655 = (0);
var G__38656 = (0);
seq__38271_38556 = G__38653;
chunk__38275_38557 = G__38654;
count__38276_38558 = G__38655;
i__38277_38559 = G__38656;
continue;
} else {
var G__38657 = cljs.core.next(seq__38271_38610__$1);
var G__38658 = null;
var G__38659 = (0);
var G__38660 = (0);
seq__38271_38556 = G__38657;
chunk__38275_38557 = G__38658;
count__38276_38558 = G__38659;
i__38277_38559 = G__38660;
continue;
}
} else {
var G__38661 = cljs.core.next(seq__38271_38610__$1);
var G__38662 = null;
var G__38663 = (0);
var G__38664 = (0);
seq__38271_38556 = G__38661;
chunk__38275_38557 = G__38662;
count__38276_38558 = G__38663;
i__38277_38559 = G__38664;
continue;
}
}
} else {
}
}
break;
}


var G__38665 = seq__38059;
var G__38666 = chunk__38061;
var G__38667 = count__38062;
var G__38668 = (i__38063 + (1));
seq__38059 = G__38665;
chunk__38061 = G__38666;
count__38062 = G__38667;
i__38063 = G__38668;
continue;
} else {
var G__38669 = seq__38059;
var G__38670 = chunk__38061;
var G__38671 = count__38062;
var G__38672 = (i__38063 + (1));
seq__38059 = G__38669;
chunk__38061 = G__38670;
count__38062 = G__38671;
i__38063 = G__38672;
continue;
}
} else {
var temp__5823__auto__ = cljs.core.seq(seq__38059);
if(temp__5823__auto__){
var seq__38059__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__38059__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__38059__$1);
var G__38673 = cljs.core.chunk_rest(seq__38059__$1);
var G__38674 = c__5694__auto__;
var G__38675 = cljs.core.count(c__5694__auto__);
var G__38676 = (0);
seq__38059 = G__38673;
chunk__38061 = G__38674;
count__38062 = G__38675;
i__38063 = G__38676;
continue;
} else {
var path = cljs.core.first(seq__38059__$1);
if(clojure.string.ends_with_QMARK_(path,"css")){
var seq__38341_38677 = cljs.core.seq(cljs.core.array_seq.cljs$core$IFn$_invoke$arity$1(document.querySelectorAll("link[rel=\"stylesheet\"]")));
var chunk__38345_38678 = null;
var count__38346_38679 = (0);
var i__38347_38680 = (0);
while(true){
if((i__38347_38680 < count__38346_38679)){
var node_38681 = chunk__38345_38678.cljs$core$IIndexed$_nth$arity$2(null,i__38347_38680);
if(cljs.core.not(node_38681.shadow$old)){
var path_match_38682 = shadow.cljs.devtools.client.browser.match_paths(node_38681.getAttribute("href"),path);
if(cljs.core.truth_(path_match_38682)){
var new_link_38683 = (function (){var G__38384 = node_38681.cloneNode(true);
G__38384.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_38682)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__38384;
})();
(node_38681.shadow$old = true);

(new_link_38683.onload = ((function (seq__38341_38677,chunk__38345_38678,count__38346_38679,i__38347_38680,seq__38059,chunk__38061,count__38062,i__38063,new_link_38683,path_match_38682,node_38681,path,seq__38059__$1,temp__5823__auto__,map__38058,map__38058__$1,msg,updates,reload_info){
return (function (e){
var seq__38386_38684 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__38388_38685 = null;
var count__38389_38686 = (0);
var i__38390_38687 = (0);
while(true){
if((i__38390_38687 < count__38389_38686)){
var map__38395_38688 = chunk__38388_38685.cljs$core$IIndexed$_nth$arity$2(null,i__38390_38687);
var map__38395_38689__$1 = cljs.core.__destructure_map(map__38395_38688);
var task_38690 = map__38395_38689__$1;
var fn_str_38691 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38395_38689__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38692 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38395_38689__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38696 = goog.getObjectByName(fn_str_38691,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38692)));

(fn_obj_38696.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38696.cljs$core$IFn$_invoke$arity$2(path,new_link_38683) : fn_obj_38696.call(null,path,new_link_38683));


var G__38697 = seq__38386_38684;
var G__38698 = chunk__38388_38685;
var G__38699 = count__38389_38686;
var G__38700 = (i__38390_38687 + (1));
seq__38386_38684 = G__38697;
chunk__38388_38685 = G__38698;
count__38389_38686 = G__38699;
i__38390_38687 = G__38700;
continue;
} else {
var temp__5823__auto___38702__$1 = cljs.core.seq(seq__38386_38684);
if(temp__5823__auto___38702__$1){
var seq__38386_38703__$1 = temp__5823__auto___38702__$1;
if(cljs.core.chunked_seq_QMARK_(seq__38386_38703__$1)){
var c__5694__auto___38704 = cljs.core.chunk_first(seq__38386_38703__$1);
var G__38705 = cljs.core.chunk_rest(seq__38386_38703__$1);
var G__38706 = c__5694__auto___38704;
var G__38707 = cljs.core.count(c__5694__auto___38704);
var G__38708 = (0);
seq__38386_38684 = G__38705;
chunk__38388_38685 = G__38706;
count__38389_38686 = G__38707;
i__38390_38687 = G__38708;
continue;
} else {
var map__38396_38709 = cljs.core.first(seq__38386_38703__$1);
var map__38396_38710__$1 = cljs.core.__destructure_map(map__38396_38709);
var task_38711 = map__38396_38710__$1;
var fn_str_38712 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38396_38710__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38713 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38396_38710__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38714 = goog.getObjectByName(fn_str_38712,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38713)));

(fn_obj_38714.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38714.cljs$core$IFn$_invoke$arity$2(path,new_link_38683) : fn_obj_38714.call(null,path,new_link_38683));


var G__38715 = cljs.core.next(seq__38386_38703__$1);
var G__38716 = null;
var G__38717 = (0);
var G__38718 = (0);
seq__38386_38684 = G__38715;
chunk__38388_38685 = G__38716;
count__38389_38686 = G__38717;
i__38390_38687 = G__38718;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_38681);
});})(seq__38341_38677,chunk__38345_38678,count__38346_38679,i__38347_38680,seq__38059,chunk__38061,count__38062,i__38063,new_link_38683,path_match_38682,node_38681,path,seq__38059__$1,temp__5823__auto__,map__38058,map__38058__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_38682], 0));

goog.dom.insertSiblingAfter(new_link_38683,node_38681);


var G__38722 = seq__38341_38677;
var G__38723 = chunk__38345_38678;
var G__38724 = count__38346_38679;
var G__38725 = (i__38347_38680 + (1));
seq__38341_38677 = G__38722;
chunk__38345_38678 = G__38723;
count__38346_38679 = G__38724;
i__38347_38680 = G__38725;
continue;
} else {
var G__38726 = seq__38341_38677;
var G__38727 = chunk__38345_38678;
var G__38728 = count__38346_38679;
var G__38729 = (i__38347_38680 + (1));
seq__38341_38677 = G__38726;
chunk__38345_38678 = G__38727;
count__38346_38679 = G__38728;
i__38347_38680 = G__38729;
continue;
}
} else {
var G__38730 = seq__38341_38677;
var G__38731 = chunk__38345_38678;
var G__38732 = count__38346_38679;
var G__38733 = (i__38347_38680 + (1));
seq__38341_38677 = G__38730;
chunk__38345_38678 = G__38731;
count__38346_38679 = G__38732;
i__38347_38680 = G__38733;
continue;
}
} else {
var temp__5823__auto___38735__$1 = cljs.core.seq(seq__38341_38677);
if(temp__5823__auto___38735__$1){
var seq__38341_38736__$1 = temp__5823__auto___38735__$1;
if(cljs.core.chunked_seq_QMARK_(seq__38341_38736__$1)){
var c__5694__auto___38737 = cljs.core.chunk_first(seq__38341_38736__$1);
var G__38738 = cljs.core.chunk_rest(seq__38341_38736__$1);
var G__38739 = c__5694__auto___38737;
var G__38740 = cljs.core.count(c__5694__auto___38737);
var G__38741 = (0);
seq__38341_38677 = G__38738;
chunk__38345_38678 = G__38739;
count__38346_38679 = G__38740;
i__38347_38680 = G__38741;
continue;
} else {
var node_38742 = cljs.core.first(seq__38341_38736__$1);
if(cljs.core.not(node_38742.shadow$old)){
var path_match_38743 = shadow.cljs.devtools.client.browser.match_paths(node_38742.getAttribute("href"),path);
if(cljs.core.truth_(path_match_38743)){
var new_link_38744 = (function (){var G__38399 = node_38742.cloneNode(true);
G__38399.setAttribute("href",(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path_match_38743)+"?r="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.rand.cljs$core$IFn$_invoke$arity$0())));

return G__38399;
})();
(node_38742.shadow$old = true);

(new_link_38744.onload = ((function (seq__38341_38677,chunk__38345_38678,count__38346_38679,i__38347_38680,seq__38059,chunk__38061,count__38062,i__38063,new_link_38744,path_match_38743,node_38742,seq__38341_38736__$1,temp__5823__auto___38735__$1,path,seq__38059__$1,temp__5823__auto__,map__38058,map__38058__$1,msg,updates,reload_info){
return (function (e){
var seq__38404_38745 = cljs.core.seq(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(msg,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"reload-info","reload-info",1648088086),new cljs.core.Keyword(null,"asset-load","asset-load",-1925902322)], null)));
var chunk__38406_38746 = null;
var count__38407_38747 = (0);
var i__38408_38748 = (0);
while(true){
if((i__38408_38748 < count__38407_38747)){
var map__38426_38749 = chunk__38406_38746.cljs$core$IIndexed$_nth$arity$2(null,i__38408_38748);
var map__38426_38750__$1 = cljs.core.__destructure_map(map__38426_38749);
var task_38751 = map__38426_38750__$1;
var fn_str_38752 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38426_38750__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38753 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38426_38750__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38754 = goog.getObjectByName(fn_str_38752,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38753)));

(fn_obj_38754.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38754.cljs$core$IFn$_invoke$arity$2(path,new_link_38744) : fn_obj_38754.call(null,path,new_link_38744));


var G__38755 = seq__38404_38745;
var G__38756 = chunk__38406_38746;
var G__38757 = count__38407_38747;
var G__38758 = (i__38408_38748 + (1));
seq__38404_38745 = G__38755;
chunk__38406_38746 = G__38756;
count__38407_38747 = G__38757;
i__38408_38748 = G__38758;
continue;
} else {
var temp__5823__auto___38759__$2 = cljs.core.seq(seq__38404_38745);
if(temp__5823__auto___38759__$2){
var seq__38404_38760__$1 = temp__5823__auto___38759__$2;
if(cljs.core.chunked_seq_QMARK_(seq__38404_38760__$1)){
var c__5694__auto___38761 = cljs.core.chunk_first(seq__38404_38760__$1);
var G__38762 = cljs.core.chunk_rest(seq__38404_38760__$1);
var G__38763 = c__5694__auto___38761;
var G__38764 = cljs.core.count(c__5694__auto___38761);
var G__38765 = (0);
seq__38404_38745 = G__38762;
chunk__38406_38746 = G__38763;
count__38407_38747 = G__38764;
i__38408_38748 = G__38765;
continue;
} else {
var map__38427_38766 = cljs.core.first(seq__38404_38760__$1);
var map__38427_38767__$1 = cljs.core.__destructure_map(map__38427_38766);
var task_38768 = map__38427_38767__$1;
var fn_str_38769 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38427_38767__$1,new cljs.core.Keyword(null,"fn-str","fn-str",-1348506402));
var fn_sym_38770 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38427_38767__$1,new cljs.core.Keyword(null,"fn-sym","fn-sym",1423988510));
var fn_obj_38771 = goog.getObjectByName(fn_str_38769,$CLJS);
shadow.cljs.devtools.client.browser.devtools_msg((""+"call "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(fn_sym_38770)));

(fn_obj_38771.cljs$core$IFn$_invoke$arity$2 ? fn_obj_38771.cljs$core$IFn$_invoke$arity$2(path,new_link_38744) : fn_obj_38771.call(null,path,new_link_38744));


var G__38772 = cljs.core.next(seq__38404_38760__$1);
var G__38773 = null;
var G__38774 = (0);
var G__38775 = (0);
seq__38404_38745 = G__38772;
chunk__38406_38746 = G__38773;
count__38407_38747 = G__38774;
i__38408_38748 = G__38775;
continue;
}
} else {
}
}
break;
}

return goog.dom.removeNode(node_38742);
});})(seq__38341_38677,chunk__38345_38678,count__38346_38679,i__38347_38680,seq__38059,chunk__38061,count__38062,i__38063,new_link_38744,path_match_38743,node_38742,seq__38341_38736__$1,temp__5823__auto___38735__$1,path,seq__38059__$1,temp__5823__auto__,map__38058,map__38058__$1,msg,updates,reload_info))
);

shadow.cljs.devtools.client.browser.devtools_msg.cljs$core$IFn$_invoke$arity$variadic("load CSS",cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([path_match_38743], 0));

goog.dom.insertSiblingAfter(new_link_38744,node_38742);


var G__38777 = cljs.core.next(seq__38341_38736__$1);
var G__38778 = null;
var G__38779 = (0);
var G__38780 = (0);
seq__38341_38677 = G__38777;
chunk__38345_38678 = G__38778;
count__38346_38679 = G__38779;
i__38347_38680 = G__38780;
continue;
} else {
var G__38781 = cljs.core.next(seq__38341_38736__$1);
var G__38782 = null;
var G__38783 = (0);
var G__38784 = (0);
seq__38341_38677 = G__38781;
chunk__38345_38678 = G__38782;
count__38346_38679 = G__38783;
i__38347_38680 = G__38784;
continue;
}
} else {
var G__38785 = cljs.core.next(seq__38341_38736__$1);
var G__38786 = null;
var G__38787 = (0);
var G__38788 = (0);
seq__38341_38677 = G__38785;
chunk__38345_38678 = G__38786;
count__38346_38679 = G__38787;
i__38347_38680 = G__38788;
continue;
}
}
} else {
}
}
break;
}


var G__38789 = cljs.core.next(seq__38059__$1);
var G__38790 = null;
var G__38791 = (0);
var G__38792 = (0);
seq__38059 = G__38789;
chunk__38061 = G__38790;
count__38062 = G__38791;
i__38063 = G__38792;
continue;
} else {
var G__38793 = cljs.core.next(seq__38059__$1);
var G__38794 = null;
var G__38795 = (0);
var G__38796 = (0);
seq__38059 = G__38793;
chunk__38061 = G__38794;
count__38062 = G__38795;
i__38063 = G__38796;
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
shadow.cljs.devtools.client.browser.global_eval = (function shadow$cljs$devtools$client$browser$global_eval(js){
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2("undefined",typeof(module))){
return eval(js);
} else {
return (0,eval)(js);;
}
});
shadow.cljs.devtools.client.browser.runtime_info = (((typeof SHADOW_CONFIG !== 'undefined'))?shadow.json.to_clj.cljs$core$IFn$_invoke$arity$1(SHADOW_CONFIG):null);
shadow.cljs.devtools.client.browser.client_info = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([shadow.cljs.devtools.client.browser.runtime_info,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"host","host",-1558485167),(cljs.core.truth_(goog.global.document)?new cljs.core.Keyword(null,"browser","browser",828191719):new cljs.core.Keyword(null,"browser-worker","browser-worker",1638998282)),new cljs.core.Keyword(null,"user-agent","user-agent",1220426212),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(goog.userAgent.OPERA)?"Opera":(cljs.core.truth_(goog.userAgent.product.CHROME)?"Chrome":(cljs.core.truth_(goog.userAgent.IE)?"MSIE":(cljs.core.truth_(goog.userAgent.EDGE)?"Edge":(cljs.core.truth_(goog.userAgent.GECKO)?"Firefox":(cljs.core.truth_(goog.userAgent.SAFARI)?"Safari":(cljs.core.truth_(goog.userAgent.WEBKIT)?"Webkit":null))))))))+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(goog.userAgent.VERSION)+" ["+cljs.core.str.cljs$core$IFn$_invoke$arity$1(goog.userAgent.PLATFORM)+"]"),new cljs.core.Keyword(null,"dom","dom",-1236537922),(!((goog.global.document == null)))], null)], 0));
if((typeof shadow !== 'undefined') && (typeof shadow.cljs !== 'undefined') && (typeof shadow.cljs.devtools !== 'undefined') && (typeof shadow.cljs.devtools.client !== 'undefined') && (typeof shadow.cljs.devtools.client.browser !== 'undefined') && (typeof shadow.cljs.devtools.client.browser.ws_was_welcome_ref !== 'undefined')){
} else {
shadow.cljs.devtools.client.browser.ws_was_welcome_ref = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false);
}
if(((shadow.cljs.devtools.client.env.enabled) && ((shadow.cljs.devtools.client.env.worker_client_id > (0))))){
(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$remote$runtime$api$IEvalJS$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$remote$runtime$api$IEvalJS$_js_eval$arity$4 = (function (this$,code,success,fail){
var this$__$1 = this;
try{var G__38440 = shadow.cljs.devtools.client.browser.global_eval(code);
return (success.cljs$core$IFn$_invoke$arity$1 ? success.cljs$core$IFn$_invoke$arity$1(G__38440) : success.call(null,G__38440));
}catch (e38439){var e = e38439;
return (fail.cljs$core$IFn$_invoke$arity$1 ? fail.cljs$core$IFn$_invoke$arity$1(e) : fail.call(null,e));
}}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_invoke$arity$5 = (function (this$,ns,p__38443,success,fail){
var map__38444 = p__38443;
var map__38444__$1 = cljs.core.__destructure_map(map__38444);
var js = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38444__$1,new cljs.core.Keyword(null,"js","js",1768080579));
var this$__$1 = this;
try{var G__38446 = shadow.cljs.devtools.client.browser.global_eval(js);
return (success.cljs$core$IFn$_invoke$arity$1 ? success.cljs$core$IFn$_invoke$arity$1(G__38446) : success.call(null,G__38446));
}catch (e38445){var e = e38445;
return (fail.cljs$core$IFn$_invoke$arity$1 ? fail.cljs$core$IFn$_invoke$arity$1(e) : fail.call(null,e));
}}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_repl_init$arity$4 = (function (runtime,p__38447,done,error){
var map__38448 = p__38447;
var map__38448__$1 = cljs.core.__destructure_map(map__38448);
var repl_sources = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38448__$1,new cljs.core.Keyword(null,"repl-sources","repl-sources",723867535));
var runtime__$1 = this;
return shadow.cljs.devtools.client.shared.load_sources(runtime__$1,cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2(shadow.cljs.devtools.client.env.src_is_loaded_QMARK_,repl_sources)),(function (sources){
shadow.cljs.devtools.client.browser.do_js_load(sources);

return (done.cljs$core$IFn$_invoke$arity$0 ? done.cljs$core$IFn$_invoke$arity$0() : done.call(null));
}));
}));

(shadow.cljs.devtools.client.shared.Runtime.prototype.shadow$cljs$devtools$client$shared$IHostSpecific$do_repl_require$arity$4 = (function (runtime,p__38449,done,error){
var map__38450 = p__38449;
var map__38450__$1 = cljs.core.__destructure_map(map__38450);
var msg = map__38450__$1;
var sources = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38450__$1,new cljs.core.Keyword(null,"sources","sources",-321166424));
var reload_namespaces = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38450__$1,new cljs.core.Keyword(null,"reload-namespaces","reload-namespaces",250210134));
var js_requires = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38450__$1,new cljs.core.Keyword(null,"js-requires","js-requires",-1311472051));
var runtime__$1 = this;
var sources_to_load = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentVector.EMPTY,cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p__38452){
var map__38453 = p__38452;
var map__38453__$1 = cljs.core.__destructure_map(map__38453);
var src = map__38453__$1;
var provides = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38453__$1,new cljs.core.Keyword(null,"provides","provides",-1634397992));
var and__5160__auto__ = shadow.cljs.devtools.client.env.src_is_loaded_QMARK_(src);
if(cljs.core.truth_(and__5160__auto__)){
return cljs.core.not(cljs.core.some(reload_namespaces,provides));
} else {
return and__5160__auto__;
}
}),sources));
if(cljs.core.not(cljs.core.seq(sources_to_load))){
var G__38457 = cljs.core.PersistentVector.EMPTY;
return (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(G__38457) : done.call(null,G__38457));
} else {
return shadow.remote.runtime.shared.call.cljs$core$IFn$_invoke$arity$3(runtime__$1,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"op","op",-1882987955),new cljs.core.Keyword(null,"cljs-load-sources","cljs-load-sources",-1458295962),new cljs.core.Keyword(null,"to","to",192099007),shadow.cljs.devtools.client.env.worker_client_id,new cljs.core.Keyword(null,"sources","sources",-321166424),cljs.core.into.cljs$core$IFn$_invoke$arity$3(cljs.core.PersistentVector.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"resource-id","resource-id",-1308422582)),sources_to_load)], null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"cljs-sources","cljs-sources",31121610),(function (p__38458){
var map__38459 = p__38458;
var map__38459__$1 = cljs.core.__destructure_map(map__38459);
var msg__$1 = map__38459__$1;
var sources__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38459__$1,new cljs.core.Keyword(null,"sources","sources",-321166424));
try{shadow.cljs.devtools.client.browser.do_js_load(sources__$1);

if(cljs.core.seq(js_requires)){
shadow.cljs.devtools.client.browser.do_js_requires(js_requires);
} else {
}

return (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(sources_to_load) : done.call(null,sources_to_load));
}catch (e38461){var ex = e38461;
return (error.cljs$core$IFn$_invoke$arity$1 ? error.cljs$core$IFn$_invoke$arity$1(ex) : error.call(null,ex));
}})], null));
}
}));

shadow.cljs.devtools.client.shared.add_plugin_BANG_(new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282),cljs.core.PersistentHashSet.EMPTY,(function (p__38469){
var map__38470 = p__38469;
var map__38470__$1 = cljs.core.__destructure_map(map__38470);
var env = map__38470__$1;
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38470__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
var svc = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"runtime","runtime",-1331573996),runtime], null);
shadow.remote.runtime.api.add_extension(runtime,new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282),new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"on-welcome","on-welcome",1895317125),(function (){
cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,true);

shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

shadow.cljs.devtools.client.env.patch_goog_BANG_();

return shadow.cljs.devtools.client.browser.devtools_msg((""+"#"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"client-id","client-id",-464622140).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(new cljs.core.Keyword(null,"state-ref","state-ref",2127874952).cljs$core$IFn$_invoke$arity$1(runtime))))+" ready!"));
}),new cljs.core.Keyword(null,"on-disconnect","on-disconnect",-809021814),(function (e){
if(cljs.core.truth_(cljs.core.deref(shadow.cljs.devtools.client.browser.ws_was_welcome_ref))){
shadow.cljs.devtools.client.hud.connection_error("The Websocket connection was closed!");

return cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,false);
} else {
return null;
}
}),new cljs.core.Keyword(null,"on-reconnect","on-reconnect",1239988702),(function (e){
return shadow.cljs.devtools.client.hud.connection_error("Reconnecting ...");
}),new cljs.core.Keyword(null,"ops","ops",1237330063),new cljs.core.PersistentArrayMap(null, 7, [new cljs.core.Keyword(null,"access-denied","access-denied",959449406),(function (msg){
cljs.core.reset_BANG_(shadow.cljs.devtools.client.browser.ws_was_welcome_ref,false);

return shadow.cljs.devtools.client.hud.connection_error((""+"Stale Output! Your loaded JS was not produced by the running shadow-cljs instance."+" Is the watch for this build running?"));
}),new cljs.core.Keyword(null,"cljs-asset-update","cljs-asset-update",1224093028),(function (msg){
return shadow.cljs.devtools.client.browser.handle_asset_update(msg);
}),new cljs.core.Keyword(null,"cljs-build-configure","cljs-build-configure",-2089891268),(function (msg){
return null;
}),new cljs.core.Keyword(null,"cljs-build-start","cljs-build-start",-725781241),(function (msg){
shadow.cljs.devtools.client.hud.hud_hide();

shadow.cljs.devtools.client.hud.load_start();

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-start","build-start",-959649480)));
}),new cljs.core.Keyword(null,"cljs-build-complete","cljs-build-complete",273626153),(function (msg){
var msg__$1 = shadow.cljs.devtools.client.env.add_warnings_to_info(msg);
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

shadow.cljs.devtools.client.hud.hud_warnings(msg__$1);

shadow.cljs.devtools.client.browser.handle_build_complete(runtime,msg__$1);

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg__$1,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-complete","build-complete",-501868472)));
}),new cljs.core.Keyword(null,"cljs-build-failure","cljs-build-failure",1718154990),(function (msg){
shadow.cljs.devtools.client.hud.load_end();

shadow.cljs.devtools.client.hud.hud_error(msg);

return shadow.cljs.devtools.client.env.run_custom_notify_BANG_(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(msg,new cljs.core.Keyword(null,"type","type",1174270348),new cljs.core.Keyword(null,"build-failure","build-failure",-2107487466)));
}),new cljs.core.Keyword("shadow.cljs.devtools.client.env","worker-notify","shadow.cljs.devtools.client.env/worker-notify",-1456820670),(function (p__38471){
var map__38472 = p__38471;
var map__38472__$1 = cljs.core.__destructure_map(map__38472);
var event_op = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38472__$1,new cljs.core.Keyword(null,"event-op","event-op",200358057));
var client_id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38472__$1,new cljs.core.Keyword(null,"client-id","client-id",-464622140));
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-disconnect","client-disconnect",640227957),event_op)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(client_id,shadow.cljs.devtools.client.env.worker_client_id)))){
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

return shadow.cljs.devtools.client.hud.connection_error("The watch for this build was stopped!");
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"client-connect","client-connect",-1113973888),event_op)){
shadow.cljs.devtools.client.hud.connection_error_clear_BANG_();

return shadow.cljs.devtools.client.hud.connection_error("The watch for this build was restarted. Reload required!");
} else {
return null;
}
}
})], null)], null));

return svc;
}),(function (p__38473){
var map__38474 = p__38473;
var map__38474__$1 = cljs.core.__destructure_map(map__38474);
var svc = map__38474__$1;
var runtime = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38474__$1,new cljs.core.Keyword(null,"runtime","runtime",-1331573996));
return shadow.remote.runtime.api.del_extension(runtime,new cljs.core.Keyword("shadow.cljs.devtools.client.browser","client","shadow.cljs.devtools.client.browser/client",-1461019282));
}));

shadow.cljs.devtools.client.shared.init_runtime_BANG_(shadow.cljs.devtools.client.browser.client_info,shadow.cljs.devtools.client.websocket.start,shadow.cljs.devtools.client.websocket.send,shadow.cljs.devtools.client.websocket.stop);
} else {
}

//# sourceMappingURL=shadow.cljs.devtools.client.browser.js.map
