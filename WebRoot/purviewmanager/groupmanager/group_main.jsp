<%
/*
 * <p>Title: 用户组模块的主框架</p>
 * <p>Description: 用户组模块主框架</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-18
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkManagerAccess(request,response);

			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setDateHeader("max-age", 0);


		%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
		<script type="text/javascript" src="../../include/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="../../html/js/commontool.js"></script>
		<script type="text/javascript" src="../../html/js/dialog/lhgdialog.js?self=false"></script>	
<%@ include file="/include/css.jsp"%>
		
		<title>用户组管理</title>
		<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.style3 
	{
		font-size: 14px;
		font-weight: bold;
		color: #3300FF;
	}
	.operStyle
	{
	width:17;
	height:16;
	}
</style>

<SCRIPT LANGUAGE="JavaScript">	
</SCRIPT>
	</head>
	<frameset name="userOrgFrame" cols="20%,*" border=0 framespacing="0">
		<frame frameborder=0  noResize scrolling="auto" name="group_tree" src="groupTree.jsp" marginWidth=0 marginHeight=0 >
		</frame>
		<frame frameborder=0  noResize scrolling="auto" name="groupinfo" src="groupInfo.jsp" marginWidth=0 marginHeight=0 >
		</frame>
	</frameset>
</html>
