package com.frameworkset.platform.sanylog.service;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import com.frameworkset.platform.cms.countermanager.CounterManager;
import com.frameworkset.platform.cms.countermanager.bean.VideoHitsCounter;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class DocPlayedList extends com.frameworkset.common.tag.pager.DataInfoImpl {

	/**
	 *  serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	protected ListInfo getDataList(String sortKey, boolean desc, long offset, int maxPagesize) {
		String docId = request.getParameter("docId");

		String userName = request.getParameter("userName");
		String ipAddress = request.getParameter("ipAddress");
		String subTimeBgin = request.getParameter("subTimeBgin");
		String subTimeEnd = request.getParameter("subTimeEnd");

		VideoHitsCounter videoHitsCounter = new VideoHitsCounter();
		videoHitsCounter.setDocId(Integer.valueOf(docId));
		videoHitsCounter.setHitIP(ipAddress);
		videoHitsCounter.setHitUser(userName);

		Timestamp startTime = null;
		Timestamp endTime = null;
		if (!StringUtil.isEmpty(subTimeBgin)) {
			startTime = Timestamp.valueOf(subTimeBgin);
		}
		if (!StringUtil.isEmpty(subTimeEnd)) {
			endTime = Timestamp.valueOf(subTimeEnd);
		}

		CounterManager counterManager = (CounterManager) WebApplicationContextUtils.getWebApplicationContext()
				.getBeanObject("counterManager");

		try {
			return counterManager.getVideoHitsCounterList(videoHitsCounter, startTime, endTime, (int) offset, maxPagesize);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected ListInfo getDataList(String arg0, boolean arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
