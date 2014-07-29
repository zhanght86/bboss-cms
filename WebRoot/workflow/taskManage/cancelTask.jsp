<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：撤销任务填写原因
作者：谭湘
版本：1.0
日期：2014-07-10
 --%>	
 <%
  	request.setAttribute("processKey", request.getParameter("processKey"));
 	request.setAttribute("processId", request.getParameter("processId"));
 	request.setAttribute("taskId", request.getParameter("taskId"));
 
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>撤销任务填写原因</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">

</script>
</head>
	
<body>
<div class="form">
	<form id="cancelTaskFrom" name="cancelTaskFrom" method="post" >
		
		<table border="0" cellpadding="0" cellspacing="0" class="table2" width="100%" >
			<tr>
				<th width="100px">撤销原因：</th>
				<td width="220px">
					<textarea rows="8" cols="50" id="cancelTaskReason" name="cancelTaskReason"  
						class="w120" style="width: 280px;font-size: 12px;height:40px;" maxlength="200"></textarea>
				</td>
			</tr>
		</table>			
		
		<div class="btnarea" >
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
			<input type="reset" id="reset" style="display: none;" />
		</div>
	</form>
</div>
</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
function dosubmit(){
	var cancelTaskReason = $.trim($("#cancelTaskReason").val());
	
	if(cancelTaskReason ==''){
		alert("请填写撤销原因");
		return;
	}
	 
	$.dialog.confirm('确定撤销任务吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/taskManage/cancelTask.page",
			data: {"processKey":'${processKey}',"processId":'${processId}',
				   "taskId":'${taskId}',"cancelTaskReason":cancelTaskReason},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("撤销任务出错:"+data);
				}else {
					W.modifyQueryData();
					api.close();	
				}
			}
		 });
	},function(){});  
}
	
</script>
</html>
