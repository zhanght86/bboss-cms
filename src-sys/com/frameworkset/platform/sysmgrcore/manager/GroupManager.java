package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;

import org.frameworkset.spi.Provider;


import com.frameworkset.platform.sysmgrcore.entity.Group;
import com.frameworkset.platform.sysmgrcore.entity.Groupexp;
import com.frameworkset.platform.sysmgrcore.entity.Grouprole;
import com.frameworkset.platform.sysmgrcore.entity.Role;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.tag.pager.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：组管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 王卓
 */
public interface GroupManager extends Provider,Serializable {

	/**
	 * 组所对应的“用户组”关系对象集合的名称 usergroupSet
	 */
	public static final String ASSOCIATED_USERGROUPSET = "usergroupSet";

	/**
	 * 组所对应的“机构组”关系对象集合的名称 orggroupSet
	 */
	public static final String ASSOCIATED_ORGGROUPSET = "orggroupSet";

	/**
	 * 组所对应的“动态组表达式”关系对象集合的名称 groupexpSet
	 */
	public static final String ASSOCIATED_GROUPEXPSET = "groupexpSet";


	/**
	 * 组所对应的“组角色”关系对象集合的名称 grouproleSet
	 */
	public static final String ASSOCIATED_GROUPROLESET = "grouproleSet";

	
	/**
	 * 新增组对象实例
	 * 
	 * @param group
	 *         需要新增到数据源中的组实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 返回提示信息
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public String insertGroup(Group group,String currentUserId) throws ManagerException;
	
	/**
	 * 存储组对象实例
	 * 
	 * @param group
	 *         需要存储到数据源中的组实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeGroup(Group group) throws ManagerException;

	
	/**
	 * 修改组对象实例
	 * 
	 * @param group
	 *         需要修改到数据源中的组实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果修改成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public String updateGroup(Group group,String oldName) throws ManagerException;
	/**
	 * 新增当前组下的角色集合（支持批量处理）
	 * @param groupId 组ID
	 * @param roleId 角色ID数组
	 */
	public boolean insertGroupRole(String groupId,String[] roleId) throws ManagerException;
	/**
	 * 删除当前组下的角色集合（支持批量处理）
	 * @param groupId 组ID
	 * @param roleIds 角色ID组合串（注：以","为分割符的组合字符串）
	 */
	public boolean deleteGroupRole(String groupId,String roleIds) throws ManagerException;
	/**
	 * 新增当前组下的用户集合（支持批量处理）
	 * @param groupId 组ID
	 * @param userIds 用户ID数组
	 */
	public boolean insertGroupUser(String groupId,String[] userId) throws ManagerException;
	/**
	 * 删除当前组下的用户集合（支持批量处理）
	 * @param groupId 组ID
	 * @param userIds 用户ID组合串（注：以","为分割符的组合字符串）
	 */
	public boolean deleteGroupUser(String groupId,String userIds) throws ManagerException;
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
	public List getUserList(String groupId,String orgid,String isRecursive)throws Exception;
	/**
	 * 得到一个用户组对应的角色列表和所有角色列表
	 * 
	 * @param groupId 用户组ID
	 * @return 
	 * @throws Exception
	 */
	public List getGroupRolesByGroupId(String groupId) throws Exception;
	
	/**
	 * 存储组角色实例，即往数据源（如：DB）中增加一个“组角色”的关系实体对象
	 * 
	 * @param grouprole
	 *         需要存储的组角色对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeGrouprole(Grouprole grouprole) throws ManagerException;

//	/**
//	 * 存储组前先根据指定的属性名（propName）和属性值（value）来查找数据源中的记录，如果存在则更新该记录 <br>
//	 * 否则插入一条新的记录
//	 * 
//	
//	 * @param propName
//	 *            对应于 Group 对象中的属性，如：groupName
//	 * @param value
//	 *            与 propName 对应的属性值
//	 * @return
//	 * @throws ManagerException
//	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
//	 */
//	public Group getGroup(String propName, String value)
//			throws ManagerException;
	
	public Group getGroupByID(String groupid)
	throws ManagerException;
	
	public Group getGroupByName(String groupName)
	throws ManagerException;


	/**
	 * 根据用户取所有的组
	 * 
	 * @param user
	 *            做为取组实体对象列表的用户实体对象，由于无法确定数据源所以传入该对<br>
	 *            象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果成功则返回组实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getGroupList(User user) throws ManagerException;

	
	/**
	 * 取所有组实例
	 * 
	 * @return 组实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getGroupList() throws ManagerException;

	/**
	 * 根据组对象的属性名和值取组列表，例如：<br>
	 * Group group = getGroupList("groupName","组名称",false);
	 * 
	 * @param propName
	 *            组对象实体的属性名，如：groupName
	 * @param value
	 *            与 propName 对应的属性值
	 * @param isLike
	 *            用于描述在提取对象的过程中是否取大致相同的组对象
	 * @return 取得的组实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getGroupList(String propName, String value, boolean isLike)
			throws ManagerException;

	/**
	 * 删除组实例同时将连带删除与该实例有关的所有实例，如：组与用户关系实例。
	 * 
	 * @param group
	 *         需要删除的组对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 * @throws Exception 
	 */
	public boolean deleteGroup(Group group) throws ManagerException, Exception;

	/**
	 * 删除动态组表达式实例同时将连带删除与该实例有关的所有实例，如：组与动态组表达式关系实例。
	 * 
	 * @param groupexp
	 *         需要删除的动态组表达式对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteGroupexp(Groupexp groupexp) throws ManagerException;

	/**
	 * 删除组与组角色的关系
	 * 
	 * @param grouprole
	 *            组角色的关系实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return boolean 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteGrouprole(Grouprole grouprole) throws ManagerException;

	/**
	 * 根据组名判断该组是否存在
	 * 
	 * @param group
	 *            组实体对象
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean isGroupExist(Group group) throws ManagerException;

	/**
	 * 刷新指定组的状态以及该组与其它实体的关联
	 * 
	 * @param group
	 */
	public void refresh(Group group) throws ManagerException;

	/**
	 * 装载当前 Group 对象实例所关联的其它对象到集合中
	 * 
	 * @param groupId
	 *            做为取组实体对象（包含指定对象集合）的组ID
	 * @param associated
	 *            需要装载的组实体中的集合名称，可以直接使用 GroupManager.ASSOCIATED_XXX 常量名来指定。<br>
	 *            如：GroupManager.ASSOCIATED_ORGGROUPSET
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public Group loadAssociatedSet(String groupId, String associated)
			throws ManagerException;


	/**
	 * 检查指定的组是否包含子组
	 * 
	 * @param group
	 *          需要检查是否包含子组的机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果包含了子组则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
    public boolean isContainChildGroup(Group group) throws ManagerException;
    /**
	 * 取指定组所包含的子组列表（不递归取所有子组）
	 * 
	 * @param group
	 *          需要取子组列表的组实体对象
	 * @return 子组实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
    public List getChildGroupList(Group group) throws ManagerException;
    /**
	 * 删除指定的组角色对象实例
	 * 
	 * @param group
	 *         需要删除的组角色中的组实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteGrouprole(Group group)throws ManagerException;
 
	/**
	 * 根据角色取组列表
	 * 
	 * @param role
	 *         角色实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 组实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getGroupList(Role role) throws ManagerException;
	
	/**
	 * 删除组的组角色关系实体对象
	 * 
	 * @param role
	 *          做为删除依据的角色实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteGrouprole(Role role) throws ManagerException;	
	/**
	 * 根据组id删除组及其子组
	 * @param groupId
	 * @return
	 * @throws ManagerException
	 */
	//public boolean deleteGroup(String groupId) throws ManagerException;
	
	/**
	 * 根据 Hibernate 的 HQL 语言取组列表，例如：<br>
	*/
	public List getGroupList(String hql) throws ManagerException;
	  /**
	 * 取指定组所包含的子组列表（递归取所有）
	 * 
	
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
    public List getChildGroupList(String groupid) throws ManagerException;

	/**
     * 分页查询信息
     * @param hql
     * @param offset
     * @param maxsize
     * @return
     * @throws ManagerException
     */
    public ListInfo getGroupList(String hql,long offset,int maxsize) throws ManagerException;
    
    /**
     * 根据用户当前ID添加用户组，与用户组关联起来
     * @param group
     * @param currentUserId
     * @return
     * @throws ManagerException
     */
    public String saveGroup(Group group,String currentUserId) throws ManagerException;
    
}
