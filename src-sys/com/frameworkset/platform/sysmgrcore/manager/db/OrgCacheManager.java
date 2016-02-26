package com.frameworkset.platform.sysmgrcore.manager.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.SPIException;

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
public class OrgCacheManager implements Listener,OrgCacheCallback{
	private static Logger log = Logger.getLogger(OrgCacheManager.class);
	OrgManager orgManager = SecurityDatabase.getOrgManager();
	/** 机构的缓冲器 */
	Map<String,Organization> orgMap;
	private static Object lock = new Object();
	
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
	 
	public static void init()
	{
		
		if(!inited )
		{
			synchronized(lock)
			{
				if(!inited )
				{
					OrgCacheManager _instance = new OrgCacheManager();
					_instance.root = new Organization();
					_instance.root.setOrgId("0");
					_instance.root.orgCacheCallback(_instance);
					_instance.cacheorg(_instance.root);
//					TransactionManager tm = new TransactionManager();
//					try
//					{
//						tm.begin(TransactionType.RW_TRANSACTION);
//						_instance.loadOrganization(_instance.root);
//						tm.commit();
//					}
//					catch(Throwable e)
//					{
//						try {
//							tm.rollback();
//						} catch (RollbackException e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
//					}
					//注册感兴趣饿的事件类型
					List eventTypes = new ArrayList();
					eventTypes.add(ACLEventType.ORGUNIT_INFO_ADD);
					eventTypes.add(ACLEventType.ORGUNIT_INFO_DELETE);
					eventTypes.add(ACLEventType.ORGUNIT_INFO_UPDATE);
					eventTypes.add(ACLEventType.ORGUNIT_INFO_SORT);
					eventTypes.add(ACLEventType.ORGUNIT_INFO_TRAN);
					NotifiableFactory.getNotifiable()
			                .addListener(_instance,eventTypes);
					instance = _instance;
					inited = true;
				}
			}
		}
	}
	private void initroot()
	{
		root = new Organization();
		
		root.setOrgId("0");
		root.orgCacheCallback(this);
		root.putloadfather(true);
		root.setOrgtreelevel("0");
		this.orgMap.put("0", root);
//		root.putloadfathers(true);
	}
	/**
	 *重新加载
	 */
	public void reset()
	{
		
		synchronized(lock)
		{
			orgMap = new HashMap();
			root = null;
			initroot();
			
			
//			loadOrganization(root);
		}
	}
	
	private void cacheorg(Organization root)
	{
		if(orgMap == null)
		{
			orgMap = new HashMap();
			this.orgMap.put(root.getOrgId(),root);
		}
	}
	
//	private void loadOrganization(Organization root)
//	{
//	
//		if(orgMap == null)
//		{
//			orgMap = new HashMap();
//			this.orgMap.put(root.getOrgId(),root);
//		}
////		else if(this.orgMap.containsKey(root.getOrgId()))
////		{
////			log.debug("机构ID[" + root.getOrgId() + "]已经被加载，忽略重复加载!");
////			return;
////		}
////		try {
////			
////			List subOrgList = orgManager.getChildOrgList(root);
////			for(int i=0;subOrgList != null && i<subOrgList.size();i++){
////				Organization org = (Organization)subOrgList.get(i);
////				
////				if(this.orgMap.containsKey(org.getOrgId()))
////				{
////					log.debug("机构ID[" + org.getOrgId() + "]已经被加载，忽略重复加载!");
////					return;
////				}
////				root.addSubOrg(org);
////				org.setParentOrg(root);
////				this.orgMap.put(org.getOrgId(),org);
////				loadOrganization(org);
////			}
////			
////		} catch (Exception e) {
////			e.printStackTrace();
////		}		
//		cacheSubOrgs(root);
//	}
	
//	private void cacheSubOrgs(Organization root)
//	{
//		try {
//			
//			List subOrgList = orgManager.getChildOrgList(root);
//			for(int i=0;subOrgList != null && i<subOrgList.size();i++){
//				Organization org = (Organization)subOrgList.get(i);
//				
//				if(this.orgMap.containsKey(org.getOrgId()))
//				{
//					log.debug("机构ID[" + org.getOrgId() + "]已经被加载，忽略重复加载!");
//					return;
//				}
//				root.addSubOrg(org);
//				org.setParentOrg(root);
//				this.orgMap.put(org.getOrgId(),org);
//				loadOrganization(org);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}		
//	}
	
	private void cacheSubOrgs(Organization root)
	{
		try {
			
			List subOrgList = orgManager.getChildOrgList(root);
			for(int i=0;subOrgList != null && i<subOrgList.size();i++){
				Organization org = (Organization)subOrgList.get(i);
				Organization cacheorg = this.orgMap.get(org.getOrgId())  ;
				if(cacheorg != null)
				{
					
					root.addSubOrg(cacheorg);
					if(!cacheorg.loadfather())
					{
						cacheorg.setParentOrg(root);
						cacheorg.putloadfather(true);
					}
				}
				else
				{
					root.addSubOrg(org);
					org.setParentOrg(root);
					org.putloadfather(true);
					org.orgCacheCallback(this);
					this.orgMap.put(org.getOrgId(),org);
				}
	
				
//				loadOrganization(org);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void cachefather(Organization node)
	{
		try {
			if(node.getParentId() != null )
			{
				if(node.getOrgId() != null && node.getOrgId().equals("0"))
					return;
				if(node.getParentId().equals("0"))
				{
					if(root != null)
					{
						node.setParentOrg(root);
					}
					else
					{
						initroot();
						node.setParentOrg(root);
					}
				}
				else
				{
//					Organization father = orgManager.getOrgById(node.getParentId());
					Organization father = this.getOrganization(node.getParentId());
					if(father != null)
					{
						 
						node.setParentOrg(father);
					}
				}
					
			}
			 
			
//			for(int i=0;subOrgList != null && i<subOrgList.size();i++){
//				Organization org = (Organization)subOrgList.get(i);
//				
//				if(this.orgMap.containsKey(org.getOrgId()))
//				{
//					log.debug("机构ID[" + org.getOrgId() + "]已经被加载，忽略重复加载!");
//					return;
//				}
//				root.addSubOrg(org);
//				org.setParentOrg(root);
//				org.putloadfather(true);
//				org.orgCacheCallback(this);
//				this.orgMap.put(org.getOrgId(),org);
////				loadOrganization(org);
//			}
			
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
	         return org != null && org.getSuborgs() != null && org.getSuborgs().size() > 0;
		 }
	}

	/**
	 * 获取机构资源，并且对其进行缓冲
	 */

	public Organization getOrganization(String orgid) throws ManagerException {
		Organization org = (Organization)this.orgMap.get(orgid);
		if(org == null)
		{
			synchronized(lock)
			{
				org = (Organization)this.orgMap.get(orgid);
				if(org == null)
				{
					org = _getOrganization( orgid);
				}
				
			}
		}
		
        return org;
		
	}
	
	private Organization _getOrganization(String orgid)
	{
		try {
			if(orgid != null )
			{
				
				if(orgid.equals("0"))
				{
					if(root != null)
					{
						return (root);
					}
					else
					{
						initroot();
						return (root);
					}
				}
				else
				{
					Organization node = orgManager.getOrgById(orgid);
					if(node != null)
					{
						node.orgCacheCallback(this);
						this.orgMap.put(orgid,node);
						
					}
					return node;
				}
					
			}
			return null;
			 
			
//			for(int i=0;subOrgList != null && i<subOrgList.size();i++){
//				Organization org = (Organization)subOrgList.get(i);
//				
//				if(this.orgMap.containsKey(org.getOrgId()))
//				{
//					log.debug("机构ID[" + org.getOrgId() + "]已经被加载，忽略重复加载!");
//					return;
//				}
//				root.addSubOrg(org);
//				org.setParentOrg(root);
//				org.putloadfather(true);
//				org.orgCacheCallback(this);
//				this.orgMap.put(org.getOrgId(),org);
////				loadOrganization(org);
//			}
			
		} catch (Exception e) {
			log.error("orgid:"+orgid,e);
		}		
		return null;
	}
	
	/**
	 *	取子机构列表 
	*/
	public List getSubOrganizations(String orgid) throws ManagerException {
		Organization org = getOrganization(orgid);
        return org.getSuborgs();
	}
	
//	/**
//	 *	取父机构列表,包括当前机构
//	*/
//	public List getFatherOrganizations(String orgid) throws ManagerException {
//		ArrayList list = new ArrayList();
//		if(orgid != null)
//		{
//			Organization org = getOrganization(orgid);
//			if(org != null)
//			{
//				list.add(org);
//				String parentOrgid = org.getParentId();
//				while(parentOrgid != null)
//				{
//					Organization parentOrg = getOrganization(parentOrgid);
//					if(parentOrg != null)
//					{
//						list.add(parentOrg);
//						parentOrgid = parentOrg.getParentId();
//					}
//					else
//					{
//						parentOrgid = null;
//					}
//				}				
//			}			
//		}
//		return list;
//	}
	/**
	 *	取父机构列表,包括当前机构
	*/
	public List<Organization> getFatherOrganizations(String orgid) throws ManagerException {
		ArrayList list = new ArrayList();
		if(orgid != null)
		{
			Organization org = getOrganization(orgid);
			if(org != null)
			{
				list.add(org);
				Organization parentOrg = org.getParentOrg();
				
				while(parentOrg != null)
				{
					if(parentOrg.getOrgId() != null && !parentOrg.getOrgId().equals("0"))
					{
						list.add(parentOrg);
						parentOrg = parentOrg.getParentOrg();
					}
					else
					{
						parentOrg = null;
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
            try {
				Organization org =  getOrganization(orgid);
				return org.getParentOrg();
			} catch (ManagerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
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
	
//	public void handle(Event e) {
//		/**
//		 * 机构添加时，直接将添加的机构
//		 */
//		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_ADD ))	//机构添加
//		{
//			Object source = e.getSource();
//			if(source == null || !(source instanceof String))
//				return ;
//			String orgid = source.toString();
//			try {
////				OrgManager orgManager = SecurityDatabase.getOrgManager();
//				Organization org = orgManager.getOrgById(orgid);
//				if(org == null)
//				{
//					return;
//				}
//				String parentid = org.getParentId();
//				 
//				synchronized(lock)
//				{					
//					Organization parent = this.getOrganization(parentid);
//					parent.addSubOrg(org);	
//					org.setParentOrg(parent);
//					this.orgMap.put(org.getOrgId(), org);
//				}
//			} catch (SPIException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (ManagerException e2) {
//				// TODO Auto-generated catch block
//				e2.printStackTrace();
//			}
//			
//		}
//		
//		if( e.getType() .equals( ACLEventType.ORGUNIT_INFO_DELETE))	//机构删除
//		{
//			Object source = e.getSource();
//			if(source == null || !(source instanceof String))
//				return ;
//			String orgid = source.toString();
//			try {
//				Organization org = getOrganization(orgid);
//				if(org != null)
//				{
//					String parentid = org.getParentId();
//					Organization parent = this.getOrganization(parentid);
//					parent.deleteSubOrg(org);
//					deleteOrg(org);
//					
//				}
//				
//					
//			} catch (ManagerException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			
//			
//			
//		}
//		
//		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_UPDATE))	//机构更新
//		{
//			Object source = e.getSource();
//			if(source == null || !(source instanceof String))
//				return ;
//			String orgid = source.toString();		
//			try {
//				Organization to = getOrganization(orgid);
//				if(to != null)
//				{
////					OrgManager orgManager = SecurityDatabase.getOrgManager();
//					Organization from = orgManager.getOrgById(orgid);
//					if(from != null)
//					{
//						if(from.getRemark3() != null && from.getRemark3().equals("0"))//冻结部门信息清理
//						{
//							String parentid = from.getParentId();
//							Organization parent = this.getOrganization(parentid);
//							if(parent != null)
//								parent.deleteSubOrg(to);
//							this.deleteOrg(to,false);
//						}
//						else
//							OrgManagerImpl.orgcopy(from, to);
//					}
//					
//				}
//				else
//				{
//					Organization from = orgManager.getOrgById(orgid);
//					
//					if(from != null)
//					{
//						if(from.getRemark3() != null && from.getRemark3().equals("0"))//冻结部门信息清理
//						{
//							String parentid = from.getParentId();
//							Organization parent = this.getOrganization(parentid);
//							if(parent != null)
//								parent.deleteSubOrg(from);
//							this.deleteOrg(from,false);
//						}
//						else
//						{
//							String parentid = from.getParentId();
//							 
//							synchronized(lock)
//							{					
//								Organization parent = this.getOrganization(parentid);
//								if(parent != null)
//								{
//									parent.addSubOrg(from);	
//									from.setParentOrg(parent);
//								}
//								this.cacheSubOrgs(from);
//								this.orgMap.put(from.getOrgId(), from);
//							}
//						}
//					}
//				}
//			} catch (ManagerException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} catch (SPIException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//		}
//		
//		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_SORT))	//机构排序
//		{
//			Object source = e.getSource();
//			if(source == null || !(source instanceof Object[]))
//				return ;
//			Object[] infos = (Object[]) source;
//			String orgid = infos[0].toString();			
//			String[] suborgids = (String[])infos[1];
//			if(orgid == null || orgid.length() == 0 || suborgids == null || suborgids.length == 0)
//				return;
//			try {
//				
//				
//				synchronized(lock)
//				{
//					Organization parent = getOrganization(orgid);
//					if(parent != null)
//			        {
//						List subs = new ArrayList();
//						for(int i = 0; i < suborgids.length;i ++)
//						{
//							Organization to = getOrganization(suborgids[i]);
//							if(to != null)
//								subs.add(to);
//						}
//						parent.setSuborgs(subs);
//			        }
//				}
//				
//				
//			} catch (ManagerException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
//		
//		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_TRAN))	//机构转移
//		{
//			Object source = e.getSource();
//			if(source == null || !(source instanceof String[]))
//				return ;
//			String[] infos = (String[]) source;
//			String orgid = infos[0].toString();		
//			String tanstoOrgId = infos[1];
//			try {
//				
//				
//				synchronized(lock)
//				{
//					Organization org = getOrganization(orgid);
//					org.getParentOrg().deleteSubOrg(org);
//					Organization tanstoOrg = getOrganization(tanstoOrgId);
//					tanstoOrg.addSubOrg(org);
//					org.setParentOrg(tanstoOrg);
//					org.setParentId(tanstoOrg.getOrgId());
//					
//				}
//				
//				
//			} catch (ManagerException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//		}
//		
//	}
	
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
				Organization org = this.orgMap.get(orgid);
				if(org == null)
				{
					synchronized(lock)
					{
						org = this.orgMap.get(orgid);
						if(org == null)
						{
							org = orgManager.getOrgById(orgid);
							if(org == null)
							{
								return;
							}
							org.orgCacheCallback(this);
							Organization parent = this.orgMap.get(org.getParentId());
							if(parent != null)
							{
								if(parent.loadsubs())
								{
									if(!parent.containSubOrg(org.getOrgId()))
										parent.addSubOrg(org);
								}
								org.setParentOrg(parent);
								org.putloadfather(true);
							}
							this.orgMap.put(org.getOrgId(), org);
						}
						else
						{
							Organization parent = this.orgMap.get(org.getParentId());
							if(parent != null)
							{
								if(parent.loadsubs())
								{
									if(!parent.containSubOrg(org.getOrgId()))
										parent.addSubOrg(org);
								}
								org.setParentOrg(parent);
								org.putloadfather(true);
							}
						}
					}
				}
				else
				{
					
					
						synchronized(lock)
						{
							Organization parent = this.orgMap.get(org.getParentId());
							if(parent != null)
							{
							
								if(parent.loadsubs())
								{
									
									if(!parent.containSubOrg(org.getOrgId()))
										parent.addSubOrg(org);
								}
								if(!org.loadfather())
								{
									org.setParentOrg(parent);
									org.putloadfather(true);
								}
							}
						}
					}
				 
//				Organization org = orgManager.getOrgById(orgid);
//				if(org == null)
//				{
//					return;
//				}
//				
//				 
//				synchronized(lock)
//				{					
//					Organization parent = this.orgMap.get(orgid);
//					parent.addSubOrg(org);	
//					org.setParentOrg(parent);
//					this.orgMap.put(org.getOrgId(), org);
//				}
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
			
				synchronized(lock)
				{
					Organization org = this.orgMap.get(orgid);
					if(org != null)
					{
						String parentid = org.getParentId();
						Organization parent = this.orgMap.get(parentid);
						if(parent != null && 
								parent.loadsubs())
							parent.deleteSubOrg(org);
						deleteOrg(org);
						
					}
					
				}
				
					
				
			
			
			
			
			
		}
		
		if(e.getType() .equals( ACLEventType.ORGUNIT_INFO_UPDATE))	//机构更新
		{
			Object source = e.getSource();
			if(source == null || !(source instanceof String))
				return ;
			String orgid = source.toString();		
			try {
				synchronized(lock)
				{
					Organization to = this.orgMap.get(orgid);
					if(to != null)
					{
	//					OrgManager orgManager = SecurityDatabase.getOrgManager();
						
						
						Organization from = orgManager.getOrgById(orgid);
						if(from != null)
						{
							if(from.getRemark3() != null && from.getRemark3().equals("0"))//冻结部门信息清理
							{
								
								String parentid = from.getParentId();
								Organization parent = this.orgMap.get(parentid);
								if(parent != null && 
										parent.loadsubs())
									parent.deleteSubOrg(to);
								this.deleteOrg(to,false);
							}
							else
								OrgManagerImpl.orgcopy(from, to);
						}
						
					}
					 
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
			 
				
				
			synchronized(lock)
			{
				Organization parent = this.orgMap.get(orgid);
				if(parent != null && parent.loadsubs())
		        {
					List subs = new ArrayList();
					for(int i = 0; i < suborgids.length;i ++)
					{
						Organization to = orgMap.get(suborgids[i]);
						if(to != null)
							subs.add(to);
					}
					parent.setSuborgs(subs);
		        }
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
			synchronized(lock)
			{
				Organization org = orgMap.get(orgid);
				if(org != null)
				{
					Organization parent = this.orgMap.get(org.getParentId());
					if(parent != null && 
							parent.loadsubs())
						org.getParentOrg().deleteSubOrg(org);
					if(org.loadfather())
					{
						org.setParentOrg(null);
						org.putloadfather(false);
					}
					org.setSuborgs(null);
					org.putloadsubs(false);
					Organization tanstoOrg = this.getOrganization(tanstoOrgId);
					if(tanstoOrg != null )
					{
						org.setOrgtreelevel(tanstoOrg.getOrgtreelevel() + "|"+org.getOrgId());
						if(tanstoOrg.loadsubs() && !tanstoOrg.containSubOrg(orgid))
							tanstoOrg.addSubOrg(org);
						org.setParentOrg(tanstoOrg);
						org.putloadfather(true); 
						
					}
					org.setParentId(tanstoOrgId);
					
				}
				else
				{
					org = this.getOrganization(orgid);
					Organization tanstoOrg = this.getOrganization(tanstoOrgId);
					if(tanstoOrg != null )
					{
						if(tanstoOrg.loadsubs() && !tanstoOrg.containSubOrg(orgid))
							tanstoOrg.addSubOrg(org);
						org.setParentOrg(tanstoOrg);
						org.putloadfather(true); 
						
					}
				}
				this.deleteSubOrgs(org);
				
			}
				
				
			 
			
		}
		
	}
	
	
	
	/**
	 * 删除机构及子机构的索引
	 * @param org
	 */
	private void deleteOrg(Organization org)
	{
		deleteOrg(org,true);
			
	}
	

	/**
	 * 删除机构及子机构的索引
	 * @param org
	 */
	private void deleteOrg(Organization org,boolean clearsub)
	{
		synchronized(lock)
		{
			Organization cached = this.orgMap.remove(org.getOrgId());		
			if(clearsub)
			{
				deleteSubOrgs(cached);
			}
		}
			
	}
	/**
	 * 查找要删除的机构id
	 * @param org
	 * @return
	 */
	private List<String> findSubs(  Organization org)
	{
		List<String> ids = new ArrayList<String>();
		Iterator<Map.Entry<String, Organization>> entries = this.orgMap.entrySet().iterator();
		String treelevel = org.getOrgtreelevel() + "|";
		while(entries.hasNext())
		{
			Map.Entry<String, Organization> entry = entries.next();
			if(entry.getValue().getOrgtreelevel().startsWith(treelevel))
			{
				ids.add(entry.getValue().getOrgId());
			}
		}
		return ids;
		
	}
	/**
	 * 删除机构的子机构索引
	 * @param org
	 */
	private void deleteSubOrgs(Organization org)
	{
		synchronized(lock){
			List<String> subids = findSubs(  org);
			
			if(subids != null && subids.size() > 0)
			{
				for(int i = 0; i < subids.size();i ++)
				{
				
					orgMap.remove(subids.get(i));				
				}
			
			}
		}
		
			
	}

	@Override
	public void loadsubs(Organization root) {
		if(!root.loadsubs())
		{
			synchronized(lock)
			{
				if(!root.loadsubs())
				{
					this.cacheSubOrgs(root);
					root.putloadsubs(true);
				}
			}
		}
		
	}

	@Override
	public void loadfather(Organization node) {
		if(!node.loadfather())
		{
			synchronized(lock)
			{
				if(!node.loadfather())
				{
					this.cachefather(node);
					node.putloadfather(true);
				}
			}
		}
	}


//	@Override
//	public void loadfathers(Organization node) {
//		if(!node.loadfathers())
//		{
//			synchronized(lock)
//			{
//				if(!node.loadfathers())
//				{
//					this.cachefathers(node);
//					node.putloadfathers(true);
//				}
//			}
//		}
//		
//	}
}
