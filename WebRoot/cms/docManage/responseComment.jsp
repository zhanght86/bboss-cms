<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	String srcCommentId = request.getParameter("srcCommentId");
	String srcComment = request.getParameter("srcComment");
	String docId = request.getParameter("docId");
	
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>请留言：：</title>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<link href="inc/css/cms.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.STYLE1 {color: blue}
	</style>
	<script language="javascript">
		function sub(){
			if(myForm.docComment.value==""){
				alert("回复不能为空");
				return;
			}
			myForm.action="addCommentHandleDefault.jsp?docId=<%=docId%>&srcCommentId=<%=srcCommentId%>&flag=response";
			myForm.submit();		
		}
		function changeBoxVluae(){
			if(myForm.userhidebox.checked)
				myForm.userhide.value = "1";
			else myForm.userhide.value = "0";
		}
	</script>
</head>
<body scroll=no leftmargin=0>
	<form method="post" target="addCommentIframe" name="myForm">
		<table width="100%"  align="center">
		  <tr>
			<td width="15%">用户名:</td>
			<td width="18%"><input type="text" name="commenterName" size="15"></td>
			<td width="13%">密码:</td>
			<td width="15%"><input type="password" name="psword" size="10"></td> 
			<td ><input type="checkbox" name="userhidebox" checked="checked" onClick="changeBoxVluae()">匿名发表</td>
			<input type="hidden" name="userhide" checked="checked" value="1">
		  </tr>
		  <div>
		  	原贴：<%=srcComment%>		  </div>
		  <tr>
			<td colspan="5" height="100" align="left"><textarea style="width:80%; height:100;" name="docComment"></textarea></td>
		  </tr>
		  <tr>
			<td colspan="5" align="center">
				<input type="button" name="button" onClick="sub()"  class = "cms_button" value="提交留言">&nbsp;
				<input type="button" name="button" onClick="window.close()"  class = "cms_button" value="取消">			</td>
		  </tr>
		</table>
	</form>
	<div height=0 width=0 style="display:none">
		<iframe name="addCommentIframe"></iframe>
	</div>
</body>
</html>