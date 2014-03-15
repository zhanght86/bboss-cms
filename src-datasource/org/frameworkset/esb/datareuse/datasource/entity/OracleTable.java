package org.frameworkset.esb.datareuse.datasource.entity;
/**  
 * @Title: OracleTable.java
 * @Package org.frameworkset.esb.datareuse.datasource.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @Copyright:Copyright (c) 2011
 * @Company:湖南科创
 * @author zhiyong.sun 
 * @date 2011-11-24 上午11:47:33
 * @version V1.0  
 */
public class OracleTable {
	
	private String table_name;
	private String owner;
	private String dbname;
	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String tableName) {
		table_name = tableName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}
	

}


