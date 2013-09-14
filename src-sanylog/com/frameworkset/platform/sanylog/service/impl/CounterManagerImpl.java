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
package com.frameworkset.platform.sanylog.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.platform.sanylog.service.CounterManager;
import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.BrowserCounter;
import com.frameworkset.platform.sanylog.bean.BrowserVisitInfo;
import com.frameworkset.platform.sanylog.bean.DownLoadCounter;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.OperChart;
import com.frameworkset.platform.sanylog.bean.OperRank;
import com.frameworkset.platform.sanylog.bean.VideoHitsCounter;
import com.frameworkset.platform.sanylog.bean.OperateCounter;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author qingl2
 *
 */
public class CounterManagerImpl implements CounterManager {

	private ConfigSQLExecutor executor;
	@Override
	public ListInfo getOperCounterRankByWeek(String appId, String vtime,
			int offset, int pagesize) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appId", appId);
		paramMap.put("vtime", vtime);
		return executor.queryListInfoBean(OperRank.class, "getOperCounterRankByWeek", offset, pagesize, paramMap);	
	}
	//获得操作记录日排名
		public ListInfo getOperCounterRankByDay( String appId,String  vtime,int offset,int pagesize)throws SQLException{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("appId", appId);
			paramMap.put("vtime", vtime);
			return executor.queryListInfoBean(OperRank.class, "getOperCounterRankByDay", offset, pagesize, paramMap);		
			};

		//获得操作记录月度排名
		public ListInfo getOperCounterRankByMonth( String appId,String  vtime,int offset,int pagesize)throws SQLException{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("appId", appId);
			paramMap.put("vtime", vtime);
			return executor.queryListInfoBean(OperRank.class, "getOperCounterRankByMonth", offset, pagesize, paramMap);		
			};

		//获得操作记录年度排名
		public ListInfo getOperCounterRankByYear( String appId,String  vtime,int offset,int pagesize)throws SQLException{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("appId", appId);
			paramMap.put("vtime", vtime);
			return executor.queryListInfoBean(OperRank.class, "getOperCounterRankByYear", offset, pagesize, paramMap);		
			};

	@Override
	public void incrementOperateCounter(OperateCounter counter)
			throws SQLException {
		// TODO Auto-generated method stub
		executor.insertBean("incrementOperateCounter", counter);
	}
	@Override
	public List<Module> getAllModulesOfApp(String appId) throws SQLException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		List<Module> list = new ArrayList<Module>();
		System.out.println("appId=="+appId);
		String sql="select MODULE_ID,MODULE_NAME  from td_log_module_manager where app_id = '"+appId+"'";
		try{
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				Module mod = new Module ();
				mod.setModuleId(db.getString(i,"MODULE_ID"));
				mod.setModuleName(db.getString(i,"MODULE_NAME"));
				list.add(mod);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<App> getApp( String userId ) throws SQLException {
		DBUtil db = new DBUtil();
		List<App> list = new ArrayList<App>();
		String sql="select APP_ID,APP_NAME  from td_log_user_authorize where user_id = '"+userId+"'";
		try{
			db.executeSelect(sql);
			for(int i=0;i<db.size();i++){
				App app = new App();	
				app.setAppId(db.getString(i,"APP_ID"));
				app.setAppName(db.getString(i,"APP_NAME"));
				list.add(app);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}
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

		return executor.queryObjectBean(Long.class, "countBrowserCounter", paramMap);
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

	@Override
	public ListInfo getOperateCounterList(OperateCounter counter, Timestamp startTime, Timestamp endTime, int offset,int pagesize) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appId", counter.getAppId());
		paramMap.put("moduleId", counter.getModuleId());
		paramMap.put("pageName", counter.getPageName());
		paramMap.put("browserType", StringUtil.isEmpty(counter.getBrowserType()) ? null : counter.getBrowserType()
				+ "%");
		paramMap.put("operateIp", counter.getOperateIp());
		paramMap.put("operator", counter.getOperator());
		paramMap.put("operation", counter.getOperation());
		paramMap.put("operContent", counter.getOperContent());
		paramMap.put("moduleCode", counter.getModuleCode());

		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		
		

		return executor.queryListInfoBean(counter.getClass(), "getOperateCounterList", offset, pagesize, paramMap);
	}
	@Override
	public List<OperateCounter> getOperateCounterDetail(String operateId)
			throws SQLException {
		// TODO Auto-generated method stub
		
		List<OperateCounter> datas = executor.queryList(OperateCounter.class, "getOperateCounterDetail", operateId);
		
		return datas;
	}

	@Override
	public void staticOperCounterByDay(String vtime) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", vtime);

		executor.insertBean("statisticOperCounterByDay", paramMap);
	}

	@Override
	public void staticOperCounterByMonth(String vtime) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", vtime);

		executor.insertBean("statisticOperCounterByMonth", paramMap);
	}

	@Override
	public void staticOperCounterByYear(String vtime) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", vtime);

		executor.insertBean("statisticOperCounterByYear", paramMap);
	}

	@Override
	public void deleteOperCounterByMonth(String vtime) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", vtime);

		executor.deleteBean("deleteOperCounterByMonth", paramMap);
	}

	@Override
	public void deleteOperCounterByYear(String vtime) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", vtime);

		executor.deleteBean("deleteOperCounterByYear", paramMap);
	}
	@Override
	public void deleteOperCounterByDay(String vtime) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", vtime);

		executor.deleteBean("deleteOperCounterByDay", paramMap);
	}
	@Override
	public List<String> getSiteList()throws SQLException{
		
		return executor.queryList(String.class, "getAllAppId", "");
	}

	@Override
	public List<BrowserCounter> getBrowserCounterDetail(String browserId)throws SQLException {
		List<BrowserCounter> datas = executor.queryList(BrowserCounter.class, "getBrowserCounterDetail", browserId);
		return datas;
	}

	@Override
	public List<App> getAdminApp(String userId) throws SQLException {
		List<App> datas = executor.queryList(App.class, "getAdminApp");
		return datas;
	}

	@Override
	public void deleteOperCounterByWeek(String week) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", week);

		executor.deleteBean("deleteOperCounterByWeek", paramMap);
	}

	@Override
	public void staticOperCounterByWeek(String startTime, String todayTime,String week)
			throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("Time", week);
		paramMap.put("startTime", startTime);
		paramMap.put("todayTime", todayTime);
		executor.insertBean("staticOperCounterByWeek", paramMap);
	}
	@Override
	public List<OperRank> getExcelDatas(String time, String type, String appId)
			throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("time", time);
		paramMap.put("type", type);
		paramMap.put("appId", appId);
		List<OperRank> datas = executor.queryListBean(OperRank.class, "getExcelDatas", paramMap);
		return datas;
	}
	/**操作统计新报表
	 * @author qingl2
	*/
	@Override
	public List<OperChart> getOperChartCount(String appId, String time,
			String tableName,float divisor) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		paramMap.put("time", time);
		paramMap.put("appId", appId);
		paramMap.put("divisor", divisor);
		List<OperChart> datas = executor.queryListBean(OperChart.class, "getOperChartCount", paramMap);
		return datas;
	}
	@Override
	public List<OperChart> getOperChartUser(String appId, String time,
			String tableName, float divisor) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		paramMap.put("time", time);
		paramMap.put("appId", appId);
		paramMap.put("divisor", divisor);
		List<OperChart> datas = executor.queryListBean(OperChart.class, "getOperChartUser", paramMap);
		return datas;
	}
	@Override
	public List<OperRank> getOperRankForCompareByDay(String appId,
			String startTime, String endTime, String tableName)
			throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		paramMap.put("startTime", startTime);
		paramMap.put("appId", appId);
		paramMap.put("endTime", endTime);
		List<OperRank> datas = executor.queryListBean(OperRank.class, "getOperRankForCompare", paramMap);
		return datas;
	}
	@Override
	public List<String> getTimeNodes(String startTime, String endTime,
			String tableName) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		List<String> datas = executor.queryListBean(String.class, "getTimeNodes", paramMap);
		return datas;
	}
	@Override
	public List<String> getModuleNodes(String appId, String startTime,
			String endTime, String tableName) throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("tableName", tableName);
		paramMap.put("startTime", startTime);
		paramMap.put("appId", appId);
		paramMap.put("endTime", endTime);
		List<String> datas = executor.queryListBean(String.class, "getModuleNodes", paramMap);
		return datas;
	}

	

	


	
	
	
}
