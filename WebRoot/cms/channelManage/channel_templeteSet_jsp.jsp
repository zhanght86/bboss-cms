<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="com.frameworkset.platform.cms.container.Template" %>
<%@page import="java.util.*" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	String siteName = request.getParameter("siteName");
	String siteId   = request.getParameter("siteId");
	String channelId = request.getParameter("channelId");
	String channelName = request.getParameter("channelName");
	
	ChannelManager cm = new ChannelManagerImpl();
	Channel chnl = cm.getChannelInfo(channelId);
	String outlineTemplateName="当前没有选择模板";
	String detailTemplateName ="当前没有选择模板";
	String otId = "";
	String dtId = "";
	try
	{		
	    TemplateManager tpltMng = new TemplateManagerImpl();
		otId = String.valueOf(chnl.getOutlineTemplateId());	    
		outlineTemplateName = tpltMng.getTemplateInfo(otId).getName();
		dtId = String.valueOf(chnl.getDetailTemplateId());
		detailTemplateName = tpltMng.getTemplateInfo(dtId).getName();
		
	}catch(Exception e)
	{
		//System.out.println("xx"+siteId);
		//e.printStackTrace();
	}
	
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<title>频道模板设置</title>
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

function setChannelOutlineTemplateId()
{
		var channelName = "<%=channelName%>";
		var w = showModalDialog("channel_templateSet_frameset.jsp?siteId=<%=siteId%>&channelId=<%=channelId%>&channelName="+encodeURIComponent(channelName)+"&outlineTemplateName="+encodeURIComponent(siteForm.divOutlineTemplate.value)+"&otId="+siteForm.outlineTemplateId.value+"&type=1&action=search&isSite=doc","setChannelOutlineTemplateId","dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");

		if(w!=null && w.length>0)
		{
			siteForm.outlineTemplateId.value = w[0];
			siteForm.divOutlineTemplate.value = w[1];
		}

}

function setChannelDetailTemplateId()
{
		var channelName = "<%=channelName%>";
		var w = showModalDialog("channel_templateSet_frameset.jsp?siteId=<%=siteId%>&channelId=<%=channelId%>&channelName="+encodeURIComponent(channelName)+"&detailTemplateName="+encodeURIComponent(siteForm.divDetailTemplate.value)+"&dtId="+siteForm.detailTemplateId.value+"&type=2&action=search&isSite=doc","setChannelOutlineTemplateId","dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");
		
		if(w!=null && w!=undefined)
		{
			siteForm.detailTemplateId.value = w[0];
			siteForm.divDetailTemplate.value = w[1];			
		}

}
function clo(){
	//alert("设置成功！");
	window.close();
}
//模板预览
function templatePreview(obj){
	var tplId = obj.value;
	window.open("<%=request.getContextPath()%>/cms/templateManage/template_previewbyid.jsp?tplId=" + tplId + "&siteId=<%=siteId%>");
}

function save()
{
	siteForm.action="channel_templateSet_do.jsp?type=1&channelId=<%=channelId%>&tId="
									+siteForm.outlineTemplateId.value+"&tName="+siteForm.divOutlineTemplate.value+"&siteId=<%=siteId%>";
	siteForm.submit();
	
	siteForm.action="channel_templateSet_do.jsp?type=2&channelId=<%=channelId%>&tId="
									+siteForm.detailTemplateId.value+"&tName="+siteForm.divDetailTemplate.value+"&siteId=<%=siteId%>";
	siteForm.submit();
	
}
</script>
</head>
<body bgcolor="#F7F8FC" leftmargin="0" topmargin="0">
<form id="siteForm" name="siteForm"  target="exeman" action="site_edit_do.jsp" method="post">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" bordercolorlight="#FFFFFF">
				<tr>
					<td colspan="2" class="topDIV" align="left" valign="top">
						&nbsp;
						<div class="siteCTitle">
							频道模板设置
						</div>
					</td>
				</tr>
				<tr>
					<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6">
						站点中文名称:
					</td>
					<td height="24">
						&nbsp;&nbsp;<%=(channelName == null ? "&nbsp;" : channelName)%>
					</td>
				</tr>
				<tr><td colspan="2"></td></tr>
				<tr>
						<td width="25%" height="24" align="right" nowrap>
							概览模板:	
						</td>
					  <td height="24">
					    <input name="outlineTemplateId" id="outlineTemplateId" type="hidden" value="<%=otId%>" />
						<input name="divOutlineTemplate" id="divOutlineTemplate" type="text" style="width:110px"　class="cms_text" disabled value="<%=outlineTemplateName%>" />
						<input class="Channel_5wordsBtn" type="button" value="选 择" 
								id="btSetChannelOutlineTemplateId" name="btSetChannelOutlineTemplateId" 
								onClick="setChannelOutlineTemplateId()" />
						<input class="Channel_5wordsBtn" type="button" value="预 览" id="outlineTemplatePreview" name="outlineTemplatePreview" onClick="templatePreview(outlineTemplateId)">
					　</td>
					</tr>
					<tr>
						<td width="25%" height="24" align="right" nowrap>
							细览模板:	
						</td>
						<td>
							<input name="detailTemplateId" id="detailTemplateId" type="hidden" value="<%=dtId%>" />
							<input name="divDetailTemplate" id="divDetailTemplate" type="text" style="width:110px" value="<%=detailTemplateName%>"  class="cms_text" disabled />
							<input class="Channel_5wordsBtn" type="button" value="选 择" id="btSetChannelDetailTemplateId" name="btSetChannelDetailTemplateId" onClick="setChannelDetailTemplateId()">
							<input class="Channel_5wordsBtn" type="button" value="预 览" onClick="templatePreview(detailTemplateId)">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<br>
	<div style="text-align:center">
		<INPUT name="button" type="button" class="cms_button" onClick="save()"  value="保存">
		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="clo()"  value="关闭">
	</div>
</form>
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>