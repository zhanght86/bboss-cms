<%
/**
  *  文档的扩展字段CLOB字段信息HTML编辑器
  *
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String name = request.getParameter("name");
%>
<html>
<head>
<title>HTML编辑器</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<script language="javascript">
	var name = "<%=name%>";
	//subform()
	function subform()
	{
		window.dialogArguments.document.all(name).value = eWebEditor1.eWebEditor.document.body.innerHTML;
		window.close();
	}
	// 替换特殊字符
	function HTMLEncode(text){
		text = text.replace(/&/g, "&amp;") ;
		text = text.replace(/"/g, "&quot;") ;
		text = text.replace(/</g, "&lt;") ;
		text = text.replace(/>/g, "&gt;") ;
		text = text.replace(/'/g, "&#146;") ;
		text = text.replace(/\ /g,"&nbsp;");
		text = text.replace(/\n/g,"<br>");
		text = text.replace(/\t/g,"&nbsp;&nbsp;&nbsp;&nbsp;");
		return text;
	}
</script>
<body >
<table width="100%" border="0" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4">
	<tr width="100%">
	<td align="center">
		<script language="javascript">
		var content = window.dialogArguments.document.all(name).value;
		document.write("<input type='hidden' name='content' value='" + HTMLEncode(content) + "'>");
		</script>
		<iframe id="eWebEditor1" src="<%=request.getContextPath()%>/cms/editor/eWebeditor/eWebEditor.jsp?id=content&style=standard&edittype=self" frameborder="0" scrolling="no" width="100%" height="460">										
		</iframe>
	</td>
	</tr>
	<tr><td>&nbsp;</td></tr>
	<tr>
	<td align="center">
	<input type="button" name="subform" value="保存" class="cms_button" onclick="subform();"/>
	<input type="button" name="closew" value="关闭窗口" class="cms_button" onclick="window.close();"/>
	</td>
	</tr>
</table>
</body>
</html>