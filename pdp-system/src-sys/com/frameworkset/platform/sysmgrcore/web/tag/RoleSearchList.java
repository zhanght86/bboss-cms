package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author feng.jing
 * @version 1.0
 */
public class RoleSearchList extends DataInfoImpl implements Serializable
{
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) 
	 {
	    	
	        ListInfo listInfo = new ListInfo();
	        String roleName = request.getParameter("roleName");
	        
	        String roleTypeName = request.getParameter("roleTypeName");
	        
	        String roleDesc = request.getParameter("roleDesc");
	        
	        
	        
			DBUtil dbUtil = new DBUtil();	
			DBUtil realdbUtil = new DBUtil();
			
			try
			{
				StringBuffer hsql = new StringBuffer("select role_id,owner_id from TD_SM_ROLE");
				
				if (roleName != null && roleName.length() > 0) 
				{
					hsql.append(" where ROLE_NAME like '%" + roleName + "%' ");
				}
				
				StringBuffer realhsql = new StringBuffer("select r.ROLE_ID, r.ROLE_NAME, r.ROLE_DESC, rt.type_name from TD_SM_ROLE r, td_sm_roletype rt where r.role_type=rt.type_id ");
				
				if (roleName != null && roleName.length() > 0) 
				{
					realhsql.append(" and r.ROLE_NAME like '%" + roleName + "%' ");
				}
				
				if(roleTypeName != null && roleTypeName.length() > 0)
				{
					realhsql.append(" and rt.type_name like '%" + roleTypeName + "%' ");
				}
				
				if(roleDesc != null && roleDesc.length() > 0)
				{
					realhsql.append(" and r.ROLE_DESC like '%" + roleDesc + "%' ");
				}
				
				dbUtil.executeSelect(hsql.toString());
				
				Role role = null;
				List roles = new ArrayList();
					
				for(int i = 0; i < dbUtil.size(); i ++)
				{
					role = new Role();
					
					role.setRoleId(dbUtil.getString(i,"ROLE_ID"));				
					String owner_id = dbUtil.getString(i,"OWNER_ID") + "";
					//角色权限判断
					if(super.accessControl.checkPermission(role.getRoleId(), 
	    					AccessControl.READ_PERMISSION, AccessControl.ROLE_RESOURCE)
	    					|| super.accessControl.checkPermission(role.getRoleId(), 
	    	    					AccessControl.WRITE_PERMISSION, AccessControl.ROLE_RESOURCE))
					{
					
//					if(super.accessControl.checkPermission(role.getRoleId(),"roleset",
//								AccessControl.ROLE_RESOURCE) 
//							|| super.accessControl.getUserID().equals(owner_id)){
						
						
						roles.add(role.getRoleId());
					}
						
//					}					
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
					realdbUtil.executeSelect(realhsql.toString(),(int)offset,maxPagesize);
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

		protected ListInfo getDataList(String arg0, boolean arg1) {
			return null;
		}

}
//    public RoleSearchList() {
//    }

//    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
//                                   int maxPagesize) {
//        ListInfo listInfo = new ListInfo();
//        String roleName = (String) request.getAttribute("roleName");
//       
//        try {
//            RoleManager roleManager = SecurityDatabase.getRoleManager();
//            List list = null;
//            PageConfig pageConfig = roleManager.getPageConfig();
//            pageConfig.setPageSize(maxPagesize);
//            pageConfig.setStartIndex((int) offset);
//            
//            if (roleName == null) {
//                list = roleManager.getRoleList("from Role");
//                listInfo.setTotalSize(pageConfig.getTotalSize());                                
//                listInfo.setDatas(list);
//            }else{
//            	 list = roleManager.getRoleList("roleName", "%"+roleName+"%", true);
//                 listInfo.setTotalSize(pageConfig.getTotalSize());
//                 listInfo.setDatas(list);
//
//            } 
//               
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return listInfo;
//
//    }
//
//    protected ListInfo getDataList(String string, boolean _boolean) {
//        return null;
//    }
//}
