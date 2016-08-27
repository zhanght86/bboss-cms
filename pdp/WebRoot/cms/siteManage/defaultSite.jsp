<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page  import="com.frameworkset.platform.cms.sitemanager.SiteManager,com.frameworkset.platform.cms.sitemanager.SiteManagerImpl"%>
<%@page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String username = control.getUserAccount();
	String userId = request.getParameter("userId");
	SiteManager smi = new SiteManagerImpl();
	String siteId = smi.userDefaultSite(userId);
	
	if(siteId==null || siteId.equals("")){
		siteId ="";
	}
	else
	{
		siteId = smi.userDefaultSite(userId);
	}
	
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<title>设置默认站点</title>
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
function checkForm(){
	var defaultSiteForm = document.all.defaultSiteForm;
	document.all.defaultSiteForm.submit();
	
}

</script>
</head>
<body bgcolor="#F7F8FC" leftmargin="0" topmargin="0">
<form id="defaultSiteForm" name="defaultSiteForm"  target="exeman" action="defaultSite_do.jsp" method="post">
	<input name="userId" type="hidden" id="userId" value="<%=userId%>">
	<table width="100%" border="0" cellpadding="3" cellspacing="0" align="center" class="Templateedittable">
		<tr>
			<td colspan="2" class="topDIV"  align="left" valign="top">&nbsp;<div class="siteCTitle">默认站点设置</div></td>
		</tr>
		<tr>
			<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6">当前用户帐号:</td>
			<td height="24">
				<%=username%>
			</td>
		</tr>
		<tr>
			<td height="24" rowspan="2" align="right" nowrap bgcolor="#EDEFF6">默认站点:</td>
			<td height="24">
					<select name="siteId" style="width:200px" >
		                  	<%
		                  	com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
							cmsManager.init(request,session,response,control);
		                    List sitelist = cmsManager.getSiteAllRuningList();
							request.setAttribute("sitelist",sitelist);
						
							%>
							<option value="">无</option>
		          			<pg:list requestKey="sitelist">
			          				<option <pg:equal colName="siteId" value="<%=siteId%>">selected</pg:equal>
			          				value="<pg:cell colName="siteId"/>">
			         				<pg:cell colName="name"/>
			         			
		          			</pg:list>
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