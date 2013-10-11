package com.frameworkset.platform.cms.sitemanager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;

import com.frameworkset.platform.cms.channelmanager.ChannelCacheManager;
import com.frameworkset.platform.cms.driver.htmlconverter.LinkCache;
import com.frameworkset.platform.cms.event.CMSEventType;


/**
 * 加载站点做缓冲管理
 * 
 * @author zhuo.wang
 * 
 */
public class SiteCacheManager implements Listener {
	private Map<String,LinkCache> siteLinkCache = new HashMap<String,LinkCache>();
	
	/** 站点缓冲器 */
	Map siteMap;
	
	/** 根据站点的第二名称，缓冲器 */
	Map siteEnameMap;
	
	static SiteCacheManager instance;
	
	/** 通过站点id ，站点频道缓冲*/
	Map<String,ChannelCacheManager> siteChannelCacheManagers;
	
	/** 通过站点第二名称， 站点频道缓冲*/
	Map<String,ChannelCacheManager> siteChannelCacheManagersByEname;
	
	Site siteroot; 
	
	private static boolean inited = false;
	
	static 
	{
		instance = new SiteCacheManager();
		
		instance.siteroot = new Site();
		instance.siteroot.setSiteId(Long.parseLong("0"));
		instance.siteroot.setSecondName("0");
		instance.loadSite(instance.siteroot);	
		
		List eventTypes = new ArrayList();
		eventTypes.add(CMSEventType.EVENT_SITE_ADD);
		eventTypes.add(CMSEventType.EVENT_SITE_DELETE);
		eventTypes.add(CMSEventType.EVENT_SITE_UPDATE);
		
		eventTypes.add(CMSEventType.EVENT_SITESTATUS_UPDATE);

		
		eventTypes.add(CMSEventType.EVENT_CHANNEL_MOVE);
		
		NotifiableFactory.getNotifiable().addListener(instance,eventTypes);
		
		inited = true;
		
	}
	/**
	 * 通过站点id获取该站点下频道缓冲类的引用
	 * @param siteid
	 * @return ChannelCacheManager
	 */
	public ChannelCacheManager getChannelCacheManager(String siteid)
	{
		return (ChannelCacheManager)siteChannelCacheManagers.get(siteid);
	}
	
	/**
	 * 通过站点第二名称（英文名字）获取该站点下频道缓冲类的引用
	 * @param eName
	 * @return ChannelCacheManager
	 */
	public ChannelCacheManager getChannelCacheManagerByEname(String eName)
	{
		return (ChannelCacheManager)siteChannelCacheManagersByEname.get(eName);
	}
	
	public LinkCache getSiteLinkCache(String siteName)
	{
		LinkCache cache = this.siteLinkCache.get(siteName) ;
		if(cache != null)
			return cache;
		synchronized(this.siteLinkCache)
		{
			cache = this.siteLinkCache.get(siteName) ;
			if(cache != null)
				return cache;
			cache = new LinkCache();
			this.siteLinkCache.put(siteName, cache);
		}
		return cache;
	}
	

	/**
	 * 重新加载
	 */
	public void reset()
	{
		synchronized(instance)
		{
			inited = false;
			siteroot = null;
			siteroot = new Site();
			
			siteroot.setSiteId(Long.parseLong("0"));
			siteroot.setSecondName("0");
			siteMap = null;
			siteEnameMap = null;
			instance.loadSite(siteroot);
		}
		
		this.siteLinkCache.clear();
	}
	
	/**
	 * 加载站点id为siteid的站点下的所有频道信息
	 * @param siteid
	 */
	private void loadChannelCacheOfSite(Site siteid)
	{
		if(siteChannelCacheManagers == null)
			siteChannelCacheManagers = new HashMap<String,ChannelCacheManager>();
		if(siteChannelCacheManagersByEname == null)
			siteChannelCacheManagersByEname = new HashMap<String,ChannelCacheManager>();
		
		ChannelCacheManager temp = new ChannelCacheManager(siteid);
		temp.init();
		List eventTypes = new ArrayList();
		eventTypes.add(CMSEventType.EVENT_CHANNEL_ADD);
		eventTypes.add(CMSEventType.EVENT_CHANNEL_DELETE);
		eventTypes.add(CMSEventType.EVENT_CHANNEL_UPDATE);
		NotifiableFactory.getNotifiable().addListener(temp,eventTypes);
		siteChannelCacheManagers.put(siteid.getSiteId()+ "",temp);
		siteChannelCacheManagersByEname.put(siteid.getSecondName(),temp);
	}
	
	/**
	 * 初始化，加载所有有效站点及站点下所有有效频道的信息
	 * @param siteroot
	 */
	private void loadSite(Site siteroot)
	{
		if(siteMap == null)
		{
			siteMap = new HashMap();
		}
		if(siteEnameMap == null)
		{
			siteEnameMap = new HashMap();
		}
		String siteId = String.valueOf(siteroot.getSiteId());
		String siteEname = siteroot.getSecondName();
		if(!inited && !siteId.equals("0"))
		{
			this.loadChannelCacheOfSite(siteroot);
		}
		this.siteMap.put(siteId,siteroot);
		this.siteEnameMap.put(siteEname,siteroot);
		try {
			SiteManager sm = new SiteManagerImpl();
			List subSiteList ;
			if(siteId.equals("0")){
				 subSiteList = sm.getTopSubSiteList();
			}
			else
			{
				subSiteList = sm.getSubSiteList(siteId);
			}
				
			for(int i =0; subSiteList !=null && i<subSiteList.size();i++){
				Site site = (Site)subSiteList.get(i);
				siteroot.addSubSite(site);
//				if(!inited)
//				{
//					this.loadChannelCacheOfSite(site);
//				}
				//this.siteMap.put(String.valueOf(site.getSiteId()),site);
				loadSite(site);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private SiteCacheManager()
	{
		
	}
	
	/**
	 * 判断站点id为siteid的站点是否有子站点
	 * 有子站点返回true，没有子站点返回false
	 * @param siteid
	 * @return boolean
	 */
	public boolean hasSubSite(String siteid) throws Exception {
		
		 String tmp[] = siteid.split(":");
		 String _siteid = "";
		 if(tmp.length<2)
			 _siteid = siteid;
		 else
			 _siteid = tmp[1];
		 Site site = getSite(_siteid);
		 if(site == null)
			 return false;
         return site.getSubsites() != null && site.getSubsites().size() > 0;
	}
	
	/**
	 * 从缓冲区内获取站点id为siteid的站点的信息
	 * @param siteid
	 * @return Site
	 */
	public Site getSite(String siteid) throws Exception {
		Site site = (Site)this.siteMap.get(siteid);
        return site;
		
	}
	
	/**
	 * 从缓冲区内获取站点第二名称(英文名字)为eName的站点的信息
	 * @param eName
	 * @return Site
	 */
	public Site getSiteByEname(String eName) throws Exception {
		Site site = (Site)this.siteEnameMap.get(eName);
        return site;
		
	}
	
	/**
	 * 从缓冲区内获取站点id为siteid的站点的所有子站点的信息的列表
	 * @param siteid
	 * @return List<Site>
	 */
	public List getSubSites(String siteid) throws Exception {
		Site site = (Site)this.siteMap.get(siteid);
        return site.getSubsites();
		
	}
	
	/**
	 * 从缓冲区内获取站点第二名称(英文名字)为eName的站点的所有子站点的信息的列表
	 * @param siteid
	 * @return List<Site>
	 */
	public List getSubSitesByEname(String eName) throws Exception {
		Site site = (Site)this.siteEnameMap.get(eName);
        return site.getSubsites();
		
	}
	
	/**
	 * 获取系统的站点缓冲类的一个引用
	 * @return SiteCacheManager
	 */
	public static SiteCacheManager getInstance() {
		return instance;
	}

	public void handle(Event e) {
		Site site = null;
		
		try
		{
			site = (Site)e.getSource();
		}
		catch(Exception e1)
		{
			return;
		}
		
		if(e.getType() .equals( CMSEventType.EVENT_SITE_ADD))
		{
			synchronized(instance)
			{
				String parentid = site.getParentSiteId() + "";
				Site parent = (Site)this.siteMap.get(parentid);
				parent.addSubSite(site);
				siteMap.put(site.getSiteId() + "",site);
				this.siteEnameMap.put(site.getSecondName(),site);
				Collections.sort(parent.getSubsites());
				loadChannelCacheOfSite(site);
			}
		}
		
		if(e.getType() .equals( CMSEventType.EVENT_SITE_DELETE))
		{
			synchronized(instance)
			{
				String parentid = site.getParentSiteId() + "";
				Site parent = (Site)this.siteMap.get(parentid);
				List list = parent.getSubsites();//System.out.println("before:::::" + list.size());
				Site tmp = null;
				for(int i=0;list != null && i<list.size();i++)
				{
					//System.out.println(i);
					tmp = (Site)list.get(i);
					if(tmp != null && tmp.getSiteId() == site.getSiteId())
					{
						//System.out.println("remove at:::::::" + i);
						list.remove(tmp);
						break;
					}
				}
				//System.out.println(list.indexOf(site));
				//System.out.println(list.remove(site));//
				//System.out.println("after:::::::" + list.size());
				this.siteChannelCacheManagers.remove(site.getSiteId() + "");//
				SiteManager sm = new SiteManagerImpl();
				List subSiteList = null;
				this.siteMap.remove(site.getSiteId() + "");//
				this.siteEnameMap.remove(site.getSecondName());//
				try {
					subSiteList = sm.getAllSubSiteList(site.getSiteId() + "");
				} catch (SiteManagerException e1) {
					e1.printStackTrace();
				}
				for(int i =0; subSiteList !=null && i<subSiteList.size();i++)
				{
					Site subsite = (Site)subSiteList.get(i);
					this.siteMap.remove(subsite.getSiteId() + "");//
					this.siteEnameMap.remove(subsite.getSecondName());//
				}
			}
		}

		if(e.getType() .equals( CMSEventType.EVENT_SITE_UPDATE) )
	    {		
			synchronized(instance)
			{
				Site osite = (Site)this.siteMap.get(site.getSiteId() + "");
				SiteManager sm = new SiteManagerImpl();
				Site newsite = null;
				try {
					newsite = sm.getSiteInfo(osite.getSiteId() + "");
				} catch (SiteManagerException e1) {
					e1.printStackTrace();
				}
				
				this.siteEnameMap.remove(osite.getSecondName());
				
				newsite.setSubsites(osite.getSubsites());
				this.siteEnameMap.put(newsite.getSecondName(),newsite);
				this.siteMap.put(newsite.getSiteId() + "",newsite);
				
				String parentid = site.getParentSiteId() + "";
				Site parent = (Site)this.siteMap.get(parentid);
				
				replaceSubSite(parent,newsite);
				
				if(osite.getSiteOrder() != site.getSiteOrder())
				{
					Collections.sort(parent.getSubsites());
				}
			}
		}
		if(e.getType() .equals( CMSEventType.EVENT_SITESTATUS_UPDATE) )
	    {		
			synchronized(instance)
			{
				Site osite = (Site)this.siteMap.get(site.getSiteId() + "");
				if(osite.getStatus() == 0 && site.getStatus() != 0)//停用站点
				{
					osite.setStatus(site.getStatus());
					this.siteChannelCacheManagers.remove(site.getSiteId() + "");//
				}
				if(osite.getStatus() != 0 && site.getStatus() == 0)//开通站点
				{
					osite.setStatus(site.getStatus());
					loadChannelCacheOfSite(site);
				}
			}
		}
		if(e.getType() .equals( CMSEventType.EVENT_CHANNEL_MOVE) )
	    {		
			synchronized(instance)
			{
				this.siteChannelCacheManagers.remove(site.getSiteId() + "");
				this.siteEnameMap.remove(site.getSecondName());
				loadChannelCacheOfSite(site);
			}
		}
	}
	
	private void replaceSubSite(Site parent,Site subSite)
	{
		List subSites = parent.getSubsites();
		for(int i = 0; subSites != null && i < subSites.size(); i ++)
		{
			Site temp_ = (Site)subSites.get(i);
			if(subSite.getSiteId() == temp_.getSiteId())
			{
				subSites.remove(i);
				subSites.add(i,subSite);
				break;
			}
		}
	}
}
