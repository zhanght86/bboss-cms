<%@ page contentType="text/html; charset=UTF-8" %>

<html>
	<head>
		<link href="../../css/navigator.css" rel="stylesheet" type="text/css">
		<script src="../../include/leftFrame.js"></script>
	</head>    

	<body bgcolor="#B9CEF0" width="100%" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no">
 
		<script language="JavaScript">
			init(3);
			
			addItem('0','模板标签','template_tags.jsp');
			addItem('1','组件标签','component_tags.jsp');
			addItem('2','模板基本信息','template_baseinfo.jsp');
			finishIt();
			clickItem(0);
		</script>
	</body>
</html>