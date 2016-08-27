<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
<head>
<title>系统管理</title>

<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
 
</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
    <table >
    	
    	
        <tr><td>
            导航区
             <%
	String account = request.getParameter("user");
String pass = request.getParameter("password");
%>
    navigartor_tent
        当前用户信息：<%= account%>
        当前用户米啊：<%= pass%> 
         </td></tr>
    </table>
</div>
</body>
</html>

