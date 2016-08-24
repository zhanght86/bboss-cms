package com.frameworkset.platform.cms.cache;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.platform.cms.driver.publish.impl.ScriptletUtil;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;

public class CMSCacheUtil {

	 
	
	public String clearCMSSite2ndChannelCache()
	{
		StringBuilder errorMessage = new StringBuilder();
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
		StringBuilder errorMessage = new StringBuilder();
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
