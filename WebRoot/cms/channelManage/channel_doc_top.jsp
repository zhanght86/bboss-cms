<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String siteid = request.getParameter("siteid");
	String flag = request.getParameter("flag");
			//String channelName = request.getParameter("channelName");
			//String siteid = request.getParameter("siteid");
			//String channelid = request.getParameter("channelid");
			//ChannelManager channelManager = new ChannelManagerImpl();
			//Channel channel = channelManager.getChannelInfo(Integer.parseInt(channelId));
			//String listdocumenttemplate =channel.getListdocumenttemplate_id();
			//if(listdocumenttemplate == null || listdocumenttemplate.equals(""))
			//	listdocumenttemplate = "cms/docManage/doc_list.jsp";
%>
<html>
	<frameset cols="25%,*" border=0>
	<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="chanenl_tree" src="channel_tree.jsp?siteid=<%=siteid%>&flag=<%=flag%>"></frame>
	<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="chn_doc_list" src="doc_list.jsp?siteid=<%=siteid%>"></frame>
	</frameset>
</html>