package com.frameworkset.platform.portal;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 类说明:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author gao.tang
 * @version V1.0 创建时间：Oct 19, 2009 4:39:27 PM
 */
public interface PortalIssueManager {

	/**
	 * 应用模块iframe发布接口,将应用中的模块发布成iframe portlet
	 * @param portletCount	发布portlet的数
	 * @param request		
	 * @return
	 */
	public boolean appIssueIframePlugin(int portletCount,
			HttpServletRequest request) throws PortalIssueException;

	/**
	 * 自定义iframe发布模块接口，自定义自己要发布的应用模块
	 * @param portletCount	发布的portlet数
	 * @param request
	 * @return
	 */
	public boolean definedIssueIframePlugin(int portletCount,
			HttpServletRequest request) throws PortalIssueException;

}
