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

/**
 * 
 * @author 景峰
 * @file OrgTreeCheckbox.java Created on: Apr 7, 2006
 */
public class OrgTreeCheckbox extends COMTree implements Serializable{

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
					map.put("orgId", sonorg.getOrgId());
					map.put("resId", sonorg.getOrgId());
					map.put("orgName", sonorg.getRemark5());

					if (super.accessControl.checkPermission(sonorg.getOrgId(),
							AccessControl.WRITE_PERMISSION, AccessControl.ORGUNIT_RESOURCE)) {
						addNode(father, sonorg.getOrgId(), sonorg.getRemark5(),
								"org", true, curLevel, (String) null,
								(String) sonorg.getOrgId() + ";"
								+ sonorg.getRemark5(), (String) sonorg.getOrgId() + ";"
										+ sonorg.getRemark5(), map);
					} else {
						if (accessControl.checkPermission(sonorg.getOrgId(),
								AccessControl.READ_PERMISSION, AccessControl.ORGUNIT_RESOURCE)) {
							addNode(father, sonorg.getOrgId(), sonorg
									.getRemark5(), "org", false, curLevel,
									(String) sonorg.getOrgId() + ";"
									+ sonorg.getRemark5(), sonorg.getOrgId() + ";",
									(String) sonorg.getOrgId() + ";"
											+ sonorg.getRemark5(), map);
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
