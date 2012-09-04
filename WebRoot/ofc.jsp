<%@page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<% String contextPath = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Line Dot</title>

<script type="text/javascript" src="common/scripts/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="include/swfobject.js"></script>
<script type="text/javascript" language="javascript">
$(document).ready(function() {
	swfobject.embedSWF("include/open-flash-chart.swf",
					   "chart",
					   "600",
					   "180",
					   "9.0.0",
					   "expressInstall.swf", 
					   {"data-file" : "line.action"}
					   
		   			  );
});
</script>
</head>
<body>
<div id="chart"></div>
</body>
</html>

