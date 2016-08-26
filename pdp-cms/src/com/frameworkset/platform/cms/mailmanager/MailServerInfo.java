package com.frameworkset.platform.cms.mailmanager;

public class MailServerInfo implements java.io.Serializable {
	private String id;
	private String name;
	private String smtpServer;
	private String mailSender;
	private String mailSenderName;
	private String description;
	private String mailDomain;
	private String mail_userName;
	private String mail_password;
	private String smtp_Port;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getMailSender() {
		return mailSender;
	}
	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}
	public String getMailSenderName() {
		return mailSenderName;
	}
	public void setMailSenderName(String mailSenderName) {
		this.mailSenderName = mailSenderName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getMailDomain() {
		return mailDomain;
	}
	public void setMailDomain(String mailDomain) {
		this.mailDomain = mailDomain;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMail_password() {
		return mail_password;
	}
	public void setMail_password(String mail_password) {
		this.mail_password = mail_password;
	}
	public String getMail_userName() {
		return mail_userName;
	}
	public void setMail_userName(String mail_userName) {
		this.mail_userName = mail_userName;
	}
	public String getSmtp_Port() {
		return smtp_Port;
	}
	public void setSmtp_Port(String smtp_Port) {
		this.smtp_Port = smtp_Port;
	}

}
