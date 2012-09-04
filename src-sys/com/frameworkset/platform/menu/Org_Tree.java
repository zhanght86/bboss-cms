package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class Org_Tree extends COMTree implements Serializable{

	public boolean hasSon(ITreeNode father) {

		if (father.isRoot())
			return true;
		else if (father.getType().equals("orgroot")) {
			String treeID = "0";
			try {

				 return OrgCacheManager.getInstance().hasSubOrg(treeID);
			} catch (Exception e) {
				
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
		} else if (father.getType().equals("persongrouproot")) {
			DBUtil dbUtil = new DBUtil();
		
			String userId = accessControl.getUserID();
			String username = accessControl.getUserAccount();		
			String sql = "select count(*) from TD_GROUP where (ower='"+ username+ "' or ','||share_id like '%,"+ userId + ",%')";
			try {
				dbUtil.executeSelect(sql);
				if(dbUtil.getInt(0,0)>0)
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
			String sql = "select count(*) from TD_CHILDGROUP where parentid='"+treeID+"'";
			try {
				dbUtil.executeSelect(sql);
				if(dbUtil.getInt(0,0)>0)
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
//			addNode(father, "sysgrouproot", "系统组", "sysgrouproot", false,
//					curLevel, (String) null, (String) null, (String) null);
//			addNode(father, "persongrouproot", "个人组", "persongrouproot", false,
//					curLevel, (String) null, (String) null, (String) null);
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
						map.put("orgName",sonorg.getOrgName());
						map.put("showName",sonorg.getRemark5());
						map.put("nodeType","org");
						
						map.put("displayNameInput", displayNameInput);
						map.put("displayValueInput", displayValueInput);
						
						addNode(father, sonorg.getOrgId(), treeName, "org",
								true, curLevel, (String) null,
								(String) null, (String) null, map);						
//						if (super.accessControl.checkPermission(sonorg
//								.getOrgId(), AccessControl.WRITE_PERMISSION,
//								AccessControl.ORGUNIT_RESOURCE)) {
//							addNode(father, sonorg.getOrgId(), treeName, "org",
//									true, curLevel, (String) null,
//									(String) null, (String) null, map);
//						} else {
//							if (accessControl.checkPermission(
//									sonorg.getOrgId(),
//									AccessControl.READ_PERMISSION,
//									AccessControl.ORGUNIT_RESOURCE)) {
//								addNode(father, sonorg.getOrgId(), treeName,
//										"org", false, curLevel, (String) null,
//										(String) null, (String) null, map);
//							}
//						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} else if (father.getType().equals("sysgrouproot") || father.getType().equals("sysgroup")) {
			String treeID = "0";
			if(father.getType().equals("sysgrouproot"))
				treeID = "0";
			else
				treeID = father.getId();
			
			try {
				GroupManager groupManager = SecurityDatabase.getGroupManager();
				Group group = new Group();
				group.setGroupId(Integer.parseInt(treeID));
				List groupList = groupManager.getChildGroupList(group);
				if (groupList != null) {
					Iterator iterator = groupList.iterator();
					while (iterator.hasNext()) {
						Group songroup = (Group) iterator.next();
						Map map = new HashMap();
						map.put("groupId", songroup.getGroupId() + "");
						map.put("nodeType","sysgroup");
						
						map.put("displayNameInput", displayNameInput);
						map.put("displayValueInput", displayValueInput);
//						addNode(father, songroup.getGroupId().toString(),
//								songroup.getGroupName(), "sysgroup", true, curLevel,
//								(String) null, (String) null, (String) null,
//								map);

//						if (super.accessControl.checkPermission(songroup
//								.getGroupId().toString(),
//								AccessControl.WRITE_PERMISSION,
//								AccessControl.GROUP_RESOURCE)) {
//							addNode(father, songroup.getGroupId().toString(),
//									songroup.getGroupName(), "sysgroup", true, curLevel,
//									(String) null, (String) null, (String) null,
//									map);
//						} else if (super.accessControl.checkPermission(songroup
//								.getGroupId().toString(),
//								AccessControl.READ_PERMISSION,
//								AccessControl.GROUP_RESOURCE)) {
//							addNode(father, songroup.getGroupId().toString(),
//									songroup.getGroupName(), "sysgroup", false,
//									curLevel, (String) null, (String) null,
//									(String) null, map);
//						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 设置系统组儿子组
		} else if (father.getType().equals("persongrouproot") || father.getType().equals("persongroup")) {
				DBUtil dbUtil = new DBUtil();
			
				String userId = accessControl.getUserID();
				String username = accessControl.getUserAccount();

				if (father.getType().equals("persongrouproot")) {

					String sql = "select id,GROUPNAME from TD_GROUP where"
							+ " (ower='"+ username+ "' or ','||share_id like '%,"+ userId + ",%')";
					try {
						dbUtil.executeSelect(sql);
						for (int i = 0; dbUtil != null && i < dbUtil.size(); i++) {
							Map map = new HashMap();
							map.put("pgroupId", dbUtil.getString(i, "id"));
							map.put("nodeType", "persongroup");
							map.put("displayNameInput", displayNameInput);
							map.put("displayValueInput", displayValueInput);
//							addNode(father, dbUtil.getString(i, "id"), dbUtil
//									.getString(i, " GROUPNAME"), "persongroup",
//									false, curLevel, (String) null, (String) null,
//									(String) null, map);
						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}else{				
					String treeID = father.getId();
					String sql = "select id,GROUPNAME from TD_CHILDGROUP where parentid = '"
							+ treeID + "'";
					try {
						dbUtil.executeSelect(sql);
						for (int i = 0; dbUtil != null && i < dbUtil.size(); i++) {

							Map map = new HashMap();
							map.put("pgroupId", dbUtil.getString(i, "id"));
							map.put("nodeType", "persongroup");

							map.put("displayNameInput", displayNameInput);
							map.put("displayValueInput", displayValueInput);
//							addNode(father, dbUtil.getString(i, "id"), dbUtil
//									.getString(i, " GROUPNAME"), "persongroup",
//									true, curLevel, (String) null, (String) null,
//									(String) null, map);

						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}			

		}

		return true;
	}

}
