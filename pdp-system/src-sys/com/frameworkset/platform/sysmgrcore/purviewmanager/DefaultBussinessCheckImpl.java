/**
 * 
 */
package com.frameworkset.platform.sysmgrcore.purviewmanager;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.ListInfo;

/**
 * <p>Title: DefaultBussinessCheckImpl.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2008-3-6 10:42:51
 * @author ge.tao
 * @version 1.0
 */
public class DefaultBussinessCheckImpl implements BussinessCheck ,Serializable{

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#userDeleteCheck(com.frameworkset.platform.security.AccessControl, java.lang.String)
	 */
	public List userDeleteCheck(AccessControl control, String userId) {
//		如果是超级管理员或者时部门管理员， 都不允许删除。
		OrgManagerImpl orgImpl = new OrgManagerImpl();
		List list = new ArrayList();
		//判断是否部门管理员
		List managerOrgs = orgImpl.getUserManageOrgs(userId);
	
		if(control.getUserID().equals(userId))
		{
			list.add("用户["+control.getUserAccount()+"]员不能删除自己");
		}
	
		if(AccessControl.isAdminByUserid(userId)){//是超级管理员
			list.add("超级管理员["+userId+"]不能删除"); 
			return list;
		}else if( managerOrgs != null && managerOrgs.size() > 0){//是部门管理员
			list.add("部门管理员不能直接删除，要删除必须先变为普通用户！") ;
			return list;
		}else{					
			
			return list;
		}
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#userMoveCheck(com.frameworkset.platform.security.AccessControl, java.lang.String)
	 */
	public List userMoveCheck(AccessControl control, String userId) {
//		如果是超级管理员或者时部门管理员， 都不允许删除。
		OrgManagerImpl orgImpl = new OrgManagerImpl();
		List list = new ArrayList();
		//判断是否部门管理员
		List managerOrgs = orgImpl.getUserManageOrgs(userId);
	
		//admin是否可以调动
		if(ConfigManager.getInstance().getConfigBooleanValue("administransferred",false)){
			if("1".equals(userId)){
				return list;
			}
		}
		
		if(control.getUserID().equals(userId))
		{
			list.add("用户["+control.getUserAccount()+"]员不能调动自己");
		}
	
		if(AccessControl.isAdminByUserid(userId)){//是超级管理员
			list.add("超级管理员["+userId+"]不能调动"); 
			return list;
		}else if( managerOrgs != null && managerOrgs.size() > 0){//是部门管理员
			list.add("部门管理员不能直接调动，要调动必须先变为普通用户！") ;
			return list;
		}else{					
			
			return list;
		}
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#orgDeleteCheck(com.frameworkset.platform.security.AccessControl, java.lang.String)
	 */
	public List orgDeleteCheck(AccessControl control, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#orgMoveCheck(com.frameworkset.platform.security.AccessControl, java.lang.String)
	 */
	public List orgMoveCheck(AccessControl control, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#roleReclaimCheck(java.lang.String)
	 */
	public String roleReclaimCheck(String roleId) {		
		String infos = "";
		if(ADMIN_ROLEID.equals(roleId)){
			//超级管理员角色 不允许 权限回收
			infos =  "超级管理员角色，不允许权限回收";			
		}else if(ORGMANAGER_ROLEID.equals(roleId)){
			//部门管理员角色 不允许 权限回收
			infos =  "部门管理员角色，不允许权限回收";
		}		
		return infos;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#userReclaimCheck(com.frameworkset.platform.security.AccessControl, java.lang.String)
	 */
	public List userReclaimCheck(AccessControl control, String userId) {
		OrgManagerImpl orgImpl = new OrgManagerImpl();
		List list = new ArrayList();
		//判断是否部门管理员
		List managerOrgs = orgImpl.getUserManageOrgs(userId);
		if(AccessControl.isAdminByUserid(userId)){//是超级管理员
			list.add("超级管理员不能权限回收"); 
			return list;
		}else if( managerOrgs != null && managerOrgs.size() > 0){//是部门管理员
			list.add("部门管理员不能直接权限回收，要权限回收必须先变为普通用户！") ;
			return list;
		}else{					
//			StringBuffer sql = new StringBuffer();
//			sql.append("select * from table(f_getTaskByUser(").append(userId).append("))");
//			DBUtil db = new DBUtil();
//			try {
//				db.executeSelect(sql.toString());
//				for(int i=0; i<db.size(); i++){
//					list.add(db.getString(i,0));					
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}		
//			sql.setLength(0);
			return list;
		}
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#orgReclaimCheck(com.frameworkset.platform.security.AccessControl, java.lang.String)
	 */
	public List orgReclaimCheck(AccessControl control, String orgId) {
		
		return orgDeleteCheck(control, orgId);
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.purviewmanager.BussinessCheck#roleDeleteCheck(java.lang.String)
	 */
	public String roleDeleteCheck(String roleId) {
		String infos = "";
		if(ADMIN_ROLEID.equals(roleId)){
			//超级管理员角色 不允许删除
			infos =  "超级管理员角色，不允许删除";			
		}else if(ORGMANAGER_ROLEID.equals(roleId)){
			//部门管理员角色 不允许删除
			infos =  "部门管理员角色，不允许删除";
		}		
		return infos;
	}
	
	/**
	 * 判断岗位是否被机构设置
	 */
	public boolean jobDeleteCheck(String jobId) {
		boolean state = false;
		StringBuffer sql = new StringBuffer()
			.append("select * from td_sm_orgjob a,td_sm_organization b where a.job_id='").append(jobId)
			.append("' and a.org_id = b.org_id");
		//删除该岗位所关联的资源授予记录
		String roleresop  = "delete from TD_SM_roleresop where restype_id='job' and res_id ='" + jobId + "'";
		String deljob = "delete from td_sm_job where job_id='" + jobId + "'";
		DBUtil db = new DBUtil();
		DBUtil db_del = new DBUtil();
		try {
			db.executeSelect(sql.toString());
			if(db.size() > 0){
				state = false;
			}else{
				db_del.addBatch(roleresop);
				db_del.addBatch(deljob);
				db_del.executeBatch();
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return state;
	}
	
	public ListInfo orgSetJobList(String jobId, String remark5, String orgnumber,
			long offset, int maxPagesize) {
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();
		StringBuffer sql = new StringBuffer()
			.append("select b.* from td_sm_orgjob a,td_sm_organization b where a.job_id='").append(jobId)
			.append("' and a.org_id = b.org_id ");
		if(!"".equals(remark5)){
			sql.append(" and b.remark5 like '%").append(remark5).append("%' ");
		}
		if(!"".equals(orgnumber)){
			sql.append(" and b.ORGNUMBER like '%").append(orgnumber).append("%' ");
		}
		try {
			db.executeSelect(sql.toString(), (int)offset, maxPagesize);
			Organization organization = null;
			for(int i = 0; i < db.size(); i++){
				organization = new Organization();
				organization.setOrgName(db.getString(i, "ORG_NAME"));
				organization.setOrgnumber(db.getString(i, "ORGNUMBER"));
				organization.setOrgSn(db.getString(i, "ORG_SN"));
				organization.setRemark5(db.getString(i, "REMARK5"));
				organization.setOrgdesc(db.getString(i, "ORGDESC"));
				list.add(organization);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

}
