package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.Listener;

import com.frameworkset.platform.sysmgrcore.control.PageConfig;
import com.frameworkset.platform.sysmgrcore.entity.ChargeOrg;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orggroup;
import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.entity.Orgrole;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.tag.pager.ListInfo;
  
public class VODOrgManger extends EventHandle implements OrgManager { 
  	    
	public boolean storeOrg(Organization org) throws ManagerException {
		boolean r = false;
		PreparedDBUtil preDBUtil = new PreparedDBUtil();
		String sqlstr = "INSERT INTO TB_Branch (FD_Branch_ID,FD_Branch_ParentID,FD_Branch_Name,FD_Branch_ShowName,FD_BRANCH_ISDELETED,FD_BRANCH_POSITION) " +
			"VALUES ("+Integer.parseInt(org.getOrgId())+","+ Integer.parseInt(org.getParentId()) +",'"+ org.getOrgName() +"','"+ org.getRemark5() +"','0','"+org.getOrgSn()+"')";
		
		String sqlstr1 = "update TB_Branch set FD_Branch_ParentID="+ Integer.parseInt(org.getParentId())+",FD_Branch_ShowName='"+org.getRemark5()+"'"+
			",FD_Branch_Name='"+ org.getOrgName() + "' where  FD_BRANCH_ID='"+ org.getOrgId() +"'";
//		S ystem.out.println(org.getOrgId());
//		System.out.println(org.getOrgName());
		
		String rtm="rtm";

			
								
				try {

				
					preDBUtil.executeInsert(rtm,sqlstr);	//新增记录
					
					r = true;
				} catch (SQLException e) {
					try {
						preDBUtil.executeUpdate(rtm,sqlstr1);
						r = true;
					} catch (SQLException e1) {
						
						//e1.printStackTrace();
					}	//修改记录
					//e.printStackTrace();
					
				}
			
			return r;
		
		
		
	}

	public boolean storeOrg(Organization org, String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeOrgjob(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean storeSubOrgjob(Orgjob orgjob) throws ManagerException {
		return false;
	}

	public boolean storeOrggroup(Orggroup orggroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public Organization getOrg(String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(Job job) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(String hql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteOrg(Organization org) throws ManagerException {//删除机构
		boolean r = false;
		if(org!=null){
			PreparedDBUtil preDBUtil = new PreparedDBUtil();
			String sqlstr = "DELETE FROM TB_Branch WHERE FD_BRANCH_ID " +
					"in(SELECT t.FD_BRANCH_ID FROM TB_BRANCH t START WITH " +
					"t.FD_BRANCH_ID ="+ org.getOrgId() +" CONNECT BY PRIOR" +
					" t.FD_BRANCH_ID =t.FD_BRANCH_PARENTID) ";
			
			String sqlstr1="update TB_Employee set FD_Employee_BranchID=1 where FD_Employee_BranchID in (SELECT t.FD_BRANCH_ID FROM TB_BRANCH t START WITH " +
					"t.FD_BRANCH_ID ="+ org.getOrgId() +" CONNECT BY PRIOR" +
					" t.FD_BRANCH_ID =t.FD_BRANCH_PARENTID)";
		
			String rtm="rtm";
			try {
				preDBUtil.executeUpdate(rtm,sqlstr1);
				preDBUtil.executeDelete(rtm,sqlstr);
				
				
				r = true;
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return r;
	}

	public boolean deleteOrg(String orgId) throws ManagerException {
		boolean r = false;
		
			PreparedDBUtil preDBUtil = new PreparedDBUtil();
			
			String sqlstr = "DELETE FROM TB_Branch WHERE FD_Branch_ID="+ orgId +"";
			
			String sqlstr1="update TB_Employee set FD_Employee_BranchID=1 where FD_Employee_BranchID="+Integer.parseInt(orgId)+"";
			String rtm="rtm";
			try {
				preDBUtil.executeDelete(rtm,sqlstr);
				preDBUtil.executeUpdate(rtm,sqlstr1);
				
				r = true;
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return r;
	}

	public boolean deleteOrgjob(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrgjob(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrggroup(Orggroup orggroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public Organization loadAssociatedSet(String orgId, String associated)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isContainChildOrg(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isContainUser(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public List getChildOrgList(Organization org, boolean isRecursion)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getChildOrgList(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public PageConfig getPageConfig() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isOrgExist(String orgName) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public List getOrgList(Role role) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean storeOrgrole(Orgrole orgrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrgrole(Orgrole orgrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public ListInfo getUserCanWriteAndReadOrgList(String userId,
			String userAccount, int offSet, int pageItemsize, String condition)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteOrgrole(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public void addListener(Listener listener) {
		// TODO Auto-generated method stub

	}

	public void change(Event source, boolean synchronizable) {
		// TODO Auto-generated method stub

	}

	public void change(Event source) {
		// TODO Auto-generated method stub

	}
	public ChargeOrg getSatrapList(User user) throws ManagerException{
		return null;
		
	}

	public ChargeOrg getSatrapListByUserAccount(String userAccount) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public ChargeOrg getSatrapListByUserID(String userID) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization getMainOrganizationOfUser(String userAccount) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getSecondOrganizationsOfUser(String userAccount) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public void addMainOrgnazitionOfUser(String userID, String orgID) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	public void deleteMainOrgnazitionOfUser(String userID) throws ManagerException {
		// TODO Auto-generated method stub
		
	}
	public ChargeOrg getSatrapUpByOrgID(String orgId) throws ManagerException{
		return null;
		
	}
	public ChargeOrg getSatrapByUser(String layer,String orgId) throws ManagerException{
		return null;
		
	}
	public List getHasPemissionOfUsers(String orgid,String opID,String restype) throws ManagerException{
		return null;
		
	}
	public List getOrgByUser(String orgId,String jobName) throws ManagerException{
		return null;
		
	}

	public boolean orgHasJob(String orgid, String jobid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}
	public String getSecondOrganizations(String userAccount)throws ManagerException{
		return userAccount;
		
	}

	public List getOrgListOfUser(String userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 根据当前机构id和页面传来的子机构ids进行机构的排序
	 * @param String orgId 父机构id
	 * @param String[] orggroup 子机构ids
	 */
	public boolean sortOrg(String orgId,String[] sonOrgIds) throws ManagerException
	{
		return false;
	}
	
	/**
	 * 删除机构下的岗位
	 * job_id = 1 的在岗的岗位 不能删除
	 * @param orgid
	 * @param jobid
	 * @return
	 * @throws ManagerException 
	 * OrgManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean deleteOrgjob(String orgids, String[] jobids) throws ManagerException {
		return true;
	}
	
	/**
	 * 保存机构岗位
	 * @param org_id
	 * @param job_ids
	 * @param job_sn
	 * @return
	 * @throws ManagerException 
	 * OrgManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean addOrgjob(String org_id,String[] job_ids,String job_sn) throws ManagerException{
		return true;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#addSubOrgjob(java.lang.String, java.lang.String[])
	 */
	public boolean addSubOrgjob(String orgid, String[] jobids) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#deleteOrgrole(java.lang.String, java.lang.String[], java.lang.String)
	 */
	public boolean deleteOrgrole(String orgId, String[] roleIds, String flag) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#storeOrgRole(java.lang.String, java.lang.String[], java.lang.String)
	 */
	public boolean storeOrgRole(String orgId, String[] roleIds, String flag) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#isBussinessDepartment(java.lang.String)
	 */
	public boolean isBussinessDepartment(String orgId) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#userBelongsCountyDepartment(java.lang.String)
	 */
	public Organization userBelongsCountyDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#userBelongsCityDepartment(java.lang.String)
	 */
	public Organization userBelongsCityDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean insertOrg(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#userBelongsBussinessDepartment(java.lang.String)
	 */
	public List userBelongsBussinessDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#getUserManageOrgs(java.lang.String)
	 */
	public List getUserManageOrgs(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization getOrgById(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization getOrgByName(String orgName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteSubOrgjob(String orgids, String[] jobids) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public List getSubOrgList(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getAllOrgList(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getFatherOrg(String orgId, boolean isFather) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#userBelongsProvinceDepartment(java.lang.String)
	 */
	public Organization userBelongsProvinceDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization orgBelongsBusinessDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization orgBelongsCountryDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization orgBelongsCityDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization orgBelongsProvinceDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization orgBelongsMiniDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgListOfRole(String roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgListBySql(String sql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean storeOrgRole(String[] orgIds, String[] roleIds) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrgrole(String[] orgIds, String[] roleIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrg_UserJob(String orgId, String[] userIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#getRecursionAllOrgList(java.lang.String)
	 */
	public List getRecursionAllOrgList(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserAllManagerOrg(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public Organization orgBelongsOfficeDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean userIsOrgManager(String userId, String orgId) throws ManagerException {
		// TODO 自动生成方法存根
		return false;
	}

	public Map getSubOrgId(String userId, boolean isRoleAdmin){
		// TODO Auto-generated method stub
		return null;
	}

	public Map getParentOrgId(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isCurOrgManager(String curOrgId, String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean tranOrg(String orgId, String tranToOrgId) {
		// TODO Auto-generated method stub
		return false;
	}

	public void loadOrganization(Map orgMap, Organization root) {
		// TODO Auto-generated method stub
		
	}



}
