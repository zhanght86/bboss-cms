<%
/**
 * 
 * <p>Title: 用户自定义资源frame页面</p>
 *
 * <p>Description: 用户自定义资源frame页面，右边显示自定义资源树，右边显示明细</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String roleId = request.getParameter("roleId");
%>

<html>
<frameset name="dictFrame" cols="30,70" frameborder="no" border="0" framespacing="0" >
  	<frame src="../../grantmanager/resoucesetTree.jsp?resTypeId=rescustom&currRoleId=<%=roleId%>&role_type=organization" name="globalOperList" id="globalOperList" scrolling="No" noresize="noresize" />
  	<frame src="operdefault.jsp" name="operList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
