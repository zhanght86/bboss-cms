package com.frameworkset.platform.cms.driver.context;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.frameworkset.platform.cms.driver.config.DriverConfiguration;
import com.frameworkset.platform.cms.driver.dataloader.CMSDetailDataLoader;
import com.frameworkset.platform.cms.driver.distribute.DistributeDestination;
import com.frameworkset.platform.cms.driver.distribute.IndexObject;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.jsp.CMSRequestContext;
import com.frameworkset.platform.cms.driver.jsp.ContextInf;
import com.frameworkset.platform.cms.driver.publish.PubObjectReference;
import com.frameworkset.platform.cms.driver.publish.PublishMode;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.sitemanager.Site;

/**
 * 
  * <p>Title: Context</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public interface Context extends java.io.Serializable,ContextInf {
	
	/**
	 * 获取发布的文件存放的相对物理目录
	 * @return
	 */
	public String getPublishPath();
	/**
	 * 获取发布的请求相对路径，不包含文件名称
	 * @return
	 */
	public String getRendPath();
	/**
	 * 获取发布的请求相对路径，包含文件名称
	 * @return
	 */
	public String getRendURI();
	 
	public String getMimeType();
	
	public String getCharset();
	
	public PublishMode getPublishMode();
	
	public String getFileName();
	public String getFileExt();
	
//	public String getFullFileName();
	/**
	 * 获取站点的dir
	 * @return
	 */
	public String getSiteDir();
	
	/**
	 * 是否远程发布
	 * @return
	 */
	public boolean[] getLocal2ndRemote() ;
	
	/**
	 * 获取发布的临时目录，用来存放发布的临时文件以及发布的中间存放结果
	 * @return
	 */
	public String getPublishTemppath();
	
	/**
	 * 获取站点发布的临时目录，用来存放发布的临时文件以及发布的中间存放结果
	 * @return
	 */
	public String getSitePublishTemppath();
	
	
	/**
	 * 判断发布的类型是否允许,
	 * @param distributeType 取值范围：0-html,1-rss,2-mail
	 * @return 
	 */
	public List getEnabledDistributes();
	
	public int[] getDistributeManners();
	/**
	 * 获取驱动配置
	 * @return
	 */
	public DriverConfiguration getDriverConfiguration();

	/**
	 * 获取当前任务的发布监控器
	 * @return
	 */
	public PublishMonitor getPublishMonitor();
	
	/**
	 * 获取发布人的信息,
	 * @return String[] {userid,userinfo,roles}
	 */
	public String[] getPublisher();
	
	/**
	 * 获取发布的范围:增量发布,全部发布
	 * @return
	 */
	public int[] getPublishScope();
	
	/**
	 * 判断当前的context是否是顶层的context,整个发布任务的起点context即为发布的
	 * root context
	 * @return
	 */
	public boolean isRootContext();
	
	public Context getParentContext();
	
	/**
	 * 获取发布文件的绝对存放地址
	 * @return
	 */
	public String getPublishAbsolutePath();
	
	/**
	 * 获取站点发布的根地址
	 * @return
	 */
	public String getPublishRootPath();
	
	/**
	 * 获取当前的发布对象
	 * @return
	 */
	public PublishObject getPublishObject();
	
	public String getDBName();
	
	public String getSiteID();
	public Context getRootContext();
	public String getAbsoluteRendPath();
	public String getAbsoluteRendRootPath();
	
	
	
	/**
	 * 获取请求上下文的唯一标识
	 * @return
	 */
	public String getID();
	public CMSDetailDataLoader getCMSDetailDataLoader();
	
	public void setCMSDetailDataLoader(CMSDetailDataLoader dataLoader);
	
	/**
	 * 获取发布文件的物理路径
	 * @return
	 */
	public String getRealRendPath();
	
	/**
	 * 获取站点工程文件的物理路径
	 * @return
	 */
	public String getRealProjectPath();
	
	public String getAbsoluteTemplatePath();
	
	public String getAbsoluteTemplateRootPath();
	
	public String getAbsoluteProjectPath();
	
	public String getAbsoluteProjectRootPath();
	
	/**
	 * 获取上下文中的属性
	 * @param property
	 * @return
	 */
	public String getProperty(String property);
	
	/**
	 * 获取上下文中所有的属性
	 * @return
	 */
	public Properties getProperties();
	
	/**
	 * 设置上下文属性
	 * @param name
	 * @param value
	 */
	public void setProperty(String name,String value);
	
	
	/**
	 * 获取站点模版文件的物理路径
	 * @return
	 */
	public String getRealTemplatePath();
	
	
	/**
	 * 获取图片库相对路径
	 * @return
	 */
	public String getAbsoluteImagesPath();
	
	/**
	 * 获取图片库相对路径
	 * @return
	 */
	public String getAbsoluteImagesRootPath();
	
	/**
	 * 获取图片库物理路径
	 * @return
	 */
	public String getRealImagesPath();
	
	
	/**
	 * 获取样式库相对路径
	 * @return
	 */
	public String getAbsoluteStylePath();
	
	/**
	 * 获取样式库相对路径
	 * @return
	 */
	public String getAbsoluteStyleRootPath();
	
	/**
	 * 获取样式库物理路径
	 * @return
	 */
	public String getRealStylePath();
	
//	/**
//	 * 获取应用的上下文路径，例如：creatorcms
//	 * @return
//	 */
//	public String getCurrentContextPath();
	
	/**
	 * 获取站点模版文件的物理根路径
	 * @return
	 */
	public String getRealTemplateRootPath();
	
	/**
	 * 是否允许发布结果进行html自动校正
	 * @return
	 */
	public boolean autoCorrectHtml();
	
	/**
	 * 获取站点发布的上下文，如果为空则选择站点的域名为上下文
	 * @return
	 */
	public String getSitePublishContext();
	
	public String getPublishedPageUrl();
	
	public String getPreviewPageUrl();
	
	public String getPreviewContextPath();
	
	public String getPreviewRootPath();
	
	/**
	 * 获取引起发布的动作类型
	 * 包括：发布，预览，设计
	 * @return
	 */
	public int getActionType();
	
	public FTPConfig getFTPConfig();
	
	/**
	 * 获取发布的请求上下文
	 * @return
	 */
	public CMSRequestContext getRequestContext();
	
	/**
	 * 获取发布时生成的临时文件名称
	 * @return
	 */
	public String getTempFileName();
	
	/**
	 * 添加标签处理过程中分析出来的附件链接
	 * @param link
	 */
	public void addTemplateLink(CMSLink link);
	
	/**
	 * 添加标签处理过程中分析出来的动态页面地址
	 * @param link
	 */
	public void addDynamicTemplateLink(CMSLink link);
	
	/**
	 * 添加标签处理过程中分析出来的静态页面地址
	 * @param link
	 */
	public void addStaticTemplateLink(CMSLink link);
	
	/**
	 * 获取发布过程中提取的动态页面链接列表
	 * @return
	 */
	public CmsLinkTable getDynamicPageLinkTable();
	
	/**
	 * 获取发布过程中提取的静态页面链接列表
	 * @return
	 */
	public CmsLinkTable getStaticPageLinkTable() ;
	
	/**
	 * 获取发布过程中提取的模版附件链接列表
	 * @return
	 */
	public CMSTemplateLinkTable getTemplateLinkTable();
	
	/**
	 * 获取站点本地发布路径
	 * @return
	 */
	public String getLocalPublishDestination();
	
	public String getDomain();
	
	/**
	 * 获取站点信息
	 * @return
	 */
	public Site getSite();
	
	/**
	 * 获取站点工程文件的物理根路径
	 * @return
	 */
	public String getRealProjectRootPath();
	
	/**
	 * 获取发布引擎的临时目录的唯一标识，每个任务都有自己的唯一id，防止并发发布的冲突
	 * @return
	 */
	public String getTempPublishUID();
	
	
//	/**
//	 * 判断当前的对象是否是缺省的
//	 * @return
//	 */
//	public boolean isDefaultContext();
	
	
//	public String getContentFileName(String contentID);
	public PublishMode getPublishMode(int isdynamic,int isprotected);
	
//	/**
//	 * 记录当前发布的元素与发布宿主之间的关系
//	 */
//	public void tracePublishObjectRelations();
	
	/**
	 * 获取发布的深度
	 * -1，0标识不受限制
	 * 其他情况每发布一个层次，depth 自动加1，
	 * 达到最深允许的深度时，如果还存在下级子任务，不执行直接返回
	 */
	public int getCurrentPublishDepth();
	
	/**
	 * 设置发布的深度
	 * -1，0标识不受限制
	 * 其他情况每发布一个层次，depth 自动加1，
	 * 达到最深允许的深度时，如果还存在下级子任务，不执行直接返回
	 */
	public void setCurrentPublishDepth(int currentdepth);
	
	/**
	 * 获取发布的最大允许深度
	 * -1，0标识不受限制
	 * 其他情况每发布一个层次，depth 自动加1，
	 * 达到最深允许的深度时，如果还存在下级子任务，不执行直接返回
	 */
	public int getMaxPublishDepth();
	
	/**
	 * 设置发布的最大深度
	 * @param maxPublishDepth
	 */
	public void setMaxPublishDepth(int maxPublishDepth);
	
	/**
	 * 判断当前的发布是否源自递归发布
	 * @return
	 */
	public boolean isRecursive();	
	
	/**
	 * 获取和当前发布任务相关的发布对象，然后递归发布这些发布对象
	 * @return
	 */
	public Set getRecursivePubObjects();
	
	/**
	 * 添加一个递归发布对象
	 * @param pubObjectReference
	 */
	public void addPubObjectReference(PubObjectReference pubObjectReference);
	

//	/**
//	 * 将递归发布对象列表添加到上下文
//	 */
//	public void addPubObjectReferences(Set pubObjectReference);
	
	/**
	 * 跟踪记录当前发布任务所对应的发布对象和发布对象引用的元素之间的关系
	 * @param refobj 引用对象元素标识
	 * @param reftype 引用元素类型，取值范围如下：
	 *    文档
	 *	public static final int REFOBJECTTYPE_DOCUMENT = 3;
	 *	频道
	 *	public static final int REFOBJECTTYPE_CHANNEL = 1;
	 *	
	 * 页面类型
	 *	 
	 *	public static final int REFOBJECTTYPE_PAGE = 2;
	 *	
	 * 站点类型		 
     *	public static final int REFOBJECTTYPE_SITE = 0;
     *  @see com.frameworkset.platform.cms.driver.publish.RecursivePublishManager
	 * @param site 站点英文名称
	 */
	public void recordRecursivePubObj(String refobj, int reftype, String site);
	
	
	/**
	 * 添加当前发布任务的一个引用对象
	 * @param pubObjectReference
	 */
	public void addRefObject(PubObjectReference refObjectReference);
	

	/**
	 * 添加当前发布任务的引用对象集合
	 */
	public void addRefObjects(Set refObjectReferences);
	
	/**
	 * 获取当前对象所引用的发布对象集
	 * @return
	 */
	public Set getRefObjects();
	
	/**
	 * 判断当前的发布对象是否允许递归发布
	 * @return
	 */
	public boolean enableRecursive();
	
	public void setEnableRecursive(boolean enableRecursive);
	
	
	/**
	 *  抽象方法，具体的发布对象子类去实现
	 * 添加与当前任务相关的发布对象到上下文中
	 */
	public void addRecursivePubObjToContext(Set recursivePubObjs) ;
	
	/**
	 * 清除当前已经执行过的递归发布任务,包括页面，
	 * 频道首页 ，
	 * 站点首页，
	 * 文档细览页面
	 * @param siteid 发布对象所属的站点id
	 * @param pubobject 发布的对象标识,值的格式为pubobjcetid
	 * @param type 对象类型，0-站点，1-频道首页，2-页面,3-文档细览页面
	 */
	public void removeRecursivePubObject(String siteid,String pubobject,int type);
	
	
	/**
	 * 设置是否需要记录引用元素
	 * @param b
	 */
	public void setNeedRecordRefObject(boolean b);
	
	/**
	 * 判断是否需要记录引用元素
	 * @param b
	 */
	public boolean needRecordRefObject();
	
	public boolean forceStatusPublished();
	
	/**
	 * 记录当前的正在发布的任务所属的站点
	 * 当由根任务触发的发布任务全部执行完毕后，
	 * 将发布过程中涉及的各个站点生成的网页分发到每个站点的对应的目的地
	 * @param DistributeDestination
	 */
	public void recordSiteDistributeDestination(DistributeDestination distributeDestination);
	
	public Set getSiteDistributeDestinations();
	
	/**
	 * 添加需要建立索引的对象
	 * @param indexObject
	 */
	public void addIndexObject(IndexObject indexObject);
	
	/**
	 * 获取需要建立索引的对象集合
	 * @return Set<IndexObject>
	 * added 2007/10/13 PM 17:24
	 */
	public Set getIndexObjects();
	
	/**
	 * 发布任务执行完毕后销毁发布过程中使用的所有资源
	 * added 2007/10/12 PM 15:24
	 */
	public void destroy();
	
	/**
	 * 判断是否需要强制发布link文件
	 * @param link
	 * @return
	 */
	public boolean forcepublishLinks(CMSLink link);
	public boolean isClearFileCache();	

	public void setClearFileCache(boolean clearFileCache) ;
	public String getJspFileName();
	public void setContentOrigineTemplateLinkTable(
			CMSTemplateLinkTable origineTemplateLinkTable);	
	public CMSTemplateLinkTable getContentOrigineTemplateLinkTable();
	
	
	
	
	
	
	
}
