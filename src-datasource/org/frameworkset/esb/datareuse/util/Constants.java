package org.frameworkset.esb.datareuse.util;

public class Constants {
	/**
	 * 防止实例化
	 */
	private Constants() {

	}

	public static final String DEFAULT_DATE_FORMAT = "yyyyMMddHHmmss";

	public final static String DATAREUSE_DBNAME = "datareuse";

	// 数据对象类型table
	public final static String OBJECT_TYPE_TABLE = "TABLE";

	// 数据对象类型view
	public final static String OBJECT_TYPE_VIEW = "VIEW";

	// 数据对象类型view
	public final static String OBJECT_TYPE_SQL = "SQL";

	// 注册中心服务注册接口
	public final static String UDDI_REG_WS_ADDRESS = "/cxfservices/UDDIServiceServiceImplPort";
	public final static String UDDI_Permission_WS_ADDRESS = "/cxfservices/UDDIWSPermissionServiceImplPort";

	// 数据服用注册服务接口
	public final static String UDDI_DATAUESE_WS_ADDRESS = "/cxfservices/queryservice";

	// 调度中心注册服务接口
	public final static String UDDI_MEDIAQUERY_WS_ADDRESS = "/cxfservices/servicecall";

	// 复用品台状态回写接口
	public final static String DATAREUSE_SERVICE_CALLBACK_WS_ADDRESS = "/cxfservices/datareuseServiceImplPort";

	// 服务接口描叙文件地址
	public final static String DATAREUSE_SERVICE_INTERFACE_DESC = "/datareuse/serviceintefacedes/serviceinterfacedes.jsp";
	// rmi调度中心访问服务名称
	public final static String RMI_MEDIA_SERVICECALL = "/media_servicecall";

	// 共享项类型
	public final static String METADATA_TYPE_STRING = "String";

	public final static String METADATA_TYPE_DECIMAL = "Decimal";

	public final static String METADATA_TYPE_DATETIME = "Datetime";

	public final static String METADATA_TYPE_BYTE = "Byte";

	public final static String METADATA_TYPE_BOOLEAN = "Boolean";

	/**
	 * 
	 *<p>
	 * Title:Constants.java
	 * </p>
	 *<p>
	 * Description: 基础数据的基本状态
	 * </p>
	 *<p>
	 * Copyright:Copyright (c) 2010
	 * </p>
	 *<p>
	 * Company:湖南科创
	 * </p>
	 * 
	 * @author 刘剑峰
	 *@version 1.0 2011-4-20
	 */
	public static enum BaseDataStatus {
		NEW("0", "新增"), USED("1", "启用"), UNUSED("2", "停用");

		private String value;
		private String text;

		public String toString() {
			return value;
		}

		private BaseDataStatus(String value, String text) {
			this.value = value;
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public String getValue() {
			return this.value;
		}
	}

	/**
	 * 
	 *<p>
	 * Title:Constants.java
	 * </p>
	 *<p>
	 * Description: 服务的状态
	 * </p>
	 *<p>
	 * Copyright:Copyright (c) 2010
	 * </p>
	 *<p>
	 * Company:湖南科创
	 * </p>
	 * 
	 * @author 刘剑峰
	 *@version 1.0 2011-4-20
	 */
	public static enum ServiceStatus {
		NOREG("1", "未注册"), WAIT("2", "待审核"), UNPASS("3", "被拒绝"), START("4",
				"启用"), STOP("0", "停用");

		private String value;
		private String text;

		public String toString() {
			return value;
		}

		private ServiceStatus(String value, String text) {
			this.value = value;
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public String getValue() {
			return this.value;
		}
	}

	/**
	 * 服务响应返回的错误代码
	 *<p>
	 * Title:Constants.java
	 * </p>
	 *<p>
	 * Description:
	 * </p>
	 *<p>
	 * Copyright:Copyright (c) 2010
	 * </p>
	 *<p>
	 * Company:湖南科创
	 * </p>
	 * 
	 * @author 刘剑峰
	 *@version 1.0 2011-4-26
	 */
	public static enum ServiceResponseError {
		ACCOUNT_OR_PASSWORD_ERR("01", "账号或密码错误"), ACCOUNT_STOPED("02", "账号已停用"), SERVICE_UNUSED(
				"03", "服务被停用或删除"), SERVICE_NOAUTH("04", "没有访问该服务的权限"), RESULT_FIELD_NOAUTH(
				"05", "某些结果字段不在支持范围或您无权访问"), ORDER_FIELD_NOAUTH("06",
				"某些排序字段不在支持范围"), WHERE_FIELD_NOAUTH("06", "某些查询字段不在支持范围"), IP_NOT_ALLOW(
				"07", "不合法的IP访问地址"), UNKNOW_EER("999", "未知异常"), AUTH_OK("00",
				"校验通过"),SERVICE_CALL_OK("998",
				"服务调用成功"),SERVICE_CALL_FAILED("997",
				"服务调用失败");

		private String value;
		private String text;

		// public String toString(){
		// return value;
		// }
		private ServiceResponseError(String value, String text) {
			this.value = value;

			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public String getValue() {
			return this.value;
		}

	}

	/**
	 * 是否启用的标记
	 *<p>
	 * Title:Constants.java
	 * </p>
	 *<p>
	 * Description:
	 * </p>
	 *<p>
	 * Copyright:Copyright (c) 2010
	 * </p>
	 *<p>
	 * Company:湖南科创
	 * </p>
	 * 
	 * @author 刘剑峰
	 *@version 1.0 2011-4-26
	 */
	public static enum UsedBoolean {
		FALSE("0", "停用"), TRUE("1", "启用");

		private String value;
		private String text;

		public String toString() {
			return value;
		}

		private UsedBoolean(String value, String text) {
			this.value = value;
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public String getValue() {
			return this.value;
		}
	}

	/**
	 * 
	 *<p>
	 * Title:Constants.java
	 * </p>
	 *<p>
	 * Description: 服务的数据格式
	 * </p>
	 *<p>
	 * Copyright:Copyright (c) 2010
	 * </p>
	 *<p>
	 * Company:湖南科创
	 * </p>
	 * 
	 * @author 刘剑峰
	 *@version 1.0 2011-4-20
	 */
	public static enum ServiceDataTypes {
		type1("yyyy/MM/dd", "yyyy/MM/dd"), type2("yyyy-MM-dd", "yyyy-MM-dd"), type3(
				"yyyyMMdd", "yyyyMMdd"), type4("ddMMyyyy", "ddMMyyyy"), type5(
				"d-M-yyyy", "d-M-yyyy");

		private String value;
		private String text;

		public String toString() {
			return value;
		}

		private ServiceDataTypes(String value, String text) {
			this.value = value;
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public String getValue() {
			return this.value;
		}
	}

	public static enum ServiceCategory {
		self("1", "复用平台服务"), other("2", "第三方服务");

		private String value;
		private String text;

		public String toString() {
			return value;
		}

		private ServiceCategory(String value, String text) {
			this.value = value;
			this.text = text;
		}

		public String getText() {
			return this.text;
		}

		public String getValue() {
			return this.value;
		}
	}

}
