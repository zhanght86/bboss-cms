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

	String flag = request.getParameter("flag");
	String channelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");
	String title = "::::::文档选择::::::::::::::::::::::.";
	if(flag.equals("2"))
		title =":::从其他频道复制文档到当前频道::::::::";
	else if(flag.equals("3"))
			title =":::从其他频道引用文档到当前频道::::::::";
	else if(flag.equals("4"))	
			title =":::从其他频道剪切文档到当前频道::::::::"; 
	else if(flag.equals("5"))
			title =":::请选择一篇文档::::::::";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title><%=title%></title> 
<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.style3 {
		font-size: 14px;
		font-weight: bold;
		color: #3300FF;
	}
	.operStyle{
	width:17;
	height:16;
	}
</style>
<SCRIPT LANGUAGE="JavaScript">
	
</SCRIPT>
</head>
	<frameset rows="85%,*" border=0>
	<frame frameborder=0  noResize scrolling="yes" marginWidth=0 name="chn_doc_top" src="channel_doc_top.jsp?siteid=<%=siteid%>&flag=<%=flag%>"></frame>
	<frame   frameborder=0  noResize scrolling="yes" marginWidth=0 name="chn_doc_ok" src="channel_doc_submit.jsp?siteid=<%=siteid%>&flag=<%=flag%>&channelid=<%=channelid%>"></frame>
	</frameset>
</html>