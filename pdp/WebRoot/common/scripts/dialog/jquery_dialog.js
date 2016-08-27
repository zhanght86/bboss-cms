var JqueryDialog = {
	
	//配置项
	//模态窗口背景色
	"cBackgroundColor"			:	"#ffffff",
	
	//边框尺寸(像素)
	"cBorderSize"				:	1,
	
	//边框颜色
	"cBorderColor"				:	"#999999",
	
	//Header背景色
	"cHeaderBackgroundColor"	:	"#f0f0f0",
	
	//Bottom背景色
	"cBottomBackgroundColor"	:	"#f0f0f0",
	
	"imgFolder" 				:   gDialogImgFolder, 
	
	/// <summary>创建对话框</summary>
	/// <param name="dialogTitle">对话框标题</param>
	/// <param name="iframeSrc">iframe嵌入页面地址</param>
	/// <param name="iframeWidth">iframe嵌入页面宽</param>
	/// <param name="iframeHeight">iframe嵌入页面高</param>
	Open:function(dialogTitle, iframeSrc, iframeWidth, iframeHeight){
		JqueryDialog.init(dialogTitle, iframeSrc, iframeWidth, iframeHeight, false);
	},
	
	/// <summary>创建对话框</summary>
	/// <param name="dialogTitle">对话框标题</param>
	/// <param name="iframeSrc">iframe嵌入页面地址</param>
	/// <param name="iframeWidth">iframe嵌入页面宽</param>
	/// <param name="iframeHeight">iframe嵌入页面高</param>>
	/// <param name="isDrag">是否支持拖拽</param>
	Open1:function(dialogTitle, iframeSrc, iframeWidth, iframeHeight, isDrag){
		JqueryDialog.init(dialogTitle, iframeSrc, iframeWidth, iframeHeight, isDrag);
	},
	
	/// <summary>创建对话框</summary>
	/// <param name="dialogTitle">对话框标题</param>
	/// <param name="iframeSrc">iframe嵌入页面地址</param>
	/// <param name="iframeWidth">iframe嵌入页面宽</param>
	/// <param name="iframeHeight">iframe嵌入页面高</param>
	/// <param name="isDrag">是否支持拖拽</param>
	init:function(dialogTitle, iframeSrc, iframeWidth, iframeHeight, isDrag){
		
		//获取客户端页面宽高
		var _client_width = document.body.clientWidth;
		//modify by jianfeng
		//var _client_height = document.documentElement.scrollHeight;
		var _client_height = $(document).height() - 25;
		
		//create shadow
		if(typeof($("#jd_shadow")[0]) == "undefined"){
			//前置
			$("body").prepend("<div id='jd_shadow'>&nbsp;</div>");
			var _jd_shadow = $("#jd_shadow");
			_jd_shadow.css("width", _client_width + "px");
			_jd_shadow.css("height", _client_height + "px");
			
		}
		
		//create dialog
		if(typeof($("#jd_dialog")[0]) != "undefined"){
			$("#jd_dialog").remove();
		}
		$("body").prepend("<div id='jd_dialog'></div>");
		
		//dialog location
		//left 边框*2 阴影5
		//top 边框*2 阴影5 header30 bottom50
		var _jd_dialog = $("#jd_dialog");
		var _left = (_client_width - (iframeWidth + JqueryDialog.cBorderSize * 2 + 5)) / 2;
		_jd_dialog.css("left", (_left < 0 ? 0 : _left) + document.documentElement.scrollLeft + "px");
		var _top = (document.documentElement.clientHeight - (iframeHeight + JqueryDialog.cBorderSize * 2 + 26 + 6 + 5)) / 2 - 100;
		_jd_dialog.css("top", (_top < 0 ? 0 : _top) + document.documentElement.scrollTop +40+ "px");
		
		//create dialog shadow
		_jd_dialog.append("<div id='jd_dialog_s'>&nbsp;</div>");
		var _jd_dialog_s = $("#jd_dialog_s");
		//iframeWidth + double border
		_jd_dialog_s.css("width", iframeWidth + JqueryDialog.cBorderSize * 2 + "px");
		
		//iframeWidth + double border + header + bottom
		_jd_dialog_s.css("height", iframeHeight + JqueryDialog.cBorderSize * 2 + 26 + 6 + "px");
		//create dialog main
		
		_jd_dialog.append("<div id='jd_dialog_m'><iframe style='position:absolute;z-index:-1;width:100%;height:100%;top:0;left:0;scrolling:no;' frameborder='0'></iframe></div>");
		var _jd_dialog_m = $("#jd_dialog_m");
		_jd_dialog_m.css("border", JqueryDialog.cBorderColor + " " + JqueryDialog.cBorderSize + "px solid");
		_jd_dialog_m.css("width", iframeWidth + "px");
		_jd_dialog_m.css("background-color", JqueryDialog.cBackgroundColor);
		//cursor:move;
		_jd_dialog_m.append("<table id='frameTable' width='100%' height='" + (iframeHeight + 26 + 6) + "' border='0' cellpadding='0' cellspacing='0'><thead id='jd_dialog_h' style='height:26px'><tr> <td width='31' height='26'><img src='" + JqueryDialog.imgFolder + "/div_01.gif' width='31' height='26'></td> <td width='100%' background='" + JqueryDialog.imgFolder + "/div_02.gif'>" + dialogTitle + "</td> <td width='33' height='26'><img src='" + JqueryDialog.imgFolder + "/div_03.gif' style='cursor:pointer' width='33' height='26' border='0' onclick='JqueryDialog.Close();'></td> </tr></thead><tbody style='height:100%'><tr><td colspan='3' height='100%'><table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0'><tr> <td width='1' background='" + JqueryDialog.imgFolder + "/div_08.gif'  height='100%'>&nbsp;</td> <td width='100%' align='center' valign='middle' bgcolor='#FFFFFF' height='100%'> <iframe id='jd_iframe' name='jd_iframe' src='"+iframeSrc+"' frameborder='0' border='0'  width='100%' height='100%' noresize='noresize'></iframe> </td><td width='10' background='" + JqueryDialog.imgFolder + "/div_07.gif' height='100%'><img src='" + JqueryDialog.imgFolder + "/div_07.gif' width='10' height='7' ></td> </tr></table></td></tr></tbody><tfoot style='height:6px'><tr><td height='6'><img src='" + JqueryDialog.imgFolder + "/div_04.gif' width='31' height='6'></td> <td background='" + JqueryDialog.imgFolder + "/div_05.gif'></td> <td width='33'><img src='" + JqueryDialog.imgFolder + "/div_06.gif' width='33' height='6'></td></tr></tfoot></table>");
	    var _jd_dialog_m_h = $('#jd_dialog_h');
		
	    //2011-02-25修改
	    var iframeObj = document.getElementById("jd_iframe");
	    iframeObj.focus();
	    
		//register drag
		if(isDrag){
			DragAndDrop.Register(_jd_dialog[0], _jd_dialog_m_h[0]);
		}
	},
	
	/// <summary>关闭模态窗口</summary>
	Close:function(){
		$("#jd_shadow").remove();
		$("#jd_dialog").remove();
	}
	
};

var DragAndDrop = function(){
	
	//客户端当前屏幕尺寸(忽略滚动条)
	var _clientWidth;
	
	var _clientHeight;
	
	//拖拽控制区
	var _controlObj;
	
	//拖拽对象
	var _dragObj;
	
	//拖动状态
	var _flag = false;
	
	//拖拽对象的当前位置
	var _dragObjCurrentLocation;
	
	//鼠标最后位置
	var _mouseLastLocation;
	
	var getElementDocument = function(element){
		return element.ownerDocument || element.document;
	};
	
	//鼠标按下
	var dragMouseDownHandler = function(evt){
		if(_dragObj){
			evt = evt || window.event;
			//获取客户端屏幕尺寸
			_clientWidth = document.body.clientWidth;
			_clientHeight = document.documentElement.scrollHeight;
			//标记
			_flag = true;
			//拖拽对象位置初始化
			_dragObjCurrentLocation = {
				x : $(_dragObj).offset().left,
				y : $(_dragObj).offset().top
			};
			//鼠标最后位置初始化
			_mouseLastLocation = {
				x : evt.screenX,
				y : evt.screenY
			};
			//注：mousemove与mouseup下件均针对document注册，以解决鼠标离开_controlObj时事件丢失问题
			//注册事件(鼠标移动)			
			$(document).bind("mousemove", dragMouseMoveHandler);
			//注册事件(鼠标松开)
			$(document).bind("mouseup", dragMouseUpHandler);
			//取消事件的默认动作
			if(evt.preventDefault) {
				evt.preventDefault();
			} else {
				evt.returnValue = false;
			}
		}
	};
	
	//鼠标移动
	var dragMouseMoveHandler = function(evt){
		if(_flag){
			evt = evt || window.event;
			//当前鼠标的x,y座标
			var _mouseCurrentLocation = {
				x : evt.screenX,
				y : evt.screenY
			};
			//拖拽对象座标更新(变量)
			_dragObjCurrentLocation.x = _dragObjCurrentLocation.x + (_mouseCurrentLocation.x - _mouseLastLocation.x);
			_dragObjCurrentLocation.y = _dragObjCurrentLocation.y + (_mouseCurrentLocation.y - _mouseLastLocation.y);
			//将鼠标最后位置赋值为当前位置
			_mouseLastLocation = _mouseCurrentLocation;
			//拖拽对象座标更新(位置)
			$(_dragObj).css("left", _dragObjCurrentLocation.x + "px");
			$(_dragObj).css("top", _dragObjCurrentLocation.y + "px");
			//取消事件的默认动作
			if(evt.preventDefault) {
				evt.preventDefault();
			} else {
				evt.returnValue = false;
			}
		}
	};
	
	//鼠标松开
	var dragMouseUpHandler = function(evt){
		if(_flag){
			evt = evt || window.event;
			//注销鼠标事件(mousemove mouseup)
			cleanMouseHandlers();
			//标记
			_flag = false;
		}
	};
	
	//注销鼠标事件(mousemove mouseup)
	var cleanMouseHandlers = function(){
		if(_controlObj){
			$(_controlObj.document).unbind("mousemove");
			$(_controlObj.document).unbind("mouseup");
		}
	};
	
	return {
		//注册拖拽(参数为dom对象)
		Register : function(dragObj, controlObj){
			//赋值
			_dragObj = dragObj;
			_controlObj = controlObj;
			//注册事件(鼠标按下)
			$(_controlObj).bind("mousedown", dragMouseDownHandler);			
		}
	}
	
}();

//-->