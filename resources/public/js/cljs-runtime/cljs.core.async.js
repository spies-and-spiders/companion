goog.provide('cljs.core.async');
goog.scope(function(){
  cljs.core.async.goog$module$goog$array = goog.module.get('goog.array');
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async30466 = (function (f,blockable,meta30467){
this.f = f;
this.blockable = blockable;
this.meta30467 = meta30467;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async30466.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_30468,meta30467__$1){
var self__ = this;
var _30468__$1 = this;
return (new cljs.core.async.t_cljs$core$async30466(self__.f,self__.blockable,meta30467__$1));
}));

(cljs.core.async.t_cljs$core$async30466.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_30468){
var self__ = this;
var _30468__$1 = this;
return self__.meta30467;
}));

(cljs.core.async.t_cljs$core$async30466.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async30466.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async30466.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.blockable;
}));

(cljs.core.async.t_cljs$core$async30466.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.f;
}));

(cljs.core.async.t_cljs$core$async30466.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"blockable","blockable",-28395259,null),new cljs.core.Symbol(null,"meta30467","meta30467",795890632,null)], null);
}));

(cljs.core.async.t_cljs$core$async30466.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async30466.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async30466");

(cljs.core.async.t_cljs$core$async30466.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async30466");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async30466.
 */
cljs.core.async.__GT_t_cljs$core$async30466 = (function cljs$core$async$__GT_t_cljs$core$async30466(f,blockable,meta30467){
return (new cljs.core.async.t_cljs$core$async30466(f,blockable,meta30467));
});


cljs.core.async.fn_handler = (function cljs$core$async$fn_handler(var_args){
var G__30465 = arguments.length;
switch (G__30465) {
case 1:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1 = (function (f){
return cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(f,true);
}));

(cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2 = (function (f,blockable){
return (new cljs.core.async.t_cljs$core$async30466(f,blockable,cljs.core.PersistentArrayMap.EMPTY));
}));

(cljs.core.async.fn_handler.cljs$lang$maxFixedArity = 2);

/**
 * Returns a fixed buffer of size n. When full, puts will block/park.
 */
cljs.core.async.buffer = (function cljs$core$async$buffer(n){
return cljs.core.async.impl.buffers.fixed_buffer(n);
});
/**
 * Returns a buffer of size n. When full, puts will complete but
 *   val will be dropped (no transfer).
 */
cljs.core.async.dropping_buffer = (function cljs$core$async$dropping_buffer(n){
return cljs.core.async.impl.buffers.dropping_buffer(n);
});
/**
 * Returns a buffer of size n. When full, puts will complete, and be
 *   buffered, but oldest elements in buffer will be dropped (not
 *   transferred).
 */
cljs.core.async.sliding_buffer = (function cljs$core$async$sliding_buffer(n){
return cljs.core.async.impl.buffers.sliding_buffer(n);
});
/**
 * Returns true if a channel created with buff will never block. That is to say,
 * puts into this buffer will never cause the buffer to be full. 
 */
cljs.core.async.unblocking_buffer_QMARK_ = (function cljs$core$async$unblocking_buffer_QMARK_(buff){
if((!((buff == null)))){
if(((false) || ((cljs.core.PROTOCOL_SENTINEL === buff.cljs$core$async$impl$protocols$UnblockingBuffer$)))){
return true;
} else {
if((!buff.cljs$lang$protocol_mask$partition$)){
return cljs.core.native_satisfies_QMARK_(cljs.core.async.impl.protocols.UnblockingBuffer,buff);
} else {
return false;
}
}
} else {
return cljs.core.native_satisfies_QMARK_(cljs.core.async.impl.protocols.UnblockingBuffer,buff);
}
});
/**
 * Creates a channel with an optional buffer, an optional transducer (like (map f),
 *   (filter p) etc or a composition thereof), and an optional exception handler.
 *   If buf-or-n is a number, will create and use a fixed buffer of that size. If a
 *   transducer is supplied a buffer must be specified. ex-handler must be a
 *   fn of one argument - if an exception occurs during transformation it will be called
 *   with the thrown value as an argument, and any non-nil return value will be placed
 *   in the channel.
 */
cljs.core.async.chan = (function cljs$core$async$chan(var_args){
var G__30497 = arguments.length;
switch (G__30497) {
case 0:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1 = (function (buf_or_n){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(buf_or_n,null,null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2 = (function (buf_or_n,xform){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(buf_or_n,xform,null);
}));

(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3 = (function (buf_or_n,xform,ex_handler){
var buf_or_n__$1 = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(buf_or_n,(0)))?null:buf_or_n);
if(cljs.core.truth_(xform)){
if(cljs.core.truth_(buf_or_n__$1)){
} else {
throw (new Error((""+"Assert failed: "+"buffer must be supplied when transducer is"+"\n"+"buf-or-n")));
}
} else {
}

return cljs.core.async.impl.channels.chan.cljs$core$IFn$_invoke$arity$3(((typeof buf_or_n__$1 === 'number')?cljs.core.async.buffer(buf_or_n__$1):buf_or_n__$1),xform,ex_handler);
}));

(cljs.core.async.chan.cljs$lang$maxFixedArity = 3);

/**
 * Creates a promise channel with an optional transducer, and an optional
 *   exception-handler. A promise channel can take exactly one value that consumers
 *   will receive. Once full, puts complete but val is dropped (no transfer).
 *   Consumers will block until either a value is placed in the channel or the
 *   channel is closed, then return the value (or nil) forever. See chan for the
 *   semantics of xform and ex-handler.
 */
cljs.core.async.promise_chan = (function cljs$core$async$promise_chan(var_args){
var G__30512 = arguments.length;
switch (G__30512) {
case 0:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0();

break;
case 1:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$0 = (function (){
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1(null);
}));

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$1 = (function (xform){
return cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2(xform,null);
}));

(cljs.core.async.promise_chan.cljs$core$IFn$_invoke$arity$2 = (function (xform,ex_handler){
return cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3(cljs.core.async.impl.buffers.promise_buffer(),xform,ex_handler);
}));

(cljs.core.async.promise_chan.cljs$lang$maxFixedArity = 2);

/**
 * Returns a channel that will close after msecs
 */
cljs.core.async.timeout = (function cljs$core$async$timeout(msecs){
return cljs.core.async.impl.timers.timeout(msecs);
});
/**
 * takes a val from port. Must be called inside a (go ...) block. Will
 *   return nil if closed. Will park if nothing is available.
 *   Returns true unless port is already closed
 */
cljs.core.async._LT__BANG_ = (function cljs$core$async$_LT__BANG_(port){
throw (new Error("<! used not in (go ...) block"));
});
/**
 * Asynchronously takes a val from port, passing to fn1. Will pass nil
 * if closed. If on-caller? (default true) is true, and value is
 * immediately available, will call fn1 on calling thread.
 * Returns nil.
 */
cljs.core.async.take_BANG_ = (function cljs$core$async$take_BANG_(var_args){
var G__30527 = arguments.length;
switch (G__30527) {
case 2:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,fn1){
return cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3(port,fn1,true);
}));

(cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,fn1,on_caller_QMARK_){
var ret = cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(fn1));
if(cljs.core.truth_(ret)){
var val_34186 = cljs.core.deref(ret);
if(cljs.core.truth_(on_caller_QMARK_)){
(fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(val_34186) : fn1.call(null,val_34186));
} else {
cljs.core.async.impl.dispatch.run((function (){
return (fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(val_34186) : fn1.call(null,val_34186));
}));
}
} else {
}

return null;
}));

(cljs.core.async.take_BANG_.cljs$lang$maxFixedArity = 3);

cljs.core.async.nop = (function cljs$core$async$nop(_){
return null;
});
cljs.core.async.fhnop = cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(cljs.core.async.nop);
/**
 * puts a val into port. nil values are not allowed. Must be called
 *   inside a (go ...) block. Will park if no buffer space is available.
 *   Returns true unless port is already closed.
 */
cljs.core.async._GT__BANG_ = (function cljs$core$async$_GT__BANG_(port,val){
throw (new Error(">! used not in (go ...) block"));
});
/**
 * Asynchronously puts a val into port, calling fn1 (if supplied) when
 * complete. nil values are not allowed. Will throw if closed. If
 * on-caller? (default true) is true, and the put is immediately
 * accepted, will call fn1 on calling thread.  Returns nil.
 */
cljs.core.async.put_BANG_ = (function cljs$core$async$put_BANG_(var_args){
var G__30536 = arguments.length;
switch (G__30536) {
case 2:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (port,val){
var temp__5821__auto__ = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fhnop);
if(cljs.core.truth_(temp__5821__auto__)){
var ret = temp__5821__auto__;
return cljs.core.deref(ret);
} else {
return true;
}
}));

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (port,val,fn1){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4(port,val,fn1,true);
}));

(cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$4 = (function (port,val,fn1,on_caller_QMARK_){
var temp__5821__auto__ = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$1(fn1));
if(cljs.core.truth_(temp__5821__auto__)){
var retb = temp__5821__auto__;
var ret = cljs.core.deref(retb);
if(cljs.core.truth_(on_caller_QMARK_)){
(fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(ret) : fn1.call(null,ret));
} else {
cljs.core.async.impl.dispatch.run((function (){
return (fn1.cljs$core$IFn$_invoke$arity$1 ? fn1.cljs$core$IFn$_invoke$arity$1(ret) : fn1.call(null,ret));
}));
}

return ret;
} else {
return true;
}
}));

(cljs.core.async.put_BANG_.cljs$lang$maxFixedArity = 4);

cljs.core.async.close_BANG_ = (function cljs$core$async$close_BANG_(port){
return cljs.core.async.impl.protocols.close_BANG_(port);
});
cljs.core.async.random_array = (function cljs$core$async$random_array(n){
var a = (new Array(n));
var n__5762__auto___34205 = n;
var x_34206 = (0);
while(true){
if((x_34206 < n__5762__auto___34205)){
(a[x_34206] = x_34206);

var G__34207 = (x_34206 + (1));
x_34206 = G__34207;
continue;
} else {
}
break;
}

cljs.core.async.goog$module$goog$array.shuffle(a);

return a;
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async30549 = (function (flag,meta30550){
this.flag = flag;
this.meta30550 = meta30550;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async30549.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_30551,meta30550__$1){
var self__ = this;
var _30551__$1 = this;
return (new cljs.core.async.t_cljs$core$async30549(self__.flag,meta30550__$1));
}));

(cljs.core.async.t_cljs$core$async30549.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_30551){
var self__ = this;
var _30551__$1 = this;
return self__.meta30550;
}));

(cljs.core.async.t_cljs$core$async30549.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async30549.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.deref(self__.flag);
}));

(cljs.core.async.t_cljs$core$async30549.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async30549.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.flag,null);

return true;
}));

(cljs.core.async.t_cljs$core$async30549.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"meta30550","meta30550",524291206,null)], null);
}));

(cljs.core.async.t_cljs$core$async30549.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async30549.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async30549");

(cljs.core.async.t_cljs$core$async30549.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async30549");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async30549.
 */
cljs.core.async.__GT_t_cljs$core$async30549 = (function cljs$core$async$__GT_t_cljs$core$async30549(flag,meta30550){
return (new cljs.core.async.t_cljs$core$async30549(flag,meta30550));
});


cljs.core.async.alt_flag = (function cljs$core$async$alt_flag(){
var flag = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(true);
return (new cljs.core.async.t_cljs$core$async30549(flag,cljs.core.PersistentArrayMap.EMPTY));
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async30584 = (function (flag,cb,meta30585){
this.flag = flag;
this.cb = cb;
this.meta30585 = meta30585;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async30584.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_30586,meta30585__$1){
var self__ = this;
var _30586__$1 = this;
return (new cljs.core.async.t_cljs$core$async30584(self__.flag,self__.cb,meta30585__$1));
}));

(cljs.core.async.t_cljs$core$async30584.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_30586){
var self__ = this;
var _30586__$1 = this;
return self__.meta30585;
}));

(cljs.core.async.t_cljs$core$async30584.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async30584.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.active_QMARK_(self__.flag);
}));

(cljs.core.async.t_cljs$core$async30584.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async30584.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.async.impl.protocols.commit(self__.flag);

return self__.cb;
}));

(cljs.core.async.t_cljs$core$async30584.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"flag","flag",-1565787888,null),new cljs.core.Symbol(null,"cb","cb",-2064487928,null),new cljs.core.Symbol(null,"meta30585","meta30585",-1754864241,null)], null);
}));

(cljs.core.async.t_cljs$core$async30584.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async30584.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async30584");

(cljs.core.async.t_cljs$core$async30584.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async30584");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async30584.
 */
cljs.core.async.__GT_t_cljs$core$async30584 = (function cljs$core$async$__GT_t_cljs$core$async30584(flag,cb,meta30585){
return (new cljs.core.async.t_cljs$core$async30584(flag,cb,meta30585));
});


cljs.core.async.alt_handler = (function cljs$core$async$alt_handler(flag,cb){
return (new cljs.core.async.t_cljs$core$async30584(flag,cb,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * returns derefable [val port] if immediate, nil if enqueued
 */
cljs.core.async.do_alts = (function cljs$core$async$do_alts(fret,ports,opts){
if((cljs.core.count(ports) > (0))){
} else {
throw (new Error((""+"Assert failed: "+"alts must have at least one channel operation"+"\n"+"(pos? (count ports))")));
}

var flag = cljs.core.async.alt_flag();
var ports__$1 = cljs.core.vec(ports);
var n = cljs.core.count(ports__$1);
var _ = (function (){var i = (0);
while(true){
if((i < n)){
var port_34214 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ports__$1,i);
if(cljs.core.vector_QMARK_(port_34214)){
if((!(((port_34214.cljs$core$IFn$_invoke$arity$1 ? port_34214.cljs$core$IFn$_invoke$arity$1((1)) : port_34214.call(null,(1))) == null)))){
} else {
throw (new Error((""+"Assert failed: "+"can't put nil on channel"+"\n"+"(some? (port 1))")));
}
} else {
}

var G__34222 = (i + (1));
i = G__34222;
continue;
} else {
return null;
}
break;
}
})();
var idxs = cljs.core.async.random_array(n);
var priority = new cljs.core.Keyword(null,"priority","priority",1431093715).cljs$core$IFn$_invoke$arity$1(opts);
var ret = (function (){var i = (0);
while(true){
if((i < n)){
var idx = (cljs.core.truth_(priority)?i:(idxs[i]));
var port = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ports__$1,idx);
var wport = ((cljs.core.vector_QMARK_(port))?(port.cljs$core$IFn$_invoke$arity$1 ? port.cljs$core$IFn$_invoke$arity$1((0)) : port.call(null,(0))):null);
var vbox = (cljs.core.truth_(wport)?(function (){var val = (port.cljs$core$IFn$_invoke$arity$1 ? port.cljs$core$IFn$_invoke$arity$1((1)) : port.call(null,(1)));
return cljs.core.async.impl.protocols.put_BANG_(wport,val,cljs.core.async.alt_handler(flag,((function (i,val,idx,port,wport,flag,ports__$1,n,_,idxs,priority){
return (function (p1__30644_SHARP_){
var G__30679 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__30644_SHARP_,wport], null);
return (fret.cljs$core$IFn$_invoke$arity$1 ? fret.cljs$core$IFn$_invoke$arity$1(G__30679) : fret.call(null,G__30679));
});})(i,val,idx,port,wport,flag,ports__$1,n,_,idxs,priority))
));
})():cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.alt_handler(flag,((function (i,idx,port,wport,flag,ports__$1,n,_,idxs,priority){
return (function (p1__30645_SHARP_){
var G__30681 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [p1__30645_SHARP_,port], null);
return (fret.cljs$core$IFn$_invoke$arity$1 ? fret.cljs$core$IFn$_invoke$arity$1(G__30681) : fret.call(null,G__30681));
});})(i,idx,port,wport,flag,ports__$1,n,_,idxs,priority))
)));
if(cljs.core.truth_(vbox)){
return cljs.core.async.impl.channels.box(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.deref(vbox),(function (){var or__5162__auto__ = wport;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return port;
}
})()], null));
} else {
var G__34241 = (i + (1));
i = G__34241;
continue;
}
} else {
return null;
}
break;
}
})();
var or__5162__auto__ = ret;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
if(cljs.core.contains_QMARK_(opts,new cljs.core.Keyword(null,"default","default",-1987822328))){
var temp__5823__auto__ = (function (){var and__5160__auto__ = flag.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1(null);
if(cljs.core.truth_(and__5160__auto__)){
return flag.cljs$core$async$impl$protocols$Handler$commit$arity$1(null);
} else {
return and__5160__auto__;
}
})();
if(cljs.core.truth_(temp__5823__auto__)){
var got = temp__5823__auto__;
return cljs.core.async.impl.channels.box(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"default","default",-1987822328).cljs$core$IFn$_invoke$arity$1(opts),new cljs.core.Keyword(null,"default","default",-1987822328)], null));
} else {
return null;
}
} else {
return null;
}
}
});
/**
 * Completes at most one of several channel operations. Must be called
 * inside a (go ...) block. ports is a vector of channel endpoints,
 * which can be either a channel to take from or a vector of
 *   [channel-to-put-to val-to-put], in any combination. Takes will be
 *   made as if by <!, and puts will be made as if by >!. Unless
 *   the :priority option is true, if more than one port operation is
 *   ready a non-deterministic choice will be made. If no operation is
 *   ready and a :default value is supplied, [default-val :default] will
 *   be returned, otherwise alts! will park until the first operation to
 *   become ready completes. Returns [val port] of the completed
 *   operation, where val is the value taken for takes, and a
 *   boolean (true unless already closed, as per put!) for puts.
 * 
 *   opts are passed as :key val ... Supported options:
 * 
 *   :default val - the value to use if none of the operations are immediately ready
 *   :priority true - (default nil) when true, the operations will be tried in order.
 * 
 *   Note: there is no guarantee that the port exps or val exprs will be
 *   used, nor in what order should they be, so they should not be
 *   depended upon for side effects.
 */
cljs.core.async.alts_BANG_ = (function cljs$core$async$alts_BANG_(var_args){
var args__5903__auto__ = [];
var len__5897__auto___34245 = arguments.length;
var i__5898__auto___34246 = (0);
while(true){
if((i__5898__auto___34246 < len__5897__auto___34245)){
args__5903__auto__.push((arguments[i__5898__auto___34246]));

var G__34247 = (i__5898__auto___34246 + (1));
i__5898__auto___34246 = G__34247;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((1) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((1)),(0),null)):null);
return cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__5904__auto__);
});

(cljs.core.async.alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (ports,p__30703){
var map__30704 = p__30703;
var map__30704__$1 = cljs.core.__destructure_map(map__30704);
var opts = map__30704__$1;
throw (new Error("alts! used not in (go ...) block"));
}));

(cljs.core.async.alts_BANG_.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(cljs.core.async.alts_BANG_.cljs$lang$applyTo = (function (seq30696){
var G__30697 = cljs.core.first(seq30696);
var seq30696__$1 = cljs.core.next(seq30696);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__30697,seq30696__$1);
}));

/**
 * Puts a val into port if it's possible to do so immediately.
 *   nil values are not allowed. Never blocks. Returns true if offer succeeds.
 */
cljs.core.async.offer_BANG_ = (function cljs$core$async$offer_BANG_(port,val){
var ret = cljs.core.async.impl.protocols.put_BANG_(port,val,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref(ret);
} else {
return null;
}
});
/**
 * Takes a val from port if it's possible to do so immediately.
 *   Never blocks. Returns value if successful, nil otherwise.
 */
cljs.core.async.poll_BANG_ = (function cljs$core$async$poll_BANG_(port){
var ret = cljs.core.async.impl.protocols.take_BANG_(port,cljs.core.async.fn_handler.cljs$core$IFn$_invoke$arity$2(cljs.core.async.nop,false));
if(cljs.core.truth_(ret)){
return cljs.core.deref(ret);
} else {
return null;
}
});
/**
 * Takes elements from the from channel and supplies them to the to
 * channel. By default, the to channel will be closed when the from
 * channel closes, but can be determined by the close?  parameter. Will
 * stop consuming the from channel if the to channel closes
 */
cljs.core.async.pipe = (function cljs$core$async$pipe(var_args){
var G__30721 = arguments.length;
switch (G__30721) {
case 2:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$2 = (function (from,to){
return cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3(from,to,true);
}));

(cljs.core.async.pipe.cljs$core$IFn$_invoke$arity$3 = (function (from,to,close_QMARK_){
var c__30379__auto___34253 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_30791){
var state_val_30792 = (state_30791[(1)]);
if((state_val_30792 === (7))){
var inst_30779 = (state_30791[(2)]);
var state_30791__$1 = state_30791;
var statearr_30813_34256 = state_30791__$1;
(statearr_30813_34256[(2)] = inst_30779);

(statearr_30813_34256[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (1))){
var state_30791__$1 = state_30791;
var statearr_30815_34257 = state_30791__$1;
(statearr_30815_34257[(2)] = null);

(statearr_30815_34257[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (4))){
var inst_30757 = (state_30791[(7)]);
var inst_30757__$1 = (state_30791[(2)]);
var inst_30760 = (inst_30757__$1 == null);
var state_30791__$1 = (function (){var statearr_30819 = state_30791;
(statearr_30819[(7)] = inst_30757__$1);

return statearr_30819;
})();
if(cljs.core.truth_(inst_30760)){
var statearr_30820_34261 = state_30791__$1;
(statearr_30820_34261[(1)] = (5));

} else {
var statearr_30821_34262 = state_30791__$1;
(statearr_30821_34262[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (13))){
var state_30791__$1 = state_30791;
var statearr_30822_34263 = state_30791__$1;
(statearr_30822_34263[(2)] = null);

(statearr_30822_34263[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (6))){
var inst_30757 = (state_30791[(7)]);
var state_30791__$1 = state_30791;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_30791__$1,(11),to,inst_30757);
} else {
if((state_val_30792 === (3))){
var inst_30784 = (state_30791[(2)]);
var state_30791__$1 = state_30791;
return cljs.core.async.impl.ioc_helpers.return_chan(state_30791__$1,inst_30784);
} else {
if((state_val_30792 === (12))){
var state_30791__$1 = state_30791;
var statearr_30833_34265 = state_30791__$1;
(statearr_30833_34265[(2)] = null);

(statearr_30833_34265[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (2))){
var state_30791__$1 = state_30791;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_30791__$1,(4),from);
} else {
if((state_val_30792 === (11))){
var inst_30772 = (state_30791[(2)]);
var state_30791__$1 = state_30791;
if(cljs.core.truth_(inst_30772)){
var statearr_30839_34266 = state_30791__$1;
(statearr_30839_34266[(1)] = (12));

} else {
var statearr_30840_34267 = state_30791__$1;
(statearr_30840_34267[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (9))){
var state_30791__$1 = state_30791;
var statearr_30841_34268 = state_30791__$1;
(statearr_30841_34268[(2)] = null);

(statearr_30841_34268[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (5))){
var state_30791__$1 = state_30791;
if(cljs.core.truth_(close_QMARK_)){
var statearr_30850_34269 = state_30791__$1;
(statearr_30850_34269[(1)] = (8));

} else {
var statearr_30851_34270 = state_30791__$1;
(statearr_30851_34270[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (14))){
var inst_30777 = (state_30791[(2)]);
var state_30791__$1 = state_30791;
var statearr_30858_34271 = state_30791__$1;
(statearr_30858_34271[(2)] = inst_30777);

(statearr_30858_34271[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (10))){
var inst_30769 = (state_30791[(2)]);
var state_30791__$1 = state_30791;
var statearr_30864_34272 = state_30791__$1;
(statearr_30864_34272[(2)] = inst_30769);

(statearr_30864_34272[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_30792 === (8))){
var inst_30765 = cljs.core.async.close_BANG_(to);
var state_30791__$1 = state_30791;
var statearr_30870_34273 = state_30791__$1;
(statearr_30870_34273[(2)] = inst_30765);

(statearr_30870_34273[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_30877 = [null,null,null,null,null,null,null,null];
(statearr_30877[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_30877[(1)] = (1));

return statearr_30877;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_30791){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_30791);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e30882){var ex__29648__auto__ = e30882;
var statearr_30884_34275 = state_30791;
(statearr_30884_34275[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_30791[(4)]))){
var statearr_30892_34276 = state_30791;
(statearr_30892_34276[(1)] = cljs.core.first((state_30791[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34277 = state_30791;
state_30791 = G__34277;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_30791){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_30791);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_30893 = f__30380__auto__();
(statearr_30893[(6)] = c__30379__auto___34253);

return statearr_30893;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return to;
}));

(cljs.core.async.pipe.cljs$lang$maxFixedArity = 3);

cljs.core.async.pipeline_STAR_ = (function cljs$core$async$pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,type){
if((n > (0))){
} else {
throw (new Error("Assert failed: (pos? n)"));
}

var jobs = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(n);
var results = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(n);
var process__$1 = (function (p__30926){
var vec__30927 = p__30926;
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30927,(0),null);
var p = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__30927,(1),null);
var job = vec__30927;
if((job == null)){
cljs.core.async.close_BANG_(results);

return null;
} else {
var res = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$3((1),xf,ex_handler);
var c__30379__auto___34280 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_30954){
var state_val_30955 = (state_30954[(1)]);
if((state_val_30955 === (1))){
var state_30954__$1 = state_30954;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_30954__$1,(2),res,v);
} else {
if((state_val_30955 === (2))){
var inst_30951 = (state_30954[(2)]);
var inst_30952 = cljs.core.async.close_BANG_(res);
var state_30954__$1 = (function (){var statearr_30980 = state_30954;
(statearr_30980[(7)] = inst_30951);

return statearr_30980;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_30954__$1,inst_30952);
} else {
return null;
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0 = (function (){
var statearr_30984 = [null,null,null,null,null,null,null,null];
(statearr_30984[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__);

(statearr_30984[(1)] = (1));

return statearr_30984;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1 = (function (state_30954){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_30954);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e30988){var ex__29648__auto__ = e30988;
var statearr_30993_34285 = state_30954;
(statearr_30993_34285[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_30954[(4)]))){
var statearr_30994_34286 = state_30954;
(statearr_30994_34286[(1)] = cljs.core.first((state_30954[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34287 = state_30954;
state_30954 = G__34287;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = function(state_30954){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1.call(this,state_30954);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_31005 = f__30380__auto__();
(statearr_31005[(6)] = c__30379__auto___34280);

return statearr_31005;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(p,res);

return true;
}
});
var async = (function (p__31013){
var vec__31015 = p__31013;
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31015,(0),null);
var p = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__31015,(1),null);
var job = vec__31015;
if((job == null)){
cljs.core.async.close_BANG_(results);

return null;
} else {
var res = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
(xf.cljs$core$IFn$_invoke$arity$2 ? xf.cljs$core$IFn$_invoke$arity$2(v,res) : xf.call(null,v,res));

cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(p,res);

return true;
}
});
var n__5762__auto___34298 = n;
var __34299 = (0);
while(true){
if((__34299 < n__5762__auto___34298)){
var G__31020_34301 = type;
var G__31020_34302__$1 = (((G__31020_34301 instanceof cljs.core.Keyword))?G__31020_34301.fqn:null);
switch (G__31020_34302__$1) {
case "compute":
var c__30379__auto___34314 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (__34299,c__30379__auto___34314,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async){
return (function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = ((function (__34299,c__30379__auto___34314,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async){
return (function (state_31039){
var state_val_31040 = (state_31039[(1)]);
if((state_val_31040 === (1))){
var state_31039__$1 = state_31039;
var statearr_31042_34323 = state_31039__$1;
(statearr_31042_34323[(2)] = null);

(statearr_31042_34323[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31040 === (2))){
var state_31039__$1 = state_31039;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31039__$1,(4),jobs);
} else {
if((state_val_31040 === (3))){
var inst_31037 = (state_31039[(2)]);
var state_31039__$1 = state_31039;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31039__$1,inst_31037);
} else {
if((state_val_31040 === (4))){
var inst_31028 = (state_31039[(2)]);
var inst_31029 = process__$1(inst_31028);
var state_31039__$1 = state_31039;
if(cljs.core.truth_(inst_31029)){
var statearr_31059_34329 = state_31039__$1;
(statearr_31059_34329[(1)] = (5));

} else {
var statearr_31060_34330 = state_31039__$1;
(statearr_31060_34330[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31040 === (5))){
var state_31039__$1 = state_31039;
var statearr_31062_34331 = state_31039__$1;
(statearr_31062_34331[(2)] = null);

(statearr_31062_34331[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31040 === (6))){
var state_31039__$1 = state_31039;
var statearr_31065_34332 = state_31039__$1;
(statearr_31065_34332[(2)] = null);

(statearr_31065_34332[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31040 === (7))){
var inst_31035 = (state_31039[(2)]);
var state_31039__$1 = state_31039;
var statearr_31068_34335 = state_31039__$1;
(statearr_31068_34335[(2)] = inst_31035);

(statearr_31068_34335[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__34299,c__30379__auto___34314,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async))
;
return ((function (__34299,switch__29644__auto__,c__30379__auto___34314,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0 = (function (){
var statearr_31075 = [null,null,null,null,null,null,null];
(statearr_31075[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__);

(statearr_31075[(1)] = (1));

return statearr_31075;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1 = (function (state_31039){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31039);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31076){var ex__29648__auto__ = e31076;
var statearr_31077_34340 = state_31039;
(statearr_31077_34340[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31039[(4)]))){
var statearr_31078_34341 = state_31039;
(statearr_31078_34341[(1)] = cljs.core.first((state_31039[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34342 = state_31039;
state_31039 = G__34342;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = function(state_31039){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1.call(this,state_31039);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__;
})()
;})(__34299,switch__29644__auto__,c__30379__auto___34314,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async))
})();
var state__30381__auto__ = (function (){var statearr_31084 = f__30380__auto__();
(statearr_31084[(6)] = c__30379__auto___34314);

return statearr_31084;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
});})(__34299,c__30379__auto___34314,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async))
);


break;
case "async":
var c__30379__auto___34344 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run(((function (__34299,c__30379__auto___34344,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async){
return (function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = ((function (__34299,c__30379__auto___34344,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async){
return (function (state_31097){
var state_val_31098 = (state_31097[(1)]);
if((state_val_31098 === (1))){
var state_31097__$1 = state_31097;
var statearr_31103_34345 = state_31097__$1;
(statearr_31103_34345[(2)] = null);

(statearr_31103_34345[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31098 === (2))){
var state_31097__$1 = state_31097;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31097__$1,(4),jobs);
} else {
if((state_val_31098 === (3))){
var inst_31095 = (state_31097[(2)]);
var state_31097__$1 = state_31097;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31097__$1,inst_31095);
} else {
if((state_val_31098 === (4))){
var inst_31087 = (state_31097[(2)]);
var inst_31088 = async(inst_31087);
var state_31097__$1 = state_31097;
if(cljs.core.truth_(inst_31088)){
var statearr_31105_34355 = state_31097__$1;
(statearr_31105_34355[(1)] = (5));

} else {
var statearr_31112_34356 = state_31097__$1;
(statearr_31112_34356[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31098 === (5))){
var state_31097__$1 = state_31097;
var statearr_31113_34357 = state_31097__$1;
(statearr_31113_34357[(2)] = null);

(statearr_31113_34357[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31098 === (6))){
var state_31097__$1 = state_31097;
var statearr_31115_34360 = state_31097__$1;
(statearr_31115_34360[(2)] = null);

(statearr_31115_34360[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31098 === (7))){
var inst_31093 = (state_31097[(2)]);
var state_31097__$1 = state_31097;
var statearr_31119_34363 = state_31097__$1;
(statearr_31119_34363[(2)] = inst_31093);

(statearr_31119_34363[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
});})(__34299,c__30379__auto___34344,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async))
;
return ((function (__34299,switch__29644__auto__,c__30379__auto___34344,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async){
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0 = (function (){
var statearr_31120 = [null,null,null,null,null,null,null];
(statearr_31120[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__);

(statearr_31120[(1)] = (1));

return statearr_31120;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1 = (function (state_31097){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31097);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31122){var ex__29648__auto__ = e31122;
var statearr_31123_34373 = state_31097;
(statearr_31123_34373[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31097[(4)]))){
var statearr_31124_34374 = state_31097;
(statearr_31124_34374[(1)] = cljs.core.first((state_31097[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34375 = state_31097;
state_31097 = G__34375;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = function(state_31097){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1.call(this,state_31097);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__;
})()
;})(__34299,switch__29644__auto__,c__30379__auto___34344,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async))
})();
var state__30381__auto__ = (function (){var statearr_31129 = f__30380__auto__();
(statearr_31129[(6)] = c__30379__auto___34344);

return statearr_31129;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
});})(__34299,c__30379__auto___34344,G__31020_34301,G__31020_34302__$1,n__5762__auto___34298,jobs,results,process__$1,async))
);


break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__31020_34302__$1))));

}

var G__34376 = (__34299 + (1));
__34299 = G__34376;
continue;
} else {
}
break;
}

var c__30379__auto___34377 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_31152){
var state_val_31153 = (state_31152[(1)]);
if((state_val_31153 === (7))){
var inst_31148 = (state_31152[(2)]);
var state_31152__$1 = state_31152;
var statearr_31159_34381 = state_31152__$1;
(statearr_31159_34381[(2)] = inst_31148);

(statearr_31159_34381[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31153 === (1))){
var state_31152__$1 = state_31152;
var statearr_31161_34383 = state_31152__$1;
(statearr_31161_34383[(2)] = null);

(statearr_31161_34383[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31153 === (4))){
var inst_31132 = (state_31152[(7)]);
var inst_31132__$1 = (state_31152[(2)]);
var inst_31133 = (inst_31132__$1 == null);
var state_31152__$1 = (function (){var statearr_31162 = state_31152;
(statearr_31162[(7)] = inst_31132__$1);

return statearr_31162;
})();
if(cljs.core.truth_(inst_31133)){
var statearr_31164_34384 = state_31152__$1;
(statearr_31164_34384[(1)] = (5));

} else {
var statearr_31166_34387 = state_31152__$1;
(statearr_31166_34387[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31153 === (6))){
var inst_31132 = (state_31152[(7)]);
var inst_31137 = (state_31152[(8)]);
var inst_31137__$1 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var inst_31139 = cljs.core.PersistentVector.EMPTY_NODE;
var inst_31140 = [inst_31132,inst_31137__$1];
var inst_31141 = (new cljs.core.PersistentVector(null,2,(5),inst_31139,inst_31140,null));
var state_31152__$1 = (function (){var statearr_31171 = state_31152;
(statearr_31171[(8)] = inst_31137__$1);

return statearr_31171;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31152__$1,(8),jobs,inst_31141);
} else {
if((state_val_31153 === (3))){
var inst_31150 = (state_31152[(2)]);
var state_31152__$1 = state_31152;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31152__$1,inst_31150);
} else {
if((state_val_31153 === (2))){
var state_31152__$1 = state_31152;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31152__$1,(4),from);
} else {
if((state_val_31153 === (9))){
var inst_31145 = (state_31152[(2)]);
var state_31152__$1 = (function (){var statearr_31176 = state_31152;
(statearr_31176[(9)] = inst_31145);

return statearr_31176;
})();
var statearr_31180_34394 = state_31152__$1;
(statearr_31180_34394[(2)] = null);

(statearr_31180_34394[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31153 === (5))){
var inst_31135 = cljs.core.async.close_BANG_(jobs);
var state_31152__$1 = state_31152;
var statearr_31181_34395 = state_31152__$1;
(statearr_31181_34395[(2)] = inst_31135);

(statearr_31181_34395[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31153 === (8))){
var inst_31137 = (state_31152[(8)]);
var inst_31143 = (state_31152[(2)]);
var state_31152__$1 = (function (){var statearr_31182 = state_31152;
(statearr_31182[(10)] = inst_31143);

return statearr_31182;
})();
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31152__$1,(9),results,inst_31137);
} else {
return null;
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0 = (function (){
var statearr_31187 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_31187[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__);

(statearr_31187[(1)] = (1));

return statearr_31187;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1 = (function (state_31152){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31152);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31192){var ex__29648__auto__ = e31192;
var statearr_31193_34405 = state_31152;
(statearr_31193_34405[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31152[(4)]))){
var statearr_31194_34406 = state_31152;
(statearr_31194_34406[(1)] = cljs.core.first((state_31152[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34413 = state_31152;
state_31152 = G__34413;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = function(state_31152){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1.call(this,state_31152);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_31195 = f__30380__auto__();
(statearr_31195[(6)] = c__30379__auto___34377);

return statearr_31195;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


var c__30379__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_31241){
var state_val_31242 = (state_31241[(1)]);
if((state_val_31242 === (7))){
var inst_31231 = (state_31241[(2)]);
var state_31241__$1 = state_31241;
var statearr_31247_34423 = state_31241__$1;
(statearr_31247_34423[(2)] = inst_31231);

(statearr_31247_34423[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (20))){
var state_31241__$1 = state_31241;
var statearr_31248_34424 = state_31241__$1;
(statearr_31248_34424[(2)] = null);

(statearr_31248_34424[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (1))){
var state_31241__$1 = state_31241;
var statearr_31249_34425 = state_31241__$1;
(statearr_31249_34425[(2)] = null);

(statearr_31249_34425[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (4))){
var inst_31198 = (state_31241[(7)]);
var inst_31198__$1 = (state_31241[(2)]);
var inst_31199 = (inst_31198__$1 == null);
var state_31241__$1 = (function (){var statearr_31252 = state_31241;
(statearr_31252[(7)] = inst_31198__$1);

return statearr_31252;
})();
if(cljs.core.truth_(inst_31199)){
var statearr_31253_34427 = state_31241__$1;
(statearr_31253_34427[(1)] = (5));

} else {
var statearr_31254_34434 = state_31241__$1;
(statearr_31254_34434[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (15))){
var inst_31211 = (state_31241[(8)]);
var state_31241__$1 = state_31241;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31241__$1,(18),to,inst_31211);
} else {
if((state_val_31242 === (21))){
var inst_31226 = (state_31241[(2)]);
var state_31241__$1 = state_31241;
var statearr_31257_34435 = state_31241__$1;
(statearr_31257_34435[(2)] = inst_31226);

(statearr_31257_34435[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (13))){
var inst_31228 = (state_31241[(2)]);
var state_31241__$1 = (function (){var statearr_31259 = state_31241;
(statearr_31259[(9)] = inst_31228);

return statearr_31259;
})();
var statearr_31260_34443 = state_31241__$1;
(statearr_31260_34443[(2)] = null);

(statearr_31260_34443[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (6))){
var inst_31198 = (state_31241[(7)]);
var state_31241__$1 = state_31241;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31241__$1,(11),inst_31198);
} else {
if((state_val_31242 === (17))){
var inst_31220 = (state_31241[(2)]);
var state_31241__$1 = state_31241;
if(cljs.core.truth_(inst_31220)){
var statearr_31265_34444 = state_31241__$1;
(statearr_31265_34444[(1)] = (19));

} else {
var statearr_31266_34452 = state_31241__$1;
(statearr_31266_34452[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (3))){
var inst_31234 = (state_31241[(2)]);
var state_31241__$1 = state_31241;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31241__$1,inst_31234);
} else {
if((state_val_31242 === (12))){
var inst_31208 = (state_31241[(10)]);
var state_31241__$1 = state_31241;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31241__$1,(14),inst_31208);
} else {
if((state_val_31242 === (2))){
var state_31241__$1 = state_31241;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31241__$1,(4),results);
} else {
if((state_val_31242 === (19))){
var state_31241__$1 = state_31241;
var statearr_31274_34454 = state_31241__$1;
(statearr_31274_34454[(2)] = null);

(statearr_31274_34454[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (11))){
var inst_31208 = (state_31241[(2)]);
var state_31241__$1 = (function (){var statearr_31275 = state_31241;
(statearr_31275[(10)] = inst_31208);

return statearr_31275;
})();
var statearr_31277_34460 = state_31241__$1;
(statearr_31277_34460[(2)] = null);

(statearr_31277_34460[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (9))){
var state_31241__$1 = state_31241;
var statearr_31280_34463 = state_31241__$1;
(statearr_31280_34463[(2)] = null);

(statearr_31280_34463[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (5))){
var state_31241__$1 = state_31241;
if(cljs.core.truth_(close_QMARK_)){
var statearr_31281_34465 = state_31241__$1;
(statearr_31281_34465[(1)] = (8));

} else {
var statearr_31283_34468 = state_31241__$1;
(statearr_31283_34468[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (14))){
var inst_31211 = (state_31241[(8)]);
var inst_31214 = (state_31241[(11)]);
var inst_31211__$1 = (state_31241[(2)]);
var inst_31213 = (inst_31211__$1 == null);
var inst_31214__$1 = cljs.core.not(inst_31213);
var state_31241__$1 = (function (){var statearr_31284 = state_31241;
(statearr_31284[(8)] = inst_31211__$1);

(statearr_31284[(11)] = inst_31214__$1);

return statearr_31284;
})();
if(inst_31214__$1){
var statearr_31285_34478 = state_31241__$1;
(statearr_31285_34478[(1)] = (15));

} else {
var statearr_31286_34479 = state_31241__$1;
(statearr_31286_34479[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (16))){
var inst_31214 = (state_31241[(11)]);
var state_31241__$1 = state_31241;
var statearr_31290_34483 = state_31241__$1;
(statearr_31290_34483[(2)] = inst_31214);

(statearr_31290_34483[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (10))){
var inst_31205 = (state_31241[(2)]);
var state_31241__$1 = state_31241;
var statearr_31295_34484 = state_31241__$1;
(statearr_31295_34484[(2)] = inst_31205);

(statearr_31295_34484[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (18))){
var inst_31217 = (state_31241[(2)]);
var state_31241__$1 = state_31241;
var statearr_31299_34485 = state_31241__$1;
(statearr_31299_34485[(2)] = inst_31217);

(statearr_31299_34485[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31242 === (8))){
var inst_31202 = cljs.core.async.close_BANG_(to);
var state_31241__$1 = state_31241;
var statearr_31303_34493 = state_31241__$1;
(statearr_31303_34493[(2)] = inst_31202);

(statearr_31303_34493[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = null;
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0 = (function (){
var statearr_31321 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_31321[(0)] = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__);

(statearr_31321[(1)] = (1));

return statearr_31321;
});
var cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1 = (function (state_31241){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31241);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31330){var ex__29648__auto__ = e31330;
var statearr_31334_34502 = state_31241;
(statearr_31334_34502[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31241[(4)]))){
var statearr_31336_34503 = state_31241;
(statearr_31336_34503[(1)] = cljs.core.first((state_31241[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34507 = state_31241;
state_31241 = G__34507;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__ = function(state_31241){
switch(arguments.length){
case 0:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1.call(this,state_31241);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____0;
cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$pipeline_STAR__$_state_machine__29645__auto____1;
return cljs$core$async$pipeline_STAR__$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_31342 = f__30380__auto__();
(statearr_31342[(6)] = c__30379__auto__);

return statearr_31342;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));

return c__30379__auto__;
});
/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the async function af, with parallelism n. af
 *   must be a function of two arguments, the first an input value and
 *   the second a channel on which to place the result(s). The
 *   presumption is that af will return immediately, having launched some
 *   asynchronous operation whose completion/callback will put results on
 *   the channel, then close! it. Outputs will be returned in order
 *   relative to the inputs. By default, the to channel will be closed
 *   when the from channel closes, but can be determined by the close?
 *   parameter. Will stop consuming the from channel if the to channel
 *   closes. See also pipeline, pipeline-blocking.
 */
cljs.core.async.pipeline_async = (function cljs$core$async$pipeline_async(var_args){
var G__31351 = arguments.length;
switch (G__31351) {
case 4:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$4 = (function (n,to,af,from){
return cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5(n,to,af,from,true);
}));

(cljs.core.async.pipeline_async.cljs$core$IFn$_invoke$arity$5 = (function (n,to,af,from,close_QMARK_){
return cljs.core.async.pipeline_STAR_(n,to,af,from,close_QMARK_,null,new cljs.core.Keyword(null,"async","async",1050769601));
}));

(cljs.core.async.pipeline_async.cljs$lang$maxFixedArity = 5);

/**
 * Takes elements from the from channel and supplies them to the to
 *   channel, subject to the transducer xf, with parallelism n. Because
 *   it is parallel, the transducer will be applied independently to each
 *   element, not across elements, and may produce zero or more outputs
 *   per input.  Outputs will be returned in order relative to the
 *   inputs. By default, the to channel will be closed when the from
 *   channel closes, but can be determined by the close?  parameter. Will
 *   stop consuming the from channel if the to channel closes.
 * 
 *   Note this is supplied for API compatibility with the Clojure version.
 *   Values of N > 1 will not result in actual concurrency in a
 *   single-threaded runtime.
 */
cljs.core.async.pipeline = (function cljs$core$async$pipeline(var_args){
var G__31364 = arguments.length;
switch (G__31364) {
case 4:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
case 6:
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$4 = (function (n,to,xf,from){
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5(n,to,xf,from,true);
}));

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$5 = (function (n,to,xf,from,close_QMARK_){
return cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6(n,to,xf,from,close_QMARK_,null);
}));

(cljs.core.async.pipeline.cljs$core$IFn$_invoke$arity$6 = (function (n,to,xf,from,close_QMARK_,ex_handler){
return cljs.core.async.pipeline_STAR_(n,to,xf,from,close_QMARK_,ex_handler,new cljs.core.Keyword(null,"compute","compute",1555393130));
}));

(cljs.core.async.pipeline.cljs$lang$maxFixedArity = 6);

/**
 * Takes a predicate and a source channel and returns a vector of two
 *   channels, the first of which will contain the values for which the
 *   predicate returned true, the second those for which it returned
 *   false.
 * 
 *   The out channels will be unbuffered by default, or two buf-or-ns can
 *   be supplied. The channels will close after the source channel has
 *   closed.
 */
cljs.core.async.split = (function cljs$core$async$split(var_args){
var G__31389 = arguments.length;
switch (G__31389) {
case 2:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 4:
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.split.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.split.cljs$core$IFn$_invoke$arity$4(p,ch,null,null);
}));

(cljs.core.async.split.cljs$core$IFn$_invoke$arity$4 = (function (p,ch,t_buf_or_n,f_buf_or_n){
var tc = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(t_buf_or_n);
var fc = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(f_buf_or_n);
var c__30379__auto___34524 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_31429){
var state_val_31430 = (state_31429[(1)]);
if((state_val_31430 === (7))){
var inst_31423 = (state_31429[(2)]);
var state_31429__$1 = state_31429;
var statearr_31431_34525 = state_31429__$1;
(statearr_31431_34525[(2)] = inst_31423);

(statearr_31431_34525[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (1))){
var state_31429__$1 = state_31429;
var statearr_31432_34526 = state_31429__$1;
(statearr_31432_34526[(2)] = null);

(statearr_31432_34526[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (4))){
var inst_31404 = (state_31429[(7)]);
var inst_31404__$1 = (state_31429[(2)]);
var inst_31405 = (inst_31404__$1 == null);
var state_31429__$1 = (function (){var statearr_31433 = state_31429;
(statearr_31433[(7)] = inst_31404__$1);

return statearr_31433;
})();
if(cljs.core.truth_(inst_31405)){
var statearr_31434_34530 = state_31429__$1;
(statearr_31434_34530[(1)] = (5));

} else {
var statearr_31435_34531 = state_31429__$1;
(statearr_31435_34531[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (13))){
var state_31429__$1 = state_31429;
var statearr_31439_34532 = state_31429__$1;
(statearr_31439_34532[(2)] = null);

(statearr_31439_34532[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (6))){
var inst_31404 = (state_31429[(7)]);
var inst_31410 = (p.cljs$core$IFn$_invoke$arity$1 ? p.cljs$core$IFn$_invoke$arity$1(inst_31404) : p.call(null,inst_31404));
var state_31429__$1 = state_31429;
if(cljs.core.truth_(inst_31410)){
var statearr_31441_34533 = state_31429__$1;
(statearr_31441_34533[(1)] = (9));

} else {
var statearr_31442_34534 = state_31429__$1;
(statearr_31442_34534[(1)] = (10));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (3))){
var inst_31425 = (state_31429[(2)]);
var state_31429__$1 = state_31429;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31429__$1,inst_31425);
} else {
if((state_val_31430 === (12))){
var state_31429__$1 = state_31429;
var statearr_31444_34535 = state_31429__$1;
(statearr_31444_34535[(2)] = null);

(statearr_31444_34535[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (2))){
var state_31429__$1 = state_31429;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31429__$1,(4),ch);
} else {
if((state_val_31430 === (11))){
var inst_31404 = (state_31429[(7)]);
var inst_31414 = (state_31429[(2)]);
var state_31429__$1 = state_31429;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31429__$1,(8),inst_31414,inst_31404);
} else {
if((state_val_31430 === (9))){
var state_31429__$1 = state_31429;
var statearr_31448_34536 = state_31429__$1;
(statearr_31448_34536[(2)] = tc);

(statearr_31448_34536[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (5))){
var inst_31407 = cljs.core.async.close_BANG_(tc);
var inst_31408 = cljs.core.async.close_BANG_(fc);
var state_31429__$1 = (function (){var statearr_31451 = state_31429;
(statearr_31451[(8)] = inst_31407);

return statearr_31451;
})();
var statearr_31452_34539 = state_31429__$1;
(statearr_31452_34539[(2)] = inst_31408);

(statearr_31452_34539[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (14))){
var inst_31421 = (state_31429[(2)]);
var state_31429__$1 = state_31429;
var statearr_31454_34540 = state_31429__$1;
(statearr_31454_34540[(2)] = inst_31421);

(statearr_31454_34540[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (10))){
var state_31429__$1 = state_31429;
var statearr_31455_34541 = state_31429__$1;
(statearr_31455_34541[(2)] = fc);

(statearr_31455_34541[(1)] = (11));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31430 === (8))){
var inst_31416 = (state_31429[(2)]);
var state_31429__$1 = state_31429;
if(cljs.core.truth_(inst_31416)){
var statearr_31456_34546 = state_31429__$1;
(statearr_31456_34546[(1)] = (12));

} else {
var statearr_31457_34547 = state_31429__$1;
(statearr_31457_34547[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_31460 = [null,null,null,null,null,null,null,null,null];
(statearr_31460[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_31460[(1)] = (1));

return statearr_31460;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_31429){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31429);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31461){var ex__29648__auto__ = e31461;
var statearr_31462_34551 = state_31429;
(statearr_31462_34551[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31429[(4)]))){
var statearr_31467_34555 = state_31429;
(statearr_31467_34555[(1)] = cljs.core.first((state_31429[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34556 = state_31429;
state_31429 = G__34556;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_31429){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_31429);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_31483 = f__30380__auto__();
(statearr_31483[(6)] = c__30379__auto___34524);

return statearr_31483;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [tc,fc], null);
}));

(cljs.core.async.split.cljs$lang$maxFixedArity = 4);

/**
 * f should be a function of 2 arguments. Returns a channel containing
 *   the single result of applying f to init and the first item from the
 *   channel, then applying f to that result and the 2nd item, etc. If
 *   the channel closes without yielding items, returns init and f is not
 *   called. ch must close before reduce produces a result.
 */
cljs.core.async.reduce = (function cljs$core$async$reduce(f,init,ch){
var c__30379__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_31554){
var state_val_31555 = (state_31554[(1)]);
if((state_val_31555 === (7))){
var inst_31545 = (state_31554[(2)]);
var state_31554__$1 = state_31554;
var statearr_31576_34557 = state_31554__$1;
(statearr_31576_34557[(2)] = inst_31545);

(statearr_31576_34557[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31555 === (1))){
var inst_31517 = init;
var inst_31518 = inst_31517;
var state_31554__$1 = (function (){var statearr_31581 = state_31554;
(statearr_31581[(7)] = inst_31518);

return statearr_31581;
})();
var statearr_31582_34558 = state_31554__$1;
(statearr_31582_34558[(2)] = null);

(statearr_31582_34558[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31555 === (4))){
var inst_31523 = (state_31554[(8)]);
var inst_31523__$1 = (state_31554[(2)]);
var inst_31528 = (inst_31523__$1 == null);
var state_31554__$1 = (function (){var statearr_31584 = state_31554;
(statearr_31584[(8)] = inst_31523__$1);

return statearr_31584;
})();
if(cljs.core.truth_(inst_31528)){
var statearr_31585_34567 = state_31554__$1;
(statearr_31585_34567[(1)] = (5));

} else {
var statearr_31586_34568 = state_31554__$1;
(statearr_31586_34568[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31555 === (6))){
var inst_31518 = (state_31554[(7)]);
var inst_31523 = (state_31554[(8)]);
var inst_31532 = (state_31554[(9)]);
var inst_31532__$1 = (f.cljs$core$IFn$_invoke$arity$2 ? f.cljs$core$IFn$_invoke$arity$2(inst_31518,inst_31523) : f.call(null,inst_31518,inst_31523));
var inst_31533 = cljs.core.reduced_QMARK_(inst_31532__$1);
var state_31554__$1 = (function (){var statearr_31587 = state_31554;
(statearr_31587[(9)] = inst_31532__$1);

return statearr_31587;
})();
if(inst_31533){
var statearr_31589_34569 = state_31554__$1;
(statearr_31589_34569[(1)] = (8));

} else {
var statearr_31594_34570 = state_31554__$1;
(statearr_31594_34570[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31555 === (3))){
var inst_31551 = (state_31554[(2)]);
var state_31554__$1 = state_31554;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31554__$1,inst_31551);
} else {
if((state_val_31555 === (2))){
var state_31554__$1 = state_31554;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31554__$1,(4),ch);
} else {
if((state_val_31555 === (9))){
var inst_31532 = (state_31554[(9)]);
var inst_31518 = inst_31532;
var state_31554__$1 = (function (){var statearr_31603 = state_31554;
(statearr_31603[(7)] = inst_31518);

return statearr_31603;
})();
var statearr_31604_34574 = state_31554__$1;
(statearr_31604_34574[(2)] = null);

(statearr_31604_34574[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31555 === (5))){
var inst_31518 = (state_31554[(7)]);
var state_31554__$1 = state_31554;
var statearr_31606_34575 = state_31554__$1;
(statearr_31606_34575[(2)] = inst_31518);

(statearr_31606_34575[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31555 === (10))){
var inst_31543 = (state_31554[(2)]);
var state_31554__$1 = state_31554;
var statearr_31607_34576 = state_31554__$1;
(statearr_31607_34576[(2)] = inst_31543);

(statearr_31607_34576[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31555 === (8))){
var inst_31532 = (state_31554[(9)]);
var inst_31539 = cljs.core.deref(inst_31532);
var state_31554__$1 = state_31554;
var statearr_31609_34580 = state_31554__$1;
(statearr_31609_34580[(2)] = inst_31539);

(statearr_31609_34580[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$reduce_$_state_machine__29645__auto__ = null;
var cljs$core$async$reduce_$_state_machine__29645__auto____0 = (function (){
var statearr_31611 = [null,null,null,null,null,null,null,null,null,null];
(statearr_31611[(0)] = cljs$core$async$reduce_$_state_machine__29645__auto__);

(statearr_31611[(1)] = (1));

return statearr_31611;
});
var cljs$core$async$reduce_$_state_machine__29645__auto____1 = (function (state_31554){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31554);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31612){var ex__29648__auto__ = e31612;
var statearr_31613_34590 = state_31554;
(statearr_31613_34590[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31554[(4)]))){
var statearr_31614_34606 = state_31554;
(statearr_31614_34606[(1)] = cljs.core.first((state_31554[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34623 = state_31554;
state_31554 = G__34623;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$reduce_$_state_machine__29645__auto__ = function(state_31554){
switch(arguments.length){
case 0:
return cljs$core$async$reduce_$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$reduce_$_state_machine__29645__auto____1.call(this,state_31554);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$reduce_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$reduce_$_state_machine__29645__auto____0;
cljs$core$async$reduce_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$reduce_$_state_machine__29645__auto____1;
return cljs$core$async$reduce_$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_31616 = f__30380__auto__();
(statearr_31616[(6)] = c__30379__auto__);

return statearr_31616;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));

return c__30379__auto__;
});
/**
 * async/reduces a channel with a transformation (xform f).
 *   Returns a channel containing the result.  ch must close before
 *   transduce produces a result.
 */
cljs.core.async.transduce = (function cljs$core$async$transduce(xform,f,init,ch){
var f__$1 = (xform.cljs$core$IFn$_invoke$arity$1 ? xform.cljs$core$IFn$_invoke$arity$1(f) : xform.call(null,f));
var c__30379__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_31626){
var state_val_31627 = (state_31626[(1)]);
if((state_val_31627 === (1))){
var inst_31619 = cljs.core.async.reduce(f__$1,init,ch);
var state_31626__$1 = state_31626;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_31626__$1,(2),inst_31619);
} else {
if((state_val_31627 === (2))){
var inst_31622 = (state_31626[(2)]);
var inst_31624 = (f__$1.cljs$core$IFn$_invoke$arity$1 ? f__$1.cljs$core$IFn$_invoke$arity$1(inst_31622) : f__$1.call(null,inst_31622));
var state_31626__$1 = state_31626;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31626__$1,inst_31624);
} else {
return null;
}
}
});
return (function() {
var cljs$core$async$transduce_$_state_machine__29645__auto__ = null;
var cljs$core$async$transduce_$_state_machine__29645__auto____0 = (function (){
var statearr_31663 = [null,null,null,null,null,null,null];
(statearr_31663[(0)] = cljs$core$async$transduce_$_state_machine__29645__auto__);

(statearr_31663[(1)] = (1));

return statearr_31663;
});
var cljs$core$async$transduce_$_state_machine__29645__auto____1 = (function (state_31626){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31626);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31671){var ex__29648__auto__ = e31671;
var statearr_31673_34643 = state_31626;
(statearr_31673_34643[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31626[(4)]))){
var statearr_31681_34644 = state_31626;
(statearr_31681_34644[(1)] = cljs.core.first((state_31626[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34645 = state_31626;
state_31626 = G__34645;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$transduce_$_state_machine__29645__auto__ = function(state_31626){
switch(arguments.length){
case 0:
return cljs$core$async$transduce_$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$transduce_$_state_machine__29645__auto____1.call(this,state_31626);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$transduce_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$transduce_$_state_machine__29645__auto____0;
cljs$core$async$transduce_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$transduce_$_state_machine__29645__auto____1;
return cljs$core$async$transduce_$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_31687 = f__30380__auto__();
(statearr_31687[(6)] = c__30379__auto__);

return statearr_31687;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));

return c__30379__auto__;
});
/**
 * Puts the contents of coll into the supplied channel.
 * 
 *   By default the channel will be closed after the items are copied,
 *   but can be determined by the close? parameter.
 * 
 *   Returns a channel which will close after the items are copied.
 */
cljs.core.async.onto_chan_BANG_ = (function cljs$core$async$onto_chan_BANG_(var_args){
var G__31692 = arguments.length;
switch (G__31692) {
case 2:
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2 = (function (ch,coll){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,true);
}));

(cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3 = (function (ch,coll,close_QMARK_){
var c__30379__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_31737){
var state_val_31738 = (state_31737[(1)]);
if((state_val_31738 === (7))){
var inst_31718 = (state_31737[(2)]);
var state_31737__$1 = state_31737;
var statearr_31744_34653 = state_31737__$1;
(statearr_31744_34653[(2)] = inst_31718);

(statearr_31744_34653[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (1))){
var inst_31711 = cljs.core.seq(coll);
var inst_31712 = inst_31711;
var state_31737__$1 = (function (){var statearr_31745 = state_31737;
(statearr_31745[(7)] = inst_31712);

return statearr_31745;
})();
var statearr_31746_34654 = state_31737__$1;
(statearr_31746_34654[(2)] = null);

(statearr_31746_34654[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (4))){
var inst_31712 = (state_31737[(7)]);
var inst_31716 = cljs.core.first(inst_31712);
var state_31737__$1 = state_31737;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_31737__$1,(7),ch,inst_31716);
} else {
if((state_val_31738 === (13))){
var inst_31731 = (state_31737[(2)]);
var state_31737__$1 = state_31737;
var statearr_31748_34655 = state_31737__$1;
(statearr_31748_34655[(2)] = inst_31731);

(statearr_31748_34655[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (6))){
var inst_31721 = (state_31737[(2)]);
var state_31737__$1 = state_31737;
if(cljs.core.truth_(inst_31721)){
var statearr_31749_34656 = state_31737__$1;
(statearr_31749_34656[(1)] = (8));

} else {
var statearr_31750_34657 = state_31737__$1;
(statearr_31750_34657[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (3))){
var inst_31735 = (state_31737[(2)]);
var state_31737__$1 = state_31737;
return cljs.core.async.impl.ioc_helpers.return_chan(state_31737__$1,inst_31735);
} else {
if((state_val_31738 === (12))){
var state_31737__$1 = state_31737;
var statearr_31752_34662 = state_31737__$1;
(statearr_31752_34662[(2)] = null);

(statearr_31752_34662[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (2))){
var inst_31712 = (state_31737[(7)]);
var state_31737__$1 = state_31737;
if(cljs.core.truth_(inst_31712)){
var statearr_31753_34665 = state_31737__$1;
(statearr_31753_34665[(1)] = (4));

} else {
var statearr_31755_34666 = state_31737__$1;
(statearr_31755_34666[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (11))){
var inst_31728 = cljs.core.async.close_BANG_(ch);
var state_31737__$1 = state_31737;
var statearr_31756_34667 = state_31737__$1;
(statearr_31756_34667[(2)] = inst_31728);

(statearr_31756_34667[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (9))){
var state_31737__$1 = state_31737;
if(cljs.core.truth_(close_QMARK_)){
var statearr_31757_34668 = state_31737__$1;
(statearr_31757_34668[(1)] = (11));

} else {
var statearr_31758_34669 = state_31737__$1;
(statearr_31758_34669[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (5))){
var inst_31712 = (state_31737[(7)]);
var state_31737__$1 = state_31737;
var statearr_31759_34670 = state_31737__$1;
(statearr_31759_34670[(2)] = inst_31712);

(statearr_31759_34670[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (10))){
var inst_31733 = (state_31737[(2)]);
var state_31737__$1 = state_31737;
var statearr_31760_34671 = state_31737__$1;
(statearr_31760_34671[(2)] = inst_31733);

(statearr_31760_34671[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_31738 === (8))){
var inst_31712 = (state_31737[(7)]);
var inst_31723 = cljs.core.next(inst_31712);
var inst_31712__$1 = inst_31723;
var state_31737__$1 = (function (){var statearr_31762 = state_31737;
(statearr_31762[(7)] = inst_31712__$1);

return statearr_31762;
})();
var statearr_31764_34672 = state_31737__$1;
(statearr_31764_34672[(2)] = null);

(statearr_31764_34672[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_31766 = [null,null,null,null,null,null,null,null];
(statearr_31766[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_31766[(1)] = (1));

return statearr_31766;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_31737){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_31737);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e31767){var ex__29648__auto__ = e31767;
var statearr_31768_34673 = state_31737;
(statearr_31768_34673[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_31737[(4)]))){
var statearr_31769_34674 = state_31737;
(statearr_31769_34674[(1)] = cljs.core.first((state_31737[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34675 = state_31737;
state_31737 = G__34675;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_31737){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_31737);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_31771 = f__30380__auto__();
(statearr_31771[(6)] = c__30379__auto__);

return statearr_31771;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));

return c__30379__auto__;
}));

(cljs.core.async.onto_chan_BANG_.cljs$lang$maxFixedArity = 3);

/**
 * Creates and returns a channel which contains the contents of coll,
 *   closing when exhausted.
 */
cljs.core.async.to_chan_BANG_ = (function cljs$core$async$to_chan_BANG_(coll){
var ch = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(cljs.core.bounded_count((100),coll));
cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$2(ch,coll);

return ch;
});
/**
 * Deprecated - use onto-chan!
 */
cljs.core.async.onto_chan = (function cljs$core$async$onto_chan(var_args){
var G__31779 = arguments.length;
switch (G__31779) {
case 2:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$2 = (function (ch,coll){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,true);
}));

(cljs.core.async.onto_chan.cljs$core$IFn$_invoke$arity$3 = (function (ch,coll,close_QMARK_){
return cljs.core.async.onto_chan_BANG_.cljs$core$IFn$_invoke$arity$3(ch,coll,close_QMARK_);
}));

(cljs.core.async.onto_chan.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - use to-chan!
 */
cljs.core.async.to_chan = (function cljs$core$async$to_chan(coll){
return cljs.core.async.to_chan_BANG_(coll);
});

/**
 * @interface
 */
cljs.core.async.Mux = function(){};

var cljs$core$async$Mux$muxch_STAR_$dyn_34684 = (function (_){
var x__5519__auto__ = (((_ == null))?null:_);
var m__5520__auto__ = (cljs.core.async.muxch_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5520__auto__.call(null,_));
} else {
var m__5518__auto__ = (cljs.core.async.muxch_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$1(_) : m__5518__auto__.call(null,_));
} else {
throw cljs.core.missing_protocol("Mux.muxch*",_);
}
}
});
cljs.core.async.muxch_STAR_ = (function cljs$core$async$muxch_STAR_(_){
if((((!((_ == null)))) && ((!((_.cljs$core$async$Mux$muxch_STAR_$arity$1 == null)))))){
return _.cljs$core$async$Mux$muxch_STAR_$arity$1(_);
} else {
return cljs$core$async$Mux$muxch_STAR_$dyn_34684(_);
}
});


/**
 * @interface
 */
cljs.core.async.Mult = function(){};

var cljs$core$async$Mult$tap_STAR_$dyn_34685 = (function (m,ch,close_QMARK_){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.tap_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$3(m,ch,close_QMARK_) : m__5520__auto__.call(null,m,ch,close_QMARK_));
} else {
var m__5518__auto__ = (cljs.core.async.tap_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$3(m,ch,close_QMARK_) : m__5518__auto__.call(null,m,ch,close_QMARK_));
} else {
throw cljs.core.missing_protocol("Mult.tap*",m);
}
}
});
cljs.core.async.tap_STAR_ = (function cljs$core$async$tap_STAR_(m,ch,close_QMARK_){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$tap_STAR_$arity$3 == null)))))){
return m.cljs$core$async$Mult$tap_STAR_$arity$3(m,ch,close_QMARK_);
} else {
return cljs$core$async$Mult$tap_STAR_$dyn_34685(m,ch,close_QMARK_);
}
});

var cljs$core$async$Mult$untap_STAR_$dyn_34691 = (function (m,ch){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.untap_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5520__auto__.call(null,m,ch));
} else {
var m__5518__auto__ = (cljs.core.async.untap_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5518__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mult.untap*",m);
}
}
});
cljs.core.async.untap_STAR_ = (function cljs$core$async$untap_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$untap_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mult$untap_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mult$untap_STAR_$dyn_34691(m,ch);
}
});

var cljs$core$async$Mult$untap_all_STAR_$dyn_34692 = (function (m){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.untap_all_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5520__auto__.call(null,m));
} else {
var m__5518__auto__ = (cljs.core.async.untap_all_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5518__auto__.call(null,m));
} else {
throw cljs.core.missing_protocol("Mult.untap-all*",m);
}
}
});
cljs.core.async.untap_all_STAR_ = (function cljs$core$async$untap_all_STAR_(m){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mult$untap_all_STAR_$arity$1 == null)))))){
return m.cljs$core$async$Mult$untap_all_STAR_$arity$1(m);
} else {
return cljs$core$async$Mult$untap_all_STAR_$dyn_34692(m);
}
});


/**
* @constructor
 * @implements {cljs.core.async.Mult}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async31805 = (function (ch,cs,meta31806){
this.ch = ch;
this.cs = cs;
this.meta31806 = meta31806;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_31807,meta31806__$1){
var self__ = this;
var _31807__$1 = this;
return (new cljs.core.async.t_cljs$core$async31805(self__.ch,self__.cs,meta31806__$1));
}));

(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_31807){
var self__ = this;
var _31807__$1 = this;
return self__.meta31806;
}));

(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
}));

(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$async$Mult$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$async$Mult$tap_STAR_$arity$3 = (function (_,ch__$1,close_QMARK_){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.cs,cljs.core.assoc,ch__$1,close_QMARK_);

return null;
}));

(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$async$Mult$untap_STAR_$arity$2 = (function (_,ch__$1){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.dissoc,ch__$1);

return null;
}));

(cljs.core.async.t_cljs$core$async31805.prototype.cljs$core$async$Mult$untap_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return null;
}));

(cljs.core.async.t_cljs$core$async31805.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"meta31806","meta31806",-191648267,null)], null);
}));

(cljs.core.async.t_cljs$core$async31805.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async31805.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async31805");

(cljs.core.async.t_cljs$core$async31805.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async31805");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async31805.
 */
cljs.core.async.__GT_t_cljs$core$async31805 = (function cljs$core$async$__GT_t_cljs$core$async31805(ch,cs,meta31806){
return (new cljs.core.async.t_cljs$core$async31805(ch,cs,meta31806));
});


/**
 * Creates and returns a mult(iple) of the supplied channel. Channels
 *   containing copies of the channel can be created with 'tap', and
 *   detached with 'untap'.
 * 
 *   Each item is distributed to all taps in parallel and synchronously,
 *   i.e. each tap must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow taps from holding up the mult.
 * 
 *   Items received when there are no taps get dropped.
 * 
 *   If a tap puts to a closed channel, it will be removed from the mult.
 */
cljs.core.async.mult = (function cljs$core$async$mult(ch){
var cs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var m = (new cljs.core.async.t_cljs$core$async31805(ch,cs,cljs.core.PersistentArrayMap.EMPTY));
var dchan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var dctr = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var done = (function (_){
if((cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(dchan,true);
} else {
return null;
}
});
var c__30379__auto___34697 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_32074){
var state_val_32075 = (state_32074[(1)]);
if((state_val_32075 === (7))){
var inst_32052 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32095_34698 = state_32074__$1;
(statearr_32095_34698[(2)] = inst_32052);

(statearr_32095_34698[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (20))){
var inst_31892 = (state_32074[(7)]);
var inst_31911 = cljs.core.first(inst_31892);
var inst_31916 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_31911,(0),null);
var inst_31917 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_31911,(1),null);
var state_32074__$1 = (function (){var statearr_32099 = state_32074;
(statearr_32099[(8)] = inst_31916);

return statearr_32099;
})();
if(cljs.core.truth_(inst_31917)){
var statearr_32102_34699 = state_32074__$1;
(statearr_32102_34699[(1)] = (22));

} else {
var statearr_32105_34700 = state_32074__$1;
(statearr_32105_34700[(1)] = (23));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (27))){
var inst_31963 = (state_32074[(9)]);
var inst_31965 = (state_32074[(10)]);
var inst_31974 = (state_32074[(11)]);
var inst_31847 = (state_32074[(12)]);
var inst_31974__$1 = cljs.core._nth(inst_31963,inst_31965);
var inst_31977 = cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3(inst_31974__$1,inst_31847,done);
var state_32074__$1 = (function (){var statearr_32113 = state_32074;
(statearr_32113[(11)] = inst_31974__$1);

return statearr_32113;
})();
if(cljs.core.truth_(inst_31977)){
var statearr_32116_34702 = state_32074__$1;
(statearr_32116_34702[(1)] = (30));

} else {
var statearr_32118_34703 = state_32074__$1;
(statearr_32118_34703[(1)] = (31));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (1))){
var state_32074__$1 = state_32074;
var statearr_32123_34705 = state_32074__$1;
(statearr_32123_34705[(2)] = null);

(statearr_32123_34705[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (24))){
var inst_31892 = (state_32074[(7)]);
var inst_31922 = (state_32074[(2)]);
var inst_31923 = cljs.core.next(inst_31892);
var inst_31859 = inst_31923;
var inst_31860 = null;
var inst_31862 = (0);
var inst_31864 = (0);
var state_32074__$1 = (function (){var statearr_32128 = state_32074;
(statearr_32128[(13)] = inst_31922);

(statearr_32128[(14)] = inst_31859);

(statearr_32128[(15)] = inst_31860);

(statearr_32128[(16)] = inst_31862);

(statearr_32128[(17)] = inst_31864);

return statearr_32128;
})();
var statearr_32131_34706 = state_32074__$1;
(statearr_32131_34706[(2)] = null);

(statearr_32131_34706[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (39))){
var state_32074__$1 = state_32074;
var statearr_32148_34708 = state_32074__$1;
(statearr_32148_34708[(2)] = null);

(statearr_32148_34708[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (4))){
var inst_31847 = (state_32074[(12)]);
var inst_31847__$1 = (state_32074[(2)]);
var inst_31849 = (inst_31847__$1 == null);
var state_32074__$1 = (function (){var statearr_32153 = state_32074;
(statearr_32153[(12)] = inst_31847__$1);

return statearr_32153;
})();
if(cljs.core.truth_(inst_31849)){
var statearr_32155_34709 = state_32074__$1;
(statearr_32155_34709[(1)] = (5));

} else {
var statearr_32157_34710 = state_32074__$1;
(statearr_32157_34710[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (15))){
var inst_31864 = (state_32074[(17)]);
var inst_31859 = (state_32074[(14)]);
var inst_31860 = (state_32074[(15)]);
var inst_31862 = (state_32074[(16)]);
var inst_31887 = (state_32074[(2)]);
var inst_31888 = (inst_31864 + (1));
var tmp32136 = inst_31862;
var tmp32137 = inst_31859;
var tmp32138 = inst_31860;
var inst_31859__$1 = tmp32137;
var inst_31860__$1 = tmp32138;
var inst_31862__$1 = tmp32136;
var inst_31864__$1 = inst_31888;
var state_32074__$1 = (function (){var statearr_32171 = state_32074;
(statearr_32171[(18)] = inst_31887);

(statearr_32171[(14)] = inst_31859__$1);

(statearr_32171[(15)] = inst_31860__$1);

(statearr_32171[(16)] = inst_31862__$1);

(statearr_32171[(17)] = inst_31864__$1);

return statearr_32171;
})();
var statearr_32172_34711 = state_32074__$1;
(statearr_32172_34711[(2)] = null);

(statearr_32172_34711[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (21))){
var inst_31926 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32186_34712 = state_32074__$1;
(statearr_32186_34712[(2)] = inst_31926);

(statearr_32186_34712[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (31))){
var inst_31974 = (state_32074[(11)]);
var inst_31984 = m.cljs$core$async$Mult$untap_STAR_$arity$2(null,inst_31974);
var state_32074__$1 = state_32074;
var statearr_32189_34713 = state_32074__$1;
(statearr_32189_34713[(2)] = inst_31984);

(statearr_32189_34713[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (32))){
var inst_31965 = (state_32074[(10)]);
var inst_31962 = (state_32074[(19)]);
var inst_31963 = (state_32074[(9)]);
var inst_31964 = (state_32074[(20)]);
var inst_31986 = (state_32074[(2)]);
var inst_31988 = (inst_31965 + (1));
var tmp32177 = inst_31963;
var tmp32178 = inst_31962;
var tmp32179 = inst_31964;
var inst_31962__$1 = tmp32178;
var inst_31963__$1 = tmp32177;
var inst_31964__$1 = tmp32179;
var inst_31965__$1 = inst_31988;
var state_32074__$1 = (function (){var statearr_32200 = state_32074;
(statearr_32200[(21)] = inst_31986);

(statearr_32200[(19)] = inst_31962__$1);

(statearr_32200[(9)] = inst_31963__$1);

(statearr_32200[(20)] = inst_31964__$1);

(statearr_32200[(10)] = inst_31965__$1);

return statearr_32200;
})();
var statearr_32203_34715 = state_32074__$1;
(statearr_32203_34715[(2)] = null);

(statearr_32203_34715[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (40))){
var inst_32010 = (state_32074[(22)]);
var inst_32017 = m.cljs$core$async$Mult$untap_STAR_$arity$2(null,inst_32010);
var state_32074__$1 = state_32074;
var statearr_32205_34716 = state_32074__$1;
(statearr_32205_34716[(2)] = inst_32017);

(statearr_32205_34716[(1)] = (41));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (33))){
var inst_31994 = (state_32074[(23)]);
var inst_31998 = cljs.core.chunked_seq_QMARK_(inst_31994);
var state_32074__$1 = state_32074;
if(inst_31998){
var statearr_32212_34717 = state_32074__$1;
(statearr_32212_34717[(1)] = (36));

} else {
var statearr_32213_34718 = state_32074__$1;
(statearr_32213_34718[(1)] = (37));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (13))){
var inst_31877 = (state_32074[(24)]);
var inst_31884 = cljs.core.async.close_BANG_(inst_31877);
var state_32074__$1 = state_32074;
var statearr_32215_34719 = state_32074__$1;
(statearr_32215_34719[(2)] = inst_31884);

(statearr_32215_34719[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (22))){
var inst_31916 = (state_32074[(8)]);
var inst_31919 = cljs.core.async.close_BANG_(inst_31916);
var state_32074__$1 = state_32074;
var statearr_32217_34720 = state_32074__$1;
(statearr_32217_34720[(2)] = inst_31919);

(statearr_32217_34720[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (36))){
var inst_31994 = (state_32074[(23)]);
var inst_32000 = cljs.core.chunk_first(inst_31994);
var inst_32005 = cljs.core.chunk_rest(inst_31994);
var inst_32006 = cljs.core.count(inst_32000);
var inst_31962 = inst_32005;
var inst_31963 = inst_32000;
var inst_31964 = inst_32006;
var inst_31965 = (0);
var state_32074__$1 = (function (){var statearr_32220 = state_32074;
(statearr_32220[(19)] = inst_31962);

(statearr_32220[(9)] = inst_31963);

(statearr_32220[(20)] = inst_31964);

(statearr_32220[(10)] = inst_31965);

return statearr_32220;
})();
var statearr_32227_34723 = state_32074__$1;
(statearr_32227_34723[(2)] = null);

(statearr_32227_34723[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (41))){
var inst_31994 = (state_32074[(23)]);
var inst_32019 = (state_32074[(2)]);
var inst_32020 = cljs.core.next(inst_31994);
var inst_31962 = inst_32020;
var inst_31963 = null;
var inst_31964 = (0);
var inst_31965 = (0);
var state_32074__$1 = (function (){var statearr_32235 = state_32074;
(statearr_32235[(25)] = inst_32019);

(statearr_32235[(19)] = inst_31962);

(statearr_32235[(9)] = inst_31963);

(statearr_32235[(20)] = inst_31964);

(statearr_32235[(10)] = inst_31965);

return statearr_32235;
})();
var statearr_32236_34726 = state_32074__$1;
(statearr_32236_34726[(2)] = null);

(statearr_32236_34726[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (43))){
var state_32074__$1 = state_32074;
var statearr_32239_34727 = state_32074__$1;
(statearr_32239_34727[(2)] = null);

(statearr_32239_34727[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (29))){
var inst_32032 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32244_34732 = state_32074__$1;
(statearr_32244_34732[(2)] = inst_32032);

(statearr_32244_34732[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (44))){
var inst_32046 = (state_32074[(2)]);
var state_32074__$1 = (function (){var statearr_32245 = state_32074;
(statearr_32245[(26)] = inst_32046);

return statearr_32245;
})();
var statearr_32246_34733 = state_32074__$1;
(statearr_32246_34733[(2)] = null);

(statearr_32246_34733[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (6))){
var inst_31943 = (state_32074[(27)]);
var inst_31941 = cljs.core.deref(cs);
var inst_31943__$1 = cljs.core.keys(inst_31941);
var inst_31949 = cljs.core.count(inst_31943__$1);
var inst_31950 = cljs.core.reset_BANG_(dctr,inst_31949);
var inst_31957 = cljs.core.seq(inst_31943__$1);
var inst_31962 = inst_31957;
var inst_31963 = null;
var inst_31964 = (0);
var inst_31965 = (0);
var state_32074__$1 = (function (){var statearr_32251 = state_32074;
(statearr_32251[(27)] = inst_31943__$1);

(statearr_32251[(28)] = inst_31950);

(statearr_32251[(19)] = inst_31962);

(statearr_32251[(9)] = inst_31963);

(statearr_32251[(20)] = inst_31964);

(statearr_32251[(10)] = inst_31965);

return statearr_32251;
})();
var statearr_32252_34742 = state_32074__$1;
(statearr_32252_34742[(2)] = null);

(statearr_32252_34742[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (28))){
var inst_31962 = (state_32074[(19)]);
var inst_31994 = (state_32074[(23)]);
var inst_31994__$1 = cljs.core.seq(inst_31962);
var state_32074__$1 = (function (){var statearr_32254 = state_32074;
(statearr_32254[(23)] = inst_31994__$1);

return statearr_32254;
})();
if(inst_31994__$1){
var statearr_32259_34746 = state_32074__$1;
(statearr_32259_34746[(1)] = (33));

} else {
var statearr_32260_34747 = state_32074__$1;
(statearr_32260_34747[(1)] = (34));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (25))){
var inst_31965 = (state_32074[(10)]);
var inst_31964 = (state_32074[(20)]);
var inst_31970 = (inst_31965 < inst_31964);
var inst_31971 = inst_31970;
var state_32074__$1 = state_32074;
if(cljs.core.truth_(inst_31971)){
var statearr_32265_34748 = state_32074__$1;
(statearr_32265_34748[(1)] = (27));

} else {
var statearr_32266_34749 = state_32074__$1;
(statearr_32266_34749[(1)] = (28));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (34))){
var state_32074__$1 = state_32074;
var statearr_32267_34750 = state_32074__$1;
(statearr_32267_34750[(2)] = null);

(statearr_32267_34750[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (17))){
var state_32074__$1 = state_32074;
var statearr_32272_34751 = state_32074__$1;
(statearr_32272_34751[(2)] = null);

(statearr_32272_34751[(1)] = (18));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (3))){
var inst_32054 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32074__$1,inst_32054);
} else {
if((state_val_32075 === (12))){
var inst_31931 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32277_34752 = state_32074__$1;
(statearr_32277_34752[(2)] = inst_31931);

(statearr_32277_34752[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (2))){
var state_32074__$1 = state_32074;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32074__$1,(4),ch);
} else {
if((state_val_32075 === (23))){
var state_32074__$1 = state_32074;
var statearr_32281_34758 = state_32074__$1;
(statearr_32281_34758[(2)] = null);

(statearr_32281_34758[(1)] = (24));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (35))){
var inst_32027 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32283_34760 = state_32074__$1;
(statearr_32283_34760[(2)] = inst_32027);

(statearr_32283_34760[(1)] = (29));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (19))){
var inst_31892 = (state_32074[(7)]);
var inst_31899 = cljs.core.chunk_first(inst_31892);
var inst_31900 = cljs.core.chunk_rest(inst_31892);
var inst_31901 = cljs.core.count(inst_31899);
var inst_31859 = inst_31900;
var inst_31860 = inst_31899;
var inst_31862 = inst_31901;
var inst_31864 = (0);
var state_32074__$1 = (function (){var statearr_32288 = state_32074;
(statearr_32288[(14)] = inst_31859);

(statearr_32288[(15)] = inst_31860);

(statearr_32288[(16)] = inst_31862);

(statearr_32288[(17)] = inst_31864);

return statearr_32288;
})();
var statearr_32290_34761 = state_32074__$1;
(statearr_32290_34761[(2)] = null);

(statearr_32290_34761[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (11))){
var inst_31859 = (state_32074[(14)]);
var inst_31892 = (state_32074[(7)]);
var inst_31892__$1 = cljs.core.seq(inst_31859);
var state_32074__$1 = (function (){var statearr_32292 = state_32074;
(statearr_32292[(7)] = inst_31892__$1);

return statearr_32292;
})();
if(inst_31892__$1){
var statearr_32295_34762 = state_32074__$1;
(statearr_32295_34762[(1)] = (16));

} else {
var statearr_32297_34763 = state_32074__$1;
(statearr_32297_34763[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (9))){
var inst_31933 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32298_34764 = state_32074__$1;
(statearr_32298_34764[(2)] = inst_31933);

(statearr_32298_34764[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (5))){
var inst_31857 = cljs.core.deref(cs);
var inst_31858 = cljs.core.seq(inst_31857);
var inst_31859 = inst_31858;
var inst_31860 = null;
var inst_31862 = (0);
var inst_31864 = (0);
var state_32074__$1 = (function (){var statearr_32300 = state_32074;
(statearr_32300[(14)] = inst_31859);

(statearr_32300[(15)] = inst_31860);

(statearr_32300[(16)] = inst_31862);

(statearr_32300[(17)] = inst_31864);

return statearr_32300;
})();
var statearr_32303_34769 = state_32074__$1;
(statearr_32303_34769[(2)] = null);

(statearr_32303_34769[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (14))){
var state_32074__$1 = state_32074;
var statearr_32306_34770 = state_32074__$1;
(statearr_32306_34770[(2)] = null);

(statearr_32306_34770[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (45))){
var inst_32043 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32308_34771 = state_32074__$1;
(statearr_32308_34771[(2)] = inst_32043);

(statearr_32308_34771[(1)] = (44));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (26))){
var inst_31943 = (state_32074[(27)]);
var inst_32035 = (state_32074[(2)]);
var inst_32036 = cljs.core.seq(inst_31943);
var state_32074__$1 = (function (){var statearr_32313 = state_32074;
(statearr_32313[(29)] = inst_32035);

return statearr_32313;
})();
if(inst_32036){
var statearr_32316_34772 = state_32074__$1;
(statearr_32316_34772[(1)] = (42));

} else {
var statearr_32317_34773 = state_32074__$1;
(statearr_32317_34773[(1)] = (43));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (16))){
var inst_31892 = (state_32074[(7)]);
var inst_31897 = cljs.core.chunked_seq_QMARK_(inst_31892);
var state_32074__$1 = state_32074;
if(inst_31897){
var statearr_32319_34774 = state_32074__$1;
(statearr_32319_34774[(1)] = (19));

} else {
var statearr_32320_34775 = state_32074__$1;
(statearr_32320_34775[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (38))){
var inst_32024 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32321_34776 = state_32074__$1;
(statearr_32321_34776[(2)] = inst_32024);

(statearr_32321_34776[(1)] = (35));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (30))){
var state_32074__$1 = state_32074;
var statearr_32324_34777 = state_32074__$1;
(statearr_32324_34777[(2)] = null);

(statearr_32324_34777[(1)] = (32));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (10))){
var inst_31860 = (state_32074[(15)]);
var inst_31864 = (state_32074[(17)]);
var inst_31876 = cljs.core._nth(inst_31860,inst_31864);
var inst_31877 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_31876,(0),null);
var inst_31878 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_31876,(1),null);
var state_32074__$1 = (function (){var statearr_32327 = state_32074;
(statearr_32327[(24)] = inst_31877);

return statearr_32327;
})();
if(cljs.core.truth_(inst_31878)){
var statearr_32328_34779 = state_32074__$1;
(statearr_32328_34779[(1)] = (13));

} else {
var statearr_32329_34780 = state_32074__$1;
(statearr_32329_34780[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (18))){
var inst_31929 = (state_32074[(2)]);
var state_32074__$1 = state_32074;
var statearr_32331_34781 = state_32074__$1;
(statearr_32331_34781[(2)] = inst_31929);

(statearr_32331_34781[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (42))){
var state_32074__$1 = state_32074;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32074__$1,(45),dchan);
} else {
if((state_val_32075 === (37))){
var inst_31994 = (state_32074[(23)]);
var inst_32010 = (state_32074[(22)]);
var inst_31847 = (state_32074[(12)]);
var inst_32010__$1 = cljs.core.first(inst_31994);
var inst_32012 = cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$3(inst_32010__$1,inst_31847,done);
var state_32074__$1 = (function (){var statearr_32335 = state_32074;
(statearr_32335[(22)] = inst_32010__$1);

return statearr_32335;
})();
if(cljs.core.truth_(inst_32012)){
var statearr_32336_34783 = state_32074__$1;
(statearr_32336_34783[(1)] = (39));

} else {
var statearr_32337_34785 = state_32074__$1;
(statearr_32337_34785[(1)] = (40));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32075 === (8))){
var inst_31864 = (state_32074[(17)]);
var inst_31862 = (state_32074[(16)]);
var inst_31868 = (inst_31864 < inst_31862);
var inst_31869 = inst_31868;
var state_32074__$1 = state_32074;
if(cljs.core.truth_(inst_31869)){
var statearr_32339_34787 = state_32074__$1;
(statearr_32339_34787[(1)] = (10));

} else {
var statearr_32341_34788 = state_32074__$1;
(statearr_32341_34788[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$mult_$_state_machine__29645__auto__ = null;
var cljs$core$async$mult_$_state_machine__29645__auto____0 = (function (){
var statearr_32347 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32347[(0)] = cljs$core$async$mult_$_state_machine__29645__auto__);

(statearr_32347[(1)] = (1));

return statearr_32347;
});
var cljs$core$async$mult_$_state_machine__29645__auto____1 = (function (state_32074){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_32074);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e32349){var ex__29648__auto__ = e32349;
var statearr_32350_34791 = state_32074;
(statearr_32350_34791[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_32074[(4)]))){
var statearr_32351_34792 = state_32074;
(statearr_32351_34792[(1)] = cljs.core.first((state_32074[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34793 = state_32074;
state_32074 = G__34793;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$mult_$_state_machine__29645__auto__ = function(state_32074){
switch(arguments.length){
case 0:
return cljs$core$async$mult_$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$mult_$_state_machine__29645__auto____1.call(this,state_32074);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mult_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mult_$_state_machine__29645__auto____0;
cljs$core$async$mult_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mult_$_state_machine__29645__auto____1;
return cljs$core$async$mult_$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_32355 = f__30380__auto__();
(statearr_32355[(6)] = c__30379__auto___34697);

return statearr_32355;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return m;
});
/**
 * Copies the mult source onto the supplied channel.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.tap = (function cljs$core$async$tap(var_args){
var G__32360 = arguments.length;
switch (G__32360) {
case 2:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.tap.cljs$core$IFn$_invoke$arity$2 = (function (mult,ch){
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3(mult,ch,true);
}));

(cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3 = (function (mult,ch,close_QMARK_){
cljs.core.async.tap_STAR_(mult,ch,close_QMARK_);

return ch;
}));

(cljs.core.async.tap.cljs$lang$maxFixedArity = 3);

/**
 * Disconnects a target channel from a mult
 */
cljs.core.async.untap = (function cljs$core$async$untap(mult,ch){
return cljs.core.async.untap_STAR_(mult,ch);
});
/**
 * Disconnects all target channels from a mult
 */
cljs.core.async.untap_all = (function cljs$core$async$untap_all(mult){
return cljs.core.async.untap_all_STAR_(mult);
});

/**
 * @interface
 */
cljs.core.async.Mix = function(){};

var cljs$core$async$Mix$admix_STAR_$dyn_34798 = (function (m,ch){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.admix_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5520__auto__.call(null,m,ch));
} else {
var m__5518__auto__ = (cljs.core.async.admix_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5518__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mix.admix*",m);
}
}
});
cljs.core.async.admix_STAR_ = (function cljs$core$async$admix_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$admix_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$admix_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mix$admix_STAR_$dyn_34798(m,ch);
}
});

var cljs$core$async$Mix$unmix_STAR_$dyn_34802 = (function (m,ch){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.unmix_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5520__auto__.call(null,m,ch));
} else {
var m__5518__auto__ = (cljs.core.async.unmix_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$2(m,ch) : m__5518__auto__.call(null,m,ch));
} else {
throw cljs.core.missing_protocol("Mix.unmix*",m);
}
}
});
cljs.core.async.unmix_STAR_ = (function cljs$core$async$unmix_STAR_(m,ch){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$unmix_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$unmix_STAR_$arity$2(m,ch);
} else {
return cljs$core$async$Mix$unmix_STAR_$dyn_34802(m,ch);
}
});

var cljs$core$async$Mix$unmix_all_STAR_$dyn_34803 = (function (m){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.unmix_all_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5520__auto__.call(null,m));
} else {
var m__5518__auto__ = (cljs.core.async.unmix_all_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$1(m) : m__5518__auto__.call(null,m));
} else {
throw cljs.core.missing_protocol("Mix.unmix-all*",m);
}
}
});
cljs.core.async.unmix_all_STAR_ = (function cljs$core$async$unmix_all_STAR_(m){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$unmix_all_STAR_$arity$1 == null)))))){
return m.cljs$core$async$Mix$unmix_all_STAR_$arity$1(m);
} else {
return cljs$core$async$Mix$unmix_all_STAR_$dyn_34803(m);
}
});

var cljs$core$async$Mix$toggle_STAR_$dyn_34808 = (function (m,state_map){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.toggle_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$2(m,state_map) : m__5520__auto__.call(null,m,state_map));
} else {
var m__5518__auto__ = (cljs.core.async.toggle_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$2(m,state_map) : m__5518__auto__.call(null,m,state_map));
} else {
throw cljs.core.missing_protocol("Mix.toggle*",m);
}
}
});
cljs.core.async.toggle_STAR_ = (function cljs$core$async$toggle_STAR_(m,state_map){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$toggle_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$toggle_STAR_$arity$2(m,state_map);
} else {
return cljs$core$async$Mix$toggle_STAR_$dyn_34808(m,state_map);
}
});

var cljs$core$async$Mix$solo_mode_STAR_$dyn_34812 = (function (m,mode){
var x__5519__auto__ = (((m == null))?null:m);
var m__5520__auto__ = (cljs.core.async.solo_mode_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$2(m,mode) : m__5520__auto__.call(null,m,mode));
} else {
var m__5518__auto__ = (cljs.core.async.solo_mode_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$2(m,mode) : m__5518__auto__.call(null,m,mode));
} else {
throw cljs.core.missing_protocol("Mix.solo-mode*",m);
}
}
});
cljs.core.async.solo_mode_STAR_ = (function cljs$core$async$solo_mode_STAR_(m,mode){
if((((!((m == null)))) && ((!((m.cljs$core$async$Mix$solo_mode_STAR_$arity$2 == null)))))){
return m.cljs$core$async$Mix$solo_mode_STAR_$arity$2(m,mode);
} else {
return cljs$core$async$Mix$solo_mode_STAR_$dyn_34812(m,mode);
}
});

cljs.core.async.ioc_alts_BANG_ = (function cljs$core$async$ioc_alts_BANG_(var_args){
var args__5903__auto__ = [];
var len__5897__auto___34816 = arguments.length;
var i__5898__auto___34817 = (0);
while(true){
if((i__5898__auto___34817 < len__5897__auto___34816)){
args__5903__auto__.push((arguments[i__5898__auto___34817]));

var G__34818 = (i__5898__auto___34817 + (1));
i__5898__auto___34817 = G__34818;
continue;
} else {
}
break;
}

var argseq__5904__auto__ = ((((3) < args__5903__auto__.length))?(new cljs.core.IndexedSeq(args__5903__auto__.slice((3)),(0),null)):null);
return cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),argseq__5904__auto__);
});

(cljs.core.async.ioc_alts_BANG_.cljs$core$IFn$_invoke$arity$variadic = (function (state,cont_block,ports,p__32421){
var map__32422 = p__32421;
var map__32422__$1 = cljs.core.__destructure_map(map__32422);
var opts = map__32422__$1;
var statearr_32423_34819 = state;
(statearr_32423_34819[(1)] = cont_block);


var temp__5823__auto__ = cljs.core.async.do_alts((function (val){
var statearr_32429_34820 = state;
(statearr_32429_34820[(2)] = val);


return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state);
}),ports,opts);
if(cljs.core.truth_(temp__5823__auto__)){
var cb = temp__5823__auto__;
var statearr_32433_34821 = state;
(statearr_32433_34821[(2)] = cljs.core.deref(cb));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}));

(cljs.core.async.ioc_alts_BANG_.cljs$lang$maxFixedArity = (3));

/** @this {Function} */
(cljs.core.async.ioc_alts_BANG_.cljs$lang$applyTo = (function (seq32414){
var G__32415 = cljs.core.first(seq32414);
var seq32414__$1 = cljs.core.next(seq32414);
var G__32416 = cljs.core.first(seq32414__$1);
var seq32414__$2 = cljs.core.next(seq32414__$1);
var G__32417 = cljs.core.first(seq32414__$2);
var seq32414__$3 = cljs.core.next(seq32414__$2);
var self__5882__auto__ = this;
return self__5882__auto__.cljs$core$IFn$_invoke$arity$variadic(G__32415,G__32416,G__32417,seq32414__$3);
}));


/**
* @constructor
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mix}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async32459 = (function (change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta32460){
this.change = change;
this.solo_mode = solo_mode;
this.pick = pick;
this.cs = cs;
this.calc_state = calc_state;
this.out = out;
this.changed = changed;
this.solo_modes = solo_modes;
this.attrs = attrs;
this.meta32460 = meta32460;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_32461,meta32460__$1){
var self__ = this;
var _32461__$1 = this;
return (new cljs.core.async.t_cljs$core$async32459(self__.change,self__.solo_mode,self__.pick,self__.cs,self__.calc_state,self__.out,self__.changed,self__.solo_modes,self__.attrs,meta32460__$1));
}));

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_32461){
var self__ = this;
var _32461__$1 = this;
return self__.meta32460;
}));

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.out;
}));

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mix$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mix$admix_STAR_$arity$2 = (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(self__.cs,cljs.core.assoc,ch,cljs.core.PersistentArrayMap.EMPTY);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mix$unmix_STAR_$arity$2 = (function (_,ch){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.dissoc,ch);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mix$unmix_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
cljs.core.reset_BANG_(self__.cs,cljs.core.PersistentArrayMap.EMPTY);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mix$toggle_STAR_$arity$2 = (function (_,state_map){
var self__ = this;
var ___$1 = this;
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.cs,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(cljs.core.merge_with,cljs.core.merge),state_map);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32459.prototype.cljs$core$async$Mix$solo_mode_STAR_$arity$2 = (function (_,mode){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_((self__.solo_modes.cljs$core$IFn$_invoke$arity$1 ? self__.solo_modes.cljs$core$IFn$_invoke$arity$1(mode) : self__.solo_modes.call(null,mode)))){
} else {
throw (new Error((""+"Assert failed: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1((""+"mode must be one of: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(self__.solo_modes)))+"\n"+"(solo-modes mode)")));
}

cljs.core.reset_BANG_(self__.solo_mode,mode);

return (self__.changed.cljs$core$IFn$_invoke$arity$0 ? self__.changed.cljs$core$IFn$_invoke$arity$0() : self__.changed.call(null));
}));

(cljs.core.async.t_cljs$core$async32459.getBasis = (function (){
return new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"change","change",477485025,null),new cljs.core.Symbol(null,"solo-mode","solo-mode",2031788074,null),new cljs.core.Symbol(null,"pick","pick",1300068175,null),new cljs.core.Symbol(null,"cs","cs",-117024463,null),new cljs.core.Symbol(null,"calc-state","calc-state",-349968968,null),new cljs.core.Symbol(null,"out","out",729986010,null),new cljs.core.Symbol(null,"changed","changed",-2083710852,null),new cljs.core.Symbol(null,"solo-modes","solo-modes",882180540,null),new cljs.core.Symbol(null,"attrs","attrs",-450137186,null),new cljs.core.Symbol(null,"meta32460","meta32460",-1002098745,null)], null);
}));

(cljs.core.async.t_cljs$core$async32459.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async32459.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async32459");

(cljs.core.async.t_cljs$core$async32459.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async32459");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async32459.
 */
cljs.core.async.__GT_t_cljs$core$async32459 = (function cljs$core$async$__GT_t_cljs$core$async32459(change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta32460){
return (new cljs.core.async.t_cljs$core$async32459(change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,meta32460));
});


/**
 * Creates and returns a mix of one or more input channels which will
 *   be put on the supplied out channel. Input sources can be added to
 *   the mix with 'admix', and removed with 'unmix'. A mix supports
 *   soloing, muting and pausing multiple inputs atomically using
 *   'toggle', and can solo using either muting or pausing as determined
 *   by 'solo-mode'.
 * 
 *   Each channel can have zero or more boolean modes set via 'toggle':
 * 
 *   :solo - when true, only this (ond other soloed) channel(s) will appear
 *        in the mix output channel. :mute and :pause states of soloed
 *        channels are ignored. If solo-mode is :mute, non-soloed
 *        channels are muted, if :pause, non-soloed channels are
 *        paused.
 * 
 *   :mute - muted channels will have their contents consumed but not included in the mix
 *   :pause - paused channels will not have their contents consumed (and thus also not included in the mix)
 */
cljs.core.async.mix = (function cljs$core$async$mix(out){
var cs = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var solo_modes = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"pause","pause",-2095325672),null,new cljs.core.Keyword(null,"mute","mute",1151223646),null], null), null);
var attrs = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(solo_modes,new cljs.core.Keyword(null,"solo","solo",-316350075));
var solo_mode = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"mute","mute",1151223646));
var change = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(cljs.core.async.sliding_buffer((1)));
var changed = (function (){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(change,true);
});
var pick = (function (attr,chs){
return cljs.core.reduce_kv((function (ret,c,v){
if(cljs.core.truth_((attr.cljs$core$IFn$_invoke$arity$1 ? attr.cljs$core$IFn$_invoke$arity$1(v) : attr.call(null,v)))){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ret,c);
} else {
return ret;
}
}),cljs.core.PersistentHashSet.EMPTY,chs);
});
var calc_state = (function (){
var chs = cljs.core.deref(cs);
var mode = cljs.core.deref(solo_mode);
var solos = pick(new cljs.core.Keyword(null,"solo","solo",-316350075),chs);
var pauses = pick(new cljs.core.Keyword(null,"pause","pause",-2095325672),chs);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"solos","solos",1441458643),solos,new cljs.core.Keyword(null,"mutes","mutes",1068806309),pick(new cljs.core.Keyword(null,"mute","mute",1151223646),chs),new cljs.core.Keyword(null,"reads","reads",-1215067361),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(mode,new cljs.core.Keyword(null,"pause","pause",-2095325672))) && (cljs.core.seq(solos))))?cljs.core.vec(solos):cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2(pauses,cljs.core.keys(chs)))),change)], null);
});
var m = (new cljs.core.async.t_cljs$core$async32459(change,solo_mode,pick,cs,calc_state,out,changed,solo_modes,attrs,cljs.core.PersistentArrayMap.EMPTY));
var c__30379__auto___34844 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_32591){
var state_val_32592 = (state_32591[(1)]);
if((state_val_32592 === (7))){
var inst_32542 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
if(cljs.core.truth_(inst_32542)){
var statearr_32601_34845 = state_32591__$1;
(statearr_32601_34845[(1)] = (8));

} else {
var statearr_32603_34846 = state_32591__$1;
(statearr_32603_34846[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (20))){
var inst_32528 = (state_32591[(7)]);
var state_32591__$1 = state_32591;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_32591__$1,(23),out,inst_32528);
} else {
if((state_val_32592 === (1))){
var inst_32508 = calc_state();
var inst_32509 = cljs.core.__destructure_map(inst_32508);
var inst_32511 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32509,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_32512 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32509,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_32513 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32509,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var inst_32514 = inst_32508;
var state_32591__$1 = (function (){var statearr_32604 = state_32591;
(statearr_32604[(8)] = inst_32511);

(statearr_32604[(9)] = inst_32512);

(statearr_32604[(10)] = inst_32513);

(statearr_32604[(11)] = inst_32514);

return statearr_32604;
})();
var statearr_32608_34847 = state_32591__$1;
(statearr_32608_34847[(2)] = null);

(statearr_32608_34847[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (24))){
var inst_32517 = (state_32591[(12)]);
var inst_32514 = inst_32517;
var state_32591__$1 = (function (){var statearr_32610 = state_32591;
(statearr_32610[(11)] = inst_32514);

return statearr_32610;
})();
var statearr_32611_34849 = state_32591__$1;
(statearr_32611_34849[(2)] = null);

(statearr_32611_34849[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (4))){
var inst_32528 = (state_32591[(7)]);
var inst_32535 = (state_32591[(13)]);
var inst_32526 = (state_32591[(2)]);
var inst_32528__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32526,(0),null);
var inst_32529 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_32526,(1),null);
var inst_32535__$1 = (inst_32528__$1 == null);
var state_32591__$1 = (function (){var statearr_32618 = state_32591;
(statearr_32618[(7)] = inst_32528__$1);

(statearr_32618[(14)] = inst_32529);

(statearr_32618[(13)] = inst_32535__$1);

return statearr_32618;
})();
if(cljs.core.truth_(inst_32535__$1)){
var statearr_32620_34856 = state_32591__$1;
(statearr_32620_34856[(1)] = (5));

} else {
var statearr_32622_34857 = state_32591__$1;
(statearr_32622_34857[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (15))){
var inst_32518 = (state_32591[(15)]);
var inst_32561 = (state_32591[(16)]);
var inst_32561__$1 = cljs.core.empty_QMARK_(inst_32518);
var state_32591__$1 = (function (){var statearr_32623 = state_32591;
(statearr_32623[(16)] = inst_32561__$1);

return statearr_32623;
})();
if(inst_32561__$1){
var statearr_32624_34858 = state_32591__$1;
(statearr_32624_34858[(1)] = (17));

} else {
var statearr_32625_34859 = state_32591__$1;
(statearr_32625_34859[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (21))){
var inst_32517 = (state_32591[(12)]);
var inst_32514 = inst_32517;
var state_32591__$1 = (function (){var statearr_32627 = state_32591;
(statearr_32627[(11)] = inst_32514);

return statearr_32627;
})();
var statearr_32628_34860 = state_32591__$1;
(statearr_32628_34860[(2)] = null);

(statearr_32628_34860[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (13))){
var inst_32550 = (state_32591[(2)]);
var inst_32551 = calc_state();
var inst_32514 = inst_32551;
var state_32591__$1 = (function (){var statearr_32633 = state_32591;
(statearr_32633[(17)] = inst_32550);

(statearr_32633[(11)] = inst_32514);

return statearr_32633;
})();
var statearr_32634_34861 = state_32591__$1;
(statearr_32634_34861[(2)] = null);

(statearr_32634_34861[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (22))){
var inst_32583 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
var statearr_32635_34862 = state_32591__$1;
(statearr_32635_34862[(2)] = inst_32583);

(statearr_32635_34862[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (6))){
var inst_32529 = (state_32591[(14)]);
var inst_32540 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_32529,change);
var state_32591__$1 = state_32591;
var statearr_32637_34865 = state_32591__$1;
(statearr_32637_34865[(2)] = inst_32540);

(statearr_32637_34865[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (25))){
var state_32591__$1 = state_32591;
var statearr_32639_34866 = state_32591__$1;
(statearr_32639_34866[(2)] = null);

(statearr_32639_34866[(1)] = (26));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (17))){
var inst_32520 = (state_32591[(18)]);
var inst_32529 = (state_32591[(14)]);
var inst_32563 = (inst_32520.cljs$core$IFn$_invoke$arity$1 ? inst_32520.cljs$core$IFn$_invoke$arity$1(inst_32529) : inst_32520.call(null,inst_32529));
var inst_32564 = cljs.core.not(inst_32563);
var state_32591__$1 = state_32591;
var statearr_32640_34870 = state_32591__$1;
(statearr_32640_34870[(2)] = inst_32564);

(statearr_32640_34870[(1)] = (19));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (3))){
var inst_32587 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32591__$1,inst_32587);
} else {
if((state_val_32592 === (12))){
var state_32591__$1 = state_32591;
var statearr_32648_34872 = state_32591__$1;
(statearr_32648_34872[(2)] = null);

(statearr_32648_34872[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (2))){
var inst_32514 = (state_32591[(11)]);
var inst_32517 = (state_32591[(12)]);
var inst_32517__$1 = cljs.core.__destructure_map(inst_32514);
var inst_32518 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32517__$1,new cljs.core.Keyword(null,"solos","solos",1441458643));
var inst_32520 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32517__$1,new cljs.core.Keyword(null,"mutes","mutes",1068806309));
var inst_32521 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32517__$1,new cljs.core.Keyword(null,"reads","reads",-1215067361));
var state_32591__$1 = (function (){var statearr_32657 = state_32591;
(statearr_32657[(12)] = inst_32517__$1);

(statearr_32657[(15)] = inst_32518);

(statearr_32657[(18)] = inst_32520);

return statearr_32657;
})();
return cljs.core.async.ioc_alts_BANG_(state_32591__$1,(4),inst_32521);
} else {
if((state_val_32592 === (23))){
var inst_32572 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
if(cljs.core.truth_(inst_32572)){
var statearr_32661_34873 = state_32591__$1;
(statearr_32661_34873[(1)] = (24));

} else {
var statearr_32662_34874 = state_32591__$1;
(statearr_32662_34874[(1)] = (25));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (19))){
var inst_32567 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
var statearr_32664_34877 = state_32591__$1;
(statearr_32664_34877[(2)] = inst_32567);

(statearr_32664_34877[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (11))){
var inst_32529 = (state_32591[(14)]);
var inst_32547 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(cs,cljs.core.dissoc,inst_32529);
var state_32591__$1 = state_32591;
var statearr_32668_34880 = state_32591__$1;
(statearr_32668_34880[(2)] = inst_32547);

(statearr_32668_34880[(1)] = (13));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (9))){
var inst_32518 = (state_32591[(15)]);
var inst_32529 = (state_32591[(14)]);
var inst_32555 = (state_32591[(19)]);
var inst_32555__$1 = (inst_32518.cljs$core$IFn$_invoke$arity$1 ? inst_32518.cljs$core$IFn$_invoke$arity$1(inst_32529) : inst_32518.call(null,inst_32529));
var state_32591__$1 = (function (){var statearr_32674 = state_32591;
(statearr_32674[(19)] = inst_32555__$1);

return statearr_32674;
})();
if(cljs.core.truth_(inst_32555__$1)){
var statearr_32675_34884 = state_32591__$1;
(statearr_32675_34884[(1)] = (14));

} else {
var statearr_32679_34885 = state_32591__$1;
(statearr_32679_34885[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (5))){
var inst_32535 = (state_32591[(13)]);
var state_32591__$1 = state_32591;
var statearr_32681_34887 = state_32591__$1;
(statearr_32681_34887[(2)] = inst_32535);

(statearr_32681_34887[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (14))){
var inst_32555 = (state_32591[(19)]);
var state_32591__$1 = state_32591;
var statearr_32682_34888 = state_32591__$1;
(statearr_32682_34888[(2)] = inst_32555);

(statearr_32682_34888[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (26))){
var inst_32577 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
var statearr_32683_34889 = state_32591__$1;
(statearr_32683_34889[(2)] = inst_32577);

(statearr_32683_34889[(1)] = (22));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (16))){
var inst_32569 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
if(cljs.core.truth_(inst_32569)){
var statearr_32684_34891 = state_32591__$1;
(statearr_32684_34891[(1)] = (20));

} else {
var statearr_32685_34892 = state_32591__$1;
(statearr_32685_34892[(1)] = (21));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (10))){
var inst_32585 = (state_32591[(2)]);
var state_32591__$1 = state_32591;
var statearr_32686_34893 = state_32591__$1;
(statearr_32686_34893[(2)] = inst_32585);

(statearr_32686_34893[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (18))){
var inst_32561 = (state_32591[(16)]);
var state_32591__$1 = state_32591;
var statearr_32689_34895 = state_32591__$1;
(statearr_32689_34895[(2)] = inst_32561);

(statearr_32689_34895[(1)] = (19));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32592 === (8))){
var inst_32528 = (state_32591[(7)]);
var inst_32544 = (inst_32528 == null);
var state_32591__$1 = state_32591;
if(cljs.core.truth_(inst_32544)){
var statearr_32690_34896 = state_32591__$1;
(statearr_32690_34896[(1)] = (11));

} else {
var statearr_32691_34897 = state_32591__$1;
(statearr_32691_34897[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$mix_$_state_machine__29645__auto__ = null;
var cljs$core$async$mix_$_state_machine__29645__auto____0 = (function (){
var statearr_32696 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32696[(0)] = cljs$core$async$mix_$_state_machine__29645__auto__);

(statearr_32696[(1)] = (1));

return statearr_32696;
});
var cljs$core$async$mix_$_state_machine__29645__auto____1 = (function (state_32591){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_32591);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e32699){var ex__29648__auto__ = e32699;
var statearr_32700_34908 = state_32591;
(statearr_32700_34908[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_32591[(4)]))){
var statearr_32701_34909 = state_32591;
(statearr_32701_34909[(1)] = cljs.core.first((state_32591[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34910 = state_32591;
state_32591 = G__34910;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$mix_$_state_machine__29645__auto__ = function(state_32591){
switch(arguments.length){
case 0:
return cljs$core$async$mix_$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$mix_$_state_machine__29645__auto____1.call(this,state_32591);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mix_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mix_$_state_machine__29645__auto____0;
cljs$core$async$mix_$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mix_$_state_machine__29645__auto____1;
return cljs$core$async$mix_$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_32705 = f__30380__auto__();
(statearr_32705[(6)] = c__30379__auto___34844);

return statearr_32705;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return m;
});
/**
 * Adds ch as an input to the mix
 */
cljs.core.async.admix = (function cljs$core$async$admix(mix,ch){
return cljs.core.async.admix_STAR_(mix,ch);
});
/**
 * Removes ch as an input to the mix
 */
cljs.core.async.unmix = (function cljs$core$async$unmix(mix,ch){
return cljs.core.async.unmix_STAR_(mix,ch);
});
/**
 * removes all inputs from the mix
 */
cljs.core.async.unmix_all = (function cljs$core$async$unmix_all(mix){
return cljs.core.async.unmix_all_STAR_(mix);
});
/**
 * Atomically sets the state(s) of one or more channels in a mix. The
 *   state map is a map of channels -> channel-state-map. A
 *   channel-state-map is a map of attrs -> boolean, where attr is one or
 *   more of :mute, :pause or :solo. Any states supplied are merged with
 *   the current state.
 * 
 *   Note that channels can be added to a mix via toggle, which can be
 *   used to add channels in a particular (e.g. paused) state.
 */
cljs.core.async.toggle = (function cljs$core$async$toggle(mix,state_map){
return cljs.core.async.toggle_STAR_(mix,state_map);
});
/**
 * Sets the solo mode of the mix. mode must be one of :mute or :pause
 */
cljs.core.async.solo_mode = (function cljs$core$async$solo_mode(mix,mode){
return cljs.core.async.solo_mode_STAR_(mix,mode);
});

/**
 * @interface
 */
cljs.core.async.Pub = function(){};

var cljs$core$async$Pub$sub_STAR_$dyn_34919 = (function (p,v,ch,close_QMARK_){
var x__5519__auto__ = (((p == null))?null:p);
var m__5520__auto__ = (cljs.core.async.sub_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$4 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$4(p,v,ch,close_QMARK_) : m__5520__auto__.call(null,p,v,ch,close_QMARK_));
} else {
var m__5518__auto__ = (cljs.core.async.sub_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$4 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$4(p,v,ch,close_QMARK_) : m__5518__auto__.call(null,p,v,ch,close_QMARK_));
} else {
throw cljs.core.missing_protocol("Pub.sub*",p);
}
}
});
cljs.core.async.sub_STAR_ = (function cljs$core$async$sub_STAR_(p,v,ch,close_QMARK_){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$sub_STAR_$arity$4 == null)))))){
return p.cljs$core$async$Pub$sub_STAR_$arity$4(p,v,ch,close_QMARK_);
} else {
return cljs$core$async$Pub$sub_STAR_$dyn_34919(p,v,ch,close_QMARK_);
}
});

var cljs$core$async$Pub$unsub_STAR_$dyn_34929 = (function (p,v,ch){
var x__5519__auto__ = (((p == null))?null:p);
var m__5520__auto__ = (cljs.core.async.unsub_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$3(p,v,ch) : m__5520__auto__.call(null,p,v,ch));
} else {
var m__5518__auto__ = (cljs.core.async.unsub_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$3 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$3(p,v,ch) : m__5518__auto__.call(null,p,v,ch));
} else {
throw cljs.core.missing_protocol("Pub.unsub*",p);
}
}
});
cljs.core.async.unsub_STAR_ = (function cljs$core$async$unsub_STAR_(p,v,ch){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_STAR_$arity$3 == null)))))){
return p.cljs$core$async$Pub$unsub_STAR_$arity$3(p,v,ch);
} else {
return cljs$core$async$Pub$unsub_STAR_$dyn_34929(p,v,ch);
}
});

var cljs$core$async$Pub$unsub_all_STAR_$dyn_34931 = (function() {
var G__34932 = null;
var G__34932__1 = (function (p){
var x__5519__auto__ = (((p == null))?null:p);
var m__5520__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$1(p) : m__5520__auto__.call(null,p));
} else {
var m__5518__auto__ = (cljs.core.async.unsub_all_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$1 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$1(p) : m__5518__auto__.call(null,p));
} else {
throw cljs.core.missing_protocol("Pub.unsub-all*",p);
}
}
});
var G__34932__2 = (function (p,v){
var x__5519__auto__ = (((p == null))?null:p);
var m__5520__auto__ = (cljs.core.async.unsub_all_STAR_[goog.typeOf(x__5519__auto__)]);
if((!((m__5520__auto__ == null)))){
return (m__5520__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5520__auto__.cljs$core$IFn$_invoke$arity$2(p,v) : m__5520__auto__.call(null,p,v));
} else {
var m__5518__auto__ = (cljs.core.async.unsub_all_STAR_["_"]);
if((!((m__5518__auto__ == null)))){
return (m__5518__auto__.cljs$core$IFn$_invoke$arity$2 ? m__5518__auto__.cljs$core$IFn$_invoke$arity$2(p,v) : m__5518__auto__.call(null,p,v));
} else {
throw cljs.core.missing_protocol("Pub.unsub-all*",p);
}
}
});
G__34932 = function(p,v){
switch(arguments.length){
case 1:
return G__34932__1.call(this,p);
case 2:
return G__34932__2.call(this,p,v);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
G__34932.cljs$core$IFn$_invoke$arity$1 = G__34932__1;
G__34932.cljs$core$IFn$_invoke$arity$2 = G__34932__2;
return G__34932;
})()
;
cljs.core.async.unsub_all_STAR_ = (function cljs$core$async$unsub_all_STAR_(var_args){
var G__32741 = arguments.length;
switch (G__32741) {
case 1:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$1 = (function (p){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$1 == null)))))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$1(p);
} else {
return cljs$core$async$Pub$unsub_all_STAR_$dyn_34931(p);
}
}));

(cljs.core.async.unsub_all_STAR_.cljs$core$IFn$_invoke$arity$2 = (function (p,v){
if((((!((p == null)))) && ((!((p.cljs$core$async$Pub$unsub_all_STAR_$arity$2 == null)))))){
return p.cljs$core$async$Pub$unsub_all_STAR_$arity$2(p,v);
} else {
return cljs$core$async$Pub$unsub_all_STAR_$dyn_34931(p,v);
}
}));

(cljs.core.async.unsub_all_STAR_.cljs$lang$maxFixedArity = 2);



/**
* @constructor
 * @implements {cljs.core.async.Pub}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.async.Mux}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async32765 = (function (ch,topic_fn,buf_fn,mults,ensure_mult,meta32766){
this.ch = ch;
this.topic_fn = topic_fn;
this.buf_fn = buf_fn;
this.mults = mults;
this.ensure_mult = ensure_mult;
this.meta32766 = meta32766;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_32767,meta32766__$1){
var self__ = this;
var _32767__$1 = this;
return (new cljs.core.async.t_cljs$core$async32765(self__.ch,self__.topic_fn,self__.buf_fn,self__.mults,self__.ensure_mult,meta32766__$1));
}));

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_32767){
var self__ = this;
var _32767__$1 = this;
return self__.meta32766;
}));

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$async$Mux$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$async$Mux$muxch_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return self__.ch;
}));

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$async$Pub$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$async$Pub$sub_STAR_$arity$4 = (function (p,topic,ch__$1,close_QMARK_){
var self__ = this;
var p__$1 = this;
var m = (self__.ensure_mult.cljs$core$IFn$_invoke$arity$1 ? self__.ensure_mult.cljs$core$IFn$_invoke$arity$1(topic) : self__.ensure_mult.call(null,topic));
return cljs.core.async.tap.cljs$core$IFn$_invoke$arity$3(m,ch__$1,close_QMARK_);
}));

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$async$Pub$unsub_STAR_$arity$3 = (function (p,topic,ch__$1){
var self__ = this;
var p__$1 = this;
var temp__5823__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(self__.mults),topic);
if(cljs.core.truth_(temp__5823__auto__)){
var m = temp__5823__auto__;
return cljs.core.async.untap(m,ch__$1);
} else {
return null;
}
}));

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.reset_BANG_(self__.mults,cljs.core.PersistentArrayMap.EMPTY);
}));

(cljs.core.async.t_cljs$core$async32765.prototype.cljs$core$async$Pub$unsub_all_STAR_$arity$2 = (function (_,topic){
var self__ = this;
var ___$1 = this;
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(self__.mults,cljs.core.dissoc,topic);
}));

(cljs.core.async.t_cljs$core$async32765.getBasis = (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"topic-fn","topic-fn",-862449736,null),new cljs.core.Symbol(null,"buf-fn","buf-fn",-1200281591,null),new cljs.core.Symbol(null,"mults","mults",-461114485,null),new cljs.core.Symbol(null,"ensure-mult","ensure-mult",1796584816,null),new cljs.core.Symbol(null,"meta32766","meta32766",728209902,null)], null);
}));

(cljs.core.async.t_cljs$core$async32765.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async32765.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async32765");

(cljs.core.async.t_cljs$core$async32765.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async32765");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async32765.
 */
cljs.core.async.__GT_t_cljs$core$async32765 = (function cljs$core$async$__GT_t_cljs$core$async32765(ch,topic_fn,buf_fn,mults,ensure_mult,meta32766){
return (new cljs.core.async.t_cljs$core$async32765(ch,topic_fn,buf_fn,mults,ensure_mult,meta32766));
});


/**
 * Creates and returns a pub(lication) of the supplied channel,
 *   partitioned into topics by the topic-fn. topic-fn will be applied to
 *   each value on the channel and the result will determine the 'topic'
 *   on which that value will be put. Channels can be subscribed to
 *   receive copies of topics using 'sub', and unsubscribed using
 *   'unsub'. Each topic will be handled by an internal mult on a
 *   dedicated channel. By default these internal channels are
 *   unbuffered, but a buf-fn can be supplied which, given a topic,
 *   creates a buffer with desired properties.
 * 
 *   Each item is distributed to all subs in parallel and synchronously,
 *   i.e. each sub must accept before the next item is distributed. Use
 *   buffering/windowing to prevent slow subs from holding up the pub.
 * 
 *   Items received when there are no matching subs get dropped.
 * 
 *   Note that if buf-fns are used then each topic is handled
 *   asynchronously, i.e. if a channel is subscribed to more than one
 *   topic it should not expect them to be interleaved identically with
 *   the source.
 */
cljs.core.async.pub = (function cljs$core$async$pub(var_args){
var G__32750 = arguments.length;
switch (G__32750) {
case 2:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.pub.cljs$core$IFn$_invoke$arity$2 = (function (ch,topic_fn){
return cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3(ch,topic_fn,cljs.core.constantly(null));
}));

(cljs.core.async.pub.cljs$core$IFn$_invoke$arity$3 = (function (ch,topic_fn,buf_fn){
var mults = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
var ensure_mult = (function (topic){
var or__5162__auto__ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(mults),topic);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(mults,(function (p1__32747_SHARP_){
if(cljs.core.truth_((p1__32747_SHARP_.cljs$core$IFn$_invoke$arity$1 ? p1__32747_SHARP_.cljs$core$IFn$_invoke$arity$1(topic) : p1__32747_SHARP_.call(null,topic)))){
return p1__32747_SHARP_;
} else {
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__32747_SHARP_,topic,cljs.core.async.mult(cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((buf_fn.cljs$core$IFn$_invoke$arity$1 ? buf_fn.cljs$core$IFn$_invoke$arity$1(topic) : buf_fn.call(null,topic)))));
}
})),topic);
}
});
var p = (new cljs.core.async.t_cljs$core$async32765(ch,topic_fn,buf_fn,mults,ensure_mult,cljs.core.PersistentArrayMap.EMPTY));
var c__30379__auto___34957 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_32875){
var state_val_32876 = (state_32875[(1)]);
if((state_val_32876 === (7))){
var inst_32871 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
var statearr_32881_34958 = state_32875__$1;
(statearr_32881_34958[(2)] = inst_32871);

(statearr_32881_34958[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (20))){
var state_32875__$1 = state_32875;
var statearr_32883_34959 = state_32875__$1;
(statearr_32883_34959[(2)] = null);

(statearr_32883_34959[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (1))){
var state_32875__$1 = state_32875;
var statearr_32884_34960 = state_32875__$1;
(statearr_32884_34960[(2)] = null);

(statearr_32884_34960[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (24))){
var inst_32853 = (state_32875[(7)]);
var inst_32863 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(mults,cljs.core.dissoc,inst_32853);
var state_32875__$1 = state_32875;
var statearr_32886_34961 = state_32875__$1;
(statearr_32886_34961[(2)] = inst_32863);

(statearr_32886_34961[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (4))){
var inst_32788 = (state_32875[(8)]);
var inst_32788__$1 = (state_32875[(2)]);
var inst_32789 = (inst_32788__$1 == null);
var state_32875__$1 = (function (){var statearr_32888 = state_32875;
(statearr_32888[(8)] = inst_32788__$1);

return statearr_32888;
})();
if(cljs.core.truth_(inst_32789)){
var statearr_32889_34962 = state_32875__$1;
(statearr_32889_34962[(1)] = (5));

} else {
var statearr_32890_34963 = state_32875__$1;
(statearr_32890_34963[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (15))){
var inst_32845 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
var statearr_32891_34964 = state_32875__$1;
(statearr_32891_34964[(2)] = inst_32845);

(statearr_32891_34964[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (21))){
var inst_32868 = (state_32875[(2)]);
var state_32875__$1 = (function (){var statearr_32892 = state_32875;
(statearr_32892[(9)] = inst_32868);

return statearr_32892;
})();
var statearr_32893_34968 = state_32875__$1;
(statearr_32893_34968[(2)] = null);

(statearr_32893_34968[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (13))){
var inst_32815 = (state_32875[(10)]);
var inst_32818 = cljs.core.chunked_seq_QMARK_(inst_32815);
var state_32875__$1 = state_32875;
if(inst_32818){
var statearr_32897_34971 = state_32875__$1;
(statearr_32897_34971[(1)] = (16));

} else {
var statearr_32899_34972 = state_32875__$1;
(statearr_32899_34972[(1)] = (17));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (22))){
var inst_32860 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
if(cljs.core.truth_(inst_32860)){
var statearr_32900_34973 = state_32875__$1;
(statearr_32900_34973[(1)] = (23));

} else {
var statearr_32901_34975 = state_32875__$1;
(statearr_32901_34975[(1)] = (24));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (6))){
var inst_32788 = (state_32875[(8)]);
var inst_32853 = (state_32875[(7)]);
var inst_32855 = (state_32875[(11)]);
var inst_32853__$1 = (topic_fn.cljs$core$IFn$_invoke$arity$1 ? topic_fn.cljs$core$IFn$_invoke$arity$1(inst_32788) : topic_fn.call(null,inst_32788));
var inst_32854 = cljs.core.deref(mults);
var inst_32855__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inst_32854,inst_32853__$1);
var state_32875__$1 = (function (){var statearr_32905 = state_32875;
(statearr_32905[(7)] = inst_32853__$1);

(statearr_32905[(11)] = inst_32855__$1);

return statearr_32905;
})();
if(cljs.core.truth_(inst_32855__$1)){
var statearr_32914_34977 = state_32875__$1;
(statearr_32914_34977[(1)] = (19));

} else {
var statearr_32915_34978 = state_32875__$1;
(statearr_32915_34978[(1)] = (20));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (25))){
var inst_32865 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
var statearr_32922_34980 = state_32875__$1;
(statearr_32922_34980[(2)] = inst_32865);

(statearr_32922_34980[(1)] = (21));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (17))){
var inst_32815 = (state_32875[(10)]);
var inst_32827 = cljs.core.first(inst_32815);
var inst_32828 = cljs.core.async.muxch_STAR_(inst_32827);
var inst_32829 = cljs.core.async.close_BANG_(inst_32828);
var inst_32838 = cljs.core.next(inst_32815);
var inst_32800 = inst_32838;
var inst_32801 = null;
var inst_32802 = (0);
var inst_32803 = (0);
var state_32875__$1 = (function (){var statearr_32923 = state_32875;
(statearr_32923[(12)] = inst_32829);

(statearr_32923[(13)] = inst_32800);

(statearr_32923[(14)] = inst_32801);

(statearr_32923[(15)] = inst_32802);

(statearr_32923[(16)] = inst_32803);

return statearr_32923;
})();
var statearr_32927_34981 = state_32875__$1;
(statearr_32927_34981[(2)] = null);

(statearr_32927_34981[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (3))){
var inst_32873 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
return cljs.core.async.impl.ioc_helpers.return_chan(state_32875__$1,inst_32873);
} else {
if((state_val_32876 === (12))){
var inst_32847 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
var statearr_32933_34984 = state_32875__$1;
(statearr_32933_34984[(2)] = inst_32847);

(statearr_32933_34984[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (2))){
var state_32875__$1 = state_32875;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_32875__$1,(4),ch);
} else {
if((state_val_32876 === (23))){
var state_32875__$1 = state_32875;
var statearr_32934_34985 = state_32875__$1;
(statearr_32934_34985[(2)] = null);

(statearr_32934_34985[(1)] = (25));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (19))){
var inst_32855 = (state_32875[(11)]);
var inst_32788 = (state_32875[(8)]);
var inst_32858 = cljs.core.async.muxch_STAR_(inst_32855);
var state_32875__$1 = state_32875;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_32875__$1,(22),inst_32858,inst_32788);
} else {
if((state_val_32876 === (11))){
var inst_32800 = (state_32875[(13)]);
var inst_32815 = (state_32875[(10)]);
var inst_32815__$1 = cljs.core.seq(inst_32800);
var state_32875__$1 = (function (){var statearr_32937 = state_32875;
(statearr_32937[(10)] = inst_32815__$1);

return statearr_32937;
})();
if(inst_32815__$1){
var statearr_32938_34986 = state_32875__$1;
(statearr_32938_34986[(1)] = (13));

} else {
var statearr_32939_34987 = state_32875__$1;
(statearr_32939_34987[(1)] = (14));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (9))){
var inst_32849 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
var statearr_32941_34988 = state_32875__$1;
(statearr_32941_34988[(2)] = inst_32849);

(statearr_32941_34988[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (5))){
var inst_32797 = cljs.core.deref(mults);
var inst_32798 = cljs.core.vals(inst_32797);
var inst_32799 = cljs.core.seq(inst_32798);
var inst_32800 = inst_32799;
var inst_32801 = null;
var inst_32802 = (0);
var inst_32803 = (0);
var state_32875__$1 = (function (){var statearr_32947 = state_32875;
(statearr_32947[(13)] = inst_32800);

(statearr_32947[(14)] = inst_32801);

(statearr_32947[(15)] = inst_32802);

(statearr_32947[(16)] = inst_32803);

return statearr_32947;
})();
var statearr_32948_35001 = state_32875__$1;
(statearr_32948_35001[(2)] = null);

(statearr_32948_35001[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (14))){
var state_32875__$1 = state_32875;
var statearr_32953_35002 = state_32875__$1;
(statearr_32953_35002[(2)] = null);

(statearr_32953_35002[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (16))){
var inst_32815 = (state_32875[(10)]);
var inst_32820 = cljs.core.chunk_first(inst_32815);
var inst_32821 = cljs.core.chunk_rest(inst_32815);
var inst_32823 = cljs.core.count(inst_32820);
var inst_32800 = inst_32821;
var inst_32801 = inst_32820;
var inst_32802 = inst_32823;
var inst_32803 = (0);
var state_32875__$1 = (function (){var statearr_32956 = state_32875;
(statearr_32956[(13)] = inst_32800);

(statearr_32956[(14)] = inst_32801);

(statearr_32956[(15)] = inst_32802);

(statearr_32956[(16)] = inst_32803);

return statearr_32956;
})();
var statearr_32957_35004 = state_32875__$1;
(statearr_32957_35004[(2)] = null);

(statearr_32957_35004[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (10))){
var inst_32801 = (state_32875[(14)]);
var inst_32803 = (state_32875[(16)]);
var inst_32800 = (state_32875[(13)]);
var inst_32802 = (state_32875[(15)]);
var inst_32809 = cljs.core._nth(inst_32801,inst_32803);
var inst_32810 = cljs.core.async.muxch_STAR_(inst_32809);
var inst_32811 = cljs.core.async.close_BANG_(inst_32810);
var inst_32812 = (inst_32803 + (1));
var tmp32950 = inst_32802;
var tmp32951 = inst_32801;
var tmp32952 = inst_32800;
var inst_32800__$1 = tmp32952;
var inst_32801__$1 = tmp32951;
var inst_32802__$1 = tmp32950;
var inst_32803__$1 = inst_32812;
var state_32875__$1 = (function (){var statearr_32958 = state_32875;
(statearr_32958[(17)] = inst_32811);

(statearr_32958[(13)] = inst_32800__$1);

(statearr_32958[(14)] = inst_32801__$1);

(statearr_32958[(15)] = inst_32802__$1);

(statearr_32958[(16)] = inst_32803__$1);

return statearr_32958;
})();
var statearr_32959_35006 = state_32875__$1;
(statearr_32959_35006[(2)] = null);

(statearr_32959_35006[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (18))){
var inst_32842 = (state_32875[(2)]);
var state_32875__$1 = state_32875;
var statearr_32960_35008 = state_32875__$1;
(statearr_32960_35008[(2)] = inst_32842);

(statearr_32960_35008[(1)] = (15));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_32876 === (8))){
var inst_32803 = (state_32875[(16)]);
var inst_32802 = (state_32875[(15)]);
var inst_32806 = (inst_32803 < inst_32802);
var inst_32807 = inst_32806;
var state_32875__$1 = state_32875;
if(cljs.core.truth_(inst_32807)){
var statearr_32965_35012 = state_32875__$1;
(statearr_32965_35012[(1)] = (10));

} else {
var statearr_32968_35013 = state_32875__$1;
(statearr_32968_35013[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_32972 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_32972[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_32972[(1)] = (1));

return statearr_32972;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_32875){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_32875);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e32973){var ex__29648__auto__ = e32973;
var statearr_32974_35016 = state_32875;
(statearr_32974_35016[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_32875[(4)]))){
var statearr_32975_35019 = state_32875;
(statearr_32975_35019[(1)] = cljs.core.first((state_32875[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35020 = state_32875;
state_32875 = G__35020;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_32875){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_32875);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_32976 = f__30380__auto__();
(statearr_32976[(6)] = c__30379__auto___34957);

return statearr_32976;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return p;
}));

(cljs.core.async.pub.cljs$lang$maxFixedArity = 3);

/**
 * Subscribes a channel to a topic of a pub.
 * 
 *   By default the channel will be closed when the source closes,
 *   but can be determined by the close? parameter.
 */
cljs.core.async.sub = (function cljs$core$async$sub(var_args){
var G__33021 = arguments.length;
switch (G__33021) {
case 3:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.sub.cljs$core$IFn$_invoke$arity$3 = (function (p,topic,ch){
return cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4(p,topic,ch,true);
}));

(cljs.core.async.sub.cljs$core$IFn$_invoke$arity$4 = (function (p,topic,ch,close_QMARK_){
return cljs.core.async.sub_STAR_(p,topic,ch,close_QMARK_);
}));

(cljs.core.async.sub.cljs$lang$maxFixedArity = 4);

/**
 * Unsubscribes a channel from a topic of a pub
 */
cljs.core.async.unsub = (function cljs$core$async$unsub(p,topic,ch){
return cljs.core.async.unsub_STAR_(p,topic,ch);
});
/**
 * Unsubscribes all channels from a pub, or a topic of a pub
 */
cljs.core.async.unsub_all = (function cljs$core$async$unsub_all(var_args){
var G__33041 = arguments.length;
switch (G__33041) {
case 1:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$1 = (function (p){
return cljs.core.async.unsub_all_STAR_(p);
}));

(cljs.core.async.unsub_all.cljs$core$IFn$_invoke$arity$2 = (function (p,topic){
return cljs.core.async.unsub_all_STAR_(p,topic);
}));

(cljs.core.async.unsub_all.cljs$lang$maxFixedArity = 2);

/**
 * Takes a function and a collection of source channels, and returns a
 *   channel which contains the values produced by applying f to the set
 *   of first items taken from each source channel, followed by applying
 *   f to the set of second items from each channel, until any one of the
 *   channels is closed, at which point the output channel will be
 *   closed. The returned channel will be unbuffered by default, or a
 *   buf-or-n can be supplied
 */
cljs.core.async.map = (function cljs$core$async$map(var_args){
var G__33048 = arguments.length;
switch (G__33048) {
case 2:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.map.cljs$core$IFn$_invoke$arity$2 = (function (f,chs){
return cljs.core.async.map.cljs$core$IFn$_invoke$arity$3(f,chs,null);
}));

(cljs.core.async.map.cljs$core$IFn$_invoke$arity$3 = (function (f,chs,buf_or_n){
var chs__$1 = cljs.core.vec(chs);
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var cnt = cljs.core.count(chs__$1);
var rets = cljs.core.object_array.cljs$core$IFn$_invoke$arity$1(cnt);
var dchan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
var dctr = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var done = cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (i){
return (function (ret){
(rets[i] = ret);

if((cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec) === (0))){
return cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(dchan,rets.slice((0)));
} else {
return null;
}
});
}),cljs.core.range.cljs$core$IFn$_invoke$arity$1(cnt));
if((cnt === (0))){
cljs.core.async.close_BANG_(out);
} else {
var c__30379__auto___35047 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_33125){
var state_val_33126 = (state_33125[(1)]);
if((state_val_33126 === (7))){
var state_33125__$1 = state_33125;
var statearr_33137_35048 = state_33125__$1;
(statearr_33137_35048[(2)] = null);

(statearr_33137_35048[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (1))){
var state_33125__$1 = state_33125;
var statearr_33138_35049 = state_33125__$1;
(statearr_33138_35049[(2)] = null);

(statearr_33138_35049[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (4))){
var inst_33068 = (state_33125[(7)]);
var inst_33067 = (state_33125[(8)]);
var inst_33071 = (inst_33068 < inst_33067);
var state_33125__$1 = state_33125;
if(cljs.core.truth_(inst_33071)){
var statearr_33141_35050 = state_33125__$1;
(statearr_33141_35050[(1)] = (6));

} else {
var statearr_33142_35051 = state_33125__$1;
(statearr_33142_35051[(1)] = (7));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (15))){
var inst_33111 = (state_33125[(9)]);
var inst_33116 = cljs.core.apply.cljs$core$IFn$_invoke$arity$2(f,inst_33111);
var state_33125__$1 = state_33125;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33125__$1,(17),out,inst_33116);
} else {
if((state_val_33126 === (13))){
var inst_33111 = (state_33125[(9)]);
var inst_33111__$1 = (state_33125[(2)]);
var inst_33112 = cljs.core.some(cljs.core.nil_QMARK_,inst_33111__$1);
var state_33125__$1 = (function (){var statearr_33151 = state_33125;
(statearr_33151[(9)] = inst_33111__$1);

return statearr_33151;
})();
if(cljs.core.truth_(inst_33112)){
var statearr_33152_35057 = state_33125__$1;
(statearr_33152_35057[(1)] = (14));

} else {
var statearr_33157_35058 = state_33125__$1;
(statearr_33157_35058[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (6))){
var state_33125__$1 = state_33125;
var statearr_33159_35059 = state_33125__$1;
(statearr_33159_35059[(2)] = null);

(statearr_33159_35059[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (17))){
var inst_33118 = (state_33125[(2)]);
var state_33125__$1 = (function (){var statearr_33165 = state_33125;
(statearr_33165[(10)] = inst_33118);

return statearr_33165;
})();
var statearr_33166_35062 = state_33125__$1;
(statearr_33166_35062[(2)] = null);

(statearr_33166_35062[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (3))){
var inst_33123 = (state_33125[(2)]);
var state_33125__$1 = state_33125;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33125__$1,inst_33123);
} else {
if((state_val_33126 === (12))){
var _ = (function (){var statearr_33169 = state_33125;
(statearr_33169[(4)] = cljs.core.rest((state_33125[(4)])));

return statearr_33169;
})();
var state_33125__$1 = state_33125;
var ex33164 = (state_33125__$1[(2)]);
var statearr_33172_35063 = state_33125__$1;
(statearr_33172_35063[(5)] = ex33164);


if((ex33164 instanceof Object)){
var statearr_33175_35064 = state_33125__$1;
(statearr_33175_35064[(1)] = (11));

(statearr_33175_35064[(5)] = null);

} else {
throw ex33164;

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (2))){
var inst_33066 = cljs.core.reset_BANG_(dctr,cnt);
var inst_33067 = cnt;
var inst_33068 = (0);
var state_33125__$1 = (function (){var statearr_33181 = state_33125;
(statearr_33181[(11)] = inst_33066);

(statearr_33181[(8)] = inst_33067);

(statearr_33181[(7)] = inst_33068);

return statearr_33181;
})();
var statearr_33182_35069 = state_33125__$1;
(statearr_33182_35069[(2)] = null);

(statearr_33182_35069[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (11))){
var inst_33086 = (state_33125[(2)]);
var inst_33087 = cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(dctr,cljs.core.dec);
var state_33125__$1 = (function (){var statearr_33187 = state_33125;
(statearr_33187[(12)] = inst_33086);

return statearr_33187;
})();
var statearr_33188_35070 = state_33125__$1;
(statearr_33188_35070[(2)] = inst_33087);

(statearr_33188_35070[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (9))){
var inst_33068 = (state_33125[(7)]);
var _ = (function (){var statearr_33189 = state_33125;
(statearr_33189[(4)] = cljs.core.cons((12),(state_33125[(4)])));

return statearr_33189;
})();
var inst_33097 = (chs__$1.cljs$core$IFn$_invoke$arity$1 ? chs__$1.cljs$core$IFn$_invoke$arity$1(inst_33068) : chs__$1.call(null,inst_33068));
var inst_33098 = (done.cljs$core$IFn$_invoke$arity$1 ? done.cljs$core$IFn$_invoke$arity$1(inst_33068) : done.call(null,inst_33068));
var inst_33099 = cljs.core.async.take_BANG_.cljs$core$IFn$_invoke$arity$2(inst_33097,inst_33098);
var ___$1 = (function (){var statearr_33190 = state_33125;
(statearr_33190[(4)] = cljs.core.rest((state_33125[(4)])));

return statearr_33190;
})();
var state_33125__$1 = state_33125;
var statearr_33191_35073 = state_33125__$1;
(statearr_33191_35073[(2)] = inst_33099);

(statearr_33191_35073[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (5))){
var inst_33109 = (state_33125[(2)]);
var state_33125__$1 = (function (){var statearr_33192 = state_33125;
(statearr_33192[(13)] = inst_33109);

return statearr_33192;
})();
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33125__$1,(13),dchan);
} else {
if((state_val_33126 === (14))){
var inst_33114 = cljs.core.async.close_BANG_(out);
var state_33125__$1 = state_33125;
var statearr_33197_35074 = state_33125__$1;
(statearr_33197_35074[(2)] = inst_33114);

(statearr_33197_35074[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (16))){
var inst_33121 = (state_33125[(2)]);
var state_33125__$1 = state_33125;
var statearr_33199_35079 = state_33125__$1;
(statearr_33199_35079[(2)] = inst_33121);

(statearr_33199_35079[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (10))){
var inst_33068 = (state_33125[(7)]);
var inst_33102 = (state_33125[(2)]);
var inst_33103 = (inst_33068 + (1));
var inst_33068__$1 = inst_33103;
var state_33125__$1 = (function (){var statearr_33200 = state_33125;
(statearr_33200[(14)] = inst_33102);

(statearr_33200[(7)] = inst_33068__$1);

return statearr_33200;
})();
var statearr_33201_35087 = state_33125__$1;
(statearr_33201_35087[(2)] = null);

(statearr_33201_35087[(1)] = (4));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33126 === (8))){
var inst_33107 = (state_33125[(2)]);
var state_33125__$1 = state_33125;
var statearr_33203_35095 = state_33125__$1;
(statearr_33203_35095[(2)] = inst_33107);

(statearr_33203_35095[(1)] = (5));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_33206 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_33206[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_33206[(1)] = (1));

return statearr_33206;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_33125){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_33125);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e33208){var ex__29648__auto__ = e33208;
var statearr_33210_35097 = state_33125;
(statearr_33210_35097[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_33125[(4)]))){
var statearr_33211_35099 = state_33125;
(statearr_33211_35099[(1)] = cljs.core.first((state_33125[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35100 = state_33125;
state_33125 = G__35100;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_33125){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_33125);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_33215 = f__30380__auto__();
(statearr_33215[(6)] = c__30379__auto___35047);

return statearr_33215;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));

}

return out;
}));

(cljs.core.async.map.cljs$lang$maxFixedArity = 3);

/**
 * Takes a collection of source channels and returns a channel which
 *   contains all values taken from them. The returned channel will be
 *   unbuffered by default, or a buf-or-n can be supplied. The channel
 *   will close after all the source channels have closed.
 */
cljs.core.async.merge = (function cljs$core$async$merge(var_args){
var G__33232 = arguments.length;
switch (G__33232) {
case 1:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.merge.cljs$core$IFn$_invoke$arity$1 = (function (chs){
return cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2(chs,null);
}));

(cljs.core.async.merge.cljs$core$IFn$_invoke$arity$2 = (function (chs,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__30379__auto___35106 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_33281){
var state_val_33282 = (state_33281[(1)]);
if((state_val_33282 === (7))){
var inst_33253 = (state_33281[(7)]);
var inst_33254 = (state_33281[(8)]);
var inst_33253__$1 = (state_33281[(2)]);
var inst_33254__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_33253__$1,(0),null);
var inst_33255 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(inst_33253__$1,(1),null);
var inst_33256 = (inst_33254__$1 == null);
var state_33281__$1 = (function (){var statearr_33287 = state_33281;
(statearr_33287[(7)] = inst_33253__$1);

(statearr_33287[(8)] = inst_33254__$1);

(statearr_33287[(9)] = inst_33255);

return statearr_33287;
})();
if(cljs.core.truth_(inst_33256)){
var statearr_33288_35112 = state_33281__$1;
(statearr_33288_35112[(1)] = (8));

} else {
var statearr_33289_35113 = state_33281__$1;
(statearr_33289_35113[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (1))){
var inst_33238 = cljs.core.vec(chs);
var inst_33239 = inst_33238;
var state_33281__$1 = (function (){var statearr_33290 = state_33281;
(statearr_33290[(10)] = inst_33239);

return statearr_33290;
})();
var statearr_33291_35115 = state_33281__$1;
(statearr_33291_35115[(2)] = null);

(statearr_33291_35115[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (4))){
var inst_33239 = (state_33281[(10)]);
var state_33281__$1 = state_33281;
return cljs.core.async.ioc_alts_BANG_(state_33281__$1,(7),inst_33239);
} else {
if((state_val_33282 === (6))){
var inst_33274 = (state_33281[(2)]);
var state_33281__$1 = state_33281;
var statearr_33295_35118 = state_33281__$1;
(statearr_33295_35118[(2)] = inst_33274);

(statearr_33295_35118[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (3))){
var inst_33276 = (state_33281[(2)]);
var state_33281__$1 = state_33281;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33281__$1,inst_33276);
} else {
if((state_val_33282 === (2))){
var inst_33239 = (state_33281[(10)]);
var inst_33246 = cljs.core.count(inst_33239);
var inst_33247 = (inst_33246 > (0));
var state_33281__$1 = state_33281;
if(cljs.core.truth_(inst_33247)){
var statearr_33298_35119 = state_33281__$1;
(statearr_33298_35119[(1)] = (4));

} else {
var statearr_33299_35120 = state_33281__$1;
(statearr_33299_35120[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (11))){
var inst_33239 = (state_33281[(10)]);
var inst_33267 = (state_33281[(2)]);
var tmp33296 = inst_33239;
var inst_33239__$1 = tmp33296;
var state_33281__$1 = (function (){var statearr_33303 = state_33281;
(statearr_33303[(11)] = inst_33267);

(statearr_33303[(10)] = inst_33239__$1);

return statearr_33303;
})();
var statearr_33304_35125 = state_33281__$1;
(statearr_33304_35125[(2)] = null);

(statearr_33304_35125[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (9))){
var inst_33254 = (state_33281[(8)]);
var state_33281__$1 = state_33281;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33281__$1,(11),out,inst_33254);
} else {
if((state_val_33282 === (5))){
var inst_33272 = cljs.core.async.close_BANG_(out);
var state_33281__$1 = state_33281;
var statearr_33305_35130 = state_33281__$1;
(statearr_33305_35130[(2)] = inst_33272);

(statearr_33305_35130[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (10))){
var inst_33270 = (state_33281[(2)]);
var state_33281__$1 = state_33281;
var statearr_33306_35135 = state_33281__$1;
(statearr_33306_35135[(2)] = inst_33270);

(statearr_33306_35135[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33282 === (8))){
var inst_33239 = (state_33281[(10)]);
var inst_33253 = (state_33281[(7)]);
var inst_33254 = (state_33281[(8)]);
var inst_33255 = (state_33281[(9)]);
var inst_33262 = (function (){var cs = inst_33239;
var vec__33249 = inst_33253;
var v = inst_33254;
var c = inst_33255;
return (function (p1__33225_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(c,p1__33225_SHARP_);
});
})();
var inst_33263 = cljs.core.filterv(inst_33262,inst_33239);
var inst_33239__$1 = inst_33263;
var state_33281__$1 = (function (){var statearr_33314 = state_33281;
(statearr_33314[(10)] = inst_33239__$1);

return statearr_33314;
})();
var statearr_33315_35155 = state_33281__$1;
(statearr_33315_35155[(2)] = null);

(statearr_33315_35155[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_33318 = [null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_33318[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_33318[(1)] = (1));

return statearr_33318;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_33281){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_33281);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e33319){var ex__29648__auto__ = e33319;
var statearr_33320_35156 = state_33281;
(statearr_33320_35156[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_33281[(4)]))){
var statearr_33324_35157 = state_33281;
(statearr_33324_35157[(1)] = cljs.core.first((state_33281[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35165 = state_33281;
state_33281 = G__35165;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_33281){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_33281);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_33328 = f__30380__auto__();
(statearr_33328[(6)] = c__30379__auto___35106);

return statearr_33328;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return out;
}));

(cljs.core.async.merge.cljs$lang$maxFixedArity = 2);

/**
 * Returns a channel containing the single (collection) result of the
 *   items taken from the channel conjoined to the supplied
 *   collection. ch must close before into produces a result.
 */
cljs.core.async.into = (function cljs$core$async$into(coll,ch){
return cljs.core.async.reduce(cljs.core.conj,coll,ch);
});
/**
 * Returns a channel that will return, at most, n items from ch. After n items
 * have been returned, or ch has been closed, the return chanel will close.
 * 
 *   The output channel is unbuffered by default, unless buf-or-n is given.
 */
cljs.core.async.take = (function cljs$core$async$take(var_args){
var G__33338 = arguments.length;
switch (G__33338) {
case 2:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.take.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.take.cljs$core$IFn$_invoke$arity$3(n,ch,null);
}));

(cljs.core.async.take.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__30379__auto___35171 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_33367){
var state_val_33368 = (state_33367[(1)]);
if((state_val_33368 === (7))){
var inst_33347 = (state_33367[(7)]);
var inst_33347__$1 = (state_33367[(2)]);
var inst_33348 = (inst_33347__$1 == null);
var inst_33349 = cljs.core.not(inst_33348);
var state_33367__$1 = (function (){var statearr_33369 = state_33367;
(statearr_33369[(7)] = inst_33347__$1);

return statearr_33369;
})();
if(inst_33349){
var statearr_33370_35182 = state_33367__$1;
(statearr_33370_35182[(1)] = (8));

} else {
var statearr_33373_35183 = state_33367__$1;
(statearr_33373_35183[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (1))){
var inst_33342 = (0);
var state_33367__$1 = (function (){var statearr_33374 = state_33367;
(statearr_33374[(8)] = inst_33342);

return statearr_33374;
})();
var statearr_33375_35184 = state_33367__$1;
(statearr_33375_35184[(2)] = null);

(statearr_33375_35184[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (4))){
var state_33367__$1 = state_33367;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33367__$1,(7),ch);
} else {
if((state_val_33368 === (6))){
var inst_33361 = (state_33367[(2)]);
var state_33367__$1 = state_33367;
var statearr_33378_35189 = state_33367__$1;
(statearr_33378_35189[(2)] = inst_33361);

(statearr_33378_35189[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (3))){
var inst_33363 = (state_33367[(2)]);
var inst_33365 = cljs.core.async.close_BANG_(out);
var state_33367__$1 = (function (){var statearr_33379 = state_33367;
(statearr_33379[(9)] = inst_33363);

return statearr_33379;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_33367__$1,inst_33365);
} else {
if((state_val_33368 === (2))){
var inst_33342 = (state_33367[(8)]);
var inst_33344 = (inst_33342 < n);
var state_33367__$1 = state_33367;
if(cljs.core.truth_(inst_33344)){
var statearr_33380_35222 = state_33367__$1;
(statearr_33380_35222[(1)] = (4));

} else {
var statearr_33381_35224 = state_33367__$1;
(statearr_33381_35224[(1)] = (5));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (11))){
var inst_33342 = (state_33367[(8)]);
var inst_33352 = (state_33367[(2)]);
var inst_33354 = (inst_33342 + (1));
var inst_33342__$1 = inst_33354;
var state_33367__$1 = (function (){var statearr_33382 = state_33367;
(statearr_33382[(10)] = inst_33352);

(statearr_33382[(8)] = inst_33342__$1);

return statearr_33382;
})();
var statearr_33383_35229 = state_33367__$1;
(statearr_33383_35229[(2)] = null);

(statearr_33383_35229[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (9))){
var state_33367__$1 = state_33367;
var statearr_33385_35230 = state_33367__$1;
(statearr_33385_35230[(2)] = null);

(statearr_33385_35230[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (5))){
var state_33367__$1 = state_33367;
var statearr_33386_35231 = state_33367__$1;
(statearr_33386_35231[(2)] = null);

(statearr_33386_35231[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (10))){
var inst_33358 = (state_33367[(2)]);
var state_33367__$1 = state_33367;
var statearr_33391_35236 = state_33367__$1;
(statearr_33391_35236[(2)] = inst_33358);

(statearr_33391_35236[(1)] = (6));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33368 === (8))){
var inst_33347 = (state_33367[(7)]);
var state_33367__$1 = state_33367;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33367__$1,(11),out,inst_33347);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_33393 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_33393[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_33393[(1)] = (1));

return statearr_33393;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_33367){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_33367);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e33394){var ex__29648__auto__ = e33394;
var statearr_33395_35258 = state_33367;
(statearr_33395_35258[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_33367[(4)]))){
var statearr_33396_35263 = state_33367;
(statearr_33396_35263[(1)] = cljs.core.first((state_33367[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35264 = state_33367;
state_33367 = G__35264;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_33367){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_33367);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_33398 = f__30380__auto__();
(statearr_33398[(6)] = c__30379__auto___35171);

return statearr_33398;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return out;
}));

(cljs.core.async.take.cljs$lang$maxFixedArity = 3);


/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Handler}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async33411 = (function (f,ch,meta33406,_,fn1,meta33412){
this.f = f;
this.ch = ch;
this.meta33406 = meta33406;
this._ = _;
this.fn1 = fn1;
this.meta33412 = meta33412;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async33411.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_33413,meta33412__$1){
var self__ = this;
var _33413__$1 = this;
return (new cljs.core.async.t_cljs$core$async33411(self__.f,self__.ch,self__.meta33406,self__._,self__.fn1,meta33412__$1));
}));

(cljs.core.async.t_cljs$core$async33411.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_33413){
var self__ = this;
var _33413__$1 = this;
return self__.meta33412;
}));

(cljs.core.async.t_cljs$core$async33411.prototype.cljs$core$async$impl$protocols$Handler$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33411.prototype.cljs$core$async$impl$protocols$Handler$active_QMARK_$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
return cljs.core.async.impl.protocols.active_QMARK_(self__.fn1);
}));

(cljs.core.async.t_cljs$core$async33411.prototype.cljs$core$async$impl$protocols$Handler$blockable_QMARK_$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
return true;
}));

(cljs.core.async.t_cljs$core$async33411.prototype.cljs$core$async$impl$protocols$Handler$commit$arity$1 = (function (___$1){
var self__ = this;
var ___$2 = this;
var f1 = cljs.core.async.impl.protocols.commit(self__.fn1);
return (function (p1__33402_SHARP_){
var G__33424 = (((p1__33402_SHARP_ == null))?null:(self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(p1__33402_SHARP_) : self__.f.call(null,p1__33402_SHARP_)));
return (f1.cljs$core$IFn$_invoke$arity$1 ? f1.cljs$core$IFn$_invoke$arity$1(G__33424) : f1.call(null,G__33424));
});
}));

(cljs.core.async.t_cljs$core$async33411.getBasis = (function (){
return new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta33406","meta33406",1624387126,null),cljs.core.with_meta(new cljs.core.Symbol(null,"_","_",-1201019570,null),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"tag","tag",-1290361223),new cljs.core.Symbol("cljs.core.async","t_cljs$core$async33405","cljs.core.async/t_cljs$core$async33405",622000439,null)], null)),new cljs.core.Symbol(null,"fn1","fn1",895834444,null),new cljs.core.Symbol(null,"meta33412","meta33412",1999008451,null)], null);
}));

(cljs.core.async.t_cljs$core$async33411.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async33411.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async33411");

(cljs.core.async.t_cljs$core$async33411.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async33411");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async33411.
 */
cljs.core.async.__GT_t_cljs$core$async33411 = (function cljs$core$async$__GT_t_cljs$core$async33411(f,ch,meta33406,_,fn1,meta33412){
return (new cljs.core.async.t_cljs$core$async33411(f,ch,meta33406,_,fn1,meta33412));
});



/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async33405 = (function (f,ch,meta33406){
this.f = f;
this.ch = ch;
this.meta33406 = meta33406;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_33407,meta33406__$1){
var self__ = this;
var _33407__$1 = this;
return (new cljs.core.async.t_cljs$core$async33405(self__.f,self__.ch,meta33406__$1));
}));

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_33407){
var self__ = this;
var _33407__$1 = this;
return self__.meta33406;
}));

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
var ret = cljs.core.async.impl.protocols.take_BANG_(self__.ch,(new cljs.core.async.t_cljs$core$async33411(self__.f,self__.ch,self__.meta33406,___$1,fn1,cljs.core.PersistentArrayMap.EMPTY)));
if(cljs.core.truth_((function (){var and__5160__auto__ = ret;
if(cljs.core.truth_(and__5160__auto__)){
return (!((cljs.core.deref(ret) == null)));
} else {
return and__5160__auto__;
}
})())){
return cljs.core.async.impl.channels.box((function (){var G__33433 = cljs.core.deref(ret);
return (self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(G__33433) : self__.f.call(null,G__33433));
})());
} else {
return ret;
}
}));

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33405.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,val,fn1);
}));

(cljs.core.async.t_cljs$core$async33405.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta33406","meta33406",1624387126,null)], null);
}));

(cljs.core.async.t_cljs$core$async33405.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async33405.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async33405");

(cljs.core.async.t_cljs$core$async33405.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async33405");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async33405.
 */
cljs.core.async.__GT_t_cljs$core$async33405 = (function cljs$core$async$__GT_t_cljs$core$async33405(f,ch,meta33406){
return (new cljs.core.async.t_cljs$core$async33405(f,ch,meta33406));
});


/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_LT_ = (function cljs$core$async$map_LT_(f,ch){
return (new cljs.core.async.t_cljs$core$async33405(f,ch,cljs.core.PersistentArrayMap.EMPTY));
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async33441 = (function (f,ch,meta33442){
this.f = f;
this.ch = ch;
this.meta33442 = meta33442;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_33443,meta33442__$1){
var self__ = this;
var _33443__$1 = this;
return (new cljs.core.async.t_cljs$core$async33441(self__.f,self__.ch,meta33442__$1));
}));

(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_33443){
var self__ = this;
var _33443__$1 = this;
return self__.meta33442;
}));

(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_(self__.ch,fn1);
}));

(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33441.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,(self__.f.cljs$core$IFn$_invoke$arity$1 ? self__.f.cljs$core$IFn$_invoke$arity$1(val) : self__.f.call(null,val)),fn1);
}));

(cljs.core.async.t_cljs$core$async33441.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"f","f",43394975,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta33442","meta33442",-688680239,null)], null);
}));

(cljs.core.async.t_cljs$core$async33441.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async33441.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async33441");

(cljs.core.async.t_cljs$core$async33441.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async33441");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async33441.
 */
cljs.core.async.__GT_t_cljs$core$async33441 = (function cljs$core$async$__GT_t_cljs$core$async33441(f,ch,meta33442){
return (new cljs.core.async.t_cljs$core$async33441(f,ch,meta33442));
});


/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.map_GT_ = (function cljs$core$async$map_GT_(f,ch){
return (new cljs.core.async.t_cljs$core$async33441(f,ch,cljs.core.PersistentArrayMap.EMPTY));
});

/**
* @constructor
 * @implements {cljs.core.async.impl.protocols.Channel}
 * @implements {cljs.core.async.impl.protocols.WritePort}
 * @implements {cljs.core.async.impl.protocols.ReadPort}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.IWithMeta}
*/
cljs.core.async.t_cljs$core$async33459 = (function (p,ch,meta33460){
this.p = p;
this.ch = ch;
this.meta33460 = meta33460;
this.cljs$lang$protocol_mask$partition0$ = 393216;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (_33461,meta33460__$1){
var self__ = this;
var _33461__$1 = this;
return (new cljs.core.async.t_cljs$core$async33459(self__.p,self__.ch,meta33460__$1));
}));

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$IMeta$_meta$arity$1 = (function (_33461){
var self__ = this;
var _33461__$1 = this;
return self__.meta33460;
}));

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$async$impl$protocols$Channel$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$async$impl$protocols$Channel$close_BANG_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.close_BANG_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$async$impl$protocols$Channel$closed_QMARK_$arity$1 = (function (_){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.closed_QMARK_(self__.ch);
}));

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$async$impl$protocols$ReadPort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$async$impl$protocols$ReadPort$take_BANG_$arity$2 = (function (_,fn1){
var self__ = this;
var ___$1 = this;
return cljs.core.async.impl.protocols.take_BANG_(self__.ch,fn1);
}));

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$async$impl$protocols$WritePort$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.async.t_cljs$core$async33459.prototype.cljs$core$async$impl$protocols$WritePort$put_BANG_$arity$3 = (function (_,val,fn1){
var self__ = this;
var ___$1 = this;
if(cljs.core.truth_((self__.p.cljs$core$IFn$_invoke$arity$1 ? self__.p.cljs$core$IFn$_invoke$arity$1(val) : self__.p.call(null,val)))){
return cljs.core.async.impl.protocols.put_BANG_(self__.ch,val,fn1);
} else {
return cljs.core.async.impl.channels.box(cljs.core.not(cljs.core.async.impl.protocols.closed_QMARK_(self__.ch)));
}
}));

(cljs.core.async.t_cljs$core$async33459.getBasis = (function (){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"p","p",1791580836,null),new cljs.core.Symbol(null,"ch","ch",1085813622,null),new cljs.core.Symbol(null,"meta33460","meta33460",-629154119,null)], null);
}));

(cljs.core.async.t_cljs$core$async33459.cljs$lang$type = true);

(cljs.core.async.t_cljs$core$async33459.cljs$lang$ctorStr = "cljs.core.async/t_cljs$core$async33459");

(cljs.core.async.t_cljs$core$async33459.cljs$lang$ctorPrWriter = (function (this__5455__auto__,writer__5456__auto__,opt__5457__auto__){
return cljs.core._write(writer__5456__auto__,"cljs.core.async/t_cljs$core$async33459");
}));

/**
 * Positional factory function for cljs.core.async/t_cljs$core$async33459.
 */
cljs.core.async.__GT_t_cljs$core$async33459 = (function cljs$core$async$__GT_t_cljs$core$async33459(p,ch,meta33460){
return (new cljs.core.async.t_cljs$core$async33459(p,ch,meta33460));
});


/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_GT_ = (function cljs$core$async$filter_GT_(p,ch){
return (new cljs.core.async.t_cljs$core$async33459(p,ch,cljs.core.PersistentArrayMap.EMPTY));
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_GT_ = (function cljs$core$async$remove_GT_(p,ch){
return cljs.core.async.filter_GT_(cljs.core.complement(p),ch);
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.filter_LT_ = (function cljs$core$async$filter_LT_(var_args){
var G__33488 = arguments.length;
switch (G__33488) {
case 2:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3(p,ch,null);
}));

(cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__30379__auto___35349 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_33525){
var state_val_33526 = (state_33525[(1)]);
if((state_val_33526 === (7))){
var inst_33513 = (state_33525[(2)]);
var state_33525__$1 = state_33525;
var statearr_33542_35351 = state_33525__$1;
(statearr_33542_35351[(2)] = inst_33513);

(statearr_33542_35351[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (1))){
var state_33525__$1 = state_33525;
var statearr_33543_35352 = state_33525__$1;
(statearr_33543_35352[(2)] = null);

(statearr_33543_35352[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (4))){
var inst_33498 = (state_33525[(7)]);
var inst_33498__$1 = (state_33525[(2)]);
var inst_33499 = (inst_33498__$1 == null);
var state_33525__$1 = (function (){var statearr_33544 = state_33525;
(statearr_33544[(7)] = inst_33498__$1);

return statearr_33544;
})();
if(cljs.core.truth_(inst_33499)){
var statearr_33545_35357 = state_33525__$1;
(statearr_33545_35357[(1)] = (5));

} else {
var statearr_33546_35358 = state_33525__$1;
(statearr_33546_35358[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (6))){
var inst_33498 = (state_33525[(7)]);
var inst_33504 = (p.cljs$core$IFn$_invoke$arity$1 ? p.cljs$core$IFn$_invoke$arity$1(inst_33498) : p.call(null,inst_33498));
var state_33525__$1 = state_33525;
if(cljs.core.truth_(inst_33504)){
var statearr_33549_35360 = state_33525__$1;
(statearr_33549_35360[(1)] = (8));

} else {
var statearr_33551_35362 = state_33525__$1;
(statearr_33551_35362[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (3))){
var inst_33517 = (state_33525[(2)]);
var state_33525__$1 = state_33525;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33525__$1,inst_33517);
} else {
if((state_val_33526 === (2))){
var state_33525__$1 = state_33525;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33525__$1,(4),ch);
} else {
if((state_val_33526 === (11))){
var inst_33507 = (state_33525[(2)]);
var state_33525__$1 = state_33525;
var statearr_33553_35365 = state_33525__$1;
(statearr_33553_35365[(2)] = inst_33507);

(statearr_33553_35365[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (9))){
var state_33525__$1 = state_33525;
var statearr_33554_35369 = state_33525__$1;
(statearr_33554_35369[(2)] = null);

(statearr_33554_35369[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (5))){
var inst_33501 = cljs.core.async.close_BANG_(out);
var state_33525__$1 = state_33525;
var statearr_33555_35375 = state_33525__$1;
(statearr_33555_35375[(2)] = inst_33501);

(statearr_33555_35375[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (10))){
var inst_33510 = (state_33525[(2)]);
var state_33525__$1 = (function (){var statearr_33556 = state_33525;
(statearr_33556[(8)] = inst_33510);

return statearr_33556;
})();
var statearr_33558_35383 = state_33525__$1;
(statearr_33558_35383[(2)] = null);

(statearr_33558_35383[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33526 === (8))){
var inst_33498 = (state_33525[(7)]);
var state_33525__$1 = state_33525;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33525__$1,(11),out,inst_33498);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_33561 = [null,null,null,null,null,null,null,null,null];
(statearr_33561[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_33561[(1)] = (1));

return statearr_33561;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_33525){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_33525);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e33568){var ex__29648__auto__ = e33568;
var statearr_33569_35390 = state_33525;
(statearr_33569_35390[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_33525[(4)]))){
var statearr_33570_35391 = state_33525;
(statearr_33570_35391[(1)] = cljs.core.first((state_33525[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35397 = state_33525;
state_33525 = G__35397;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_33525){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_33525);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_33572 = f__30380__auto__();
(statearr_33572[(6)] = c__30379__auto___35349);

return statearr_33572;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return out;
}));

(cljs.core.async.filter_LT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.remove_LT_ = (function cljs$core$async$remove_LT_(var_args){
var G__33577 = arguments.length;
switch (G__33577) {
case 2:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$2 = (function (p,ch){
return cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3(p,ch,null);
}));

(cljs.core.async.remove_LT_.cljs$core$IFn$_invoke$arity$3 = (function (p,ch,buf_or_n){
return cljs.core.async.filter_LT_.cljs$core$IFn$_invoke$arity$3(cljs.core.complement(p),ch,buf_or_n);
}));

(cljs.core.async.remove_LT_.cljs$lang$maxFixedArity = 3);

cljs.core.async.mapcat_STAR_ = (function cljs$core$async$mapcat_STAR_(f,in$,out){
var c__30379__auto__ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_33642){
var state_val_33644 = (state_33642[(1)]);
if((state_val_33644 === (7))){
var inst_33638 = (state_33642[(2)]);
var state_33642__$1 = state_33642;
var statearr_33648_35430 = state_33642__$1;
(statearr_33648_35430[(2)] = inst_33638);

(statearr_33648_35430[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (20))){
var inst_33608 = (state_33642[(7)]);
var inst_33619 = (state_33642[(2)]);
var inst_33620 = cljs.core.next(inst_33608);
var inst_33594 = inst_33620;
var inst_33595 = null;
var inst_33596 = (0);
var inst_33597 = (0);
var state_33642__$1 = (function (){var statearr_33649 = state_33642;
(statearr_33649[(8)] = inst_33619);

(statearr_33649[(9)] = inst_33594);

(statearr_33649[(10)] = inst_33595);

(statearr_33649[(11)] = inst_33596);

(statearr_33649[(12)] = inst_33597);

return statearr_33649;
})();
var statearr_33650_35431 = state_33642__$1;
(statearr_33650_35431[(2)] = null);

(statearr_33650_35431[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (1))){
var state_33642__$1 = state_33642;
var statearr_33651_35432 = state_33642__$1;
(statearr_33651_35432[(2)] = null);

(statearr_33651_35432[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (4))){
var inst_33583 = (state_33642[(13)]);
var inst_33583__$1 = (state_33642[(2)]);
var inst_33584 = (inst_33583__$1 == null);
var state_33642__$1 = (function (){var statearr_33652 = state_33642;
(statearr_33652[(13)] = inst_33583__$1);

return statearr_33652;
})();
if(cljs.core.truth_(inst_33584)){
var statearr_33653_35433 = state_33642__$1;
(statearr_33653_35433[(1)] = (5));

} else {
var statearr_33657_35434 = state_33642__$1;
(statearr_33657_35434[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (15))){
var state_33642__$1 = state_33642;
var statearr_33661_35435 = state_33642__$1;
(statearr_33661_35435[(2)] = null);

(statearr_33661_35435[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (21))){
var state_33642__$1 = state_33642;
var statearr_33662_35436 = state_33642__$1;
(statearr_33662_35436[(2)] = null);

(statearr_33662_35436[(1)] = (23));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (13))){
var inst_33597 = (state_33642[(12)]);
var inst_33594 = (state_33642[(9)]);
var inst_33595 = (state_33642[(10)]);
var inst_33596 = (state_33642[(11)]);
var inst_33604 = (state_33642[(2)]);
var inst_33605 = (inst_33597 + (1));
var tmp33658 = inst_33596;
var tmp33659 = inst_33595;
var tmp33660 = inst_33594;
var inst_33594__$1 = tmp33660;
var inst_33595__$1 = tmp33659;
var inst_33596__$1 = tmp33658;
var inst_33597__$1 = inst_33605;
var state_33642__$1 = (function (){var statearr_33665 = state_33642;
(statearr_33665[(14)] = inst_33604);

(statearr_33665[(9)] = inst_33594__$1);

(statearr_33665[(10)] = inst_33595__$1);

(statearr_33665[(11)] = inst_33596__$1);

(statearr_33665[(12)] = inst_33597__$1);

return statearr_33665;
})();
var statearr_33666_35447 = state_33642__$1;
(statearr_33666_35447[(2)] = null);

(statearr_33666_35447[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (22))){
var state_33642__$1 = state_33642;
var statearr_33669_35448 = state_33642__$1;
(statearr_33669_35448[(2)] = null);

(statearr_33669_35448[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (6))){
var inst_33583 = (state_33642[(13)]);
var inst_33592 = (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(inst_33583) : f.call(null,inst_33583));
var inst_33593 = cljs.core.seq(inst_33592);
var inst_33594 = inst_33593;
var inst_33595 = null;
var inst_33596 = (0);
var inst_33597 = (0);
var state_33642__$1 = (function (){var statearr_33680 = state_33642;
(statearr_33680[(9)] = inst_33594);

(statearr_33680[(10)] = inst_33595);

(statearr_33680[(11)] = inst_33596);

(statearr_33680[(12)] = inst_33597);

return statearr_33680;
})();
var statearr_33681_35462 = state_33642__$1;
(statearr_33681_35462[(2)] = null);

(statearr_33681_35462[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (17))){
var inst_33608 = (state_33642[(7)]);
var inst_33612 = cljs.core.chunk_first(inst_33608);
var inst_33613 = cljs.core.chunk_rest(inst_33608);
var inst_33614 = cljs.core.count(inst_33612);
var inst_33594 = inst_33613;
var inst_33595 = inst_33612;
var inst_33596 = inst_33614;
var inst_33597 = (0);
var state_33642__$1 = (function (){var statearr_33688 = state_33642;
(statearr_33688[(9)] = inst_33594);

(statearr_33688[(10)] = inst_33595);

(statearr_33688[(11)] = inst_33596);

(statearr_33688[(12)] = inst_33597);

return statearr_33688;
})();
var statearr_33689_35481 = state_33642__$1;
(statearr_33689_35481[(2)] = null);

(statearr_33689_35481[(1)] = (8));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (3))){
var inst_33640 = (state_33642[(2)]);
var state_33642__$1 = state_33642;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33642__$1,inst_33640);
} else {
if((state_val_33644 === (12))){
var inst_33628 = (state_33642[(2)]);
var state_33642__$1 = state_33642;
var statearr_33690_35488 = state_33642__$1;
(statearr_33690_35488[(2)] = inst_33628);

(statearr_33690_35488[(1)] = (9));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (2))){
var state_33642__$1 = state_33642;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33642__$1,(4),in$);
} else {
if((state_val_33644 === (23))){
var inst_33636 = (state_33642[(2)]);
var state_33642__$1 = state_33642;
var statearr_33692_35494 = state_33642__$1;
(statearr_33692_35494[(2)] = inst_33636);

(statearr_33692_35494[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (19))){
var inst_33623 = (state_33642[(2)]);
var state_33642__$1 = state_33642;
var statearr_33696_35499 = state_33642__$1;
(statearr_33696_35499[(2)] = inst_33623);

(statearr_33696_35499[(1)] = (16));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (11))){
var inst_33594 = (state_33642[(9)]);
var inst_33608 = (state_33642[(7)]);
var inst_33608__$1 = cljs.core.seq(inst_33594);
var state_33642__$1 = (function (){var statearr_33697 = state_33642;
(statearr_33697[(7)] = inst_33608__$1);

return statearr_33697;
})();
if(inst_33608__$1){
var statearr_33698_35517 = state_33642__$1;
(statearr_33698_35517[(1)] = (14));

} else {
var statearr_33699_35518 = state_33642__$1;
(statearr_33699_35518[(1)] = (15));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (9))){
var inst_33630 = (state_33642[(2)]);
var inst_33631 = cljs.core.async.impl.protocols.closed_QMARK_(out);
var state_33642__$1 = (function (){var statearr_33700 = state_33642;
(statearr_33700[(15)] = inst_33630);

return statearr_33700;
})();
if(cljs.core.truth_(inst_33631)){
var statearr_33705_35521 = state_33642__$1;
(statearr_33705_35521[(1)] = (21));

} else {
var statearr_33706_35526 = state_33642__$1;
(statearr_33706_35526[(1)] = (22));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (5))){
var inst_33586 = cljs.core.async.close_BANG_(out);
var state_33642__$1 = state_33642;
var statearr_33708_35534 = state_33642__$1;
(statearr_33708_35534[(2)] = inst_33586);

(statearr_33708_35534[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (14))){
var inst_33608 = (state_33642[(7)]);
var inst_33610 = cljs.core.chunked_seq_QMARK_(inst_33608);
var state_33642__$1 = state_33642;
if(inst_33610){
var statearr_33709_35549 = state_33642__$1;
(statearr_33709_35549[(1)] = (17));

} else {
var statearr_33710_35551 = state_33642__$1;
(statearr_33710_35551[(1)] = (18));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (16))){
var inst_33626 = (state_33642[(2)]);
var state_33642__$1 = state_33642;
var statearr_33711_35552 = state_33642__$1;
(statearr_33711_35552[(2)] = inst_33626);

(statearr_33711_35552[(1)] = (12));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33644 === (10))){
var inst_33595 = (state_33642[(10)]);
var inst_33597 = (state_33642[(12)]);
var inst_33602 = cljs.core._nth(inst_33595,inst_33597);
var state_33642__$1 = state_33642;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33642__$1,(13),out,inst_33602);
} else {
if((state_val_33644 === (18))){
var inst_33608 = (state_33642[(7)]);
var inst_33617 = cljs.core.first(inst_33608);
var state_33642__$1 = state_33642;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33642__$1,(20),out,inst_33617);
} else {
if((state_val_33644 === (8))){
var inst_33597 = (state_33642[(12)]);
var inst_33596 = (state_33642[(11)]);
var inst_33599 = (inst_33597 < inst_33596);
var inst_33600 = inst_33599;
var state_33642__$1 = state_33642;
if(cljs.core.truth_(inst_33600)){
var statearr_33724_35567 = state_33642__$1;
(statearr_33724_35567[(1)] = (10));

} else {
var statearr_33725_35568 = state_33642__$1;
(statearr_33725_35568[(1)] = (11));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$mapcat_STAR__$_state_machine__29645__auto__ = null;
var cljs$core$async$mapcat_STAR__$_state_machine__29645__auto____0 = (function (){
var statearr_33729 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_33729[(0)] = cljs$core$async$mapcat_STAR__$_state_machine__29645__auto__);

(statearr_33729[(1)] = (1));

return statearr_33729;
});
var cljs$core$async$mapcat_STAR__$_state_machine__29645__auto____1 = (function (state_33642){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_33642);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e33734){var ex__29648__auto__ = e33734;
var statearr_33735_35598 = state_33642;
(statearr_33735_35598[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_33642[(4)]))){
var statearr_33736_35599 = state_33642;
(statearr_33736_35599[(1)] = cljs.core.first((state_33642[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35602 = state_33642;
state_33642 = G__35602;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$mapcat_STAR__$_state_machine__29645__auto__ = function(state_33642){
switch(arguments.length){
case 0:
return cljs$core$async$mapcat_STAR__$_state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$mapcat_STAR__$_state_machine__29645__auto____1.call(this,state_33642);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$mapcat_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$mapcat_STAR__$_state_machine__29645__auto____0;
cljs$core$async$mapcat_STAR__$_state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$mapcat_STAR__$_state_machine__29645__auto____1;
return cljs$core$async$mapcat_STAR__$_state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_33737 = f__30380__auto__();
(statearr_33737[(6)] = c__30379__auto__);

return statearr_33737;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));

return c__30379__auto__;
});
/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_LT_ = (function cljs$core$async$mapcat_LT_(var_args){
var G__33741 = arguments.length;
switch (G__33741) {
case 2:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$2 = (function (f,in$){
return cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3(f,in$,null);
}));

(cljs.core.async.mapcat_LT_.cljs$core$IFn$_invoke$arity$3 = (function (f,in$,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
cljs.core.async.mapcat_STAR_(f,in$,out);

return out;
}));

(cljs.core.async.mapcat_LT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.mapcat_GT_ = (function cljs$core$async$mapcat_GT_(var_args){
var G__33751 = arguments.length;
switch (G__33751) {
case 2:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$2 = (function (f,out){
return cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3(f,out,null);
}));

(cljs.core.async.mapcat_GT_.cljs$core$IFn$_invoke$arity$3 = (function (f,out,buf_or_n){
var in$ = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
cljs.core.async.mapcat_STAR_(f,in$,out);

return in$;
}));

(cljs.core.async.mapcat_GT_.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.unique = (function cljs$core$async$unique(var_args){
var G__33759 = arguments.length;
switch (G__33759) {
case 1:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.unique.cljs$core$IFn$_invoke$arity$1 = (function (ch){
return cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2(ch,null);
}));

(cljs.core.async.unique.cljs$core$IFn$_invoke$arity$2 = (function (ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__30379__auto___35616 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_33790){
var state_val_33791 = (state_33790[(1)]);
if((state_val_33791 === (7))){
var inst_33785 = (state_33790[(2)]);
var state_33790__$1 = state_33790;
var statearr_33792_35618 = state_33790__$1;
(statearr_33792_35618[(2)] = inst_33785);

(statearr_33792_35618[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33791 === (1))){
var inst_33764 = null;
var state_33790__$1 = (function (){var statearr_33793 = state_33790;
(statearr_33793[(7)] = inst_33764);

return statearr_33793;
})();
var statearr_33794_35619 = state_33790__$1;
(statearr_33794_35619[(2)] = null);

(statearr_33794_35619[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33791 === (4))){
var inst_33770 = (state_33790[(8)]);
var inst_33770__$1 = (state_33790[(2)]);
var inst_33771 = (inst_33770__$1 == null);
var inst_33772 = cljs.core.not(inst_33771);
var state_33790__$1 = (function (){var statearr_33795 = state_33790;
(statearr_33795[(8)] = inst_33770__$1);

return statearr_33795;
})();
if(inst_33772){
var statearr_33801_35620 = state_33790__$1;
(statearr_33801_35620[(1)] = (5));

} else {
var statearr_33802_35621 = state_33790__$1;
(statearr_33802_35621[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33791 === (6))){
var state_33790__$1 = state_33790;
var statearr_33803_35622 = state_33790__$1;
(statearr_33803_35622[(2)] = null);

(statearr_33803_35622[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33791 === (3))){
var inst_33787 = (state_33790[(2)]);
var inst_33788 = cljs.core.async.close_BANG_(out);
var state_33790__$1 = (function (){var statearr_33805 = state_33790;
(statearr_33805[(9)] = inst_33787);

return statearr_33805;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_33790__$1,inst_33788);
} else {
if((state_val_33791 === (2))){
var state_33790__$1 = state_33790;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33790__$1,(4),ch);
} else {
if((state_val_33791 === (11))){
var inst_33770 = (state_33790[(8)]);
var inst_33779 = (state_33790[(2)]);
var inst_33764 = inst_33770;
var state_33790__$1 = (function (){var statearr_33808 = state_33790;
(statearr_33808[(10)] = inst_33779);

(statearr_33808[(7)] = inst_33764);

return statearr_33808;
})();
var statearr_33809_35624 = state_33790__$1;
(statearr_33809_35624[(2)] = null);

(statearr_33809_35624[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33791 === (9))){
var inst_33770 = (state_33790[(8)]);
var state_33790__$1 = state_33790;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33790__$1,(11),out,inst_33770);
} else {
if((state_val_33791 === (5))){
var inst_33770 = (state_33790[(8)]);
var inst_33764 = (state_33790[(7)]);
var inst_33774 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_33770,inst_33764);
var state_33790__$1 = state_33790;
if(inst_33774){
var statearr_33812_35630 = state_33790__$1;
(statearr_33812_35630[(1)] = (8));

} else {
var statearr_33813_35631 = state_33790__$1;
(statearr_33813_35631[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33791 === (10))){
var inst_33782 = (state_33790[(2)]);
var state_33790__$1 = state_33790;
var statearr_33821_35632 = state_33790__$1;
(statearr_33821_35632[(2)] = inst_33782);

(statearr_33821_35632[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33791 === (8))){
var inst_33764 = (state_33790[(7)]);
var tmp33810 = inst_33764;
var inst_33764__$1 = tmp33810;
var state_33790__$1 = (function (){var statearr_33829 = state_33790;
(statearr_33829[(7)] = inst_33764__$1);

return statearr_33829;
})();
var statearr_33830_35637 = state_33790__$1;
(statearr_33830_35637[(2)] = null);

(statearr_33830_35637[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_33834 = [null,null,null,null,null,null,null,null,null,null,null];
(statearr_33834[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_33834[(1)] = (1));

return statearr_33834;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_33790){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_33790);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e33835){var ex__29648__auto__ = e33835;
var statearr_33836_35643 = state_33790;
(statearr_33836_35643[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_33790[(4)]))){
var statearr_33837_35648 = state_33790;
(statearr_33837_35648[(1)] = cljs.core.first((state_33790[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35654 = state_33790;
state_33790 = G__35654;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_33790){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_33790);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_33842 = f__30380__auto__();
(statearr_33842[(6)] = c__30379__auto___35616);

return statearr_33842;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return out;
}));

(cljs.core.async.unique.cljs$lang$maxFixedArity = 2);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition = (function cljs$core$async$partition(var_args){
var G__33853 = arguments.length;
switch (G__33853) {
case 2:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.partition.cljs$core$IFn$_invoke$arity$2 = (function (n,ch){
return cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3(n,ch,null);
}));

(cljs.core.async.partition.cljs$core$IFn$_invoke$arity$3 = (function (n,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__30379__auto___35661 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_33911){
var state_val_33912 = (state_33911[(1)]);
if((state_val_33912 === (7))){
var inst_33905 = (state_33911[(2)]);
var state_33911__$1 = state_33911;
var statearr_33915_35662 = state_33911__$1;
(statearr_33915_35662[(2)] = inst_33905);

(statearr_33915_35662[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (1))){
var inst_33871 = (new Array(n));
var inst_33872 = inst_33871;
var inst_33873 = (0);
var state_33911__$1 = (function (){var statearr_33928 = state_33911;
(statearr_33928[(7)] = inst_33872);

(statearr_33928[(8)] = inst_33873);

return statearr_33928;
})();
var statearr_33929_35664 = state_33911__$1;
(statearr_33929_35664[(2)] = null);

(statearr_33929_35664[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (4))){
var inst_33876 = (state_33911[(9)]);
var inst_33876__$1 = (state_33911[(2)]);
var inst_33878 = (inst_33876__$1 == null);
var inst_33879 = cljs.core.not(inst_33878);
var state_33911__$1 = (function (){var statearr_33933 = state_33911;
(statearr_33933[(9)] = inst_33876__$1);

return statearr_33933;
})();
if(inst_33879){
var statearr_33934_35666 = state_33911__$1;
(statearr_33934_35666[(1)] = (5));

} else {
var statearr_33935_35667 = state_33911__$1;
(statearr_33935_35667[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (15))){
var inst_33899 = (state_33911[(2)]);
var state_33911__$1 = state_33911;
var statearr_33936_35668 = state_33911__$1;
(statearr_33936_35668[(2)] = inst_33899);

(statearr_33936_35668[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (13))){
var state_33911__$1 = state_33911;
var statearr_33937_35669 = state_33911__$1;
(statearr_33937_35669[(2)] = null);

(statearr_33937_35669[(1)] = (14));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (6))){
var inst_33873 = (state_33911[(8)]);
var inst_33895 = (inst_33873 > (0));
var state_33911__$1 = state_33911;
if(cljs.core.truth_(inst_33895)){
var statearr_33938_35671 = state_33911__$1;
(statearr_33938_35671[(1)] = (12));

} else {
var statearr_33939_35673 = state_33911__$1;
(statearr_33939_35673[(1)] = (13));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (3))){
var inst_33907 = (state_33911[(2)]);
var state_33911__$1 = state_33911;
return cljs.core.async.impl.ioc_helpers.return_chan(state_33911__$1,inst_33907);
} else {
if((state_val_33912 === (12))){
var inst_33872 = (state_33911[(7)]);
var inst_33897 = cljs.core.vec(inst_33872);
var state_33911__$1 = state_33911;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33911__$1,(15),out,inst_33897);
} else {
if((state_val_33912 === (2))){
var state_33911__$1 = state_33911;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_33911__$1,(4),ch);
} else {
if((state_val_33912 === (11))){
var inst_33889 = (state_33911[(2)]);
var inst_33890 = (new Array(n));
var inst_33872 = inst_33890;
var inst_33873 = (0);
var state_33911__$1 = (function (){var statearr_33940 = state_33911;
(statearr_33940[(10)] = inst_33889);

(statearr_33940[(7)] = inst_33872);

(statearr_33940[(8)] = inst_33873);

return statearr_33940;
})();
var statearr_33941_35683 = state_33911__$1;
(statearr_33941_35683[(2)] = null);

(statearr_33941_35683[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (9))){
var inst_33872 = (state_33911[(7)]);
var inst_33887 = cljs.core.vec(inst_33872);
var state_33911__$1 = state_33911;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_33911__$1,(11),out,inst_33887);
} else {
if((state_val_33912 === (5))){
var inst_33872 = (state_33911[(7)]);
var inst_33873 = (state_33911[(8)]);
var inst_33876 = (state_33911[(9)]);
var inst_33882 = (state_33911[(11)]);
var inst_33881 = (inst_33872[inst_33873] = inst_33876);
var inst_33882__$1 = (inst_33873 + (1));
var inst_33883 = (inst_33882__$1 < n);
var state_33911__$1 = (function (){var statearr_33942 = state_33911;
(statearr_33942[(12)] = inst_33881);

(statearr_33942[(11)] = inst_33882__$1);

return statearr_33942;
})();
if(cljs.core.truth_(inst_33883)){
var statearr_33943_35691 = state_33911__$1;
(statearr_33943_35691[(1)] = (8));

} else {
var statearr_33944_35692 = state_33911__$1;
(statearr_33944_35692[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (14))){
var inst_33902 = (state_33911[(2)]);
var inst_33903 = cljs.core.async.close_BANG_(out);
var state_33911__$1 = (function (){var statearr_33946 = state_33911;
(statearr_33946[(13)] = inst_33902);

return statearr_33946;
})();
var statearr_33947_35693 = state_33911__$1;
(statearr_33947_35693[(2)] = inst_33903);

(statearr_33947_35693[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (10))){
var inst_33893 = (state_33911[(2)]);
var state_33911__$1 = state_33911;
var statearr_33948_35695 = state_33911__$1;
(statearr_33948_35695[(2)] = inst_33893);

(statearr_33948_35695[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_33912 === (8))){
var inst_33872 = (state_33911[(7)]);
var inst_33882 = (state_33911[(11)]);
var tmp33945 = inst_33872;
var inst_33872__$1 = tmp33945;
var inst_33873 = inst_33882;
var state_33911__$1 = (function (){var statearr_33949 = state_33911;
(statearr_33949[(7)] = inst_33872__$1);

(statearr_33949[(8)] = inst_33873);

return statearr_33949;
})();
var statearr_33950_35697 = state_33911__$1;
(statearr_33950_35697[(2)] = null);

(statearr_33950_35697[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_33951 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_33951[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_33951[(1)] = (1));

return statearr_33951;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_33911){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_33911);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e33955){var ex__29648__auto__ = e33955;
var statearr_33957_35707 = state_33911;
(statearr_33957_35707[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_33911[(4)]))){
var statearr_33965_35708 = state_33911;
(statearr_33965_35708[(1)] = cljs.core.first((state_33911[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35709 = state_33911;
state_33911 = G__35709;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_33911){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_33911);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_33971 = f__30380__auto__();
(statearr_33971[(6)] = c__30379__auto___35661);

return statearr_33971;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return out;
}));

(cljs.core.async.partition.cljs$lang$maxFixedArity = 3);

/**
 * Deprecated - this function will be removed. Use transducer instead
 */
cljs.core.async.partition_by = (function cljs$core$async$partition_by(var_args){
var G__33993 = arguments.length;
switch (G__33993) {
case 2:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",arguments.length].join("")));

}
});

(cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$2 = (function (f,ch){
return cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3(f,ch,null);
}));

(cljs.core.async.partition_by.cljs$core$IFn$_invoke$arity$3 = (function (f,ch,buf_or_n){
var out = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1(buf_or_n);
var c__30379__auto___35717 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__30380__auto__ = (function (){var switch__29644__auto__ = (function (state_34040){
var state_val_34041 = (state_34040[(1)]);
if((state_val_34041 === (7))){
var inst_34036 = (state_34040[(2)]);
var state_34040__$1 = state_34040;
var statearr_34044_35719 = state_34040__$1;
(statearr_34044_35719[(2)] = inst_34036);

(statearr_34044_35719[(1)] = (3));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (1))){
var inst_33995 = [];
var inst_33996 = inst_33995;
var inst_33997 = new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123);
var state_34040__$1 = (function (){var statearr_34045 = state_34040;
(statearr_34045[(7)] = inst_33996);

(statearr_34045[(8)] = inst_33997);

return statearr_34045;
})();
var statearr_34046_35724 = state_34040__$1;
(statearr_34046_35724[(2)] = null);

(statearr_34046_35724[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (4))){
var inst_34000 = (state_34040[(9)]);
var inst_34000__$1 = (state_34040[(2)]);
var inst_34001 = (inst_34000__$1 == null);
var inst_34002 = cljs.core.not(inst_34001);
var state_34040__$1 = (function (){var statearr_34047 = state_34040;
(statearr_34047[(9)] = inst_34000__$1);

return statearr_34047;
})();
if(inst_34002){
var statearr_34048_35729 = state_34040__$1;
(statearr_34048_35729[(1)] = (5));

} else {
var statearr_34049_35730 = state_34040__$1;
(statearr_34049_35730[(1)] = (6));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (15))){
var inst_33996 = (state_34040[(7)]);
var inst_34028 = cljs.core.vec(inst_33996);
var state_34040__$1 = state_34040;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_34040__$1,(18),out,inst_34028);
} else {
if((state_val_34041 === (13))){
var inst_34023 = (state_34040[(2)]);
var state_34040__$1 = state_34040;
var statearr_34050_35735 = state_34040__$1;
(statearr_34050_35735[(2)] = inst_34023);

(statearr_34050_35735[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (6))){
var inst_33996 = (state_34040[(7)]);
var inst_34025 = inst_33996.length;
var inst_34026 = (inst_34025 > (0));
var state_34040__$1 = state_34040;
if(cljs.core.truth_(inst_34026)){
var statearr_34051_35737 = state_34040__$1;
(statearr_34051_35737[(1)] = (15));

} else {
var statearr_34052_35738 = state_34040__$1;
(statearr_34052_35738[(1)] = (16));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (17))){
var inst_34033 = (state_34040[(2)]);
var inst_34034 = cljs.core.async.close_BANG_(out);
var state_34040__$1 = (function (){var statearr_34053 = state_34040;
(statearr_34053[(10)] = inst_34033);

return statearr_34053;
})();
var statearr_34054_35742 = state_34040__$1;
(statearr_34054_35742[(2)] = inst_34034);

(statearr_34054_35742[(1)] = (7));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (3))){
var inst_34038 = (state_34040[(2)]);
var state_34040__$1 = state_34040;
return cljs.core.async.impl.ioc_helpers.return_chan(state_34040__$1,inst_34038);
} else {
if((state_val_34041 === (12))){
var inst_33996 = (state_34040[(7)]);
var inst_34016 = cljs.core.vec(inst_33996);
var state_34040__$1 = state_34040;
return cljs.core.async.impl.ioc_helpers.put_BANG_(state_34040__$1,(14),out,inst_34016);
} else {
if((state_val_34041 === (2))){
var state_34040__$1 = state_34040;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_34040__$1,(4),ch);
} else {
if((state_val_34041 === (11))){
var inst_33996 = (state_34040[(7)]);
var inst_34000 = (state_34040[(9)]);
var inst_34004 = (state_34040[(11)]);
var inst_34013 = inst_33996.push(inst_34000);
var tmp34061 = inst_33996;
var inst_33996__$1 = tmp34061;
var inst_33997 = inst_34004;
var state_34040__$1 = (function (){var statearr_34062 = state_34040;
(statearr_34062[(12)] = inst_34013);

(statearr_34062[(7)] = inst_33996__$1);

(statearr_34062[(8)] = inst_33997);

return statearr_34062;
})();
var statearr_34063_35753 = state_34040__$1;
(statearr_34063_35753[(2)] = null);

(statearr_34063_35753[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (9))){
var inst_33997 = (state_34040[(8)]);
var inst_34009 = cljs.core.keyword_identical_QMARK_(inst_33997,new cljs.core.Keyword("cljs.core.async","nothing","cljs.core.async/nothing",-69252123));
var state_34040__$1 = state_34040;
var statearr_34064_35760 = state_34040__$1;
(statearr_34064_35760[(2)] = inst_34009);

(statearr_34064_35760[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (5))){
var inst_34000 = (state_34040[(9)]);
var inst_34004 = (state_34040[(11)]);
var inst_33997 = (state_34040[(8)]);
var inst_34005 = (state_34040[(13)]);
var inst_34004__$1 = (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(inst_34000) : f.call(null,inst_34000));
var inst_34005__$1 = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(inst_34004__$1,inst_33997);
var state_34040__$1 = (function (){var statearr_34067 = state_34040;
(statearr_34067[(11)] = inst_34004__$1);

(statearr_34067[(13)] = inst_34005__$1);

return statearr_34067;
})();
if(inst_34005__$1){
var statearr_34068_35776 = state_34040__$1;
(statearr_34068_35776[(1)] = (8));

} else {
var statearr_34069_35777 = state_34040__$1;
(statearr_34069_35777[(1)] = (9));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (14))){
var inst_34000 = (state_34040[(9)]);
var inst_34004 = (state_34040[(11)]);
var inst_34018 = (state_34040[(2)]);
var inst_34019 = [];
var inst_34020 = inst_34019.push(inst_34000);
var inst_33996 = inst_34019;
var inst_33997 = inst_34004;
var state_34040__$1 = (function (){var statearr_34071 = state_34040;
(statearr_34071[(14)] = inst_34018);

(statearr_34071[(15)] = inst_34020);

(statearr_34071[(7)] = inst_33996);

(statearr_34071[(8)] = inst_33997);

return statearr_34071;
})();
var statearr_34073_35798 = state_34040__$1;
(statearr_34073_35798[(2)] = null);

(statearr_34073_35798[(1)] = (2));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (16))){
var state_34040__$1 = state_34040;
var statearr_34074_35800 = state_34040__$1;
(statearr_34074_35800[(2)] = null);

(statearr_34074_35800[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (10))){
var inst_34011 = (state_34040[(2)]);
var state_34040__$1 = state_34040;
if(cljs.core.truth_(inst_34011)){
var statearr_34084_35801 = state_34040__$1;
(statearr_34084_35801[(1)] = (11));

} else {
var statearr_34085_35803 = state_34040__$1;
(statearr_34085_35803[(1)] = (12));

}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (18))){
var inst_34030 = (state_34040[(2)]);
var state_34040__$1 = state_34040;
var statearr_34086_35807 = state_34040__$1;
(statearr_34086_35807[(2)] = inst_34030);

(statearr_34086_35807[(1)] = (17));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
if((state_val_34041 === (8))){
var inst_34005 = (state_34040[(13)]);
var state_34040__$1 = state_34040;
var statearr_34087_35808 = state_34040__$1;
(statearr_34087_35808[(2)] = inst_34005);

(statearr_34087_35808[(1)] = (10));


return new cljs.core.Keyword(null,"recur","recur",-437573268);
} else {
return null;
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
});
return (function() {
var cljs$core$async$state_machine__29645__auto__ = null;
var cljs$core$async$state_machine__29645__auto____0 = (function (){
var statearr_34097 = [null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null];
(statearr_34097[(0)] = cljs$core$async$state_machine__29645__auto__);

(statearr_34097[(1)] = (1));

return statearr_34097;
});
var cljs$core$async$state_machine__29645__auto____1 = (function (state_34040){
while(true){
var ret_value__29646__auto__ = (function (){try{while(true){
var result__29647__auto__ = switch__29644__auto__(state_34040);
if(cljs.core.keyword_identical_QMARK_(result__29647__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__29647__auto__;
}
break;
}
}catch (e34098){var ex__29648__auto__ = e34098;
var statearr_34100_35813 = state_34040;
(statearr_34100_35813[(2)] = ex__29648__auto__);


if(cljs.core.seq((state_34040[(4)]))){
var statearr_34101_35818 = state_34040;
(statearr_34101_35818[(1)] = cljs.core.first((state_34040[(4)])));

} else {
throw ex__29648__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__29646__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__35821 = state_34040;
state_34040 = G__35821;
continue;
} else {
return ret_value__29646__auto__;
}
break;
}
});
cljs$core$async$state_machine__29645__auto__ = function(state_34040){
switch(arguments.length){
case 0:
return cljs$core$async$state_machine__29645__auto____0.call(this);
case 1:
return cljs$core$async$state_machine__29645__auto____1.call(this,state_34040);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$0 = cljs$core$async$state_machine__29645__auto____0;
cljs$core$async$state_machine__29645__auto__.cljs$core$IFn$_invoke$arity$1 = cljs$core$async$state_machine__29645__auto____1;
return cljs$core$async$state_machine__29645__auto__;
})()
})();
var state__30381__auto__ = (function (){var statearr_34107 = f__30380__auto__();
(statearr_34107[(6)] = c__30379__auto___35717);

return statearr_34107;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__30381__auto__);
}));


return out;
}));

(cljs.core.async.partition_by.cljs$lang$maxFixedArity = 3);


//# sourceMappingURL=cljs.core.async.js.map
