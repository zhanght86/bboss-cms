<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@ page import="com.frameworkset.platform.cms.util.*" %>
  <%AccessControl control = AccessControl.getInstance();
    //页面保护
    control.checkAccess(request,response);//

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
    
    //测试ftp联接  
  try{
	String sip   = request.getParameter("ftpip");   
	String sport = request.getParameter("ftpport"); 
	int sporta=0;
	sporta=Integer.parseInt(sport);
	String suser = request.getParameter("ftpuser");   
	String spwd  = request.getParameter("ftppasswd");  
//	out.print(sip+";"+sport+";"+suser+";"+spwd);  

   
	FtpUpfile myFtp=new FtpUpfile(sip,sporta,suser,spwd);
     if (myFtp.testftp()) {%>
      <script>
      alert("恭喜: \n       \n 指定的FTP服务器测试连接成功!!");
        window.close();
      </script>
     <%
     
	 }else{ %>
	  <script>
      alert("抱歉: \n       \n指定的FTP服务器测试连接失败,\n请检查ftp设置!!");
      window.close();
      </script>
	 <%
     	 }
	 }
    catch(Exception ex){
    %>
      <script>
      alert("抱歉: \n       \n指定的FTP服务器测试连接失败,\n请检查ftp设置!!");
        window.close();
      </script>
	 <%
   } 
 
%>


