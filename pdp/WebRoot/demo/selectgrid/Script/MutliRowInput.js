


/*
 * 多列输入功能
 * 
 */
var MutliRowInput = function(params){
	this.params = params||{};
	
	this.inputValue = "";
	
	//弹出窗口的宽
	this.width = this.params.width||200;
	//弹出窗口的高
	this.height = this.params.height||120;
	
	//
	this.createDom();
	
	//绑定事件
	this.bindEvent();
};


MutliRowInput.prototype = {
	
	//创建弹出窗口
	createDom:function(){
		
		var params = this.params;
		
		//弹出窗口ID
		var popId = params.id+"_Pop";
		
		//控制按钮ID
		var conId = params.id+"_Con";
		
		//069 增加一个<br/>解决 IE7, IE6兼容问题
		var html = "<iframe style=\"width: 0px;height: 0px;\" ></iframe><br/><div id=\""+popId+"\" style=\"width: "+this.width+
			"px; height: "+this.height+"px; "+//background: #999;left:-50px; relative
			"display:none;position: absolute;\">";
		
		html += "<textarea rows=\"\" cols=\"20\" style=\"width: 100%;height: "+this.height+"px; \""+
				"></textarea>"+
				"<div style='border: #ccc 1px solid; border-top: none; padding-bottom: 3px; background-color: #e7e7e7; width: 203px; padding-top: 3px;' id=\""+conId+"\"></div>";
		
		html += "</div>";
		
		$("#"+params.id).after(html);//before
		
		//alert(html);
		
		var _that = this;
		
		//确定按钮事件
		var btn = document.createElement("input");
		btn.type = "button";
		btn.value = "确定";
		btn.onclick = function (){
			
			var val = $("#"+popId+" textarea").val();
			
			_that.inputValue = val;
			
			if(val.split("\n").length>0){
				$("#"+_that.params.targetId).val(val.split("\n").join(","));
				
				var fn = _that.params.callback;				
				if(fn){
					fn.call(fn, val.split("\n").join(","));
				}
				
				_that.dismiss();
			}
		}
		
		$(btn).appendTo("#"+conId);
		
		//取消按钮事件
		btn = document.createElement("input");
		btn.type = "button";
		btn.value = "取消";
		btn.onclick = function (){
			_that.dismiss();
		}
		$(btn).appendTo("#"+conId);
		
	},
	
	//绑定事件
	bindEvent:function(){
		var _that = this;
		$("#"+this.params.id).live("click", function(){
			_that.showAndHide();
		});
		//输入框 blur
		$("#"+this.params.targetId).live("keyup", function(){
			
			var popId = _that.params.id+"_Pop";
			
			var val = $(this).val();
			var textVal = $("#"+popId+" textarea").val();
			//自动替换文本输入框的第一行的值
			if(textVal){
				var texts = textVal.split("\n");
				//texts[0] = val;
				$("#"+popId+" textarea").val(val.split(",").join("\n"));
				
			}else{
				$("#"+popId+" textarea").val(val);
			}
			
			var fn = _that.params.callback;				
			if(fn){
				fn.call(fn, val);
			}
			
		});
		
	},
	
	//显示或者隐藏
	showAndHide:function(){
		var dis = $("#"+this.params.id+"_Pop").css("display");
		if("none"==dis){
			$("#"+this.params.id+"_Pop").css("display", "");
			
			var popId = this.params.id+"_Pop";
			
			/**/
			var val = $("#"+this.params.targetId).val();
			var textArea = $("#"+popId+" textarea").val();
			if(val){
				$("#"+popId+" textarea").val(val.split(",").join("\n"));
			}else{
				$("#"+popId+" textarea").val("");
			}
				
			$("#"+popId+" textarea").focus();
		}else{
			$("#"+this.params.id+"_Pop").css("display", "none");
		}
	},
	
	//隐藏弹出窗口
	dismiss:function(){
		$("#"+this.params.id+"_Pop").css("display", "none");
	}
		
}



//GSP基础操作类 
var SanyGSP = {
		
		setCookie : function(cookieName, cookieValue, expires, path, domain, secure){
			if(!expires||expires<10){
				var now = new Date();
				//加一天
				expires = new Date(now.getTime() + 1000 * 60 * 60 * 24);
			}
			document.cookie =
				escape(cookieName) + '=' + escape(cookieValue)
				+ (expires ? '; expires=' + expires.toGMTString() : '')
				+ (path ? '; path=' + path : '')
				+ (domain ? '; domain=' + domain : '')
				+ (secure ? '; secure' : '');
		},
		
		getCookie : function(cookieName) {
			var cookieValue = '';
			var posName = document.cookie.indexOf(escape(cookieName) + '=');
			if (posName != -1) {
				var posValue = posName + (escape(cookieName) + '=').length;
				var endPos = document.cookie.indexOf(';', posValue);
				if (endPos != -1) cookieValue = unescape(document.cookie.substring(posValue, endPos));
				else cookieValue = unescape(document.cookie.substring(posValue));
			}
			return (cookieValue);
		},
		
		//切换显示Table详细信息
		switchShowAndHide:function(targetTable, hitDom){
			//$(hitDom).css("cursor", "hand");
			var img = $(hitDom).find("img");//minus.gif
			//plus.gif
			var src = $(img).attr("src");
			
			var isShow = false;
			//减号
			if(src.indexOf("minus")!=-1){
				$(img).attr("src", src.replace("minus", "plus"));
			//加号
			}else{
				$(img).attr("src", src.replace("plus", "minus"));
				isShow = true;
			}
			
			if(targetTable){
				//.find("tbody[id=goodsTbody]")
				$("#"+targetTable).css("display", isShow?"":"none");
				$("."+targetTable).css("display", isShow?"":"none");
			}
			
		}
};

