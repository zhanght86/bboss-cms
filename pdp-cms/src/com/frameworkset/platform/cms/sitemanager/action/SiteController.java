/*
 * @(#)DocumentController.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.cms.sitemanager.action;

import java.util.List;

import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManager;

/**
 * @author gw_hel
 * 站点服务控制器
 */
public class SiteController {

	private SiteManager siteManager;

	/**
	 * 获取所有站点信息
	 * @return List<Site>
	 */
	@SuppressWarnings("unchecked")
	public @ResponseBody(datatype = "json")
	List<Site> getAllSite() throws Exception {
		return siteManager.getSiteList();
	}
}
