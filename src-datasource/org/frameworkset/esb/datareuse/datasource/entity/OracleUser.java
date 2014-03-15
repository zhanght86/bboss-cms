package org.frameworkset.esb.datareuse.datasource.entity;
/**  
 * @Title: OracleUser.java
 * @Package org.frameworkset.esb.datareuse.datasource.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @Copyright:Copyright (c) 2011
 * @Company:湖南科创
 * @author zhiyong.sun 
 * @date 2011-11-24 上午11:16:53
 * @version V1.0  
 */
public class OracleUser {
	private String username;
	private String dbname;
	private int tablenum;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public int getTablenum() {
		return tablenum;
	}

	public void setTablenum(int tablenum) {
		this.tablenum = tablenum;
	}
	
}


