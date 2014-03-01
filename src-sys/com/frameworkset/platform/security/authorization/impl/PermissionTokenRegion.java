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
import com.frameworkset.util.StringUtil;

/**
 * <p>Title: PermissionTokenRegion.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date 2013-8-14
 * @author biaoping.yin
 * @version 1.0
 */
public class PermissionTokenRegion {
	/**
	 * Map<String,        Map<String,         List<PermissionToken>>>
	 *     region(资源区域划分)       url（或其他）              资源类型/资源标识/资源操作(一个url可能会对应多个资源操作)
	 */
	private Map<String,Map<RID,List<PermissionToken>>> regionResourcTokenMap;
	
	private Map<RID,List<PermissionToken>> resourcTokenMap;
	
	private Map<String,Map<RID,Object>> regionUnprotectedResourcTokenMap;
	
	private Map<RID,Object> resourcUnprotectedTokenMap;
	public PermissionTokenRegion ()
	{
		regionResourcTokenMap = new HashMap<String,Map<RID,List<PermissionToken>>>();
		resourcTokenMap = new HashMap<RID,List<PermissionToken>>();
		
		regionUnprotectedResourcTokenMap = new HashMap<String,Map<RID,Object>>();		
		resourcUnprotectedTokenMap = new HashMap<RID,Object>();
	}
	public void addPermissionToken(String url,PermissionToken token)
	{
		if(url == null || url.equals(""))
			return;
		if(!url.startsWith("/"))
		{
			url = "/"+url;
		}
		int idx = url.indexOf("?");
		if(idx > 0)
		{
			url = url.substring(0,idx);
		}
			
		Map<RID,List<PermissionToken>> resourceTokens = resourcTokenMap;
		
		RID rid = new RID(url,true);
		List<PermissionToken> tokens = resourceTokens.get(rid);
		if(tokens == null)
		{
			tokens = new ArrayList<PermissionToken>();
			resourceTokens.put(rid, tokens);
					
		}
		tokens.add(token);
		
	}
	public void addPermissionToken(String url,String region,PermissionToken token)
	{
		if(url == null || url.equals(""))
			return;
		if(!url.startsWith("/"))
		{
			url = "/"+url;
		}
		int idx = url.indexOf("?");
		if(idx > 0)
		{
			url = url.substring(0,idx);
		}
		
		idx = url.indexOf("{");
		if(idx > 0)
		{
			String temp = url;
			url = temp.substring(0,idx);
			String condition = temp.substring(idx);
			condition = condition.replace('|', ',');
			HashMap map = StringUtil.json2Object(condition,HashMap.class);
			if(map != null && map.size() > 0)
				token.setConditions(map);
			
		}
		Map<RID,List<PermissionToken>> resourceTokens = this.regionResourcTokenMap.get(region);
		if(resourceTokens == null)
		{
			resourceTokens = new HashMap<RID,List<PermissionToken>>();
			this.regionResourcTokenMap.put(region, resourceTokens);
		}
		
		RID rid = new RID(url,true);
		List<PermissionToken> tokens = resourceTokens.get(rid);
		if(tokens == null)
		{
			tokens = new ArrayList<PermissionToken>();
			resourceTokens.put(rid, tokens);
					
		}
		tokens.add(token);
		
	}
	
	public void resetPermission()
	{
		if(resourcTokenMap != null)
		{
			resourcTokenMap.clear();
		}
		
		if(regionResourcTokenMap != null)
		{
			Iterator<Entry<String, Map<RID, List<PermissionToken>>>> entries = this.regionResourcTokenMap.entrySet().iterator();
			while(entries.hasNext())
			{
				Entry<String, Map<RID, List<PermissionToken>>> entry = entries.next();
				Map<RID, List<PermissionToken>> resourceTokens = entry.getValue();
				resourceTokens.clear();
			}
			regionResourcTokenMap.clear();
		}
		this.resourcUnprotectedTokenMap.clear();
		Iterator<Entry<String, Map<RID, Object>>> uentries = this.regionUnprotectedResourcTokenMap.entrySet().iterator();
		while(uentries.hasNext())
		{
			Entry<String, Map<RID, Object>> entry = uentries.next();
			entry.getValue().clear();
		}
		this.regionUnprotectedResourcTokenMap.clear();
	}
	
	public void resetPermissionByRegion(String region)
	{		
		if(regionResourcTokenMap != null)
		{
			Map<RID,List<PermissionToken>> resourceTokens = regionResourcTokenMap.get(region);
			
			if(resourceTokens != null)
				resourceTokens.clear();
			
		}
		Map<RID,Object> resourceTokens = this.regionUnprotectedResourcTokenMap.get(region);
		if(resourceTokens != null)
			resourceTokens.clear();
	}
	
	public boolean isUnprotectedURL(RID rid)
	{
		boolean successed = false;
		if(resourcUnprotectedTokenMap.size() > 0 && this.resourcUnprotectedTokenMap.containsKey(rid))
		{
			successed = true;
		}
		else 
		{
			Iterator<Entry<String, Map<RID, Object>>> uentries = this.regionUnprotectedResourcTokenMap.entrySet().iterator();
			while(uentries.hasNext())
			{
				Entry<String, Map<RID, Object>> entry = uentries.next();
				if(entry.getValue().containsKey(rid))
				{
					successed = true;
					break;
				}
					
			}
		}
		return successed;
	}
	private class Flag
	{
		boolean touched = false;
	}
	/**
	 * 判断url资源是否有访问权限,模式和精确匹配，精确匹配优先
	 * @param url
	 * @param resourceType
	 * @return
	 */
	public boolean checkUrlPermission(String url,String resourceType)
	{
		if (!ConfigManager.getInstance().securityEnabled() )
			return true;
		Map<RID,List<PermissionToken>> resourceTokens = this.resourcTokenMap;
		if((resourceTokens.size() == 0) 
				&& (this.regionResourcTokenMap.size() == 0) 
				//&& this.regionUnprotectedResourcTokenMap.size() == 0 && this.resourcUnprotectedTokenMap.size() == 0
				)
		{
			if (BaseAccessManager._allowIfNoRequiredRoles(resourceType))
				return true;
			return true;
		}
		RID rid = new RID(url);
		Boolean successed = isUnprotectedURL(rid);
		if(successed)
			return true;
		Flag flag = new Flag();
		if(resourceTokens.size() > 0)
		{
			List<PermissionToken> tokens = resourceTokens.get(rid);
			
			if(tokens == null)
			{		
				
				successed = checkRegionUrlPermission( rid, resourceType,flag);
			}
			else
			{
				flag.touched = true;
				for(PermissionToken token:tokens)
				{
					if(AccessControl.getAccessControl().checkPermission(token.getResourcedID(), token.getOperation(), token.getResourceType()))
					{
						successed = true;
						break;
					}
				}
				if(!successed)
				{
					successed = checkRegionUrlPermission( rid, resourceType,flag);
				}
			}
		}
		else
		{
			successed = checkRegionUrlPermission( rid, resourceType,flag);
		}
		if(flag.touched)
			return successed;
		else
			return true;
		
	}
	private Boolean checkRegionUrlPermission(RID rid,String resourceType,Flag flag)
	{
		if((this.regionResourcTokenMap == null || this.regionResourcTokenMap.size() == 0))
			return true;
		Iterator<Entry<String, Map<RID, List<PermissionToken>>>> entries = this.regionResourcTokenMap.entrySet().iterator();
		Boolean successed = new Boolean(false);
label:	while(entries.hasNext())
		{
			Entry<String, Map<RID, List<PermissionToken>>> entry = entries.next();
			Map<RID, List<PermissionToken>> resourceTokens = entry.getValue();
			List<PermissionToken> tokens = resourceTokens.get(rid);
			if(tokens == null)
			{		
				
				continue;
			}
			else
			{
				flag.touched = true;
				for(PermissionToken token:tokens)
				{
					if(AccessControl.getAccessControl().checkPermission(token.getResourcedID(), token.getOperation(), token.getResourceType()))
					{
						successed = true;
						break label;
					}
				}
			}
		}
		return successed;
		
		
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
	private static Object dual = new Object();
	public void addUnprotectedPermissionToken(String url, String region,
			PermissionToken token) {
		if(url == null || url.equals(""))
			return;
		if(!url.startsWith("/"))
		{
			url = "/"+url;
		}
		int idx = url.indexOf("?");
		if(idx > 0)
		{
			url = url.substring(0,idx);
		}
		Map<RID,Object> regionTokenMap = this.regionUnprotectedResourcTokenMap.get(region);
		if(regionTokenMap == null)
		{
			regionTokenMap = new HashMap<RID,Object>();
			this.regionUnprotectedResourcTokenMap.put(region,regionTokenMap);
		}
		regionTokenMap.put(new RID(url,true), dual);
		
	}
	
	public void addUnprotectedPermissionToken(String url, 
			PermissionToken token) {
		if(url == null || url.equals(""))
			return;
		if(!url.startsWith("/"))
		{
			url = "/"+url;
		}
		int idx = url.indexOf("?");
		if(idx > 0)
		{
			url = url.substring(0,idx);
		}
		this.resourcUnprotectedTokenMap.put(new RID(url,true), dual);
		
	}
	
	public List<PermissionToken> getAllURLToken(RID rid) {
		List<PermissionToken> ptokens = new ArrayList<PermissionToken>();
		if(resourcTokenMap.size() > 0)
		{
			List<PermissionToken> tokens = new ArrayList<PermissionToken>();
			Iterator<Entry<RID, List<PermissionToken>>> resources = resourcTokenMap.entrySet().iterator();
			while(resources.hasNext())
			{
				Entry<RID, List<PermissionToken>> entry = resources.next();
				if(entry.getKey().match(rid))
					tokens.addAll(entry.getValue());
			}
			if(tokens.size() > 0)
			{
				ptokens.addAll(tokens);
				
			}
		}
		if((this.regionResourcTokenMap == null || this.regionResourcTokenMap.size() == 0))
			return ptokens;
		Iterator<Entry<String, Map<RID, List<PermissionToken>>>> entries = this.regionResourcTokenMap.entrySet().iterator();		
		while(entries.hasNext())
		{
			Entry<String, Map<RID, List<PermissionToken>>> entry = entries.next();
			Map<RID, List<PermissionToken>> resourceTokens_ = entry.getValue();
			List<PermissionToken> tokens = new ArrayList<PermissionToken>();
			Iterator<Entry<RID, List<PermissionToken>>> resources = resourceTokens_.entrySet().iterator();
			while(resources.hasNext())
			{
				Entry<RID, List<PermissionToken>> entry_= resources.next();
				if(entry_.getKey().match(rid))
					tokens.addAll(entry_.getValue());
			}
			if(tokens.size() > 0)
			{
				ptokens.addAll(tokens);				
			}
		}
		return ptokens;
	}


}
