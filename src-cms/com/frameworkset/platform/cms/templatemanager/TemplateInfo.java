/**
 * 
 */
package com.frameworkset.platform.cms.templatemanager;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.container.Template;

/**
 * <p>Title: TemplateInfo.java</p>
 *  
 * <p>Description: 保存模板包中的模板信息(Template,及模板附件列表List)</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-10-15 15:29:47
 * @author ge.tao
 * @version 1.0
 */
public class TemplateInfo implements java.io.Serializable {
	private Template template;
	private List attachements;
	public TemplateInfo()
	{
		attachements = new ArrayList();
	}
	public Template getTemplate() {
		return template;
	}
	public void setTemplate(Template template) {
		this.template = template;
	}
	public List getAttachements() {
		return attachements;
	}
	
	public void addAttachement(String attachement)
	{
		this.attachements.add(attachement);
	}

}
