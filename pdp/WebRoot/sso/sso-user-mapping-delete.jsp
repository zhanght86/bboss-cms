<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:directive.page import="com.liferay.portlet.iframe.action.SSOUserMapping" />

<%
	String Id = request.getParameter("ID");
	SSOUserMapping ssoum = new SSOUserMapping();
	if (ssoum.deleteUserMapping(Id)) {
%>
<script type="text/javascript">
				alert("操作成功!");
				window.location.href="sso-user-mapping.jsp";
</script>
<%
} else {
%>
<script type="text/javascript">
				alert("很抱谦,操作失败！");
				window.location.href="sso-user-mapping.jsp";
</script>
<%
}
%>



