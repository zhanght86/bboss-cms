package com.frameworkset.platform.sysmgrcore.purviewmanager.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.sysmgrcore.entity.RoleResCopy;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class RoleCopy  extends DataInfoImpl implements Serializable{

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		String roleId = request.getParameter("roleId");
		if(roleId == null)
			roleId = (String) request.getAttribute("roleId");
		request.setAttribute("roleId",roleId);

		try {
            String hsql = "select role_name,role_id,t.type_name from td_sm_role,td_sm_roletype t where " +
            		"role_id <> '"+ roleId +"' " +
            		"and role_id <> '1' and role_id <> '2' and role_id <> '3' and role_id <> '4' " +
            		"and role_id in " +
							" ( select r.role_id from td_sm_role r" +
							" where r.owner_id='" + super.accessControl.getUserID() + "' ) " +
						" and  role_type = t.type_id " +
					" order by role_Name";
            if(super.accessControl.isAdmin())
            {
            	hsql = "select role_name,role_id,t.type_name from td_sm_role,td_sm_roletype t where " +
	        		"role_id <> '"+ roleId +"' " +
	        		"and role_id <> '1' and role_id <> '2' and role_id <> '3' and role_id <> '4' " +
            		"and role_type = t.type_id order by role_Name";
            }
			dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
			RoleResCopy ur = null;
			List users = new ArrayList();
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				ur = new RoleResCopy();
			    ur.setRoleName(dbUtil.getString(i,"role_name"));
			    ur.setRoleId(dbUtil.getString(i,"role_id"));
			    ur.setRoleTypeName(dbUtil.getString(i,"type_name"));
				users.add(ur);
			}
			listInfo.setDatas(users);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}

}

