package com.sany.sms;


import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;


public class SMSSender {
	
	private static final Log log = LogFactory.getLog(SMSSender.class);
	public final static Config config;
	static
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("com/sany/sms/sms.xml");
		config = context.getTBeanObject("smsconf", Config.class);
	}
	/**
	 * 发送短信
	 * @param tel
	 * @param msg
	 * @return true:发送成功；false：发送失败
	 * @throws Exception 
	 */
	public  static String sendSMS(String tel,String msg) throws Exception{

        String account = new String(SMSSender.config.getUser()); // 接口账号用户名
        String password = new String(MD5.crypt(SMSSender.config.getPassword()));// 接口密码

        String orgeh_level1 = SMSSender.config.getFirstDeptId();
        String firstDeptId = orgeh_level1; // 一级部门编码 IT总部 50109849
        String secondDeptId = SMSSender.config.getSecondDeptId(); // 二级部门编码
        String thirdDeptId = SMSSender.config.getThirdDeptId(); // 三级部门编码

       
        String subCode = new String(SMSSender.config.getSMS_SUBCODE());

      return SmsNewOperatoraddsubCodeServiceLocator.sendSms( account,  password,  firstDeptId,  secondDeptId,
      		 thirdDeptId, msg, tel, subCode );
	}
	
	
	public static void main(String[] args) throws Exception
	{
		sendSMS("短信内容++","13319589069");
	}

} 

