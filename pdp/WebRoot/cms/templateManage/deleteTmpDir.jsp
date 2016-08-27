<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.util.zip.*,com.frameworkset.platform.cms.util.FileUtil"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.container.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.util.*"%>
<%
    String tmpPath = request.getParameter("tmpPath");
    tmpPath=tmpPath.replaceAll("\\\\","\\\\\\\\");
    tmpPath = tmpPath==null?"":tmpPath;
    FileUtil.deleteFile(tmpPath);
    System.out.println("本地上传的临时目录:"+tmpPath+"成功清除!");
%>
<html>
    <body>
    </body>
    <script>
        //alert("删除成功!");
        window.returnValue="ok";			
		window.close();
    </script>
</html>