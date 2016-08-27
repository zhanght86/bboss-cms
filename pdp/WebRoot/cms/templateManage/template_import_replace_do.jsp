<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*,java.util.*,java.util.zip.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title></title>
</head>
<body>
<%
File temp = null;

try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	//建立一个临时目录
	temp = new File(application.getRealPath("/"),"temp16759"+Math.random());
	temp.mkdirs();
 	 
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
	System.out.print("uri="+uri);
	if(uri==null || uri.trim().length()==0){
		throw new Exception("请提供模板存放的相对路径.");
	}

	File currTemplateFolder = new File(new File(siteroot,"_template").getAbsolutePath(),uri);

	
	System.out.println("文件目录:"+currTemplateFolder.getAbsolutePath());
	//因为目录是这一次动作建的,可以删除它
	currTemplateFolder.mkdirs();
	
	FileItemFactory fileitemfactory = new DefaultFileItemFactory(2048000,temp);
	FileUpload fu = new FileUpload(fileitemfactory);
	List fields = fu.parseRequest(request);
	for(int i=0;fields!=null&&i<fields.size();i++){
		FileItem file = (FileItem)fields.get(i);
		if(!file.isFormField()){
			ZipInputStream fullzipfile = new ZipInputStream(new BufferedInputStream(file.getInputStream()));
			FileUtil.upzip(fullzipfile,currTemplateFolder.getAbsolutePath());
		}
	}

	%>
	
	<script language="javascript">
		alert('导入模板成功');
		window.dialogArguments.location.reload();
		top.close();
	</script>
<%}catch(Exception e){
	out.println("<script language=\"javascript\">");
	out.println("alert('"+e+"');");
	out.println("</script>");
	e.printStackTrace();
}finally{
	try{
		if(temp!=null){
			FileUtil.deleteSubfiles(temp.getAbsolutePath());
			FileUtil.deleteFile(temp.getAbsolutePath());
		}
	}catch(Exception e){}

}
%>
</body>
</html>
