package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.config.ConfigException;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/*
 * <p>Title: 用户调出树</p>
 * <p>Description: 用户调出树显示类</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: iSany</p>
 * @Date 2008-3-20
 * @author gao.tang
 * @version 1.0
 */
public class DisperseOrgJob extends COMTree implements Serializable{

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
		

		try {
			
			List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);
			
			if (orglist != null) {
				Iterator iterator = orglist.iterator();
				while (iterator.hasNext()) {
					Organization sonorg = (Organization) iterator.next();
					Map map = new HashMap();
					String orgId = sonorg.getOrgId();
					map.put("orgId", orgId);
					map.put("resId", orgId);
					map.put("orgName", sonorg.getRemark5());

					if (super.accessControl.isOrganizationManager(orgId) ||
							super.accessControl.isAdmin()) {
						addNode(father, orgId, sonorg.getRemark5().trim(),
								"org", true, curLevel, (String) null,
								(String) sonorg.getOrgId() + ";"
								+ sonorg.getRemark5(),
								(String) sonorg.getOrgId() + ";"
										+ sonorg.getRemark5(), map);
					} else {
						if (super.accessControl.isSubOrgManager(orgId)) {
							addNode(father, orgId, sonorg
									.getRemark5().trim(), "org", false, curLevel,									
									(String) null,//备注字段 
									(String) sonorg.getOrgId() + ";"
									+ sonorg.getRemark5()+"' disabled='true",//单选框的值
									(String) sonorg.getOrgId() + ";"
									+ sonorg.getRemark5()+"' disabled='true",//复选框的值
								    map);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//2006-08-01添加离散用户权限控制
        if(ConfigManager.getInstance().getConfigBooleanValue("enableorgusermove",true) &&
        		!ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg",true)){
            if(father.isRoot())
            {
            	String resId = null;
				try {
					Map map = new HashMap();
	                ResourceManager resManager = new ResourceManager();
					resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
					map.put("resId", resId);
	                    addNode(father, "lisan", "离散用户管理",
	                            "lisan", true, curLevel, (String) null,
	                            resId, resId, map);
				} catch (ConfigException e) {
					e.printStackTrace();
				}      
            }
        }

		return true;
	}
}
