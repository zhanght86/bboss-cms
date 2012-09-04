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
	String resName = request.getParameter("resName2");
	
	//是否批量授予资源
	String isBatch = request.getParameter("isBatch");
	
	//System.out.println(resId2);
	//System.out.println(resTypeId2);
	//System.out.println(resTypeName);
	//System.out.println(title);
%>
<html>
<head>
<title>授予用户</title>
</head>
<!--iframe src="../../sysmanager/resmanager/hasPower_ajax.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>&resTypeName=<%=resTypeName%>&title=<%=title%>"  border=0 scrolling="no" id="docVerListFrame" name="docVerListFrame" height="100%" width="100%"></iframe-->
	<frameset cols="25%,*" border=0>
		<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="res_user_tree" src="res_user_tree.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>&resTypeName=<%=resTypeName%>&title=<%=title%>&resName2=<%=resName %>&isBatch=<%=isBatch %>">
		</frame>
		<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="res_user_list" src="res_org_userlist.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>&resTypeName=<%=resTypeName%>&title=<%=title%>&resName2=<%=resName %>&isBatch=<%=isBatch %>">
		</frame>
	</frameset>
</html>
