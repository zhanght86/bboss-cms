package com.frameworkset.platform.cms.driver.context;

import com.frameworkset.platform.cms.container.Template;

public interface TemplateContext extends Context{
	public String getTemplateid() ;
	public Template getTemplate();
	public String getViewid();

}
