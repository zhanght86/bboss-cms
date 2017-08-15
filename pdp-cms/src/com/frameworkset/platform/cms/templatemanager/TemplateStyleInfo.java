package com.frameworkset.platform.cms.templatemanager;

/**
 * <p>Title: TemplateInfo.java</p>
 *  
 * <p>Description: 保存模板风格中的所有信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-12-10 11:01:47
 * @author peng.yang
 * @version 1.0
 */
public class TemplateStyleInfo implements java.io.Serializable {
	private Integer styleId ;
	private String styleName ;
	private String styleDesc ;
	private int styleOrder ;
	
	public int getStyleOrder() {
		return styleOrder;
	}
	public void setStyleOrder(int styleOrder) {
		this.styleOrder = styleOrder;
	}
	public String getStyleDesc() {
		return styleDesc;
	}
	public void setStyleDesc(String styleDesc) {
		this.styleDesc = styleDesc;
	}
	public Integer getStyleId() {
		return styleId;
	}
	public void setStyleId(Integer styleId) {
		this.styleId = styleId;
	}
	public String getStyleName() {
		return styleName;
	}
	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}
	
	
}
