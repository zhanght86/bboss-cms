<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>

<%
//频道选择树，单选多选。
//个人工作平台中的任务（审核任务、发布任务、返工任务、发布中文档、新稿文档、归档文档、回收文档）
//等查询时用到了频道多选；
//文档采集模板编辑时用到了频道单选；
//频道的引用文档管理中，频道引用时用到了频道多选；
//频道编辑以及新增时，发布引用频道首页时用到了频道单选；
%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String type = request.getParameter("type");	  //树类型，值为single	表示单选树，值为null或multi表示多选树
	String tempTitle = ":::多频道选择::::::";
	if("single".equals(type))
		tempTitle = ":::单频道选择::::::";
	
	String siteName = request.getParameter("siteName");
	String siteid = "";
	try{
		DBUtil dbSiteId = new DBUtil();
		String getSiteId = "select site_id from td_cms_site where second_name ='" + siteName + "'";
		dbSiteId.executeSelect(getSiteId);
		siteid = dbSiteId.getString(0,"site_id");
	}catch(Exception e){}
	
	String taskType = request.getParameter("taskType");
	String curChannelid = request.getParameter("channelid");
	
	
%>
<script language="javascript">
	
</script>
<html>
	<head>
		<title><%=tempTitle%></title>
	</head>
	<frameset rows="80%,*" border=0>
		<%if("single".equals(type)){%>
		<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="multi_chn_slt_top" src="single_channel_select.jsp?siteid=<%=siteid%>"></frame>
		<%}else{%>
		<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="multi_chn_slt_top" src="multi_channel_select.jsp?siteid=<%=siteid%>&taskType=<%=taskType%>"></frame>
		<%}%>
		<frame frameborder=0  noResize scrolling="no" marginWidth=0 name="multi_chn_slt_ok" src="multi_channel_select_submit.jsp?taskType=<%=taskType%>&channelid=<%=curChannelid%>" ></frame>	
	</frameset>
</html>