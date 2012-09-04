<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request,response);
%>
<%--
	描述：物料领用测试用户列表
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>物料领用测试用户列表</title>
		<%@ include file="/common/jsp/css.jsp"%>
		<script type="text/javascript">
			$(document).ready(function() {	
				 $("#wait").hide();
				 var user = '<%=accesscontroler.getUserAccount()%>';
				 queryUserTaskList(user);
			});
			
			function queryTaskList()
			{
				$("#custombackContainer").load("showTaskListByUser.page #taskList",
						function(){
						loadjs();
						});
			}
			
			function queryUserTaskList(user)
			{
				$("#custombackContainer").load("showTaskListByUser.page?username=" + user + " #taskList",function(){
					loadjs();
				});
			}
       	 </script>
	</head>
	<body>
		<div class="mcontent">
			<sany:menupath />
			<div id="searchblock">
			<form name="applyForm" method="post">
			<!--  class="collapsible"  收缩 -->
			<fieldset>
				<legend>用户待办</legend>
			</fieldset>
			</form>
			</div>
			<div id="custombackContainer" >
			
			</div>
		</div>
	</body>
<script language="javascript">

   function dosubmit()
   {
		document.applyForm.action="apply.page";
		document.applyForm.submit();
   	 }
function doreset(){
	$("#reset").click();
}
</script>