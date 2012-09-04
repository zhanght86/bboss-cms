<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.cms.workflowmanager.*"%>
<%@ taglib uri="../../WEB-INF/pager-taglib.tld" prefix="pg"%>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(pageContext);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String workflowId = request.getParameter("workflowId");

	WorkflowManager workflowManager = new WorkflowManagerImpl();
	Workflow workflow = workflowManager.getWorkflow(workflowId);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>修改流程名称</title>
<script src="../inc/js/func.js"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="javascript" >

function setFocus()
{
	document.forms[0].workflowName.focus();
}

function checkForm()
{
	if(document.forms[0].workflowName.value == "")
	{
		alert("请填写流程名称！");
		document.forms[0].workflowName.focus();
		return false;
	}
	document.forms[0].action = "workflow_update_do.jsp";
	document.forms[0].target = "updateworkflowiframe";
	document.forms[0].submit();
}

</script>
<style type="text/css">
	.topDIV {
		background-image: url(../images/toolbar_common_func_site.jpg);
		height: 82px;
		padding-top: 7px;
		padding-left: 55px;
	}

	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
</head>

<body bgcolor="#F7F8FC"  onLoad="setFocus();">
<form id="workflowForm" name="workflowForm" method="post">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center">
		<tr >
			<td colspan="2" class="topDIV"  align="left" valign="top">&nbsp;<div class="siteCTitle">修改流程名称</div></td>
		</tr>
		<tr >
			<td width="4%" height="20" align="right">
				<img src="../images/ico_point.gif" width="25" height="25">
			</td>
			<td height="20" class="cms_title_blue">
				流程基本信息
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="hidden" name="workflowId" value="<%=workflow.getWorkflowId()%>" >
				<table width="98%" border="0" cellpadding="3" cellspacing="0" class="Templateedittable">
					<tr>
						<td width="130" height="24" align="right" nowrap>流程名称:</td>
						<td height="24">
							<input type="text" name="workflowName" value="<%=workflow.getWorkflowName()%>" class="cms_text"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		
		<tr>			
		</tr>
	</table>
	<br>
	<div style="text-align:center">

		<INPUT name="button" type="button" class="cms_button" onClick="checkForm()"  value="保存">

		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.returnValue = 'false';window.close()"  value="关闭">
	</div>
	
</form>
<iframe name="updateworkflowiframe" width="0" height="0" style="display:none"></iframe>
</body>
</html>
