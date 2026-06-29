goog.provide('replicant.core');
replicant.core.parse_tag = (function replicant$core$parse_tag(tag){
var ns = cljs.core.namespace(tag);
var tag__$1 = cljs.core.name(tag);
var id_index = (function (){var index = tag__$1.indexOf("#");
if((index > (0))){
return index;
} else {
return null;
}
})();
var class_index = (function (){var index = tag__$1.indexOf(".");
if((index > (0))){
return index;
} else {
return null;
}
})();
var tag_name = (function (){var G__38021 = (cljs.core.truth_(id_index)?tag__$1.substring((0),id_index):(cljs.core.truth_(class_index)?tag__$1.substring((0),class_index):tag__$1
));
if(cljs.core.truth_(ns)){
return cljs.core.keyword.cljs$core$IFn$_invoke$arity$2(ns,G__38021);
} else {
return G__38021;
}
})();
var id = (cljs.core.truth_(id_index)?(cljs.core.truth_(class_index)?tag__$1.substring((id_index + (1)),class_index):tag__$1.substring((id_index + (1)))):null);
var classes = (cljs.core.truth_(class_index)?cljs.core.seq(tag__$1.substring((class_index + (1))).split(".")):null);
return [tag_name,id,classes];
});
/**
 * Hiccup symbols can include tag name, id and classes. The argument map is
 *   optional. This function finds the important bits of the hiccup data structure
 *   and returns a "headers" tuple with a stable position for:
 * 
 *   - tag-name
 *   - id from the hiccup symbol
 *   - classes from the hiccup symbol
 *   - key
 *   - attributes
 *   - children
 *   - namespace
 *   - original s-expression
 * 
 *   Attributes and children are completely untouched. Headers can be used to
 *   quickly determine tag name and key, or sent to `get-attrs` and
 *   `get-children` for usable information about those things.
 * 
 *   Returns a tuple (instead of a map) for speed.
 * 
 *   - `sexp` is the hiccup to parse
 * 
 *   - `ns` is the namespace of the elements, used for SVG elements. The SVG
 *   element has an explicit namespace, which needs to be set on all of its
 *   children, so they can all be created with createElementNS etc.
 */
replicant.core.get_hiccup_headers = (function replicant$core$get_hiccup_headers(ns,sexp){
if(cljs.core.truth_(sexp)){
if(replicant.hiccup.hiccup_QMARK_(sexp)){
var sym = cljs.core.first(sexp);
var args = cljs.core.rest(sexp);
var has_args_QMARK_ = cljs.core.map_QMARK_(cljs.core.first(args));
var attrs = ((has_args_QMARK_)?cljs.core.first(args):cljs.core.PersistentArrayMap.EMPTY);
if((!(cljs.core.not(cljs.core.re_find(/#($|\.)/,(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sym))))))){
var fn__37317__auto___38845 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38846 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38847 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38041 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = sexp;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Hiccup tag "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sym)+" contains an empty id"),new cljs.core.Keyword(null,"message","message",-406056002),"Either complete the id or remove the # character."], null);
var G__38041__$1 = (cljs.core.truth_(fn__37317__auto___38845)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38041,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38845):G__38041);
var G__38041__$2 = (cljs.core.truth_(alias__37318__auto___38846)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38041__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38846):G__38041__$1);
if(cljs.core.truth_(fd__37319__auto___38847)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38041__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38847);
} else {
return G__38041__$2;
}
})());
} else {
}

if((!(cljs.core.not(cljs.core.re_find(/#[^a-zA-Z_\.]/,(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sym))))))){
var fn__37317__auto___38848 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38849 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38850 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38049 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = sexp;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Hiccup tag "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sym)+" contains an invalid id"),new cljs.core.Keyword(null,"message","message",-406056002),"IDs must start with a letter."], null);
var G__38049__$1 = (cljs.core.truth_(fn__37317__auto___38848)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38049,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38848):G__38049);
var G__38049__$2 = (cljs.core.truth_(alias__37318__auto___38849)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38049__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38849):G__38049__$1);
if(cljs.core.truth_(fd__37319__auto___38850)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38049__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38850);
} else {
return G__38049__$2;
}
})());
} else {
}

if((!(cljs.core.not(cljs.core.re_find(/\.$/,(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sym))))))){
var fn__37317__auto___38851 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38852 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38853 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38054 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = sexp;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Hiccup tag "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sym)+" contains an empty class"),new cljs.core.Keyword(null,"message","message",-406056002),"This may cause a DOMException and is considered a coding error. Replicant will not sacrifice performance to work around it."], null);
var G__38054__$1 = (cljs.core.truth_(fn__37317__auto___38851)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38054,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38851):G__38054);
var G__38054__$2 = (cljs.core.truth_(alias__37318__auto___38852)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38054__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38852):G__38054__$1);
if(cljs.core.truth_(fd__37319__auto___38853)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38054__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38853);
} else {
return G__38054__$2;
}
})());
} else {
}

var pt__36985__auto__ = replicant.core.parse_tag(sym);
var G__38055 = pt__36985__auto__;
G__38055.push((function (){var temp__5823__auto__ = new cljs.core.Keyword("replicant","key","replicant/key",-670108117).cljs$core$IFn$_invoke$arity$1(attrs);
if(cljs.core.truth_(temp__5823__auto__)){
var k__36970__auto__ = temp__5823__auto__;
return replicant.hiccup_headers.make_key((pt__36985__auto__[(0)]),k__36970__auto__);
} else {
return null;
}
})());

G__38055.push(attrs);

G__38055.push(((has_args_QMARK_)?cljs.core.rest(args):args));

G__38055.push(ns);

G__38055.push(sexp);

G__38055.push(null);

G__38055.push(null);

return G__38055;
} else {
var text__37010__auto__ = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sexp));
return (new Array(null,null,null,null,null,null,null,text__37010__auto__,text__37010__auto__,null));
}
} else {
return null;
}
});
replicant.core.get_classes = (function replicant$core$get_classes(classes){
if((classes instanceof cljs.core.Keyword)){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.name(classes)], null);
} else {
if((classes instanceof cljs.core.Symbol)){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.name(classes)], null);
} else {
if(cljs.core.empty_QMARK_(classes)){
return cljs.core.PersistentVector.EMPTY;
} else {
if(cljs.core.coll_QMARK_(classes)){
return cljs.core.keep.cljs$core$IFn$_invoke$arity$2((function (class$){
if(cljs.core.truth_(class$)){
if((class$ instanceof cljs.core.Keyword)){
return cljs.core.name(class$);
} else {
if((class$ instanceof cljs.core.Symbol)){
return cljs.core.name(class$);
} else {
if(typeof class$ === 'string'){
return cljs.core.not_empty(class$.trim());
} else {
return null;
}
}
}
} else {
return null;
}
}),classes);
} else {
if(typeof classes === 'string'){
return cljs.core.keep.cljs$core$IFn$_invoke$arity$2((function (p1__38082_SHARP_){
return cljs.core.not_empty(p1__38082_SHARP_.trim());
}),classes.split(" "));
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("class name is neither string, keyword, or a collection of those",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"classes","classes",2037804510),classes], null));

}
}
}
}
}
});
replicant.core.skip_pixelize_attrs = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 26, [new cljs.core.Keyword(null,"line-height","line-height",1870784992),null,new cljs.core.Keyword(null,"box-flex-group","box-flex-group",-1468550335),null,new cljs.core.Keyword(null,"zoom","zoom",-1827487038),null,new cljs.core.Keyword(null,"column-count","column-count",1235131236),null,new cljs.core.Keyword(null,"flex-negative","flex-negative",-1543996476),null,new cljs.core.Keyword(null,"tab-size","tab-size",-1265790523),null,new cljs.core.Keyword(null,"font-weight","font-weight",2085804583),null,new cljs.core.Keyword(null,"stroke-opacity","stroke-opacity",-1191543159),null,new cljs.core.Keyword(null,"flex-order","flex-order",-621251126),null,new cljs.core.Keyword(null,"flex-grow","flex-grow",1865160747),null,new cljs.core.Keyword(null,"stroke-dashoffset","stroke-dashoffset",-782320340),null,new cljs.core.Keyword(null,"flex","flex",-1425124628),null,new cljs.core.Keyword(null,"flex-shrink","flex-shrink",1481146383),null,new cljs.core.Keyword(null,"stop-opacity","stop-opacity",-2018003729),null,new cljs.core.Keyword(null,"orphans","orphans",1913357231),null,new cljs.core.Keyword(null,"widows","widows",1989591025),null,new cljs.core.Keyword(null,"z-index","z-index",1892827090),null,new cljs.core.Keyword(null,"stroke-width","stroke-width",716836435),null,new cljs.core.Keyword(null,"opacity","opacity",397153780),null,new cljs.core.Keyword(null,"box-ordinal-group","box-ordinal-group",201694774),null,new cljs.core.Keyword(null,"order","order",-1254677256),null,new cljs.core.Keyword(null,"animation-iteration-count","animation-iteration-count",-1410888227),null,new cljs.core.Keyword(null,"line-clamp","line-clamp",-1079334403),null,new cljs.core.Keyword(null,"fill-opacity","fill-opacity",-537571170),null,new cljs.core.Keyword(null,"flex-positive","flex-positive",-239614242),null,new cljs.core.Keyword(null,"box-flex","box-flex",-831196194),null], null), null);
/**
 * Converts string values for the style attribute to a map of keyword keys and
 *   string values.
 */
replicant.core.explode_styles = (function replicant$core$explode_styles(s){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (kv){
var vec__38089 = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__38085_SHARP_){
return p1__38085_SHARP_.trim();
}),kv.split(":"));
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38089,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38089,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(k),v], null);
}),s.split(";")));
});
replicant.core.get_style_val = (function replicant$core$get_style_val(attr,v){
if(typeof v === 'number'){
if(cljs.core.truth_((replicant.core.skip_pixelize_attrs.cljs$core$IFn$_invoke$arity$1 ? replicant.core.skip_pixelize_attrs.cljs$core$IFn$_invoke$arity$1(attr) : replicant.core.skip_pixelize_attrs.call(null,attr)))){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v));
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)+"px");
}
} else {
if((v instanceof cljs.core.Keyword)){
return cljs.core.name(v);
} else {
return v;

}
}
});
replicant.core.prep_attrs = (function replicant$core$prep_attrs(attrs,id,classes){
var classes__$1 = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(replicant.core.get_classes(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(attrs)),classes);
var G__38099 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(attrs,new cljs.core.Keyword(null,"class","class",-2030961996),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword("replicant","mounting","replicant/mounting",-699756499),new cljs.core.Keyword("replicant","unmounting","replicant/unmounting",1629433009)], 0));
var G__38099__$1 = (cljs.core.truth_(id)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38099,new cljs.core.Keyword(null,"id","id",-1388402092),id):G__38099);
var G__38099__$2 = ((cljs.core.seq(classes__$1))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38099__$1,new cljs.core.Keyword(null,"classes","classes",2037804510),classes__$1):G__38099__$1);
if(typeof new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1(attrs) === 'string'){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(G__38099__$2,new cljs.core.Keyword(null,"style","style",-496642736),replicant.core.explode_styles);
} else {
return G__38099__$2;
}
});
/**
 * Given `headers` as produced by `get-hiccup-headers`, returns a map of all HTML
 *   attributes.
 */
replicant.core.get_attrs = (function replicant$core$get_attrs(headers){
if((!((!(cljs.core.contains_QMARK_((headers[(4)]),new cljs.core.Keyword(null,"className","className",-1983287057))))))){
var fn__37317__auto___38860 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38861 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38862 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38106 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = (headers[(7)]);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),"Use :class, not :className",new cljs.core.Keyword(null,"message","message",-406056002),":className is not supported, please use :class instead. It takes a keyword, a string, or a collection of either of those."], null);
var G__38106__$1 = (cljs.core.truth_(fn__37317__auto___38860)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38106,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38860):G__38106);
var G__38106__$2 = (cljs.core.truth_(alias__37318__auto___38861)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38106__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38861):G__38106__$1);
if(cljs.core.truth_(fd__37319__auto___38862)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38106__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38862);
} else {
return G__38106__$2;
}
})());
} else {
}

if((!((function (){var class__37803__auto__ = new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1((headers[(4)]));
return (((!(typeof class__37803__auto__ === 'string'))) || ((class__37803__auto__.indexOf(" ") < (0))));
})()))){
var fn__37317__auto___38863 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38864 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38865 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38108 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = (headers[(7)]);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),"Avoid space separated :class strings",new cljs.core.Keyword(null,"message","message",-406056002),(function (){var class__37803__auto__ = new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1((headers[(4)]));
return (""+":class supports collections of keywords and/or strings as classes. These perform better, and are usually more convenient to work with. Solve by converting "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([class__37803__auto__], 0)))+" to "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.vec(class__37803__auto__.split(" "))], 0))));
})()], null);
var G__38108__$1 = (cljs.core.truth_(fn__37317__auto___38863)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38108,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38863):G__38108);
var G__38108__$2 = (cljs.core.truth_(alias__37318__auto___38864)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38108__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38864):G__38108__$1);
if(cljs.core.truth_(fd__37319__auto___38865)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38108__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38865);
} else {
return G__38108__$2;
}
})());
} else {
}

if((!((!(typeof new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1((headers[(4)])) === 'string'))))){
var fn__37317__auto___38866 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38867 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38868 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38113 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = (headers[(7)]);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),"Avoid string styles",new cljs.core.Keyword(null,"message","message",-406056002),":style supports structured maps of CSS property/value pairs. Strings must be parsed, so they're both slower and harder to read and write."], null);
var G__38113__$1 = (cljs.core.truth_(fn__37317__auto___38866)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38113,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38866):G__38113);
var G__38113__$2 = (cljs.core.truth_(alias__37318__auto___38867)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38113__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38867):G__38113__$1);
if(cljs.core.truth_(fd__37319__auto___38868)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38113__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38868);
} else {
return G__38113__$2;
}
})());
} else {
}

return replicant.core.prep_attrs((headers[(4)]),(headers[(1)]),(headers[(2)]));
});
replicant.core.merge_attrs = (function replicant$core$merge_attrs(attrs,overrides){
var G__38115 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([attrs,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(overrides,new cljs.core.Keyword(null,"style","style",-496642736))], 0));
if(cljs.core.truth_((function (){var or__5162__auto__ = new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1(attrs);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1(overrides);
}
})())){
return cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__38115,new cljs.core.Keyword(null,"style","style",-496642736),cljs.core.merge,new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1(overrides));
} else {
return G__38115;
}
});
replicant.core.get_mounting_attrs = (function replicant$core$get_mounting_attrs(headers){
var temp__5821__auto__ = new cljs.core.Keyword("replicant","mounting","replicant/mounting",-699756499).cljs$core$IFn$_invoke$arity$1((headers[(4)]));
if(cljs.core.truth_(temp__5821__auto__)){
var mounting = temp__5821__auto__;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [replicant.core.get_attrs(headers),(function (){var headers__$1 = (function (){var G__38117 = headers;
if(cljs.core.truth_(mounting)){
var headers__37024__auto__ = G__38117;
(headers__37024__auto__[(4)] = replicant.core.merge_attrs((headers__37024__auto__[(4)]),mounting));

return headers__37024__auto__;
} else {
return G__38117;
}
})();
return replicant.core.prep_attrs((headers__$1[(4)]),(headers__$1[(1)]),(headers__$1[(2)]));
})()], null);
} else {
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [replicant.core.get_attrs(headers)], null);
}
});
replicant.core.get_unmounting_attrs = (function replicant$core$get_unmounting_attrs(vdom){
if(cljs.core.truth_((vdom[(6)]))){
return replicant.core.prep_attrs(replicant.core.merge_attrs((vdom[(3)]),new cljs.core.Keyword("replicant","unmounting","replicant/unmounting",1629433009).cljs$core$IFn$_invoke$arity$1(cljs.core.nth.cljs$core$IFn$_invoke$arity$2((vdom[(7)]),(1)))),null,(vdom[(2)]));
} else {
return null;
}
});
replicant.core.seq_tag = null;
/**
 * Tag a built child collection as a flattenable seq. nil when empty, matching
 *   clojure.core/seq.
 */
replicant.core.__GT_seq = (function replicant$core$__GT_seq(xs){
return cljs.core.seq(xs);
});
/**
 * Like clojure.core/seq?. In squint vectors and strings are seq?, so narrow to
 *   genuine sequences or marked child seqs. A vector whose head is not a tag
 *   (keyword) is a sequence of nodes, not a hiccup node, so it is treated as a
 *   seq. This catches user-built seqs such as (rest some-vector), which squint
 *   returns as a vector.
 */
replicant.core.proper_seq_QMARK_ = (function replicant$core$proper_seq_QMARK_(x){
return cljs.core.seq_QMARK_(x);
});
replicant.core.flatten_seqs_STAR_ = (function replicant$core$flatten_seqs_STAR_(xs,coll){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (_,x){
if(replicant.core.proper_seq_QMARK_(x)){
return (replicant.core.flatten_seqs_STAR_.cljs$core$IFn$_invoke$arity$2 ? replicant.core.flatten_seqs_STAR_.cljs$core$IFn$_invoke$arity$2(x,coll) : replicant.core.flatten_seqs_STAR_.call(null,x,coll));
} else {
return cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(coll,x);

}
}),null,xs);
});
replicant.core.flatten_seqs = (function replicant$core$flatten_seqs(xs){
var coll = cljs.core.transient$(cljs.core.PersistentVector.EMPTY);
replicant.core.flatten_seqs_STAR_(xs,coll);

return cljs.core.persistent_BANG_(coll);
});
replicant.core.flatten_map_seqs_STAR_ = (function replicant$core$flatten_map_seqs_STAR_(f,xs,coll){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (_,x){
if(replicant.core.proper_seq_QMARK_(x)){
return (replicant.core.flatten_map_seqs_STAR_.cljs$core$IFn$_invoke$arity$3 ? replicant.core.flatten_map_seqs_STAR_.cljs$core$IFn$_invoke$arity$3(f,x,coll) : replicant.core.flatten_map_seqs_STAR_.call(null,f,x,coll));
} else {
return cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(coll,(f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(x) : f.call(null,x)));

}
}),null,xs);
});
replicant.core.flatten_map_seqs = (function replicant$core$flatten_map_seqs(f,xs){
var coll = cljs.core.transient$(cljs.core.PersistentVector.EMPTY);
replicant.core.flatten_map_seqs_STAR_(f,xs,coll);

return cljs.core.persistent_BANG_(coll);
});
/**
 * Given an optional tag namespace `ns` (e.g. for SVG nodes) and `headers`, as
 *   produced by `get-hiccup-headers`, returns a flat collection of children as
 *   "hiccup headers". Children will carry the `ns`, if any.
 */
replicant.core.get_children = (function replicant$core$get_children(headers,ns){
if(cljs.core.truth_(new cljs.core.Keyword(null,"innerHTML","innerHTML",-1856751343).cljs$core$IFn$_invoke$arity$1((headers[(4)])))){
return null;
} else {
return replicant.core.flatten_map_seqs((function (p1__38135_SHARP_){
var G__38136 = p1__38135_SHARP_;
if((G__38136 == null)){
return null;
} else {
return replicant.core.get_hiccup_headers(ns,G__38136);
}
}),(headers[(5)]));
}
});
/**
 * Like `get-children` but returns a tuple of `[children ks]` where `ks` is a set
 *   of the keys in `children`.
 */
replicant.core.get_children_ks = (function replicant$core$get_children_ks(headers,ns){
var vec__38139 = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p__38142,hiccup){
var vec__38144 = p__38142;
var children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38144,(0),null);
var ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38144,(1),null);
if(cljs.core.truth_(hiccup)){
var headers__$1 = replicant.core.get_hiccup_headers(ns,hiccup);
var k = (headers__$1[(3)]);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(children,headers__$1),(function (){var G__38147 = ks;
if(cljs.core.truth_(k)){
return cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(G__38147,k);
} else {
return G__38147;
}
})()], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(children,null),ks], null);
}
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.transient$(cljs.core.PersistentVector.EMPTY),cljs.core.transient$(cljs.core.PersistentHashSet.EMPTY)], null),replicant.core.flatten_seqs((headers[(5)])));
var children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38139,(0),null);
var ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38139,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.persistent_BANG_(children),cljs.core.persistent_BANG_(ks)], null);
});
replicant.core._STAR_dispatch_STAR_ = null;
replicant.core.build_event_map = (function replicant$core$build_event_map(e){
var node = e.target;
var G__38150 = new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("replicant","trigger","replicant/trigger",543650841),new cljs.core.Keyword("replicant.trigger","dom-event","replicant.trigger/dom-event",930136738),new cljs.core.Keyword("replicant","dom-event","replicant/dom-event",-1613182512),e], null);
var G__38150__$1 = (cljs.core.truth_(node)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38150,new cljs.core.Keyword("replicant","node","replicant/node",1306451380),node):G__38150);
if(cljs.core.ifn_QMARK_(replicant.core._STAR_dispatch_STAR_)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38150__$1,new cljs.core.Keyword("replicant","dispatch","replicant/dispatch",2079272115),replicant.core._STAR_dispatch_STAR_);
} else {
return G__38150__$1;
}
});
/**
 * Returns the function to use for handling DOM events. Uses `handler` directly
 *   when it's a function or a string (assumed to be inline JavaScript, not really
 *   recommended), or a wrapper that dispatches through
 *   `replicant.core/*dispatch*`, if it is bound to a function. 
 */
replicant.core.get_event_handler = (function replicant$core$get_event_handler(handler,event,options){
var or__5162__auto__ = ((((cljs.core.fn_QMARK_(handler)) || (((cljs.core.var_QMARK_(handler)) && (cljs.core.fn_QMARK_(cljs.core.deref(handler)))))))?(cljs.core.truth_(new cljs.core.Keyword("replicant.event","wrap-handler?","replicant.event/wrap-handler?",845655928).cljs$core$IFn$_invoke$arity$1(options))?(function (e){
var G__38166 = replicant.core.build_event_map(e);
return (handler.cljs$core$IFn$_invoke$arity$1 ? handler.cljs$core$IFn$_invoke$arity$1(G__38166) : handler.call(null,G__38166));
}):handler):null);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
var or__5162__auto____$1 = ((cljs.core.ifn_QMARK_(replicant.core._STAR_dispatch_STAR_))?(function (e){
return replicant.core._STAR_dispatch_STAR_.call(null,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(replicant.core.build_event_map(e),new cljs.core.Keyword("replicant","js-event","replicant/js-event",1927942146),e),handler);
}):null);
if(cljs.core.truth_(or__5162__auto____$1)){
return or__5162__auto____$1;
} else {
var or__5162__auto____$2 = ((typeof handler === 'string')?handler:null);
if(cljs.core.truth_(or__5162__auto____$2)){
return or__5162__auto____$2;
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("Cannot use non-function event handler when replicant.core/*dispatch* is not bound to a function",new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"event","event",301435442),event,new cljs.core.Keyword(null,"handler","handler",-195596612),handler,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),replicant.core._STAR_dispatch_STAR_], null));
}
}
}
});
/**
 * Returns the function to use to dispatch life-cycle hooks on an element. Uses
 *   `handler` directly when it's a function, or a wrapper that dispatches through
 *   `replicant.core/*dispatch*`, if it is bound to a function.
 */
replicant.core.get_life_cycle_hook = (function replicant$core$get_life_cycle_hook(handler){
var or__5162__auto__ = ((cljs.core.fn_QMARK_(handler))?handler:null);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
var or__5162__auto____$1 = (cljs.core.truth_((function (){var and__5160__auto__ = handler;
if(cljs.core.truth_(and__5160__auto__)){
return cljs.core.ifn_QMARK_(replicant.core._STAR_dispatch_STAR_);
} else {
return and__5160__auto__;
}
})())?(function (e){
return replicant.core._STAR_dispatch_STAR_.call(null,e,handler);
}):null);
if(cljs.core.truth_(or__5162__auto____$1)){
return or__5162__auto____$1;
} else {
if(cljs.core.truth_(handler)){
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("Cannot use non-function life-cycle hook when replicant.core/*dispatch* is not bound to a function",new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"handler","handler",-195596612),handler,new cljs.core.Keyword(null,"dispatch","dispatch",1319337009),replicant.core._STAR_dispatch_STAR_], null));
} else {
return null;
}
}
}
});
replicant.core.call_hook = (function replicant$core$call_hook(renderer,p__38171){
var vec__38173 = p__38171;
var hook = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38173,(0),null);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38173,(1),null);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38173,(2),null);
var new$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38173,(3),null);
var old = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38173,(4),null);
var details = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38173,(5),null);
var life_cycle = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38173,(6),null);
var f = replicant.core.get_life_cycle_hook(hook);
var life_cycle__$1 = (function (){var or__5162__auto__ = life_cycle;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
if((old == null)){
return new cljs.core.Keyword("replicant.life-cycle","mount","replicant.life-cycle/mount",-1685508865);
} else {
if((new$ == null)){
return new cljs.core.Keyword("replicant.life-cycle","unmount","replicant.life-cycle/unmount",-144344184);
} else {
return new cljs.core.Keyword("replicant.life-cycle","update","replicant.life-cycle/update",-439589235);

}
}
}
})();
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("replicant","on-render","replicant/on-render",1674377901),k)) || (((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(k,new cljs.core.Keyword("replicant","on-mount","replicant/on-mount",-1518504162))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(life_cycle__$1,new cljs.core.Keyword("replicant.life-cycle","mount","replicant.life-cycle/mount",-1685508865))))) || (((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(k,new cljs.core.Keyword("replicant","on-unmount","replicant/on-unmount",-1287095753))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(life_cycle__$1,new cljs.core.Keyword("replicant.life-cycle","unmount","replicant.life-cycle/unmount",-144344184))))) || (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(k,new cljs.core.Keyword("replicant","on-update","replicant/on-update",-2121629394))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(life_cycle__$1,new cljs.core.Keyword("replicant.life-cycle","update","replicant.life-cycle/update",-439589235))))))))))){
var G__38188 = (function (){var G__38189 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword("replicant","trigger","replicant/trigger",543650841),new cljs.core.Keyword("replicant.trigger","life-cycle","replicant.trigger/life-cycle",-205660972),new cljs.core.Keyword("replicant","life-cycle","replicant/life-cycle",1802888458),life_cycle__$1,new cljs.core.Keyword("replicant","node","replicant/node",1306451380),node,new cljs.core.Keyword("replicant","remember","replicant/remember",809947829),(function replicant$core$call_hook_$_remember(memory){
return replicant.protocols.remember(renderer,node,memory);
})], null);
var G__38189__$1 = (cljs.core.truth_(details)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38189,new cljs.core.Keyword("replicant","details","replicant/details",-1846218867),details):G__38189);
var G__38189__$2 = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(life_cycle__$1,new cljs.core.Keyword("replicant.life-cycle","mount","replicant.life-cycle/mount",-1685508865)))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38189__$1,new cljs.core.Keyword("replicant","memory","replicant/memory",-1882688464),replicant.protocols.recall(renderer,node)):G__38189__$1);
if(cljs.core.ifn_QMARK_(replicant.core._STAR_dispatch_STAR_)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38189__$2,new cljs.core.Keyword("replicant","dispatch","replicant/dispatch",2079272115),replicant.core._STAR_dispatch_STAR_);
} else {
return G__38189__$2;
}
})();
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(G__38188) : f.call(null,G__38188));
} else {
return null;
}
});
replicant.core.node_map = (function replicant$core$node_map(){
return cljs.core.PersistentArrayMap.EMPTY;
});
/**
 * Register the life-cycle hooks from the corresponding virtual DOM node to call
 *   in `impl`, if any. `details` is a vector of keywords that provide some detail
 *   about why the hook is invoked.
 */
replicant.core.register_hooks = (function replicant$core$register_hooks(var_args){
var args__5903__auto__ = [];
var len__5897__auto___38884 = arguments.length;
var i__5898__auto___38886 = (0);
while(true){
if((i__5898__auto___38886 < len__5897__auto___38884)){
args__5903__auto__.push((arguments[i__5898__auto___38886]));

var G__38888 = (i__5898__auto___38886 + (1));
i__5898__auto___38886 = G__38888;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((3) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((3)),(0),null)):null);
return replicant.core.register_hooks.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__5904__auto__);
});

(replicant.core.register_hooks.cljs$core$IFn$_invoke$arity$variadic = (function (p__38207,node,headers,p__38208){
var map__38209 = p__38207;
var map__38209__$1 = cljs.core.__destructure_map(map__38209);
var hooks = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38209__$1,new cljs.core.Keyword(null,"hooks","hooks",-413590103));
var unmount_hooks = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38209__$1,new cljs.core.Keyword(null,"unmount-hooks","unmount-hooks",-1376824226));
var vec__38210 = p__38208;
var vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38210,(0),null);
var details = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38210,(1),null);
var target = (cljs.core.truth_(headers)?(headers[(4)]):(vdom[(3)]));
var new_hooks = cljs.core.keep.cljs$core$IFn$_invoke$arity$2((function (life_cycle_key){
var temp__5823__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(target,life_cycle_key);
if(cljs.core.truth_(temp__5823__auto__)){
var hook = temp__5823__auto__;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [life_cycle_key,hook], null);
} else {
return null;
}
}),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replicant","on-render","replicant/on-render",1674377901),new cljs.core.Keyword("replicant","on-mount","replicant/on-mount",-1518504162),new cljs.core.Keyword("replicant","on-unmount","replicant/on-unmount",-1287095753),new cljs.core.Keyword("replicant","on-update","replicant/on-update",-2121629394)], null));
if(cljs.core.truth_((function (){var and__5160__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(unmount_hooks),node);
if(cljs.core.truth_(and__5160__auto__)){
var and__5160__auto____$1 = headers;
if(cljs.core.truth_(and__5160__auto____$1)){
return cljs.core.empty_QMARK_(new_hooks);
} else {
return and__5160__auto____$1;
}
} else {
return and__5160__auto__;
}
})())){
cljs.core._vreset_BANG_(unmount_hooks,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._deref(unmount_hooks),node));
} else {
}

if(cljs.core.empty_QMARK_(new_hooks)){
return null;
} else {
var headers_sexp = (function (){var G__38213 = headers;
if((G__38213 == null)){
return null;
} else {
return (G__38213[(7)]);
}
})();
var vdom_sexp = (function (){var G__38214 = vdom;
if((G__38214 == null)){
return null;
} else {
return (G__38214[(7)]);
}
})();
var new_hooks__$1 = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__38216){
var vec__38217 = p__38216;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38217,(0),null);
var hook = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38217,(1),null);
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [hook,k,node,headers_sexp,vdom_sexp,details], null);
}),new_hooks);
var temp__5823__auto___38928 = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p__38226){
var vec__38228 = p__38226;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38228,(0),null);
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38228,(1),null);
var node__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38228,(2),null);
var hook = vec__38228;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [node__$1,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(hook,new cljs.core.Keyword("replicant.life-cycle","unmount","replicant.life-cycle/unmount",-144344184))], null);
}),cljs.core.filterv(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword("replicant","on-render","replicant/on-render",1674377901),null,new cljs.core.Keyword("replicant","on-unmount","replicant/on-unmount",-1287095753),null], null), null),cljs.core.second),new_hooks__$1));
if(cljs.core.truth_(temp__5823__auto___38928)){
var new_unmount_hooks_38932 = temp__5823__auto___38928;
cljs.core._vreset_BANG_(unmount_hooks,cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core._deref(unmount_hooks),new_unmount_hooks_38932));
} else {
}

return cljs.core._vreset_BANG_(hooks,cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core._deref(hooks),new_hooks__$1));
}
}));

(replicant.core.register_hooks.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(replicant.core.register_hooks.cljs$lang$applyTo = (function (seq38202){
var G__38203 = cljs.core.first(seq38202);
var seq38202__$1 = cljs.core.next(seq38202);
var G__38204 = cljs.core.first(seq38202__$1);
var seq38202__$2 = cljs.core.next(seq38202__$1);
var G__38205 = cljs.core.first(seq38202__$2);
var seq38202__$3 = cljs.core.next(seq38202__$2);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38203,G__38204,G__38205,seq38202__$3);
}));

replicant.core.register_mount = (function replicant$core$register_mount(p__38233,node,mounting_attrs,attrs){
var map__38234 = p__38233;
var map__38234__$1 = cljs.core.__destructure_map(map__38234);
var mounts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38234__$1,new cljs.core.Keyword(null,"mounts","mounts",-791474425));
return cljs.core._vreset_BANG_(mounts,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core._deref(mounts),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [node,mounting_attrs,attrs], null)));
});
replicant.core.update_styles = (function replicant$core$update_styles(renderer,el,new_styles,old_styles){
var new_ks = cljs.core.set(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__38235_SHARP_){
return (cljs.core.get.cljs$core$IFn$_invoke$arity$2(new_styles,p1__38235_SHARP_) == null);
}),cljs.core.keys(new_styles)));
var old_ks = cljs.core.set(cljs.core.keys(old_styles));
cljs.core.run_BANG_((function (p1__38237_SHARP_){
return replicant.protocols.remove_style(renderer,el,p1__38237_SHARP_);
}),cljs.core.remove.cljs$core$IFn$_invoke$arity$2(new_ks,old_ks));

return cljs.core.run_BANG_((function (p1__38238_SHARP_){
var new_style = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new_styles,p1__38238_SHARP_);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new_style,cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_styles,p1__38238_SHARP_))){
if((!((p1__38238_SHARP_ instanceof cljs.core.Keyword)))){
var fn__37317__auto___38937 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38938 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38939 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38240 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Style key "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38238_SHARP_)+" should be a keyword"),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Replicant expects your style keys to be keywords. While anything that supports `name` (strings, symbols) will "+"technically work, mixing types will hinder Replicant from recognizing changes properly. Rendering once with "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38238_SHARP_)))+" and once with "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38238_SHARP_))))+" may produce undesired results. Your safest option is to always use keywords.")], null);
var G__38240__$1 = (cljs.core.truth_(fn__37317__auto___38937)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38240,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38937):G__38240);
var G__38240__$2 = (cljs.core.truth_(alias__37318__auto___38938)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38240__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38938):G__38240__$1);
if(cljs.core.truth_(fd__37319__auto___38939)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38240__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38939);
} else {
return G__38240__$2;
}
})());
} else {
}

if((!((function (){var name__37887__auto__ = cljs.core.name(p1__38238_SHARP_);
return ((clojure.string.starts_with_QMARK_(name__37887__auto__,"--")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(name__37887__auto__,clojure.string.lower_case(name__37887__auto__))));
})()))){
var fn__37317__auto___38942 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38943 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38944 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38249 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Use "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(replicant.asserts.camel__GT_dash_k(p1__38238_SHARP_))+", not "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38238_SHARP_)),new cljs.core.Keyword(null,"message","message",-406056002),"Replicant passes style keys directly to `el.style.setProperty`, which expects CSS-style dash-cased property names."], null);
var G__38249__$1 = (cljs.core.truth_(fn__37317__auto___38942)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38249,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38942):G__38249);
var G__38249__$2 = (cljs.core.truth_(alias__37318__auto___38943)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38249__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38943):G__38249__$1);
if(cljs.core.truth_(fd__37319__auto___38944)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38249__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38944);
} else {
return G__38249__$2;
}
})());
} else {
}

return replicant.protocols.set_style(renderer,el,p1__38238_SHARP_,replicant.core.get_style_val(p1__38238_SHARP_,new_style));
} else {
return null;
}
}),new_ks);
});
replicant.core.update_classes = (function replicant$core$update_classes(renderer,el,new_classes,old_classes){
cljs.core.run_BANG_((function (p1__38251_SHARP_){
return replicant.protocols.remove_class(renderer,el,p1__38251_SHARP_);
}),cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.set(new_classes),old_classes));

return cljs.core.run_BANG_((function (p1__38252_SHARP_){
return replicant.protocols.add_class(renderer,el,p1__38252_SHARP_);
}),cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.set(old_classes),new_classes));
});
replicant.core.get_event_handler_options = (function replicant$core$get_event_handler_options(m){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (res,k){
var G__38254 = res;
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("replicant.event",cljs.core.namespace(k))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38254,cljs.core.name(k),cljs.core.get.cljs$core$IFn$_invoke$arity$2(m,k));
} else {
return G__38254;
}
}),null,cljs.core.keys(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(m,new cljs.core.Keyword("replicant.event","handler","replicant.event/handler",-1806871006),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword("replicant.event","wrap-handler?","replicant.event/wrap-handler?",845655928)], 0))));
});
replicant.core.add_event_listeners = (function replicant$core$add_event_listeners(renderer,el,val){
return cljs.core.run_BANG_((function (p__38256){
var vec__38257 = p__38256;
var event = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38257,(0),null);
var handler = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38257,(1),null);
if((!((function (){var event__37821__auto__ = cljs.core.name(event);
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("DOMContentLoaded",event__37821__auto__)) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(event__37821__auto__,clojure.string.lower_case(event__37821__auto__))));
})()))){
var fn__37317__auto___38958 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38959 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38960 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38267 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Use "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(clojure.string.lower_case(cljs.core.name(event))))+", not "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(event)),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Most event names should be in all lower-case. Replicant passes your event names directly to addEventListener, and mis-cased event names will fail silently.")], null);
var G__38267__$1 = (cljs.core.truth_(fn__37317__auto___38958)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38267,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38958):G__38267);
var G__38267__$2 = (cljs.core.truth_(alias__37318__auto___38959)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38267__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38959):G__38267__$1);
if(cljs.core.truth_(fd__37319__auto___38960)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38267__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38960);
} else {
return G__38267__$2;
}
})());
} else {
}

var temp__5821__auto__ = new cljs.core.Keyword("replicant.event","handler","replicant.event/handler",-1806871006).cljs$core$IFn$_invoke$arity$1(handler);
if(cljs.core.truth_(temp__5821__auto__)){
var eh = temp__5821__auto__;
var temp__5823__auto__ = replicant.core.get_event_handler(eh,event,handler);
if(cljs.core.truth_(temp__5823__auto__)){
var eh__$1 = temp__5823__auto__;
return replicant.protocols.set_event_handler(renderer,el,event,eh__$1,replicant.core.get_event_handler_options(handler));
} else {
return null;
}
} else {
var temp__5823__auto__ = replicant.core.get_event_handler(handler,event,null);
if(cljs.core.truth_(temp__5823__auto__)){
var handler__$1 = temp__5823__auto__;
return replicant.protocols.set_event_handler(renderer,el,event,handler__$1,null);
} else {
return null;
}
}
}),cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.comp.cljs$core$IFn$_invoke$arity$2(cljs.core.nil_QMARK_,cljs.core.second),val));
});
replicant.core.update_event_listeners = (function replicant$core$update_event_listeners(renderer,el,new_handlers,old_handlers){
return cljs.core.run_BANG_((function (event){
var new_handler = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new_handlers,event);
var old_handler = cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_handlers,event);
var old_opts = (cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(old_handler,new cljs.core.Keyword("replicant.event","handler","replicant.event/handler",-1806871006)))?cljs.core.not_empty(replicant.core.get_event_handler_options(old_handler)):null);
var new_opts = (cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(new_handler,new cljs.core.Keyword("replicant.event","handler","replicant.event/handler",-1806871006)))?cljs.core.not_empty(replicant.core.get_event_handler_options(new_handler)):null);
if(cljs.core.truth_((function (){var and__5160__auto__ = old_handler;
if(cljs.core.truth_(and__5160__auto__)){
return (((new_handler == null)) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(old_opts,new_opts)));
} else {
return and__5160__auto__;
}
})())){
replicant.protocols.remove_event_handler(renderer,el,event,old_opts);
} else {
}

if(cljs.core.truth_((function (){var and__5160__auto__ = new_handler;
if(cljs.core.truth_(and__5160__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new_handler,old_handler);
} else {
return and__5160__auto__;
}
})())){
var temp__5821__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new_handler,new cljs.core.Keyword("replicant.event","handler","replicant.event/handler",-1806871006));
if(cljs.core.truth_(temp__5821__auto__)){
var handler = temp__5821__auto__;
return replicant.protocols.set_event_handler(renderer,el,event,replicant.core.get_event_handler(handler,event,new_handler),new_opts);
} else {
return replicant.protocols.set_event_handler(renderer,el,event,replicant.core.get_event_handler(new_handler,event,null),null);
}
} else {
return null;
}
}),cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.set(cljs.core.keys(new_handlers)),cljs.core.keys(old_handlers)));
});
replicant.core.xlinkns = "http://www.w3.org/1999/xlink";
replicant.core.xmlns = "http://www.w3.org/XML/1998/namespace";
replicant.core.stringify = (function replicant$core$stringify(x){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var temp__5823__auto__ = cljs.core.namespace(x);
if(cljs.core.truth_(temp__5823__auto__)){
var ns = temp__5823__auto__;
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ns)+"/");
} else {
return null;
}
})())+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(x)));
});
replicant.core.set_attr_val = (function replicant$core$set_attr_val(renderer,el,attr,v){
var an = cljs.core.name(attr);
if((!((!(clojure.string.starts_with_QMARK_(cljs.core.name(attr),"on")))))){
var fn__37317__auto___38988 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___38989 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___38990 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38292 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),"Set event listeners in the :on map",new cljs.core.Keyword(null,"message","message",-406056002),(""+"Event handler attributes are not supported. Instead of "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(attr)+" set :on {"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1(replicant.asserts.camel__GT_dash(cljs.core.name(attr).substring((2)))))+" ,,,}")], null);
var G__38292__$1 = (cljs.core.truth_(fn__37317__auto___38988)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38292,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___38988):G__38292);
var G__38292__$2 = (cljs.core.truth_(alias__37318__auto___38989)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38292__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___38989):G__38292__$1);
if(cljs.core.truth_(fd__37319__auto___38990)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38292__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___38990);
} else {
return G__38292__$2;
}
})());
} else {
}

if(cljs.core.not(cljs.core.re_find(/^[a-zA-Z\-:_][a-zA-Z0-9\-:\._]*$/,cljs.core.name(attr)))){
var fn__37317__auto___39002 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___39003 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___39004 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38301 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Invalid attribute name "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(attr))),new cljs.core.Keyword(null,"message","message",-406056002),(function (){var attr__37898__auto__ = cljs.core.name(attr);
return (""+"Tried to set attribute "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(attr__37898__auto__)+" to value "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)+". This will fail"+"horribly in the browser because "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(cljs.core.re_find(/^[0-9]/,attr__37898__auto__))?" it starts with a number":(cljs.core.truth_(cljs.core.re_find(/^\./,attr__37898__auto__))?" it starts with a dot":(""+" it contains the character "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.re_find(/[^a-zA-Z0-9\-:\._]/,attr__37898__auto__)))
)))+", which isn't allowed as per the HTML spec.");
})()], null);
var G__38301__$1 = (cljs.core.truth_(fn__37317__auto___39002)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38301,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___39002):G__38301);
var G__38301__$2 = (cljs.core.truth_(alias__37318__auto___39003)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38301__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___39003):G__38301__$1);
if(cljs.core.truth_(fd__37319__auto___39004)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38301__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___39004);
} else {
return G__38301__$2;
}
})());
} else {
}

return replicant.protocols.set_attribute(renderer,el,an,(function (){var G__38303 = v;
if((((v instanceof cljs.core.Keyword)) || ((v instanceof cljs.core.Symbol)))){
return replicant.core.stringify(G__38303);
} else {
return G__38303;
}
})(),(function (){var G__38305 = cljs.core.PersistentArrayMap.EMPTY;
var G__38305__$1 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((0),an.indexOf("xml:")))?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38305,new cljs.core.Keyword(null,"ns","ns",441598760),replicant.core.xmlns):G__38305);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((0),an.indexOf("xlink:"))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38305__$1,new cljs.core.Keyword(null,"ns","ns",441598760),replicant.core.xlinkns);
} else {
return G__38305__$1;
}
})());
});
replicant.core.update_attr = (function replicant$core$update_attr(renderer,el,attr,new$,old){
if(cljs.core.truth_(cljs.core.namespace(attr))){
return null;
} else {
var G__38307 = attr;
var G__38307__$1 = (((G__38307 instanceof cljs.core.Keyword))?G__38307.fqn:null);
switch (G__38307__$1) {
case "style":
return replicant.core.update_styles(renderer,el,new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1(new$),new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1(old));

break;
case "classes":
return replicant.core.update_classes(renderer,el,new cljs.core.Keyword(null,"classes","classes",2037804510).cljs$core$IFn$_invoke$arity$1(new$),new cljs.core.Keyword(null,"classes","classes",2037804510).cljs$core$IFn$_invoke$arity$1(old));

break;
case "on":
return replicant.core.update_event_listeners(renderer,el,new cljs.core.Keyword(null,"on","on",173873944).cljs$core$IFn$_invoke$arity$1(new$),new cljs.core.Keyword(null,"on","on",173873944).cljs$core$IFn$_invoke$arity$1(old));

break;
default:
var temp__5821__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(new$,attr);
if(cljs.core.truth_(temp__5821__auto__)){
var v = temp__5821__auto__;
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(v,cljs.core.get.cljs$core$IFn$_invoke$arity$2(old,attr))){
return replicant.core.set_attr_val(renderer,el,attr,v);
} else {
return null;
}
} else {
return replicant.protocols.remove_attribute(renderer,el,cljs.core.name(attr));
}

}
}
});
replicant.core.update_attributes = (function replicant$core$update_attributes(renderer,el,new_attrs,old_attrs){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__38317_SHARP_,p2__38316_SHARP_){
return replicant.core.update_attr(renderer,el,p2__38316_SHARP_,new_attrs,old_attrs);
}),null,cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.set(cljs.core.keys(new_attrs)),cljs.core.keys(old_attrs)));
});
replicant.core.reconcile_attributes = (function replicant$core$reconcile_attributes(renderer,el,new_attrs,old_attrs){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new_attrs,old_attrs)){
return false;
} else {
replicant.core.update_attributes(renderer,el,new_attrs,old_attrs);

return true;
}
});
replicant.core.set_styles = (function replicant$core$set_styles(renderer,el,new_styles){
return cljs.core.run_BANG_((function (p1__38320_SHARP_){
if((!((p1__38320_SHARP_ instanceof cljs.core.Keyword)))){
var fn__37317__auto___39051 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___39052 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___39053 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38322 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Style key "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38320_SHARP_)+" should be a keyword"),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Replicant expects your style keys to be keywords. While anything that supports `name` (strings, symbols) will "+"technically work, mixing types will hinder Replicant from recognizing changes properly. Rendering once with "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38320_SHARP_)))+" and once with "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.keyword.cljs$core$IFn$_invoke$arity$1((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38320_SHARP_))))+" may produce undesired results. Your safest option is to always use keywords.")], null);
var G__38322__$1 = (cljs.core.truth_(fn__37317__auto___39051)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38322,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___39051):G__38322);
var G__38322__$2 = (cljs.core.truth_(alias__37318__auto___39052)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38322__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___39052):G__38322__$1);
if(cljs.core.truth_(fd__37319__auto___39053)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38322__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___39053);
} else {
return G__38322__$2;
}
})());
} else {
}

if((!((function (){var name__37887__auto__ = cljs.core.name(p1__38320_SHARP_);
return ((clojure.string.starts_with_QMARK_(name__37887__auto__,"--")) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(name__37887__auto__,clojure.string.lower_case(name__37887__auto__))));
})()))){
var fn__37317__auto___39071 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___39072 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___39073 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38330 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Use "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(replicant.asserts.camel__GT_dash_k(p1__38320_SHARP_))+", not "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(p1__38320_SHARP_)),new cljs.core.Keyword(null,"message","message",-406056002),"Replicant passes style keys directly to `el.style.setProperty`, which expects CSS-style dash-cased property names."], null);
var G__38330__$1 = (cljs.core.truth_(fn__37317__auto___39071)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38330,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___39071):G__38330);
var G__38330__$2 = (cljs.core.truth_(alias__37318__auto___39072)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38330__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___39072):G__38330__$1);
if(cljs.core.truth_(fd__37319__auto___39073)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38330__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___39073);
} else {
return G__38330__$2;
}
})());
} else {
}

return replicant.protocols.set_style(renderer,el,p1__38320_SHARP_,replicant.core.get_style_val(p1__38320_SHARP_,cljs.core.get.cljs$core$IFn$_invoke$arity$2(new_styles,p1__38320_SHARP_)));
}),cljs.core.filter.cljs$core$IFn$_invoke$arity$2(new_styles,cljs.core.keys(new_styles)));
});
replicant.core.set_classes = (function replicant$core$set_classes(renderer,el,new_classes){
return cljs.core.run_BANG_((function (p1__38333_SHARP_){
return replicant.protocols.add_class(renderer,el,p1__38333_SHARP_);
}),new_classes);
});
replicant.core.set_attr = (function replicant$core$set_attr(renderer,el,attr,new$){
if(cljs.core.truth_(cljs.core.namespace(attr))){
return null;
} else {
var G__38335 = attr;
var G__38335__$1 = (((G__38335 instanceof cljs.core.Keyword))?G__38335.fqn:null);
switch (G__38335__$1) {
case "style":
return replicant.core.set_styles(renderer,el,new cljs.core.Keyword(null,"style","style",-496642736).cljs$core$IFn$_invoke$arity$1(new$));

break;
case "classes":
return replicant.core.set_classes(renderer,el,new cljs.core.Keyword(null,"classes","classes",2037804510).cljs$core$IFn$_invoke$arity$1(new$));

break;
case "on":
return replicant.core.add_event_listeners(renderer,el,new cljs.core.Keyword(null,"on","on",173873944).cljs$core$IFn$_invoke$arity$1(new$));

break;
default:
return replicant.core.set_attr_val(renderer,el,attr,cljs.core.get.cljs$core$IFn$_invoke$arity$2(new$,attr));

}
}
});
replicant.core.set_attributes = (function replicant$core$set_attributes(renderer,el,new_attrs){
cljs.core.run_BANG_((function (p__38337){
var vec__38338 = p__38337;
var attr = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38338,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38338,(1),null);
if(cljs.core.truth_(v)){
return replicant.core.set_attr(renderer,el,attr,new_attrs);
} else {
return null;
}
}),cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(new_attrs,new cljs.core.Keyword(null,"value","value",305978217),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"default-value","default-value",232220170)], 0)));

if(cljs.core.truth_(new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(new_attrs))){
replicant.core.set_attr(renderer,el,new cljs.core.Keyword(null,"value","value",305978217),new_attrs);
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"default-value","default-value",232220170).cljs$core$IFn$_invoke$arity$1(new_attrs))){
return replicant.core.set_attr(renderer,el,new cljs.core.Keyword(null,"default-value","default-value",232220170),new_attrs);
} else {
return null;
}
});
replicant.core.render_default_alias = (function replicant$core$render_default_alias(tag_name,_attrs,children){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div","div",1057191632),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"data-replicant-error","data-replicant-error",1583668098),(""+"Undefined alias "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(tag_name))], null),(function (){var iter__5649__auto__ = (function replicant$core$render_default_alias_$_iter__38359(s__38360){
return (new cljs.core.LazySeq(null,(function (){
var s__38360__$1 = s__38360;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__38360__$1);
if(temp__5823__auto__){
var s__38360__$2 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38360__$2)){
var c__5647__auto__ = cljs.core.chunk_first(s__38360__$2);
var size__5648__auto__ = cljs.core.count(c__5647__auto__);
var b__38362 = cljs.core.chunk_buffer(size__5648__auto__);
if((function (){var i__38361 = (0);
while(true){
if((i__38361 < size__5648__auto__)){
var child = cljs.core._nth(c__5647__auto__,i__38361);
cljs.core.chunk_append(b__38362,(function (){var G__38364 = child;
if((((!(typeof child === 'string'))) && ((!(replicant.hiccup.hiccup_QMARK_(child)))))){
return cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([G__38364], 0));
} else {
return G__38364;
}
})());

var G__39119 = (i__38361 + (1));
i__38361 = G__39119;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38362),replicant$core$render_default_alias_$_iter__38359(cljs.core.chunk_rest(s__38360__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38362),null);
}
} else {
var child = cljs.core.first(s__38360__$2);
return cljs.core.cons((function (){var G__38366 = child;
if((((!(typeof child === 'string'))) && ((!(replicant.hiccup.hiccup_QMARK_(child)))))){
return cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([G__38366], 0));
} else {
return G__38366;
}
})(),replicant$core$render_default_alias_$_iter__38359(cljs.core.rest(s__38360__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5649__auto__(children);
})()], null);
});
replicant.core.add_classes = (function replicant$core$add_classes(class_attr,classes){
if(cljs.core.coll_QMARK_(class_attr)){
return cljs.core.set(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(class_attr,classes));
} else {
if((class_attr == null)){
return cljs.core.set(classes);
} else {
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.set(classes),class_attr);

}
}
});
replicant.core.get_alias_headers = (function replicant$core$get_alias_headers(p__38377,headers){
var map__38378 = p__38377;
var map__38378__$1 = cljs.core.__destructure_map(map__38378);
var aliases = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38378__$1,new cljs.core.Keyword(null,"aliases","aliases",1346874714));
var alias_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38378__$1,new cljs.core.Keyword(null,"alias-data","alias-data",1743863930));
var on_alias_exception = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38378__$1,new cljs.core.Keyword(null,"on-alias-exception","on-alias-exception",1142240043));
var tag_name = (headers[(0)]);
if(cljs.core.qualified_keyword_QMARK_(tag_name)){
var f = (function (){var or__5162__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(aliases,tag_name);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.partial.cljs$core$IFn$_invoke$arity$2(replicant.core.render_default_alias,tag_name);
}
})();
var id = (headers[(1)]);
var classes = (headers[(2)]);
var attrs = (headers[(4)]);
var attrs__$1 = (function (){var G__38380 = attrs;
var G__38380__$1 = (cljs.core.truth_(id)?cljs.core.update.cljs$core$IFn$_invoke$arity$3(G__38380,new cljs.core.Keyword(null,"id","id",-1388402092),(function (p1__38369_SHARP_){
var or__5162__auto__ = p1__38369_SHARP_;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return id;
}
})):G__38380);
var G__38380__$2 = (cljs.core.truth_((function (){var or__5162__auto__ = cljs.core.seq(classes);
if(or__5162__auto__){
return or__5162__auto__;
} else {
return new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(attrs);
}
})())?cljs.core.update.cljs$core$IFn$_invoke$arity$4(G__38380__$1,new cljs.core.Keyword(null,"class","class",-2030961996),replicant.core.add_classes,classes):G__38380__$1);
if(cljs.core.truth_(alias_data)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38380__$2,new cljs.core.Keyword("replicant","alias-data","replicant/alias-data",1929752572),alias_data);
} else {
return G__38380__$2;
}
})();
var children = replicant.core.__GT_seq(replicant.core.flatten_seqs((headers[(5)])));
if((!(cljs.core.fn_QMARK_(cljs.core.get.cljs$core$IFn$_invoke$arity$2(aliases,tag_name))))){
var fn__37317__auto___39152 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___39153 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___39154 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38383 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Alias "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(tag_name)+" isn't defined"),new cljs.core.Keyword(null,"message","message",-406056002),(""+"There's no available function to render this alias. Replicant will "+"render an empty element with data attributes in its place. Available "+"aliases are:\n"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$2("\n",cljs.core.keys(aliases))))], null);
var G__38383__$1 = (cljs.core.truth_(fn__37317__auto___39152)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38383,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___39152):G__38383);
var G__38383__$2 = (cljs.core.truth_(alias__37318__auto___39153)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38383__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___39153):G__38383__$1);
if(cljs.core.truth_(fd__37319__auto___39154)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38383__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___39154);
} else {
return G__38383__$2;
}
})());
} else {
}

var alias_hiccup = (f.cljs$core$IFn$_invoke$arity$2 ? f.cljs$core$IFn$_invoke$arity$2(attrs__$1,children) : f.call(null,attrs__$1,children));
if((!(((typeof alias_hiccup === 'string') || (replicant.hiccup.hiccup_QMARK_(alias_hiccup)))))){
var fn__37317__auto___39174 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___39175 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___39176 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38385 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),(""+"Aliases must return valid hiccup"),new cljs.core.Keyword(null,"message","message",-406056002),(""+"Aliases must always represent a node in the document, and "+"cannot return "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((((alias_hiccup == null))?"nil":((cljs.core.map_QMARK_(alias_hiccup))?"a map":((cljs.core.coll_QMARK_(alias_hiccup))?"multiple nodes":cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([alias_hiccup], 0))
))))+". Please check the implementation of "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(tag_name)+".")], null);
var G__38385__$1 = (cljs.core.truth_(fn__37317__auto___39174)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38385,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___39174):G__38385);
var G__38385__$2 = (cljs.core.truth_(alias__37318__auto___39175)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38385__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___39175):G__38385__$1);
if(cljs.core.truth_(fd__37319__auto___39176)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38385__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___39176);
} else {
return G__38385__$2;
}
})());
} else {
}

var hh__37035__auto__ = replicant.core.get_hiccup_headers(null,alias_hiccup);
var alias__37036__auto__ = headers;
if(cljs.core.truth_(hh__37035__auto__)){
var G__38393 = hh__37035__auto__;
(G__38393[(3)] = (function (){var or__5162__auto__ = (alias__37036__auto__[(3)]);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return (hh__37035__auto__[(3)]);
}
})());

(G__38393[(6)] = (alias__37036__auto__[(6)]));

(G__38393[(7)] = (hh__37035__auto__[(7)]));

(G__38393[(9)] = (alias__37036__auto__[(7)]));

return G__38393;
} else {
return null;
}
} else {
return null;
}
});
/**
 * Create DOM node according to virtual DOM in `headers`. Register relevant
 *   life-cycle hooks from the new node or its descendants in `impl`. Returns a
 *   tuple of the newly created node and the fully realized vdom.
 */
replicant.core.create_node = (function replicant$core$create_node(p__38397,headers){
var map__38398 = p__38397;
var map__38398__$1 = cljs.core.__destructure_map(map__38398);
var impl = map__38398__$1;
var renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38398__$1,new cljs.core.Keyword(null,"renderer","renderer",336841071));
if(cljs.core.truth_(headers)){
var temp__5823__auto___39187 = (function (){var or__5162__auto__ = new cljs.core.Keyword("replicant","context","replicant/context",-909059467).cljs$core$IFn$_invoke$arity$1((headers[(4)]));
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return new cljs.core.Keyword("replicant","context","replicant/context",-909059467).cljs$core$IFn$_invoke$arity$1(cljs.core.meta((headers[(7)])));
}
})();
if(cljs.core.truth_(temp__5823__auto___39187)){
var ctx__37309__auto___39188 = temp__5823__auto___39187;
cljs.core.reset_BANG_(replicant.assert.current_context,ctx__37309__auto___39188);
} else {
}

cljs.core.reset_BANG_(replicant.assert.current_node,(headers[(7)]));
} else {
}

var or__5162__auto__ = (function (){var temp__5823__auto__ = (headers[(8)]);
if(cljs.core.truth_(temp__5823__auto__)){
var text = temp__5823__auto__;
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [replicant.protocols.create_text_node(renderer,text),(function (){var text__37529__auto__ = text;
return (new Array(null,null,null,null,null,null,false,text__37529__auto__,text__37529__auto__,null,null));
})()], null);
} else {
return null;
}
})();
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
var or__5162__auto____$1 = (function (){var temp__5823__auto__ = replicant.core.get_alias_headers(impl,headers);
if(cljs.core.truth_(temp__5823__auto__)){
var alias_headers = temp__5823__auto__;
var vec__38400 = (replicant.core.create_node.cljs$core$IFn$_invoke$arity$2 ? replicant.core.create_node.cljs$core$IFn$_invoke$arity$2(impl,alias_headers) : replicant.core.create_node.call(null,impl,alias_headers));
var child_node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38400,(0),null);
var vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38400,(1),null);
var k = (alias_headers[(3)]);
var vdom__$1 = (function (){var headers__37545__auto__ = headers;
return (new Array((headers__37545__auto__[(0)]),(headers__37545__auto__[(3)]),(headers__37545__auto__[(2)]),(headers[(4)]),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [vdom], null),(function (){var G__38403 = cljs.core.PersistentHashSet.EMPTY;
if(cljs.core.truth_(k)){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(G__38403,k);
} else {
return G__38403;
}
})(),cljs.core.boolean$(new cljs.core.Keyword("replicant","unmounting","replicant/unmounting",1629433009).cljs$core$IFn$_invoke$arity$1((headers__37545__auto__[(4)]))),(headers__37545__auto__[(7)]),null,null,(1)));
})();
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [child_node,vdom__$1], null);
} else {
return null;
}
})();
if(cljs.core.truth_(or__5162__auto____$1)){
return or__5162__auto____$1;
} else {
var tag_name = (headers[(0)]);
var ns = (function (){var or__5162__auto____$2 = (headers[(6)]);
if(cljs.core.truth_(or__5162__auto____$2)){
return or__5162__auto____$2;
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("svg",tag_name)){
return "http://www.w3.org/2000/svg";
} else {
return null;
}
}
})();
var node = replicant.protocols.create_element(renderer,tag_name,(cljs.core.truth_(ns)?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ns","ns",441598760),ns], null):null));
var vec__38410 = replicant.core.get_mounting_attrs(headers);
var attrs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38410,(0),null);
var mounting_attrs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38410,(1),null);
var children_ns = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("foreignObject",tag_name))?null:ns);
var _ = replicant.core.set_attributes(renderer,node,(function (){var or__5162__auto____$2 = mounting_attrs;
if(cljs.core.truth_(or__5162__auto____$2)){
return or__5162__auto____$2;
} else {
return attrs;
}
})());
var vec__38413 = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p__38418,child_headers){
var vec__38419 = p__38418;
var children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38419,(0),null);
var ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38419,(1),null);
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38419,(2),null);
if(cljs.core.truth_(child_headers)){
var vec__38422 = (replicant.core.create_node.cljs$core$IFn$_invoke$arity$2 ? replicant.core.create_node.cljs$core$IFn$_invoke$arity$2(impl,child_headers) : replicant.core.create_node.call(null,impl,child_headers));
var child_node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38422,(0),null);
var vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38422,(1),null);
var k = (vdom[(1)]);
replicant.protocols.append_child(renderer,node,child_node);

return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(children,vdom),(function (){var G__38425 = ks;
if(cljs.core.truth_(k)){
return cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(G__38425,k);
} else {
return G__38425;
}
})(),(n + (1))], null);
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(children,null),ks,n], null);
}
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.transient$(cljs.core.PersistentVector.EMPTY),cljs.core.transient$(cljs.core.PersistentHashSet.EMPTY),(0)], null),replicant.core.get_children(headers,children_ns));
var children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38413,(0),null);
var ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38413,(1),null);
var n_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38413,(2),null);
replicant.core.register_hooks(impl,node,headers);

if(cljs.core.truth_(mounting_attrs)){
replicant.core.register_mount(impl,node,mounting_attrs,attrs);
} else {
}

return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [node,(function (){var headers__37545__auto__ = headers;
return (new Array((headers__37545__auto__[(0)]),(headers__37545__auto__[(3)]),(headers__37545__auto__[(2)]),attrs,cljs.core.persistent_BANG_(children),cljs.core.persistent_BANG_(ks),cljs.core.boolean$(new cljs.core.Keyword("replicant","unmounting","replicant/unmounting",1629433009).cljs$core$IFn$_invoke$arity$1((headers__37545__auto__[(4)]))),(headers__37545__auto__[(7)]),null,null,n_children));
})()], null);
}
}
});
/**
 * Two elements are considered similar enough for reuse if they are both hiccup
 *   elements with the same tag name and the same key (or both have no key) - or
 *   they are both strings.
 * 
 *   Similarity in this case indicates that the node can be used for reconciliation
 *   instead of creating a new node from scratch.
 */
replicant.core.reusable_QMARK_ = (function replicant$core$reusable_QMARK_(headers,vdom){
var or__5162__auto__ = (function (){var and__5160__auto__ = (headers[(8)]);
if(cljs.core.truth_(and__5160__auto__)){
return (vdom[(8)]);
} else {
return and__5160__auto__;
}
})();
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((headers[(3)]),(vdom[(1)]))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((headers[(0)]),(vdom[(0)]))));
}
});
replicant.core.same_QMARK_ = (function replicant$core$same_QMARK_(headers,vdom){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((headers[(3)]),(vdom[(1)]))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((headers[(0)]),(vdom[(0)]))));
});
replicant.core.index_of = (function replicant$core$index_of(f,xs){
var coll_n = (0);
var dom_n = (0);
var xs__$1 = cljs.core.seq(xs);
while(true){
if((xs__$1 == null)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(-1),(-1)], null);
} else {
if((cljs.core.first(xs__$1) == null)){
var G__39227 = (coll_n + (1));
var G__39228 = dom_n;
var G__39229 = cljs.core.next(xs__$1);
coll_n = G__39227;
dom_n = G__39228;
xs__$1 = G__39229;
continue;
} else {
if(cljs.core.truth_((function (){var G__38429 = cljs.core.first(xs__$1);
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(G__38429) : f.call(null,G__38429));
})())){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [coll_n,dom_n], null);
} else {
var G__39230 = (coll_n + (1));
var G__39231 = (dom_n + (1));
var G__39232 = cljs.core.next(xs__$1);
coll_n = G__39230;
dom_n = G__39231;
xs__$1 = G__39232;
continue;

}
}
}
break;
}
});
replicant.core.get_ns = (function replicant$core$get_ns(headers){
var or__5162__auto__ = (headers[(6)]);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("svg",(headers[(0)]))){
return "http://www.w3.org/2000/svg";
} else {
return null;
}
}
});
replicant.core.insert_children = (function replicant$core$insert_children(p__38430,el,children,vdom){
var map__38431 = p__38430;
var map__38431__$1 = cljs.core.__destructure_map(map__38431);
var impl = map__38431__$1;
var renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38431__$1,new cljs.core.Keyword(null,"renderer","renderer",336841071));
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p__38432,child){
var vec__38433 = p__38432;
var res = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38433,(0),null);
var n = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38433,(1),null);
if(cljs.core.truth_(child)){
var vec__38436 = replicant.core.create_node(impl,child);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38436,(0),null);
var vdom__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38436,(1),null);
replicant.protocols.append_child(renderer,el,node);

return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(res,vdom__$1),(n + (1))], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(res,null),n], null);
}
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [vdom,(0)], null),children);
});
replicant.core.remove_child = (function replicant$core$remove_child(p__38441,unmounts,el,n,vdom){
var map__38442 = p__38441;
var map__38442__$1 = cljs.core.__destructure_map(map__38442);
var impl = map__38442__$1;
var renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38442__$1,new cljs.core.Keyword(null,"renderer","renderer",336841071));
var temp__5821__auto__ = (vdom[(9)]);
if(cljs.core.truth_(temp__5821__auto__)){
var id = temp__5821__auto__;
if(cljs.core.contains_QMARK_(unmounts,id)){
return vdom;
} else {
return null;
}
} else {
var res = (function (){var temp__5821__auto____$1 = replicant.core.get_unmounting_attrs(vdom);
if(cljs.core.truth_(temp__5821__auto____$1)){
var attrs = temp__5821__auto____$1;
var vdom__$1 = (function (){var vdom__37521__auto__ = vdom;
(vdom__37521__auto__[(9)] = replicant.vdom.id.cljs$core$IVolatile$_vreset_BANG_$arity$2(null,(replicant.vdom.id.cljs$core$IDeref$_deref$arity$1(null) + (1))));

return vdom__37521__auto__;
})();
var child = replicant.protocols.get_child(renderer,el,n);
replicant.core.update_attributes(renderer,child,attrs,(vdom__$1[(3)]));

cljs.core._vreset_BANG_(new cljs.core.Keyword(null,"unmounts","unmounts",173366521).cljs$core$IFn$_invoke$arity$1(impl),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core._deref(new cljs.core.Keyword(null,"unmounts","unmounts",173366521).cljs$core$IFn$_invoke$arity$1(impl)),(vdom__$1[(9)])));

replicant.protocols.on_transition_end(renderer,child,(function (){
cljs.core._vreset_BANG_(new cljs.core.Keyword(null,"unmounts","unmounts",173366521).cljs$core$IFn$_invoke$arity$1(impl),cljs.core.disj.cljs$core$IFn$_invoke$arity$2(cljs.core._deref(new cljs.core.Keyword(null,"unmounts","unmounts",173366521).cljs$core$IFn$_invoke$arity$1(impl)),(vdom__$1[(9)])));

replicant.protocols.remove_child(renderer,el,child);

var temp__5823__auto___39255 = new cljs.core.Keyword("replicant","on-render","replicant/on-render",1674377901).cljs$core$IFn$_invoke$arity$1((vdom__$1[(3)]));
if(cljs.core.truth_(temp__5823__auto___39255)){
var hook_39259 = temp__5823__auto___39255;
replicant.core.call_hook(renderer,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [hook_39259,new cljs.core.Keyword("replicant","on-render","replicant/on-render",1674377901),child,null,vdom__$1], null));
} else {
}

return renderer;
}));

return vdom__$1;
} else {
var child = replicant.protocols.get_child(renderer,el,n);
replicant.protocols.remove_child(renderer,el,child);

replicant.core.register_hooks.cljs$core$IFn$_invoke$arity$variadic(impl,child,null,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([vdom], 0));

return null;
}
})();
return res;
}
});
replicant.core.move_node_details = new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replicant","move-node","replicant/move-node",-1189251602)], null);
replicant.core.unchanged_QMARK_ = (function replicant$core$unchanged_QMARK_(headers,vdom){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((function (){var G__38451 = headers;
if((G__38451 == null)){
return null;
} else {
return (G__38451[(7)]);
}
})(),(function (){var G__38454 = vdom;
if((G__38454 == null)){
return null;
} else {
return (G__38454[(7)]);
}
})());
});
replicant.core.move_nodes = (function replicant$core$move_nodes(p__38460,el,headers,new_children,vdom,old_children,n,n_children){
var map__38462 = p__38460;
var map__38462__$1 = cljs.core.__destructure_map(map__38462);
var impl = map__38462__$1;
var renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38462__$1,new cljs.core.Keyword(null,"renderer","renderer",336841071));
var vec__38463 = (cljs.core.truth_((headers[(3)]))?replicant.core.index_of((function (p1__38455_SHARP_){
return replicant.core.same_QMARK_(headers,p1__38455_SHARP_);
}),old_children):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(-1),(-1)], null));
var o_idx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38463,(0),null);
var o_dom_idx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38463,(1),null);
var vec__38466 = (cljs.core.truth_((vdom[(1)]))?replicant.core.index_of((function (p1__38456_SHARP_){
return replicant.core.same_QMARK_(p1__38456_SHARP_,vdom);
}),new_children):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(-1),(-1)], null));
var n_idx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38466,(0),null);
var n_dom_idx = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38466,(1),null);
if((o_idx < n_idx)){
var idx = ((n + n_dom_idx) + (1));
var child = replicant.protocols.get_child(renderer,el,n);
if((idx < n_children)){
replicant.protocols.insert_before(renderer,el,child,replicant.protocols.get_child(renderer,el,idx));
} else {
replicant.protocols.append_child(renderer,el,child);
}

replicant.core.register_hooks.cljs$core$IFn$_invoke$arity$variadic(impl,child,cljs.core.nth.cljs$core$IFn$_invoke$arity$2(new_children,n_idx),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([vdom,replicant.core.move_node_details], 0));

return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new_children,cljs.core.concat.cljs$core$IFn$_invoke$arity$variadic(cljs.core.take.cljs$core$IFn$_invoke$arity$2(n_idx,cljs.core.next(old_children)),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.first(old_children)], null),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.drop.cljs$core$IFn$_invoke$arity$2((n_idx + (1)),old_children)], 0)),n,(idx - (1))], null);
} else {
var idx = (n + o_dom_idx);
var child = replicant.protocols.get_child(renderer,el,idx);
var corresponding_old_vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(old_children,o_idx);
replicant.protocols.insert_before(renderer,el,child,replicant.protocols.get_child(renderer,el,n));

(replicant.core.reconcile_STAR_.cljs$core$IFn$_invoke$arity$5 ? replicant.core.reconcile_STAR_.cljs$core$IFn$_invoke$arity$5(impl,el,headers,corresponding_old_vdom,n) : replicant.core.reconcile_STAR_.call(null,impl,el,headers,corresponding_old_vdom,n));

if(replicant.core.unchanged_QMARK_(headers,corresponding_old_vdom)){
replicant.core.register_hooks.cljs$core$IFn$_invoke$arity$variadic(impl,child,headers,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([corresponding_old_vdom,replicant.core.move_node_details], 0));
} else {
}

return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.next(new_children),cljs.core.concat.cljs$core$IFn$_invoke$arity$2(cljs.core.take.cljs$core$IFn$_invoke$arity$2(o_idx,old_children),cljs.core.drop.cljs$core$IFn$_invoke$arity$2((o_idx + (1)),old_children)),(n + (1)),((n + o_idx) + (1)),corresponding_old_vdom], null);
}
});
replicant.core.insert_node = (function replicant$core$insert_node(r,el,child,n,n_children){
if((n_children <= n)){
return replicant.protocols.append_child(r,el,child);
} else {
return replicant.protocols.insert_before(r,el,child,replicant.protocols.get_child(r,el,n));
}
});
replicant.core.update_children = (function replicant$core$update_children(impl,el,new_children,new_ks,old_children,old_ks,n_children){
var r = new cljs.core.Keyword(null,"renderer","renderer",336841071).cljs$core$IFn$_invoke$arity$1(impl);
var unmounts = cljs.core.deref(new cljs.core.Keyword(null,"unmounts","unmounts",173366521).cljs$core$IFn$_invoke$arity$1(impl));
var new_c = cljs.core.seq(new_children);
var old_c = cljs.core.seq(old_children);
var n = (0);
var move_n = (0);
var n_children__$1 = (function (){var or__5162__auto__ = n_children;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return (0);
}
})();
var changed_QMARK_ = false;
var vdom = cljs.core.transient$(cljs.core.PersistentVector.EMPTY);
while(true){
var new_headers = cljs.core.first(new_c);
var old_vdom = cljs.core.first(old_c);
var new_empty_QMARK_ = (new_c == null);
var old_empty_QMARK_ = (old_c == null);
var new_nil_QMARK_ = (new_headers == null);
var old_nil_QMARK_ = (old_vdom == null);
if(((new_empty_QMARK_) && (old_empty_QMARK_))){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [changed_QMARK_,cljs.core.persistent_BANG_(vdom),new_ks,n_children__$1], null);
} else {
if(new_empty_QMARK_){
var children = cljs.core.seq(old_c);
var vdom__$1 = vdom;
var n__$1 = n;
var n_children__$2 = n_children__$1;
while(true){
if((children == null)){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [true,cljs.core.persistent_BANG_(vdom__$1),new_ks,n_children__$2], null);
} else {
if((cljs.core.first(children) == null)){
var G__39326 = cljs.core.next(children);
var G__39327 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom__$1,null);
var G__39328 = n__$1;
var G__39329 = n_children__$2;
children = G__39326;
vdom__$1 = G__39327;
n__$1 = G__39328;
n_children__$2 = G__39329;
continue;
} else {
var temp__5821__auto__ = replicant.core.remove_child(impl,unmounts,el,n__$1,cljs.core.first(children));
if(cljs.core.truth_(temp__5821__auto__)){
var pending_vdom = temp__5821__auto__;
var G__39333 = cljs.core.next(children);
var G__39334 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom__$1,pending_vdom);
var G__39336 = (n__$1 + (1));
var G__39338 = n_children__$2;
children = G__39333;
vdom__$1 = G__39334;
n__$1 = G__39336;
n_children__$2 = G__39338;
continue;
} else {
var G__39342 = cljs.core.next(children);
var G__39343 = vdom__$1;
var G__39344 = n__$1;
var G__39345 = (n_children__$2 - (1));
children = G__39342;
vdom__$1 = G__39343;
n__$1 = G__39344;
n_children__$2 = G__39345;
continue;
}

}
}
break;
}
} else {
if(old_empty_QMARK_){
var vec__38578 = replicant.core.insert_children(impl,el,new_c,vdom);
var vdom__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38578,(0),null);
var n__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38578,(1),null);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [true,cljs.core.persistent_BANG_(vdom__$1),new_ks,(n_children__$1 + n__$1)], null);
} else {
if(((new_nil_QMARK_) && (old_nil_QMARK_))){
var G__39350 = cljs.core.next(new_c);
var G__39351 = cljs.core.next(old_c);
var G__39352 = n;
var G__39353 = move_n;
var G__39354 = n_children__$1;
var G__39355 = changed_QMARK_;
var G__39356 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,null);
new_c = G__39350;
old_c = G__39351;
n = G__39352;
move_n = G__39353;
n_children__$1 = G__39354;
changed_QMARK_ = G__39355;
vdom = G__39356;
continue;
} else {
if(cljs.core.truth_((function (){var and__5160__auto__ = old_vdom;
if(cljs.core.truth_(and__5160__auto__)){
return (old_vdom[(9)]);
} else {
return and__5160__auto__;
}
})())){
var vec__38611 = (cljs.core.truth_((function (){var and__5160__auto__ = new_headers;
if(cljs.core.truth_(and__5160__auto__)){
return (!(cljs.core.contains_QMARK_(old_ks,(new_headers[(3)]))));
} else {
return and__5160__auto__;
}
})())?(function (){var res = replicant.core.create_node(impl,new_headers);
replicant.core.insert_node(r,el,cljs.core.first(res),n,n_children__$1);

return res;
})():null);
var child = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38611,(0),null);
var child_vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38611,(1),null);
if(cljs.core.contains_QMARK_(unmounts,(old_vdom[(9)]))){
if(new_nil_QMARK_){
var G__39367 = cljs.core.next(new_c);
var G__39368 = cljs.core.next(old_c);
var G__39369 = (n + (1));
var G__39370 = move_n;
var G__39371 = n_children__$1;
var G__39372 = changed_QMARK_;
var G__39373 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,old_vdom);
new_c = G__39367;
old_c = G__39368;
n = G__39369;
move_n = G__39370;
n_children__$1 = G__39371;
changed_QMARK_ = G__39372;
vdom = G__39373;
continue;
} else {
if(cljs.core.truth_(child)){
var G__39374 = cljs.core.next(new_c);
var G__39375 = cljs.core.next(old_c);
var G__39376 = (n + (2));
var G__39377 = move_n;
var G__39378 = (n_children__$1 + (1));
var G__39379 = true;
var G__39380 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,child_vdom),old_vdom);
new_c = G__39374;
old_c = G__39375;
n = G__39376;
move_n = G__39377;
n_children__$1 = G__39378;
changed_QMARK_ = G__39379;
vdom = G__39380;
continue;
} else {
var G__39381 = new_c;
var G__39382 = cljs.core.next(old_c);
var G__39383 = (n + (1));
var G__39384 = move_n;
var G__39385 = n_children__$1;
var G__39386 = changed_QMARK_;
var G__39387 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,old_vdom);
new_c = G__39381;
old_c = G__39382;
n = G__39383;
move_n = G__39384;
n_children__$1 = G__39385;
changed_QMARK_ = G__39386;
vdom = G__39387;
continue;

}
}
} else {
if(new_nil_QMARK_){
var G__39388 = cljs.core.next(new_c);
var G__39389 = cljs.core.next(old_c);
var G__39390 = n;
var G__39391 = (move_n - (1));
var G__39392 = (n_children__$1 - (1));
var G__39393 = changed_QMARK_;
var G__39394 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,null);
new_c = G__39388;
old_c = G__39389;
n = G__39390;
move_n = G__39391;
n_children__$1 = G__39392;
changed_QMARK_ = G__39393;
vdom = G__39394;
continue;
} else {
if(cljs.core.truth_(child)){
var G__39395 = cljs.core.next(new_c);
var G__39396 = cljs.core.next(old_c);
var G__39397 = (n + (1));
var G__39398 = move_n;
var G__39399 = n_children__$1;
var G__39400 = true;
var G__39401 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,child_vdom);
new_c = G__39395;
old_c = G__39396;
n = G__39397;
move_n = G__39398;
n_children__$1 = G__39399;
changed_QMARK_ = G__39400;
vdom = G__39401;
continue;
} else {
var G__39402 = new_c;
var G__39403 = cljs.core.next(old_c);
var G__39404 = n;
var G__39405 = (move_n - (1));
var G__39406 = (n_children__$1 - (1));
var G__39407 = changed_QMARK_;
var G__39408 = vdom;
new_c = G__39402;
old_c = G__39403;
n = G__39404;
move_n = G__39405;
n_children__$1 = G__39406;
changed_QMARK_ = G__39407;
vdom = G__39408;
continue;

}
}
}
} else {
if(new_nil_QMARK_){
if(cljs.core.contains_QMARK_(new_ks,(old_vdom[(1)]))){
var G__39409 = cljs.core.next(new_c);
var G__39410 = old_c;
var G__39411 = n;
var G__39412 = move_n;
var G__39413 = n_children__$1;
var G__39414 = true;
var G__39415 = vdom;
new_c = G__39409;
old_c = G__39410;
n = G__39411;
move_n = G__39412;
n_children__$1 = G__39413;
changed_QMARK_ = G__39414;
vdom = G__39415;
continue;
} else {
var temp__5821__auto__ = replicant.core.remove_child(impl,unmounts,el,n,old_vdom);
if(cljs.core.truth_(temp__5821__auto__)){
var unmounting_node = temp__5821__auto__;
var G__39416 = cljs.core.next(new_c);
var G__39417 = cljs.core.next(old_c);
var G__39418 = (n + (1));
var G__39419 = move_n;
var G__39420 = n_children__$1;
var G__39421 = true;
var G__39422 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,unmounting_node);
new_c = G__39416;
old_c = G__39417;
n = G__39418;
move_n = G__39419;
n_children__$1 = G__39420;
changed_QMARK_ = G__39421;
vdom = G__39422;
continue;
} else {
var G__39423 = cljs.core.next(new_c);
var G__39424 = cljs.core.next(old_c);
var G__39425 = n;
var G__39426 = move_n;
var G__39427 = (n_children__$1 - (1));
var G__39428 = true;
var G__39429 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,null);
new_c = G__39423;
old_c = G__39424;
n = G__39425;
move_n = G__39426;
n_children__$1 = G__39427;
changed_QMARK_ = G__39428;
vdom = G__39429;
continue;
}
}
} else {
if(cljs.core.truth_((function (){var and__5160__auto__ = old_vdom;
if(cljs.core.truth_(and__5160__auto__)){
return replicant.core.reusable_QMARK_(new_headers,old_vdom);
} else {
return and__5160__auto__;
}
})())){
var new_vdom = (replicant.core.reconcile_STAR_.cljs$core$IFn$_invoke$arity$5 ? replicant.core.reconcile_STAR_.cljs$core$IFn$_invoke$arity$5(impl,el,new_headers,old_vdom,n) : replicant.core.reconcile_STAR_.call(null,impl,el,new_headers,old_vdom,n));
var node_unchanged_QMARK_ = replicant.core.unchanged_QMARK_(new_headers,old_vdom);
if(((node_unchanged_QMARK_) && ((n < move_n)))){
replicant.core.register_hooks.cljs$core$IFn$_invoke$arity$variadic(impl,replicant.protocols.get_child(r,el,n),new_headers,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_vdom,replicant.core.move_node_details], 0));
} else {
}

var G__39430 = cljs.core.next(new_c);
var G__39431 = cljs.core.next(old_c);
var G__39432 = (n + (1));
var G__39433 = move_n;
var G__39434 = n_children__$1;
var G__39435 = (function (){var or__5162__auto__ = changed_QMARK_;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return (!(node_unchanged_QMARK_));
}
})();
var G__39436 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,new_vdom);
new_c = G__39430;
old_c = G__39431;
n = G__39432;
move_n = G__39433;
n_children__$1 = G__39434;
changed_QMARK_ = G__39435;
vdom = G__39436;
continue;
} else {
if((!(cljs.core.contains_QMARK_(old_ks,(new_headers[(3)]))))){
var vec__38693 = replicant.core.create_node(impl,new_headers);
var child = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38693,(0),null);
var child_vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38693,(1),null);
replicant.core.insert_node(r,el,child,n,n_children__$1);

var G__39437 = cljs.core.next(new_c);
var G__39438 = (function (){var G__38701 = old_c;
if((old_vdom == null)){
return cljs.core.next(G__38701);
} else {
return G__38701;
}
})();
var G__39439 = (n + (1));
var G__39440 = move_n;
var G__39441 = (n_children__$1 + (1));
var G__39442 = true;
var G__39443 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,child_vdom);
new_c = G__39437;
old_c = G__39438;
n = G__39439;
move_n = G__39440;
n_children__$1 = G__39441;
changed_QMARK_ = G__39442;
vdom = G__39443;
continue;
} else {
if(((old_nil_QMARK_) || ((!(cljs.core.contains_QMARK_(new_ks,(old_vdom[(1)]))))))){
if(old_nil_QMARK_){
var G__39444 = new_c;
var G__39445 = cljs.core.next(old_c);
var G__39446 = n;
var G__39447 = move_n;
var G__39448 = n_children__$1;
var G__39449 = changed_QMARK_;
var G__39450 = vdom;
new_c = G__39444;
old_c = G__39445;
n = G__39446;
move_n = G__39447;
n_children__$1 = G__39448;
changed_QMARK_ = G__39449;
vdom = G__39450;
continue;
} else {
var temp__5821__auto__ = replicant.core.remove_child(impl,unmounts,el,n,old_vdom);
if(cljs.core.truth_(temp__5821__auto__)){
var unmounting_node = temp__5821__auto__;
var G__39451 = new_c;
var G__39452 = cljs.core.next(old_c);
var G__39453 = (n + (1));
var G__39454 = move_n;
var G__39455 = n_children__$1;
var G__39456 = true;
var G__39457 = cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(vdom,unmounting_node);
new_c = G__39451;
old_c = G__39452;
n = G__39453;
move_n = G__39454;
n_children__$1 = G__39455;
changed_QMARK_ = G__39456;
vdom = G__39457;
continue;
} else {
var G__39458 = new_c;
var G__39459 = cljs.core.next(old_c);
var G__39460 = n;
var G__39461 = move_n;
var G__39462 = (n_children__$1 - (1));
var G__39463 = true;
var G__39464 = vdom;
new_c = G__39458;
old_c = G__39459;
n = G__39460;
move_n = G__39461;
n_children__$1 = G__39462;
changed_QMARK_ = G__39463;
vdom = G__39464;
continue;
}
}
} else {
var vec__38719 = replicant.core.move_nodes(impl,el,new_headers,new_c,old_vdom,old_c,n,n_children__$1);
var nc = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38719,(0),null);
var oc = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38719,(1),null);
var n__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38719,(2),null);
var move_n__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38719,(3),null);
var vdom_node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38719,(4),null);
var G__39465 = nc;
var G__39466 = oc;
var G__39467 = n__$1;
var G__39468 = move_n__$1;
var G__39469 = n_children__$1;
var G__39470 = true;
var G__39471 = (function (){var G__38734 = vdom;
if(cljs.core.truth_(vdom_node)){
return cljs.core.conj_BANG_.cljs$core$IFn$_invoke$arity$2(G__38734,vdom_node);
} else {
return G__38734;
}
})();
new_c = G__39465;
old_c = G__39466;
n = G__39467;
move_n = G__39468;
n_children__$1 = G__39469;
changed_QMARK_ = G__39470;
vdom = G__39471;
continue;

}
}
}
}
}
}
}
}
}
break;
}
});
replicant.core.reconcile_STAR_ = (function replicant$core$reconcile_STAR_(p__38776,el,headers,vdom,index){
var map__38797 = p__38776;
var map__38797__$1 = cljs.core.__destructure_map(map__38797);
var impl = map__38797__$1;
var renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38797__$1,new cljs.core.Keyword(null,"renderer","renderer",336841071));
if(cljs.core.truth_(headers)){
var temp__5823__auto___39472 = (function (){var or__5162__auto__ = new cljs.core.Keyword("replicant","context","replicant/context",-909059467).cljs$core$IFn$_invoke$arity$1((headers[(4)]));
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return new cljs.core.Keyword("replicant","context","replicant/context",-909059467).cljs$core$IFn$_invoke$arity$1(cljs.core.meta((headers[(7)])));
}
})();
if(cljs.core.truth_(temp__5823__auto___39472)){
var ctx__37309__auto___39473 = temp__5823__auto___39472;
cljs.core.reset_BANG_(replicant.assert.current_context,ctx__37309__auto___39473);
} else {
}

cljs.core.reset_BANG_(replicant.assert.current_node,(headers[(7)]));
} else {
}

if((!((!(replicant.asserts.has_bad_conditional_attrs_QMARK_(vdom,headers)))))){
var fn__37317__auto___39474 = new cljs.core.Keyword(null,"fn-name","fn-name",-766594004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var alias__37318__auto___39475 = new cljs.core.Keyword(null,"alias","alias",-2039751630).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
var fd__37319__auto___39476 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(replicant.assert.current_context));
cljs.core.reset_BANG_(replicant.assert.error,(function (){var G__38798 = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"hiccup","hiccup",1218876238),(function (){var or__5162__auto__ = null;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.deref(replicant.assert.current_node);
}
})(),new cljs.core.Keyword(null,"title","title",636505583),"Avoid conditionals around the attribute map",new cljs.core.Keyword(null,"message","message",-406056002),replicant.asserts.convey_bad_conditional_attributes(vdom,headers)], null);
var G__38798__$1 = (cljs.core.truth_(fn__37317__auto___39474)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38798,new cljs.core.Keyword(null,"fname","fname",1500291491),fn__37317__auto___39474):G__38798);
var G__38798__$2 = (cljs.core.truth_(alias__37318__auto___39475)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38798__$1,new cljs.core.Keyword(null,"alias","alias",-2039751630),alias__37318__auto___39475):G__38798__$1);
if(cljs.core.truth_(fd__37319__auto___39476)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__38798__$2,new cljs.core.Keyword(null,"data","data",-232669377),fd__37319__auto___39476);
} else {
return G__38798__$2;
}
})());
} else {
}

var or__5162__auto__ = ((replicant.core.unchanged_QMARK_(headers,vdom))?vdom:null);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
var or__5162__auto____$1 = (function (){var temp__5823__auto__ = replicant.core.get_alias_headers(impl,headers);
if(cljs.core.truth_(temp__5823__auto__)){
var alias_headers = temp__5823__auto__;
var vdom_child = cljs.core.first((vdom[(4)]));
var updated_vdom = (cljs.core.truth_(replicant.core.reusable_QMARK_(alias_headers,vdom_child))?(replicant.core.reconcile_STAR_.cljs$core$IFn$_invoke$arity$5 ? replicant.core.reconcile_STAR_.cljs$core$IFn$_invoke$arity$5(impl,el,alias_headers,vdom_child,index) : replicant.core.reconcile_STAR_.call(null,impl,el,alias_headers,vdom_child,index)):(function (){var vec__38799 = replicant.core.create_node(impl,alias_headers);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38799,(0),null);
var updated_vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38799,(1),null);
replicant.protocols.replace_child(renderer,el,node,replicant.protocols.get_child(renderer,el,index));

return updated_vdom;
})());
var headers__37545__auto__ = headers;
return (new Array((headers__37545__auto__[(0)]),(headers__37545__auto__[(3)]),(headers__37545__auto__[(2)]),(headers[(4)]),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [updated_vdom], null),(function (){var temp__5823__auto____$1 = (updated_vdom[(1)]);
if(cljs.core.truth_(temp__5823__auto____$1)){
var k = temp__5823__auto____$1;
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [k], null);
} else {
return null;
}
})(),cljs.core.boolean$(new cljs.core.Keyword("replicant","unmounting","replicant/unmounting",1629433009).cljs$core$IFn$_invoke$arity$1((headers__37545__auto__[(4)]))),(headers__37545__auto__[(7)]),null,null,(1)));
} else {
return null;
}
})();
if(cljs.core.truth_(or__5162__auto____$1)){
return or__5162__auto____$1;
} else {
var or__5162__auto____$2 = ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2((headers[(8)]),(vdom[(8)])))?(function (){var vec__38802 = replicant.core.create_node(impl,headers);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38802,(0),null);
var vdom__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38802,(1),null);
replicant.protocols.replace_child(renderer,el,node,replicant.protocols.get_child(renderer,el,index));

return vdom__$1;
})():null);
if(cljs.core.truth_(or__5162__auto____$2)){
return or__5162__auto____$2;
} else {
var child = replicant.protocols.get_child(renderer,el,index);
var headers__$1 = (function (){var or__5162__auto____$3 = replicant.core.get_alias_headers(impl,headers);
if(cljs.core.truth_(or__5162__auto____$3)){
return or__5162__auto____$3;
} else {
return headers;
}
})();
var attrs = replicant.core.get_attrs(headers__$1);
var vdom_attrs = (vdom[(3)]);
var attrs_changed_QMARK_ = replicant.core.reconcile_attributes(renderer,child,attrs,vdom_attrs);
var vec__38805 = (cljs.core.truth_(new cljs.core.Keyword(null,"innerHTML","innerHTML",-1856751343).cljs$core$IFn$_invoke$arity$1((headers__$1[(4)])))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [null,null,true], null):replicant.core.get_children_ks(headers__$1,replicant.core.get_ns(headers__$1)));
var new_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38805,(0),null);
var new_ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38805,(1),null);
var inner_html_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38805,(2),null);
var vec__38808 = (cljs.core.truth_(new cljs.core.Keyword(null,"contenteditable","contenteditable",-770210530).cljs$core$IFn$_invoke$arity$1(vdom_attrs))?(function (){
replicant.protocols.remove_all_children(renderer,child);

return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [null,null,(0)], null);
})()
:(cljs.core.truth_(inner_html_QMARK_)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [null,null,(0)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [(vdom[(4)]),(vdom[(5)]),(vdom[(10)])], null)
));
var old_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38808,(0),null);
var old_ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38808,(1),null);
var old_nc = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38808,(2),null);
var vec__38811 = replicant.core.update_children(impl,child,new_children,new_ks,old_children,old_ks,old_nc);
var children_changed_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38811,(0),null);
var children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38811,(1),null);
var child_ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38811,(2),null);
var n_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38811,(3),null);
var attrs_changed_QMARK___$1 = ((attrs_changed_QMARK_) || (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("replicant","on-render","replicant/on-render",1674377901).cljs$core$IFn$_invoke$arity$1((headers__$1[(4)])),new cljs.core.Keyword("replicant","on-render","replicant/on-render",1674377901).cljs$core$IFn$_invoke$arity$1(vdom_attrs))));
replicant.core.register_hooks.cljs$core$IFn$_invoke$arity$variadic(impl,child,headers__$1,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([vdom,(cljs.core.truth_((function (){var and__5160__auto__ = attrs_changed_QMARK___$1;
if(and__5160__auto__){
return children_changed_QMARK_;
} else {
return and__5160__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replicant","updated-attrs","replicant/updated-attrs",295362895),new cljs.core.Keyword("replicant","updated-children","replicant/updated-children",2041302229)], null):((attrs_changed_QMARK___$1)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replicant","updated-attrs","replicant/updated-attrs",295362895)], null):new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("replicant","updated-children","replicant/updated-children",2041302229)], null)
))], 0));

var headers__37545__auto__ = headers__$1;
return (new Array((headers__37545__auto__[(0)]),(headers__37545__auto__[(3)]),(headers__37545__auto__[(2)]),attrs,children,child_ks,cljs.core.boolean$(new cljs.core.Keyword("replicant","unmounting","replicant/unmounting",1629433009).cljs$core$IFn$_invoke$arity$1((headers__37545__auto__[(4)]))),(headers__37545__auto__[(7)]),null,null,n_children));
}
}
}
});
replicant.core.perform_post_mount_update = (function replicant$core$perform_post_mount_update(renderer,p__38814){
var vec__38815 = p__38814;
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38815,(0),null);
var mounting_attrs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38815,(1),null);
var attrs = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38815,(2),null);
return replicant.core.update_attributes(renderer,node,attrs,mounting_attrs);
});
replicant.core.get_hooks_to_call = (function replicant$core$get_hooks_to_call(p__38819){
var map__38820 = p__38819;
var map__38820__$1 = cljs.core.__destructure_map(map__38820);
var renderer = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38820__$1,new cljs.core.Keyword(null,"renderer","renderer",336841071));
var hooks = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38820__$1,new cljs.core.Keyword(null,"hooks","hooks",-413590103));
var unmount_hooks = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38820__$1,new cljs.core.Keyword(null,"unmount-hooks","unmount-hooks",-1376824226));
var potential_unmounts = cljs.core.deref(unmount_hooks);
var hooks_to_call = cljs.core.deref(hooks);
var unmounted_nodes = cljs.core.remove.cljs$core$IFn$_invoke$arity$2(cljs.core.set(cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (p__38821){
var vec__38822 = p__38821;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38822,(0),null);
var ___$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38822,(1),null);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38822,(2),null);
return node;
}),hooks_to_call)),cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__38818_SHARP_){
return replicant.protocols.attached_QMARK_(renderer,p1__38818_SHARP_);
}),cljs.core.keys(potential_unmounts)));
if(cljs.core.truth_(unmounted_nodes)){
cljs.core._vreset_BANG_(unmount_hooks,(function (h){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$3(cljs.core.dissoc,h,unmounted_nodes);
})(cljs.core._deref(unmount_hooks)));
} else {
}

return cljs.core.into.cljs$core$IFn$_invoke$arity$2(hooks_to_call,cljs.core.vals(cljs.core.select_keys(potential_unmounts,unmounted_nodes)));
});
/**
 * Reconcile the DOM in `el` by diffing `hiccup` with `vdom`. If there is no
 *   `vdom`, `reconcile` will create the DOM as per `hiccup`. Assumes that the DOM
 *   in `el` is in sync with `vdom` - if not, this will certainly not produce the
 *   desired result.
 */
replicant.core.reconcile = (function replicant$core$reconcile(var_args){
var args__5903__auto__ = [];
var len__5897__auto___39503 = arguments.length;
var i__5898__auto___39504 = (0);
while(true){
if((i__5898__auto___39504 < len__5897__auto___39503)){
args__5903__auto__.push((arguments[i__5898__auto___39504]));

var G__39505 = (i__5898__auto___39504 + (1));
i__5898__auto___39504 = G__39505;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((3) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((3)),(0),null)):null);
return replicant.core.reconcile.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__5904__auto__);
});

(replicant.core.reconcile.cljs$core$IFn$_invoke$arity$variadic = (function (renderer,el,hiccup,p__38834){
var vec__38835 = p__38834;
var vdom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38835,(0),null);
var map__38838 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38835,(1),null);
var map__38838__$1 = cljs.core.__destructure_map(map__38838);
var unmounts = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38838__$1,new cljs.core.Keyword(null,"unmounts","unmounts",173366521));
var unmount_hooks = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38838__$1,new cljs.core.Keyword(null,"unmount-hooks","unmount-hooks",-1376824226));
var aliases = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38838__$1,new cljs.core.Keyword(null,"aliases","aliases",1346874714));
var alias_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38838__$1,new cljs.core.Keyword(null,"alias-data","alias-data",1743863930));
var on_alias_exception = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38838__$1,new cljs.core.Keyword(null,"on-alias-exception","on-alias-exception",1142240043));
var impl = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"renderer","renderer",336841071),renderer,new cljs.core.Keyword(null,"hooks","hooks",-413590103),cljs.core.volatile_BANG_(cljs.core.PersistentVector.EMPTY),new cljs.core.Keyword(null,"mounts","mounts",-791474425),cljs.core.volatile_BANG_(cljs.core.PersistentVector.EMPTY),new cljs.core.Keyword(null,"unmount-hooks","unmount-hooks",-1376824226),(function (){var or__5162__auto__ = unmount_hooks;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.volatile_BANG_(replicant.core.node_map());
}
})(),new cljs.core.Keyword(null,"unmounts","unmounts",173366521),(function (){var or__5162__auto__ = unmounts;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.volatile_BANG_(cljs.core.PersistentHashSet.EMPTY);
}
})(),new cljs.core.Keyword(null,"aliases","aliases",1346874714),aliases,new cljs.core.Keyword(null,"alias-data","alias-data",1743863930),alias_data,new cljs.core.Keyword(null,"on-alias-exception","on-alias-exception",1142240043),on_alias_exception], null);
var vdom__$1 = ((replicant.core.proper_seq_QMARK_(hiccup))?(function (){var vec__38839 = replicant.core.get_children_ks((function (){var pt__36985__auto__ = [null,null,null];
var G__38842 = pt__36985__auto__;
G__38842.push((function (){var temp__5823__auto__ = new cljs.core.Keyword("replicant","key","replicant/key",-670108117).cljs$core$IFn$_invoke$arity$1(null);
if(cljs.core.truth_(temp__5823__auto__)){
var k__36970__auto__ = temp__5823__auto__;
return replicant.hiccup_headers.make_key((pt__36985__auto__[(0)]),k__36970__auto__);
} else {
return null;
}
})());

G__38842.push(null);

G__38842.push(hiccup);

G__38842.push(null);

G__38842.push(null);

G__38842.push(null);

G__38842.push(null);

return G__38842;
})(),null);
var children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38839,(0),null);
var ks = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38839,(1),null);
return cljs.core.second(replicant.core.update_children(impl,el,children,ks,vdom,cljs.core.set(cljs.core.keep.cljs$core$IFn$_invoke$arity$2((function (p1__38825_SHARP_){
var G__38843 = p1__38825_SHARP_;
if((G__38843 == null)){
return null;
} else {
return (G__38843[(1)]);
}
}),vdom)),cljs.core.count(vdom)));
})():(function (){var headers = replicant.core.get_hiccup_headers(null,hiccup);
if(cljs.core.truth_(headers)){
var temp__5823__auto___39523 = (function (){var or__5162__auto__ = new cljs.core.Keyword("replicant","context","replicant/context",-909059467).cljs$core$IFn$_invoke$arity$1((headers[(4)]));
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return new cljs.core.Keyword("replicant","context","replicant/context",-909059467).cljs$core$IFn$_invoke$arity$1(cljs.core.meta((headers[(7)])));
}
})();
if(cljs.core.truth_(temp__5823__auto___39523)){
var ctx__37309__auto___39524 = temp__5823__auto___39523;
cljs.core.reset_BANG_(replicant.assert.current_context,ctx__37309__auto___39524);
} else {
}

cljs.core.reset_BANG_(replicant.assert.current_node,(headers[(7)]));
} else {
}

if(cljs.core.truth_((function (){var and__5160__auto__ = headers;
if(cljs.core.truth_(and__5160__auto__)){
var and__5160__auto____$1 = vdom;
if(cljs.core.truth_(and__5160__auto____$1)){
return ((replicant.core.unchanged_QMARK_(headers,cljs.core.first(vdom))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((1),cljs.core.count(vdom))));
} else {
return and__5160__auto____$1;
}
} else {
return and__5160__auto__;
}
})())){
return vdom;
} else {
var k = (cljs.core.truth_(headers)?(headers[(3)]):null);
return cljs.core.second(replicant.core.update_children(impl,el,(cljs.core.truth_(headers)?new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [headers], null):null),(function (){var G__38844 = cljs.core.PersistentHashSet.EMPTY;
if(cljs.core.truth_(k)){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(G__38844,k);
} else {
return G__38844;
}
})(),vdom,cljs.core.set(cljs.core.keep.cljs$core$IFn$_invoke$arity$2((function (p1__38826_SHARP_){
return (p1__38826_SHARP_[(1)]);
}),vdom)),(cljs.core.truth_(cljs.core.first(vdom))?(1):(0))));
}
})());
var hooks_to_call = replicant.core.get_hooks_to_call(impl);
var temp__5821__auto___39535 = cljs.core.seq(cljs.core.deref(new cljs.core.Keyword(null,"mounts","mounts",-791474425).cljs$core$IFn$_invoke$arity$1(impl)));
if(temp__5821__auto___39535){
var mounts_39536 = temp__5821__auto___39535;
replicant.protocols.next_frame(renderer,(function (){
cljs.core.run_BANG_((function (p1__38827_SHARP_){
return replicant.core.perform_post_mount_update(renderer,p1__38827_SHARP_);
}),mounts_39536);

return cljs.core.run_BANG_((function (p1__38828_SHARP_){
return replicant.core.call_hook(renderer,p1__38828_SHARP_);
}),hooks_to_call);
}));
} else {
cljs.core.run_BANG_((function (p1__38829_SHARP_){
return replicant.core.call_hook(renderer,p1__38829_SHARP_);
}),hooks_to_call);
}

return new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"hooks","hooks",-413590103),hooks_to_call,new cljs.core.Keyword(null,"vdom","vdom",953162958),vdom__$1,new cljs.core.Keyword(null,"unmounts","unmounts",173366521),new cljs.core.Keyword(null,"unmounts","unmounts",173366521).cljs$core$IFn$_invoke$arity$1(impl),new cljs.core.Keyword(null,"unmount-hooks","unmount-hooks",-1376824226),new cljs.core.Keyword(null,"unmount-hooks","unmount-hooks",-1376824226).cljs$core$IFn$_invoke$arity$1(impl)], null);
}));

(replicant.core.reconcile.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(replicant.core.reconcile.cljs$lang$applyTo = (function (seq38830){
var G__38831 = cljs.core.first(seq38830);
var seq38830__$1 = cljs.core.next(seq38830);
var G__38832 = cljs.core.first(seq38830__$1);
var seq38830__$2 = cljs.core.next(seq38830__$1);
var G__38833 = cljs.core.first(seq38830__$2);
var seq38830__$3 = cljs.core.next(seq38830__$2);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38831,G__38832,G__38833,seq38830__$3);
}));

replicant.assert.add_reporter(new cljs.core.Keyword("replicant.assert","default","replicant.assert/default",-504441266),replicant.console_logger.report);

//# sourceMappingURL=replicant.core.js.map
