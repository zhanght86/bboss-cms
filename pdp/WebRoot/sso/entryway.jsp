<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.platform.security.authorization.AccessException,com.frameworkset.platform.config.ConfigManager,com.liferay.portlet.iframe.action.SSOUserMapping"%>
<%
String successRedirect = request.getParameter("successRedirect");
boolean isCasServer = ConfigManager.getInstance().getConfigBooleanValue("isCasServer", false);
if (isCasServer)
{
    String userName = (String) session.getAttribute("edu.yale.its.tp.cas.client.filter.user");
    boolean state = false;
    if (userName != null && !"".equals(userName))
    {
        state = SSOUserMapping.isIncludeUser(userName);
    }
    if (state)
    {
        //系统管理版本号，2.0和2.0以上的版本,默认版本为1.0
        String systemVersion = ConfigManager.getInstance().getConfigValue("system.version", "1.0");
        String subsystem = request.getParameter("subsystem_id");
        String password = SSOUserMapping.getUserPassword(userName);
        if (subsystem == null)
            subsystem = "module";
        
        
        if (systemVersion.compareTo("1.0") > 0)
        {
            AccessControl.getInstance().login(request, response, userName, password);

            //  if(subsystem == null) subsystem = "module";
            /** 
            需要全屏时，将response.sendRedirect("index.jsp");注释掉，
            将response.sendRedirect("sysmanager/refactorwindow.jsp");打开
             */
            if(successRedirect == null)
            {
            	successRedirect = "../index.jsp?subsystem_id=" + subsystem;
            }
            response.sendRedirect(successRedirect);
        }
        else
        {
            AccessControl control = AccessControl.getInstance();
            control.checkAccess(request, response, false);
            String user = control.getUserAccount();
            if (user == null || "".equals(user) || !userName.equals(user))
            {
                AccessControl.getInstance().login(request, response, userName, password);
                if (subsystem == null)
                    subsystem = "module";
                if(successRedirect == null)
	            {
	            	successRedirect = "../index.jsp?subsystem_id=" + subsystem;
	            }
                response.sendRedirect(successRedirect);
            }
            else
            {
                if (subsystem == null)
                    subsystem = "module";
                if(successRedirect == null)
	            {
	            	successRedirect = "../index.jsp?subsystem_id=" + subsystem;
	            }
                response.sendRedirect(successRedirect);
            }
        }
    }
    else
    {
    	if(userName == null || userName.equals(""))
    	{
        	out.print("系统启用了cas单点登录功能，请在web.xml的CAS Filte中拦截login.jsp页面");
        }
        else
        {
        	out.print("用户【" + userName + "】在此应用中没有开通！ ");
        }
    }
}else{
	out.print("对不起，您没有启用CAS！");
}
%>
<html>
