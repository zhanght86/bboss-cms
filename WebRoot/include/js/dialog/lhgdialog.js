/*
 */
(function(h,m,j){var b=!!m.ActiveXObject,n=b&&!m.XMLHttpRequest,w=function(){},A=0,x=/^url:/,q,f,u="JDG"+(new Date).getTime(),r=m.document
/*
 * _path 获取组件核心文件lhgdialog.js所在的绝对路径
 * _args 获取lhgdialog.js文件后的url参数组，如：lhgdialog.js?self=true&skin=aero中的?后面的内容
 */
,t,g=(function(C,D,E){var e=C.length;for(;D<e;D++){E=!!r.querySelector?C[D].src:C[D].getAttribute("src",4);if(E.substr(E.lastIndexOf("/")).indexOf("lhgdialog")!==-1){break}}E=E.split("?");t=E[1];return E[0].substr(0,E[0].lastIndexOf("/")+1)})(r.getElementsByTagName("script"),0)
/*
 * 获取url参数值函数
 * @param  {String}
 * @return {String||null}
 * @demo lhgdialog.js?skin=aero | _getArgs('skin') => 'aero'
 */
,p=function(D){if(t){var F=t.split("&"),E=0,C=F.length,e;for(;E<C;E++){e=F[E].split("=");if(D===e[0]){return e[1]}}}return null}
/* 取皮肤样式名，默认为 default */
,l=p("skin")||"chrome"
/* 获取 lhgdialog 可跨级调用的最高层的 window 对象和 document 对象 */
,c,s=(function(C){try{c=C.top.document;c.getElementsByTagName}catch(D){c=C.document;return C}if(p("self")==="true"||c.getElementsByTagName("frameset").length>0){c=C.document;return C}return C.top})(m),k=c.documentElement,B=h(c),o=h(s),i=h(c.getElementsByTagName("html")[0]);if(c.compatMode==="BackCompat"){alert("\u9519\u8BEF: \u9875\u9762\u6587\u6863\u7C7B\u578B(DOCTYPE)\u672A\u58F0\u660E!");return;
/* 开启IE6 CSS背景图片缓存 */
}try{c.execCommand("BackgroundImageCache",false,true)}catch(y){}
/* 在最顶层页面添加样式文件 */
(function(C){if(!C){var e=c.getElementsByTagName("head")[0],D=c.createElement("link");D.href=g+"skins/"+l+".css";D.rel="stylesheet";D.id="lhgdglink";e.insertBefore(D,e.firstChild)}})(c.getElementById("lhgdglink"));if(!m.top.global_top_zIndex){m.top.global_top_zIndex=1000;
/*
 * lhgdialog 核心代码部分
 */
}var a=function(e,D,G){e=e||{};if(typeof e==="string"){e={content:e}}var F,E=a.setting;for(var C in E){if(e[C]===j){e[C]=E[C]}}e.id=e.id||u+A;F=a.list[e.id];if(F){return F.zindex().focus()}e.button=e.button||[];e.ok=D||e.ok;e.cancel=G||e.cancel;e.ok&&e.button.push({name:e.okVal,callback:e.ok,focus:true});e.cancel&&e.button.push({name:e.cancelVal,callback:e.cancel});a.setting.zIndex=e.zIndex;A++;return a.list[e.id]=q?q._init(e):new a.fn._init(e)};a.fn=a.prototype={constructor:a,_init:function(C){var G=this,F,e=C.content,E=x.test(e),D=C.icon;G.config=C;G.DOM=F=G.DOM||G._getDOM();G.opener=m;if(D&&!E){C.min=false;C.max=false;F.icon[0].style.display="";F.icon_bg[0].src=C.path+"skins/icons/"+D}else{F.icon[0].style.display="none"}F.wrap[0].style.display="block";F.wrap.addClass(C.skin);F.rb[0].style.cursor=C.resize?"se-resize":"auto";F.title[0].style.cursor=C.drag?"move":"auto";F.max[0].style.display=C.max?"inline-block":"none";F.min[0].style.display=C.min?"inline-block":"none";F.close[0].style.display=C.cancel===false?"none":"inline-block";F.content[0].style.padding=C.padding;G.title(C.title).content(e,true,E).button(C.button).size(C.width,C.height).position(C.left,C.top).time(C.time)[C.show?"show":"hide"](true).zindex();C.lock&&G.lock();G._addEvent();G._ie6PngFix();q=null;if(!E&&C.init){C.init.call(G,m)}if(C.maxState){G.max()}return G},title:function(F){if(F===j){return this}var D=this.DOM,e=D.border,E=D.title[0],C="ui_state_tips";if(F===false){E.style.display="none";E.innerHTML="";e.addClass(C)}else{E.style.display="";E.innerHTML=F;e.removeClass(C)}return this},
/*
	 * 设置内容
	 * @param	{String}	内容 (如果内容前3个字符为‘url:’就加载单独页面的内容页)
	 * @param   {Boolean}   是否为后增加的内容
	 * @param   {Boolean}   是否使用iframe方式加载内容页
	 * @return	{this}		如果无参数则返回对象本身
	 */
content:function(E,M,J){if(E===j){return this}var H=this,N=H.DOM,C=N.wrap[0],D=C.offsetWidth,L=C.offsetHeight,F=parseInt(C.style.left),I=parseInt(C.style.top),K=C.style.width,e=N.content,G=a.setting.content;if(J){e[0].innerHTML=G;H._iframe(E.split("url:")[1])}else{e.html(E)}if(!M){D=C.offsetWidth-D;L=C.offsetHeight-L;F=F-D/2;I=I-L/2;C.style.left=Math.max(F,0)+"px";C.style.top=Math.max(I,0)+"px";if(K&&K!=="auto"){C.style.width=C.offsetWidth+"px"}H._autoPositionType()}H._ie6SelectFix();return H},button:function(){var D=this,G=arguments,C=D.DOM.buttons[0],F="ui_state_highlight",e=D._listeners=D._listeners||{},E=h.isArray(G[0])?G[0]:[].slice.call(G);h.each(E,function(J,K){var H=K.name,L=!e[H],I=!L?e[H].elem:c.createElement("input");if(!e[H]){e[H]={}}if(K.callback){e[H].callback=K.callback}if(K.focus){D._focus&&D._focus.removeClass(F);D._focus=h(I).addClass(F);D.focus()}I[u+"callback"]=H;I.disabled=!!K.disabled;if(L){I.type="button";I.value=H;e[H].elem=I;C.appendChild(I)}});C.style.display=E.length?"":"none";D._ie6SelectFix();return D},size:function(E,e){var H=this,G=H.DOM,D=G.wrap[0],F=D.style,C=G.main[0].style;if(E){F.width="auto";if(typeof E==="number"){C.width=E+"px"}else{if(typeof E==="string"){C.width=E}}if(E!=="auto"){F.width=D.offsetWidth+"px"}}if(e){if(typeof e==="number"){C.height=e+"px"}else{if(typeof e==="string"){C.height=e}}}H._ie6SelectFix();return H},position:function(I,O){var N=this,G=N.config,D=N.DOM.wrap[0],J=n?false:G.fixed,M=n&&G.fixed,H=B.scrollLeft(),Q=B.scrollTop(),L=J?0:H,E=J?0:Q,K=o.width(),C=o.height(),F=D.offsetWidth,P=D.offsetHeight,e=D.style;if(I||I===0){N._left=I.toString().indexOf("%")!==-1?I:null;I=N._toNumber(I,K-F);if(typeof I==="number"){I=M?(I+=H):I+L;e.left=Math.max(I,L)+"px"}else{if(typeof I==="string"){e.left=I}}}if(O||O===0){N._top=O.toString().indexOf("%")!==-1?O:null;if(C!=j){O=N._toNumber(O,C-P)}if(typeof O==="number"){O=M?(O+=Q):O+E;e.top=Math.max(O,E)+"px"}else{if(typeof O==="string"){e.top=O}}}if(I!==j&&O!==j){N._autoPositionType()}return N},
/* 显示对话框 */
show:function(e){this.DOM.wrap[0].style.visibility="visible";if(!e&&this._lock){h("#ldg_lockmask",c)[0].style.display=""}return this},
/* 隐藏对话框 */
hide:function(e){this.DOM.wrap[0].style.visibility="hidden";if(!e&&this._lock){h("#ldg_lockmask",c)[0].style.display="none"}return this},
/* 关闭对话框 */
close:function(){var F=this,E=F.DOM,D=E.wrap,G=a.list,C=F.config.close;F.time();if(F.iframe){if(typeof C==="function"&&C.call(F,F.iframe.contentWindow,m)===false){return F}h(F.iframe).unbind("load",F._fmLoad).attr("src","about:blank").remove();E.content.removeClass("ui_state_full");if(F._frmTimer){clearTimeout(F._frmTimer)}}else{if(typeof C==="function"&&C.call(F,m)===false){return F}}F.unlock();if(F._minState){E.main[0].style.display="";E.buttons[0].style.display="";E.dialog[0].style.width=""}if(F._maxState){i.removeClass("ui_lock_scroll");E.res[0].style.display="none"}D[0].className=E.wrap[0].style.cssText="";D[0].style.display="none";E.border.removeClass("ui_state_focus");E.title[0].innerHTML="";E.content.html("");E.buttons[0].innerHTML="";if(a.focus===F){a.focus=null}delete G[F.config.id];F._removeEvent();F.hide(true)._setAbsolute();for(var e in F){if(F.hasOwnProperty(e)&&e!=="DOM"){delete F[e]}}q?D.remove():q=F;return F},
/*
	 * 定时关闭
	 * @param	{Number}	单位为秒, 无参数则停止计时器
	 * @param   {Function}  关闭窗口前执行的回调函数
	 */
time:function(e,F){var D=this,C=D.config.cancelVal,E=D._timer;E&&clearTimeout(E);if(F){F.call(D)}if(e){D._timer=setTimeout(function(){D._click(C)},1000*e)}return D},reload:function(E,C){E=E||m;try{E.location.href=C?C:E.location.href}catch(D){C=this.iframe.src;h(this.iframe).attr("src",C)}return this},
/* 置顶对话框 */
zindex:function(){var C=this,e=C.DOM,D=C._load,E=a.focus;if(m.top.global_top_zIndex){index=m.top.global_top_zIndex++}else{index=m.top.global_top_zIndex=1000}e.wrap[0].style.zIndex=index;E&&E.DOM.border.removeClass("ui_state_focus");a.focus=C;e.border.addClass("ui_state_focus");if(D&&D.style.zIndex){D.style.display="none"}if(E&&E!==C&&E.iframe){E._load.style.display=""}return C},
/* 设置焦点 */
focus:function(){try{elemFocus=this._focus&&this._focus[0]||this.DOM.close[0];elemFocus&&elemFocus.focus()}catch(C){}return this},
/* 锁屏 */
lock:function(){var F=this,G,D=m.top.global_top_zIndex-1,C=F.config,e=h("#ldg_lockmask",c)[0],E=e?e.style:"",H=n?"absolute":"fixed";if(!e){G='<iframe src="about:blank" style="width:100%;height:100%;position:absolute;top:0;left:0;z-index:-1;filter:alpha(opacity=0)"></iframe>';e=c.createElement("div");e.id="ldg_lockmask";e.style.cssText="position:"+H+";left:0;top:0;width:100%;height:100%;overflow:hidden;background:"+C.background+";";h(e).css({opacity:C.opacity});E=e.style;if(n){e.innerHTML=G}c.body.appendChild(e)}if(H==="absolute"){E.width=o.width();E.height=o.height();E.top=B.scrollTop();E.left=B.scrollLeft();F._setFixed(e)}E.zIndex=D;E.display="";F.zindex();F.DOM.border.addClass("ui_state_lock");F._lock=true;return F},
/* 解除锁屏 */
unlock:function(){var E=this,D=E.config,e=h("#ldg_lockmask",c)[0];if(e&&E._lock){if(D.parent){var C=D.parent.DOM.wrap[0].style.zIndex;if(C!=0){e.style.zIndex=parseInt(C,10)-1}}else{e.style.display="none"}E.DOM.border.removeClass("ui_state_lock")}return E},
/* 最大化窗口 */
max:function(){if(!this.config.max){return this}var E=this,J,K=E.DOM,G=K.wrap[0].style,C=K.main[0].style,I=K.rb[0].style,H=K.title[0].style,e=E.config,F=B.scrollTop(),D=B.scrollLeft();if(!E._maxState){i.addClass("ui_lock_scroll");if(E._minState){E.min()}E._or={t:G.top,l:G.left,w:C.width,h:C.height,d:e.drag,r:e.resize,rc:I.cursor,tc:H.cursor};G.top=F+"px";G.left=D+"px";J=E._maxSize();E.size(J.w,J.h)._setAbsolute();e.drag=false;e.resize=false;I.cursor="auto";H.cursor="auto";K.max[0].style.display="none";K.res[0].style.display="inline-block";E._maxState=true}else{i.removeClass("ui_lock_scroll");G.top=E._or.t;G.left=E._or.l;E.size(E._or.w,E._or.h)._autoPositionType();e.drag=E._or.d;e.resize=E._or.r;I.cursor=E._or.rc;H.cursor=E._or.tc;K.res[0].style.display="none";K.max[0].style.display="inline-block";delete E._or;E._maxState=false}return E},
/* 计算最大化窗口时窗口的尺寸 */
_maxSize:function(){var G=this,F=G.DOM,C=F.wrap[0],e=F.main[0],E,D;E=o.width()-C.offsetWidth+e.offsetWidth;D=o.height()-C.offsetHeight+e.offsetHeight;return{w:E,h:D}},
/* 最小化窗口 */
min:function(){if(!this.config.min){return this}var G=this,F=G.DOM,e=F.main[0].style,E=F.buttons[0].style,D=F.dialog[0].style,H=F.rb[0].style.cursor,C=G.config.resize;if(!G._minState){if(G._maxState){G.max()}G._minRz={rzs:C,btn:E.display};e.display="none";E.display="none";D.width=e.width;H.cursor="auto";C=false;G._minState=true}else{e.display="";E.display=G._minRz.btn;D.width="";C=G._minRz;H.cursor=G._minRz.rzs?"se-resize":"auto";delete G._minRz;G._minState=false}G._ie6SelectFix();return G},
/*
	 * 获取指定id的窗口对象或窗口中iframe加载的内容页的window对象
	 * @param {String} 指定的id
	 * @param {String} 是否返回的为指定id的窗口对象
	 *        用数字1来表示真，如果不写或写其它为false
	 * @return {Object|null}
	 */
get:function(C,e){if(a.list[C]){if(e===1){return a.list[C]}else{return a.list[C].iwin||null}}return null},
/*
	 * 设置iframe方式加载内容页
	 */
_iframe:function(D){var L=this,K,R,F,I,J,E,e,C=L.DOM.content,G=L.config,H=L._load=h(".ui_loading",C[0])[0],P="position:absolute;left:-9999em;border:none 0;background:transparent",O="width:100%;height:100%;border:none 0;";if(G.cache===false){var N=(new Date).getTime(),M=D.replace(/([?&])_=[^&]*/,"$1_="+N);D=M+((M===D)?(/\?/.test(D)?"&":"?")+"_="+N:"")}K=L.iframe=c.createElement("iframe");K.name=G.id;K.style.cssText=P;K.setAttribute("frameborder",0,0);R=h(K);C[0].appendChild(K);L._frmTimer=setTimeout(function(){R.attr("src",D)},1);var Q=L._fmLoad=function(){C.addClass("ui_state_full");var U=L.DOM,W,T=U.lt[0].offsetHeight,S=U.main[0].style;H.style.cssText="display:none;position:absolute;background:#FFF;opacity:0;filter:alpha(opacity=0);z-index:1;width:"+S.width+";height:"+S.height+";";try{F=L.iwin=K.contentWindow;I=h(F.document);J=h(F.document.body)}catch(V){K.style.cssText=O;return}E=G.width==="auto"?I.width()+(n?0:parseInt(J.css("marginLeft"))):G.width;e=G.height==="auto"?I.height():G.height;setTimeout(function(){K.style.cssText=O},0);if(!L._maxState){L.size(E,e).position(G.left,G.top)}H.style.width=S.width;H.style.height=S.height;G.init&&G.init.call(L,F,s)};L.iframe.api=L;R.bind("load",Q)},
/* 获取窗口元素 */
_getDOM:function(){var G=c.createElement("div"),C=c.body;G.style.cssText="position:absolute;visibility:hidden;";G.innerHTML=a.templates;C.insertBefore(G,C.firstChild);var D,F=0,H={wrap:h(G)},E=G.getElementsByTagName("*"),e=E.length;for(;F<e;F++){D=E[F].className.split("ui_")[1];if(D){H[D]=h(E[F])}}return H},
/*
	 * px与%单位转换成数值 (百分比单位按照最大值换算)
	 * 其他的单位返回原值
	 */
_toNumber:function(e,C){if(!e&&e!==0||typeof e==="number"){return e}if(e.indexOf("%")!==-1){e=parseInt(C*e.split("%")[0]/100)}return e},
/* 让IE6 CSS支持PNG背景 */
_ie6PngFix:n?function(){var C=0,E,H,D,e,G=a.setting.path+"/skins/",F=this.DOM.wrap[0].getElementsByTagName("*");for(;C<F.length;C++){E=F[C];H=E.currentStyle.png;if(H){D=G+H;e=E.runtimeStyle;e.backgroundImage="none";e.filter="progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+D+"',sizingMethod='scale')"}}}:w,
/* 强制覆盖IE6下拉控件 */
_ie6SelectFix:n?function(){var C=this.DOM.wrap,F=C[0],G=u+"iframeMask",E=C[G],D=F.offsetWidth,e=F.offsetHeight;D=D+"px";e=e+"px";if(E){E.style.width=D;E.style.height=e}else{E=F.appendChild(c.createElement("iframe"));C[G]=E;E.src="about:blank";E.style.cssText="position:absolute;z-index:-1;left:0;top:0;filter:alpha(opacity=0);width:"+D+";height:"+e}}:w,
/* 自动切换定位类型 */
_autoPositionType:function(){this[this.config.fixed?"_setFixed":"_setAbsolute"]()},
/* 设置静止定位
	 * IE6 Fixed @see: http://www.planeart.cn/?p=877
	 */
_setFixed:function(F){var E=F?F.style:this.DOM.wrap[0].style;if(n){var D=B.scrollLeft(),C=B.scrollTop(),H=parseInt(E.left)-D,G=parseInt(E.top)-C,e="this.ownerDocument.documentElement";this._setAbsolute();E.setExpression("left",e+".scrollLeft +"+H);E.setExpression("top",e+".scrollTop +"+G)}else{E.position="fixed"}},
/* 设置绝对定位 */
_setAbsolute:function(){var e=this.DOM.wrap[0].style;if(n){e.removeExpression("left");e.removeExpression("top")}e.position="absolute"},
/* 按钮回调函数触发 */
_click:function(e){var D=this,C=D._listeners[e]&&D._listeners[e].callback;return typeof C!=="function"||C.call(D,m)!==false?D.close():D},
/* 重置位置与尺寸 */
_reset:function(I){var G,F=this,E=o.width(),H=o.height(),e=F._winSize||E*H,D=F._lockDocW||E,C=F._left,J=F._top;if(I){if(F._lock&&n){h("#ldg_lockmask",c).css({width:E+"px",height:H+"px"})}newWidth=F._lockDocW=E;G=F._winSize=E*H;if(e===G){return}}if(F._maxState){var K=F._maxSize();F.size(K.w,K.h)}if(I&&Math.abs(D-newWidth)===17){return}if(C||J){F.position(C,J)}},
/* 事件代理 */
_addEvent:function(){var e,E=this,C=E.config,D=E.DOM;E._winResize=function(){e&&clearTimeout(e);e=setTimeout(function(){E._reset(b)},140)};o.bind("resize",E._winResize);D.wrap.bind("click",function(G){var H=G.target,F;if(H.disabled){return false}if(H===D.close[0]){E._click(C.cancelVal);return false}else{if(H===D.max[0]||H===D.res[0]||H===D.max_b[0]||H===D.res_b[0]||H===D.res_t[0]){E.max();return false}else{if(H===D.min[0]||H===D.min_b[0]){E.min();return false}else{F=H[u+"callback"];F&&E._click(F)}}}}).bind("mousedown",function(F){E.zindex();var G=F.target;if(C.drag!==false&&G===D.title[0]||C.resize!==false&&G===D.rb[0]){z(F);return false}});if(C.max){D.title.bind("dblclick",function(){E.max();return false})}},
/*  卸载事件代理 */
_removeEvent:function(){var C=this,e=C.DOM;e.wrap.unbind();e.title.unbind();o.unbind("resize",C._winResize)}};a.fn._init.prototype=a.fn;
/* 使用jQ方式调用窗口 */
h.fn.dialog=function(){var e=arguments;this.bind("click",function(){a.apply(this,e);return false});return this};
/* 此对象用来存储获得焦点的窗口对象实例 */
a.focus=null;
/* 存储窗口实例的对象列表 */
a.list={};
/*
 * 全局快捷键
 * 由于跨框架时事件是绑定到最顶层页面，所以当当前页面卸载时必须要除移此事件
 * 所以必须unbind此事件绑定的函数，所以这里要给绑定的事件定义个函数
 * 这样在当前页面卸载时就可以移此事件绑定的相应函数，不而不影响顶层页面此事件绑定的其它函数
 */
f=function(C){var E=C.target,e=a.focus,D=C.keyCode;if(!e||!e.config.esc){return}D===27&&e._click(e.config.cancelVal)};B.bind("keydown",f);
/*
 * 框架页面卸载前关闭所有穿越的对话框
 * 同时移除拖动层和遮罩层
 */
s!=m&&h(m).bind("unload",function(){var C=a.list;for(var e in C){if(C[e]){C[e].close()}}q&&q.DOM.wrap.remove();B.unbind("keydown",f);delete a[u+"_data"];h("#ldg_lockmask",c)[0]&&h("#ldg_lockmask",c).remove();h("#ldg_dragmask",c)[0]&&h("#ldg_dragmask",c).remove()});a[u+"_data"]={};a.data=function(C,D){var e=a[u+"_data"];if(D!==j){e[C]=D}else{return e[C]}return e};a.removeDate=function(C){var e=a[u+"_data"];if(e&&e[C]){delete e[C]}};
/* 
 * 页面DOM加载完成执行的代码
 */
h(function(){setTimeout(function(){if(A){return}a({left:"-9999em",time:9,fixed:false,lock:false})},150);n&&(function(){var e="backgroundAttachment";if(i.css(e)!=="fixed"&&h(c.body).css(e)!=="fixed"){i.css({zoom:1,backgroundImage:"url(about:blank)",backgroundAttachment:"fixed"})}})();a.setting.extendDrag&&(function(D){var e=c.createElement("div"),C=e.style,E=n?"absolute":"fixed";e.id="ldg_dragmask";C.cssText="display:none;position:"+E+";left:0;top:0;width:100%;height:100%;cursor:move;filter:alpha(opacity=0);opacity:0;background:#FFF;pointer-events:none;";c.body.appendChild(e);D._start=D.start;D._end=D.end;D.start=function(){var H=a.focus,F=H.DOM.main[0],G=H.iframe;D._start.apply(this,arguments);C.display="block";C.zIndex=a.setting.zIndex+3;if(E==="absolute"){C.width="100%";C.height=o.height()+"px";C.left=B.scrollLeft()+"px";C.top=B.scrollTop()+"px"}if(G&&F.offsetWidth*F.offsetHeight>307200){F.style.visibility="hidden"}};D.end=function(){var F=a.focus;D._end.apply(this,arguments);C.display="none";if(F){F.DOM.main[0].style.visibility="visible"}}})(a.dragEvent)});
/*
 *------------------------------------------------
 * 对话框模块-拖拽支持（可选外置模块）
 *------------------------------------------------
 */
var z,d="setCapture" in k,v="onlosecapture" in k;a.dragEvent={onstart:w,start:function(C){var e=a.dragEvent;B.bind("mousemove",e.move).bind("mouseup",e.end);e._sClientX=C.clientX;e._sClientY=C.clientY;e.onstart(C.clientX,C.clientY);return false},onmove:w,move:function(C){var e=a.dragEvent;e.onmove(C.clientX-e._sClientX,C.clientY-e._sClientY);return false},onend:w,end:function(C){var e=a.dragEvent;B.unbind("mousemove",e.move).unbind("mouseup",e.end);e.onend(C.clientX,C.clientY);return false}};z=function(e){var G,H,N,D,J,L,I=a.focus,E=I.config,O=I.DOM,C=O.wrap[0],K=O.title,F=O.main[0],P=a.dragEvent,M="getSelection" in s?function(){s.getSelection().removeAllRanges()}:function(){try{c.selection.empty()}catch(Q){}};P.onstart=function(Q,R){if(L){H=F.offsetWidth;N=F.offsetHeight}else{D=C.offsetLeft;J=C.offsetTop}B.bind("dblclick",P.end);!n&&v?K.bind("losecapture",P.end):o.bind("blur",P.end);d&&K[0].setCapture();O.border.addClass("ui_state_drag");I.focus()};P.onmove=function(R,X){if(L){var U=C.style,T=F.style,S=R+H,Q=X+N;U.width="auto";E.width=T.width=Math.max(0,S)+"px";U.width=C.offsetWidth+"px";E.height=T.height=Math.max(0,Q)+"px";I._ie6SelectFix();I._load&&h(I._load).css({width:T.width,height:T.height})}else{var T=C.style,W=R+D,V=X+J;E.left=Math.max(G.minX,Math.min(G.maxX,W));E.top=Math.max(G.minY,Math.min(G.maxY,V));T.left=E.left+"px";T.top=E.top+"px"}M()};P.onend=function(Q,R){B.unbind("dblclick",P.end);!n&&v?K.unbind("losecapture",P.end):o.unbind("blur",P.end);d&&K[0].releaseCapture();n&&I._autoPositionType();O.border.removeClass("ui_state_drag")};L=e.target===O.rb[0]?true:false;G=(function(){var U=C.style.position==="fixed",S=C.offsetWidth,V=K[0].offsetHeight||20,W=o.width(),Q=o.height(),R=U?0:B.scrollLeft(),T=U?0:B.scrollTop();maxX=W-S+R;maxY=Q-V+T;return{minX:R,minY:T,maxX:maxX,maxY:maxY}})();P.start(e)};a.templates='<table class="ui_border"><tbody><tr><td class="ui_lt"></td><td class="ui_t"></td><td class="ui_rt"></td></tr><tr><td class="ui_l"></td><td class="ui_c"><div class="ui_inner"><table class="ui_dialog"><tbody><tr><td colspan="2"><div class="ui_title_bar"><div class="ui_title" unselectable="on"></div><div class="ui_title_buttons"><a class="ui_min" href="javascript:void(0);" title="\u6700\u5C0F\u5316"><b class="ui_min_b"></b></a><a class="ui_max" href="javascript:void(0);" title="\u6700\u5927\u5316"><b class="ui_max_b"></b></a><a class="ui_res" href="javascript:void(0);" title="\u8FD8\u539F"><b class="ui_res_b"></b><b class="ui_res_t"></b></a><a class="ui_close" href="javascript:void(0);" title="\u5173\u95ED(esc\u952E)">\xd7</a></div></div></td></tr><tr><td class="ui_icon"><img  class="ui_icon_bg"/></td><td class="ui_main"><div class="ui_content"></div></td></tr><tr><td colspan="2"><div class="ui_buttons"></div></td></tr></tbody></table></div></td><td class="ui_r"></td></tr><tr><td class="ui_lb"></td><td class="ui_b"></td><td class="ui_rb"></td></tr></tbody></table>';
/* lhgdialog 的全局默认配置 */
a.setting={content:'<div class="ui_loading"><span>loading...</span></div>',title:"\u89C6\u7A97 ",button:null,ok:null,cancel:null,init:null,close:null,okVal:"\u786E\u5B9A",cancelVal:"\u53D6\u6D88",skin:"",esc:true,show:true,width:"auto",height:"auto",icon:null,path:g,lock:true,parent:null,background:"#DCE2F1",opacity:0.6,padding:"10px",fixed:false,left:"50%",top:"38.2%",max:true,min:true,zIndex:m.top.global_top_zIndex,resize:true,drag:true,cache:true,extendDrag:false,currentwindow:null,maxState:false};m.lhgdialog=h.dialog=a})(this.jQuery||this.lhgcore,this);
/*
 *------------------------------------------------
 * 对话框其它功能扩展模块（可选外置模块）
 *------------------------------------------------
 */
(function(c,b,d){var a=function(){return b.setting.zIndex};b.alert=function(f,h,e,g){return b({title:g,id:"Alert",zIndex:a(),icon:"alert.gif",fixed:true,lock:true,content:f,ok:true,resize:false,close:h,parent:e||null})};b.confirm=function(f,i,h,e,g){return b({title:g,id:"confirm.gif",zIndex:a(),icon:"confirm.gif",fixed:true,lock:true,content:f,resize:false,parent:e||null,ok:function(j){return i.call(this,j)},cancel:function(j){return h&&h.call(this,j)}})};b.prompt=function(g,i,h,f){h=h||"";var e;return b({title:"提问",id:"Prompt",zIndex:a(),icon:"prompt.gif",fixed:true,lock:true,parent:f||null,content:['<div style="margin-bottom:5px;font-size:12px">',g,"</div>","<div>",'<input value="',h,'" style="width:18em;padding:6px 4px" />',"</div>"].join(""),init:function(){e=this.DOM.content[0].getElementsByTagName("input")[0];e.select();e.focus()},ok:function(j){return i&&i.call(this,e.value,j)},cancel:true})};b.tips=function(g,h,f,i){var e=f?function(){this.DOM.icon_bg[0].src=this.config.path+"skins/icons/"+f;this.DOM.icon[0].style.display="";if(i){this.config.close=i}}:function(){this.DOM.icon[0].style.display="none"};return b({id:"Tips",zIndex:a(),title:false,cancel:false,fixed:true,lock:false,resize:false,close:i}).content(g).time(h||1.5,e)}})(this.jQuery||this.lhgcore,this.lhgdialog);