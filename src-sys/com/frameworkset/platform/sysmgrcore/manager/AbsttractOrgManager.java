package com.frameworkset.platform.sysmgrcore.manager;

import java.util.List;
import java.util.Map;

import org.frameworkset.event.EventHandle;

import com.frameworkset.common.tag.pager.ListInfo;
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

public class AbsttractOrgManager extends EventHandle implements OrgManager {

	@Override
	public boolean storeOrg(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertOrg(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeOrg(Organization org, String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeOrgjob(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeSubOrgjob(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addSubOrgjob(String orgid, String[] jobids)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeOrggroup(Orggroup orggroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sortOrg(String orgId, String[] sonOrgIds)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getOrgList(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgList(Job job) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgList(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgList(String hql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteOrg(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrgjob(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrgjob(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrggroup(Orggroup orggroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Organization loadAssociatedSet(String orgId, String associated)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isContainChildOrg(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isContainUser(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getChildOrgList(Organization org, boolean isRecursion)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getRecursionAllOrgList(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getChildOrgList(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isOrgExist(String orgName) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getOrgList(Role role) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean storeOrgrole(Orgrole orgrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrgrole(Orgrole orgrole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ListInfo getUserCanWriteAndReadOrgList(String userId,
			String userAccount, int offSet, int pageItemsize, String condition)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteOrgrole(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ChargeOrg getSatrapListByUserAccount(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChargeOrg getSatrapListByUserID(String userID)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization getMainOrganizationOfUser(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getSecondOrganizationsOfUser(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addMainOrgnazitionOfUser(String userID, String orgID)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addMainOrgnazitionOfUser(String userID, String orgID,
			boolean broadcastevent) throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteMainOrgnazitionOfUser(String userID)
			throws ManagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public ChargeOrg getSatrapUpByOrgID(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChargeOrg getSatrapByUser(String layer, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getHasPemissionOfUsers(String orgid, String opID, String restype)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgByUser(String orgId, String jobName)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean orgHasJob(String orgid, String jobid)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSecondOrganizations(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgListOfUser(String userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteOrgjob(String orgids, String[] jobids)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteSubOrgjob(String orgids, String[] jobids)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getSubOrgList(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addOrgjob(String org_id, String[] job_ids, String job_sn)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrgrole(String orgId, String[] roleIds, String flag)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean storeOrgRole(String orgId, String[] roleIds, String flag) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isBussinessDepartment(String orgId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Organization userBelongsCountyDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization userBelongsCityDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization userBelongsProvinceDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List userBelongsBussinessDepartment(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserManageOrgs(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization getOrgById(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization getOrgByName(String orgName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization getOrg(String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getAllOrgList(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getFatherOrg(String orgId, boolean isFather)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization orgBelongsOfficeDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization orgBelongsMiniDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization orgBelongsCountryDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization orgBelongsCityDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Organization orgBelongsProvinceDepartment(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgListOfRole(String roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getOrgListBySql(String sql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean storeOrgRole(String[] orgIds, String[] roleIds) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrgrole(String[] orgIds, String[] roleIds)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrg_UserJob(String orgId, String[] userIds)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteAllOrg_UserJob(String[] userIds)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List getUserAllManagerOrg(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean userIsOrgManager(String userId, String orgId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map getSubOrgId(String userId, boolean isRoleAdmin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getParentOrgId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCurOrgManager(String curOrgId, String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tranOrg(String orgId, String tranToOrgId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrg(Organization org, boolean sendEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteOrg(String orgId, boolean sendEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

}
