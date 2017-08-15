/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package org.frameworkset.mail;

import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Test;

import com.sany.mail.SendMail;

/**
 * <p>Title: MailSend.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-6-19
 * @author biaoping.yin
 * @version 1.0
 */
public class MailSend {
	public void send(DelegateExecution execution,List assigneeList )
	{
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("com/sany/mail/property-mail.xml");
		SendMail sendMail = context.getTBeanObject("sendMail", SendMail.class);		
		sendMail.sendTextMail(new String[]{"yinbp@sany.com.cn"}, "你好", "很好，很强大!");
		String content = " <table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"96\">"
	+"<tr>"
    +"<td width=\"24%\" height=\"94\"><a href='http://ippresource.sany.com.cn/qikan/html/2013-04/29/node_1.htm' target='_blank'><img width='80' height='90' border='0' src='uploadfiles/201207/20120731060411867.jpg'></a></td>"
    +"<td width=\"76%\" valign=\"top\"><div><strong>bbossgroups报</strong></div>"
    +  "<div><span class=\"red\">bbossgroups报</span> <a href=\"javascript:void('0')\" onclick=\"showPdf('http://ippresource.sany.com.cn/qikan/html/2013-04/29/node_1.htm')\">浏览</a>|<a href='brands/magazine/default.htm' target='_blank'>更多 >></a></div>"
    +  "<div>做一流企业，办一流企业报。《bbossgroups》报1999年创刊，它见证记载了三一发展的艰辛和辉煌。</div></td>"
  +"</tr>"
  +"</table>"
		 ;
		
		sendMail.sendHtmlMail(new String[]{"yinbp@sany.com.cn"}, "你好html", content);
		sendMail.sendAttach(new String[]{"yinbp@sany.com.cn"}, "你好html", content,new String[]{"D:\\workspace\\SanyPDP\\resources\\org\\frameworkset\\task\\quarts-task.xml"});
	}
	@Test
	public void aaa( )
	{
		com.sany.greatwall.WorkflowService s;
		BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("beans.xml");
		context.getBeanObject("WSServiceClient");
	}

}
