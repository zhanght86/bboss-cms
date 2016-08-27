<%
/**
  *	上传文档相关附件
  * 然后导入文档信息到数据库
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.jspsmart.upload.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.util.DocZipUtil"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<jsp:useBean id="date" scope="page" class="net.fiyu.edit.TimeStamp"/>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String siteid= request.getParameter("siteid");
	String channelId = request.getParameter("channelId");

	String FileName,FileExt;
	String fileAllName = "";
	int count = 0;
	
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager sm = new SiteManagerImpl();
	String docpath = sm.getSiteInfo(siteid).getSiteDir();// + "/_webprj/" + cm.getChannelInfo(channelId).getChannelPath() + "/content_files";

	try
	{
		String path = "";
		String type = "";
		
		SmartUpload up = new SmartUpload();
		up.initialize(pageContext);
		up.setMaxFileSize(50000*1024);
		up.upload();
		
		com.jspsmart.upload.Request req = up.getRequest();
		String fname = req.getParameter("fname");
		String urlfname = req.getParameter("urlfname");
		
		if(fname != null && !(fname.trim().equals("")))//上传的处理
		{
			//存文件
			for (int i=0;i<up.getFiles().getCount();i++){
				com.jspsmart.upload.File myFile = up.getFiles().getFile(i);
				if (!myFile.isMissing()) {
					FileName = (String)date.Time_Stamp();
					FileExt = myFile.getFileExt();
					fileAllName = FileName + "." + FileExt;
					path = "cms\\docManage\\temp\\upload\\" + fileAllName;
					myFile.saveAs(config.getServletContext().getRealPath("/") + path);
				}
			}
			//导入的zip文件来自上传
			type = DocZipUtil.FORM_REMOTE;
			//解压，拷贝文件
			count = DocZipUtil.xml2DB(request,response,channelId,config.getServletContext().getRealPath("/"),fileAllName,docpath,type);
			
			java.io.File tmpfile = new java.io.File(config.getServletContext().getRealPath("/") + path);
			tmpfile.delete();
		}
		if(urlfname != null && !(urlfname.trim().equals("")))//选择服务器zip文件的处理
		{
			fileAllName = urlfname.trim();
			//导入的zip文件是从服务器选择
			type = DocZipUtil.FORM_LOCAL;
			//解压，拷贝文件
			count = DocZipUtil.xml2DB(request,response,channelId,config.getServletContext().getRealPath("/"),fileAllName,docpath,type);
		}
		
%>
<SCRIPT LANGUAGE="JavaScript">
	//调用父窗口的结束方法
	var count = "<%=count%>";
	parent.UploadLoaded(count);
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