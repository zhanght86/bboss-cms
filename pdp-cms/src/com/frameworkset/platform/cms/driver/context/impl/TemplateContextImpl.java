package com.frameworkset.platform.cms.driver.context.impl;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.TemplateContext;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerException;

/**
 * 
  * <p>Title: TemplateContextImpl</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class TemplateContextImpl extends BaseContextImpl implements TemplateContext {
	private String templateid;
	private String viewid;
	private Template template;
	public TemplateContextImpl(String siteid,String templateid,
							   String viewid,
							   Context parentContext,
							   PublishObject publishObject,
							   PublishMonitor monitor)
	{
		super(siteid,parentContext,publishObject,monitor);
		this.templateid = templateid;
		this.viewid = viewid;
		 try {
			template = getDriverConfiguration()
			.getCMSService()
			.getTemplateManager()
			.getTemplateInfo(templateid);
		} catch (TemplateManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getTemplateid() {
		return templateid;
	}
	public String getViewid() {
		return viewid;
	}
	public Template getTemplate() {
		// TODO Auto-generated method stub
		return template;
	}
	
	

	

}
