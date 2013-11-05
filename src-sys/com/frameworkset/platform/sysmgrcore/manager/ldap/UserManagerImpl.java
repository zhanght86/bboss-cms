package com.frameworkset.platform.sysmgrcore.manager.ldap;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.Listener;
import org.frameworkset.spi.SPIException;
import org.frameworkset.util.MoreListInfo;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.control.LdapControl;
import com.frameworkset.platform.sysmgrcore.control.Parameter;
import com.frameworkset.platform.sysmgrcore.entity.Accredit;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Operation;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Orgjob;
import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.Tempaccredit;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userattr;
import com.frameworkset.platform.sysmgrcore.entity.Usergroup;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.entity.Userresop;
import com.frameworkset.platform.sysmgrcore.entity.Userrole;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
import com.frameworkset.platform.sysmgrcore.manager.OrgManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;
import com.frameworkset.util.ListInfo;
  
/**
 * 项目：SysMgrCore <br>
 * 描述：用户管理(LDAP实现类) <br>
 * 版本：1.0 <br>
 *  
 * @author    
 */    
public class UserManagerImpl extends EventHandle implements UserManager {
     
	private Logger logger = Logger.getLogger(UserManagerImpl.class.getName());

	

	private String userBase = ConfigManager.getInstance().getConfigValue(
			"userBase");

	private String groupBase = ConfigManager.getInstance().getConfigValue(
			"groupBase");

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
			logger.error(e);
		}

		return attr;
	}

	private boolean isGroupExist(Group group) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			String cn = group.getGroupName();

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty("filter",
					"(&(objectclass=groupOfNames)|(cn=" + cn + "))");

			List list = (List) dc.execute(p);

			if (list != null && !list.isEmpty())
				r = true;
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

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
			logger.error(e);
		}

		return r;
	}

	private boolean isAttributeExist(String uid, String attributeName) {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", userBase);
			p.getProperties().setProperty("filter",
					"(&(objectclass=inetorgperson)|(uid=" + uid + "))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				SearchResult sr = (SearchResult) list.get(0);
				// 基本属性
				Attributes attrs = sr.getAttributes();
				if (attrs.get(attributeName) != null)
					r = true;
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean deleteAccredit(Accredit accredit) throws ManagerException {
		
		return false;
	}

	public boolean deleteTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		
		return false;
	}

	public boolean deleteUser(User user) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			// 删除指定的用户
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_DELETE);

			String rdn = user.getUserName();
			p.getProperties().setProperty("dn", "uid=" + rdn + "," + userBase);

			dc.execute(p);

			// 删除与该用户有关联的组中的相关成员属性
			deleteUsergroup(user);

			r = true;
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean deleteUser(Group group) throws ManagerException {
		
		return false;
	}

	public boolean deleteBatchUser(String userIds[]) throws ManagerException {
		return false;
	}

	public boolean deleteUserattr(Userattr userattr) throws ManagerException {
		
		return false;
	}

	public boolean deleteUsergroup(Usergroup usergroup) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Group group = usergroup.getGroup();
			User user = usergroup.getUser();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty(
					"filter",
					"(&(objectclass=groupOfNames)(member=uid="
							+ user.getUserName() + "," + userBase + "))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					SearchResult sr = (SearchResult) list.get(i);
					Attributes attrs = sr.getAttributes();
					String groupName = attrs.get("cn").get().toString();
					if(groupName.equals(group.getGroupName())){
						Attribute member = getItemAttribute(groupBase,
								"(&(objectclass=groupOfNames)|(cn=" + groupName
										+ "))", "member");

						// 删除指向当前用户的属性
						member.remove("uid=" + user.getUserName() + "," + userBase);

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
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean deleteUsergroup(User user) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			DBUtil db = new DBUtil();
//			String sql ="select group_id from TD_SM_USERGROUP where " +
//			"  user_id ="+ user.getUserId() +"";
			String sql ="select group_name from TD_SM_GROUP where " +
			"  group_id in (select group_id from TD_SM_USERGROUP where " +
			"  user_id ="+ user.getUserId() +" )";

			db.executeSelect(sql);
			
				
				Parameter p = new Parameter();
				p.setCommand(Parameter.COMMAND_GET);

				p.getProperties().setProperty("parentdn", groupBase);
				p.getProperties().setProperty(
						"filter",
						"(&(objectclass=groupOfNames)|(member=uid="
								+ user.getUserName() + "," + userBase + "))");

				List list = (List) dc.execute(p);
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SearchResult sr = (SearchResult) list.get(i);
						Attributes attrs = sr.getAttributes();
						String groupName = attrs.get("cn").get().toString();
						System.out.println("///////////////"+groupName);
						for(int j=0; j<db.size() ;j++){
							
						//GroupManager groupManager = SecurityDatabase.getGroupManager();
						//Group group = groupManager.getGroup("groupId",String.valueOf(db.getInt(j, "group_id")));
						
						if(groupName.equals(db.getString(j,"group_name"))){
						Attribute member = getItemAttribute(groupBase,
								"(&(objectclass=groupOfNames)|(cn=" + groupName
										+ "))", "member");

						// 删除指向当前用户的属性
						member.remove("uid=" + user.getUserName() + "," + userBase);

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
				}
			}
			
			
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean deleteUsergroup(Group group) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			// 检查组条目是否存在
			if (isGroupExist(group)) {
				Attribute member = getItemAttribute(groupBase,
						"(&(objectclass=groupOfNames)|(cn="
								+ group.getGroupName() + "))", "member");

				if (member != null) {
					Attribute newMember = new BasicAttribute("member");
					newMember.add("uid=root");

					// 删除 newMember 中的 uid= 属性
					for (int i = 0; i < member.size(); i++) {
						String s = member.get(i).toString();
						if (!s.startsWith("uid"))
							newMember.add(s);
					}

					// 替换当前组条目中的 member 属性
					Attributes attrs = new BasicAttributes();
					attrs.put(newMember);

					Parameter p = new Parameter();
					p.setCommand(Parameter.COMMAND_STORE);
					p.setObject(attrs);

					p.getProperties().setProperty("dn",
							"cn=" + group.getGroupName() + "," + groupBase);

					p.getProperties().setProperty("id", group.getGroupName());
					p.getProperties().setProperty("modifyoption",
							String.valueOf(DirContext.REPLACE_ATTRIBUTE));

					dc.execute(p);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}

		return r;
	}

	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			String orgId = userjoborg.getId().getOrgId();
			String uId = userjoborg.getId().getUserId().toString();
			String jobId = userjoborg.getId().getJobId();
			
			DBUtil db = new DBUtil();
			String sql ="select * from TD_SM_USERJOBORG where " +
			" org_id ='"+ orgId +"' and user_id ="+ uId +" and job_id ='"+ jobId +"'";
			db.executeSelect(sql);
			
			if(db.size()>0){
				r = false;
				
			}else{
			User user = userjoborg.getUser();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = orgManager.getOrgById(orgId);

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty(
					"filter",
					"(&(objectclass=groupOfNames)(member=uid="
							+ user.getUserName() + "," + userBase + "))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					SearchResult sr = (SearchResult) list.get(i);
					Attributes attrs = sr.getAttributes();
					String groupName = attrs.get("cn").get().toString();
					
						if(groupName.equals(org.getOrgName())){
							Attribute member = getItemAttribute(groupBase,
									"(&(objectclass=groupOfNames)|(cn=" + groupName
											+ "))", "member");
		
							// 删除指向当前用户的属性
							member.remove("uid=" + user.getUserName() + "," + userBase);
		
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
			    }
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
//		boolean r = false;
//
//		try {
//			
//			User user = userjoborg.getUser();
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//
//			p.getProperties().setProperty("parentdn", groupBase);
//			p.getProperties().setProperty(
//					"filter",
//					"(&(objectclass=groupOfNames)|(member=uid="
//							+ user.getUserName() + "," + userBase + "))");
//
//			List list = (List) dc.execute(p);
//			if (list != null && !list.isEmpty()) {
//				for (int i = 0; i < list.size(); i++) {
//					SearchResult sr = (SearchResult) list.get(i);
//					Attributes attrs = sr.getAttributes();
//					String groupName = attrs.get("cn").get().toString();
//
//					Attribute member = getItemAttribute(groupBase,
//							"(&(objectclass=groupOfNames)|(cn=" + groupName
//									+ "))", "member");
//
//					// 删除指向当前用户的属性
//					member.remove("uid=" + user.getUserName() + "," + userBase);
//
//					attrs = new BasicAttributes();
//					attrs.put(member);
//
//					p = new Parameter();
//					p.setCommand(Parameter.COMMAND_STORE);
//					p.setObject(attrs);
//
//					p.getProperties().setProperty("dn",
//							"cn=" + groupName + "," + groupBase);
//
//					p.getProperties().setProperty("id", groupName);
//					p.getProperties().setProperty("modifyoption",
//							String.valueOf(DirContext.REPLACE_ATTRIBUTE));
//
//					dc.execute(p);
//				}
//			}
//		} catch (Exception e) {
//			logger.error(e);
//		}
//
//		return r;
	}
	public boolean deleteUserjoborg(Organization org,User user)
	throws ManagerException{
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			String orgId = org.getOrgId();
			String uId = user.getUserId().toString();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org1= orgManager.getOrgById(orgId);

//			System.out.println("................."+orgId);
//			System.out.println("................."+uId);
//			System.out.println("................."+org1.getOrgName());
//			
			
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty(
					"filter",
					"(&(objectclass=groupOfNames)(member=uid="
							+ user.getUserName() + "," + userBase + "))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					SearchResult sr = (SearchResult) list.get(i);
					Attributes attrs = sr.getAttributes();
					String groupName = attrs.get("cn").get().toString();
					
						if(groupName.equals(org1.getOrgName())){
							
							Attribute member = getItemAttribute(groupBase,
									"(&(objectclass=groupOfNames)|(cn=" + groupName
											+ "))", "member");
		
							// 删除指向当前用户的属性
							member.remove("uid=" + user.getUserName() + "," + userBase);
		
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
			    
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
		
	}
	public boolean deleteUserjoborg(User user) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty(
					"filter",
					"(&(objectclass=groupOfNames)|(member=uid="
							+ user.getUserName() + "," + userBase + "))");

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
					member.remove("uid=" + user.getUserName() + "," + userBase);

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
			logger.error(e);
		}

		return r;
	}

	public boolean deleteUserresop(Userresop userresop) throws ManagerException {
		
		return false;
	} 

	public boolean deleteUserrole(Userrole userrole) throws ManagerException {
		
		return false;
	}

	public boolean deleteUserrole(User user) throws ManagerException {
		
		return false;
	}

	public boolean deleteUserrole(Organization org, Role role)
			throws ManagerException {
		
		return false;
	}

	public List getAccreditList(String userName) throws ManagerException {
		
		return null;
	}

	public List getTempaccredit(String userName) throws ManagerException {
		
		return null;
	}

	/**
	 * 根据属性名和属性值取LDAP中的用户对象实例
	 * 
	 * @param propName
	 *            LDAP中的User对象属性名，如：uid (uid对应用户帐号)
	 * @param value
	 *            与属性对应的值
	 */
	public User getUser(String propName, String value) throws ManagerException {
		User user = null;

		try {
			LdapControl dc = new LdapControl();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", userBase);
			p.getProperties().setProperty(
					"filter",
					"(&(objectclass=inetorgperson)|(" + propName + "=" + value
							+ "))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				SearchResult sr = (SearchResult) list.get(0);
				user = new User();
				Attributes attrs = sr.getAttributes();

				// 更新开始：UserID 已经修改 Integer 型，从 LDAP 中取数据时设置 UserID 为 -1 ，而如果
				// 需要保存数据到 DB 中的话则判断 UserID 是否为 -1 是则表明该数据来自 LDAP 中。
				// user.setUserId(attrs.get("uid").get().toString());
				user.setUserId(new Integer(-1));
				// 更新结束

				user.setUserName(attrs.get("cn").get().toString());
				user
						.setUserPassword(attrs.get("userPassword").get()
								.toString());
				user.setUserRealname(attrs.get("displayName").get().toString());
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return user;
	}

	public User getUser(String hsql) throws ManagerException {
		
		return null;
	}

	public List getUserList() throws ManagerException {
		List userList = new ArrayList();

		try {
			LdapControl dc = new LdapControl();
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", userBase);
			p.getProperties().setProperty("filter",
					"(&(objectclass=inetorgperson))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				for (int i = 0; i < list.size(); i++) {
					SearchResult sr = (SearchResult) list.get(i);
					User user = new User();
					Attributes attrs = sr.getAttributes();
					user.setUserName(attrs.get("uid").get().toString());

					// 更新开始：UserID 已经修改 Integer 型，从 LDAP 中取数据时设置 UserID 为 -1
					// ，而如果需要保存数据到 DB 中的话则判断 UserID 是否为 -1 是则表明该数据来自 LDAP 中。
					// user.setUserId(attrs.get("cn").get().toString());
					user.setUserId(new Integer(-1));
					// 更新结束

					user.setUserPassword(attrs.get("userPassword").get()
							.toString());
					user.setUserRealname(attrs.get("displayName").get()
							.toString());
					userList.add(user);
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return userList;
	}

	public List getUserList(String hsql) throws ManagerException {
		
		return null;
	}

	public List getUserList(Group group) throws ManagerException {
		
		return null;
	}

	public List getUserList(Job job) throws ManagerException {
		
		return null;
	}

	public List getUserList(Operation oper) throws ManagerException {
		
		return null;
	}

	public List getUserList(Organization org) throws ManagerException {
		
		return null;
	}

	public List getUserList(Res res) throws ManagerException {
		
		return null;
	}

	public List getUserList(Role role) throws ManagerException {
		
		return null;
	}

	public List getUserList(String propName, String value, boolean isLike)
			throws ManagerException {
		
		return null;
	}

	public List getUserList(Organization org, Role role)
			throws ManagerException {
		
		return null;
	}

	public List getUserList(Organization org, Job job) throws ManagerException {
		
		return null;
	}

	public boolean isUserExist(User user) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			String uid = user.getUserName();

			p.getProperties().setProperty("parentdn", userBase);
			p.getProperties().setProperty("filter",
					"(&(objectclass=inetorgperson)|(uid=" + uid + "))");

			List list = (List) dc.execute(p);

			if (list != null && !list.isEmpty())
				r = true;
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean isUserroleExist(Userrole userrole) throws ManagerException {
		return false;
	}

	public boolean isUserjoborgExist(Userjoborg userjoborg)
			throws ManagerException {
		
		return false;
	}

	public User loadAssociatedSet(String userId, String associated)
			throws ManagerException {
		User user = null;

		try {
			LdapControl dc = new LdapControl();

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", userBase);
			p.getProperties().setProperty("filter",
					"(&(objectclass=inetorgperson)|(cn=" + userId + "))");

			List list = (List) dc.execute(p);
			if (list != null && !list.isEmpty()) {
				SearchResult sr = (SearchResult) list.get(0);
				user = new User();
				// 基本属性
				Attributes attrs = sr.getAttributes();

				// 更新开始：UserID 已经修改 Integer 型，从 LDAP 中取数据时设置 UserID 为 -1 ，而如果
				// 需要保存数据到 DB 中的话则判断 UserID 是否为 -1 是则表明该数据来自 LDAP 中。
				// user.setUserId(attrs.get("cn").get().toString());
				user.setUserId(new Integer(-1));
				// 更新结束

				user.setUserName(attrs.get("uid").get().toString());
				user
						.setUserPassword(attrs.get("userPassword").get()
								.toString());
				user.setUserRealname(attrs.get("displayName").get().toString());

				// 清除基本属性
				attrs.remove("uid");
				attrs.remove("cn");
				attrs.remove("displayName");
				attrs.remove("userPassword");

				// 用户属性
				if (associated.equals(UserManager.ASSOCIATED_USERATTRSET)) {
					// 取其它属性
					user.setUserattrSet(new HashSet());

					NamingEnumeration ne = attrs.getAll();
					while (ne.hasMore()) {
						Attribute attr = (Attribute) ne.next();
						Userattr userattr = new Userattr();
						userattr.setUser(user);
						userattr.setUserattrName(attr.getID());
						userattr.setUserattrValue(attr.get().toString());
						user.getUserattrSet().add(userattr);
					}
				}

				// 用户组
				if (associated.equals(UserManager.ASSOCIATED_USERGROUPSET)) {
					user.setUsergroupSet(new HashSet());

				}
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return user;
	}

	public boolean storeAccredit(Accredit accredit) throws ManagerException {
		
		return false;
	}

	public boolean storeTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException {
		
		return false;
	}

	public boolean storeUser(User user) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			// 用户在LDAP中所继承的类
			Attribute itemClass = new BasicAttribute("objectclass");
			itemClass.add("inetOrgPerson");
			itemClass.add("ibm-appuuidaux");

			Attribute cn = new BasicAttribute("cn", user.getUserName());
			Attribute uid = new BasicAttribute("uid", user.getUserName());
			Attribute sn = new BasicAttribute("sn", user.getUserName());
			Attribute userPassword = new BasicAttribute("userPassword", user
					.getUserPassword());
			Attribute givenname = new BasicAttribute("givenname", user
					.getUserRealname());

			//add by pwl 20060414
			String strBirthday = "";
			if ( user.getUserBirthday() != null ){
				strBirthday = user.getUserBirthday().toString();
			}
			
			Attribute destinationIndicator = new BasicAttribute("destinationIndicator", 
					strBirthday); //生日
			
			Attribute mail = new BasicAttribute("mail",	user.getUserEmail()); //Email			
			Attribute postalCode = new BasicAttribute("postalCode",
					user.getUserPostalcode()); //邮编
			Attribute telexNumber = new BasicAttribute("telexNumber",
					user.getUserIdcard()); //身份证号码
			Attribute displayName = new BasicAttribute("displayName",
					user.getUserPinyin()); //拼音			
			Attribute mobile = new BasicAttribute("mobile"); 
			mobile.add(user.getUserMobiletel1());//移动电话/移动电话2 一个多值属性存储
			
			Attribute telephoneNumber = new BasicAttribute("telephoneNumber"); 
			telephoneNumber.add(user.getRemark2()); //手机短号码		一个多值属性存储
			//add by pwl 20060428
			Attribute usertype = new BasicAttribute("employeeType"); 
			usertype.add(""+user.getUserType()); //用户类型 是否是OA用户

			Attribute userisvalid = new BasicAttribute("description"); 
			userisvalid.add(""+user.getUserIsvalid()); //用户是否有效

			Attributes attrs = new BasicAttributes();
			attrs.put(itemClass);
			attrs.put(cn);
			attrs.put(uid);
			attrs.put(sn);
			attrs.put(userPassword);
			attrs.put(givenname);
			//add by pwl 20060414
			
			attrs.put(destinationIndicator);
			attrs.put(mail);
			attrs.put(postalCode);
			attrs.put(telexNumber);
			attrs.put(displayName);
			attrs.put(mobile);
			attrs.put(telephoneNumber);
			//add by pwl 20060428
			attrs.put(usertype);
			attrs.put(userisvalid);
			
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_STORE);
			p.setObject(attrs);

			p.getProperties().setProperty("dn",
					"uid=" + user.getUserName() + "," + userBase);

			String id = "";
			if (isUserExist(user))
				id = user.getUserName();

			p.getProperties().setProperty("id", id);
			p.getProperties().setProperty("modifyoption",
					String.valueOf(DirContext.REPLACE_ATTRIBUTE));

			dc.execute(p);
			r = true;
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean storeUser(User user, String propName, String value)
			throws ManagerException {
		
		return false;
	}

	public boolean storeUserattr(Userattr userattr) throws ManagerException {
		boolean r = false;

//		try {
//			User user = userattr.getUser();
//
//			Attributes attrs = new BasicAttributes();
//			// 用户属性
//			Attribute attr = new BasicAttribute(userattr.getUserattrName(),
//					userattr.getUserattrValue());
//			attrs.put(attr);
//
//			// 存储用户属性
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_STORE);
//			p.setObject(attrs);
//
//			String rdn = user.getUserName();
//
//			p.getProperties().setProperty("dn", "uid=" + rdn + "," + userBase);
//			p.getProperties().setProperty("id", rdn);
//
//			int mo = DirContext.ADD_ATTRIBUTE;
//			if (isAttributeExist(rdn, userattr.getUserattrName()))
//				mo = DirContext.REPLACE_ATTRIBUTE;
//
//			p.getProperties().setProperty("modifyoption", String.valueOf(mo));
//			dc.execute(p);
//			r = true;
//		} catch (Exception e) {
//			logger.error(e);
//		}
//
		return r;
	}

	public boolean storeUsergroup(Usergroup usergroup) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Group group = usergroup.getGroup();
			User user = usergroup.getUser();

			if (group == null || user == null)
				throw new ManagerException("usergroup 对象必须关联 user 和 group 对象");

			if (isGroupExist(group)) {
				Attribute member = getItemAttribute(groupBase,
						"(&(objectclass=groupOfNames)|(cn="
								+ group.getGroupName() + "))", "member");

				if (member == null)
					member = new BasicAttribute("member");
				else {
					for (int i = 0; i < member.size(); i++) {
						String s = (String) member.get(i);
						if (s.equals("uid=" + user.getUserName()))
							return false;
					}
				}

				member.add("uid=" + user.getUserName() + "," + userBase);

				Attributes attrs = new BasicAttributes();
				attrs.put(member);

				Parameter p = new Parameter();
				p.setCommand(Parameter.COMMAND_STORE);
				p.setObject(attrs);

				p.getProperties().setProperty("dn",
						"cn=" + group.getGroupName() + "," + groupBase);

				p.getProperties().setProperty("id", group.getGroupName());
				p.getProperties().setProperty("modifyoption",
						String.valueOf(DirContext.REPLACE_ATTRIBUTE));

				dc.execute(p);
				r = true;
			} else {
				logger.error("无法找用户组 “" + group.getGroupName() + "”");
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean storeUserjoborg(Userjoborg userjoborg)
			throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Organization org = userjoborg.getOrg();
			User user = userjoborg.getUser();

			if (org == null || user == null)
				throw new ManagerException("usergroup 对象必须关联 user 和 org 对象");

			if (isOrgExist(org)) {
				Attribute member = getItemAttribute(groupBase,
						"(&(objectclass=groupOfNames)|(cn=" + org.getOrgName()
								+ "))", "member");

				if (member == null)
					member = new BasicAttribute("member");
				else {
					for (int i = 0; i < member.size(); i++) {
						String s = (String) member.get(i);
						if (s.equals("uid=" + user.getUserName()))
							return false;
					}
				}

				member.add("uid=" + user.getUserName() + "," + userBase);

				Attributes attrs = new BasicAttributes();
				attrs.put(member);

				Parameter p = new Parameter();
				p.setCommand(Parameter.COMMAND_STORE);
				p.setObject(attrs);

				p.getProperties().setProperty("dn",
						"cn=" + org.getOrgName() + "," + groupBase);

				p.getProperties().setProperty("id", org.getOrgName());
				p.getProperties().setProperty("modifyoption",
						String.valueOf(DirContext.REPLACE_ATTRIBUTE));

				dc.execute(p);
				r = true;
			} else {
				logger.error("无法找用户组 “" + org.getOrgName() + "”");
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}

	public boolean storeUserresop(Userresop userresop) throws ManagerException {
		
		return false;
	}

	public boolean storeUserrole(Userrole userrole) throws ManagerException {
		
		return false;
	}

	public void addListener(Listener listener) {
		
	}

	public void change(Event source, boolean synchronizable) {
		
	}

	public void change(Event source) {
		
	}


	public boolean deleteUserjoborg(Job job, Organization org)
			throws ManagerException {
		return false;

	}

	public boolean deleteUserjoborg(Organization org) throws ManagerException {
		return false;

	}

	public List getUserList(Orgjob oj) throws ManagerException {
		return null;
	}
	public boolean deleteUserrole(Role role,Group group) throws ManagerException{
		//王卓添加
		return false;
	}
	public boolean storeLogincount(String userName)
	throws ManagerException{
		return false;
		
	}
	public boolean getUserSnList(String orgId,String jobId,int jobSn) throws ManagerException
{
		return false;
		
	}

	public ListInfo getUserList(String sql, int offset, int maxItem) throws ManagerException {
		
		return null;
	}
	public boolean deleteUserjoborg(Job job, User user)
	throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_GET);

			p.getProperties().setProperty("parentdn", groupBase);
			p.getProperties().setProperty(
					"filter",
					"(&(objectclass=groupOfNames)|(member=uid="
							+ user.getUserName() + "," + userBase + "))");

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
					member.remove("uid=" + user.getUserName() + "," + userBase);

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
			logger.error(e);
		}

		return r;
		
	}
	public List getUserjoborgList(String userId,String orgId) throws ManagerException{
		return null;
	
	}
	public List getDicList() throws ManagerException{
		return null;
		
	}
	public boolean storeUserjoborg(String userId,String jobId,String orgId,boolean needevent)
	throws ManagerException{
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			UserManager userManager = SecurityDatabase.getUserManager();
			OrgManager orgManager = SecurityDatabase.getOrgManager();
			Organization org = orgManager.getOrgById(orgId);
			User user = userManager.getUserById(userId);
			
			if (org == null || user == null)
				throw new ManagerException("usergroup 对象必须关联 user 和 org 对象");

			if (isOrgExist(org)) {
				Attribute member = getItemAttribute(groupBase,
						"(&(objectclass=groupOfNames)|(cn=" + org.getOrgName()
								+ "))", "member");

				if (member == null)
					member = new BasicAttribute("member");
				else {
					for (int i = 0; i < member.size(); i++) {
						String s = (String) member.get(i);
						if (s.equals("uid=" + user.getUserName()))
							return false;
					}
				}

				member.add("uid=" + user.getUserName() + "," + userBase);

				Attributes attrs = new BasicAttributes();
				attrs.put(member);

				Parameter p = new Parameter();
				p.setCommand(Parameter.COMMAND_STORE);
				p.setObject(attrs);

				p.getProperties().setProperty("dn",
						"cn=" + org.getOrgName() + "," + groupBase);

				p.getProperties().setProperty("id", org.getOrgName());
				p.getProperties().setProperty("modifyoption",
						String.valueOf(DirContext.REPLACE_ATTRIBUTE));

				dc.execute(p);
				r = true;
			} else {
				logger.error("无法找用户组 “" + org.getOrgName() + "”");
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
		
	}
	
	public boolean deleteUserjoborg(String userId,String jobId,String orgId)
	throws ManagerException{
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			DBUtil db = new DBUtil();
			String sql ="select * from TD_SM_USERJOBORG where " +
			" org_id ='"+ orgId +"' and user_id ="+ userId +"";
			db.executeSelect(sql);
//			System.out.println(db.size());
			if(db.size()>0){
				r = false;
				
			}else{

				UserManager userManager = SecurityDatabase.getUserManager();
				User user = userManager.getUserById(userId);
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				Organization org = orgManager.getOrgById(orgId);
				Parameter p = new Parameter();
				p.setCommand(Parameter.COMMAND_GET);

				p.getProperties().setProperty("parentdn", groupBase);
				p.getProperties().setProperty(
						"filter",
						"(&(objectclass=groupOfNames)(member=uid="
								+ user.getUserName() + "," + userBase + "))");

				List list = (List) dc.execute(p);
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SearchResult sr = (SearchResult) list.get(i);
						Attributes attrs = sr.getAttributes();
						String groupName = attrs.get("cn").get().toString();
						if(groupName.equals(org.getOrgName())){
							Attribute member = getItemAttribute(groupBase,
									"(&(objectclass=groupOfNames)|(cn=" + groupName
											+ "))", "member");

							// 删除指向当前用户的属性
							member.remove("uid=" + user.getUserName() + "," + userBase);

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
				}
			}
			
			
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
	}
	public boolean userResCopy(String userId,String[] userid)
	throws ManagerException{
		return false;
		
	}

	public List getUserList(String orgid, String jobid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserList(String[][] orgjobs) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean deleteUsergroup(String userId,String groupId) throws ManagerException{
		boolean r = false;
			try {
				LdapControl dc = new LdapControl();
				UserManager userManager = SecurityDatabase.getUserManager();
				User user = userManager.getUserById(userId);
				GroupManager groupManager = SecurityDatabase.getGroupManager();
				Group group = groupManager.getGroupByID(groupId);
				
				Parameter p = new Parameter();
				p.setCommand(Parameter.COMMAND_GET);

				p.getProperties().setProperty("parentdn", groupBase);
				p.getProperties().setProperty(
						"filter",
						"(&(objectclass=groupOfNames)|(member=uid="
								+ user.getUserName() + "," + userBase + "))");

				List list = (List) dc.execute(p);
				if (list != null && !list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						SearchResult sr = (SearchResult) list.get(i);
						Attributes attrs = sr.getAttributes();
						String groupName = attrs.get("cn").get().toString();
						
						
							
												
						if(groupName.equals(group.getGroupName())){
						Attribute member = getItemAttribute(groupBase,
								"(&(objectclass=groupOfNames)|(cn=" + groupName
										+ "))", "member");

						// 删除指向当前用户的属性
						member.remove("uid=" + user.getUserName() + "," + userBase);

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
				}
			
			
			
		} catch (Exception e) {
			logger.error(e);
		}

		return r;
		
	}

	public boolean storeAlotUserRole(String[] ids, String[] roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delAlotUserRole(String[] ids, String[] roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean storeUserjoborg(String userId,String jobId,String orgId,String jobuserSn,String jobSn,boolean needevent)
	throws ManagerException{
		return false;
		
	}

	public boolean storeAlotUserJob(String[] ids, String[] jobid,String orgid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delAlotJobRole(String[] ids, String[] jobid, String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeUserjoborg(String userId, String[] jobId, String orgId) throws ManagerException {
		// TODO Auto-generated method stub 
		return false;
	}
	public List getmemberTypeList(String typeid) throws ManagerException{
		return null;
		
	}
	public void storeUserrole(String userId,String roleId) throws ManagerException{
	
	}
	public void creatorUser(User user,String orgId,String jobId) throws ManagerException{
			
	}

	public List getOrgUserList(String orgid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUsersListOfRole(String roleid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public ListInfo getUserInfoList(String sql, int offset, int maxItem)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 保存用户的排序
	 * @param orgId
	 * @param jobId
	 * @param jobSn
	 * @param userId
	 * @return
	 * @throws Exception 
	 * UserManager.java
	 * @author: ge.tao
	 */
	public  String storeAllUserSnJobOrg(String orgId, String jobId,
			String jobSn, String[] userId) throws Exception {
		try {
			UserManager userManager = SecurityDatabase.getUserManager();

			// 修改：需要完整用户对象信息
			// Organization org = new Organization();
			// org.setOrgId(orgId);
			OrgManager orgMgr = SecurityDatabase.getOrgManager();
			Organization org = orgMgr.getOrgById(orgId);
			// 修改结束
			Job job = new Job();
			job.setJobId(jobId);
			// 修改：需要完成用户对象信息
			// User user = new User();
			// user.setUserId(Integer.valueOf(userList[i]));

			// 在保存之前先删除该机构下改岗位的所有用户.然后再添加
			//userManager.deleteUserjoborg(job, org);
			DBUtil db = new DBUtil();
			String str="delete from td_sm_userjoborg where " +
					" org_id ='"+ orgId+ "' and job_id='"+ jobId +"'";
			db.executeDelete(str);

			for (int i = 0; userId != null && i < userId.length; i++) {
				User user = userManager.getUserById(userId[i]);
				// 修改结束
				Userjoborg userjoborg = new Userjoborg();
				userjoborg.setUser(user);
				userjoborg.setJob(job);
				userjoborg.setOrg(org);
				userjoborg.setSameJobUserSn( new Integer(i + 1));
				userjoborg.setJobSn(new Integer(Integer.parseInt(jobSn) + 1));
				userManager.storeUserjoborg(userjoborg);
			}

		} catch (Exception e) {
			return "fail";
		}
		return "success";
	}

	public void storeOrgUserOrder(String orgId, String[] userId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public String deleteUJOAjax(String uid, String[] jobIds, String orgId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public int getUserSN(String orgid, String userid) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}


	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	public String deleteUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	public String storeUJOAjax(String uid, String[] jobIds, String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String storeUserOrgJob(Integer uid, String orgId, String[] jobid) {
		// TODO Auto-generated method stub
		return null;
	}

	public void resetUserMainOrg(String userid, String oldmainorg) {
		// TODO Auto-generated method stub
		
	}

	public void storeUJOAjax_batch(String[] ids, String[] jobid, String orgid) {
		// TODO Auto-generated method stub
		
	}

	public void storeAlotUserOrg(String[] ids, String[] jobids, String orgid) throws ManagerException, SPIException {
		// TODO Auto-generated method stub
		
	}

	public void delAlotUserOrg(String[] ids, String[] jobids, String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	public String addUser(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean updateUser(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateUserPassword(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#addUsergroup(java.lang.Integer, java.lang.String[])
	 */
	public boolean addUsergroup(Integer userid, String[] groupid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#deleteUsergroup(java.lang.Integer, java.lang.String[])
	 */
	public boolean deleteUsergroup(Integer userid, String[] groupids) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#addUserrole(java.lang.String, java.lang.String[])
	 */
	public void addUserrole(String userId, String[] roleIds) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#deleteUserrole(java.lang.String, java.lang.String[])
	 */
	public void deleteUserrole(String userId, String[] roleIds) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	public boolean addUserOrg(String[] userIds, String orgId, String classType) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public User getUserById(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public User getUserByName(String userName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteUserRes(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds, boolean isInsert) throws ManagerException{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteDisperseOrguser() {
		// TODO Auto-generated method stub
		return false;
	}

	public List getUsersListOfRoleInOrg(String roleid, String orgId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isTaxmanager(String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public List getOrgUserList(String orgid, String userId) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isContainUser(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#addUserrole(java.lang.String, java.lang.String[], java.lang.String)
	 */
	public void addUserrole(String userId, String[] roleIds, String currentUserId) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#deleteUserrole(java.lang.String, java.lang.String[], java.lang.String)
	 */
	public void deleteUserrole(String userId, String[] roleIds, String roleTypes) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#userOrgInfo(java.lang.String)
	 */
	public String userOrgInfo(AccessControl control,String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteBatchUserRes(String[] userIds) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getUserMainOrgId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getCuruserAdministrableDeleteUser(String curUserId,String[] selectUserNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteUserjoborg(String userId, String jobId, String orgId,
			boolean sendEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public String addUser(User user, boolean isEvent) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean deleteBatchUser(User[] users) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<User> getOrgManager(String org_id) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadUsers(UserCacheManager userCache) throws ManagerException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#ishistorypassword(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean ishistorypassword(int userid, String password)
			throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#isPasswordExpired(int)
	 */
	@Override
	public boolean isPasswordExpired(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#isPasswordExpired(com.frameworkset.platform.sysmgrcore.entity.User)
	 */
	@Override
	public boolean isPasswordExpired(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#initpasswordupdatetime(int)
	 */
	@Override
	public Timestamp initpasswordupdatetime(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#getPasswordExpiredTime(int)
	 */
	@Override
	public Date getPasswordExpiredTime(int userid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#getPasswordExpiredTime(java.sql.Timestamp)
	 */
	@Override
	public Date getPasswordExpiredTime(Timestamp passwordupdatetime,int expiredays) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDefaultPasswordDualTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#getUserPasswordDualTimeByUserAccount(java.lang.String)
	 */
	@Override
	public int getUserPasswordDualTimeByUserAccount(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.platform.sysmgrcore.manager.UserManager#getPasswordExpiredTimeByUserAccount(java.lang.String)
	 */
	@Override
	public Date getPasswordExpiredTimeByUserAccount(String userAccount)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsers(String username) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MoreListInfo getMoreUsers(String username, long offset, int pagesize)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}