<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%@page import="com.frameworkset.platform.util.RemoteFileHandle"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="java.util.*"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<body>
<%
	String tag = "success";

	String fileName = request.getParameter("fileName");
	String uri = request.getParameter("uri");

	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);

	//String siteId =  cmsmanager.getSiteID();
	//SiteManager siteManager = new SiteManagerImpl();

	//String sitedir = siteManager.getSiteInfo(siteId).getSiteDir();//站点相对路径
	String sitedir = cmsmanager.getCurrentSite().getSiteDir();
	String relatePath = "";
	//System.out.println("--/---"+ relatePath);
	
	
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
	
	

	if(fileName==null||fileName.trim().length()==0){
%>
	<script language="javascript">
		alert("请提供相对路径和文件名!");
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
	String fileContent = request.getParameter("fileContent");
	//处理文档内容
	CmsLinkProcessor processor = new CmsLinkProcessor(request,relatePath,sitedir);
	processor.setHandletype(CmsLinkProcessor.PROCESS_EDITTEMPLATE);
	try {
		String tempcontent = processor.process(fileContent,CmsEncoder.ENCODING_UTF_8);
		fileContent = tempcontent;
		CmsLinkTable linktable = processor.getExternalPageLinkTable();
		Iterator it = linktable.iterator();
		while(it.hasNext())
		{
			CmsLinkProcessor.CMSLink link = (CmsLinkProcessor.CMSLink)it.next();
			String remoteAddr = link.getOrigineLink().getHref();
			String contentPath = link.getRelativeFilePath();
			String localPath = config.getServletContext().getRealPath("/") + "cms/siteResource/" + sitedir + "/_template/" + contentPath;
			
			try {
				//远程信息本地化
				RemoteFileHandle rf = new RemoteFileHandle(remoteAddr,localPath);
				rf.download();
			} catch (Exception e) {
				System.out.println("取远程图片时出错！");
				e.printStackTrace();
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}

	File tpltFolder = new File(pathContext,relatePath);
	
	try{
		FileUtil.saveFile(new File(tpltFolder.getAbsolutePath(),fileName).getAbsolutePath(),fileContent,CMSUtil.getCharset());
	}
	catch(Exception e)
	{
		tag = "fail";
	%>		
		<script language="javascript">
			alert('保存失败,请去掉文件的只读属性!');
		</script>
	<%
	}
	if(tag.equals("success"))
	{
%>
<script language="javascript">
	alert('编辑文件内容成功');
	top.close();
</script>
<%
	}
%>
</body>
</html>