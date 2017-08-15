package com.frameworkset.common.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.InternalImplConverter;

/**
 * <p>
 * Title: CMSBaseCellTag.java
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
 * @Date 2012-7-16 上午11:00:21
 * @author biaoping.yin
 * @version 1.0.0
 */
public class CMSBaseCellTag extends BaseCellTag{
	
	protected CMSServletRequest cmsrequest;
	protected CMSServletResponse cmsresponse;
	protected String channeldir ;
	protected String channel ;
	protected String site;
	
	
	
	public void setPageContext(PageContext pageContext)
	{
		super.setPageContext(pageContext);
		cmsrequest = InternalImplConverter.getInternalRequest(this.request);
		cmsresponse = InternalImplConverter.getInternalResponse(this.response);
		if(cmsrequest != null)
		{
			this.context = (Context)this.cmsrequest.getContext();
		}
		

		/**
		 * 如果context 不存在，表示当前运行的环境不是系统发布的环境，则采用缺省的上下文环境以便
		 * 系统能够正常的运转
		 */
		if(this.context == null)
		{
			context = new DefaultContextImpl(request,response);
			
		}
	}

	public int doStartTag() throws JspException
	{
		int ret = super.doStartTag();
		if(context instanceof DefaultContextImpl)
		{
//			if(site == null || site.equals(""))
//				throw new JspException("没有指定站点名称，请检查对应的模板和文件是否设置了site属性。");
			if(site != null)
				((DefaultContextImpl)context).setSite(site);
		}
		
		
		return ret;
	}

	public String getSite() {
		return site;
	}



	public void setSite(String site) {
		this.site = site;
	}
	
	public int doEndTag() throws JspException{
		
		return super.doEndTag();
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	@Override
	public void doFinally() {
		context = null;
		cmsrequest = null;
		cmsresponse = null;
		channeldir = null;
		channel = null;
		site = null;
		super.doFinally();
	}

}
