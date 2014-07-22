<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>

<script type="text/javascript">
var api = frameElement.api;

$(document).ready(function() {
	querySubscribeList();
});  
  
    
  function querySubscribeList(){
	  
	   
	   $("#custombackContainer").load("querySubscribeList.page #customContent", {},function(){loadjs();
	  
	   var trs = document.getElementsByTagName('tr');
	   for(var i=1;i<trs.length;i++){
		   
		   if("0"==trs[i].childNodes[4].innerHTML){
			   $("#"+trs[i].childNodes[0].innerHTML+"_cancel").attr("disabled","disabled");
			   $("#"+trs[i].childNodes[0].innerHTML+"_cancel").removeAttr("onclick");
			   $("#"+trs[i].childNodes[0].innerHTML+"_sub").attr("disabled","disabled");
			   $("#"+trs[i].childNodes[0].innerHTML+"_sub").removeAttr("onclick");
			   $("#"+trs[i].childNodes[0].innerHTML).removeAttr("class");
			   $("#"+trs[i].childNodes[0].innerHTML).attr("bgcolor","#ffebeb");
			   $("#"+trs[i].childNodes[0].innerHTML).wrap("<span class='toolTip' title='该应用待办功能已经关闭，无法订阅'></sapn>");
		   }else {
			   if("0"==trs[i].childNodes[5].innerHTML||""==trs[i].childNodes[5].innerHTML){
				   $("#"+trs[i].childNodes[0].innerHTML+"_cancel").attr("disabled","disabled");
				   $("#"+trs[i].childNodes[0].innerHTML+"_cancel").removeAttr("onclick");
			   }else{
				   $("#"+trs[i].childNodes[0].innerHTML+"_sub").attr("disabled","disabled");
				   $("#"+trs[i].childNodes[0].innerHTML+"_sub").removeAttr("onclick");
			   }
			   
		   }
		   
		   
	   }
	   
	   });
	  
  }
  function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
      return ( containSpecial.test(s) );      
 }
  
  
  
 
  
  function confirm(obj,subType){
		 var id = obj.parentNode.parentNode.childNodes[0].innerHTML;
		 var index = obj.parentNode.parentNode.rowIndex;
		 $.ajax({
		 	 	type: "POST",
				url : "insertOrUpdatePendingSubscribe.page",
				data :{appId:id,pendingType:subType},
				dataType : '',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if (data == "success") {
						$.dialog.alert("保存成功");
						querySubscribeList();	
						
					} else {
						$.dialog.alert("保存失败");
						
					}
				}	
			 });
		 
	}
  
	function CloseDlg(){
		   api.close();
	 }

   
</script>
</head>
	<body>
		<div class="mcontent">
			<div class="title_box">
				<div class="rightbtn">
				<!-- <a href="#" class="bt_small" id="addButton" onclick="javascript:addWorkTime();"><span>新增工作时间</span></a> -->
				<!-- <a href="#" class="bt_2"  onclick="CloseDlg()"><span>关闭</span></a> -->
				</div>
				
				<strong></strong>				
			</div>
			<div id="custombackContainer"  style="overflow:auto"></div>
		</div>
	</body>
</html>
