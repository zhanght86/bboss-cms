<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>

<%
	String resTypeId2 = request.getParameter("resTypeId2");
	String resId2 = request.getParameter("resId2");
%>
<html>
<head>
<title>系统管理</title>
</head> 
<frameset name="frame1" rows="*,*" frameborder="no" border="0" framespacing="0" >
  <frame src="hasPower.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>" name="hasPower" id="hasPower"   />
  <frame src="leftPower.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>" name="leftPower" scrolling="No" noresize="noresize" id="leftPower" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>

