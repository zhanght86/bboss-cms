<%@page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"%>
<%
	String publishMessage = (String)session.getAttribute("publishMessage");
	//System.out.println(publishMessage);
	//String head = publishMessage.split("：")[0];
	//String messageContent = publishMessage.split("：")[1];
	String[] messages = publishMessage.split(";");
%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<body>
<div>
<%
for(int i=0;i<messages.length;i++){
	String oneMessage = messages[i];
//	String[] oneMessageDetail = oneMessage.split(":");
//	for(int j=0;j<oneMessageDetail.length;j++){
//		System.out.println(oneMessageDetail[j]);
//		out.println(oneMessageDetail[j]);
//	}
	%>
	<div>
		<%=oneMessage%>
	</div>
	<%
}
%>
</div>
<div align="center">
<input type="button" name="back" class="cms_button" value="返回" onclick="window.close()">
</div>
</body>
</html>