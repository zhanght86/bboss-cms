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

package org.frameworkset.trans.entity;

import java.sql.Timestamp;
/**
 * <p>Title: CmsWxtoken</p> <p>Description: token管理服务实体类 </p> <p>wowo</p>
 * <p>Copyright (c) 2007</p> @Date 2016-11-12 15:44:26 @author suwei @version
 * v1.0
 */
public class CmsWxtoken implements java.io.Serializable {
	/**
	 * 主键id
	 */
	private long id;
	/**
	 * 微信的access_token
	 */
	private String accessToken;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 失效时间
	 */
	private Timestamp endTime;
	/**
	 * token有效秒数
	 */
	private long expiresIn;
	/**
	 * 状态 0 有效 1失效
	 */
	private int state;
	public CmsWxtoken() {
	}
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}

}