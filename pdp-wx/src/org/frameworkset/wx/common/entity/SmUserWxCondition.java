/**
 *  Copyright 2008-2010 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.frameworkset.wx.common.entity;

import java.sql.Timestamp;
/**
 * <p>Title: SmUserWxCondition</p> <p>Description: wx管理查询条件实体类 </p> <p>wowo</p>
 * <p>Copyright (c) 2007</p> @Date 2017-01-18 23:42:32 @author suwei @version
 * v1.0
 */
public class SmUserWxCondition implements java.io.Serializable {
	private long wxid;
	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	private String accessToken;
	/**
	 * 普通用户个人资料填写的城市
	 */
	private String city;
	/**
	 * 国家，如中国为CN
	 */
	private String country;
	private Timestamp createTime_start;
	private Timestamp createTime_end;
	/**
	 * 错误代码
	 */
	private int errcode;
	/**
	 * 错误信息
	 */
	private String errmsg;
	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	private String expiresIn;
	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。
	 * 若用户更换头像，原有头像URL将失效。
	 */
	private String headimgurl;
	/**
	 * 用户昵称
	 */
	private String nickname;
	/**
	 * 用户的唯一标识
	 */
	private String openid;
	/**
	 * 用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	 */
	private String privilege;
	/**
	 * 用户个人资料填写的省份
	 */
	private String province;
	private String refreshToken;
	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	private String scope;
	/**
	 * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	 */
	private String sex;
	/**
	 * 0 成功 1失败
	 */
	private String state;
	/**
	 * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
	 */
	private String unionid;
	/**
	 * td_sm_user表的主建user_id
	 */
	private long userId;
	public SmUserWxCondition() {
	}
	public void setWxid(long wxid) {
		this.wxid = wxid;
	}

	public long getWxid() {
		return wxid;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public void setCreateTime_start(Timestamp createTime_start) {
		this.createTime_start = createTime_start;
	}

	public Timestamp getCreateTime_start() {
		return createTime_start;
	}
	public void setCreateTime_end(Timestamp createTime_end) {
		this.createTime_end = createTime_end;
	}

	public Timestamp getCreateTime_end() {
		return createTime_end;
	}

	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public int getErrcode() {
		return errcode;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
		return province;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return scope;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getSex() {
		return sex;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

}