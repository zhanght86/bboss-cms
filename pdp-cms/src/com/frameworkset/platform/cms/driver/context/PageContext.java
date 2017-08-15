package com.frameworkset.platform.cms.driver.context;

/**
 * 
  * <p>Title: PageContext</p>
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
public interface PageContext extends PagineContext {
	/**
	 * 获取发布页面所在的相对于站点根目录的目录路径
	 * @return
	 */
	public String getPageDir() ;
	
	/**
	 * 获取发布页面所在的相对于站点根目录的路径
	 * @return
	 */
	public String getPagePath() ;


	/**
	 * 获取页面对应的类型,主要有两种类型：
	 * CMSLink.static -静态类型
	 * CMSLink.dynamic -动态类型
	 * @return
	 */
	public String getPageType() ;
	
	

}
