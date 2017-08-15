package com.frameworkset.platform.cms.usermanager.tag;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * <p>用户权限管理－－角色列表</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author zhuo.wang
 * @version 1.0
 */
public class InnerRoleList extends DataInfoImpl implements java.io.Serializable {
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {
		 	
		 	ListInfo listInfo = new ListInfo();
	        String roleName = request.getParameter("roleName");
	        String roleids = this.getAllRolesByUserId();
	        
	        if(roleids == null || roleids.equals(""))
	        	return listInfo;
			DBUtil dbUtil = new DBUtil();
	
			try {
				StringBuffer hsql = new StringBuffer("select * from TD_SM_ROLE ");
				
				if(roleName == null)
				{
					roleName = "";
				}
				hsql.append("where ROLE_NAME like '%" + roleName + "%' ");
				
				if(!this.accessControl.isAdmin())
				{
					hsql.append(" and role_id in (" + roleids + ") ");
				}				
				
				dbUtil.executeSelect(hsql.toString(),(int)offset,maxPagesize);
			
				
				Role role = null;
				List roles = new ArrayList();
				for(int i = 0; i < dbUtil.size(); i ++)
				{
					role = new Role();
					role.setRoleId(dbUtil.getString(i,"ROLE_ID"));
					role.setRoleName(dbUtil.getString(i,"ROLE_NAME"));
					role.setRoleDesc(dbUtil.getString(i,"ROLE_DESC"));
					role.setRoleType(dbUtil.getString(i,"ROLE_TYPE")) ;
					roles.add(role);
					
				}
				listInfo.setDatas(roles);
				listInfo.setTotalSize(dbUtil.getTotalSize());
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			return listInfo;
		}

		protected ListInfo getDataList(String arg0, boolean arg1) {
			return null;
		}
		
		/**
		 * 获得用户所有有权限的角色的ids
		 * @param userId
		 * @return
		 */
		private String getAllRolesByUserId()
		{
			String roleids = "";
			DBUtil db = new DBUtil();
			String org_sql = "select role_id from td_sm_role";
			try
			{
				db.executeSelect(org_sql);
				if(db.size()>0)
				{
					for(int j=0;j<db.size();j++)
					{
						if(this.accessControl.checkPermission(db.getInt(j,"role_id") + "",
								AccessControl.WRITE_PERMISSION, AccessControl.ROLE_RESOURCE))
							roleids += db.getInt(j,"role_id") + ",";
					} 
				}
				if(roleids.endsWith(","))
					roleids = roleids.substring(0,roleids.length() -1);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return roleids;
		}

}

