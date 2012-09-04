<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
%>
<html>
    <body class="contentbodymargin" scroll="no">
    <br><br>
    <p align="center"><strong><--请选择字典类型</strong>
    </body>
</html>


