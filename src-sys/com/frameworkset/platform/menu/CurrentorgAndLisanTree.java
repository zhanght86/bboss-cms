/*
 * Created on 2006-3-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * @author ok
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CurrentorgAndLisanTree extends COMTree implements Serializable {
	String parentPath = "";
	String orgId = "";
	String curOrgId = "";
	public void setPageContext(PageContext context)
	{
		super.setPageContext(context);
		orgId = request.getParameter("orgId");
		curOrgId = super.accessControl.getChargeOrgId();
		
	}
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

			List orglist = OrgCacheManager.getInstance().getSubOrganizations(
					treeID);
			
			
				if (orglist != null) {
					Iterator iterator = orglist.iterator();

					while (iterator.hasNext()) {
						Organization sonorg = (Organization) iterator.next();

						/*
						 * 判断如果sonorg是orgId的子机构，或者sonorg是离散用户，就执行以下代码
						 * 留待完成，危达200711141516
						 */

						String treeName = "";
						if (sonorg.getRemark5() == null
								|| sonorg.getRemark5().trim().equals("")) {
							if (ConfigManager.getInstance()
									.getConfigBooleanValue(
											"sys.orgtree.org_no_name_display",
											false)) {
								treeName = sonorg.getOrgId() + "-"
										+ sonorg.getOrgName();
							} else {
								treeName = sonorg.getOrgName();
							}
						} else {
							if (ConfigManager.getInstance()
									.getConfigBooleanValue(
											"sys.orgtree.org_no_name_display",
											false)) {
								treeName = sonorg.getOrgId() + "-"
										+ sonorg.getRemark5();
							} else {
								treeName = sonorg.getRemark5();
							}
						}

						Map map = new HashMap();
						map.put("orgId", sonorg.getOrgId());
						map.put("resId", sonorg.getOrgId());
						map.put("resName", treeName);
						map.put("displayNameInput", displayNameInput);
						map.put("displayValueInput", displayValueInput);
						if (accessControl.isOrganizationManager(sonorg.getOrgId())
								|| accessControl.isAdmin()) {
							if (orgId.equals(sonorg.getOrgId())) {
								
							} else {
								addNode(father, sonorg.getOrgId(), treeName,
										"org", true, curLevel, (String) null,
										(String) null, (String) null, map);
							}
						} else {
							if (super.accessControl.isSubOrgManager(sonorg.getOrgId())) {
								addNode(father, sonorg.getOrgId(), treeName,
										"org", false, curLevel, (String) null,
										(String) null, (String) null, map);
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
