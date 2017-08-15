/**
 * 
 */
package com.frameworkset.platform.dictionary;

/**
 * 字典管理机构树
 * 获取用户有权限选择的机构
 * <p>Title: AccreditOrgTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-11-21 15:58:02
 * @author ge.tao
 * @version 1.0
 */
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
public class AccreditOrgTree extends COMTree implements java.io.Serializable{
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
	               
	    			//当前用户所在的 主机构ID
	    			String orgId = super.accessControl.getChargeOrgId();
	                while (iterator.hasNext()) {
	                    Organization sonorg = (Organization) iterator.next();
	                    
	                    String treeName  = "";
	                	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals(""))
	                		treeName = sonorg.getOrgName();
	                    else
	                    	treeName = sonorg.getRemark5();
	                	treeName = sonorg.getOrgnumber() + " " + treeName; 
	                    Map map = new HashMap();
	                    map.put("orgId",sonorg.getOrgId());
	                    map.put("resId",sonorg.getOrgId());
	                    map.put("resName", treeName);
	                    String nodeType = "org";
	                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,sonorg.getOrgId(),resTypeId)){
	                    	nodeType = "org_true";
	                    }else{
	                    	nodeType = "org";
	                    }
	                    if (super.accessControl.isOrganizationManager(sonorg.getOrgId()) ||
	                			super.accessControl.isAdmin()) {
	                		
	                		//如果用户不是admin,过滤登陆用户所在的机构... radiao变灰色
		                    if(orgId != null && sonorg.getOrgId().equalsIgnoreCase(orgId) && !super.accessControl.isAdmin()){
		                    	addNode(father, sonorg.getOrgId(), treeName,
		                        		nodeType, true, curLevel, (String) null,
		                        		(String) sonorg.getOrgId()+":"+treeName+"' disabled='true",
		                        		(String) sonorg.getOrgId(), map);
		                    }else{
		                    	addNode(father, sonorg.getOrgId(), treeName,
		                        		nodeType, true, curLevel, (String) null,
		                        		(String) sonorg.getOrgId()+":"+treeName, (String) sonorg.getOrgId(), map);
		                    }
	                        
	                    } else {
	                    	if (super.accessControl.isSubOrgManager(sonorg.getOrgId())) {
//	                        	如果用户不是admin,过滤登陆用户所在的机构... radiao变灰色
			                    if(orgId != null && sonorg.getOrgId().equalsIgnoreCase(orgId) && !super.accessControl.isAdmin()){
		                            addNode(father, sonorg.getOrgId(),
		                            		treeName, nodeType, false, curLevel,
		                                (String) null, 
		                                (String) sonorg.getOrgId()+":"+treeName+"' disabled='true",
		                                (String) sonorg.getOrgId(), map);
			                    }else{
			                    	addNode(father, sonorg.getOrgId(),
		                            		treeName, nodeType, false, curLevel,
		                                (String) null, 
		                                (String) sonorg.getOrgId()+":"+treeName+"' disabled='true",
		                                (String) sonorg.getOrgId(), map);
			                    }
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


