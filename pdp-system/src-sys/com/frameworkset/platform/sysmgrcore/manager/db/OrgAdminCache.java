package com.frameworkset.platform.sysmgrcore.manager.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.spi.BaseApplicationContext;

import java.util.concurrent.ConcurrentHashMap;

import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator;

/**
 * 
 * 
 * <p>Title: OrgAdminCache.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: 三一集团</p>
 * @Date May 22, 2008 9:55:15 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class OrgAdminCache implements Listener, Serializable{
	/**
	 *  机构管理员缓冲器 
	 *  Map<orgid,List<User>>
	 */
	Map orgmanagersMap = new ConcurrentHashMap();
	
	/** 
	 * 管理员管理机构缓冲器 
	 *  Map<userid,List<Organization>>
	 */
	Map usermanagerOrgsMap = new ConcurrentHashMap();
	static OrgAdminCache instance = new OrgAdminCache();
	static 
	{
		BaseApplicationContext.addShutdownHook(new Runnable(){

			@Override
			public void run() {
				destroy();
				
			}
			
		});
	}
	
	
	
	/**
	 * 空的用户可管理机构列表
	 * 
	 */
	public final static List NULL_MANAGER_ORGS = new ArrayList();
	
	/**
	 * 空的机构管理员列表
	 */
	public final static List NULL_ORG_MANAGERS = new ArrayList();
	
	static boolean inited = false;
	static 
	{
		init();
	}
	
	public static  void init()
	{
		if(!inited)
		{
			
			//注册感兴趣饿的事件类型
			List eventTypes = new ArrayList();
			eventTypes.add(ACLEventType.ORGUNIT_INFO_DELETE);
			
			eventTypes.add(ACLEventType.ORGUNIT_INFO_ADD);
			eventTypes.add(ACLEventType.ORGUNIT_INFO_DELETE);
			
			eventTypes.add(ACLEventType.ORGUNIT_MANAGER_ADD);
			eventTypes.add(ACLEventType.ORGUNIT_MANAGER_DELETE);
			eventTypes.add(ACLEventType.ORGUNIT_INFO_DELETE);
			eventTypes.add(ACLEventType.USER_INFO_DELETE);
			NotifiableFactory.getNotifiable()
	                .addListener(getOrgAdminCache(),eventTypes);
			inited = true;
		}
	}
	
	private OrgAdminCache()
	{
		
	}
	
	public static OrgAdminCache getOrgAdminCache()
	{
		if(instance == null)
		{
			instance = new OrgAdminCache();
		}
		return instance;
	}

	/**
	 * 获取用户的可管理的机构的列表
	 * @param userid 用户id
	 * @return 返回用户的可管理机构列表
	 */
	public List getManagerOrgsOfUserByID(String userid)
	{
		List managerOrgs = (List)this.usermanagerOrgsMap.get(userid);
		if(managerOrgs == null )
		{
			OrgAdministrator orgmanager = new OrgAdministratorImpl();
			managerOrgs = orgmanager.getManagerOrgsOfUserByID(userid);
			if(managerOrgs == null || managerOrgs.size() == 0)
			{
				managerOrgs = NULL_MANAGER_ORGS;
			}
			this.usermanagerOrgsMap.put(userid,managerOrgs);
		}
		
		return managerOrgs;
	}
	
	
	/**
	 * 获取机构的管理员列表
	 * @param userid 用户id
	 * @return 返回用户的可管理机构列表
	 */
	public List getManagersOfOrg(String orgid)
	{
		List orgManagers = (List)this.orgmanagersMap.get(orgid);
		if(orgManagers == null )
		{
			OrgAdministrator orgmanager = new OrgAdministratorImpl();
			orgManagers = orgmanager.getAdministorsOfOrg(orgid);
			if(orgManagers == null || orgManagers.size() == 0)
			{
				orgManagers = NULL_ORG_MANAGERS;
			}
			this.orgmanagersMap.put(orgid,orgManagers);
		}
		
		return orgManagers;
	}

	public void handle(Event e) {
		if(e.getType().equals( ACLEventType.ORGUNIT_INFO_DELETE)
				|| e.getType() .equals( ACLEventType.ORGUNIT_INFO_ADD)
				|| e.getType() .equals( ACLEventType.ORGUNIT_INFO_DELETE)
				|| e.getType() .equals( ACLEventType.ORGUNIT_MANAGER_ADD)
				|| e.getType() .equals( ACLEventType.ORGUNIT_MANAGER_DELETE)
				|| e.getType() .equals( ACLEventType.ORGUNIT_INFO_DELETE)
				|| e.getType() .equals( ACLEventType.USER_INFO_DELETE))
		{
			synchronized(this)
			{
				orgmanagersMap.clear();
				usermanagerOrgsMap.clear();
			}
		}
		
	}
	
	/**
	 *重新加载
	 */
	public void reset()
	{
		synchronized(instance)
		{
			orgmanagersMap.clear();
			usermanagerOrgsMap.clear();
		}
		
	}
	void _destroy()
	{
		orgmanagersMap.clear();
		usermanagerOrgsMap.clear();
		orgmanagersMap = null;
		usermanagerOrgsMap = null;
		inited = false;
	}
	
	
	public static void destroy()
	{
		if(instance != null)
		{
			instance._destroy();
			instance = null;
		}
	}
	

}


