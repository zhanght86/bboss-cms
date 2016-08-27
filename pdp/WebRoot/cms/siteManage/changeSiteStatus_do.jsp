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

	String siteId = request.getParameter("siteId");
	String siteStatus = request.getParameter("siteStatus");
	if(siteStatus==null || siteStatus.trim().length()==0){
		siteStatus = "0";
	}

	SiteManager smi = new SiteManagerImpl();
	smi.updateSiteStatus(Integer.parseInt(siteId),Integer.parseInt(siteStatus));
%>
<script language="javascript">
	alert("改变站点状态成功!");
	parent.updateParentForm();
	parent.close();
	parent.parent.window.open("../top.jsp","perspective_topbar");
</script>
<%}catch (SiteManagerException e) {
		e.printStackTrace();
%>
<script language="javascript">
	alert('改变站点状态出错,请检查!原因是:\n<%=e.getMessage().replaceAll("\n", "").replaceAll("'","")%>');
</script>
<%
}
%>
