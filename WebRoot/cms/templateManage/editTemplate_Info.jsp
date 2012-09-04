<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="java.util.List" %>
<%
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>编辑模板信息</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 00px;
	margin-right: 0px;
	margin-bottom: 0px;
}
body,td,th {
	font-family: Verdana, 宋体;
	font-size: 12px;
}
-->
</style>
<script type="text/javascript"> 
function setAsTemplate(){
	var templateName = document.form1.templateName.value;
	if(templateName.search(/\S/img)==-1){
		alert("请输入模板名!");
		return;
	}
	var templateDesc = document.form1.templateDesc.value;
	if(templateDesc.search(/\S/img)==-1){
		alert("请输入模板的简短描述!");
		return;
	}
	document.form1.submit();
}
</script>

</head>
<body background="../images/grayline_bg.jpg">
<%
	String templateId = request.getParameter("templateId");
	if(templateId==null || templateId.trim().length()==0){
%>	
<script type="text/javascript">
		alert("没有模板id无法编辑模板!");
		return;
</script>
<%
	return;
	}
	Template t = new TemplateManagerImpl().getTemplateInfo(templateId);
	
%>
<form name="form1" id="form1" action="editTemplate_Info_do.jsp" target="hidFrm">
<input name="templateId" type="hidden" id="templateId" value="<%=templateId%>">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td width="80" align="right" valign="middle">
			 文件名:		
		</td>
		<td>
			<input name="fileName" type="text" class="cms_text" id="fileName" value="<%=t.getTemplateFileName()%>" readonly="true">
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			文件路径:	
		</td>
		<td>
			<input name="uri" type="text" class="cms_text" id="uri" value="<%=t.getTemplatePath()%>" readonly="true">
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			模板名称:	
		</td>
		<td>
			<input name="templateName" type="text" class="cms_text" id="templateName" size="20" maxlength="40" value="<%=t.getName()%>">
			<span class="red_star">*</span>
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			模板类别:		</td>
		<td><select name="templateType" class="cms_select" id="templateType">
				<option value="0" <%=(0==t.getType())?"selected":""%> >首页</option>
				<option value="1" <%=(1==t.getType())?"selected":""%> >概览</option>
				<option value="2" <%=(2==t.getType())?"selected":""%> >细览</option>
			</select>
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">模板风格:</td>
		<td><select name="templateStyle" class="cms_select" id="templateStyle">
			<%
				TemplateStyleManager tsm = new TemplateStyleManagerImpl();
				List list = tsm.getAllTemplateStyleIdAndName();
				int tId = t.getStyle();
				for(int i=0;i<list.size();i++){
					TemplateStyleInfo tsi = (TemplateStyleInfo)list.get(i);
					if(tId != tsi.getStyleId().intValue()){
			%>
				<option value="<%= tsi.getStyleId().intValue() %>"><%= tsi.getStyleName() %></option>
			<%		} else {
			%>
				<option value="<%= tsi.getStyleId().intValue() %>"
					selected="selected"><%= tsi.getStyleName() %></option>
			<%
					}
				}
			%>
		</select>
		</td>
	</tr>
	<tr>
		<td width="80" align="right" valign="middle">
			模板描述:
		</td>
		<td>
			<input name="templateDesc" type="text" class="cms_text" id="templateDesc" size="30" maxlength="100" value="<%=t.getDescription()%>">
			<span class="red_star">*</span>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center" valign="middle">
			<input type="button" class="cms_button" value="确定" onclick="setAsTemplate()">&nbsp;&nbsp;
			 <input type="button" class="cms_button" value="取消" onclick="top.close()">	
		</td>
	</tr>
</table>
</form>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>
