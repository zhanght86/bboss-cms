package com.frameworkset.platform.sysmgrcore.manager.ldap;

import java.util.List;
import java.util.Map;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.pager.ListInfo;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.control.LdapControl;
import com.frameworkset.platform.sysmgrcore.control.Parameter;
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
import com.frameworkset.platform.sysmgrcore.manager.AbsttractOrgManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
  
public class OrgManagerImpl extends AbsttractOrgManager implements OrgManager {
      
	private Logger logger = LoggerFactory.getLogger(OrgManagerImpl.class);
 
	

	private String groupBase = ConfigManager.getInstance().getConfigValue(
			"groupBase");
 
	public boolean isOrgExist(Organization org) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			String cn = org.getOrgName();

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty("filter",
					"(&(objectclass=groupOfNames)|(cn=" + cn + "))");

			List list = (List) dc.execute(p);

			if (list != null && !list.isEmpty())
				r = true;
		} catch (Exception e) {
			logger.error("",e);
		}

		return r;
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

	public boolean deleteOrg(Organization org) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_DELETE);

			String rdn = org.getOrgName();
			p.getProperties().setProperty("dn", "cn=" + rdn + "," + groupBase);

			dc.execute(p);
			r = true;

			// 删除当前组父组的成员属性中指向当前组的属性
			p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty(
					"filter",
					"(&(objectclass=groupOfNames)|(member=cn="
							+ org.getOrgName() + "," + groupBase + "))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					SearchResult sr = (SearchResult) list.get(i);
					Attributes attrs = sr.getAttributes();
					String groupName = attrs.get("cn").get().toString();

					Attribute member = getItemAttribute(groupBase,
							"(&(objectclass=groupOfNames)|(cn=" + groupName
									+ "))", "member");

					// 删除指向当前用户的属性
					member.remove("cn=" + org.getOrgName() + "," + groupBase);

					attrs = new BasicAttributes();
					attrs.put(member);

					p = new Parameter();
					p.setCommand(Parameter.COMMAND_STORE);
					p.setObject(attrs);

					p.getProperties().setProperty("dn",
							"cn=" + groupName + "," + groupBase);

					p.getProperties().setProperty("id", groupName);
					p.getProperties().setProperty("modifyoption",
							String.valueOf(DirContext.REPLACE_ATTRIBUTE));

					dc.execute(p);
				}
			}
		} catch (Exception e) {
			logger.error("",e);
		}

		return r;
	}

	public boolean deleteOrg(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrggroup(Orggroup orggroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrgjob(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteOrgjob(Orgjob orgjob) throws ManagerException {
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

	public Organization getOrg(String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(Job job) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(String hql) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getOrgList(User user) throws ManagerException {
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

	public boolean isOrgExist(String orgName) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public Organization loadAssociatedSet(String orgId, String associated)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean storeOrg(Organization org, String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeOrg(Organization org,boolean triggerevent) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Attribute itemClass = new BasicAttribute("objectclass");
			itemClass.add("groupOfNames");
			itemClass.add("ibm-appuuidaux");

			Attribute cn = new BasicAttribute("cn", org.getOrgName());
			Attribute member = new BasicAttribute("member");
			
			
			
			member.add("uid=root");

			Attributes attrs = new BasicAttributes();
			attrs.put(itemClass);
			attrs.put(cn);
			

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_STORE);
			p.setObject(attrs);

			p.getProperties().setProperty("dn",
					"cn=" + org.getOrgName() + "," + groupBase);

			String id = "";
			if (isOrgExist(org))
			{
//				id = org.getOrgName();
				Attribute member_ = getItemAttribute(groupBase,
						"(&(objectclass=groupOfNames)|(cn="
						+ org.getOrgName() + "))","member");
				
				//member_.add("uid=root");
				member = member_;
			}
			attrs.put(member);	
			p.getProperties().setProperty("id", id);
			p.getProperties().setProperty("modifyoption",
					String.valueOf(DirContext.REPLACE_ATTRIBUTE));

			dc.execute(p);

			// 如果当前组是其它组的子组则在父组的成员属性中增加一个指向它的属性
			if (org.getParentOrg() != null) {
				// 查找 ParentID 所指向的父组条目
				if (isOrgExist(org.getParentOrg())) {
					member = getItemAttribute(groupBase,
							"(&(objectclass=groupOfNames)|(cn="
									+ org.getParentOrg().getOrgName() + "))",
							"member");

					if (member == null)
						member = new BasicAttribute("member");
					else {
						for (int i = 0; i < member.size(); i++) {
							String s = (String) member.get(i);
							if (s.equals("cn=" + org.getOrgName()))
								return true;
						}
					}

					member.add("cn=" + org.getOrgName() + "," + groupBase);

					attrs = new BasicAttributes();
					attrs.put(member);

					p.setCommand(Parameter.COMMAND_STORE);
					p.setObject(attrs);

					p.getProperties().setProperty(
							"dn",
							"cn=" + org.getParentOrg().getOrgName() + ","
									+ groupBase);

					p.getProperties().setProperty("id",
							org.getParentOrg().getOrgName());
					p.getProperties().setProperty("modifyoption",
							String.valueOf(DirContext.REPLACE_ATTRIBUTE));

					dc.execute(p);
				}
			}

			r = true;
		} catch (Exception e) {
			logger.error("",e);
		}

		return r;
	}

	public boolean storeOrggroup(Orggroup orggroup) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeOrgjob(Orgjob orgjob) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean storeSubOrgjob(Orgjob orgjob) throws ManagerException{
		return false;
	}

	private Attribute getItemAttribute(String parentdn, String filter,
			String propName) {
		Attribute attr = null;
		try {
			LdapControl dc = new LdapControl();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", parentdn);
			p.getProperties().setProperty("filter", filter);

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				SearchResult sr = (SearchResult) list.get(0);

				Attributes attrs = sr.getAttributes();
				attr = attrs.get(propName);
			}
		} catch (Exception e) {
			logger.error("",e);
		}

		return attr;
	}

	/* （非 Javadoc）
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#getOrgList(com.frameworkset.platform.sysmgrcore.entity.Role)
	 */
	public List getOrgList(Role role) throws ManagerException {
		// TODO 自动生成方法存根
		return null;
	}

	/* （非 Javadoc）
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#storeOrgrole(com.frameworkset.platform.sysmgrcore.entity.Orgrole)
	 */
	public boolean storeOrgrole(Orgrole orgrole) throws ManagerException {
		// TODO 自动生成方法存根
		return false;
	}

	/* （非 Javadoc）
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#deleteOrgrole(com.frameworkset.platform.sysmgrcore.entity.Orgrole)
	 */
	public boolean deleteOrgrole(Orgrole orgrole) throws ManagerException {
		// TODO 自动生成方法存根
		return false;
	}

	/* （非 Javadoc）
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#deleteOrgrole(com.frameworkset.platform.sysmgrcore.entity.Organization)
	 */
	public boolean deleteOrgrole(Organization org) throws ManagerException {
		// TODO 自动生成方法存根
		return false;
	}
	/* （非 Javadoc）
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#deleteOrgrole(com.frameworkset.platform.sysmgrcore.entity.Orgrole)
	 */
	public ListInfo getUserCanWriteAndReadOrgList(String userId,String userAccount,int offSet, int pageItemsize,String condition) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
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

	public ChargeOrg getSatrapUpByOrgID(String orgId) throws ManagerException {
		// TODO Auto-generated method stub
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

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#addSubOrgjob(java.lang.String, java.lang.String[])
	 */
	public boolean addSubOrgjob(String orgid, String[] jobids) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#deleteOrgjob(java.lang.String, java.lang.String[])
	 */
	public boolean deleteOrgjob(String orgids, String[] jobids) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.OrgManager#addOrgjob(java.lang.String, java.lang.String[], java.lang.String)
	 */
	public boolean addOrgjob(String org_id, String[] job_ids, String job_sn) throws ManagerException {
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

	public Map getSubOrgId(String userId, boolean isRoleAdmin) {
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

	@Override
	public void addMainOrgnazitionOfUser(String userID, String orgID,
			boolean broadcastevent) throws ManagerException {
		// TODO Auto-generated method stub
		
	}


}
