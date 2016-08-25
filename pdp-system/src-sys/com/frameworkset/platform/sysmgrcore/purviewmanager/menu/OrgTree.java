/*
 * Created on 2006-3-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
 * <p>Title: 角色管理模块;机构管理模块</p>
 * <p>Description: 角色管理模块：角色授予给用户左边显示的机构树；机构管理模块：岗位授予机构菜单弹出页面右边显示机构树;
 * 			机构管理模块：机构转移树
 * </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: iSany</p>
 * @Date 2008-3-20
 * @author gao.tang
 * @version 1.0
 */
public class OrgTree extends COMTree implements Serializable {
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
                    String orgId = sonorg.getOrgId();
                    
                	if(sonorg.getRemark5() == null || sonorg.getRemark5().trim().equals("")){
                		if(ConfigManager.getInstance().getConfigBooleanValue("sys.orgtree.org_no_name_display", false)){
                			treeName = orgId + "-" + sonorg.getOrgName();
                		}else{
                			treeName = sonorg.getOrgName();
                		}
                	}
                    else{   
                    	if(ConfigManager.getInstance().getConfigBooleanValue("sys.orgtree.org_no_name_display", false)){
                			treeName = orgId + "-" + sonorg.getRemark5();
                		}else{
                			treeName = sonorg.getRemark5();
                		}
                    }
                	String encodeOrgName = java.net.URLEncoder.encode(treeName,"UTF-8");
                    Map map = new HashMap();
                    map.put("orgId", orgId);
                    map.put("resId", orgId);                    
                	map.put("resName", encodeOrgName);
                	map.put("displayNameInput", displayNameInput);
                	map.put("displayValueInput", displayValueInput);
                
                	if (accessControl.isOrganizationManager(orgId) ||
                			accessControl.isAdmin()) {
                        addNode(father, orgId, treeName.trim(),
                            "org", true, curLevel, orgId+":"+encodeOrgName,
                            orgId+":"+treeName, (String) null, map);
                    } else {
                        if (super.accessControl.isSubOrgManager(orgId)) {
                            addNode(father, orgId,
                            		treeName.trim(), "org", false, curLevel,
                                (String) null, (String) null, (String) null, map);
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
