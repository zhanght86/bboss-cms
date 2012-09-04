package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class OrgChargeTree extends COMTree implements Serializable{
	  public boolean hasSon(ITreeNode father) {
	        String treeID = father.getId();

	        try {
	        	  return OrgCacheManager.getInstance().hasSubOrg(treeID);
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }

	        return false;
	    }

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
	                    
	                    Map map = new HashMap();
	                    String orgId = sonorg.getOrgId();
	                    map.put("orgId", orgId);
	                    map.put("resId",orgId);                    
	                	map.put("resName", treeName);
	                	map.put("displayNameInput", displayNameInput);
	                	map.put("displayValueInput", displayValueInput);
	                	if (accessControl.isOrganizationManager(orgId) ||
	                			accessControl.isAdmin()) {
	                		ITreeNode node = addNode(father, orgId, treeName,
	                            "org", true, curLevel, (String) null,
	                            (String) null, (String) null, map);
	                		
	                		Menu orgmenu = new Menu();
	                		
	                		//机构排序部门管理员自动拥有该权限
	        	        	orgmenu.setIdentity(sonorg.getOrgId());
	                		Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
	        				sitemenuitem5.setName("机构排序");
	        				sitemenuitem5.setLink("javascript:sortOrg('" + sonorg.getOrgId() + "','" + sonorg.getOrgName() + " 机构下的子机构排序：')");
	        				sitemenuitem5.setIcon(request.getContextPath()
	        								+ "/sysmanager/images/actions.gif");
	        				orgmenu.addContextMenuItem(sitemenuitem5);
	        				
	        				
	                		if (super.accessControl.checkPermission("orgunit",
		                            "changeorgadmin", AccessControl.ORGUNIT_RESOURCE))
	                		{
	                			if(!super.accessControl.isAdmin() && sonorg.getOrgId().equals(super.accessControl.getChargeOrgId())){
	                				
	                			}else{
			        	        	orgmenu.setIdentity(sonorg.getOrgId());
			        	        	
			                		Menu.ContextMenuItem sitemenuitem6 = new Menu.ContextMenuItem();
			        				sitemenuitem6.setName("部门管理员设置");
			        				sitemenuitem6.setLink("javascript:changeOrgAdmin('" + sonorg.getOrgId() + "');");
			        				sitemenuitem6
			        						.setIcon(request.getContextPath()
			        								+ "/sysmanager/images/actions.gif");
			        				orgmenu.addContextMenuItem(sitemenuitem6);
	                			}
		                	   
	                		}
	                		//当前用户 是否 对当前 机构有 用户权限设置的 权限
	                		if (super.accessControl.checkPermission("orgunit",
		                            "purset", AccessControl.ORGUNIT_RESOURCE))
	                		{
	                			orgmenu.setIdentity(sonorg.getOrgId());
		        	        	
		                		Menu.ContextMenuItem sitemenuitem6 = new Menu.ContextMenuItem();
		        				sitemenuitem6.setName("权限回收");
		        				sitemenuitem6.setLink("javascript:reclaimOrgUserRes('" + sonorg.getOrgId() + "');");
		        				sitemenuitem6
		        						.setIcon(request.getContextPath()
		        								+ "/sysmanager/images/actions.gif");
		        				orgmenu.addContextMenuItem(sitemenuitem6);
	                		}
	                		
	                		if(orgmenu.getContextMenuItems().size() > 0){
	                			super.addContextMenuOfNode(node, orgmenu);
	                		}	                		
	                		
	                    } else {
	                        if (super.accessControl.isSubOrgManager(orgId)) {
	                            addNode(father, sonorg.getOrgId(),
	                            		treeName, "org", false, curLevel,
	                                (String) null, (String) null, (String) null, map);
	                        }
	                    }
	                }
	            }
	        	
	        	/*if(father.isRoot())
	            {
	            	String orgId = request.getParameter("orgId");
	            	System.out.println(orgId);
	                Map map = new HashMap();
	                map.put("nodeLink",request.getContextPath() + "/sysmanager/orgmanager/chargeOrg_toolbar.jsp");
//	                if (super.accessControl.checkPermission("lisan",
//	                        AccessControl.WRITE_PERMISSION, AccessControl.ORGUNIT_RESOURCE)){
//	                    addNode(father, "lisan", "主管机构设置",
//	                            "lisan", true, curLevel, (String) null,
//	                            (String) null, (String) null, map);
//	                }else if (super.accessControl.checkPermission("lisan",
//	                        AccessControl.READ_PERMISSION, AccessControl.ORGUNIT_RESOURCE)){
//	                    addNode(father, "lisan", "主管机构设置",
//	                            "lisan", false, curLevel, (String) null,
//	                            (String) null, (String) null, map);
//	                }
	            }*/
	        
	            
	        
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return true;
	    }
	    
	    protected void buildContextMenus() {
	    	if(accessControl.isAdmin())
	    	{
		    	Menu orgmenu = new Menu();
	        	orgmenu.setIdentity("1");
	        	
	    		Menu.ContextMenuItem sitemenuitem5 = new Menu.ContextMenuItem();
				sitemenuitem5.setName("机构排序");
				sitemenuitem5.setLink("javascript:sortOrg('0','所有一级机构排序：')");
				sitemenuitem5
						.setIcon(request.getContextPath()
								+ "/sysmanager/images/actions.gif");
				orgmenu.addContextMenuItem(sitemenuitem5);
	    		
	            super.addContextMenuOfType(orgmenu);
	    	}
		}
}


