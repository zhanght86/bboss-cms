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
var appInfoId = '${appInfoId}';
$(document).ready(function() {
});  
 
  function containSpecial( s ) {  
	  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
      return ( containSpecial.test(s) );      
 }
  
	function CloseDlg(){
		   api.close();
	 }
	
	function submit(){
		var selectPicRadio = $('input[name="selectPic"]:checked');
		if(selectPicRadio != null && selectPicRadio.length > 0){
			picName = $('input[name="selectPic"]:checked').val();
		}else{
			alert("请先选择一条记录查看");
			return;
		}
		$.ajax({
	 	 	type: "POST",
			url : "selectPicForApp.page",
			data :{appId:appInfoId , picName:picName},
			dataType : '',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if("success"==data){
					W.$.dialog.alert("操作成功", function() {
						 api.close(); 
					}, api);
				}else{
					W.$.dialog.alert(data, function() {}, api);
				}
			}	
		 });
	}

  
</script>
</head>
	<body>
<div class="title_system">请选择您需要的系统图片：</div>
<div class="system_list">
  <ul>
  <pg:list  autosort="false" requestKey="datas">
  <li id="<pg:cell colName="id"/>">
  <img src="${pageContext.request.contextPath}/application/app_images/<pg:cell colName="name"/>" width="50" height="50" /> 
  <span><pg:notequal colName="name"  value="${picSelected}"><input id="<pg:cell colName="id"/>_check" name="selectPic" type="radio" value="<pg:cell colName="name"/>"/></pg:notequal>
		<pg:equal colName="name" value="${picSelected}"><input id="<pg:cell colName="id"/>_check" name="selectPic" type="radio"  checked="checked" value="<pg:cell colName="name"/>"/></pg:equal>
  <input type="text" id = '<pg:cell colName="id"/>_app' value='${appInfoId}' style="display:none">
  </li>
  </pg:list>
  </ul>
</div>
<div class="foot_position"></div>
<div class="foot_button"><a href="#" onclick="CloseDlg()"><img src="images/cancel.gif" width="78" height="34" alt="取消" /></a>
 <a href="#" onclick="submit()"><img src="images/submit.gif" width="78" height="34" alt="确认" /></a></div>
</body>
</html>
