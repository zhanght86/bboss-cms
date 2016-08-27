package com.sany.workflow.demo.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.junit.Before;
import org.junit.Test;

import com.sany.workflow.service.TempleService;
import com.sany.workflow.service.impl.TempleServiceImpl;

public class TestTemple {

	private TempleService templeService;

	@Before
	public void init() throws Exception {
		BaseApplicationContext context1 = DefaultApplicationContext
				.getApplicationContext("WebRoot/WEB-INF/conf/workflow/bboss-workflow-templemanage.xml");

		templeService = context1.getTBeanObject(
				"workflow.temple.templeService", TempleServiceImpl.class);

	}

	@Test
	public void testEmail() throws Exception {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("emailTempleId", "6296be8f-e449-479b-9677-f365f01afc30");
		map.put("mailAddress", "qingl2@sany.com.cn");
		map.put("subject", "邮件测试");
		map.put("taskId", "23434");
		map.put("realName", "卿琳");
		map.put("processName", "测试流程");
		map.put("taskName", "测试节点");
		list.add(map);

		templeService.sendNotice(list);

	}

	@Test
	public void testMessage() throws Exception {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("messageTempleId", "97ccf603-2db4-475a-9ead-be44608366da");
		map.put("worknum", "");
		map.put("mobile", "");
		map.put("taskId", "23434");
		map.put("realName", "卿琳");
		map.put("processName", "测试流程");
		map.put("taskName", "测试节点");
		list.add(map);

		templeService.sendNotice(list);

	}
}
