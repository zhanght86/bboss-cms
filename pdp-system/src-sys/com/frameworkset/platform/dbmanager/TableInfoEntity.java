package com.frameworkset.platform.dbmanager;

import java.io.Serializable;

/**
 *	对应数据库中TableInfo表的实体类
 */
public class TableInfoEntity 
			implements Serializable{
	private static final long serialVersionUID = -5327377907428784000L;
	
	/**
	 * 表的名称
	 */
	private String tableName ;
	
	
	/**
	 * 对应表的主键名称
	 */
	private String tableKeyName ;
	
	
	/**
	 * 主键递增量 默认值为1
	 */
	private int keyIncrement ;
	
	/**
	 * 当前主键的值　默认值为0
	 */
	private int currentKeyValue ;
	
	
	/**
	 * 自定义表关键生成机构
	 */
	private String keyGenerator ;
	
	/**
	 * 主键类型: int string 
	 */
	private String keyType ;
	
	/**
	 * 类型为String 主键前缀 默认为""
	 */
	private String keyPrefix ;
	

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	
	public void setTableKeyName(String tableKeyName) {
		this.tableKeyName = tableKeyName;
	}
	
	public String getTableKeyName() {
		return tableKeyName;
	}
	
	public void setKeyIncrement(int keyIncrement) {
		this.keyIncrement = keyIncrement;
	}
	
	public int getKeyIncrement() {
		return keyIncrement;
	}
	
	public void setCurrentKeyValue(int currentKeyValue) {
		this.currentKeyValue = currentKeyValue;
	}
	
	public int getCurrentKeyValue() {
		return currentKeyValue;
	}
	
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyGenerator(String keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
	public String getKeyGenerator() {
		return keyGenerator;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}
	
	public String getKeyPrefix() {
		return keyPrefix;
	}
}
