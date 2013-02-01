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
	 */
	public  static boolean SendSMS(String tel,String msg){
		boolean ok = true;
		try {
			SmsNewOperatoraddsubCodeServiceLocator locator = new SmsNewOperatoraddsubCodeServiceLocator();
			SmsNewOperatoraddsubCode_PortType pt = locator.getSmsNewOperatoraddsubCode();
			
			//参数
			String account=new String(config.getUser()); //接口账号用户名
			String password=new String(MD5.crypt(config.getPassword()));//接口密码
			String firstDeptId=new String("50109849"); //一级部门编码    IT总部  50109849
			String secondDeptId=new String(""); //二级部门编码
			String thirdDeptId=new String(""); //三级部门编码
			MtNewMessage message=new MtNewMessage();
			message.setContent(msg); //短信内容
			message.setPhoneNumber(tel); //接受短信的号码
			String subCode=new String("18");//业务代码
			
			StringHolder sendResMsg = new StringHolder();
			StringHolder errMsg = new StringHolder();
			//发送短信接口
			pt.sendSms(account, password, firstDeptId, secondDeptId, thirdDeptId, message, subCode, sendResMsg, errMsg);
			
			ok = sendIsSuccess(ok, sendResMsg, errMsg);
			
		} catch (Exception e) {
			ok = false;
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return ok;
	}
	private static boolean sendIsSuccess(boolean ok, StringHolder sendResMsg,
			StringHolder errMsg) {
		if (errMsg.value.length() == 0 && sendResMsg != null
				&& sendResMsg.value != null)
			System.out.println("sendResMsg:" + sendResMsg.value);
		else if (errMsg.value.equalsIgnoreCase("-1")) {
			System.out.println("输入参数不正确，请检查账户，密码，等输入参数是否为空");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-2")) {
			System.out.println("请检查用户名，密码是否正确及部门名称是否与短信平台匹配");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-3")) {
			System.out.println("账户已经超过每日发送短信限制数量（当账户被限制每日发送量时有用）");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-4")) {
			System.out.println("客户端ip地址不正确（当需要ip校验时）");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-5")) {
			System.out.println("smsId与数据库重复（下发短信时，如果smsId 由客户端传入，该参数不能重复）");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-6")) {
			System.out.println("内容含有非法关键字，请检查下发内容");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-7")) {
			System.out.println("对应的号码下发失败，下发号码为空或其他错误，导致该号码发送失败");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-8")) {
			System.out.println("访问频率过快");
			ok = false;
		} else if (errMsg.value.equalsIgnoreCase("-9")) {
			System.out.println("提交号码数量超过最大限制");
			ok = false;
		}
		return ok;
	}

} 

