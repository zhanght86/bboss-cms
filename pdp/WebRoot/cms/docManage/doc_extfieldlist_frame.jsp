<%
/**
  * 扩展字段管理
  * 由于考虑打开得是模态窗口的形式，所以用了iframe
  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	//1：所有的；2：站点or频道
	String type = request.getParameter("type");
	//1：所有的；2：站点or频道
	String id = request.getParameter("id");
	
	String url = "";
	//all
	if(type == null)
	{
		url = "doc_extfieldofall_list.jsp?id=" + id;
	}//站点or频道
	else
	{
		//站点
		if("1".equals(type))
			url = "doc_extfieldofsiteorchl_list.jsp?type=" + type + "&id=" + id;
		//频道
		if("2".equals(type))
			url = "doc_extfieldofsiteorchl_list.jsp?type=" + type + "&id=" + id;
	}
%>
<html>
<head>
<title>.::::::::::扩展字段管理::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
</head>
<body>
<iframe src="<%=url%>" width=100% height=100%></frame>
</body>
</html>