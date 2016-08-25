package com.frameworkset.platform.sysmgrcore.purviewmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.event.SimpleEventType;
import org.frameworkset.util.ParamsHandler;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.dictionary.DataManagerFactory;
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
	public static final String resetPrimaryKeyCache = "resetPrimaryKeyCache";
	
	public static final String clearParamsHandlerCache = "clearParamsHandlerCache";
	public static final SimpleEventType cacheRefreshEvent = new SimpleEventType("cacheRefreshEvent");
	
	public static void registRefreshEventListener()
	{
		
		NotifiableFactory.addListener(new CacheManager(), cacheRefreshEvent);
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
		else if(event.equals(resetPrimaryKeyCache))
		{
			result = this.resetPrimaryKeyCache();
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
	
	public String resetPrimaryKeyCache() {
		StringBuilder errorMessage = new StringBuilder();
		try {
			DBUtil.resetPrimaryKeyCache();;
			
		} catch (Exception e) {
			errorMessage .append(StringUtil.formatBRException(e));
		}
		
		if(errorMessage.length() == 0)
			errorMessage.append("清除数据库表主键元数据缓存成功");
		return errorMessage.toString();		
	}

	public  String clearAll()
	{
		StringBuilder ret = new StringBuilder();
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
		StringBuilder errorMessage = new StringBuilder();
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
		
		StringBuilder errorMessage = new StringBuilder();
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
		
		StringBuilder errorMessage = new StringBuilder();
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
		
		StringBuilder errorMessage = new StringBuilder();
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
		StringBuilder errorMessage = new StringBuilder();
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
		StringBuilder errorMessage = new StringBuilder();
		RoleCacheManager.getInstance().reset();
		if(errorMessage.length() == 0)
			errorMessage.append("清除角色缓存成功");
		return errorMessage.toString();		
		
		
	}
	public String clearGroupCache()
	{
		
		
		StringBuilder errorMessage = new StringBuilder();
		try{
			GroupCacheManager.getInstance().reset();
		}catch(Exception e){
			errorMessage .append(StringUtil.formatBRException(e));
		}
		if(errorMessage.length() == 0)
			errorMessage.append("清除用户组缓存成功");
		return errorMessage.toString();			
		
		
	}
	private static Method clearCMSSite2ndChannelCache_M;
	private static Method clearCMSPublishCache_M;
	static 
	{
		try {
			Class CMSCacheUtil = Class.forName("com.frameworkset.platform.cms.cache.CMSCacheUtil");
			clearCMSPublishCache_M = CMSCacheUtil.getMethod("clearCMSPublishCache");
			clearCMSSite2ndChannelCache_M = CMSCacheUtil.getMethod("clearCMSSite2ndChannelCache");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String clearCMSSite2ndChannelCache()
	{
		 
			try {
				return (String)clearCMSSite2ndChannelCache_M.invoke(null);
			} catch (IllegalAccessException e) {
				throw new java.lang.RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new java.lang.RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new java.lang.RuntimeException(e);
			}
			 
			
		 
		 
		
		
		
	}
	
	
	public String clearCMSPublishCache()
	{
		try {
			return (String)clearCMSPublishCache_M.invoke(null);
		} catch (IllegalAccessException e) {
			throw new java.lang.RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new java.lang.RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new java.lang.RuntimeException(e);
		}	
		
		
		
		
		
		
	}
	
	public String clearUserCache()
	{
		StringBuilder errorMessage = new StringBuilder();
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
		StringBuilder errorMessage = new StringBuilder();
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
