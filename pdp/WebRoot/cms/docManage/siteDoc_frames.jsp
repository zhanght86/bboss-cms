<%
	/**
	  * 站内文档查询
	  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String channelName = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	
%>
<html>
	<frameset rows="145,*" border=0>
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="sitedocQuery" src="<%=rootpath%>/cms/docManage/siteDoc_queryFrame.jsp?channelName=<%=channelName%>&siteid=<%=siteid%>&channelId=<%=channelId%>"></frame>	
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="sitedocList" src="<%=rootpath %>/cms/docManage/siteDoc_list.jsp?channelName=<%=channelName%>&siteid=<%=siteid%>&channelId=<%=channelId%>"></frame>
	</frameset><noframes></noframes>
</html>
