package com.frameworkset.platform.cms.container;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 
 * <p>Title: Container</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-6-21 22:25:52
 * @author biaoping.yin
 * @version 1.0
 */
public interface Container extends java.io.Serializable {
	/**
	 * 初始化内容管理系统容器,在模板中使用
	 * @param request
	 * @param session
	 * @param response
	 */
	public void init(HttpServletRequest request,
			 HttpSession session,
			 HttpServletResponse response) throws ContainerException;
	
	/**
	 * 初始化内容管理系统容器，在动态页面上使用，必须指定站点的英文名称
	 * @param site 站点英文名称
	 * @param request
	 * @param session
	 * @param response
	 */
	public void init(String site,HttpServletRequest request,
			 HttpSession session,
			 HttpServletResponse response) throws ContainerException;
	
	/**
	 * 初始化内容管理系统容器，在动态页面上使用，必须指定站点的英文名称
	 * @param site 站点英文名称
	 * @param request
	 * @param session
	 * @param response
	 */
	public void initWithSiteid(String siteid,HttpServletRequest request,
			 HttpSession session,
			 HttpServletResponse response) throws ContainerException;
	/**
	 * 添加链接以便系统能够自动发布出去，通过
	 * @param link
	 */
	public void addLink(String link)throws ContainerException;
	
	/**
	 * 添加链接以便系统能够自动发布出去，通过
	 * @param link
	 * @from 1-webprj
	 *       2-template
	 *       
	 */
	public void addLink(String link,int from)throws ContainerException;
	
	/**
	 * 返回当前发布上下文中的频道对象
	 * @return
	 */
    public Channel getContextChannel()throws ContainerException;
    
    /**
     * 获取当前上下文中的站点对象
     * @return
     */
    public Site getContextSite()throws ContainerException;
    
    /**
     * 获取当前发布上下文中的文档对象
     * @return
     */
    public Document getContextDocument()throws ContainerException;
    
    /**
     * 获取当前发布上下文对象
     * @return
     */
    public Context getContext()throws ContainerException;
    
    /**
	 * 获取当前频道的父频道
	 * @return
	 * @throws ContainerException
	 */
    public Channel getContextParentChannel() throws ContainerException;
    
   
    /**
	 * 获取当前发布文档的路径
	 * @param channelid
	 * @param documentid
	 * @return
	 */
    public String getPublishedDocumentUrl(String channelid,String documentid) throws ContainerException;
    
    /**
	 * 获取频道的发布路径
	 * @param channel 频道显示名称
	 * @return
	 */
    public String getPublishedChannelUrlByDisplayName(String channel) throws ContainerException;
    
    /**
     * 通过频道id获取频道的发布路径
     * @param channelid
     * @return
     * @throws ContainerException
     */
    public String getPublishedChannelUrlByID(String channelid) throws ContainerException;
    
    
    /**
	 * 通过站点名称获取站点的首页地址
	 * @param siteName
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteIndexUrlBySiteName(String siteName) throws ContainerException;
	
	/**
	 * 通过站点名称获取站点的域名
	 * @param siteName
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteDomainBySiteName(String siteName) throws ContainerException;
	
	/**
	 * 通过站点id获取站点的域名
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteDomainBySiteID(String siteID) throws ContainerException;
	
	/**
	 * 通过站点id获取站点的首页地址
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public String getPulishedSiteIndexUrlBySiteID(String siteID) throws ContainerException;
	
	/**
	 * 通过频道id获频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getChannel(String channelid) throws ContainerException;
	
	
	/**
	 * 通过频道id获父频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getParentChannel(String channelid) throws ContainerException;
	
	/**
	 * 通过频道显示名称获频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getChannelByDisplayName(String channel) throws ContainerException;
	
	
	/**
	 * 通过频道显示名称获父频道信息
	 * @param siteID
	 * @return
	 * @throws ContainerException 
	 */
	public Channel getParentChannelByDisplayName(String channel) throws ContainerException;
	
	/**
	 * 获取频道给定层次的祖先
	 * @param channel 频道显示名称
	 * @return
	 */
	public Channel getAncestorChannel(Channel current,int level) throws ContainerException ;
	
	/**
	 * 获取当前频道的给定层次的祖先频道
	 * @param channel 频道显示名称
	 * @return
	 */
	public Channel getContextAncestorChannel(int level) throws ContainerException ;

	public String getPublishedDocumentUrl(Document document) throws ContainerException ;
	
	/**
	 * 通过文档id获取文档的发布细览访问地址路径
	 * @param documentid
	 * @return
	 * @throws ContainerException
	 */
	public String getPublishedDocumentUrl(String documentid) throws ContainerException;

	/**
	 * 通过
	 * @param defaultctx
	 */
	public void init(Context defaultctx) throws ContainerException ;
	
	public String getPublishedChannelUrlByID(Channel channel) throws ContainerException;
	
}
