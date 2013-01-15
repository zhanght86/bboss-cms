package com.frameworkset.platform.cms.mailmanager;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;


import com.frameworkset.platform.config.ConfigManager;

import com.frameworkset.common.poolman.DBUtil;


import java.net.*;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class EMailImpl implements EMailInterface {
	/**
	 * 存放邮件发送服务机器信息的map对象,存放的值为
	 * Map<服务器域名,MailServerInfo>
	 */
	private static final Map serverinfos = new HashMap();
	static {
		DBUtil db = new DBUtil();
		String sql = "select * from TD_CMS_MAILSERVERINFO order by id desc";
		try {
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				MailServerInfo msi = new MailServerInfo();
				msi.setName(db.getString(i,"SERVER_NAME"));
				msi.setSmtpServer(db.getString(i,"SMTP_SERVER"));
				msi.setMailSender(db.getString(i,"MAIL_SENDER"));
				msi.setMailSenderName(db.getString(i,"MAIL_SENDERNAME"));
				msi.setDescription(db.getString(i,"mail_desc"));
				msi.setMailDomain(db.getString(i,"MAIL_DOMAIN"));
				msi.setMail_userName(db.getString(i,"mail_userName"));
				msi.setMail_password(db.getString(i,"mail_password"));
				String[] domains = msi.getMailDomain().split(",");
				for(int j = 0; j < domains.length; j ++)
				{
					serverinfos.put(domains[j],msi);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getMailDomain(String senderMail)
	{
		int idx = senderMail.indexOf("@");
		if(idx != -1 && idx != senderMail.length() - 1)
		{
			String domain = senderMail.substring(idx + 1);
			return domain;
		}
		return null;
		
	}
	
	private void setEmail(Email email, String toEmail, String toName,
			String subject, String msg, String sendEmail, String sendName)
			throws Exception {
				ConfigManager cm = ConfigManager.getInstance();
				String hostName = cm.getConfigValue("cms.smtpServer","mail.sany.com");
				String sender = sendEmail == null ? cm.getConfigValue("cms.mail_sender") : sendEmail;
				String senderName = sendName == null ? cm.getConfigValue("cms.mail_senderName", "sany") : sendName;
				email.setHostName(hostName);
			    email.addTo(toEmail, toName);
			    email.setFrom(sender, senderName);
				email.setSubject(subject);
				email.setContent(msg,"text/plain; charset=utf-8");
				//email.setMsg(msg);
			}

	private void setEmail(Email email, Map toEmails, String subject, String msg)
			throws Exception {
		// 配置文件中获取邮件服务器
		ConfigManager cm = ConfigManager.getInstance();
		String hostName = cm.getConfigValue("cms.smtpServer","mail.sany.com");
		String sender = cm.getConfigValue("cms.mail_sender");
		String senderName = cm.getConfigValue("cms.mail_senderName", "sany");
		email.setHostName(hostName);
		Iterator it = toEmails.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			email.addTo((String) toEmails.get(key), key);
		}
		email.setFrom(sender, senderName);
		email.setSubject(subject);
		email.setContent(msg,"text/plain; charset=utf-8");
		//email.setMsg(msg);
	}
	
	public void sendSimpleEmail(String toEmail, String toName, String subject,
			String msg, String senderEmail, String senderName, String serverName)
			throws Exception {
		SimpleEmail email = new SimpleEmail();
		ConfigManager cm = ConfigManager.getInstance();
		String hostName = serverName==null ? cm
				.getConfigValue("cms.smtpServer","mail.sany.com"):serverName; 
		String senderE = senderEmail == null ? cm
				.getConfigValue("cms.mail_sender") : senderEmail;
		String senderN = senderName == null ? cm.getConfigValue(
				"cms.mail_senderName", "sany") : senderName;
		email.setHostName(hostName);
		email.addTo(toEmail, toName);
		email.setFrom(senderE, senderN);
		email.setSubject(subject);
		email.setContent(msg,"text/plain; charset=utf-8");
		email.send();
		// TODO Auto-generated method stub
		
	}

	public void sendSimpleEmail(String toEmail, String toName, String subject,
			String msg) throws Exception {
		try {
			SimpleEmail email = new SimpleEmail();
			this.setEmail(email, toEmail, toName, subject, msg, null, null);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendSimpleEmail(String toEmail, String toName, String subject,
			String msg, String senderEmail, String senderName) throws Exception {
		try {
			SimpleEmail email = new SimpleEmail();
			this.setEmail(email, toEmail, toName, subject, msg, senderEmail,
					senderName);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendSimpleEmails(Map toEmails, String subject, String msg)
			throws Exception {
		try {
			SimpleEmail email = new SimpleEmail();
			this.setEmail(email, toEmails, subject, msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendAttachEmail(String toEmail, String toName, String subject,
			String msg, List attachments) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();
			this.setEmail(email, toEmail, toName, subject, msg, null, null);

			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachPath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}

			email.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public void sendAttachEmails(Map toEmails, String subject, String msg,
			List attachments) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();
			this.setEmail(email, toEmails, subject, msg);

			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachPath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}

			email.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public void sendWebAttachEmail(String toEmail, String toName,
			String subject, String msg, List attachments) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();
			this.setEmail(email, toEmail, toName, subject, msg, null, null);

			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setURL(new URL(attachPath));
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}

			email.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public void sendWebAttachEmails(Map toEmails, String subject, String msg,
			List attachments) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();
			this.setEmail(email, toEmails, subject, msg);

			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setURL(new URL(attachPath));
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}

			email.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public void sendHtmlEmail(String toEmail, String toName, String subject,
			String htmlMsg) throws Exception {
		try {
			HtmlEmail email = new HtmlEmail();
			this.setEmail(email, toEmail, toName, subject, "HtmlEmail", null,
					null);
			email.setHtmlMsg(htmlMsg);
			email
					.setTextMsg("Your email client does not support HTML messages");
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public void sendHtmlEmails(Map toEmails, String subject, String htmlMsg)
			throws Exception {
		try {
			HtmlEmail email = new HtmlEmail();
			this.setEmail(email, toEmails, subject, "HtmlEmail");
			email.setHtmlMsg(htmlMsg);
			email
					.setTextMsg("Your email client does not support HTML messages");
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public static void main(String[] args) throws Exception {		

//		EMailImpl eMailImpl = new EMailImpl();
//		String toEmail = "tonyformu@sina.com";
//		String toName = "tony";
//		String subject = "新邮件";
//		String msg = "我爱世界波";
//		String serverName = "mail.sany.com";
//		String senderEmail = "tonyformu@163.com";
//		String senderName = "da.wei";
//		String userName = "da.wei";
//		String password = "da.wei";
//		String htmlMsg = "<html>The apache logo - <img src=\"G:/Pics/Beauties/20070617143050_7_2.jpg\"></html>";
//		List attachments = new ArrayList();
//		attachments.add("G:/Pics/Beauties/20070617143050_7_2.jpg");
//		attachments.add("G:/Pics/Beauties/200773114401747077_2_Center.jpg");
//		List webAttachments = new ArrayList();
//		webAttachments.add("http://www.apache.org/images/asf_logo_wide.gif");
//		String smtpPort = "25";
//
//		Map toEmails = new HashMap();
//		toEmails.put("tony", "tonyformu@sina.com");
//		toEmails.put("beckham", "da.wei@sany.com");
		try {
//			eMailImpl.sendSimpleEmail(toEmail,toName,subject,msg);
//			eMailImpl.sendSimpleEmail(toEmail, toName, subject, msg, senderEmail, senderName, serverName);
//			eMailImpl.sendAttachEmail(toEmail,toName,subject,msg,attachments);
//			eMailImpl.sendWebAttachEmail(toEmail,toName,subject,msg,webAttachments);
//			eMailImpl.sendHtmlEmail(toEmail,toName,subject,htmlMsg);
//			eMailImpl.sendSimpleEmails(toEmails, subject, msg);
//			eMailImpl.sendAttachEmails(toEmails, subject, msg, attachments);

//			eMailImpl.sendSimpleEmails(toEmails, subject, msg, senderEmail, senderName, userName, password, smtpPort);//发送成功
//			eMailImpl.sendAttachEmail(toEmail, toName, subject, msg, attachments, senderEmail, senderName, userName, password);//发送成功,能收到附件
//			eMailImpl.sendAttachEmails(toEmails, subject, msg, attachments, senderEmail, senderName, userName, password);//发送成功,能收到附件
//			eMailImpl.sendHtmlEmail(toEmail, toName, subject, htmlMsg, senderEmail, senderName, userName, password);//发送成功,能看到html内容
//			eMailImpl.sendHtmlEmails(toEmails, subject, htmlMsg, senderEmail, senderName, userName, password);//发送成功,能看到html内容
//			eMailImpl.sendWebAttachEmail(toEmail, toName, subject, msg, webAttachments, senderEmail, senderName, userName, password);//解析附件失败,发送不成功,但是在服务器上外网发送成功,代理上网的原因?
//			eMailImpl.sendWebAttachEmails(toEmails, subject, msg, webAttachments, senderEmail, senderName, userName, password);//解析附件失败,发送不成功,但是在服务器上外网发送成功,代理上网的原因?
			
			
			//test code			
			SimpleEmail email = new SimpleEmail();
			email.setHostName("sany.com");
			email.setSmtpPort(25);
			email.setAuthentication("da.wei", "da.wei");
			email.addTo("da.wei@sany.com", "da.wei");
			email.setFrom("da.wei@sany.com", "da.wei");
			email.setSubject("Test message");
			email.setMsg("This is a simple test of commons-email");
			email.setDebug(true);
			email.send();
			
			
			
//			String host = "smtp.sina.com"; 
//	        String from =  "tonyformu@sina.com"; 
//	        String to = "tonyformu@163.com"; 
//	        String username = "tonyformu"; 
//	        String password = "629048"; 
//	        // Get system properties 
//	        // Properties props = System.getProperties(); 很多例子中是这样的，其实下面这句更好，可以用在applet中 
//	        Properties props = new Properties(); 
//			props.setProperty("proxySet", "true");
//			props.setProperty("http.proxyHost", "172.16.168.234");
//			props.setProperty("http.proxyPort", "808");	
//	        // Setup mail server 
//	        props.put("mail.smtp.host", host); 
//	        props.put("mail.smtp.auth", "true"); //这样才能通过验证 
//	        // Get session 
//	        Session session = Session.getDefaultInstance(props); 
//	        // watch the mail commands go by to the mail server 
//	        session.setDebug(true); 
//	        // Define message 
//	        MimeMessage message = new MimeMessage(session); 
//	        message.setFrom(new InternetAddress(from)); 
//	        message.addRecipient(Message.RecipientType.TO, 
//	          new InternetAddress(to)); 
//	        message.setSubject("Hello JavaMail"); 
//	        message.setText("Welcome to JavaMail"); 
//	        // Send message 
//	        message.saveChanges(); 
//	        Transport transport = session.getTransport("smtp"); 
//	        transport.connect(host, username, password); 
//	        transport.sendMessage(message, message.getAllRecipients()); 
//	        transport.close(); 


			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	/**
	 * 以下全部由da.wei添加!200708231532
	 */

	public void sendAttachEmail(String toEmail, String toName, String subject, String msg, List attachments, String senderMail, String senderName, String mail_userName, String mail_password, String smtpPort) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();	
			MailServerInfo msi = (MailServerInfo)serverinfos.get(this.getMailDomain(senderMail));
			String hostName = msi.getSmtpServer();
			String sender_Mail = senderMail == null ? msi.getMailSender() : senderMail;
			String sender_Name = senderName == null ? msi.getMailSenderName() : senderName;		
			String mailUserName = mail_userName == null ? msi.getMail_userName() : mail_userName;
			String mailPassword = mail_password == null ? msi.getMail_password() : mail_password;
			email.setAuthentication(mailUserName, mailPassword);
			email.setHostName(hostName);
			email.setSmtpPort(Integer.parseInt(smtpPort));
		    email.addTo(toEmail, toName);
		    email.setFrom(sender_Mail, sender_Name);
			email.setSubject(subject);
//			email.setContent(msg,"text/plain; charset=utf-8");
			email.setMsg(msg);
			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachPath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}
			email.setDebug(true);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void sendSimpleEmails(Map toEmails, String subject, String msg, String senderMail, String senderName, String mail_userName, String mail_password, String smtpPort) throws Exception {
		try {
			SimpleEmail email = new SimpleEmail();
			String domain = this.getMailDomain(senderMail);
			MailServerInfo msi = (MailServerInfo)serverinfos.get(domain);
			String hostName = msi.getSmtpServer();
			email.setSmtpPort(Integer.parseInt(smtpPort));
			String sender_Mail = senderMail == null ? msi.getMailSender() : senderMail;
			String sender_Name = senderName == null ? msi.getMailSenderName() : senderName;
			String mailUserName = mail_userName == null ? msi.getMail_userName() : mail_userName;
			String mailPassword = mail_password == null ? msi.getMail_password() : mail_password;
			
			email.setAuthentication(mailUserName, mailPassword);
			email.setHostName(hostName);
			
//			email.setSmtpPort(465);
			Iterator it = toEmails.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				email.addTo((String) toEmails.get(key), key);
			}
			email.setFrom(sender_Mail, sender_Name);
			email.setSubject(subject);
			email.setContent(msg,"text/plain; charset=utf-8");
//			email.setMsg(msg);			
			email.setDebug(true);
			email.send();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendAttachEmails(Map toEmails, String subject, String msg, List attachments, String senderMail, String senderName, String mail_userName, String mail_password, String smtpPort) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();			
			MailServerInfo msi = (MailServerInfo)serverinfos.get(this.getMailDomain(senderMail));
			String hostName = msi.getSmtpServer();
			email.setSmtpPort(Integer.parseInt(smtpPort));
			String sender_Mail = senderMail == null ? msi.getMailSender() : senderMail;
			String sender_Name = senderName == null ? msi.getMailSenderName() : senderName;		
			String mailUserName = mail_userName == null ? msi.getMail_userName() : mail_userName;
			String mailPassword = mail_password == null ? msi.getMail_password() : mail_password;
			email.setAuthentication(mailUserName, mailPassword);
			email.setHostName(hostName);
			Iterator it = toEmails.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				email.addTo((String) toEmails.get(key), key);
			}
			email.setFrom(sender_Mail, sender_Name);
			email.setSubject(subject);
//			email.setContent(msg,"text/plain; charset=utf-8");
			email.setMsg(msg);
			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setPath(attachPath);
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}
			email.setDebug(true);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendWebAttachEmail(String toEmail, String toName, String subject, String msg, List attachments, String senderMail, String senderName, String mail_userName, String mail_password, String smtpPort) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();			
			MailServerInfo msi = (MailServerInfo)serverinfos.get(this.getMailDomain(senderMail));
			String hostName = msi.getSmtpServer();
			String sender_Mail = senderMail == null ? msi.getMailSender() : senderMail;
			String sender_Name = senderName == null ? msi.getMailSenderName() : senderName;	
			String mailUserName = mail_userName == null ? msi.getMail_userName() : mail_userName;
			String mailPassword = mail_password == null ? msi.getMail_password() : mail_password;
			email.setAuthentication(mailUserName, mailPassword);
			email.setHostName(hostName);
			email.setSmtpPort(Integer.parseInt(smtpPort));
		    email.addTo(toEmail, toName);
		    email.setFrom(sender_Mail, sender_Name);
			email.setSubject(subject);
//			email.setContent(msg,"text/plain; charset=utf-8");
			email.setMsg(msg);
			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setURL(new URL(attachPath));
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}
			email.setDebug(true);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendWebAttachEmails(Map toEmails, String subject, String msg, List attachments, String senderMail, String senderName, String mail_userName, String mail_password, String smtpPort) throws Exception {
		try {
			MultiPartEmail email = new MultiPartEmail();
			MailServerInfo msi = (MailServerInfo)serverinfos.get(this.getMailDomain(senderMail));
			String hostName = msi.getSmtpServer();
			String sender_Mail = senderMail == null ? msi.getMailSender() : senderMail;
			String sender_Name = senderName == null ? msi.getMailSenderName() : senderName;	
			String mailUserName = mail_userName == null ? msi.getMail_userName() : mail_userName;
			String mailPassword = mail_password == null ? msi.getMail_password() : mail_password;
			email.setAuthentication(mailUserName, mailPassword);
			email.setHostName(hostName);
			email.setSmtpPort(Integer.parseInt(smtpPort));
			Iterator it = toEmails.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				email.addTo((String) toEmails.get(key), key);
			}
			email.setFrom(sender_Mail, sender_Name);
			email.setSubject(subject);
//			email.setContent(msg,"text/plain; charset=utf-8");
			email.setMsg(msg);
			for (int i = 0; i < attachments.size(); i++) {
				String attachPath = (String) attachments.get(i);
				String attachName = attachPath.substring(attachPath
						.lastIndexOf("/"), attachPath.length());
				EmailAttachment attachment = new EmailAttachment();
				attachment.setURL(new URL(attachPath));
				attachment.setDisposition(EmailAttachment.ATTACHMENT);
				attachment.setDescription(attachName);
				attachment.setName(attachName);
				email.attach(attachment);
			}
			email.setDebug(true);
			email.send();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendHtmlEmail(String toEmail, String toName, String subject, String htmlMsg, String senderMail, String senderName, String mail_userName, String mail_password, String smtpPort) throws Exception {
		try {
			HtmlEmail email = new HtmlEmail();			
			MailServerInfo msi = (MailServerInfo)serverinfos.get(this.getMailDomain(senderMail));
			String hostName = msi.getSmtpServer();
			String sender_Mail = senderMail == null ? msi.getMailSender() : senderMail;
			String sender_Name = senderName == null ? msi.getMailSenderName() : senderName;	
			String mailUserName = mail_userName == null ? msi.getMail_userName() : mail_userName;
			String mailPassword = mail_password == null ? msi.getMail_password() : mail_password;
			email.setAuthentication(mailUserName, mailPassword);
			email.setHostName(hostName);
			email.setSmtpPort(Integer.parseInt(smtpPort));
		    email.addTo(toEmail, toName);
		    email.setFrom(sender_Mail, sender_Name);
			email.setSubject(subject);
//			email.setContent("HtmlEmail", "text/plain; charset=utf-8");			
			email.setHtmlMsg(htmlMsg);
			email.setTextMsg("Your email client does not support HTML messages");
			email.setDebug(true);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void sendHtmlEmails(Map toEmails, String subject, String htmlMsg, String senderMail, String senderName, String mail_userName, String mail_password, String smtpPort) throws Exception {
		try {
			HtmlEmail email = new HtmlEmail();			
			this.setEmail(email, toEmails, subject, "HtmlEmail");
			MailServerInfo msi = (MailServerInfo)serverinfos.get(this.getMailDomain(senderMail));
			String hostName = msi.getSmtpServer();
			String sender_Mail = senderMail == null ? msi.getMailSender() : senderMail;
			String sender_Name = senderName == null ? msi.getMailSenderName() : senderName;	
			String mailUserName = mail_userName == null ? msi.getMail_userName() : mail_userName;
			String mailPassword = mail_password == null ? msi.getMail_password() : mail_password;
			email.setAuthentication(mailUserName, mailPassword);
			email.setHostName(hostName);
			email.setSmtpPort(Integer.parseInt(smtpPort));
			email.setDebug(true);
			Iterator it = toEmails.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				email.addTo((String) toEmails.get(key), key);
			}
			email.setFrom(sender_Mail, sender_Name);
			email.setSubject(subject);
//			email.setContent("HtmlEmail", "text/plain; charset=utf-8");			
			email.setHtmlMsg(htmlMsg);
			email.setTextMsg("Your email client does not support HTML messages");
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
