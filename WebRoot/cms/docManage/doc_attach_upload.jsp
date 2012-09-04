<%
/**
  *	上传文档相关附件
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.jspsmart.upload.*"%>
<%@ page import="java.io.*"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	try
	{
		String FileName,FileExt;
		String fileAllName = "";
		//
		String docpath = request.getParameter("docpath");
		//存文件
		SmartUpload up = new SmartUpload();
		//初始化上传组件
		up.initialize(pageContext);
		//设置上传文件大小
		up.setMaxFileSize(5000*1024);
		//设置上传文件类型
		//String setExt=sAllowExt.replace('|',',');
		//up.setAllowedFilesList(setExt);
		// Upload
		up.upload();
		// Select each file
		for (int i=0;i<up.getFiles().getCount();i++){
		// Retreive the current file
			com.jspsmart.upload.File myFile = up.getFiles().getFile(i);
			if (!myFile.isMissing()) {
				FileName = (String)date.Time_Stamp();
				FileExt = myFile.getFileExt();
				fileAllName = FileName + "." + FileExt;
				//sOriginalFileName=myFile.getFileName();
				String path = "/cms/siteResource/" + docpath + "/" + fileAllName;
				String parentPath = config.getServletContext().getRealPath("/") + "/cms/siteResource/"+docpath;
				java.io.File parentFile = new java.io.File(parentPath);
				/*
				if(!parentFile.exists())
				{
					parentFile.mkdirs();
				}*/
				myFile.saveAs(config.getServletContext().getRealPath("/") + path);
			}
		}
%>
<SCRIPT LANGUAGE="JavaScript">
	//调用父窗口的结束方法
	var fname = "<%=fileAllName%>";
	parent.UploadLoaded(fname);
</SCRIPT>
<%
	}catch(Exception e)
	{
%>
<SCRIPT LANGUAGE="JavaScript">
	alert("上传文件失败！");
	window.close();
</SCRIPT>
<%
	e.printStackTrace();
	}
%>