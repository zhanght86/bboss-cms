/**
 *  Copyright 2008 biaoping.yin
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
package com.frameworkset.platform.security.authorization.impl;

/**
 * <p>Title: PermissionToken.java</p>
 *
 * <p>Description: 标识权限资源
 * 资源类型，资源id，资源操作
 * 主要用于URL和资源权限的反向索引
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-7-15
 * @author biaoping.yin
 * @version 1.0
 */
public class PermissionToken {
	private String resourceType;
	private String resourcedID;
	public PermissionToken(String resourceType, String resourcedID,
			String operation) {
		super();
		this.resourceType = resourceType;
		this.resourcedID = resourcedID;
		this.operation = operation;
	}
	private String operation;
	public String getResourceType() {
		return resourceType;
	}
	public String getResourcedID() {
		return resourcedID;
	}
	public String getOperation() {
		return operation;
	}

}
