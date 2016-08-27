<%@page contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.frameworkset.platform.menu.OutlookbarMenu" %>
<%@page import="com.frameworkset.platform.security.AccessControl
                ,com.frameworkset.platform.security.authorization.AccessException"%>

    <%
    String showMode = request.getParameter("showMode");
    if(showMode == null)
    	showMode = "0";
    
    
    OutlookbarMenu column = new OutlookbarMenu();  
    
    AccessControl control = AccessControl.getInstance();
    
    control.checkAccess(pageContext);
    column.init(pageContext,control);
    column.setShowMode(showMode);
    column.buildColumn();

    %> 


