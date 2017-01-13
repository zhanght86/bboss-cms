/**
 * 微信公众号商户支付对象实体（提现）
 */
package org.frameworkset.wx.common.entity;

/**
 * @author suwei
 * @date 2017年1月11日
 *
 */
public class WxServiceCompanyResult {
	/**
	 * 微信分配的公众账号ID（企业号corpid即为此appId）
	 */
	private String mchAppid;
	/**
	 * 微信支付分配的商户号
	 * 
	 */
	private String mchid;
	/**
	 * 微信支付分配的终端设备号
	 */
	private String deviceInfo;
	/**
	 * 随机字符串，不长于32位
	 */
	private String nonceStr;
	/**
	 * 业务结果
	 */
	private String resultCode;
	/**
	 * 错误代码
	 */
	private String errCode;
	/**
	 * 错误代码描述
	 */
	private String errCodeDes;

	/**
	 * @return the mchAppid
	 */
	public String getMchAppid() {
		return mchAppid;
	}

	/**
	 * @param mchAppid
	 *            the mchAppid to set
	 */
	public void setMchAppid(String mchAppid) {
		this.mchAppid = mchAppid;
	}

	/**
	 * @return the mchid
	 */
	public String getMchid() {
		return mchid;
	}

	/**
	 * @param mchid
	 *            the mchid to set
	 */
	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	/**
	 * @return the deviceInfo
	 */
	public String getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * @param deviceInfo
	 *            the deviceInfo to set
	 */
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	/**
	 * @return the nonceStr
	 */
	public String getNonceStr() {
		return nonceStr;
	}

	/**
	 * @param nonceStr
	 *            the nonceStr to set
	 */
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode
	 *            the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the errCode
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * @param errCode
	 *            the errCode to set
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	/**
	 * @return the errCodeDes
	 */
	public String getErrCodeDes() {
		return errCodeDes;
	}

	/**
	 * @param errCodeDes
	 *            the errCodeDes to set
	 */
	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

}
