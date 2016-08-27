<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*,java.util.*,java.util.zip.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<%
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);
	String siteId =  cmsmanager.getSiteID();
	
	//建立一个临时目录
	File temp = new File(application.getRealPath("/"), System.currentTimeMillis()+""+Math.random());
	temp.mkdirs();
 	 

	//处理文件上传
	FileItemFactory fileitemfactory = new DefaultFileItemFactory(2048000,temp);
	FileUpload fu = new FileUpload(fileitemfactory);
	fu.setHeaderEncoding("UTF-8");
	List fields = fu.parseRequest(request);
	String uri = null;
	String pathContext = null;
	FileItem file = null;
	try
	{
		
		for(int i=0;fields!=null&&i<fields.size();i++){
			FileItem field = (FileItem)fields.get(i);
			if(field.isFormField())
			{
				if("uri".equals(field.getFieldName())){
					uri = field.getString();
				}
				else if("pathContext".equals(field.getFieldName())){
					pathContext = field.getString();
					if(pathContext==null || pathContext.trim().length()==0){
						%>
								<script type="text/javascript">
									alert("没有提供资源的上下文路径,无法管理资源.");
									top.close();
								</script>
						<%		
								return;
							}
				}
			}
			else
			{
				file = field;
			}	
		}
		
		
		
		
		File parentFolder = null;
		if(uri!=null && uri.trim().length()!=0){
			parentFolder = new File(pathContext,uri);
		}else{
			parentFolder = new File(pathContext);
		}
		
		
		
		//把xml文件取出来
		List templateXml = new ArrayList();
		if(file != null)
		{
			if(!file.isFormField()){
				ZipInputStream fullzipfile = new ZipInputStream(new BufferedInputStream(file.getInputStream()));
				ZipEntry azipfile = null;
				while((azipfile = fullzipfile.getNextEntry())!=null){
					//String name = new String(azipfile.getName().getBytes("UTF-8"),"UTF-8");
					String name = azipfile.getName();
					System.out.println("azipfile.getName()="+name);
	
					//分析zip文件中的块
					if(!azipfile.isDirectory()){	
						File targetFile = new File(parentFolder.getAbsolutePath(),name);
						if(targetFile.exists()){
							out.println("<script language=\"javascript\">");
							out.println("alert('压缩包中["+name+"]文件在服务器中已经存在,请解压,从新给该文件命名!');");
							out.println("</script>");
							return;		
						}
						targetFile.getParentFile().mkdirs();
						targetFile.createNewFile();
						BufferedOutputStream diskfile = new BufferedOutputStream(new FileOutputStream(targetFile));
						byte[] buffer =new byte[1024];
						int read;
						
						//根据文件后缀名来判断是否分析,
						boolean toParse = false;
						StringBuffer sb = new StringBuffer();
						if(name.toLowerCase().endsWith(".xml")){
							toParse = true;
						}
						while((read = fullzipfile.read(buffer))!=-1){
							diskfile.write(buffer,0,read);
							if(toParse){
								sb.append(new String(buffer,0,read));
							}
						}
						if(toParse){
							templateXml.add(sb.toString());
						}
						toParse = false;
						
						diskfile.close();
					}
				}
				fullzipfile.close();
			}
		}
		TemplateManager tm = new TemplateManagerImpl();
		TemplateFromXML tfx = new TemplateFromXML();
		
		for(int i=0;i<templateXml.size();i++){
			Template t = tfx.parse((String)templateXml.get(i));
			if(t!=null){
				t.setCreateUserId(Long.parseLong(control.getUserID()));
				t.setTemplatePath(uri);
				t.setPersistType(1);
				t.setSiteId(Integer.parseInt(siteId));
				tm.createTemplateofSite(t,Integer.parseInt(siteId));
			}
		}
		FileUtil.deleteSubfiles(temp.getAbsolutePath());
		FileUtil.deleteFile(temp.getAbsolutePath());
	}
	finally
	{
		if(file != null)
			file.delete();
	}
}catch(Exception e){
	out.println("<script language=\"javascript\">");
	out.println("alert('上传的文件发生异常,异常为"+e+"');");
	out.println("parent.win.reloadView();");	
	out.println("top.close();");
	out.println("</script>");			
	e.printStackTrace();
}
%>
<script type="text/javascript">
	alert("上传文件解压成功.");
	parent.win.reloadView();
	top.close();
</script>
</body>
</html>
