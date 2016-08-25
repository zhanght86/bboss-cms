package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * @author feng.jing
 * @file GroupTree.java Created on: Mar 20, 2006
 */
public class GroupTree extends COMTree implements Serializable{

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		try {
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			Group group = new Group();
			group.setGroupId(Integer.parseInt(treeID));
			return groupManager.isContainChildGroup(group);
		} catch (Exception e) {
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
			GroupManager groupManager = SecurityDatabase.getGroupManager();
			Group group = new Group();
			group.setGroupId(Integer.parseInt(treeID));
			List groupList = groupManager.getChildGroupList(group);
			if (groupList != null) {
				Iterator iterator = groupList.iterator();
				while (iterator.hasNext()) {
					Group songroup = (Group) iterator.next();
					Map map = new HashMap();
					map.put("groupId", songroup.getGroupId()+"");
					map.put("resId", songroup.getGroupId()+"");
					map.put("resName", songroup.getGroupName());
					String nodeType = "";
					if(AccessControl.hasGrantedRole(roleId,roleTypeId,String.valueOf(songroup.getGroupId()),resTypeId)){
                    	nodeType = "org_true";
                    }else{
                    	nodeType = "org";
                    }
					if (super.accessControl.checkPermission(songroup
							.getGroupId()+"",
							AccessControl.READ_PERMISSION,
							AccessControl.GROUP_RESOURCE)) {
						addNode(father, songroup.getGroupId()+"",
								songroup.getGroupName(), nodeType, true, curLevel,
								(String) null, (String) null, (String) null,
								map);
					} 
//					else if (super.accessControl.checkPermission(songroup
//							.getGroupId().toString(),
//							AccessControl.READ_PERMISSION,
//							AccessControl.GROUP_RESOURCE)) {
//						addNode(father, songroup.getGroupId().toString(),
//								songroup.getGroupName(), "org", false,
//								curLevel, (String) null, (String) null,
//								(String) null, map);
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
