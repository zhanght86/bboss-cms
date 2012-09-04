<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

%>
<html>
<head>
<title>特殊权限资源查询</title>
</head> 
<frameset  rows="18%,*"  border="0"  >
  <frame src="special_query.jsp" name="query" id="query"  scrolling="No" noresize="noresize"/>
  <frame src="special_querylist.jsp" name="querylist"  noresize="noresize" id="querylist" />
</frameset>

<body>
</body>

</html>

