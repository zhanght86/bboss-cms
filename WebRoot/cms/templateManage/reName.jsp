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
	String tempuri = request.getParameter("tempuri");
	String viewtype = request.getParameter("viewtype");
	//String templateId=request.getParameter("templateId");//增加一个模板ID
	String fileFlag = request.getParameter("fileFlag");//区分文件为图片，多媒体，首页等
	if(tempuri==null || tempuri.trim().length()==0){
%>
		<script type="text/javascript">
			alert("不知道要改哪个文件(或文件夹),请在列表中选中一个文件(或文件夹).");
			top.close();
		</script>
<%	
		return;
	}
%>
<style type="text/css">
<!--
body,td,th {
	font-family: Verdana, 宋体;
	font-size: 12px;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
<script language="javascript">
var win = window.dialogArguments;
function reName(){
	var fileFlag = "<%=fileFlag%>";
	var pattern=/\.(bmp|gif|jpeg|jpg)$/i;
	
	var filename = document.form1.fileName.value;
	if(filename.search(/\S/img)==-1){
		alert("请输入新的文件(或文件夹)名称.");
		return;
	}
	else
	{
		if(fileFlag == "" || fileFlag == "pic")//图片
		{
			if(filename.search(/\S/)==-1){
				alert("请输入新的文件(或文件夹)名称.");
				return;
			}
			if(!pattern.test(filename)){
				alert("这里的图片后缀名只能为.gif,.bmp,.jpeg,.jpg!");
				return;
			}
		}
		if(fileFlag == "" || fileFlag == "media")//多媒体文件
		{
			pattern=/\.(rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rmvb)$/i;
			if(filename.search(/\S/)==-1){
				alert("请输入新的文件(或文件夹)名称.");
				return;
			}
			if(!pattern.test(filename)){
				alert("这里的多媒体文件后缀名只能为rm|mp3|wav|mid|midi|ra|avi|mpg|mpeg|asf|asx|wma|mov|wmv|rmvb!");
				return;
			}
		}
		if(fileFlag == "" || fileFlag == "1")//首页文件
		{
			pattern=/\.(html|htm|jsp)$/i;
			if(filename.search(/\S/)==-1){
				alert("请输入新的文件(或文件夹)名称.");
				return;
			}
			if(!pattern.test(filename)){
				alert("这里的首页文件后缀名只能为html|htm|jsp!");
				return;
			}
		}
	}
	document.form1.action = "reName_do.jsp?viewtype=<%=viewtype%>";
	document.form1.submit();
}
</script>
</head>
<body>
<form action="reName_do.jsp" method="post" name="form1" target="hidFrm">
<table width="400" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="300" colspan="2" align="center" valign="middle">
		<table width="400" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="25" align="right" valign="middle">原先文件(夹)名:</td>
				<td height="25" align="left" valign="middle"><%=tempuri%></td>
			</tr>
			<tr>
				<td height="25" align="right" valign="middle">新文件(夹)名:</td>
				<td height="25" align="left" valign="middle"><input name="fileName" type="text" id="fileName">
						<input name="uri" type="hidden" id="uri" value="<%=(uri==null?"":uri)%>">
						<input name="oldFileName" type="hidden" id="oldFileName" value="<%=tempuri%>">
						<input type="button" class="cms_button" name="Submit" value="确定" onClick="reName()">
						<input type="button" class="cms_button" name="quxiao" value="取消" onClick="window.close()">
				</td>
			</tr>
		</table></td>
		</tr>
</table>
</form>
<iframe id="hidFrm" name="hidFrm" style="display:none"></iframe>
</body>
</html>
