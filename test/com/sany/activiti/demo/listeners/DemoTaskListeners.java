package com.sany.activiti.demo.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.sany.activiti.demo.pojo.ActivitiNode;
import com.sany.activiti.demo.service.ActivitiTestService;
import com.sany.activiti.demo.service.impl.ActivitiTestServiceImpl;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.impl.ActivitiServiceImpl;


public class DemoTaskListeners implements TaskListener {

	private ActivitiService activitiService ;
	
	private ActivitiTestService activitiTestService;
	
	public void notify(DelegateTask delegateTask) {
//		activitiService = new ActivitiServiceImpl("activiti.cfg.xml");
//		activitiTestService = new ActivitiTestServiceImpl();
//		activitiTestService.setExecutor(new ConfigSQLExecutor("com/sany/activiti/demo/demo.xml"));
//		String key = activitiService.getProcessDefinitionById(delegateTask.getProcessDefinitionId()).getKey();
//		ActivitiNode node = activitiTestService.getActivitiNodeByKeyAndNodeId(key, delegateTask.getTaskDefinitionKey());
		//delegateTask.ad
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+delegateTask.getTaskDefinitionKey());
	}

}
