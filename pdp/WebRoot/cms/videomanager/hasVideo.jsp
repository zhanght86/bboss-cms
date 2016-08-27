<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.*"%>
<%@ page import="java.io.*"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<html>
<body>
<%    
    String path = request.getParameter("path");
    String key = request.getParameter("key");
    String channel = request.getParameter("channel");
    String site_id = request.getParameter("site_id");
    ChannelManagerImpl channelImpl = new ChannelManagerImpl();
    DocumentManagerImpl docImpl = new DocumentManagerImpl();
    Channel chl = channelImpl.getChannelInfoByDisplayName(site_id,channel);    
    String filePath = path +"/"+ key + ".html";    
    boolean flag = true;
    flag = docImpl.isExistDocumentInChannel(chl.getChannelId(),key); 
    if(flag){
    %>
    <SCRIPT LANGUAGE="JavaScript">
    <!--
        var path = "<%=filePath%>";                     
        window.open(path);
        window.close();
    -->
    </SCRIPT>
    <%
    }else{
    %>
    <SCRIPT LANGUAGE="JavaScript">
        document.write("<table align='center'><tr><td align='center'>");
        document.write("没有采集该天的新闻联播!");
        document.write("</td></tr>");
        document.write("<tr><td align='center'>");
        document.write("<input type='button' value='关闭窗口' onclick='window.close()'>");
        document.write("</td></tr></table>");
    </SCRIPT>
    <%
    }
%>
  </body>
</html>
