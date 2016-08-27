<%
/**
  *	从数据库导出到文档基本信息到xml文件中，在结合附件信息打成zip包
  * 供用户下载
  */
%>
<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.platform.cms.documentmanager.util.DocZipUtil"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
    //String userid = accesscontroler.getUserID();

	String path = config.getServletContext().getRealPath("/");
	
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager sm = new SiteManagerImpl();
	String siteid = request.getParameter("siteid");
	String zipname = request.getParameter("expzipname");
	
	String[] docids = request.getParameter("docids").split(",");
	int[] docId = new int[docids.length];
	for(int i=0;i<docids.length;i++)
	{
		docId[i] = Integer.parseInt(docids[i]);
	}
	String attrpath="";
	//根据频道id，站点id获取文档保存路径(新方法) 保存路径=站点路径+频道路径
	attrpath = sm.getSiteInfo(siteid).getSiteDir();
	String zipfile = DocZipUtil.toZip(zipname,docId,request,path,attrpath);
%>
<script>
	function winClose()
	{
		window.close();
	}
	var zipname = "<%=zipname%>";//用户自己指定
	var zipfile = "<%=zipfile%>";//文件重名判断返回值
	if(zipfile == "")
	{
		alert("文件名“<%=zipname%>”已存在，请重新指定名称！");
	}
	else
	{
		var uristr = "temp/<%=zipfile%>";
		location.replace("download_expzip.jsp?file=" + uristr);
	}

	//window.setTimeout("winClose()", 1500);
</script>