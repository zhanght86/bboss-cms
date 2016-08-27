package com.sany.workflow.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
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
import com.sany.workflow.util.WorkFlowConstant;

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

	// 发送短信参数
	private String token_value;
	private String app_name;

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
			template.setCreateTime1(new Timestamp(new Date().getTime()));
			template.setCreator(creator);

			executor.insertBean("insertMessTemplate", template);

			// 修改保存
		} else {
			template.setLastUpdatetime(new Timestamp(new Date().getTime()));
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
	public void sendNotice(List<Map<String, Object>> fieldList) {

		if (fieldList != null && fieldList.size() > 0) {

			for (int i = 0; i < fieldList.size(); i++) {
				Map<String, Object> map = fieldList.get(i);
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
				String[] mailAddresss = (String[]) map.get("mailAddress");
				map.remove("mailAddress");

				// 邮件主题
				String subject = map.get("subject") == null ? "" : map
						.get("subject") + "";
				map.remove("subject");

				// 任务id
				String taskId = map.get("taskId") == null ? "" : map
						.get("taskId") + "";
				map.remove("taskId");

				// 提醒类型
				String noticeType = map.get("noticeType") == null ? "" : map
						.get("noticeType") + "";
				map.remove("noticeType");

				if (StringUtil.isEmpty(messagetempleid)
						&& StringUtil.isEmpty(emailtempleid)) {
					continue;
				}

				// 0 未发送 1发送成功 2 发送失败
				String isMessSuccess = "0";
				String isEmailSuccess = "0";
				// 调短信接口，发送短信
				if (StringUtil.isNotEmpty(messagetempleid)) {

					try {

						String content = replaceTemplate(messagetempleid, map);

						isMessSuccess = sendMessage(worknum, content) == true ? "1"
								: "2";

					} catch (Exception e) {
						isMessSuccess = "2";

						logger.error("任务id:" + taskId + "短信发送异常:"
								+ e.getMessage());
					}
				}

				// 调邮件接口，发邮件
				if (StringUtil.isNotEmpty(emailtempleid)) {
					try {

						String content = replaceTemplate(emailtempleid, map);

						isEmailSuccess = sendEmail(mailAddresss, subject,
								content) == true ? "1" : "2";

					} catch (Exception e) {
						isEmailSuccess = "2";

						logger.error("任务id:" + taskId + "邮件发送异常:"
								+ e.getMessage());
					}
				}

				try {

					int isSend = 0;

					if ("1".equals(isMessSuccess) && "1".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.ALL_SEND_SUCCESS;// 短信、邮件发送成功
					} else if ("1".equals(isMessSuccess) && "0".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.MESS_SEND_SUCCESS;// 短信发送成功
					} else if ("2".equals(isMessSuccess) && "0".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.MESS_SEND_FAIL;// 短信发送失败
					} else if ("0".equals(isMessSuccess) && "1".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.EMAIL_SEND_SUCCESS;//邮件发送成功
					} else if ("0".equals(isMessSuccess) && "2".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.EMAIL_SEND_FAIL;//邮件发送失败
					} else if ("1".equals(isMessSuccess) && "2".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.SEND_MESS_SUCCESS_EMAIL_FAIL;// 短信发送成功、邮件发送失败
					} else if ("2".equals(isMessSuccess) && "1".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.SEND_MESS_FAIL_EMAIL_SUCCESS;// 短信发送失败、邮件发送成功
					} else if ("2".equals(isMessSuccess) && "2".equals(isEmailSuccess)) {
						isSend = WorkFlowConstant.ALL_SEND_FAIL;// 短信、邮件发送失败
					} else {
						isSend = WorkFlowConstant.NO_SEND;// 未发送
					}

					// 记录预警短信提醒发送状态 1预警2超时
					if ("1".equals(noticeType)) {
						activitiService.updateMessSendState(taskId, isSend, 0);
					} else {
						activitiService.updateMessSendState(taskId, 0, isSend);
					}

				} catch (Exception e) {
					logger.error("记录任务id:" + taskId
							+ (noticeType.equals("0") ? "预警" : "超时")
							+ "提醒发送状态异常:" + e.getMessage());
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
	private String replaceTemplate(String templateId, Map<String, Object> map)
			throws Exception {
		// 获取模板内容
		Template template = executor.queryObject(Template.class,
				"selectMessTemplateById", templateId);

		String content = template.getTempleContent();

		Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();

		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();

			content = content.replaceAll("\\$\\{" + entry.getKey() + "\\}",
					entry.getValue() + "");
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
		accessToken.setToken(token_value);
		accessToken.setApplication(app_name);

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

}
