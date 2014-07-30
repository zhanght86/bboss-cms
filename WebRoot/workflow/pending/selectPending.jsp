<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
 <%@ include file="/common/jsp/css.jsp"%> 
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<link href="stylesheet/system.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var api = frameElement.api, W = api.opener;

$(document).ready(function() {
	var lis = $("li");
	for(var i=0;i<lis.length;i++){
		var id_value = lis[i].id;
		if("0"==$("#"+id_value+"_used").val()){
			$("#"+id_value+"_check").attr("disabled","disabled");
		}
	}
});  
 
  function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
      return ( containSpecial.test(s) );      
 }
  
	function CloseDlg(){
		   api.close();
	 }

function submit(){
	var msg = "";
	var lis = $("li");
	for(var i=0;i<lis.length;i++){
		var id_value = lis[i].id;
		if("1"==$("#"+id_value+"_used").val()){
			var type="0";
			var sys_name = $("#"+id_value+"_nameCH").val();
			
			if(document.getElementById(id_value+"_check").checked){
				type = "1";
			}
			$.ajax({
		 	 	type: "POST",
				url : "insertOrUpdatePendingSubscribe.page",
				data :{appId:id_value , pendingType:type},
				dataType : '',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if("success"!=data){
						msg = msg + sys_name +"更新订阅失败!</br>";
					}
				}	
			 });
		}
	}
	if(""==msg){
		W.$.dialog.alert("操作成功", function() {
			 api.close(); 
		}, api);
	}else{
		W.$.dialog.alert(msg, function() {}, api);
	}
	
}
function findPic(picName){
	   if(null == picName || ""==picName){
		   return;
	   }
	   $.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/application/findPic.page",
			data :{picName:picName},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				 
			}	
		 });
}
</script>
</head>
	<body>
<div class="title_system">请选择您需要订阅的系统待办：</div>
<div class="system_list">
  <ul>
  <pg:list  autosort="false" requestKey="datas">
  <li id="<pg:cell colName="id"/>">
  <img src="${pageContext.request.contextPath}/application/app_images/<pg:cell colName="picName"/>" width="68" height="68" onerror="findPic('<pg:cell colName="picName"/>')"/> 
  <span><pg:equal colName="pendingSubscribe"  value="0"><input id="<pg:cell colName="id"/>_check" name="" type="checkbox"/></pg:equal>
		<pg:equal colName="pendingSubscribe" value="1"><input id="<pg:cell colName="id"/>_check" name="" type="checkbox"  checked="checked"/></pg:equal>
		<pg:empty colName="pendingSubscribe" ><input id="<pg:cell colName="id"/>_check" name="" type="checkbox"/></pg:empty>
		<pg:cell colName="nameCH"/></span>
		<input type="text" id="<pg:cell colName="id"/>_used" value="<pg:cell colName="pendingUsed"/>" style="display:none">
		<input type="text" id="<pg:cell colName="id"/>_nameCH" value="<pg:cell colName="nameCH"/>" style="display:none">
  </li>
  </pg:list>
    
    
  </ul>
</div>
<div class="foot_position"></div>
<div class="foot_button"><a href="#" onclick="CloseDlg()"><img src="images/cancel.gif" width="78" height="34" alt="取消" /></a>
 <a href="#" onclick="submit()"><img src="images/submit.gif" width="78" height="34" alt="确认" /></a></div>
</body>
</html>
