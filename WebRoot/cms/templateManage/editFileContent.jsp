<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%
	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", -1);
	response.setDateHeader("max-age", 0);

	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);

	String siteId =  cmsmanager.getSiteID();

	SiteManager siteManager = new SiteManagerImpl();

	String sitedir = siteManager.getSiteInfo(siteId).getSiteDir();//站点相对路径
	String fileName = request.getParameter("fileName");
	String uri = request.getParameter("uri");
	
	String relatePath = "";
	
	
	if(uri !=null)
	{
		if(uri.startsWith("/"))
			relatePath = uri.substring(1);
		else
			relatePath = uri;
		if(relatePath.endsWith("/"))
		{
			relatePath = relatePath.substring(0,relatePath.length() - 1);
		}
	}
	System.out.println("--/---"+ relatePath);

%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
.fileContent {
	font-family: Verdana, "宋体";
	width: 100%;
}
-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>文件内容编辑</title>
</head>
<body  bottommargin="0"  background="../images/grayline_bg.jpg" rightmargin="0" leftmargin="0" topmargin="0">
<%
	if(fileName==null||fileName.trim().length()==0){
%>
	<script language="javascript">
		alert("请提供文件名!");
		top.close();
	</script>
<%		
		return;
	}
	String pathContext = (String)session.getAttribute("pathContext");
	if(pathContext==null || pathContext.trim().length()==0){
%>
		<script type="text/javascript">
			alert("没有提供资源的上下文路径,无法管理资源.");
			top.close();
		</script>
<%		
		return;
	}	
%>
<form method="post" name="TemplateForm" target="hidFrm" id="TemplateFrm" action="editFileContent_do.jsp">
	<div align="center">
	<input name="uri" type="hidden" id="uri" value="<%=uri%>">
	<input name="fileName" type="hidden" id="fileName" value="<%=fileName%>">
	<textarea name="fileContent" rows="45" class="fileContent" id="fileContent" type="_moz"><%
File tpltFolder = new File(pathContext,uri);
String content = FileUtil.getFileContent(new File(tpltFolder.getAbsolutePath(),fileName).getAbsolutePath(),CMSUtil.getCharset());
//处理文档内容
CmsLinkProcessor processor = new CmsLinkProcessor(request,relatePath,sitedir);
processor.setHandletype(CmsLinkProcessor.PROCESS_READTEMPLATE);
try {
	content = processor.process(content,CmsEncoder.ENCODING_UTF_8);
} catch (Exception e) {
	e.printStackTrace();
}

out.print(content);
%></textarea>
	<input class="cms_button" name="提交" type="submit" value="确定">
	<input type="button" class="cms_button" name="Submit" value="取消" onclick="top.close()">
	</div>
</form>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>
