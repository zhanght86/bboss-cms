package com.frameworkset.platform.cms.driver.dataloader;

import java.util.List;

import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;

/**
 * 
 * <p>Title: CMSSpecialAttachmentList</p>
 *
 * <p>Description: 频道所有文档附件列表和分页数据加载器，将文档中的附件作为列表和分页进行处理，要求频道文档只做附件类型发布
 * 与概览(outline)、attach标签结合使用，输出下载专区列表和下载专区首页
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-11-19 12:02:23
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSSpecialAttachmentList extends CMSBaseListData implements java.io.Serializable {

	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		try {
		
		if(channel != null && !this.channel.equals(""))
		{
			/**
			 * 如果存在发布上下文，则表示为发布过程中调用本方法
			 */
			if(context != null)
			{
				if(this.site == null)
				{
					return CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getLatestPubDocAttachListOrderByDocwtime(super.context.getSiteID(),channel,(int)offSet,pageItemsize,this.doctype);
					
				}
				else
				{ 
					Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					return CMSUtil.getCMSDriverConfiguration()
								  .getCMSService()
								  .getChannelManager()
								  .getLatestPubDocAttachListOrderByDocwtime(siteinfo.getSiteId()+"",channel,(int)offSet,pageItemsize,this.doctype);
				}
			}
			else
			{
				if(this.channel == null || this.channel.equals("") || this.site == null || this.site.equals(""))
					throw new IllegalArgumentException("没有指定站点的名称和频道名称");
				
				Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
				
				return CMSUtil.getCMSDriverConfiguration()
				  .getCMSService()
				  .getChannelManager()
				  .getLatestPubDocAttachListOrderByDocwtime(siteinfo.getSiteId()+"",channel,(int)offSet,pageItemsize,this.doctype);
				
				
			}
		}
		else
			return new ListInfo();
	} catch (ChannelManagerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DriverConfigurationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SiteManagerException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return new ListInfo();
	}

	protected ListInfo getDataList(String sortKey, boolean desc) {
		try {
			if(this.channel != null && !this.channel.equals(""))
			{
				/**
				 * 如果存在发布上下文，则表示为发布过程中调用本方法
				 */
				if(context != null)
				{
					if(this.site == null)
					{
						List published = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getLatestPubDocAttachListOrderByDocwtime(context.getSiteID(),channel,count,this.doctype);
						ListInfo listInfo = new ListInfo();
						listInfo.setDatas(published);
						return listInfo;
					}
					else
					{
						Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
						List published = CMSUtil.getCMSDriverConfiguration()
												.getCMSService()
												.getChannelManager()
												.getLatestPubDocAttachListOrderByDocwtime(siteinfo.getSiteId() + "",channel,count,this.doctype);
						ListInfo listInfo = new ListInfo();
						listInfo.setDatas(published);
						return listInfo;
					}
				}
				else
				{
					Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					List published = CMSUtil.getCMSDriverConfiguration()
											.getCMSService()
											.getChannelManager()
											.getLatestPubDocAttachListOrderByDocwtime(siteinfo.getSiteId() + "",channel,count,this.doctype);
					ListInfo listInfo = new ListInfo();
					listInfo.setDatas(published);
					return listInfo;
				}
			}
			else
			{
				return new ListInfo();
			}
		} catch (ChannelManagerException e) {
			
			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			
			e.printStackTrace();
		} catch (SiteManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ListInfo();
	}

}
