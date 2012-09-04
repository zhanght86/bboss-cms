<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>

<script type="text/javascript" src="<%=request.getContextPath()%>/common/scripts/jquery-1.4.4.min.js"></script>

<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>

<script type="text/javascript">
	function showWindow() {
		JqueryDialog.Open('新增任务', 'test.jsp', 750, 370);
	}

	function closeWindow() {
		JqueryDialog.Close();
	}
</script>

<body>
<input type="button" value="打开窗口" onclick="showWindow()"/> 
</body>
</html>
