package com.sany.workflow.webservice.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Before;
import org.junit.Test;

import com.caucho.hessian.client.HessianProxyFactory;
import com.frameworkset.util.ValueObjectUtil;
import com.sany.workflow.webservice.entity.DataResponse;
import com.sany.workflow.webservice.entity.DealInfoResponse;
import com.sany.workflow.webservice.entity.DeploymentInfo;
import com.sany.workflow.webservice.entity.NoHandTaskResponse;
import com.sany.workflow.webservice.entity.ResultResponse;
import com.sany.workflow.webservice.service.WorkflowService;

public class WorkflowServiceTest {
	// hessian服务方式
	private WorkflowService hassianService = null;
	// webservice方式
	private WorkflowService cxfService = null;
	private String context = "http://pdp.sany.com.cn:8080/SanyPDP/";
	private String user = "chenm24";
	private String app = "pdp";
	@Before
	public void init() throws Exception {
		// hessian服务方式
		HessianProxyFactory factory = new HessianProxyFactory();
		String url = context +"hessian?service=workflowService";
		hassianService = (WorkflowService) factory.create(
				WorkflowService.class, url);

		// webservice方式
		String cxfUrl = context +"cxfservices/workflowService";
		JaxWsProxyFactoryBean WSServiceClientFactory = new JaxWsProxyFactoryBean();
		WSServiceClientFactory.setAddress(cxfUrl);
		WSServiceClientFactory.setServiceClass(WorkflowService.class);
		cxfService = (WorkflowService) WSServiceClientFactory.create();

	}

	@Test
	public void testDeployProcess() throws Exception {
		DeploymentInfo info = new DeploymentInfo();
		info.setBusinessTypeId("e559e851-0142-3a21-d1b5-8ae7c56784c7");
		info.setDeployName("test1");
		info.setNeedConfig("1");
		info.setUpgradepolicy(0);
		info.setWfAppId("da8e7c0a-8ef3-46b4-a275-151d41763141");

		// File file = new File(
		// "E:\\workspace\\SanyPDP\\test\\com\\sany\\activiti\\demo\\diagrams\\mms.return.zip");
		// FileInputStream in = new FileInputStream(file);
		// ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		// byte[] temp = new byte[1024];
		// int size = 0;
		// while ((size = in.read(temp)) != -1) {
		// out.write(temp, 0, size);
		// }
		// in.close();
		// byte[] bytes = out.toByteArray();
		byte[] bytes = ValueObjectUtil
				.getBytesFileContent("E:\\workspace\\SanyPDP\\test\\com\\sany\\activiti\\demo\\diagrams\\mms.return.zip");
		info.setProcessDefFile(bytes);

		// hessian服务方式
		ResultResponse hessianResult = hassianService.deployProcess(info);
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService.deployProcess(info);
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());
		/**
		 * http请求部署流程，可以直接上传文件
		 */
		String url = context +"workflow/webservice/deployZipProcess.freepage";
		Map params = new HashMap();
		Map<String,File> fileparams = new HashMap<String,File>();
		fileparams.put("processZipDef", new File("E:\\workspace\\SanyPDP\\test\\com\\sany\\activiti\\demo\\diagrams\\mms.return.zip"));
		fileparams.put("processParamFile", new File("E:\\workspace\\SanyPDP\\test\\com\\sany\\activiti\\demo\\diagrams\\mms.return.zip"));
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url, params, fileparams);
		System.out.println("http请求方式返回结果:" + httpResult);

	}

	@Test
	public void testSuspendProcessDef() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService
				.suspendProcessDef("Mms.return:12:b8427c9a-1dd2-11e4-bf57-4437e6999a31");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService
				.suspendProcessDef("Mms.return:12:b8427c9a-1dd2-11e4-bf57-4437e6999a31");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());

		// // HTTP方式
		String url = context +"workflow/webservice/suspendProcessDef.freepage?processDefId=Mms.return:12:b8427c9a-1dd2-11e4-bf57-4437e6999a31";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testActivateProcessDef() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService
				.activateProcessDef("Mms.return:12:b8427c9a-1dd2-11e4-bf57-4437e6999a31");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService
				.activateProcessDef("Mms.return:12:b8427c9a-1dd2-11e4-bf57-4437e6999a31");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());

		// HTTP方式
		String url = context +"workflow/webservice/activateProcessDef.freepage?processDefId=Mms.return:12:b8427c9a-1dd2-11e4-bf57-4437e6999a31";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testDelDeployment() throws Exception {

		// hessian服务方式
		ResultResponse hessianResult = hassianService
				.delDeployment("appactionMyProcess");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService.delDeployment("Test.mail");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());

		// HTTP方式
		String url = context +"workflow/webservice/delDeployment.freepage?processKeys=Test.mail";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testGetProccessXML() throws Exception {

		// hessian服务方式
		DataResponse hessianResult = hassianService.getProccessXML(
				"Mms.return", "1");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultData());

		// webservice方式
		DataResponse cxfResult = cxfService.getProccessXML("Mms.return", "1");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultData());

		// HTTP方式
		String url = context +"workflow/webservice/getProccessXML.freepage?processKey=Mms.return&version=1";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);

	}

	@Test
	public void testGetProccessPic() throws Exception {

		// HTTP方式
		String url = context +"workflow/webservice/getProccessPic.freepage";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);

	}

	@Test
	public void testStartInstance() throws Exception {
		List<HashMap<String, Object>> nodeInfoList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> nodeInfoMap1 = new HashMap<String, Object>();
		nodeInfoMap1.put("nodeKey", "usertask1");
		nodeInfoMap1.put("nodeUserIds", "test1");
		nodeInfoList.add(nodeInfoMap1);
		HashMap<String, Object> nodeInfoMap2 = new HashMap<String, Object>();
		nodeInfoMap2.put("nodeKey", "usertask2");
		nodeInfoMap2.put("nodeUserIds", "test6");
		nodeInfoList.add(nodeInfoMap2);
		HashMap<String, Object> nodeInfoMap3 = new HashMap<String, Object>();
		nodeInfoMap3.put("nodeKey", "usertask3");
		nodeInfoMap3.put("nodeUserIds", "test3,test5");
		nodeInfoMap3.put("isMulti", "1");
		nodeInfoList.add(nodeInfoMap3);
		HashMap<String, Object> nodeInfoMap4 = new HashMap<String, Object>();
		nodeInfoMap4.put("nodeKey", "usertask4");
		nodeInfoMap4.put("nodeUserIds", "test4");
		nodeInfoList.add(nodeInfoMap4);
		
		List<HashMap<String, Object>> variableList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("paramName", "isPass");
		variableMap.put("paramValue", "true");
		variableList.add(variableMap);

		// hessian服务方式
		ResultResponse hessianResult = hassianService.startInstance("Mms.return", "测试服务开启4", "test1", nodeInfoList, variableList);
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());
	}

	@Test
	public void testUpgradeInstancesByProcessKey() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService
				.upgradeInstancesByProcessKey("Mms.return");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService
				.upgradeInstancesByProcessKey("Test.mail");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());

		// HTTP方式
		String url = context +"workflow/webservice/upgradeInstancesByProcessKey.freepage?processKey=Test.mail";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testDelInstancesForLogic() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService.delInstancesForLogic(
				"0675be3f-1c48-11e4-a6be-4437e6999a31", "hessian服务测试删除","","");
		System.out.println("hessian方式返回结果:" + hessianResult);

		// webservice方式
		ResultResponse cxfResult = cxfService.delInstancesForLogic(
				"45d70902-1bad-11e4-ab01-4437e6999a31", "webservice方式测试删除","","");
		System.out.println("webservice方式返回结果:" + cxfResult);

		// HTTP方式
		String deleteReason = java.net.URLEncoder.encode("HTTP测试删除", "UTF-8");
		String url = context +"workflow/webservice/delInstancesForLogic.freepage?instancesIds=3ca0037a-1e1a-11e4-b400-4437e6999a31&deleteReason="
				+ deleteReason;
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testDelInstancesForPhysics() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService
				.delInstancesForPhysics("0675be3f-1c48-11e4-a6be-4437e6999a31");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService
				.delInstancesForPhysics("3ca0037a-1e1a-11e4-b400-4437e6999a31");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());

		// HTTP方式
		String url = context +"workflow/webservice/delInstancesForPhysics.freepage?instancesIds=2f9b0603-190f-11e4-9627-4437e6999a31";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testSuspendInstance() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService
				.suspendInstance("0abfea4b-1e94-11e4-a662-4437e6999a31");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService
				.suspendInstance("0f6e3028-1e94-11e4-a662-4437e6999a31");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());

		// HTTP方式
		String url = context +"workflow/webservice/suspendInstance.freepage?instancesId=136cd515-1e94-11e4-a662-4437e6999a31";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testActivateInstance() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService
				.activateInstance("0abfea4b-1e94-11e4-a662-4437e6999a31");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		ResultResponse cxfResult = cxfService
				.activateInstance("0f6e3028-1e94-11e4-a662-4437e6999a31");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess());

		// HTTP方式
		String url = context +"workflow/webservice/activateInstance.freepage?instancesId=136cd515-1e94-11e4-a662-4437e6999a31";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testGetInstanceDealInfo() throws Exception {
		// hessian服务方式
		DealInfoResponse hessianResult = hassianService
				.getInstanceDealInfo("0f6e3028-1e94-11e4-a662-4437e6999a31");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess() + " "
				+ hessianResult.getDataList());

		// webservice方式
		DealInfoResponse cxfResult = cxfService
				.getInstanceDealInfo("0f6e3028-1e94-11e4-a662-4437e6999a31");
		System.out.println("webservice方式返回结果:" + cxfResult.getResultCode()
				+ " " + cxfResult.getResultMess() + " "
				+ cxfResult.getDataList());

		// HTTP方式
		String url = context +"workflow/webservice/getInstanceDealInfo.freepage?instancesId=0f6e3028-1e94-11e4-a662-4437e6999a31";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testSignTask() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService.signTask(
				"136cd51f-1e94-11e4-a662-4437e6999a31", "test1");
		System.out.println("hessian方式返回结果:" + hessianResult);

		// webservice方式
		ResultResponse cxfResult = cxfService.signTask(
				"0f6e3032-1e94-11e4-a662-4437e6999a31", "test1");
		System.out.println("webservice方式返回结果:" + cxfResult);

		// HTTP方式
		String url = context +"workflow/webservice/signTask.freepage?taskId=0ac71645-1e94-11e4-a662-4437e6999a31&userId=test1";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testDiscardTask() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService.discardTask(
				"0b814505-1e9c-11e4-a662-4437e6999a31", "废弃测试","","","");
		System.out.println("hessian方式返回结果:" + hessianResult);

		// webservice方式
		ResultResponse cxfResult = cxfService.discardTask("", "","","","");
		System.out.println("webservice方式返回结果:" + cxfResult);

		// HTTP方式
		String url = context +"workflow/webservice/discardTask.freepage";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testCancelTask() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService.cancelTask("", "", "", "", "");
		System.out.println("hessian方式返回结果:" + hessianResult);

		// webservice方式
		ResultResponse cxfResult = cxfService.cancelTask("", "", "", "", "");
		System.out.println("webservice方式返回结果:" + cxfResult);

		// HTTP方式
		String url = context +"workflow/webservice/cancelTask.freepage";
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testDelegateTask() throws Exception {
		// hessian服务方式
		ResultResponse hessianResult = hassianService.delegateTask(
				"0b81450f-1e9c-11e4-a662-4437e6999a31", "test1", "test2",
				"0b814505-1e9c-11e4-a662-4437e6999a31", "Mms.return");
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());

		// webservice方式
		// ResultResponse cxfResult = cxfService.delegateTask(
		// "0f6e3032-1e94-11e4-a662-4437e6999a31", "test1", "test2",
		// "0f6e3028-1e94-11e4-a662-4437e6999a31", "Mms.return");
		// System.out.println("webservice方式返回结果:" + cxfResult);

		// HTTP方式
		// String url =
		// context +"workflow/webservice/delegateTask.freepage?"
		// +
		// "taskId=0ac71645-1e94-11e4-a662-4437e6999a31&fromUserId=test1&toUserId=test2"
		// +
		// "&instancesId=0abfea4b-1e94-11e4-a662-4437e6999a31&processKey=Mms.return";
		// String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
		// .httpPostforString(url);
		// System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testRejectToPreTask() throws Exception {
	}

	@Test
	public void testCompleteTask() throws Exception {
		HashMap<String, Object> taskMap = new HashMap<String, Object>();
		taskMap.put("taskId", "2e4ed494-1ea6-11e4-a2f0-4437e6999a31");
		taskMap.put("processKey", "Mms.return");
		taskMap.put("instancesId", "2e4ed48a-1ea6-11e4-a2f0-4437e6999a31");
		taskMap.put("taskState", "1");
		taskMap.put("currentUser", "test1");

		List<HashMap<String, Object>> nodeInfoList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> nodeInfoMap1 = new HashMap<String, Object>();
		nodeInfoMap1.put("nodeKey", "usertask1");
		nodeInfoMap1.put("nodeUserIds", "test1");
		nodeInfoList.add(nodeInfoMap1);
		HashMap<String, Object> nodeInfoMap2 = new HashMap<String, Object>();
		nodeInfoMap2.put("nodeKey", "usertask2");
		nodeInfoMap2.put("nodeUserIds", "test6");
		nodeInfoList.add(nodeInfoMap2);
		HashMap<String, Object> nodeInfoMap3 = new HashMap<String, Object>();
		nodeInfoMap3.put("nodeKey", "usertask3");
		nodeInfoMap3.put("nodeUserIds", "test3,test5");
		nodeInfoMap3.put("isMulti", "1");
		nodeInfoList.add(nodeInfoMap3);
		HashMap<String, Object> nodeInfoMap4 = new HashMap<String, Object>();
		nodeInfoMap4.put("nodeKey", "usertask4");
		nodeInfoMap4.put("nodeUserIds", "test4");
		nodeInfoList.add(nodeInfoMap4);

		// hessian服务方式
		ResultResponse hessianResult = hassianService.completeTask(taskMap,
				nodeInfoList, null);
		System.out.println("hessian方式返回结果:" + hessianResult.getResultCode()
				+ " " + hessianResult.getResultMess());
	}

	@Test
	public void testCountTaskNum() throws Exception {
		// hessian服务方式
		DataResponse hessianResult = hassianService
				.countTaskNum(user, app);
		System.out.println("hessian方式返回结果:" + hessianResult);

		// webservice方式
		DataResponse cxfResult = cxfService.countTaskNum(user, app);
		System.out.println("webservice方式返回结果:" + cxfResult);

		// HTTP方式
		String url = context +"workflow/webservice/countTaskNum.freepage?pernr="+user+"&sysId="+app;
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

	@Test
	public void testGetNoHandleTask() throws Exception {
		// hessian服务方式
		NoHandTaskResponse hessianResult = hassianService.getNoHandleTask(
				user, app);
		System.out.println("hessian方式返回结果:" + hessianResult);

		// webservice方式
		NoHandTaskResponse cxfResult = cxfService.getNoHandleTask(user,
				app);
		System.out.println("webservice方式返回结果:" + cxfResult);

		// HTTP方式
		String url = context +"workflow/webservice/getNoHandleTask.freepage?pernr="+user+"&sysId="+app;
		String httpResult = org.frameworkset.spi.remote.http.HttpReqeust
				.httpPostforString(url);
		System.out.println("http请求方式返回结果:" + httpResult);
	}

}