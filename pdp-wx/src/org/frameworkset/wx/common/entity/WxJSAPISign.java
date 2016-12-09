/**
 * 
 */
package org.frameworkset.wx.common.entity;

/**
 * 微信统一下单接口参数
 * 
 * @author suwei
 * @Date 2016-10-09
 */
public class WxJSAPISign {
	/**
	 * 公众账号ID
	 */
	private String appId;

	private String timeStamp;

	/**
	 * 随机字符串
	 */
	private String nonceStr;
	private String prepayId;
	/**
	 * 签名
	 */
	private String signType;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
}