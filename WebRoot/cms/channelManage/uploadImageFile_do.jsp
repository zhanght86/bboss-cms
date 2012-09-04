<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.io.*,java.util.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<%

	String fileName = "";
try{
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	//建立一个临时目录
	File temp = new File(application.getRealPath("/"),System.currentTimeMillis()+""+Math.random());
	temp.mkdirs();
 	 
	CMSManager cmsmanager = new CMSManager();
	cmsmanager.init(request,session,response,control);

	String siteId =  cmsmanager.getSiteID();

	String pathContext = null;
	if(siteId!=null && siteId.trim().length()!=0){
		String sitepath = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
		if(sitepath!=null && sitepath.trim().length()!=0){
			pathContext = new File(sitepath,"_template").getAbsolutePath();
		}
	}
	System.out.println("========="+pathContext+"=============");
	if(pathContext==null || pathContext.trim().length()==0){
%>
		<script language="javascript">
			alert("没有找到站点文件夹所在的路径!.");
			out.println("parent.parent.ImageListFrm.document.getElementById('divProcessing').style.display='none';");
		</script>
<% 
		return;
	}
	String uri = request.getParameter("uri");
	if(uri.equals("/"))
		uri = "uploadfiles/"+(String)date.Time_YMD().substring(0,6)+"/";
	String coverFlag = request.getParameter("coverflag");
	File parentFolder = null;
	if(uri!=null && uri.trim().length()!=0){
		parentFolder = new File(pathContext,uri);
	}else{
		parentFolder = new File(pathContext);
	}
	
	FileItemFactory fileitemfactory = new DefaultFileItemFactory(2048000,temp);
	FileUpload fu = new FileUpload(fileitemfactory);
	List fields = fu.parseRequest(request);
	int flag = 0;//校验客户端文件是否存在
	
	
	
	for(int i=0;fields!=null&&i<fields.size();i++){
		FileItem file = (FileItem)fields.get(i);	
		if(file.getInputStream().available() <= 0)
		{
			//break;
            continue;
		}
		flag++;
		if(!file.isFormField()){
			//建个文件用来获取名字
			String FileName=(String)date.Time_Stamp();
			
			String fileBottom = file.getName();
			int index = fileBottom.lastIndexOf('.');
			fileBottom = fileBottom.substring(index);
			File tempFile = new File(FileName + fileBottom);
			
			fileName = tempFile.getName() ;
			
			File f = new File(parentFolder.getAbsoluteFile(),fileName);
			if((coverFlag==null && f.exists())||("false".equals(coverFlag) && f.exists())){				
				out.println("<script language=\"javascript\">");
				out.println("alert('文件已经存在,请在上传之前修改好文件名!');");
				out.println("parent.parent.ImageListFrm.document.getElementById('divProcessing').style.display='none';");
				out.println("</script>");
				FileUtil.deleteSubfiles(temp.getAbsolutePath());
				FileUtil.deleteFile(temp.getAbsolutePath());
				return;	
			}
			f.getParentFile().mkdirs();
			f.createNewFile();
			file.write(f);
		}
	}
	FileUtil.deleteSubfiles(temp.getAbsolutePath());
	FileUtil.deleteFile(temp.getAbsolutePath());
	if(flag <= 0)
	{
		out.println("<script language=\"javascript\">");
		out.println("alert('文件为空或不存在,请重新上传!');");
		out.println("parent.parent.ImageListFrm.document.getElementById('divProcessing').style.display='none';");
		out.println("</script>");
		return;	
	}
}catch(Exception e){
	out.println("<script language=\"javascript\">");
	out.println("alert('上传图片发生异常,异常为"+e.getMessage()+"');");
	out.println("parent.parent.ImageListFrm.document.getElementById('divProcessing').style.display='none';");
	out.println("</script>");		
	e.printStackTrace();
}
%>
<script type="text/javascript">
    parent.parent.ImageListFrm.document.getElementById("divProcessing").style.display="none";
    
	parent.parent.ImageListFrm.location.href = parent.parent.ImageListFrm.location+"&fileName=<%=fileName%>";
	parent.parent.TreeFrm.location.href = parent.parent.TreeFrm.location;
	//alert(parent.parent.ImageListFrm.location.reload());
	
</script>
</body>
</html>