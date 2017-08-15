package com.frameworkset.platform.sysmgrcore.purviewmanager.action;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.sysmgrcore.purviewmanager.CacheManager;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.ValueObjectUtil;

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
 * Company: bbossgroups
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
//		StringBuffer ret = new StringBuffer();
//		ret.append(clearOrg());
//		ret.append("<br/>").append(clearOrgAdminCache());
//		ret.append("<br/>").append(clearDict());
//		ret.append("<br/>").append(clearPermission());
//		ret.append("<br/>").append(clearRoleCache());
//		ret.append("<br/>").append(clearGroupCache());
//		
//		ret.append("<br/>").append(clearCMSSite2ndChannelCache());
//		ret.append("<br/>").append(clearCMSPublishCache());
//		ret.append("<br/>").append(clearUserCache());
//		ret.append("<br/>").append(this.clearDBMetaCache());
//		ret.append("<br/>").append(clearParamsHandlerCache());
//		
		Event<String> event = new EventImpl<String>(CacheManager.clearAll,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";
		
	}
	public @ResponseBody String clearParamsHandlerCache()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		try{
//			ParamsHandler.cleanAllCache();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除ParamHandler缓存成功");
//		return errorMessage.toString();
		Event<String> event = new EventImpl<String>(CacheManager.clearParamsHandlerCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";
	}
	public @ResponseBody String clearOrg()
	{
		
//		StringBuffer errorMessage = new StringBuffer();
//		try{
//			OrgCacheManager.getInstance().reset();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除机构缓存成功");
//		return errorMessage.toString();
		Event<String> event = new EventImpl<String>(CacheManager.clearOrg,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";		
		
	}
	
	public @ResponseBody String clearOrgAdminCache()
	{
		
//		StringBuffer errorMessage = new StringBuffer();
//		try{
//			OrgAdminCache.getOrgAdminCache().reset();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除机构管理员缓存成功");
//		return errorMessage.toString();
		Event<String> event = new EventImpl<String>(CacheManager.clearOrgAdminCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";				
		
	}
	
	public @ResponseBody String clearDict()
	{
		
//		StringBuffer errorMessage = new StringBuffer();
//		try{
//			DataManagerFactory.getDataManager().reinit();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除字典缓存成功");
//		return errorMessage.toString();		
		Event<String> event = new EventImpl<String>(CacheManager.clearDict,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";			
	}
	
	public @ResponseBody String clearPermission()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		try{
//			AccessControl.resetAuthCache();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		try{
//			
//			AccessControl.resetPermissionCache();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除权限缓存成功");
//		return errorMessage.toString();			
		Event<String> event = new EventImpl<String>(CacheManager.clearPermission,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";			
		
	}
	
	public @ResponseBody String clearRoleCache()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		RoleCacheManager.getInstance().reset();
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除角色缓存成功");
//		return errorMessage.toString();		
		Event<String> event = new EventImpl<String>(CacheManager.clearRoleCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";			
		
		
	}
	public @ResponseBody String clearGroupCache()
	{
		
		
//		StringBuffer errorMessage = new StringBuffer();
//		try{
//			GroupCacheManager.getInstance().reset();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除用户组缓存成功");
//		return errorMessage.toString();					
		Event<String> event = new EventImpl<String>(CacheManager.clearGroupCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";	
		
		
	}
	
	public @ResponseBody String clearCMSSite2ndChannelCache()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		try{
//			try {
//				SQLExecutor.queryObject(int.class,"select 1 from td_cms_site where 1=0");
//				
//			} catch (Exception e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
//				return "清除站点和频道缓存，未安装cms系统";
//			}
//			SiteCacheManager.getInstance().reset();
//			
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除站点和频道缓存成功");
//		return errorMessage.toString();				
		Event<String> event = new EventImpl<String>(CacheManager.clearCMSSite2ndChannelCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";			
		
		
		
	}
	
	
	public @ResponseBody String clearCMSPublishCache()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		try {
//			SQLExecutor.queryObject(int.class,"select 1 from td_cms_site where 1=0");
//			
//		} catch (Exception e) {
//			return "清除站点发布缓存，未安装cms系统";
//		}
//		try{
//			CMSUtil.getCMSDriverConfiguration().getPublishEngine().clearTasks();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		try{
//			ScriptletUtil.resetCache();
//		}catch(Exception e){
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除站点发布缓存成功");
//		return errorMessage.toString();				
		Event<String> event = new EventImpl<String>(CacheManager.clearCMSPublishCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";				
		
		
		
		
		
		
	}
	
	public @ResponseBody String clearUserCache()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		try {
//			UserCacheManager.getInstance().refresh();
//			
//		} catch (Exception e) {
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除用户缓存成功");
//		return errorMessage.toString();				
		Event<String> event = new EventImpl<String>(CacheManager.clearUserCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";				
		
		
		
		
		
		
	}
	
	public @ResponseBody String clearDBMetaCache()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		try {
//			DBUtil.refreshDatabaseMetaData();
//			
//		} catch (Exception e) {
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除数据库元数据缓存成功");
//		return errorMessage.toString();					
		Event<String> event = new EventImpl<String>(CacheManager.clearDBMetaCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";		
		
		
		
		
		
		
	}
	public @ResponseBody String resetPrimaryKeyCache()
	{
//		StringBuffer errorMessage = new StringBuffer();
//		try {
//			DBUtil.refreshDatabaseMetaData();
//			
//		} catch (Exception e) {
//			errorMessage .append(StringUtil.formatBRException(e));
//		}
//		
//		if(errorMessage.length() == 0)
//			errorMessage.append("清除数据库元数据缓存成功");
//		return errorMessage.toString();					
		Event<String> event = new EventImpl<String>(CacheManager.resetPrimaryKeyCache,CacheManager.cacheRefreshEvent);
		
		/**
		 * 事件以同步方式传播
		 */
		
		EventHandle.sendEvent(event);
		return "请求提交成功.";		
		
		
		
		
		
		
	}
	
	
	public @ResponseBody String synAll()
	{
		StringBuffer errorMessage = new StringBuffer();
		try {
			BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("bboss-masterdata-humanResource.xml");
			Object task = context.getBeanObject("masterdata.hrSyncTask");
			ValueObjectUtil.invoke(task, "syncAllData", null);
//			task.syncAllData();//同步用户数据
		} catch (Exception e) {
			errorMessage .append(StringUtil.formatBRException(e));
		}
		
		if(errorMessage.length() == 0)
			errorMessage.append("同步用户组织岗位数据成功");
		return errorMessage.toString();		
		
		
		
		
		
		
	}
	
	
	
	

}
