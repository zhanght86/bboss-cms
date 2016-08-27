$(document).ready(function() {            
            var w = $(window).width();//$(document).width();
            var h = $(window).height();//$(document).height();           
            changePoisition();
            warn();   
            queryWarnContent();             
           // showActionMessage('<a href="javascript:void(0);" onclick="addNoticeItem(frame1.mainFrame);">您还有<span class="fc5">56</span>条信息未阅读信息</a>');        
        });
 $(window).resize(function() {
	 changePoisition();       
 });
 /**
  * 更新气泡位置
  * @return
  */
 function changePoisition(){
	 $('#Layer4').css("right",0);	
	 $('#Layer4').css("bottom",15);	 
 }
 function showActionMessage(message){
 		$('#messagecontent').html(message);
 		$('#Layer4').show();
 }
 /**
  * 定时器
  * @return
  */
 function queryWarnContent(){
	 setInterval("warn()",5*60*1000);
 }
 /**
  * 查询数据
  * @return
  */
 function warn(){
	$.ajax({
		type: "POST",
		url: "queryWarnContent.action",
		dataType : "json",
		success: function(data){
			var content = "";
			$.each(data,function(){
				if (content != ""){
					content = content +"<br/>";
				} 
				content =  content+" <a href=\"javascript:addItem('"+this.messageType+"');\">"+this.messageTitle+"</a>";
			});
			if (content!=""){
				showActionMessage(content);
			}
		}
	});
 }
 /**
  * 添加TAB
  * @param type
  * @return
  */
 function addItem(type){
	 if (type=="notice"){
		 addNoticeItem(frame1.mainFrame);
	 } /*else if (type=="order"){
		 addNoticeItem(frame1.mainFrame);
	 } else if (type=="revisit"){
		 addNoticeItem(frame1.mainFrame); 
	 }*/
	 //隐藏气泡
	 $('#Layer4').hide();
 }