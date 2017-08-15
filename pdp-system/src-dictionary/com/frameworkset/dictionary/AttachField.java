/**
 * 
 */
package com.frameworkset.dictionary;

import java.io.Serializable;

/**
 * 附加字典对象
 * <p>Title: AttachField.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-11-19 15:42:57
 * @author ge.tao
 * @version 1.0
 */
public class AttachField implements Serializable{
	private String dicttypeId;
	private String fieldName;
	private String fieldLabel;
	private int attachId;
	/**
	 * 0:字段可为空
	 * 1:字段不可为空
	 */
	private int isnullable;
	
	public int getIsnullable() {
		return isnullable;
	}
	public void setIsnullable(int isnullable) {
		this.isnullable = isnullable;
	}
	public int getAttachId() {
		return attachId;
	}
	public void setAttachId(int attachId) {
		this.attachId = attachId;
	}
	public String getDicttypeId() {
		return dicttypeId;
	}
	public void setDicttypeId(String dicttypeId) {
		this.dicttypeId = dicttypeId;
	}
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

}
