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
 * 
 * <p>Title: GroupAuthorTree</p>
 *
 * <p>Description: 组的授权树</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-26 14:20:38
 * @author biaoping.yin
 * @version 1.0
 */

public class GroupAuthorTree extends COMTree implements Serializable{

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();
		try {
			if(father.isRoot())
			{
				return true;
			}
			else
			{
				if(treeID.equals("addrootgroup")) //创建组的权限节点
				{
					return false;
				}
				else
				{
					GroupManager groupManager = SecurityDatabase.getGroupManager();
					Group group = new Group();
					group.setGroupId(Integer.parseInt(treeID));
					return groupManager.isContainChildGroup(group);
				}
			}
//			GroupManager groupManager = SecurityDatabase.getGroupManager();
//			Group group = new Group();
//			group.setGroupId(Integer.valueOf(treeID));
//			return groupManager.isContainChildGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();

		try {
			 String resTypeId=request.getParameter("resTypeId");
			String roleId = (String)session.getAttribute("currRoleId");
			String roleTypeId = (String)session.getAttribute("role_type");
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
					if (super.accessControl.checkPermission(songroup
							.getGroupId()+"",
							AccessControl.READ_PERMISSION,
							AccessControl.GROUP_RESOURCE))
					{
						if(AccessControl.hasGrantedRole(roleId,roleTypeId,songroup.getGroupId()+"",resTypeId)){
							addNode(father, songroup.getGroupId()+"",
									songroup.getGroupName(), "org_true", true, curLevel,
									(String) null, (String) null, (String) null,
									map);
	                    	
	                    }
						else
	                    {
	                    	
	                    	addNode(father, songroup.getGroupId()+"",
									songroup.getGroupName(), "org", true, curLevel,
									(String) null, (String) null, (String) null,
									map);
	                    }
					}
						
					
				}
			}
//			if(father.isRoot()) //添加全局的组创建节点
//			{
//				if (super.accessControl.checkPermission("addrootgroup",
//						"addgroup",
//						AccessControl.GROUP_RESOURCE)) {
//					Map map = new HashMap();
//					map.put("resId", "addrootgroup");
//					map.put("resName", "添加一级组");
//					if(AccessControl.hasGrantedRole(roleId,roleTypeId,"addrootgroup",resTypeId)){
//						addNode(father, "addrootgroup",
//								"添加一级组", "addrootgroup_true", true, curLevel,
//								(String) null, (String) null, (String) null,
//								map);
//					}
//					else
//					{
//						addNode(father, "addrootgroup",
//								"添加一级组", "addrootgroup", true, curLevel,
//								(String) null, (String) null, (String) null,
//								map);
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
