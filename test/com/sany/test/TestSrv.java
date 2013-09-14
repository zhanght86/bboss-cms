/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sany.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.workflow.entity.ProcessDef;
import com.sany.workflow.service.ActivitiService;
import com.sany.workflow.service.impl.ActivitiServiceImpl;

/**
 *
 * @author gw_yuel
 */
public class TestSrv {

	@org.junit.Test
	public void test()
	{
		String dirq = "F:\\workspace\\SanyIPP\\WebRoot\\cms\\siteResource\\sanyIPP\\_template\\reportDetail.html";
		String dirb = "F:\\workspace\\SanyIPP\\WebRoot\\cms\\siteResource\\sanyIPP\\_template\\reportsList.html";
		
		java.io.File f = new java.io.File(dirq);
		java.io.File f1 = new java.io.File(dirb);
		System.out.println(f.lastModified() );
		System.out.println(f1.lastModified() );
		System.out.println(f.lastModified() == f1.lastModified());
	}
    /**
     *
     */
    public static void main(String[] args) {
        checkTwo();
    }

    public static void checkTwo()
    {
//    		throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
//    	Class c = null;
//    	Constructor cc = c.getConstructor(int.class,String.class);
//    	Object o = cc.newInstance(0,"1");
    	
        TransactionManager tm = new TransactionManager();

        //JDBCPool

        try {

            //业务对象列表
            Map<String, List> bos = new HashMap();
            bos.put("bo_1", Arrays.asList("no_1", "no_2", "no_3"));

            //业务与流程实例关联表
            List<Map> bo_no_ins = new ArrayList();

            //初始化引擎

            ActivitiService activitiService = new ActivitiServiceImpl("activiti.cfg.xml");

            //Hibernate3Util.beginTransaction();
            tm.begin(TransactionType.RW_TRANSACTION);

            //测试hibernate数据源
            //new TppNodeUserSrv().test();

            //检查是否已发布
            System.out.println("发布信息--------------");
            Deployment deployment = activitiService.getRepositoryService().createDeploymentQuery().deploymentName("临时计划带子流程").singleResult();
            if (deployment == null) {
                deployment = activitiService.deployProcDefByPath("临时计划带子流程", "com/sany/test/TempPlanDiagram3.bpmn", "");
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

            //检查是否有流程实例存在
            List<ProcessInstance> instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("TempPlanDiagram").active().list();

            Map common_params = new HashMap();
            common_params.put("passed", "true");

            Map params = new HashMap();
            params.put("request", "requestUser");
            params.put("check_1_user", "check1User");
            params.put("check_2_user", "check2User");
            params.put("check_3_user", "check3User");
            params.put("assign_4_user", "swjhy");
            params.put("allot_user", "allot");
            params.put("back_user", "back");
            params.putAll(common_params);

            if (instanceList.isEmpty()) {
                activitiService.getIdentityService().setAuthenticatedUserId("requestUser");
                Set<Map.Entry<String, List>> sets = bos.entrySet();
                Iterator<Map.Entry<String, List>> it = sets.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List> entry = it.next();
                    String key = entry.getKey();
                    ProcessInstance instance = activitiService.startProcDef(key, "TempPlanDiagram", params);
                    System.out.println("processDefinitionId=" + instance.getProcessDefinitionId());
                    System.out.println("businessKey=" + instance.getBusinessKey());
                    System.out.println("instanceId=" + instance.getProcessInstanceId());
                }
            }

            instanceList = activitiService.getRuntimeService().createProcessInstanceQuery().processDefinitionKey("TempPlanDiagram").list();


            System.out.println("=====================================requestUser");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList = taskService.createTaskQuery().taskAssignee("requestUser").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
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
                    taskService.complete(task.getId(), common_params);
                }
                HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            System.out.println("=====================================check1User");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList = taskService.createTaskQuery().taskAssignee("check1User").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
                for (Task task : taskList) {
                    System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
                    System.out.println("id=" + task.getId());
                    System.out.println("assignee=" + task.getAssignee());
                    System.out.println("executionId=" + task.getExecutionId());
                    System.out.println("owner=" + task.getOwner());
                    System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
                    System.out.println("definitionKey=" + task.getTaskDefinitionKey());
                    System.out.println("dueDate=" + task.getDueDate());
                    taskService.complete(task.getId(), common_params);
                }
                HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            System.out.println("=====================================check2User");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList = taskService.createTaskQuery().taskAssignee("check2User").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
                for (Task task : taskList) {
                    System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
                    System.out.println("id=" + task.getId());
                    System.out.println("assignee=" + task.getAssignee());
                    System.out.println("executionId=" + task.getExecutionId());
                    System.out.println("owner=" + task.getOwner());
                    System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
                    System.out.println("definitionKey=" + task.getTaskDefinitionKey());
                    System.out.println("dueDate=" + task.getDueDate());

                    Map _params = new HashMap();
                    _params.put("std_price", 500000);
                    _params.putAll(common_params);

                    taskService.complete(task.getId(), _params);
                }
                HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            System.out.println("=====================================check3User");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList = taskService.createTaskQuery().taskAssignee("check3User").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
                for (Task task : taskList) {
                    System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
                    System.out.println("id=" + task.getId());
                    System.out.println("assignee=" + task.getAssignee());
                    System.out.println("executionId=" + task.getExecutionId());
                    System.out.println("owner=" + task.getOwner());
                    System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
                    System.out.println("definitionKey=" + task.getTaskDefinitionKey());
                    System.out.println("dueDate=" + task.getDueDate());

                    //common_params.put("passed", "false_check2");
                    taskService.complete(task.getId(), common_params);
                }
                HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            System.out.println("=====================================swjhy");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList = taskService.createTaskQuery().taskAssignee("swjhy").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
                for (Task task : taskList) {
                    System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
                    System.out.println("id=" + task.getId());
                    System.out.println("name=" + task.getName());
                    System.out.println("desc=" + task.getDescription());
                    System.out.println("assignee=" + task.getAssignee());
                    System.out.println("executionId=" + task.getExecutionId());
                    System.out.println("owner=" + task.getOwner());
                    System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
                    System.out.println("definitionKey=" + task.getTaskDefinitionKey());
                    System.out.println("dueDate=" + task.getDueDate());

                    Map _params = new HashMap();

                    List<String> _bos = bos.get(instance.getBusinessKey());

                    Map<String, String> noIndexes_swgcs = new HashMap();
                    noIndexes_swgcs.put("no_1", "swgcsUser1");
                    noIndexes_swgcs.put("no_3", "swgcsUser3");

                    Map<String, String> noIndexes_wlgcs = new HashMap();
                    noIndexes_wlgcs.put("no_1", "wlgcsUser1");
                    noIndexes_wlgcs.put("no_2", "wlgcsUser2");
                    noIndexes_wlgcs.put("no_3", "wlgcsUser3");



                    _params.put("swgcs_no_indexes", noIndexes_swgcs);
                    _params.put("wlgcs_no_indexes", noIndexes_wlgcs);

                    _params.put("no_num", _bos.size());
                    _params.putAll(common_params);

                    taskService.complete(task.getId(), _params);
                }
                HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            System.out.println("=====================================allot");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList = taskService.createTaskQuery().taskAssignee("allot").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
                for (Task task : taskList) {
                    System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
                    System.out.println("id=" + task.getId());
                    System.out.println("name=" + task.getName());
                    System.out.println("desc=" + task.getDescription());
                    System.out.println("assignee=" + task.getAssignee());
                    System.out.println("executionId=" + task.getExecutionId());
                    System.out.println("owner=" + task.getOwner());
                    System.out.println("processDefinitionId=" + task.getProcessDefinitionId());
                    System.out.println("definitionKey=" + task.getTaskDefinitionKey());
                    System.out.println("dueDate=" + task.getDueDate());

                    Map<String, String> swgcsNoindexes = (Map) taskService.getVariable(task.getId(), "swgcs_no_indexes");
                    Map<String, String> wlgcsNoindexes = (Map) taskService.getVariable(task.getId(), "wlgcs_no_indexes");

                    int loopConter = (Integer) taskService.getVariable(task.getId(), "loopCounter");

                    Map.Entry<String, String> entry = (Map.Entry<String, String>) wlgcsNoindexes.entrySet().toArray()[loopConter];

                    Map _params = new HashMap();
                    String noIndex = entry.getKey();
                    String wlgcsUser = entry.getValue();
                    if (swgcsNoindexes.containsKey(entry.getKey())) {
                        _params.put("has_contract", "false");
                        _params.put("confirm_5_user", swgcsNoindexes.get(noIndex));
                    } else {
                        _params.put("has_contract", "true");
                    }
                    _params.put("purchase_6_user", wlgcsUser);

                    taskService.complete(task.getId(), _params);
                }
                HistoricProcessInstance hisInstance = activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            System.out.println("=====================================swgcsUser3");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList =
                        taskService.createTaskQuery().taskAssignee("swgcsUser3").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
                for (Task task : taskList) {
                    System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
                    System.out.println("id=" + task.getId());
                    System.out.println("name=" + task.getName());
                    System.out.println("desc=" + task.getDescription());
                    System.out.println("assignee=" + task.getAssignee());
                    System.out.println("executionId=" + task.getExecutionId());
                    System.out.println("owner=" + task.getOwner());
                    System.out.println("processDefinitionId="
                            + task.getProcessDefinitionId());
                    System.out.println("definitionKey="
                            + task.getTaskDefinitionKey());
                    System.out.println("dueDate="
                            + task.getDueDate());
                    //String noIndex = (String) taskService.getVariable(task.getId(), "no_index");
                    String noIndex = "no_3";
                    System.out.println("noIndex=" + noIndex);
                    //noIndexes.remove("swgcsUser1");

                    taskService.getVariables(task.getId());

                    int nrOfInstances = (Integer) taskService.getVariable(task.getId(), "nrOfInstances");
                    int nrOfActiveInstances = (Integer) taskService.getVariable(task.getId(), "nrOfActiveInstances");
                    int nrOfCompletedInstances = (Integer) taskService.getVariable(task.getId(), "nrOfCompletedInstances");

                    System.out.println("nrOfInstances=" + nrOfInstances);
                    System.out.println("nrOfActiveInstances=" + nrOfActiveInstances);
                    System.out.println("nrOfCompletedInstances="
                            + nrOfCompletedInstances);

                    Map _params = new HashMap();
                    _params.put("passed", "check_2");

                    taskService.complete(task.getId(), _params);
                }
                HistoricProcessInstance hisInstance =
                        activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            System.out.println("=====================================back");
            for (ProcessInstance instance : instanceList) {
                TaskService taskService = activitiService.getTaskService();
                List<Task> taskList =
                        taskService.createTaskQuery().taskAssignee("back").list();
                System.out.println("instanceId=" + instance.getId());
                System.out.println("businessKey=" + instance.getBusinessKey());
                System.out.println("instanceEnded=" + instance.isEnded());
                for (Task task : taskList) {
                    System.out.println("-----------------------------------" + task.getTaskDefinitionKey());
                    System.out.println("id=" + task.getId());
                    System.out.println("name=" + task.getName());
                    System.out.println("desc=" + task.getDescription());
                    System.out.println("assignee=" + task.getAssignee());
                    System.out.println("executionId=" + task.getExecutionId());
                    System.out.println("owner=" + task.getOwner());
                    System.out.println("processDefinitionId="
                            + task.getProcessDefinitionId());
                    System.out.println("definitionKey="
                            + task.getTaskDefinitionKey());
                    System.out.println("dueDate="
                            + task.getDueDate());
                    //String noIndex = (String) taskService.getVariable(task.getId(), "no_index");
                    String noIndex = "no_3";
                    System.out.println("noIndex=" + noIndex);
                    //noIndexes.remove("swgcsUser1");

                    taskService.getVariables(task.getId());

                    int nrOfInstances = (Integer) taskService.getVariable(task.getId(), "nrOfInstances");
                    int nrOfActiveInstances = (Integer) taskService.getVariable(task.getId(), "nrOfActiveInstances");
                    int nrOfCompletedInstances = (Integer) taskService.getVariable(task.getId(), "nrOfCompletedInstances");

                    System.out.println("nrOfInstances=" + nrOfInstances);
                    System.out.println("nrOfActiveInstances=" + nrOfActiveInstances);
                    System.out.println("nrOfCompletedInstances="
                            + nrOfCompletedInstances);

                    //Map _params = new HashMap();
                    //_params.put("passed", "check_2");

                    //taskService.complete(task.getId(), _params);
                }
                HistoricProcessInstance hisInstance =
                        activitiService.getHistoryService().createHistoricProcessInstanceQuery().processInstanceId(instance.getId()).singleResult();
                System.out.println("TEST ENDTIME = " + hisInstance.getEndTime());
            }

            tm.commit();
//            tm.rollback();
            //Hibernate3Util.rollbackTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                tm.rollback();
            } catch (Exception etx) {
                ex.printStackTrace();
            }
        }
    }
}
