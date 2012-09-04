<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>    
  <title>保存成功</title>
  <SCRIPT LANGUAGE="JavaScript">
		alert("新增用户成功！");
		//window.dialogArguments.location.reload();
		//var str = window.dialogArguments.location.href;
		//var strArray = str.slice(0,str.indexOf("?"));
		//window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
		var strArray = "../../cms/orgManage/org_userlist.jsp";
		window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
		window.close();
  </SCRIPT>
 </head>
 <body>保存成功
 </body>
 
 </html>
