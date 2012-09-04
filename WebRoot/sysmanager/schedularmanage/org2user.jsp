<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.web.struts.form.UserOrgManagerForm" %>
<%
	String displayNameInput=request.getParameter("displayNameInput");
	String displayValueInput=request.getParameter("displayValueInput");
		
%>
<html>
<head>
<title>选择执行人</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>

</head> 
<frameset name="frame1" cols="0,30,70" frameborder="no" border="0" framespacing="0" >
  <frame src="frame_bridge.jsp" name="frame_bridge" scrolling="No" noresize="noresize" id="frame_bridge" />
  <frame src="orgTree.jsp" name="orgTree" id="orgTree" />
  <frame src="userList.jsp?displayNameInput=<%=displayNameInput%>&displayValueInput=<%=displayValueInput%>" name="userList" scrolling="No" noresize="noresize" id="userList" />  
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

