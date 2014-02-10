package com.frameworkset.platform.cms.documentmanager.bean;

import java.util.Date;
/**
 * 
 * @author yinbp
 *
 */
public class DocExtValue {
	private String field;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Date getDatevalue() {
		return datevalue;
	}
	public void setDatevalue(Date datevalue) {
		this.datevalue = datevalue;
	}
	public String getClobvalue() {
		return clobvalue;
	}
	public void setClobvalue(String clobvalue) {
		this.clobvalue = clobvalue;
	}
	public String getStringvalue() {
		return stringvalue;
	}
	public void setStringvalue(String stringvalue) {
		this.stringvalue = stringvalue;
	}
	public String getFieldtype() {
		return fieldtype;
	}
	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}
	public int getIntvalue() {
		return intvalue;
	}
	public void setIntvalue(int intvalue) {
		this.intvalue = intvalue;
	}
	private Date datevalue;
	private String clobvalue;
	private String stringvalue;
	private String fieldtype;
	private int intvalue;

}
