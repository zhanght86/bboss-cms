package com.sany.workflow.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.workflow.entity.ProcessDef;
//import com.sany.workflow.service.impl.ActivitiServiceImpl;

public class TestDemo2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				deployment("demo1", "com/sany/workflow/test/demo2.bpmn", "");
//		checkOne();
//				huiqianbohui();
				task1jumptotask5();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * 任务往后跳转
	 */
	public static void task1jumptotask5()
	{
		TransactionManager tm = new TransactionManager();
		try {
			//初始化引擎
			ActivitiServiceImpl activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
			//tm.begin();

			//检查是否已发布
			System.out.println("发布信息--------------");

			List<Deployment> deployments = activitiService.getRepositoryService().createDeploymentQuery().deploymentName("demo1").orderByDeploymenTime().desc().list();
			Deployment deployment = null;
			if(deployments != null && deployments.size()>0){
				deployment = deployments.get(0);
			}
			System.out.println("deploymentId=" + deployment.getId());
			System.out.println("deploymentName=" + deployment.getName());
			//查看流程定义信息
			System.out.println("定义信息--------------");
			ProcessDef def = activitiService.getProcessDefByDeploymentId(deployment.getId());
			String psKey = def.getKEY_();
			System.out.println("processKey=" + psKey);

			//查看流程节点信息
			System.out.println("流程信息--------------");
			List<ActivityImpl> activities = activitiService.getActivitImplListByProcessKey(psKey);
			for (ActivityImpl actImpl : activities) {
				String actId = (String) actImpl.getId();
				String actName = (String) actImpl.getProperty("name");
				String actType = (String) actImpl.getProperty("type");
				if (actType.equals("userTask") || actType.equals("subProcess")) {
					System.out.println("-");
					System.out.println("actId=" + actId);
					System.out.println("actName=" + actName);
					System.out.println("actType=" + actType);
					if (actType.equals("subProcess")) {
						List<ActivityImpl> _activities = actImpl.getActivities();
						for (ActivityImpl _actImpl : _activities) {
							actId = (String) _actImpl.getId();
							actName = (String) _actImpl.getProperty("name");
							actType = (String) _actImpl.getProperty("type");
							if (actType.equals("userTask") && !"allot".equals(_actImpl.getId())) {
								System.out.println("--");
								System.out.println("actId=" + actId);
								System.out.println("actName=" + actName);
								System.out.println("actType=" + actType);
							}
						}
					}
				}
			}

			

			//分支1后续会在节点4被驳回，分支2直接通过。
			Map common_params = new HashMap();
			common_params.put("next_task", 2);

			Map params = new HashMap();
			params.put("usertask1_user", Arrays.asList("user1".split(",")));
			params.put("usertask2_user", Arrays.asList("user2,user22,user23".split(",")));
			params.put("usertask3_user", Arrays.asList("user3,user32".split(",")));
			params.put("usertask4_user", Arrays.asList("user4,user42".split(",")));
			params.put("usertask5_user", Arrays.asList("user5".split(",")));
			params.put("businessType", "test");

//			if (instanceList.isEmpty()) 
			{
				//            	ProcessInstance instance = activitiService.startProcDef("demo1", params);
//				ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"demo1", params);
				ProcessInstance instance = activitiService.startProcDef("test","demo2", params);
				System.out.println("processDefinitionId=" + instance.getProcessDefinitionId());
				System.out.println("businessKey=" + instance.getBusinessKey());
				System.out.println("instanceId=" + instance.getProcessInstanceId());
			}
			
//			System.out.println("==============================删除流程实例");
//			for (ProcessInstance instance : instanceList) {
//				activitiService.getRuntimeService().deleteProcessInstance(instance.getId(),"");
//			}
			
			
			
			TaskService taskService = activitiService.getTaskService();
			
			System.out.println("==============================根据用户名获取待办");
			String[] users = {"user1"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					System.out.println(user+"-----------------------------------" + task.getTaskDefinitionKey());
					System.out.println("taskName=" + task.getName());
					System.out.println("id=" + task.getId());
					System.out.println("assignee=" + task.getAssignee());
					System.out.println("executionId=" + task.getExecutionId());
					System.out.println("owner=" + task.getOwner());
					System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
					System.out.println("definitionKey=" + task.getTaskDefinitionKey());
					System.out.println("dueDate=" + task.getDueDate());
					System.out.println("Description=" + task.getDescription());
					System.out.println("ProcessInstanceId=" + task.getProcessInstanceId());
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.commitProcess(task.getId(), user, common_params, "usertask5");
					
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
					System.out.println("历史记录："+his);
					
//					if("user1"==user){
//						System.out.println("==============================转办");
//						activitiService.transferAssignee(task.getId(),"user2");
//					}

				
				}
			}
			
			
			
			
			users = new String[] {"user5"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					System.out.println(user+"-----------------------------------" + task.getTaskDefinitionKey());
					System.out.println("taskName=" + task.getName());
					System.out.println("id=" + task.getId());
					System.out.println("assignee=" + task.getAssignee());
					System.out.println("executionId=" + task.getExecutionId());
					System.out.println("owner=" + task.getOwner());
					System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
					System.out.println("definitionKey=" + task.getTaskDefinitionKey());
					System.out.println("dueDate=" + task.getDueDate());
					System.out.println("Description=" + task.getDescription());
					System.out.println("ProcessInstanceId=" + task.getProcessInstanceId());
					
					System.out.println("=========获取可驳回的节点");
					List<ActivityImpl> activityList = activitiService.findBackAvtivity(task.getId());
					System.out.println("节点列表："+activityList);
					
					System.out.println("==============================驳回任意节点");
					activitiService.commitProcess(task.getId(), user, common_params, null);
//					activitiService.rejectProcess(task.getProcessInstanceId(), toTaskKey,task.getTaskDefinitionKey(),user, new HashMap());
//				
//					System.out.println("==============================终止流程");
//					activitiService.endProcess(task.getId(),user);
				
				}
			}
			//检查是否有流程实例存在
			List<ProcessInstance> instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("demo2").active().list();
			System.out.println("==============================获取实例的待办");
			for (ProcessInstance instance : instanceList) {
				HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
				System.out.println("instanceId=" + instance.getId());
				System.out.println("businessKey=" + instance.getBusinessKey());
				System.out.println("instanceEnded=" + (hisInstance.getEndTime() != null));
				for (Task task : taskList) {
					System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
					System.out.println("taskName=" + task.getName());
					System.out.println("id=" + task.getId());
					System.out.println("assignee=" + task.getAssignee());
					System.out.println("executionId=" + task.getExecutionId());
					System.out.println("owner=" + task.getOwner());
					System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
					System.out.println("definitionKey=" + task.getTaskDefinitionKey());
					System.out.println("dueDate=" + task.getDueDate());
				}
				
				
				if(hisInstance!=null){
					System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
				}
			}

			//tm.commit();
			//	            tm.rollback();
			//Hibernate3Util.rollbackTransaction();
		} catch (Exception ex) {
			
		}
		finally
		{
			//tm.releasenolog();
		}
	}
	/**
	 * 会签驳回测试
	 */
	public static void huiqianbohui()
	{
		TransactionManager tm = new TransactionManager();
		try {
			//初始化引擎
			ActivitiServiceImpl activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
			//tm.begin();

			//检查是否已发布
			System.out.println("发布信息--------------");

			List<Deployment> deployments = activitiService.getRepositoryService().createDeploymentQuery().deploymentName("demo1").orderByDeploymenTime().desc().list();
			Deployment deployment = null;
			if(deployments != null && deployments.size()>0){
				deployment = deployments.get(0);
			}
			System.out.println("deploymentId=" + deployment.getId());
			System.out.println("deploymentName=" + deployment.getName());
			//查看流程定义信息
			System.out.println("定义信息--------------");
			ProcessDef def = activitiService.getProcessDefByDeploymentId(deployment.getId());
			String psKey = def.getKEY_();
			System.out.println("processKey=" + psKey);

			//查看流程节点信息
			System.out.println("流程信息--------------");
			List<ActivityImpl> activities = activitiService.getActivitImplListByProcessKey(psKey);
			for (ActivityImpl actImpl : activities) {
				String actId = (String) actImpl.getId();
				String actName = (String) actImpl.getProperty("name");
				String actType = (String) actImpl.getProperty("type");
				if (actType.equals("userTask") || actType.equals("subProcess")) {
					System.out.println("-");
					System.out.println("actId=" + actId);
					System.out.println("actName=" + actName);
					System.out.println("actType=" + actType);
					if (actType.equals("subProcess")) {
						List<ActivityImpl> _activities = actImpl.getActivities();
						for (ActivityImpl _actImpl : _activities) {
							actId = (String) _actImpl.getId();
							actName = (String) _actImpl.getProperty("name");
							actType = (String) _actImpl.getProperty("type");
							if (actType.equals("userTask") && !"allot".equals(_actImpl.getId())) {
								System.out.println("--");
								System.out.println("actId=" + actId);
								System.out.println("actName=" + actName);
								System.out.println("actType=" + actType);
							}
						}
					}
				}
			}

			

			//分支1后续会在节点4被驳回，分支2直接通过。
			Map common_params = new HashMap();
			common_params.put("next_task", 2);

			Map params = new HashMap();
			params.put("usertask1_user", Arrays.asList("user1".split(",")));
			params.put("usertask2_user", Arrays.asList("user2,user22,user23".split(",")));
			params.put("usertask3_user", Arrays.asList("user3,user32".split(",")));
			params.put("usertask4_user", Arrays.asList("user4,user42".split(",")));
			params.put("usertask5_user", Arrays.asList("user5".split(",")));
			params.put("businessType", "test");

//			if (instanceList.isEmpty()) 
			{
				//            	ProcessInstance instance = activitiService.startProcDef("demo1", params);
//				ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"demo1", params);
				ProcessInstance instance = activitiService.startProcDef("test","demo2", params);
				System.out.println("processDefinitionId=" + instance.getProcessDefinitionId());
				System.out.println("businessKey=" + instance.getBusinessKey());
				System.out.println("instanceId=" + instance.getProcessInstanceId());
			}
			
//			System.out.println("==============================删除流程实例");
//			for (ProcessInstance instance : instanceList) {
//				activitiService.getRuntimeService().deleteProcessInstance(instance.getId(),"");
//			}
			
			
			
			TaskService taskService = activitiService.getTaskService();
			
			System.out.println("==============================根据用户名获取待办");
			String[] users = {"user1"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					System.out.println(user+"-----------------------------------" + task.getTaskDefinitionKey());
					System.out.println("taskName=" + task.getName());
					System.out.println("id=" + task.getId());
					System.out.println("assignee=" + task.getAssignee());
					System.out.println("executionId=" + task.getExecutionId());
					System.out.println("owner=" + task.getOwner());
					System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
					System.out.println("definitionKey=" + task.getTaskDefinitionKey());
					System.out.println("dueDate=" + task.getDueDate());
					System.out.println("Description=" + task.getDescription());
					System.out.println("ProcessInstanceId=" + task.getProcessInstanceId());
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.commitProcess(task.getId(), user, common_params, null);
					
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
					System.out.println("历史记录："+his);
					
//					if("user1"==user){
//						System.out.println("==============================转办");
//						activitiService.transferAssignee(task.getId(),"user2");
//					}

				
				}
			}
			
			users = new String[]{"user2","user22","user23"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					System.out.println(user+"-----------------------------------" + task.getTaskDefinitionKey());
					System.out.println("taskName=" + task.getName());
					System.out.println("id=" + task.getId());
					System.out.println("assignee=" + task.getAssignee());
					System.out.println("executionId=" + task.getExecutionId());
					System.out.println("owner=" + task.getOwner());
					System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
					System.out.println("definitionKey=" + task.getTaskDefinitionKey());
					System.out.println("dueDate=" + task.getDueDate());
					System.out.println("Description=" + task.getDescription());
					System.out.println("ProcessInstanceId=" + task.getProcessInstanceId());
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					String toTaskKey = "usertask1";//驳回节点id
					activitiService.commitProcess(task.getId(), user, common_params, toTaskKey);
					if(true)
						break;
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
					System.out.println("历史记录："+his);
					
//					if("user1"==user){
//						System.out.println("==============================转办");
//						activitiService.transferAssignee(task.getId(),"user2");
//					}

				
				}
			}
			
			
			
			
			users = new String[] {"user1","user2","user22","user23","user5"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					System.out.println(user+"-----------------------------------" + task.getTaskDefinitionKey());
					System.out.println("taskName=" + task.getName());
					System.out.println("id=" + task.getId());
					System.out.println("assignee=" + task.getAssignee());
					System.out.println("executionId=" + task.getExecutionId());
					System.out.println("owner=" + task.getOwner());
					System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
					System.out.println("definitionKey=" + task.getTaskDefinitionKey());
					System.out.println("dueDate=" + task.getDueDate());
					System.out.println("Description=" + task.getDescription());
					System.out.println("ProcessInstanceId=" + task.getProcessInstanceId());
					
					System.out.println("=========获取可驳回的节点");
					List<ActivityImpl> activityList = activitiService.findBackAvtivity(task.getId());
					System.out.println("节点列表："+activityList);
					
					System.out.println("==============================驳回任意节点");
					activitiService.commitProcess(task.getId(), user, common_params, null);
//					activitiService.rejectProcess(task.getProcessInstanceId(), toTaskKey,task.getTaskDefinitionKey(),user, new HashMap());
//				
//					System.out.println("==============================终止流程");
//					activitiService.endProcess(task.getId(),user);
				
				}
			}
			//检查是否有流程实例存在
			List<ProcessInstance> instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("demo2").active().list();
			System.out.println("==============================获取实例的待办");
			for (ProcessInstance instance : instanceList) {
				HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
				List<Task> taskList = taskService.createTaskQuery().processInstanceId(instance.getId()).list();
				System.out.println("instanceId=" + instance.getId());
				System.out.println("businessKey=" + instance.getBusinessKey());
				System.out.println("instanceEnded=" + (hisInstance.getEndTime() != null));
				for (Task task : taskList) {
					System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
					System.out.println("taskName=" + task.getName());
					System.out.println("id=" + task.getId());
					System.out.println("assignee=" + task.getAssignee());
					System.out.println("executionId=" + task.getExecutionId());
					System.out.println("owner=" + task.getOwner());
					System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
					System.out.println("definitionKey=" + task.getTaskDefinitionKey());
					System.out.println("dueDate=" + task.getDueDate());
				}
				
				
				if(hisInstance!=null){
					System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
				}
			}

			//tm.commit();
			//	            tm.rollback();
			//Hibernate3Util.rollbackTransaction();
		} catch (Exception ex) {
			
		}
		finally
		{
			//tm.releasenolog();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void deployment(String deploymentkey,String path,String imagePath)
	{
		TransactionManager tm = new TransactionManager();

		try {

			//初始化引擎
			ActivitiServiceImpl activitiService = new ActivitiServiceImpl("activiti.cfg.xml");

			tm.begin();

			//检查是否已发布
			System.out.println("发布信息--------------");

			Deployment deployment = activitiService.deployProcDefByPath(deploymentkey, path, imagePath);
			System.out.println("deploymentId=" + deployment.getId());
			System.out.println("deploymentName=" + deployment.getName());
			//查看流程定义信息
			System.out.println("定义信息--------------");
			ProcessDef def = activitiService.getProcessDefByDeploymentId(deployment.getId());
			String psKey = def.getKEY_();
			System.out.println("processKey=" + psKey);

			//查看流程节点信息
			System.out.println("流程信息--------------");
			List<ActivityImpl> activities = activitiService.getActivitImplListByProcessKey(psKey);
			for (ActivityImpl actImpl : activities) {
				String actId = (String) actImpl.getId();
				String actName = (String) actImpl.getProperty("name");
				String actType = (String) actImpl.getProperty("type");
				if (actType.equals("userTask") || actType.equals("subProcess")) {
					System.out.println("-");
					System.out.println("actId=" + actId);
					System.out.println("actName=" + actName);
					System.out.println("actType=" + actType);
					if (actType.equals("subProcess")) {
						List<ActivityImpl> _activities = actImpl.getActivities();
						for (ActivityImpl _actImpl : _activities) {
							actId = (String) _actImpl.getId();
							actName = (String) _actImpl.getProperty("name");
							actType = (String) _actImpl.getProperty("type");
							if (actType.equals("userTask") && !"allot".equals(_actImpl.getId())) {
								System.out.println("--");
								System.out.println("actId=" + actId);
								System.out.println("actName=" + actName);
								System.out.println("actType=" + actType);
							}
						}
					}
				}
			}
			tm.commit();
			//	            tm.rollback();
			//Hibernate3Util.rollbackTransaction();
		} catch (Exception ex) {
			
		}
		finally{
			tm.release();
		}

	}
}
