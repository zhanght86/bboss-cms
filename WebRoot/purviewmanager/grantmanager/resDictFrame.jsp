<%
/**
 * 
 * <p>Title: 字典授权框架页面</p>
 *
 * <p>Description: 字典授权框架页面-右边显示字典树，右边显示字典操作项</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String currOrgId = "";
	if(role_type.equals("user")){
		currOrgId = request.getParameter("currOrgId");
	}
	String isBatch = request.getParameter("isBatch");
	
%>
<html>
<frameset name="dictFrame" cols="30,70" frameborder="no" border="0" framespacing="0" >
  	<frame src="dictsetTree.jsp?resTypeId=dict&currRoleId=<%=currRoleId%>&currOrgId=<%=currOrgId%>&role_type=<%=role_type%>&isBatch=<%=isBatch %>" name="globalOperList" id="globalOperList" scrolling="No" noresize="noresize" />
  	<frame src="operdefault.jsp" name="operList" scrolling="No" noresize="noresize" id="orgList" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
