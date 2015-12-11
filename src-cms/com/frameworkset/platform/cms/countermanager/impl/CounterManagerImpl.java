/*
 * @(#)CounterManagerImpl.java
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
package com.frameworkset.platform.cms.countermanager.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.platform.cms.countermanager.CounterManager;
import com.frameworkset.platform.cms.countermanager.bean.Browser;
import com.frameworkset.platform.cms.countermanager.bean.BrowserCounter;
import com.frameworkset.platform.cms.countermanager.bean.BrowserVisitInfo;
import com.frameworkset.platform.cms.countermanager.bean.DownLoadCounter;
import com.frameworkset.platform.cms.countermanager.bean.VideoHitsCounter;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 *
 */
public class CounterManagerImpl implements CounterManager {

	private ConfigSQLExecutor executor;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.countermanager.CounterManager#getVideoHitsCounterList(com.frameworkset.platform
	 * .cms.counter.bean.VideoHitsCounter, int, int)
	 */
	@Override
	public ListInfo getVideoHitsCounterList(VideoHitsCounter counter, Timestamp startTime, Timestamp endTime,
			int offset, int pagesize) throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", counter.getSiteId());
		paramMap.put("channelId", counter.getChannelId());
		paramMap.put("docId", counter.getDocId());
		paramMap.put("hitUser", counter.getHitUser());
		paramMap.put("hitIP", counter.getHitIP());
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);

		return executor.queryListInfoBean(counter.getClass(), "getVideoHitsCounterList", offset, pagesize, paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserCounterList(com.frameworkset.platform.cms
	 * .counter.bean.BrowserCounter, java.sql.Timestamp, java.sql.Timestamp, int, int)
	 */
	@Override
	public ListInfo getBrowserCounterList(BrowserCounter counter, Timestamp startTime, Timestamp endTime, int offset,
			int pagesize) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", counter.getSiteId());
		paramMap.put("channelId", counter.getChannelId());
		paramMap.put("docId", counter.getDocId());
		paramMap.put("browserType", StringUtil.isEmpty(counter.getBrowserType()) ? null : counter.getBrowserType()
				+ "%");
		paramMap.put("browserIp", counter.getBrowserIp());
		paramMap.put("browserUser", counter.getBrowserUser());
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);

		return executor.queryListInfoBean(counter.getClass(), "getBrowserCounterList", offset, pagesize, paramMap);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserCounterHourDistribute(int siteId, String vtime) throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("vtime", vtime);

		return executor.queryListBean(HashMap.class, "getBrowserCounterHourDistribute", paramMap);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserCounterDayDistribute(int siteId, String startTime, String endTime)
			throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);

		return executor.queryListBean(HashMap.class, "getBrowserCounterDayDistribute", paramMap);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserCounterDayDistributeByMonth(int siteId, int startMonth, int endMonth)
			throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("startMonth", startMonth);
		paramMap.put("endMonth", endMonth);

		return executor.queryListBean(HashMap.class, "getBrowserCounterDayDistributeByMonth", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.countermanager.CounterManager#incrementBrowserCounter(com.frameworkset.platform
	 * .cms.countermanager.bean.BrowserCounter)
	 */
	@Override
	public void incrementBrowserCounter(BrowserCounter counter) throws SQLException {
		counter.setBrowserTime(new Timestamp(new java.util.Date().getTime()));
		executor.insertBean("incrementBrowserCounter", counter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserCount(int)
	 */
	@Override
	public long getBrowserCount(int siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);

		return executor.queryObjectBean(long.class, "countBrowserCounter", paramMap);
	}
	

	/**
	 * 获得浏览统计计数和今日统计计数
	 * @param siteId 站点ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public Browser getTotalAndTodayBrowserCount(int siteId) throws SQLException
	{
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);

		return executor.queryObjectBean(Browser.class, "countTodayAndTotalBrowserCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserCount(int, int, boolean)
	 */
	@Override
	public long getBrowserCount(int siteId, int channelId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("channelId", channelId);

		return executor.queryObjectBean(Long.class, "countBrowserCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserCount(int, int, int, boolean)
	 */
	@Override
	public long getBrowserCount(int siteId, int channelId, int docId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("channelId", channelId);
		paramMap.put("docId", docId);

		return executor.queryObjectBean(Long.class, "countBrowserCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.countermanager.CounterManager#incrementDownLoadCounter(com.frameworkset.platform
	 * .cms.countermanager.bean.DownLoadCounter)
	 */
	@Override
	public void incrementDownLoadCounter(DownLoadCounter counter) throws SQLException {
		executor.insertBean("incrementDownlodCounter", counter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getDownLoadCount(int, boolean)
	 */
	@Override
	public long getDownLoadCount(int siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);

		return executor.queryObjectBean(Long.class, "countDownLoadCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getDownLoadCount(int, int, boolean)
	 */
	@Override
	public long getDownLoadCount(int siteId, int channelId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("channelId", channelId);

		return executor.queryObjectBean(Long.class, "countDownLoadCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getDownLoadCount(int, int, int, boolean)
	 */
	@Override
	public long getDownLoadCount(int siteId, int channelId, int docId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("channelId", channelId);
		paramMap.put("docId", docId);

		return executor.queryObjectBean(Long.class, "countDownLoadCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.countermanager.CounterManager#incrementVideoHitsCounter(com.frameworkset.platform
	 * .cms.countermanager.bean.VideoHitsCounter)
	 */
	@Override
	public void incrementVideoHitsCounter(VideoHitsCounter counter) throws SQLException {
		counter.setHitTime(new Timestamp(new java.util.Date().getTime()));
		executor.insertBean("incrementVideoHitsCounter", counter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getVideoHitsCount(int, boolean)
	 */
	@Override
	public long getVideoHitsCount(int siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);

		return executor.queryObjectBean(Long.class, "countVideoHitsCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getVideoHitsCount(int, int, boolean)
	 */
	@Override
	public long getVideoHitsCount(int siteId, int channelId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("channelId", channelId);

		return executor.queryObjectBean(Long.class, "countVideoHitsCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getVideoHitsCount(int, int, int, boolean)
	 */
	@Override
	public long getVideoHitsCount(int siteId, int channelId, int docId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("channelId", channelId);
		paramMap.put("docId", docId);

		return executor.queryObjectBean(Long.class, "countVideoHitsCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#statisticBrowserCounterADay()
	 */
	@Override
	public void statisticBrowserCounter(String startTime, String endTime, Integer siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("siteId", siteId);

		executor.insertBean("statisticBrowserCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#statisticBrowserTypeADay()
	 */
	@Override
	public void statisticBrowserType(String startTime, String endTime, Integer siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("siteId", siteId);

		executor.insertBean("statisticBrowserType", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#statisticBrowserIPADay()
	 */
	@Override
	public void statisticBrowserIP(String startTime, String endTime, Integer siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		paramMap.put("siteId", siteId);

		executor.insertBean("statisticBrowserIP", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserTypeDistributeToday(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserTypeDayDistribute(int siteId, String vtime) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("vtime", vtime);

		return executor.queryListBean(HashMap.class, "getBrowserTypeDayDistributeByTime", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserTypeDayDistribute(java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserTypeDayDistribute(int siteId, String startTime, String endTime) throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);

		return executor.queryListBean(HashMap.class, "getBrowserTypeDayDistributeByTimeLimit", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserTypeDayDistributeByMonth(int, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserTypeDayDistributeByMonth(int siteId, int startMonth, int endMonth)
			throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("startMonth", startMonth);
		paramMap.put("endMonth", endMonth);

		return executor.queryListBean(HashMap.class, "getBrowserTypeDayDistributeByMonth", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserIPDayDistribute(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserIPDayDistribute(int siteId, String vtime) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("vtime", vtime);

		return executor.queryListBean(HashMap.class, "getBrowserIPDayDistributeByTime", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserIPDayDistribute(java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<HashMap> getBrowserIPDayDistribute(int siteId, String startTime, String endTime) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);

		return executor.queryListBean(HashMap.class, "getBrowserIPDayDistributeByTimeLimit", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.countermanager.CounterManager#getPageBrowserCounterGatherList(com.frameworkset.
	 * platform.cms.countermanager.bean.BrowserCounter, int, int)
	 */
	@Override
	public ListInfo getPageBrowserCounterGatherList(BrowserCounter counter, String startTime, String endTime,
			int offset, int pagesize) throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", counter.getSiteId());
		paramMap.put("channelId", counter.getChannelId());
		paramMap.put("docName", counter.getDocName());
		paramMap.put("pageURL", counter.getPageURL());
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);

		return executor.queryListInfoBean(BrowserCounter.class, "getPageBrowserCounterGatherList", offset, pagesize,
				paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#getBrowserVisitInfo(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<BrowserVisitInfo> getBrowserVisitInfo(int siteId, String startTime, String endTime) throws SQLException {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);

		return executor.queryListBean(BrowserVisitInfo.class, "getBrowserVisitInfo", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#deleteStatisticBrowserIP(java.lang.Integer)
	 */
	@Override
	public void deleteStatisticBrowserIP(Integer siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);

		executor.deleteBean("deleteStatisticBrowserIP", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#deleteBrowserCounter(java.lang.Integer)
	 */
	@Override
	public void deleteStatisticBrowserCounter(Integer siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);

		executor.deleteBean("deleteStatisticBrowserCounter", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.countermanager.CounterManager#deleteStatisticBrowserType(java.lang.Integer)
	 */
	@Override
	public void deleteStatisticBrowserType(Integer siteId) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("siteId", siteId);

		executor.deleteBean("deleteStatisticBrowserType", paramMap);
	}
}
