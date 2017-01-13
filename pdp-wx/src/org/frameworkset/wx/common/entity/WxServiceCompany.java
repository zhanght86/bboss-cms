/**
 * 微信公众号商户支付对象实体（提现）
 */
package org.frameworkset.wx.common.entity;

/**
 * @author suwei
 * @date 2017年1月11日
 *
 */
public class WxServiceCompany  {
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
	 * 签名
	 */
	private String sign;
	/**
	 * 商户订单号，需保持唯一性
	 */
	private String partnerTradeNo;
	/**
	 * 商户appid下，某用户的openid
	 */
	private String openid;
	/**
	 * NO_CHECK：不校验真实姓名 FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
	 * OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
	 */
	private String checkName;
	/**
	 * 收款用户真实姓名。 如果check_name设置为FORCE_CHECK或OPTION_CHECK，则必填用户真实姓名
	 */
	private String reUserName;
	/**
	 * 金额 企业付款金额，单位为分
	 */
	private long amount;
	/**
	 * 企业付款描述信息
	 */
	private String desc;
	/**
	 * Ip地址
	 */
	private String spbillCreateIp;
	/**
	 * @return the mchAppid
	 */
	public String getMchAppid() {
		return mchAppid;
	}
	/**
	 * @param mchAppid the mchAppid to set
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
	 * @param mchid the mchid to set
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
	 * @param deviceInfo the deviceInfo to set
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
	 * @param nonceStr the nonceStr to set
	 */
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the partnerTradeNo
	 */
	public String getPartnerTradeNo() {
		return partnerTradeNo;
	}
	/**
	 * @param partnerTradeNo the partnerTradeNo to set
	 */
	public void setPartnerTradeNo(String partnerTradeNo) {
		this.partnerTradeNo = partnerTradeNo;
	}
	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * @return the checkName
	 */
	public String getCheckName() {
		return checkName;
	}
	/**
	 * @param checkName the checkName to set
	 */
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	/**
	 * @return the reUserName
	 */
	public String getReUserName() {
		return reUserName;
	}
	/**
	 * @param reUserName the reUserName to set
	 */
	public void setReUserName(String reUserName) {
		this.reUserName = reUserName;
	}
	/**
	 * @return the amount
	 */
	public long getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(long amount) {
		this.amount = amount;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the spbillCreateIp
	 */
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	/**
	 * @param spbillCreateIp the spbillCreateIp to set
	 */
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

}
