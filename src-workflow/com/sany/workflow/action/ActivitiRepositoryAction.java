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
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.frameworkset.spi.SOAApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.servlet.mvc.MultiActionController;

import com.frameworkset.util.ListInfo;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.entity.ProcessDefCondition;
import com.sany.workflow.entity.ProcessDeployment;
import com.sany.workflow.service.ActivitiConfigService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.util.WorkFlowConstant;

/**
 * @author yinbp
 * @since 2012-3-22 下午6:03:09
 */
public class ActivitiRepositoryAction extends MultiActionController {

	private static final String FILE_TYPE_ZIP = "zip";

	private ActivitiService activitiService;
	
	private ActivitiConfigService activitiConfigService;
	
	//private ActivitiTestService activitiTestService;
	
	/**
	 * 部署流程
	 * 
	 * @param processDeployment
	 * @return String
	 */
	public @ResponseBody
	String deployProcess(ProcessDeployment processDeployment,String needConfig) {
		String result="success";
		try {
			Deployment deployment = null;
			if (processDeployment.getProcessDef().getContentType().contains(FILE_TYPE_ZIP)) {
				deployment = activitiService.deployProcDefByZip(processDeployment.getNAME_(), new ZipInputStream(processDeployment
						.getProcessDef().getInputStream()));
			} else {
				deployment = activitiService.deployProcDefByInputStream(processDeployment.getNAME_(), processDeployment
						.getProcessDef().getOriginalFilename(), processDeployment.getProcessDef().getInputStream());
			}
			if(deployment!=null){
				ProcessDef pd = activitiService.getProcessDefByDeploymentId(deployment.getId());
				if(needConfig.equals("1")){
					activitiConfigService.addActivitiNodeInfo(activitiService.getPorcessKeyByDeployMentId(deployment.getId()));
				}
				if(!processDeployment.getParamFile().isEmpty()){
					result = activitiConfigService.addNodeParams(processDeployment.getParamFile().getInputStream(),pd.getKEY_());
				}
				if(pd!=null){
					activitiConfigService.addProBusinessType(pd.getKEY_(), processDeployment.getBusinessTypeId());
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
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
			ProcessDefCondition processDefCondition, ModelMap model) {
		try {
			// 获取流程部署信息集合
			ListInfo listInfo = activitiService.queryProcessDefs(offset,
					pagesize, processDefCondition);
			if(listInfo!=null){
				List<ProcessDef> list = listInfo.getDatas();
				for (int i = 0; list!=null&&i < list.size(); i++) {
					list.get(i).setBusiness_name(
							activitiConfigService.queryBusinessName(list.get(i)
									.getKEY_()));
				}
				listInfo.setDatas(list);
			}
			
			model.addAttribute("processDefs", listInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "path:queryProcessDefs";
	}

	public String queryProcessHisVer(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "resourceName") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			ProcessDefCondition processDefCondition, ModelMap model) {
		try {
			ListInfo listInfo = activitiService.queryProcessDefs(offset,
					pagesize, processDefCondition);
			model.addAttribute("processDefs", listInfo);
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
	String deleteDeploymentCascade(String[] processDeploymentids, boolean[] cascades) {
		// processDef.getProcessDef();
		try {
			activitiService.deleteDeploymentAllVersions(processDeploymentids);

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

	public String viewProcessInfo(String deploymentId, ModelMap model) {
		model.addAttribute("processDef", activitiService.getProcessDefByDeploymentId(deploymentId));
		List<ActivityImpl> aList = activitiService
				.getActivitImplListByProcessKey(activitiService.getPorcessKeyByDeployMentId(deploymentId));
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

	public String index() {
		return "path:index";
	}

	public void getProccessPic(String processId, HttpServletResponse response) throws IOException {
		if(processId!=null&&!processId.equals("")){
			OutputStream out = response.getOutputStream();
			activitiService.getProccessPic(processId, out);
		}
	}
	
	public String getProccessXML(String processId) throws IOException {
		if(processId!=null&&!processId.equals("")){
			
			return activitiService.getProccessXML(processId);
		}
		return null;
	}
}
