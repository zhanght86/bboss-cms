package com.frameworkset.platform.cms.documentmanager.bean;
/**
 * <p>
 * Title: DocClass.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2012-8-13 下午3:38:27
 * @author biaoping.yin
 * @version 1.0.0
 */
public class DocClass {
	private int site_id;
	private String class_name;
	private String class_desc;
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	public String getClass_desc() {
		return class_desc;
	}
	public void setClass_desc(String class_desc) {
		this.class_desc = class_desc;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}

}
