package com.frameworkset.platform.sysmgrcore.purviewmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.frameworkset.web.servlet.support.RequestContextUtils;

import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB;

public class PurviewManagerOrgTree extends COMTree implements Serializable{
	
//	  private Map subOrgIdMap = null;//子机构
//	  private Map parentOrgIdMap = null;//父机构
	  public PurviewManagerOrgTree(){
		  
	  }
	  
	  public boolean hasSon(ITreeNode father) {
	        String treeID = father.getId();
	        try {
//	        	OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
//				String curUserId = super.accessControl.getUserID();
//				List list = orgAdministrator.getManagerOrgsOfUserByID(curUserId);
//				if(list.size() > 0 || super.accessControl.isAdmin()){//判断是否是部门管理员和拥有超级管理员角色
	        	if(father.isRoot()){
	        		return true;
	        	}else{
	        		return OrgCacheManager.getInstance().hasSubOrg(treeID);
	        	}
//				}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }

	        return false;
	    }

//	    public void setPageContext(PageContext context) {
//	    	super.setPageContext(context);
//	    	OrgManager orgManagerMap = new OrgManagerImpl();
//	    	if(subOrgIdMap == null){
//	    		subOrgIdMap = orgManagerMap.getSubOrgId(super.accessControl.getUserID(),super.accessControl.isAdmin()); 
//	    	}
//	    	if(parentOrgIdMap == null){
//	    		parentOrgIdMap = orgManagerMap.getParentOrgId(super.accessControl.getUserID());
//	    	}
//	    }

		public boolean setSon(ITreeNode father, int curLevel) {
	        String treeID = father.getId();
	        
	        String displayNameInput = request.getParameter("displayNameInput");
			String displayValueInput = request.getParameter("displayValueInput");
	        try {
	        	List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);
	        	
	        	if (orglist != null) {
	        		 for(int i = 0;  i < orglist.size(); i ++)
	                 {                	
	                    Organization sonorg = (Organization) orglist.get(i);
	                    String treeName  = "";
	                	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals(""))
	                		treeName = sonorg.getOrgName();
	                    else                    	
	                    	treeName = sonorg.getRemark5();
	                    String orgId = sonorg.getOrgId();
	                    Map map = new HashMap();
	                    map.put("orgId", orgId);
	                    map.put("resId",orgId);                    
	                	map.put("resName", treeName);
	                	map.put("displayNameInput", displayNameInput);
	                	map.put("displayValueInput", displayValueInput);
	                	map.put("nodeLink", request.getContextPath() + "/purviewmanager/userorgmanager/org/org_userlist.jsp");
	                	if (super.accessControl.isOrganizationManager(orgId) ||
	                			super.accessControl.isAdmin()) {//部门管理员登陆过滤机构
	                		ITreeNode node = addNode(father, orgId, treeName,
	                            "org", true, curLevel, (String) null,
	                            (String) null, (String) null, map);
	                		
	                		Menu orgmenu = new Menu();
	                		
		        	        orgmenu.setIdentity(orgId);
		        	        
		                	Menu.ContextMenuItem sitemenuitem1 = new Menu.ContextMenuItem();
		        			sitemenuitem1.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.info.view", request));
		        			sitemenuitem1.setLink("javascript:checkOrgInfo('" + orgId + "','" + sonorg.getParentId() + "');");
		        			sitemenuitem1.setIcon(request.getContextPath()
		        								+ "/sysmanager/images/icons/task.gif");
		        			orgmenu.addContextMenuItem(sitemenuitem1);
		        			orgmenu.addSeperate();
		        			
	                		if (super.accessControl.checkPermission("orgunit",
		                            "orgupdate", AccessControl.ORGUNIT_RESOURCE))
	                		{
		        	        	orgmenu.setIdentity(orgId);
		        	        	String orgpath =  FunctionDB.buildOrgPath(orgId);	
		                		Menu.ContextMenuItem sitemenuitem2 = new Menu.ContextMenuItem();
		        				sitemenuitem2.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.info.modfiy", request));
		        				sitemenuitem2.setLink("javascript:modifyOrgInfo('" + orgId + "','" + sonorg.getParentId() + "');");
		        				sitemenuitem2.setIcon(request.getContextPath()
		        								+ "/sysmanager/images/icons/task-edit.gif");
		        				orgmenu.addContextMenuItem(sitemenuitem2);
		        				
		        				
		        				Menu.ContextMenuItem orgattrmenuitem = new Menu.ContextMenuItem();
		        				orgattrmenuitem.setName(RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.user.attrs", request));
		        				orgattrmenuitem.setLink("javascript:orgAttrs('" + orgId + "','" + orgpath + "');");
		        				orgattrmenuitem.setIcon(request.getContextPath()
		        								+ "/sysmanager/images/icons/task-edit.gif");
		        				orgmenu.addContextMenuItem(orgattrmenuitem);
		                	   
	                		}
	                		
	                		//删除只能是具有超级管理员角色和自己创造的机构
	                		if (super.accessControl.checkPermission("orgunit",
		                            "newdelorg", AccessControl.ORGUNIT_RESOURCE))
	                		{
	                			OrgManager orgManager = new OrgManagerImpl();
	                			
//	                			Organization org = orgManager.getOrgById(orgId) ;
	                			Organization org = OrgCacheManager.getInstance().getOrganization(
	                					orgId);
	                			               			
	                			if(accessControl.isAdmin() || org.getCreator().equals(accessControl.getUserID()))
	                			{
	                				orgmenu.setIdentity(orgId);
			        	        	
			                		Menu.ContextMenuItem sitemenuitem3 = new Menu.ContextMenuItem();
			        				sitemenuitem3.setName(RequestContextUtils.getI18nMessage("sany.pdp.authoration.delete", request));
			        				sitemenuitem3.setLink("javascript:deleteOrg('" + orgId + "','" + sonorg.getRemark5() + "');");
			        				sitemenuitem3.setIcon(request.getContextPath()
			        								+ "/sysmanager/images/clean.gif");
			        				orgmenu.addContextMenuItem(sitemenuitem3);
	                			}
	                		}
	                		
	                		//转移机构授予
	                		if (super.accessControl.checkPermission("orgunit",
		                            "tranorg", AccessControl.ORGUNIT_RESOURCE)){
	                			
	                			if(accessControl.isAdmin()){
	                				orgmenu.setIdentity(orgId);
			        	        	/**
	                                    *修改传入transferorg()方法的参数,加入了remark5参数
	                                    *2008-3-20
	                                    *baowen.liu
			        	        	**/
			                		Menu.ContextMenuItem sitemenuitem4 = new Menu.ContextMenuItem();
			        				sitemenuitem4.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.organization.move", request));
			        				sitemenuitem4.setLink("javascript:transferOrg('" + orgId + "','" + sonorg.getRemark5()+ " ');");
			        				sitemenuitem4.setIcon(request.getContextPath()
			        								+ "/sysmanager/images/integrate.gif");
			        				orgmenu.addContextMenuItem(sitemenuitem4);	
	                			}else{
	//	                			OrgManager orgManager = new OrgManagerImpl();
		                			
		                			//过虑自己所属机构
		                			List orgs = new ArrayList();
		                			Organization main = ((AccessControl)accessControl).getChargeOrg();
		                			if(main != null)
		                				orgs.add(main);
		                			List lishuorgs = ((AccessControl)accessControl).getAllOrgListExcludeCharge();
		                			if(lishuorgs != null)
		                				orgs.addAll(lishuorgs);
	//	                			accessControl.getChargeOrg();
	//	                			accessControl.getAllOrgListExcludeCharge();
	//	                			List orgs = orgManager.getOrgListOfUser(accessControl.getUserID());
		                			if(orgs.size() > 0){
			                			for (Iterator iter = orgs.iterator(); iter.hasNext();){
											Organization org = (Organization) iter.next();
											if(!org.getOrgId().equals(orgId) || accessControl.isAdmin()){
												
												orgmenu.setIdentity(orgId);
						        	        	/**
				                                    *修改传入transferorg()方法的参数,加入了remark5参数
				                                    *2008-3-20
				                                    *baowen.liu
						        	        	**/
						                		Menu.ContextMenuItem sitemenuitem4 = new Menu.ContextMenuItem();
						        				sitemenuitem4.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.organization.move", request));
						        				sitemenuitem4.setLink("javascript:transferOrg('" + orgId + "','" + sonorg.getRemark5()+ " ');");
						        				sitemenuitem4.setIcon(request.getContextPath()
						        								+ "/sysmanager/images/integrate.gif");
						        				orgmenu.addContextMenuItem(sitemenuitem4);	
											}
										}
			                		}
	                			}
		                	}
	                		
	                		if (super.accessControl.checkPermission("orgunit",
		                            "newdelorg", AccessControl.ORGUNIT_RESOURCE))
	                		{
		        	        	orgmenu.setIdentity(orgId);
		        	        	/**
                                 *修改传入addOrgSon()方法的参数,加入了remark5参数
                                 *2008-3-20
                                 *baowen.liu
		        	        	**/
		                		Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
		        				sitemenuitem5.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.add.child.organization", request));
		        				sitemenuitem5.setLink("javascript:addOrgSon('" + orgId + "','" + sonorg.getRemark5() + " ');");
		        				sitemenuitem5.setIcon(request.getContextPath()
		        								+ "/sysmanager/images/addFieldCon.gif");
		        				orgmenu.addContextMenuItem(sitemenuitem5);
		                	   
	                		}
	                		
	                		
		        	        	orgmenu.setIdentity(orgId);
		                		Menu.ContextMenuItem sitemenuitem6 = new Menu.ContextMenuItem();
		        				sitemenuitem6.setName(RequestContextUtils.getI18nMessage("sany.pdp.authoration.child.organization.sort", request));
		        				String orgName = sonorg.getOrgName() + " " + RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.child.sort", request) + "：";
		        				//orgName=new String(orgName.getBytes("ISO-8859-1"),"GBK");
		        				sitemenuitem6.setLink("javascript:sortSonOrg('" + orgId + "','" + orgName+"')");
		        				sitemenuitem6.setIcon(request.getContextPath()
		        								+ "/sysmanager/images/procedure.gif");
		        				orgmenu.addContextMenuItem(sitemenuitem6);
	                		
		        				orgmenu.addSeperate();
		        	        	orgmenu.setIdentity(orgId);
		        	        	/**
                                 *修改传入changeOrgJob()方法的参数,加入了remark5参数
                                 *2008-3-20
                                 *baowen.liu
		        	        	**/
		        	        	if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false)){
			                		Menu.ContextMenuItem sitemenuitem7 = new Menu.ContextMenuItem();
			        				sitemenuitem7.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.organization.job.setting", request));
			        				sitemenuitem7.setLink("javascript:changeOrgJob('" + orgId + "','" + sonorg.getRemark5() + "');");
			        				sitemenuitem7.setIcon(request.getContextPath()
			        								+ "/sysmanager/images/Valve.gif");
			        				orgmenu.addContextMenuItem(sitemenuitem7);
		        	        	}
		                	   
	                		
		        	        	
	                		/*
	                		 * 增加右键角色授予
	                		 * 2008-3-26
	                		 * liangbing.tao
	                		 */
	                		 if (ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", false))
	                		{
	                			 //部门管理员默认拥有角色授权机构的权限，但是不能给部门管理员所在的主机构授予角色
	                			if(accessControl.isAdmin() || !orgId.equals(accessControl.getChargeOrgId())){
			        	        	orgmenu.setIdentity(orgId);
			        	        	
			                		Menu.ContextMenuItem sitemenuitem8 = new Menu.ContextMenuItem();
			        				sitemenuitem8.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.setting", request));
			        				sitemenuitem8.setLink("javascript:rightRole('" + orgId + "');");
			        				sitemenuitem8
			        						.setIcon(request.getContextPath()
			        								+ "/sysmanager/images/rightMemu/chl_publish.gif");
			        				orgmenu.addContextMenuItem(sitemenuitem8);
	                			}
		                	   
	                		}
	                		
	                		 //管理员授权:只能授予下级机构
	                		if (super.accessControl.checkPermission("orgunit",
		                            "changeorgadmin", AccessControl.ORGUNIT_RESOURCE)){
	                			//是超级管理员
	                			if(accessControl.isAdmin()){
	                				orgmenu.setIdentity(orgId);
			                		Menu.ContextMenuItem sitemenuitem9 = new Menu.ContextMenuItem();
			        				sitemenuitem9.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.manager.set", request));
			        				sitemenuitem9.setLink("javascript:changeOrgAdmin('" + orgId + "');");
			        				sitemenuitem9.setIcon(request.getContextPath()
			        								+ "/sysmanager/images/profile.gif");
			        				orgmenu.addContextMenuItem(sitemenuitem9);
	                			}
	                			else{
	                				OrgManager orgManager = new OrgManagerImpl();
		                			List orgs = orgManager.getOrgListOfUser(accessControl.getUserID()) ;
		                			
		                			if(orgs.size() > 0){
	                				for (Iterator iter = orgs.iterator(); 
	                											iter.hasNext();){
										Organization org = (Organization) iter.next();
										if(!org.getOrgId().equals(orgId)){
											 orgmenu.setIdentity(orgId);
											 
					                		Menu.ContextMenuItem sitemenuitem9 = new Menu.ContextMenuItem();
					        				sitemenuitem9.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.manager.set", request));
					        				sitemenuitem9.setLink("javascript:changeOrgAdmin('" + orgId + "');");
					        				sitemenuitem9.setIcon(request.getContextPath()
					        								+ "/sysmanager/images/profile.gif");
							        				orgmenu.addContextMenuItem(sitemenuitem9);
											}
										}
									}
	                			}
	                		}
	                		
	                		orgmenu.addSeperate();
	                		if (super.accessControl.checkPermission("orgunit",
		                            "purset", AccessControl.ORGUNIT_RESOURCE)){
	                			if(accessControl.isAdmin()){
	                				orgmenu.setIdentity(orgId);
			                		Menu.ContextMenuItem sitemenuitem10 = new Menu.ContextMenuItem();
			        				sitemenuitem10.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.recover", request));
			        				sitemenuitem10.setLink("javascript:reclaimOrgUserRes('" + orgId + "');");
			        				sitemenuitem10.setIcon(request.getContextPath()
			        								+ "/sysmanager/images/icons/16x16/recycle.gif");
			        				orgmenu.addContextMenuItem(sitemenuitem10);
	                			}else{
			                			OrgManager orgManager =  new OrgManagerImpl();
			                			
			                			List orgs = orgManager.getOrgListOfUser(accessControl.getUserID());
			                			
		                			 if(orgs.size() > 0){
		                				for (Iterator iter = orgs.iterator(); iter
												.hasNext();) {
											Organization org = (Organization) iter.next();
											
											if(!org.getOrgId().equals(orgId) ){
												orgmenu.setIdentity(orgId);
						                		Menu.ContextMenuItem sitemenuitem10 = new Menu.ContextMenuItem();
						        				sitemenuitem10.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.recover", request));
						        				sitemenuitem10.setLink("javascript:reclaimOrgUserRes('" + orgId + "');");
						        				sitemenuitem10.setIcon(request.getContextPath()
						        								+ "/sysmanager/images/icons/16x16/recycle.gif");
						        				orgmenu.addContextMenuItem(sitemenuitem10);
											}
											
										}
		                			}
	                		   }
	                		}
	                		
	                		if (ConfigManager.getInstance().getConfigBooleanValue("enableorgrole", false))
	                		{
	                			orgmenu.setIdentity(orgId);
		        	        	
		                		Menu.ContextMenuItem sitemenuitem8 = new Menu.ContextMenuItem();
		        				sitemenuitem8.setName(RequestContextUtils.getI18nMessage("sany.pdp.sys.purview.search", request));
		        				sitemenuitem8.setLink("javascript:orgResQuery('" + orgId + "');");
		        				sitemenuitem8.setIcon(request.getContextPath()
		        								+ "/sysmanager/images/icons/16x16/book-purple-open.gif");
		        				orgmenu.addContextMenuItem(sitemenuitem8);
		        				if(accessControl.checkPermission("orgunit",
		                                "purset", AccessControl.ORGUNIT_RESOURCE)){
			        				if(accessControl.isAdmin() || !orgId.equals(accessControl.getChargeOrgId())){
				        				Menu.ContextMenuItem sitemenuitem9 = new Menu.ContextMenuItem();
				        				sitemenuitem9.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.organization.empower", request));
				        				sitemenuitem9.setLink("javascript:grantOrgRes('" + orgId + "');");
				        				sitemenuitem9.setIcon(request.getContextPath()
				        								+ "/sysmanager/images/JSTreeImgs/task-view.gif");
				        				orgmenu.addContextMenuItem(sitemenuitem9);
			        				}
		        				}
		        				
		        				
		        				
	                			
	                		}
	                		
	                		
	                		
	                		if(orgmenu.getContextMenuItems().size() > 0){
	                			super.addContextMenuOfNode(node, orgmenu);
	                		}	                		
	                		
	                    } else if (super.accessControl.isSubOrgManager(orgId)) {
                            addNode(father, orgId,
                            		treeName, "org", false, curLevel,
                                (String) null, (String) null, (String) null, map);
	                    }
	                }
	            }
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        //2006-08-01添加离散用户权限控制
            if(ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",true)){
                if(father.isRoot())
                {
                   
                	String resId = null;
					try {
						Map map = new HashMap();
		                    
		                ResourceManager resManager = new ResourceManager();
						resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
						map.put("nodeLink","javascript:openModalDialogWin('"+request.getContextPath() + "/purviewmanager/userorgmanager/user/discreteUserList.jsp',screen.availWidth-50,screen.availHeight-50);");
						map.put("node_linktarget","lisanwindows");
//	                    if (super.accessControl.checkPermission(resId,
//	                            "lisanusermanager", AccessControl.ORGUNIT_RESOURCE) || super.accessControl.checkPermission("resId",
//	                                    "lisanusertoorg", AccessControl.ORGUNIT_RESOURCE ))
						
		                    addNode(father, "lisan", RequestContextUtils.getI18nMessage("sany.pdp.role.disperse.user.manage", request),
		                            "lisan", true, curLevel, (String) null,
		                            (String) null, (String) null, map);
					} catch (ConfigException e) {
						e.printStackTrace();
					}
                	
                   
                    
                }
            }

	        return true;
	    }
	    
	    protected void buildContextMenus() {
	    	if(accessControl.isAdmin())
	    	{
		    	Menu orgmenu = new Menu();
	        	orgmenu.setIdentity("1");
	        	//机构排序
	    		Menu.ContextMenuItem orgreordermenu = new Menu.ContextMenuItem();
	    		orgreordermenu.setName(RequestContextUtils.getI18nMessage("sany.pdp.role.organization.sort", request));
	    		orgreordermenu.setLink("javascript:sortOrg('0', ' "+RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.org.all.sort.level1", request)+"：')");
	    		orgreordermenu
						.setIcon(request.getContextPath()
								+ "/sysmanager/images/actions.gif");
				orgmenu.addContextMenuItem(orgreordermenu);
				
				orgmenu.addSeperate();
				
				//机构缓冲刷新
				Menu.ContextMenuItem orgcacherefresh = new Menu.ContextMenuItem();
				orgcacherefresh.setName(RequestContextUtils.getI18nMessage("sany.pdp.purviewmanager.rolemanager.org.cache.ref", request));
				orgcacherefresh.setLink("javascript:refreshorgcache()");
				orgcacherefresh
						.setIcon(request.getContextPath()
								+ "/sysmanager/images/dialog-reset.gif");
				orgmenu.addContextMenuItem(orgcacherefresh);
				
	    		//部门管理员与可管理机构缓冲刷新
				Menu.ContextMenuItem orgmanagercacherefresh = new Menu.ContextMenuItem();
				orgmanagercacherefresh.setName(RequestContextUtils.getI18nMessage("sany.pdp.userorgmanager.user.org.admin.ref", request));
				orgmanagercacherefresh.setLink("javascript:refreshorgmanagercache()");
				orgmanagercacherefresh
						.setIcon(request.getContextPath()
								+ "/sysmanager/images/dialog-reset.gif");
				orgmenu.addContextMenuItem(orgmanagercacherefresh);
				
				//权限缓冲刷新
				Menu.ContextMenuItem cacherefresh = new Menu.ContextMenuItem();
				cacherefresh.setName(RequestContextUtils.getI18nMessage("sany.pdp.purview.authoration.cache.ref", request));
				cacherefresh.setLink("javascript:refreshcache()");
				cacherefresh
						.setIcon(request.getContextPath()
								+ "/sysmanager/images/dialog-reset.gif");
				orgmenu.addContextMenuItem(cacherefresh);
				
				
	            super.addContextMenuOfType(orgmenu);
	    	}
		}
}


