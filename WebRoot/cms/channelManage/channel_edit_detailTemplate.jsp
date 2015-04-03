<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.container.Template"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.flowmanager.*"%>
<%@ page import="com.frameworkset.platform.util.*"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	//String siteName = request.getParameter("siteName");
	String siteId   = request.getParameter("siteId");
	Site site = SiteCacheManager.getInstance().getSite(siteId);
	
	String siteName = site.getSecondName()+"-" +site.getName();
	String channelId = request.getParameter("channelId");
	//String channelName = request.getParameter("channelName");
	if(siteId == null || siteId.trim().length()==0){
	%>
		<script language="javascript" type="text/javascript">
			alert("频道id为空,无法编辑频道.");
			self.location.href = "about:blank";
		</script>
	<%
		return;
	}

	ChannelManager cm = new ChannelManagerImpl();
	Channel chnl = cm.getChannelInfo(channelId);
	String channelName = chnl.getDisplayName() + "-" +  chnl.getName();
	if (chnl == null) {
	%>
		<script language="javascript" type="text/javascript">
			alert("没有找到该频道,无法编辑频道.");
			self.location.href = "about:blank";
		</script>
	<%
		return;
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<title>设置频道首页模板</title>
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
	document.all.siteForm.submit();	
}
function updateParentForm()
{
	window.returnValue = siteForm.detailTemplateId.options[siteForm.detailTemplateId.selectedIndex].text;
}
function preview(){
	var siteForm = document.all.siteForm;
	var siteId = "<%=siteId%>";
	var templateId = siteForm.detailTemplateId[siteForm.detailTemplateId.selectedIndex].value;
	//alert("预览频道id为:"+siteId+",模板id为"+templateId+"的模板!");
	window.open("../templateManage/template_previewbyid.jsp?tplId=" + templateId + "&siteId=<%=siteId%>");
}
function templatePreview(obj){
	for(var i=0;i<obj.options.length;i++){
		if(obj.options[i].selected == true){
			var templatePath = obj.options[i].templatePath; 
			var templateFileName = obj.options[i].templateFileName;
			if(templatePath==undefined){
				alert("未选择模板文件，不能预览");
				break;
			}
			pubPreviewFile(templateFileName,templatePath,<%=siteId%>)
		}
	}
}
function pubPreviewFile(filename,uri,siteId) {
		var u = uri.replace("\\", "/").replace(/\s+/img,"");
		if(u.charAt(u.length - 1)!="/")
		{
			u = u+"/";
		}
		if(u=="/")
		{
			u = "";
		} 
		exeman.location.href = "../templateManage/template_pub_preview.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=preview";
	}
</script>
</head>
<body background="../images/grayline_bg.jpg">
<form id="siteForm" name="siteForm"  target="exeman" action="channel_edit_detailTemplate_do.jsp" method="post">
	<input name="siteName" type="hidden" id="siteName" value="<%=siteName%>">
	<input name="siteId" type="hidden" id="siteId" value="<%=siteId%>">
	<input name="channelId" type="hidden" id="channelId" value="<%=channelId%>">
	<input name="channelName" type="hidden" id="channelName" value="<%=channelName%>">
	<table width="98%" border="1" cellpadding="0" cellspacing="0" align="center" bordercolorlight="#FFFFFF" >
		<tr>
			<td colspan="2" class="topDIV"  align="left" valign="top">设置频道细缆模板:</td>
		</tr>
		<tr>
			<td width="100" height="24" align="right" nowrap>频道中文名称:</td>
			<td height="24">
				<%=channelName%>			</td>
		</tr>
		<tr>
			<td height="24"  align="right" nowrap>请选择模板:</td>
			<td height="24"><select name="detailTemplateId" id="detailTemplateId" style="width:130px">
									<%
										int dtp = chnl.getDetailTemplateId();
										TemplateManager tpltMng = new TemplateManagerImpl();
									
										List templateList2 = tpltMng.getTemplateInfoListofSite(Integer.parseInt(siteId), 2);
										for (int i = 0; templateList2 != null && i < templateList2.size(); i++) {
											Template t = (Template) templateList2.get(i);
											out.println("<option value=\"" + t.getTemplateId() + "\" " +
												(dtp==t.getTemplateId()?"selected":"") +
												" templatePath=\"" + t.getTemplatePath() + "\"  templateFileName=\"" + t.getTemplateFileName() + "\" >" + 
												t.getName() + "</option>");
										}
									%>
								</select><input type="button" value="预览" onClick="templatePreview(detailTemplateId)"></td>
		</tr>
		<tr>			
		</tr>
	</table>
	<br>
	<div style="text-align:center">

					<INPUT name="button" type="button" class="cms_button" onClick="checkForm()"  value="保存">

		&nbsp;&nbsp;&nbsp;
		<INPUT name="button" type="button" class="cms_button" onClick="window.returnValue = 'false';window.close()"  value="关闭">
	</div>
	
</form>
<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>