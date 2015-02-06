package com.frameworkset.platform.cms.driver.context.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;


/**
 * 缺省的上下文对象，用于无上下文环境时的发布情景，比如无需发布的首页上
 * 扩站点引用其他站点的频道文档概览列表时，需要将原发布上下文转化为缺省上下文
 * 并将原来的上下文保存在缺省上下文的old_属性中，
 * <p>Title: DefaultContextImpl</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-26 8:12:29
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultContextImpl extends BaseContextImpl {
	Site siteinfo ;
	Context old_;
//	public DefaultContextImpl(Context parentContext, PublishObject publishObject, PublishMonitor monitor) {
//		super(parentContext, publishObject, monitor);
//	}
	
	/** 
	 * 
	 */
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	
	public DefaultContextImpl(HttpServletRequest request,HttpServletResponse response)
	{
		super();
		this.request = request;
		this.response = response;
		
	}
	
	/**
	 * 
	 * @param site 站点英文名称
	 * @param request
	 * @param response
	 */
	public DefaultContextImpl(String site,HttpServletRequest request,HttpServletResponse response)
	{
		super();
		this.request = request;
		this.response = response;
		
//		this.site
		try {
			this.siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public CMSRequestContext getRequestContext()
	{
		if(this.parentContext == null)
			return null;
		return this.parentContext.getRequestContext();
	}
	public DefaultContextImpl(Context parent)
	{
		
		super();
		parentContext = parent;
		this.request = parent.getRequestContext().getRequest();
		this.response = parent.getRequestContext().getResponse();
//		this.site
		try {
			this.siteinfo = parentContext.getSite();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public DefaultContextImpl(String siteid) {
		this.siteID = siteid;
		if(siteID != null && !siteID.equals(""))
		{
			try {
				this.siteinfo = CMSUtil.getSiteCacheManager().getSite(siteID);
				this.siteDir = siteinfo.getSiteDir();
				this.dbName = siteinfo.getDbName();		
				this.domain = siteinfo.getWebHttp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public DefaultContextImpl(Site site) {		 
		try {
			this.siteID = site.getSiteId()+"";
			this.siteinfo = site;
			this.siteDir = siteinfo.getSiteDir();
			this.dbName = siteinfo.getDbName();		
			this.domain = siteinfo.getWebHttp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		
	}


	/**
	 * 
	 * @param site 站点英文名称
	 * @param requestContext
	 */
	public DefaultContextImpl(String site, CMSRequestContext requestContext) {
		this(site,requestContext.getRequest(),requestContext.getResponse());
		
	}

	public void setSite(String site)
	{
		if(site != null && !site.equals(""))
		{
			try {
				this.siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				this.siteDir = siteinfo.getSiteDir();
				this.dbName = siteinfo.getDbName();
				this.siteID = siteinfo.getSiteId() + "";
				this.domain = siteinfo.getWebHttp();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/**
	 * 获取动态发布时的发布上下文
	 * @param linkPath
	 * @return
	 */
	public String getPublishedLinkPath(String linkPath)
	{
		if(linkPath != null)
		{
			if(linkPath.toLowerCase().startsWith("http://") || linkPath.toLowerCase().startsWith("https://")
					|| linkPath.toLowerCase().startsWith("ftp://"))
				return linkPath;
			if(siteinfo.getPublishDestination() == 0 || siteinfo.getPublishDestination() == 2)//本地发布
			{
				//System.out.println("siteinfo.getWebHttp():"+siteinfo.getWebHttp());
				boolean a = siteinfo.getLocalPublishPath() == null || siteinfo.getLocalPublishPath().equals("");
				boolean b = this.siteinfo.getWebHttp() == null || this.siteinfo.getWebHttp().equals("")
				|| this.siteinfo.getWebHttp().equals("http://");
				if(a && b)
				{
					if( this.siteinfo.getWebHttp() == null || this.siteinfo.getWebHttp().equals("")
							|| this.siteinfo.getWebHttp().equals("http://"))
					{
						if(request != null)
						{
							return CMSUtil.getPath(this.request.getContextPath() + "/sitepublish/" + this.siteinfo.getSiteDir(),linkPath);
						}
						else
						{
							return CMSUtil.getPath("/sitepublish/" + this.siteinfo.getSiteDir(),linkPath);
						}
					}
					else
					{
						return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
					}
				}
				else
				{
					return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
				}
			}
			
			else
			{
				return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
			}
		}
		else
		{
			
			if(siteinfo.getPublishDestination() == 0 || siteinfo.getPublishDestination() == 2)//本地发布
			{
				//System.out.println("siteinfo.getWebHttp():"+siteinfo.getWebHttp());
				boolean a = siteinfo.getLocalPublishPath() == null || siteinfo.getLocalPublishPath().equals("");
				boolean b = this.siteinfo.getWebHttp() == null || this.siteinfo.getWebHttp().equals("")
				|| this.siteinfo.getWebHttp().equals("http://");
				if(a && b)
				{
					if( this.siteinfo.getWebHttp() == null || this.siteinfo.getWebHttp().equals("")
							|| this.siteinfo.getWebHttp().equals("http://"))
					{
						if(request != null)
						{
							return this.request.getContextPath() + "/sitepublish/" + this.siteinfo.getSiteDir();
						}
						else
						{
							return "/sitepublish/" + this.siteinfo.getSiteDir();
						}
					}
					else
					{
						return this.siteinfo.getWebHttp();
					}
				}
				else
				{
					return this.siteinfo.getWebHttp();
				}
			}
			
			else
			{
				return this.siteinfo.getWebHttp();
			}
		}
	}
	
	/**
	 * RSS 获取动态发布时的发布全路径 
	 * @param linkPath
	 * @return 
	 * DefaultContextImpl.java
	 * @author: ge.tao
	 */
	public String getRssPublishedLinkPath(String linkPath)
	{
		if(linkPath.toLowerCase().startsWith("http://") || linkPath.toLowerCase().startsWith("https://")
				|| linkPath.toLowerCase().startsWith("ftp://"))
			return linkPath;
		if(siteinfo.getPublishDestination() == 0 || siteinfo.getPublishDestination() == 2)//本地发布
		{
			
			if(siteinfo.getLocalPublishPath() == null || siteinfo.getLocalPublishPath().equals("") )
			{
				String prex = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
				if( this.siteinfo.getWebHttp() == null || this.siteinfo.getWebHttp().equals(""))
					return CMSUtil.getPath(prex + this.request.getContextPath() + "/sitepublish/" + this.siteinfo.getSiteDir(),linkPath);
				else
				{
					return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
				}
			}
			else
			{
				return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
			}
		}
		
		else
		{
			return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
		}
	}
	
	public Site getSite()
	{
		return this.siteinfo;
	}
	
	public String getSiteID() {
		return this.siteinfo.getSiteId() + "";
	}
	
	public String getSiteDir()
	{
		return this.siteinfo.getSiteDir();
	}

	public Context getOldContext() {
		return old_;
	}

	public void setOldContext(Context old_) {
		this.old_ = old_;
	}
	
	

//	public CMSLink getPublishedLink(String linkPath) {
//		if(siteinfo.getPublishDestination() == 0 || siteinfo.getPublishDestination() == 2)//本地发布
//		{
//			
//			if(siteinfo.getLocalPublishPath() == null || siteinfo.getLocalPublishPath().equals("") )
//			{
//				if( this.siteinfo.getWebHttp() == null || this.siteinfo.getWebHttp().equals(""))
//					return CMSUtil.getPath(this.request.getContextPath() + "/sitepublish/site" + this.siteinfo.getSiteId(),linkPath);
//				else
//				{
//					return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
//				}
//			}
//			else
//			{
//				return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
//			}
//		}
//		
//		else
//		{
//			return CMSUtil.getPath(this.siteinfo.getWebHttp(),linkPath);
//		}
//	}
}
