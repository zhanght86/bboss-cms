package com.frameworkset.platform.cms.driver.dataloader;

import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.votemanager.VoteManager;
import com.frameworkset.platform.cms.votemanager.VoteManagerException;
import com.frameworkset.platform.cms.votemanager.VoteManagerImpl;
import com.frameworkset.util.ListInfo;

public class CMSSurveyListData extends CMSBaseListData implements java.io.Serializable {

	protected ListInfo getDataList(String arg0, boolean arg1, long arg2,
			int arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		try {
			VoteManager voteMgr = new VoteManagerImpl();
			
			if(channel != null && !this.channel.equals(""))
			{
				/**
				 * 如果存在发布上下文，则表示为发布过程中调用本方法
				 */
				if(context != null)
				{
					if(this.site == null)
					{
						ListInfo lstifo = new ListInfo();
						lstifo.setDatas(voteMgr.getActiveSurvey(super.context.getSiteID(),channel,count));
						return lstifo;
					}
					else
					{
						Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
						ListInfo lstifo = new ListInfo();
						lstifo.setDatas(voteMgr.getActiveSurvey(siteinfo.getSiteId()+"",channel,count));
						return lstifo;
					}
				}
				else
				{
					if(this.channel == null || this.channel.equals("") || this.site == null || this.site.equals(""))
						throw new IllegalArgumentException("没有指定站点的名称和频道名称");
					
					Site siteinfo = CMSUtil.getSiteCacheManager().getSiteByEname(site);
					ListInfo lstifo = new ListInfo();
					lstifo.setDatas(voteMgr.getActiveSurvey(siteinfo.getSiteId()+"",channel,count));
					return lstifo;
					
					
				}
			}
			else
				return new ListInfo();
		} catch (VoteManagerException e) {
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

}
