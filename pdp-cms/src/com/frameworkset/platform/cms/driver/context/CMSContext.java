package com.frameworkset.platform.cms.driver.context;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.sitemanager.Site;

/**
 * <p>Title: com.frameworkset.platform.cms.driver.context.CMSContext.java</p>
 *
 * <p>Description: 站点上下文</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-9
 * @author biaoping.yin
 * @version 1.0
 */
public interface CMSContext extends Context{
	

	
	/**
	 * 站点对应的根路径
	 * @return
	 */
	public String getSiteDir();
	

	
	public Site getSite();
	
	
	public FTPConfig getFTPConfig();

	public Template getIndexTemplate();
	
	/**
	 * 判断首页模版是否存在
	 * @return
	 */
	public boolean haveIndexTemplate();

	
	
	
	
}
