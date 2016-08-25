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

public class OrgGrantUserOrRoleTree extends COMTree implements Serializable{
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
       
        String resTypeId=(String)request.getAttribute("resTypeId");
    	String roleId = (String)session.getAttribute("currRoleId");
    	String roleTypeId = (String)request.getAttribute("role_type");
    	List roleIds = (List)request.getAttribute("roleIds");
    	//System.out.println("resTypeId:"+resTypeId+" ,roleTypeId:"+roleTypeId+" ,roleIds1:"+roleIds);
    	
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
                	//treeName = sonorg.getOrgnumber() + " " + treeName; 
                    Map map = new HashMap();
                    map.put("orgId",sonorg.getOrgId());
                    map.put("resId",sonorg.getOrgId());
                    map.put("resName", treeName);
                    //map.put("nodeLink","choose("+sonorg.getOrgId()+")");
                    String nodeType = "org";
                    String cbValue = sonorg.getOrgId()+";"+treeName+";orgunit";
                    if(hasOrgChecked(roleIds,sonorg.getOrgId())){//AccessControl.hasGrantedRole(roleId,roleTypeId,sonorg.getOrgId(),resTypeId)){
                    	map.put("node_checkboxchecked",new Boolean(true));
                    }
                	if (super.accessControl.checkPermission(sonorg.getOrgId(),
                            "readorgname", AccessControl.ORGUNIT_RESOURCE)) {
                		
                		//如果用户不是admin,过滤登陆用户所在的机构... radiao变灰色
	                    if(orgId != null && sonorg.getOrgId().equalsIgnoreCase(orgId) && !accessControl.isAdmin()){
	                    	addNode(father, sonorg.getOrgId(), treeName,
	                        		nodeType, true, curLevel, (String) null,
	                        		(String) sonorg.getOrgId()+":"+treeName+"' disabled='true",
	                        		cbValue, map);
	                    }else{
	                    	addNode(father, sonorg.getOrgId(), treeName,
		                        		nodeType, true, curLevel, (String) null,
		                        		(String) null,
		                        		cbValue, map);
	                    }
                        
                    } else {
                        if (super.accessControl.isSubOrgManager(sonorg.getOrgId())) {
//                        	如果用户不是admin,过滤登陆用户所在的机构... radiao变灰色
		                    if(orgId != null && sonorg.getOrgId().equalsIgnoreCase(orgId) && !accessControl.isAdmin()){
	                            addNode(father, sonorg.getOrgId(),
	                            		treeName, nodeType, false, curLevel,
	                                (String) null, 
	                                (String) sonorg.getOrgId()+":"+treeName+"' disabled='true",
	                                cbValue, map);
		                    }else{
		                    	addNode(father, sonorg.getOrgId(),
	                            		treeName, nodeType, false, curLevel,
	                                (String) null, 
	                                (String) sonorg.getOrgId()+":"+treeName+"' disabled='true",
	                                cbValue, map);
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

    /**
     * 判断
     * @param orgId 机构ID
     * @return
     */
    private boolean hasOrgChecked(List roleIds,String mId){
    	if(roleIds!=null && roleIds.size()>0){
    		for(int i=0;i<roleIds.size();i++){
    			String roleId = (String)roleIds.get(i);
    			if(mId.equals(roleId)) return true;
    		}
    	}
    	return false;
    }

}
