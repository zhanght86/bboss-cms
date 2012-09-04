package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.contextmenu.Menu;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * @author feng.jing
 * @file GroupTree.java Created on: Mar 20, 2006
 */
public class GroupAddTree extends COMTree implements Serializable {

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
					String groupId = songroup.getGroupId()+"";
					String groupName = songroup.getGroupName();
					map.put("groupId", groupId);
					map.put("resId", groupId);
					map.put("resName", groupName);
					addNode(father, groupId,
							groupName.trim(), "org", true, curLevel,
							(String) null, (String) null, (String) null,
							map); 
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
	
	protected void buildContextMenus() {
    	if(accessControl.isAdmin())
    	{
	    	Menu groupmenu = new Menu();
	    	groupmenu.setIdentity("1");
        
    		//用户组缓冲刷新
			Menu.ContextMenuItem groupcacherefresh = new Menu.ContextMenuItem();
			groupcacherefresh.setName("用户组缓冲刷新");
			groupcacherefresh.setLink("javascript:refreshgroupcache()");
			groupcacherefresh
					.setIcon(request.getContextPath()
							+ "/sysmanager/images/dialog-reset.gif");
			groupmenu.addContextMenuItem(groupcacherefresh);
			
			
            super.addContextMenuOfType(groupmenu);
    	}
	}
}
