package com.frameworkset.common.tag.html;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import org.frameworkset.util.ParamsHandler.Params;

import com.frameworkset.common.tag.CMSBaseTag;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;

/**
 * <p>
 * Title: SiteParamTag.java
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
 * @Date 2012-8-3 下午12:02:12
 * @author biaoping.yin
 * @version 1.0.0
 */
public class SiteParamTag extends CMSBaseTag{
	private String name;
	private String defaultValue;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		int ret = super.doStartTag();
		if (site == null || "".equals(site)) {
			site = context.getSite().getSecondName();
		}
		if(StringUtil.isEmpty(name) )
		{
			if( !StringUtil.isEmpty(defaultValue))
			try {
				out.print(defaultValue);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		else
		{
			Params params = CMSUtil.getSiteParams(site,request);
			String value = params.getAttributeString(name);
			if(StringUtil.isEmpty(value))
			{
				if( !StringUtil.isEmpty(defaultValue))
					try {
						out.print(defaultValue);
					} catch (IOException e) {
						throw new JspException(e);
					}
			}
			else
			{
				try {
					out.print(value);
				} catch (IOException e) {
					throw new JspException(e);
				}
			}
		}
		
		
		return ret;
	}
	@Override
	public void doFinally() {
		// TODO Auto-generated method stub
		name = null;
		defaultValue = null;
		super.doFinally();
	}
}
