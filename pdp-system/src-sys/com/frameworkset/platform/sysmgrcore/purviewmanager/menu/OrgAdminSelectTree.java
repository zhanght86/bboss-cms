/*
 * Created on 2006-3-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;


/**
 * @author ok
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OrgAdminSelectTree extends COMTree implements Serializable {
       public boolean hasSon(ITreeNode father) {
        String treeID = father.getId();

        try {
            
        	if(ConfigManager.getInstance().getConfigValue("enableorgadminall", "parent").equals("parent")//开关，只允许当前机构和父机构的用户作为当前机构的管理员
        			|| ConfigManager.getInstance().getConfigValue("enableorgadminall", "current").equals("current"))//开关，只允许当前机构的用户作为当前机构的管理员
        	{
//        		这个参数是要设置机构管理员的机构id，取出其所有父机构列表，判断属于这个列表的机构才会显示
        		String orgId1 = request.getParameter("orgId1");
        		List list1 = null;
        		ArrayList orgids1 = new ArrayList();
        		try {
        			list1 = OrgCacheManager.getInstance().getFatherOrganizations(orgId1);
        			if(list1 != null && list1.size()>0)
        			{
        				for(int k=0;k<list1.size();k++)
        				{
        					Organization parentOrg1 = (Organization)list1.get(k);
        					if(parentOrg1 != null)
        					{
        						String parentOrgId1 = parentOrg1.getOrgId();
        						if(parentOrgId1 != null)
        						{
        							orgids1.add(parentOrgId1);
        						}
        					}
        				}
        			}
        		} catch (Exception e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
        		if(orgids1 != null)
        		{
        			orgids1.remove(orgId1.toString());
        		}
        		if(orgids1!=null && orgids1.contains(treeID.toString()))
        		{
        			return OrgCacheManager.getInstance().hasSubOrg(treeID);
        		}
        	}
        	else//开关，允许选择所有机构的用户作为当前机构的管理员
        	{
        		return OrgCacheManager.getInstance().hasSubOrg(treeID);
        	}
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean setSon(ITreeNode father, int curLevel) {
        String treeID = father.getId();
        
        String displayNameInput = request.getParameter("displayNameInput");
		String displayValueInput = request.getParameter("displayValueInput");
		String isRecursive = request.getParameter("isRecursive");
		
		//这个参数是要设置机构管理员的机构id，取出其所有父机构列表，判断属于这个列表的机构才会显示
		String orgId1 = request.getParameter("orgId1");
		List list1 = null;
		ArrayList orgids1 = new ArrayList();
		if(ConfigManager.getInstance().getConfigValue("enableorgadminall", "parent").equals("parent")//开关，只允许当前机构和父机构的用户作为当前机构的管理员
    			|| ConfigManager.getInstance().getConfigValue("enableorgadminall", "current").equals("current"))//开关，只允许当前机构的用户作为当前机构的管理员
    	{
			try {
				list1 = OrgCacheManager.getInstance().getFatherOrganizations(orgId1);
				if(list1 != null && list1.size()>0)
				{
					for(int k=0;k<list1.size();k++)
					{
						Organization parentOrg1 = (Organization)list1.get(k);
						if(parentOrg1 != null)
						{
							String parentOrgId1 = parentOrg1.getOrgId();
							if(parentOrgId1 != null)
							{
								orgids1.add(parentOrgId1);
							}
						}
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
        try {
            
           
            List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);

            if (orglist != null) {
                

               for(int i = 0;  i < orglist.size(); i ++)
               {                	
                    Organization sonorg = (Organization) orglist.get(i);
                    String orgId = sonorg.getOrgId();
                    if(ConfigManager.getInstance().getConfigValue("enableorgadminall", "parent").equals("parent")//开关，只允许当前机构和父机构的用户作为当前机构的管理员
                			|| ConfigManager.getInstance().getConfigValue("enableorgadminall", "current").equals("current"))//开关，只允许当前机构的用户作为当前机构的管理员
                	{
//                    	如果机构属于要设置管理员的父机构集合，则执行以下动作
                        if(orgids1!=null && orgids1.contains(sonorg.getOrgId().toString()))
                        {
                        	String treeName  = "";
                        	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals("")){
                        		if(ConfigManager.getInstance().getConfigBooleanValue("sys.orgtree.org_no_name_display", false)){
                        			treeName = sonorg.getOrgId() + "-" + sonorg.getOrgName();
                        		}else{
                        			treeName = sonorg.getOrgName();
                        		}
                        	}
                            else{   
                            	if(ConfigManager.getInstance().getConfigBooleanValue("sys.orgtree.org_no_name_display", false)){
                        			treeName = sonorg.getOrgId() + "-" + sonorg.getRemark5();
                        		}else{
                        			treeName = sonorg.getRemark5();
                        		}
                            }
                            
                            Map map = new HashMap();
                            map.put("orgId", sonorg.getOrgId());
                            map.put("resId",sonorg.getOrgId());                    
                        	map.put("resName", treeName);
                        	map.put("displayNameInput", displayNameInput);
                        	map.put("displayValueInput", displayValueInput);
                        	if (super.accessControl.isOrganizationManager(orgId) ||
        							super.accessControl.isAdmin()) 
                        	{
                        		if(ConfigManager.getInstance().getConfigValue("enableorgadminall", "current").equals("current")
                        				&& !sonorg.getOrgId().equals(orgId1))//如果开关只允许当前机构的用户被选择且又不是当前机构，则机构节点不可点
                        		{
                        			addNode(father, sonorg.getOrgId(),
                                    		treeName, "org", false, curLevel,
                                        (String) null, (String) null, (String) null, map);
                        		}
                        		else
                        		{
                        			addNode(father, sonorg.getOrgId(), treeName,
                                            "org", true, curLevel, (String) null,
                                            (String) null, (String) null, map);
                        		}
                            } 
                        	else if (super.accessControl.isSubOrgManager(orgId)) 
                        	{
                                addNode(father, sonorg.getOrgId(),
                                		treeName, "org", false, curLevel,
                                    (String) null, (String) null, (String) null, map);
                            }
                        }
                    }
                    else//开关，允许选择所有机构的用户作为当前机构的管理员
                    {
                    	String treeName  = "";
                    	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals("")){
                    		if(ConfigManager.getInstance().getConfigBooleanValue("sys.orgtree.org_no_name_display", false)){
                    			treeName = sonorg.getOrgId() + "-" + sonorg.getOrgName();
                    		}else{
                    			treeName = sonorg.getOrgName();
                    		}
                    	}
                        else{   
                        	if(ConfigManager.getInstance().getConfigBooleanValue("sys.orgtree.org_no_name_display", false)){
                    			treeName = sonorg.getOrgId() + "-" + sonorg.getRemark5();
                    		}else{
                    			treeName = sonorg.getRemark5();
                    		}
                        }
                        
                        Map map = new HashMap();
                        map.put("orgId", sonorg.getOrgId());
                        map.put("resId",sonorg.getOrgId());                    
                    	map.put("resName", treeName);
                    	map.put("displayNameInput", displayNameInput);
                    	map.put("displayValueInput", displayValueInput);
                    	if (super.accessControl.checkPermission(sonorg.getOrgId(),
                                AccessControl.WRITE_PERMISSION, AccessControl.ORGUNIT_RESOURCE)) 
                    	{
                            addNode(father, sonorg.getOrgId(), treeName,
                                "org", true, curLevel, (String) null,
                                (String) null, (String) null, map);
                        } 
                    	else if (accessControl.checkPermission(sonorg.getOrgId(),
                                    AccessControl.READ_PERMISSION, AccessControl.ORGUNIT_RESOURCE)) 
                    	{
                            addNode(father, sonorg.getOrgId(),
                            		treeName, "org", false, curLevel,
                                (String) null, (String) null, (String) null, map);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
