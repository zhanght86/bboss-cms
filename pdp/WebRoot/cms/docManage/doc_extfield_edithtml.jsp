<%
/**
  *  文档的扩展字段CLOB字段信息HTML编辑器
  *
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getAccessControl();

	String name = request.getParameter("name");
	String cusdir =  request.getParameter("cusdir");
	String channelId = request.getParameter("channelId");
%>
<html>
<head>
<title>HTML编辑器</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
</head>
<script language="javascript">
	var name = "<%=name%>";
	//subform()
	function subform()
	{
		//编辑器保持在设计模式
		document.getElementById("eWebEditor1").contentWindow.setMode('EDIT');
		setPageTagNone("eWebEditor1");
		window.dialogArguments.document.all(name).value = (document.getElementById("eWebEditor1").contentWindow.document.getElementById("eWebEditor").contentWindow.document.body.innerHTML);
		
		window.close();
	}
	
</script>
<body >
<table width="100%" border="0" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4">
	<tr width="100%">
	<td align="center">
		
		<input type="hidden" name="channelId" value="<%=channelId%>"/>
		<script language="javascript">
		var content = window.dialogArguments.document.all(name).value;
		document.write("<input type='hidden' name='content' value='" + (content) + "'>");
		</script>
		<iframe id="eWebEditor1" src="<%=request.getContextPath()%>/cms/editor/eWebEditor48/ewebeditor.htm?id=content&style=coolblue&cusdir=<%=cusdir %>" frameborder="0" scrolling="no" width="100%" height="460">										
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