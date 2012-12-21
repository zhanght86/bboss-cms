package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;

import org.frameworkset.spi.Provider;
import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.security.AccessControl;
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
import com.frameworkset.util.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：用户管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 
 */
public interface UserManager extends Provider, Serializable {

	/**
	 * 用户所对应的“用户岗位机构”关系对象集合的名称 userjoborgSet
	 */
	public final static String ASSOCIATED_USERJOBORGSET = "userjoborgSet";

	/**
	 * 用户所对应的“用户属性”关系对象集合的名称 userattrSet
	 */
	public static final String ASSOCIATED_USERATTRSET = "userattrSet";

	/**
	 * 用户所对应的“用户资源操作”关系对象集合的名称 userresopSet
	 */
	public static final String ASSOCIATED_USERRESOPSET = "userresopSet";

	/**
	 * 用户所对应的“委托授权”关系对象集合的名称 accreditSet
	 */
	public static final String ASSOCIATED_ACCREDITSET = "accreditSet";

	/**
	 * 用户所对应的“用户组”关系对象集合的名称 usergroupSet
	 */
	public static final String ASSOCIATED_USERGROUPSET = "usergroupSet";

	/**
	 * 用户所对应的“临时授权”关系对象集合的名称 tempaccreditSet
	 */
	public static final String ASSOCIATED_TEMPACCREDITSET = "tempaccreditSet";

	/**
	 * 用户所对应的“用户角色”关系对象集合的名称 userroleSet
	 */
	public static final String ASSOCIATED_USERROLESET = "userroleSet";

	/**
	 * 存储用户实例
	 * 
	 * @param user
	 *            需要存储的用户对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
//	public boolean storeUser(User user) throws ManagerException;

	/**
	 * 存储用户实例前先判断数据源中是事存在与属性名和值对应的记录，如果有则直接更新该记录否则增加新的记录
	 * 
	 * @param user
	 *            需要存储的用户对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @param propName
	 *            User 对象的属性，如：userName
	 * @param value
	 *            与 propName 对应的属性值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 * @deprecated 不推荐使用该方法，该方法的实现已经删除，调用该方法没有任何意义。
	 */
	public boolean storeUser(User user, String propName, String value)
			throws ManagerException;
	
	/**
	 * 根据用户登陆名保存该用户的登陆次数
	 */
	
	public boolean storeLogincount(String userName)
	throws ManagerException;

	/**
	 * 存储用户属性实例
	 * 
	 * @param userattr
	 *            需要存储的用户属性对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 * @deprecated 无效方法，方法实现已被删除
	 */
	public boolean storeUserattr(Userattr userattr) throws ManagerException;

	/**
	 * 存储用户岗位机构实例，即往数据源（如：DB）中增加一个“用户岗位机构”的关系实体对象
	 * 
	 * @param userjoborg
	 *            需要存储的“用户岗位机构”关系实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeUserjoborg(Userjoborg userjoborg)
			throws ManagerException;

	/**
	 * 存储用户角色实例，即往数据源（如：DB）中增加一个“用户角色”的关系实体对象
	 * 
	 * @param userrole
	 *            需要存储的用户角色对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeUserrole(Userrole userrole) throws ManagerException;

	/**
	 * 存储用户组实例，即往数据源（如：DB）中增加一个“用户组”的关系实体对象
	 * 
	 * @param usergroup
	 *            需要存储的用户组对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeUsergroup(Usergroup usergroup) throws ManagerException;

	/**
	 * 存储用户资源操作，即往数据源（如：DB）中增加一个“用户资源操作”的关系实体对象
	 * 
	 * @param userresop
	 *            需要存储的用户资源操作对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeUserresop(Userresop userresop) throws ManagerException;

	/**
	 * 存储针对用户的临时授权，暂时未使用
	 * 
	 * @param tempaccredit
	 *            需要保存的临时授权对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException;

	/**
	 * 存储委托授权，暂时未使用
	 * 
	 * @param accredit
	 *            需要保存的委托授权对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeAccredit(Accredit accredit) throws ManagerException;
	/**
	 * 批量用户角色授权
	 * @param ids
	 * @param roleid
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeAlotUserRole(String[] ids,String[] roleid)
	throws ManagerException;
	/**
	 * 存储用户岗位和机构对象
	 * @param userId
	 * @param jobId
	 * @param orgId
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeUserjoborg(String userId, String jobId[], String orgId)
	throws ManagerException;
	/**
	 * 存储用户角色关系
	 * @param userId
	 * @param roleId
	 * @return
	 * @throws ManagerException
	 */
	public void storeUserrole(String userId,String roleId) throws ManagerException;
	/**
	 * 根据用户id、岗位id和机构id保存用户岗位机构关系
	 * @param userId
	 * @param jobId
	 * @param orgId
	 * @param needevent add by biaoping.yin 2007.11.9
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeUserjoborg(String userId,String jobId,String orgId,boolean needevent)
	throws ManagerException;
	/**
	 * 根据用户id、岗位id、机构id、用户排序id和岗位排序id保存用户岗位机构关系
	 * @param userId
	 * @param jobId
	 * @param orgId
	 * @param jobuserSn
	 * @param jobSn
	 * @param needevent add by biaoping.yin on 2007.11.9
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeUserjoborg(String userId,String jobId,String orgId,String jobuserSn,String jobSn,boolean needevent)
	throws ManagerException;
	/**
	 * 批量用户岗位授权
	 * @param ids
	 * @param roleid
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeAlotUserJob(String[] ids,String[] jobid,String orgid)
	throws ManagerException;
	/**
	 * 删除用户实例同时将连带删除与该实例有关的所有实例，如：用户与角色关系实例。
	 * 
	 * @param user
	 *            需要删除的用户对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUser(User user) throws ManagerException;

	// 删除：因为根据 userId 来删除用户对象实例对于保存在数据库中的数据来说是合适的，但
	// 对于保存于 LDAP 中的数据确是行不通的。而通过传入 User 对象来决定删除的具体记录的优势
	// 在于可以用具体的维护类来决定用什么属性来做为删除依据。
	// /**
	// * 根据用户ID删除指定的用户实例
	// *
	// * @param userId
	// * @return
	// * @throws ManagerException
	// */
	// public boolean deleteUser(String userId) throws ManagerException;

	/**
	 * 通过数组中的用户ID批量删除指定的用户
	 * 
	 * @param userIds
	 *            用户ID数组
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteBatchUser(String userIds[]) throws ManagerException;
	
	public boolean deleteBatchUser(User[] users) throws ManagerException;
	
	/**
	 * 批量删除用户资源时改为批出理，gao.tang 修改
	 */
	public boolean deleteBatchUserRes(String userIds[]) throws ManagerException;

	/**
	 * 删除指定的用户属性
	 * 
	 * @param userattr
	 *            需要删除的用户属性，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserattr(Userattr userattr) throws ManagerException;

	/**
	 * 删除用户与用户机构岗位的关系
	 * 
	 * @param Userjoborg
	 *            需要删除的用户岗位机构对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return boolean 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException;

	/**
	 * 根据机构和岗位对象删除与用户机构岗位的关系（王卓添加）
	 * 
	 * @param job
	 *            岗位对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @param org
	 *            机构对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return boolean 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserjoborg(Job job, Organization org)
			throws ManagerException;
	
	/**
	 * 根据用户和岗位删除Userjoborg关系
	 * @param job
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteUserjoborg(Job job, User user)
			throws ManagerException;
	
	/**
	 * 根据机构和用户删除Userjoborg关系
	 * @param job
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteUserjoborg(Organization org,User user)
			throws ManagerException;

	/**
	 * 删除指定用户的所有“用户岗位机构”对象实例
	 * 
	 * @param user
	 *            做为删除依据的用户对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserjoborg(User user) throws ManagerException;

	/**
	 * 删除用户与用户组的关系
	 * 
	 * @param usergroup
	 *            用户组的关系实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return boolean 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUsergroup(Usergroup usergroup) throws ManagerException;

	/**
	 * 删除用户的用户组关系实体对象
	 * 
	 * @param user
	 *            做为删除依据的用户实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUsergroup(User user) throws ManagerException;

	/**
	 * 指定指定组下的所有用户与组的关系
	 * 
	 * @param group
	 *            做为删除依据的组实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUsergroup(Group group) throws ManagerException;

	/**
	 * 删除用户与角色的关系实体对象
	 * 
	 * @param userrole
	 *            做为删除依据的用户角色关系实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserrole(Userrole userrole) throws ManagerException;

	/**
	 * 删除指定用户的所有用户角色对象实例
	 * 
	 * @param user
	 *            做为删除依据的用户实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserrole(User user) throws ManagerException;
	
	/**
	 * 删除指定机构下具有指定角色的所有用户
	 * 
	 * @param org
	 *            机构对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @param role
	 *            角色对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserrole(Organization org, Role role)
			throws ManagerException;

	/**
	 * 删除指定角色的所有用户角色对象实例 //王卓添加
	 * 
	 * @param role 做为删除依据的角色实体对象
	 * @return 删除成功返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserrole(Role role, Group group)
			throws ManagerException;

	/**
	 * 删除用户与资源、操作的关系
	 * 
	 * @param userresop
	 *            用户资源操作，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserresop(Userresop userresop) throws ManagerException;

	/**
	 * 删除指定的临时授权
	 * 
	 * @param tempaccredit
	 *            临时授权对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteTempaccredit(Tempaccredit tempaccredit)
			throws ManagerException;

	/**
	 * 删除授权实体对象
	 * 
	 * @param accredit
	 *            做为删除依据的授权对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteAccredit(Accredit accredit) throws ManagerException;
	/**
	 * 根据用户id、岗位id和机构id删除用户岗位机构关系
	 * @param userId
	 * @param jobId
	 * @param orgId
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteUserjoborg(String userId,String jobId,String orgId)
	throws ManagerException;
	
	public boolean deleteUserjoborg(String userId,String jobId,String orgId, boolean sendEvent)
	throws ManagerException;
	/**
	 * 删除已有的用户角色
	 * @param ids
	 * @param roleid
	 * @return
	 * @throws ManagerException
	 */
	public boolean delAlotUserRole(String[] ids,String[] roleid)
	throws ManagerException;
		
	/**
	 * 根据用户id和用户组id删除用户组对象
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteUsergroup(String userId,String groupId) throws ManagerException;
	
	/**
	 * 删除已有的用户岗位
	 * @param ids
	 * @param roleid
	 * @return
	 * @throws ManagerException
	 */
	public boolean delAlotJobRole(String[] ids,String[] jobid,String orgId)
	throws ManagerException;
	/**
	 * 根据用户对象的属性和值来取用户实例来取得用户实体对象
	 * 
	 * @param propName
	 *            做为取用户实体对象的属性名，该属性名为用户实体对象的属性，如：userName
	 * @param value
	 *            与 propName 对应的属性值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public User getUser(String propName, String value) throws ManagerException;
	
	/**
	 * 根据userId得到user
	 * @param userId
	 * @return
	 * @throws ManagerException
	 */
	public User getUserById(String userId)  throws ManagerException;
	
	/**
	 * 根据userName得到user
	 * @param userName
	 * @return
	 * @throws ManagerException
	 */
	public User getUserByName(String userName)  throws ManagerException;

	/**
	 * 根据 HQL 语法取用户对象实例，例如：<br>
	 * getUser("from User user where user.userName='用户名'"); <br>
	 * 有关HSQL语法的更详细介绍请参考 Hibernate 的相关书籍
	 * 
	 * @param hql
	 *            Hibernate 的 HQL 语法，类似于 SQL 语法
	 * @return 通过 hql 语法取到的用户实体对象
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public User getUser(String hql) throws ManagerException;

	/**
	 * 根据用户对象的属性名和值取用户列表，例如：<br>
	 * User user = getUserList("userName","用户名称",false);
	 * 
	 * @param propName
	 *            用户对象实体的属性名，如：userName
	 * @param value
	 *            与 propName 对应的属性值
	 * @param isLike
	 *            用于描述在提取对象的过程中是否取大致相同的用户对象
	 * @return 取得的用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(String propName, String value, boolean isLike)
			throws ManagerException;

	/**
	 * 根据角色取所有相关的用户
	 * 
	 * @param role
	 *            做为取用户实体对象列表依据的角色实体对象
	 * @return 如果成功则返回用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Role role) throws ManagerException;

	/**
	 * 根据岗位取所有的用户
	 * 
	 * @param job
	 *            做为取用户实体对象列表的岗位实体对象，由于无法确定数据源所以传入该对<br>
	 *            象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果成功则返回用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Job job) throws ManagerException;

	/**
	 * 根据HQL语法取用户列表，例如：<br>
	 * getUserList("from User user where user.userName='用户名'"); <br>
	 * 有关HSQL语法的更详细介绍请参考 Hibernate 的相关书籍
	 * 
	 * @param hql
	 *            Hibernate 的 HQL 语法，类似于 SQL 语法
	 * @return 如果成功则返回用户对象实体列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	//public List getUserList(String hsql) throws ManagerException;
	
	public ListInfo getUserList(String sql,int offset,int maxItem) throws ManagerException;
	
	/**
	 * 获取机构下的人员,已经该人员的岗位信息 
	 * 合并了机构下的人员和岗位信息
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws ManagerException 
	 * UserManager.java
	 * @author: ge.tao
	 */
	public ListInfo getUserInfoList(String sql,int offset,int maxItem) throws ManagerException;
	
	public List getUserList(String sql) throws ManagerException;
	
	/**
	 * 根据用户Id和机构Id得Userjoborg列表
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws ManagerException
	 */
	public List getUserjoborgList(String userId,String orgId) throws ManagerException;

	/**
	 *更新Userjoborg的jobSn
	 */
	
	public boolean getUserSnList(String orgId,String jobId,int jobSn) throws ManagerException;
	/**
	 * 根据机构取所有的用户
	 * 
	 * @param org
	 *            做为取用户对象列表依据的机构对象
	 * @return 用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	
	
	
	public List getUserList(Organization org) throws ManagerException;
	/**
	 * 取离散用户列表
	 * @return
	 * @throws ManagerException
	 */
	public List getDicList() throws ManagerException;

	/**
	 * 根据组织取所有的用户
	 * 
	 * @param group
	 *            做为取用户列表依据的组对象，由于无法确定数据源所以传入该对象<br>
	 *            时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Group group) throws ManagerException;

	/**
	 * 根据操作取相关用户
	 * 
	 * @param oper
	 *            做为取用户列表的操作对象，由于无法确定数据源所以传入该对象时请尽<br>
	 *            可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Operation oper) throws ManagerException;

	/**
	 * 根据资源取用户列表
	 * 
	 * @param res
	 *            做为取用户列表依据的资源对象，由于无法确定数据源所以传入该<br>
	 *            对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Res res) throws ManagerException;

	/**
	 * 取所有用户
	 * 
	 * @return 用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList() throws ManagerException;

	/**
	 * 根据机构与角色取得用户列表
	 * 
	 * @param org
	 *            机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的<br>
	 *            完整性也就是它的所有属性都有相应的值
	 * @param role
	 *            角色实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象<br>
	 *            的完整性也就是它的所有属性都有相应的值
	 * @return 用户对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Organization org, Role role)
			throws ManagerException;

	/**
	 * 根据机构与岗位取得用户列表(王卓增加)
	 * 
	 * @param org
	 *            机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @param role
	 *            角色实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Organization org, Job job) throws ManagerException;

	/**
	 * 根据机构与岗位取得该机构下不属于该岗位的用户列表(增加)
	 * 
	 * @param org
	 *            机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @param role
	 *            角色实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 用户实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getUserList(Orgjob orgjob) throws ManagerException;

	/**
	 * 根据用户名称取与之相关的授权列表
	 * 
	 * @return 委托授权实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getAccreditList(String userName) throws ManagerException;

	/**
	 * 根据用户名取该用户的所有临时授权的列表
	 * 
	 * @param userName
	 *            做为取临时授权列表依据的用户名
	 * @return 临时授权列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getTempaccredit(String userName) throws ManagerException;
	/**
	 * 获取机构相应岗位下的用户信息
	 * @param orgid
	 * @param jobid
	 * @return java.util.List<User>
	 * @throws ManagerException
	 */
	public List getUserList(String orgid,String jobid) throws ManagerException;
	
	/**
	 * 获取机构相应岗位集合中的用户信息，二维数组中存放orgid和jobid信息
	 * @param orgid
	 * @param jobid
	 * @return java.util.List<User>
	 * @throws ManagerException
	 */
	public List getUserList(String[][] orgjobs) throws ManagerException;
	/**
	 * 物价局项目（根据字典类型取会员相关属性列表）
	 * @return
	 * @throws ManagerException
	 */
	public List getmemberTypeList(String typeid) throws ManagerException;

	/**
	 * 根据用户名判断该用户是否存在
	 * 
	 * @param user
	 *            用户实体对象
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean isUserExist(User user) throws ManagerException;

	/**
	 * 判断指定的 Userrole 对象是否存在于数据源中
	 * 
	 * @param userrole
	 *            用户角色关系实体对象
	 * @return 如果数据源中存在 userrole 关系实体对象则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean isUserroleExist(Userrole userrole) throws ManagerException;

	/**
	 * 判断指定的用户岗位机构是否存在
	 * 
	 * @param userjoborg
	 *            用户岗位机构关系实体对象
	 * @return 如果数据源中存在 userjoborg 关系实体对象则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean isUserjoborgExist(Userjoborg userjoborg)
			throws ManagerException;

	/**
	 * 装载当前 User 对象实例所关联的其它对象到集合中，如：userattr -> userattrSet
	 * 
	 * @param userId
	 *            做为取用户实体对象（包含指定对象集合）的用户ID
	 * @param associated
	 *            需要装载的用户实体中的集合名称，可以直接使用 UserManager.ASSOCIATED_XXX 常量名来指定。<br>
	 *            如：UserManager.ASSOCIATED_USERJOBORGSET
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public User loadAssociatedSet(String userId, String associated)
			throws ManagerException;
	

	
	
	/**
	 * 用户权限复制
	 * @param userId
	 * @param userid
	 * @return
	 * @throws ManagerException
	 */
	public boolean userResCopy(String userId,String[] userid)
	throws ManagerException;
	/**
	 * 新增用户，存储用户、机构和岗位的关系
	 * @param user 用户对象
	 * @param orgId	机构id
	 * @param jobId 岗位id
	 * @throws ManagerException
	 */
	public void creatorUser(User user,String orgId,String jobId) throws ManagerException;
	
	
	/**
	 * 通过机构id获取机构下隶属的用户列表
	 * added by biaoping.yin on 2007.5.26
	 * @param orgid
	 * @return
	 */
	public List getOrgUserList(String orgid) throws ManagerException;
	
	/**
	 * 通过机构id获取机构下隶属的用户列表,如果不是系统管理员将不出现admin
	 * @param orgid
	 * @param userId
	 * @return
	 * @throws ManagerException
	 */
	public List getOrgUserList(String orgid, String userId) throws ManagerException ;
	
	/**
	 * 获取当前角色的用户
	 * @param roleid
	 * @return
	 * @throws ManagerException
	 */
	public List getUsersListOfRole(String roleid) throws ManagerException;
	
	/**
	 * 获取当前机构当前角色的用户
	 * @param roleid
	 * @param orgId
	 * @return
	 * @throws ManagerException
	 */
	public List getUsersListOfRoleInOrg(String roleid, String orgId) throws ManagerException;
	
	
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
	public String storeAllUserSnJobOrg(String orgId, String jobId,
			String jobSn, String[] userId) throws Exception ;
	
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
	public void storeOrgUserOrder(String orgId, String[] userId) throws Exception;
	
	/**
	 * 删除人员岗位和机构的关系，用户管理隶属机构中的调入
	 */
	public String deleteUJOAjax(String uid, String[] jobIds,String orgId)throws Exception;
	
	/**
	 * 获取机构用户排序号，如果用户在机构下已经存在，则保持用户的排序号不变
	 * 如果不存在，则将本机构下最大的用户排序号加1返回
	 * @param orgid
	 * @param userid
	 * @return
	 */
	public int getUserSN(String orgid,String userid)throws Exception;
	
	public void storeUJOAjax_batch(String[] ids, String[] jobid,String orgid);
	
	public void deleteUJOAjax_batch(String[] ids, String[] jobid, String orgId) throws ManagerException;
	
	public String deleteUserOrgJob(Integer uid,String orgId,String[] jobid);
	public String storeUJOAjax(String uid, String[] jobIds,String orgId);
	
	/**隶属岗位－－保存用户岗位机构的关系
	 * store and delete userOrgJob by ajax the reference page is refresh auto (hongyu.deng)
	 * @param uid
	 * @param orgId
	 * @param jobid
	 * @return
	 */
	public String storeUserOrgJob(Integer uid,String orgId,String[] jobid);
	
	/**
	 * 更改用户的主机构，所有取消用户岗位的方法在处理完后需要调用本方法进行检测，并进行相应的主机构处理
	 * @param id
	 */
	public void resetUserMainOrg(String userid,String oldmainorg);
	
	
	/**
	 * 批量用户加入机构
	 * @param ids
	 * @param jobid
	 * @param orgid
	 * @throws ManagerException
	 * @throws SPIException 
	 */
	public void storeAlotUserOrg(String[] ids, String[] jobids,String orgid) throws ManagerException, SPIException ;
	
	
	/**
	 * 批量用户移出机构
	 * @param ids
	 * @param jobid
	 * @param orgId
	 * @throws ManagerException
	 */
	public void delAlotUserOrg(String[] ids, String[] jobids, String orgId) throws ManagerException;
	
	/**
	 * 添加用户
	 * @param user
	 * @return 返回添加用户的userId
	 * @throws ManagerException
	 * @author gao.tang
	 */
	public String addUser(User user) throws ManagerException ;
	public String addUser(User user,boolean isEvent) throws ManagerException ;
	
	/**
	 * 修改用户信息
	 * @param user
	 * @return
	 * @throws ManagerException
	 * @author: gao.tang
	 */
	public boolean updateUser(User user) throws ManagerException ;
	
	/**
	 *	更新密码 
	 * @param user
	 * @return
	 * @throws ManagerException
	 * @author: gao.tang
	 */
	public boolean updateUserPassword(User user) throws ManagerException ; 
	
	/**
	 * 从其他机构下调入用户
	 * @param userIds 用户ID
	 * @param orgId 机构ID
	 * @param classType 判断添加的机构是离散用户还是其他机构下的用户
	 * @return
	 * @throws ManagerException
	 * @author gao.tang
	 */
	public boolean addUserOrg(String[] userIds, String orgId, String classType) throws ManagerException;
	
	/**
	 * 保存用户 用户组关系
	 * @param usergroup
	 * @return
	 * @throws ManagerException 
	 * UserManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean addUsergroup(Integer userid, String[] groupid) throws ManagerException ;
	
	/**
	 * 删除用户,用户组关系
	 * @param userid
	 * @param groupid
	 * @return
	 * @throws ManagerException 
	 * UserManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean deleteUsergroup(Integer userid, String[] groupids) throws ManagerException;
	
	/**
	 * 保存用户 角色
	 * @param userId
	 * @param roleIds
	 * @throws ManagerException 
	 * UserManagerImpl.java
	 * @author: ge.tao
	 */
	public void addUserrole(String userId, String[] roleIds)throws ManagerException ;
	
	/**
	 * 保存用户 角色
	 * @param userId
	 * @param roleIds
	 * @param currentUserId
	 * @throws ManagerException 
	 * UserManager.java
	 * @author: ge.tao
	 */
	public void addUserrole(String userId, String[] roleIds, String currentUserId)throws ManagerException ;
	
	/**
	 * 删除用户 角色
	 * @param userId
	 * @param roleIds
	 * @throws ManagerException 
	 * UserManagerImpl.java
	 * @author: ge.tao
	 */
	public void deleteUserrole(String userId, String[] roleIds)throws ManagerException;
	
	/**
	 * 删除用户 角色
	 * @param userId
	 * @param roleIds
	 * @throws ManagerException 
	 * UserManagerImpl.java
	 * @author: ge.tao
	 */
	public void deleteUserrole(String userId, String[] roleIds, String roleTypes)throws ManagerException;
	
	/**
	 * 从机构中删除，且当用户只存在该机构下时删除用户所有资源 gao.tang 2007.11.23
	 * @param user
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteUserRes(User user) throws ManagerException ; 
	
	/**
	 * 批量插入用户主管机构与用户机构岗位
	 * @param userIds
	 * @param orgIds
	 * @param isInsert 为true时插入主机构关系
	 * @return
	 * @throws ManagerException
	 */
	public boolean storeBatchUserOrg(String[] userIds, String[] orgIds, boolean isInsert) throws ManagerException;
	
	/**
	 * 删除离散用户与主机构的关系表
	 * @return
	 */
	public boolean deleteDisperseOrguser();
	
	/**
	 * 根据用户id判断是否为税管员
	 * @param userId
	 * @return
	 */
	public boolean isTaxmanager(String userId) throws ManagerException ;
	
	/**
	 * 判断机构下是否有用户
	 * @param org
	 * @return
	 * @throws ManagerException
	 */
	public boolean isContainUser(Organization org) throws ManagerException ;
	
	/**
	 * 删除 和 调动 用户的时候, 判断用户的机构信息, 决定是否能执行 删除和调动操作. 
	 * @param userId
	 * @return 用户机构信息, 如果返回为空, 可以删除 和 调动 用户
	 * UserManager.java
	 * @author: ge.tao
	 * @date 2008-01-25
	 */
	public String userOrgInfo(AccessControl control, String userId);
	
	/**
	 * 根据用户ID得到用户主机构ID
	 * @param userId
	 * @return
	 */
	public String getUserMainOrgId(String userId);
	
	/**
	 * 得到有权限删除的用户名userName
	 * @param curUserId 当前用户ID
	 * @param selectUserName 选中删除的用户
	 * @return 用户名数组
	 */
	public String[] getCuruserAdministrableDeleteUser(String curUserId,String[] selectUserNames);
	/**
	 * 获取机构的管理员列表
	 * @param org_id
	 * @return
	 * @throws ManagerException
	 */
	public List<User> getOrgManager(String org_id) throws ManagerException;
}