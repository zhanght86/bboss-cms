<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.portal.PortalUtil"%>
<%@page import="com.frameworkset.platform.portal.PortalIssueManager"%>
<%@page import="com.frameworkset.platform.portal.PortalIssueManagerImpl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
AccessControl control = AccessControl.getInstance();
control.checkAccess(request,response);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

//发布的模块数
int moduleCount = Integer.parseInt(request.getParameter("moduleCount"));
String msg = null;
try{
	PortalIssueManager manager = new PortalIssueManagerImpl();
	manager.appIssueIframePlugin(moduleCount,request);
}catch(Exception e){
	msg = PortalUtil.formatErrorMsg(e.getMessage());
}

if(msg == null){
%>
<script type="text/javascript">
<!--
	alert("发布成功!请进入“发布文件管理”列表中下载");
//-->
</script>
<%}else{%>
<script type="text/javascript">
<!--
	alert("发布失败!\n<%=msg%>");
//-->
</script>
<%} %>


