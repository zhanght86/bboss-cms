package com.frameworkset.platform.sysmgrcore.manager.db;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.SPIException;

import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;

/**
 * 加载机构做缓冲管理
 * 
 * @author zhuo.wang
 * 
 */
public class OrgCacheManager implements Listener, Serializable{
	private static Logger log = Logger.getLogger(OrgCacheManager.class);
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	/** 机构的缓冲器 */
	Map orgMap;
	
	/**
	 * 缓冲系统中所有的用户列表
	 */
	List userList;
	
	static OrgCacheManager instance;
	
	Organization root;
	
	 private static boolean inited = false;
	
	static 
	{
		BaseApplicationContext.addShutdownHook(new Runnable(){

			@Override
			public void run() {
				OrgCacheManager.destroy();
				
			}
			
		});
	}
	
	
	
	public List getUserList()
	{
		init();
		return this.userList;
	}
	static boolean initing = false; 
	public static void init()
	{
		if(!inited && !initing)
		{
			initing = true;
			instance = new OrgCacheManager();
			instance.root = new Organization();
			instance.root.setOrgId("0");
			TransactionManager tm = new TransactionManager();
			try
			{
				tm.begin(TransactionType.RW_TRANSACTION);
				instance.loadOrganization(instance.root);
				tm.commit();
			}
			catch(Throwable e)
			{
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//注册感兴趣饿的事件类型
			List eventTypes = new ArrayList();
			eventTypes.add(ACLEventType.ORGUNIT_INFO_ADD);
			eventTypes.add(ACLEventType.ORGUNIT_INFO_DELETE);
			eventTypes.add(ACLEventType.ORGUNIT_INFO_UPDATE);
			eventTypes.add(ACLEventType.ORGUNIT_INFO_SORT);
			eventTypes.add(ACLEventType.ORGUNIT_INFO_TRAN);
			NotifiableFactory.getNotifiable()
	                .addListener(instance,eventTypes);
			inited = true;
		}
	}
	/**
	 *重新加载
	 */
	public void reset()
	{
		
		synchronized(this)
		{
			root = null;
			root = new Organization();
			
			root.setOrgId("0");
			orgMap = null;
			
			loadOrganization(root);
		}
	}
	
	private void loadOrganization(Organization root)
	{
	
		if(orgMap == null)
		{
			orgMap = new EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap();
			this.orgMap.put(root.getOrgId(),root);
		}
//		else if(this.orgMap.containsKey(root.getOrgId()))
//		{
//			log.debug("机构ID[" + root.getOrgId() + "]已经被加载，忽略重复加载!");
//			return;
//		}
		try {
			
			List subOrgList = orgManager.getChildOrgList(root);
			for(int i=0;subOrgList != null && i<subOrgList.size();i++){
				Organization org = (Organization)subOrgList.get(i);
				
				if(this.orgMap.containsKey(org.getOrgId()))
				{
					log.debug("机构ID[" + org.getOrgId() + "]已经被加载，忽略重复加载!");
					return;
				}
				root.addSubOrg(org);
				org.setParentOrg(root);
				this.orgMap.put(org.getOrgId(),org);
				loadOrganization(org);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	
	
	

	private OrgCacheManager()
	{
		
	}
	
	

	/**
	 *判断是否有子机构 
	*/
	public boolean hasSubOrg(String orgid) throws ManagerException {
	
		 if(orgid.equals("lisan")){
			 return false;
		 }
		 else
		 {
			 Organization org = getOrganization(orgid);
	         return org.getSuborgs() != null && org.getSuborgs().size() > 0;
		 }
	}

	/**
	 * 获取机构资源，并且对其进行缓冲
	 */

	public Organization getOrganization(String orgid) throws ManagerException {
		Organization org = (Organization)this.orgMap.get(orgid);
		
        return org;
		
	}
	
	/**
	 *	取子机构列表 
	*/
	public List getSubOrganizations(String orgid) throws ManagerException {
		Organization org = (Organization)this.orgMap.get(orgid);
        return org.getSuborgs();
	}
	
	/**
	 *	取父机构列表,包括当前机构
	*/
	public List getFatherOrganizations(String orgid) throws ManagerException {
		ArrayList list = new ArrayList();
		if(orgid != null)
		{
			Organization org = (Organization)this.orgMap.get(orgid);
			if(org != null)
			{
				list.add(org);
				String parentOrgid = org.getParentId();
				while(parentOrgid != null)
				{
					Organization parentOrg = (Organization)this.orgMap.get(parentOrgid);
					if(parentOrg != null)
					{
						list.add(parentOrg);
						parentOrgid = parentOrg.getParentId();
					}
					else
					{
						parentOrgid = null;
					}
				}				
			}			
		}
		return list;
	}
	
	/**
     *  取父机构列表,包括当前机构
    */
    public Organization getFatherOrganization(String orgid)  {
        
        if(orgid != null)
        {
            Organization org = (Organization)this.orgMap.get(orgid);
            return org.getParentOrg();
            
        }
        return null;
        
    }

	public static OrgCacheManager getInstance() {
		init();
		return instance;
	}
	
	public void _destroy()
	{
		if(this.orgMap != null)
		{
			this.orgMap.clear();
		}
		this.orgMap = null;
		
		root = null;
		orgManager = null;
		initing = false; 
		inited = false;
//		this.inited = false;
//		this.initing = false;
		
	}
	public static void destroy()
	{
		if(instance != null)
		{
			instance._destroy();
			instance = null;
		}
	}
	
	public void handle(Event e) {
		/**
		 * 机构添加时，直接将添加的机构
		 */
		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_ADD ))	//机构添加
		{
			Object source = e.getSource();
			if(source == null || !(source instanceof String))
				return ;
			String orgid = source.toString();
			try {
//				OrgManager orgManager = SecurityDatabase.getOrgManager();
				Organization org = orgManager.getOrgById(orgid);
				if(org == null)
				{
					return;
				}
				String parentid = org.getParentId();
				 
				synchronized(this.orgMap)
				{					
					Organization parent = this.getOrganization(parentid);
					parent.addSubOrg(org);	
					org.setParentOrg(parent);
					this.orgMap.put(org.getOrgId(), org);
				}
			} catch (SPIException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ManagerException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
		}
		
		if( e.getType() .equals( ACLEventType.ORGUNIT_INFO_DELETE))	//机构删除
		{
			Object source = e.getSource();
			if(source == null || !(source instanceof String))
				return ;
			String orgid = source.toString();
			try {
				Organization org = getOrganization(orgid);
				if(org != null)
				{
					String parentid = org.getParentId();
					Organization parent = this.getOrganization(parentid);
					parent.deleteSubOrg(org);
					deleteOrg(org);
					
				}
				
					
			} catch (ManagerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
		}
		
		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_UPDATE))	//机构更新
		{
			Object source = e.getSource();
			if(source == null || !(source instanceof String))
				return ;
			String orgid = source.toString();		
			try {
				Organization to = getOrganization(orgid);
				if(to != null)
				{
//					OrgManager orgManager = SecurityDatabase.getOrgManager();
					Organization from = orgManager.getOrgById(orgid);
					if(from != null)
						OrgManagerImpl.orgcopy(from, to);
				}
			} catch (ManagerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SPIException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_SORT))	//机构排序
		{
			Object source = e.getSource();
			if(source == null || !(source instanceof Object[]))
				return ;
			Object[] infos = (Object[]) source;
			String orgid = infos[0].toString();			
			String[] suborgids = (String[])infos[1];
			if(orgid == null || orgid.length() == 0 || suborgids == null || suborgids.length == 0)
				return;
			try {
				
				
				synchronized(this.orgMap)
				{
					Organization parent = getOrganization(orgid);
					if(parent != null)
			        {
						List subs = new ArrayList();
						for(int i = 0; i < suborgids.length;i ++)
						{
							Organization to = getOrganization(suborgids[i]);
							if(to != null)
								subs.add(to);
						}
						parent.setSuborgs(subs);
			        }
				}
				
				
			} catch (ManagerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_TRAN))	//机构转移
		{
			Object source = e.getSource();
			if(source == null || !(source instanceof String[]))
				return ;
			String[] infos = (String[]) source;
			String orgid = infos[0].toString();		
			String tanstoOrgId = infos[1];
			try {
				
				
				synchronized(this.orgMap)
				{
					Organization org = getOrganization(orgid);
					org.getParentOrg().deleteSubOrg(org);
					Organization tanstoOrg = getOrganization(tanstoOrgId);
					tanstoOrg.addSubOrg(org);
					org.setParentOrg(tanstoOrg);
					org.setParentId(tanstoOrg.getOrgId());
					
				}
				
				
			} catch (ManagerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	
	
	
	/**
	 * 删除机构及子机构的索引
	 * @param org
	 */
	private void deleteOrg(Organization org)
	{
		synchronized(this.orgMap)
		{
			this.orgMap.remove(org.getOrgId());			
			deleteSubOrgs(org);
		}
			
	}
	/**
	 * 删除机构的子机构索引
	 * @param org
	 */
	private void deleteSubOrgs(Organization org)
	{
		List orgs = org.getSuborgs();
		if(orgs != null && orgs.size() > 0)
		{
			synchronized(this.orgMap)
			{
				for(int i = 0; i < orgs.size();i ++)
				{
					Organization org_ = (Organization)orgs.get(i);
					orgMap.remove(org_.getOrgId());
					deleteSubOrgs(org_);
				}
			}
		}
			
	}
}
