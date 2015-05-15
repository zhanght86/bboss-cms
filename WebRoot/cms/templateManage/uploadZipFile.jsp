<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<% 
	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request, response);
	String uri = request.getParameter("uri");

%>
<style type="text/css">
<!--
body,td,th {
	font-family: Verdana, 宋体;
	font-size: 12px;
}
body {background:F2FAFF;
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
var uri = "<%=(uri==null?"":uri)%>";
function uploadZipFile(){
	var file = document.form1.newFile.value.toLowerCase();
	if(file.search(/\S/img)==-1){
		alert("请选择一个要上传的文件!");
		return;
	}
	if(file.search(/\.zip$/img)==-1){
		alert("只能是zip格式压缩的文件，其他格式的文件将不会解压!");
		return;		
	}
	var uri2 = uri.replace(/\s/img); 
	if(uri2=="/"||uri2=="\\"||uri2==""){
		if(!window.confirm("当前目录为根目录，你确实要把文件上传解压到根目录吗?")){
			return;		
		}
	}
	document.form1.pathContext.value=win.parent.fileList.document.form1.pathContext.value;
	document.form1.action = "uploadZipFile_do.jsp?uri="+uri;			
	document.form1.submit();
}
</script>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="100" align="center" valign="middle">
						<div class="Template_CreateBox">
		<form action="uploadZipFile_do.jsp" method="post" enctype="multipart/form-data" name="form1" target="hidFrm">
			请选择文件:
				<input name="newFile" type="file" id="newFile" size="15">
				<input name="pathContext" type="hidden" id="pathContext" >
				<input name="uri" type="hidden" id="uri" value="<%=(uri==null?"":uri)%>">
				<input type="button" class="cms_button" name="Submit" value="确定" onClick="uploadZipFile()">
				<input type="button" class="cms_button" name="quxiao" value="取消" onClick="window.close()">

		</form></div>
	  </td>
	</tr>
</table>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>
