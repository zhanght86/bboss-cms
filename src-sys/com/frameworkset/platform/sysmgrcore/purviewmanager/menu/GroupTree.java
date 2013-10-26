package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

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

//		String resTypeId=request.getParameter("resTypeId");
//		String roleId = (String)session.getAttribute("currRoleId");
//		String roleTypeId = (String)session.getAttribute("role_type");
		String resTypeId=request.getParameter("resTypeId");
		String roleId = request.getParameter("currRoleId");
		String roleTypeId = request.getParameter("role_type");
		String currOrgId = request.getParameter("currOrgId");
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
					 //res_id:restype_id:res_name
	                String ckeckVal = groupId+"#"+resTypeId+"#"+groupName;
	              //已经授权的复选框显示选中状态
                    String ms = ((AccessControl)accessControl).getSourceUserRes_jobRoleandRoleandSelf(currOrgId,roleId,groupName,resTypeId,groupId,"usergroupset");
                    //System.out.println("ms = " + ms);
                    if(!"".equals(ms) && ms != null){
                    	ms = "-->资源来源：" + ms;
                    }
                    if(AccessControl.hasGrantedRole(roleId,roleTypeId,groupId,resTypeId)){
        				map.put("node_checkboxchecked",new Boolean(true));
        			}
//					String nodeType = "";
//					if(AccessControl.hasGrantedRole(roleId,roleTypeId,String.valueOf(songroup.getGroupId()),resTypeId)){
//                    	nodeType = "org_true";
//                    }else{
//                    	nodeType = "org";
//                    }
//					if (super.accessControl.checkPermission(groupId,
//							AccessControl.READ_PERMISSION,
//							AccessControl.GROUP_RESOURCE)) {
                    if(super.accessControl.isAdmin()){
						addNode(father, groupId,
								(groupName+ms).trim(), "org", true, curLevel,
								(String) null, (String) null, ckeckVal,
								map);
					}else{
						if(super.accessControl.getUserID().equals(String.valueOf(songroup.getOwner_id()))){
							addNode(father, groupId,
									(groupName+ms).trim(), "org", true, curLevel,
									(String) null, (String) null, ckeckVal,
									map);
						}else{
							addNode(father, groupId,
									(groupName+ms).trim(), "org", true, curLevel,
									(String) null, (String) null, (String) null,
									map);
						}
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
