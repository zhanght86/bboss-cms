package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;


/**
 * <p>Title: UserOrgTree</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class UserOrgTree extends COMTree implements Serializable{


    /* (non-Javadoc)
     * @see com.frameworkset.common.tag.tree.itf.ActiveTree#hasSon(com.frameworkset.common.tag.tree.itf.ITreeNode)
     */
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
       
        
        
        try {
           
            List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);

            if (orglist != null) {
                Iterator iterator = orglist.iterator();

                while (iterator.hasNext()) {
                	
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
                			accessControl.isAdmin()) {
                        addNode(father, sonorg.getOrgId(), treeName,
                            "org", true, curLevel, (String) null,
                            (String) orgId, (String) orgId, map);
                    } else {
                        if (super.accessControl.isSubOrgManager(orgId)) {
                            addNode(father, sonorg.getOrgId(),
                            		treeName, "org", false, curLevel,
                                (String) null, (String) orgId, (String) orgId, map);
                        }
                    }                                 
                }
                
                
                
                //2006-08-01添加离散用户权限控制
                if(ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",true)){
	                if(father.isRoot())
	                {
	                    Map map = new HashMap();
	                    
	                    ResourceManager resManager = new ResourceManager();
	                	String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	                	
	                    map.put("nodeLink",request.getContextPath() + "/sysmanager/user/discreteUser.jsp");
	                    if (super.accessControl.checkPermission(resId,
	                            "lisanusermanager", AccessControl.ORGUNIT_RESOURCE) || super.accessControl.checkPermission("resId",
	                                    "lisanusertoorg", AccessControl.ORGUNIT_RESOURCE ))
		                    addNode(father, "lisan", "离散用户管理",
		                            "lisan", true, curLevel, (String) null,
		                            (String) null, (String) null, map);
	                    
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
