<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.liferay.portlet.iframe.action.UserMapping"%>
<%@page import="com.liferay.portlet.iframe.action.SSOUserMapping"%>
<%
		try { 
			UserMapping um = new UserMapping();
			String A_APP_ID = request.getParameter("A_APP_ID");
			String A_USER_NAME    = request.getParameter("A_USER_NAME");
			String B_APP_ID   = request.getParameter("B_APP_ID");
			String B_USER_NAME    = request.getParameter("B_USER_NAME");
			String B_USER_PASSWORD    = request.getParameter("B_USER_PASSWORD");
			String USER_REMARK    = request.getParameter("USER_REMARK");
			int ID    = Integer.parseInt(request.getParameter("pID"));
			um.setA_APP_ID(A_APP_ID);
			um.setA_USER_NAME(A_USER_NAME);
			um.setB_APP_ID(B_APP_ID);
			um.setB_USER_NAME(B_USER_NAME);
			um.setB_USER_PASSWORD(B_USER_PASSWORD);
			um.setUSER_REMARK(USER_REMARK);
			um.setID(ID);
			SSOUserMapping ssoum = new SSOUserMapping();
			
			if(ssoum.modifyMapping(um)) {				
		 	%>
		 	<script type="text/javascript">
				alert("操作成功!");
				window.location.href="sso-user-mapping.jsp";
			</script>
		 	<%
			}else {
				%>
			 	<script type="text/javascript">
					alert("操作失败!");
					window.location.href="sso-user-mapping.jsp";
				</script>
		 	<%
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
 %>
	
	  
	
