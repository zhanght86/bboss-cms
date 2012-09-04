package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.RoleType;
import com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

/**
 * 
 * @author 景峰
 * @file RoleTree.java Created on: Apr 6, 2006
 */
public class ChargeRoleTypeTree extends COMTree implements Serializable

{
	List rolesealed = null;
	public void setPageContext(PageContext pageContext)
	{
		super.setPageContext(pageContext);
		rolesealed = rtm.getRoleSealedRole();
	}
	RoleTypeManager rtm = new RoleTypeManager();
	public boolean hasSon(ITreeNode father) 
	{
		try {
			//Role role = new Role();
			//role.setRoleId(roleID);
			if (father.isRoot()) {
				boolean hastype = rolesealed != null;
				return hastype ?hastype:rtm.hasType();
			}
			else if(father.getType().equals("roletype"))
			{
				if(father.getId().equals("roleSealedtype"))
					return true;
				String id[] = father.getId().split("\\:");
				return rtm.hasRoles(id[1]);
			}		
//			 return role.isContainChildRole(role);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	public boolean setSon(ITreeNode father, int curLevel)
	{
		//String roleTypeID = father.getId();
		//判断是否为根结点，如果为curLevel为1时，获取角色类型列表
		
		if(father.isRoot())
		{				
			try
			{				
				List roleTypeList = rtm.getTypeNameList();
				if (roleTypeList.size()>0) 
				{
					for(int i = 0;  i < roleTypeList.size(); i ++)
					{
						RoleType rt = (RoleType) roleTypeList.get(i);
						String roleTypeId = rt.getRoleTypeID();
						String roleTypeName = rt.getTypeName();
						Map map = new HashMap();
						map.put("roleTypeId", roleTypeId);
						map.put("roleTypeName", roleTypeName);	
						map.put("roleTYPEDesc",rt.getTypeDesc());
						addNode(father, "type:" + roleTypeId, roleTypeName, "roletype", false, curLevel,(String) null, (String) null, (String) null,map);					
					}	
				}
				//挂未知类型角色
				if(rolesealed != null){
					addNode(father, "roleSealedtype", "未知类型", "roletype", false, curLevel,(String) null, 
							(String) null, (String) null, (String) null);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(father.getType().equals("roletype") && father.getId().equals("roleSealedtype")){
			for(int i = 0; i < rolesealed.size(); i++){
				Role sonrole = (Role) rolesealed.get(i);					
				Map map1 = new HashMap();
				map1.put("roleId", sonrole.getRoleId());
				map1.put("resId", sonrole.getRoleId());
				map1.put("resName", sonrole.getRoleName());
				if (super.accessControl.checkPermission(sonrole.getRoleId(),
						AccessControl.WRITE_PERMISSION,
						AccessControl.ROLE_RESOURCE)) 
				{
					String rolename = sonrole.getRoleName();
					addNode(father, sonrole.getRoleId(), rolename, "org", true, curLevel,
							(String) null, sonrole.getRoleName(), (String) null,
							(String) null);
				}
				else if (super.accessControl.checkPermission(sonrole.getRoleId(),
						AccessControl.READ_PERMISSION,
						AccessControl.ROLE_RESOURCE))
				{
					String rolename = sonrole.getRoleName();
					addNode(father, sonrole.getRoleId(), rolename, "org", false, curLevel,
							(String) null, sonrole.getRoleName(), (String) null,
							(String) null);
				}
			}
		}
		else
		{
			try
			{			
				String id[] = father.getId().split("\\:");
				List roleList = rtm.getRoleList(id[1]);
				if(roleList!=null)
				{
					for(int i = 0;  i < roleList.size(); i ++)
					{
						Role sonrole = (Role) roleList.get(i);					
						Map map1 = new HashMap();
						map1.put("roleId", sonrole.getRoleId());
						map1.put("resId", sonrole.getRoleId());
						map1.put("resName", sonrole.getRoleName());
						if (super.accessControl.checkPermission(sonrole.getRoleId(),
								AccessControl.WRITE_PERMISSION,
								AccessControl.ROLE_RESOURCE)) 
						{
							String rolename = sonrole.getRoleName();
							if(AccessControl.isAdministratorRole(rolename)){
								rolename += "（系统管理员）";
							}
							addNode(father, sonrole.getRoleId(), rolename, "org", true, curLevel,
									(String) null, sonrole.getRoleId()+":"+sonrole.getRoleName(), (String) null,
									(String) null);
						}
						else if (super.accessControl.checkPermission(sonrole.getRoleId(),
								AccessControl.READ_PERMISSION,
								AccessControl.ROLE_RESOURCE))
						{
							String rolename = sonrole.getRoleName();
							if(AccessControl.isAdministratorRole(rolename)){
								rolename += "（系统管理员）";
							}
							addNode(father, sonrole.getRoleId(), rolename, "org", false, curLevel,
									(String) null, sonrole.getRoleId()+":"+sonrole.getRoleName(), (String) null,
									(String) null);
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		
		return false;
	}			
}