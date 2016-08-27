
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.jspsmart.upload.*"%>
<%@ page import="com.frameworkset.platform.cms.imagemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.awt.Color"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>

<%
	AccessControl accesscontroler = AccessControl.getInstance();	
	//accesscontroler.checkAccess(request, response);
    ImageManagerImpl impl = new ImageManagerImpl();
    
	//
	String waterpath = impl.getWATERIMAGE_FORDER();
    String normalpath = impl.getNORMALIMAGE_FORDER();
    String mixpath = impl.getMIXIMAGE_FORDER();
	String addwater = request.getParameter("addwater");
	String backup = request.getParameter("backup");
    String position = request.getParameter("position");
    String waterStr = request.getParameter("waterStr");
    
    CMSManager cmsmanager = new CMSManager();
    cmsmanager.init(request,session,response,accesscontroler);
    String siteId =  cmsmanager.getSiteID();
    String rootPath = null;
    String waterpicpath = ImageManagerImpl.getWATERIMAGE_FORDER();//水印图片路径
    if(siteId!=null && siteId.trim().length()!=0){
        String temp = (new SiteManagerImpl()).getSiteAbsolutePath(siteId);
        if(temp!=null && temp.trim().length()!=0){
            rootPath = new java.io.File(temp,"_template/" ).getAbsolutePath();
        }
    } 
    
	int p = 0;
    try{
        p = Integer.parseInt(position);
    }catch(Exception ee){
        p = 0;
    }
	try
	{
		String FileName = "";
		String fileAllName = "";
		
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
		List list = new ArrayList();
        java.io.File parentFile = null;
		for (int i=0;i<up.getFiles().getCount();i++){
		// Retreive the current file
			com.jspsmart.upload.File myFile = up.getFiles().getFile(i);
			if (!myFile.isMissing()) {
				FileName = myFile.getFileName();
				//FileExt = myFile.getFileExt();
				String path = FileName;
				String savePath = "";
                /* 水印图片*/                
                parentFile = new java.io.File(rootPath + waterpath);
                if(!parentFile.exists()) parentFile.mkdirs();
                savePath = waterpath + path;
				myFile.saveAs(rootPath + savePath);                
			}
		}
        impl.logWaterImageMsg("文档管理","添加水印图片",request,response);//只保留"站点管理.图片管理.水印图片管理",weida
%>
<SCRIPT LANGUAGE="JavaScript">
	//调用父窗口的结束方法
	var fname = "<%=fileAllName%>";
	alert("文件上传成功！");
    window.returnValue='True'
	window.close();
    //parent.UploadLoaded(fname);
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