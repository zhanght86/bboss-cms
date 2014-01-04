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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;

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
	
	/**
	 * 与url相关的系统中所有资源操作许可
	 * 当进行权限检测时，首先检测url相关的所有资源许可中的任何一个有操作权限，则说明用户有权限访问相应的资源
	 */
	private Map<String,LinkPermissionToken> protectedURLLinkPermissions;
	
	/**
	 * 与url相关的系统中所有资源操作许可
	 * 当进行权限检测时，首先检测url相关的所有资源许可中的任何一个有操作权限，则说明用户有权限访问相应的资源
	 */
	private Map<String,LinkPermissionToken> nullURLLinkPermissions;
	
	/**
	 * 与url相关的系统中所有资源操作许可
	 * 当进行权限检测时，首先检测url相关的所有资源许可中的任何一个有操作权限，则说明用户有权限访问相应的资源
	 */
	private Map<String,LinkPermissionToken> unprotectedURLLinkPermissions;
	/**
	 * 受保护url缓存区大小，超过大小时protectedURLLinkPermissions将被重置
	 */
	private int protectedURLLinkPermissionLimit = 100000;
	
	
	/**
	 * 不受保护url缓存区大小，超过大小时unprotectedURLLinkPermissions将被重置
	 */
	private int unprotectedURLLinkPermissionLimit = 200000;
	/**
	 *非配置url权限缓存区大小，超过大小时nullURLLinkPermissions将被重置
	 */
	private int nullURLLinkPermissionLimit = 200000;
	
	public PermissionTokenMap ()
	{
		resourcTokenMap = new HashMap<String,PermissionTokenRegion>();
		
		protectedURLLinkPermissions = new HashMap<String,LinkPermissionToken>();
		unprotectedURLLinkPermissions = new HashMap<String,LinkPermissionToken>();
		nullURLLinkPermissions = new HashMap<String,LinkPermissionToken>();
		
	}
	public PermissionTokenMap (int protectedURLLinkPermissionLimit,int unprotectedURLLinkPermissionLimit,int nullURLLinkPermissionLimit)
	{
		resourcTokenMap = new HashMap<String,PermissionTokenRegion>();
		
		protectedURLLinkPermissions = new HashMap<String,LinkPermissionToken>();
		unprotectedURLLinkPermissions = new HashMap<String,LinkPermissionToken>();
		this.protectedURLLinkPermissionLimit = protectedURLLinkPermissionLimit;
		this.unprotectedURLLinkPermissionLimit = unprotectedURLLinkPermissionLimit;
		this.nullURLLinkPermissionLimit = nullURLLinkPermissionLimit;
		
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
	public void addUnprotectedPermissionToken(String url,PermissionToken token)
	{
		String resourctType = token.getResourceType();
		
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens == null)
		{
			resourceTokens = new PermissionTokenRegion();
			this.resourcTokenMap.put(resourctType, resourceTokens);
		}
		resourceTokens.addUnprotectedPermissionToken(url, token);
		
		
		
	}
	public void resetPermissionByResourceType(String resourctType)
	{
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens != null)
		{
			resourceTokens.resetPermission( );
			clearAllURLLinkPermissions();
		}
	}
	
	public void resetPermissionByRegion(String resourctType,String region)
	{
		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourctType);
		if(resourceTokens != null)
		{
			resourceTokens.resetPermissionByRegion(  region);
			clearAllURLLinkPermissions();
		}
	}
	/**
	 * 如果有修改，需要重新加载每个url的相关权限许可，只要资源定义有调整就需要需要调用这个方法
	 */
	private void clearAllURLLinkPermissions()
	{
		synchronized(this.scanlock)//如果有修改，需要重新加载每个url的相关权限许可，只要资源定义有调整就需要需要调用这个方法
		{
			this.protectedURLLinkPermissions.clear();
			this.unprotectedURLLinkPermissions.clear();
			this.nullURLLinkPermissions.clear();
		}
	}
	
	
//	/**
//	 * 判断url资源是否有访问权限
//	 * @param url
//	 * @param resourceType 优先检测给定类型的包含url相关的资源权限
//	 * @return
//	 */
//	public boolean checkUrlPermission(String url,String resourceType)
//	{
//		if (!ConfigManager.getInstance().securityEnabled() )
//			return true;
//		PermissionTokenRegion resourceTokens = this.resourcTokenMap.get(resourceType);
//		if(resourceTokens == null)
//		{
//			if (BaseAccessManager._allowIfNoRequiredRoles(resourceType))
//				return true;
//			return true;
//		}
//		return resourceTokens.checkUrlPermission(url,resourceType);
//	}
	
	private LinkPermissionToken NULL_PERMISSIONTOKENS = new LinkPermissionToken();
	
	/**
	 * 判断url资源是否有访问权限
	 * @param url
	 * @param resourceType
	 * @return
	 */
	public boolean checkUrlPermission(String url)
	{
		if (!ConfigManager.getInstance().securityEnabled() )
			return true;
		if(AccessControl.getAccessControl().isAdmin())
			return true;
		LinkPermissionToken ptokens = scanUrlPermissionTokens(url);
		
		if(NULL_PERMISSIONTOKENS == ptokens || ptokens.isUnprotected())
			return true;
		else
		{
			boolean successed = false;
			for(PermissionToken token:ptokens.getPermissionTokens())
			{
				if(AccessControl.getAccessControl().checkPermission(token.getResourcedID(), token.getOperation(), token.getResourceType()))
				{
					successed = true;
					break;
				}
			}
			return successed;
		}
		
				
	}
	private LinkPermissionToken _getToken(String url)
	{
		LinkPermissionToken ptokens = this.protectedURLLinkPermissions.get(url);
		if(ptokens != null)
		{
			return ptokens;
		}
		else
		{
			ptokens = this.unprotectedURLLinkPermissions.get(url);
		}
		
		if(ptokens != null)
		{
			return ptokens;
		}
		else
		{
			ptokens = this.nullURLLinkPermissions.get(url);
		}
		return ptokens;
		
	}
	private Object scanlock = new Object();
	/**
	 * 扫描系统中和url相关的所有url资源
	 * @param url
	 */
	private LinkPermissionToken scanUrlPermissionTokens(String url) {
		LinkPermissionToken ptokens = _getToken(url);
		
		
		if(ptokens != null)
		{
			return ptokens;
		}	
		else
		{
			RID id = new RID(url);
			synchronized(scanlock)
			{
				ptokens = _getToken(url);
				if(ptokens != null)
				{
					return ptokens;
				}
				Iterator<Entry<String, PermissionTokenRegion>> resources = this.resourcTokenMap.entrySet().iterator();
				//首先进行url的未受保护资源扫描
				while(resources.hasNext())
				{
					Entry<String, PermissionTokenRegion> entry = resources.next();
					PermissionTokenRegion region = entry.getValue();
					if(region.isUnprotectedURL(id))
					{
						ptokens = new LinkPermissionToken(url,true,null);
						if(unprotectedURLLinkPermissionLimit > 0 && this.unprotectedURLLinkPermissions.size() > this.unprotectedURLLinkPermissionLimit)
						{
							this.unprotectedURLLinkPermissions.clear();
						}
						this.unprotectedURLLinkPermissions.put(url, ptokens);
						break;
					}
						
				}
				if(ptokens == null )//如果不是未受保护资源，则扫描所有区域的相关资源
				{
					resources = this.resourcTokenMap.entrySet().iterator();
					List<PermissionToken> tokes = new ArrayList<PermissionToken>();
					while(resources.hasNext())
					{
						Entry<String, PermissionTokenRegion> entry = resources.next();
						PermissionTokenRegion region = entry.getValue();
						List<PermissionToken> rtokens = region.getAllURLToken(id);
						if(rtokens != null && rtokens.size() > 0)
						{
							tokes.addAll(rtokens);
						}
							
					}
					if(tokes.size() == 0)
					{
						if(nullURLLinkPermissionLimit > 0 && this.nullURLLinkPermissions.size() > this.nullURLLinkPermissionLimit)
						{
							this.nullURLLinkPermissions.clear();
						}
						ptokens = NULL_PERMISSIONTOKENS;
						nullURLLinkPermissions.put(url, ptokens);
					}
					else
					{
						removeSamePermissions(tokes );
						ptokens = new LinkPermissionToken(url,false,tokes);
						if(protectedURLLinkPermissionLimit > 0 && this.protectedURLLinkPermissions.size() > this.protectedURLLinkPermissionLimit)
						{
							this.protectedURLLinkPermissions.clear();
						}
						protectedURLLinkPermissions.put(url, ptokens);
						
					}
					
				}
			}
			return ptokens;
		}		
	}
	/**
	 * 资源去重
	 * @param tokes
	 */
	private void removeSamePermissions(List<PermissionToken> tokes )
	{
		if(tokes.size() == 1)
			return ;
		
		List<PermissionToken> removePermissionTokens = new ArrayList<PermissionToken>();
		for(int i = 0; i < tokes.size(); i ++)
		{
			PermissionToken first = tokes.get(i);
			if(removePermissionTokens.contains(first))
				continue;
			for(int j = i + 1; j < tokes.size(); j ++)
			{	
				PermissionToken second = tokes.get(j);
				if(removePermissionTokens.contains(second))
					continue;
				
				if(first.getOperation().equals(second.getOperation()) 
						&& first.getResourcedID().equals(second.getResourcedID())
						&& first.getResourceType().equals(second.getResourceType()))
				{	
					removePermissionTokens.add(second);
				}
			}
			
		}
		for(PermissionToken first:removePermissionTokens)
		{
			tokes.remove(first);
		}
		
		
	}
	public void destory() {
		if(nullURLLinkPermissions != null)
		{
			this.nullURLLinkPermissions.clear();
			nullURLLinkPermissions = null;
		}
		if(protectedURLLinkPermissions != null)
		{
			this.protectedURLLinkPermissions.clear();
			protectedURLLinkPermissions = null;
		}
		if(resourcTokenMap != null)
		{
			this.resourcTokenMap.clear();
			resourcTokenMap = null;
		}
		if(unprotectedURLLinkPermissions != null)
		{
			this.unprotectedURLLinkPermissions.clear();
			unprotectedURLLinkPermissions = null;
		}
		
	}

}
