/**
 * 
 */
package org.frameworkset.wx.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author suwei
 * @date 2016年11月14日
 *
 */
public class WxNotify {
	// 公众账号ID appid 是 String(32) wx8888888888888888
	// 微信分配的公众账号ID（企业号corpid即为此appId）
	private String appid;
	// 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号
	@JsonProperty("mch_id")
	private String mchId;
	// 设备号 device_info 否 String(32) 013467007045764 微信支付分配的终端设备号，
	@JsonProperty("device_info")
	private String deviceInfo;
	// 随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS
	// 随机字符串，不长于32位
	@JsonProperty("mch_id")
	private String nonceStr;
	// 签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，详见签名算法
	private String sign;
	// 签名类型 sign_type 否 String(32) HMAC-SHA256 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
	@JsonProperty("sign_type")
	private String signType;
	// 业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL
	@JsonProperty("result_code")
	private String resultCode;
	@JsonProperty("return_code")
	private String returnCode;
	// 错误代码 err_code 否 String(32) SYSTEMERROR 错误返回的信息描述
	@JsonProperty("err_code")
	private String errCode;
	// 错误代码描述 err_code_des 否 String(128) 系统错误 错误返回的信息描述
	@JsonProperty("err_code_des")
	private String errCodeDes;
	// 用户标识 openid 是 String(128) wxd930ea5d5a258f4f 用户在商户appid下的唯一标识
	private String openid;
	// 是否关注公众账号 is_subscribe 否 String(1) Y 用户是否关注公众账号，Y-关注，N-未关注，仅在公众账号类型支付有效
	@JsonProperty("is_subscribe")
	private String isSubscribe;
	// 交易类型 trade_type 是 String(16) JSAPI JSAPI、NATIVE、APP
	@JsonProperty("trade_type")
	private String tradeType;
	// 付款银行 bank_type 是 String(16) CMC 银行类型，采用字符串类型的银行标识，银行类型见银行列表
	@JsonProperty("bank_type")
	private String bankType;
	// 订单金额 total_fee 是 Int 100 订单总金额，单位为分
	@JsonProperty("total_fee")
	private String totalFee;
	// 应结订单金额 settlement_total_fee 否 Int 100 应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
	@JsonProperty("settlement_total_fee")
	private String settlementTotalFee;
	// 货币种类 fee_type 否 String(8) CNY
	// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	@JsonProperty("fee_type")
	private String feeType;
	// 现金支付金额 cash_fee 是 Int 100 现金支付金额订单现金支付金额，详见支付金额
	@JsonProperty("cash_fee")
	private String cashFee;
	// 现金支付货币类型 cash_fee_type 否 String(16) CNY
	// 货币类型，符合ISO4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
	@JsonProperty("cash_fee_type")
	private String cashFeeType;
	// 代金券金额 coupon_fee 否 Int 10 代金券金额<=订单金额，订单金额-代金券金额=现金支付金额，详见支付金额
	@JsonProperty("coupon_fee")
	private String couponFee;
	// 代金券使用数量 coupon_count 否 Int 1 代金券使用数量
	@JsonProperty("coupon_count")
	private String couponCount;
	// 微信支付订单号 transaction_id 是 String(32) 1217752501201407033233368018 微信支付订单号
	@JsonProperty("transaction_id")
	private String transactionId;
	// 商户订单号 out_trade_no 是 String(32) 1212321211201407033568112322
	// 商户系统的订单号，与请求一致。
	@JsonProperty("out_trade_no")
	private String outTradeNo;
	// 商家数据包 attach 否 String(128) 123456 商家数据包，原样返回
	private String attach;
	// 支付完成时间 time_end 是 String(14) 20141030133525
	// 支付完成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
	@JsonProperty("time_end")
	private String timeEnd;

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

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getSettlementTotalFee() {
		return settlementTotalFee;
	}

	public void setSettlementTotalFee(String settlementTotalFee) {
		this.settlementTotalFee = settlementTotalFee;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getCashFee() {
		return cashFee;
	}

	public void setCashFee(String cashFee) {
		this.cashFee = cashFee;
	}

	public String getCashFeeType() {
		return cashFeeType;
	}

	public void setCashFeeType(String cashFeeType) {
		this.cashFeeType = cashFeeType;
	}

	public String getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(String couponFee) {
		this.couponFee = couponFee;
	}

	public String getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(String couponCount) {
		this.couponCount = couponCount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

}
