package com.sany.mail;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

/**
 * <p>
 * Title: TestMail.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2013-5-20 下午6:07:17
 * @author biaoping.yin
 * @version 1.0.0
 */
public class TestMail {
	@Test
	public void test()
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("com/sany/mail/property-mail.xml");
		SendMail sendMail = context.getTBeanObject("sendMail", SendMail.class);	
		MailInfo mailInfo = new MailInfo();
		mailInfo.setContent("很好，很强大!");
		mailInfo.setFromAddress("uimadmin@sany.com.cn");
		mailInfo.setMailServerHost("172.16.9.7");
		mailInfo.setMailServerPort("25");
		mailInfo.setPassword("uimpc@SANY");
		mailInfo.setSubject("邮件测试");
		mailInfo.setToAddress(new String[]{"yinbp@sany.com.cn"});
		mailInfo.setUserName("uimadmin");
		mailInfo.setValidate(true);
		sendMail.sendTextMail(mailInfo);
//		sendMail.sendTextMail(new String[]{"yinbp@sany.com.cn"}, "你好", "很好，很强大!");
//		String content = " <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"96\">"
//	+"<tr>"
//    +"<td width=\"24%\" height=\"94\"><a href='http://ippresource.sany.com.cn/qikan/html/2013-04/29/node_1.htm' target='_blank'><img width='80' height='90' border='0' src='uploadfiles/201207/20120731060411867.jpg'></a></td>"
//    +"<td width=\"76%\" valign=\"top\"><div><strong>bbossgroups报</strong></div>"
//    +  "<div><span class=\"red\">bbossgroups报</span> <a href=\"javascript:void('0')\" onclick=\"showPdf('http://ippresource.sany.com.cn/qikan/html/2013-04/29/node_1.htm')\">浏览</a>|<a href='brands/magazine/default.htm' target='_blank'>更多 >></a></div>"
//    +  "<div>做一流企业，办一流企业报。《bbossgroups》报1999年创刊，它见证记载了三一发展的艰辛和辉煌。</div></td>"
//  +"</tr>"
//  +"</table>"
//		 ;
//		
//		sendMail.sendHtmlMail(new String[]{"yinbp@sany.com.cn"}, "你好html", content);
//		sendMail.sendAttach(new String[]{"yinbp@sany.com.cn"}, "你好html", content,new String[]{"D:\\workspace\\SanyPDP\\resources\\org\\frameworkset\\task\\quarts-task.xml"});
	}

}
