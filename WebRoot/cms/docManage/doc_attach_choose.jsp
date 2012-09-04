<%
/**
  *	文档相关附件选择
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String docpath = request.getParameter("docpath");
	String type = request.getParameter("type");//判断是图片还是附件
	String typeName = "";
	if("2".equals(type))
		typeName = "附件";
	if("3".equals(type))
		typeName = "图片";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>:::::相关<%=typeName%>的选择::::::::::::::::::::::.</title>
<style type="text/css">
	.STYLE1 {color: #0000FF}
	.STYLE2 {color: #000099}
	.operStyle{
	width:17;
	height:16;
	}
.style7 {font-size: 5px}
</style>
<script src="../inc/js/func.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	function subform(){
		var filepath = uploadform.UploadFileName.value;
		var type = "<%=type%>"
		if(filepath=="")
		{
			alert("请选择上传文件！");
			return false;
		}
		if(type=="3")
		{
			var reg =/.+\.([Jj][Pp][Gg]|[Bb][Mm][Pp]|[Gg][Ii][Ff]|[Jj][Pp][Ee][Gg])\b/;
			if(!reg.test(filepath))
			{
				alert("提示：\n\n请选择一个有效的文件，\n支持的格式有（gif|jpg|jpeg|bmp）！");
				return false;
			}
		}

		uploadform.action = "doc_attach_upload.jsp?docpath=<%=docpath%>";
		uploadform.submit();
		document.all.divProcessing.style.display = "";
	}

	function UploadLoaded(fileAllName){
		//alert(window.parent.doc_attach_list);
		var tableatt = window.dialogArguments.document.all("doc_attach_list");
		//alert(tableatt);
		
		var row1 = tableatt.insertRow(1);
		var td1 = window.dialogArguments.document.createElement("td");
		var td2 = window.dialogArguments.document.createElement("td");
		var td3 = window.dialogArguments.document.createElement("td");

		var originalFilename = uploadform.UploadFileName.value;
		originalFilename = originalFilename.substring(originalFilename.lastIndexOf("\\")+1,originalFilename.length);

		td1.innerHTML="<input type='checkBox' name='id' hideFocus=true onclick='checkOne(id)'/>";		 
		td2.innerHTML = "<input type='hidden' name='url' value='"+fileAllName+"'/>" + "<input type='text' name='description' value='"+originalFilename+"'/>";
		
		td3.innerHTML = "<input type='hidden' name='originalFilename' value='" + originalFilename +"' />" + "<a href='#' onclick=downloadattrs('../siteResource/<%=docpath%>/"+fileAllName+"')>"+uploadform.UploadFileName.value+"</a>";
		row1.appendChild(td1);
		row1.appendChild(td2);
		row1.appendChild(td3);
		window.close();
	}
</SCRIPT>
</head>
<body bgcolor="#e7e7f7">
	<form target="uploadattach" method="post" name="uploadform" enctype="multipart/form-data">
		<table width="100%" bgcolor="#e7e7f7">
			<tr>
				<td width="100%">
					<font color="#000000">
						1、点“
						<font color="#8080ff">浏览...</font>
						”按钮，选择你想要上传的文件，
					</font>
					<br>
					<input type="file" name="UploadFileName" id="UploadFileName" size="20">
					<br>
					<br>
					<font color="#000000">
						2、选定一个文件后，点击“
						<font color="#8080ff">确定</font>
						”按钮，将该文件上传；
						<br>
					</font>
					<center>
						<input type="button" name="add" value="确定" onclick="subform()">
						&nbsp;&nbsp;&nbsp;&nbsp;
						<input type="button" name="close" value="取消" onclick="window.close();">
					</center>
				</td>
			</tr>
		</table>
	</form>
	<div id="divProcessing" style="width:180px;height:30px;position:absolute;left:50px;top:30px;display:none">
		<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
			<tr>
				<td bgcolor=#3A6EA5>
					<marquee align="middle" behavior="alternate" scrollamount="5">
						<font color=#FFFFFF>...文件上传中...请等待...</font>
					</marquee>
				</td>
			</tr>
		</table>
	</div>
	<iframe name="uploadattach" height="0" width="0"></iframe>
</body>
</html>