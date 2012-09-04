<%@page contentType="text/html; charset=UTF-8" %>

<HTML><HEAD></HEAD> 
<%
	String orgId = (String)session.getAttribute("orgId");
	String currUserId = (String)session.getAttribute("currUserId");
%>
<FRAMESET border=0 frameSpacing=1 cols=""   frameborder="1" rows="45%,55%">
<%
	String offset = (String)session.getAttribute("userListPageOffset");
%>
<%if (offset == null )
  {
%>
<FRAME id=userList name=userList src="userList.jsp?orgId=<%=orgId%>">
<%
  }
  else{
%>
<FRAME id=userList name=userList src="userList.jsp?orgId=<%=orgId%>&pager.offset=<%=offset%>&pager.desc=true">
<%
  }
%>
<%if (currUserId == null )
  {
%>
<FRAME id=userInfo name=userInfo src="userInfo.jsp" bordercolor='#7F7F7F' frameborder="0">
<%
  }
  else{
%>
<FRAME id=userInfo name=userInfo src="<%=request.getContextPath()%>/sysmanager/user/userManager.do?method=getUser&userId=<%=currUserId%>"; bordercolor='#7F7F7F' frameborder="0">
<%
  }
%>

</FRAMESET>
</HTML>
