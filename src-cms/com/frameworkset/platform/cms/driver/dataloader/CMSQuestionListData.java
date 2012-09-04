package com.frameworkset.platform.cms.driver.dataloader;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.driver.config.DriverConfigurationException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.votemanager.VoteManager;
import com.frameworkset.platform.cms.votemanager.VoteManagerException;
import com.frameworkset.platform.cms.votemanager.VoteManagerImpl;
import com.frameworkset.util.ListInfo;

public class CMSQuestionListData extends CMSBaseListData implements java.io.Serializable {

	protected ListInfo getDataList(String arg0, boolean arg1, long arg2,
			int arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		try {
			if (this.channel != null && !this.channel.equals("")) {
				ListInfo listInfo = new ListInfo();
				if (isNumber(channel)) {
					VoteManager voteMgr = new VoteManagerImpl();
					listInfo.setDatas(voteMgr.getQstionsOfServey(Integer
							.parseInt(channel)));
				} else {
					String siteid = "";
					ChannelManagerImpl chnlMgr = new ChannelManagerImpl();
					Channel chnl = chnlMgr.getChannelInfoByDisplayName(context.getSiteID(),channel);
					
					VoteManager voteMgr = new VoteManagerImpl();
					listInfo.setDatas(voteMgr.getQstionsOfServey(voteMgr.getLatestActiveSurveyIDInChnl(String.valueOf(chnl.getChannelId()))));
				}
				return listInfo;
			} else if (CMSUtil.getCurrentChannel(context)!=null){
				ListInfo listInfo = new ListInfo();
				Channel chnl = CMSUtil.getCurrentChannel(context);
				VoteManager voteMgr = new VoteManagerImpl();
				listInfo.setDatas(voteMgr.getQstionsOfServey(voteMgr.getLatestActiveSurveyIDInChnl(String.valueOf(chnl.getChannelId()))));

				return listInfo;
			}else {
				String siteid = "";
				if (context != null && this.site == null) {
					siteid = super.context.getSiteID();
				} else {
					siteid = CMSUtil.getSiteCacheManager().getSiteByEname(site)
							.getSiteId() + "";
				}
				
				ListInfo listInfo = new ListInfo();
				VoteManager voteMgr = new VoteManagerImpl();
				listInfo.setDatas(voteMgr.getQstionsOfServey(voteMgr.getTopSurveyID(Integer.parseInt(siteid))));
				return listInfo;

			}

		} catch (VoteManagerException e) {

			e.printStackTrace();
		} catch (DriverConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SiteManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new ListInfo();
	}
	
	public boolean isNumber(String num){
		try{
			Integer.parseInt(num);
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
