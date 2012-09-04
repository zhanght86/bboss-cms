<%@page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.menu.MainColumn" %>
<%@page import="com.frameworkset.platform.security.AccessControl
                ,com.frameworkset.platform.security.authorization.AccessException"%>

    <%
    
    MainColumn column = new MainColumn();  
    
    AccessControl control = AccessControl.getInstance();
    
    control.checkAccess(pageContext);
    column.init(pageContext,control);
    column.buildColumn();

    %>


