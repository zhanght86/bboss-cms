<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<pg:notnull actual="${errorMsg}">
<SCRIPT LANGUAGE="JavaScript">
	//调用父窗口的结束方法
		alert("${errorMsg}");
		parent.parent.ImageListFrm.document.getElementById('divProcessing').style.display='none';
</SCRIPT>
</pg:notnull>
<pg:null actual="${errorMsg}">
<script type="text/javascript">
	
	if(parent.parent.ImageListFrm.document.getElementById("divProcessing"))
    	parent.parent.ImageListFrm.document.getElementById("divProcessing").style.display="none";
	
   
	parent.parent.ImageListFrm.location.href = parent.parent.ImageListFrm.location+"&fileName=${fileName}";
	
	parent.parent.TreeFrm.location.href = parent.parent.TreeFrm.location;
	
	//alert(parent.parent.ImageListFrm.location.reload());
	
</script>
</pg:null>
</body>
</html>