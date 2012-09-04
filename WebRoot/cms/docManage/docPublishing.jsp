<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
<frameset rows="20%,*" border=0>
	<frame  frameborder="0" marginWidth=0  scrolling="auto" noresize name="docPublishingQueryF" src="<%=rootpath%>/cms/docManage/docPublishingQuery.jsp"></frame>
	<frame marginWidth=0 frameborder="0" scrolling="auto" noresize name="docPublishingListF" src="<%=rootpath%>/cms/docManage/docPublishingList.jsp"></frame>		
</frameset><noframes></noframes>
</html>