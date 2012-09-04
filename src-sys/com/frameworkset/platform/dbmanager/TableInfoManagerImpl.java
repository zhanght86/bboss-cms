package com.frameworkset.platform.dbmanager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.PrimaryKeyCacheManager;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.common.tag.pager.ListInfo;
import com.frameworkset.orm.transaction.TransactionManager;


public class TableInfoManagerImpl implements TableInfoManager {

	public boolean saveTableInfo(String dbName,TableInfoEntity tableInfoEntiy)
			throws TableInfoManagerException {
		
		boolean flag = false ;
		
		String tableName = tableInfoEntiy.getTableName() ;	
		
		String keyName = tableInfoEntiy.getTableKeyName();
			
		int keyIncrement = tableInfoEntiy.getKeyIncrement();
		
		int currentKeyValue = tableInfoEntiy.getCurrentKeyValue();
		
		String keyType = tableInfoEntiy.getKeyType();
		
		String keyGenerator = tableInfoEntiy.getKeyGenerator();
		
		String keyPrefix = tableInfoEntiy.getKeyPrefix() ;
		
		DBUtil db = new DBUtil();
		
		StringBuffer sb = new StringBuffer("INSERT INTO TABLEINFO (TABLE_NAME, TABLE_ID_NAME, TABLE_ID_INCREMENT, TABLE_ID_VALUE,TABLE_ID_GENERATOR, TABLE_ID_TYPE, TABLE_ID_PREFIX ) VALUES (");
		
				sb.append("'").append(tableName).append("', ")
				  .append("'") .append(keyName).append("', ")
				  .append(keyIncrement).append(", ")
				  .append(currentKeyValue).append(", ")
				  .append("'").append(keyGenerator).append("', ")
				  .append("'").append(keyType).append("', ")
				  .append("'").append(keyPrefix).append("')");
		
		try{
			db.executeInsert(dbName,sb.toString());
			//刷新系统中的表主键信息
			PrimaryKeyCacheManager.getInstance().loaderPrimaryKey(dbName,
					tableName.toUpperCase());
			flag = true ;
		}catch(Exception e){
			e.printStackTrace();
			throw new TableInfoManagerException("TABLEINFO表添加信息失败!");
		}
		return flag ;
	}

	public boolean isExist(String dbName,String tableName) 
				throws TableInfoManagerException {
		
		boolean flag = false ;
		
		if(dbName == null || dbName.trim().equals("") 
						|| tableName == null || tableName.trim().equals("")){
			throw new TableInfoManagerException("表名称为空！");
		}
		
		DBUtil db = new DBUtil();
		try{
			String sql = "select * from TABLEINFO a where a.TABLE_NAME = '" + tableName + "'";
			db.executeSelect(dbName,sql);
			if(db.size() > 0){
				flag = true ;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new TableInfoManagerException("在判断表[" + tableName + "]是不是存在时，出现异常!"); 
		}
		return flag;
	}

	public boolean update(String dbName,TableInfoEntity tableInfoEntiy)
			throws TableInfoManagerException {
		
		boolean flag = false ;
		
		if(tableInfoEntiy == null){
			throw new TableInfoManagerException("更新表信息失败，失败原因：表信息不存在！");
		}
		
		String tableName = tableInfoEntiy.getTableName() ;
		String keyName = tableInfoEntiy.getTableKeyName();
		
		int keyIncrement = tableInfoEntiy.getKeyIncrement();
		int currentKeyValue = tableInfoEntiy.getCurrentKeyValue();
		String keyType = tableInfoEntiy.getKeyType();
		String keyGenerator = tableInfoEntiy.getKeyGenerator();
		String keyPrefix = tableInfoEntiy.getKeyPrefix() ;
		
		
		StringBuffer sb = new StringBuffer("update TABLEINFO set ");
		
		sb.append("TABLE_ID_NAME = '").append(keyName).append("', ")
		  .append("TABLE_ID_INCREMENT =").append(keyIncrement).append(", ")
		  .append("TABLE_ID_VALUE = ").append(currentKeyValue).append(", ")
		  .append("TABLE_ID_GENERATOR = '").append(keyGenerator).append("', ")
		  .append("TABLE_ID_TYPE = '").append(keyType).append("', ")
		  .append("TABLE_ID_PREFIX = '").append(keyPrefix).append("' ")
		  .append("where TABLE_NAME = '").append(tableName).append("'");
		
		DBUtil db = new DBUtil();
		try{
			db.executeUpdate(dbName,sb.toString());
			
			//刷新系统中的表主键信息
			PrimaryKeyCacheManager.getInstance()
						.loaderPrimaryKey(dbName,tableName);
			flag = true ;
		}catch(Exception e){
			e.printStackTrace();
			flag = false ;
			throw new TableInfoManagerException("表TABLEINFO更新失败!");
		}
		  
		return flag;
	}

	public boolean deleteByTableName(String dbName,String tableName)
			throws TableInfoManagerException {
		
		boolean flag = false ;
		
		String sql = "delete from TABLEINFO where TABLE_NAME = '" + tableName +"'";
		
		DBUtil db = new DBUtil();
		
		try{
			db.executeDelete(dbName,sql);
			
			//刷新系统中的表主键信息
			PrimaryKeyCacheManager.getInstance()
						.loaderPrimaryKey(dbName,tableName);
			flag = true ;
		}catch(Exception e){
			e.printStackTrace();
			flag = false ;
			throw new TableInfoManagerException("在删除[" + tableName +"]时发生异常!");
		}
		return flag;
	}

	public boolean deletesByTableNames(String dbName,String[] tableNames)
			throws TableInfoManagerException {
		
		boolean flag = false ;
		
		if(tableNames == null || tableNames.length == 0){
			throw new TableInfoManagerException("删除失败,失败原因:参数不正确!" );
		}
		
		DBUtil db = new DBUtil();
		TransactionManager tm = new TransactionManager();
		PrimaryKeyCacheManager primaryKeyCacheManager = PrimaryKeyCacheManager.getInstance() ;
		try{
			tm.begin();
			for(int i=0; i<tableNames.length; i++){
				String sql = "delete from TABLEINFO where TABLE_NAME = '" + tableNames[i] +"'";
				
				db.executeDelete(dbName,sql);
				//刷新系统中的表主键信息
				primaryKeyCacheManager.loaderPrimaryKey(dbName,tableNames[i]);
			}
			tm.commit();
			flag = true ;
		}catch(Exception e){
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			flag = false ;
		}
		
		
		return flag;
	}

	public TableInfoEntity getTableInfoEntity(String dbName,String tableName)
			throws TableInfoManagerException {
		
		String sql = "select * from TABLEINFO where TABLE_NAME = '" + tableName + "'";
		
		DBUtil db = new DBUtil();
		
		TableInfoEntity tableInfoEntiy = null ;
		
		try{
			db.executeSelect(dbName,sql);
			tableInfoEntiy = this.dbutilToTableEntity(db);
		}catch(Exception e){
			e.printStackTrace();
			throw new TableInfoManagerException("根据[" + tableName +"]查询失败!");
		}
		
		return tableInfoEntiy;
	}

	public ListInfo getTableInfoEntityList(String dbName,String sql, int offset, int maxItem) 
												throws TableInfoManagerException {
		
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();
		
		try{
			db.executeSelect(dbName,sql,offset,maxItem);
			list = this.dbutilToTableEntiyList(db);
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		}catch(Exception e){
			throw new TableInfoManagerException();
		}
		
		return listInfo;
	}
	
	private TableInfoEntity dbutilToTableEntity(DBUtil db) throws SQLException {
		
		TableInfoEntity tableInfoEntiy = new TableInfoEntity() ;
		if(db.size() > 0){
			tableInfoEntiy.setTableName(db.getString(0,"TABLE_NAME")) ;
			tableInfoEntiy.setTableKeyName(db.getString(0,"TABLE_ID_NAME"));
			tableInfoEntiy.setKeyIncrement(db.getInt(0,"TABLE_ID_INCREMENT"));
			tableInfoEntiy.setCurrentKeyValue(db.getInt(0,"TABLE_ID_VALUE"));
			tableInfoEntiy.setKeyGenerator(db.getString(0,"TABLE_ID_GENERATOR"));
			tableInfoEntiy.setKeyType(db.getString(0,"TABLE_ID_TYPE"));
			tableInfoEntiy.setKeyPrefix(db.getString(0,"TABLE_ID_PREFIX"));
		}	
		return tableInfoEntiy ;
	}
	
	private List dbutilToTableEntiyList(DBUtil db) throws SQLException {
			List list = new ArrayList();
			if(db.size() > 0){
				for(int i=0; i<db.size(); i++){
					TableInfoEntity tableInfoEntiy = new TableInfoEntity();
					tableInfoEntiy.setTableName(db.getString(i,"TABLE_NAME")) ;
					tableInfoEntiy.setTableKeyName(db.getString(i,"TABLE_ID_NAME"));
					tableInfoEntiy.setKeyIncrement(db.getInt(i,"TABLE_ID_INCREMENT"));
					tableInfoEntiy.setCurrentKeyValue(db.getInt(i,"TABLE_ID_VALUE"));
					tableInfoEntiy.setKeyGenerator(db.getString(i,"TABLE_ID_GENERATOR"));
					tableInfoEntiy.setKeyType(db.getString(i,"TABLE_ID_TYPE"));
					tableInfoEntiy.setKeyPrefix(db.getString(i,"TABLE_ID_PREFIX"));
					
					list.add(tableInfoEntiy);
				}	
		}
		return list;
	}
	
	
	public List getTableNamesOfTableInfo(String dbName)
									throws TableInfoManagerException {
		List list = new ArrayList();
		
		DBUtil db = new DBUtil();
		
		try{
			db.executeSelect(dbName,"select TABLE_NAME from TABLEINFO");
			if(db.size() > 0){
				for(int i=0; i< db.size(); i++){
					list.add(db.getString(i,"TABLE_NAME"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new TableInfoManagerException();
		}
		
		return list;
	}

	public List getTablesByDBName(String dbName, boolean isAll) throws TableInfoManagerException {
		
		if(dbName == null || "".equals(dbName))
		{
			throw new TableInfoManagerException("数据源不存在!");
		}
		
		List tablesList = new ArrayList();
		
		DBUtil db = new DBUtil();
		try{
			Set dbSetMetaDatas = db.getTableMetaDatas(dbName);
			
			//获取所有表的名称
			if(isAll){
				for (Iterator iter = dbSetMetaDatas.iterator(); iter.hasNext();) {
					TableMetaData tableMetaData = (TableMetaData) iter.next();
					String tableName = tableMetaData.getTableName() ;	
					tablesList.add(tableName);
				}
			}else{
				
				//TableInfo中表名称
				List existTables = getTableNamesOfTableInfo(dbName);
				
				for (Iterator iter = dbSetMetaDatas.iterator(); iter.hasNext();) {
					TableMetaData tableMetaData = (TableMetaData) iter.next();
					
					//注意返回的是大小的表名称
					String tableName = tableMetaData.getTableName() ;
					
					//获取不在TableInfo中表(排除大小写的名称)
					if(!existTables.contains(tableName) && 
								!existTables.contains(tableName.toLowerCase())){
						tablesList.add(tableName);
					}
				}
			}
			
		}catch(Exception e){
			throw new TableInfoManagerException("数据源[ " +dbName +" ],获取表失败,失败原因:" + e.getMessage());
		}
		
		return tablesList;
	}

}
