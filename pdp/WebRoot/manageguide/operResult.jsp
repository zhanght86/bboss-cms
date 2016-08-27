<%@page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String s = (String) request.getAttribute("message");
%>
<html>
	<title></title>
	<head>

	</head>
	<body>
	<form action="" method="post" id ="form0" name="form0">
	</form>
	</body>
<script type="text/javascript">
var message = '<s:property value="message"/>';
var consignee = '<s:property value="tdShopDeliverAddr.consignee"/>';
var params = "realName=" + consignee  + "&timeStamp="
		+ new Date().getTime();

document.form0.action = "<%=request.getContextPath()%>/manageguide/manageguide_list.jsp?timeStamp="+ new Date().getTime();
document.form0.target = "queryList";
document.form0.submit();
</script>	
</html>

