package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class OrgByAdministratorTree extends COMTree implements Serializable{
	public boolean hasSon(ITreeNode father) {
        String treeID = father.getId();
        if(father.isRoot())
            return true;

        try {
        	
        	 return OrgCacheManager.getInstance().hasSubOrg(treeID);
        	 
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    public boolean setSon(ITreeNode father, int curLevel) {
        String treeID = father.getId();
       
        AccessControl accesscontroler = AccessControl.getInstance();
    	accesscontroler.checkAccess(request, response);
    	        
        try {
        	List orglist = null;
        	//用户如果是否为Admin,否则加载用户可直接管理的机构,是则全部加载
        	if(accesscontroler.isAdmin()){
        		orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);
        	}
        	else{
        		String userId = accesscontroler.getUserID();
        		orglist = new OrgAdministratorImpl().getManagerOrgsOfUserByID(userId);
        	}
            if (orglist != null) {
                Iterator iterator = orglist.iterator();

                while (iterator.hasNext()) {
                    Organization sonorg = (Organization) iterator.next();
                    String treeName  = "";
                    String orgId = sonorg.getOrgId();
                	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals(""))
                		treeName = sonorg.getOrgName();
                    else                    	
                    	treeName = sonorg.getRemark5();
                    Map map = new HashMap();
                    map.put("orgId", orgId);
                    map.put("resId",orgId);
                    map.put("resName", treeName);
                	if (accessControl.isOrganizationManager(orgId) ||
                			accessControl.isAdmin()) {
                        addNode(father, orgId, treeName,
                            "org", true, curLevel, (String) null,
                            (String) orgId, (String) orgId, map);
                    } else {
                        if (super.accessControl.isSubOrgManager(orgId)) {
                            addNode(father, orgId,
                            		treeName, "org", false, curLevel,
                                (String) null, (String) orgId, (String) orgId, map);
                        }
                    }                                 
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }
}
