package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 
 * @author gao.tang 
 * @file DisperseOrgJob.java Created on: Apr 12, 2007
 */
public class DictOrgSelect extends COMTree implements Serializable{
	
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

					if (accessControl.isOrganizationManager(orgId) ||
                			accessControl.isAdmin()) {
						addNode(father, orgId, sonorg.getRemark5(),
								"org", true, curLevel, (String) null,
								(String) orgId + " " + sonorg.getRemark5(),
								 (String) orgId + " " + sonorg.getRemark5(),
								map);
					} 
						else {
						if (super.accessControl.isSubOrgManager(orgId)) {
							addNode(father, orgId, sonorg
									.getRemark5(), "org", false, curLevel,
									(String) orgId + " "+ sonorg.getRemark5(),
									orgId+"' disabled='true",
									(String) orgId + " "+ sonorg.getRemark5(),
									map);
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
