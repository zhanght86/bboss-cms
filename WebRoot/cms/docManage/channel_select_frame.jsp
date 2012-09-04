<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.sql.*,java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*,com.frameworkset.platform.cms.util.CMSUtil"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String docidStr = request.getParameter("docidStr");
	String channelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");
	String newDoc = request.getParameter("newDoc");
	newDoc = newDoc==null?"":newDoc;
	//是否 被转发页面调用
	String framesrc = "channel_select.jsp";
	String framesubmitsrc = "channel_select_submit.jsp";
	String opt = request.getParameter("opt");
	opt = opt==null?"":opt;
	if(opt.length()>0){
	    framesrc = "channel_muti_select.jsp";
	    framesubmitsrc = "channel_muti_select_submit.jsp";
	}
	
    Channel channel = CMSUtil.getChannelCacheManager(siteid).getChannel(channelid)	;
%>
<script language="javascript">
	
</script>
<html>
	<head>
		<title>:::将当前频道[<%=channel.getDisplayName()%>]的文档转发到其他频道::::::</title>
	</head>
	<frameset rows="80%,*" border=0>
		<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="chn_slt_top" src="<%=framesrc%>?siteid=<%=siteid%>&docidStr=<%=docidStr%>&channelid=<%=channelid%>"></frame>
		<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="chn_slt_ok" src="<%=framesubmitsrc%>?newDoc=<%=newDoc%>&docidStr=<%=docidStr%>&channelid=<%=channelid%>&docsiteid=<%=siteid%>" ></frame>
	</frameset>
</html>