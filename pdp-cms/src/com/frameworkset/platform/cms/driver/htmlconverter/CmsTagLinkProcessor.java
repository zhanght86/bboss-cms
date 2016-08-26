package com.frameworkset.platform.cms.driver.htmlconverter;

import javax.servlet.http.HttpServletRequest;

import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;

/**
 * 标签链接处理程序
  * <p>Title: CmsTagLinkProcessor</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-4-11 11:08:32
 * @author biaoping.yin
 * @version 1.0
 */
public class CmsTagLinkProcessor extends CmsLinkProcessor  {

	public CmsTagLinkProcessor(Context context, int m_mode, String encode) {
		super(context, m_mode, encode);
		
	}
	public CmsTagLinkProcessor(Context context, String templatePath) {
		super(context,REPLACE_LINKS, CmsEncoder.ENCODING_UTF_8,templatePath);
		
	}

	public CmsTagLinkProcessor(Context context, 
//							   int m_mode, 
//							   String encode, 
							   String templatePath,  
							   String m_relativePath) {
		super(context, REPLACE_LINKS, CmsEncoder.ENCODING_UTF_8, templatePath, m_relativePath);
	}

	public CmsTagLinkProcessor(HttpServletRequest request, String m_relativePath, String sitedir) {
		super(request, m_relativePath, sitedir);
		
	}
	
	
	public CmsTagLinkProcessor(Context context, int replace_links, String encoding_iso_8859_1, String templatePath) {
		super(context, replace_links, encoding_iso_8859_1, templatePath);
	}
	public CmsTagLinkProcessor() {
		
	}
	/**
	 * 处理一般的链接
	 * @param link
	 * @return
	 */
	public CMSLink processHref(String href,int linkhandletype)
	{

		if(href == null || href.equals("") || href.equals("#") || href.equals(".."))
		{
			return new CMSLink(href,linkhandletype);
		}
		LinkParser parser;

		CMSLink link;
		
		switch (m_mode) {

			case PROCESS_LINKS:
				// macros are replaced with links
				link = m_linkTable.getLink(href);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					return link;
				} else {
					
					link = new CMSLink(href,linkhandletype);
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {

						recordTemplateLink( link);
						if(super.isStyleLink(href))
						{
							super.handleStyleLink(link.getRelativeFilePath());
						}
					}
					
					return link;
//					
				}
				

			case REPLACE_LINKS:
				link = m_linkTable.getLink(href);
				if (link != null) {
					// tag.setLink(escapeLink(link.getHref()));
					return link;
				} else {
					
					link = new CMSLink(href,linkhandletype);
					parser = new LinkParser(link);
					link = parser.getResult();
					m_linkTable.addLink(link);
					/*
					 * 记录内部模板附件路径，以便后续得附件分发
					 */
					if (this.isTemplateProcess() && link.isInternal()) {

						recordTemplateLink( link);
						if(super.isStyleLink(href))
						{
							super.handleStyleLink(link.getRelativeFilePath());
						}
					}
					
					return link;
//					
				}
				
			default: 
				return null;// noop
		}
	}
	
	
	/**
	 * 记录执行jsp标签处理过程中分析出来的超链接
	 */
	protected void recordTemplateLink(CMSLink link)
	{
		
		if(link.isJavaScript())
			return;
		if(!link.isParserAndDistribute())//属于带变量的地址，忽略记录跟踪
		{
			this.context.getPublishMonitor().addSuccessMessage("页面中带变量或者标签的地址，忽略记录分发:" + link.getOriginHref(),context.getPublisher());
			return ;
		}
		if(link.isDirectory())
		{
			/**
			 * 普通目录无需进行发布分析过程，直接拷贝文件就可以了
			 */
			if(context.getPublishMonitor() != null && !context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getHref()))
				context.addTemplateLink(link);
		}
		else if(link.getRelativeFilePathType() == CMSLink.TYPE_STATIC_PAGE)
		{
			if(context.getPublishMonitor() != null && !context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath(),false))
				context.addStaticTemplateLink(link);
		}
		else if(link.getRelativeFilePathType() == CMSLink.TYPE_DYNAMIC_PAGE)
		{
			if(context.getPublishMonitor() != null && !context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath(),false))
				context.addDynamicTemplateLink(link);
		}
		else	
		{
			/**
			 * 普通链接无需进行发布分析过程，直接拷贝文件就可以了
			 */
			if(context.getPublishMonitor() != null && !context.getPublishMonitor().containDistributePage(context.getSiteID(),link.getRelativeFilePath()))
				context.addTemplateLink(link);
		}
	}
}
