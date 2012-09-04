package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class CMSOrgChargeTree extends COMTree implements Serializable{
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
      
        //String displayNameInput = request.getParameter("displayNameInput");
		//String displayValueInput = request.getParameter("displayValueInput");
        String resId2 = request.getParameter("resId2");
        String resTypeId2 = request.getParameter("resTypeId2");
        String resTypeName = request.getParameter("resTypeName");
        String title = request.getParameter("title");
        String resName2 = request.getParameter("resName2");
        String isBatch = request.getParameter("isBatch");
		
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
                    
                    Map map = new HashMap();
                    map.put("orgId", sonorg.getOrgId());
                    map.put("resId",sonorg.getOrgId());                    
                	map.put("resName", treeName);
                	//map.put("displayNameInput", displayNameInput);
                	//map.put("displayValueInput", displayValueInput);
                	map.put("resId2", resId2);
                	map.put("resTypeId2", resTypeId2);
                	map.put("resTypeName", resTypeName);
                	map.put("title", title);
                	map.put("resName2", resName2);
                	map.put("isBatch", isBatch);
                	
                	if (accessControl.isOrganizationManager(sonorg.getOrgId()) ||
                			accessControl.isAdmin()) {
                        addNode(father, sonorg.getOrgId(), treeName,
                            "org", true, curLevel, (String) null,
                            (String) null, (String) sonorg.getOrgId()+":"+treeName, map);
                    } else {
                    	if (super.accessControl.isSubOrgManager(sonorg.getOrgId())) {
                            addNode(father, sonorg.getOrgId(),
                            		treeName, "org", false, curLevel,
                                (String) null, (String) null, (String)sonorg.getOrgId()+":"+treeName, map);
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
                if (super.accessControl.checkPermission("lisan",
                        AccessControl.WRITE_PERMISSION, AccessControl.ORGUNIT_RESOURCE))
                    addNode(father, "lisan", "主管机构设置",
                            "lisan", true, curLevel, (String) null,
                            (String) null, (String) null, map);
                else if (super.accessControl.checkPermission("lisan",
                        AccessControl.READ_PERMISSION, AccessControl.ORGUNIT_RESOURCE))
                    addNode(father, "lisan", "主管机构设置",
                            "lisan", false, curLevel, (String) null,
                            (String) null, (String) null, map);
                
            }*/
        
            
        
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

}
