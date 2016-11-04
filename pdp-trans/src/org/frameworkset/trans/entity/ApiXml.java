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
 * <p>Title: ApiXml</p> <p>Description: 传输数据管理服务实体类 </p> <p>wowo</p>
 * <p>Copyright (c) 2007</p> @Date 2016-10-27 00:34:11 @author suwei @version
 * v1.0
 */
public class ApiXml implements java.io.Serializable {
	private int id;
	/**
	 * 备注
	 */
	private String comment;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 传输数据包
	 */
	private String data;
	/**
	 * 请求ip地址
	 */
	private String srcIp;
	/**
	 * 状态0 成功 1失败
	 */
	private String state;
	/**
	 * w0 调用微信数据包  w1 微信回调数据包
	 */
	private String type;
	public ApiXml() {
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}

	public String getSrcIp() {
		return srcIp;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}