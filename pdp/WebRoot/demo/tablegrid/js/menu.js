/*
Tutorial URL: http://www.neoease.com/wordpress-menubar-5/
*/
;(function($){
		   
var Class = {
	create: function() {
		return function() {
			this.initialize.apply(this, arguments);
		}
	}
}

/** 菜单列表 */
var MenuList = Class.create();
MenuList.prototype = {

	/**
	 * 构造方法
	 * id: 菜单列表
	 * opacity: 不透明度 (0.0 - 1.0, 0.0 为全透明, 1.0 为不透明)
	 */
	initialize: function(id, opacity) {
		// 获取菜单列表
		this.obj = document.getElementById(id);
		if (!this.obj) { return; }

		// 找到所有的菜单
		var menus = this.obj.getElementsByTagName('ul');
		for (var i = 0; i < menus.length; i++) {
			// 找到菜单的父节点 (包括标题链接部分)
			var menu = menus[i].parentNode;

			// 如果菜单的父节点就是根菜单, 显示一般的菜单
			if(menu.parentNode === this.obj) {
				new Menu(menu, opacity);

			// 如果菜单的父节点不是根菜单, 说明当前菜单是子菜单
			} else {
				new Menu(menu, opacity, 1);
				// 在子菜单的标题链接上加上 class 名, 以便定义样式
				menu.firstChild.className += 'subtitle';				
			}
		}
		/*
		// 对菜单列表内的所有菜单进行处理
		var menus = this.obj.childNodes;
		for (var i = 0; i < menus.length; i++) {
			var menu = menus[i];
			if (menu.tagName == 'LI') {
				// 构建菜单
				new Menu(menu, opacity);
			}
		}
		*/
	}
}

/** 菜单 */
var Menu = Class.create();
Menu.prototype = {

	/**
	 * 构造方法
	 * target: 目标菜单
	 * opacity: 不透明度 (0.0 - 1.0, 0.0 为全透明, 1.0 为不透明)
	 * sub: 是否为子菜单 (1 为肯定, -1 为否定) [multi 2009/06/11 ADD]
	 */
	initialize: function(target, opacity, sub) {
		this.util = new MenuUtil();

		// 获取目标菜单 (没多余元素)
		this.obj = this.util.cleanWhitespace(target);
		
		// 定义透明度, 默认透明
		this.opacity = opacity || 1;

		/* multi 2009/06/11 ADD START */
		// 是否为子菜单
		this.sub = sub || -1;
		/* multi 2009/06/11 ADD START */

		// 获取菜单
		this.menu = this.obj.childNodes

		
		
		// 重要! 如果菜单不包含菜单项, 则不进行处理
		if (this.menu.length < 2) { return; }

		// 菜单标题和菜单体
		this.title = this.menu[0];
		this.body = this.menu[1];

		// 定义初始样式
		this.util.setStyle(this.body, 'visibility', 'hidden');
		this.util.setStyle(this.body, 'position', 'absolute');

		//this.util.setStyle(this.body, 'overflow', 'hidden');

		this.util.setStyle(this.body, 'display', 'block');

		
		// 添加监听器
		if(navigator.appName.indexOf("Microsoft Internet Explorer")!=-1 && document.all){
			this.addListener(this.obj, 'mouseenter', this.util.bind(this, this.activate), false);
			this.addListener(this.obj, 'mouseleave', this.util.bind(this, this.deactivate), false);
		}
		else{
			this.addListener(this.obj, 'mouseover', this.util.bind(this, this.activate), false);
			this.addListener(this.obj, 'mouseout', this.util.bind(this, this.deactivate), false);		
		}			
	},

	/**
	 * 激活方法
	 * 当鼠标移动到菜单标题是激活
	 */
	activate: function() {

		// 获取当前菜单体的位置 (子菜)   
		
		if(this.sub == 1) {//为子菜单
			var pos = this.util.currentOffset(this.title);
			var left = this.util.getWidth(this.body)-1;
			var top = pos[1]-1;
			if(this.obj.className=='menuleft'){left=(left-2*left);};
 		// 获取当前菜单体的位置 (菜单)		
			this.title.className = ' current';				
		}else {			
			this.title.className = ' fcurrent';	
			var pos = this.util.cumulativeOffset(this.title);
			
			var left = pos[0];

			var top = pos[1] + this.util.getHeight(this.title);
			// 子菜单不需要设置不透明度, 否则会形成多重透明, 效果不好.
			this.util.setStyle(this.body, 'opacity', this.opacity);
		}
		
		// 定义激活时样式
		this.util.setStyle(this.body, 'left', left + 'px');
		this.util.setStyle(this.body, 'top', top + 'px');
		this.util.setStyle(this.body, 'visibility', 'visible');


		// 子菜单不需要设置不透明度 (请参考前几行的注释)
		//this.util.setStyle(this.body, 'opacity', this.opacity);
		// IE 不能设置不透明度, 否则子菜单显示不出来
		//this.util.setStyle(this.body, 'filter', 'alpha(opacity=' + this.opacity * 100 + ')');

	},
	
	/**
	 * 解除方法
	 * 当鼠标移动出菜单标题是激活
	 */
	deactivate: function(){
		// 定义解除时样式
		this.util.setStyle(this.body, 'visibility', 'hidden');		
	
		// 离开菜单时取消当前菜单上的 class 名, 恢复原本的样式
		if(this.sub == 1){
				this.title.className = this.title.className.replace('current', 'subtitle');
			}
			else{
				this.title.className = this.title.className.replace('current', '');
			}

	},

	/**
	 * 监听方法
	 * element: 监听对象
	 * name: 监听方法
	 * observer: 执行的方法
	 * useCapture: 浏览器调用事件的方式 (true 为 Capture 方式, false 为 Bubbling 方式)
	 */
	addListener: function(element, name, observer, useCapture) {
		if(element.addEventListener) {
			element.addEventListener(name, observer, useCapture);
		} else if(element.attachEvent) {
			element.attachEvent('on' + name, observer);
		}
	}
}

/** 一些实用的方法 */
var MenuUtil = Class.create();
MenuUtil.prototype = {
	initialize: function() {
	},

	$: function(id) {
		return document.getElementById(id);
	},

	$A: function(iterable) {
		if(!iterable) {
			return [];
		}
		if(iterable.toArray) {
			return iterable.toArray();
		} else {
			var results = [];
			for(var i = 0; i < iterable.length; i++) {
				results.push(iterable[i]);
			}
			return results;
		}
	},

	bind: function() {
		var array = this.$A(arguments);
		var func = array[array.length - 1];
		var _method = func, args = array, object = args.shift();
		return function() {
			return _method.apply(object, args.concat(array));
		}
	},

	getHeight: function(element) {
		return element.offsetHeight;
	},

	setStyle: function(element, key, value) {
		element.style[key] = value;
	},

	getStyle: function(element, key) {
		return element.style[key];
	},

	cleanWhitespace: function(list) {
		var node = list.firstChild;
		while (node) {
			var nextNode = node.nextSibling;
			if(node.nodeType == 3 && !/\S/.test(node.nodeValue)) {
				list.removeChild(node);
			}
			node = nextNode;
		}
		return list;
	},

	cumulativeOffset: function(element) {
		var valueT = 0, valueL = 0;
		do {
			valueT += element.offsetTop  || 0;
			valueL += element.offsetLeft || 0;
			element = element.offsetParent;
		} while (element);
		return [valueL, valueT];
	},

	/* multi 2009/06/11 ADD START */
	getWidth: function(element) {
		return element.offsetWidth;
	},

	currentOffset: function(element) {
		var valueT = element.offsetTop  || 0;
		var valueL = element.offsetLeft || 0;
		return [valueL, valueT];
	}
	/* multi 2009/06/11 ADD END */
}

$.fn.bgiframe = ($.browser.msie && /msie 6\.0/i.test(navigator.userAgent) ? function(s) {
    s = $.extend({
        top     : 'auto', // auto == .currentStyle.borderTopWidth
        left    : 'auto', // auto == .currentStyle.borderLeftWidth
        width   : 'auto', // auto == offsetWidth
        height  : 'auto', // auto == offsetHeight
        opacity : true,
        src     : 'javascript:false;'
    }, s);
    var html = '<iframe class="bgiframe"frameborder="0"tabindex="-1"src="'+s.src+'"'+
                   'style="display:block;position:absolute;z-index:1000;'+
                       (s.opacity !== false?'filter:Alpha(Opacity=\'0\');':'')+
                       'top:'+(s.top=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+\'px\')':prop(s.top))+';'+
                       'left:'+(s.left=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+\'px\')':prop(s.left))+';'+
                       'width:'+(s.width=='auto'?'expression(this.parentNode.offsetWidth+\'px\')':prop(s.width))+';'+
                       'height:'+(s.height=='auto'?'expression(this.parentNode.offsetHeight+\'px\')':prop(s.height))+';'+
                '"/>';
    return this.each(function() {
        if ( $(this).children('iframe.bgiframe').length === 0 )
            this.insertBefore( document.createElement(html), this.firstChild );
    });
} : function() { return this; });

// old alias
$.fn.bgIframe = $.fn.bgiframe;

function prop(n) {
    return n && n.constructor === Number ? n + 'px' : n;
}

/** 添加到页面加载事件 */
$(function(){
	new MenuList('menus', 0.9);
	$('#menus .second').bgiframe(); 
	$('#menus .third').bgiframe(); 
})
})(jQuery);