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
//角色管理资源操作授予－－机构资源授权树
public class AccreditOrgTree extends COMTree implements Serializable{
	 public boolean hasSon(ITreeNode father) {
	        String treeID = father.getId();
	        if(father.isRoot())
	            return true;

	        try {
	        	if(treeID.equals("lisan"))
	        		return false;
	        	if(treeID.equals("00"))
	        		return false;
	        	return OrgCacheManager.getInstance().hasSubOrg(treeID);
	        	
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        return false;
	    }

	    public boolean setSon(ITreeNode father, int curLevel) {
	        String treeID = father.getId();
	       
	        String resTypeId=request.getParameter("resTypeId");
			String roleId = (String)session.getAttribute("currRoleId");
			String roleTypeId = (String)session.getAttribute("role_type");
	        try {
	           

	            List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);

	            if (orglist != null) {
	                Iterator iterator = orglist.iterator();

	                while (iterator.hasNext()) {
	                    Organization sonorg = (Organization) iterator.next();
	                    String orgId = sonorg.getOrgId();
	                    String treeName  = "";
	                	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals(""))
	                		treeName = sonorg.getOrgName();
	                    else
	                    	treeName = sonorg.getRemark5();
	                    Map map = new HashMap();
	                    map.put("orgId",orgId);
	                    map.put("resId",orgId);
	                    map.put("resName", treeName);
	                    String nodeType = "org";
	                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,orgId,resTypeId)){
	                    	nodeType = "org_true";
	                    }else{
	                    	nodeType = "org";
	                    }
	                	if (accessControl.isOrganizationManager(orgId) 
	                			|| accessControl.isAdmin()
	                			|| super.accessControl.checkPermission(orgId,
			                            "readorgname", AccessControl.ORGUNIT_RESOURCE)) {
	                        addNode(father, orgId, treeName,
	                        		nodeType, true, curLevel, (String) null,
	                        		(String) orgId, (String) orgId, map);
	                    } else {
	                        if (super.accessControl.isSubOrgManager(orgId)) {
	                            addNode(father, orgId,
	                            		treeName, nodeType, false, curLevel,
	                                (String) null, (String) orgId, (String) null, map);
	                        }
	                    }                                 
	                }
	                
	                
	                
	                
//	                if(father.isRoot())
//	                {
//	                	Map rootparam = new HashMap();
////	                	rootparam.put("nodeLink",request.getContextPath() + "/sysmanager/accessmanager/role/operList_org_ajax.jsp?resTypeId=orgunit");
//	                    String treeName ="顶级机构";
//	                    String resId ="00";
//	                    rootparam.put("resName",treeName);
//	                    rootparam.put("resId",resId);
//	                    String nodeType = "lisan";
//	                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,"00",resTypeId)){
//	                    	nodeType = nodeType + "_true";
//	                    }else{
////	                    	nodeType = "org";
//	                    }
////	                    if (accessControl.checkPermission("00",
////                                "addsuborg", AccessControl.ORGUNIT_RESOURCE)) {
////		                    addNode(father, resId, treeName,
////		                    		nodeType, true, curLevel, (String) null,
////		                            "00", "00", rootparam);
////	                    }
//	                    
//	                    Map map = new HashMap();
//	                    map.put("nodeLink",request.getContextPath() + "/sysmanager/accessmanager/role/operList_org_ajax.jsp?resTypeId=orgunit");
//	                     treeName ="离散用户管理";
//	                     resId ="lisan";
//	                    map.put("resName",treeName);
//	                    map.put("resId",resId);
//	                    String lisannodeType = "lisan";
//	                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,resId,resTypeId)){
//	                    	lisannodeType = "lisan_true";
//	                    }else{
//	                    	
//	                    }
//	                    
//	                    if (super.accessControl.checkPermission("lisan",
//	                            "lisanusermanager", AccessControl.ORGUNIT_RESOURCE) ||
//	                            super.accessControl.checkPermission("lisan",
//	                                    "lisanusertoorg", AccessControl.ORGUNIT_RESOURCE )) {
//		                    addNode(father, resId, treeName,
//		                    		lisannodeType, true, curLevel, (String) null,
//		                    		(String) resId, (String) resId, map);
//	                    }
//	                    
//	                    
//	                }
	            }
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        return true;
	    }

	}

