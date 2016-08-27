<%
/**
  *  文档的扩展字段CLOB字段信息纯文本编辑器
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
<title>文本编辑器</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<script language="javascript">
	var name = "<%=name%>";
	//init()
	function init()
	{
		//alert(name);
		document.all.textvalue.value = window.dialogArguments.document.all(name).value;
	}
	//subform()
	function subform()
	{//alert(window.dialogArguments.document.all(name).value);
		window.dialogArguments.document.all(name).value = document.all.textvalue.value;
		//alert(window.dialogArguments.document.all(name).value);
		window.close();
	}
</script>
<body onload="init()">
<table>
	<tr>
	<td align="center">
		<textarea name="textvalue" cols="78" rows="25" class="cms_textarea"></textarea>
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