package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
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
public class DisperseTree extends COMTree implements Serializable{


     public boolean hasSon(ITreeNode father) {
	        String treeID = father.getId();

	        try {
	            OrgManager orgManager = SecurityDatabase.getOrgManager();
	            Organization org = new Organization();
	            org.setOrgId(treeID);

	            return orgManager.isContainChildOrg(org);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return false;
	    }

	    public boolean setSon(ITreeNode father, int curLevel) {
	        String treeID = father.getId();
	        OrgManager orgManager;
	        String displayNameInput = request.getParameter("displayNameInput");
			String displayValueInput = request.getParameter("displayValueInput");
	        try {
	            orgManager = SecurityDatabase.getOrgManager();

	            Organization org = new Organization();
	            org.setOrgId(treeID);

	            List orglist = orgManager.getChildOrgList(org);

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
	                	map.put("displayNameInput", displayNameInput);
	                	map.put("displayValueInput", displayValueInput);
	                	if (accessControl.isOrganizationManager(orgId) ||
	                			accessControl.isAdmin()) {
	                        addNode(father, orgId, treeName,
	                            "org", true, curLevel, (String) null,
	                            (String) null, (String) null, map);
	                    } else {
	                        if (super.accessControl.isSubOrgManager(orgId)) {
	                            addNode(father, orgId,
	                            		treeName, "org", false, curLevel,
	                                (String) null, (String) null, (String) null, map);
	                        }
	                    }
	                }
	                if(father.isRoot())
	                {
	                    Map map = new HashMap();
	                    String jobId = request.getParameter("jobId");
	                    String orgId = request.getParameter("orgId");
	                    map.put("jobId",jobId);
	                    map.put("orgId1",orgId);
	                    map.put("nodeLink",request.getContextPath() + "/orgmanager/org.do?method=getDicList&jobId="+jobId+"&orgId1="+orgId);
	                    addNode(father, "lisanyonghu", "离散用户",
	                            "lisan", true, curLevel, (String) null,
	                            (String) null, (String) null, map);
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return true;
	    }

}
