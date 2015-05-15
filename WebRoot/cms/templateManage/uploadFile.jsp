<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<% 
	AccessControl control = AccessControl.getAccessControl();
	String uri = request.getParameter("uri");
%>
<style type="text/css">
<!--
body,td,th {
	font-family: Verdana, 宋体;
	font-size: 12px;
}
body {   background:F2FAFF;
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
input{ border:1px  #999999 solid; height:18px; color:#1974C8; font-size:12px; background:#EDF5FA;}
-->
</style>
<script language="javascript">
var win = window.dialogArguments;
function uploadFile(){
	var file = document.form1.newFile.value;
	if(file.search(/\S/img)==-1){
		alert("请选择一个要上传的文件!");
		return;
	}
	document.form1.pathContext.value=win.parent.fileList.document.form1.pathContext.value;
	
	document.form1.submit();
}
</script>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<body>
<form action="uploadFile_do.jsp" method="post" enctype="multipart/form-data" name="form1" target="hidFrm">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr><td height="10"></td></tr>
	<tr>
		<td class="Template_CreateBox" align="center">
请选择文件:
				<input type="file" name="newFile" id="newFile">
				<input name="uri" type="hidden" id="uri" value="<%=(uri==null?"":uri)%>">
				<input name="pathContext" type="hidden" id="pathContext" >
				<input type="checkbox" id="coverFlag" name="coverFlag">&nbsp;覆盖重名文件
	  </td>
	</tr>
	<tr>
	<td class="Template_CreateBox" align="center">
				<input type="button" class="cms_button" name="Submit" value="确定" onClick="uploadFile()">
				<input type="button" class="cms_button" name="quxiao" value="取消" onClick="window.close()"></td>
	</tr>
</table>
</form>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>
