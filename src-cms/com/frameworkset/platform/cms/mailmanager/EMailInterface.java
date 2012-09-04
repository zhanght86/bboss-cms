/**
 * 
 */
package com.frameworkset.platform.cms.mailmanager;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 
 */
public interface EMailInterface extends java.io.Serializable {
	
	/**
	 * 发送简单邮件，没有附件
	 * 
	 * @param to
	 *            目的邮箱
	 * @param subject
	 *            邮件主题
	 * @param msg
	 *            邮件信息
	 * @throws Exception
	 */
	public void sendSimpleEmail(String toEmail, String toName, String subject,
			String msg) throws Exception;

	/**
	 * 发送简单邮件，需传递“发送者”以及“发送者邮箱”这两个参数
	 * 
	 * @param toEmail
	 * @param toName
	 * @param subject
	 * @param msg
	 * @param senderEmail
	 */
	public void sendSimpleEmail(String toEmail, String toName, String subject,
			String msg, String senderEmail, String senderName) throws Exception;
	
	/**
	 * 发送简单邮件，需传递“发送者”以及“发送者邮箱”以及“邮箱服务器”这三个参数
	 * 
	 * @param toEmail
	 * @param toName
	 * @param subject
	 * @param msg
	 * @param senderEmail
	 * @param serverName
	 */
	public void sendSimpleEmail(String toEmail, String toName, String subject,
			String msg, String senderEmail, String senderName, String serverName) throws Exception;

	/**
	 * 简单邮件群发，toEmails中键值对为（收件人名，收件人邮箱）
	 * 
	 * @param toEmails
	 * @param subject
	 * @param msg
	 * @throws Exception
	 */
	public void sendSimpleEmails(Map toEmails, String subject, String msg)
			throws Exception;

	/**
	 * 发送带附件的邮件，附件为本地附件
	 * 
	 * @param to
	 *            目的邮箱
	 * @param subject
	 *            邮件主题
	 * @param msg
	 *            邮件信息
	 * @param attachments
	 *            附件（列表元素为附件地址字符串，如d:/mypictures/john.jpg）
	 * @throws Exception
	 */
	public void sendAttachEmail(String toEmail, String toName, String subject,
			String msg, List attachments) throws Exception;

	/**
	 * 带附件邮件群发，toEmails中键值对为（收件人名，收件人邮箱）
	 * 
	 * @param toEmails
	 * @param subject
	 * @param msg
	 * @param attachments
	 *            附件（列表元素为附件地址字符串，如d:/mypictures/john.jpg）
	 * @throws Exception
	 */
	public void sendAttachEmails(Map toEmails, String subject, String msg,
			List attachments) throws Exception;

	/**
	 * 发送带附件的邮件，附件从网上下载
	 * 
	 * @param to
	 *            目的邮箱
	 * @param subject
	 *            邮件主题
	 * @param msg
	 *            邮件信息
	 * @param attachments
	 *            附件（列表元素为附件网上地址字符串，如http://www.apache.org/images/asf_logo_wide.gif）
	 * @throws Exception
	 */
	public void sendWebAttachEmail(String toEmail, String toName,
			String subject, String msg, List attachments) throws Exception;

	/**
	 * 带附件邮件群发，附件从网上下载，toEmails中键值对为（收件人名，收件人邮箱）
	 * 
	 * @param toEmails
	 * @param subject
	 * @param msg
	 * @param attachments
	 *            附件（列表元素为附件网上地址字符串，如http://www.apache.org/images/asf_logo_wide.gif）
	 * @throws Exception
	 */
	public void sendWebAttachEmails(Map toEmails, String subject, String msg,
			List attachments) throws Exception;

	/**
	 * 发送网页邮件,邮件信息为html文档格式
	 * 
	 * @param to
	 *            目的邮箱
	 * @param subject
	 *            邮件主题
	 * @param htmlMsg
	 *            html格式信息
	 * @throws Exception
	 */
	public void sendHtmlEmail(String toEmail, String toName, String subject,
			String htmlMsg) throws Exception;

	/**
	 * 群发网页邮件，邮件信息为html文档格式
	 * 
	 * @param toEmails
	 * @param subject
	 * @param htmlMsg
	 * @throws Exception
	 */
	public void sendHtmlEmails(Map toEmails, String subject, String htmlMsg)
			throws Exception;

	/**
	 * 简单邮件群发，toEmails中键值对为（收件人名，收件人邮箱）
	 * 
	 * @param toEmails
	 * @param subject
	 * @param msg
	 * @throws Exception
	 */
	public void sendSimpleEmails(Map toEmails, String subject, String msg,String senderMail,String senderName, String userName, String password, String smtpPort)
			throws Exception;

	/**
	 * 发送带附件的邮件，附件为本地附件
	 * 
	 * @param to
	 *            目的邮箱
	 * @param subject
	 *            邮件主题
	 * @param msg
	 *            邮件信息
	 * @param attachments
	 *            附件（列表元素为附件地址字符串，如d:/mypictures/john.jpg）
	 * @throws Exception
	 */
	public void sendAttachEmail(String toEmail, String toName, String subject,
			String msg, List attachments,String senderMail,String senderName, String userName, String password, String smtpPort) throws Exception;

	/**
	 * 带附件邮件群发，toEmails中键值对为（收件人名，收件人邮箱）
	 * 
	 * @param toEmails
	 * @param subject
	 * @param msg
	 * @param attachments
	 *            附件（列表元素为附件地址字符串，如d:/mypictures/john.jpg）
	 * @throws Exception
	 */
	public void sendAttachEmails(Map toEmails, String subject, String msg,
			List attachments,String senderMail,String senderName, String userName, String password, String smtpPort) throws Exception;

	/**
	 * 发送带附件的邮件，附件从网上下载
	 * 
	 * @param to
	 *            目的邮箱
	 * @param subject
	 *            邮件主题
	 * @param msg
	 *            邮件信息
	 * @param attachments
	 *            附件（列表元素为附件网上地址字符串，如http://www.apache.org/images/asf_logo_wide.gif）
	 * @throws Exception
	 */
	public void sendWebAttachEmail(String toEmail, String toName,
			String subject, String msg, List attachments,String senderMail,String senderName, String userName, String password, String smtpPort) throws Exception;

	/**
	 * 带附件邮件群发，附件从网上下载，toEmails中键值对为（收件人名，收件人邮箱）
	 * 
	 * @param toEmails
	 * @param subject
	 * @param msg
	 * @param attachments
	 *            附件（列表元素为附件网上地址字符串，如http://www.apache.org/images/asf_logo_wide.gif）
	 * @throws Exception
	 */
	public void sendWebAttachEmails(Map toEmails, String subject, String msg,
			List attachments,String senderMail,String senderName, String userName, String password, String smtpPort) throws Exception;

	/**
	 * 发送网页邮件,邮件信息为html文档格式
	 * 
	 * @param to
	 *            目的邮箱
	 * @param subject
	 *            邮件主题
	 * @param htmlMsg
	 *            html格式信息
	 * @throws Exception
	 */
	public void sendHtmlEmail(String toEmail, String toName, String subject,
			String htmlMsg,String senderMail,String senderName, String userName, String password, String smtpPort) throws Exception;

	/**
	 * 群发网页邮件，邮件信息为html文档格式
	 * 
	 * @param toEmails
	 * @param subject
	 * @param htmlMsg
	 * @throws Exception
	 */
	public void sendHtmlEmails(Map toEmails, String subject, String htmlMsg,String senderMail,String senderName, String userName, String password, String smtpPort)
			throws Exception;
}
