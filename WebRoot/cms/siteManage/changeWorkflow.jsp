<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="java.util.*" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String siteId = request.getParameter("siteId");
	if(siteId == null || siteId.trim().length()==0){
	%>
		<script language="javascript" type="text/javascript">
			alert("站点id为空,无法编辑站点.");
			self.location.href = "about:blank";
		</script>
	<%
		return;
	}
	SiteManager sm = new SiteManagerImpl();
	Site site = sm.getSiteInfo(siteId) ;

	String siteName = site.getName();
	int workflowFromParent = site.workflowIsFromParent();
	long parentId = site.getParentSiteId();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<title>修改站点流程</title>
<style type="text/css">
	.topDIV {
		background-image: url(../images/toolbar_common_func_site.jpg);
		height: 82px;
		padding-top: 7px;
		padding-left: 55px;

	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
<script language="javascript">

//站点窗口输入参数校验
function checkForm(){
	var siteForm = document.all.siteForm;
	document.all.siteForm.submit();
	
}
function updateParentForm()
{

	<%if(parentId>0){ %>			
				
	if(!document.all.siteForm.workFlow.disabled)
	{
		window.returnValue = siteForm.workFlow.options[siteForm.workFlow.selectedIndex].text;
	}else{
		window.returnValue = "继承父站点的流程设置";
	}	
	<%}else{%>
		window.returnValue = siteForm.workFlow.options[siteForm.workFlow.selectedIndex].text;
	<%}%>
}

function changeWorkFlowType(type){
	if(0==type){
		document.all.siteForm.workFlow.disabled = false;
	}else{
		document.all.siteForm.workFlow.disabled = true;
	}
}

</script>
</head>
<body bgcolor="#F7F8FC" leftmargin="0" topmargin="0" >
<form id="siteForm" name="siteForm"  target="exeman" action="changeWorkflow_do.jsp" method="post">
	<input name="siteId" type="hidden" id="siteId" value="<%=siteId%>">
	<table width="100%" border="0" cellpadding="3" cellspacing="0" align="center" class="Templateedittable">
		<tr>
			<td colspan="2" class="topDIV"  align="left" valign="top">&nbsp;<div class="siteCTitle">修改站点流程</div></td>
		</tr>
		<tr>
			<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6">站点中文名称:</td>
			<td height="24">
				<%=(siteName==null?"&nbsp;":siteName)%>
			</td>
		</tr>
		<tr>
			<td height="24" rowspan="2" align="right" nowrap bgcolor="#EDEFF6">工作流程:</td>
			<td height="24">
				<%if(parentId>0){ %>
					<input type="radio" name="workFlowFrom" value="0" <%=0==workflowFromParent?"checked":""%> onClick="changeWorkFlowType(0)">
				<%}
				String wfName = "";
				String wfId = "";
				if(siteId!=null && !"".equals(siteId)){
					List workflowInfo = sm.getFlowInfo(siteId);
					if(workflowInfo!=null && workflowInfo.size()>=2){
						if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
							wfId = workflowInfo.get(0).toString();
							wfName = workflowInfo.get(1).toString();
						}
					}
				}
				FlowManager fm = new FlowManagerImpl();
				List list = fm.getFlowList();
				%>
				<select id="workFlow" name="workFlow" class="cms_select" style="width:130px"
					<%=(parentId<=0 || workflowFromParent==0?"":"disabled")%>>
					<%= JSPFunc.getOption(list,"".equals(wfId)?"1":wfId) %>
				</select>
			</td>
		</tr>
		<tr>
			<%if(parentId>0){
				wfName = "";
				wfId = "";
				List workflowInfo = sm.getFlowInfo(""+parentId);
				if(workflowInfo!=null && workflowInfo.size()>=2){
					if(workflowInfo.get(0)!=null && workflowInfo.get(1)!=null){
						wfId = workflowInfo.get(0).toString();
						wfName = workflowInfo.get(1).toString();
					}
				}
			%>
			<td height="24">
				<input type="radio" name="workFlowFrom" value="1" <%=0!=workflowFromParent?"checked":""%> onClick="changeWorkFlowType(1)">
				继承父站点的流程设置(<span style="color: #FF0000"><%=wfName%></span>)
			</td>
			<%}%>
		</tr>
  </table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" type="button" class="cms_button" onClick="checkForm()"  value="保存">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.close()"  value="关闭">
	</div>
</form>
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>