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
package com.frameworkset.platform.sanylog.action;

import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.util.DateUtils;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.BrowserCounter;
import com.frameworkset.platform.sanylog.bean.BrowserVisitInfo;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.OperRank;
import com.frameworkset.platform.sanylog.bean.OperateCounter;
import com.frameworkset.platform.sanylog.bean.VideoHitsCounter;
import com.frameworkset.platform.sanylog.dictionary.Dictionary;
import com.frameworkset.platform.sanylog.service.ConfigManager;
import com.frameworkset.platform.sanylog.service.CounterManager;
import com.frameworkset.platform.sanylog.util.POIExcelUtil2007;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * @author gw_hel
 * 计数服务控制器
 */
public class CounterController {

	private final String[] browserTypeSet = { "MSIE", "Firefox", "Safari", "Chrome" };
    private ConfigManager  configManager;
	private CounterManager counterManager;
	private Logger log = Logger.getLogger(CounterController.class);
	/**手动统计操作量
	*/
	
	
	public @ResponseBody(datatype = "json")
	String statisticOperCounterImmediately() {
		Calendar today = Calendar.getInstance();
		String todayTime = DateUtils.format(today.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		String week = String.valueOf(today.get(Calendar.WEEK_OF_YEAR));
		week = todayTime.substring(0, 4)+"-"+week;
		Calendar startDate = Calendar.getInstance();
		int offset = startDate.get(Calendar.DAY_OF_WEEK);
		startDate.add(Calendar.DAY_OF_MONTH, offset - (offset * 2 - 1));
		String startTime =  DateUtils.format(startDate.getTime(), DateUtils.ISO8601_DATE_PATTERN);
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			//按周统计
			counterManager.deleteOperCounterByWeek(week);
			counterManager.staticOperCounterByWeek(startTime,todayTime,week);
			
			
			//按天统计
			counterManager.deleteOperCounterByDay(todayTime);
			counterManager.staticOperCounterByDay(todayTime);
			
			//按月统计
			counterManager.deleteOperCounterByMonth(todayTime.substring(0, 7));
			counterManager.staticOperCounterByMonth(todayTime.substring(0, 7));
			
			//按年统计
			
			counterManager.deleteOperCounterByYear(todayTime.substring(0, 4));
			counterManager.staticOperCounterByYear(todayTime.substring(0, 4));
			

			tm.commit();

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getLocalizedMessage();
		} finally {
			tm.release();
		}
	}
	/**
	 * 导出Excel
	*/
	public void downloadExcel(String time,String type,String appId,HttpServletResponse response){

		try{
			List<OperRank> datas =  counterManager.getExcelDatas(time,type,appId);
			if(null!=datas&&datas.size()>0){
				for(int i=0;i<datas.size();i++){
					int j=i+1;
					datas.get(i).setAppId("("+j+")");
				}
			}
			String colDesc = getExcelColDesc(Dictionary.titles);
			XSSFWorkbook wb = POIExcelUtil2007.createHSSFWorkbook(colDesc, datas);

			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + new URLCodec().encode(time+"__"+datas.get(0).getAppName()+"__操作统计报表.xlsx"));
			wb.write(response.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
			
		}

	}
	public String getExcelColDesc(String []desc) {
		List<String> colList = new ArrayList<String>();
		for(int i =0;i<desc.length;i++){
			colList.add(desc[i]);
		}
		return StringUtils.join(colList, ", ");
	}
	/**获得操作统计的周排名
	*/
	public String showOperCounterRankByWeek(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "vcount") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, String appId,String vtime, ModelMap model) throws Exception {
				if(null == vtime||"".equals(vtime)){
					Calendar calendar = Calendar.getInstance();
					Calendar today = Calendar.getInstance();
					String todayTime = DateUtils.format(today.getTime(), DateUtils.ISO8601_DATE_PATTERN);
					String year = todayTime.substring(0, 4);
					int weeks = calendar.get(Calendar.WEEK_OF_YEAR);
					vtime = year+"-"+String.valueOf(weeks);
				}
				/*else{
					String year = vtime.substring(0, 4);
					String week = vtime.substring(5,7);
					vtime = year+"-"+week;
					
				}*/
		ListInfo datas = counterManager.getOperCounterRankByWeek(appId, vtime,(int) offset, pagesize);
		model.addAttribute("datas", datas);
		return "path:operCounterRankData";
	}
	/**获得操作统计的日排名
	*/
	public String showOperCounterRankByDay(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "vcount") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, String appId,String vtime, ModelMap model) throws Exception {
				if(null == vtime||"".equals(vtime)){
					Calendar calendar = Calendar.getInstance();
					vtime = DateUtils.format(calendar.getTime(), DateUtils.ISO8601_DATE_PATTERN);
				}
		ListInfo datas = counterManager.getOperCounterRankByDay(appId, vtime,(int) offset, pagesize);
		model.addAttribute("datas", datas);
		return "path:operCounterRankData";
	}
	/**获得操作统计的月排名
	*/
	public String showOperCounterRankByMonth(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "vcount") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, String appId,String month, String year,ModelMap model) throws Exception {

		if(null == month||"".equals(month)){
			Calendar calendar = Calendar.getInstance();
			month = DateUtils.format(calendar.getTime(), DateUtils.ISO8601_DATE_PATTERN).substring(5, 7);
		}
		if(null == year||"".equals(year)){
			Calendar calendar = Calendar.getInstance();
			year = DateUtils.format(calendar.getTime(), DateUtils.ISO8601_DATE_PATTERN).substring(0, 4);
		}
		String vtime = year.concat("-").concat(month);
		ListInfo datas = counterManager.getOperCounterRankByMonth(appId, vtime,(int) offset, pagesize);
		model.addAttribute("datas", datas);
		return "path:operCounterRankData";
	}
	/**获得操作统计的年度排名
	*/
	public String showOperCounterRankByYear(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "vcount") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, String appId,String vtime, ModelMap model) throws Exception {

		if(null == vtime||"".equals(vtime)){
			Calendar calendar = Calendar.getInstance();
			vtime = DateUtils.format(calendar.getTime(), DateUtils.ISO8601_DATE_PATTERN).substring(0, 4);
		}
		ListInfo datas = counterManager.getOperCounterRankByYear(appId, vtime,(int) offset, pagesize);
		model.addAttribute("datas", datas);
		return "path:operCounterRankData";
	}
	/**
	 * 查看详细的浏览记录
	 */
	
	
	public String checkBrowserDetail(String browserId,ModelMap model) throws SQLException{
		log.info("operateId-----"+browserId);
		
		List<BrowserCounter> browserCounterDetail = counterManager.getBrowserCounterDetail(browserId);
        model.addAttribute("browserCounterDetail",browserCounterDetail);
		return "path:browserCounterDetail";
	}
	/**
	 * 查看详细的操作记录
	 */
	
	
	public String checkOperateDetail(String operateId,ModelMap model) throws SQLException{
		log.info("operateId-----"+operateId);
		
		List<OperateCounter> operateCounterDetail = counterManager.getOperateCounterDetail(operateId);
        model.addAttribute("operateCounterDetail",operateCounterDetail);
		return "path:operateCounterDetail";
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
	long operateCounter(OperateCounter paramCounter, boolean enable, HttpServletRequest request) throws Exception {

		paramCounter.setOperateId(UUID.randomUUID().toString());
       
		String appName = paramCounter.getAppName();
		String moduleName = paramCounter.getModuleName();
		String moduleCode = paramCounter.getModuleCode();
		String modulePath = paramCounter.getModulePath();
		String operator = paramCounter.getOperator();
		if(null!=operator&&!"".equals(operator)){
			paramCounter.setOperator(URLDecoder.decode(operator, "UTF-8"));
		}else{
			paramCounter.setOperator(AccessControl.getAccessControl().getUserAccount());
		}
		
		String pageName = paramCounter.getPageName();
		String operContent = paramCounter.getOperContent();
		
		paramCounter.setModulePath(StringUtil.isEmpty(modulePath) ? null : URLDecoder.decode(modulePath, "UTF-8"));
		paramCounter.setAppName(StringUtil.isEmpty(appName) ? null : URLDecoder.decode(appName, "UTF-8"));
		paramCounter.setModuleName(StringUtil.isEmpty(moduleName) ? null : URLDecoder.decode(moduleName, "UTF-8"));
		paramCounter.setPageName(StringUtil.isEmpty(pageName) ? null : URLDecoder.decode(pageName, "UTF-8"));
		paramCounter.setOperContent(StringUtil.isEmpty(operContent) ? null : URLDecoder.decode(operContent, "UTF-8"));
		Module module = configManager.checkAppModule(paramCounter.getAppName(), paramCounter.getModuleName(),paramCounter.getModuleCode(),paramCounter.getModulePath());//检查 appId  moduleId
		paramCounter.setAppId(Integer.parseInt(module.getAppId()));
		paramCounter.setModuleId(Integer.parseInt(module.getModuleId()));
		paramCounter.setOperateIp(request.getRemoteAddr());
		
		paramCounter.setPageURL(paramCounter.getPageURL());
//设置浏览器类型
		String userAgent = request.getHeader("User-Agent");
		for (String agent : userAgent.split(";")) {
			for (String browser : browserTypeSet) {
				if (agent.indexOf(browser) > 0) {
					paramCounter.setBrowserType(agent.substring(agent.indexOf(browser)).replaceAll("/", " "));
					break;
				}
			}
		}
		//设置访问来源的url
		paramCounter.setReferer(request.getHeader("Referer"));
//事务提交代码
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			counterManager.incrementOperateCounter(paramCounter);//新增一条记录在基础计数表中
			long ret = 0;
			/*if (enable) {
				ret = counterManager.getBrowserCount(paramCounter.getSiteId());
			}*/
			tm.commit();
			return ret;
		} finally {
			tm.release();
		}
	}
	/**
	 * 展示操作统计数据列表showOperateCounterList
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
	 * 
	 */
	public String showOperateCounterList(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "browserTime") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize, OperateCounter counter,
			Timestamp startTime, Timestamp endTime, ModelMap model) throws Exception {

		ListInfo operateCounterDataList = counterManager.getOperateCounterList(counter, startTime, endTime,(int) offset, pagesize);
        System.out.println("--------------showOperateCounterList---------------");
		model.addAttribute("operateCounterDataList", operateCounterDataList);
       
        
        
        if(operateCounterDataList.getSize()>0){
        	 log.info("--------------result is not null!---------------");
        }
		return "path:showoperateCounterList";
	}
	/**
	 * 首页面 
	 * @return String
	 */
	//获取应用下的模块名称
	@SuppressWarnings("unchecked")
	public @ResponseBody(datatype = "json")
	List<Module> getModuleBySiteId(String appId) throws Exception {
		return counterManager.getAllModulesOfApp(appId);
	}
	//获取用户可以访问的应用名称
	@SuppressWarnings("unchecked")
	public @ResponseBody(datatype = "json")
	List<App> getAllApp(HttpServletRequest request,HttpServletResponse response)throws SQLException{
		System.out.println("------------程序执行到此处--------------");
		com.frameworkset.platform.security.AccessControl control = com.frameworkset.platform.security.AccessControl.getInstance();
		control.checkAccess(request, response,false);
		String userId = control.getUserID();
		 if("1".equals(userId)){
			 return counterManager.getAdminApp(userId);
		 }else{
			 return counterManager.getApp(userId);
		 }
	}
	public String index() {
		return "path:index";
	}
	//访问统计的菜单跳转
	public String attachIndex() {
		System.out.println("-------------------path:attachindex--------------------");
		return "path:attachindex";
	}
	//操作统计的菜单跳转
	public String operateIndex() {
		System.out.println("--------------------path:operateindex-------------------");
		return "path:operateindex";
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
       log.info("------------------here-------------------");
		paramCounter.setBrowserId(UUID.randomUUID().toString());

		String siteName = paramCounter.getSiteName();
		String channelName = paramCounter.getChannelName();
		String docName = paramCounter.getDocName();
		String moduleCode = paramCounter.getModuleCode();
		String modulePath = paramCounter.getModulePath();
		paramCounter.setModuleCode(StringUtil.isEmpty(moduleCode) ? null : URLDecoder.decode(moduleCode, "UTF-8"));
		paramCounter.setModulePath(StringUtil.isEmpty(modulePath) ? null : URLDecoder.decode(modulePath, "UTF-8"));

		paramCounter.setSiteName(StringUtil.isEmpty(siteName) ? null : URLDecoder.decode(siteName, "UTF-8"));
		paramCounter.setChannelName(StringUtil.isEmpty(channelName) ? null : URLDecoder.decode(channelName, "UTF-8"));
		paramCounter.setDocName(StringUtil.isEmpty(docName) ? null : URLDecoder.decode(docName, "UTF-8"));
		Module module = configManager.checkAppModule(paramCounter.getSiteName(), paramCounter.getChannelName(),paramCounter.getModuleCode(),paramCounter.getModulePath());//检查 appId  moduleId
		paramCounter.setSiteId(Integer.parseInt(module.getAppId()));
		paramCounter.setChannelId(Integer.parseInt(module.getModuleId()));
		paramCounter.setBrowserIp(request.getRemoteAddr());
		paramCounter.setBrowserUser(AccessControl.getAccessControl().getUserAccount());
		paramCounter.setPageURL(paramCounter.getPageURL());
//设置浏览器类型
		String userAgent = request.getHeader("User-Agent");
		for (String agent : userAgent.split(";")) {
			for (String browser : browserTypeSet) {
				if (agent.indexOf(browser) > 0) {
					paramCounter.setBrowserType(agent.substring(agent.indexOf(browser)).replaceAll("/", " "));
					break;
				}
			}
		}
		//设置访问来源的url
		paramCounter.setReferer(request.getHeader("Referer"));
//事务提交代码
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin(TransactionManager.RW_TRANSACTION);
			counterManager.incrementBrowserCounter(paramCounter);//新增一条记录在基础计数表中
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
		System.out.println("xml----------------------"+xml.toString());
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

		List<HashMap> browserCounterDayDistribute = counterManager.getBrowserCounterDayDistribute(siteId, startTime,endTime);

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
		System.out.println("xml---------浏览计数统计按天-------------"+xml.toString());
		return xml.toString();
	}

	/**
	 * 展示浏览器类型今天，昨天分布
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
		System.out.println("xml----------------------"+xml.toString());
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
                  System.out.println();
				xml.append(" <set value='").append(vcount).append("' label='").append(browserType).append("'/>");
			}
		}

		xml.append(" </chart>");
         System.out.println("xml--------------------------------"+xml.toString());
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

		String browserCounterForwordPage = "showBrowserCounterDayDistribute.page";
		String browserTypeForwordPage = "showBrowserTypeDayDistribute.page";
		String browserIPForwordPage = "showBrowserIPDayDistribute.page";

		if ("today".equals(type) || "yesterday".equals(type) || "custom".equals(type)) {
			browserCounterForwordPage = "showBrowserCounterHourDistribute.page";
			browserTypeForwordPage = "showBrowserTypeDistributeToday.page";
			browserIPForwordPage = "showBrowserIPDistributeToday.page";
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
