package com.frameworkset.platform.dictionary;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class DictOrgUserTree extends COMTree implements java.io.Serializable{
	public boolean hasSon(ITreeNode father) {

		if (father.isRoot())
			return true;
		else if (father.getType().equals("orgroot")) {
			String treeID = "0";
			try {

				 return OrgCacheManager.getInstance().hasSubOrg(treeID);
				 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (father.getType().equals("org")) {
			try {
				
				String treeID = father.getId();
				return OrgCacheManager.getInstance().hasSubOrg(treeID);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (father.getType().equals("sysgrouproot") || father.getType().equals("sysgroup")) {
			String treeID = "0";
			if(father.getType().equals("sysgroup"))
				treeID = father.getId();
			try {
				GroupManager groupManager = SecurityDatabase.getGroupManager();
				Group group = new Group();
				group.setGroupId(Integer.parseInt(treeID));
				return groupManager.isContainChildGroup(group);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		else if (father.getType().equals("persongrouproot")) {
			DBUtil dbUtil = new DBUtil();
			AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String userId = accesscontroler.getUserID();
			String username = accesscontroler.getUserAccount();		
			String sql = "select id from TD_GROUP where (ower='"+ username+ "' or ','||share_id like '%,"+ userId + ",%')";
			try {
				dbUtil.executeSelect(sql);
				if(dbUtil!=null&&dbUtil.size()>0)
					return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return false;
			// 判断有没有儿子
		}
		else if (father.getType().equals("persongroup")) {
			String treeID = father.getId();
			DBUtil dbUtil = new DBUtil();			
			String sql = "select id from TD_CHILDGROUP where parentid='"+treeID+"'";
			try {
				dbUtil.executeSelect(sql);
				if(dbUtil!=null&&dbUtil.size()>0)
					return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return false;			
			// 判断有没有儿子
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {

		String displayNameInput = request.getParameter("displayNameInput");
		String displayValueInput = request.getParameter("displayValueInput");
		if (father.isRoot()) {
			String treeID = father.getId();

			addNode(father, "orgroot", "部门", "orgroot", false, curLevel,
					(String) null, (String) null, (String) null);
			

		} else if (father.getType().equals("orgroot") || father.getType().equals("org")) {
			String treeID = "0";
			if(father.getType().equals("orgroot"))
				treeID = "0";
			else
				treeID = father.getId();
			try {
				

				 List orglist = OrgCacheManager.getInstance().getSubOrganizations(treeID);

				if (orglist != null) {
					Iterator iterator = orglist.iterator();

					while (iterator.hasNext()) {
						Organization sonorg = (Organization) iterator.next();
						String treeName = "";
						if (sonorg.getRemark5() == null
								|| sonorg.getRemark5().trim().equals(""))
							treeName = sonorg.getOrgName();
						else
							treeName = sonorg.getRemark5();

						Map map = new HashMap();
						map.put("orgId", sonorg.getOrgId());
						
						map.put("nodeType","org");
						
						map.put("displayNameInput", displayNameInput);
						map.put("displayValueInput", displayValueInput);
						if (super.accessControl.isOrganizationManager(sonorg.getOrgId()) ||
	                			super.accessControl.isAdmin()) {//部门管理员登陆过滤机构 {
							addNode(father, sonorg.getOrgId(), treeName, "org",
									true, curLevel, (String) null,
									(String) null, (String) null, map);
						} else if (super.accessControl.isSubOrgManager(sonorg.getOrgId())) {
							addNode(father, sonorg.getOrgId(), treeName,
										"org", false, curLevel, (String) null,
										(String) null, (String) null, map);
							
						}
					}
				}
			} catch (Exception e) {
			
				e.printStackTrace();
			}
		
		} 

		return true;
	}

}

