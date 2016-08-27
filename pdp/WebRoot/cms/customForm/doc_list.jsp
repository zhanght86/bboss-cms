<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
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
	ChannelManager channelManager = new ChannelManagerImpl();
	Channel channel = channelManager.getChannelInfo(channelId);

	//取频道所设定的自定义表单
	CustomFormManager cfm = new CustomFormManagerImpl();
	String path = cfm.getCustomFormFilename(channelId,"2","3");
%>
<html>
	<frameset rows="145,*" border=0>
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="forQuery2" src="queryFrame.jsp?channelName=<%=channelName%>&siteid=<%=siteid%>&channelId=<%=channelId%>"></frame>	
	<frame frameborder=0  noResize scrolling="auto" marginWidth=0 name="forDocList2" src="list.jsp?channelName=<%=channelName%>&siteid=<%=siteid%>&channelId=<%=channelId%>"></frame>
	</frameset><noframes></noframes>
</html>
