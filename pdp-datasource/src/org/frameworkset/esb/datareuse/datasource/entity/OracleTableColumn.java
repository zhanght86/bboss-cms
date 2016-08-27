package org.frameworkset.esb.datareuse.datasource.entity;
/**  
 * @Title: OracleTableColumn.java
 * @Package org.frameworkset.esb.datareuse.datasource.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @Copyright:Copyright (c) 2011
 * @Company:湖南科创
 * @author zhiyong.sun 
 * @date 2011-11-24 下午04:50:21
 * @version V1.0  
 */
public class OracleTableColumn {
	private String column_name;
	private String data_type;
	private int data_length;
	private int data_scale;
	private String table_name;
	private String owner;
	private String dbname;
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String columnName) {
		column_name = columnName;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String dataType) {
		data_type = dataType;
	}
	public int getData_length() {
		return data_length;
	}
	public void setData_length(int dataLength) {
		data_length = dataLength;
	}
	public int getData_scale() {
		return data_scale;
	}
	public void setData_scale(int dataScale) {
		data_scale = dataScale;
	}
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


