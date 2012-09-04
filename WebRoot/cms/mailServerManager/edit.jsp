<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.mailmanager.*"%>
<%@ page import="com.frameworkset.platform.util.*" %>
<%@ page import="com.frameworkset.platform.cms.util.CMSUtil" %>
<%@ page import="java.util.List"%>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    
    MailServerManager msm = new MailServerManager();
    String msiId = request.getParameter("msiId");
    MailServerInfo msi = msm.getMailServerInfo(msiId);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>::邮件服务器信息修改:::</title>
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/include/pager.js" type="text/javascript"></script>
<script language="javascript">
	function save(){
		//检查合法性
		if(editForm.name.value.length<=0 || editForm.name.value.replace(/\s/g,"")==""){
			alert("请输入名称！");
			addForm.name.focus();
			return false;
		}
		if(editForm.smtpServer.value.length<=0 || editForm.smtpServer.value.replace(/\s/g,"")==""){
			alert("请输入邮件服务器！");
			addForm.smtpServer.focus();
			return false;
		}
		if(editForm.mailDomain.value.length<=0 || editForm.mailDomain.value.replace(/\s/g,"")==""){
			alert("请输入域名！");
			editForm.mailDomain.focus();
			return false;
		}
		
		var na = editForm.name.value;
		var sm = editForm.smtpServer.value;
		var ma = editForm.mailDomain.value;
		if(na.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("名称不能有\\/|:*?<>\"'!等特殊字符");
					return;
		}
		if(sm.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("邮件服务器不能有\\/|:*?<>\"'!等特殊字符");
					return;
		}
		if(ma.search(/[\\\/\|:\*\?<>"']/g)!=-1){
					alert("域名不能有\\/|:*?<>\"'!等特殊字符");
					return;
		}
		
		if(editForm.mailSender.value.length>0)
		{
			var mailss = editForm.mailSender.value;
			var patrn=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;     
	  		if (!patrn.exec(mailss))
	  		{   
	  			alert("发送人邮箱格式不正确!");   
	  			editForm.mailSender.focus();   
	  			return false;   
	  		}
		}
		
		editForm.action = "handle.jsp?action=edit";	
		editForm.target = "editFrame";
		editForm.submit();
	}
</script>
</head>

<base target="_self">
<body background="../../images/grayline_bg.jpg" scroll="no">
<form action="" method="post"  name="editForm" id="editForm">	
	<br>
	<table width="100%">
	   <tr>
	     <td>
			<table bgcolor=#0066CC cellspacing="1" cellpadding="0" width=100%>
			<input type="hidden" value="<%=msi.getId()%>" name="id">
			  <tr bgcolor="#FFFFFF">
				<td colspan="2" align="center" class="cms_report_tr">邮件服务器信息修改</td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">名称(必填)：</td>
				<td width="76%"><input name="name" type="text" value="<%=msi.getName()%>" size="48"></td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">邮件服务器(必填)：</td>
				<td width="76%"><input name="smtpServer" type="text" value="<%=msi.getSmtpServer()%>" size="48"></td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">邮件服务器端口：</td>
				<td width="76%"><input name="smtp_Port" type="text" value="<%=msi.getSmtp_Port()%>" size="48"></td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">域名(必填)：</td>
				<td width="76%"><input name="mailDomain" type="text" value="<%=msi.getMailDomain()%>" size="48">(多个域名用","分隔)</td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">发送人名字：</td>
				<td width="76%"><input name="mailSenderName" type="text" value="<%=msi.getMailSenderName()%>" size="48"></td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">发送人邮箱：</td>
				<td width="76%"><input name="mailSender" type="text" value="<%=msi.getMailSender()%>" size="48"></td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">邮箱帐号：</td>
				<td width="76%"><input name="mail_userName" type="text" value="<%=msi.getMail_userName()%>" size="48"></td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">邮箱密码：</td>
				<td width="76%"><input name="mail_password" type="password" value="<%=msi.getMail_password()%>" size="48"></td>
			  </tr>
			  <tr bgcolor="#FFFFFF">
				<td width="24%">注释：</td>
				<td width="76%"><input name="description" type="text" value="<%=msi.getDescription()%>" size="64"></td>
			  </tr>
		  </table>
		</td>
      </tr>
  	  <tr>
  	  	<td align="center">
			<input name="修改" type="button" class="cms_button" value="保存" onClick="save()" style="cursor:hand">
			&nbsp;
			<input name="返回" type="button" class="cms_button" value="返回" onClick="window.close()" style="cursor:hand">
		</td>
	  </tr>
	</table>
</form>
<iframe name="editFrame" width="0" height="0" style="display:none"></iframe>

</body>
</html>