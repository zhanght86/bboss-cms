package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import java.sql.SQLException;

import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.platform.cms.driver.publish.impl.ScriptletUtil;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.manager.db.GroupCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdminCache;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.RoleCacheManager;
import com.frameworkset.util.StringUtil;

/**
 * <p>
 * Title: CacheController.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2012-12-20 下午2:51:42
 * @author biaoping.yin
 * @version 1.0.0
 */
public  class CacheController {

	public String cache_console()
	{
		return "path:cache_console";
	}
	public @ResponseBody String clearAll()
	{
		StringBuffer ret = new StringBuffer();
		ret.append(clearOrg());
		ret.append("<br/>").append(clearOrgAdminCache());
		ret.append("<br/>").append(clearDict());
		ret.append("<br/>").append(clearPermission());
		ret.append("<br/>").append(clearRoleCache());
		ret.append("<br/>").append(clearGroupCache());
		
			ret.append("<br/>").append(clearCMSSite2ndChannelCache());
			ret.append("<br/>").append(clearCMSPublishCache());
		
		
		
		return ret.toString();
		
	}
	public @ResponseBody String clearOrg()
	{
		
		StringBuffer errorMessage = new StringBuffer();
		try{
			OrgCacheManager.getInstance().reset();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除机构缓存成功");
		return errorMessage.toString();		
		
	}
	
	public @ResponseBody String clearOrgAdminCache()
	{
		
		StringBuffer errorMessage = new StringBuffer();
		try{
			OrgAdminCache.getOrgAdminCache().reset();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除机构管理员缓存成功");
		return errorMessage.toString();		
		
	}
	
	public @ResponseBody String clearDict()
	{
		
		StringBuffer errorMessage = new StringBuffer();
		try{
			DataManagerFactory.getDataManager().reinit();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除字典缓存成功");
		return errorMessage.toString();		
	}
	
	public @ResponseBody String clearPermission()
	{
		StringBuffer errorMessage = new StringBuffer();
		try{
			AccessControl.resetAuthCache();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		try{
			
			AccessControl.resetPermissionCache();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除权限缓存成功");
		return errorMessage.toString();		
		
	}
	
	public @ResponseBody String clearRoleCache()
	{
		StringBuffer errorMessage = new StringBuffer();
		RoleCacheManager.getInstance().reset();
		if(errorMessage.length() == 0)
			errorMessage.append("清除角色缓存成功");
		return errorMessage.toString();		
		
		
	}
	public @ResponseBody String clearGroupCache()
	{
		
		
		StringBuffer errorMessage = new StringBuffer();
		try{
			GroupCacheManager.getInstance().reset();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除用户组缓存成功");
		return errorMessage.toString();			
		
		
	}
	
	public @ResponseBody String clearCMSSite2ndChannelCache()
	{
		StringBuffer errorMessage = new StringBuffer();
		try{
			try {
				SQLExecutor.queryObject(int.class,"select 1 from td_cms_site where 1=0");
				
			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
				return "清除站点和频道缓存，未安装cms系统";
			}
			SiteCacheManager.getInstance().reset();
			
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除站点和频道缓存成功");
		return errorMessage.toString();		
		
		
		
	}
	
	
	public @ResponseBody String clearCMSPublishCache()
	{
		StringBuffer errorMessage = new StringBuffer();
		try {
			SQLExecutor.queryObject(int.class,"select 1 from td_cms_site where 1=0");
			
		} catch (Exception e) {
			return "清除站点发布缓存，未安装cms系统";
		}
		try{
			CMSUtil.getCMSDriverConfiguration().getPublishEngine().clearTasks();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		try{
			ScriptletUtil.resetCache();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除站点发布缓存成功");
		return errorMessage.toString();		
		
		
		
		
		
		
	}
	
	
	
	

}
