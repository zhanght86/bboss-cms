package com.frameworkset.platform.cms.driver.dataloader;

import java.util.List;

import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;
/**
 * 根据频道显示名称取直接子频道，该频道必须有首页
 * <p>Title: CMSNaviChannelListData.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-9-13 9:16:57
 * @author ge.tao
 * @version 1.0
 */
public class CMSNaviChannelListData extends CMSBaseListData implements java.io.Serializable {

	protected ListInfo getDataList(String arg0, boolean arg1, long arg2,
			int arg3) {
		// TODO Auto-generated method stub
//		 TODO Auto-generated method stub
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
						ListInfo lstifo = CMSUtil.getCMSDriverConfiguration()
									  .getCMSService()
									  .getChannelManager()
									  .getDirectSubNaviChannels(super.context.getSiteID(),channel,arg2,arg3);
						return lstifo;
					}
					else
					{
						Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
						ListInfo lstifo = CMSUtil.getCMSDriverConfiguration()
									  .getCMSService()
									  .getChannelManager()
									  .getDirectSubNaviChannels(siteinfo.getSiteId()+"",channel,arg2,arg3);
						return lstifo;
					}
				}
				else
				{
					if(this.channel == null || this.channel.equals("") || this.site == null || this.site.equals(""))
						throw new IllegalArgumentException("没有指定站点的名称和频道名称");
					
					Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					ListInfo lstifo = CMSUtil.getCMSDriverConfiguration()
								  .getCMSService()
								  .getChannelManager()
								  .getDirectSubNaviChannels(siteinfo.getSiteId()+"",channel,arg2,arg3);
					return lstifo;
					
					
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
						List published = CMSUtil.getCMSDriverConfiguration()
												.getCMSService()
												.getChannelManager()
												.getDirectSubNaviChannels(context.getSiteID(),channel,count);
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
												.getDirectSubNaviChannels(siteinfo.getSiteId() + "",channel,count);
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
											.getDirectSubNaviChannels(siteinfo.getSiteId() + "",channel,count);
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
