package com.sany.hrm.personnel;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;
import com.sany.hrm.common.service.LoginService;
import com.sany.hrm.personnel.service.PersonnelService;
import com.sany.hrm.workflow.dto.WfNodeDto;
import com.sany.hrm.workflow.service.WorkflowService;
import com.sany.workflow.util.JsonBinder;

/**
 * 离司
 * 
 * @author gw_yaoht
 * @version 2012-8-15
 **/
public class DimissionController {
	/**
	 * 登录服务
	 */
	private LoginService loginService;

	/**
	 * 人事事务
	 */
	private PersonnelService personnelService;

	/**
	 * 工作流
	 */
	private WorkflowService workflowService;

	/**
	 * 审核
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param model
	 *            数据
	 * @return 模板路径
	 * @throws Exception
	 */
	public String auditing(String taskId, ModelMap model) throws Exception {
		// 注册webService接口凭证。
//		String warrantId = this.loginService.getWarrantId();
		AccessControl control = AccessControl.getAccessControl(); 
		String warrantId = control.getUserAccount();

//		String taskId = request.getParameter("taskId");

//		List<Map<String, Object>> objectMapList = this.workflowService.findWorkflowLog(warrantId, taskId);
		
		List<Map<String, Object>> objectMapList = new ArrayList<Map<String, Object>>();
		Map<String, Object> log = new HashMap<String, Object>();
		log.put("CREATE_TIME_NA_", "2014-03-10 12:00");
		log.put("NODE_NAME", "起草");
		log.put("OPERATION_PERSON_NA_", "罗宏");
		log.put("MESSAGE_", "提交");
		log.put("DESCRIBING", "第一次提交流程");
		objectMapList.add(log);
		log = new HashMap<String, Object>();
		log.put("CREATE_TIME_NA_", "2014-03-10 14:00");
		log.put("NODE_NAME", "初审");
		log.put("OPERATION_PERSON_NA_", "王微");
		log.put("MESSAGE_", "初审结果");
		log.put("DESCRIBING", "业绩属实，符合董办人才需求");
		objectMapList.add(log);
	
		model.addAttribute("workflowLogList", objectMapList);

//		List<WfNodeDto> wfNodeList = this.personnelService.findDimissionTaskBackNode(warrantId, taskId);
		List<WfNodeDto> wfNodeList = new ArrayList<WfNodeDto>();
		WfNodeDto wfNodeDto = new WfNodeDto();
		wfNodeDto.setWfNodeCode("subperson");
		wfNodeDto.setDescribing("[{\"name\":\"起草人罗宏\",\"userCode\":\"luoh6\"},{\"name\":\"起草人罗宏1\",\"userCode\":\"luoh9\"}]");
		wfNodeDto.setName("流程起草人");
		wfNodeList.add(wfNodeDto);
		wfNodeDto = new WfNodeDto();
		wfNodeDto.setWfNodeCode("marc1");
		wfNodeDto.setDescribing("[{\"name\":\"初审人马总\",\"userCode\":\"marc1\"},{\"name\":\"初审人马总\",\"userCode\":\"marc12\"}]");
		wfNodeDto.setName("流程初审人");
		wfNodeList.add(wfNodeDto);
		model.addAttribute("backWfNodeList", wfNodeList);
		return "path:auditing";
	}

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
	public String index(String taskId, ModelMap model) throws Exception {
		// 注册webService接口凭证。
		AccessControl control = AccessControl.getAccessControl(); 
		String warrantId = control.getUserAccount();
		
//		Map<String, Object> objectMap = this.personnelService.getDimissionTask(warrantId, taskId);
		Map<String, Object> objectMap = new HashMap<String,Object>();
		objectMap.put("PERNR", "10006673");
		objectMap.put("PERNR_NA_", "罗宏");
		objectMap.put("USRID_3", "18807409059");
		objectMap.put("orgName", "流程信息化部>信息应用部>架构科");
		objectMap.put("POSTY_NA", "合同工");
		objectMap.put("POSNC_NA", "普工");
		objectMap.put("POSSC", "A类高一");
		objectMap.put("POST_NA", "IT研发类");
		objectMap.put("USRID_5", "luoh9");
		objectMap.put("CREATETIME_NA_", "2014-03-10 12:00");
		objectMap.put("DISPOSE_NAME", control.getUserName()+"("+warrantId+")-"+control.getUserAttribute("orgjob"));
		objectMap.put("GW", "架构工程师");
		
		objectMap.put("WORKFLOW_NA_", "IT支持类");
		objectMap.put("DAT01_NA_", "2011-12-30");
		objectMap.put("WORK_YEAR_", 10);
		objectMap.put("WORK_ENDDA", "2021-12-30");
		objectMap.put("INSTANCE_DE_", "调入董办");
		objectMap.put("taskId", taskId);
		
		
		
		List<Map<String,String>> fileList = new ArrayList<Map<String,String>>();
		objectMap.put("fileList", fileList);
		for(int i = 0; i < 5; i ++)
		{
			Map<String,String> file = new HashMap<String,String>();
			file.put("FILEPATH", "http://10.0.15.38:9081/bboss-mvc/file/download.htm?fileName=jquery.mobile-1.3.2.zip");
			file.put("FILENAME", "光荣业绩_"+i);
			fileList.add(file);
		}
		

		model.addAttribute("dimissionTask", objectMap);

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
	 * @param personnelService
	 *            人事事务
	 */
	public void setPersonnelService(PersonnelService personnelService) {
		this.personnelService = personnelService;
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
	 * 提交待办
	 * 
	 * HttpServletRequest
	 * 
	 * @param model
	 *            数据
	 * @return 模板路径
	 * @throws Exception
	 */

	public @ResponseBody
	String submitDimissionTask(HttpServletRequest request, ModelMap model) throws Exception {
		// 注册webService接口凭证。
		String warrantId = this.loginService.getWarrantId();

		Map<String, Object> stringStringMap = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		Enumeration<String> enumeration = request.getParameterNames();

		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();

			stringStringMap.put(key, request.getParameter(key));
		}

		Map<String, Object> jsonMap = new HashMap<String, Object>();

		String nodeMsg = MapUtils.getString(stringStringMap, "nodeMsg");

		if (StringUtils.isNotBlank(nodeMsg)) {
			nodeMsg = new URLCodec().decode(nodeMsg);

			stringStringMap.put("nodeMsg", nodeMsg);
		}

		try {
			String data = this.personnelService.submitDimissionTask(warrantId, stringStringMap);

			jsonMap.put("data", data);
		} catch (Exception e) {
			e.printStackTrace();

			jsonMap.put("message", e.getMessage());
		}

		String json = JsonBinder.buildNormalBinder().toJson(jsonMap);

		return json;
	}
}
