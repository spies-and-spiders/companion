goog.provide('cljs.repl');
cljs.repl.print_doc = (function cljs$repl$print_doc(p__35026){
var map__35027 = p__35026;
var map__35027__$1 = cljs.core.__destructure_map(map__35027);
var m = map__35027__$1;
var n = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35027__$1,new cljs.core.Keyword(null,"ns","ns",441598760));
var nm = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35027__$1,new cljs.core.Keyword(null,"name","name",1843675177));
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["-------------------------"], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(function (){var or__5162__auto__ = new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var temp__5823__auto__ = new cljs.core.Keyword(null,"ns","ns",441598760).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(temp__5823__auto__)){
var ns = temp__5823__auto__;
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(ns)+"/");
} else {
return null;
}
})())+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m)));
}
})()], 0));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Protocol"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m))){
var seq__35031_35450 = cljs.core.seq(new cljs.core.Keyword(null,"forms","forms",2045992350).cljs$core$IFn$_invoke$arity$1(m));
var chunk__35032_35451 = null;
var count__35033_35452 = (0);
var i__35034_35453 = (0);
while(true){
if((i__35034_35453 < count__35033_35452)){
var f_35454 = chunk__35032_35451.cljs$core$IIndexed$_nth$arity$2(null,i__35034_35453);
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["  ",f_35454], 0));


var G__35456 = seq__35031_35450;
var G__35457 = chunk__35032_35451;
var G__35458 = count__35033_35452;
var G__35459 = (i__35034_35453 + (1));
seq__35031_35450 = G__35456;
chunk__35032_35451 = G__35457;
count__35033_35452 = G__35458;
i__35034_35453 = G__35459;
continue;
} else {
var temp__5823__auto___35460 = cljs.core.seq(seq__35031_35450);
if(temp__5823__auto___35460){
var seq__35031_35461__$1 = temp__5823__auto___35460;
if(cljs.core.chunked_seq_QMARK_(seq__35031_35461__$1)){
var c__5694__auto___35463 = cljs.core.chunk_first(seq__35031_35461__$1);
var G__35464 = cljs.core.chunk_rest(seq__35031_35461__$1);
var G__35465 = c__5694__auto___35463;
var G__35466 = cljs.core.count(c__5694__auto___35463);
var G__35467 = (0);
seq__35031_35450 = G__35464;
chunk__35032_35451 = G__35465;
count__35033_35452 = G__35466;
i__35034_35453 = G__35467;
continue;
} else {
var f_35468 = cljs.core.first(seq__35031_35461__$1);
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["  ",f_35468], 0));


var G__35472 = cljs.core.next(seq__35031_35461__$1);
var G__35473 = null;
var G__35474 = (0);
var G__35475 = (0);
seq__35031_35450 = G__35472;
chunk__35032_35451 = G__35473;
count__35033_35452 = G__35474;
i__35034_35453 = G__35475;
continue;
}
} else {
}
}
break;
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m))){
var arglists_35476 = new cljs.core.Keyword(null,"arglists","arglists",1661989754).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_((function (){var or__5162__auto__ = new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m);
}
})())){
cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([arglists_35476], 0));
} else {
cljs.core.prn.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Symbol(null,"quote","quote",1377916282,null),cljs.core.first(arglists_35476)))?cljs.core.second(arglists_35476):arglists_35476)], 0));
}
} else {
}
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"special-form","special-form",-1326536374).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Special Form"], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m)], 0));

if(cljs.core.contains_QMARK_(m,new cljs.core.Keyword(null,"url","url",276297046))){
if(cljs.core.truth_(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m))){
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n  Please see http://clojure.org/"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"url","url",276297046).cljs$core$IFn$_invoke$arity$1(m)))], 0));
} else {
return null;
}
} else {
return cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n  Please see http://clojure.org/special_forms#"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(m)))], 0));
}
} else {
if(cljs.core.truth_(new cljs.core.Keyword(null,"macro","macro",-867863404).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Macro"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"spec","spec",347520401).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Spec"], 0));
} else {
}

if(cljs.core.truth_(new cljs.core.Keyword(null,"repl-special-function","repl-special-function",1262603725).cljs$core$IFn$_invoke$arity$1(m))){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["REPL Special Function"], 0));
} else {
}

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",new cljs.core.Keyword(null,"doc","doc",1913296891).cljs$core$IFn$_invoke$arity$1(m)], 0));

if(cljs.core.truth_(new cljs.core.Keyword(null,"protocol","protocol",652470118).cljs$core$IFn$_invoke$arity$1(m))){
var seq__35075_35500 = cljs.core.seq(new cljs.core.Keyword(null,"methods","methods",453930866).cljs$core$IFn$_invoke$arity$1(m));
var chunk__35076_35501 = null;
var count__35077_35502 = (0);
var i__35078_35503 = (0);
while(true){
if((i__35078_35503 < count__35077_35502)){
var vec__35108_35507 = chunk__35076_35501.cljs$core$IIndexed$_nth$arity$2(null,i__35078_35503);
var name_35508 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35108_35507,(0),null);
var map__35111_35509 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35108_35507,(1),null);
var map__35111_35510__$1 = cljs.core.__destructure_map(map__35111_35509);
var doc_35511 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35111_35510__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_35512 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35111_35510__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println();

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",name_35508], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",arglists_35512], 0));

if(cljs.core.truth_(doc_35511)){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",doc_35511], 0));
} else {
}


var G__35522 = seq__35075_35500;
var G__35523 = chunk__35076_35501;
var G__35524 = count__35077_35502;
var G__35525 = (i__35078_35503 + (1));
seq__35075_35500 = G__35522;
chunk__35076_35501 = G__35523;
count__35077_35502 = G__35524;
i__35078_35503 = G__35525;
continue;
} else {
var temp__5823__auto___35529 = cljs.core.seq(seq__35075_35500);
if(temp__5823__auto___35529){
var seq__35075_35533__$1 = temp__5823__auto___35529;
if(cljs.core.chunked_seq_QMARK_(seq__35075_35533__$1)){
var c__5694__auto___35535 = cljs.core.chunk_first(seq__35075_35533__$1);
var G__35540 = cljs.core.chunk_rest(seq__35075_35533__$1);
var G__35541 = c__5694__auto___35535;
var G__35542 = cljs.core.count(c__5694__auto___35535);
var G__35543 = (0);
seq__35075_35500 = G__35540;
chunk__35076_35501 = G__35541;
count__35077_35502 = G__35542;
i__35078_35503 = G__35543;
continue;
} else {
var vec__35121_35544 = cljs.core.first(seq__35075_35533__$1);
var name_35545 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35121_35544,(0),null);
var map__35124_35546 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35121_35544,(1),null);
var map__35124_35547__$1 = cljs.core.__destructure_map(map__35124_35546);
var doc_35548 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35124_35547__$1,new cljs.core.Keyword(null,"doc","doc",1913296891));
var arglists_35550 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35124_35547__$1,new cljs.core.Keyword(null,"arglists","arglists",1661989754));
cljs.core.println();

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",name_35545], 0));

cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",arglists_35550], 0));

if(cljs.core.truth_(doc_35548)){
cljs.core.println.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([" ",doc_35548], 0));
} else {
}


var G__35563 = cljs.core.next(seq__35075_35533__$1);
var G__35564 = null;
var G__35565 = (0);
var G__35566 = (0);
seq__35075_35500 = G__35563;
chunk__35076_35501 = G__35564;
count__35077_35502 = G__35565;
i__35078_35503 = G__35566;
continue;
}
} else {
}
}
break;
}
} else {
}

if(cljs.core.truth_(n)){
var temp__5823__auto__ = cljs.spec.alpha.get_spec(cljs.core.symbol.cljs$core$IFn$_invoke$arity$2((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.ns_name(n))),cljs.core.name(nm)));
if(cljs.core.truth_(temp__5823__auto__)){
var fnspec = temp__5823__auto__;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2(["Spec"], 0));

var seq__35151 = cljs.core.seq(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"args","args",1315556576),new cljs.core.Keyword(null,"ret","ret",-468222814),new cljs.core.Keyword(null,"fn","fn",-1175266204)], null));
var chunk__35152 = null;
var count__35153 = (0);
var i__35154 = (0);
while(true){
if((i__35154 < count__35153)){
var role = chunk__35152.cljs$core$IIndexed$_nth$arity$2(null,i__35154);
var temp__5823__auto___35572__$1 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(fnspec,role);
if(cljs.core.truth_(temp__5823__auto___35572__$1)){
var spec_35578 = temp__5823__auto___35572__$1;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(role))+":"),cljs.spec.alpha.describe(spec_35578)], 0));
} else {
}


var G__35590 = seq__35151;
var G__35591 = chunk__35152;
var G__35592 = count__35153;
var G__35593 = (i__35154 + (1));
seq__35151 = G__35590;
chunk__35152 = G__35591;
count__35153 = G__35592;
i__35154 = G__35593;
continue;
} else {
var temp__5823__auto____$1 = cljs.core.seq(seq__35151);
if(temp__5823__auto____$1){
var seq__35151__$1 = temp__5823__auto____$1;
if(cljs.core.chunked_seq_QMARK_(seq__35151__$1)){
var c__5694__auto__ = cljs.core.chunk_first(seq__35151__$1);
var G__35594 = cljs.core.chunk_rest(seq__35151__$1);
var G__35595 = c__5694__auto__;
var G__35596 = cljs.core.count(c__5694__auto__);
var G__35597 = (0);
seq__35151 = G__35594;
chunk__35152 = G__35595;
count__35153 = G__35596;
i__35154 = G__35597;
continue;
} else {
var role = cljs.core.first(seq__35151__$1);
var temp__5823__auto___35600__$2 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(fnspec,role);
if(cljs.core.truth_(temp__5823__auto___35600__$2)){
var spec_35601 = temp__5823__auto___35600__$2;
cljs.core.print.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(""+"\n "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(cljs.core.name(role))+":"),cljs.spec.alpha.describe(spec_35601)], 0));
} else {
}


var G__35607 = cljs.core.next(seq__35151__$1);
var G__35608 = null;
var G__35609 = (0);
var G__35610 = (0);
seq__35151 = G__35607;
chunk__35152 = G__35608;
count__35153 = G__35609;
i__35154 = G__35610;
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
} else {
return null;
}
}
});
/**
 * Constructs a data representation for a Error with keys:
 *  :cause - root cause message
 *  :phase - error phase
 *  :via - cause chain, with cause keys:
 *           :type - exception class symbol
 *           :message - exception message
 *           :data - ex-data
 *           :at - top stack element
 *  :trace - root cause stack elements
 */
cljs.repl.Error__GT_map = (function cljs$repl$Error__GT_map(o){
return cljs.core.Throwable__GT_map(o);
});
/**
 * Returns an analysis of the phase, error, cause, and location of an error that occurred
 *   based on Throwable data, as returned by Throwable->map. All attributes other than phase
 *   are optional:
 *  :clojure.error/phase - keyword phase indicator, one of:
 *    :read-source :compile-syntax-check :compilation :macro-syntax-check :macroexpansion
 *    :execution :read-eval-result :print-eval-result
 *  :clojure.error/source - file name (no path)
 *  :clojure.error/line - integer line number
 *  :clojure.error/column - integer column number
 *  :clojure.error/symbol - symbol being expanded/compiled/invoked
 *  :clojure.error/class - cause exception class symbol
 *  :clojure.error/cause - cause exception message
 *  :clojure.error/spec - explain-data for spec error
 */
cljs.repl.ex_triage = (function cljs$repl$ex_triage(datafied_throwable){
var map__35270 = datafied_throwable;
var map__35270__$1 = cljs.core.__destructure_map(map__35270);
var via = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35270__$1,new cljs.core.Keyword(null,"via","via",-1904457336));
var trace = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35270__$1,new cljs.core.Keyword(null,"trace","trace",-1082747415));
var phase = cljs.core.get.cljs$core$IFn$_invoke$arity$3(map__35270__$1,new cljs.core.Keyword(null,"phase","phase",575722892),new cljs.core.Keyword(null,"execution","execution",253283524));
var map__35277 = cljs.core.last(via);
var map__35277__$1 = cljs.core.__destructure_map(map__35277);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35277__$1,new cljs.core.Keyword(null,"type","type",1174270348));
var message = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35277__$1,new cljs.core.Keyword(null,"message","message",-406056002));
var data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35277__$1,new cljs.core.Keyword(null,"data","data",-232669377));
var map__35278 = data;
var map__35278__$1 = cljs.core.__destructure_map(map__35278);
var problems = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35278__$1,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814));
var fn = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35278__$1,new cljs.core.Keyword("cljs.spec.alpha","fn","cljs.spec.alpha/fn",408600443));
var caller = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35278__$1,new cljs.core.Keyword("cljs.spec.test.alpha","caller","cljs.spec.test.alpha/caller",-398302390));
var map__35279 = new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.first(via));
var map__35279__$1 = cljs.core.__destructure_map(map__35279);
var top_data = map__35279__$1;
var source = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35279__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3((function (){var G__35285 = phase;
var G__35285__$1 = (((G__35285 instanceof cljs.core.Keyword))?G__35285.fqn:null);
switch (G__35285__$1) {
case "read-source":
var map__35286 = data;
var map__35286__$1 = cljs.core.__destructure_map(map__35286);
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35286__$1,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471));
var column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35286__$1,new cljs.core.Keyword("clojure.error","column","clojure.error/column",304721553));
var G__35287 = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"data","data",-232669377).cljs$core$IFn$_invoke$arity$1(cljs.core.second(via)),top_data], 0));
var G__35287__$1 = (cljs.core.truth_(source)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35287,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),source):G__35287);
var G__35287__$2 = (cljs.core.truth_((function (){var fexpr__35288 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__35288.cljs$core$IFn$_invoke$arity$1 ? fexpr__35288.cljs$core$IFn$_invoke$arity$1(source) : fexpr__35288.call(null,source));
})())?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(G__35287__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397)):G__35287__$1);
if(cljs.core.truth_(message)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35287__$2,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message);
} else {
return G__35287__$2;
}

break;
case "compile-syntax-check":
case "compilation":
case "macro-syntax-check":
case "macroexpansion":
var G__35295 = top_data;
var G__35295__$1 = (cljs.core.truth_(source)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35295,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),source):G__35295);
var G__35295__$2 = (cljs.core.truth_((function (){var fexpr__35299 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__35299.cljs$core$IFn$_invoke$arity$1 ? fexpr__35299.cljs$core$IFn$_invoke$arity$1(source) : fexpr__35299.call(null,source));
})())?cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(G__35295__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397)):G__35295__$1);
var G__35295__$3 = (cljs.core.truth_(type)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35295__$2,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type):G__35295__$2);
var G__35295__$4 = (cljs.core.truth_(message)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35295__$3,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message):G__35295__$3);
if(cljs.core.truth_(problems)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35295__$4,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595),data);
} else {
return G__35295__$4;
}

break;
case "read-eval-result":
case "print-eval-result":
var vec__35301 = cljs.core.first(trace);
var source__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35301,(0),null);
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35301,(1),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35301,(2),null);
var line = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35301,(3),null);
var G__35309 = top_data;
var G__35309__$1 = (cljs.core.truth_(line)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35309,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471),line):G__35309);
var G__35309__$2 = (cljs.core.truth_(file)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35309__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),file):G__35309__$1);
var G__35309__$3 = (cljs.core.truth_((function (){var and__5160__auto__ = source__$1;
if(cljs.core.truth_(and__5160__auto__)){
return method;
} else {
return and__5160__auto__;
}
})())?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35309__$2,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[source__$1,method],null))):G__35309__$2);
var G__35309__$4 = (cljs.core.truth_(type)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35309__$3,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type):G__35309__$3);
if(cljs.core.truth_(message)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35309__$4,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message);
} else {
return G__35309__$4;
}

break;
case "execution":
var vec__35314 = cljs.core.first(trace);
var source__$1 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35314,(0),null);
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35314,(1),null);
var file = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35314,(2),null);
var line = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__35314,(3),null);
var file__$1 = cljs.core.first(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__35259_SHARP_){
var or__5162__auto__ = (p1__35259_SHARP_ == null);
if(or__5162__auto__){
return or__5162__auto__;
} else {
var fexpr__35323 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["NO_SOURCE_PATH",null,"NO_SOURCE_FILE",null], null), null);
return (fexpr__35323.cljs$core$IFn$_invoke$arity$1 ? fexpr__35323.cljs$core$IFn$_invoke$arity$1(p1__35259_SHARP_) : fexpr__35323.call(null,p1__35259_SHARP_));
}
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"file","file",-1269645878).cljs$core$IFn$_invoke$arity$1(caller),file], null)));
var err_line = (function (){var or__5162__auto__ = new cljs.core.Keyword(null,"line","line",212345235).cljs$core$IFn$_invoke$arity$1(caller);
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return line;
}
})();
var G__35324 = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890),type], null);
var G__35324__$1 = (cljs.core.truth_(err_line)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35324,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471),err_line):G__35324);
var G__35324__$2 = (cljs.core.truth_(message)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35324__$1,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742),message):G__35324__$1);
var G__35324__$3 = (cljs.core.truth_((function (){var or__5162__auto__ = fn;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
var and__5160__auto__ = source__$1;
if(cljs.core.truth_(and__5160__auto__)){
return method;
} else {
return and__5160__auto__;
}
}
})())?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35324__$2,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994),(function (){var or__5162__auto__ = fn;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return (new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[source__$1,method],null));
}
})()):G__35324__$2);
var G__35324__$4 = (cljs.core.truth_(file__$1)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35324__$3,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397),file__$1):G__35324__$3);
if(cljs.core.truth_(problems)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(G__35324__$4,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595),data);
} else {
return G__35324__$4;
}

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__35285__$1))));

}
})(),new cljs.core.Keyword("clojure.error","phase","clojure.error/phase",275140358),phase);
});
/**
 * Returns a string from exception data, as produced by ex-triage.
 *   The first line summarizes the exception phase and location.
 *   The subsequent lines describe the cause.
 */
cljs.repl.ex_str = (function cljs$repl$ex_str(p__35353){
var map__35355 = p__35353;
var map__35355__$1 = cljs.core.__destructure_map(map__35355);
var triage_data = map__35355__$1;
var phase = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","phase","clojure.error/phase",275140358));
var source = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","source","clojure.error/source",-2011936397));
var line = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","line","clojure.error/line",-1816287471));
var column = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","column","clojure.error/column",304721553));
var symbol = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","symbol","clojure.error/symbol",1544821994));
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","class","clojure.error/class",278435890));
var cause = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","cause","clojure.error/cause",-1879175742));
var spec = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__35355__$1,new cljs.core.Keyword("clojure.error","spec","clojure.error/spec",2055032595));
var loc = (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5162__auto__ = source;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return "<cljs repl>";
}
})())+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1((function (){var or__5162__auto__ = line;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return (1);
}
})())+cljs.core.str.cljs$core$IFn$_invoke$arity$1((cljs.core.truth_(column)?(""+":"+cljs.core.str.cljs$core$IFn$_invoke$arity$1(column)):"")));
var class_name = cljs.core.name((function (){var or__5162__auto__ = class$;
if(cljs.core.truth_(or__5162__auto__)){
return or__5162__auto__;
} else {
return "";
}
})());
var simple_class = class_name;
var cause_type = ((cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, ["RuntimeException",null,"Exception",null], null), null),simple_class))?"":(""+" ("+cljs.core.str.cljs$core$IFn$_invoke$arity$1(simple_class)+")"));
var format = goog.string.format;
var G__35366 = phase;
var G__35366__$1 = (((G__35366 instanceof cljs.core.Keyword))?G__35366.fqn:null);
switch (G__35366__$1) {
case "read-source":
return (format.cljs$core$IFn$_invoke$arity$3 ? format.cljs$core$IFn$_invoke$arity$3("Syntax error reading source at (%s).\n%s\n",loc,cause) : format.call(null,"Syntax error reading source at (%s).\n%s\n",loc,cause));

break;
case "macro-syntax-check":
var G__35371 = "Syntax error macroexpanding %sat (%s).\n%s";
var G__35372 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__35373 = loc;
var G__35374 = (cljs.core.truth_(spec)?(function (){var sb__5816__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR__orig_val__35379_35678 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR__orig_val__35380_35679 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_newline_STAR__temp_val__35381_35680 = true;
var _STAR_print_fn_STAR__temp_val__35382_35681 = (function (x__5817__auto__){
return sb__5816__auto__.append(x__5817__auto__);
});
(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35381_35680);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35382_35681);

try{cljs.spec.alpha.explain_out(cljs.core.update.cljs$core$IFn$_invoke$arity$3(spec,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814),(function (probs){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__35343_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__35343_SHARP_,new cljs.core.Keyword(null,"in","in",-1531184865));
}),probs);
}))
);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35380_35679);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35379_35678);
}
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__5816__auto__));
})():(format.cljs$core$IFn$_invoke$arity$2 ? format.cljs$core$IFn$_invoke$arity$2("%s\n",cause) : format.call(null,"%s\n",cause)));
return (format.cljs$core$IFn$_invoke$arity$4 ? format.cljs$core$IFn$_invoke$arity$4(G__35371,G__35372,G__35373,G__35374) : format.call(null,G__35371,G__35372,G__35373,G__35374));

break;
case "macroexpansion":
var G__35392 = "Unexpected error%s macroexpanding %sat (%s).\n%s\n";
var G__35393 = cause_type;
var G__35394 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__35395 = loc;
var G__35396 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__35392,G__35393,G__35394,G__35395,G__35396) : format.call(null,G__35392,G__35393,G__35394,G__35395,G__35396));

break;
case "compile-syntax-check":
var G__35398 = "Syntax error%s compiling %sat (%s).\n%s\n";
var G__35399 = cause_type;
var G__35400 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__35401 = loc;
var G__35402 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__35398,G__35399,G__35400,G__35401,G__35402) : format.call(null,G__35398,G__35399,G__35400,G__35401,G__35402));

break;
case "compilation":
var G__35404 = "Unexpected error%s compiling %sat (%s).\n%s\n";
var G__35405 = cause_type;
var G__35406 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__35407 = loc;
var G__35408 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__35404,G__35405,G__35406,G__35407,G__35408) : format.call(null,G__35404,G__35405,G__35406,G__35407,G__35408));

break;
case "read-eval-result":
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5("Error reading eval result%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause) : format.call(null,"Error reading eval result%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause));

break;
case "print-eval-result":
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5("Error printing return value%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause) : format.call(null,"Error printing return value%s at %s (%s).\n%s\n",cause_type,symbol,loc,cause));

break;
case "execution":
if(cljs.core.truth_(spec)){
var G__35410 = "Execution error - invalid arguments to %s at (%s).\n%s";
var G__35411 = symbol;
var G__35412 = loc;
var G__35413 = (function (){var sb__5816__auto__ = (new goog.string.StringBuffer());
var _STAR_print_newline_STAR__orig_val__35415_35699 = cljs.core._STAR_print_newline_STAR_;
var _STAR_print_fn_STAR__orig_val__35416_35700 = cljs.core._STAR_print_fn_STAR_;
var _STAR_print_newline_STAR__temp_val__35417_35701 = true;
var _STAR_print_fn_STAR__temp_val__35418_35702 = (function (x__5817__auto__){
return sb__5816__auto__.append(x__5817__auto__);
});
(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__temp_val__35417_35701);

(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__temp_val__35418_35702);

try{cljs.spec.alpha.explain_out(cljs.core.update.cljs$core$IFn$_invoke$arity$3(spec,new cljs.core.Keyword("cljs.spec.alpha","problems","cljs.spec.alpha/problems",447400814),(function (probs){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__35350_SHARP_){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(p1__35350_SHARP_,new cljs.core.Keyword(null,"in","in",-1531184865));
}),probs);
}))
);
}finally {(cljs.core._STAR_print_fn_STAR_ = _STAR_print_fn_STAR__orig_val__35416_35700);

(cljs.core._STAR_print_newline_STAR_ = _STAR_print_newline_STAR__orig_val__35415_35699);
}
return (""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(sb__5816__auto__));
})();
return (format.cljs$core$IFn$_invoke$arity$4 ? format.cljs$core$IFn$_invoke$arity$4(G__35410,G__35411,G__35412,G__35413) : format.call(null,G__35410,G__35411,G__35412,G__35413));
} else {
var G__35425 = "Execution error%s at %s(%s).\n%s\n";
var G__35426 = cause_type;
var G__35427 = (cljs.core.truth_(symbol)?(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(symbol)+" "):"");
var G__35428 = loc;
var G__35429 = cause;
return (format.cljs$core$IFn$_invoke$arity$5 ? format.cljs$core$IFn$_invoke$arity$5(G__35425,G__35426,G__35427,G__35428,G__35429) : format.call(null,G__35425,G__35426,G__35427,G__35428,G__35429));
}

break;
default:
throw (new Error((""+"No matching clause: "+cljs.core.str.cljs$core$IFn$_invoke$arity$1(G__35366__$1))));

}
});
cljs.repl.error__GT_str = (function cljs$repl$error__GT_str(error){
return cljs.repl.ex_str(cljs.repl.ex_triage(cljs.repl.Error__GT_map(error)));
});

//# sourceMappingURL=cljs.repl.js.map
