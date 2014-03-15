package org.frameworkset.esb.datareuse.codeset.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.frameworkset.orm.annotation.Column;

public class CodesetBeanDetail implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1099628424968650073L;
	private String column_code; //条目字段
	private String column_id; //条目ID
	private String column_name; //条目标签
	private String column_type; //字段类型
	@Column(name="length_")
	private String length; //字段长度
	private String object_id; //数据对象ID
	@Column(name="precision_")
	private String precision; //字段精度
	@Column(name="seq_")
	private int seq; //排序号
	private Timestamp create_time;//新建时间
	private String creator;//新建者
	private String modifier;//最新修改者
	private Timestamp modify_time;//最新修改时间
	
	private String remark;//选择字段的注释
	
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
	public String getColumn_code() {
		return column_code;
	}
	public void setColumn_code(String column_code) {
		this.column_code = column_code;
	}
	public String getColumn_id() {
		return column_id;
	}
	public void setColumn_id(String column_id) {
		this.column_id = column_id;
	}
	public String getColumn_name() {
		return column_name;
	}
	public void setColumn_name(String column_name) {
		this.column_name = column_name;
	}
	public String getColumn_type() {
		return column_type;
	}
	public void setColumn_type(String column_type) {
		this.column_type = column_type;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getObject_id() {
		return object_id;
	}
	public void setObject_id(String object_id) {
		this.object_id = object_id;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
