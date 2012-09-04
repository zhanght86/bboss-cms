package com.frameworkset.platform.cms.driver.taglib;

import javax.servlet.jsp.JspException;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.TemplateServletResponse;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.TemplatePublishObject;
import com.frameworkset.common.tag.BaseTag;

/**
 * <p>Title: com.frameworkset.platform.cms.taglib.TemplateTag.java</p>
 *
 * <p>Description: 模版引用标签，计算模版内容</p>
 *
 * <p>Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-29
 * @author biaoping.yin
 * @version 1.0
 */
public class TemplateTag extends BaseTag implements java.io.Serializable {


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 模版id
	 */
	private String templateid;
	/**
	 * 存放模版对应的视图id,相关的样式配置通过viewid关联
	 */
	private String viewid;
	
	private Context context;
	
	public int doStartTag() throws JspException
	{
		CMSServletRequest cmsRequest = (CMSServletRequest)super.request;
		/**
		 * 获取模版引用标签所在的模版的context
		 */
		context = cmsRequest.getContext();
		CMSServletResponse cmsResponse = (CMSServletResponse)super.response;
		TemplateServletResponse tplResponse = new TemplateServletResponse(cmsResponse,cmsRequest);
		try {
			PublishObject publishObject = new TemplatePublishObject(cmsRequest,tplResponse,
																	new String[] {this.getTemplateid(),this.getViewid()},
																	context);
			context.getDriverConfiguration().getPublishEngine().publish(publishObject);
		} catch (PublishException e) {
			
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public String getViewid() {
		return viewid;
	}

	public void setViewid(String viewid) {
		this.viewid = viewid;
	} 
}
