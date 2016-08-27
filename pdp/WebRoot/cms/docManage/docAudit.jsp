<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
<frameset rows="80,*" border=0>
	<frame  marginwidth="0" frameborder=0 scrolling="auto" noresize name="auditDocQueryF" src="<%=rootpath%>/cms/docManage/doc_audit_query.jsp"></frame>
	<frame frameborder=0 marginwidth="0" scrolling="auto" noresize name="auditDocListF" src="<%=rootpath%>/cms/docManage/doc_audit_list.jsp"></frame>		
</frameset><noframes></noframes>
</html>