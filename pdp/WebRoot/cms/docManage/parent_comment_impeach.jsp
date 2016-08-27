<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String commentId = request.getParameter("commentId");
	String comment = request.getParameter("comment");
%>
<!--本页面是为了刷新加进来的！！-->
<html>
<head>
<title>.::::::::::举报信息列表::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
</head>
<!--<body>-->
<frameset rows="*" border=0>
<frame src="comment_Impeach_List.jsp?commentId=<%=commentId%>&comment=<%=comment%>"  border=0 scrolling="no" id="comimpeachFrame" name="docVerQueryFrame" ></frame>
</frameset>
<!--</body>-->
</html>