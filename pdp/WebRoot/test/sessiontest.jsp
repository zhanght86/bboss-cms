<%@ page contentType="text/html; charset=UTF-8"%>
<%
String value = (String)session.getAttribute("$a.b.c");
if(value == null)
{
	session.setAttribute("$a.b.c", "a");
}
value = (String)session.getAttribute("$a.b.c");
out.println("before remove $a.b.c:"+value);
out.println("<br>");
session.removeAttribute("$a.b.c");
value = (String)session.getAttribute("$a.b.c");
out.println("after remove $a.b.c:"+value);
out.println("<br>");
session.setAttribute("local", java.util.Locale.CHINESE);
out.println("getServletContext:"+session.getServletContext());
out.println("<br>");
out.println("local:"+session.getAttribute("local"));
out.println("<br>");
out.println("request.getSessionID:"+request.getRequestedSessionId());
out.println("<br>");
out.println("request.isRequestedSessionIdFromCookie:"+request.isRequestedSessionIdFromCookie());
out.println("<br>");
out.println("request.isRequestedSessionIdFromURL:"+request.isRequestedSessionIdFromURL());
out.println("<br>");
out.println("request.isRequestedSessionIdFromUrl:"+request.isRequestedSessionIdFromUrl());
out.println("<br>");
out.println("request.isRequestedSessionIdValid:"+request.isRequestedSessionIdValid());
 %>