<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.ca.CAManager"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
AccessControl control = AccessControl.getInstance();
control.checkAccess(request,response);
String path = request.getContextPath();
String userName = request.getParameter("userName");
String errorMessage = null;
try{
	CAManager.resetUserCERT_SN(userName);
}catch(Exception e){
	errorMessage = e.getMessage();
    if (errorMessage != null)
    {
        errorMessage = errorMessage.replaceAll("\\n", "\\\\n");
        errorMessage = errorMessage.replaceAll("\\r", "\\\\r");
    }
}
%>

<script language="javascript">
	<%if(errorMessage == null){%>
		alert("操作成功！");
	<%}else{%>
		alert("操作失败！/n<%=errorMessage%>");
	<%}%>
</script>
