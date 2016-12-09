/**
 * 
 */
package org.frameworkset.wx.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid）， snsapi_userinfo
 * （弹出授权页面，可通过openid拿到昵称、性别、所在地。 并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
 * 
 * @author suwei
 * @date 2016年10月16日
 *
 */
public enum EnumWeiXinAccountFlag {
	ENTERPRISE("enterprise", "企业号"), SERVICE("service", "公众号");
	private static Map<String, EnumWeiXinAccountFlag> enumFundnamepools = new HashMap<String, EnumWeiXinAccountFlag>();

	static {
		for (EnumWeiXinAccountFlag fundname : EnumWeiXinAccountFlag.values()) {
			enumFundnamepools.put(fundname.getName(), fundname);
		}
	}
	private String code;
	private String name;

	EnumWeiXinAccountFlag(String code, String name) {
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

	public static EnumWeiXinAccountFlag getEnumTradeTypeByname(String name) {
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
