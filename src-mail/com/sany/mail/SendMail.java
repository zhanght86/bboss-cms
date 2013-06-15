package com.sany.mail;


import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import   javax.mail.*;  
import   javax.mail.internet.*;  


import   java.util.*;  

public class SendMail {
	private MailConf mailConf;

	public boolean sendTextMail(MailInfo mailInfo) {
		Properties pro = null;
		if(mailInfo.getMailServerHost() == null || "".equals(mailInfo.getMailServerHost()))
			pro = mailConf.getMailProperties();
		else
			pro = mailInfo.getProperties();
		MyAuthenticator authenticator = null;
		boolean useconffromuser = mailInfo.getFromAddress() == null || mailInfo.getFromAddress().equals("");
		boolean validate =  useconffromuser?mailConf.isValidate():mailInfo.isValidate();

		if (validate) {   
			// 如果需要身份认证，则创建一个密码验证器
			if(useconffromuser)
			{
				authenticator = new MyAuthenticator(mailConf.getUserName(),mailConf.getPassword());
			}
			else
			{
				authenticator = new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
			}
		}   

		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
//		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
		Session sendMailSession = Session.getInstance(pro,authenticator);
		try {   
			//根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);   
			//创建邮件发送者地址
			Address from = null;
			if(useconffromuser)
				from = new InternetAddress(mailConf.getFromAddress());
			else
				from = new InternetAddress(mailInfo.getFromAddress());
			//设置邮件消息的发送者  
			mailMessage.setFrom(from);   
			//创建邮件的接收者地址，并设置到邮件消息中 
			String []toArray=mailInfo.getToAddress();   
			if(toArray.length==0)return false;   
			InternetAddress[] toAddrArray = new InternetAddress[toArray.length];     
			for (int i = 0; i < toArray.length; i++) {   
				toAddrArray[i] = new InternetAddress(toArray[i]);   
			}   
			mailMessage.setRecipients(Message.RecipientType.TO, toAddrArray);   
			//设置邮件消息的主题   
			mailMessage.setSubject(mailInfo.getSubject());   
			//设置邮件消息发送的时间  
			mailMessage.setSentDate(new Date());   
			//设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();   
			mailMessage.setText(mailContent);   
			//发送邮件
			Transport.send(mailMessage);   
			return true;   
		} catch (MessagingException ex) {   
			ex.printStackTrace();   
		}   
		return false;   
	}   

	/** 
	 * 以HTML格式发送邮件 
	 * @param mailInfo  待发送的邮件信息 
	 */  
	public boolean sendHtmlMail(MailInfo mailInfo) {   
		MyAuthenticator authenticator = null;   
		Properties pro = null;
		if(mailInfo.getMailServerHost() == null || "".equals(mailInfo.getMailServerHost()))
			pro = mailConf.getMailProperties();
		else
			pro = mailInfo.getProperties();
		boolean useconffromuser = mailInfo.getFromAddress() == null || mailInfo.getFromAddress().equals("");
		boolean validate =  useconffromuser?mailConf.isValidate():mailInfo.isValidate();

		if (validate) {   
			// 如果需要身份认证，则创建一个密码验证器
			if(useconffromuser)
			{
				authenticator = new MyAuthenticator(mailConf.getUserName(),mailConf.getPassword());
			}
			else
			{
				authenticator = new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
			}
		}    
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);   
		sendMailSession.setDebug(true);   
		try {   
			Message mailMessage = new MimeMessage(sendMailSession);   
			//创建邮件发送者地址
			Address from = null;
			if(useconffromuser)
				from = new InternetAddress(mailConf.getFromAddress());
			else
				from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);   
			//Address to = new InternetAddress(mailInfo.getToAddress());单用户
			//mailMessage.setRecipient(Message.RecipientType.TO, to);   
			String []toArray=mailInfo.getToAddress();   
			if(toArray.length==0)return false;   
			Address[] toAddrArray = new InternetAddress[toArray.length];     
			for (int i = 0; i < toArray.length; i++) {   
				toAddrArray[i] = new InternetAddress(toArray[i]);   
			}   
			mailMessage.setRecipients(Message.RecipientType.TO, toAddrArray);   
			mailMessage.setSubject(mailInfo.getSubject());   
			mailMessage.setSentDate(new Date());   
			//MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象  
			Multipart mainPart = new MimeMultipart();   
			//创建一个包含HTML内容的MimeBodyPart   
			BodyPart html = new MimeBodyPart();   
			//设置HTML内容 
			html.setContent(mailInfo.getContent(),"text/html; charset=utf-8");   
			mainPart.addBodyPart(html);   
			//将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);   
//			//发送邮件
//			mailMessage.saveChanges();   
//			Transport transport = sendMailSession.getTransport("smtp");   
//			transport.connect(mailInfo.getMailServerHost(),mailInfo.getUserName(),mailInfo.getPassword());   
//			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());   
//			transport.close(); 
			Transport.send(mailMessage);    
			return true;   
		} catch (MessagingException ex) {   
			ex.printStackTrace();   
		}   
		return false;   
	}   
	/**   
	 * 发送附件　
	 * @param mailInfo 待发送的邮件信息   
	 */  
	public boolean sendAttach(MailInfo mailInfo) {   
		MyAuthenticator authenticator = null;   
		Properties pro = null;
		if(mailInfo.getMailServerHost() == null || "".equals(mailInfo.getMailServerHost()))
			pro = mailConf.getMailProperties();
		else
			pro = mailInfo.getProperties();
		boolean useconffromuser = mailInfo.getFromAddress() == null || mailInfo.getFromAddress().equals("");
		boolean validate =  useconffromuser?mailConf.isValidate():mailInfo.isValidate();

		if (validate) {   
			// 如果需要身份认证，则创建一个密码验证器
			if(useconffromuser)
			{
				authenticator = new MyAuthenticator(mailConf.getUserName(),mailConf.getPassword());
			}
			else
			{
				authenticator = new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
			}
		}      
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);   
		sendMailSession.setDebug(true);   
		try {   
			Message mailMessage = new MimeMessage(sendMailSession);   
			//创建邮件发送者地址
			Address from = null;
			if(useconffromuser)
				from = new InternetAddress(mailConf.getFromAddress());
			else
				from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);   
			String []toArray=mailInfo.getToAddress();   
			if(toArray.length==0)return false;   
			Address[] toAddrArray = new InternetAddress[toArray.length];     
			for (int i = 0; i < toArray.length; i++) {   
				toAddrArray[i] = new InternetAddress(toArray[i]);   
			}   
			mailMessage.setRecipients(Message.RecipientType.TO, toAddrArray);   
			mailMessage.setSubject(mailInfo.getSubject());   
			mailMessage.setSentDate(new Date());   
			//MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();   
			//创建一个包含HTML内容的MimeBodyPart
			BodyPart mbp = new MimeBodyPart();   
			//设置HTML内容 
			mbp.setContent(mailInfo.getContent(),"text/html; charset=utf-8");   
			mainPart.addBodyPart(mbp);   
			String attachFiles[]=mailInfo.getAttachFileNames();   
			for(String f:attachFiles){   
				mbp=new MimeBodyPart();    
				FileDataSource fds=new FileDataSource(f); //得到数据源 
				mbp.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
				mbp.setFileName(fds.getName());  //得到文件名同样至入BodyPart       
				mainPart.addBodyPart(mbp);      
			}   
			// 将MiniMultipart对象设置为邮件内容  
			mailMessage.setContent(mainPart);   
			//发送邮件 
			//mailMessage.saveChanges();   
			//Transport transport = sendMailSession.getTransport("smtp");   
			//transport.connect(mailInfo.getMailServerHost(),mailInfo.getUserName(),mailInfo.getPassword());   
			//transport.sendMessage(mailMessage, mailMessage.getAllRecipients());   
			//transport.close();   
			Transport.send(mailMessage);   
			return true;   
		} catch (MessagingException ex) {   
			ex.printStackTrace();   
		}   
		return false;   
	}   
	
	
	public boolean sendTextMail(//邮件接收者的地址[支持多用户]　 
			 String [] toAddress,
			//邮件主题
			String subject,   
			// 邮件的文本内容　
			  String content  ) {
		Properties pro = null;
		pro = mailConf.getMailProperties();
		
		MyAuthenticator authenticator = null;
		boolean validate =  mailConf.isValidate();
		if (validate) {   
			authenticator = new MyAuthenticator(mailConf.getUserName(),mailConf.getPassword());
		}   

		// 根据邮件会话属性和密码验证器构造一个发送邮件的session
//		Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
		Session sendMailSession = Session.getInstance(pro,authenticator);
		try {   
			//根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);   
			//创建邮件发送者地址
			Address from = null;
			from = new InternetAddress(mailConf.getFromAddress());
			
			//设置邮件消息的发送者  
			mailMessage.setFrom(from);   
			//创建邮件的接收者地址，并设置到邮件消息中 
			if(toAddress.length==0)return false;   
			InternetAddress[] toAddrArray = new InternetAddress[toAddress.length];     
			for (int i = 0; i < toAddress.length; i++) {   
				toAddrArray[i] = new InternetAddress(toAddress[i]);   
			}   
			mailMessage.setRecipients(Message.RecipientType.TO, toAddrArray);   
			//设置邮件消息的主题   
			mailMessage.setSubject(subject);   
			//设置邮件消息发送的时间  
			mailMessage.setSentDate(new Date());   
			//设置邮件消息的主要内容
			String mailContent = content;   
			mailMessage.setText(mailContent);   
			//发送邮件
			Transport.send(mailMessage);   
			return true;   
		} catch (MessagingException ex) {   
			ex.printStackTrace();   
		}   
		return false;   
	}   

	/** 
	 * 以HTML格式发送邮件 
	 * @param mailInfo  待发送的邮件信息 
	 */  
	public boolean sendHtmlMail(//邮件接收者的地址[支持多用户]　 
			 String [] toAddress,
			//邮件主题
			String subject,   
			// 邮件的文本内容　
			  String content   
			) {   
		MyAuthenticator authenticator = null;   
		Properties pro = null;
		pro = mailConf.getMailProperties();
		
		boolean validate =  mailConf.isValidate();

		if (validate) {   
			// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailConf.getUserName(),mailConf.getPassword());
			
		}    
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);   
		sendMailSession.setDebug(true);   
		try {   
			Message mailMessage = new MimeMessage(sendMailSession);   
			//创建邮件发送者地址
			Address from = null;
			from = new InternetAddress(mailConf.getFromAddress());
			
			mailMessage.setFrom(from);   
			//Address to = new InternetAddress(mailInfo.getToAddress());单用户
			//mailMessage.setRecipient(Message.RecipientType.TO, to);   
			if(toAddress.length==0)return false;   
			Address[] toAddrArray = new InternetAddress[toAddress.length];     
			for (int i = 0; i < toAddress.length; i++) {   
				toAddrArray[i] = new InternetAddress(toAddress[i]);   
			}   
			mailMessage.setRecipients(Message.RecipientType.TO, toAddrArray);   
			mailMessage.setSubject(subject);   
			mailMessage.setSentDate(new Date());   
			//MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象  
			Multipart mainPart = new MimeMultipart();   
			//创建一个包含HTML内容的MimeBodyPart   
			BodyPart html = new MimeBodyPart();   
			//设置HTML内容 
			html.setContent(content,"text/html; charset=utf-8");   
			mainPart.addBodyPart(html);   
			//将MiniMultipart对象设置为邮件内容
			mailMessage.setContent(mainPart);   
//			//发送邮件
//			mailMessage.saveChanges();   
//			Transport transport = sendMailSession.getTransport("smtp");   
//			transport.connect(mailInfo.getMailServerHost(),mailInfo.getUserName(),mailInfo.getPassword());   
//			transport.sendMessage(mailMessage, mailMessage.getAllRecipients());   
//			transport.close();
			Transport.send(mailMessage);   
			return true;   
		} catch (MessagingException ex) {   
			ex.printStackTrace();   
		}   
		return false;   
	}   
	/**   
	 * 发送附件　
	 * @param mailInfo 待发送的邮件信息   
	 */  
	public boolean sendAttach(   //邮件接收者的地址[支持多用户]　 
						 String [] toAddress,
						//邮件主题
						String subject,   
						// 邮件的文本内容　
						  String content,   
						// 邮件附件的文件名[上传文件路径]　
						  String[] attachFileNames   ) {   
		MyAuthenticator authenticator = null;   
		Properties pro = null;
		pro = mailConf.getMailProperties();
		boolean validate = mailConf.isValidate();
		if (validate) {   
		// 如果需要身份认证，则创建一个密码验证器
			authenticator = new MyAuthenticator(mailConf.getUserName(),mailConf.getPassword());
		
			
		}      
		Session sendMailSession = Session.getDefaultInstance(pro, authenticator);   
		sendMailSession.setDebug(true);   
		try {   
			Message mailMessage = new MimeMessage(sendMailSession);   
			//创建邮件发送者地址
			Address from = null;
			
				from = new InternetAddress(mailConf.getFromAddress());
			
			mailMessage.setFrom(from);   
			
			if(toAddress.length==0)return false;   
			Address[] toAddrArray = new InternetAddress[toAddress.length];     
			for (int i = 0; i < toAddress.length; i++) {   
				toAddrArray[i] = new InternetAddress(toAddress[i]);   
			}   
			mailMessage.setRecipients(Message.RecipientType.TO, toAddrArray);   
			mailMessage.setSubject(subject);   
			mailMessage.setSentDate(new Date());   
			//MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();   
			//创建一个包含HTML内容的MimeBodyPart
			BodyPart mbp = new MimeBodyPart();   
			//设置HTML内容 
			mbp.setContent(content,"text/html; charset=utf-8");   
			mainPart.addBodyPart(mbp);   
			String attachFiles[]=attachFileNames;   
			for(String f:attachFiles){   
				mbp=new MimeBodyPart();    
				FileDataSource fds=new FileDataSource(f); //得到数据源 
				mbp.setDataHandler(new DataHandler(fds)); //得到附件本身并至入BodyPart  
				mbp.setFileName(fds.getName());  //得到文件名同样至入BodyPart       
				mainPart.addBodyPart(mbp);      
			}   
			// 将MiniMultipart对象设置为邮件内容  
			mailMessage.setContent(mainPart);   
			//发送邮件 
			//mailMessage.saveChanges();   
			//Transport transport = sendMailSession.getTransport("smtp");   
			//transport.connect(mailInfo.getMailServerHost(),mailInfo.getUserName(),mailInfo.getPassword());   
			//transport.sendMessage(mailMessage, mailMessage.getAllRecipients());   
			//transport.close();   
			Transport.send(mailMessage);   
			return true;   
		} catch (MessagingException ex) {   
			ex.printStackTrace();   
		}   
		return false;   
	}   
}