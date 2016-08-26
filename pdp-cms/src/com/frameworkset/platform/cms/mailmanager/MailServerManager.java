package com.frameworkset.platform.cms.mailmanager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.ListInfo;

public class MailServerManager implements java.io.Serializable {

	/**
	 * 获取邮件服务器列表
	 * @param sql
	 * @param offset
	 * @param maxPagesize
	 * @return
	 * @throws Exception
	 */
	public ListInfo getMailServerList(String sql,int offset,int maxPagesize) throws Exception{
		DBUtil db = new DBUtil();
		ListInfo listInfo = new ListInfo(); 
		try{
			db.executeSelect(sql,offset,maxPagesize);
			List indexList = new ArrayList();
			for(int i=0;i<db.size();i++){
				MailServerInfo msi = new MailServerInfo();
				msi.setId(db.getString(i,"id"));
				msi.setName(db.getString(i,"SERVER_NAME"));
				msi.setSmtpServer(db.getString(i,"SMTP_SERVER"));
				msi.setMailDomain(db.getString(i,"MAIL_DOMAIN"));
				msi.setMailSender(db.getString(i,"MAIL_SENDER"));
				msi.setMailSenderName(db.getString(i,"MAIL_SENDERNAME"));
				msi.setDescription(db.getString(i,"mail_desc"));
				msi.setMail_userName(db.getString(i,"mail_userName"));
				msi.setMail_password(db.getString(i,"mail_password"));
				msi.setSmtp_Port(db.getString(i,"smtp_port"));
				indexList.add(msi);
			}
			listInfo.setDatas(indexList);
			listInfo.setTotalSize(db.getTotalSize());
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new Exception("获取邮件服务器列表时失败！" + e.getMessage());
		 }
		return listInfo;
	}
	
	/**
	 * 获取一个邮件服务器信息
	 * @param msiId
	 * @return
	 * @throws Exception
	 */
	public MailServerInfo getMailServerInfo(String msiId) throws Exception{
		DBUtil db = new DBUtil();
		MailServerInfo msi = new MailServerInfo();
		String sql = "select * from TD_CMS_MAILSERVERINFO where id=" + msiId;
		try{
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				msi.setId(db.getString(i,"id"));
				msi.setName(db.getString(i,"server_name"));
				msi.setSmtpServer(db.getString(i,"SMTP_SERVER"));
				msi.setMailDomain(db.getString(i,"MAIL_DOMAIN"));
				msi.setMailSender(db.getString(i,"MAIL_SENDER"));
				msi.setMailSenderName(db.getString(i,"MAIL_SENDERNAME"));
				msi.setDescription(db.getString(i,"mail_desc"));
				msi.setMail_userName(db.getString(i,"mail_userName"));
				msi.setMail_password(db.getString(i,"mail_password"));
				msi.setSmtp_Port(db.getString(i,"smtp_port"));
			}
		 }catch(SQLException e){
			 e.printStackTrace();
			 throw new Exception("获取邮件服务器信息时失败！" + e.getMessage());
		 }
		return msi;
	}
	
	/**
	 * 删除多个邮件服务器信息条目
	 * @param msis
	 * @throws Exception
	 */
	public void delete(String[] msis) throws Exception{
		DBUtil db = new DBUtil();
		String sql = "delete from TD_CMS_MAILSERVERINFO where id=";
		try{
			for(int i=0;i<msis.length;i++){
				db.executeDelete(sql + msis[i]);
			}
		}catch(SQLException e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 增加一条邮件服务器信息
	 * @param searchIndex
	 * @return
	 * @throws Exception
	 */
	public boolean add(MailServerInfo msi) throws Exception{	
		DBUtil db0 = new DBUtil();
		String sql0 = "select * from TD_CMS_MAILSERVERINFO where server_name ='" + msi.getName() + "'";
		try {
			db0.executeSelect(sql0);
			if(db0.size()>0)
			{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}		
		DBUtil db = new DBUtil();
		
		long mailId = db.getNextPrimaryKey("TD_CMS_MAILSERVERINFO") ;
		
		
		String sql = "insert into td_cms_mailserverinfo " +
				"(ID,SMTP_SERVER,MAIL_SENDER,MAIL_SENDERNAME,mail_desc,server_name,MAIL_DOMAIN,mail_userName,mail_password,smtp_Port) " +
				"values("+ mailId + ",'" + msi.getSmtpServer() + "','" + msi.getMailSender() + "','" + msi.getMailSenderName() + "'," +
				"'" + msi.getDescription() + "','" + msi.getName() + "','" + msi.getMailDomain() + "','" + msi.getMail_userName() + "','" + msi.getMail_password() + "','" + msi.getSmtp_Port() + "')";	
		try {
			db.executeInsert(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}			
		return true;
	}
	
	/**
	 * 修改一条邮件服务器信息
	 * @param msi
	 * @return
	 * @throws Exception
	 */
	public boolean updata(MailServerInfo msi) throws Exception{
		DBUtil db0 = new DBUtil();
		String sql0 = "select * from TD_CMS_MAILSERVERINFO where server_name ='" + msi.getName() + "' and id !=" + msi.getId();
		try {
			db0.executeSelect(sql0);
			if(db0.size()>0)
			{
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}		
		DBUtil db = new DBUtil();
		String sql = "update TD_CMS_MAILSERVERINFO set SMTP_SERVER='" + msi.getSmtpServer() + "'," +
				"MAIL_SENDER='" + msi.getMailSender() + "',MAIL_SENDERNAME='" + msi.getMailSenderName() + "'," +
				"mail_desc='" + msi.getDescription() + "',server_name='" + msi.getName() + "'," +
				"MAIL_DOMAIN='" + msi.getMailDomain() + "',mail_userName='" + msi.getMail_userName() + "',mail_password='" + msi.getMail_password() + "',smtp_Port='" + msi.getSmtp_Port() + "' where id =" + msi.getId();	
		try {
			db.executeInsert(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}			
		return true;
	}
	
}
