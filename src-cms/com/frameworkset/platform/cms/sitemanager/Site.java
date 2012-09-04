package com.frameworkset.platform.cms.sitemanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;

/**
 *  站点基本信息.
 * <p>Title: Site</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2006-9-19 9:14:53
 * @author biaoping.yin
 * @version 1.0
 */

public class Site implements Serializable,Comparable {
	
	private long siteId = 0;	//0 表示无意义

	private String name;

	private String secondName;

	private String siteDesc;
	

	private String siteDir; 	//站点相对于"存储站点的根目录"的相对路径

	private String dbName;

	private String webHttp;

	private String ftpIp;

	private short ftpPort = 21;

	private String ftpUser;

	private String ftpPassword;

	private String ftpFolder;

	private int indexTemplateId = 0;// 首页模板id, 0表示无意义	

	private String indexFileName; 	//首页文件名
	
	
	/**
	 * 文档首页后缀名
	 */
	private String indexFileSuffix;
		
	private int workflow = 0;			//0 表示无意义
	
	private int workflowIsFromParent = 1;	//1 表示继承"父"的工作流程;0,表示不继承
	
	private long parentSiteId = 0; 	//0 表示没有父站点

	private int status = 0; 		// 站点状态: 0,开通;1,未开通;2,停用;-1,已删除

	private int publishDestination = 0; // 定义发布目的地:0:本地1:远程2:远程本地
	
	private String distributeManners; //分发方式设置,分发方式，以逗号分割的数字值:0表示html,1表示rss，2表示mail,缺省为0

	private int publishMode = 0; // 站点发布模式:0,动态发布;1,动静结合的发布模式

	private int siteOrder = 0;

	private Date createTime;

	private long createUser = 0;	//0 表示没有意义
	
	//本地发布路径
	private String localPublishPath;

	// 没有出现在数据库中
	private String path;
	
	//整站索引
	private String index_level;
	private String index_day;
	private String index_time;
		
	/**
	 * 子站点列表
	 */
	private List subsites;
	
	/**
	 * 站点下频道列表
	 */
	private List chnlofsites;
	/**
	 * 自定义表单集
	 */
	private List customforms;
	
	/**
	 * 定义站点的模板页面编码，缺省为GBK字符编码，Added by biaoping.yin on 2007.7.31
	 */
	private String encoding = CmsEncoder.ENCODING_UTF_8;
	
	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
	}

	public String getSiteDesc() {
		return siteDesc;
	}

	public void setSiteDesc(String siteDesc) {
		this.siteDesc = siteDesc;
	}

	public String getSiteDir() {
		return siteDir;
	}

	public void setSiteDir(String siteDir) {
		this.siteDir = siteDir;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getWebHttp() {
		return webHttp;
	}

	public void setWebHttp(String webHttp) {
		this.webHttp = webHttp;
	}

	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIP) {
		this.ftpIp = ftpIP;
	}

	public short getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(short ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getFtpFolder() {
		return ftpFolder;
	}

	public void setFtpFolder(String ftpFolder) {
		this.ftpFolder = ftpFolder;
	}

	public int getIndexTemplateId() {
		return indexTemplateId;
	}

	public void setIndexTemplateId(int indextemplateId) {
		this.indexTemplateId = indextemplateId;
	}

	public String getIndexFileName() {
		return indexFileName;
	}

	public void setIndexFileName(String indexFileName) {
		this.indexFileName = indexFileName;
	}

	public int getWorkflow() {
		return workflow;
	}

	public void setWorkflow(int workflow) {
		this.workflow = workflow;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getCreateUser() {
		return createUser;
	}

	public void setCreateUser(long createUser) {
		this.createUser = createUser;
	}

	public long getParentSiteId() {
		return parentSiteId;
	}

	public void setParentSiteId(long parentSiteId) {
		this.parentSiteId = parentSiteId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPublishDestination() {
		return publishDestination;
	}

	public void setPublishDestination(int publishDestination) {
		this.publishDestination = publishDestination;
	}

	public int getPublishMode() {
		return publishMode;
	}

	public void setPublishMode(int publishMode) {
		this.publishMode = publishMode;		
	}

	public int getSiteOrder() {
		return siteOrder;
	}

	public void setSiteOrder(int siteOrder) {
		this.siteOrder = siteOrder;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int workflowIsFromParent() {
		return workflowIsFromParent;
	}

	public void setWorkflowIsFromParent(int workflowIsFromParent) {
		this.workflowIsFromParent = workflowIsFromParent;
	}

	public String getIndexFileSuffix() {
		return indexFileSuffix;
	}

	public void setIndexFileSuffix(String indexFileSuffix) {
		this.indexFileSuffix = indexFileSuffix;
	}

	public List getChnlofsites() {
		return chnlofsites;
	}

	public void setChnlofsites(List chnlofsites) {
		this.chnlofsites = chnlofsites;
	}

	public List getSubsites() {
		return subsites;
	}

	public void setSubsites(List subsites) {
		this.subsites = subsites;
	}
	
	public void addSubSite(Site site) {
		if(this.subsites == null)
		{
			subsites = new ArrayList();			
		}
		subsites.add(site);
	}

	public List getCustomforms() {
		return customforms;
	}

	public void setCustomforms(List customforms) {
		this.customforms = customforms;
	}

	public String getLocalPublishPath() {
		return localPublishPath;
	}

	public void setLocalPublishPath(String localPublishPath) {
		this.localPublishPath = localPublishPath;
	}

	public String getDistributeManners() {
		return distributeManners;
	}

	public void setDistributeManners(String distributeManners) {
		this.distributeManners = distributeManners;
	}
	
	/**
	 * 过载父类的equal方法,
	 * 以便将站点作为map对象的key时避免重复的key,
	 * 与hashCode方法结合使用
	 * @param other
	 * @return
	 */
	public boolean equal(Object other)
	{
		if(other == null)
			return false;
		if( other instanceof Site)
		{
			Site _temp = (Site)other;
			return _temp.getSiteId() == this.getSiteId();
		}
		return false;
	}
	
	/**
	 * 过载父类的hashCode方法，以便将站点作为map对象的key时避免重复的key
	 */
	public int hashCode()
	{
		return (this.siteId + "").hashCode();
	}

	public int compareTo(Object o) {
		
		if(o instanceof Site)
		{
			Site other = (Site)o;
			if(other.getSiteOrder() == this.getSiteOrder())
				return 0;
			else if(other.getSiteOrder() > this.getSiteOrder())
			{
				return -1;
			}
			else
			{
				return 1;
			}
				
		}
		else
			return 0;
	}

	

	public String getEncoding() {
		return encoding != null ? encoding : CmsEncoder.ENCODING_UTF_8;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getIndex_day() {
		return index_day;
	}

	public void setIndex_day(String index_day) {
		this.index_day = index_day;
	}

	public String getIndex_level() {
		return index_level;
	}

	public void setIndex_level(String index_level) {
		this.index_level = index_level;
	}

	public String getIndex_time() {
		return index_time;
	}

	public void setIndex_time(String index_time) {
		this.index_time = index_time;
	}

	
}