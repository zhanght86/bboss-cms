package com.frameworkset.platform.cms.driver.dataloader;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerException;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.util.ListInfo;

/**
 * 专题和文档的混合概览数据接口 获取 频道下的 直接子频道 和文档 (引用的另外实现)
 * <p>Title: CMSSpecialDocDataList.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date Jun 5, 2005 10:33:41 AM
 * @author ge.tao
 * @version 1.0
 */
public class CMSDirectSpecialDocDataList extends CMSBaseListData implements java.io.Serializable {
	
	/**
	* 分页显示时获取每页的数据项
	* sortKey:排序字段
	* desc:排序的秩序，true为降序，false为升序
	* @param offSet - 从数据源获取数据的游标位置
	*
	* @param pageItemsize - 每页显示的数据条数
	* @return java.util.List
	 * @throws DriverConfigurationException 
	 * @throws DocumentManagerException 
	*/
	protected ListInfo getDataList(String sortKey,
										 boolean desc,
										 long offSet,
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
						ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
									  .getCMSService()
									  .getDocumentManager()
									  .getDirectSpecialDocList(super.context.getSiteID(),channel,offSet,pageItemsize));
						return lstifo;
					}
					else
					{
						Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
						ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
									  .getCMSService()
									  .getDocumentManager()
									  .getDirectSpecialDocList(siteinfo.getSiteId()+"",channel,offSet,pageItemsize));
						return lstifo;
					}
				}
				else
				{
					if(this.channel == null || this.channel.equals("") || this.site == null || this.site.equals(""))
						throw new IllegalArgumentException("没有指定站点的名称和频道名称");
					
					Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					ListInfo lstifo = (CMSUtil.getCMSDriverConfiguration()
								  .getCMSService()
								  .getDocumentManager()
								  .getDirectSpecialDocList(siteinfo.getSiteId()+"",channel,offSet,pageItemsize));
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

	/**
	 * 如果分页tag只是作为列表来实现时，调用该方法
	 * sortKey:排序字段
	 * desc:排序的秩序，true为降序，false为升序
	 * @return java.util.List
	 */
	protected ListInfo getDataList(String sortKey,
									 boolean desc){
		
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
								                 .getDocumentManager()
								                 .getDirectSpecialDocList(context.getSiteID(),channel,count);
						ListInfo listInfo = new ListInfo();
						listInfo.setDatas(published);
						return listInfo;
					}
					else
					{
						Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
						List published =CMSUtil.getCMSDriverConfiguration()
				                 				.getCMSService()
				                 				.getDocumentManager()
												.getDirectSpecialDocList(siteinfo.getSiteId() + "",channel,count);
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
											.getDocumentManager()
											.getDirectSpecialDocList(siteinfo.getSiteId() + "",channel,count);
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
