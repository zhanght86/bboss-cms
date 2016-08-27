<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.frameworkset.platform.search.SearchService"%>
<%
   String keywords=request.getParameter("keywords"); 
   
   String[] result = SearchService.search(keywords);
   StringBuffer returns = new StringBuffer();
   boolean flag = false;
   for(int i = 0; i < result.length; i ++)
   {
   		if(flag)
   			returns.append(";")
   		    .append(result[i]); 	
   		else
   		{
   			flag = true;
   			returns.append(result[i]);
   		}
   }
   out.print(keywords + "$~$" + returns.toString());
%>
