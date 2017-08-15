/**
 * 
 */
package com.frameworkset.platform.cms.templatemanager;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: TemplatePackage.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-10-15 15:21:13
 * @author ge.tao
 * @version 1.0
 */
public class TemplatePackage implements java.io.Serializable {

	/**
	 * true:是模板包
	 * false:不是模板包
	 */
	private boolean isTemplatePackage = false;
	/**
	 * 如果是不是模板包,指定解压之后临时根目录
	 */
	private String unpackage_path ;
	/**
	 * 是模板包,将其中的模板信息存放在列表中
	 * List<TemplateInfo>
	 */
	private List templateInfos;
	/**
	 * 选中信息列表
	 * List<TemplateInfo>
	 */
	private List selectedTemplateInfos;
	public TemplatePackage()
	{
		
	}
	public boolean isTemplatePackage() {
		return isTemplatePackage;
	}
	public List getTemplateInfos() {
		return templateInfos;
	}
	public String getUnpackage_path() {
		return unpackage_path;
	}
	/**
	 * 添加模板信息
	 * @param templateInfo 
	 * TemplatePackage.java
	 * @author: ge.tao
	 */
	public void addTemplateInfo(TemplateInfo templateInfo)
	{
		if(templateInfos == null)
			templateInfos = new ArrayList();
		templateInfos.add(templateInfo);
	}
	/**
	 * 添加选中的模板信息
	 * @param templateInfo 
	 * TemplatePackage.java
	 * @author: ge.tao
	 */
	public void addSelectTemplateInfo(TemplateInfo templateInfo)
	{
		if(selectedTemplateInfos == null)
			selectedTemplateInfos = new ArrayList();
		selectedTemplateInfos.add(templateInfo);
	}
	public void setTemplatePackage(boolean isTemplatePackage) {
		this.isTemplatePackage = isTemplatePackage;
	}
	public void setTemplateInfos(List templateInfos) {
		this.templateInfos = templateInfos;
	}
	public void setUnpackage_path(String unpackage_path) {
		this.unpackage_path = unpackage_path;
	}
	public List getSelectedTemplateInfos() {
		return selectedTemplateInfos;
	}
	
	
	

}
