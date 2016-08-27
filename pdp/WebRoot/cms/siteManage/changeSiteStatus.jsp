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
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<title>修改站点状态</title>
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

	document.all.siteForm.submit();
	
}
function updateParentForm()
{	
	window.returnValue = siteForm.siteStatus.options[siteForm.siteStatus.selectedIndex].text;
}

</script>
</head>
<body bgcolor="#F7F8FC"  leftmargin="0" topmargin="0">
<form id="siteForm" name="siteForm"  target="exeman" action="changeSiteStatus_do.jsp" method="post">
	<input name="siteId" type="hidden" id="siteId" value="<%=siteId%>">
	<table width="100%" border="0" cellpadding="3" cellspacing="0" align="center" class="Templateedittable">
		<tr>
			<td colspan="2" class="topDIV"  align="left" valign="top">&nbsp;<div class="siteCTitle">修改站点状态</div></td>
		</tr>
		<tr>
			<td width="100" align="right" nowrap bgcolor="#EDEFF6">站点中文名称:</td>
			<td height="24">
				<%=(siteName==null?"&nbsp;":siteName)%>
			</td>
		</tr>
		<tr>
			<td rowspan="2" align="right" nowrap bgcolor="#EDEFF6">站点状态:</td>
			<td height="24">
				<%
					String status = site.getStatus()+"";
					List list = sm.getSiteStatusList();
				%>
				<select id="siteStatus" name="siteStatus" class="cms_select" style="width:130px">
					<%= JSPFunc.getOption(list,"".equals(status)?"1":status) %>
				</select>
			</td>
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