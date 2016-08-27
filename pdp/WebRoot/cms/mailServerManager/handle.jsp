<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.mailmanager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	String action = request.getParameter("action");
	
	String successFlag = "1";	//"1"为添加成功,"0"为添加失败,"-1"为有重名
	String closeWin = "1";
	boolean samename = true;	//重名判断
	
	MailServerManager msm = new MailServerManager();
	
	if("delete".equals(action)){
		String[] msis = null;
		msis = request.getParameterValues("msiID");
		try{
			msm.delete(msis);
			closeWin = "0";
		}catch(Exception e){
			successFlag = "0";
		}
	}else if("add".equals(action)){
		String name = request.getParameter("name");
		String smtpServer = request.getParameter("smtpServer");
		String mailDomain = request.getParameter("mailDomain");
		String mailSenderName = request.getParameter("mailSenderName");
		String mailSender = request.getParameter("mailSender");
		String description = request.getParameter("description");
		String mail_userName = request.getParameter("mail_userName");
		String mail_password = request.getParameter("mail_password");
		String smtp_Port = request.getParameter("smtp_Port");
				
		MailServerInfo msi = new MailServerInfo();
		msi.setName(name);
		msi.setSmtpServer(smtpServer);
		msi.setMailDomain(mailDomain);
		msi.setMailSender(mailSender);
		msi.setMailSenderName(mailSenderName);
		msi.setDescription(description);
		msi.setMail_userName(mail_userName);
		msi.setMail_password(mail_password);
		msi.setSmtp_Port(smtp_Port);		
		try{
			samename = msm.add(msi); //重名判断,weida
			closeWin = "1";
		}catch(Exception e){
			successFlag = "0";
		}
		
	}else if("edit".equals(action)){		//更新
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String smtpServer = request.getParameter("smtpServer");
		String mailDomain = request.getParameter("mailDomain");
		String mailSenderName = request.getParameter("mailSenderName");
		String mailSender = request.getParameter("mailSender");
		String description = request.getParameter("description");
		String mail_userName = request.getParameter("mail_userName");
		String mail_password = request.getParameter("mail_password");
		String smtp_Port = request.getParameter("smtp_Port");
				
		MailServerInfo msi = new MailServerInfo();
		msi.setId(id);
		msi.setName(name);
		msi.setSmtpServer(smtpServer);
		msi.setMailDomain(mailDomain);
		msi.setMailSender(mailSender);
		msi.setMailSenderName(mailSenderName);
		msi.setDescription(description);
		msi.setMail_userName(mail_userName);
		msi.setMail_password(mail_password);
		msi.setSmtp_Port(smtp_Port);
		try{
			samename = msm.updata(msi);	//重名判断,weida
			closeWin = "1";
		}catch(Exception e){
			successFlag = "0";
		}
	}

	if(samename == false)
		successFlag = "-1";

	if("1".equals(successFlag)){
		%>
		<script language="JavaScript">
		//刷新页面
		try
		{		
				parent.win.alert('恭喜，操作成功！');
		        parent.closewin();
				var str = parent.document.location.href;
				var querystr = parent.document.all.queryString;
				if(querystr)
				{
					var strArray = str.slice(0,str.indexOf("?"));	
					parent.document.location.href = strArray+"?"+querystr.value;
				}
				else
				{
					parent.document.location.href = str;
				}				
		}
		catch(e)
		{
			alert('恭喜，操作成功！');
		}
		</script>
		<%
		if("1".equals(closeWin))
			out.println("<script language='javascript'>window.close();window.returnValue='cf';</script>");
		}

		if("0".equals(successFlag))
		{
			out.println("<script language='javascript'>alert('对不起，操作失败！');</script>");
		}

		if("-1".equals(successFlag))
		{
			out.println("<script language='javascript'>alert('名称有重复,请重填！');</script>");
		}
%>