package com.frameworkset.platform.cms.customform;

import java.util.ArrayList;

/**
 * 文档扩展字段基本信息bean类
 * 
 * @author jxw
 *
 */

public class DocExtField implements java.io.Serializable
{
	/**
	 * 扩展字段主键
	 */
	private int fieldId;
	/**
	 * 字段名称
	 * 英文名字
	 */
	private String fieldName;
	/**
	 * 字段label
	 * 中文名字，在文档发布时的展现名字
	 */
	private String fieldLable;
	/**
	 * 字段描述
	 */
	private String fieldDesc;
	/**
	 * 字段最大长度
	 */
	private int maxlen;
	/**
	 * 字段类型
	 */
	private String fieldType;
	/**
	 * '扩展字段类型文档和频道：0-频道 ，channel_id表示频道id 1-文档  channel_id表示文档id'
'扩展字段类型文档和频道：0-频道 1-文档 '
	 */
	private int field_owner;
	/**
	 * 字段对应的输入框类型
	 * 
	 */
	private int inputType;
	/**
	 * 枚举类型扩展字段枚举值
	 * enum在jdk1.5后是保留名称，所以这里修改为eno
	 */
	private ArrayList eno;
	/**
	 * 连续类型扩展字段的取值的起点
	 */
	private int minvalue;
	/**
	 * 连续的扩展字段的最大值
	 */
	private int maxvalue;
	/**
	 * 是站点还是频道
	 * 1：站点
	 * 2：频道
	 */
	private String type;
	/**
	 * 如果是站点则为站点id
	 * 如果是频道则为频道id
	 */
	private String idOfSiteOrChl;
	/**
	 * 字段内容
	 */
	private String extfieldvalue;
	/**
	 * 字段内容
	 */
	private String numbervalue;
	/**
	 * 字段内容
	 */
	private String datevalue;
	/**
	 * 字段内容
	 */
	private String clobvalue;
	
	public String getFieldDesc() 
	{
		return fieldDesc;
	}
	
	public void setFieldDesc(String fieldDesc) 
	{
		this.fieldDesc = fieldDesc;
	}
	
	public int getFieldId() 
	{
		return fieldId;
	}
	
	public void setFieldId(int fieldId) 
	{
		this.fieldId = fieldId;
	}
	
	public String getFieldLable() 
	{
		return fieldLable;
	}
	
	public void setFieldLable(String fieldLable) 
	{
		this.fieldLable = fieldLable;
	}
	
	public String getFieldName() 
	{
		return fieldName;
	}
	
	public void setFieldName(String fieldName) 
	{
		this.fieldName = fieldName;
	}
	
	public String getFieldType() 
	{
		return fieldType;
	}
	
	public void setFieldType(String fieldType) 
	{
		this.fieldType = fieldType;
	}
	
	public int getInputType() 
	{
		return inputType;
	}
	
	public void setInputType(int inputType) 
	{
		this.inputType = inputType;
	}
	
	public int getMaxlen() 
	{
		return maxlen;
	}
	
	public void setMaxlen(int maxlen) 
	{
		this.maxlen = maxlen;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdOfSiteOrChl() {
		return idOfSiteOrChl;
	}

	public void setIdOfSiteOrChl(String idOfSiteOrChl) {
		this.idOfSiteOrChl = idOfSiteOrChl;
	}

	public String getExtfieldvalue() {
		return extfieldvalue;
	}

	public void setExtfieldvalue(String extfieldvalue) {
		this.extfieldvalue = extfieldvalue;
	}

	public String getClobvalue() {
		return clobvalue;
	}

	public void setClobvalue(String clobvalue) {
		this.clobvalue = clobvalue;
	}

	public String getDatevalue() {
		return datevalue;
	}

	public void setDatevalue(String datevalue) {
		this.datevalue = datevalue;
	}

	public String getNumbervalue() {
		return numbervalue;
	}

	public void setNumbervalue(String numbervalue) {
		this.numbervalue = numbervalue;
	}

	public int getMaxvalue() {
		return maxvalue;
	}

	public void setMaxvalue(int maxvalue) {
		this.maxvalue = maxvalue;
	}

	public int getMinvalue() {
		return minvalue;
	}

	public void setMinvalue(int minvalue) {
		this.minvalue = minvalue;
	}

	public ArrayList getEno() {
		return eno;
	}

	public void setEno(ArrayList eno) {
		this.eno = eno;
	}

	public int getField_owner() {
		return field_owner;
	}

	public void setField_owner(int field_owner) {
		this.field_owner = field_owner;
	}


}
