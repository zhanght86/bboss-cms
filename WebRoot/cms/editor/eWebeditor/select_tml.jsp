<%
/**
  *  模板选择列表
  *
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" errorPage=""%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	DocumentManager dmi = new DocumentManagerImpl();

	String channelId = request.getParameter("channelId");
%>
<html>
<head>
<title>模板选择列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<style type="text/css">
body {padding:0px;margin:0px}
</style>
</head>
<script language="javascript">
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
<body>
<select name="selftmp" style="width:180px;" onChange="" id="tmpType" disabled>
	<%
		List tpllist = null;
		tpllist = dmi.getAllDocTPLList(channelId);
		request.setAttribute("tpllist",tpllist);
	%>
	<pg:list requestKey="tpllist">

	<option value='<pg:cell colName="tplCode"/>' >
	<pg:cell colName="tplName"/> </pg:list>
</select>
</body>
</html>