package com.frameworkset.platform.sanylog.action;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.fiyu.edit.TimeStamp;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.util.DateUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.bean.Module;
import com.frameworkset.platform.sanylog.bean.PageCounter;
import com.frameworkset.platform.sanylog.bean.PageList;
import com.frameworkset.platform.sanylog.bean.SpentTime;
import com.frameworkset.platform.sanylog.service.FunctionListManager;
import com.frameworkset.platform.sanylog.service.PageListManager;
import com.frameworkset.platform.sanylog.util.POIExcelUtil2007;
import com.frameworkset.platform.sanylog.util.SanyLogUtil;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class PageListController {
	private PageListManager pageListManager;
	private String pageListDir;
	private SanyLogUtil util;
	//页面清单管理
		public String pageListIndex(){
			return "path:pageListIndex";
		}
		public @ResponseBody String pageListbatchInput( MultipartFile uploadFileName,ModelMap model,String appId){
			String fileNameOrig = uploadFileName.getOriginalFilename();
		
		try {
			TimeStamp date = new TimeStamp();
		    String fileName = "pageListbatchInput"+date.Time_Stamp()+".xlsx";
			File _file = new File(this.pageListDir + "/" + fileName);
			
			uploadFileName.transferTo(_file);
			Map map = util.batchImportPageList(this.pageListDir + "/", fileName,fileNameOrig, appId);

			if (map != null) {
				model.addAttribute("msg", map.get("msg").toString()); 
			}else if(null == map){
				return "test";
			}
			return map.get("msg").toString();
		} catch (Exception ex) {
			model.addAttribute("errMsg", StringUtil.exceptionToString(ex));
			return StringUtil.exceptionToString(ex);
		}}
		public String getExcelColDesc(String []desc) {
			List<String> colList = new ArrayList<String>();
			for(int i =0;i<desc.length;i++){
				colList.add(desc[i]);
			}
			return StringUtils.join(colList, ", ");
		}
		public void downloadExcel(String appId,HttpServletResponse response){
			  String[] pageListDesc = {"页面标示:functionName","功能路径:moduleCode","功能编码:functionCode"};
			try{
				List<String> appNameList = pageListManager.getAppNameById(appId);
				String appName = appNameList.size()>0?appNameList.get(0):"";
				List<PageList> datas =  pageListManager.getExcelDataByAppId(appId);
				String colDesc = getExcelColDesc(pageListDesc);
				XSSFWorkbook wb = POIExcelUtil2007.createHSSFWorkbook(colDesc, datas);

				response.setContentType("application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=" + new URLCodec().encode(appName+"页面清单.xlsx"));
				wb.write(response.getOutputStream());
			}catch(Exception e){
				e.printStackTrace();
				
			}

		}
public String showPageList(
		@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
		@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
		@PagerParam(name = PagerParam.OFFSET) long offset,
		@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "20") int pagesize, String appId,String functionCode,String functionName, ModelMap model) throws Exception {
			
	ListInfo datas = pageListManager.showFunctionList(appId, functionCode,functionName,(int) offset, pagesize);
	model.addAttribute("datas", datas);
	return "path:pageList";
}

		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")
		List<App> getAllApp() throws Exception {
			List<App> datas = pageListManager.getAllApp();
			return datas;
		}
		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")String staticSpentTime(){
			try{
				/**
				 * 1.搜集日志系统库中td_log_page_spenttime表中的所有数据,并删除
				 * 2.统计所有的to_log_browser_counts应用，功能名称，功能编码
				 * 3.两者进行比较后，插入td_log_page_spenttime
				 * 4.在jira中统计工时，结果插入日志系统库中
				*/
				pageListManager.staticAllPageList();
				return "统计成功";
			}catch(SQLException e){
				return "统计失败"+e.getMessage();
			}
			
		}	
		public String modifyOrIncrementRecord(String id,String type,ModelMap model){
			if("modify".equals(type)){
				try{
					List<PageList> datas = pageListManager.getSinglePageList(id);
					model.addAttribute("type", type);
					model.addAttribute("datas", datas);
					return "path:singlePageList";
				}catch(SQLException e ){
					model.addAttribute("errorMsg", e.getMessage());
					return "path:singlePageList";
				}
				
			}else{
				List<FunctionList> datas = new ArrayList<FunctionList> ();
				FunctionList data = new FunctionList();
				data.setAppId("  ");
				datas.add(data);
				model.addAttribute("type", type);
				model.addAttribute("datas", datas);
				return "path:singleFunctionList";
			}
		}
		
		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")
		String pageListSingleModify(String id,String functionCode)  {
			try{
				pageListManager.updatePageList( id, functionCode);
				return "修改成功";
			}catch(SQLException e){
				return "修改失败"+e.getMessage();
			}
			
		}
		public String showPageCounterList( String startTime,String endTime, ModelMap model) throws Exception {
					
			List<PageCounter> datas = pageListManager.showPageCounterList( startTime, endTime);
			model.addAttribute("datas", datas);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			
			return "path:showPageCounterList";
		}
		public String maintainPage(ModelMap model) throws SQLException{
			
			List<PageCounter> datas = pageListManager.getMaintainPage();
			model.addAttribute("datas", datas);
			return "path:showMaintainPage";
		}
		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")
		String savePages(String appName,String pages){
			try {
				pageListManager.savePages(appName, pages);
				
			} catch (SQLException e) {
				return "保存失败"+e.getMessage();
			}
			return "保存成功";
		}
}
