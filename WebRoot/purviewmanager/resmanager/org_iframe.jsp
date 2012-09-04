<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*,com.frameworkset.platform.config.model.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String resId2 = request.getParameter("resId2");
	String resTypeId2 = request.getParameter("resTypeId2");
	//String resTypeName = request.getParameter("resTypeName");
	ResourceInfo res =  com.frameworkset.platform.config.ConfigManager.getInstance().getResources().getResourceInfoByid(resTypeId2);
	String resTypeName = res != null? res.getName():"";
	String title = request.getParameter("title");
	
	String resName2 = request.getParameter("resName2");
	String isBatch = request.getParameter("isBatch");
%>
<html>
<head>
<title>授予组织</title>
</head>
	<frameset cols="25%,*" border=0>
		<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="res_org_tree" src="res_org_tree.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>&resTypeName=<%=resTypeName%>&title=<%=title%>&resName2=<%=resName2 %>&isBatch=<%=isBatch %>">
		</frame>
		<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="res_org_list" src="../userorgmanager/org/properties_content.jsp">
		</frame>
	</frameset>
</html>
