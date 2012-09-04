<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
try {
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String userId = request.getParameter("userId");
	String siteId = request.getParameter("siteId");
	

	SiteManager smi = new SiteManagerImpl();
	smi.defaultSite(siteId,userId);
%>
<script language="javascript">
	alert("设置默认站点成功!");
	//parent.updateParentForm();
	parent.close();
</script>
<%}catch (SiteManagerException e) {
		e.printStackTrace();
		throw new SiteManagerException(e.getMessage());

}
%>
