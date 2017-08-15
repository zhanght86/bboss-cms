/**
 * 
 */
package com.frameworkset.platform.dictionary;

import com.frameworkset.platform.dictionary.input.InputTypeScript;

/**
 * <p>Title: DictAttachField.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-8 11:28:11
 * @author ge.tao
 * @version 1.0
 */
public class DictAttachField implements java.io.Serializable {
	/**
	 * 字段可为空
	 */
	public static final int ISNULLABLE = 0;
	/**
	 * 字段不能为空
	 */
	public static final int NOTNULLABLE = 1;
	/**
	 * 字段值不能重复
	 */
	public static final int UNIQUE = 1;
	/**
	 * 字段值可以重复
	 */
	public static final int REPEATABLE = 0;
	
	private String dicttypeId;
	/**	 
	 * 页面字段的名称
	 */
	private String dictField;
	private String dictFieldName;
	private String inputTypeId;
	private String inputTypeName;
	private String table_column;
	/**
	 * 该附加字段是否已经保存了数据
	 */
	private boolean isUsed;
	/**
	 * 0:字段可为空
	 * 1:字段不可为空
	 */
	private int isnullable;
	/**
	 * 0:可重复
	 * 1:唯一
	 */
	private int isunique;
	/**
	 * 数据库字典类型的长度
	 */
	private int maxLength;
	/**
	 * 数据库字段的类型
	 */
	private String columnTypeName;
	/**
	 * 数据库字段在页面的校验类型
	 */
	private String fieldValidType;
	
	/**
	 * 输入域的值,update回写或者缺省值
	 */
	private String fieldValue;
	
//	private InputType inputType;
	private InputTypeScript inputTypeScript;
	
	private String dateFormat = "yyyy-MM-dd hh:mm:ss";
	
	
	public InputTypeScript getInputTypeScript() {
		return inputTypeScript;
	}
	public void setInputTypeScript(InputTypeScript inputTypeScript) {
		this.inputTypeScript = inputTypeScript;
	}
	public boolean isUsed() {
		return isUsed;
	}
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	public String getTable_column() {
		return table_column;
	}
	public void setTable_column(String table_column) {
		this.table_column = table_column;
	}
	public String getDictField() {
		return dictField;
	}
	public void setDictField(String dictField) {
		this.dictField = dictField;
	}
	public String getDictFieldName() {
		return dictFieldName;
	}
	public void setDictFieldName(String dictFieldName) {
		this.dictFieldName = dictFieldName;
	}
	public String getInputTypeId() {
		return inputTypeId;
	}
	public void setInputTypeId(String inputTypeId) {
		this.inputTypeId = inputTypeId;
	}

	public String getDicttypeId() {
		return dicttypeId;
	}
	public void setDicttypeId(String dicttypeId) {
		this.dicttypeId = dicttypeId;
	}
	public String getInputTypeName() {
		return inputTypeName;
	}
	public void setInputTypeName(String inputTypeName) {
		this.inputTypeName = inputTypeName;
	}
	public int getIsnullable() {
		return isnullable;
	}
	public void setIsnullable(int isnullable) {
		this.isnullable = isnullable;
	}
	public String getColumnTypeName() {
		return columnTypeName;
	}
	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public String getFieldValidType() {
		return fieldValidType;
	}
	public void setFieldValidType(String fieldValidType) {
		this.fieldValidType = fieldValidType;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	public int getIsunique() {
		return isunique;
	}
	public void setIsunique(int isunique) {
		this.isunique = isunique;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		if(dateFormat != null && !dateFormat.equals(""))
			this.dateFormat = dateFormat;
	}

}
