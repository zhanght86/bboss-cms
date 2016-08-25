package com.frameworkset.platform.sysmgrcore.manager.ldap;

import java.util.List;

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

import com.frameworkset.common.tag.pager.ListInfo;
import com.frameworkset.common.tag.pager.config.PageConfig;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.control.LdapControl;
import com.frameworkset.platform.sysmgrcore.control.Parameter;
import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Groupexp;
import com.frameworkset.platform.sysmgrcore.entity.Grouprole;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.GroupManager;
  
public class GroupManagerImpl extends EventHandle implements GroupManager {

	private static Logger logger = Logger.getLogger(GroupManagerImpl.class
			.getName());

//	private LdapControl dc = new LdapControl();

	private String groupBase = ConfigManager.getInstance().getConfigValue(
			"groupBase");

	private Attribute getItemAttribute(String parentdn, String filter,
			String propName) {
		Attribute attr = null;
		try {
			LdapControl dc = new LdapControl();
//			DataControl dc = DataControl
//					.getInstance(DataControl.CONTROL_INSTANCE_LDAP);

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
	/**
	 * 新增用户组信息
	 * @param group 组实体对象
	 */
	public String insertGroup(Group group,String currentUserId) throws ManagerException
	{
		return null;
	}
	
	/**
	 * 修改用户组信息
	 * @param group 组实体对象
	 */
	public String updateGroup(Group group,String oldName) throws ManagerException
	{
		return null;
	}
	/**
	 * 新增当前组下的角色集合（支持批量处理）
	 * @param groupId 组ID
	 * @param roleId 角色ID数组
	 */
	public boolean insertGroupRole(String groupId,String[] roleId) throws ManagerException
	{return false;}
	/**
	 * 删除当前组下的角色集合（支持批量处理）
	 * @param groupId 组ID
	 * @param roleIds 角色ID组合串（注：以","为分割符的组合字符串）
	 */
	public boolean deleteGroupRole(String groupId,String roleIds) throws ManagerException
	{return false;}
	/**
	 * 新增当前组下的用户集合（支持批量处理）
	 * @param groupId 组ID
	 * @param userIds 用户ID数组
	 */
	public boolean insertGroupUser(String groupId,String[] userId) throws ManagerException
	{return false;}
	/**
	 * 删除当前组下的用户集合（支持批量处理）
	 * @param groupId 组ID
	 * @param userIds 用户ID组合串（注：以","为分割符的组合字符串）
	 */
	public boolean deleteGroupUser(String groupId,String userIds) throws ManagerException
	{return false;}
	/**
	 * 得到一个用户组对应的角色列表和所有角色列表
	 * 
	 * @param groupId 用户组ID
	 * @return 
	 * @throws Exception
	 */
	public List getGroupRolesByGroupId(String groupId) throws Exception
	{return null;}
	/**
	 * 得到某一机构的用户列表和隶属某一用户组的用户列表
	 * 
	 * 修改：用户列表变为用户实名+工号
	 * 
	 * @param groupId
	 *            用户组ID
	 * @param orgid
	 *            部门ID
	 * @param isRecursive
	 *                   
	 * @throws Exception
	 */
	public List getUserList(String groupId,String orgid,String isRecursive)throws Exception
	{return null;}
	
	public boolean deleteGroup(Group group) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_DELETE);

			String rdn = group.getGroupName();
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
							+ group.getGroupName() + "," + groupBase + "))");

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
					member.remove("cn=" + group.getGroupName() + "," + groupBase);

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
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	public boolean deleteGroupexp(Groupexp groupexp) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteGrouprole(Grouprole grouprole) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteGrouprole(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	// public boolean deleteGrouprole(String groupId) throws ManagerException {
	// // TODO Auto-generated method stub
	// return false;
	// }

	public Group getGroup(String propName, String value)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupList() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupList(Groupexp gexp) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupList(Organization org) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupList(String propName, String value, boolean isLike)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupList(User user) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public PageConfig getPageConfig() throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isGroupExist(Group group) throws ManagerException {
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
			e.printStackTrace();
			logger.error(e);
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	public Group loadAssociatedSet(String groupId, String associated)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public void refresh(Group group) throws ManagerException {
		// TODO Auto-generated method stub
	}

	public boolean storeGroup(Group group) throws ManagerException {
		boolean r = false;

		try {
			LdapControl dc = new LdapControl();
			Attribute itemClass = new BasicAttribute("objectclass");
			itemClass.add("groupOfNames");
			itemClass.add("ibm-appuuidaux");

			Attribute cn = new BasicAttribute("cn", group.getGroupName());
			Attribute member = new BasicAttribute("member");
			member.add("uid=root");

			Attributes attrs = new BasicAttributes();
			attrs.put(itemClass);
			attrs.put(cn);
			attrs.put(member);

			Parameter p = new Parameter();
			p.setCommand(Parameter.COMMAND_STORE);
			p.setObject(attrs);

			p.getProperties().setProperty("dn",
					"cn=" + group.getGroupName() + "," + groupBase);

			String id = "";
			if (isGroupExist(group))
				id = group.getGroupName();

			p.getProperties().setProperty("id", id);
			p.getProperties().setProperty("modifyoption",
					String.valueOf(DirContext.REPLACE_ATTRIBUTE));

			dc.execute(p);

			// 如果当前组是其它组的子组则在父组的成员属性中增加一个指向它的属性
			if (group.getParentGroup() != null) {
				// 查找 ParentID 所指向的父组条目
				if (isGroupExist(group.getParentGroup())) {
					member = getItemAttribute(groupBase,
							"(&(objectclass=groupOfNames)|(cn="
									+ group.getParentGroup().getGroupName()
									+ "))", "member");

					if (member == null)
						member = new BasicAttribute("member");
					else {
						for (int i = 0; i < member.size(); i++) {
							String s = (String) member.get(i);
							if (s.equals("cn=" + group.getGroupName()))
								return true;
						}
					}

					member.add("cn=" + group.getGroupName() + "," + groupBase);

					attrs = new BasicAttributes();
					attrs.put(member);

					p.setCommand(Parameter.COMMAND_STORE);
					p.setObject(attrs);

					p.getProperties().setProperty(
							"dn",
							"cn=" + group.getParentGroup().getGroupName() + ","
									+ groupBase);

					p.getProperties().setProperty("id",
							group.getParentGroup().getGroupName());
					p.getProperties().setProperty("modifyoption",
							String.valueOf(DirContext.REPLACE_ATTRIBUTE));

					dc.execute(p);
				}
			}

			r = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	public boolean storeGroupexp(Groupexp groupexp) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean storeGrouprole(Grouprole grouprole) throws ManagerException {
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

	public List getChildGroupList(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isContainChildGroup(Group group) throws ManagerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* （非 Javadoc）
	 * @see com.frameworkset.platform.sysmgrcore.manager.GroupManager#getGroupList(com.frameworkset.platform.sysmgrcore.entity.Role)
	 */
	public List getGroupList(Role role) throws ManagerException {
		// TODO 自动生成方法存根
		return null;
	}

	/* （非 Javadoc）
	 * @see com.frameworkset.platform.sysmgrcore.manager.GroupManager#deleteGrouprole(com.frameworkset.platform.sysmgrcore.entity.Role)
	 */
	public boolean deleteGrouprole(Role role) throws ManagerException {
		// TODO 自动生成方法存根
		return false;
	}
	public boolean deleteGroup(String groupId) throws ManagerException{
		return false;
	}
	public List getGroupList(String hql) throws ManagerException{
		return null;
		
	}
	 public List getChildGroupList(String groupid) throws ManagerException{
		return null;
		 
	 }
	public Group getGroupByID(String groupid) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}
	public Group getGroupByName(String groupName) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public ListInfo getGroupList(String hql, long offset, int maxsize) throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}
	public String saveGroup(Group group, String currentUserId)
			throws ManagerException {
		// TODO Auto-generated method stub
		return null;
	}
}
