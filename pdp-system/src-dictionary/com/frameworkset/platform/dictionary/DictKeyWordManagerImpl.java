package com.frameworkset.platform.dictionary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 
 * 
 * <p>Title: DictKeyWordManagerImpl.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date  2008-3-22 10:30:16
 * @author gao.tang
 * @version 1.0
 */
public class DictKeyWordManagerImpl implements DictKeyWordManager {

	public boolean defineKeyField(KeyWord keyWord) {
		boolean state = false;
		DBUtil db = new DBUtil();
		String sql = "select count(*) from TD_SM_DICTKEYWORDS where DICTTYPE_ID='"+keyWord.getDictypeId()+"'"
			+" and FILED_NAME='"+keyWord.getFieldName()+"'";
		try {
			db.executeSelect(sql);
			if(db.getInt(0,0) > 0){
				state = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return state;
	}

	public boolean defineKeyFields(List newkeyWord, String dicttypeId) {
		boolean state = false;
		DBUtil add_db = new DBUtil();
		DBUtil del_db = new DBUtil();
		StringBuffer add_sql = new StringBuffer();
		StringBuffer del_sql = new StringBuffer();
		//创建事物
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			del_sql.append("delete from TD_SM_DICTKEYWORDS where DICTTYPE_ID='").append(dicttypeId).append("'");
			del_db.executeDelete(del_sql.toString());
			if(newkeyWord != null && newkeyWord.size() > 0){
				for(int i = 0; i < newkeyWord.size(); i++){
					int kid = (int)del_db.getNextPrimaryKey("TD_SM_DICTKEYWORDS");
//					TD_SM_DICTKEYWORDS"."KEYWORD_ID
					KeyWord keyWord = (KeyWord)newkeyWord.get(i);
					add_sql.append("insert into TD_SM_DICTKEYWORDS(DICTTYPE_ID,FILED_NAME,JAVA_PROPERTY,KEYWORD_ID) ")
						.append("values('").append(keyWord.getDictypeId()).append("','")
						.append(keyWord.getFieldName().toUpperCase()).append("','")
						.append(keyWord.getJavaProperty().trim()).append("',").append(kid).append(")");
					add_db.addBatch(add_sql.toString());
					add_sql.setLength(0);
				}
				add_db.executeBatch();
			}
			tm.commit();
			state = true;
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return state;
	}

	public boolean getKeyFields(String dicttypeId) {
		return false;
	}

	/**
	 * 获取所有已定义的关键字段
	 * @param dicttypeid
	 * @return Map<String columnName,KeyWord keyword>
	 */
	public Map getAllKeyWords(String dicttypeid) {
		Map map = new HashMap();
		KeyWord keyWord = null;
		DBUtil db = new DBUtil();
		String sql = "select JAVA_PROPERTY,FILED_NAME,DICTTYPE_ID from TD_SM_DICTKEYWORDS where DICTTYPE_ID='"+dicttypeid+"'";
		try {
			db.executeSelect(sql);
			for(int i = 0; i < db.size(); i++){
				keyWord = new KeyWord();
				keyWord.setFieldName(db.getString(i,"FILED_NAME"));
				keyWord.setJavaProperty(db.getString(i,"JAVA_PROPERTY"));
				keyWord.setDictypeId(db.getString(i,"DICTTYPE_ID"));
				map.put(db.getString(i,"FILED_NAME").toUpperCase(),keyWord);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取字典中已经定义的表的字段：
	 * @param dicttypeid
	 * @return Map<String columnName,String columnName>
	 */
	public Map getAllDictFields(String dicttypeid) {
		Map map = new HashMap();
		DBUtil db_dicttype = new DBUtil();
		DBUtil db_dicattachfield = new DBUtil();
		String sql_dicttype = "SELECT DATA_CREATE_ORGID_FIELD,DATA_PARENTID_FIELD,DATA_NAME_FILED," +
				"DATA_VALUE_FIELD,DATA_ORDER_FIELD,DATA_TYPEID_FIELD FROM TD_SM_DICTTYPE WHERE " +
				"DICTTYPE_ID='"+dicttypeid+"'";
		String sql_dicattachfield = "select TABLE_COLUMN from TD_SM_DICATTACHFIELD where DICTTYPE_ID='"+dicttypeid+"'";
		try {
			db_dicttype.executeSelect(sql_dicttype);
			db_dicattachfield.executeSelect(sql_dicattachfield);
//			if(db_dicttype.getString(0,"DATA_CREATE_ORGID_FIELD") != null && 
//					!"".equals(db_dicttype.getString(0,"DATA_CREATE_ORGID_FIELD"))){
//				map.put(db_dicttype.getString(0,"DATA_CREATE_ORGID_FIELD"),db_dicttype.getString(0,"DATA_CREATE_ORGID_FIELD"));
//			}
//			if(db_dicttype.getString(0,"DATA_PARENTID_FIELD") != null &&
//					!"".equals(db_dicttype.getString(0,"DATA_PARENTID_FIELD"))){
//				map.put(db_dicttype.getString(0,"DATA_PARENTID_FIELD"),db_dicttype.getString(0,"DATA_PARENTID_FIELD"));
//			}
			if(db_dicttype.getString(0,"DATA_NAME_FILED") != null &&
					!"".equals(db_dicttype.getString(0,"DATA_NAME_FILED"))){
				map.put(db_dicttype.getString(0,"DATA_NAME_FILED").toUpperCase(),db_dicttype.getString(0,"DATA_NAME_FILED"));
			}
			if(db_dicttype.getString(0,"DATA_VALUE_FIELD") != null &&
					!"".equals(db_dicttype.getString(0,"DATA_VALUE_FIELD"))){
				map.put(db_dicttype.getString(0,"DATA_VALUE_FIELD").toUpperCase(),db_dicttype.getString(0,"DATA_VALUE_FIELD"));
			}
//			if(db_dicttype.getString(0,"DATA_ORDER_FIELD") != null &&
//					!"".equals(db_dicttype.getString(0,"DATA_ORDER_FIELD"))){
//				map.put(db_dicttype.getString(0,"DATA_ORDER_FIELD"),db_dicttype.getString(0,"DATA_ORDER_FIELD"));
//			}
//			if(db_dicttype.getString(0,"DATA_TYPEID_FIELD") != null &&
//					!"".equals(db_dicttype.getString(0,"DATA_TYPEID_FIELD"))){
//				map.put(db_dicttype.getString(0,"DATA_TYPEID_FIELD"),db_dicttype.getString(0,"DATA_TYPEID_FIELD"));
//			}
			for(int i = 0; i < db_dicattachfield.size(); i++){
				map.put(db_dicattachfield.getString(i,"TABLE_COLUMN").toUpperCase(),db_dicattachfield.getString(i,"TABLE_COLUMN"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public String getJavaProperty(KeyWord keyWord) {
		String javaProperty = "";
		DBUtil db = new DBUtil();
		String sql = "select JAVA_PROPERTY from TD_SM_DICTKEYWORDS where DICTTYPE_ID='"+keyWord.getDictypeId()+"'"
			+" and FILED_NAME='"+keyWord.getFieldName()+"'";
		try {
			db.executeSelect(sql);
			if(db.size() > 0){
				javaProperty = db.getString(0,"JAVA_PROPERTY");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return javaProperty;
	}
	
	/**
	 * 得到字典中失效的关键字段
	 */
	public Map getInvalidationKeys(String dicttypeId, String dbname, String tablename){
		Map map = new HashMap();
		DBUtil db = new DBUtil();
		KeyWord keyWord = null;
		Set set = DBUtil.getColumnMetaDatas(dbname,tablename);	
		Iterator it = set.iterator();
		String columnNames = "";
		while(it.hasNext()){
		    ColumnMetaData  metaData = (ColumnMetaData)it.next();
		    String columnName = metaData.getColumnName();
		    if("".equals(columnNames)){
		    	columnNames = "'" + columnName + "'"; 
		    }else{
		    	columnNames += "," + "'" + columnName + "'";
		    }
		}
		String sql = "SELECT * FROM TD_SM_DICTKEYWORDS WHERE DICTTYPE_ID='"+dicttypeId+"' and FILED_NAME not in" + 
			"("+columnNames+")";
		try {
			db.executeSelect(sql);
			for(int i = 0; i < db.size(); i++){
				keyWord = new KeyWord();
				keyWord.setFieldName(db.getString(i,"FILED_NAME"));
				keyWord.setJavaProperty(db.getString(i,"JAVA_PROPERTY"));
				keyWord.setDictypeId(db.getString(i,"DICTTYPE_ID"));
				map.put(db.getString(i,"FILED_NAME"),keyWord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}

	public boolean delDefineKeyFields(List keyWords) {
		DBUtil db = new DBUtil();
		boolean state = false;
		StringBuffer del_sql = new StringBuffer();
		KeyWord keyWord = null;
		if(keyWords != null && keyWords.size() > 0){
			try {
				for(int i = 0; i < keyWords.size(); i++){
					keyWord = (KeyWord)keyWords.get(i);
					del_sql.append("delete from TD_SM_DICTKEYWORDS where DICTTYPE_ID='")
						.append(keyWord.getDictypeId()).append("' and ")
						.append("FILED_NAME='").append(keyWord.getFieldName()).append("'");
						db.addBatch(del_sql.toString());
						del_sql.setLength(0);
					}
				db.executeBatch();
				state = true;
			}catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return state;
	}
	
	/**
	 * 得到高级字段设置的关键字段名称
	 * @param dicttypeId
	 * @return
	 */
	public String getDicattachfieldKey(String dicttypeId){
		DBUtil db = new DBUtil();
		DBUtil dbKey = new DBUtil();
		String sql = "select DATA_TABLE_NAME,DATA_DBNAME from td_sm_dicttype where DICTTYPE_ID='"+dicttypeId+"'";
		String keyColumnName = "";
			
		try {
			db.executeSelect(sql);
			String dbname = db.getString(0,"DATA_DBNAME");
			String tablename = db.getString(0,"DATA_TABLE_NAME");
			Set set = DBUtil.getColumnMetaDatas(dbname,tablename);	
			Iterator it = set.iterator();
			String columnNames = "";
			while(it.hasNext()){
			    ColumnMetaData  metaData = (ColumnMetaData)it.next();
			    String columnName = metaData.getColumnName();
			    if("".equals(columnNames)){
			    	columnNames = "'" + columnName + "'"; 
			    }else{
			    	columnNames += "," + "'" + columnName + "'";
			    }
			}
			String sqlKey = "SELECT a.TABLE_COLUMN FROM TD_SM_DICATTACHFIELD a,TD_SM_DICTKEYWORDS b WHERE a.dicttype_id='"+dicttypeId+"' and upper(a.TABLE_COLUMN) in" + 
			"("+columnNames.toUpperCase()+") and upper(b.FILED_NAME)=upper(a.TABLE_COLUMN) and a.dicttype_id=b.DICTTYPE_ID";
			System.out.println("sqlKey = " + sqlKey);
			dbKey.executeSelect(sqlKey);
			for(int i = 0; i < dbKey.size(); i++){
				if("".equals(keyColumnName)){
					keyColumnName = dbKey.getString(i,"TABLE_COLUMN").toLowerCase();
				}else{
					keyColumnName += "," + dbKey.getString(i,"TABLE_COLUMN").toLowerCase();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return keyColumnName;
	}

}
