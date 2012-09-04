<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);

	String fid = request.getParameter("fid");
	String tid = request.getParameter("tid");

	ChannelManager cm = new ChannelManagerImpl();
	boolean flag = cm.moveChannel(fid,tid);
	
%>
<SCRIPT LANGUAGE="JavaScript"> 
	if(window.confirm("操作成功！\n您还要移动其他频道吗？"))
	{
		parent.parent.from_chl.document.location.href = parent.parent.from_chl.document.location.href;
  		parent.parent.to_chl.document.location.href = parent.parent.to_chl.document.location.href;
	}
	else
	{
		window.open("../siteManage/navigator_content.jsp","perspective_toolbar");
		parent.parent.window.open("../top.jsp","perspective_topbar");
		window.close();
	}
</script>
