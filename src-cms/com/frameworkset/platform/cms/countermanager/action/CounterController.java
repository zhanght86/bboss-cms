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
package com.frameworkset.platform.cms.countermanager.action;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.tools.ant.util.DateUtils;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.countermanager.CounterManager;
import com.frameworkset.platform.cms.countermanager.bean.BrowserCounter;
import com.frameworkset.platform.cms.countermanager.bean.BrowserVisitInfo;
import com.frameworkset.platform.cms.countermanager.bean.VideoHitsCounter;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 * 计数服务控制器
 */
public class CounterController {

	private final String[] browserTypeSet = { "MSIE", "Firefox", "Safari", "Chrome" };

	private CounterManager counterManager;

	/**
	 * 首页面 
	 * @return String
	 */
	public String index() {
		return "path:index";
	}

	/**
	 * 视频点击播放计数
	 * @param paramCounter 计数器参数
	 * @param enable 是否启用
	 * @param request HttpServletRequest
	 * @return long 视频播放计数 
	 * @throws Exception
	 */
	public @ResponseBody(datatype = "jsonp")
	long videoHitsCount(VideoHitsCounter paramCounter, boolean enable, HttpServletRequest request) throws Exception {

		paramCounter.setHitId(UUID.randomUUID().toString());

		String siteName = paramCounter.getSiteName();
		String channelName = paramCounter.getChannelName();
		String docName = paramCounter.getDocName();

		paramCounter.setSiteName(StringUtil.isEmpty(siteName) ? null : URLDecoder.decode(siteName, "UTF-8"));
		paramCounter.setChannelName(StringUtil.isEmpty(channelName) ? null : URLDecoder.decode(channelName, "UTF-8"));
		paramCounter.setDocName(StringUtil.isEmpty(docName) ? null : URLDecoder.decode(docName, "UTF-8"));

		paramCounter.setHitIP(request.getRemoteAddr());
		paramCounter.setHitUser(AccessControl.getAccessControl().getUserAccount());
		paramCounter.setReferer(request.getHeader("Referer"));

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			counterManager.incrementVideoHitsCounter(paramCounter);
			long ret = 0;
			if (enable) {
				ret = counterManager.getVideoHitsCount(paramCounter.getSiteId(), paramCounter.getChannelId(),
						paramCounter.getDocId());
			}
			tm.commit();
			return ret;
		} finally {
			tm.release();
		}
	}

	/**
	 * 浏览器计数
	 * @param paramCounter 计数器参数
	 * @param enable 是否启用
	 * @param request HttpServletRequest
	 * @return long 浏览计数 
	 * @throws Exception
	 */
	public @ResponseBody(datatype = "jsonp")
	long browserCounter(BrowserCounter paramCounter, boolean enable, HttpServletRequest request) throws Exception {

		paramCounter.setBrowserId(UUID.randomUUID().toString());

		String siteName = paramCounter.getSiteName();
		String channelName = paramCounter.getChannelName();
		String docName = paramCounter.getDocName();

		paramCounter.setSiteName(StringUtil.isEmpty(siteName) ? null : URLDecoder.decode(siteName, "UTF-8"));
		paramCounter.setChannelName(StringUtil.isEmpty(channelName) ? null : URLDecoder.decode(channelName, "UTF-8"));
		paramCounter.setDocName(StringUtil.isEmpty(docName) ? null : URLDecoder.decode(docName, "UTF-8"));

		paramCounter.setBrowserIp(request.getRemoteAddr());
		paramCounter.setBrowserUser(AccessControl.getAccessControl().getUserAccount());
		paramCounter.setPageURL(paramCounter.getPageURL());

		String userAgent = request.getHeader("User-Agent");
		for (String agent : userAgent.split(";")) {
			for (String browser : browserTypeSet) {
				if (agent.indexOf(browser) > 0) {
					paramCounter.setBrowserType(agent.substring(agent.indexOf(browser)).replaceAll("/", " "));
					break;
				}
			}
		}
		if(StringUtil.isEmpty(paramCounter.getReferer()))
			paramCounter.setReferer(request.getHeader("Referer"));

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			counterManager.incrementBrowserCounter(paramCounter);
			long ret = 0;
			if (enable) {
				ret = counterManager.getBrowserCount(paramCounter.getSiteId());
			}
			tm.commit();
			return ret;
		} finally {
			tm.release();
		}
	}

	/**
	 * 展示浏览计数统计数据列表
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式
	 * @param offset 偏移量
	 * @param pagesize 页面数据大小
	 * @param counter 查询条件
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param model 作用域模型
	 * @return String
	 */
	public String showBrowserCounterList(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "browserTime") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, BrowserCounter counter,
			Timestamp startTime, Timestamp endTime, ModelMap model) throws Exception {

		ListInfo browserCounterDataList = counterManager.getBrowserCounterList(counter, startTime, endTime,
				(int) offset, pagesize);

		model.addAttribute("browserCounterDataList", browserCounterDataList);

		return "path:showBrowserCounterList";
	}

	/**
	 * 展示浏览计数统计小时分布图
	 * @param type 类型：无:今日， yesterday:昨日
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public @ResponseBody
	String showBrowserCounterHourDistribute(int siteId, String type, String startTime) throws Exception {

		String caption = "浏览计数统计小时分布";
		String subCaption = "今日访问趋势";

		Calendar calendar = Calendar.getInstance();
		if ("yesterday".equals(type)) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			subCaption = "昨日访问趋势";
		}

		if (StringUtil.isEmpty(startTime)) {
			startTime = DateUtils.format(calendar.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}

		List<HashMap> browserCounterDayDistribute = counterManager.getBrowserCounterHourDistribute(siteId, startTime);

		StringBuilder xml = new StringBuilder("<chart caption='")
				.append(caption)
				.append("' subCaption='")
				.append(subCaption)
				.append("' yMaxValue='100' bgColor='406181, 6DA5DB'  bgAlpha='100' baseFontColor='FFFFFF' canvasBgAlpha='0' canvasBorderColor='FFFFFF' divLineColor='FFFFFF' divLineAlpha='100' numVDivlines='10' vDivLineisDashed='1' showAlternateVGridColor='1' lineColor='BBDA00' anchorRadius='4' anchorBgColor='BBDA00' anchorBorderColor='FFFFFF' anchorBorderThickness='2' showValues='0' toolTipBgColor='406181' toolTipBorderColor='406181' alternateHGridAlpha='5'>");

		if (!CollectionUtils.isEmpty(browserCounterDayDistribute)) {
			for (int i = 1; i <= 24; i++) {

				int vhour = i;
				int vcount = 0;

				for (HashMap map : browserCounterDayDistribute) {
					int time = Integer.parseInt(map.get("VHOUR").toString());

					if (time == i) {
						vcount = Integer.parseInt(map.get("VCOUNT").toString());

						break;
					}
				}

				xml.append("<set label='").append(vhour).append(":00'").append(" value='").append(vcount)
						.append("' />");
			}
		}

		xml.append("<styles><definition><style name='LineShadow' type='shadow' color='333333' distance='6'/></definition><application><apply toObject='DATAPLOT' styles='LineShadow' /></application></styles></chart> ");

		return xml.toString();
	}

	/**
	 * 展示浏览计数统计日分布图
	 * @param type 类型：week:本周，7days:最近7天，month:本月，30days:最近30天
	 * @return String
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public @ResponseBody
	String showBrowserCounterDayDistribute(int siteId, String type, String startTime, String endTime) throws Exception {

		String caption = "浏览计数统计日分布";
		String subCaption = "";

		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		int addDay = 0;

		if ("week".equals(type)) {
			int offset = startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
			endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.DAY_OF_MONTH, 6);

			addDay = 6;

			subCaption = "本周访问趋势";
		} else if ("7days".equals(type)) {
			startDate.add(Calendar.DAY_OF_MONTH, -6);

			addDay = 6;

			subCaption = "最近7天访问趋势";
		} else if ("month".equals(type)) {
			int offset = startDate.get(Calendar.DAY_OF_MONTH);
			startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
			endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.MONTH, 1);
			endDate.add(Calendar.DAY_OF_MONTH, -1);

			addDay = startDate.getActualMaximum(Calendar.DAY_OF_MONTH)
					- startDate.getActualMinimum(Calendar.DAY_OF_MONTH);

			subCaption = "本月访问趋势";
		} else if ("30days".equals(type)) {
			startDate.add(Calendar.DAY_OF_MONTH, -29);

			addDay = 29;

			subCaption = "最近30天访问趋势";
		}

		List<Calendar> calendarList = new ArrayList<Calendar>();
		for (int i = 0; i <= addDay; i++) {
			Calendar tempStartDate = (Calendar) startDate.clone();
			tempStartDate.add(Calendar.DAY_OF_MONTH, i);
			calendarList.add(tempStartDate);
		}

		if (StringUtil.isEmpty(startTime)) {
			startTime = DateUtils.format(startDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}
		if (StringUtil.isEmpty(endTime)) {
			endTime = DateUtils.format(endDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}

		List<HashMap> browserCounterDayDistribute = counterManager.getBrowserCounterDayDistribute(siteId, startTime,
				endTime);

		StringBuilder xml = new StringBuilder("<chart caption='")
				.append(caption)
				.append("' subCaption='")
				.append(subCaption)
				.append("' yMaxValue='100' bgColor='406181, 6DA5DB'  bgAlpha='100' baseFontColor='FFFFFF' canvasBgAlpha='0' canvasBorderColor='FFFFFF' divLineColor='FFFFFF' divLineAlpha='100' numVDivlines='10' vDivLineisDashed='1' showAlternateVGridColor='1' lineColor='BBDA00' anchorRadius='4' anchorBgColor='BBDA00' anchorBorderColor='FFFFFF' anchorBorderThickness='2' showValues='0' toolTipBgColor='406181' toolTipBorderColor='406181' alternateHGridAlpha='5'>");

		if (!CollectionUtils.isEmpty(browserCounterDayDistribute)) {

			for (Calendar calendar : calendarList) {
				int vday = calendar.get(Calendar.DAY_OF_MONTH);
				int vcount = 0;

				for (HashMap map : browserCounterDayDistribute) {
					if (vday == Integer.parseInt(map.get("VDAY").toString())) {
						vcount = Integer.parseInt(map.get("VCOUNT").toString());

						break;
					}
				}

				xml.append("<set label='").append(vday).append("' value='").append(vcount).append("' />");
			}

			xml.append("<styles><definition><style name='LineShadow' type='shadow' color='333333' distance='6'/></definition><application><apply toObject='DATAPLOT' styles='LineShadow' /></application></styles></chart> ");
		}
		return xml.toString();
	}

	/**
	 * 展示浏览器类型日分布
	 * @param type 类型：无:今日， yesterday:昨日
	 * @return String 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public @ResponseBody
	String showBrowserTypeDistributeToday(int siteId, String type, String startTime) throws Exception {

		String caption = "浏览计数类型分布";
		String subCaption = "今日分布";

		Calendar calendar = Calendar.getInstance();
		if ("yesterday".equals(type)) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			subCaption = "昨日分布";
		}

		if (StringUtil.isEmpty(startTime)) {
			startTime = DateUtils.format(calendar.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}

		List<HashMap> browserTypeDayDistribute = counterManager.getBrowserTypeDayDistribute(siteId, startTime);

		StringBuilder xml = new StringBuilder("<chart caption='")
				.append(caption)
				.append("' subCaption='")
				.append(subCaption)
				.append("' showLabels='0' showValues='0' showLegend='1'  legendPosition='RIGHT' chartrightmargin='40' bgcolor='ECF5FF' bgalpha='70' bordercolor='C6D2DF' basefontcolor='2F2F2F' basefontsize='11' showpercentvalues='1' bgratio='0' startingangle='200' animation='1'>");

		if (!CollectionUtils.isEmpty(browserTypeDayDistribute)) {
			for (HashMap map : browserTypeDayDistribute) {
				int vcount = Integer.parseInt(map.get("VCOUNT").toString());
				String browserType = map.get("VBROWSERTYPE").toString();

				xml.append(" <set value='").append(vcount).append("' label='").append(browserType).append("'/>");
			}
		}

		xml.append(" </chart>");

		return xml.toString();
	}

	/**
	 * 展示浏览器类型日分布 
	 * @param type 类型：week:本周，7days:最近7天，month:本月，30days:最近30天
	 * @return String 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public @ResponseBody
	String showBrowserTypeDayDistribute(int siteId, String type, String startTime, String endTime) throws Exception {

		String caption = "浏览计数类型分布";
		String subCaption = "";

		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		if ("week".equals(type)) {
			int offset = startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
			endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.DAY_OF_MONTH, 6);

			subCaption = "本周分布";
		} else if ("7days".equals(type)) {
			startDate.add(Calendar.DAY_OF_MONTH, -6);

			subCaption = "最近7天分布";
		} else if ("month".equals(type)) {
			int offset = startDate.get(Calendar.DAY_OF_MONTH);
			startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
			endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.MONTH, 1);
			endDate.add(Calendar.DAY_OF_MONTH, -1);

			subCaption = "本月分布";
		} else if ("30days".equals(type)) {
			startDate.add(Calendar.DAY_OF_MONTH, -30);

			subCaption = "最近30天分布";
		}

		if (StringUtil.isEmpty(startTime)) {
			startTime = DateUtils.format(startDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}
		if (StringUtil.isEmpty(endTime)) {
			endTime = DateUtils.format(endDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}

		List<HashMap> browserTypeDayDistribute = counterManager.getBrowserTypeDayDistribute(siteId, startTime, endTime);

		StringBuilder xml = new StringBuilder("<chart caption='")
				.append(caption)
				.append("' subCaption='")
				.append(subCaption)
				.append("' showLabels='0' showValues='0' showLegend='1'  legendPosition='RIGHT' chartrightmargin='40' bgcolor='ECF5FF' bgalpha='70' bordercolor='C6D2DF' basefontcolor='2F2F2F' basefontsize='11' showpercentvalues='1' bgratio='0' startingangle='200' animation='1'>");

		if (!CollectionUtils.isEmpty(browserTypeDayDistribute)) {
			for (HashMap map : browserTypeDayDistribute) {
				int vcount = Integer.parseInt(map.get("VCOUNT").toString());
				String browserType = map.get("VBROWSERTYPE").toString();

				xml.append(" <set value='").append(vcount).append("' label='").append(browserType).append("'/>");
			}
		}

		xml.append(" </chart>");

		return xml.toString();
	}

	/**
	 * 展示浏览IP地址日分布
	 * @param type 类型：无:今日， yesterday:昨日
	 * @return String 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public @ResponseBody
	String showBrowserIPDistributeToday(int siteId, String type, String startTime) throws Exception {

		String caption = "浏览IP地址分布";
		String subCaption = "今日分布";

		Calendar calendar = Calendar.getInstance();
		if ("yesterday".equals(type)) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			subCaption = "昨日分布";
		}

		if (StringUtil.isEmpty(startTime)) {
			startTime = DateUtils.format(calendar.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}

		List<HashMap> browserIPDayDistribute = counterManager.getBrowserIPDayDistribute(siteId, startTime);

		StringBuilder xml = new StringBuilder("<chart caption='")
				.append(caption)
				.append("' subCaption='")
				.append(subCaption)
				.append("' showLabels='0' showValues='0' showLegend='1'  legendPosition='RIGHT' chartrightmargin='40' bgcolor='ECF5FF' bgalpha='70' bordercolor='C6D2DF' basefontcolor='2F2F2F' basefontsize='11' showpercentvalues='1' bgratio='0' startingangle='200' animation='1'>");

		if (!CollectionUtils.isEmpty(browserIPDayDistribute)) {
			for (HashMap map : browserIPDayDistribute) {
				int vcount = Integer.parseInt(map.get("VCOUNT").toString());
				String browserType = map.get("VBROWSERIP").toString();

				xml.append(" <set value='").append(vcount).append("' label='").append(browserType).append(".**")
						.append("'/>");
			}
		}

		xml.append(" </chart>");

		return xml.toString();
	}

	/**
	 * 展示浏览IP地址类型日分布 
	 * @param type 类型：week:本周，7days:最近7天，month:本月，30days:最近30天
	 * @return String 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public @ResponseBody
	String showBrowserIPDayDistribute(int siteId, String type, String startTime, String endTime) throws Exception {

		String caption = "浏览IP地址分布";
		String subCaption = "";

		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();

		if ("week".equals(type)) {
			int offset = startDate.get(Calendar.DAY_OF_WEEK);
			startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
			endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.DAY_OF_MONTH, 6);

			subCaption = "本周分布";
		} else if ("7days".equals(type)) {
			startDate.add(Calendar.DAY_OF_MONTH, -6);

			subCaption = "最近7天分布";
		} else if ("month".equals(type)) {
			int offset = startDate.get(Calendar.DAY_OF_MONTH);
			startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
			endDate = (Calendar) startDate.clone();
			endDate.add(Calendar.MONTH, 1);
			endDate.add(Calendar.DAY_OF_MONTH, -1);

			subCaption = "本月分布";
		} else if ("30days".equals(type)) {
			startDate.add(Calendar.DAY_OF_MONTH, -30);

			subCaption = "最近30天分布";
		}

		if (StringUtil.isEmpty(startTime)) {
			startTime = DateUtils.format(startDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}
		if (StringUtil.isEmpty(endTime)) {
			endTime = DateUtils.format(endDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}

		List<HashMap> browserIPDayDistribute = counterManager.getBrowserIPDayDistribute(siteId, startTime, endTime);

		StringBuilder xml = new StringBuilder("<chart caption='")
				.append(caption)
				.append("' subCaption='")
				.append(subCaption)
				.append("' showLabels='0' showValues='0' showLegend='1'  legendPosition='RIGHT' chartrightmargin='40' bgcolor='ECF5FF' bgalpha='70' bordercolor='C6D2DF' basefontcolor='2F2F2F' basefontsize='11' showpercentvalues='1' bgratio='0' startingangle='200' animation='1'>");

		if (!CollectionUtils.isEmpty(browserIPDayDistribute)) {
			for (HashMap map : browserIPDayDistribute) {
				int vcount = Integer.parseInt(map.get("VCOUNT").toString());
				String browserType = map.get("VBROWSERIP").toString();

				xml.append(" <set value='").append(vcount).append("' label='").append(browserType).append(".**")
						.append("'/>");
			}
		}

		xml.append(" </chart>");

		return xml.toString();
	}

	/**
	 * 展示页面浏览统计汇总数据
	 * 
	 * @param sortKey 排序关键字
	 * @param desc 排序方式
	 * @param offset 偏移量
	 * @param pagesize 页面数据大小
	 * @param counter 查询条件
	 * @param model 作用域模型
	 * @return String
	 */
	public String showPageBrowserCounterGatherList(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "browserTime") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, BrowserCounter counter,
			String startTime, String endTime, ModelMap model) throws Exception {

		ListInfo pageBrowserCounterGatherDataList = counterManager.getPageBrowserCounterGatherList(counter, startTime,
				endTime, (int) offset, pagesize);

		model.addAttribute("pageBrowserCounterGatherDataList", pageBrowserCounterGatherDataList);

		return "path:showPageBrowserCounterGatherList";
	}

	/**
	 * @param siteId
	 * @param type
	 * @param model
	 * @return
	 */
	public String showBrowserCounterDistribute(int siteId, String type, String startTime, String endTime, ModelMap model)
			throws Exception {

		if (!"custom".equals(type)) {

			Calendar startDate = Calendar.getInstance();
			Calendar endDate = Calendar.getInstance();

			if ("yesterday".equals(type)) {
				startDate.add(Calendar.DAY_OF_MONTH, -1);
				endDate = (Calendar)startDate.clone();
			} else if ("week".equals(type)) {
				int offset = startDate.get(Calendar.DAY_OF_WEEK);
				startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
				endDate = (Calendar) startDate.clone();
				endDate.add(Calendar.DAY_OF_MONTH, 6);
			} else if ("7days".equals(type)) {
				startDate.add(Calendar.DAY_OF_MONTH, -6);
			} else if ("month".equals(type)) {
				int offset = startDate.get(Calendar.DAY_OF_MONTH);
				startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
				endDate = (Calendar) startDate.clone();
				endDate.add(Calendar.MONTH, 1);
				endDate.add(Calendar.DAY_OF_MONTH, -1);
			} else if ("30days".equals(type)) {
				startDate.add(Calendar.DAY_OF_MONTH, -30);
			}

			startTime = DateUtils.format(startDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
			endTime = DateUtils.format(endDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		}

		// 获取概况信息
		List<BrowserVisitInfo> browserVisitInfoList = counterManager.getBrowserVisitInfo(siteId, startTime, endTime);

		if (!CollectionUtils.isEmpty(browserVisitInfoList)) {
			if ("week".equals(type) || "7days".equals(type) || "month".equals(type) || "30days".equals(type)) {
				browserVisitInfoList.remove(1);
				browserVisitInfoList.remove(1);
			}
		}

		model.addAttribute("browserVisitInfoList", browserVisitInfoList);

		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		model.addAttribute("siteId", siteId);
		model.addAttribute("type", type);

		String browserCounterForwordPage = "showBrowserCounterDayDistribute.freepage";
		String browserTypeForwordPage = "showBrowserTypeDayDistribute.freepage";
		String browserIPForwordPage = "showBrowserIPDayDistribute.freepage";

		if ("today".equals(type) || "yesterday".equals(type) || "custom".equals(type)) {
			browserCounterForwordPage = "showBrowserCounterHourDistribute.freepage";
			browserTypeForwordPage = "showBrowserTypeDistributeToday.freepage";
			browserIPForwordPage = "showBrowserIPDistributeToday.freepage";
		}

		model.addAttribute("browserCounterForwordPage", browserCounterForwordPage);
		model.addAttribute("browserTypeForwordPage", browserTypeForwordPage);
		model.addAttribute("browserIPForwordPage", browserIPForwordPage);

		return "path:showBrowserCounterDistribute";
	}

	/**
	 * @param siteId
	 * @return
	 * @throws Exception
	 */
	public @ResponseBody(datatype = "json")
	long getBrowserCount(int siteId) throws Exception {
		return counterManager.getBrowserCount(siteId);
	}

	/**
	 * @param siteId
	 * @return
	 */
	public @ResponseBody(datatype = "json")
	String statisticImmediately(int siteId) {

		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);

			counterManager.deleteStatisticBrowserCounter(siteId);
			counterManager.deleteStatisticBrowserType(siteId);
			counterManager.deleteStatisticBrowserIP(siteId);

			counterManager.statisticBrowserCounter(null, null, siteId);
			counterManager.statisticBrowserType(null, null, siteId);
			counterManager.statisticBrowserIP(null, null, siteId);

			tm.commit();

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		} finally {
			tm.release();
		}
	}
}
