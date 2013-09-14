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

		params.put("businessType", "test");
		ProcessInstance instance = activitiService.startProcDef(UUID.randomUUID().toString(),"timedemo1", params);
		
		String[] users = {"user1"};
		for(String user:users){
			
			List<Task> taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
			do
			{
				for (Task task : taskList1) {
					System.out.println(user+"-----------------------------------" + task.getTaskDefinitionKey());
					
					
					System.out.println("=========获取流程各节点审批人信息");
					Map variables = activitiService.getRuntimeService().getVariables(task.getProcessInstanceId(), new ArrayList<String>());
					System.out.println("参数："+variables);
					
					System.out.println("=========通过流程");
					
					activitiService.completeTaskByUser(task.getId(), user);
					


				}
				taskList1 = activitiService.getTaskService().createTaskQuery().taskCandidateUser(user).list();
			}while(taskList1 != null && taskList1.size() > 0);
		}
	}

}
