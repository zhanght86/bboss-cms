package com.sany.workflow.test;

import java.util.HashMap;
import java.util.Map;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Before;
import org.junit.Test;

import com.sany.mail.MailInfo;
import com.sany.mail.SendMail;
import com.sany.workflow.service.TempleService;
import com.sany.workflow.service.impl.TempleServiceImpl;

public class TestTemple {

	private SendMail sendMail;
	private TempleService templeService;

	@Before
	public void init() throws Exception {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("com/sany/mail/property-mail.xml");
		sendMail = context.getTBeanObject("sendMail", SendMail.class);
		
		BaseApplicationContext context2 = DefaultApplicationContext
				.getApplicationContext("WebRoot/WEB-INF/conf/workflow/bboss-workflow-templemanage.xml");

		templeService = context2.getTBeanObject("templeService",
				TempleServiceImpl.class);

	}

	@Test
	public void testEmail() throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		map.put("userName", "李四");
		map.put("processName", "退料申请流程");
		map.put("nodeName", "工艺工程师审批");

//		String content = templeService.replaceTemple(
//				"6e3b0827-627e-45f6-8788-add5080b9fe3", map);

		MailInfo mailInfo = new MailInfo();
		mailInfo.setMailServerHost("smtp.sany.com.cn");
		mailInfo.setMailServerPort("25");
		mailInfo.setFromAddress("uimadmin@sany.com.cn");
		mailInfo.setUserName("uimadmin");
		mailInfo.setPassword("uimpc@SANY");
		mailInfo.setValidate(true);
		mailInfo.setSubject("邮件测试");
		mailInfo.setToAddress(new String[] { "yinbp@sany.com.cn" });
//		mailInfo.setContent(content);

		sendMail.sendTextMail(mailInfo);
	}
}
