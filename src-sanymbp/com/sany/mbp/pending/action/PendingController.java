package com.sany.mbp.pending.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;

public class PendingController {

	private static Logger logger = Logger.getLogger(PendingController.class);

//	/**
//	 * hrm接口登录服务
//	 */
//	private LoginService loginService;
//
//	/**
//	 * 工作流
//	 */
//	private WorkflowService workflowService;

	/**
	 * 方法描述:JSONP方式获取待办
	 * 
	 * @param userName
	 *            输入的用户名
	 * @return 数量
	 * @author: fudk
	 * @date: 2012-8-16 下午02:14:10
	 */
	public @ResponseBody Map getHrmPending() {
//		final String userName = request.getParameter("userName");

		AccessControl control = AccessControl.getAccessControl();
//		System.out.println(control.getUserName());
//		System.out.println(control.getUserID());
//		System.out.println(control.getUserAccount());
		final String userName = control.getUserAccount();
		
//		String output = request.getParameter("jsoncallback") + "({\"error\":\"fail\"});";
		String output = "{\"error\":\"fail\"}";
		Map jsonObject = new HashMap();
		logger.info(output);
		jsonObject.put("hrmTaskCount", 0);
		if (!StringUtil.isEmpty(userName)) {
			// 异步同时获取多个待办
			List<Thread> threadList = new ArrayList<Thread>();

			// 返回格式组装
//			final JSONObject jsonObject = new JSONObject();

			// 获取HRM待办
			int hrmPersonnelCount = 14;
			jsonObject.put("hrmTaskCount", hrmPersonnelCount);


		}

		return jsonObject;
	}
	
	
	/**
	 * 方法描述:JSONP方式获取待办
	 * 
	 * @param userName
	 *            输入的用户名
	 * @return 数量
	 * @author: fudk
	 * @date: 2012-8-16 下午02:14:10
	 */
	public @ResponseBody Map getGspPending() {
//		final String userName = request.getParameter("userName");

		AccessControl control = AccessControl.getAccessControl();
//		System.out.println(control.getUserName());
//		System.out.println(control.getUserID());
//		System.out.println(control.getUserAccount());
		final String userName = control.getUserAccount();
		
//		String output = request.getParameter("jsoncallback") + "({\"error\":\"fail\"});";
		String output = "{\"error\":\"fail\"}";
		Map jsonObject = new HashMap();
		logger.info(output);
		jsonObject.put("gspTaskCount", 0);
		if (!StringUtil.isEmpty(userName)) {
			// 异步同时获取多个待办
			List<Thread> threadList = new ArrayList<Thread>();

			// 返回格式组装
//			final JSONObject jsonObject = new JSONObject();

			// 获取gsp待办
			int gspTaskCount = 13;
			jsonObject.put("gspTaskCount", gspTaskCount);

		}

		return jsonObject;
	}
	

	/**
	 * 方法描述:JSONP方式获取待办
	 * 
	 * @param userName
	 *            输入的用户名
	 * @return 数量
	 * @author: fudk
	 * @date: 2012-8-16 下午02:14:10
	 */
	public @ResponseBody Map getEMSPending() {
//		final String userName = request.getParameter("userName");

		AccessControl control = AccessControl.getAccessControl();
//		System.out.println(control.getUserName());
//		System.out.println(control.getUserID());
//		System.out.println(control.getUserAccount());
		final String userName = control.getUserAccount();
		
//		String output = request.getParameter("jsoncallback") + "({\"error\":\"fail\"});";
		String output = "{\"error\":\"fail\"}";
		Map jsonObject = new HashMap();
		logger.info(output);
		jsonObject.put("emsTaskCount", 0);
		if (!StringUtil.isEmpty(userName)) {
			// 异步同时获取多个待办
			List<Thread> threadList = new ArrayList<Thread>();

			// 返回格式组装
//			final JSONObject jsonObject = new JSONObject();

			// 获取ems待办
			int emsTaskCount = 16;
			jsonObject.put("emsTaskCount", emsTaskCount);

		}

		return jsonObject;
	}

	
}
