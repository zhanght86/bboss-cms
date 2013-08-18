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

import java.util.HashMap;
import java.util.Map;

import com.frameworkset.platform.config.ConfigManager;

/**
 * <p>Title: PermissionTokenMap.java</p>
 *
 * <p>Description: 权限相关联资源，比如菜单管理的url</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-8-13
 * @author biaoping.yin
 * @version 1.0
 */
public class PermissionTokenMap {
	/**
	 * Map<String,        PermissionTokenRegion>
	 *     resourceType(资源类型划分)       资源权限区域
	 */
	private Map<String,PermissionTokenRegion> resourcTokenMap;
	public PermissionTokenMap ()
	{
		resourcTokenMap = new HashMap<String,PermissionTokenRegion>();
	}
	public void addPermissionToken(String url,PermissionToken token)
	{
		String resourctType = token.getResourceType();
		
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens == null)
		{
			resourceTokens = new PermissionTokenRegion();
			this.resourcTokenMap.put(resourctType, resourceTokens);
		}
		resourceTokens.addPermissionToken(url, token);
		
	}
	
	public void addPermissionToken(String url,String region,PermissionToken token)
	{
		String resourctType = token.getResourceType();
		
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens == null)
		{
			resourceTokens = new PermissionTokenRegion();
			this.resourcTokenMap.put(resourctType, resourceTokens);
		}
		resourceTokens.addPermissionToken(url, region,token);
		
		
		
	}
	public void addUnprotectedPermissionToken(String url,String region,PermissionToken token)
	{
		String resourctType = token.getResourceType();
		
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens == null)
		{
			resourceTokens = new PermissionTokenRegion();
			this.resourcTokenMap.put(resourctType, resourceTokens);
		}
		resourceTokens.addUnprotectedPermissionToken(url, region,token);
		
		
		
	}
	
	public void resetPermissionByResourceType(String resourctType)
	{
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens != null)
			resourceTokens.resetPermission( );
	}
	
	public void resetPermissionByRegion(String resourctType,String region)
	{
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens != null)
			resourceTokens.resetPermissionByRegion(  region);
	}
	
	/**
	 * 判断url资源是否有访问权限
	 * @param url
	 * @param resourceType
	 * @return
	 */
	public boolean checkUrlPermission(String url,String resourceType)
	{
		if (!ConfigManager.getInstance().securityEnabled() )
			return true;
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourceType);
		if(resourceTokens == null)
		{
			if (BaseAccessManager._allowIfNoRequiredRoles(resourceType))
				return true;
			return true;
		}
		return resourceTokens.checkUrlPermission(url,resourceType);
	}
	
	
	
	/**
	 * 判断url资源是否有访问权限
	 * @param url
	 * @param resourceType
	 * @return
	 */
	public boolean checkUrlPermission(String url)
	{
		return checkUrlPermission(url,"column");		
	}

}
