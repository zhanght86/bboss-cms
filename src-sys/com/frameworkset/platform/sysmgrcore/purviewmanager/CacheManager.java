package com.frameworkset.platform.sysmgrcore.purviewmanager;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.event.SimpleEventType;
import org.frameworkset.util.ParamsHandler;

import com.frameworkset.common.poolman.DBUtil;
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
import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;
import com.frameworkset.util.StringUtil;

public class CacheManager  implements Listener<String>{
	private static final Logger log = Logger.getLogger(CacheManager.class);
	public static final String clearAll = "clearAll";
	public static final String clearOrg = "clearOrg";
	public static final String refreshMasterData = "refreshMasterData";
	
	public static final String clearOrgAdminCache = "clearOrgAdminCache";
	public static final String clearDict = "clearDict";
	public static final String clearPermission = "clearPermission";
	public static final String clearRoleCache = "clearRoleCache";
	public static final String clearGroupCache = "clearGroupCache";
	public static final String clearCMSSite2ndChannelCache = "clearCMSSite2ndChannelCache";
	public static final String clearCMSPublishCache = "clearCMSPublishCache";
	public static final String clearUserCache = "clearUserCache";
	public static final String clearDBMetaCache = "clearDBMetaCache";
	public static final String clearParamsHandlerCache = "clearParamsHandlerCache";
	public static final SimpleEventType cacheRefreshEvent = new SimpleEventType("cacheRefreshEvent");
	
	public static void registRefreshEventListener()
	{
		
		NotifiableFactory.getNotifiable().addListener(new CacheManager(), cacheRefreshEvent);
	}

	@Override
	public void handle(Event<String> e) {
		String event = e.getSource();
		log.debug("CacheManager receive event["+event+"] .");	
		String result = "";
		
		if(event.equals(clearAll))
		{
			result = this.clearAll();
		}
		else if(event.equals(clearOrg))
		{
			result = this.clearOrg();
		}
		else if(event.equals(clearOrgAdminCache))
		{
			result = this.clearOrgAdminCache();
		}
		else if(event.equals(clearDict))
		{
			result = this.clearDict();
		}
		else if(event.equals(clearPermission))
		{
			result = this.clearPermission();
		}
		else if(event.equals(clearRoleCache))
		{
			result = this.clearRoleCache();
		}
		else if(event.equals(clearGroupCache))
		{
			result = this.clearGroupCache();
		}
		else if(event.equals(clearCMSSite2ndChannelCache))
		{
			result = this.clearCMSSite2ndChannelCache();
		}
		else if(event.equals(clearCMSPublishCache))
		{
			result = this.clearCMSPublishCache();
		}
		else if(event.equals(clearUserCache))
		{
			result = this.clearUserCache();
		}
		else if(event.equals(clearDBMetaCache))
		{
			result = this.clearDBMetaCache();
		}
		else if(event.equals(clearParamsHandlerCache))
		{
			result = this.clearParamsHandlerCache();
		}
		else if(event.equals(refreshMasterData))
		{
			result = this.refreshMasterData();
		}
		log.debug("CacheManager hand event["+event+"] result:\r\n"+result);	
		
	}
	
	public  String clearAll()
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
		ret.append("<br/>").append(clearUserCache());
		ret.append("<br/>").append(this.clearDBMetaCache());
		ret.append("<br/>").append(clearParamsHandlerCache());
		
		
		return ret.toString();
		
	}
	public  String clearParamsHandlerCache()
	{
		StringBuffer errorMessage = new StringBuffer();
		try{
			ParamsHandler.cleanAllCache();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除ParamHandler缓存成功");
		return errorMessage.toString();
	}
	public  String clearOrg()
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
	
	public  String clearOrgAdminCache()
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
	
	public  String clearDict()
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
	
	public String clearPermission()
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
	
	public String clearRoleCache()
	{
		StringBuffer errorMessage = new StringBuffer();
		RoleCacheManager.getInstance().reset();
		if(errorMessage.length() == 0)
			errorMessage.append("清除角色缓存成功");
		return errorMessage.toString();		
		
		
	}
	public String clearGroupCache()
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
	
	public String clearCMSSite2ndChannelCache()
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
	
	
	public String clearCMSPublishCache()
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
	
	public String clearUserCache()
	{
		StringBuffer errorMessage = new StringBuffer();
		try {
			UserCacheManager.getInstance().refresh();
			
		} catch (Exception e) {
			errorMessage .append(StringUtil.formatBRException(e));
		}
		
		if(errorMessage.length() == 0)
			errorMessage.append("清除用户缓存成功");
		return errorMessage.toString();		
		
		
		
		
		
		
	}
	
	public String clearDBMetaCache()
	{
		StringBuffer errorMessage = new StringBuffer();
		try {
			DBUtil.refreshDatabaseMetaData();
			
		} catch (Exception e) {
			errorMessage .append(StringUtil.formatBRException(e));
		}
		
		if(errorMessage.length() == 0)
			errorMessage.append("清除数据库元数据缓存成功");
		return errorMessage.toString();		
		
	}
	
	public String refreshMasterData()
	{
		    log.info("Refresh HR master user cache data ...");
			UserCacheManager.getInstance().refresh();
			log.info("Refresh HR master organization cache data ...");
			OrgCacheManager.getInstance().reset();
			log.info("Refresh HR master organization administrators cache data ...");
			OrgAdminCache.getOrgAdminCache().reset();
			return "刷新用户/机构/机构管理员缓存完成";
	}

}
