package com.frameworkset.platform.cms.container;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.jsp.CMSServletRequest;
import com.frameworkset.platform.cms.driver.jsp.CMSServletResponse;
import com.frameworkset.platform.cms.driver.jsp.InternalImplConverter;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.SimpleStringUtil;

/**
 * 内容管理系统模板容器接口实现类
 * <p>Title: ContainerImpl</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-6-22 9:29:27
 * @author biaoping.yin
 * @version 1.0
 */
public class ContainerImpl implements Container{
	HttpServletRequest request; 
	HttpSession session;
	HttpServletResponse response;
	CMSServletRequest cmsrequest;
	CMSServletResponse cmsresponse; 
	Context context = null;
	
	public static final int LINK_FROM_WEBPRJS = 1;
	public static final int LINK_FROM_TEMPLATE = 2;
	/**
	 * 初始化内容管理系统容器，在动态页面上使用，必须指定站点的英文名称
	 * @param site 站点英文名称
	 * @param request
	 * @param session
	 * @param response
	 */
	public void initWithSiteid(String siteid,HttpServletRequest request,
			 HttpSession session,
			 HttpServletResponse response) throws ContainerException
	 {
			try {
				String site = CMSUtil.getSiteCacheManager().getSite(siteid).getSecondName();
				this.init(site, request, session, response);
			} catch (Exception e) {
				throw new ContainerException(e);
			}
			
	 }
	/**
	 * 本方法在模板中调用
	 */
	public void init(HttpServletRequest request, 
					 HttpSession session, 
					 HttpServletResponse response) throws ContainerException {
		this.request = request;
		this.session = session;
		this.response = response;
		this.cmsrequest = InternalImplConverter.getInternalRequest(request);
		this.cmsresponse = InternalImplConverter.getInternalResponse(response);		
		if(cmsrequest != null)
			context = cmsrequest.getContext();
		if(context == null)
			throw new ContainerException("获取上下文异常，请检查是否在发布上下文中使用容器，如果不是请调用方法[init(String site, HttpServletRequest request, HttpSession session, HttpServletResponse response)]");
	}
	/**
	 * 检查容器是否被正确的初始化，没有就抛容器未初始化异常
	 */
	private void check() throws ContainerException
	{
		if(request == null)
			throw new ContainerException("容器未初始化异常，请调用方法[init]进行初始化。");
			
	}
	public void addLink(String link) throws ContainerException{
//		check();
//		if(link != null && this.context != null)
//		{
//			String templatePath = CMSUtil.getTemplatePath(context);
//			if(templatePath == null)
//				templatePath = "";
//			CMSUtil.addPublishLink(context,templatePath,link);			
//		}
		addLink(link, LINK_FROM_TEMPLATE);	
	}

	public Channel getContextChannel() throws ContainerException {
		check();		
		return CMSUtil.getCurrentChannel(context);
	}
	
	/**
	 * 获取当前频道的父频道
	 * @return
	 * @throws ContainerException
	 */
	public Channel getContextParentChannel() throws ContainerException {
		check();
		Channel channel = CMSUtil.getCurrentChannel(context);
		try {
			return channel = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channel.getParentChannelId() + "");
		} catch (Exception e) {
			throw new ContainerException();
		}
	}

	public Site getContextSite() throws ContainerException {
		check();
		
		return CMSUtil.getCurrentSite(context);
	}

	public Document getContextDocument() throws ContainerException {
		check();
		
		return CMSUtil.getCurrentDocuemnt(context);
	}
	public Context getContext() throws ContainerException {
		check();
		return context;
	}
	
	/**
	 * 获取当前发布文档的路径
	 * @param documentid
	 * @return
	 */
	public String getPublishedDocumentUrl(String channelid,String documentid) throws ContainerException
	{
		check();
		String channeldir;
		try {
			channeldir = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channelid).getChannelPath();

			String content = CMSUtil.getPublishedContentPath(context,channeldir,documentid);
			return content;
		} catch (Exception e) {
			throw new ContainerException(e);
		}
		
	}
	
	/**
	 * 获取站点的web访问地址
	 * @param request 页面请求对象
	 * @param response 页面响应对象
	 * @param siteename 站点英文名称
	 * @return
	 */
	public static String getSiteUrl(HttpServletRequest request,HttpServletResponse response,String siteename)	
	{
		DefaultContextImpl context = new DefaultContextImpl(siteename,request,response);
		String siteindex = "";
		try {
			siteindex = CMSUtil.getSiteCacheManager().getSiteByEname(siteename).getIndexFileName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(siteindex == null || siteindex.equals(""))
			siteindex = "index.htm";
		return context.getPublishedLinkPath(siteindex);
	}
	
	/**
	 * 获取频道的访问地址
	 * @param request 页面请求对象
	 * @param response 页面相应对象
	 * @param siteid 站点id
	 * @param channelid 频道id
	 * @return
	 */
	public static String getChannelUrl(HttpServletRequest request,HttpServletResponse response,String siteid,String channelid)	
	{
		String siteename = "";
		try {
			siteename = CMSUtil.getSiteCacheManager().getSite(siteid).getSecondName();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(siteename.equals(""))
			return "";
		DefaultContextImpl context = new DefaultContextImpl(siteename,request,response);
		
		try {
			
			return CMSUtil.getPublishedChannelPath(context,CMSUtil.getChannelCacheManager(siteid).getChannel(channelid));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取文档的web访问地址
	 * @param request 页面请求对象
	 * @param response 页面相应对象
	 * @param siteid 站点id
	 * @param channelid 频道id
	 * @param documentid 文档id
	 * @return
	 */
	public static String getDocumentUrl(HttpServletRequest request,HttpServletResponse response,String siteid,String channelid,String documentid)	
	{
		String siteename = "";
		try {
			siteename = CMSUtil.getSiteCacheManager().getSite(siteid).getSecondName();
			String channeldir = CMSUtil.getChannelCacheManager(siteid).getChannel(channelid).getChannelPath();
			return CMSUtil.getPublishContentPath(siteename,channeldir,documentid,request,response);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 获取频道的路径
	 * @param channel
	 * @return
	 */
	public String getPublishedChannelUrlByDisplayName(String channel) throws ContainerException
	{
		check();		
		try {
			return CMSUtil.getPublishedChannelPath(context, CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(channel));
		} catch (Exception e) {
			throw new ContainerException(e);
		}
		
	}
	/**
	 * 获取频道给定层次的祖先
	 * @param channel 频道显示名称
	 * @return
	 */
	public Channel getAncestorChannel(Channel current,int level) throws ContainerException {
		check();
		return CMSUtil.getParentChannelWithLevel( current, level);
	}
	
	/**
	 * 获取当前频道的给定层次的祖先频道
	 * @param channel 频道显示名称
	 * @return
	 */
	public Channel getContextAncestorChannel(int level) throws ContainerException {
		check();
		return getAncestorChannel(this.getContextChannel(),level);
	}
	public String getPublishedChannelUrlByID(String channelid) throws ContainerException {
		check();		
		try {
			return CMSUtil.getPublishedChannelPath(context, CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channelid));
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	public String getPublishedChannelUrlByID(Channel channel) throws ContainerException {
		check();		
		try {
			return CMSUtil.getPublishedChannelPath(context, channel);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	public void init(String site, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws ContainerException {
		this.request = request;
		this.session = session;
		this.response = response;
		this.cmsrequest = InternalImplConverter.getInternalRequest(request);
		this.cmsresponse = InternalImplConverter.getInternalResponse(response);		
		if(cmsrequest != null)
			context = cmsrequest.getContext();
		if(site == null)
			throw new ContainerException("站点英文名称不能为空");
		if(context == null)
			context = new DefaultContextImpl(site,request,response);
		
	}
	
	public void init(Context context) throws ContainerException {
		this.request = context.getRequestContext().getRequest();
		this.session = context.getRequestContext().getRequest().getSession();
		this.response =context.getRequestContext().getResponse();
		this.cmsrequest = InternalImplConverter.getInternalRequest(request);
		this.cmsresponse = InternalImplConverter.getInternalResponse(response);		
		this.context = context;
		
		
	}
	
	/**
	 * 通过站点名称获取站点的首页地址
	 * @param siteName
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteIndexUrlBySiteName(String siteName) throws ContainerException
	{
		this.check();
		try
		{
			Context ctx = new DefaultContextImpl(siteName,request,response);
			Site site = CMSUtil.getSiteCacheManager().getSiteByEname(siteName);
			String indexName = site.getIndexFileName();
			if(indexName == null)
				indexName = "default.htm";
			return CMSUtil.getPublishedSitePath(ctx,indexName);
		}
		catch(Exception e)
		{
			throw new ContainerException(e);
		}
	}
	
	/**
	 * 通过站点id获取站点的首页地址
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteIndexUrlBySiteID(String siteID) throws ContainerException
	{
		this.check();
		try
		{
			Site site = CMSUtil.getSiteCacheManager().getSite(siteID);
			Context ctx = new DefaultContextImpl(site.getSecondName(),request,response);
			
			String indexName = site.getIndexFileName();
			if(indexName == null)
				indexName = "default.htm";
			return CMSUtil.getPublishedSitePath(ctx,indexName);
		}
		catch(Exception e)
		{
			throw new ContainerException(e);
		}
	}
	
	
	/**
	 * 通过站点名称获取站点的域名
	 * @param siteName
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteDomainBySiteName(String siteName) throws ContainerException
	{
		this.check();
		try
		{
			Context ctx = new DefaultContextImpl(siteName,request,response);
			
			
			return CMSUtil.getPublishedSitePath(ctx,null);
		}
		catch(Exception e)
		{
			throw new ContainerException(e);
		}
	}
	
	/**
	 * 通过站点id获取站点的域名
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteDomainBySiteID(String siteID) throws ContainerException
	{
		this.check();
		try
		{
			Site site = CMSUtil.getSiteCacheManager().getSite(siteID);
			Context ctx = new DefaultContextImpl(site.getSecondName(),request,response);
			
			
			return CMSUtil.getPublishedSitePath(ctx,null);
		}
		catch(Exception e)
		{
			throw new ContainerException(e);
		}
	}
	
	
	/**
	 * 通过频道id获频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getChannel(String channelid) throws ContainerException
	{
		this.check();
		try {
			return CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channelid);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	
	/**
	 * 通过频道id获父频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getParentChannel(String channelid) throws ContainerException
	{
		this.check();
		try {
			Channel channel = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channelid);
			return CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channel.getParentChannelId() + "");
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	/**
	 * 通过频道显示名称获频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getChannelByDisplayName(String channel) throws ContainerException
	{
		this.check();
		try {
			return CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(channel);
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	
	/**
	 * 通过频道显示名称获父频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getParentChannelByDisplayName(String channel) throws ContainerException
	{
		this.check();
		try {
			Channel channel_ = CMSUtil.getChannelCacheManager(context.getSiteID()).getChannelByDisplayName(channel);
			return CMSUtil.getChannelCacheManager(context.getSiteID()).getChannel(channel_.getParentChannelId() + "");
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	
	/**
	 * 通过文档对象或者文档的访问地址
	 */
	public String getPublishedDocumentUrl(Document document) throws ContainerException {
		check();
		
		try {
			
			String content = CMSUtil.getPublishedContentPath(context,document);
			return content;
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	
	/**
	 * 通过文档对象或者文档的附件访问地址
	 * @param document 文档对象
	 * @param attachurl 附件的url
	 */
	public String getPublishedDocumentAttachUrl(Document document,String attachurl) throws ContainerException {
		check();
		
		try {
			
			String content = CMSUtil.getPublishedContentAttachPath(context,document,attachurl);
			return content;
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	/**
	 * 通过文档对象或者文档的附件访问地址
	 * @param document 文档对象
	 * @param attachurl 附件的url
	 */
	public String getPublishedDocumentAttachUrlAppendContentFile(Document document,String attachurl) throws ContainerException {
		check();
		
		try {
			if(!attachurl.startsWith(CMSUtil.PUBLISHFILEFORDER))
				attachurl = SimpleStringUtil.getRealPath(CMSUtil.PUBLISHFILEFORDER, attachurl);
			String content = CMSUtil.getPublishedContentAttachPath(context,document,attachurl);
			return content;
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	
	/**
	 * 通过文档id获取文档的发布细览访问地址路径
	 * @param documentid
	 * @return
	 * @throws ContainerException
	 */
	public String getPublishedDocumentUrl(String documentid) throws ContainerException {
		check();
		try {
			Document doc = CMSUtil.getCMSDriverConfiguration()
								  .getCMSService()
								  .getDocumentManager()
								  .getPartDocInfoById(documentid);
			String content = CMSUtil.getPublishedContentPath(context,doc);
			return content;
		} catch (Exception e) {
			throw new ContainerException(e);
		}
	}
	public void addLink(String link, int from) throws ContainerException {
		check();
		if(from == LINK_FROM_TEMPLATE)
		{
			if(link != null && this.context != null)
			{
				String templatePath = CMSUtil.getTemplatePath(context);
				if(templatePath == null)
					templatePath = "";
				CMSUtil.addPublishLink(context,templatePath,link);			
			}
			return ;
		}
		else if (from == LINK_FROM_WEBPRJS)
		{
			if(link != null && this.context != null)
			{
	//			String templatePath = CMSUtil.getTemplatePath(context);
	//			if(templatePath == null)
	//				templatePath = "";
				CMSUtil.addPublishWEBPrjsLink(context,link);			
			}
		}
		
	}
}
