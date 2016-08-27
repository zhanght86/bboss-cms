<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.io.*" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@page import="com.frameworkset.platform.cms.driver.htmlconverter.*"%>
<%@page import="com.frameworkset.platform.cms.driver.i18n.*"%>
<%@page import="java.util.*"%>
<%@page import ="com.frameworkset.platform.cms.templatemanager.*"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", -1);
	response.setDateHeader("max-age", 0);


%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
.STYLE5 {color: #486086; font-weight:bold}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>模板编辑</title>
</head>
<script language="javascript">

function submitForm(){
	var f = document.all.TemplateForm;
	var action = "template_edit2_do.jsp";
	var templateName = f.templateName.value;
	if(templateName.replace(/\s/ig,"")==""){
		alert("请输入模板名.");
		return;
	}	

	var templateDesc = f.templateDesc.value;
	if(templateDesc.replace(/\s/ig,"")==""){
		alert("请输入模板的简单描述.");
		return;
	}

	f.action=action;
	f.submit();
}

</script>
<body  bottommargin="0" bgcolor="#F3F4F9" rightmargin="0" leftmargin="0" topmargin="0">
<%
	String siteId = request.getParameter("siteId");
	String userId = control.getUserID()+"";
	SiteManager siteManager = new SiteManagerImpl();
	String templateId = request.getParameter("templateId");
	String sitedir = siteManager.getSiteInfo(siteId).getSiteDir();//站点相对路径

	if(siteId==null||siteId.trim().length()==0||templateId==null||templateId.trim().length()==0){
%>
	<script language="javascript">
		alert("请提供站点id和模板id");
		top.close();
	</script>
<%		
		return;
	}
	Template template = new TemplateManagerImpl().getTemplateInfo(templateId);
	if(template==null){
%>
	<script language="javascript">
		alert("根据模板id没有找到相应模板!");
		top.close();
	</script>
<%		
		return;
	}
	if(template.getPersistType()==1){
	String sitepath = new SiteManagerImpl().getSiteAbsolutePath(siteId);
		if(sitepath==null || sitepath.trim().length()==0){
	%>
		<script language="javascript">
			alert("模板文件是存储在文件系统中,但根据站点id没有找到站点对应的路径!");
			top.close();
		</script>
	<%		
			return;
		}
		String theURI = "";
		if(template.getTemplatePath()!=null&&template.getTemplatePath().trim().length()!=0){
			theURI = template.getTemplatePath().trim()+"/";
		}
		if(template.getTemplateFileName()==null || template.getTemplateFileName().trim().length()==0){
			%>
			<script language="javascript">
				alert("模板存储在数据库中，没有模板的主文件名，无法编辑模板!");
				top.close();
			</script>
			<%		
			return;			
		}
		theURI += template.getTemplateFileName();
		FileManager fm = new FileManagerImpl();
		try{
			fm.checkOutFile(siteId,theURI,userId);
		}catch(Exception e){
			%>
			<script language="javascript">
				alert('编辑模板之前试图对模板文件锁定发生错误，<%=e.getMessage().replace('\n',' ').replace('\'',' ')%>');
				top.close();
			</script>
			<%		
			return;			
		}
		File topTemplateFolder = new File(sitepath,"_template");
		File currTemplateFolder = new File(topTemplateFolder.getAbsolutePath(),template.getTemplatePath()==null?"":template.getTemplatePath());
		File currTemplateFile = new File(currTemplateFolder.getAbsolutePath(),template.getTemplateFileName());

		if(currTemplateFile.exists()){
			String value = FileUtil.getFileContent(currTemplateFile.getAbsolutePath(),CMSUtil.getCharset());

			//处理文档内容
			CmsLinkProcessor processor = new CmsLinkProcessor(request,template.getTemplatePath(),sitedir);
			processor.setHandletype(CmsLinkProcessor.PROCESS_READTEMPLATE);
			try {
				String content = processor.process(value,CmsEncoder.ENCODING_UTF_8);
				value = content;//
			} catch (Exception e) {
				e.printStackTrace();
			}
			template.setText(value);
		}else{
			template.setText("");
		}
	}
%>
<form method="post" name="TemplateForm" target="hidFrm" id="TemplateFrm">
<input name="siteId" type="hidden" id="siteId" value="<%=siteId%>">
<input name="persistType" type="hidden" id="persistType" value="<%=template.getPersistType()%>">
<input name="templateId" type="hidden" id="templateId" value="<%=templateId%>">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
	  <td height="82" colspan="4" valign="top" background="../images/toolbar_common_func_template.jpg"><div  class="templateCTitle">模板导入</div></td>
	</tr>
	<tr >
		<td width="4%">&nbsp;</td>
		<td width="3%" height="20"><img src="../images/ico_point.gif" width="25" height="25"></td>
		<td width="93%" height="20" colspan="2">&nbsp;<span class="cms_title_blue">模板基本信息</span></td>
	</tr>
	<tr>
		<td height="8" colspan="4">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="4">
			<table width="100%" border="0" cellspacing="0" cellpadding="3"  class="Templateedittable">
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="#F3F4F9" class="STYLE5">模板名称:</td>
					<td>
						<input name="templateName" type="text" class="cms_text" id="templateName" size="48" value="<%=template.getName()%>"/>
						<span class="red_star">*</span>
					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="#F3F4F9" class="STYLE5">模板描述:</td>
					<td>
						<input name="templateDesc" type="text" class="cms_text" id="templateDesc" size="48" value="<%=template.getDescription()%>"/>
						<span class="red_star">*</span>
					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="#F3F4F9" class="STYLE5">&nbsp;模板类型:</td>
					<td><select name="templateType" class="cms_select" id="templateType">
						<%String type = ""+template.getType(); %>
						<option value="0" <%="0".equals(type)?"selected":""%>>首页</option>
						<option value="1" <%="1".equals(type)?"selected":""%>>概览</option>
						<option value="2" <%="2".equals(type)?"selected":""%>>细览</option>
					</select></td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="#F3F4F9" class="STYLE5">&nbsp;模板风格:</td>
					<td>
					<select name="templateStyle" class="cms_select" id="templateStyle" >
						<%
							TemplateStyleManager tsm = new TemplateStyleManagerImpl();
							List list = tsm.getAllTemplateStyleIdAndName();
							int tId = template.getStyle();
							for(int i=0;i<list.size();i++){
								TemplateStyleInfo tsi = (TemplateStyleInfo)list.get(i);
								if(tId != tsi.getStyleId().intValue()){
						%>
									<option value="<%= tsi.getStyleId().intValue() %>"><%= tsi.getStyleName() %></option>
						<%		} else {
						%>
									<option value="<%= tsi.getStyleId().intValue() %>" selected="selected"><%= tsi.getStyleName() %></option>
						<%
								}
							}
						%>
					</select>
					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="#F3F4F9" class="STYLE5">输入模板存放的路径:</td>
					<td>
						<input name="uri" type="text" id="uri" size="20" maxlength="40" class="cms_text" value="<%=template.getTemplatePath()%>" readonly>
						
					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="#F3F4F9" class="STYLE5">模板文件的名称:</td>
					<td>
						<input name="fileName" type="text" id="fileName" size="20" maxlength="40"  class="cms_text"  value="<%=template.getTemplateFileName()%>" >
						<input type="hidden" name="oldFileName" value="<%=template.getTemplateFileName()%>">
						<span class="red_star">*</span>(如index.html)					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="#F3F4F9" class="STYLE5">模板的内容:</td>
				  <td><textarea name="content" cols="130" rows="30" id="content"><%=template.getText()%></textarea></td>
				</tr>					
				<tr align="center">
					<td height="22" colspan="2">
						<input name="btn_ok" type="button" class="cms_button"  value="确定" onClick="submitForm()"/>
						<input name="btn_close" type="button" class="cms_button"  onclick="window.close();" value="关闭" />
					</td>
				</tr>
		  </table>
		</td>
	</tr>
</table>
</form>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>
