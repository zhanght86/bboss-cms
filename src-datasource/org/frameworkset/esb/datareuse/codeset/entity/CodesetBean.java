package org.frameworkset.esb.datareuse.codeset.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.frameworkset.orm.annotation.Column;

/*
 * Copyright:bboss
 * author:qian.wang
 * Date:4.2.2011
 */
public class CodesetBean implements Serializable{

	private String ds_name; //数据源名称
	private String last_version_flag; //是否为最新版本，1为是，0为否
	private String object_code; //数据对象编号
	private String object_id; //数据对象ID
	private String object_name; //数据对象名称
	private String object_type; //数据实体类型TABLE、VIEW、SQL
	private String param_table_name; //数据实体名
	private String remark; //注释
	@Column(name="sql_")
	private String sql; //SQL语句
	private String status; //状态(0表示新建，1表示启用，2表示停用)
	@Column(name="version_")
	private int version; //版本号
	private Timestamp create_time;//新建时间
	private String creator;//新建者
	private String modifier;//最新修改者
	private Timestamp modify_time;//最新修改时间
	
	public Timestamp getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Timestamp create_time) {
		this.create_time = create_time;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Timestamp getModify_time() {
		return modify_time;
	}
	public void setModify_time(Timestamp modify_time) {
		this.modify_time = modify_time;
	}
	public String getDs_name() {
		return ds_name;
	}
	public void setDs_name(String ds_name) {
		this.ds_name = ds_name;
	}
	public String getLast_version_flag() {
		return last_version_flag;
	}
	public void setLast_version_flag(String last_version_flag) {
		this.last_version_flag = last_version_flag;
	}
	public String getObject_code() {
		return object_code;
	}
	public void setObject_code(String object_code) {
		this.object_code = object_code;
	}
	public String getObject_id() {
		return object_id;
	}
	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}
	public String getObject_name() {
		return object_name;
	}
	public void setObject_name(String object_name) {
		this.object_name = object_name;
	}
	public String getObject_type() {
		return object_type;
	}
	public void setObject_type(String object_type) {
		this.object_type = object_type;
	}
	public String getParam_table_name() {
		return param_table_name;
	}
	public void setParam_table_name(String param_table_name) {
		this.param_table_name = param_table_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
}
