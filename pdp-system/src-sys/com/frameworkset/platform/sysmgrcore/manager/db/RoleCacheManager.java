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
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;

/**
 * 角色缓冲器
 * <p>Title: RoleCacheManager</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-11-24 17:14:05
 * @author biaoping.yin
 * @version 1.0
 */
public class RoleCacheManager implements Listener, Serializable {
	
	private static  Map rolesById = new ConcurrentHashMap();
	private static  Map rolesByName = new ConcurrentHashMap();
	private static  RoleCacheManager instance = new RoleCacheManager();
	
	private static boolean inited = false;
	static
	{
		if(!inited)
		{
			init();
			
			List eventTypes = new ArrayList();
			
			eventTypes.add(ACLEventType.ROLE_INFO_CHANGE);
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
	
	public static void destroy()
	{
		inited = false;
		rolesById.clear();
		rolesByName.clear();
		instance = null;
	}
	
	/**
	 * 初始化加载系统中所有的角色
	 *
	 */
	private static void init()
	{
		if(inited)
			return ;
		RoleManager rolemgr;
		try {
			rolemgr = SecurityDatabase.getRoleManager();
			List roles = rolemgr.getRoleList();
			for(int i = 0; i < roles.size(); i ++)
			{
				Role role = (Role)roles.get(i);
				rolesById.put(role.getRoleId(),role);
				rolesByName.put(role.getRoleName(),role);
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
			rolesById.clear();
			rolesByName.clear();
			init();		
		}
	}
	public void handle(Event e) {
		
		if(e.getType().equals( ACLEventType.ROLE_INFO_CHANGE))
		{
			reinited();
		}

	}
	
	private RoleCacheManager()
	{
		
	}
	
	public static RoleCacheManager getInstance()
	{
		return instance;
	}
	
	public Role getRoleByName(String roleName)
	{
		return (Role)rolesByName.get(roleName);
	}
	
	public Role getRoleByID(String roleID)
	{
		return (Role)rolesById.get(roleID);
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
