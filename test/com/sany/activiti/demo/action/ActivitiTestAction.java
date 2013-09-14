package com.sany.activiti.demo.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.ListInfo;
import com.ibm.icu.text.SimpleDateFormat;
import com.sany.activiti.demo.pojo.ActivitiNode;
import com.sany.activiti.demo.pojo.MaterielTest;
import com.sany.activiti.demo.pojo.TaskInfo;
import com.sany.activiti.demo.service.ActivitiTestService;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.util.WorkFlowConstant;

public class ActivitiTestAction {
	
	private final static  String PROCESS_KEY = "Demo";
	
	private ActivitiTestService activitiTestService;
	
	private ActivitiService activitiService ;

	/**
	 * 跳转到物料申请页面
	 * @param model
	 * @return
	 */
	public String toApplyPage(ModelMap model){
		//SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		MaterielTest object = new MaterielTest();
		object.setApply_time(new Timestamp(System.currentTimeMillis()));
		model.addAttribute("object", object);
		return "path:task_apply";
	}
	
	/**
	 * 发起物料申请单，发起流程，并完成申请环节
	 * @param materiel
	 * @return
	 */
	public String apply(MaterielTest materiel) {
		
		//List<ActivitiNode> nodeList = activitiTestService.showNodeList(PROCESS_KEY);
//		for(ActivitiNode node:nodeList){
//			Map<String,List<String>> childMap = new HashMap<String,List<String>>();
//			if(node.getCandidate_users_id()==null||node.getCandidate_users_id().equals("")){
//				node.setCandidate_users_id(UUID.randomUUID().toString());
//			}
//			if(node.getCandidate_groups_id()==null||node.getCandidate_groups_id().equals("")){
//				node.setCandidate_groups_id(UUID.randomUUID().toString());
//			}
//			childMap.put("candidateUsers",  Arrays.asList(node.getCandidate_users_id().split(",")));
//			childMap.put("candidateGroups", Arrays.asList(node.getCandidate_groups_id()));
//			map.put(node.getNode_id(), childMap);
//		}

		//map.put("applyusers", materiel.getApply_name());
		ProcessInstance processInstance = null;
		TransactionManager tm = new TransactionManager();  
		try
		{
//			tm.begin();
//			List list = new ArrayList();
//			list.add("admin");
			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("usertask6_users", list);
		
//			processInstance = activitiService.startProcDefLoadCandidate(map, PROCESS_KEY,"50020021",WorkFlowConstant.BUSINESS_TYPE_ORG,materiel.getApply_name());
			processInstance = activitiService.startProcDefLoadCandidate(map, PROCESS_KEY,"",WorkFlowConstant.BUSINESS_TYPE_COMMON,materiel.getApply_name());
			materiel.setProcess_instance_id(processInstance.getId());
			activitiTestService.applyMateriel(materiel);
		
			List<Task> taskList = activitiService.listTaskByUser(PROCESS_KEY, materiel
					.getApply_name());
			map = new HashMap<String, Object>();
			map.put("isPass", true);
			for (int i = 0; i < taskList.size(); i++) {
				if(taskList.get(i).getProcessInstanceId().equals(processInstance.getId())){
					activitiService.completeTask(taskList.get(i).getId(),
							materiel.getApply_name(), map);
				}
			}
//			tm.commit();
		}
		catch(Throwable e)
		{
			
			e.printStackTrace();
		}
		finally
		{
			tm.release();
		}
		return "path:task_userlist";
	}
	
	public String showApplyListByUser(String username,ModelMap model,@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize){
		List<MaterielTest> mList = activitiTestService.queryMaterielListByUser(username);
		List<MaterielTest> materielTestList = new ArrayList<MaterielTest>();
		for(MaterielTest materiel:mList){
			List<Execution> executionList = activitiService.listExecutionByProId(materiel.getProcess_instance_id());
			for(Execution execution :executionList){
				List<Task> taskList = activitiService.listTaskByExecId(execution.getId());
				for(Task task:taskList){
					MaterielTest object = materiel;
					ProcessDefinition processDefinition = activitiService.getProcessDefinitionById(task.getProcessDefinitionId());
					Deployment deployment = activitiService.getDeploymentById(processDefinition.getDeploymentId());
					object.setProcess_name(deployment.getName());
					object.setTask_id(task.getId());
					object.setTask_name(task.getName());
					materielTestList.add(object);
				}
			}
			
		}
		ListInfo listinfo = com.frameworkset.common.tag.pager.DataInfoImpl.pagerList(mList, (int)offset, pagesize);
		model.addAttribute("taskList", listinfo);
		model.addAttribute("username", username);
		model.addAttribute("isDetail", "true");
		return "path:task_tasklist";
	}
	
	/**
	 * 根据流程key获取流程的最新版本流程图
	 * @param process_key
	 * @param response
	 * @throws IOException
	 */
	public void getPic(String process_key,HttpServletResponse response) throws IOException
	{
		this.activitiService.getProccessPicByProcessKey(process_key, response.getOutputStream());
	}
	/**
	 * 根据流程定义id获取对应版本的流程图
	 * @param process_defid
	 * @param response
	 * @throws IOException
	 */
	public void getPicByProceccDefid(String process_defid,HttpServletResponse response) throws IOException
	{
		this.activitiService.getProccessPic(process_defid, response.getOutputStream());
	}
	
	/**
	 * 查询用户待办任务
	 * @param username
	 * @param model
	 * @param offset
	 * @param pagesize
	 * @return
	 */
	public String showTaskListByUser(String username,ModelMap model,@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize){
		try{
			if(username != null)
			{
				List<Task> taskList = activitiService.listTaskByUser(username);
				
				List<MaterielTest> mList = new ArrayList<MaterielTest>();
				for(int i=0;i<taskList.size();i++){
					MaterielTest object = activitiTestService.queryMaterielTestByProcessId(taskList.get(i).getProcessInstanceId());
					if(object!=null){
						ProcessDefinition processDefinition = activitiService.getProcessDefinitionById(taskList.get(i).getProcessDefinitionId());
						Deployment deployment = activitiService.getDeploymentById(processDefinition.getDeploymentId());
						object.setProcess_name(deployment.getName());
						object.setTask_id(taskList.get(i).getId());
						object.setTask_name(taskList.get(i).getName());
						mList.add(object);
					}
					
				}
				ListInfo listinfo = com.frameworkset.common.tag.pager.DataInfoImpl.pagerList(mList, (int)offset, pagesize);
				model.addAttribute("taskList", listinfo);
				model.addAttribute("username", username);
				model.addAttribute("isDetail", "false");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "path:task_tasklist";
	}
	
	public String showHisTaskList(String username,ModelMap model,@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize){
		if(username==null){
			username="";
		}
		List<HistoricTaskInstance> taskInstanceList = activitiService.getHisTaskByUsername(username);
		List<MaterielTest> mList = new ArrayList<MaterielTest>();
		for(HistoricTaskInstance taskInstance:taskInstanceList){
			MaterielTest object = activitiTestService.queryMaterielTestByProcessId(taskInstance.getProcessInstanceId());
			if(object!=null){
				ProcessDefinition processDefinition = activitiService.getProcessDefinitionById(taskInstance.getProcessDefinitionId());
				ProcessInstance processInstance = activitiService.getProcessInstanceById(taskInstance.getProcessInstanceId());
				Deployment deployment = activitiService.getDeploymentById(processDefinition.getDeploymentId());
				object.setProcess_name(deployment.getName());
				object.setTask_id(taskInstance.getId());
				object.setTask_name(taskInstance.getName());
				if(processInstance==null){
					object.setAct_name("流程完成");
				}else{
					List<Task> taskList = activitiService.listTaskByExecId(taskInstance.getExecutionId());
					object.setAct_name(taskList.get(0).getName());
				}
				mList.add(object);
			}
		}
		ListInfo listinfo = com.frameworkset.common.tag.pager.DataInfoImpl.pagerList(mList, (int)offset, pagesize);
		model.addAttribute("taskList", listinfo);
		model.addAttribute("isDetail", "true");
		return "path:his_tasklist";
	}
	
	/**
	 * 跳转到任务处理页面
	 * @param taskId
	 * @param username
	 * @param model
	 * @return
	 */
	public String toOperateTask(String taskId,String username,String isDetail,ModelMap model){
		Task task = activitiService.getTaskById(taskId);		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		MaterielTest object = activitiTestService.queryMaterielTestByProcessId(task.getProcessInstanceId());
		ActivityImpl activityImpl = activitiService.getActivityImplByDefId(task.getTaskDefinitionKey(),task.getProcessDefinitionId());
		List<TaskInfo> taskInfoList = activitiTestService.queryTaskInfoByProcessId(task.getProcessInstanceId());
		
		model.addAttribute("coordinateObj", activityImpl);
		model.addAttribute("object", object);
		model.addAttribute("dealTime", df.format(new Date()));
		model.addAttribute("task", task);
		model.addAttribute("username", username);
		model.addAttribute("taskInfoList", taskInfoList);
		model.addAttribute("isDetail", isDetail);
		model.addAttribute("process_defid", activityImpl.getProcessDefinition().getId());
		return "path:task_main";
	}
	
	
	
	public String toHisTask(String taskId,String isDetail,ModelMap model){
		HistoricTaskInstance hisTask = activitiService.getHisTaskById(taskId);
		
		ActivityImpl activityImpl = activitiService.getActivityImplByDefId(hisTask.getTaskDefinitionKey(),hisTask.getProcessDefinitionId());
		MaterielTest object = activitiTestService.queryMaterielTestByProcessId(hisTask.getProcessInstanceId());
		List<TaskInfo> taskInfoList = activitiTestService.queryTaskInfoByProcessId(hisTask.getProcessInstanceId());
		
			model.addAttribute("coordinateObj", activityImpl);
			model.addAttribute("process_defid", activityImpl.getProcessDefinition().getId());
		
		model.addAttribute("object", object);
		model.addAttribute("taskInfoList", taskInfoList);
		model.addAttribute("isDetail", isDetail);
		return "path:task_main";
	}

	/**
	 * 处理任务
	 * @param taskInfo
	 * @return
	 */
	public String operateTask(TaskInfo taskInfo){
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			activitiTestService.insertTaskInfo(taskInfo);
			Map<String, Object> map = new HashMap<String, Object> ();
			map.put("isPass", taskInfo.getIs_pass());
			activitiService.completeTask(String.valueOf(taskInfo.getTask_id()),taskInfo.getDeal_user(), map);
			tm.commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			tm.release();
		}
		
		return "path:task_userlist";
	}
	
	public String toUserListPage(){
		return "path:task_userlist";
	}
	/**
	 * 签收任务
	 * @param taskId
	 * @param user
	 * @return
	 */
	public @ResponseBody(datatype="json") String claimTask(String taskId,String user){
		activitiService.claim(taskId, user);
		return "success";
	}
	
	/**
	 * 转派任务
	 * @param taskId
	 * @param user
	 * @return
	 */
	public @ResponseBody(datatype="json") String delegateTask(String taskId,String user){
		activitiService.delegateTask(taskId, user);
		return "success";
	}
	
	/**
	 * 跳转到转派任务用户选择页面
	 * @param taskId
	 * @param model
	 * @return
	 */
	public String toDelegateTask(String taskId,ModelMap model){
		model.addAttribute("taskId", taskId);
		return "path:chooseuser";
	}
	
	/**
	 * 节点配置列表
	 * @param processKey
	 * @return
	 */
	public String showConfigActivitiNodeList(String processKey,ModelMap model){
		List<ActivitiNode> nodeList = activitiTestService.showNodeList(processKey);
		model.addAttribute("nodelist", nodeList);
		return "path:nodelist";
	}
	
	public @ResponseBody(datatype="json") String editActivitiNode(ActivitiNode node){
		node.setCreate_date(new Timestamp(System.currentTimeMillis()));
		node.setCreate_person_id("admin");
		node.setCreate_person_name("admin");
		activitiTestService.updateActivitiNode(node);
		return "success";
	}
	
	public String showActivitiNodeEdit(String nodeId,String usernames,String groups,ModelMap model){
		ActivitiNode nodeInfo = activitiTestService.getActivitiNodeById(nodeId);
		if(usernames!=null&&!usernames.equals("")){
			nodeInfo.setCandidate_users_id(usernames);
			nodeInfo.setCandidate_users_name(usernames);
		}
		if(groups!=null&&!groups.equals("")){
			nodeInfo.setCandidate_groups_id(groups);
			nodeInfo.setCandidate_groups_name(groups);
		}
		model.addAttribute("object", nodeInfo);
		return "path:nodeinfo";
	}
	
	public String showActivitiNodeInfo(String processKey,String taskKey,ModelMap model){
		ActivitiNode nodeInfo = activitiTestService.getActivitiNodeByKeys(processKey,taskKey);
		model.addAttribute("object", nodeInfo);
		return "path:nodeinfo";
	}
	
	public String toChooseUserPage(String users,String nodeinfoId,ModelMap model){
		model.addAttribute("usernames", users);
		model.addAttribute("nodeinfoId", nodeinfoId);
		return "path:chooseusers";
	}
	
	public String toChooseGroupPage(String groups,String nodeinfoId,ModelMap model){
		model.addAttribute("groups",groups);
		model.addAttribute("nodeinfoId",nodeinfoId);
		return "path:choosegroups";
	}
	
	public String queryCandidateUserByNames(String usernames,ModelMap model,@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "5") int pagesize){
		if(usernames!=null&&!usernames.equals("")){
			ListInfo list = com.frameworkset.common.tag.pager.DataInfoImpl.pagerList(activitiTestService.getUserInfoByNames(usernames), (int)offset, pagesize);
			model.addAttribute("userlist", list);
		}
		return "path:chooseuserlist";
	}
	
	public String queryCandidateGroupByGroups(String groups,ModelMap model,@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "5") int pagesize){
		if(groups!=null&&!groups.equals("")){
			ListInfo list = com.frameworkset.common.tag.pager.DataInfoImpl.pagerList(activitiTestService.getGroupInfoByNames(groups), (int)offset, pagesize);
			model.addAttribute("grouplist", list);
		}
		return "path:choosegroupslist";
	}
	
	
	public ActivitiTestService getActivitiTestService() {
		return activitiTestService;
	}

	public void setActivitiTestService(ActivitiTestService activitiTestService) {
		this.activitiTestService = activitiTestService;
	}

	public ActivitiService getActivitiService() {
		return activitiService;
	}

	public void setActivitiService(ActivitiService activitiService) {
		this.activitiService = activitiService;
	}
	
	
}
