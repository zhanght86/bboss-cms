var currIndex = -1;

$(function(){
	var timeInterval;
	var word = $("#word");
	
	//设置样式
	$("#auto").css("border","1px solid black")
    .css("position","absolute")
    .css("top",word.offset().top+word.height()+1+"px")
    .css("left",word.offset().left+"px")
    .css("width",word.width()+"px")
    .hide();
 
	//添加键盘事件
	word.keyup(function(e){
		var e = e || window.event;
 
		//如查输入的是字母或退格或删除
		if(e.keyCode >= 65 && e.keyCode <= 90 || e.keyCode == 8 || e.keyCode == 46){
			var auto = $("#auto");
			auto.html("");
			auto.hide();
			currIndex = -1;
			var value = word.val();
    
			//清除事件
			clearTimeout(timeInterval);
   
			if(value != ""){
				//延迟发送
				timeInterval = setTimeout(ajaxPost,500);
			}
   
		}else if(e.keyCode == 38 || e.keyCode == 40){
			//向上
			if(e.keyCode == 38){
				var divs = $("#auto > div");
				if(currIndex != -1){
					divs.eq(currIndex).css("background-color","white");
				}
				currIndex--;
				if(currIndex == -1){
					currIndex = divs.length - 1;
				}
				divs.eq(currIndex).css("background-color","red");
			}
			//向下
			if(e.keyCode == 40){
				var divs = $("#auto > div");
				if(currIndex != -1){
					divs.eq(currIndex).css("background-color","white");
				}
				currIndex++;
				if(currIndex == divs.length){
					currIndex = 0;
				}
				divs.eq(currIndex).css("background-color","red");
			}
		}else if(e.keyCode == 13){
			//回车
			var divs = $("#auto > div");
			var value = divs.eq(currIndex).text();
			$("#word").val(value);
			$("#auto").hide();
			currIndex = -1;
		}
 });
 
 $("input[type='button']").click(function(){
  alert("文本框中的内容是："+word.val());
 });
});

function ajaxPost(){
	var value = $("#businessKey").val();
	var auto = $("#auto");
	$.post(ctx+"/workflow/businessDemo/getBusinessKeyList.page",{businessKey:value},function(data){
		alert(data);
	    //得到所有的word节点
	    var words = $(data).find("word");//处理XML
	    words.each(function(w){
	    var wdiv = $("<div>").attr("id",w);
	    wdiv.html($(this).text()).appendTo(auto);
	    //添加鼠标事件
	    wdiv.mouseover(function(){
	    	if(currIndex != -1){
	    		divs = $("#auto > div").eq(currIndex).css("background-color","white");
	    	}
	       currIndex = this.id;
	       $(this).css("background-color","red");
	    });
	      
	    wdiv.mouseout(function(){
	       $(this).css("background-color","white");
	    });
	      
	    wdiv.click(function(){
	       $("#word").val($(this).text());
	       $("#auto").hide();
	       currIndex = -1;
	    });
      
     });
     if(words.length > 0){
      auto.show();
     }else{
      auto.hide();
      currIndex = -1;
     }
     
    },"json");
}
