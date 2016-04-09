package com.frameworkset.platform.sysmgrcore.purviewmanager.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Role;
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
public class RoleSearchList extends DataInfoImpl 
{
	private static ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/platform/sysmgrcore/purviewmanager/prviewmanager-role.xml");  
//	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
//	            int maxPagesize) 
//	 {
//	    	
//	        ListInfo listInfo = new ListInfo();
//	        String roleName = request.getParameter("roleName");
//	        
//	        String roleTypeName = request.getParameter("roleTypeName");
//	        
//	        String roleDesc = request.getParameter("roleDesc");
//	        String createrName=request.getParameter("creatorName");
//	        
//	        
//			DBUtil dbUtil = new DBUtil();	
//			DBUtil realdbUtil = new DBUtil();
//			
//			try
//			{
////				StringBuffer sql = new StringBuffer()
////					.append("SELECT r.role_name, roletype.type_name, u.user_name, u.user_realname,")
////					.append("r.role_id, r.role_desc, r.role_id, r.owner_id ")
////					.append("FROM TD_SM_ROLE r, TD_SM_ROLETYPE roletype, TD_SM_USER u ")
////					.append("WHERE r.role_type = roletype.type_id AND r.owner_id = u.user_id ");
////				
////				if (roleName != null && roleName.length() > 0) {
////					sql.append(" and r.role_name like '%").append(roleName).append("%' ");
////				}
////				
////				if(roleTypeName != null && roleTypeName.length() > 0){
////					sql.append(" and roletype.type_id = '" + roleTypeName + "' ");
////				}
////				
////				if(roleDesc != null && roleDesc.length() > 0){
////					sql.append(" and r.ROLE_DESC like '%" + roleDesc + "%' ");
////				}
////				
////				if(createrName != null && createrName.length() > 0){
////					sql.append(" and u.USER_NAME like '%" + createrName + "%' ");	
////				}
////				
////				
////				sql.append("ORDER BY roletype.TYPE_ID,r.ROLE_NAME");
//				
//				
//				
//				StringBuffer hsql = new StringBuffer("select role_id,owner_id from TD_SM_ROLE");
//				
//				if (roleName != null && roleName.length() > 0) 
//				{
//					hsql.append(" where ROLE_NAME like '%" + roleName + "%' ");
//				}
//				
//				StringBuffer realhsql = new StringBuffer("select r.ROLE_ID, r.ROLE_NAME, r.ROLE_DESC, rt.type_name, r.owner_id from TD_SM_ROLE r, td_sm_roletype rt,TD_SM_USER u where u.user_id = r.owner_id  and r.role_type=rt.type_id ");
//				
//				if (roleName != null && roleName.length() > 0) 
//				{
//					realhsql.append(" and r.ROLE_NAME like '%" + roleName + "%' ");
//				}
//				
//				if(roleTypeName != null && roleTypeName.length() > 0)
//				{
//					realhsql.append(" and rt.type_id = '" + roleTypeName + "' ");
//				}
//				
//				if(roleDesc != null && roleDesc.length() > 0)
//				{
//					realhsql.append(" and r.ROLE_DESC like '%" + roleDesc + "%' ");
//				}
//				if(createrName != null && createrName.length() > 0){
//					
//					realhsql.append(" and u.USER_NAME like '%" + createrName + "%' ");	
//				}
//				
//				dbUtil.executeSelect(hsql.toString());
//				
//				Role role = null;
//				List roles = new ArrayList();
//					
//				for(int i = 0; i < dbUtil.size(); i ++)
//				{
//					role = new Role();
//					
//					role.setRoleId(dbUtil.getString(i,"ROLE_ID"));				
//					String owner_id = dbUtil.getString(i,"OWNER_ID") + "";
//					//角色权限判断
////					if(super.accessControl.checkPermission(role.getRoleId(), 
////	    					AccessControl.READ_PERMISSION, AccessControl.ROLE_RESOURCE)
////	    					|| super.accessControl.checkPermission(role.getRoleId(), 
////	    	    					AccessControl.WRITE_PERMISSION, AccessControl.ROLE_RESOURCE))
////					{
//					
//					if(super.accessControl.checkPermission(role.getRoleId(),"roleset",
//								AccessControl.ROLE_RESOURCE) 
//							|| super.accessControl.getUserID().equals(owner_id)){
//						
//						
//						roles.add(role.getRoleId());
//					}
//						
////					}					
//				}
//				Role realrole = null;
//				List realroles = new ArrayList();
//				if(roles!=null && roles.size() > 0)
//				{
//					realhsql = realhsql.append(" and ( r.role_id='" + roles.get(0).toString() + "' ");
//					if(roles.size()>1)
//					{
//						for(int i=1;i<roles.size();i++)
//						{
//							realhsql = realhsql.append(" or r.role_id='" + roles.get(i).toString() + "' ");
//						}
//					}					
//					realhsql.append(" ) ");
//					realhsql.append(" order by rt.type_id,r.role_name");
//					realdbUtil.executeSelect(realhsql.toString(),(int)offset,maxPagesize);
//					for(int j = 0; j < realdbUtil.size(); j ++)
//					{
//						realrole = new Role();
//						realrole.setRoleId(realdbUtil.getString(j,"ROLE_ID"));
//						realrole.setRoleName(realdbUtil.getString(j,"ROLE_NAME"));
//						realrole.setRoleDesc(realdbUtil.getString(j,"ROLE_DESC"));	
//						realrole.setRoleType(realdbUtil.getString(j,"type_name"));	
//						realrole.setOwner_id(realdbUtil.getInt(j,"OWNER_ID"));
//						realroles.add(realrole);					
//					}
//				}
//				listInfo.setDatas(realroles);
//				listInfo.setTotalSize(realdbUtil.getTotalSize());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return listInfo;
//		}
	
	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
            int maxPagesize) 
 {
    	
        ListInfo listInfo = null;
        String roleName = request.getParameter("roleName");
        
        String roleTypeName = request.getParameter("roleTypeName");
        
        String roleDesc = request.getParameter("roleDesc");
        String createrName=request.getParameter("creatorName");
        
        
		
		
		try
		{			
			Map allrolesparams = new HashMap();			
			StringBuilder hsql = new StringBuilder("select role_id,owner_id from TD_SM_ROLE");
			
			if (roleName != null && roleName.length() > 0) 
			{
				allrolesparams.put("roleName","%" + roleName + "%");
			}
			else
				allrolesparams.put("roleName",roleName);
			List<HashMap> aroles = executor.queryListBean(HashMap.class, "queryAllRoles", allrolesparams);
			
			if(aroles != null && aroles.size() > 0)
			{
				Role role = null;
				List roles = new ArrayList();
				for(int i = 0; i < aroles.size(); i ++)
				{
					role = new Role();
					HashMap aa = aroles.get(i);
					role.setRoleId((String)aa.get("ROLE_ID"));				
					String owner_id = ""+aa.get("OWNER_ID");
					//角色权限判断
					
					if(super.accessControl.checkPermission(role.getRoleId(),"roleset",
								AccessControl.ROLE_RESOURCE) 
							|| super.accessControl.getUserID().equals(owner_id)){
						
						
						roles.add(role.getRoleId());
					}
						
				
				}
				if(roles.size() > 0)
				{
					allrolesparams.put("roles", roles);
					if(roleTypeName != null && roleTypeName.length() > 0)
					{
						allrolesparams.put("roleTypeName",Integer.parseInt(roleTypeName));
		//				realhsql.append(" and rt.type_id = '" + roleTypeName + "' ");
					}
					else
						allrolesparams.put("roleTypeName",-1);
					
					if(roleDesc != null && roleDesc.length() > 0)
					{
						allrolesparams.put("roleDesc","%" + roleDesc + "%");
					}
					else
						allrolesparams.put("roleDesc",roleDesc);
					if(createrName != null && createrName.length() > 0){
						
						allrolesparams.put("createrName","%" + createrName + "%");
					}
					else
						allrolesparams.put("createrName",createrName);
					listInfo = executor.queryListInfoBeanByRowHandler(new RowHandler<Role>(){
	
						@Override
						public void handleRow(Role realrole, Record record)
								throws Exception {
							realrole.setRoleId(record.getString("ROLE_ID"));
							realrole.setRoleName(record.getString("ROLE_NAME"));
							realrole.setRoleDesc(record.getString("ROLE_DESC"));	
							realrole.setRoleType(record.getString("type_name"));	
							realrole.setOwner_id(record.getInt("OWNER_ID"));
							
						}},Role.class, "queryAllPermissionRoles", offset, maxPagesize, allrolesparams);
				}
			}
			
			
			
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
