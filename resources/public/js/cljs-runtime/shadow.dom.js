goog.provide('shadow.dom');
shadow.dom.transition_supported_QMARK_ = true;

/**
 * @interface
 */
shadow.dom.IElement = function(){};

var shadow$dom$IElement$_to_dom$dyn_31243 = (function (this$){
var x__5519__auto__ = (((this$ == null))?null:this$);
var m__5520__auto__ = (shadow.dom._to_dom[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5520__auto__.call(null,this$));
} else {
var m__5518__auto__ = (shadow.dom._to_dom["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5518__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IElement.-to-dom",this$);
}
}
});
shadow.dom._to_dom = (function shadow$dom$_to_dom(this$){
if((((!((this$ == null)))) && ((!((this$.shadow$dom$IElement$_to_dom$arity$1 == null)))))){
return this$.shadow$dom$IElement$_to_dom$arity$1(this$);
} else {
return shadow$dom$IElement$_to_dom$dyn_31243(this$);
}
});


/**
 * @interface
 */
shadow.dom.SVGElement = function(){};

var shadow$dom$SVGElement$_to_svg$dyn_31250 = (function (this$){
var x__5519__auto__ = (((this$ == null))?null:this$);
var m__5520__auto__ = (shadow.dom._to_svg[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5520__auto__.call(null,this$));
} else {
var m__5518__auto__ = (shadow.dom._to_svg["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__5518__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("SVGElement.-to-svg",this$);
}
}
});
shadow.dom._to_svg = (function shadow$dom$_to_svg(this$){
if((((!((this$ == null)))) && ((!((this$.shadow$dom$SVGElement$_to_svg$arity$1 == null)))))){
return this$.shadow$dom$SVGElement$_to_svg$arity$1(this$);
} else {
return shadow$dom$SVGElement$_to_svg$dyn_31250(this$);
}
});

shadow.dom.lazy_native_coll_seq = (function shadow$dom$lazy_native_coll_seq(coll,idx){
if((idx < coll.length)){
return (new cljs.core.LazySeq(null,(function (){
return cljs.core.cons((coll[idx]),(function (){var G__29678 = coll;
var G__29679 = (idx + (1));
return (shadow.dom.lazy_native_coll_seq.cljs$core$IFn$_invoke$arity$2 ? shadow.dom.lazy_native_coll_seq.cljs$core$IFn$_invoke$arity$2(G__29678,G__29679) : shadow.dom.lazy_native_coll_seq.call(null,G__29678,G__29679));
})());
}),null,null));
} else {
return null;
}
});

/**
* @constructor
 * @implements {cljs.core.IIndexed}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IDeref}
 * @implements {shadow.dom.IElement}
*/
shadow.dom.NativeColl = (function (coll){
this.coll = coll;
this.cljs$lang$protocol_mask$partition0$ = 8421394;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(shadow.dom.NativeColl.prototype.cljs$core$IDeref$_deref$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll;
}));

(shadow.dom.NativeColl.prototype.cljs$core$IIndexed$_nth$arity$2 = (function (this$,n){
var self__ = this;
var this$__$1 = this;
return (self__.coll[n]);
}));

(shadow.dom.NativeColl.prototype.cljs$core$IIndexed$_nth$arity$3 = (function (this$,n,not_found){
var self__ = this;
var this$__$1 = this;
var or__5162__auto__ = (self__.coll[n]);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return not_found;
}
}));

(shadow.dom.NativeColl.prototype.cljs$core$ICounted$_count$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll.length;
}));

(shadow.dom.NativeColl.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return shadow.dom.lazy_native_coll_seq(self__.coll,(0));
}));

(shadow.dom.NativeColl.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.dom.NativeColl.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll;
}));

(shadow.dom.NativeColl.getBasis = (function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"coll","coll",-1006698606,null)], null);
}));

(shadow.dom.NativeColl.cljs$lang$type = true);

(shadow.dom.NativeColl.cljs$lang$ctorStr = "shadow.dom/NativeColl");

(shadow.dom.NativeColl.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"shadow.dom/NativeColl");
}));

/**
 * Positional factory function for shadow.dom/NativeColl.
 */
shadow.dom.__GT_NativeColl = (function shadow$dom$__GT_NativeColl(coll){
return (new shadow.dom.NativeColl(coll));
});

shadow.dom.native_coll = (function shadow$dom$native_coll(coll){
return (new shadow.dom.NativeColl(coll));
});
shadow.dom.dom_node = (function shadow$dom$dom_node(el){
if((el == null)){
return null;
} else {
if((((!((el == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === el.shadow$dom$IElement$))))?true:false):false)){
return el.shadow$dom$IElement$_to_dom$arity$1(null);
} else {
if(typeof el === 'string'){
return document.createTextNode(el);
} else {
if(typeof el === 'number'){
return document.createTextNode((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(el)));
} else {
return el;

}
}
}
}
});
shadow.dom.query_one = (function shadow$dom$query_one(var_args){
var G__29715 = arguments.length;
switch (G__29715) {
case 1:
return shadow.dom.query_one.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.query_one.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.query_one.cljs$core$IFn$_invoke$arity$1 = (function (sel){
return document.querySelector(sel);
}));

(shadow.dom.query_one.cljs$core$IFn$_invoke$arity$2 = (function (sel,root){
return shadow.dom.dom_node(root).querySelector(sel);
}));

(shadow.dom.query_one.cljs$lang$maxFixedArity = 2);

shadow.dom.query = (function shadow$dom$query(var_args){
var G__29720 = arguments.length;
switch (G__29720) {
case 1:
return shadow.dom.query.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.query.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.query.cljs$core$IFn$_invoke$arity$1 = (function (sel){
return (new shadow.dom.NativeColl(document.querySelectorAll(sel)));
}));

(shadow.dom.query.cljs$core$IFn$_invoke$arity$2 = (function (sel,root){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(root).querySelectorAll(sel)));
}));

(shadow.dom.query.cljs$lang$maxFixedArity = 2);

shadow.dom.by_id = (function shadow$dom$by_id(var_args){
var G__29727 = arguments.length;
switch (G__29727) {
case 2:
return shadow.dom.by_id.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return shadow.dom.by_id.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.by_id.cljs$core$IFn$_invoke$arity$2 = (function (id,el){
return shadow.dom.dom_node(el).getElementById(id);
}));

(shadow.dom.by_id.cljs$core$IFn$_invoke$arity$1 = (function (id){
return document.getElementById(id);
}));

(shadow.dom.by_id.cljs$lang$maxFixedArity = 2);

shadow.dom.build = shadow.dom.dom_node;
shadow.dom.ev_stop = (function shadow$dom$ev_stop(var_args){
var G__29735 = arguments.length;
switch (G__29735) {
case 1:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 4:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1 = (function (e){
if(cljs.core.truth_(e.stopPropagation)){
e.stopPropagation();

e.preventDefault();
} else {
(e.cancelBubble = true);

(e.returnValue = false);
}

return e;
}));

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$2 = (function (e,el){
shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1(e);

return el;
}));

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$4 = (function (e,el,scope,owner){
shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1(e);

return el;
}));

(shadow.dom.ev_stop.cljs$lang$maxFixedArity = 4);

/**
 * check wether a parent node (or the document) contains the child
 */
shadow.dom.contains_QMARK_ = (function shadow$dom$contains_QMARK_(var_args){
var G__29757 = arguments.length;
switch (G__29757) {
case 1:
return shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$1 = (function (el){
return goog.dom.contains(document,shadow.dom.dom_node(el));
}));

(shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$2 = (function (parent,el){
return goog.dom.contains(shadow.dom.dom_node(parent),shadow.dom.dom_node(el));
}));

(shadow.dom.contains_QMARK_.cljs$lang$maxFixedArity = 2);

shadow.dom.add_class = (function shadow$dom$add_class(el,cls){
return goog.dom.classlist.add(shadow.dom.dom_node(el),cls);
});
shadow.dom.remove_class = (function shadow$dom$remove_class(el,cls){
return goog.dom.classlist.remove(shadow.dom.dom_node(el),cls);
});
shadow.dom.toggle_class = (function shadow$dom$toggle_class(var_args){
var G__29782 = arguments.length;
switch (G__29782) {
case 2:
return shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$2 = (function (el,cls){
return goog.dom.classlist.toggle(shadow.dom.dom_node(el),cls);
}));

(shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$3 = (function (el,cls,v){
if(cljs.core.truth_(v)){
return shadow.dom.add_class(el,cls);
} else {
return shadow.dom.remove_class(el,cls);
}
}));

(shadow.dom.toggle_class.cljs$lang$maxFixedArity = 3);

shadow.dom.dom_listen = (cljs.core.truth_((function (){var or__5162__auto__ = (!((typeof document !== 'undefined')));
if(or__5162__auto__){
return or__5162__auto__;
} else {
return document.addEventListener;
}
})())?(function shadow$dom$dom_listen_good(el,ev,handler){
return el.addEventListener(ev,handler,false);
}):(function shadow$dom$dom_listen_ie(el,ev,handler){
try{return el.attachEvent((""+"on"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ev)),(function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
}));
}catch (e29797){if((e29797 instanceof Object)){
var e = e29797;
return console.log("didnt support attachEvent",el,e);
} else {
throw e29797;

}
}}));
shadow.dom.dom_listen_remove = (cljs.core.truth_((function (){var or__5162__auto__ = (!((typeof document !== 'undefined')));
if(or__5162__auto__){
return or__5162__auto__;
} else {
return document.removeEventListener;
}
})())?(function shadow$dom$dom_listen_remove_good(el,ev,handler){
return el.removeEventListener(ev,handler,false);
}):(function shadow$dom$dom_listen_remove_ie(el,ev,handler){
return el.detachEvent((""+"on"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ev)),handler);
}));
shadow.dom.on_query = (function shadow$dom$on_query(root_el,ev,selector,handler){
var seq__29807 = cljs.core.seq(shadow.dom.query.cljs$core$IFn$_invoke$arity$2(selector,root_el));
var chunk__29808 = null;
var count__29809 = (0);
var i__29810 = (0);
while(true){
if((i__29810 < count__29809)){
var el = chunk__29808.cljs$core$IIndexed$_nth$arity$2(null,i__29810);
var handler_31345__$1 = ((function (seq__29807,chunk__29808,count__29809,i__29810,el){
return (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});})(seq__29807,chunk__29808,count__29809,i__29810,el))
;
shadow.dom.dom_listen(el,cljs.core.name(ev),handler_31345__$1);


var G__31346 = seq__29807;
var G__31347 = chunk__29808;
var G__31348 = count__29809;
var G__31349 = (i__29810 + (1));
seq__29807 = G__31346;
chunk__29808 = G__31347;
count__29809 = G__31348;
i__29810 = G__31349;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__29807);
if(temp__5823__auto__){
var seq__29807__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__29807__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__29807__$1);
var G__31352 = cljs.core.chunk_rest(seq__29807__$1);
var G__31353 = c__5694__auto__;
var G__31354 = cljs.core.count(c__5694__auto__);
var G__31355 = (0);
seq__29807 = G__31352;
chunk__29808 = G__31353;
count__29809 = G__31354;
i__29810 = G__31355;
continue;
} else {
var el = cljs.core.first(seq__29807__$1);
var handler_31356__$1 = ((function (seq__29807,chunk__29808,count__29809,i__29810,el,seq__29807__$1,temp__5823__auto__){
return (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});})(seq__29807,chunk__29808,count__29809,i__29810,el,seq__29807__$1,temp__5823__auto__))
;
shadow.dom.dom_listen(el,cljs.core.name(ev),handler_31356__$1);


var G__31357 = cljs.core.next(seq__29807__$1);
var G__31358 = null;
var G__31359 = (0);
var G__31360 = (0);
seq__29807 = G__31357;
chunk__29808 = G__31358;
count__29809 = G__31359;
i__29810 = G__31360;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.on = (function shadow$dom$on(var_args){
var G__29827 = arguments.length;
switch (G__29827) {
case 3:
return shadow.dom.on.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return shadow.dom.on.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.on.cljs$core$IFn$_invoke$arity$3 = (function (el,ev,handler){
return shadow.dom.on.cljs$core$IFn$_invoke$arity$4(el,ev,handler,false);
}));

(shadow.dom.on.cljs$core$IFn$_invoke$arity$4 = (function (el,ev,handler,capture){
if(cljs.core.vector_QMARK_(ev)){
return shadow.dom.on_query(el,cljs.core.first(ev),cljs.core.second(ev),handler);
} else {
var handler__$1 = (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});
return shadow.dom.dom_listen(shadow.dom.dom_node(el),cljs.core.name(ev),handler__$1);
}
}));

(shadow.dom.on.cljs$lang$maxFixedArity = 4);

shadow.dom.remove_event_handler = (function shadow$dom$remove_event_handler(el,ev,handler){
return shadow.dom.dom_listen_remove(shadow.dom.dom_node(el),cljs.core.name(ev),handler);
});
shadow.dom.add_event_listeners = (function shadow$dom$add_event_listeners(el,events){
var seq__29868 = cljs.core.seq(events);
var chunk__29869 = null;
var count__29870 = (0);
var i__29871 = (0);
while(true){
if((i__29871 < count__29870)){
var vec__29888 = chunk__29869.cljs$core$IIndexed$_nth$arity$2(null,i__29871);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29888,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29888,(1),null);
shadow.dom.on.cljs$core$IFn$_invoke$arity$3(el,k,v);


var G__31365 = seq__29868;
var G__31366 = chunk__29869;
var G__31367 = count__29870;
var G__31368 = (i__29871 + (1));
seq__29868 = G__31365;
chunk__29869 = G__31366;
count__29870 = G__31367;
i__29871 = G__31368;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__29868);
if(temp__5823__auto__){
var seq__29868__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__29868__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__29868__$1);
var G__31370 = cljs.core.chunk_rest(seq__29868__$1);
var G__31371 = c__5694__auto__;
var G__31372 = cljs.core.count(c__5694__auto__);
var G__31373 = (0);
seq__29868 = G__31370;
chunk__29869 = G__31371;
count__29870 = G__31372;
i__29871 = G__31373;
continue;
} else {
var vec__29892 = cljs.core.first(seq__29868__$1);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29892,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29892,(1),null);
shadow.dom.on.cljs$core$IFn$_invoke$arity$3(el,k,v);


var G__31374 = cljs.core.next(seq__29868__$1);
var G__31375 = null;
var G__31376 = (0);
var G__31377 = (0);
seq__29868 = G__31374;
chunk__29869 = G__31375;
count__29870 = G__31376;
i__29871 = G__31377;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.set_style = (function shadow$dom$set_style(el,styles){
var dom = shadow.dom.dom_node(el);
var seq__29907 = cljs.core.seq(styles);
var chunk__29908 = null;
var count__29909 = (0);
var i__29910 = (0);
while(true){
if((i__29910 < count__29909)){
var vec__29935 = chunk__29908.cljs$core$IIndexed$_nth$arity$2(null,i__29910);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29935,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29935,(1),null);
goog.style.setStyle(dom,cljs.core.name(k),(((v == null))?"":v));


var G__31379 = seq__29907;
var G__31380 = chunk__29908;
var G__31381 = count__29909;
var G__31382 = (i__29910 + (1));
seq__29907 = G__31379;
chunk__29908 = G__31380;
count__29909 = G__31381;
i__29910 = G__31382;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__29907);
if(temp__5823__auto__){
var seq__29907__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__29907__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__29907__$1);
var G__31385 = cljs.core.chunk_rest(seq__29907__$1);
var G__31386 = c__5694__auto__;
var G__31387 = cljs.core.count(c__5694__auto__);
var G__31388 = (0);
seq__29907 = G__31385;
chunk__29908 = G__31386;
count__29909 = G__31387;
i__29910 = G__31388;
continue;
} else {
var vec__29942 = cljs.core.first(seq__29907__$1);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29942,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29942,(1),null);
goog.style.setStyle(dom,cljs.core.name(k),(((v == null))?"":v));


var G__31391 = cljs.core.next(seq__29907__$1);
var G__31392 = null;
var G__31393 = (0);
var G__31394 = (0);
seq__29907 = G__31391;
chunk__29908 = G__31392;
count__29909 = G__31393;
i__29910 = G__31394;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.set_attr_STAR_ = (function shadow$dom$set_attr_STAR_(el,key,value){
var G__29954_31396 = key;
var G__29954_31397__$1 = (((G__29954_31396 instanceof cljs.core.Keyword))?G__29954_31396.fqn:null);
switch (G__29954_31397__$1) {
case "id":
(el.id = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)));

break;
case "class":
(el.className = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)));

break;
case "for":
(el.htmlFor = value);

break;
case "cellpadding":
el.setAttribute("cellPadding",value);

break;
case "cellspacing":
el.setAttribute("cellSpacing",value);

break;
case "colspan":
el.setAttribute("colSpan",value);

break;
case "frameborder":
el.setAttribute("frameBorder",value);

break;
case "height":
el.setAttribute("height",value);

break;
case "maxlength":
el.setAttribute("maxLength",value);

break;
case "role":
el.setAttribute("role",value);

break;
case "rowspan":
el.setAttribute("rowSpan",value);

break;
case "type":
el.setAttribute("type",value);

break;
case "usemap":
el.setAttribute("useMap",value);

break;
case "valign":
el.setAttribute("vAlign",value);

break;
case "width":
el.setAttribute("width",value);

break;
case "on":
shadow.dom.add_event_listeners(el,value);

break;
case "style":
if((value == null)){
} else {
if(typeof value === 'string'){
el.setAttribute("style",value);
} else {
if(cljs.core.map_QMARK_(value)){
shadow.dom.set_style(el,value);
} else {
goog.style.setStyle(el,value);

}
}
}

break;
default:
var ks_31428 = cljs.core.name(key);
if(cljs.core.truth_((function (){var or__5162__auto__ = goog.string.startsWith(ks_31428,"data-");
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return goog.string.startsWith(ks_31428,"aria-");
}
})())){
el.setAttribute(ks_31428,value);
} else {
(el[ks_31428] = value);
}

}

return el;
});
shadow.dom.set_attrs = (function shadow$dom$set_attrs(el,attrs){
return cljs.core.reduce_kv((function (el__$1,key,value){
shadow.dom.set_attr_STAR_(el__$1,key,value);

return el__$1;
}),shadow.dom.dom_node(el),attrs);
});
shadow.dom.set_attr = (function shadow$dom$set_attr(el,key,value){
return shadow.dom.set_attr_STAR_(shadow.dom.dom_node(el),key,value);
});
shadow.dom.has_class_QMARK_ = (function shadow$dom$has_class_QMARK_(el,cls){
return goog.dom.classlist.contains(shadow.dom.dom_node(el),cls);
});
shadow.dom.merge_class_string = (function shadow$dom$merge_class_string(current,extra_class){
if(cljs.core.seq(current)){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(current)+" "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(extra_class));
} else {
return extra_class;
}
});
shadow.dom.parse_tag = (function shadow$dom$parse_tag(spec){
var spec__$1 = cljs.core.name(spec);
var fdot = spec__$1.indexOf(".");
var fhash = spec__$1.indexOf("#");
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fdot)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fhash)))){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1,null,null], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fhash)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fdot),null,clojure.string.replace(spec__$1.substring((fdot + (1))),/\./," ")], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fdot)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fhash),spec__$1.substring((fhash + (1))),null], null);
} else {
if((fhash > fdot)){
throw (""+"cant have id after class?"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(spec__$1));
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fhash),spec__$1.substring((fhash + (1)),fdot),clojure.string.replace(spec__$1.substring((fdot + (1))),/\./," ")], null);

}
}
}
}
});
shadow.dom.create_dom_node = (function shadow$dom$create_dom_node(tag_def,p__29977){
var map__29978 = p__29977;
var map__29978__$1 = cljs.core.__destructure_map(map__29978);
var props = map__29978__$1;
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__29978__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var tag_props = ({});
var vec__29979 = shadow.dom.parse_tag(tag_def);
var tag_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29979,(0),null);
var tag_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29979,(1),null);
var tag_classes = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__29979,(2),null);
if(cljs.core.truth_(tag_id)){
(tag_props["id"] = tag_id);
} else {
}

if(cljs.core.truth_(tag_classes)){
(tag_props["class"] = shadow.dom.merge_class_string(class$,tag_classes));
} else {
}

var G__29985 = goog.dom.createDom(tag_name,tag_props);
shadow.dom.set_attrs(G__29985,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(props,new cljs.core.Keyword(null,"class","class",-2030961996)));

return G__29985;
});
shadow.dom.append = (function shadow$dom$append(var_args){
var G__29988 = arguments.length;
switch (G__29988) {
case 1:
return shadow.dom.append.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.append.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.append.cljs$core$IFn$_invoke$arity$1 = (function (node){
if(cljs.core.truth_(node)){
var temp__5823__auto__ = shadow.dom.dom_node(node);
if(cljs.core.truth_(temp__5823__auto__)){
var n = temp__5823__auto__;
document.body.appendChild(n);

return n;
} else {
return null;
}
} else {
return null;
}
}));

(shadow.dom.append.cljs$core$IFn$_invoke$arity$2 = (function (el,node){
if(cljs.core.truth_(node)){
var temp__5823__auto__ = shadow.dom.dom_node(node);
if(cljs.core.truth_(temp__5823__auto__)){
var n = temp__5823__auto__;
shadow.dom.dom_node(el).appendChild(n);

return n;
} else {
return null;
}
} else {
return null;
}
}));

(shadow.dom.append.cljs$lang$maxFixedArity = 2);

shadow.dom.destructure_node = (function shadow$dom$destructure_node(create_fn,p__29997){
var vec__29998 = p__29997;
var seq__29999 = cljs.core.seq(vec__29998);
var first__30000 = cljs.core.first(seq__29999);
var seq__29999__$1 = cljs.core.next(seq__29999);
var nn = first__30000;
var first__30000__$1 = cljs.core.first(seq__29999__$1);
var seq__29999__$2 = cljs.core.next(seq__29999__$1);
var np = first__30000__$1;
var nc = seq__29999__$2;
var node = vec__29998;
if((nn instanceof cljs.core.Keyword)){
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("invalid dom node",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"node","node",581201198),node], null));
}

if((((np == null)) && ((nc == null)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__30002 = nn;
var G__30003 = cljs.core.PersistentArrayMap.EMPTY;
return (create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(G__30002,G__30003) : create_fn.call(null,G__30002,G__30003));
})(),cljs.core.List.EMPTY], null);
} else {
if(cljs.core.map_QMARK_(np)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(nn,np) : create_fn.call(null,nn,np)),nc], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__30008 = nn;
var G__30010 = cljs.core.PersistentArrayMap.EMPTY;
return (create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(G__30008,G__30010) : create_fn.call(null,G__30008,G__30010));
})(),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(nc,np)], null);

}
}
});
shadow.dom.make_dom_node = (function shadow$dom$make_dom_node(structure){
var vec__30023 = shadow.dom.destructure_node(shadow.dom.create_dom_node,structure);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30023,(0),null);
var node_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30023,(1),null);
var seq__30026_31463 = cljs.core.seq(node_children);
var chunk__30027_31464 = null;
var count__30028_31465 = (0);
var i__30029_31466 = (0);
while(true){
if((i__30029_31466 < count__30028_31465)){
var child_struct_31468 = chunk__30027_31464.cljs$core$IIndexed$_nth$arity$2(null,i__30029_31466);
var children_31469 = shadow.dom.dom_node(child_struct_31468);
if(cljs.core.seq_QMARK_(children_31469)){
var seq__30089_31470 = cljs.core.seq(cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom.dom_node,children_31469));
var chunk__30091_31471 = null;
var count__30092_31472 = (0);
var i__30093_31473 = (0);
while(true){
if((i__30093_31473 < count__30092_31472)){
var child_31474 = chunk__30091_31471.cljs$core$IIndexed$_nth$arity$2(null,i__30093_31473);
if(cljs.core.truth_(child_31474)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_31474);


var G__31475 = seq__30089_31470;
var G__31476 = chunk__30091_31471;
var G__31477 = count__30092_31472;
var G__31478 = (i__30093_31473 + (1));
seq__30089_31470 = G__31475;
chunk__30091_31471 = G__31476;
count__30092_31472 = G__31477;
i__30093_31473 = G__31478;
continue;
} else {
var G__31479 = seq__30089_31470;
var G__31480 = chunk__30091_31471;
var G__31481 = count__30092_31472;
var G__31482 = (i__30093_31473 + (1));
seq__30089_31470 = G__31479;
chunk__30091_31471 = G__31480;
count__30092_31472 = G__31481;
i__30093_31473 = G__31482;
continue;
}
} else {
var temp__5823__auto___31484 = cljs.core.seq(seq__30089_31470);
if(temp__5823__auto___31484){
var seq__30089_31485__$1 = temp__5823__auto___31484;
if(cljs.core.chunked_seq_QMARK_(seq__30089_31485__$1)){
var c__5694__auto___31486 = cljs.core.chunk_first(seq__30089_31485__$1);
var G__31491 = cljs.core.chunk_rest(seq__30089_31485__$1);
var G__31492 = c__5694__auto___31486;
var G__31493 = cljs.core.count(c__5694__auto___31486);
var G__31494 = (0);
seq__30089_31470 = G__31491;
chunk__30091_31471 = G__31492;
count__30092_31472 = G__31493;
i__30093_31473 = G__31494;
continue;
} else {
var child_31495 = cljs.core.first(seq__30089_31485__$1);
if(cljs.core.truth_(child_31495)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_31495);


var G__31496 = cljs.core.next(seq__30089_31485__$1);
var G__31497 = null;
var G__31498 = (0);
var G__31499 = (0);
seq__30089_31470 = G__31496;
chunk__30091_31471 = G__31497;
count__30092_31472 = G__31498;
i__30093_31473 = G__31499;
continue;
} else {
var G__31500 = cljs.core.next(seq__30089_31485__$1);
var G__31501 = null;
var G__31502 = (0);
var G__31503 = (0);
seq__30089_31470 = G__31500;
chunk__30091_31471 = G__31501;
count__30092_31472 = G__31502;
i__30093_31473 = G__31503;
continue;
}
}
} else {
}
}
break;
}
} else {
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,children_31469);
}


var G__31505 = seq__30026_31463;
var G__31506 = chunk__30027_31464;
var G__31507 = count__30028_31465;
var G__31508 = (i__30029_31466 + (1));
seq__30026_31463 = G__31505;
chunk__30027_31464 = G__31506;
count__30028_31465 = G__31507;
i__30029_31466 = G__31508;
continue;
} else {
var temp__5823__auto___31509 = cljs.core.seq(seq__30026_31463);
if(temp__5823__auto___31509){
var seq__30026_31510__$1 = temp__5823__auto___31509;
if(cljs.core.chunked_seq_QMARK_(seq__30026_31510__$1)){
var c__5694__auto___31511 = cljs.core.chunk_first(seq__30026_31510__$1);
var G__31513 = cljs.core.chunk_rest(seq__30026_31510__$1);
var G__31514 = c__5694__auto___31511;
var G__31515 = cljs.core.count(c__5694__auto___31511);
var G__31516 = (0);
seq__30026_31463 = G__31513;
chunk__30027_31464 = G__31514;
count__30028_31465 = G__31515;
i__30029_31466 = G__31516;
continue;
} else {
var child_struct_31519 = cljs.core.first(seq__30026_31510__$1);
var children_31521 = shadow.dom.dom_node(child_struct_31519);
if(cljs.core.seq_QMARK_(children_31521)){
var seq__30125_31524 = cljs.core.seq(cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom.dom_node,children_31521));
var chunk__30127_31525 = null;
var count__30128_31526 = (0);
var i__30129_31527 = (0);
while(true){
if((i__30129_31527 < count__30128_31526)){
var child_31531 = chunk__30127_31525.cljs$core$IIndexed$_nth$arity$2(null,i__30129_31527);
if(cljs.core.truth_(child_31531)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_31531);


var G__31535 = seq__30125_31524;
var G__31536 = chunk__30127_31525;
var G__31537 = count__30128_31526;
var G__31538 = (i__30129_31527 + (1));
seq__30125_31524 = G__31535;
chunk__30127_31525 = G__31536;
count__30128_31526 = G__31537;
i__30129_31527 = G__31538;
continue;
} else {
var G__31546 = seq__30125_31524;
var G__31547 = chunk__30127_31525;
var G__31548 = count__30128_31526;
var G__31549 = (i__30129_31527 + (1));
seq__30125_31524 = G__31546;
chunk__30127_31525 = G__31547;
count__30128_31526 = G__31548;
i__30129_31527 = G__31549;
continue;
}
} else {
var temp__5823__auto___31553__$1 = cljs.core.seq(seq__30125_31524);
if(temp__5823__auto___31553__$1){
var seq__30125_31556__$1 = temp__5823__auto___31553__$1;
if(cljs.core.chunked_seq_QMARK_(seq__30125_31556__$1)){
var c__5694__auto___31558 = cljs.core.chunk_first(seq__30125_31556__$1);
var G__31559 = cljs.core.chunk_rest(seq__30125_31556__$1);
var G__31560 = c__5694__auto___31558;
var G__31561 = cljs.core.count(c__5694__auto___31558);
var G__31562 = (0);
seq__30125_31524 = G__31559;
chunk__30127_31525 = G__31560;
count__30128_31526 = G__31561;
i__30129_31527 = G__31562;
continue;
} else {
var child_31563 = cljs.core.first(seq__30125_31556__$1);
if(cljs.core.truth_(child_31563)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_31563);


var G__31564 = cljs.core.next(seq__30125_31556__$1);
var G__31565 = null;
var G__31566 = (0);
var G__31567 = (0);
seq__30125_31524 = G__31564;
chunk__30127_31525 = G__31565;
count__30128_31526 = G__31566;
i__30129_31527 = G__31567;
continue;
} else {
var G__31568 = cljs.core.next(seq__30125_31556__$1);
var G__31569 = null;
var G__31570 = (0);
var G__31571 = (0);
seq__30125_31524 = G__31568;
chunk__30127_31525 = G__31569;
count__30128_31526 = G__31570;
i__30129_31527 = G__31571;
continue;
}
}
} else {
}
}
break;
}
} else {
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,children_31521);
}


var G__31572 = cljs.core.next(seq__30026_31510__$1);
var G__31573 = null;
var G__31574 = (0);
var G__31575 = (0);
seq__30026_31463 = G__31572;
chunk__30027_31464 = G__31573;
count__30028_31465 = G__31574;
i__30029_31466 = G__31575;
continue;
}
} else {
}
}
break;
}

return node;
});
(cljs.core.Keyword.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.Keyword.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_dom_node(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$__$1], null));
}));

(cljs.core.PersistentVector.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_dom_node(this$__$1);
}));

(cljs.core.LazySeq.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.LazySeq.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom._to_dom,this$__$1);
}));
if(cljs.core.truth_(((typeof HTMLElement) != 'undefined'))){
(HTMLElement.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(HTMLElement.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1;
}));
} else {
}
if(cljs.core.truth_(((typeof DocumentFragment) != 'undefined'))){
(DocumentFragment.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(DocumentFragment.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1;
}));
} else {
}
/**
 * clear node children
 */
shadow.dom.reset = (function shadow$dom$reset(node){
return goog.dom.removeChildren(shadow.dom.dom_node(node));
});
shadow.dom.remove = (function shadow$dom$remove(node){
if((((!((node == null))))?(((((node.cljs$lang$protocol_mask$partition0$ & (8388608))) || ((cljs.core.PROTOCOL_SENTINEL === node.cljs$core$ISeqable$))))?true:false):false)){
var seq__30165 = cljs.core.seq(node);
var chunk__30166 = null;
var count__30167 = (0);
var i__30168 = (0);
while(true){
if((i__30168 < count__30167)){
var n = chunk__30166.cljs$core$IIndexed$_nth$arity$2(null,i__30168);
(shadow.dom.remove.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.remove.cljs$core$IFn$_invoke$arity$1(n) : shadow.dom.remove.call(null,n));


var G__31590 = seq__30165;
var G__31591 = chunk__30166;
var G__31592 = count__30167;
var G__31593 = (i__30168 + (1));
seq__30165 = G__31590;
chunk__30166 = G__31591;
count__30167 = G__31592;
i__30168 = G__31593;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__30165);
if(temp__5823__auto__){
var seq__30165__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__30165__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__30165__$1);
var G__31595 = cljs.core.chunk_rest(seq__30165__$1);
var G__31596 = c__5694__auto__;
var G__31597 = cljs.core.count(c__5694__auto__);
var G__31598 = (0);
seq__30165 = G__31595;
chunk__30166 = G__31596;
count__30167 = G__31597;
i__30168 = G__31598;
continue;
} else {
var n = cljs.core.first(seq__30165__$1);
(shadow.dom.remove.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.remove.cljs$core$IFn$_invoke$arity$1(n) : shadow.dom.remove.call(null,n));


var G__31599 = cljs.core.next(seq__30165__$1);
var G__31600 = null;
var G__31601 = (0);
var G__31602 = (0);
seq__30165 = G__31599;
chunk__30166 = G__31600;
count__30167 = G__31601;
i__30168 = G__31602;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return goog.dom.removeNode(node);
}
});
shadow.dom.replace_node = (function shadow$dom$replace_node(old,new$){
return goog.dom.replaceNode(shadow.dom.dom_node(new$),shadow.dom.dom_node(old));
});
shadow.dom.text = (function shadow$dom$text(var_args){
var G__30196 = arguments.length;
switch (G__30196) {
case 2:
return shadow.dom.text.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return shadow.dom.text.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.text.cljs$core$IFn$_invoke$arity$2 = (function (el,new_text){
return (shadow.dom.dom_node(el).innerText = new_text);
}));

(shadow.dom.text.cljs$core$IFn$_invoke$arity$1 = (function (el){
return shadow.dom.dom_node(el).innerText;
}));

(shadow.dom.text.cljs$lang$maxFixedArity = 2);

shadow.dom.check = (function shadow$dom$check(var_args){
var G__30202 = arguments.length;
switch (G__30202) {
case 1:
return shadow.dom.check.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.check.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.check.cljs$core$IFn$_invoke$arity$1 = (function (el){
return shadow.dom.check.cljs$core$IFn$_invoke$arity$2(el,true);
}));

(shadow.dom.check.cljs$core$IFn$_invoke$arity$2 = (function (el,checked){
return (shadow.dom.dom_node(el).checked = checked);
}));

(shadow.dom.check.cljs$lang$maxFixedArity = 2);

shadow.dom.checked_QMARK_ = (function shadow$dom$checked_QMARK_(el){
return shadow.dom.dom_node(el).checked;
});
shadow.dom.form_elements = (function shadow$dom$form_elements(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).elements));
});
shadow.dom.children = (function shadow$dom$children(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).children));
});
shadow.dom.child_nodes = (function shadow$dom$child_nodes(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).childNodes));
});
shadow.dom.attr = (function shadow$dom$attr(var_args){
var G__30228 = arguments.length;
switch (G__30228) {
case 2:
return shadow.dom.attr.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.attr.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.attr.cljs$core$IFn$_invoke$arity$2 = (function (el,key){
return shadow.dom.dom_node(el).getAttribute(cljs.core.name(key));
}));

(shadow.dom.attr.cljs$core$IFn$_invoke$arity$3 = (function (el,key,default$){
var or__5162__auto__ = shadow.dom.dom_node(el).getAttribute(cljs.core.name(key));
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return default$;
}
}));

(shadow.dom.attr.cljs$lang$maxFixedArity = 3);

shadow.dom.del_attr = (function shadow$dom$del_attr(el,key){
return shadow.dom.dom_node(el).removeAttribute(cljs.core.name(key));
});
shadow.dom.data = (function shadow$dom$data(el,key){
return shadow.dom.dom_node(el).getAttribute((""+"data-"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(key))));
});
shadow.dom.set_data = (function shadow$dom$set_data(el,key,value){
return shadow.dom.dom_node(el).setAttribute((""+"data-"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(key))),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)));
});
shadow.dom.set_html = (function shadow$dom$set_html(node,text){
return (shadow.dom.dom_node(node).innerHTML = text);
});
shadow.dom.get_html = (function shadow$dom$get_html(node){
return shadow.dom.dom_node(node).innerHTML;
});
shadow.dom.fragment = (function shadow$dom$fragment(var_args){
var args__5903__auto__ = [];
var len__5897__auto___31620 = arguments.length;
var i__5898__auto___31623 = (0);
while(true){
if((i__5898__auto___31623 < len__5897__auto___31620)){
args__5903__auto__.push((arguments[i__5898__auto___31623]));

var G__31628 = (i__5898__auto___31623 + (1));
i__5898__auto___31623 = G__31628;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((0) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((0)),(0),null)):null);
return shadow.dom.fragment.cljs$core$IFn$_invoke$arity$variadic(argseq__5904__auto__);
});

(shadow.dom.fragment.cljs$core$IFn$_invoke$arity$variadic = (function (nodes){
var fragment = document.createDocumentFragment();
var seq__30264_31630 = cljs.core.seq(nodes);
var chunk__30265_31631 = null;
var count__30266_31632 = (0);
var i__30267_31633 = (0);
while(true){
if((i__30267_31633 < count__30266_31632)){
var node_31634 = chunk__30265_31631.cljs$core$IIndexed$_nth$arity$2(null,i__30267_31633);
fragment.appendChild(shadow.dom._to_dom(node_31634));


var G__31635 = seq__30264_31630;
var G__31636 = chunk__30265_31631;
var G__31637 = count__30266_31632;
var G__31638 = (i__30267_31633 + (1));
seq__30264_31630 = G__31635;
chunk__30265_31631 = G__31636;
count__30266_31632 = G__31637;
i__30267_31633 = G__31638;
continue;
} else {
var temp__5823__auto___31639 = cljs.core.seq(seq__30264_31630);
if(temp__5823__auto___31639){
var seq__30264_31643__$1 = temp__5823__auto___31639;
if(cljs.core.chunked_seq_QMARK_(seq__30264_31643__$1)){
var c__5694__auto___31644 = cljs.core.chunk_first(seq__30264_31643__$1);
var G__31645 = cljs.core.chunk_rest(seq__30264_31643__$1);
var G__31646 = c__5694__auto___31644;
var G__31647 = cljs.core.count(c__5694__auto___31644);
var G__31648 = (0);
seq__30264_31630 = G__31645;
chunk__30265_31631 = G__31646;
count__30266_31632 = G__31647;
i__30267_31633 = G__31648;
continue;
} else {
var node_31649 = cljs.core.first(seq__30264_31643__$1);
fragment.appendChild(shadow.dom._to_dom(node_31649));


var G__31650 = cljs.core.next(seq__30264_31643__$1);
var G__31651 = null;
var G__31652 = (0);
var G__31653 = (0);
seq__30264_31630 = G__31650;
chunk__30265_31631 = G__31651;
count__30266_31632 = G__31652;
i__30267_31633 = G__31653;
continue;
}
} else {
}
}
break;
}

return (new shadow.dom.NativeColl(fragment));
}));

(shadow.dom.fragment.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(shadow.dom.fragment.cljs$lang$applyTo = (function (seq30261){
var self__5883__auto__ = this;
return self__5883__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq30261));
}));

/**
 * given a html string, eval all <script> tags and return the html without the scripts
 * don't do this for everything, only content you trust.
 */
shadow.dom.eval_scripts = (function shadow$dom$eval_scripts(s){
var scripts = cljs.core.re_seq(/<script[^>]*?>(.+?)<\/script>/,s);
var seq__30274_31656 = cljs.core.seq(scripts);
var chunk__30275_31657 = null;
var count__30276_31658 = (0);
var i__30277_31659 = (0);
while(true){
if((i__30277_31659 < count__30276_31658)){
var vec__30287_31660 = chunk__30275_31657.cljs$core$IIndexed$_nth$arity$2(null,i__30277_31659);
var script_tag_31661 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30287_31660,(0),null);
var script_body_31662 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30287_31660,(1),null);
eval(script_body_31662);


var G__31664 = seq__30274_31656;
var G__31665 = chunk__30275_31657;
var G__31666 = count__30276_31658;
var G__31667 = (i__30277_31659 + (1));
seq__30274_31656 = G__31664;
chunk__30275_31657 = G__31665;
count__30276_31658 = G__31666;
i__30277_31659 = G__31667;
continue;
} else {
var temp__5823__auto___31669 = cljs.core.seq(seq__30274_31656);
if(temp__5823__auto___31669){
var seq__30274_31670__$1 = temp__5823__auto___31669;
if(cljs.core.chunked_seq_QMARK_(seq__30274_31670__$1)){
var c__5694__auto___31672 = cljs.core.chunk_first(seq__30274_31670__$1);
var G__31674 = cljs.core.chunk_rest(seq__30274_31670__$1);
var G__31675 = c__5694__auto___31672;
var G__31676 = cljs.core.count(c__5694__auto___31672);
var G__31677 = (0);
seq__30274_31656 = G__31674;
chunk__30275_31657 = G__31675;
count__30276_31658 = G__31676;
i__30277_31659 = G__31677;
continue;
} else {
var vec__30291_31678 = cljs.core.first(seq__30274_31670__$1);
var script_tag_31679 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30291_31678,(0),null);
var script_body_31680 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30291_31678,(1),null);
eval(script_body_31680);


var G__31682 = cljs.core.next(seq__30274_31670__$1);
var G__31683 = null;
var G__31684 = (0);
var G__31685 = (0);
seq__30274_31656 = G__31682;
chunk__30275_31657 = G__31683;
count__30276_31658 = G__31684;
i__30277_31659 = G__31685;
continue;
}
} else {
}
}
break;
}

return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (s__$1,p__30294){
var vec__30297 = p__30294;
var script_tag = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30297,(0),null);
var script_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30297,(1),null);
return clojure.string.replace(s__$1,script_tag,"");
}),s,scripts);
});
shadow.dom.str__GT_fragment = (function shadow$dom$str__GT_fragment(s){
var el = document.createElement("div");
(el.innerHTML = s);

return (new shadow.dom.NativeColl(goog.dom.childrenToNode_(document,el)));
});
shadow.dom.node_name = (function shadow$dom$node_name(el){
return shadow.dom.dom_node(el).nodeName;
});
shadow.dom.ancestor_by_class = (function shadow$dom$ancestor_by_class(el,cls){
return goog.dom.getAncestorByClass(shadow.dom.dom_node(el),cls);
});
shadow.dom.ancestor_by_tag = (function shadow$dom$ancestor_by_tag(var_args){
var G__30326 = arguments.length;
switch (G__30326) {
case 2:
return shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$2 = (function (el,tag){
return goog.dom.getAncestorByTagNameAndClass(shadow.dom.dom_node(el),cljs.core.name(tag));
}));

(shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$3 = (function (el,tag,cls){
return goog.dom.getAncestorByTagNameAndClass(shadow.dom.dom_node(el),cljs.core.name(tag),cljs.core.name(cls));
}));

(shadow.dom.ancestor_by_tag.cljs$lang$maxFixedArity = 3);

shadow.dom.get_value = (function shadow$dom$get_value(dom){
return goog.dom.forms.getValue(shadow.dom.dom_node(dom));
});
shadow.dom.set_value = (function shadow$dom$set_value(dom,value){
return goog.dom.forms.setValue(shadow.dom.dom_node(dom),value);
});
shadow.dom.px = (function shadow$dom$px(value){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((value | 0))+"px");
});
shadow.dom.pct = (function shadow$dom$pct(value){
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)+"%");
});
shadow.dom.remove_style_STAR_ = (function shadow$dom$remove_style_STAR_(el,style){
return el.style.removeProperty(cljs.core.name(style));
});
shadow.dom.remove_style = (function shadow$dom$remove_style(el,style){
var el__$1 = shadow.dom.dom_node(el);
return shadow.dom.remove_style_STAR_(el__$1,style);
});
shadow.dom.remove_styles = (function shadow$dom$remove_styles(el,style_keys){
var el__$1 = shadow.dom.dom_node(el);
var seq__30358 = cljs.core.seq(style_keys);
var chunk__30359 = null;
var count__30360 = (0);
var i__30361 = (0);
while(true){
if((i__30361 < count__30360)){
var it = chunk__30359.cljs$core$IIndexed$_nth$arity$2(null,i__30361);
shadow.dom.remove_style_STAR_(el__$1,it);


var G__31698 = seq__30358;
var G__31699 = chunk__30359;
var G__31700 = count__30360;
var G__31701 = (i__30361 + (1));
seq__30358 = G__31698;
chunk__30359 = G__31699;
count__30360 = G__31700;
i__30361 = G__31701;
continue;
} else {
var temp__5823__auto__ = cljs.core.seq(seq__30358);
if(temp__5823__auto__){
var seq__30358__$1 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__30358__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__30358__$1);
var G__31703 = cljs.core.chunk_rest(seq__30358__$1);
var G__31704 = c__5694__auto__;
var G__31705 = cljs.core.count(c__5694__auto__);
var G__31706 = (0);
seq__30358 = G__31703;
chunk__30359 = G__31704;
count__30360 = G__31705;
i__30361 = G__31706;
continue;
} else {
var it = cljs.core.first(seq__30358__$1);
shadow.dom.remove_style_STAR_(el__$1,it);


var G__31707 = cljs.core.next(seq__30358__$1);
var G__31708 = null;
var G__31709 = (0);
var G__31710 = (0);
seq__30358 = G__31707;
chunk__30359 = G__31708;
count__30360 = G__31709;
i__30361 = G__31710;
continue;
}
} else {
return null;
}
}
break;
}
});

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
shadow.dom.Coordinate = (function (x,y,__meta,__extmap,__hash){
this.x = x;
this.y = y;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(shadow.dom.Coordinate.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__5469__auto__,k__5470__auto__){
var self__ = this;
var this__5469__auto____$1 = this;
return this__5469__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__5470__auto__,null);
}));

(shadow.dom.Coordinate.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__5471__auto__,k30392,else__5472__auto__){
var self__ = this;
var this__5471__auto____$1 = this;
var G__30463 = k30392;
var G__30463__$1 = (((G__30463 instanceof cljs.core.Keyword))?G__30463.fqn:null);
switch (G__30463__$1) {
case "x":
return self__.x;

break;
case "y":
return self__.y;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k30392,else__5472__auto__);

}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__5489__auto__,f__5490__auto__,init__5491__auto__){
var self__ = this;
var this__5489__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__5492__auto__,p__30469){
var vec__30470 = p__30469;
var k__5493__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30470,(0),null);
var v__5494__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30470,(1),null);
return (f__5490__auto__.cljs$core$IFn$_invoke$arity$3 ? f__5490__auto__.cljs$core$IFn$_invoke$arity$3(ret__5492__auto__,k__5493__auto__,v__5494__auto__) : f__5490__auto__.call(null,ret__5492__auto__,k__5493__auto__,v__5494__auto__));
}),init__5491__auto__,this__5489__auto____$1);
}));

(shadow.dom.Coordinate.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__5484__auto__,writer__5485__auto__,opts__5486__auto__){
var self__ = this;
var this__5484__auto____$1 = this;
var pr_pair__5487__auto__ = (function (keyval__5488__auto__){
return cljs.core.pr_sequential_writer(writer__5485__auto__,cljs.core.pr_writer,""," ","",opts__5486__auto__,keyval__5488__auto__);
});
return cljs.core.pr_sequential_writer(writer__5485__auto__,pr_pair__5487__auto__,"#shadow.dom.Coordinate{",", ","}",opts__5486__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"x","x",2099068185),self__.x],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"y","y",-1757859776),self__.y],null))], null),self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__30391){
var self__ = this;
var G__30391__$1 = this;
return (new cljs.core.RecordIter((0),G__30391__$1,2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"x","x",2099068185),new cljs.core.Keyword(null,"y","y",-1757859776)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__5467__auto__){
var self__ = this;
var this__5467__auto____$1 = this;
return self__.__meta;
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__5464__auto__){
var self__ = this;
var this__5464__auto____$1 = this;
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,self__.__extmap,self__.__hash));
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__5473__auto__){
var self__ = this;
var this__5473__auto____$1 = this;
return (2 + cljs.core.count(self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__5465__auto__){
var self__ = this;
var this__5465__auto____$1 = this;
var h__5272__auto__ = self__.__hash;
if((!((h__5272__auto__ == null)))){
return h__5272__auto__;
} else {
var h__5272__auto____$1 = (function (coll__5466__auto__){
return (145542109 ^ cljs.core.hash_unordered_coll(coll__5466__auto__));
})(this__5465__auto____$1);
(self__.__hash = h__5272__auto____$1);

return h__5272__auto____$1;
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this30393,other30394){
var self__ = this;
var this30393__$1 = this;
return (((!((other30394 == null)))) && ((((this30393__$1.constructor === other30394.constructor)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this30393__$1.x,other30394.x)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this30393__$1.y,other30394.y)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this30393__$1.__extmap,other30394.__extmap)))))))));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__5479__auto__,k__5480__auto__){
var self__ = this;
var this__5479__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"y","y",-1757859776),null,new cljs.core.Keyword(null,"x","x",2099068185),null], null), null),k__5480__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__5479__auto____$1),self__.__meta),k__5480__auto__);
} else {
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__5480__auto__)),null));
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IAssociative$_contains_key_QMARK_$arity$2 = (function (this__5476__auto__,k30392){
var self__ = this;
var this__5476__auto____$1 = this;
var G__30519 = k30392;
var G__30519__$1 = (((G__30519 instanceof cljs.core.Keyword))?G__30519.fqn:null);
switch (G__30519__$1) {
case "x":
case "y":
return true;

break;
default:
return cljs.core.contains_QMARK_(self__.__extmap,k30392);

}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__5477__auto__,k__5478__auto__,G__30391){
var self__ = this;
var this__5477__auto____$1 = this;
var pred__30522 = cljs.core.keyword_identical_QMARK_;
var expr__30523 = k__5478__auto__;
if(cljs.core.truth_((pred__30522.cljs$core$IFn$_invoke$arity$2 ? pred__30522.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"x","x",2099068185),expr__30523) : pred__30522.call(null,new cljs.core.Keyword(null,"x","x",2099068185),expr__30523)))){
return (new shadow.dom.Coordinate(G__30391,self__.y,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__30522.cljs$core$IFn$_invoke$arity$2 ? pred__30522.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"y","y",-1757859776),expr__30523) : pred__30522.call(null,new cljs.core.Keyword(null,"y","y",-1757859776),expr__30523)))){
return (new shadow.dom.Coordinate(self__.x,G__30391,self__.__meta,self__.__extmap,null));
} else {
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__5478__auto__,G__30391),null));
}
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__5482__auto__){
var self__ = this;
var this__5482__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"x","x",2099068185),self__.x,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"y","y",-1757859776),self__.y,null))], null),self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__5468__auto__,G__30391){
var self__ = this;
var this__5468__auto____$1 = this;
return (new shadow.dom.Coordinate(self__.x,self__.y,G__30391,self__.__extmap,self__.__hash));
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__5474__auto__,entry__5475__auto__){
var self__ = this;
var this__5474__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__5475__auto__)){
return this__5474__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__5475__auto__,(0)),cljs.core._nth(entry__5475__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__5474__auto____$1,entry__5475__auto__);
}
}));

(shadow.dom.Coordinate.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"x","x",-555367584,null),new cljs.core.Symbol(null,"y","y",-117328249,null)], null);
}));

(shadow.dom.Coordinate.cljs$lang$type = true);

(shadow.dom.Coordinate.cljs$lang$ctorPrSeq = (function (this__5515__auto__){
return (new cljs.core.List(null,"shadow.dom/Coordinate",null,(1),null));
}));

(shadow.dom.Coordinate.cljs$lang$ctorPrWriter = (function (this__5515__auto__,writer__5516__auto__){
return cljs.core._write(writer__5516__auto__,"shadow.dom/Coordinate");
}));

/**
 * Positional factory function for shadow.dom/Coordinate.
 */
shadow.dom.__GT_Coordinate = (function shadow$dom$__GT_Coordinate(x,y){
return (new shadow.dom.Coordinate(x,y,null,null,null));
});

/**
 * Factory function for shadow.dom/Coordinate, taking a map of keywords to field values.
 */
shadow.dom.map__GT_Coordinate = (function shadow$dom$map__GT_Coordinate(G__30403){
var extmap__5511__auto__ = (function (){var G__30548 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__30403,new cljs.core.Keyword(null,"x","x",2099068185),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"y","y",-1757859776)], 0));
if(cljs.core.record_QMARK_(G__30403)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__30548);
} else {
return G__30548;
}
})();
return (new shadow.dom.Coordinate(new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(G__30403),new cljs.core.Keyword(null,"y","y",-1757859776).cljs$core$IFn$_invoke$arity$1(G__30403),null,cljs.core.not_empty(extmap__5511__auto__),null));
});

shadow.dom.get_position = (function shadow$dom$get_position(el){
var pos = goog.style.getPosition(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});
shadow.dom.get_client_position = (function shadow$dom$get_client_position(el){
var pos = goog.style.getClientPosition(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});
shadow.dom.get_page_offset = (function shadow$dom$get_page_offset(el){
var pos = goog.style.getPageOffset(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
shadow.dom.Size = (function (w,h,__meta,__extmap,__hash){
this.w = w;
this.h = h;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(shadow.dom.Size.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__5469__auto__,k__5470__auto__){
var self__ = this;
var this__5469__auto____$1 = this;
return this__5469__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__5470__auto__,null);
}));

(shadow.dom.Size.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__5471__auto__,k30565,else__5472__auto__){
var self__ = this;
var this__5471__auto____$1 = this;
var G__30591 = k30565;
var G__30591__$1 = (((G__30591 instanceof cljs.core.Keyword))?G__30591.fqn:null);
switch (G__30591__$1) {
case "w":
return self__.w;

break;
case "h":
return self__.h;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k30565,else__5472__auto__);

}
}));

(shadow.dom.Size.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__5489__auto__,f__5490__auto__,init__5491__auto__){
var self__ = this;
var this__5489__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__5492__auto__,p__30602){
var vec__30603 = p__30602;
var k__5493__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30603,(0),null);
var v__5494__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30603,(1),null);
return (f__5490__auto__.cljs$core$IFn$_invoke$arity$3 ? f__5490__auto__.cljs$core$IFn$_invoke$arity$3(ret__5492__auto__,k__5493__auto__,v__5494__auto__) : f__5490__auto__.call(null,ret__5492__auto__,k__5493__auto__,v__5494__auto__));
}),init__5491__auto__,this__5489__auto____$1);
}));

(shadow.dom.Size.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__5484__auto__,writer__5485__auto__,opts__5486__auto__){
var self__ = this;
var this__5484__auto____$1 = this;
var pr_pair__5487__auto__ = (function (keyval__5488__auto__){
return cljs.core.pr_sequential_writer(writer__5485__auto__,cljs.core.pr_writer,""," ","",opts__5486__auto__,keyval__5488__auto__);
});
return cljs.core.pr_sequential_writer(writer__5485__auto__,pr_pair__5487__auto__,"#shadow.dom.Size{",", ","}",opts__5486__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"w","w",354169001),self__.w],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"h","h",1109658740),self__.h],null))], null),self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__30564){
var self__ = this;
var G__30564__$1 = this;
return (new cljs.core.RecordIter((0),G__30564__$1,2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"w","w",354169001),new cljs.core.Keyword(null,"h","h",1109658740)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(shadow.dom.Size.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__5467__auto__){
var self__ = this;
var this__5467__auto____$1 = this;
return self__.__meta;
}));

(shadow.dom.Size.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__5464__auto__){
var self__ = this;
var this__5464__auto____$1 = this;
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,self__.__extmap,self__.__hash));
}));

(shadow.dom.Size.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__5473__auto__){
var self__ = this;
var this__5473__auto____$1 = this;
return (2 + cljs.core.count(self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__5465__auto__){
var self__ = this;
var this__5465__auto____$1 = this;
var h__5272__auto__ = self__.__hash;
if((!((h__5272__auto__ == null)))){
return h__5272__auto__;
} else {
var h__5272__auto____$1 = (function (coll__5466__auto__){
return (-1228019642 ^ cljs.core.hash_unordered_coll(coll__5466__auto__));
})(this__5465__auto____$1);
(self__.__hash = h__5272__auto____$1);

return h__5272__auto____$1;
}
}));

(shadow.dom.Size.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this30569,other30570){
var self__ = this;
var this30569__$1 = this;
return (((!((other30570 == null)))) && ((((this30569__$1.constructor === other30570.constructor)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this30569__$1.w,other30570.w)) && (((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this30569__$1.h,other30570.h)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this30569__$1.__extmap,other30570.__extmap)))))))));
}));

(shadow.dom.Size.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__5479__auto__,k__5480__auto__){
var self__ = this;
var this__5479__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"w","w",354169001),null,new cljs.core.Keyword(null,"h","h",1109658740),null], null), null),k__5480__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__5479__auto____$1),self__.__meta),k__5480__auto__);
} else {
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__5480__auto__)),null));
}
}));

(shadow.dom.Size.prototype.cljs$core$IAssociative$_contains_key_QMARK_$arity$2 = (function (this__5476__auto__,k30565){
var self__ = this;
var this__5476__auto____$1 = this;
var G__30646 = k30565;
var G__30646__$1 = (((G__30646 instanceof cljs.core.Keyword))?G__30646.fqn:null);
switch (G__30646__$1) {
case "w":
case "h":
return true;

break;
default:
return cljs.core.contains_QMARK_(self__.__extmap,k30565);

}
}));

(shadow.dom.Size.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__5477__auto__,k__5478__auto__,G__30564){
var self__ = this;
var this__5477__auto____$1 = this;
var pred__30652 = cljs.core.keyword_identical_QMARK_;
var expr__30653 = k__5478__auto__;
if(cljs.core.truth_((pred__30652.cljs$core$IFn$_invoke$arity$2 ? pred__30652.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"w","w",354169001),expr__30653) : pred__30652.call(null,new cljs.core.Keyword(null,"w","w",354169001),expr__30653)))){
return (new shadow.dom.Size(G__30564,self__.h,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__30652.cljs$core$IFn$_invoke$arity$2 ? pred__30652.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"h","h",1109658740),expr__30653) : pred__30652.call(null,new cljs.core.Keyword(null,"h","h",1109658740),expr__30653)))){
return (new shadow.dom.Size(self__.w,G__30564,self__.__meta,self__.__extmap,null));
} else {
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__5478__auto__,G__30564),null));
}
}
}));

(shadow.dom.Size.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__5482__auto__){
var self__ = this;
var this__5482__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"w","w",354169001),self__.w,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"h","h",1109658740),self__.h,null))], null),self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__5468__auto__,G__30564){
var self__ = this;
var this__5468__auto____$1 = this;
return (new shadow.dom.Size(self__.w,self__.h,G__30564,self__.__extmap,self__.__hash));
}));

(shadow.dom.Size.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__5474__auto__,entry__5475__auto__){
var self__ = this;
var this__5474__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__5475__auto__)){
return this__5474__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__5475__auto__,(0)),cljs.core._nth(entry__5475__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__5474__auto____$1,entry__5475__auto__);
}
}));

(shadow.dom.Size.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"w","w",1994700528,null),new cljs.core.Symbol(null,"h","h",-1544777029,null)], null);
}));

(shadow.dom.Size.cljs$lang$type = true);

(shadow.dom.Size.cljs$lang$ctorPrSeq = (function (this__5515__auto__){
return (new cljs.core.List(null,"shadow.dom/Size",null,(1),null));
}));

(shadow.dom.Size.cljs$lang$ctorPrWriter = (function (this__5515__auto__,writer__5516__auto__){
return cljs.core._write(writer__5516__auto__,"shadow.dom/Size");
}));

/**
 * Positional factory function for shadow.dom/Size.
 */
shadow.dom.__GT_Size = (function shadow$dom$__GT_Size(w,h){
return (new shadow.dom.Size(w,h,null,null,null));
});

/**
 * Factory function for shadow.dom/Size, taking a map of keywords to field values.
 */
shadow.dom.map__GT_Size = (function shadow$dom$map__GT_Size(G__30575){
var extmap__5511__auto__ = (function (){var G__30693 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__30575,new cljs.core.Keyword(null,"w","w",354169001),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"h","h",1109658740)], 0));
if(cljs.core.record_QMARK_(G__30575)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__30693);
} else {
return G__30693;
}
})();
return (new shadow.dom.Size(new cljs.core.Keyword(null,"w","w",354169001).cljs$core$IFn$_invoke$arity$1(G__30575),new cljs.core.Keyword(null,"h","h",1109658740).cljs$core$IFn$_invoke$arity$1(G__30575),null,cljs.core.not_empty(extmap__5511__auto__),null));
});

shadow.dom.size__GT_clj = (function shadow$dom$size__GT_clj(size){
return (new shadow.dom.Size(size.width,size.height,null,null,null));
});
shadow.dom.get_size = (function shadow$dom$get_size(el){
return shadow.dom.size__GT_clj(goog.style.getSize(shadow.dom.dom_node(el)));
});
shadow.dom.get_height = (function shadow$dom$get_height(el){
return shadow.dom.get_size(el).h;
});
shadow.dom.get_viewport_size = (function shadow$dom$get_viewport_size(){
return shadow.dom.size__GT_clj(goog.dom.getViewportSize());
});
shadow.dom.first_child = (function shadow$dom$first_child(el){
return (shadow.dom.dom_node(el).children[(0)]);
});
shadow.dom.select_option_values = (function shadow$dom$select_option_values(el){
var native$ = shadow.dom.dom_node(el);
var opts = (native$["options"]);
var a__5759__auto__ = opts;
var l__5760__auto__ = a__5759__auto__.length;
var i = (0);
var ret = cljs.core.PersistentVector.EMPTY;
while(true){
if((i < l__5760__auto__)){
var G__31823 = (i + (1));
var G__31824 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ret,(opts[i]["value"]));
i = G__31823;
ret = G__31824;
continue;
} else {
return ret;
}
break;
}
});
shadow.dom.build_url = (function shadow$dom$build_url(path,query_params){
if(cljs.core.empty_QMARK_(query_params)){
return path;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(path)+"?"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__30730){
var vec__30731 = p__30730;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30731,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30731,(1),null);
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(k))+"="+cljs.core.str.cljs$core$IFn$_invoke$arity$1(encodeURIComponent((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)))));
}),query_params))));
}
});
shadow.dom.redirect = (function shadow$dom$redirect(var_args){
var G__30746 = arguments.length;
switch (G__30746) {
case 1:
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(shadow.dom.redirect.cljs$core$IFn$_invoke$arity$1 = (function (path){
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2(path,cljs.core.PersistentArrayMap.EMPTY);
}));

(shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2 = (function (path,query_params){
return (document["location"]["href"] = shadow.dom.build_url(path,query_params));
}));

(shadow.dom.redirect.cljs$lang$maxFixedArity = 2);

shadow.dom.reload_BANG_ = (function shadow$dom$reload_BANG_(){
return (document.location.href = document.location.href);
});
shadow.dom.tag_name = (function shadow$dom$tag_name(el){
var dom = shadow.dom.dom_node(el);
return dom.tagName;
});
shadow.dom.insert_after = (function shadow$dom$insert_after(ref,new$){
var new_node = shadow.dom.dom_node(new$);
goog.dom.insertSiblingAfter(new_node,shadow.dom.dom_node(ref));

return new_node;
});
shadow.dom.insert_before = (function shadow$dom$insert_before(ref,new$){
var new_node = shadow.dom.dom_node(new$);
goog.dom.insertSiblingBefore(new_node,shadow.dom.dom_node(ref));

return new_node;
});
shadow.dom.insert_first = (function shadow$dom$insert_first(ref,new$){
var temp__5821__auto__ = shadow.dom.dom_node(ref).firstChild;
if(cljs.core.truth_(temp__5821__auto__)){
var child = temp__5821__auto__;
return shadow.dom.insert_before(child,new$);
} else {
return shadow.dom.append.cljs$core$IFn$_invoke$arity$2(ref,new$);
}
});
shadow.dom.index_of = (function shadow$dom$index_of(el){
var el__$1 = shadow.dom.dom_node(el);
var i = (0);
while(true){
var ps = el__$1.previousSibling;
if((ps == null)){
return i;
} else {
var G__31861 = ps;
var G__31863 = (i + (1));
el__$1 = G__31861;
i = G__31863;
continue;
}
break;
}
});
shadow.dom.get_parent = (function shadow$dom$get_parent(el){
return goog.dom.getParentElement(shadow.dom.dom_node(el));
});
shadow.dom.parents = (function shadow$dom$parents(el){
var parent = shadow.dom.get_parent(el);
if(cljs.core.truth_(parent)){
return cljs.core.cons(parent,(new cljs.core.LazySeq(null,(function (){
return (shadow.dom.parents.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.parents.cljs$core$IFn$_invoke$arity$1(parent) : shadow.dom.parents.call(null,parent));
}),null,null)));
} else {
return null;
}
});
shadow.dom.matches = (function shadow$dom$matches(el,sel){
return shadow.dom.dom_node(el).matches(sel);
});
shadow.dom.get_next_sibling = (function shadow$dom$get_next_sibling(el){
return goog.dom.getNextElementSibling(shadow.dom.dom_node(el));
});
shadow.dom.get_previous_sibling = (function shadow$dom$get_previous_sibling(el){
return goog.dom.getPreviousElementSibling(shadow.dom.dom_node(el));
});
shadow.dom.xmlns = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, ["svg","http://www.w3.org/2000/svg","xlink","http://www.w3.org/1999/xlink"], null));
shadow.dom.create_svg_node = (function shadow$dom$create_svg_node(tag_def,props){
var vec__30816 = shadow.dom.parse_tag(tag_def);
var tag_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30816,(0),null);
var tag_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30816,(1),null);
var tag_classes = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30816,(2),null);
var el = document.createElementNS("http://www.w3.org/2000/svg",tag_name);
if(cljs.core.truth_(tag_id)){
el.setAttribute("id",tag_id);
} else {
}

if(cljs.core.truth_(tag_classes)){
el.setAttribute("class",shadow.dom.merge_class_string(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(props),tag_classes));
} else {
}

var seq__30826_31904 = cljs.core.seq(props);
var chunk__30827_31905 = null;
var count__30828_31906 = (0);
var i__30829_31907 = (0);
while(true){
if((i__30829_31907 < count__30828_31906)){
var vec__30885_31912 = chunk__30827_31905.cljs$core$IIndexed$_nth$arity$2(null,i__30829_31907);
var k_31913 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30885_31912,(0),null);
var v_31914 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30885_31912,(1),null);
el.setAttributeNS((function (){var temp__5823__auto__ = cljs.core.namespace(k_31913);
if(cljs.core.truth_(temp__5823__auto__)){
var ns = temp__5823__auto__;
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(shadow.dom.xmlns),ns);
} else {
return null;
}
})(),cljs.core.name(k_31913),v_31914);


var G__31935 = seq__30826_31904;
var G__31937 = chunk__30827_31905;
var G__31938 = count__30828_31906;
var G__31939 = (i__30829_31907 + (1));
seq__30826_31904 = G__31935;
chunk__30827_31905 = G__31937;
count__30828_31906 = G__31938;
i__30829_31907 = G__31939;
continue;
} else {
var temp__5823__auto___31945 = cljs.core.seq(seq__30826_31904);
if(temp__5823__auto___31945){
var seq__30826_31951__$1 = temp__5823__auto___31945;
if(cljs.core.chunked_seq_QMARK_(seq__30826_31951__$1)){
var c__5694__auto___31956 = cljs.core.chunk_first(seq__30826_31951__$1);
var G__31958 = cljs.core.chunk_rest(seq__30826_31951__$1);
var G__31959 = c__5694__auto___31956;
var G__31960 = cljs.core.count(c__5694__auto___31956);
var G__31961 = (0);
seq__30826_31904 = G__31958;
chunk__30827_31905 = G__31959;
count__30828_31906 = G__31960;
i__30829_31907 = G__31961;
continue;
} else {
var vec__30919_31967 = cljs.core.first(seq__30826_31951__$1);
var k_31968 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30919_31967,(0),null);
var v_31969 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30919_31967,(1),null);
el.setAttributeNS((function (){var temp__5823__auto____$1 = cljs.core.namespace(k_31968);
if(cljs.core.truth_(temp__5823__auto____$1)){
var ns = temp__5823__auto____$1;
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(shadow.dom.xmlns),ns);
} else {
return null;
}
})(),cljs.core.name(k_31968),v_31969);


var G__31980 = cljs.core.next(seq__30826_31951__$1);
var G__31981 = null;
var G__31982 = (0);
var G__31983 = (0);
seq__30826_31904 = G__31980;
chunk__30827_31905 = G__31981;
count__30828_31906 = G__31982;
i__30829_31907 = G__31983;
continue;
}
} else {
}
}
break;
}

return el;
});
shadow.dom.svg_node = (function shadow$dom$svg_node(el){
if((el == null)){
return null;
} else {
if((((!((el == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === el.shadow$dom$SVGElement$))))?true:false):false)){
return el.shadow$dom$SVGElement$_to_svg$arity$1(null);
} else {
return el;

}
}
});
shadow.dom.make_svg_node = (function shadow$dom$make_svg_node(structure){
var vec__30990 = shadow.dom.destructure_node(shadow.dom.create_svg_node,structure);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30990,(0),null);
var node_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30990,(1),null);
var seq__30995_32001 = cljs.core.seq(node_children);
var chunk__30997_32002 = null;
var count__30998_32003 = (0);
var i__30999_32004 = (0);
while(true){
if((i__30999_32004 < count__30998_32003)){
var child_struct_32011 = chunk__30997_32002.cljs$core$IIndexed$_nth$arity$2(null,i__30999_32004);
if((!((child_struct_32011 == null)))){
if(typeof child_struct_32011 === 'string'){
var text_32016 = (node["textContent"]);
(node["textContent"] = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(text_32016)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(child_struct_32011)));
} else {
var children_32023 = shadow.dom.svg_node(child_struct_32011);
if(cljs.core.seq_QMARK_(children_32023)){
var seq__31069_32029 = cljs.core.seq(children_32023);
var chunk__31071_32030 = null;
var count__31072_32031 = (0);
var i__31073_32033 = (0);
while(true){
if((i__31073_32033 < count__31072_32031)){
var child_32039 = chunk__31071_32030.cljs$core$IIndexed$_nth$arity$2(null,i__31073_32033);
if(cljs.core.truth_(child_32039)){
node.appendChild(child_32039);


var G__32056 = seq__31069_32029;
var G__32057 = chunk__31071_32030;
var G__32058 = count__31072_32031;
var G__32059 = (i__31073_32033 + (1));
seq__31069_32029 = G__32056;
chunk__31071_32030 = G__32057;
count__31072_32031 = G__32058;
i__31073_32033 = G__32059;
continue;
} else {
var G__32061 = seq__31069_32029;
var G__32062 = chunk__31071_32030;
var G__32063 = count__31072_32031;
var G__32064 = (i__31073_32033 + (1));
seq__31069_32029 = G__32061;
chunk__31071_32030 = G__32062;
count__31072_32031 = G__32063;
i__31073_32033 = G__32064;
continue;
}
} else {
var temp__5823__auto___32065 = cljs.core.seq(seq__31069_32029);
if(temp__5823__auto___32065){
var seq__31069_32066__$1 = temp__5823__auto___32065;
if(cljs.core.chunked_seq_QMARK_(seq__31069_32066__$1)){
var c__5694__auto___32067 = cljs.core.chunk_first(seq__31069_32066__$1);
var G__32068 = cljs.core.chunk_rest(seq__31069_32066__$1);
var G__32069 = c__5694__auto___32067;
var G__32070 = cljs.core.count(c__5694__auto___32067);
var G__32071 = (0);
seq__31069_32029 = G__32068;
chunk__31071_32030 = G__32069;
count__31072_32031 = G__32070;
i__31073_32033 = G__32071;
continue;
} else {
var child_32072 = cljs.core.first(seq__31069_32066__$1);
if(cljs.core.truth_(child_32072)){
node.appendChild(child_32072);


var G__32076 = cljs.core.next(seq__31069_32066__$1);
var G__32077 = null;
var G__32078 = (0);
var G__32079 = (0);
seq__31069_32029 = G__32076;
chunk__31071_32030 = G__32077;
count__31072_32031 = G__32078;
i__31073_32033 = G__32079;
continue;
} else {
var G__32080 = cljs.core.next(seq__31069_32066__$1);
var G__32081 = null;
var G__32082 = (0);
var G__32083 = (0);
seq__31069_32029 = G__32080;
chunk__31071_32030 = G__32081;
count__31072_32031 = G__32082;
i__31073_32033 = G__32083;
continue;
}
}
} else {
}
}
break;
}
} else {
node.appendChild(children_32023);
}
}


var G__32084 = seq__30995_32001;
var G__32085 = chunk__30997_32002;
var G__32086 = count__30998_32003;
var G__32087 = (i__30999_32004 + (1));
seq__30995_32001 = G__32084;
chunk__30997_32002 = G__32085;
count__30998_32003 = G__32086;
i__30999_32004 = G__32087;
continue;
} else {
var G__32091 = seq__30995_32001;
var G__32092 = chunk__30997_32002;
var G__32093 = count__30998_32003;
var G__32094 = (i__30999_32004 + (1));
seq__30995_32001 = G__32091;
chunk__30997_32002 = G__32092;
count__30998_32003 = G__32093;
i__30999_32004 = G__32094;
continue;
}
} else {
var temp__5823__auto___32097 = cljs.core.seq(seq__30995_32001);
if(temp__5823__auto___32097){
var seq__30995_32098__$1 = temp__5823__auto___32097;
if(cljs.core.chunked_seq_QMARK_(seq__30995_32098__$1)){
var c__5694__auto___32103 = cljs.core.chunk_first(seq__30995_32098__$1);
var G__32107 = cljs.core.chunk_rest(seq__30995_32098__$1);
var G__32108 = c__5694__auto___32103;
var G__32109 = cljs.core.count(c__5694__auto___32103);
var G__32110 = (0);
seq__30995_32001 = G__32107;
chunk__30997_32002 = G__32108;
count__30998_32003 = G__32109;
i__30999_32004 = G__32110;
continue;
} else {
var child_struct_32111 = cljs.core.first(seq__30995_32098__$1);
if((!((child_struct_32111 == null)))){
if(typeof child_struct_32111 === 'string'){
var text_32115 = (node["textContent"]);
(node["textContent"] = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(text_32115)+cljs.core.str.cljs$core$IFn$_invoke$arity$1(child_struct_32111)));
} else {
var children_32119 = shadow.dom.svg_node(child_struct_32111);
if(cljs.core.seq_QMARK_(children_32119)){
var seq__31106_32124 = cljs.core.seq(children_32119);
var chunk__31108_32125 = null;
var count__31109_32126 = (0);
var i__31110_32127 = (0);
while(true){
if((i__31110_32127 < count__31109_32126)){
var child_32130 = chunk__31108_32125.cljs$core$IIndexed$_nth$arity$2(null,i__31110_32127);
if(cljs.core.truth_(child_32130)){
node.appendChild(child_32130);


var G__32132 = seq__31106_32124;
var G__32133 = chunk__31108_32125;
var G__32134 = count__31109_32126;
var G__32135 = (i__31110_32127 + (1));
seq__31106_32124 = G__32132;
chunk__31108_32125 = G__32133;
count__31109_32126 = G__32134;
i__31110_32127 = G__32135;
continue;
} else {
var G__32141 = seq__31106_32124;
var G__32142 = chunk__31108_32125;
var G__32143 = count__31109_32126;
var G__32144 = (i__31110_32127 + (1));
seq__31106_32124 = G__32141;
chunk__31108_32125 = G__32142;
count__31109_32126 = G__32143;
i__31110_32127 = G__32144;
continue;
}
} else {
var temp__5823__auto___32150__$1 = cljs.core.seq(seq__31106_32124);
if(temp__5823__auto___32150__$1){
var seq__31106_32152__$1 = temp__5823__auto___32150__$1;
if(cljs.core.chunked_seq_QMARK_(seq__31106_32152__$1)){
var c__5694__auto___32156 = cljs.core.chunk_first(seq__31106_32152__$1);
var G__32158 = cljs.core.chunk_rest(seq__31106_32152__$1);
var G__32159 = c__5694__auto___32156;
var G__32160 = cljs.core.count(c__5694__auto___32156);
var G__32161 = (0);
seq__31106_32124 = G__32158;
chunk__31108_32125 = G__32159;
count__31109_32126 = G__32160;
i__31110_32127 = G__32161;
continue;
} else {
var child_32168 = cljs.core.first(seq__31106_32152__$1);
if(cljs.core.truth_(child_32168)){
node.appendChild(child_32168);


var G__32173 = cljs.core.next(seq__31106_32152__$1);
var G__32174 = null;
var G__32175 = (0);
var G__32176 = (0);
seq__31106_32124 = G__32173;
chunk__31108_32125 = G__32174;
count__31109_32126 = G__32175;
i__31110_32127 = G__32176;
continue;
} else {
var G__32181 = cljs.core.next(seq__31106_32152__$1);
var G__32182 = null;
var G__32183 = (0);
var G__32184 = (0);
seq__31106_32124 = G__32181;
chunk__31108_32125 = G__32182;
count__31109_32126 = G__32183;
i__31110_32127 = G__32184;
continue;
}
}
} else {
}
}
break;
}
} else {
node.appendChild(children_32119);
}
}


var G__32188 = cljs.core.next(seq__30995_32098__$1);
var G__32190 = null;
var G__32191 = (0);
var G__32192 = (0);
seq__30995_32001 = G__32188;
chunk__30997_32002 = G__32190;
count__30998_32003 = G__32191;
i__30999_32004 = G__32192;
continue;
} else {
var G__32194 = cljs.core.next(seq__30995_32098__$1);
var G__32195 = null;
var G__32196 = (0);
var G__32197 = (0);
seq__30995_32001 = G__32194;
chunk__30997_32002 = G__32195;
count__30998_32003 = G__32196;
i__30999_32004 = G__32197;
continue;
}
}
} else {
}
}
break;
}

return node;
});
(shadow.dom.SVGElement["string"] = true);

(shadow.dom._to_svg["string"] = (function (this$){
if((this$ instanceof cljs.core.Keyword)){
return shadow.dom.make_svg_node(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$], null));
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("strings cannot be in svgs",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"this","this",-611633625),this$], null));
}
}));

(cljs.core.PersistentVector.prototype.shadow$dom$SVGElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.shadow$dom$SVGElement$_to_svg$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_svg_node(this$__$1);
}));

(cljs.core.LazySeq.prototype.shadow$dom$SVGElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.LazySeq.prototype.shadow$dom$SVGElement$_to_svg$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom._to_svg,this$__$1);
}));

(shadow.dom.SVGElement["null"] = true);

(shadow.dom._to_svg["null"] = (function (_){
return null;
}));
shadow.dom.svg = (function shadow$dom$svg(var_args){
var args__5903__auto__ = [];
var len__5897__auto___32222 = arguments.length;
var i__5898__auto___32225 = (0);
while(true){
if((i__5898__auto___32225 < len__5897__auto___32222)){
args__5903__auto__.push((arguments[i__5898__auto___32225]));

var G__32234 = (i__5898__auto___32225 + (1));
i__5898__auto___32225 = G__32234;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((1) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((1)),(0),null)):null);
return shadow.dom.svg.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5904__auto__);
});

(shadow.dom.svg.cljs$core$IFn$_invoke$arity$variadic = (function (attrs,children){
return shadow.dom._to_svg(cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),attrs], null),children)));
}));

(shadow.dom.svg.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(shadow.dom.svg.cljs$lang$applyTo = (function (seq31177){
var G__31178 = cljs.core.first(seq31177);
var seq31177__$1 = cljs.core.next(seq31177);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__31178,seq31177__$1);
}));


//# sourceMappingURL=shadow.dom.js.map
