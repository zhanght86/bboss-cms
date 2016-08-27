<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.util.FtpUpfile" %>
  <%
    //测试ftp联接  
	
	String sip=request.getParameter("ip");   
	String sport=request.getParameter("port"); 
	int sporta=0;
	sporta=Integer.parseInt(sport);
	String suser=request.getParameter("user");   
	String spwd=request.getParameter("pwd");  
//	out.print(sip+";"+sport+";"+suser+";"+spwd);  

    try{
	FtpUpfile myFtp=new FtpUpfile(sip,sporta,suser,spwd);
     if (myFtp.testftp()) {
        out.print("测试连接成功!!");
	 }else{
       out.print("测试连接失败!!");
	 }
	 }
    catch(Exception ex){

	 out.print("出现异常!");
   } 
  //测试目录上传, 源文件放在d:\\ohh_cms\pub下,ftp服务器文件在:/ohh_cms/pub下

// FtpUpfile myFtp1=new FtpUpfile("127.0.0.1","administrator","yjxiong");
 
//   try{ 
//	    myFtp1.login();	    
	//上传文件:如果文件路径不对,则不上传.
//	myFtp1.upfilename("\\sitea\\lm1\\20060619002789.html");
	//上传目录,如目录不对,则不上传任何东西
//	    myFtp1.updir("\\sitea\\lm1");
//	    myFtp1.logout();
//	    out.println("success!");	  
//    }
//  catch(Exception ex){
//    out.println("exception");
//   }
%>


