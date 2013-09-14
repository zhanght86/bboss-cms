var $ = function (d){ 
typeof d == "string" &&(d = document.getElementById(d)); 
return $.fn.call(d); 
}; 
$.fn = function (){ 
this.addEvent = function (sEventType,fnHandler){ 
if (this.addEventListener) {this.addEventListener(sEventType, fnHandler, false);} 
else if (this.attachEvent) {this.attachEvent("on" + sEventType, fnHandler);} 
else {this["on" + sEventType] = fnHandler;} 
} 
this.removeEvent = function (sEventType,fnHandler){ 
if (this.removeEventListener) {this.removeEventListener(sEventType, fnHandler, false);} 
else if (this.detachEvent) {this.detachEvent("on" + sEventType, fnHandler);} 
else { this["on" + sEventType] = null;} 
} 
return this; 
}; 
var Class = {create: function() {return function() { this.initialize.apply(this, arguments); }}}; 
var Bind = function (obj,fun,arr){return function() {return fun.apply(obj,arr);}} 
var Marquee = Class.create(); 
Marquee.prototype = { 
initialize: function(id,name,out,speed) { 
this.name = name; 
this.box = $(id); 
this.out = 3;//滚动间隔时间,单位秒 
this.speed = speed; 
this.d = 1; 
this.box.style.position = "relative"; 
this.box.scrollTop = 0; 
var _li = this.box.firstChild; 
while(typeof(_li.tagName)=="undefined")_li = _li.nextSibling; 
this.lis = this.box.getElementsByTagName(_li.tagName); 
this.len = this.lis.length; 
for(var i=0;i<this.lis.length;i++){ 
var __li = document.createElement(_li.tagName); 
__li.innerHTML = this.lis[i].innerHTML; 
this.box.appendChild(__li);//cloneNode 
if(this.lis[i].offsetTop>=this.box.offsetHeight)break; 
} 
this.Start(); 
this.box.addEvent("mouseover",Bind(this,function(){clearTimeout(this.timeout);},[])); 
this.box.addEvent("mouseout",Bind(this,this.Start,[])); 
}, 
Start:function (){ 
clearTimeout(this.timeout); 
this.timeout = setTimeout(this.name+".Up()",this.out*1000) 
}, 
Up:function(){ 
clearInterval(this.interval); 
this.interval = setInterval(this.name+".Fun()",10); 
}, 
Fun:function (){ 
this.box.scrollTop+=this.speed; 
if(this.lis[this.d].offsetTop <= this.box.scrollTop){ 
clearInterval(this.interval); 
this.box.scrollTop = this.lis[this.d].offsetTop; 
this.Start(); 
this.d++; 
} 
if(this.d >= this.len + 1){ 
this.d = 1; 
this.box.scrollTop = 0; 
} 
} 
}; 
$(window).addEvent("load",function (){ 
marquee = new Marquee("msg_weibo","marquee",1,2); 
}); 
