package com.frameworkset.platform.sysmgrcore.manager;

import java.util.List;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.util.ListInfo;


/**
 * 项目：SysMgrCore <br>
 * 描述：岗位管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 王卓
 */
public interface JobManager extends Provider,java.io.Serializable {
	/**
	 * TYPE_ORGJOB
	 */
	public static final int TYPE_ORGJOB = 1;

	/**
	 * TYPE_USERJOBORG
	 */
	public static final int TYPE_USERJOBORG = 2;

	/**
	 * 岗位所对应的“用户岗位机构”关系对象集合的名称 userjoborgSet
	 */
	public static final String ASSOCIATED_USERJOBORGSET = "userjoborgSet";

	/**
	 * 岗位所对应的“机构岗位”关系对象集合的名称 orgjobSet
	 */
	public static final String ASSOCIATED_ORGJOBSET = "orgjobSet";

	/**
	 * 存储岗位对象实例
	 * 
	 * @param job
	 *         需要存储到数据源中的岗位实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean storeJob(Job job) throws ManagerException;

	/**
	 * 存储岗位前先根据指定的属性名（propName）和属性值（value）来查找数据源中的记录，如果存在则更新该记录 <br>
	 * 否则插入一条新的记录
	 * 
	
	 * @param propName
	 *            对应于 Job 对象中的属性，如：orgName
	 * @param value
	 *            与 propName 对应的属性值
	 * @return
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public Job getJob(String propName, String value) throws ManagerException;

	/**
	 * 根据岗位对象的属性名和值取岗位列表，例如：<br>
	 * Job job = getJobList("jobName","岗位名称",false);
	 * 
	 * @param propName
	 *            岗位对象实体的属性名，如：jobName
	 * @param value
	 *            与 propName 对应的属性值
	 * @param isLike
	 *            用于描述在提取对象的过程中是否取大致相同的岗位对象
	 * @return 取得的岗位实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getJobList(String propName, String value, boolean isLike)
			throws ManagerException;
	
	public ListInfo getJobList(String hql,long offset,int maxsize)
			throws ManagerException;

	/**
	 * 取所有岗位
	 * 
	 * @return 岗位实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getJobList() throws ManagerException;
	public ListInfo selectjoblist(String jobname,String jobnumber,long offset,int maxsize) throws ManagerException;

	/**
	 * 根据用户取岗位列表
	 * 
	 * @param user
	 *            做为取岗位实体对象列表的用户实体对象，由于无法确定数据源所以传入该对<br>
	 *            象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果成功则返回岗位实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getJobList(User user) throws ManagerException;

	/**
	 * 根据机构取岗位列表
	 * 
	 
	 * @param type
	 *            表示将从那一个表中取岗位列表，有关系表：TD_SM_ORGJOB、TD_SM_USERJOBORG， 可以直接使用常量
	 *            JobManager.TYPE_XXXX 。
	 * @return
	 * @throws ManagerException
	 * 			在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getJobList(Organization org, int type) throws ManagerException;

	/**
	 * 根据HQL语法取岗位列表，例如：<br>
	 * getJobList("from Job job where job.jobName='岗位名'"); <br>
	 * 有关HSQL语法的更详细介绍请参考 Hibernate 的相关书籍
	 * 
	 * @param hql
	 *            Hibernate 的 HQL 语法，类似于 SQL 语法
	 * @return 如果成功则返回岗位对象实体列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getJobList(String hql) throws ManagerException;

	/**
	 * 根据机构取所有的岗位
	 * 
	 * @param org
	 *            做为取岗位实体对象列表的机构实体对象，由于无法确定数据源所以传入该对<br>
	 *            象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果成功则返回岗位实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getJobList(Organization org) throws ManagerException;

	/**
	 * 根据机构与用户取得岗位列表
	 * 
	 * @param org
	 *            机构实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @param user
	 *            用户实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 岗位实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getJobList(Organization org, User user) throws ManagerException;

	/**
	 * 删除岗位实例同时将连带删除与该实例有关的所有实例，如：岗位与机构关系实例。
	 * 
	 * @param job
	 *         需要删除的岗位对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteJob(Job job) throws ManagerException;

	/**
	 * 根据岗位ID删除指定的岗位
	 * 
	 * @param jobId
	 *         需要删除的岗位实体对象的岗位ID
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteJob(String jobId) throws ManagerException;

	/**
	 * 删除指定的用户岗位机构对象实例
	 * 
	 * @param userjoborg
	 *         需要删除的用户岗位机构关系对象实体
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException;

	/**
	 * 删除岗位的机构岗位关系实体对象
	 * 
	 * @param job
	 *            做为删除依据的岗位实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteOrgjob(Job job) throws ManagerException;

	/**
	 * 根据岗位名判断该岗位是否存在
	 * 
	 * @param job
	 *            岗位实体对象
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean isJobExist(String jobName) throws ManagerException;
	
	/**
	 * 根据岗位编号判断该岗位是否存在
	 * 
	 
	 */
	
	public boolean isJobNumber(String jobNumber) throws ManagerException;
	
	public boolean isJobExistNumber(String jobId,String jobNumber) throws ManagerException;

	/**
	 * 刷新指定岗位的状态以及该岗位与其它实体的关联
	 * 
	 * @param user
	 */
	public void refresh(Job job) throws ManagerException;

	/**
	 * 装载当前 Job 对象实例所关联的其它对象到集合中
	 * 
	 * @param jobId
	 *            做为取岗位实体对象（包含指定对象集合）的岗位ID
	 * @param associated
	 *            需要装载的岗位实体中的集合名称，可以直接使用 JobManager.ASSOCIATED_XXX 常量名来指定。<br>
	 *            如：JobManager.ASSOCIATED_USERJOBORGSET
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public Job loadAssociatedSet(String jobId, String associated)
			throws ManagerException;

	/**
	 * 判断机构下是否有岗位
	 * @param org
	 * @return
	 * @throws ManagerException
	 */
	public boolean isContainJob(Organization org) throws ManagerException;
	/**
	 * 删除岗位的角色岗位关系实体对象--gao.tang  2007-10-18
	 * 
	 * @param jobId
	 * 			做为删除依据的岗位id
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 * 			在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteJobroleByJobId(String jobId, String orgId) throws ManagerException;
	
	public boolean deleteJobroleByJobId(String jobId, String orgId,boolean sendevent) throws ManagerException;
	
	/**
	 * 添加角色岗位关系--gao.tang  2007-10-18
	 * 
	 * @param jobId
	 * 			岗位id
	 * @pare roleids  String[] 角色ids
	 * @return 如果成功则返回 true 否则返回 false
	 * @throws ManagerException
	 * 			在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean addJobroleMap(String jobId, String orgId, String roleids[]) throws ManagerException;
	
	public boolean addJobroleMap(String jobId, String orgId, String roleids[],boolean sendevent) throws ManagerException;
	
	/**
	 * 返回根据岗位ID查询出的机构记录--gao.tang 2007.10.24
	 * 
	 * @param jobId
	 * @param orgId
	 * @param orgName
	 * @param offset
	 * @param maxPagesize
	 * @return ListInfo
	 * @throws ManagerException
	 */
	public ListInfo getJobOrgList(String jobId, String orgId, String orgName,
			long offset, int maxPagesize) throws ManagerException ;
	
	/**
	 *  和getJobOrgList(String jobId, String orgId, String orgName,
			long offset, int maxPagesize)功能方法一样,不过增加了对机构显业名称的查询条件
	 * @param jobId
	 * @param orgId
	 * @param orgShowName
	 * @param offset
	 * @param maxPagesize
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getJobOrgShowList(String jobId, String orgId, String orgShowName,
			long offset, int maxPagesize) throws ManagerException ;
	
	
	public Job getJobById(String jobId) throws ManagerException ;
	
	public Job getJobByName(String jobName) throws ManagerException ;
	
	/**
	 * 根据用户ID与岗位ID得到用户所创建的岗位，用户对自己所创建的岗位有所有的权限
	 * @param userId
	 * @param jobId
	 * @return
	 * @throws ManagerException
	 */
	public Job getByCreatorJobId(String userId, String jobId) throws ManagerException ;
	
	/**
	 * 添加岗位
	 * @param job
	 * @return
	 * @throws ManagerException
	 */
	public boolean saveJob(Job job) throws ManagerException ;
	
	/**
	 * 修改岗位信息
	 * @param job
	 * @return
	 * @throws ManagerException
	 * @author gao.tang 
	 */
	public boolean updateJob(Job job) throws ManagerException ;
}
