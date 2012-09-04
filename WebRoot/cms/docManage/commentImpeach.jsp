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
	
	String commentId = request.getParameter("commentId");
	String comment = request.getParameter("comment");
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<title>举报信息：：</title>
	<script src="../inc/js/func.js"></script>
	<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
	<link href="inc/css/cms.css" rel="stylesheet" type="text/css">
	<style type="text/css">
		.STYLE1 {color: blue}
	</style>
	<script language="javascript">
		function sub(){
			if(myForm.impeacher.value==""){
				alert("请您填写姓名！");
				return;
			}
			var flag = false;
			var radios = document.getElementsByName("reason");
			for(var i=0;i<radios.length;i++){
				if(radios[i].checked)
					flag = true;
			}
			if(flag == false){
				alert("请您选择举报原因！");
				return;
			}
			if(myForm.email.value!="" && myForm.email.value.search(/\w*@w*/i)==-1){
				alert("请您书写正确的邮箱，以便我们与您联系！");
				return;	
			}
			myForm.action="addImpeachHandleDefault.jsp?commentId=<%=commentId%>";
			myForm.submit();		
		}
	</script>
</head>
<body scroll=no leftmargin=0>
	<form method="post" target="addCommentIframe" name="myForm">
		<table width="90%"  align="center">
		  <tr>
		  	<td colspan="8">			感谢您帮助我们进行不良信息的清除工作，我们将认真仔细的处理您提交的内容。如果您在以下留言中发现有色情、暴力有其他一些违规的内容，请填写下面的表单反馈给我们，您在此表单填写的详细信息将帮助我们更好的进行查证工作。			</td>
		  </tr>
		  <tr>
		  	<td colspan="8" style="word-break:break-all;word-wrap:break-word;">评论:<br>&nbsp;&nbsp;<%=comment.length()>200?comment.substring(0,200)+"...":comment%></td>
		  </tr>
		  <tr>
			<td width="14%">您的姓名:</td>
			<td width="44%"><input type="text" name="impeacher" size="15"></td>
			<td width="42%"><font color="#FF0000">必填</font></td>
		  </tr>
		  <tr>
			<td width="14%">您的邮箱:</td>
			<td width="44%"><input type="text" name="email" size="15"></td>
			<td width="42%">填写邮箱以便我们跟您联系！</td>
		  </tr>
		  <tr>
			<td width="14%">举报原因:</td>
			<td width="44%">
							<input type="radio" name="reason" id="reason" value="0">反动
							<input type="radio" name="reason" id="reason" value="1">色情
							<input type="radio" name="reason" id="reason" value="2">暴力
							<input type="radio" name="reason" id="reason" value="3">侵权
			<input type="radio" name="reason" id="reason"  value="4">其他			</td>
			<td width="42%"><font color="#FF0000">必选</font></td>
		  </tr>
		  <tr>
		  	<td>举报描述：</td>
			<td colspan="5" height="100" align="left"><textarea style="width:100%; height:100;" name="description"></textarea></td>
		  </tr>
		  <tr>
			<td colspan="5" align="center">
				<input type="button" name="button" onClick="sub()"  class = "cms_button" value="提交">&nbsp;
				<input type="button" name="button" onClick="window.close()"  class = "cms_button" value="取消">			</td>
		  </tr>
		</table>
	</form>
	<div height=0 width=0 style="display:none">
		<iframe name="addCommentIframe"></iframe>
	</div>
</body>
</html>