package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.frameworkset.spi.Provider;

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
import com.frameworkset.common.tag.pager.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：机构管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 
 */
public interface OrgManager extends Provider, Serializable {

	/**
	 * 机构所对应的“用户岗位机构”关系对象集合的名称 userjoborgSet
	 */
	public static final String ASSOCIATED_USERJOBORGSET = "userjoborgSet";

	/**
	 * 机构所对应的“机构岗位”关系对象集合的名称 orgjobSet
	 */
	public static final String ASSOCIATED_ORGJOBSET = "orgjobSet";

	/**
	 * 机构所对应的“机构组”关系对象集合的名称 orggroupSet
	 */
	public static final String ASSOCIATED_ORGGROUPSET = "orggroupSet";
	
	/**
	 * 是业务部门
	 */
	public static final String ISBUSINESSDEPARTMENT = "0";
	
	/**
	 * 不是业务部门
	 */
	public static final String NOTBUSINESSDEPARTMENT = "1";
	
	/**
	 * 1：省级 省局
	 */
	public static final String PROVINCEDEPARTMENT = "1";
	
	/**
	 * 2：市州级 市州局
	 */
	public static final String CITYDEPARTMENT = "2";
	
	/**
	 * 3：县区级 县区局
	 */
	public static final String COUNTYDEPARTMENT = "3";
	
	/**
	 * 4：科所级 科所
	 */
	public static final String MINIDEPARTMENT = "4";
	
	/**
	 * 0：科室级 科室
	 */
	public static final String OFFICEDEPARTMENT = "0";

	/**
	 * 存储机构对象实例
	 * 
	 * @param org
	 *            需要存储到数据源中的机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 * 此函数用来更新机构基本信息
	 */
	public boolean storeOrg(Organization org) throws ManagerException;
	
	/**
	 * 与storeOrg对应，用来处理新增的情况
	 * 200711091009
	 * 危达
	 */
	public boolean insertOrg(Organization org) throws ManagerException;

	/**
	 * 存储机构前先根据指定的属性名（propName）和属性值（value）来查找数据源中的记录，如果存在则更新该记录 <br>
	 * 否则插入一条新的记录
	 * 
	 * @param org
	 *            Organization
	 *            对象实例，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @param propName
	 *            对应于 Organization 对象中的属性，如：orgName
	 * @param value
	 *            与 propName 对应的属性值
	 * @return
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeOrg(Organization org, String propName, String value)
			throws ManagerException;

	/**
	 * 存储机构岗位对象实例,方法中已经判断机构和岗位的关系是否存在,如果存在就是更新,否则就是新增
	 * 
	 * @param orgjob
	 *            需要存储到数据源中的机构岗位关系实体对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeOrgjob(Orgjob orgjob) throws ManagerException;
	
	/**
	 * 递归存储子机构岗位对象实例
	 * 
	 * @param orgjob
	 *            需要存储到数据源中的机构岗位关系实体对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeSubOrgjob(Orgjob orgjob) throws ManagerException;
	
	/**
	 * 递归存储子机构岗位对象实例
	 * 
	 * @param orgjob
	 *            需要存储到数据源中的机构岗位关系实体对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean addSubOrgjob(String orgid,String[] jobids) throws ManagerException;

	/**
	 * 存储机构组对象实例
	 * 
	 * @param orggroup
	 *            需要存储到数据源中的机构组关系实体对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeOrggroup(Orggroup orggroup) throws ManagerException;
	
	/**
	 * 根据当前机构id和页面传来的子机构ids进行机构的排序
	 * @param String orgId 父机构id
	 * @param String[] orggroup 子机构ids
	 */
	public boolean sortOrg(String orgId,String[] sonOrgIds) throws ManagerException;

	/**
	 * 根据机构实体的属性和值取相关的对象实例，如：<br>
	 * Organization org = getOrgByName("机构名称");
	 * 
	 * @param propName
	 *            做为取机构实体对象的机构属性，如：“orgName”
	 * @param value
	 *            与propName对应的属性值
	 * @return 机构实体对象
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
//	public Organization getOrg(String propName, String value)
//			throws ManagerException;

	/**
	 * 根据用户取机构列表
	 * 
	 * @param user
	 *            用户实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 机构实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getOrgList(User user) throws ManagerException;

	/**
	 * 根据岗位取机构列表
	 * 
	 * @param job
	 *            岗位实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 机构实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getOrgList(Job job) throws ManagerException;

	/**
	 * 根据组取机构列表
	 * 
	 * @param group
	 *            做为取机构列表依据的组实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 机构实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getOrgList(Group group) throws ManagerException;

	/**
	 * 根据 Hibernate 的 HQL 语言取机构列表，例如：<br>
	 * getOrgList("from Organization org where org.orgName='机构名称'"); <br>
	 * 有关HSQL语法的更详细介绍请参考 Hibernate 的相关书籍
	 * 
	 * @param hql
	 *            Hibernate 的 HQL 语法，类似于 SQL 语法
	 * @return
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getOrgList(String hql) throws ManagerException;
	
	/**
	 * 取所有机构
	 * 
	 * @return 机构实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getOrgList() throws ManagerException;

	/**
	 * 删除机构对象实例
	 * 
	 * @param org
	 *            需要删除的机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteOrg(Organization org) throws ManagerException;

	/**
	 * 根据机构id删除指定机构及其子机构
	 * 
	 * @param orgId
	 *            需要删除的机构实体对象的机构ID
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	//public boolean deleteOrg(String orgId) throws ManagerException;

	/**
	 * 删除指定的机构岗位对象实例
	 * 
	 * @param orgjob
	 *            机构岗位关系实体对象
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteOrgjob(Orgjob orgjob) throws ManagerException;

	/**
	 * 删除指定的机构与所有的关系
	 * 
	 * @param org
	 *            做为删除依据的机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteOrgjob(Organization org) throws ManagerException;

	/**
	 * 删除指定的机构组对象实例
	 * 
	 * @param orggroup
	 *            需要删除的机构组关系对象实体
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteOrggroup(Orggroup orggroup) throws ManagerException;

	/**
	 * 根据指定机构的ID装载它相关的其它子对象并返回该机构对象实例
	 * 
	 * @param orgId
	 *            做为取机构实体对象（包含指定对象集合）的用户ID
	 * @param associated
	 *            需要装载的机构实体中的集合名称，可以直接使用 OrgManager.ASSOCIATED_XXX 常量名来指定。<br>
	 *            如：OrgManager.ASSOCIATED_USERJOBORGSET
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public Organization loadAssociatedSet(String orgId, String associated)
			throws ManagerException;

	/**
	 * 检查指定的机构是否包含子机构
	 * 
	 * @param org
	 *            需要检查是否包含子机构的机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果包含了子机构则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean isContainChildOrg(Organization org) throws ManagerException;

	/**
	 * 判断指定的机构下是否包含用户
	 * 
	 * @param org
	 *            需要检查是否包含用户的机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果包含了用户则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean isContainUser(Organization org) throws ManagerException;

	/**
	 * 取指定机构所包含的子机构列表
	 * 
	 * @param org
	 *            指定的子机构实例
	 * @param isRecursion
	 *            是否以递归的方式取所有的子机构（包括子机构的子机构）
	 * @return 子机构列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getChildOrgList(Organization org, boolean isRecursion)
			throws ManagerException;
	
	/**
	 * 树状的递归显示的所有机构
	 * @param orgId
	 * @return
	 * @throws ManagerException 
	 * OrgManager.java
	 * @author: ge.tao
	 */
	public List getRecursionAllOrgList(String orgId) throws ManagerException;

	/**
	 * 取指定机构所包含的子机构列表（不递归取所有子机构）
	 * 
	 * @param org
	 *            需要取子机构列表的机构实体对象
	 * @return 子机构实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getChildOrgList(Organization org) throws ManagerException;



	/**
	 * 根据机构名判断该机构是否存在
	 * 
	 * @param orgName
	 *            需要检查是否存在于数据源中的机构名
	 * @return 如果存在则返回 true 否则返回 false
	 */
	public boolean isOrgExist(String orgName) throws ManagerException;

	/**
	 * 根据角色取机构列表
	 * 
	 * @param role
	 *            做为取机构列表依据的角色实体对象
	 * @return 取机构列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getOrgList(Role role) throws ManagerException;

	/**
	 * 保存指定的机构组对象实例
	 * 
	 * @param orgrole
	 *            需要存储的机构角色关系实体对象
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeOrgrole(Orgrole orgrole) throws ManagerException;

	/**
	 * 删除指定的机构组对象实例
	 * 
	 * @param orgrole
	 *            需要删除的机构角色实体对象
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteOrgrole(Orgrole orgrole) throws ManagerException;
	/**
	 * add by hongyu.deng 
	 * 		  	 为了提高系统性能提供一条sql语句从数据库中取出用户能看见的所有机构树资源
	 * 			 该资源通过列表分页取出
	 * @param userId
	 * @param userAccount 判断该用户是否是administrator
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getUserCanWriteAndReadOrgList(String userId,String userAccount,int offSet, int pageItemsize,String condition)throws ManagerException;

	/**
	 * 删除指定的机构组对象实例
	 * 
	 * @param org
	 *            需要删除的机构角色中的机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteOrgrole(Organization org) throws ManagerException;
	
	/**
	 * 根据当前登录用户名,提供该用户所在单位的主办处室审核领导用户帐户及该主办处室的级次
	 * @param group
	 * @return
	 * @throws ManagerException
	 */
	public ChargeOrg getSatrapListByUserAccount(String userAccount) throws ManagerException;
	
	/**
	 * 根据当前登录用户ID,提供该用户所在单位的主办处室审核领导用户帐户及该主办处室的级次
	 * @param group
	 * @return
	 * @throws ManagerException
	 */
	public ChargeOrg getSatrapListByUserID(String userID) throws ManagerException;
	
	/**
	 * 获取用户所属的单位，不包含兼职单位
	 * @param userAccount
	 * @return
	 * @throws ManagerException
	 */
	public Organization getMainOrganizationOfUser(String userAccount) throws ManagerException;
	
	/**
	 * 获取用户的兼职单位列表，不包括所属单位
	 * @param userAccount
	 * @return List<Organization>
	 * @throws ManagerException
	 */
	public List getSecondOrganizationsOfUser(String userAccount) throws ManagerException;
	
	/**
	 * 设置用户的隶属单位（所在主单位）
	 * @param userID
	 * @param orgID
	 * @throws ManagerException
	 */
	public void addMainOrgnazitionOfUser(String userID,String orgID)  throws ManagerException;
	
	/**
	 * 删除用户的隶属单位（所在主单位）
	 * @param userID
	 * @throws ManagerException
	 */
	public void deleteMainOrgnazitionOfUser(String userID)  throws ManagerException;
	/**
	 * 根据当前用户所在机构，取该机构主管机构和人员信息
	 * @param group
	 * @return
	 * @throws ManagerException
	 */
	public ChargeOrg getSatrapUpByOrgID(String orgId) throws ManagerException;
	/**
	 * 根据预算单位id和对应的级次取用户
	 * @param group
	 * @return
	 * @throws ManagerException
	 */
	public ChargeOrg getSatrapByUser(String layer,String orgId) throws ManagerException;
	/**
	 * 根据预算单位id，操作id（审核权限）和资源所属类型取上级主管处室对预算单位有审核权限的人
	 * @param orgid
	 * @param chargeOrgid
	 * @param opID
	 * @return
	 * @throws ManagerException
	 */
	public List getHasPemissionOfUsers(String orgid,String opID,String restype) throws ManagerException;
	
	/**
	 * 根据当前机构ID和岗位名称得岗位下所有用户
	 * @param orgId
	 * @param jobName
	 * @return
	 * @throws ManagerException
	 */
	public List getOrgByUser(String orgId,String jobName) throws ManagerException;
	
	/**
	 * 判断机构是否设置了相应的岗位
	 * @param orgid 机构id
	 * @param jobid 岗位id
	 * @return 如果机构授予了岗位job，则返回true，否则返回false
	 * @throws ManagerException
	 */
	public boolean orgHasJob(String orgid,String jobid) throws ManagerException;
	/**
	 * 获取用户的兼职单位列表，不包括所属单位
	 * @param userAccount
	 * @return List<Organization>
	 * @throws ManagerException
	 */
	public String getSecondOrganizations(String userAccount)throws ManagerException;
	
	
	/**
	 * 根据用户id获取用户的机构列表
	 * @param userid
	 * @return
	 * @throws ManagerException
	 */
	public List getOrgListOfUser(String  userid) throws ManagerException ;
	
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
	public boolean deleteOrgjob(String orgids, String[] jobids) throws ManagerException ;
	
	/**
	 * 删除子机构下的岗位
	 * job_id = 1 的在岗的岗位 不能删除
	 * @param orgid
	 * @param jobid
	 * @return
	 * @throws ManagerException 
	 * OrgManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean deleteSubOrgjob(String orgids, String[] jobids) throws ManagerException ;
	
	
	public List getSubOrgList(String orgId) throws ManagerException ;
	
	
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
	public boolean addOrgjob(String org_id,String[] job_ids,String job_sn) throws ManagerException;
	
	/**
	 * 
	 * @param orgId
	 * @param roleIds
	 * @param flag 0=不递归删除子机构角色; 1=递归删除子机构角色
	 * @return
	 * @throws ManagerException 
	 * OrgManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean deleteOrgrole(String orgId, String[] roleIds,String flag) throws ManagerException;
	
	/**
	 * 保存机构角色
	 * @param orgId
	 * @param roleIds
	 * @param flag 0=不递归保存到子机构;1=递归保存到子机构
	 * @return 
	 * OrgManagerImpl.java
	 * @author: ge.tao
	 */
	public boolean storeOrgRole(String orgId, String[] roleIds,String flag);
	
	/**
	 * 根据机构ID,判断该机构是否业务部门
	 * @param orgId
	 * @return boolean true=是业务部门;false=不是业务部门
	 * OrgManager.java
	 * @author: ge.tao
	 */
	public boolean isBussinessDepartment(String orgId);
	
	/**
	 * 根据用户ID,获取用户的县区局
	 * 机构类型的数据字典,
	 * 字典数据是保存在 TD_SM_DICTDATA 中
	 * 而且,字典的数据如下
	 * 省局
	 * 市州局
	 * 县区局
	 * 科所
	 * @param userId
	 * @return Organization 县区局机构对象
	 * OrgManager.java
	 * @author: ge.tao
	 */
	public Organization userBelongsCountyDepartment(String userId);
	
	/**
	 * 根据用户ID,获取用户的市州局
	 * 机构类型的数据字典,
	 * 字典数据是保存在 TD_SM_DICTDATA 中
	 * 而且,字典的数据如下
	 * 省局
	 * 市州局
	 * 县区局
	 * 科所
	 * @param userId
	 * @return Organization 市州局机构对象
	 * OrgManager.java
	 * @author: ge.tao
	 */
	public Organization userBelongsCityDepartment(String userId);
	
	/**
	 * 根据用户ID,获取用户的省局
	 * 机构类型的数据字典,
	 * 字典数据是保存在 TD_SM_DICTDATA 中
	 * 而且,字典的数据如下
	 * 省局
	 * 市州局
	 * 县区局
	 * 科所
	 * @param userId
	 * @return Organization 市州局机构对象
	 * OrgManager.java
	 * @author: ge.tao
	 */
	public Organization userBelongsProvinceDepartment(String userId);
	
	
	
	
	/**
	 * 根据用户ID,获取用户所属的业务部门列表
	 * @param userId
	 * @return list<Organization> 业务部门机构对象列表
	 * OrgManager.java
	 * @author: ge.tao
	 */
	public List userBelongsBussinessDepartment(String userId);
	
	/**
	 * 根据用户ID,获取用户管理的机构列表
	 * @param userId
	 * @return List<Orgnization>
	 * OrgManager.java
	 * @author: ge.tao
	 */
	public List getUserManageOrgs(String userId);
	
	public Organization getOrgById(String orgId) throws ManagerException ;
	
	public Organization getOrgByName(String orgName) throws ManagerException ;
	
	/**
	 * 根据字段名获得机构
	 * */
	public Organization getOrg(String propName, String value)throws ManagerException ;
	
	/**
	 * 得到所有机构包括自机构
	 * @return
	 * @throws ManagerException
	 */
	public List getAllOrgList(String orgId) throws ManagerException ;
	
	/**
	 * 根据机构ID得到机构的所有父节点，包括机构本身
	 * @param orgId
	 * @param isFather  是否递归取得父节点（true为是，false递归取得子节点）
	 * @return 返回机构ID数组
	 * @throws ManagerException
	 */
	public String[] getFatherOrg(String orgId, boolean isFather) throws ManagerException ;
	
	
	
	/**
	 * 获取机构所属的科室机构
	 * @param orgId
	 * @return Organization
	 * @author 危达
	 */
	public Organization orgBelongsOfficeDepartment(String orgId);
	
	/**
	 * 获取机构所属的科所机构
	 * @param orgId
	 * @return Organization
	 */
	public Organization orgBelongsMiniDepartment(String orgId);

	/**
	 * 获取机构所属的县区机构
	 * @param orgId
	 * @return Organization
	 */
	public Organization orgBelongsCountryDepartment(String orgId);
	
	/**
	 * 获取机构所属的市州机构
	 * @param orgId
	 * @return Organization
	 */
	public Organization orgBelongsCityDepartment(String orgId);
	
	/**
	 * 获取机构所属的省局机构
	 * @param orgId
	 * @return Organization
	 */
	public Organization orgBelongsProvinceDepartment(String orgId);
	
	public List getOrgListOfRole(String roleid) throws ManagerException;
	
	public List getOrgListBySql(String sql) throws ManagerException;
	
	public boolean storeOrgRole(String orgIds[], String[] roleIds);
	
	public boolean deleteOrgrole(String orgIds[], String[] roleIds) throws ManagerException;
	
	/**
	 * 将用户调离机构，调入到其他机构时，删除用户关联的td_sm_orguser与td_sm_userjoborg表
	 * @param orgId
	 * @param userIds
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteOrg_UserJob(String orgId, String userIds[]) throws ManagerException ;
	
	/**
	 * 得到用户可管理的所有机构
	 * @param userId
	 * @return
	 * @throws ManagerException
	 */
	public List getUserAllManagerOrg(String userId)  throws ManagerException ;
	
	/**
	 * 判断用户是否是机构下的管理员
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws ManagerException
	 */
	public boolean userIsOrgManager(String userId,String orgId) throws ManagerException ;
	
	/**
	 * 根据用户ID得到可管理得机构ID得到可管理机构的子机构包括自己
	 * @param orgId
	 * @param isRoleAdmin 用户是否拥有超级管理员角色
	 * @return
	 */
	public Map getSubOrgId(String userId, boolean isRoleAdmin);
	
	/**
	 * 根据用户ID得到可管理得机构ID得到可管理机构的父机构ID，不包括自己
	 * @param orgId
	 * @return
	 */
	public Map getParentOrgId(String userId);
	
	/**
	 * 判断用户是否是所选部门的部门管理员
	 * @param curOrgId
	 * @param userId
	 * @return
	 */
	public boolean isCurOrgManager(String curOrgId, String userId);
	
	/**
	 * 机构转移
	 * @param orgId			需要转移的机构ID
	 * @param tranToOrgId	要转移到的机构ID
	 * @return
	 */
	public boolean tranOrg(String orgId,String tranToOrgId);
	

	
	
}
