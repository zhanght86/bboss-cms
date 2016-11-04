/**
 * 
 */
package org.frameworkset.wx.common.entity;

import org.frameworkset.wx.common.enums.EnumWeiXinErrorCode;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author suwei
 * @date 2016年10月14日
 *
 */
public class OAuthSnsAPIBase {
	@JsonProperty("access_token")
	private String accessToken; // 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	@JsonProperty("expires_in")
	private int expiresIn; // access_token接口调用凭证超时时间，单位（秒）
	@JsonProperty("refresh_token")
	private String refreshToken; // 用户刷新access_token
	@JsonProperty("openid")
	private String openId; // 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	private String scope; // 用户授权的作用域，使用逗号（,）分隔
	private String unionid; // 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	private int errcode;
	private String errmsg;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return EnumWeiXinErrorCode.getEnumTradeTypeBycode(errcode).getName();
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

}
