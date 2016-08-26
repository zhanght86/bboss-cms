package com.frameworkset.platform.cms.sitemember.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
/**
 * 显示医药网角色的树
 * @author Administrator
 *
 */
public class RoleDrugTree extends COMTree implements java.io.Serializable{
	public boolean hasSon(ITreeNode father) {
		String roleID = father.getId();
		try {
			Role role = new Role();
			role.setRoleId(roleID);
			if (roleID.equals("0")) {
				return true;
			} else
				return false;
			// return role.isContainChildRole(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String roleID = father.getId();

		try {
			RoleManager roleManager = SecurityDatabase.getRoleManager();
			Role role = new Role();
			role.setRoleId(roleID);
			List roleList = roleManager.getRoleList("select * from td_sm_Role r where r.role_Type ='member' and r.remark1='2' or r.remark1='0'  order by r.role_Name");
			if (roleList != null) {
				Iterator iterator = roleList.iterator();
				while (iterator.hasNext()) {
					Role sonrole = (Role) iterator.next();
					Map map = new HashMap();
					map.put("roleId", sonrole.getRoleId());
					map.put("resId", sonrole.getRoleId());
					map.put("resName", sonrole.getRoleName());
					if (super.accessControl.checkPermission(sonrole.getRoleId(),
							AccessControl.WRITE_PERMISSION,
							AccessControl.ROLE_RESOURCE)) {
						addNode(father, sonrole.getRoleId(), sonrole
								.getRoleName(), "org", true, curLevel,
								(String) null, (String) null, (String) null,
								map);
					}
					else if (super.accessControl.checkPermission(sonrole.getRoleId(),
							AccessControl.READ_PERMISSION,
							AccessControl.ROLE_RESOURCE)) {
						addNode(father, sonrole.getRoleId(), sonrole
								.getRoleName(), "org", false, curLevel,
								(String) null, (String) null, (String) null,
								map);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
