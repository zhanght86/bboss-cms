package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class RoleList extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offSet, int pageItemsize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();	
		DBUtil realdbUtil = new DBUtil();
		try {
			StringBuffer hsql = new StringBuffer("select role_id,OWNER_ID from TD_SM_ROLE");
			StringBuffer realhsql = new StringBuffer("select r.ROLE_ID, r.ROLE_NAME, r.ROLE_DESC, rt.type_name from TD_SM_ROLE r, td_sm_roletype rt where r.role_type=rt.type_id ");
			dbUtil.executeSelect(hsql.toString());
			Role role = null;
			List roles = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				role = new Role();
				role.setRoleId(dbUtil.getString(i,"ROLE_ID"));
				//必须要有角色的资源操作授予权限
				if(super.accessControl.isAdmin() || String.valueOf(dbUtil.getInt(i, "OWNER_ID")).equals(accessControl.getUserID()))
				{
					roles.add(role.getRoleId());
				}					
			}
			Role realrole = null;
			List realroles = new ArrayList();
			if(roles!=null && roles.size() > 0)
			{
				realhsql = realhsql.append(" and ( r.role_id='" + roles.get(0).toString() + "' ");
				if(roles.size()>1)
				{
					for(int i=1;i<roles.size();i++)
					{
						realhsql = realhsql.append(" or r.role_id='" + roles.get(i).toString() + "' ");
					}
				}					
				realhsql.append(" ) ");
				realdbUtil.executeSelect(realhsql.toString(),(int)offSet,pageItemsize);
				for(int j = 0; j < realdbUtil.size(); j ++)
				{
					realrole = new Role();
					realrole.setRoleId(realdbUtil.getString(j,"ROLE_ID"));
					realrole.setRoleName(realdbUtil.getString(j,"ROLE_NAME"));
					realrole.setRoleDesc(realdbUtil.getString(j,"ROLE_DESC"));	
					realrole.setRoleType(realdbUtil.getString(j,"type_name"));	
					realroles.add(realrole);					
				}
			}
			listInfo.setDatas(realroles);
			listInfo.setTotalSize(realdbUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String sortKey, boolean desc) {
		// TODO Auto-generated method stub
		return null;
	}
}
