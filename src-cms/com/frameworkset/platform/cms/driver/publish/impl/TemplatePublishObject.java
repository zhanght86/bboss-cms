package com.frameworkset.platform.cms.driver.publish.impl;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.TemplateContext;
import com.frameworkset.platform.cms.driver.context.impl.TemplateContextImpl;
import com.frameworkset.platform.cms.driver.distribute.Distribute;
import com.frameworkset.platform.cms.driver.distribute.DistributeException;
import com.frameworkset.platform.cms.driver.distribute.DistributeWraper;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.JspFile;
import com.frameworkset.platform.cms.driver.jsp.JspletWindow;
import com.frameworkset.platform.cms.driver.jsp.JspletWindowImpl;
import com.frameworkset.platform.cms.driver.jsp.TemplateServletResponse;
import com.frameworkset.platform.cms.driver.publish.PublishException;
import com.frameworkset.platform.cms.driver.publish.PublishObject;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.publish.impl.TemplatePublishObject.java</p>
 *
 * <p>
 * Description: 模版发布对象，对模版进行解析
 * 				生成相应的html代码快并且输出到reponse.getWriter()中，同时分发
 * 				该模板对应的附件
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-22
 * @author biaoping.yin
 * @version 1.0 
 */
public class TemplatePublishObject extends PublishObject implements java.io.Serializable {
	 
	private HttpServletRequest cmsRequest;
	private TemplateServletResponse templateResponse;
	/**
	 * String[] {templateid,viewid}
	 * 存放模版id和模版对应的视图id
	 */
	private String[] params;
	
	
	public TemplatePublishObject(HttpServletRequest cmsRequest, 
								 TemplateServletResponse templateResponse,
								 String[] params, 
								 Context parentContext)
	{
		this.parentContext = parentContext;
		this.cmsRequest = cmsRequest;
		this.templateResponse = templateResponse;
		this.params = params;
	}

	public Context buildContext() throws PublishException {
		return buildTemplateContext();
	}
	
	private TemplateContext buildTemplateContext() throws PublishException 
	{
		if(params == null)
			throw new TemplatePublishException("发布模版错误：参数params=null" );
		TemplateContext context = new TemplateContextImpl(params[0],params[1],
														  params[2],
														  parentContext,
														  this,
														  parentContext.getPublishMonitor().createSubPublishMonitor());
		return context;
	}

	public void initScriptlet() throws TemplatePublishException {
//		try {
			context.getPublishMonitor().addSuccessMessage("初始化模版脚本-templateid=".concat(params[0]).concat(",viewid=").concat(params[1]).concat("结束"),new Date(),this.context.getPublisher());
			Template template = ((TemplateContext)this.context).getTemplate();
			
			this.script = ScriptletUtil.createScriptlet(context,template);			
			
//		} catch (TemplateManagerException e) {
//			context.getPublishMonitor().addFailedMessage("初始化发布模版脚本-templateid=".concat(params[0]).concat(",viewid=").concat(params[1]).concat("失败:获取模版信息异常"),
//														  new Date(),
//														  this.context.getPublisher()
//														  );
//			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
//			throw new TemplatePublishException(e.getMessage());
//		} catch (DriverConfigurationException e) {
//			context.getPublishMonitor().addFailedMessage("初始化发布模版脚本-templateid=".concat(params[0]).concat(",viewid=").concat(params[1]).concat("失败:获取模版信息异常"),new Date(),this.context.getPublisher());
//			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
//			throw new TemplatePublishException(e.getMessage());
//		}
	}
	
	private void publishTemplate() throws TemplatePublishException
	{
		JspFile jspFile = script.getJspFile();
//		CMSURL cmsUrl = requestContext.getRequestedCmsURL();
		//提供action的特殊处理
//		CMSRequestContext jspFileRequestContext = new CMSRequestContext(requestContext,jspFile);
		String uri = jspFile.getUri();
		JspletWindow jspWindow = new JspletWindowImpl(context,jspFile);
		RequestDispatcher dispatcher = this.cmsRequest.getRequestDispatcher(uri);
		CMSServletRequest request = new CMSServletRequest(this.cmsRequest,requestContext.getPageContext(),jspWindow,this.context);
		/**
		 * 使用模版标签传递过来的response
		 */

        try {
			dispatcher.include(request, this.templateResponse);
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_COMPLETED);
			context.getPublishMonitor().addSuccessMessage("发布模版-templateid=".concat(params[0]).concat(",viewid=").concat(params[1]).concat("结束"),
					  new Date(),
					  context.getPublisher());
        } catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
			throw new TemplatePublishException(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_FAILED);
			throw new TemplatePublishException(e.getMessage());
		}
		 try {
			/**
			 * 分发模版的附件,对于模版的内容则无需处理
			 */
			Distribute distribute = new DistributeWraper();
			script.setContent(null);
			distribute.init(context,jspFile);
			distribute.distribute();
		    context.getPublishMonitor().setPublishStatus(PublishMonitor.DISTRIBUTE_COMPLETED);
		    context.getPublishMonitor().addSuccessMessage("分发模版附件-templateid=".concat(params[0]).concat(",viewid=").concat(params[1]).concat("结束"),new Date(),context.getPublisher());
		 }
			
		 catch (DistributeException e) {
			context.getPublishMonitor().setPublishStatus(PublishMonitor.DISTRIBUTE_FAILED);
			context.getPublishMonitor().addFailedMessage("分发模版附件-templateid=".concat(params[0]).concat(",viewid=").concat(params[1]).concat("失败"),new Date(),context.getPublisher());
			throw new TemplatePublishException(e.getMessage());
		}
	}
	
	

	public void doPublish() throws TemplatePublishException {
		//设置发布状态
		context.getPublishMonitor().setPublishStatus(PublishMonitor.PUBLISH_STARTED);
		context.getPublishMonitor().addSuccessMessage("开始发布模版-templateid=".concat(params[0]).concat(",viewid=").concat(params[1]),
				  new Date(),
				  context.getPublisher());
		publishTemplate();		
	}

	public String getId() {
		return "";
	}



	public void recordRecursivePubObj(String refobj, int reftype, String site) {
		
		
	}

	public Set getRecursivePubObject() {
		
		return null;
	}

	protected void delteRefObjects() {
		
		
	}

	


}
