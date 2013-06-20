/**
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.sany.workflow.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.sany.workflow.service.ActivitiService;

/**
 * <p>Title: TestSubprocess.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-6-18
 * @author biaoping.yin
 * @version 1.0
 */
public class TestSubprocess {
	public static void main(String[] args)
	{
		ActivitiService activitiService = new com.sany.workflow.service.impl.ActivitiServiceImpl("activiti.cfg.xml");

//		tm.begin();

		//检查是否已发布
		System.out.println("发布信息--------------");

		Deployment deployment = activitiService.deployProcDefByPath("subprocess", "com/sany/workflow/test/timestartdemo.bpmn", null,DeploymentBuilder.Deploy_policy_upgrade);
		
		Map params = new HashMap();
		params.put("assigneeList", Arrays.asList("user1,user2,user22,user23".split(",")));
//		params.put("usertask2_user", Arrays.asList("user2,user22,user23".split(",")));
//		params.put("usertask3_user", Arrays.asList("user3,user32".split(",")));
//		params.put("usertask4_user", Arrays.asList("user4,user42".split(",")));
//		params.put("usertask5_user", Arrays.asList("user5".split(",")));
		params.put("businessType", "test");
		ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"timedemo1", params);
		
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
				
				activitiService.completeTaskByUser(task.getId(), user);
				
//				System.out.println("=========通过流程后返回驳回节点");
				//activitiService.commitProcess(task.getId(), user, new HashMap(), "usertask3");
				
				System.out.println("=========查询历史记录");
				List his = activitiService.findHistoricUserTask(task.getProcessInstanceId());
				System.out.println("历史记录："+his);
				
//				if("user1"==user){
//					System.out.println("==============================转办");
//					activitiService.transferAssignee(task.getId(),"user2");
//				}

			
			}
		}
	}

}
