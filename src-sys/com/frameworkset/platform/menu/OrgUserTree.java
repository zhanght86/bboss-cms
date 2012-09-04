package com.frameworkset.platform.menu;

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

/**
 * 
 * @author gao.tang 2007.11.13
 *
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
                	
                	Map map = new HashMap();
                	String orgId = sonorg.getOrgId();
                	map.put("orgId", orgId);
                	map.put("resId",orgId);                    
                	map.put("resName", treeName);
                	if (accessControl.isOrganizationManager(orgId) ||
                			accessControl.isAdmin()){
                		if(oid.equals(orgId)){
                			addNode(father, orgId, 
	                        		treeName, "org", false, curLevel, (String) null,
	                            (String) null, (String) null, map);
                		}else{
	                        addNode(father, orgId, 
	                        		treeName, "org", true, curLevel, (String) null,
	                            (String) null, (String) null, map);
                		}
                	}else{
                		if (super.accessControl.isSubOrgManager(orgId)) {
                			addNode(father, orgId, 
	                        		treeName, "org", false, curLevel, (String) null,
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
	                map.put("nodeLink",request.getContextPath() + "/sysmanager/user/userList_ajax.jsp");
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
