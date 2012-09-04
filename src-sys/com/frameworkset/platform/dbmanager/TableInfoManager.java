package com.frameworkset.platform.dbmanager;

import java.io.Serializable;
import java.util.List;

import com.frameworkset.common.tag.pager.ListInfo;

public interface TableInfoManager extends Serializable {
	
	/**
	 * 保存TableInfoEntiy实体
	 * @param dbName 数据源名称
	 * @param tableInfoEntiy :TableInfo实体类
	 * @return true :　表示成功　　false : 表示保存失败
	 * @throws TableInfoManagerException
	 */
	public boolean saveTableInfo(String dbName,TableInfoEntity tableInfoEntiy) 
									throws TableInfoManagerException;
	
	/**
	 * 表是否已经存在
	 * @param dbName 数据源名称
	 * @param tableName 表名称
	 * @return true : 表已经存在  false　：表不存在
	 * @throws TableInfoManagerException
	 */
	public boolean isExist(String dbName,String tableName)
									throws TableInfoManagerException;
	
	
	/**
	 * 更新表
	 * @param dbName 数据源名称
	 * @param tableInfoEntiy TableInfo实体类 
	 * @return true : 表示更新成功　false : 表示更新失败
	 * @throws TableInfoManagerException
	 */
	public boolean update(String dbName,TableInfoEntity tableInfoEntiy)
									throws TableInfoManagerException;
	
	/**
	 * 根据表的名称删除记录
	 * @param dbName
	 * @param tableInfoName
	 * @return true : 表示删除成功  false : 表示删除失败
	 * @throws TableInfoManagerException
	 */
	public boolean deleteByTableName(String dbName,String tableName)
									throws TableInfoManagerException;
	
	/**
	 * 批量删除表的记录
	 * @param dbName
	 * @param tableNames
	 * @return true : 表示删除成功 false : 表示删除失败
	 * @throws TableInfoManagerException
	 */
	public boolean deletesByTableNames(String dbName,String[] tableNames)
									throws TableInfoManagerException ;
	
	/**
	 * 根据表的名称，获取表信息实体
	 * @param dbName
	 * @param tableName
	 * @return
	 * @throws TableInfoManagerException
	 */
	public TableInfoEntity getTableInfoEntity(String dbName,String tableName)
									throws TableInfoManagerException ;
	/**
	 * 获取指定的TABLEINFO表信息
	 * @param dbName
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws TableInfoManagerException
	 */
	public ListInfo getTableInfoEntityList(String dbName,String sql, int offset, int maxItem) 
									throws TableInfoManagerException ;
	
	/**
	 * 获取数据源下的表名称
	 * @param dbName 数据源名称
	 * @param isAll  true : 获取所有表的名称  false : 获取不在TableInfo表中的表的名称
	 * @return
	 * @throws TableInfoManagerException
	 */
	public List getTablesByDBName(String dbName,boolean isAll)
									throws TableInfoManagerException;
	
	/**
	 * 获取tableinfo表中的所有表的名称
	 * @param dbName 数据源名称
	 * @return
	 */
	public List getTableNamesOfTableInfo(String dbName)
									throws TableInfoManagerException;
	
	
}
