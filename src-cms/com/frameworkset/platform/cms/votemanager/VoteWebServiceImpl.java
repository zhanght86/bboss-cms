package com.frameworkset.platform.cms.votemanager;

import java.util.List;

import javax.jws.WebService;

import org.apache.log4j.Logger;

import com.frameworkset.platform.cms.container.ContainerImpl;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.votemanager.ws.VoteTitle;
import com.frameworkset.platform.cms.votemanager.ws.VoteWebService;
import com.frameworkset.platform.cms.voteservice.VoteMobileService;

@WebService(name = "VoteWebService", targetNamespace = "com.frameworkset.platform.cms.votemanager.ws")
public class VoteWebServiceImpl implements VoteWebService {
	private VoteMobileService voteService;

	private static Logger logger = Logger.getLogger(VoteWebServiceImpl.class);

	@Override
	public List<VoteTitle> getVoteListByWorkNo(String workNo, String siteName,
			String channel) {
		try {
			ContainerImpl container = new ContainerImpl();
			Site site = CMSUtil.getSiteCacheManager().getSiteByEname(siteName);
			long siteID = site.getSiteId();
			Context context = new DefaultContextImpl(site);
			container.init(context);
			String channelurl = container
					.getPublishedChannelUrlByDisplayName(channel);
			List<VoteTitle> list = voteService.getVoteListByWorkNo(workNo,
					siteID);
			if (list != null && list.size() > 0) {
				for (VoteTitle vote : list) {
					vote.setVoteUrl(channelurl + "?id=" + vote.getId()
							+ "&userId=" + workNo);
				}
				return list;
			}else {
				return null;
			}
		} catch (Exception e) {
			logger.error("获取用户问卷出错" + e.getMessage(), e);
			return null;
		}
	}

	@Override
	public String getVoteCount(String workNo, String siteName) {
		try {
			return voteService.getVoteCount(workNo, siteName);
		} catch (Exception e) {
			logger.error("获取用户问卷统计数出错" + e.getMessage(), e);
			return "0";
		}
	}
}
