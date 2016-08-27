<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.*"%>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);

	response.setHeader("Cache-Control","no-cache");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader ("Expires", -1);
	response.setDateHeader("max-age", 0); 
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模板管理</title>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>

<style type="text/css">
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-image: url(../images/grayline_bg.jpg);
}
.topDIV {
	background-image: url(../images/toolbar_common_func_template.jpg);
	height:82px;
}
.btDIV{
	padding-top: 7px;
	padding-left: 55px;
	height:22px;
}
.anniuDIV{
	height:60px;
	padding-bottom: 8px;
}

</style>
<script language="javascript">
var tplNavFrame = null; //Navigator框架
var tplTagFrame = null; //模板标签面板框架
var openWin = window.dialogArguments;
//提交form，添加一个模板
function addTpl(){
	var openWin = window.dialogArguments;

	document.all.addTpltForm.save.blur();

	if(document.all.templateName.value.search(/\S/g)==-1){
		alert("请在模板基本信息栏目中输入模板名字.");
		return;
	}

	if(document.all.templateDesc.value.search(/\S/g)==-1){
		alert("请在模板基本信息栏目中输入模板简短描述.");
		return;
	}

	if(document.all.templateType.value.search(/\S/g)==-1){
		alert("请在模板基本信息栏目中选择模板类型.");
		return;
	}
	if(document.all.tb_templateHead.value.search(/\S/g)==-1){
		alert("请在模板基本信息栏目中输入模板头.");
		return;
	}
	

	document.all.addTpltForm.content2.value = eWebEditor1.getTemplateText();
	//alert(document.all.addTpltForm.content2.value);
	document.all.addTpltForm.submit();
}
</script>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="topDIV">
	<tr>
		<td  class="btDIV">新建模板</td>
	</tr>
	<tr>
		<td align="right" valign="bottom" class="anniuDIV">
		<form action="template_createdo.jsp" method="post" name="addTpltForm" id="addTpltForm" target="blockbox">	
			<input type="hidden" name="templateName" id="templateName">					
			<input type="hidden" name="siteId" id="siteId" value="<%=request.getParameter("siteId")%>">
			<input type="hidden" name="templateType" id="templateType">
			<input type="hidden" name="templateDesc" id="templateDesc">
			<input type="hidden" name="tb_templateHead" id="tb_templateHead">
			<input type="hidden" name="content2" id="content2" value="">
			<input type="button" class="cms_button"  id="save" onClick="addTpl()" class="cms_button" value="保存" />
			 &nbsp;  
			<input type="button" class="cms_button" class="cms_button" onClick="window.close()" value="关闭" />	
		</form>

		</td>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="20" align="left">
			模板内容:
		</td>
		<td height="600" rowspan="2">
			<iframe id="template_navigator" frameborder="0" width="234" src="template_navigator.jsp" height="100%"></iframe>			
		</td>
	</tr>
	<tr>
		<td height="580" align="right">
		<form id="form2" name="form2">

			<input type="hidden" name="content" id="content">
			<input type="hidden" name="siteId" id="siteId" value="<%=request.getParameter("siteId")%>">
			<iframe id="eWebEditor1" src="eWebeditor/eWebEditor.jsp?id=content&style=standard&siteId=<%=request.getParameter("siteId")%>" frameborder="0" scrolling="no" width="766" height="100%"></iframe>
		</form>
		</td>
	</tr>
</table>
<iframe id="blockbox" name="blockbox" style="display:none"></iframe>
</body>
</html>