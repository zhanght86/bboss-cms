package com.frameworkset.platform.cms.driver.context;

import java.util.Date;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;

/**
 * <p>Title: com.frameworkset.platform.cms.driver.context.ContentContext.java</p>
 *
 * <p>Description: 内容条目上下文信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-9
 * @author biaoping.yin
 * @version 1.0
 */
public interface ContentContext extends PagineContext{
	/**
	 * 内容管理内容条目id
	 * @return
	 */
	public String getContentid();	
	
	public String getKeywords();
	
	public String getChannelid();
	
	public CMSTemplateLinkTable getContentOrigineTemplateLinkTable();
	public void setContentOrigineTemplateLinkTable(CMSTemplateLinkTable linktable);
	

	
	
	/**
	 * 获取文档的发布时间
	 * @return
	 */
	public Date getPublishTime();
	
//	/**
//	 * 内容管理站点上下文,包含站点信息
//	 * @return CMSContext
//	 */
//	public CMSContext getCMSContext();
//	

	
	

	/**
	 * 获取文档对应的细览模版
	 * @return
	 */
	public Template getDetailTemplate();
	
	/**
	 * 获取文档的发布之前的状态，
	 * 以便在发布失败后将该文档的状态恢复回去，
	 * 这是因为系统发布文档之前会将文档的状态改为正在发布状态，
	 * 当发布成功完成时会将文档的状态改为已发布，否则将状态改回去
	 * @return
	 */
	public DocumentStatus getPreStatusOfContent();
	
	
	/**
	 * 判断文档是否是聚合新闻
	 * @return
	 */
	public boolean isAggregation();
	
	/**
	 * 判断文档是否是分页文档
	 * @return
	 */
	public boolean isPagintion();
	
	/**
	 * 判断文档是否有细览模版
	 * @return
	 */
	public boolean hasDetailTemplate();
	
	/**
	 * 获取文档所属的频道信息
	 * @return
	 */
	public Channel getChannel();
	
	/**
	 * 分页新闻时,通过本方法获取当前页面页码
	 * @return
	 */
	public int getCurrentPageNumber();
	
	/**
	 * 获取分页新闻的总页数
	 * @return
	 */
	public int getTotalPages();
	

	
	/**
	 * 获取文档中包含的附件信息
	 * @return
	 */
	public CmsLinkTable getContentLinkTable();
	
	/**
	 * 解析文档附件的过程中记录需要发布的附件链接信息
	 * @param link
	 */
	public void addLink(CMSLink link);
	
	/**
	 * 获取相应的文档
	 * @return
	 */
	public Document getDocument();
	
	public boolean isPreviewDocumentMode();
	
//	/**
//	 * 页码自动增加1
//	 *
//	 */
//	public void increament();
//
//	/**
//	 * 设置当前发布的代码段
//	 *
//	 */
//	public void setCurrentSegment();
	
//	/**
//	 * 获取分段文档的所有段数组
//	 * @return
//	 */
//	public String[] getSegments();
//	
//	/**
//	 * 获取分段文档数组中i位置上的段
//	 * @return
//	 */
//	public String getSegment(int i);
	
	/**
	 * 获取当前页面的名称
	 * @return
	 */
	public String getCurrentPageName();

	/**
	 * 判断所有的页面是否发布完
	 * @return
	 */
	public boolean isPublishAllPage();
	
	public String getCurrentPagePath();
	
	public String getPagePath(int pageNumer);
	
	public void setSegments(String[] segments);

	public void setCurrentPageNumber(int i);

}
