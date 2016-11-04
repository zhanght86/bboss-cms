/**
 * 
 */
package org.frameworkset.trans.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author suwei
 * @date 2016年10月27日
 *
 */
public enum EnumTransDataType {
	WEIXIN_TRANSDATA_OUT("调用微信数据包",  "r0"),
	WEIXIN_TRANSDATA_OUT_RES("调用微信数据包实时返回结果",  "r1"),
	WEIXIN_TRANSDATA_IN("微信回调数据包", "b1"), 
	;
	
	private static Map<String, EnumTransDataType> enumFundnamepools = new HashMap<String, EnumTransDataType>();

	static {
		for (EnumTransDataType fundname : EnumTransDataType.values()) {
			enumFundnamepools.put(fundname.getName(), fundname);
		}
	}
	private String name;

	private String code;

	EnumTransDataType(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	

	public static EnumTransDataType getEnumTransDataTypeByname(String name) {
		if (isBlank(name)) {
			return null;
		}
		return enumFundnamepools.get(name);
	}

	public static boolean isBlank(String str) {
		int length;

		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}

		return true;
	}
}
