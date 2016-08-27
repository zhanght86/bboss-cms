<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String orgId = request.getParameter("orgId");
	String did = request.getParameter("did");
	String typeName = request.getParameter("typeName");
	
%>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../../css/navigator.css">
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
		<%@ include file="/include/css.jsp"%>		
	</head>
	<frameset name="orgDictdataTree"  rows="90,10" frameborder="no" border="0" framespacing="0" >
		<frame name="dictDataTree" src="org_dictdataTree.jsp?orgId=<%=orgId%>&did=<%=did%>&typeName=<%=typeName%>"  scrolling="yes" noresize="noresize"  />
		<frame name="orgTaxcodeOpt" src="orgTaxcodeOpt.jsp"  scrolling="no" noresize="noresize"  />
	</frameset>	
	<noframes>
	<body>
	</body>
	
	</noframes>
</html>
