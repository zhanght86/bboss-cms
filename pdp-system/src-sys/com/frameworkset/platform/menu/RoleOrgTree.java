package com.frameworkset.platform.menu;

import java.io.Serializable;
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
 * 2007-12-08 15:16
 * @author 危达
 * 角色管理的角色赋予机构的机构树
 */

public class RoleOrgTree extends COMTree implements Serializable{
	
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
                    map.put("orgId", sonorg.getOrgId());
                    map.put("resId",sonorg.getOrgId());                    
                	map.put("resName", treeName);
                	map.put("displayNameInput", displayNameInput);
                	map.put("displayValueInput", displayValueInput);
                	if (super.accessControl.checkPermission(sonorg.getOrgId(),
                    		"orgroleset", AccessControl.ORGUNIT_RESOURCE)) {
                        addNode(father, sonorg.getOrgId(), treeName,
                            "org", true, curLevel, (String) null,
                            (String) null, (String) null, map);
                    } 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
