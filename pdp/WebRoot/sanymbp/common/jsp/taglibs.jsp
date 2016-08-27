<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>

<%@ taglib uri="/WEB-INF/commontag.tld" prefix="cm"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<% //全部走https通道
String ctx = request.getContextPath();
request.setAttribute("ctx",ctx); 
String scheme=request.getScheme(); 
String url=request.getRequestURI(); 
String hostname = request.getLocalName();
//if(!"HTTPS".equalsIgnoreCase(scheme)) 
//{ 
//response.sendRedirect("https://"+hostname+url); 
//return ; 
//}
%>