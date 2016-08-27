<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.channelmanager.*" %>
<%@page import="java.util.*" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String channelId = request.getParameter("channelId");
	if(channelId == null || channelId.trim().length()==0){
	%>
		<script language="javascript" type="text/javascript">
			alert("频道id为空,无法编辑频道.");
			self.location.href = "about:blank";
		</script>
	<%
		return;
	}
	ChannelManager cm = new ChannelManagerImpl();
	Channel channel = cm.getChannelInfo(channelId) ;

	String channelName = channel.getName();
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<title>修改频道概览图片</title>
<style type="text/css">
	.topDIV {
		background-image: url(../images/toolbar_common_func_site.jpg);
		height: 82px;
		padding-top: 7px;
		padding-left: 55px;

	body {
		margin-left: 0px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
	}
</style>
<script language="javascript">

//站点窗口输入参数校验
function checkForm(){
	var siteForm = document.all.siteForm;
	if(siteForm.outlinePicture.value == "")
	{
		if(confirm("概览图片为空？"))
		{
			document.all.siteForm.submit();
		}
		else
		{
			return;
		}
	}
	else
	{
		document.all.siteForm.submit();
	}
	
}

function changeWorkFlowType(type){
	if(0==type){
		document.all.siteForm.workFlow.disabled = false;
	}else{
		document.all.siteForm.workFlow.disabled = true;
	}
}

function chooseImage(str){
	currFile = eval("document.all."+str);
	openWin("chooseImageFrameset.jsp",700,580);
}

function setImage(uri){
	currFile.value  = uri;
}
</script>
</head>
<body background="../images/grayline_bg.jpg">
<form id="siteForm" name="siteForm"  target="exeman" action="change_indexPic_do.jsp" method="post">
	<input name="channelId" type="hidden" id="siteId" value="<%=channelId%>">
	<table width="98%" border="1" cellpadding="0" cellspacing="0" align="center" bordercolorlight="#FFFFFF" >
		<tr>
			<td colspan="2" class="topDIV"  align="left" valign="top">修改频道概览图片</td>
		</tr>
		<tr>
			<td width="100" height="24" align="right" nowrap>频道中文名称:</td>
			<td height="24">
				<%=(channelName==null?"&nbsp;":channelName)%>
			</td>
		</tr>
		<tr>
			<td height="24" rowspan="2" align="right" nowrap>概览图片:</td>
			<td height="24">
				<input name="outlinePicture" type="text" readonly id="outlinePicture" size="10" value="<%=(channel.getOutlinepicture()==null?"":channel.getOutlinepicture())%>" style="width:200px" class="cms_text">
				<input type="button"  onClick="chooseImage('outlinePicture')"  value="选 择" class="Channel_5wordsBtn" >
			</td>
		</tr>
		
	</table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" type="button" class="cms_button" onClick="checkForm()"  value="保存">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.close()"  value="关闭">
	</div>
</form>
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>