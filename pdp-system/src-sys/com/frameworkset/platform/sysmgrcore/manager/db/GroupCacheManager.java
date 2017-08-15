package com.frameworkset.platform.sysmgrcore.manager.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.SPIException;

import java.util.concurrent.ConcurrentHashMap;

import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;

/**
 * 用户组缓冲器
 * <p>Title: GroupCacheManager</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-11-26 21:14:05
 * @author da.wei
 * @version 1.0
 */
public class GroupCacheManager implements Listener, Serializable {
	
	private static  Map groupsById = new ConcurrentHashMap();
	private static  Map groupsByName = new ConcurrentHashMap();
	private static  GroupCacheManager instance = new GroupCacheManager();
	
	private static boolean inited = false;
	static
	{
		if(!inited)
		{
			init();
			
			List eventTypes = new ArrayList();
			
			eventTypes.add(ACLEventType.GROUP_INFO_CHANGE);
			NotifiableFactory.getNotifiable()
	                .addListener(instance,eventTypes);
			inited = true;
		}
	
		BaseApplicationContext.addShutdownHook(new Runnable(){

			@Override
			public void run() {
				destroy();
				
			}
			
		});
	}
	
	/**
	 * 初始化加载系统中所有的角色
	 *
	 */
	private static void init()
	{
		if(inited)
			return ;
		GroupManager groupmgr;
		try {
			groupmgr = SecurityDatabase.getGroupManager();
			List groups = groupmgr.getGroupList();
			for(int i = 0; i < groups.size(); i ++)
			{
				Group group = (Group)groups.get(i);
				groupsById.put(String.valueOf(group.getGroupId()),group);
				groupsByName.put(group.getGroupName(),group);
			}
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void reinited()
	{
		inited = false;
		synchronized(RoleCacheManager.class)
		{
			groupsById.clear();
			groupsByName.clear();
			init();		
		}
	}
	void _destory()
	{
		groupsById.clear();
		groupsByName.clear();
		inited = false;
	}
	public static void destroy()
	{
		if(instance != null)
		{
			instance._destory();
			instance = null;
		}
	}
	public void handle(Event e) {
		
		if(e.getType() == ACLEventType.GROUP_INFO_CHANGE)
		{
			reinited();
		}

	}
	
	private GroupCacheManager()
	{
		
	}
	
	public static GroupCacheManager getInstance()
	{
		return instance;
	}
	
	public Group getGroupByName(String groupName)
	{
		return (Group)groupsByName.get(groupName);
	}
	
	public Group getGroupByID(String groupID)
	{
		return (Group)groupsById.get(groupID);
	}
	
	/**
	 *重新加载
	 */
	public void reset(){
		synchronized(instance)
		{
			reinited();
		}
	}

}
