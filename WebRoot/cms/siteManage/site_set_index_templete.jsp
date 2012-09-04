<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*"%>
<%@page import="com.frameworkset.platform.cms.container.Template"%>
<%@ page import="java.util.List"%>
<%@page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@page import="com.frameworkset.platform.cms.flowmanager.*"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request, response);

			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", -1);
			response.setDateHeader("max-age", 0);

			String siteId = request.getParameter("siteId");
			if (siteId == null || siteId.trim().length() == 0) 
			{

%>

	<script language="javascript" type="text/javascript">
				alert("站点id为空,无法编辑站点.");
				self.location.href = "about:blank";
	</script>
<%
				return;
			}
			SiteManager sm = new SiteManagerImpl();
			Site site = sm.getSiteInfo(siteId);
			TemplateManager tpltMng = new TemplateManagerImpl();
			String siteName = site.getName();
			String stId = site.getIndexTemplateId()+"";
			
			//判断是否存在模板页面
			//liangbing.tao 2008-5-20
			Template t = sm.getIndexTemplate(siteId);
			String stName = "当前没有选择模板";		
			if(t != null)
			{
				stName = t.getName() ;
			}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<script src="../inc/js/func.js"></script>
		<title>站点模板设置</title>
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
var flag = false;
//站点窗口输入参数校验
function checkForm(){
	var siteForm = document.all.siteForm;
	document.all.siteForm.submit();	
}
function save()
{
	siteForm.action="../channelManage/channel_templateSet_do.jsp?type=0&tId="
						+siteForm.siteTemplateId.value+"&tName="
						+siteForm.siteTemplateName.value
						+"&siteId=<%=siteId%>";
	siteForm.submit();
	if(flag)window.close();

}
function preview()
{
	var siteForm = document.all.siteForm;
	var siteId = "<%=siteId%>";
	var templateId = siteForm.siteTemplateId.value;
	//alert("预览站点id为:"+siteId+",模板id为"+templateId+"的模板!");
	if(templateId == 0)
	{
		alert("请先选择首页模板!");
		return false ;
	}
	window.open("../templateManage/template_previewbyid.jsp?tplId=" + templateId + "&siteId=<%=siteId%>");
}
function setSiteTemplate()
{
 		var w = showModalDialog("../channelManage/channel_templateSet_frameset.jsp?siteId=<%=siteId%>&type=0&action=search&isSite=is&stId="+siteForm.siteTemplateId.value+"&stName="+siteForm.siteTemplateName.value,"setChannelOutlineTemplateId","dialogWidth:800px;dialogHeight:505px;help:no;scroll:yes;status:no");
		if(w!=null && w.length>0)
		{
			siteForm.siteTemplateId.value = w[0];
			siteForm.siteTemplateName.value = w[1];
		}

}
</script>
	</head>
	<body bgcolor="#F7F8FC" leftmargin="0" topmargin="0">
		<form id="siteForm" name="siteForm" target="exeman"  action="" method="post">
			<input name="siteId" type="hidden" id="siteId" value="<%=siteId%>">
			<table width="100%" border="0" cellpadding="0" cellspacing="0" align="center" bordercolorlight="#FFFFFF">
				<tr>
					<td colspan="2" class="topDIV" align="left" valign="top">
						&nbsp;
						<div class="siteCTitle">
							站点模板设置
						</div>
					</td>
				</tr>
				<tr>
					<td width="100" height="24" align="right" nowrap bgcolor="#EDEFF6">
						站点中文名称:
					</td>
					<td height="24">
						&nbsp;&nbsp;<%=(siteName == null ? "&nbsp;" : siteName)%>
					</td>
				</tr>
				<tr><td colspan="2"></td></tr>
				<tr>
					<td width="25%" height="24" align="right" nowrap>
						首页模板:
					</td>
					<td height="24">
						<input name="siteTemplateId" id="siteTemplateId" type="hidden" value="<%=stId%>" />
						<input name="siteTemplateName" id="siteTemplateName" type="text" style="width:110px" 　class="cms_text" disabled value="<%=stName%>" />
						<input class="Channel_5wordsBtn" type="button" value="选 择" id="btsetSiteTemplate" name="btsetSiteTemplate" onClick="setSiteTemplate()" />
						<input class="Channel_5wordsBtn" type="button" value="预 览" id="siteTemplatePreview" name="siteTemplatePreview" onClick="preview()">
					</td>
				</tr>
				<tr>
				</tr>
			</table>
			<br>
			<div style="text-align:center">
				<INPUT name="button" type="button" class="cms_button" onClick="save()" value="保存">
				&nbsp;&nbsp;&nbsp;
				<INPUT name="button" type="button" class="cms_button" onClick="window.returnValue = 'false';window.close()" value="关闭">
			</div>
		</form>
		<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
	</body>
</html>
