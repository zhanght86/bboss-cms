<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><%@page import="com.frameworkset.platform.security.AccessControl"%><%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request,response);
    int count = accesscontroler.getLoginUserCount();
    out.print(count);
    //out.flush();
    //out.close();
%>
