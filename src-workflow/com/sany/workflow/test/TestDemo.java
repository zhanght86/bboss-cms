package com.sany.workflow.test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.impl.ActivitiServiceImpl;

public class TestDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//				deployment("demo1", "com/sany/workflow/test/demo1.bpmn", "");
//		checkOne();
				testinitor();
//				deployment("demo1", "com/sany/workflow/test/demo1.bpmn", "");
//				huiqianparraleltosequential();
//		huiqianparralelt2ndsequentialswitch();
//		deployment("dddd", "RefundProcess.bpmn20.xml", "");
		//deployment("fff", "com/sany/workflow/test/EndorseProcess.bpmn20.xml", "");
		
//		 deploytest();
		
	}
	
	public static void deploytest()
	{
		com.sany.workflow.test.ActivitiServiceImpl service = new com.sany.workflow.test.ActivitiServiceImpl("activiti.cfg.xml");
		try {
			service.deployProcDefByPath("测试1", "/com/sany/activiti/demo/diagrams/mms.get.zip");
			service.deployProcDefByPath("测试2", "/com/sany/activiti/demo/diagrams/diagrams1.zip");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testinitor()
	{
		ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
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

//		if (instanceList.isEmpty()) 
		{
			//            	ProcessInstance instance = activitiService.startProcDef("demo1", params);
//			ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"demo1", params);
			ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"mms", params,"user1");
			System.out.println("processDefinitionId=" + instance.getProcessDefinitionId());
			System.out.println("businessKey=" + instance.getBusinessKey());
			System.out.println("instanceId=" + instance.getProcessInstanceId());
		}
		
//		System.out.println("==============================删除流程实例");
//		for (ProcessInstance instance : instanceList) {
//			activitiService.getRuntimeService().deleteProcessInstance(instance.getId(),"");
//		}
		
		
		
		TaskService taskService = activitiService.getTaskService();
		
		System.out.println("==============================根据用户名获取待办");
		String[] users = {"user1"};
		for(String user:users){
			List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
			for (Task task : taskList1) {
				
				
				System.out.println("=========获取流程各节点审批人信息");
				Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
				System.out.println("参数："+variables);
				
				System.out.println("=========通过流程");
				
				activitiService.completeTask(task.getId(),  null);
				
//				System.out.println("=========通过流程后返回驳回节点");
				//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
				
				System.out.println("=========查询历史记录");
				//List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
				//System.out.println("历史记录："+his);
				
//				if("user1"==user){
//					System.out.println("==============================转办");
//					activitiService.transferAssignee(task.getId(),"user2");
//				}

			
			}
		}
	}
	
	
	public static void testmultiinitor()
	{
		ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
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

//		if (instanceList.isEmpty()) 
		{
			//            	ProcessInstance instance = activitiService.startProcDef("demo1", params);
//			ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"demo1", params);
			ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"mms", params,"user1");
			System.out.println("processDefinitionId=" + instance.getProcessDefinitionId());
			System.out.println("businessKey=" + instance.getBusinessKey());
			System.out.println("instanceId=" + instance.getProcessInstanceId());
		}
		
//		System.out.println("==============================删除流程实例");
//		for (ProcessInstance instance : instanceList) {
//			activitiService.getRuntimeService().deleteProcessInstance(instance.getId(),"");
//		}
		
		
		
		TaskService taskService = activitiService.getTaskService();
		
		System.out.println("==============================根据用户名获取待办");
		String[] users = {"user1"};
		for(String user:users){
			List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
			for (Task task : taskList1) {
				
				
				System.out.println("=========获取流程各节点审批人信息");
				Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
				System.out.println("参数："+variables);
				
				System.out.println("=========通过流程");
				
				activitiService.completeTask(task.getId(),  null);
				
//				System.out.println("=========通过流程后返回驳回节点");
				//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
				
				System.out.println("=========查询历史记录");
				//List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
				//System.out.println("历史记录："+his);
				
//				if("user1"==user){
//					System.out.println("==============================转办");
//					activitiService.transferAssignee(task.getId(),"user2");
//				}

			
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * 串行节点驳回测试
	 */
	public static void checkOne()
	{
		TransactionManager tm = new TransactionManager();
		try {
			//初始化引擎
			ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
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
				ProcessInstance instance = activitiService.startProcDef("test","demo1", params);
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
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.completeTask(task.getId(), user, common_params, null);
					
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
					//List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
					//System.out.println("历史记录："+his);
					
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
					
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.completeTask(task.getId(), user, common_params, null);
					
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
//					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
//					System.out.println("历史记录："+his);
					
//					if("user1"==user){
//						System.out.println("==============================转办");
//						activitiService.transferAssignee(task.getId(),"user2");
//					}

				
				}
			}
			
			String[] users1 = {"user5"};
			for(String user:users1){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					
					System.out.println("=========获取可驳回的节点");
//					List<ActivityImpl> activityList = activitiService.findBackAvtivity(task.getId());
					//System.out.println("节点列表："+activityList);
					
					System.out.println("==============================驳回任意节点");
					String toTaskKey = "usertask1";//驳回节点id
					activitiService.completeTask(task.getId(), user, common_params, toTaskKey);
//					activitiService.commitProcess(task.getId(), user, common_params, toTaskKey);
//					activitiService.rejectProcess(task.getProcessInstanceId(), toTaskKey,task.getTaskDefinitionKey(),user, new HashMap());
//				
//					System.out.println("==============================终止流程");
//					activitiService.endProcess(task.getId(),user);
				
				}
			}
			
			
			users = new String[] {"user1","user2","user22","user23","user5"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					
					
					System.out.println("=========获取可驳回的节点");
//					List<ActivityImpl> activityList = activitiService.findBackAvtivity(task.getId());
//					System.out.println("节点列表："+activityList);
					
					System.out.println("==============================驳回任意节点");
					activitiService.completeTask(task.getId(), user, common_params, null);
//					activitiService.rejectProcess(task.getProcessInstanceId(), toTaskKey,task.getTaskDefinitionKey(),user, new HashMap());
//				
//					System.out.println("==============================终止流程");
//					activitiService.endProcess(task.getId(),user);
				
				}
			}
			//检查是否有流程实例存在
			List<ProcessInstance> instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("demo1").active().list();
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
			ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
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
				ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"demo1", params);
				System.out.println("processDefinitionId=" + instance.getProcessDefinitionId());
				System.out.println("businessKey=" + instance.getBusinessKey());
				System.out.println("instanceId=" + instance.getProcessInstanceId());
			}
			if(true)
				return;
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
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.completeTask(task.getId(), user, common_params, null);
					
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
//					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
//					System.out.println("历史记录："+his);
//					
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
					
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					String toTaskKey = "usertask1";//驳回节点id
					activitiService.completeTask(task.getId(), user, common_params, null);
					if(true)
						break;
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
//					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
//					System.out.println("历史记录："+his);
					
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
					
					System.out.println("=========获取可驳回的节点");
//					List<ActivityImpl> activityList = activitiService.findBackAvtivity(task.getId());
//					System.out.println("节点列表："+activityList);
					
					System.out.println("==============================驳回任意节点");
					activitiService.completeTask(task.getId(), user, common_params, null);
//					activitiService.rejectProcess(task.getProcessInstanceId(), toTaskKey,task.getTaskDefinitionKey(),user, new HashMap());
//				
//					System.out.println("==============================终止流程");
//					activitiService.endProcess(task.getId(),user);
				
				}
			}
			//检查是否有流程实例存在
			List<ProcessInstance> instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("demo1").active().list();
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
	 * 会签并行切换为串行测试
	 */
	public static void huiqianparraleltosequential()
	{
		TransactionManager tm = new TransactionManager();
		try {
			//初始化引擎
			ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
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
			/**
			 * 会签任务usertask2流程模型中被定位并行模式，这里在流程实例启动时通过流程变量将会签任务usertask2的并行模式改为串行模式
			 * 然后该流程实例的对应会签任务usertask2将以串行方式执行
			 */
			params.put("usertask2"+MultiInstanceActivityBehavior.multiInstanceMode_variable_const, MultiInstanceActivityBehavior.multiInstanceMode_sequential);

//			if (instanceList.isEmpty()) 
			{
				//            	ProcessInstance instance = activitiService.startProcDef("demo1", params);
//				ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"demo1", params);
				ProcessInstance instance = activitiService.startProcDef("test","demo1", params);
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
					
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.completeTask(task.getId(), user, common_params, null);
					
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
//					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
//					System.out.println("历史记录："+his);
					
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
					
					
					System.out.println("=========通过流程");
					String toTaskKey = null;//驳回节点id
					activitiService.completeTask(task.getId(), user, common_params, null);
					


				
				}
			}
			
			
			
			
			users = new String[] {"user5"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					
					System.out.println("==============================驳回任意节点");
					activitiService.completeTask(task.getId(), user, common_params, null);

				
				}
			}
			//检查是否有流程实例存在
			List<ProcessInstance> instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("demo1").active().list();
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
	 * 会签并行切换为串行，驳回，串行切换为并行测试
	 */
	public static void huiqianparralelt2ndsequentialswitch()
	{
		TransactionManager tm = new TransactionManager();
		try {
			//初始化引擎
			ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
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
			/**
			 * 会签任务usertask2流程模型中被定位并行模式，这里在流程实例启动时通过流程变量将会签任务usertask2的并行模式改为串行模式
			 * 然后该流程实例的对应会签任务usertask2将以串行方式执行
			 */
			params.put("usertask2"+MultiInstanceActivityBehavior.multiInstanceMode_variable_const, MultiInstanceActivityBehavior.multiInstanceMode_sequential);

//			if (instanceList.isEmpty()) 
			{
				//            	ProcessInstance instance = activitiService.startProcDef("demo1", params);
//				ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"demo1", params);
				ProcessInstance instance = activitiService.startProcDef("test","demo1", params);
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
					
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.completeTask(task.getId(), user, common_params, null);
					
//					System.out.println("=========通过流程后返回驳回节点");
					//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
					
					System.out.println("=========查询历史记录");
//					List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
//					System.out.println("历史记录："+his);
//					
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
					System.out.println("=========通过流程");
					String toTaskKey = "usertask1";//驳回节点id，将串行会签任务驳回到usertask1环节，为测试将usertask2由串行切换为并行任务做准备
					activitiService.completeTask(task.getId(), user, common_params, toTaskKey);
				}
			}
			
			
			
			
			users = new String[] {"user1"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					//
					/**
					 * 会签任务usertask2流程模型中已经被切换为串行模式，这里在完成usertask1任务时通过流程变量将会签任务usertask2的串行模式再改为并行模式
					 * 然后该流程实例的对应会签任务usertask2将以并行方式执行
					 */
					common_params.put("usertask2"+MultiInstanceActivityBehavior.multiInstanceMode_variable_const, MultiInstanceActivityBehavior.multiInstanceMode_parallel);

					activitiService.completeTask(task.getId(), user, common_params, null);

				
				}
			}
			
			users = new String[]{"user2","user22","user23","user5"};
			for(String user:users){
				List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
				for (Task task : taskList1) {
					
					
					System.out.println("=========通过流程");
					String toTaskKey = null;//驳回节点id
					activitiService.completeTask(task.getId(), user, common_params, null);
					


				
				}
			}
			//检查是否有流程实例存在
			List<ProcessInstance> instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("demo1").active().list();
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
			ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");

//			tm.begin();

			//检查是否已发布
			System.out.println("发布信息--------------");

			Deployment deployment = activitiService.deployProcDefByPath(deploymentkey, path, imagePath,DeploymentBuilder.Deploy_policy_upgrade);
			System.out.println("deploymentId=" + deployment.getId());
			System.out.println("deploymentName=" + deployment.getName());
			//查看流程定义信息
			System.out.println("定义信息--------------");
			ProcessDef def = activitiService.getProcessDefByDeploymentId(deployment.getId());
			String psKey = def.getKEY_();
			System.out.println("processKey=" + psKey);
			System.out.println(activitiService.getProccessXMLByKey(psKey));
			

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
//			tm.commit();
			//	            tm.rollback();
			//Hibernate3Util.rollbackTransaction();
		} catch (Exception ex) {
			
		}
		finally{
//			tm.release();
		}

	}
}
