package com.sany.hrm.workflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.web.servlet.ModelMap;

import com.sany.hrm.common.service.LoginService;
import com.sany.hrm.workflow.service.WorkflowService;

/**
 * 待办任务
 * 
 * @author gw_yaoht
 * @version 2012-8-14
 **/
public class TaskController {
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

//		List<Map<String, Object>> objectMapList = this.workflowService.findTask(warrantId, 0, 20);
		List<Map<String, Object>> objectMapList = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < 10; i ++)
		{
			Map<String, Object> task = new HashMap<String,Object>();
			task.put("TASK_ID_", "TASK_ID_" + i);
			task.put("CREATE_PE_NA", "罗宏");
			task.put("message", "我要调入董办，请领导批准！" );
			objectMapList.add(task);
		}
		model.addAttribute("taskList", objectMapList);

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
}
