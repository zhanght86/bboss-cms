<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看任务详情</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
$(document).ready(function() {
			
	 	    
});
</script>
</head>
	
<body>
<div class="form">
	<div class="mcontent">
		<div id="searchblock">
			<form id="viewForm" name="viewForm">
				<fieldset id="viewResouceSet">
					<legend>执行图</legend>
					<img id="pic" src="${pageContext.request.contextPath}/workflow/repository/getProccessActivePic.page?processInstId=${processInstId}" />
				</fieldset>
				<fieldset >
					<legend>处理记录</legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
				       	<th>时间</th>
				       	<th>节点名</th>
				       	<th>签收人</th>
				       	<th>操作内容</th>
				       	<th>处理意见</th>
				      </pg:header>	
					<pg:list requestKey="list">
					   	<tr height="25px">
					    	<td><pg:cell colName="startTime" dateformat="yyyy-MM-dd HH:mm:ss"/></td>   
					    	<td><pg:cell colName="name" /></td>     
					    	<td><pg:cell colName="assignee" /></td>  
					    	<td><pg:cell colName="deleteReason" /></td>
					    	<td><pg:cell colName="" /></td> 
					    </tr>
					 </pg:list>
				</table>
				</fieldset>
			</form>
		</div>
  	</div>	
  		
	<div class="btnarea" >
		<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
	</div>	
</div>
</body>
</html>
