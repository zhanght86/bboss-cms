<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.GroupCacheManager"%>
<%
/**
 * 
 * <p>Title: 用户组刷新缓冲处理页面</p>
 *
 * <p>Description: 用户组刷新缓冲处理页面</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2008-10-14
 * @author gao.tang
 * @version 1.0
 */
 %>

<%
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request, response);
	
	String errorMessage = "";
	GroupCacheManager groupCacheManager = GroupCacheManager.getInstance();
	try{
		groupCacheManager.reset();
	}catch(Exception e){
		errorMessage = e.getMessage();
	}
%>
<script language="javascript">
<%if("".equals(errorMessage)){ %>
	alert("操作成功！");
<%}else{ %>
	alert("操作失败，原因如下：\\n<%=errorMessage %>");
<%} %>
</script>
