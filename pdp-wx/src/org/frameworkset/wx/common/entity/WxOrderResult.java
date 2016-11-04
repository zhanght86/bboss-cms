/**
 * 
 */
package org.frameworkset.wx.common.entity;

import java.io.Serializable;

import org.frameworkset.wx.common.enums.EnumWeiXinOrderErrorCode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信统一下单返回bean对象
 * 
 * @author suwei
 * @date 2016年10月26日
 *
 */
public class WxOrderResult implements Serializable {
	/******************* success back **************************/
	private String appid;// 公众账号ID
	@JsonProperty("mch_id")
	private String mchId;// 商户号
	@JsonProperty("device_info")
	private String deviceInfo;// 设备号
	@JsonProperty("nonce_str")
	private String nonceStr;// 随机字符串
	private String sign;// 签名
	@JsonProperty("result_code")
	private String resultCode;// 业务结果
	@JsonProperty("err_code")
	private String errCode;// 错误代码
	@JsonProperty("err_code_des")
	private String errCodeDes;// 错误代码描述
	/******************* error or success back **************************/
	@JsonProperty("trade_type")
	private String tradeType; // 交易类型
	@JsonProperty("prepay_id")
	private String prepayId;// 预支付交易会话标识
	@JsonProperty("code_url")
	private String codeUrl;// 二维码链接
	@JsonProperty("return_code")
	private String returnCode;
	@JsonProperty("return_msg")
	private String returnMsg;

	public boolean isSuccess() {
		if (this.returnCode == null || "".equals(returnCode))
			return false;
		if ("SUCCESS".equals(this.returnCode.toUpperCase().trim()))
			return true;
		else
			return false;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCodeDes() {
		return errCodeDes;
	}

	public void setErrCodeDes(String errCodeDes) {
		this.errCodeDes = errCodeDes;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
}
