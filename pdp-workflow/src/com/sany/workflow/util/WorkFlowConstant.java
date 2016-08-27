package com.sany.workflow.util;

import java.util.HashMap;
import java.util.Map;

public class WorkFlowConstant {

	/**
	 * 待办配置类型:通用
	 */
	public static final String BUSINESS_TYPE_COMMON = "0";

	/**
	 * 待办配置类型:组织机构
	 */
	public static final String BUSINESS_TYPE_ORG = "1";

	/**
	 * 待办配置类型:业务类型
	 */
	public static final String BUSINESS_TYPE_BUSINESSTYPE = "2";

	public static final int PARAM_READONLY = 1;

	public static final int PARAM_EDIT = 0;

	/**
	 * 应用配置类型:第三方应用
	 */
	private static final String app_third_mode_type = "第三方应用";

	public static String getApp_third_mode_type() {

		return app_third_mode_type;
	}

	/**
	 * 应用配置类型:单点登陆的应用类型 独立库应用
	 */
	private static final String app_sso_mode_type = "独立库应用";

	public static String getApp_sso_mode_type() {

		return app_sso_mode_type;
	}

	private static final String tocken_service_url = "http://localhost:8080/SanyPDP/hessian?service=tokenService";

	public static String getTocken_service_url() {

		return tocken_service_url;
	}

	// 发送状态定义
	public final static int NO_SEND = 0;// 短信/邮件未发送
	public final static int ALL_SEND_SUCCESS = 1;// 短信、邮件发送成功
	public final static int MESS_SEND_SUCCESS = 2;// 短信发送成功
	public final static int MESS_SEND_FAIL = 3;// 短信发送失败
	public final static int EMAIL_SEND_SUCCESS = 4;// 邮件发送成功
	public final static int EMAIL_SEND_FAIL = 5;// 邮件发送失败
	public final static int SEND_MESS_SUCCESS_EMAIL_FAIL = 6;// 短信发送成功、邮件发送失败
	public final static int SEND_MESS_FAIL_EMAIL_SUCCESS = 7;// 短信发送失败、邮件发送成功
	public final static int ALL_SEND_FAIL = 8;// 短信、邮件发送失败
	// 预警状态含义
	public final static String NO_ADVANCESEND_CONTENT = "短信/邮件未发送";
	public final static String ALL_ADVANCESEND_SUCCESS_CONTENT = "短信/邮件预警发送成功";
	public final static String MESS_ADVANCESEND_SUCCESS_CONTENT = "短信预警发送成功";
	public final static String MESS_ADVANCESEND_FAIL_CONTENT = "短信预警发送失败";
	public final static String EMAIL_ADVANCESEND_SUCCESS_CONTENT = "邮件预警发送成功";
	public final static String EMAIL_ADVANCESEND_FAIL_CONTENT = "邮件预警发送失败";
	public final static String ADVANCESEND_MESS_SUCCESS_EMAIL_FAIL_CONTENT = "短信预警发送成功,邮件预警发送失败";
	public final static String ADVANCESEND_MESS_FAIL_EMAIL_SUCCESS_CONTENT = "短信预警发送失败,邮件预警发送成功";
	public final static String ALL_ADVANCESEND_FAIL_CONTENT = "短信、邮件预警失败";
	// 超时状态含义
	public final static String NO_OVERTIMESEND_CONTENT = "短信/邮件未发送";
	public final static String ALL_OVERTIMESEND_SUCCESS_CONTENT = "短信/邮件超时发送成功";
	public final static String MESS_OVERTIMESEND_SUCCESS_CONTENT = "短信超时发送成功";
	public final static String MESS_OVERTIMESEND_FAIL_CONTENT = "短信超时发送失败";
	public final static String EMAIL_OVERTIMESEND_SUCCESS_CONTENT = "邮件超时发送成功";
	public final static String EMAIL_OVERTIMESEND_FAIL_CONTENT = "邮件超时发送失败";
	public final static String OVERTIMESEND_MESS_SUCCESS_EMAIL_FAIL_CONTENT = "短信超时发送成功,邮件超时发送失败";
	public final static String OVERTIMESEND_MESS_FAIL_EMAIL_SUCCESS_CONTENT = "短信超时发送失败,邮件超时发送成功";
	public final static String ALL_OVERTIMESEND_FAIL_CONTENT = "短信、邮件超时失败";

	public static Map<Integer, String> advanceSendMap = null;

	/**
	 * 预警状态转义
	 * 
	 * @return 2014年7月28日
	 */
	public static Map<Integer, String> getAdvanceSend() {
		if (advanceSendMap == null) {
			advanceSendMap = new HashMap<Integer, String>();
			advanceSendMap.put(NO_SEND, NO_ADVANCESEND_CONTENT);
			advanceSendMap.put(ALL_SEND_SUCCESS,
					ALL_ADVANCESEND_SUCCESS_CONTENT);
			advanceSendMap.put(MESS_SEND_SUCCESS,
					MESS_ADVANCESEND_SUCCESS_CONTENT);
			advanceSendMap.put(MESS_SEND_FAIL, EMAIL_ADVANCESEND_FAIL_CONTENT);
			advanceSendMap.put(EMAIL_SEND_SUCCESS,
					EMAIL_ADVANCESEND_SUCCESS_CONTENT);
			advanceSendMap
					.put(EMAIL_SEND_FAIL, EMAIL_ADVANCESEND_FAIL_CONTENT);
			advanceSendMap.put(SEND_MESS_SUCCESS_EMAIL_FAIL,
					ADVANCESEND_MESS_SUCCESS_EMAIL_FAIL_CONTENT);
			advanceSendMap.put(SEND_MESS_FAIL_EMAIL_SUCCESS,
					ADVANCESEND_MESS_FAIL_EMAIL_SUCCESS_CONTENT);
			advanceSendMap.put(ALL_SEND_FAIL, ALL_ADVANCESEND_FAIL_CONTENT);
		}

		return advanceSendMap;
	}

	public static Map<Integer, String> overtimeSendMap = null;

	/**
	 * 超时状态转义
	 * 
	 * @return 2014年7月28日
	 */
	public static Map<Integer, String> getOvertimeSend() {
		if (overtimeSendMap == null) {
			overtimeSendMap = new HashMap<Integer, String>();
			overtimeSendMap.put(NO_SEND, NO_OVERTIMESEND_CONTENT);
			overtimeSendMap.put(ALL_SEND_SUCCESS,
					ALL_OVERTIMESEND_SUCCESS_CONTENT);
			overtimeSendMap.put(MESS_SEND_SUCCESS,
					MESS_OVERTIMESEND_SUCCESS_CONTENT);
			overtimeSendMap.put(MESS_SEND_FAIL, MESS_OVERTIMESEND_FAIL_CONTENT);
			overtimeSendMap.put(EMAIL_SEND_SUCCESS,
					EMAIL_OVERTIMESEND_SUCCESS_CONTENT);
			overtimeSendMap.put(EMAIL_SEND_FAIL,
					EMAIL_OVERTIMESEND_FAIL_CONTENT);
			overtimeSendMap.put(SEND_MESS_SUCCESS_EMAIL_FAIL,
					OVERTIMESEND_MESS_SUCCESS_EMAIL_FAIL_CONTENT);
			overtimeSendMap.put(SEND_MESS_FAIL_EMAIL_SUCCESS,
					OVERTIMESEND_MESS_FAIL_EMAIL_SUCCESS_CONTENT);
			overtimeSendMap.put(ALL_SEND_FAIL, ALL_OVERTIMESEND_FAIL_CONTENT);
		}

		return overtimeSendMap;
	}
	
	/** 显示已阅抄送记录数*/
	public static final Integer SHOW_READEDCOPYTASK_LIMIT= 10;  
}
