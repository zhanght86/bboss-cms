package com.frameworkset.platform.cms.driver.context.impl;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.driver.context.ChannelContext;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.publish.PublishMode;
import com.frameworkset.platform.cms.driver.publish.PublishObject;
import com.frameworkset.platform.cms.driver.publish.impl.PublishMonitor;
import com.frameworkset.platform.cms.util.CMSUtil;

/**
 * 
 * <p>
 * Title: ChannelContextImpl
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @author biaoping.yin
 * @version 1.0
 */
public class ChannelContextImpl extends PagineContextImpl implements
		ChannelContext {
	private String channelid;

	private Channel channel;

	private Template detailTemplate;

	private Template outlineTemplate;

	// private PublishMode channnelPublishMode;
	private PublishMode documentPublishMode;

	/**
	 * 获取频道首页来源类型 0－频道模版首页类型 1－直接指定首页类型 2－指定文档为频道首页
	 * 
	 * @return
	 */
	public int getChannelIndexType() {
		return this.channel.getPageflag();
	}

	/**
	 * 判断频道首页是否是由模版生成
	 * 
	 * @return
	 */
	public boolean isTemplateType() {
		return this.getChannelIndexType() == 0;
	}

	/**
	 * 判断频道首页是否是由指定的页面生成
	 * 
	 * @return
	 */
	public boolean isCustomPageType() {
		return this.getChannelIndexType() == 1;
	}

	/**
	 * 判断频道的首页是否是由细览页面生成
	 * 
	 * @return
	 */
	public boolean isDocDetailPageType() {
		return this.getChannelIndexType() == 2;
	}

	/**
	 * 判断频道的首页是否是由其他频道的首页生成
	 * 
	 * @return
	 */
	public boolean isRefchannelType() {
		return this.getChannelIndexType() == 3;
	}

	/**
	 * 判断频道是否有概览模版
	 * 
	 * @return
	 */
	public boolean haveOutlineTemplate() {
		return outlineTemplate != null;
	}

	/**
	 * 判断频道是否有概览模版
	 * 
	 * @return
	 */
	public boolean haveDetailTemplate() {
		return detailTemplate != null;
	}

//	/**
//	 * 本构造方法存以下问题： 对应的频道和parentContext对应的站点可能不一致，
//	 * 特别是跨站点频道引用文档、站点模板中直接引用其他频道模板两种情况
//	 * 如果出现不一致的情况，系统发布过程当中在调用本构造方法构造频道发布上下文时，会报频道对象空指针
//	 * 异常，为了解决本问题，对本构造函数做出修改，站点id不从父上下文中获取，通过出入的频道id获取
//	 * 
//	 * @param channelid
//	 * @param parentContext
//	 * @param publishObject
//	 * @param monitor
//	 * @deprecated  参考ChannelContextImpl(String siteid,String channelid, Context parentContext,
//			PublishObject publishObject, PublishMonitor monitor)
//	 */
//	public ChannelContextImpl(String channelid, Context parentContext,
//			PublishObject publishObject, PublishMonitor monitor) {
//		super(siteid,parentContext, publishObject, monitor);
//		
//		this.channelid = channelid;
//		try {
//			channel = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getChannelInfo(channelid);
//			
//			this.siteID = channel.getSiteId() + "";
//			this.site = CMSUtil.getSiteCacheManager().getSite(siteID);
//			this.siteDir =  site.getSiteDir();
//			this.dbName = site.getDbName();
//			this.domain = site.getWebHttp();
//			
//		} catch (ChannelManagerException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		} catch (DriverConfigurationException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
//		
//		
//	
//		try {
////			channel = CMSUtil.getChannelCacheManager(siteID).getChannel(
////					channelid);
//			/**
//			 * 初始化文档发布模式
//			 */
//
//			this.documentPublishMode = this.getPublishMode(channel
//					.getDocIsDynamic(), channel.getDocIsProtect());
//
//			/**
//			 * 初始化频道发布模式
//			 */
//			this.publishMode = this.getPublishMode(this.channel
//					.getOutlineIsDynamic(), this.channel.getOutlineIsProtect());
//
//		
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		super.fileName = channel.getPubFileName();
//		super.tempFileName = fileName;
//		super.fileExt = channel.getPubFileNameSuffix();
//		super.mimeType = CMSUtil.getMimeType(fileExt);
//		super.publishPath = channel.getChannelPath();
//			
//
//		/**
//		 * 如果频道的首页是直接通过文档的首页
//		 */
//
//		/**
//		 * 初时化频道的细览和概览模版
//		 */
//		try {
//			detailTemplate = parentContext.getDriverConfiguration()
//					.getCMSService().getChannelManager()
//					.getDetailTemplateOfChannel(this.channelid);
//		
//
//		} catch (ChannelManagerException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (DriverConfigurationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		try {
//			outlineTemplate = parentContext.getDriverConfiguration()
//					.getCMSService().getChannelManager()
//					.getOutlineTemplateOfChannel(this.channelid);
//		
//		} catch (ChannelManagerException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (DriverConfigurationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		try {
//			this.enablePublishStatus = parentContext.getDriverConfiguration()
//					.getCMSService().getChannelManager()
//					.getSupportedChannelPublishStatus(channelid);
//		} catch (ChannelManagerException e) {
//			// e.printStackTrace();
//		} catch (DriverConfigurationException e) {
//
//			// e.printStackTrace();
//		}
//	}

	/**
	 * 
	 * @param siteid
	 * @param channelid
	 * @param parentContext
	 * @param publishObject
	 * @param monitor
	 */
	public ChannelContextImpl(String siteid,String channelid, Context parentContext,
			PublishObject publishObject, PublishMonitor monitor) {
		super(siteid,parentContext, publishObject, monitor);
		this.channelid = channelid;
		
		

		try {
			channel = CMSUtil.getChannelCacheManager(siteID).getChannel(
					channelid);
			/**
			 * 初始化文档发布模式
			 */

			this.documentPublishMode = this.getPublishMode(channel
					.getDocIsDynamic(), channel.getDocIsProtect());

			/**
			 * 初始化频道发布模式
			 */
			this.publishMode = this.getPublishMode(this.channel
					.getOutlineIsDynamic(), this.channel.getOutlineIsProtect());

			// if(channel.get)
			// this.publishMode = CMSUtil.getPublishMode(channel.get)
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		super.fileName = channel.getPubFileName();
		super.tempFileName = fileName;
		super.fileExt = channel.getPubFileNameSuffix();
		super.mimeType = CMSUtil.getMimeType(fileExt);
		super.publishPath = channel.getChannelPath();


		/**
		 * 如果频道的首页是直接通过文档的首页
		 */

		/**
		 * 初时化频道的细览和概览模版
		 */
		try {
			detailTemplate = parentContext.getDriverConfiguration()
					.getCMSService().getChannelManager()
					.getDetailTemplateOfChannel(this.channelid);
			// if(detailTemplate == null);

			// detailTemplate = this.testdetailTemplate;

		} catch (ChannelManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DriverConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			outlineTemplate = parentContext.getDriverConfiguration()
					.getCMSService().getChannelManager()
					.getOutlineTemplateOfChannel(this.channelid);

			// this.monitor.setPublishStatus(PublishMonitor.SCRIPT_OUTLINETEMPLATE_NOEXIST);

			// this.outlineTemplate = this.testoutLineTemplate;
		} catch (ChannelManagerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (DriverConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		try {
			this.enablePublishStatus = parentContext.getDriverConfiguration()
					.getCMSService().getChannelManager()
					.getSupportedChannelPublishStatus(channelid);
		} catch (ChannelManagerException e) {
			// e.printStackTrace();
		} catch (DriverConfigurationException e) {

			// e.printStackTrace();
		}
	}

	public String getChannelID() {
		return channelid;
	}

	public Template getDetailTemplate() {
		return detailTemplate;

	}

	public Template getOutlineTemplate() {
		return outlineTemplate;

	}

	public String getChannelPath() {
		// TODO Auto-generated method stub
		return channel.getChannelPath();
	}

	public Channel getChannel() {
		return this.channel;
	}

	public String toString() {
		return new StringBuffer("[channel=").append(channel.getDisplayName())
				.append("(").append(this.channelid).append(")").append(
						",channelpath=").append(this.channel.getChannelPath())
				.append("/").append(this.channel.getPubFileName()).append("]")
				.toString();
	}

	/**
	 * 获取发布页面的地址,重载父类的方法
	 */
	public String getPublishedPageUrl() {
		String url = CMSUtil.getChannelIndexPath(this, channel);
		if (CMSUtil.isExternalUrl(url))
			return url;
		return CMSUtil.getPath(getSitePublishContext(), url);
	}

	/**
	 * 获取频道预览首页页面的地址,重载父类的方法
	 */
	public String getPreviewPageUrl() {
		String url = CMSUtil.getChannelIndexPath(this, channel);
		if (CMSUtil.isExternalUrl(url))
			return url;
		return CMSUtil.getPath(getPreviewContextPath(), url);
		// return this.getPreviewContextPath() + "/" +
		// CMSUtil.getChannelIndexPath(channel);
	}

	public PublishMode getDocumentPublishMode() {
		return documentPublishMode;
	}
}
