package com.frameworkset.platform.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.config.BaseSystemInit;
import com.frameworkset.platform.config.DestroyException;
import com.frameworkset.platform.config.InitException;

/**
 * 内容管理加载程序
 * <p>Title: CMSInit</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-5-17 19:03:18
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSInit extends BaseSystemInit implements java.io.Serializable{
	private static final Logger log = LoggerFactory.getLogger(CMSInit.class);
	public void init() throws InitException{
		/**
		 * 初始化站点缓冲信息
		 */
		log.debug("加载站点及站点频道信息开始.可能需要几分钟的时间，请稍等.");
		SiteCacheManager.getInstance();
		log.debug("加载站点及站点频道信息结束.");
		
	}

	public void destroy() throws DestroyException{
		
		
	}
  
}
