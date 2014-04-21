<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.util.StringUtil"%><%
	/**
	* 用户推出系统时，可以退出到用户指定的页面地址，如果没有指定就退出到缺省的登录页面
	* 即login.jsp
	* 同时用户还可指定退出的目的窗口，如果没有指定就退出到top窗口
	*/	
	String redirect = request.getParameter("_redirectPath");//"http://172.16.17.26:9080";
	if(redirect == null || redirect.trim().equals(""))
	{
		redirect = AccessControl.getSubSystemLogoutRedirect(request);
		if(redirect == null || redirect.trim().equals(""))
			redirect = request.getContextPath() + "/login.jsp";
	}
	/**
	MemTokenManager memTokenManager = MemTokenManagerFactory.getMemTokenManagerNoexception();
	if(memTokenManager != null)//如果开启令牌机制就会存在memTokenManager对象，否则不存在
	{
		redirect = memTokenManager.appendDTokenToURL(request,redirect);
	}*/
	String target = request.getParameter("_redirecttarget");
	if(target == null || target.trim().equals(""))
		target = "top";
	
%>

<head>	
	<META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
	<META  HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
	<META HTTP-EQUIV="Expires" CONTENT="0"/>	
	
	
	<script language="javascript">		
				
					
		
		
	
				<%if(target.equals("location")){%>
					window.location = "<%=redirect%>";
					<%}
					else
					{%>
						//alert(top);
						window.<%=target%>.location = "<%=redirect%>";				
					<%}%>		
	
		
	</script>
</head>





