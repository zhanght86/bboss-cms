package com.sany.hrm;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.sany.hrm.common.service.LoginService;
import com.sany.hrm.workflow.service.WorkflowService;

/**
 * 默认
 * 
 * @author gw_yaoht
 * @version 2012-8-13
 **/
public class IndexController {

	/**
	 * 登录服务
	 */
	private LoginService loginService;

	/**
	 * 工作流
	 */
	private WorkflowService workflowService;

	/**
	 * 默认页面
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            数据
	 * @return 模板路径
	 * @throws Exception
	 */
	public String index(HttpServletRequest request, ModelMap model) throws Exception {
		String warrantId = this.loginService.getWarrantId();

		Integer personnelCount = this.workflowService.readTaskCount(warrantId);

		model.addAttribute("personnelCount", personnelCount);

		return "path:index";
	}

	/**
	 * 设置
	 * 
	 * @param loginService
	 *            登录服务
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * 设置
	 * 
	 * @param workflowService
	 *            工作流
	 */
	public void setWorkflowService(WorkflowService workflowService) {
		this.workflowService = workflowService;
	}

	/**
	 * 获取
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            数据
	 * @return 待办数量
	 * @throws Exception
	 */
	public @ResponseBody
	String taskCount(HttpServletRequest request, ModelMap model) throws Exception {
		String warrantId = this.loginService.getWarrantId();

		Integer personnelCount = this.workflowService.readTaskCount(warrantId);

		return String.valueOf(personnelCount);
	}
}
