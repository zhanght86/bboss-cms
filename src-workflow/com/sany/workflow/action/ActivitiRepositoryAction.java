/*
 * @(#)ActivitiRepository.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.workflow.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.RollbackException;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.token.TokenException;
import org.frameworkset.web.token.TokenStore;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.application.entity.WfApp;
import com.sany.application.service.AppcreateService;
import com.sany.application.util.AppHelper;
import com.sany.workflow.entity.ActivitiNodeCandidate;
import com.sany.workflow.entity.ActivitiNodeInfo;
import com.sany.workflow.entity.LoadProcess;
import com.sany.workflow.entity.Nodevariable;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.ProcessDefCondition;
import com.sany.workflow.entity.ProcessDeployment;
import com.sany.workflow.entity.ProcessInstCondition;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiRelationService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.ProcessException;
import com.sany.workflow.util.WorkFlowConstant;

/**
 * @author yinbp
 * @since 2012-3-22 下午6:03:09
 */
public class ActivitiRepositoryAction {
	
	private static Log logger = LogFactory.getLog(ActivitiRepositoryAction.class);

	private static final String FILE_TYPE_ZIP = "zip";

	private ActivitiService activitiService;
	
	private ActivitiConfigService activitiConfigService;
	
	private AppcreateService appcreateService;
	
	private ActivitiRelationService activitiRelationService;
	
	/**
	 * 部署流程
	 * @param processDeployment
	 * @return String
	 */
	public @ResponseBody
	String deployProcess(ProcessDeployment processDeployment,String needConfig) {
		String result="success";
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			Deployment deployment = null;
			if (processDeployment.getProcessDef().getOriginalFilename().endsWith(".zip") || processDeployment.getProcessDef().getContentType().contains(FILE_TYPE_ZIP)) {
				deployment = activitiService.deployProcDefByZip(processDeployment.getNAME_(), new ZipInputStream(processDeployment
						.getProcessDef().getInputStream()),processDeployment.getUpgradepolicy());
			} else {
				deployment = activitiService.deployProcDefByInputStream(processDeployment.getNAME_(), processDeployment
						.getProcessDef().getOriginalFilename(), processDeployment.getProcessDef().getInputStream(),processDeployment.getUpgradepolicy());
			}
			if(deployment!=null){
				ProcessDef pd = activitiService.getProcessDefByDeploymentId(deployment.getId());
				if(needConfig.equals("1")){
					activitiConfigService.addActivitiNodeInfo(activitiService.getPorcessKeyByDeployMentId(deployment.getId()));
				}
				else
					activitiConfigService.updateActivitiNodeInfo(activitiService.getPorcessKeyByDeployMentId(deployment.getId()),processDeployment.getUpgradepolicy());
				if(!processDeployment.getParamFile().isEmpty()){
					result = activitiConfigService.addNodeParams(processDeployment.getParamFile().getInputStream(),pd.getKEY_());
				}
				if(pd!=null){
					activitiConfigService.addProBusinessType(pd.getKEY_(), processDeployment.getBusinessTypeId());
					
					activitiRelationService.addAppProcRelation(pd,processDeployment.getWf_app_id());
					
					// 部署流程定义，默认计算工时是包含节假日的（状态是1）
					activitiService.addIsContainHoliday(pd.getKEY_(),"1");
				}
			}
			tm.commit();
			return result;
		} catch (Exception e) {
			
			logger.error(e);
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				logger.error(e1);
			}
			
			return StringUtil.exceptionToString(e);
		}
		finally
		{
			tm.release();
		}
	}

	/**
	 * 查询流程定义列表
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param processDefCondition
	 * @param model
	 * @return
	 */
	public String queryProcessDefs(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "resourceName") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ProcessDefCondition processDefCondition, String processChoose ,ModelMap model) {
		try {
			// 获取流程部署信息集合
			if(processDefCondition == null){
				processDefCondition = new ProcessDefCondition();
			}
			
			processDefCondition.setWf_app_mode_type_nonexist(WorkFlowConstant.getApp_third_mode_type());
			
			ListInfo listInfo = activitiService.queryProcessDefs(offset,
					pagesize, processDefCondition);
//			if(listInfo!=null){
//				List<ProcessDef> list = listInfo.getDatas();
//				for (int i = 0; list!=null&&i < list.size(); i++) {
//					list.get(i).setBusiness_name(
//							activitiConfigService.queryBusinessName(list.get(i)
//									.getKEY_()));
//				}
//				listInfo.setDatas(list);
//			}
			
			model.addAttribute("processDefs", listInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if("Y".equals(processChoose)){
			
			return "path:queryProcessDefsHelpChoose";
		}
		return "path:queryProcessDefs";
	}

	public String queryProcessHisVer(String processKey,String version, ModelMap model) {
		try {
			List listInfo = activitiService.queryProdefHisVersion(processKey);
			model.addAttribute("processDefs", listInfo);
			if(StringUtil.isEmpty(version))
			{
				model.addAttribute("currentVersion", version);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "path:queryProcessHisVer";
	}

	/**
	 * 删除流程部署(所有版本)
	 * 
	 * @param processDeploymentids
	 * @param cascades
	 * @return
	 */
	public @ResponseBody
	String deleteDeploymentCascade(String processDeploymentids, boolean[] cascades) {
		// processDef.getProcessDef();
		String[] ids = processDeploymentids.split(",");
		try {
			activitiService.deleteDeploymentAllVersions(ids);

			return "success";
		} catch (Exception e) {
			// TODO ex
			e.printStackTrace();
			return e.getMessage();
		}

	}
	
	/**
	 * 启动流程
	 * 
	 * @param processId
	 * @return
	 */
	public @ResponseBody
	String activateProcess(String processId) {
		try {
			activitiService.activateProcess(processId);

			return "success";
		} catch (Exception e) {
			return null;
		}
	}
	

	/**
	 * 装载未装载的流程信息，主要是一些未经过流程管理部署的流程信息的装载
	 * 
	 * @param processId
	 * @return
	 */
	public String getUnloadProcesses(ModelMap model)
	{
		List<ProcessDef> unloadProcesses = activitiService.getUnloadProcesses();
		model.addAttribute("unloadProcesses", unloadProcesses);
		return "path:getUnloadProcesses";
	}
	/**
	 * 装载未装载的流程信息，主要是一些未经过流程管理部署的流程信息的装载
	 * 
	 * @param processId
	 * @return
	 */
	public @ResponseBody
	Map loadProcess(List<LoadProcess> unloadProcess) {
		try {
			String message = activitiService.loadProcess(unloadProcess);
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("success", true);
			data.put("message", message);
			return data;
		} catch (Exception e) {
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("success", false);
			data.put("message", StringUtil.formatBRException(e));
			return data;
		}
	}

	/**
	 * 停止流程
	 * 
	 * @param processId
	 * @return
	 */
	public @ResponseBody
	String suspendProcess(String processId) {
		try {
			activitiService.suspendProcess(processId);

			return "success";
		} catch (Exception e) {
			return null;
		}
	}

	public String viewProcessInfo(String processKey,String version, ModelMap model) {
		model.addAttribute("processDef", activitiService.queryProdefByKey(processKey,version));
		List<ActivityImpl> aList = activitiService
				.getActivitImplListByProcessKey(processKey);
		for(int i=0;i<aList.size();i++){
			if(!aList.get(i).getProperty("type").equals("userTask")){
				aList.remove(i);
			}
		}
		model.addAttribute("aList", aList);
		return "path:viewProcessInfo";
	}
	
	public String viewHisProcessInfo(String deploymentId, ModelMap model) {
		model.addAttribute("hisProcessDef", activitiService.getProcessDefByDeploymentId(deploymentId));
		
		return "path:viewHisProcessInfo";
	}

	public @ResponseBody(datatype = "json")
	ProcessDef refeProcessInfo(String deploymentId) {

		ProcessDef pd = activitiService.getProcessDefByDeploymentId(deploymentId);
		pd.setDEPLOYMENT_TIME_STRING_(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(pd.getDEPLOYMENT_TIME_()));
		return pd;
	}
	
	public @ResponseBody(datatype = "json")
	List<WfApp> getWfAppData(HttpServletRequest request) {

		try {
			List<WfApp> wfAppList = appcreateService.queryWfApp(new WfApp());
			
			if(!CollectionUtils.isEmpty(wfAppList)){
				
				for(WfApp wfApp : wfAppList){
						
					if(WorkFlowConstant.getApp_sso_mode_type().equals(wfApp.getApp_mode_type())){
						
						StringBuffer ssoUrl = new StringBuffer();
						
						ssoUrl.append(request.getContextPath()).append("/workflow/repository/appssowf.page?ssoAppId=").append(wfApp.getId());
						
						wfApp.setSso_url(ssoUrl.toString());
					}
				}
			}
			
			return wfAppList;
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		} catch (TokenException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
		return null;
	}
	
	/**
	 * 单点登陆地址
	 * @param ssoAppId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String appssowf(String ssoAppId, HttpServletRequest request) throws Exception
	{
			
			WfApp wfApp = appcreateService.queryWfAppById(ssoAppId);
			
			AccessControl accesscontroler = AccessControl.getAccessControl();
			
			String tokenparamname = TokenStore.temptoken_param_name;
			
			String ticket = accesscontroler.getUserAttribute("ticket");
			
			String token = AppHelper.getToken(ticket);
			
			String[] appInfo = AppHelper.getAppInfo();
			
			StringBuffer accounttokenrequest = new StringBuffer();
			
			accounttokenrequest.append(tokenparamname).append("=").append(token).append("&")
					.append(TokenStore.app_param_name).append("=").append(appInfo[0]).append("&")
					.append(TokenStore.app_secret_param_name).append("=").append(appInfo[1]);
			
			StringBuffer ssoUrl = new StringBuffer();
			
			ssoUrl.append("redirect:").append(wfApp.getApp_url()).append("/sso/ssowithtoken.page?")
				  .append(accounttokenrequest).append("&successRedirect=/workflow/repository/index.page");
				
			return ssoUrl.toString();	
		
	}

	public String index() {
		
		return "path:index";
	}
	
	public String workflowmanager(){
		
		return "path:workflowmanager";
	}

	public void getProccessPic(String processId,HttpServletResponse response) throws IOException {
		if(processId!=null&&!processId.equals("")){
			OutputStream out = response.getOutputStream();
			activitiService.getProccessPic(processId, out);
		}
	}
	
	/** 获取流程追踪图 gw_tanx
	 * @param processId
	 * @param response
	 * @throws IOException
	 * 2014年5月13日
	 */
	public void getProccessActivePic(String processInstId,HttpServletResponse response) {
		try {
			
			if(processInstId!=null&&!processInstId.equals("")){
				OutputStream out = response.getOutputStream();
				activitiService.getProccessActivePic(processInstId, out);
			}
			
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
	
	public @ResponseBody(datatype="xml") String getProccessXML(String processId) throws IOException {
		if(processId!=null&&!processId.equals("")){
			
			return activitiService.getProccessXML(processId);
		}
		return null;
	}
//	public @ResponseBody(datatype="xml") String getProccessXMLByKey(String processKey,String version) throws IOException {
//		
//		if(processKey!=null&&!processKey.equals("")){
//			
//			return activitiService.getProccessXMLByKey(processKey, version,"UTF-8");
//		}
//		return null;
//	}
	
	/** 获取流程定义XML gw_tanx
	 * @param processKey
	 * @param version
	 * @param model
	 * @return
	 * 2014年6月24日
	 */
	public String getProccessXMLByKey(String processKey, String version,
			ModelMap model) {

		try {
			if (processKey != null && !processKey.equals("")) {

				String processXML = activitiService.getProccessXMLByKey(
						processKey, version, "UTF-8");
				model.addAttribute("processXML", processXML);
			}

			model.addAttribute("processKey", processKey);
			model.addAttribute("version", version);
			
			return "path:processDefsXML";
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
	
	/**
	 * 跳转到流程实例页面 gw_tanx
	 * 
	 * @param processDefCondition
	 * @param model
	 * @return 2014年5月8日
	 */
	public String toProcessInstance(String processKey, ModelMap model) {
		try {
			List versionList = activitiService.getProcessVersionList(processKey);
			
			model.addAttribute("versionList", versionList);
			model.addAttribute("processKey", processKey);
			
			return "path:toProcessInstance";
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**
	 * 根据流程定义key查询流程实例列表 gw_tanx
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param processDefCondition
	 * @param model
	 * @return 2014年5月8日
	 */
	public String queryProcessIntsByKey(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "START_TIME_") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "true") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ProcessInstCondition processDefCondition, ModelMap model) {
		try {
			// 获取流程实例List
			ListInfo listInfo = activitiService.queryProcessInsts(offset,
					pagesize, processDefCondition);

			model.addAttribute("processInsts", listInfo);
			return "path:queryProcessIntsByKey";

		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
	
	/**开启流程实例 gw_tanx
	 * @param processKey
	 * @param businessKey
	 * @param activitiNodeCandidateList
	 * @param nodevariableList
	 * @return
	 * 2014年5月21日
	 */
	public @ResponseBody
	String startPorcessInstance(String processKey,String businessKey,
			List<ActivitiNodeCandidate> activitiNodeCandidateList,List<Nodevariable> nodevariableList) {
		
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
			// 流程引擎的变量参数集合
			Map<String, Object> map = new HashMap<String, Object>();
			// 节点工时提醒次数集合
			List<Map<String, String>> worktimeList = new ArrayList<Map<String, String>>();
				
			for (int i = 0; i < activitiNodeCandidateList.size();i++) {
				// 用户
				if (!StringUtil.isEmpty(activitiNodeCandidateList.get(i).getCandidate_users_id())) {
					map.put(activitiNodeCandidateList.get(i).getNode_key()+"_users",
							activitiNodeCandidateList.get(i).getCandidate_users_id());
				}
				// 组
				if (!StringUtil.isEmpty(activitiNodeCandidateList.get(i).getCandidate_groups_id())) {
					map.put(activitiNodeCandidateList.get(i).getNode_key()+"_groups",
							activitiNodeCandidateList.get(i).getCandidate_groups_id());
				}
				
				// 流程实例下节点的处理工时与提醒次数
				if (!StringUtil.isEmpty(activitiNodeCandidateList.get(i).getNode_key())) {
					
					Map<String, String> worktimeMap = new HashMap<String, String>();
					worktimeMap.put("NODE_KEY",activitiNodeCandidateList.get(i).getNode_key()+"");
					worktimeMap.put("DURATION_NODE", activitiNodeCandidateList.get(i).getDuration_node()+"");
					worktimeMap.put("NOTICENUM", activitiNodeCandidateList.get(i).getNoticenum()+"");
					worktimeList.add(worktimeMap);
				}
				
			}
			
			for (int i = 0; i < nodevariableList.size();i++) {
				// 变量
				if (!StringUtil.isEmpty(nodevariableList.get(i).getParam_name())) {
					map.put(nodevariableList.get(i).getParam_name(),
							nodevariableList.get(i).getParam_value());
				}
			}

			ProcessInstance processInstance =activitiService.startProcDef(businessKey, processKey, map,AccessControl
					.getAccessControl().getUserAccount());
			
			activitiService.addNodeWorktime(processKey,processInstance.getId(),worktimeList);
			
			tm.commit();
			
			return "success";
			
		} catch (Exception e) {
			return "fail"+e.getMessage();
		}finally {
			tm.release();
		}
	}
	
	/** 跳转流程开启参数配置页面 gw_tanx
	 * @param processKey
	 * @param business_id
	 * @param business_type
	 * @param model
	 * 2014年5月13日
	 */
	public String toStartProcessInst(String processKey, ModelMap model) {
		try {

			List<ActivitiNodeInfo> nodeInfoList = activitiConfigService
					.queryAllActivitiNodeInfo(processKey);
			
			model.addAttribute("nodeInfoList", nodeInfoList);
			model.addAttribute("process_key", processKey);

			return "path:startProcess";
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}

	/**删除流程实例 gw_tanx
	 * @param processInstIds
	 * @param deleteReason
	 * @param delType
	 * @return
	 * 2014年5月16日
	 */
	public @ResponseBody
	String delPorcessInstance(String processInstIds, String deleteReason,
			String delType) {

		try {
			// 逻辑删除
			if ("1".equals(delType)) {

				activitiService.cancleProcessInstances(processInstIds,
						deleteReason);
			} else {// 物理删除

				activitiService.delProcessInstances(processInstIds);

			}
			
			return "success";
		} catch (Exception e) {
			return "fail"+e.getMessage();
		}

	}
	
	/**
	 * 升级流程 gw_tanx
	 * 
	 * @param processInstIds
	 * @param deleteReason
	 * @return 2014年5月9日
	 */
	public @ResponseBody
	String upgradeInstances(String processKey) {
		try {
			
			activitiService.upgradeInstances(processKey);
			
			return "success";
		} catch (Exception e) {
			return "fail"+e.getMessage();
		}
	}
	
	/** 挂起流程实例 gw_tanx
	 * @param processInstId
	 * @return
	 * 2014年5月19日
	 */
	public @ResponseBody
	String suspendProcessInst(String processInstId) {
		try {
			activitiService.suspendProcessInst(processInstId);
			
			return "success";
		} catch (Exception e) {
			return "fail"+e.getMessage();
		}
	}
	
	/** 激活流程实例 gw_tanx
	 * @param processInstId
	 * @return
	 * 2014年5月19日
	 */
	public @ResponseBody
	String activateProcessInst(String processInstId) {
		try {
			activitiService.activateProcessInst(processInstId);
			
			return "success";
		} catch (Exception e) {
			return "fail"+e.getMessage();
		}
	}
	
	/** 获取节点配置信息  gw_tanx
	 * @param business_type
	 * @param business_id
	 * @param processKey
	 * @return
	 * 2014年6月19日
	 */
	public @ResponseBody
	Map<String, List> getConfigTempleInfo(String business_type,
			String business_id, String processKey) {
		try {

			// 节点代办配置信息
			List<ActivitiNodeCandidate> nodeConfigList = activitiConfigService
					.queryActivitiNodesCandidates(business_type, business_id,
							processKey);

			// 节点参数配置信息
			List<Nodevariable> nodeVariableList = activitiConfigService
					.queryNodeVariable(business_type, business_id,
							processKey);
			
			Map<String, List> map = new HashMap<String, List>();
			map.put("nodeConfigList", nodeConfigList);
			map.put("nodeVariableList", nodeVariableList);
			
			return map;
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
	
	/** 跳转到设置流程定义消息模板界面 gw_tanx
	 * @param processKey
	 * @param model
	 * @return
	 * 2014年6月23日
	 */
	public String toSetMessageTemplate(String processKey, ModelMap model){
		try {

			// 获取模板信息
			Map templateMap = activitiConfigService
					.queryMessageTempleById(processKey);
			
			model.addAttribute("templateMap", templateMap);
			model.addAttribute("processKey", processKey);

			return "path:setMessageTemplate";
		} catch (Exception e) {
			throw new ProcessException(e);
		}
	}
	
	/** 消息模板保存 gw_tanx
	 * @param processKey
	 * @param messagetempleid
	 * @param emailtempleid
	 * @return
	 * 2014年6月23日
	 */
	public @ResponseBody
	String saveMessageTemplate(String processKey, String messagetempleid,
			String emailtempleid, String noticeId) {
		try {

			activitiService.saveMessageType(processKey, messagetempleid,
					emailtempleid, noticeId);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}
	
	/** 打包下载流程定义xml和图片 gw_tanx
	 * @param processKey
	 * @param version
	 * 2014年6月24日
	 */
	public void downProcessXMLandPicZip(String processKey, String version,
			HttpServletResponse response) throws Exception {

		activitiService.downProcessXMLandPicZip(processKey, version, response);

	}
	
	/** 设置流程定义中，处理工时是否包含节假日 gw_tanx
	 * @param processKey
	 * @param version
	 * 2014年6月24日
	 */
	public void updateHoliday(String processKey, String IsContainHoliday)
			throws Exception {

		activitiService.addIsContainHoliday(processKey, IsContainHoliday);

	}

}









