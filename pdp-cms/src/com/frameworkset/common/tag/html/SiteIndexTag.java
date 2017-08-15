package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * <p>
 * Title: SiteIndexTag.java
 * </p>
 * 
 * <p>
 * Description:获取站点首页地址
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: bbossgroups
 * </p>
 * 
 * @Date 2012-8-3 下午4:10:36
 * @author biaoping.yin
 * @version 1.0.0
 */
public class SiteIndexTag extends CMSBaseTag{
	

	@Override
	public int doStartTag() throws JspException {
		
		int ret = super.doStartTag();
		
		try {			
			Site site_ = null;
			if (site == null || "".equals(site)) {
				site_ = context.getSite();
			} else {
				site_ = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				// site = String.valueOf(st.getSiteId());
			}
			
//			Site site_ = context.getSite();

			if (site_ == null)
				return ret;
			String siteid = site_.getSiteId() + "";
			String indexURL = CMSTagUtil.getPublishedSitePath(siteid);
			out.print(indexURL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void doFinally() {
		
		super.doFinally();
	}

}
