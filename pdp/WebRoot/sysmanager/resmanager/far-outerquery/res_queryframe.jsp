<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
/**
 * 
 * <p>Title: 资源反查询，查询条件页面</p>
 *
 * <p>Description: 资源反查询，查询条件页面</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: bboss</p>
 * @Date 2008-11-4
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    if(!accesscontroler.checkManagerAccess(request, response)){
    	return;
    }
    
    
%>
<HTML>
 <HEAD>
   <title>权限资源查询</title>
 
 </HEAD>
 	<frameset rows="18%,*" border=0>
	<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="forQuery" src="res_query.jsp"></frame>
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="forDocList" src="res_querylist.jsp?type=user"></frame>
	</frameset>	
</HTML>
