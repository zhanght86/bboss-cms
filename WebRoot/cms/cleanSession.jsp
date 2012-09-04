<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="java.util.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor"%>
<%@page import="com.frameworkset.platform.cms.driver.publish.impl.APPPublish"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String uuid = request.getParameter("uuid");
    response.setHeader("Cache-Control", "no-cache"); 
    response.setHeader("Pragma", "no-cache"); 
    response.setDateHeader("Expires", -1);  
    response.setDateHeader("max-age", 0);   
    
    session.removeAttribute(uuid);
    session.removeAttribute("pageUrl"+uuid);
    //System.out.println("清空session:"+uuid); 
%>
