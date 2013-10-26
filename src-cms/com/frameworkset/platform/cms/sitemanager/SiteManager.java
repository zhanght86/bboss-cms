package com.frameworkset.platform.cms.sitemanager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.event.Notifiable;
import org.frameworkset.security.AccessControlInf;

import com.frameworkset.platform.cms.container.Template;

public interface SiteManager extends Notifiable,java.io.Serializable{
	
	/**
	 *	站点是否有子节点 
	 */
	public boolean siteIsExist(String siteName, String secondName)throws SiteManagerException;

	/**
	 *	站点是否有子节点 
	 */	
	public boolean siteIsExist(String siteId) throws SiteManagerException;

	/**
	 *	新建一个站点 
	 */
	public void createSite(Site site) throws SiteManagerException;

	/**
	 * 更新站点
	 */
	public boolean updateSite(Site site) throws SiteManagerException;
	/**
	 * 更新站点状态
	 */
	public void updateSiteStatus(int siteId,int siteState)throws SiteManagerException;

	/**
	 * 根据站点id获取站点信息
	 */
	public Site getSiteInfo(String siteId) throws SiteManagerException;

	/**
	 * 根据站点id获取父站点信息,如果是顶级站点,将返回null
	 */
	public Site getParentSiteInfo(String siteId) throws SiteManagerException;
	/**
	 * 将站点的标志位设为删除 
	 */
	public boolean deleteSite(String siteId) throws SiteManagerException;

	
	/**
	 * 停止站点
	 */
	public boolean stopSite(String siteId) throws SiteManagerException;
	
	/**
	 * 更新站点的工作流程,并记录日志
	 * @param siteId
	 * @param newFlowId
	 * @param workflowIsFromParent
	 * @return
	 * @throws SiteManagerException
	 */

	public boolean changeWorkflow(String siteId,int newFlowId,int workflowIsFromParent)  throws SiteManagerException;
	
	public boolean testFtpLink(String ip, String user, String pwd, short port)throws SiteManagerException;


	/**
	 * 返回所有站点
	 */
	public List getSiteList() throws SiteManagerException;
	/**
	 * 返回站点允许的所有状态(不包括已删除状态)
	 * @return
	 * @throws SiteManagerException
	 */
	public List getSiteStatusList() throws SiteManagerException;
	/**
	 * 判断站点是否是活动的，即站点的status是否为0（开通状态）
	 * @return
	 * @throws SiteManagerException
	 */
	public boolean isActive(String siteId) throws SiteManagerException;
	/**
	 * 获取站点树中顶层站点,也就是没有父站点的站点
	 */
	public List getTopLevelSite() throws SiteManagerException;
	/**
	 * 获取站点树中顶层站点,也就是没有父站点的站点(不受站点状态影响)
	 */
	public List getTopSubSiteList() throws SiteManagerException;

	/**
	 * 站点是否有子站点
	 */
	public boolean hasSubSite(String siteId) throws SiteManagerException;

	/**
	 * 获取直接子节点
	 */
	public List getDirectSubSiteList(String siteId) throws SiteManagerException;
	
	/**
	 * 获取直接子站点(不受站点状态影响)
	 */
	public List getSubSiteList(String siteId) throws SiteManagerException;

	/**
	 * 获取所有子节点
	 */
	public List getAllSubSiteList(String siteId) throws SiteManagerException;
	

	
	/**
	 * 获取站点的允许发布的状态
	 * @param siteId
	 * @return List<DocumentStatus>
	 * @throws SiteManagerException
	 */
	public List getEnablePublishStatus(String siteId) throws SiteManagerException;
	
	/**
	 * 获取站点拥有的顶级频道
	 */
	public List getDirectChannelsOfSite(String siteId) throws SiteManagerException;

	/**
	 * 获取站点对应的模板
	 */
	public List getTemplatesOfSite(String siteId) throws SiteManagerException;

	/**
	 * 获取站点拥有的所有频道
	 */
	public List getAllChannelsOfSite(String siteId) throws SiteManagerException;
	
	/**
	 * 判断站点下有没有频道
	 */
	public boolean hasChannelOfSite(String siteId) throws SiteManagerException;
	
	/**
	 * 判断站点有没有关联模板
	 */
	public boolean hasChannelOfTemplate(String siteId) throws SiteManagerException;

	
	/**
	 * 是否设置了首页模板
	 * @param siteId
	 * @return
	 * @throws SiteManagerException
	 */
	public boolean hasSetIndexTemplate(String siteId) throws SiteManagerException;
	
	/**
	 * 获取站点支持的发布状态
	 */
	public List getSupportedSitePublishStatus(String siteId)
			throws SiteManagerException;

	/**
	 * 获取首页模板
	 */
	public Template getIndexTemplate(String siteId)throws SiteManagerException;
	
	/**
	 * 分发方式数组
	 * 以逗号分割的数字值:0表示html,1表示rss，2表示mail,缺省为0
	 */
	public int[] getSiteDistributeManners(String siteId)throws SiteManagerException;
	/**
	 * 定义发布目的地
	 * 0:本地1:远程2:远程本地
	 */
	public boolean[] getSitePublishDestination(String siteId)throws SiteManagerException;
	
	/**
	 * 返回当前站点的流程id,和名称.没有的话返回null
	 * @param siteId
	 * @return
	 * @throws SiteManagerException
	 */
	public List getFlowInfo(String siteId)throws SiteManagerException;
	
	public void logCreateSite(Site site,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException;
	public boolean logDeleteSite(String siteId,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException;
	public boolean logStopSite(String siteId,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException;
	public boolean logUpdateSite(Site site,HttpServletRequest request, HttpServletResponse response) throws SiteManagerException;
	
	/**
	 * 根据站点id返回站点在文件系统中的路径
	 * @param siteId
	 * @return
	 * @throws SiteManagerException
	 */
	public String getSiteAbsolutePath(String siteId) throws SiteManagerException;
	/**
	 * 传入一条sql返回站点列表
	 */
	public List getSiteList(String sql) throws SiteManagerException;
	/**
	 * 返回站点状态为0（开通）并且当前用户有权限的站点列表
	 */
	public List getSiteAllRuningList(boolean isAdmin,String userId) throws SiteManagerException;
	/**
	 * 保存用户登陆默认站点
	 */
    public void defaultSite(String siteId,String userId) throws SiteManagerException;
    /**
	 * 取当前用户默认登陆站点
	 */
    public String userDefaultSite(String userId) throws SiteManagerException;
    /**
	 * 取得该站点下所有有权限的用户id
	 * @author xinwang.jiao
	 * @param siteId
	 * @return String[] String[0] 为userids,String[1] 为usernames，String[2] 为“user”
	 * @throws SiteManagerException
	 */
	public String[] getUsersOfSite(String siteId) throws SiteManagerException;
	/**
	 * 取得该站点下所有有权限的角色id
	 * @author xinwang.jiao
	 * @param siteId
	 * @return String[] String[0] 为角色id,String[1] 为角色名称，String[2] 为“role”
	 * @throws SiteManagerException
	 */
	public String[] getRolesOfSite(String siteId) throws SiteManagerException;
	
	/**
	 * 判断当前用户是不是站点管理员
	 */
	public boolean hasSiteManager(String siteId,String userId) throws SiteManagerException;

	/**
	 * 通过站点名称获取站点信息
	 * @param siteName
	 * @return
	 */
	public Site getSiteInfoBySiteName(String siteName) throws SiteManagerException;

	public List getSiteAllRuningList(AccessControlInf accessControl);
	
}