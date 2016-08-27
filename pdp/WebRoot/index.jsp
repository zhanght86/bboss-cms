<%@ page language="java" session="false" contentType="text/html; charset=utf-8"%>
<%--<jsp:forward page="main.frame"/> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<style type="text/css">
html,body{margin: 0;padding: 0; overflow: hidden;height: 100%;}
</style>
</head>
<body>
	<iframe scrolling="no" frameborder="0" src="main.frame?subsystem_id=<%=request.getParameter("subsystem_id")%>"  style="width: 100%;height: 100%;border: 0"></iframe>
</body>
</html>
