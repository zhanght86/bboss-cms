/*
 * @(#)DocumentController.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.cms.channelmanager.action;

import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.bean.ChannelCondition;
import com.frameworkset.platform.cms.container.Container;
import com.frameworkset.platform.cms.container.ContainerImpl;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 * 频道服务控制器
 */
public class ChannelController {

	private ChannelManager channelManager;

	private SiteManager siteManager;

	/**
	 * 展示专题列表
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式
	 * @param offset 偏移量
	 * @param pagesize 页面数据大小
	 * @param condition 查询条件
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return List<Document>
	 * @throws Exception Exception
	 */
	public String showReportsList(@PagerParam(name = PagerParam.SORT, defaultvalue = "title") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "15") int pagesize, ChannelCondition condition,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (!StringUtil.isEmpty(condition.getKeywords())) {
			condition.setKeywords(URLDecoder.decode(condition.getKeywords(), "UTF-8"));
		}

		ListInfo reportsList = channelManager.querySubChannels((int) offset, pagesize, condition);

		if (!CollectionUtils.isEmpty(reportsList.getDatas())) {
			Container container = new ContainerImpl();
			String siteName = ((Channel) reportsList.getDatas().get(0)).getSiteName();
			container.init(siteName, request, request.getSession(), response);

			for (Object obj : reportsList.getDatas()) {
				Channel channel = (Channel) obj;
				String channelpuburl = container.getPublishedChannelUrlByID(String.valueOf(channel.getChannelId()));
				channel.setChannelpuburl(channelpuburl);
			}
		}

		request.setAttribute("reportsList", reportsList);

		return "path:showReportsList";
	}

	/**
	 * 根据站点ID获取站点下的所有频道
	 * @param siteId 站点ID
	 * @return List<Channel>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public @ResponseBody(datatype = "json")
	List<Channel> getChannelBySiteId(String siteId) throws Exception {
		return siteManager.getAllChannelsOfSite(siteId);
	}
}
