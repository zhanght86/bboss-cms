package com.frameworkset.platform.sysmgrcore.manager;

import java.io.Serializable;
import java.util.List;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.sysmgrcore.entity.Log;
import com.frameworkset.platform.sysmgrcore.entity.LogDetail;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;


/**
 * 项目：SysMgrCore <br>
 * 描述：日志管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author 李峰高
 */
public interface LogManager extends Provider, Serializable {
	/**
	 * 存储日志对象实例
	 * 
	 * @param log
	 *         需要存储到数据源中的日志实体对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果存储成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	//public boolean storeLog(Log log) throws ManagerException;

	
	
	/**
	 * 取所有日志
	 * 
	 * @return 日志实体对象列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getLogList() throws ManagerException;

	
	
	/**
	 * 根据HQL语法取日志列表，例如：<br>
	 * getJobList("from Job job where job.jobName='日志名'"); <br>
	 * 有关HSQL语法的更详细介绍请参考 Hibernate 的相关书籍
	 * 
	 * @param hql
	 *            Hibernate 的 HQL 语法，类似于 SQL 语法
	 * @return 如果成功则返回日志对象实体列表
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public List getLogList(String hql) throws ManagerException;

	
	
	/**
	 * 删除日志实例同时将连带删除与该实例有关的所有实例，如：日志与机构关系实例。
	 * 
	 * @param job
	 *         需要删除的日志对象，由于无法确定数据源所以传入该对象时请尽可能保证该对象的完整性也就是它的所有属性都有相应的值
	 * @return 如果删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteLog(Log log) throws ManagerException;

	/**
	 * 根据日志ID删除指定的日志
	 * 
	 * @param jobId
	 *         需要删除的日志实体对象的日志ID
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException
	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
	 */
	public boolean deleteLog(String logId) throws ManagerException;


//	
//	 /**
//	 * 返回数据分页的配置类
//	 * 
//	 * @return 可以设置数据分页对象，如：<br>
//	 *         PageConfig pageConfig = jobManager.getPageConfig();
//	 *         pageConfig.setPageSize(当前页面中需要显示的数据大小);
//	 *         pageConfig.setStartIndex(当前页面中显示数据的起始索引值); <br>
//	 *         int recordCount = pageConfig.getTotalSize();
//	 * @throws ManagerException
//	 *             在处理当前方法的过程中如果遇到问题将抛出 ManagerException 异常
//	 */
//	public PageConfig getPageConfig() throws ManagerException;

	/**
	 * <p>插入日志：有明细则插入明细，没有明细则不插入</p>
	 * @param log
	 * @return long 主键值
	 * @throws ManagerException
	 */
    public String log(Log log) throws ManagerException ;
	
    /**
     * <p>批量插入日志明细</p>
     * @param detailList
     * @return String[]
     */
    public Object[] logDetails(final List detailList)throws ManagerException ;
    
	/**
	 * 记录登陆用户操作信息
	 * @param operUser	   操作人
	 * @param operContent  日志内容
	 * @param operModule     日志类型
	 * @param operSource   操作来源（模块，ip）
	 * @param Desc		   日志描述
	 * @throws ManagerException
	 */
	public String log(String operUser,String operContent,String operModule,String operSource,String Desc) throws ManagerException;
	
	/**
	 * 记录登陆用户操作信息
	 * @param operUser	   操作人
	 * @param operContent  日志内容
	 * @param operModule     日志类型
	 * @param operSource   操作来源（模块，ip）	 
	 * @throws ManagerException
	 */
	public String log(String operUser,String operContent,String operModule,String operSource) throws ManagerException;
	
	/**
	 * <p>记录一条不带明细的日志</p>
	 * @param operUser
	 * @param operOrg
	 * @param logModule
	 * @param visitorial
	 * @param oper
	 * @param remark1
	 * @param operType
	 * @return
	 * @throws ManagerException
	 */
	public String log(String operUser,String operOrg,String logModule, String visitorial,
			String oper ,String remark1, int operType)throws ManagerException ;
	
	/**
	 * <p>不带入用户信息记录日志，只要求带入业务参数</p>
	 * @param logModule
	 * @param oper
	 * @param remark1
	 * @param operType
	 * @return String 主键
	 * @throws ManagerException
	 */
	public String log(String logModule,String oper ,String remark1,int operType) throws ManagerException;
	
	/**
	 * <p>记录一条只有一个明细的日志</p>	
	 * @param logModule	
	 * @param oper
	 * @param remark1
	 * @param operType
	 * @param operTable
	 * @param operKeyID
	 * @param detailContent
	 * @param detailOperType
	 * @return
	 * @throws ManagerException
	 */
	
	public String log(String logModule, String oper ,String remark1, int operType,String operTable,String operKeyID, 
			String detailContent, int detailOperType)throws ManagerException ;
	
	
	/**
	 * <p>保存单个明细</p>
	 * @param detail
	 * @return
	 * @throws ManagerException
	 */
	public String logDetail(final LogDetail detail)throws ManagerException ;
	
	/**
	 * <p>保存单个明细</p>
	 * @param detail
	 * @return Object 主键
	 * @throws ManagerException
	 */
	public String logDetail(long logID, String operTable,String operKeyID, String detailContent, int operType)throws ManagerException ;
			
	/**
	 * 判断要填写日志的模块是否存在
	 * @param logmodule
	 * @return
	 * @throws ManagerException
	 */ 
	public boolean enabledlog(String logmodule)throws ManagerException;
	/**
	 * 修改日志是否记录状态
	 * @param logid
	 * @return
	 * @throws ManagerException
	 */
	public boolean updatelog(String status,String logid) throws ManagerException;
	
	/**
	 * 查询
	 * @return
	 * @throws ManagerException 
	 * LogManager.java
	 * @author: ge.tao
	 */
	public List querylog(Log log)throws ManagerException;
	
	/**
	 * add by gao.tang 2007.12.28
	 * 删除用户可管理机构的所有操作日志
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteAllLog(String curUserId) throws ManagerException ;
	
	/**
	 * <p>对最新日志（在线日志）记录进行批量删除</p>
	 * @param logId
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteLatestLog(String[] logId) throws ManagerException ;
	
	/**
	 * <p>对以前日志（历史日志）记录进行批量删除</p>
	 * @param logId
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteHistoricalLog(String[] logId) throws ManagerException ;		
	
	/**
	 * 记录用户操作日志，只需传入日志内容与操作模块
	 * @param operContent  日志内容
	 * @param operModule   日志类型
	 * @throws ManagerException
	 */	
	public String log(String operContent, String operModle) throws ManagerException;
	
}
