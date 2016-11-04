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
public enum EnumWeiXinOrderErrorCode {
	NOAUTH("NOAUTH", "商户无此接口权限", "商户未开通此接口权限", "请商户前往申请此接口权限"),
	NOTENOUGH("NOTENOUGH", "余额不足", "用户帐号余额不足", "用户帐号余额不足，请用户充值或更换支付卡后再支付"),
	ORDERPAID("ORDERPAID", "商户订单已支付", "商户订单已支付，无需重复操作", "商户订单已支付，无需更多操作"),
	ORDERCLOSED("ORDERCLOSED", "订单已关闭", "当前订单已关闭，无法支付", "当前订单已关闭，请重新下单"),
	SYSTEMERROR("SYSTEMERROR", "系统错误", "系统超时", "系统异常，请用相同参数重新调用"),
	APPID_NOT_EXIST("APPID_NOT_EXIST", "APPID不存在", "参数中缺少APPID", "请检查APPID是否正确"),
	MCHID_NOT_EXIST("MCHID_NOT_EXIST", "MCHID不存在", "参数中缺少MCHID", "请检查MCHID是否正确"),
	APPID_MCHID_NOT_MATCH("APPID_MCHID_NOT_MATCH", "appid和mch_id不匹配", "appid和mch_id不匹配", "请确认appid和mch_id是否匹配"),
	LACK_PARAMS("LACK_PARAMS", "缺少参数", "缺少必要的请求参数", "请检查参数是否齐全"),
	OUT_TRADE_NO_USED("OUT_TRADE_NO_USED", "商户订单号重复", "同一笔交易不能多次提交", "请核实商户订单号是否重复提交"),
	SIGNERROR("SIGNERROR", "签名错误", "参数签名结果不正确", "请检查签名参数和方法是否都符合签名算法要求"),
	XML_FORMAT_ERROR("XML_FORMAT_ERROR", "XML格式错误", "XML格式错误", "请检查XML参数格式是否正确"),
	REQUIRE_POST_METHOD("REQUIRE_POST_METHOD", "请使用post方法", "未使用post传递参数 ", "请检查请求参数是否通过post方法提交"),
	POST_DATA_EMPTY("POST_DATA_EMPTY", "post数据为空", "post数据不能为空", "请检查post数据是否为空"),
	NOT_UTF8("NOT_UTF8", "编码格式错误", "未使用指定编码格式", "请使用UTF-8编码格式")

	;
	private static Map<String, EnumWeiXinOrderErrorCode> enumFundnamepools = new HashMap<String, EnumWeiXinOrderErrorCode>();

	static {
		for (EnumWeiXinOrderErrorCode fundname : EnumWeiXinOrderErrorCode.values()) {
			enumFundnamepools.put(fundname.getName(), fundname);
		}
	}
	private String name;
	private String dec;
	private String reason;
	private String solution;

	EnumWeiXinOrderErrorCode(String name, String dec, String reason, String solution) {
		this.name = name;
		this.dec = dec;
		this.reason = reason;
		this.solution = solution;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public static EnumWeiXinOrderErrorCode getEnumTradeTypeByname(String name) {
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
