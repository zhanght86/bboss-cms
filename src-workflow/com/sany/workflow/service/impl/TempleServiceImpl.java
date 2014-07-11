package com.sany.workflow.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.sany.greatwall.domain.AccessToken;
import com.sany.mail.util.MailHelper;
import com.sany.sim.message.MessageService;
import com.sany.sim.message.entity.ReturnBean;
import com.sany.sim.message.entity.SendMessageBean;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.Template;
import com.sany.workflow.entity.TempleCondition;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.TempleService;

/**
 * @todo 模板管理模块实现类
 * @author tanx
 * @date 2014年6月9日
 * 
 */
public class TempleServiceImpl implements TempleService,
		org.frameworkset.spi.DisposableBean {

	private static Log logger = LogFactory.getLog(TempleService.class);

	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	private ActivitiService activitiService;

	private MessageService messageService;

	@Override
	public void destroy() throws Exception {

	}

	@Override
	public ListInfo queryTemples(TempleCondition template, long offset,
			int pagesize) throws Exception {
		ListInfo listInfo = null;

		if (StringUtil.isNotEmpty(template.getTempleTitle())) {
			template.setTempleTitle("%" + template.getTempleTitle() + "%");
		}

		listInfo = executor.queryListInfoBean(Template.class,
				"selectMessTemplateList", offset, pagesize, template);

		return listInfo;
	}

	@Override
	public Template queryTemple(String templateId) throws Exception {

		return executor.queryObject(Template.class, "selectMessTemplateById",
				templateId);

	}

	@Override
	public void saveTemple(TempleCondition template) throws Exception {
		// 新增保存
		if (StringUtil.isEmpty(template.getTempleId())) {

			template.setTempleId(UUID.randomUUID().toString());
			String id = AccessControl.getAccessControl().getUserAccount();
			String creator = activitiService.userIdToUserName(id, "1");
			template.setCreator(creator);

			executor.insertBean("insertMessTemplate", template);

			// 修改保存
		} else {

			executor.updateBean("updateMessTemplate", template);
		}
	}

	@Override
	public List<Template> queryTempleList(String templateType) throws Exception {

		List<Template> templeList = executor.queryList(Template.class,
				"selectMessTemplateListByType", templateType);

		return templeList;

	}

	@Override
	public void sendNotice(List<Map<String, String>> fieldList) {

		if (fieldList != null && fieldList.size() > 0) {

			for (int i = 0; i < fieldList.size(); i++) {
				Map<String, String> map = fieldList.get(i);
				// 短信模板
				String messagetempleid = map.get("messageTempleId") == null ? ""
						: map.get("messageTempleId") + "";
				map.remove("messageTempleId");

				// 邮件模板
				String emailtempleid = map.get("emailTempleId") == null ? ""
						: map.get("emailTempleId") + "";
				map.remove("emailTempleId");

				// 接收人工号
				String worknum = map.get("worknum") == null ? "" : map
						.get("worknum") + "";
				map.remove("worknum");

				// 接收人手机号
				String mobile = map.get("mobile") == null ? "" : map
						.get("mobile") + "";
				map.remove("mobile");

				// 接收人邮件地址
				String mailAddresss = map.get("mailAddress") == null ? "" : map
						.get("mailAddress") + "";
				String[] mailAddress =mailAddresss.split(",");
				map.remove("mailAddress");

				// 邮件主题
				String subject = map.get("subject") == null ? "" : map
						.get("subject") + "";
				map.remove("subject");

				// 任务id
				String taskId = map.get("taskId") == null ? "" : map
						.get("taskId") + "";
				map.remove("taskId");

				// 接收人
				String realName = map.get("realName") == null ? "" : map
						.get("realName") + "";

				if (StringUtil.isEmpty(messagetempleid)
						&& StringUtil.isEmpty(emailtempleid)) {
					continue;
				}

				// 调短信接口，发送短信
				if (StringUtil.isNotEmpty(messagetempleid)) {

					try {

						String content = replaceTemplate(messagetempleid, map);

						boolean isSuccess = sendMessage(worknum, content);

						logger.info("短信发送" + (isSuccess ? "成功" : "失败")
								+ ",任务id:" + taskId + ";发送类型:邮件;发送内容:"
								+ content + ";接收人:" + realName + ";接收人手机号:"
								+ mobile);
					} catch (Exception e) {
						logger.error("短信发送异常:" + e.getMessage());
					}
				}

				// 调邮件接口，发邮件
				if (StringUtil.isNotEmpty(emailtempleid)) {
					try {

						String content = replaceTemplate(emailtempleid, map);
						boolean isSuccess = sendEmail(mailAddress, subject,
								content);

						logger.info("邮件发送" + (isSuccess ? "成功" : "失败")
								+ ",任务id:" + taskId + ";发送类型:邮件;发送内容:"
								+ content + ";接收人:" + realName + ";接收人地址:"
								+ mailAddress);

					} catch (Exception e) {
						logger.error("邮件发送异常,模板内容替换异常:" + e.getMessage());
					}
				}
			}
		}

	}

	/**
	 * 替换模板内容
	 * 
	 * @param templateId
	 * @param map
	 * @return
	 * @throws Exception
	 *             2014年6月25日
	 */
	private String replaceTemplate(String templateId, Map map) throws Exception {
		// 获取模板内容
		Template template = executor.queryObject(Template.class,
				"selectMessTemplateById", templateId);

		String content = template.getTempleContent();

		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();

			content = content.replaceAll("\\$\\{" + entry.getKey() + "\\}",
					entry.getValue());
		}
		return content;
	}

	/**
	 * 发送邮件
	 * 
	 * @param toAddress
	 * @param subject
	 * @param content
	 * @return 2014年6月25日
	 */
	private boolean sendEmail(String[] toAddress, String subject, String content) {

		// BaseApplicationContext context = DefaultApplicationContext
		// .getApplicationContext("com/sany/mail/property-mail.xml");
		//
		// SendMail sendMail = context.getTBeanObject("sendMail",
		// SendMail.class);

		return MailHelper.getSendMail().sendHtmlMail(toAddress, subject,
				content);
	}

	/**
	 * 发送短信
	 * 
	 * @param toAddress
	 * @param subject
	 * @param content
	 * @return 2014年6月25日
	 */
	private boolean sendMessage(String worknum, String content)
			throws Exception {
		AccessToken accessToken = new AccessToken();
		accessToken.setToken("f19f3394-8242-4842-beba-33c7731c34ed");
		accessToken.setApplication("sim");

		SendMessageBean smb = new SendMessageBean();
		smb.setWorknum(worknum);
		// smb.setMobile(mobile);
		// smb.setBusinessKey(businessKey);
		smb.setMsgContent(content);

		ReturnBean rb = messageService.sendMsg(accessToken, smb);

		return rb.isSuccess();
	}

	@Override
	public String delTemplates(String templateIds) throws Exception {

		String[] ids = templateIds.split(",");

		TransactionManager tm = new TransactionManager();

		String result = "success";

		try {

			tm.begin();

			for (String templeId : ids) {

				if (StringUtil.isNotEmpty(templeId)) {

					// 判断模板是否有没被引用
					List<ActivitiNodeCandidate> nodeList = executor.queryList(
							ActivitiNodeCandidate.class, "selectNodeById",
							templeId, templeId);

					if (nodeList != null && nodeList.size() != 0) {
						result = "fail";
						break;
					} else {
						// 执行删除
						executor.delete("deleteMessTemplate", templeId);
					}
				}
			}

			if (result.equals("success")) {

				tm.commit();
			}

			return result;

		} finally {
			tm.release();
		}

	}

	// public TempleServiceImpl() {
	// executor = new com.frameworkset.common.poolman.ConfigSQLExecutor(
	// "com/sany/workflow/messTemplate.xml");
	// }

	public static void main(String[] args) throws Exception {
		TempleServiceImpl tma = new TempleServiceImpl();
		Map map = new HashMap();
		map.put("oper", "李四");
		map.put("processKey", "退料申请流程");
		map.put("nodeKey", "工艺工程师审批");
		// System.out.println(tma.replaceTemple(
		// "45303c4b-a8a0-45ee-8513-e22a63460169", map));
	}

}
