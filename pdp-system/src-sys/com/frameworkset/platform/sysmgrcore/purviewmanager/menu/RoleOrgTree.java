package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;


/*
 * <p>Title: 角色设置给机构树</p>
 * <p>Description: 角色设置给机构</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: iSany</p>
 * @Date 2008-3-20
 * @author gao.tang
 * @version 1.0
 */

public class RoleOrgTree extends COMTree implements Serializable {
	
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
		String isRecursive = request.getParameter("isRecursive");
		
        try {
            
           
            List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);

            if (orglist != null) {
                

               for(int i = 0;  i < orglist.size(); i ++)
               {                	
                    Organization sonorg = (Organization) orglist.get(i);
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
                    String orgId = sonorg.getOrgId();
                    map.put("orgId", orgId);
                    map.put("resId",orgId);                    
                	map.put("resName", treeName);
                	map.put("displayNameInput", displayNameInput);
                	map.put("displayValueInput", displayValueInput);
                	if (super.accessControl.isOrganizationManager(orgId) ||
							super.accessControl.isAdmin()) {
                		if(!super.accessControl.getChargeOrgId().equals(orgId) || 
                				super.accessControl.isAdmin()){//不能为本机机构设置角色，这样就为自己设置了角色增大了自己的权限
	                        addNode(father, orgId, treeName.trim(),
	                            "org", true, curLevel, (String) null,
	                            (String) null, (String) null, map);
                		}else{
                			addNode(father, orgId, treeName.trim(),
    	                            "org", false, curLevel, (String) null,
    	                            (String) null, (String) null, map);
                		}
                    }else{
                    	if (super.accessControl.isSubOrgManager(orgId)) {
                    		addNode(father, orgId, treeName.trim(),
                                    "org", false, curLevel, (String) null,
                                    (String) null, (String) null, map);
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
