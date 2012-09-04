<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleManager" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleManagerImpl" %>
<%@ page import="java.util.List" %>
<%@ page import="com.frameworkset.platform.cms.templatemanager.TemplateStyleInfo" %>
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
<title>模板导入</title>
</head>
<script language="javascript">

function submitForm(){
	var f = document.all.TemplateForm;
	var action = "template_add2_do.jsp?siteId=<%=request.getParameter("siteId")%>";
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


	var uri = f.uri.value.replace(/\s/ig,"");

	if(uri.search(/\\{2,}/img)!=-1||uri.search(/\/{2,}/img)!=-1||uri.search(/\\\/{1,}/img)!=-1||uri.search(/\/\\{1,}/img)!=-1){
		alert("请输入正确的相当路径");
		return;
	}
	if(uri.search(/[:\*\?"<>\|]/img)!=-1){
		alert("请输入正确的相当路径");
		return;
	}
	uri = f.uri.value;
	
	var fileName = f.fileName.value;
	if(fileName.replace(/\s/ig,"")==""){
		alert("请输入主模板文件的名称.");
		return;
	}
	if(fileName.search(/(\.html|\.htm){1}$/img)<=0){
		alert("主模板文件的扩展名必须是.htm,.html.");
		return;		
	}
	var templateTypes = document.getElementById("templateType");
	var templateType = templateTypes[templateTypes.selectedIndex].value;//模板类型编号
	var templateStyles = document.getElementById("templateStyle");
	var templateStyle = templateStyles[templateStyles.selectedIndex].value;//模板风格编号
	
	//通过post方法和文件一起传过去在服务器端取不出来
	action = action + "&templateName="+templateName
			+"&templateDesc="+templateDesc
			+"&templateType="+templateType
			+"&uri="+uri
			+"&fileName="+fileName
			+"&templateStyle="+templateStyle;
	f.action=action;
	//alert(f.action);
	f.submit();
}

</script>
<body  bottommargin="0"  bgcolor="#F3F4F9"  rightmargin="0" leftmargin="0" topmargin="0" >
<form method="post" name="TemplateForm" target="hidFrm" id="TemplateFrm">
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
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
			<table width="100%" border="0" cellspacing="0" cellpadding="1"  class="Templateedittable">
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="F3F4F9" class="STYLE5">模板名称:</td>
					<td>
						<input name="templateName" type="text" class="cms_text" id="templateName" size="48" />
						<span class="red_star">*</span>					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="F3F4F9" class="STYLE5">模板描述:</td>
					<td>
						<input name="templateDesc" type="text" class="cms_text" id="templateDesc" size="48" />
						<span class="red_star">*</span>					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="F3F4F9" class="STYLE5">&nbsp;模板类型:</td>
					<td><select name="templateType" style="width:54" class="cms_select" id="templateType">
							<option value="0">首页</option>
							<option value="1" selected>概览</option>
							<option value="2">细览</option>
						</select>
					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="F3F4F9" class="STYLE5">&nbsp;模板风格:</td>
					<td>
					<select name="templateStyle" class="cms_select" id="templateStyle" >
						<%
							TemplateStyleManager tsm = new TemplateStyleManagerImpl();
							List list = tsm.getAllTemplateStyleIdAndName();
							for(int i=0;i<list.size();i++){
								TemplateStyleInfo tsi = (TemplateStyleInfo)list.get(i);
						%>
								<option value="<%= tsi.getStyleId() %>"><%= tsi.getStyleName() %></option>
						<%
							}
						%>
					</select>
					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="F3F4F9" class="STYLE5">输入模板存放的路径:</td>
					<td>
						<input name="uri" type="text" id="uri" size="20" maxlength="40"  class="cms_text">
					</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="F3F4F9" class="STYLE5">模板文件的名称:</td>
					<td>
						<input name="fileName" type="text" id="fileName" size="20" maxlength="40"  class="cms_text">
						<span class="red_star">*</span>(如index.html)&nbsp;<input type="checkbox" id="coverFlag" name="coverFlag">&nbsp;覆盖重名文件</td>
				</tr>
				<tr>
					<td width="140" height="25" align="right" nowrap bgcolor="F3F4F9" class="STYLE5">模板的内容:</td>
					<td><textarea name="content" cols="130" rows="30" id="content"></textarea></td>
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
