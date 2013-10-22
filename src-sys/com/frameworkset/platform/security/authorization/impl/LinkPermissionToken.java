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

import java.util.List;

/**
 * <p>Title: LinkPermissionToken.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-10-21
 * @author biaoping.yin
 * @version 1.0
 */
public class LinkPermissionToken {
	private String url;
	private boolean unprotected;
	private List<PermissionToken> permissionTokens;
	public LinkPermissionToken(String url, boolean unprotected,
			List<PermissionToken> permissionTokens) {
		super();
		this.url = url;
		this.unprotected = unprotected;
		this.permissionTokens = permissionTokens;
	}
	public LinkPermissionToken() {
		// TODO Auto-generated constructor stub
	}
	public String getUrl() {
		return url;
	}
	public boolean isUnprotected() {
		return unprotected;
	}
	public List<PermissionToken> getPermissionTokens() {
		return permissionTokens;
	}

}
