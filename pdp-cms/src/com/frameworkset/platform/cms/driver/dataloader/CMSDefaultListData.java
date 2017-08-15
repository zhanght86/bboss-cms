package com.frameworkset.platform.cms.driver.dataloader;

import java.util.List;

import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;

/**
 * 缺省的频道数据获取接口,对应于
 * <p>Title: CMSDefaultListData</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2007-4-13 10:35:04
 * @author biaoping.yin
 * @version 1.0
 */
public class CMSDefaultListData extends CMSBaseListData {
	
	/**
	 * 文档分页获取方法
	 */
	protected ListInfo getDataList(String sortKey, boolean desc, long offSet,
			int pageItemsize) {
		// TODO Auto-generated method stub
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
						return CMSUtil.getCMSDriverConfiguration()
									  .getCMSService()
									  .getChannelManager()
									  .getLatestPubDocListOrderByDocwtime(super.context.getSiteID(),channel,(int)offSet,pageItemsize,this.doctype,params);
					}
					else
					{ 
						Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
						return CMSUtil.getCMSDriverConfiguration()
									  .getCMSService()
									  .getChannelManager()
									  .getLatestPubDocListOrderByDocwtime(siteinfo.getSiteId()+"",channel,(int)offSet,pageItemsize,this.doctype,params);
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
					  .getLatestPubDocListOrderByDocwtime(siteinfo.getSiteId()+"",channel,(int)offSet,pageItemsize,this.doctype,params);
					
					
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
	
	/**
	 * 文档列表获取方法
	 */
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
						List published = CMSUtil.getCMSDriverConfiguration().getCMSService().getChannelManager().getLatestPubDocListOrderByDocwtime(context.getSiteID(),channel,count,params);
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
												.getLatestPubDocListOrderByDocwtime(siteinfo.getSiteId() + "",channel,count,params);
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
											.getLatestPubDocListOrderByDocwtime(siteinfo.getSiteId() + "",channel,count,params);
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
