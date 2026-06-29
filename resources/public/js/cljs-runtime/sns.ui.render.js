goog.provide('sns.ui.render');
sns.ui.render.field = (function sns$ui$render$field(inputs,p__38087){
var map__38088 = p__38087;
var map__38088__$1 = cljs.core.__destructure_map(map__38088);
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38088__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38088__$1,new cljs.core.Keyword(null,"label","label",1718410804));
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38088__$1,new cljs.core.Keyword(null,"type","type",1174270348));
var options = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38088__$1,new cljs.core.Keyword(null,"options","options",99638489));
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(inputs,id);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"label.field","label.field",1192833224),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("replicant","key","replicant/key",-670108117),id], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.field__label","span.field__label",1857217546),label], null),(function (){var G__38093 = type;
var G__38093__$1 = (((G__38093 instanceof cljs.core.Keyword))?G__38093.fqn:null);
switch (G__38093__$1) {
case "enum":
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"select.field__control","select.field__control",138601022),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"change","change",-1163046502),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","set-input","ui/set-input",212729820),id,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("event.target","value","event.target/value",857030372)], null)], null)], null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"value","value",305978217),""], null),"\u2014"], null),(function (){var iter__5649__auto__ = (function sns$ui$render$field_$_iter__38094(s__38095){
return (new cljs.core.LazySeq(null,(function (){
var s__38095__$1 = s__38095;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__38095__$1);
if(temp__5823__auto__){
var s__38095__$2 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38095__$2)){
var c__5647__auto__ = cljs.core.chunk_first(s__38095__$2);
var size__5648__auto__ = cljs.core.count(c__5647__auto__);
var b__38097 = cljs.core.chunk_buffer(size__5648__auto__);
if((function (){var i__38096 = (0);
while(true){
if((i__38096 < size__5648__auto__)){
var opt = cljs.core._nth(c__5647__auto__,i__38096);
cljs.core.chunk_append(b__38097,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(opt)),new cljs.core.Keyword(null,"selected","selected",574897764),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(opt)),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)))], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(opt))], null));

var G__38176 = (i__38096 + (1));
i__38096 = G__38176;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38097),sns$ui$render$field_$_iter__38094(cljs.core.chunk_rest(s__38095__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38097),null);
}
} else {
var opt = cljs.core.first(s__38095__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"option","option",65132272),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"value","value",305978217),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(opt)),new cljs.core.Keyword(null,"selected","selected",574897764),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(opt)),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)))], null),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(opt))], null),sns$ui$render$field_$_iter__38094(cljs.core.rest(s__38095__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5649__auto__(options);
})()], null);

break;
case "bool":
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.field__control","input.field__control",1864723275),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"checkbox",new cljs.core.Keyword(null,"checked","checked",-50955819),cljs.core.boolean$(value),new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"change","change",-1163046502),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","set-input","ui/set-input",212729820),id,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("event.target","checked","event.target/checked",1556027292)], null)], null)], null)], null)], null)], null);

break;
case "int":
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.field__control","input.field__control",1864723275),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"number",new cljs.core.Keyword(null,"value","value",305978217),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)),new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","set-input","ui/set-input",212729820),id,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("event.target","value","event.target/value",857030372)], null)], null)], null)], null)], null)], null);

break;
default:
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input.field__control","input.field__control",1864723275),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"type","type",1174270348),"text",new cljs.core.Keyword(null,"value","value",305978217),(""+cljs.core.str.cljs$core$IFn$_invoke$arity$1(value)),new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","set-input","ui/set-input",212729820),id,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("event.target","value","event.target/value",857030372)], null)], null)], null)], null)], null)], null);

}
})()], null);
});
/**
 * Render a spec's declared inputs, or nil when there are none.
 */
sns.ui.render.input_form = (function sns$ui$render$input_form(spec,inputs){
if(cljs.core.seq(new cljs.core.Keyword(null,"inputs","inputs",865803858).cljs$core$IFn$_invoke$arity$1(spec))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.fields","div.fields",1362682190),(function (){var iter__5649__auto__ = (function sns$ui$render$input_form_$_iter__38109(s__38110){
return (new cljs.core.LazySeq(null,(function (){
var s__38110__$1 = s__38110;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__38110__$1);
if(temp__5823__auto__){
var s__38110__$2 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38110__$2)){
var c__5647__auto__ = cljs.core.chunk_first(s__38110__$2);
var size__5648__auto__ = cljs.core.count(c__5647__auto__);
var b__38112 = cljs.core.chunk_buffer(size__5648__auto__);
if((function (){var i__38111 = (0);
while(true){
if((i__38111 < size__5648__auto__)){
var f = cljs.core._nth(c__5647__auto__,i__38111);
cljs.core.chunk_append(b__38112,sns.ui.render.field(inputs,f));

var G__38187 = (i__38111 + (1));
i__38111 = G__38187;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38112),sns$ui$render$input_form_$_iter__38109(cljs.core.chunk_rest(s__38110__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38112),null);
}
} else {
var f = cljs.core.first(s__38110__$2);
return cljs.core.cons(sns.ui.render.field(inputs,f),sns$ui$render$input_form_$_iter__38109(cljs.core.rest(s__38110__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5649__auto__(new cljs.core.Keyword(null,"inputs","inputs",865803858).cljs$core$IFn$_invoke$arity$1(spec));
})()], null);
} else {
return null;
}
});
sns.ui.render.entry = (function sns$ui$render$entry(p__38119){
var map__38120 = p__38119;
var map__38120__$1 = cljs.core.__destructure_map(map__38120);
var title = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38120__$1,new cljs.core.Keyword("item","title","item/title",629068986));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38120__$1,new cljs.core.Keyword("item","body","item/body",-2045631126));
var tags = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38120__$1,new cljs.core.Keyword("item","tags","item/tags",1765816438));
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.entry","li.entry",-1021637316),(cljs.core.truth_(title)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h4.entry__title","h4.entry__title",-1982405945),title], null):null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.entry__body","p.entry__body",-1885538879),body], null),((cljs.core.seq(tags))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul.tags","ul.tags",845253255),(function (){var iter__5649__auto__ = (function sns$ui$render$entry_$_iter__38121(s__38122){
return (new cljs.core.LazySeq(null,(function (){
var s__38122__$1 = s__38122;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__38122__$1);
if(temp__5823__auto__){
var s__38122__$2 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38122__$2)){
var c__5647__auto__ = cljs.core.chunk_first(s__38122__$2);
var size__5648__auto__ = cljs.core.count(c__5647__auto__);
var b__38124 = cljs.core.chunk_buffer(size__5648__auto__);
if((function (){var i__38123 = (0);
while(true){
if((i__38123 < size__5648__auto__)){
var t = cljs.core._nth(c__5647__auto__,i__38123);
cljs.core.chunk_append(b__38124,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.tag","li.tag",1173452254),t], null));

var G__38191 = (i__38123 + (1));
i__38123 = G__38191;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38124),sns$ui$render$entry_$_iter__38121(cljs.core.chunk_rest(s__38122__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38124),null);
}
} else {
var t = cljs.core.first(s__38122__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li.tag","li.tag",1173452254),t], null),sns$ui$render$entry_$_iter__38121(cljs.core.rest(s__38122__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5649__auto__(tags);
})()], null):null)], null);
});
sns.ui.render.block = (function sns$ui$render$block(p__38137){
var map__38138 = p__38137;
var map__38138__$1 = cljs.core.__destructure_map(map__38138);
var heading = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38138__$1,new cljs.core.Keyword("section","heading","section/heading",1025136322));
var items = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38138__$1,new cljs.core.Keyword("section","items","section/items",-1343035275));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"section.block","section.block",1605196151),(cljs.core.truth_(heading)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h3.block__heading","h3.block__heading",1514918558),heading], null):null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul.entries","ul.entries",2053654715),cljs.core.map.cljs$core$IFn$_invoke$arity$2(sns.ui.render.entry,items)], null)], null);
});
sns.ui.render.action = (function sns$ui$render$action(p__38148){
var map__38149 = p__38148;
var map__38149__$1 = cljs.core.__destructure_map(map__38149);
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38149__$1,new cljs.core.Keyword("action","label","action/label",827483238));
var event = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38149__$1,new cljs.core.Keyword("action","event","action/event",-985134116));
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.action","button.action",-1043526280),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click","click",1912301393),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [event], null)], null)], null),label], null);
});
/**
 * Render a view-model. The whole article is keyed on the title so a fresh loot
 * result re-mounts and replays the materialise animation.
 */
sns.ui.render.result = (function sns$ui$render$result(vm){
if(cljs.core.truth_(vm)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"article.sigil","article.sigil",-1404489086),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("replicant","key","replicant/key",-670108117),new cljs.core.Keyword("loot","title","loot/title",628983895).cljs$core$IFn$_invoke$arity$1(vm)], null),new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sigil__frame","div.sigil__frame",1804531951),(cljs.core.truth_(new cljs.core.Keyword("loot","subtitle","loot/subtitle",-1619426499).cljs$core$IFn$_invoke$arity$1(vm))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.sigil__eyebrow","p.sigil__eyebrow",1285391448),new cljs.core.Keyword("loot","subtitle","loot/subtitle",-1619426499).cljs$core$IFn$_invoke$arity$1(vm)], null):null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h2.sigil__title","h2.sigil__title",885177002),new cljs.core.Keyword("loot","title","loot/title",628983895).cljs$core$IFn$_invoke$arity$1(vm)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sigil__body","div.sigil__body",413410272),cljs.core.map.cljs$core$IFn$_invoke$arity$2(sns.ui.render.block,new cljs.core.Keyword("loot","sections","loot/sections",-891607618).cljs$core$IFn$_invoke$arity$1(vm))], null),((cljs.core.seq(new cljs.core.Keyword("loot","actions","loot/actions",-809295898).cljs$core$IFn$_invoke$arity$1(vm)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.sigil__actions","div.sigil__actions",818988531),cljs.core.map.cljs$core$IFn$_invoke$arity$2(sns.ui.render.action,new cljs.core.Keyword("loot","actions","loot/actions",-809295898).cljs$core$IFn$_invoke$arity$1(vm))], null):null)], null)], null);
} else {
return null;
}
});
sns.ui.render.picker = (function sns$ui$render$picker(p__38159){
var map__38160 = p__38159;
var map__38160__$1 = cljs.core.__destructure_map(map__38160);
var loot_types = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38160__$1,new cljs.core.Keyword(null,"loot-types","loot-types",417124260));
var selected = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38160__$1,new cljs.core.Keyword(null,"selected","selected",574897764));
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"nav.rail","nav.rail",-467602403),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"p.rail__eyebrow","p.rail__eyebrow",1466953641),"Disciplines"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul.rail__list","ul.rail__list",1201841065),(function (){var iter__5649__auto__ = (function sns$ui$render$picker_$_iter__38162(s__38163){
return (new cljs.core.LazySeq(null,(function (){
var s__38163__$1 = s__38163;
while(true){
var temp__5823__auto__ = cljs.core.seq(s__38163__$1);
if(temp__5823__auto__){
var s__38163__$2 = temp__5823__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38163__$2)){
var c__5647__auto__ = cljs.core.chunk_first(s__38163__$2);
var size__5648__auto__ = cljs.core.count(c__5647__auto__);
var b__38165 = cljs.core.chunk_buffer(size__5648__auto__);
if((function (){var i__38164 = (0);
while(true){
if((i__38164 < size__5648__auto__)){
var map__38168 = cljs.core._nth(c__5647__auto__,i__38164);
var map__38168__$1 = cljs.core.__destructure_map(map__38168);
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38168__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38168__$1,new cljs.core.Keyword(null,"label","label",1718410804));
cljs.core.chunk_append(b__38165,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("replicant","key","replicant/key",-670108117),id], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.discipline","button.discipline",176214194),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(id,selected))?"discipline--active":null),new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click","click",1912301393),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","select-type","ui/select-type",-1022679010),id], null)], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.discipline__glyph","span.discipline__glyph",-2033260036),"\u25C6"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.discipline__name","span.discipline__name",-2124665569),label], null)], null)], null));

var G__38200 = (i__38164 + (1));
i__38164 = G__38200;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38165),sns$ui$render$picker_$_iter__38162(cljs.core.chunk_rest(s__38163__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38165),null);
}
} else {
var map__38170 = cljs.core.first(s__38163__$2);
var map__38170__$1 = cljs.core.__destructure_map(map__38170);
var id = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38170__$1,new cljs.core.Keyword(null,"id","id",-1388402092));
var label = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38170__$1,new cljs.core.Keyword(null,"label","label",1718410804));
return cljs.core.cons(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"li","li",723558921),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("replicant","key","replicant/key",-670108117),id], null),new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.discipline","button.discipline",176214194),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(id,selected))?"discipline--active":null),new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click","click",1912301393),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","select-type","ui/select-type",-1022679010),id], null)], null)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.discipline__glyph","span.discipline__glyph",-2033260036),"\u25C6"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.discipline__name","span.discipline__name",-2124665569),label], null)], null)], null),sns$ui$render$picker_$_iter__38162(cljs.core.rest(s__38163__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__5649__auto__(loot_types);
})()], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.roll","button.roll",-1446477135),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"on","on",173873944),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"click","click",1912301393),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("ui","roll","ui/roll",11270435)], null)], null)], null)], null),"Roll the hoard"], null)], null);
});

//# sourceMappingURL=sns.ui.render.js.map
