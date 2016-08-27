<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String isUserRes = request.getParameter("isUserRes");//控制查询条件开关,为is时,查询用户为当前用户
    if(isUserRes == null)
    {
    	isUserRes ="no";
    }
    String typeName = request.getParameter("typeName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
 <HEAD>
   <title>权限资源查询</title> 
 </HEAD>
	<frameset rows="130,*" border=0>
	<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="forQuery" src="<%=request.getContextPath()%>/sysmanager/resmanager/res_query.jsp?isUserRes=<%=isUserRes%>&typeName=<%=typeName %>" ></frame>
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="forDocList" src="<%=request.getContextPath()%>/sysmanager/user/userres_querylist.jsp?isUserRes=<%=isUserRes%>&typeName=<%=typeName %>"></frame>
	</frameset>	
</HTML>
