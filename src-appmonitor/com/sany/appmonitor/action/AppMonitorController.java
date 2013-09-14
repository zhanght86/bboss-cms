/*
 * @(#)AppBomControler.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
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
package com.sany.appmonitor.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.sany.appmonitor.entity.AppInfo;
import com.sany.appmonitor.entity.PortalPendMonitorCondition;
import com.sany.appmonitor.service.AppMonitorServiceImpl;

public class AppMonitorController {
	
	private AppMonitorServiceImpl service;

	public AppMonitorServiceImpl getService() {
		return service;
	}

	public void setService(AppMonitorServiceImpl service) {
		this.service = service;
	}
	public String index(){
		
		return "path:index";
	}
	public String queryList(@PagerParam(name = PagerParam.SORT, defaultvalue = "monitorTime") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "true") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			PortalPendMonitorCondition condition, ModelMap model){
		ListInfo datas=null;
		Map<Integer,String> appInfo= service.getAppInfoMap();
		datas=service.queryList(offset, pagesize,condition);
		Date monitorTimeBegin=null,monitorTimeEnd=null;
		monitorTimeBegin=new Date();
		monitorTimeEnd=new Date();
		model.addAttribute("appInfo",appInfo);
		model.addAttribute("monitorTimeBegin",monitorTimeBegin);
		model.addAttribute("monitorTimeEnd",monitorTimeEnd);
		model.addAttribute("datas", datas);
		System.out.println(condition.getMonitorTimeBegin());
		System.out.println(condition.getMonitorTimeEnd());
		return "path:queryList";
	}

	public String queryReportList(PortalPendMonitorCondition condition, ModelMap model){
		List<HashMap> datas=service.queryReportList(condition);
		model.addAttribute("datas",datas);
		
		return "path:queryReport";
	}
	public @ResponseBody(datatype="json") List getAllAppInfo(){
		List<AppInfo> datas=service.getAllAppInfo();
		return datas;
	}

}
