<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String channelId = request.getParameter("channelId");
	ChannelManager cm = new ChannelManagerImpl();
	DocCommentManager dcm = new DocCommentManagerImpl();
	Channel channel = cm.getChannelInfo(channelId);
	String displayName = channel.getDisplayName();
	int commentSwitch = dcm.getDocCommentSwitch(channelId,"chnl");
	String action = "close";
	if(commentSwitch==1){
		action = "open";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>:::::频道评论功能开关::::::::::::::::::::::.</title>
<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.operStyle{
	width:17;
	height:16;
	}
.style7 {font-size: 5px}
</style>
<script src="../inc/js/func.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	function subform(){
		docCommentSwitch.action="docCommentHandle.jsp?channelId=<%=channelId%>&action=<%=action%>";
		docCommentSwitch.submit();	
	}

</SCRIPT>
</head>
<body scroll="no">
	<form target="docComSwitchF" method="post" name="docCommentSwitch" enctype="multipart/form-data">
		<table width="100%" bgcolor="#e7e7f7" height="100%">
			<tr>
				<td width="100%">
					当前频道“<%=displayName%>”的评论功能正处于“<%=commentSwitch==0?"开通":"关闭"%>”状态，您可以<%=commentSwitch==0?"关闭":"开通"%>评论功能，
					<p>
					您确定要<%=commentSwitch==0?"关闭":"开通"%>吗？
				</td>
			</tr>
			<tr>
				<td width="100%" align="center">
					<input type="button" name="add" value="确定" onclick="subform()">
					&nbsp;&nbsp;&nbsp;&nbsp;
					<input type="button" name="close" value="返回" onclick="window.close();">
				</td>
			</tr>
		</table>
	</form>
	<iframe name="docComSwitchF" height="0" width="0"></iframe>
</body>
</html>