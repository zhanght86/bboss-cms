package com.frameworkset.common.tag.html;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>
 * Title: SiteInfoTag.java
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
 * Company: 三一集团
 * </p>
 * 
 * @Date 2012-8-3 下午4:14:09
 * @author biaoping.yin
 * @version 1.0.0
 */
public class SiteInfoTag  extends CMSBaseTag{
	private String property;

	@Override
	public int doStartTag() throws JspException {
		
		int ret = super.doStartTag();
		try {

			if (site == null || "".equals(site)) {
				site = context.getSiteID();
			} else {
				// Site st = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				// site = String.valueOf(st.getSiteId());
			}

			Site site_ = context.getSite();

			if (site_ == null)
				return ret;

			
			
			if (property == null)
				property = "name";
			Object v = ValueObjectUtil
					.getValue(site_, property);
			String value =  v == null?null:String.valueOf(v);
			if (value == null)
				value = "站点属性[" + property + "]未指定";			
				
			out.print(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public void doFinally() {
		this.property = null;
		super.doFinally();
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
