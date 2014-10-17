package com.sany.workflow.demo.test;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.sany.greatwall.domain.AccessToken;
import com.sany.sim.message.MessageService;
import com.sany.sim.message.entity.ReturnBean;
import com.sany.sim.message.entity.SendMessageBean;

public class SendMessageTest {
	
	private static String ms_test_address = "http://10.0.14.21:8080/SanySIM/cxfservices/messageServicePort";
	private static String ms_proc_address = "http://sim.sany.com.cn:85/SanySIM/cxfservices/messageServicePort";
	private static String token_value = "f19f3394-8242-4842-beba-33c7731c34ed";
	private static String app_name = "sim";
	
	private MessageService messageService;
	
	/**
	 * 测试工号 10005873               37004470                   00028200
	 * 1.设置accessToken
	 * 2.根据好后台项目的配置1.只发短信，2.只发企信，3.先企信，失败了再短信
	*/
	
	public static void main(String [] args0){
		  JaxWsProxyFactoryBean jsw = new JaxWsProxyFactoryBean();
	      jsw.setAddress(ms_test_address);
	      jsw.setServiceClass(com.sany.sim.message.MessageService.class);
	      com.sany.sim.message.MessageService mms = (com.sany.sim.message.MessageService)jsw.create();
	      AccessToken accessToken = new AccessToken();
	      accessToken.setToken(token_value);
	      accessToken.setApplication(app_name);
			      try {
	                        SendMessageBean smb = new SendMessageBean();
	                        smb.setWorknum("10001861");
				//           smb.setMobile(mobile);21002521
				//           smb.setBusinessKey(businessKey);
	                        smb.setMsgContent("本条信息为IT发送的移动平台测试消息，谢谢！");
	                        ReturnBean rb =mms.sendMsg(accessToken, smb);
	                        System.out.println(rb.isSuccess() + " "+rb.getMessage());
			         }catch(Exception e){
			        	 e.printStackTrace();
			        	 }
			         }
	
	public void sendMessage(){
		
		 AccessToken accessToken = new AccessToken();
	     accessToken.setToken(token_value);
	     accessToken.setApplication(app_name);
			      try {
	                       SendMessageBean smb = new SendMessageBean();
	                       smb.setWorknum("10001861");
				//           smb.setMobile(mobile);
				//           smb.setBusinessKey(businessKey);
	                       smb.setMsgContent("本条信息为IT发送的移动平台测试消息，谢谢！");
	                       ReturnBean rb =messageService.sendMsg(accessToken, smb);
	                       System.out.println(rb.isSuccess() + " "+rb.getMessage());
			         }catch(Exception e){
			        	 e.printStackTrace();
			        	 }
	}
}
