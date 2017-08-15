package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.RoleType;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 
 * <p>Title: Role_Tree</p>
 *
 * <p>Description: 角色资源授权树</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-30 14:23:18
 * @author biaoping.yin
 * @version 1.0
 */
public class Role_Tree extends COMTree  implements Serializable{

	RoleTypeManager rtm = new RoleTypeManager();
	
	public boolean hasSon(ITreeNode father) {
		//String roleID = father.getId();
		try {
			//Role role = new Role();
			//role.setRoleId(roleID);
			//if (roleID.equals("0")) {
			//	return true;
			//} else
			//	return false;
			// return role.isContainChildRole(role);
			if(father.isRoot()){
				return true;
			}else{
				if(father.getType().equals("roletype")){
					String id[] = father.getId().split("\\:");
					return rtm.hasRoles(id[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String roleID = father.getId();
		
		String resTypeId=request.getParameter("resTypeId");
		String roleId = (String)session.getAttribute("currRoleId");
		String roleTypeId = (String)session.getAttribute("role_type");
		
		if(father.isRoot()){
			try
			{				
				List roleTypeList = rtm.getTypeNameList();
				RoleManager roleManager = SecurityDatabase.getRoleManager();
				if (roleTypeList.size()>0) 
				{
					for(int i = 0;  i < roleTypeList.size(); i ++)
					{
						RoleType rt = (RoleType) roleTypeList.get(i);
						Map map = new HashMap();
						map.put("roleTypeId", rt.getRoleTypeID());
						map.put("roleTypeName", rt.getTypeName());	
						map.put("roleTYPEDesc",rt.getTypeDesc());
						String roleTypename = rt.getTypeName();
						StringBuffer sql = new StringBuffer()
							.append("select * from td_sm_role r where ")
							.append("r.role_Id <> '1' and r.role_Id <> '2' and r.role_Id <> '3' ")
							.append("and r.role_Id <> '").append(roleId).append("' ")
							.append("and r.ROLE_TYPE='").append(rt.getRoleTypeID()).append("' ")
							.append(" order by r.role_Name");
						List roleList = roleManager.getRoleList(sql.toString());
						//List roleList = rtm.getRoleList(rt.getRoleTypeID());
						boolean isRoletype = false;
						for(int count = 0;  count < roleList.size(); count ++)
						{
							Role sonrole = (Role) roleList.get(count);
							if(super.accessControl.checkPermission(sonrole.getRoleId(),
									AccessControl.READ_PERMISSION,
									AccessControl.ROLE_RESOURCE) ||
									super.accessControl.checkPermission(sonrole.getRoleId(),
											AccessControl.WRITE_PERMISSION,
											AccessControl.ROLE_RESOURCE)){
								isRoletype = true;
								break;
							}
						}
						if(isRoletype){
							addNode(father, "type:" + rt.getRoleTypeID(), roleTypename, "roletype", false, curLevel,(String) null, (String) null, (String) null,map);
						}
					}	
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}else if(father.getType().equals("roletype")){
			try {
				Role role = new Role();
				role.setRoleId(roleID);
				RoleManager roleManager = SecurityDatabase.getRoleManager();
//				List roleList = new ArrayList();
				String id[] = father.getId().split("\\:");
				StringBuffer sql = new StringBuffer()
					.append("select * from td_sm_role r where ")
					.append("r.role_Id <> '1' and r.role_Id <> '2' and r.role_Id <> '3' ")
					.append("and r.role_Id <> '").append(roleId).append("' ")
					.append("and r.ROLE_TYPE='").append(id[1]).append("' ")
					.append(" order by r.role_Name");
				List roleList = roleManager.getRoleList(sql.toString());
				 
				if (roleList != null) {
					Iterator iterator = roleList.iterator();
					while (iterator.hasNext()) {
						Role sonrole = (Role) iterator.next();
						Map map = new HashMap();
						map.put("roleId", sonrole.getRoleId());
						map.put("resId", sonrole.getRoleId());
						map.put("resName", sonrole.getRoleName());
						String nodeType = "";
						if(AccessControl.hasGrantedRole(roleId,roleTypeId,sonrole.getRoleId(),resTypeId)){
	                    	nodeType = "org_true";
	                    }else{
	                    	nodeType = "org";
	                    }
						if (super.accessControl.checkPermission(sonrole.getRoleId(),
								AccessControl.WRITE_PERMISSION,
								AccessControl.ROLE_RESOURCE)) {
							addNode(father, sonrole.getRoleId(), sonrole
									.getRoleName(), nodeType, true, curLevel,
									(String) null, (String) null, (String) null,
									map);
						}
						else if (super.accessControl.checkPermission(sonrole.getRoleId(),
								AccessControl.READ_PERMISSION,
								AccessControl.ROLE_RESOURCE)) {
							addNode(father, sonrole.getRoleId(), sonrole
									.getRoleName(), nodeType, false, curLevel,
									(String) null, (String) null, (String) null,
									map);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
