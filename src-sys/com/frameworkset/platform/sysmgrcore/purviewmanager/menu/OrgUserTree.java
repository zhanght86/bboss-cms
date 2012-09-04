package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/*
 * <p>Title: 用户调入树</p>
 * <p>Description: 用户调入树显示类</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: iSany</p>
 * @Date 2008-3-20
 * @author gao.tang
 * @version 1.0
 */
public class OrgUserTree extends COMTree implements Serializable{
	
	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();

		try {
			 return OrgCacheManager.getInstance().hasSubOrg(treeID);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
//		boolean state = ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true);
//		String treeID = father.getId();
//        if(father.isRoot()){
//            return true;
//        }else if(!state){//子节点过滤
//        	return false;
//        }
//
//        try {
//        	
//        	 return OrgCacheManager.getInstance().hasSubOrg(treeID);
//        	 
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		String oid = super.request.getParameter("oid");
		try {
//			boolean state = ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true);
//			if(state)//父节点过滤
//			{
			List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);
            
            if(orglist != null){
            	Iterator iterator = orglist.iterator();
            	while(iterator.hasNext()){
            		Organization sonorg = (Organization) iterator.next();
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
                	if (accessControl.isOrganizationManager(orgId) ||
                			accessControl.isAdmin()){
                		//用户调入机构，过滤掉相同机构下的用户调入掉相同机构下
                		if(orgId.equals(oid)){
                			addNode(father, orgId, 
                            		treeName.trim(), "org", false, curLevel, (String) null,
                                (String) null, (String) null, map);
                		}else{
	                        addNode(father, orgId, 
	                        		treeName.trim(), "org", true, curLevel, (String) null,
	                            (String) null, (String) null, map);
                		}
                	}else{
                		if (accessControl.isSubOrgManager(orgId)) {
                			addNode(father, orgId, 
	                        		treeName.trim(), "org", false, curLevel, (String) null,
	                            (String) null, (String) null, map);
                		}
                	}
            	}
            }
//			}
            	
            	
            if(ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",true)){	
	        	if(father.isRoot())
	            {
	                Map map = new HashMap();
	                ResourceManager resManager = new ResourceManager();
	            	String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	            	
	                String oid2 = request.getParameter("oid");
	                map.put("oid",oid2);
	                map.put("nodeLink","userList_ajax.jsp");
	                if (super.accessControl.checkPermission(resId,
	                        "lisanusertoorg", AccessControl.ORGUNIT_RESOURCE))
	                addNode(father, "lisan", "离散用户",
	                        "lisan", true, curLevel, (String) null,
	                        (String) null, (String) null, map);
	            }
            }
            
		} catch (ManagerException e) {
			e.printStackTrace();
		} catch (ConfigException e) {
			e.printStackTrace();
		}
		return true;
	}

}
