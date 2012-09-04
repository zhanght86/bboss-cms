<%@page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/news-turnpage.tld" prefix="news-turnpage"%>
<!--
    功能说明：新闻页面内部翻页标签
    @param String totalpage:新闻总页数
--> 
<%
    response.setHeader("Pragma","No-cache"); 
    response.setHeader("Cache-Control","no-cache"); 
    response.setDateHeader("Expires", 0);  
%>
<html>
    <head>
        <title></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>    
    <body>        
        <news-turnpage:newsTurnpage totalpage="11" />
    </body>
</html>
