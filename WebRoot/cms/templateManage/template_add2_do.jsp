<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%@page import="com.frameworkset.platform.util.RemoteFileHandle"%>
<%@page import="java.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<body>
<%
File currTemplateFolder = null;

try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
 	 
	//获取站点在文件系统中的绝对路径
	String siteId = request.getParameter("siteId");
	if(siteId == null || siteId.trim().length()==0){
		throw new Exception("获取参数站点id失败,无法根据id去查找目录");
	}

	String sitepath = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
	if(sitepath==null || sitepath.trim().length()==0){
		throw new Exception("根据站点id["+siteId+"],没有找到站点对应的目录,无法处理上传的文件.");
	}

	File siteroot = new File(sitepath);
	if(!siteroot.exists()){
		siteroot.mkdirs();
	}
	
	String uri = request.getParameter("uri");
	if(uri==null || uri.trim().length()==0){
		uri = "";
		currTemplateFolder = new File(siteroot,"_template");
	}else{
		uri.replace('\\','/');
		if(uri.startsWith("/")){
			uri.substring(1);
		}
		if(uri.endsWith("/")){
			uri.substring(0,uri.length()-1);
		}
		currTemplateFolder = new File(new File(siteroot,"_template").getAbsolutePath(),uri);
	}
	
	currTemplateFolder.mkdirs();

	SiteManager siteManager = new SiteManagerImpl();

	String sitedir = siteManager.getSiteInfo(siteId).getSiteDir();//站点相对路径
	
	String content = ""+request.getParameter("content");
	String coverFlag = request.getParameter("coverFlag");
	//处理文档内容
	CmsLinkProcessor processor = new CmsLinkProcessor(request,uri,sitedir);
	processor.setHandletype(CmsLinkProcessor.PROCESS_EDITCONTENT);
	try {
		String tempcontent = processor.process(content,CmsEncoder.ENCODING_UTF_8);
		content = tempcontent;
		CmsLinkTable linktable = processor.getExternalPageLinkTable();
		Iterator it = linktable.iterator();
		while(it.hasNext())
		{
			CmsLinkProcessor.CMSLink link = (CmsLinkProcessor.CMSLink)it.next();
			String remoteAddr = link.getOrigineLink().getHref();
			String contentPath = link.getRelativeFilePath();
			String localPath = config.getServletContext().getRealPath("/") + "cms/siteResource/" + sitedir + "/_template/" + contentPath;
			System.out.println("1"+localPath);
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
	
	Template tplt = new Template();
	String fileName = request.getParameter("fileName");
	if(fileName!=null && fileName.trim().length()!=0){
		tplt.setTemplateFileName(fileName);
	}	
	fileName = tplt.getTemplateFileName();
    File file=new File(currTemplateFolder.getAbsolutePath(),fileName);
    if(coverFlag==null && file.exists())
    {
		out.println("<script language=\"javascript\">");
		out.println("alert('文件已经存在,请在上传之前修改好文件名!');");
		out.println("</script>");
		return;	    	
    }
	FileUtil.writeFile(file.getAbsolutePath(),content);
	
	String templateName = request.getParameter("templateName");
	if(templateName==null||templateName.trim().length()==0){
		throw new Exception("请提供模板名称!");
	}
	tplt.setName(templateName);
	
	String templateDesc = request.getParameter("templateDesc");
	if(templateDesc==null||templateDesc.trim().length()==0){
		throw new Exception("请提供模板简短描述!");
	}
	tplt.setDescription(templateDesc);
	
	String templateType = request.getParameter("templateType");
	tplt.setType(Integer.parseInt(templateType));
	
	tplt.setCreateUserId(Long.parseLong(control.getUserID()));
	
	int id = Integer.parseInt(siteId);
	tplt.setSiteId(id);
	tplt.setHeader("null");
	tplt.setText("null");
	
	tplt.setTemplatePath(uri);
	tplt.setPersistType(Template.PERSISTINFILE);
	
	//模板风格编号
	int style = Integer.parseInt(request.getParameter("templateStyle"));
	tplt.setStyle(style);

	new TemplateManagerImpl().createTemplateofSite(tplt,Integer.parseInt(siteId));
	%>
	
	<script language="javascript">
		alert('新建模板成功');
		var str = window.dialogArguments.location.href;
		var strArray = str.slice(0,str.indexOf("?"));
		window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
		top.close();
	</script>
<%}catch(Exception e){
	out.println("<script language=\"javascript\">");
	out.println("alert('"+e+"');");
	out.println("</script>");
	e.printStackTrace();
}%>
</body>
</html>