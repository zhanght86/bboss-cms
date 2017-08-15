package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.common.tag.CMSTagUtil;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.StringUtil;

/**
 * <p>
 * Title: SiteDomainTag.java
 * </p>
 * 
 * <p>
 * Description:
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
 * @Date 2012-8-11 下午4:54:33
 * @author biaoping.yin
 * @version 1.0.0
 */
public class SiteDomainTag extends CMSBaseTag {
	@Override
	public int doStartTag() throws JspException {
		
		int ret = super.doStartTag();
		
		try {		
			
			Site site_ = null;
			if (StringUtil.isEmpty(site)) {
				String siteId = request.getParameter("siteId");
				if(StringUtil.isEmpty(siteId))
				{
					site_ = context.getSite();
				}
				else
				{
					site_  = CMSUtil.getSite(siteId);
				}
				
			} else {
				site_ = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				// site = String.valueOf(st.getSiteId());
			}
			
//			Site site_ = context.getSite();

			if (site_ == null)
				return ret;
			String site = site_.getSecondName();
			String indexURL = CMSTagUtil.getPublishedSitePath(request,response,site);
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
