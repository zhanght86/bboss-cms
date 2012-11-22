/*
 * @(#)CounterManager.java
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
package com.frameworkset.platform.sanylog.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.BrowserCounter;
import com.frameworkset.platform.sanylog.bean.BrowserVisitInfo;
import com.frameworkset.platform.sanylog.bean.DownLoadCounter;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.VideoHitsCounter;
import com.frameworkset.platform.sanylog.bean.OperateCounter;
import com.frameworkset.util.ListInfo;

/**
 * @author gw_hel
 *
 */
public interface CounterManager {
	//按周统计操作
		public void deleteOperCounterByWeek(String week)throws SQLException;
		public void staticOperCounterByWeek(String startTime, String todayTime,String week)throws SQLException;
	
	public List<BrowserCounter> getBrowserCounterDetail(String browserId)throws SQLException;
	
	//获得所有的应用ID
	public List<String> getSiteList()throws SQLException;
	//按天统计操作量
	public void staticOperCounterByDay(String vtime)throws SQLException;
	//按月统计操作量
	public void staticOperCounterByMonth(String vtime)throws SQLException;
	//按年统计操作量
	public void staticOperCounterByYear(String vtime)throws SQLException;
	//按天删除操作记录数
		public void deleteOperCounterByDay(String vtime)throws SQLException;
	//按月删除操作记录数
	public void deleteOperCounterByMonth(String vtime)throws SQLException;
	//按年删除操作记录数
	public void deleteOperCounterByYear(String vtime)throws SQLException;
	
	public List<OperateCounter> getOperateCounterDetail (String operateId) throws SQLException;
	//获得操作记录日排名
	public ListInfo getOperCounterRankByDay( String appId,String  vtime,int offset,int pagesize)throws SQLException;

	//获得操作记录月度排名
	public ListInfo getOperCounterRankByMonth( String appId,String  vtime,int offset,int pagesize)throws SQLException;

	//获得操作记录年度排名
	public ListInfo getOperCounterRankByYear( String appId,String  vtime,int offset,int pagesize)throws SQLException;
	public ListInfo getOperCounterRankByWeek(String appId,String  vtime,int offset,int pagesize)throws SQLException;
	
	//操作统计插入数据库
	public void incrementOperateCounter(OperateCounter counter) throws SQLException;
	public List<Module> getAllModulesOfApp(String appId) throws SQLException;
    public List<App> getApp( String userId )throws SQLException;
    public List<App> getAdminApp( String userId )throws SQLException;
	/**
	 * 增加浏览计数器
	 * @param counter BrowserCounter
	 * @return 计数值
	 * @throws SQLException
	 */
	public void incrementBrowserCounter(BrowserCounter counter) throws SQLException;

	/**
	 * 获得浏览统计计数
	 * @param siteId 站点ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getBrowserCount(int siteId) throws SQLException;

	/**
	 * 获得浏览统计计数
	 * @param siteId 站点ID
	 * @param channelId 频道ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getBrowserCount(int siteId, int channelId) throws SQLException;

	/**
	 * 获得浏览统计计数
	 * @param siteId 站点ID
	 * @param channleId 频道ID
	 * @param docId 文档ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getBrowserCount(int siteId, int channelId, int docId) throws SQLException;

	/**
	 * 增加下载计数器
	 * @param counter DownLoadCounter
	 * @return 计数值
	 * @throws SQLException
	 */
	public void incrementDownLoadCounter(DownLoadCounter counter) throws SQLException;

	/**
	 * 获取下载计数器计数值 
	 * @param siteId 站点ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getDownLoadCount(int siteId) throws SQLException;

	/**
	 * 获取下载计数器计数值 
	 * @param siteId 站点ID
	 * @param channelId 频道ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getDownLoadCount(int siteId, int channelId) throws SQLException;

	/**
	 * 获取下载计数器计数值 
	 * @param siteId 站点ID
	 * @param channelId 频道ID
	 * @param odcId 文档ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getDownLoadCount(int siteId, int channelId, int docId) throws SQLException;

	/**
	 * 增加视频播放计数器
	 * @param counter VideoHitsCounter
	 * @return 计数值
	 * @throws SQLException
	 */
	public void incrementVideoHitsCounter(VideoHitsCounter counter) throws SQLException;

	/**
	 * 获取视频播放计数值
	 * @param siteId 站点ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getVideoHitsCount(int siteId) throws SQLException;

	/**
	 * 获取视频播放计数值
	 * @param siteId 站点ID
	 * @param channelId 频道ID
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getVideoHitsCount(int siteId, int channelId) throws SQLException;

	/**
	 * 获取视频播放计数值
	 * @param siteId 站点ID
	 * @param channelId 频道ID
	 * @param docId 文档ID
	 * @param enable 是否启用
	 * @return 计数值
	 * @throws SQLException
	 */
	public long getVideoHitsCount(int siteId, int channelId, int docId) throws SQLException;

	/**
	 * 获得视频播放计数器列表
	 * @param counter VideoHitsCounter
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param offset 偏移量
	 * @param pagesize 页面数量
	 * @return ListInfo
	 * @throws SQLException
	 */
	public ListInfo getVideoHitsCounterList(VideoHitsCounter counter, Timestamp startTime, Timestamp endTime,
			int offset, int pagesize) throws SQLException;

	/**
	 * 统计前一天的日访问数据插入到统计表内
	 * @throws SQLException
	 */
	public void statisticBrowserCounter(String startTime, String endTime, Integer siteId) throws SQLException;
	
	/**
	 * 统计前一天的浏览器类型分布数据插入到统计表内
	 * @throws SQLException
	 */
	public void statisticBrowserType(String startTime, String ednTime, Integer siteId) throws SQLException;
	
	/**
	 * 统计前一天的浏览IP地址分布数据插入到统计表内
	 * @throws SQLException
	 */
	public void deleteStatisticBrowserIP(Integer siteId) throws SQLException;
	
	/**
	 * 统计前一天的日访问数据插入到统计表内
	 * @throws SQLException
	 */
	public void deleteStatisticBrowserCounter(Integer siteId) throws SQLException;
	
	/**
	 * 统计前一天的浏览器类型分布数据插入到统计表内
	 * @throws SQLException
	 */
	public void deleteStatisticBrowserType(Integer siteId) throws SQLException;
	
	/**
	 * 统计前一天的浏览IP地址分布数据插入到统计表内
	 * @throws SQLException
	 */
	public void statisticBrowserIP(String stratTime, String endTime, Integer siteId) throws SQLException;

	/**
	 * 获得浏览计数器列表
	 * @param counter BrowserCounter
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param offset 偏移量
	 * @param pagesize 页面数量
	 * @return ListInfo
	 */
	public ListInfo getBrowserCounterList(BrowserCounter counter, Timestamp startTime, Timestamp endTime, int offset,
			int pagesize) throws SQLException;

	/**
	 * 获得浏览计数器小时分布
	 * @param vtime 时间
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getBrowserCounterHourDistribute(int siteId, String vtime) throws SQLException;

	/**
	 * 获得浏览计数统计日分布
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getBrowserCounterDayDistribute(int siteId, String startTime, String endTime) throws SQLException;
	
	/**
	 * 获得浏览计数统计日分布
	 * @param startMonth 开始月份
	 * @param endMonth 结束月份
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getBrowserCounterDayDistributeByMonth(int siteId, int startMonth, int endMonth) throws SQLException;
	
	/**
	 * 获取浏览器类型统计日分布
	 * @param vtime 时间
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getBrowserTypeDayDistribute(int siteId, String vtime) throws SQLException;
	
	/**
	 * 获取浏览器类型统计日分布
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public  List<HashMap> getBrowserTypeDayDistribute(int siteId, String startTime, String endTime) throws SQLException;
	
	/**
	 * 获取浏览器类型统计日分布
	 * @param startMonth 开始月份
	 * @param endMonth 结束月份
	 * @return List<HashMap>
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getBrowserTypeDayDistributeByMonth(int siteId, int startMonth, int endMonth) throws SQLException;
	
	/**
	 * 获取浏览IP地址类型统计日分布
	 * @param vtime 时间
	 * @return List<HashMap>
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getBrowserIPDayDistribute(int siteId, String vtime) throws SQLException;
	
	/**
	 *  获取浏览IP地址类型统计日分布
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return  List<HashMap>
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public List<HashMap> getBrowserIPDayDistribute(int siteId, String startTime, String endTime) throws SQLException;
	
	/**
	 * 获得页面浏览统计汇总信息集合
	 * @param counter BrowserCounter
	 * @param stratTime 开始时间
	 * @param endTime 结束时间
	 * @param offset 偏移量
	 * @param pagesize 页面数量
	 * @return ListInfo
	 */
	public ListInfo getPageBrowserCounterGatherList(BrowserCounter counter, String stratTime, String endTime, int offset,
			int pagesize) throws SQLException;
	
	/**
	 * 
	 * @param stratTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public List<BrowserVisitInfo> getBrowserVisitInfo(int siteId, String startTime, String endTime) throws SQLException;
	
	/**
	 * 获得操作计数器列表
	 * @param counter OperateCounter
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param offset 偏移量
	 * @param pagesize 页面数量
	 * @return ListInfo
	 */
	public ListInfo getOperateCounterList(OperateCounter counter, Timestamp startTime, Timestamp endTime, int offset,int pagesize) throws SQLException;
}
