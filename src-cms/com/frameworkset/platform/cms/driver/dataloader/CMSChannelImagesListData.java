package com.frameworkset.platform.cms.driver.dataloader;

import java.util.List;

import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;

/**
 * 内容管理系统 获得频道下所有图片 数据接口
 * <p>Title: CMSImageTagsListData.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date Jun 25, 2007 7:19:27 PM
 * @author ge.tao
 * @version 1.0
 */
public class CMSChannelImagesListData extends CMSBaseListData implements java.io.Serializable {

	protected ListInfo getDataList(String sortkey, boolean desc, long offset,
			int maxpagesize) {		
//		 TODO Auto-generated method stub
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
						ListInfo listInfo = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getPubDocListOfChannel(context.getSiteID(),channel,offset,maxpagesize);
						
						return listInfo;
					}
					else
					{
						Site siteinfo =CMSUtil.getSiteCacheManager().getSiteByEname(site);
						ListInfo listInfo = CMSUtil.getCMSDriverConfiguration()
												.getCMSService()
												.getChannelManager()
												.getPubDocListOfChannel(siteinfo.getSiteId() + "",channel,offset,maxpagesize);						
//						ListInfo listInfo = new ListInfo();
//						listInfo.setDatas(published);
						return listInfo;
					}
				}
				else
				{
					Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					ListInfo listInfo = CMSUtil.getCMSDriverConfiguration()
											.getCMSService()
											.getChannelManager()
											.getPubDocListOfChannel(siteinfo.getSiteId() + "",channel,offset,maxpagesize);
//					ListInfo listInfo = new ListInfo();
//					listInfo.setDatas(published);
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

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
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
						List published = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getPubDocListOfChannel(context.getSiteID(),channel,count);
						ListInfo listInfo = new ListInfo();
						listInfo.setDatas(published);
						return listInfo;
					}
					else
					{
						Site siteinfo =CMSUtil.getSiteCacheManager().getSiteByEname(site);
						List published = CMSUtil.getCMSDriverConfiguration()
												.getCMSService()
												.getChannelManager()
												.getPubDocListOfChannel(siteinfo.getSiteId() + "",channel,count);						
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
											.getPubDocListOfChannel(siteinfo.getSiteId() + "",channel,count);
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
