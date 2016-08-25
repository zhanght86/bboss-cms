package com.frameworkset.platform.mail.controllers;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.frameworkset.platform.smtp.Constants;
import com.frameworkset.platform.smtp.mail.CustomDataSource;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;


public class Mail implements java.io.Serializable {
	public static void main(String[] args) {
		String[] str = new String[] { "xingguo.yang","zhuo.wang" };
		Mail.sendMail("xingguo.yang",str, "xxxxxxx", "x21xx2");
	}

	public static Session getSession(String serverName) {
		Properties props = new Properties();
		props.put("mail.smtp.host", serverName);
		return Session.getDefaultInstance(props, null);
	}

	public static Session getSession(String serverName, final String username,
			final String password) {
		Properties props = new Properties();
		props.put("mail.smtp.host", serverName);
		props.put("mail.smtp.auth", "true");


		//Authenticator auth=new MyAuthenticator("username","password");
		Authenticator auth = new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};
		return Session.getInstance(props, auth);
	}

	public static boolean sendMail(String[] users, String subject, String content) {
		return sendMail(users, subject, content,Constants.auth);
	}
        public static boolean sendMail(String fromuser,String[] users, String subject, String content) {
                return sendMail(fromuser,users, subject, content,Constants.auth);
            }
        public static boolean sendMail(String fromuser,String[] users, String subject, String content, String filepath) {
                return sendMail(fromuser,users, subject, content,Constants.auth,filepath);
        }

        public static boolean sendMail(String fromuser,String[] users, String subject, String content, boolean flag, String filepath) {
                boolean b=true;
                Session session;
                String userpass="";
                String filename = "";
                FileInputStream os = null;

                try{
                    //modify by xubo 2006-4-5
                    //userpass=BasicDAO.getSelectValueBycolvalue("td_ac_userorg","attr1","title",fromuser);
                    //userpass=BasicDAO.getSelectValueBycolvalue("td_sm_user","user_password","user_name",fromuser);
                    UserManager userManager = SecurityDatabase.getUserManager();
                    User user = userManager.getUserByName(fromuser);
                    userpass = user.getUserPassword();
                }
                catch(Exception e){

                }
                System.out.println("======"+Constants.smtpServer+"==");
                System.out.println("======"+Constants.oa_mail+"==");
                System.out.println("======"+Constants.oa_mail_password+"==");
                if (flag) {
                    session = getSession(Constants.smtpServer,fromuser+Constants.damin,userpass);
                }
                else {
                    session = getSession(Constants.smtpServer);
                }
                if (users.length > 0) {
                    Message mimeMsg = new MimeMessage(session);
                    InternetAddress[] to = new InternetAddress[users.length];
                    if((filepath!=null)&&(filepath.length()>0)){
                        try{
                            filename = filepath.substring(filepath.lastIndexOf("\\"), filepath.length());
                            System.out.println("filename**************************"+filename);
                            //filename = "E:\\yhoa\\oa\\upload\\daily\\20060329021097会议经费.esf";
                            System.out.print("filepath:::::::::::::::::"+filepath);
                        //    filepath="F:\\动态FORM.txt";
                            java.io.File a=new java.io.File(filepath);
                            long fileLength=a.length();
                            byte[] bt=new byte[(int)fileLength];
                            os=new FileInputStream(a);
                            int i=os.read(bt);


                            Multipart multipart = new MimeMultipart();
                                // 设置邮件内容
                                BodyPart mbp = new MimeBodyPart();
                                mbp.setContent(content, "text/plain;charset=GBK");
                                multipart.addBodyPart(mbp);

                            CustomDataSource filedata=new CustomDataSource(bt,"application/msdownload",filename);
                            BodyPart mbpAttachment = new MimeBodyPart();
                                        mbpAttachment.setDataHandler(new DataHandler(
                                                        filedata));
                                        mbpAttachment.setFileName(filedata.getName());
                                        multipart.addBodyPart(mbpAttachment);

                                         mimeMsg.setContent(multipart);

                       }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < users.length; i++) {
                        try {
                            InternetAddress tem = new InternetAddress(users[i]+Constants.damin);
                            to[i] = tem;
                        }
                        catch (Exception e) {
                            System.out.println(users[i] + "is not a avild email address");
                        }
                    }
                    try {
                        mimeMsg.setFrom(new InternetAddress(fromuser+Constants.damin));
                        mimeMsg.setRecipients(Message.RecipientType.TO, to);
                        mimeMsg.setSubject(subject);
                        mimeMsg.addHeader("Disposition-Notification-To","0");
                        mimeMsg.setSentDate(new Date());
                       


                        System.out.println("邮件附件内容：＝＝＝"+filepath);
                        mimeMsg.saveChanges();
                        Transport.send(mimeMsg);
                    }
                    catch (Exception e) {
                        try{
                            os.close();
                        }
                        catch(Exception ee){
                            ee.printStackTrace();
                        }
                        e.printStackTrace();
                        System.out.println("send false");
                        b=false;
                    }
            }
            return b;
        }


        public static boolean sendMail(String fromuser,String[] users, String subject, String content,
                                boolean flag) {
                        boolean b=true;
                        Session session;
                        String userpass="";
                        try{
                                                        
                        	UserManager userManager = SecurityDatabase.getUserManager();
                            User user = userManager.getUserByName(fromuser);
                            userpass = user.getUserPassword();
//                            System.out.println("..........."+userpass);
                        }catch(Exception e){

                        }
                        System.out.println("======"+Constants.smtpServer+"==");
                        System.out.println("======"+Constants.oa_mail+"==");
                        System.out.println("======"+Constants.oa_mail_password+"==");
                        if (flag) {
                                session = getSession(Constants.smtpServer,fromuser+Constants.damin,userpass);
                        } else {
                        	System.out.println(fromuser+Constants.damin);
                                session = getSession(Constants.smtpServer);
                        }
                        if (users.length > 0) {
                                MimeMessage mimeMsg = new MimeMessage(session);
                                InternetAddress[] to = new InternetAddress[users.length];
                                for (int i = 0; i < users.length; i++) {
                                        try {
                                                InternetAddress tem = new InternetAddress(users[i]+Constants.damin);
                                                to[i] = tem;
                                        } catch (Exception e) {
                                                System.out.println(users[i] + "is not a avild email address");
                                        }
                                }
                                try {
                                    mimeMsg.setFrom(new InternetAddress(fromuser+Constants.damin));
                                    mimeMsg.setRecipients(Message.RecipientType.TO, to);
                                    mimeMsg.setSubject(subject);
                                    mimeMsg.setText(content);
                                    mimeMsg.addHeader("Disposition-Notification-To","0");
                                    mimeMsg.setSentDate(new Date());
                                    mimeMsg.saveChanges();
                                    Transport.send(mimeMsg);
                                } catch (Exception e) {
                                        //e.printStackTrace();
                                        System.out.println("错误，邮件发送失败！！！");
                                        System.out.println("send false");
                                        b=false;
                                }
                        }
                        return b;
	}

	public static boolean sendMail(String[] users, String subject, String content,
			boolean flag) {
		boolean b=true;
		Session session;
		System.out.println("======"+Constants.smtpServer+"==");
		System.out.println("======"+Constants.oa_mail+"==");
		System.out.println("======"+Constants.oa_mail_password+"==");
		if (flag) {
			session = getSession(Constants.smtpServer,Constants.oa_mail,Constants.oa_mail_password);
		} else {
			session = getSession(Constants.smtpServer);
		}
		if (users.length > 0) {
			MimeMessage mimeMsg = new MimeMessage(session);
			InternetAddress[] to = new InternetAddress[users.length];
			for (int i = 0; i < users.length; i++) {
				try {
					InternetAddress tem = new InternetAddress(users[i]+Constants.damin);
					to[i] = tem;
				} catch (Exception e) {
					System.out.println(users[i]
							+ "is not a avild email address");
				}
			}
			try {
				mimeMsg.setFrom(new InternetAddress(Constants.oa_mail));
				mimeMsg.setRecipients(Message.RecipientType.TO, to);
				mimeMsg.setSubject(subject);
				mimeMsg.addHeader("Disposition-Notification-To","0");
				mimeMsg.setText(content);
				mimeMsg.saveChanges();
				Transport.send(mimeMsg);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("send false");
				b=false;
			}
		}
		return b;
	}
}
