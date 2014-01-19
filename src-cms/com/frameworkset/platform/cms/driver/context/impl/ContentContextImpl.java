package com.frameworkset.platform.cms.driver.context.impl;

import java.util.Date;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.container.Container;
import com.frameworkset.platform.cms.container.ContainerException;
import com.frameworkset.platform.cms.container.ContainerImpl;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.publish.NestedPublishException;
import com.frameworkset.platform.cms.driver.publish.PublishMode;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.templatemanager.TemplateManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.StringUtil;

/**
 * <p>
 * Title: com.frameworkset.platform.cms.driver.context.ContentContext.java
 * </p>
 * 
 * <p>
 * Description: 内容信息上下文,传递
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: iSany
 * </p>
 * 
 * @Date 2007-1-9
 * @author biaoping.yin
 * @version 1.0
 */
public class ContentContextImpl extends PagineContextImpl implements
		ContentContext {
	private Template detailTemplate = null;

	private String keywords = null;

	private DocumentStatus preStatus;

	private Document document;

	private Date publishTime;

	private Channel channel;

	// private String fileNameWithOutExt;

	/**
	 * 内容条目id
	 */
	private String contentid;

	/**
	 * 频道id
	 */
	private String channelid;

	private String[] segments;

	private boolean isPreview = false;

	/**
	 * 文档对应的附件表
	 */
	protected CmsLinkTable contentLinkTable;

	public boolean hasDetailTemplate() {
		return this.detailTemplate != null;
	}

	/**
	 * 临时处理文件时需要的构建函数
	 * 
	 * @param contentid
	 * @param dbName
	 * @param publisher
	 */
	public ContentContextImpl(String contentid, String dbName, String publisher) {
		this.contentid = contentid;
		this.dbName = dbName;
		this.publisher = new String[] { publisher };

	}

	public ContentContextImpl(String contentid, String channelid,
			String siteid, Context parentContext, PublishObject publishObject,
			PublishMonitor monitor) {
		super(siteid, parentContext, publishObject, monitor);
		try {
			this.setContentid(contentid);
			this.setChannelid(channelid);
			contentLinkTable = new CmsLinkTable();
			document = this.getDriverConfiguration().getCMSService()
					.getDocumentManager().getDoc(contentid);
			this.keywords = document.getKeywords();
			
			try {

				channel = CMSUtil.getChannelCacheManager(siteid).getChannel(
						channelid);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			this.publishTime = new Date();

			fileName = CMSUtil.getContentFileName(this.getSiteID() + "",
					document);
			String[] _temp = StringUtil.split(fileName, "\\.");
			// this.fileNameWithOutExt = _temp[0];

			this.fileExt = _temp[1];
			super.mimeType = "text/html";
			if (this.publishObject.isRoot()) {
				this.rootContext = this;
			}
			detailTemplate = this.getDriverConfiguration().getCMSService()
					.getDocumentManager().getDetailTemplate(contentid);
			if (detailTemplate == null) {
				detailTemplate = this.getDriverConfiguration().getCMSService()
						.getChannelManager().getDetailTemplateOfChannel(
								channelid);
			}
			if (detailTemplate != null) {
				// 指定中间模板jsp页面的名称
//				if (document.getPublishfilename() == null
//						|| document.getPublishfilename().equals(""))
//					super.tempFileName = "content_"
//							+ this.detailTemplate.getTemplateId();
//				else
//					super.tempFileName = document.getPublishfilename();
				evalFileName();
			}
			if(detailTemplate == null)
			{
//				throw new NestedPublishException("Publish site[" 
//						+ this.site.getName() + "(" + site.getSiteId() + ")" +"]'s docuemnt[channel=" + channel.getDisplayName() + "-" + channel.getDisplayName() + ",doucment=" + document.getTitle() +"("+ document.getDocument_id() + ")] failed: has not set detail template for document or channel of document.");
			}
			
			if (this.publishObject.getActionType() == PublishObject.ACTIONTYPE_PUBLISH) {
				preStatus = DocumentStatus.getDocumentStatus(document.getStatus());
				getDriverConfiguration().getCMSService().getDocumentManager()
						.updateDocumentStatus(this, DocumentStatus.PUBLISHING);
			}

			this.publishPath = channel.getChannelPath();

			if (this.getActionType() == PublishObject.ACTIONTYPE_PUBLISH) {
				try {
					this.getDriverConfiguration().getCMSService()
							.getDocumentManager().storeOldStatus(this,
									this.preStatus);
				} catch (Exception e) {

				}
			}

			/**
			 * 如果是从频道过来的发布情况，文档的发布模式已经在频道上下文中构造完成，直接获取即可
			 */
			if (parentContext instanceof ChannelContext) {
				this.publishMode = ((ChannelContext) parentContext)
						.getDocumentPublishMode();
			} else // 直接发布文档时，获取文档的发布模式
			{
				this.publishMode = this.getPublishMode(this.channel
						.getDocIsDynamic(), this.channel.getDocIsProtect());
			}

		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChannelManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 文档采集编辑时,预览实时效果时调用的方法
	 * 
	 * @param document
	 * @param channelid
	 * @param parentContext
	 * @param publishObject
	 * @param monitor
	 */
	public ContentContextImpl(Document document, String channelid,
			String siteid, Context parentContext, PublishObject publishObject,
			PublishMonitor monitor, boolean isPreview) {
		super(siteid, parentContext, publishObject, monitor);

		this.isPreview = isPreview;
		try {
			contentLinkTable = new CmsLinkTable();
			this.document = document;
			// document =
			// this.getDriverConfiguration().getCMSService().getDocumentManager().getDoc(contentid);
			this.keywords = document.getKeywords();

			try {
				this.setChannelid(document.getChanel_id() + "");
				channel = CMSUtil.getChannelCacheManager(siteid).getChannel(
						channelid);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.publishTime = new Date();
			this.setContentid(document.getDocument_id() + "");
			this.setChannelid(channelid);
			super.fileName = CMSUtil.getContentFileName(this.getSiteID() + "",
					document);
			String[] _temp = StringUtil.split(fileName, "\\.");
			// this.fileNameWithOutExt = _temp[0];
			this.fileExt = _temp[1];
			super.mimeType = "text/html";
			if (this.publishObject.isRoot()) {
				this.rootContext = this;
			}

			// if(this.publishObject.getActionType() ==
			// PublishObject.ACTIONTYPE_PUBLISH)
			// {
			// getDriverConfiguration()
			// .getCMSService()
			// .getDocumentManager()
			// .updateDocumentStatus(this,
			// DocumentStatus.PUBLISHING);
			// }
			// try {
			// channel = CMSUtil.getChannelCacheManager(
			// parentContext.getSiteID()).getChannel(channelid);
			// } catch (Exception e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			this.publishPath = channel.getChannelPath();

			// 预览时文档的细览模版直接从文档对象中获取
			if (this.isPreview) {
				try {
					detailTemplate = this.getDriverConfiguration()
							.getCMSService().getTemplateManager()
							.getTemplateInfo(
									document.getDetailtemplate_id() + "");
				} catch (TemplateManagerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				detailTemplate = this.getDriverConfiguration().getCMSService()
						.getDocumentManager().getDetailTemplate(contentid);
			}
			if (detailTemplate == null) {
				detailTemplate = this.getDriverConfiguration().getCMSService()
						.getChannelManager().getDetailTemplateOfChannel(
								channelid);
			}
			if (detailTemplate != null) {
				// 指定中间模板jsp页面的名称
//				if (document.getPublishfilename() == null
//						|| document.getPublishfilename().equals(""))
//					super.tempFileName = "content_"
//							+ this.detailTemplate.getTemplateId();
//				else
//					super.tempFileName = document.getPublishfilename();
				evalFileName();
			}

			if (!isPreview
					&& getActionType() == PublishObject.ACTIONTYPE_PUBLISH) {
				try {
					this.getDriverConfiguration().getCMSService()
							.getDocumentManager().storeOldStatus(this,
									this.preStatus);
				} catch (Exception e) {

				}
			}

			if (!isPreview) {

				/**
				 * 如果是从频道过来的发布情况
				 */
				if (parentContext instanceof ChannelContext) {
					this.publishMode = ((ChannelContext) parentContext)
							.getDocumentPublishMode();
				} else {
					this.publishMode = this.getPublishMode(this.channel
							.getDocIsDynamic(), this.channel.getDocIsProtect());
				}
			}

			// 使用缺省的模版
			// detailTemplate = new Template();

		} catch (DocumentManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ChannelManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 判断文档是否是分页文档
	 * 
	 * @return
	 */
	public boolean isPagintion() {
		return this.segments != null;
		// return document.getDoctype() == 4;
	}

	/**
	 * 判断文档是否是聚合新闻
	 * 
	 * @return
	 */
	public boolean isAggregation() {
		return document.getDoctype() == 3;
	}
	
	private void evalFileName()
	{
		
		// 指定中间模板jsp页面的名称
		if (document.getPublishfilename() == null
				|| document.getPublishfilename().equals(""))
		{
			if(detailTemplate == null)
				return;
			super.tempFileName = "content_"
					+ this.detailTemplate.getTemplateId();
			super.jspFileName = "content_"
					+ this.detailTemplate.getTemplateId() + ".jsp";
		}
		else
		{
			super.tempFileName = document.getPublishfilename();
			if(detailTemplate == null)
				return;
			super.jspFileName = CMSUtil.getJspFileName(super.tempFileName,this.detailTemplate.getTemplateId()+"");
		}
	}

	/**
	 * 
	 * @param contentid
	 * @param parentContext
	 * @param publishObject
	 * @param monitor
	 */
	public ContentContextImpl(String contentid, Context parentContext,
			PublishObject publishObject, PublishMonitor monitor, String siteid) {
		super(siteid, parentContext, publishObject, monitor);
		contentLinkTable = new CmsLinkTable();
		this.setContentid(contentid);

		super.mimeType = "text/html";
		// this.dbName = this.parentContext.getDBName();
		this.publishTime = new Date();
		try {
			document = this.getDriverConfiguration().getCMSService()
					.getDocumentManager().getDoc(contentid);
			// if(this.parentContext instanceof ChannelContext)
			// this.setChannelid(((ChannelContext)parentContext).getChannelID());

			this.setChannelid(document.getChanel_id() + "");
			channel = CMSUtil.getChannelCacheManager(siteid).getChannel(
					channelid);
			super.fileName = CMSUtil.getContentFileName(this.getSiteID() + "",
					document);
			String[] _temp = StringUtil.split(fileName, "\\.");
			// this.fileNameWithOutExt = _temp[0];
			this.fileExt = _temp[1];
			this.keywords = document.getKeywords();
			preStatus = DocumentStatus.getDocumentStatus(document.getStatus());
			getDriverConfiguration().getCMSService().getDocumentManager()
					.updateDocumentStatus(this, DocumentStatus.PUBLISHING);
			// try {
			// channel = CMSUtil.getChannelCacheManager(
			// parentContext.getSiteID()).getChannel(channelid);
			// } catch (Exception e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			if (this.publishObject.isRoot()) {
				this.rootContext = this;

			}
			this.publishPath = "/" + channel.getChannelPath();

			detailTemplate = this.getDriverConfiguration().getCMSService()
					.getDocumentManager().getDetailTemplate(contentid);
			if (detailTemplate == null) {
				detailTemplate = this.getDriverConfiguration().getCMSService()
						.getChannelManager().getDetailTemplateOfChannel(
								channelid);
			}

			if (detailTemplate != null) {
				// 指定中间模板jsp页面的名称
//				if (document.getPublishfilename() == null
//						|| document.getPublishfilename().equals(""))
//				{
//					super.tempFileName = "content_"
//							+ this.detailTemplate.getTemplateId();
//					super.jspFileName = "content_"
//							+ this.detailTemplate.getTemplateId() + ".jsp";
//				}
//				else
//				{
//					super.tempFileName = document.getPublishfilename();
//					super.jspFileName = CMSUtil.getJspFileName(super.tempFileName,this.detailTemplate.getTemplateId()+"");
//				}
				evalFileName();
			}
			if (this.getActionType() == PublishObject.ACTIONTYPE_PUBLISH) {
				try {
					this.getDriverConfiguration().getCMSService()
							.getDocumentManager().storeOldStatus(this,
									this.preStatus);
				} catch (Exception e) {

				}
			}

			/**
			 * 如果是从频道过来的发布情况
			 */
			if (parentContext instanceof ChannelContext) {
				this.publishMode = ((ChannelContext) parentContext)
						.getDocumentPublishMode();
			} else {
				this.publishMode = this.getPublishMode(this.channel
						.getDocIsDynamic(), this.channel.getDocIsProtect());
			}

		} catch (DocumentManagerException e1) {

			e1.printStackTrace();
		} catch (DriverConfigurationException e1) {

			e1.printStackTrace();
		} catch (ChannelManagerException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getContentid() {
		return contentid;
	}

	public void setContentid(String contentid) {
		this.contentid = contentid;
	}

	public String getChannelid() {

		return this.channelid;
	}

	public void setChannelid(String channelid) {
		this.channelid = channelid;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		return buffer.append("Context:[site=").append(this.getSite().getName())
				.append("(").append(getSiteID()).append("),").append("dbname=")
				.append(this.parentContext.getDBName()).append(",").append(
						"channel=").append(this.channel.getDisplayName())
				.append("(").append(this.getChannelid()).append("),").append(
						"content=").append(this.document.getSubtitle()).append(
						"(").append(this.getContentid()).append(")]")
				.toString();
	}

	public String getContentPath() {
		StringBuffer contentPath = new StringBuffer();
		return contentPath.toString();
	}

	public Template getDetailTemplate() {
		return this.detailTemplate;
	}

	public String getKeywords() {
		// TODO Auto-generated method stub
		return this.keywords;
	}

	public DocumentStatus getPreStatusOfContent() {
		// TODO Auto-generated method stub
		return this.preStatus;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public Channel getChannel() {
		return this.channel;
	}

	// public int getCurrentPageNumber() {
	//		
	// return currentPageNumber;
	// }

	public int getTotalPages() {

		return segments != null ? this.segments.length : 0;
	}

	public CmsLinkTable getContentLinkTable() {
		return contentLinkTable;
	}

	public void addLink(CMSLink link) {
		this.contentLinkTable.addLink(link);

	}

	public Document getDocument() {
		return document;
	}

	public boolean isPreviewDocumentMode() {
		return this.isPreview;
	}

	// public String[] getSegments() {
	//		
	// return this.segments;
	// }
	//
	// public String getSegment(int i) {
	//		
	// return this.segments[i];
	// }

	// public String getCurrentPagePath()
	// {
	// return CMSUtil.getPath(this.getPublishPath(),getCurrentPageName());
	// }

	// public String getPagePath(int pageNumber)
	// {
	// return CMSUtil.getPath(this.getPublishPath(),getPageName(pageNumber));
	// }
	// public String getCurrentPageName() {
	// return this.getPageName(this.currentPageNumber);
	// }

	// public String getFullFileName()
	// {
	// if(!this.isPagintion())
	// {
	// this.fileExt = this.getFileExt();
	// if(this.fileExt != null && !fileExt.equals("") )
	// {
	// if(fileName.toLowerCase().endsWith("."+this.fileExt.toLowerCase()))
	// return fileName;
	// else
	// return fileName + "." + fileExt;
	// }
	// }
	// else
	// {
	// // if(this.currentPageNumber == 0)
	// // {
	// // return this.fileName;
	// // }
	// // else
	// {
	// return this.getCurrentPageName();
	// }
	// }
	// return fileName;
	// }

	public boolean isPublishAllPage() {

		return this.currentPageNumber == this.getTotalPages() - 1;
	}

	public void next() {
		this.currentPageNumber++;
		if (this.currentPageNumber < this.getTotalPages())
			this.document.setContent(this.segments[currentPageNumber]);
	}

	// public String getPageName(int pageNumer)
	// {
	// if(pageNumer == 0)
	// {
	// return this.fileName;
	// }
	// return this.fileNameWithOutExt + "_"
	// + pageNumer + "."
	// + this.fileExt;
	// }

	public void setSegments(String[] segments) {
		this.segments = segments;
	}

	// public void setCurrentPageNumber(int currentPageNumber) {
	// super.setC
	// this.currentPageNumber = currentPageNumber;
	// }

	// public String getFileName()
	// {
	// if(!this.isPagintion())
	// {
	// // this.fileExt = this.getFileExt();
	// // if(this.fileExt != null && !fileExt.equals("") )
	// // {
	// // if(fileName.toLowerCase().endsWith("."+this.fileExt.toLowerCase()))
	// // return fileName;
	// // else
	// // return fileName + "." + fileExt;
	// // }
	// return super.getFileName();
	// }
	// else
	// {
	// if(this.currentPageNumber == 0)
	// {
	// return this.fileName;
	// }
	// else
	// {
	// return this.getCurrentPageName();
	// }
	// }
	//		
	// }

	/**
	 * 过载父类得方法
	 * 
	 * @see
	 */
	public String getPublishedPageUrl() {

		if (this.publishMode == PublishMode.MODE_NO_ACTION) {
			return "PublishMode.MODE_NO_ACTION";
		}
		if (this.publishMode == PublishMode.MODE_ONLY_ATTACHMENT) {
			return "PublishMode.MODE_ONLY_ATTACHMENT";
		}
		Container container = new ContainerImpl();

		try {
			Context defaultctx = new DefaultContextImpl(this);
			container.init(defaultctx);
			return container.getPublishedDocumentUrl(this.document);
		} catch (ContainerException e) {
			//				
		}
		return "";

		// if(this.getPublishPath() != null &&
		// !this.getPublishPath().equals(""))
		// if(this.document.getDoctype() == Document.DOCUMENT_NORMAL)
		// {
		// return new StringBuffer(this.getSitePublishContext()).append("/")
		// .append(this.getPublishPath()).append("/").append(this.fileName).toString();
		// }
		// else if(this.document.getDoctype() == Document.DOCUMENT_OUTLINK)
		// {
		// return this.document.getContent();
		// }
		// else if(this.document.getDoctype() == Document.DOCUMENT_OUTFILE)
		// {
		// return this.document.getContent();
		// }
		// else
		// {
		// return new StringBuffer(this.getSitePublishContext()).append("/")
		// .append(this.getPublishPath()).append("/").append(this.fileName).toString();
		// }
		// else
		// return new
		// StringBuffer(this.getSitePublishContext()).append("/").append(fileName).toString();
		// }
		// else //如果是动态发布
		// {
		// if(this.getPublishPath() != null &&
		// !this.getPublishPath().equals(""))
		// return new StringBuffer(this.getSitePublishContext() )
		// .append("/" ).append(this.getPublishPath())
		// .append( "/" )
		// .append(this.tempFileName ).append( ".jsp")
		// .append(this.getParameters()).toString();
		// else
		// return new StringBuffer(this.getSitePublishContext())
		// .append("/")
		// .append( this.tempFileName ).append(".jsp")
		// .append(this.getParameters()).toString();
		// }
	}

	public String getPreviewPageUrl() {

		if (this.publishMode == PublishMode.MODE_NO_ACTION) {
			return "PublishMode.MODE_NO_ACTION";
		}
		if (this.publishMode == PublishMode.MODE_ONLY_ATTACHMENT) {
			return "PublishMode.MODE_ONLY_ATTACHMENT";
		}
		String url = CMSUtil.getContentPath(this, document);
		if (CMSUtil.isExternalUrl(url))
			return url;

		if (this.document.getDoctype() == Document.DOCUMENT_NORMAL) {

			return new StringBuffer(this.getPreviewContextPath()).append("/")
					.append(url).toString();
		} else if (this.document.getDoctype() == Document.DOCUMENT_OUTLINK) {
			return this.document.getContent();
		} else if (this.document.getDoctype() == Document.DOCUMENT_OUTFILE) {
			return this.document.getContent();
		} else {
			return new StringBuffer(this.getPreviewContextPath()).append("/")
					.append(url).toString();
		}
	}

	private String getParameters() {
		StringBuffer ret = new StringBuffer();
		ret.append("?siteid=").append(this.getSiteID()).append("&channelid=")
				.append(this.getChannel().getChannelId())
				.append("&documentid=").append(
						this.getDocument().getDocument_id());
		return ret.toString();

	}
	

}
