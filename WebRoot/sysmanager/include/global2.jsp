<%response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);
%>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="org.apache.struts.action.*,java.util.Iterator" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>

