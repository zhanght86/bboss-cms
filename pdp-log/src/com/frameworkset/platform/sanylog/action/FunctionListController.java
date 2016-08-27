package com.frameworkset.platform.sanylog.action;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.sanylog.bean.App;
import com.frameworkset.platform.sanylog.bean.FunctionList;
import com.frameworkset.platform.sanylog.service.FunctionListManager;
import com.frameworkset.platform.sanylog.util.SanyLogUtil;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class FunctionListController {
	private FunctionListManager functionListManager;
	private String templateDir;
	private String functionListDir;
	private SanyLogUtil util;
	//页面清单管理
		public String pageListIndex(){
			return "path:pageListIndex";
		}
	
	//功能清单管理
public String index(){
	return "path:functionQuery";
}
public String showFunctionList(
		@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
		@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
		@PagerParam(name = PagerParam.OFFSET) long offset,
		@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "20") int pagesize, String appId,String functionCode,String functionName, ModelMap model) throws Exception {
			
	ListInfo datas = functionListManager.showFunctionList(appId, functionCode,functionName,(int) offset, pagesize);
	model.addAttribute("datas", datas);
	return "path:functionList";
}

		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")
		List<App> getAllApp() throws Exception {
			List<App> datas = functionListManager.getAllApp();
			return datas;
		}
		
		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")String deleteRecord(String id)  {
			try{
				functionListManager.deleteRecord(id);
				return "删除成功";
			}catch(SQLException e){
				return "删除失败"+e.getMessage();

			}
		}
		public String modifyOrIncrementRecord(String id,String type,ModelMap model){
			if("modify".equals(type)){
				try{
					List<FunctionList> datas = functionListManager.getSingleFunctionList(id);
					model.addAttribute("type", type);
					model.addAttribute("datas", datas);
					return "path:singleFunctionList";
				}catch(SQLException e ){
					model.addAttribute("errorMsg", e.getMessage());
					return "path:singleFunctionList";
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
		public @ResponseBody(datatype = "json")String functionListSingleModify(String id,int estimateOper,int estimateUser)  {
			try{
				functionListManager.functionListSingleModify(id,estimateOper,estimateUser);
				return "修改成功";
			}catch(SQLException e){
				return "修改失败"+e.getMessage();

			}
		}
		
		
		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")String functionListSingleIncrement(String appName,String functionName,String functionCode,int estimateOper,int estimateUser)  {
			try{
				String appId = null;
				List<String> appIds = functionListManager.getAppId(appName);
				if(null!=appIds&&0!=appIds.size()){
					appId = appIds.get(0);
				}else{
					List<String> maxAppId = functionListManager.getMaxAppId();
					if(null == maxAppId || 0== maxAppId.size()){
						appId = "0";
					}else{
						appId = String.valueOf(Integer.parseInt(maxAppId.get(0))+1);
					}
					functionListManager.insertApp(appId,appId,appName);
				}
				String id = UUID.randomUUID().toString();
				functionListManager.functionListSingleIncrement(id,appId,appName,functionName,functionCode,estimateOper,estimateUser);
				return "新增成功";
			}catch(SQLException e){
				return "新增失败"+e.getMessage();

			}
		}
		@SuppressWarnings("unchecked")
		public @ResponseBody(datatype = "json")String staticSpentTime(){
			try{
				/**
				 * 1.在日志系统库中找到所有的项目名称
				 * 2.用相应的项目名称在jira里面统计工时
				 * 3.结果插入日志系统的表
				*/
				List<App> datas = functionListManager.getAllApp();
				for(App bean:datas){
					String appName = bean.getAppName();
					String appId = bean.getAppId();
					List<FunctionList> sysSpentTime = functionListManager.staticSpentTimeByAppName(appName,appId);
					if(null!=sysSpentTime&&sysSpentTime.size()>0){
						//functionListManager.deleteSysSpentTime(appId);
						//functionListManager.insertSysSpentTime(sysSpentTime);
						for(FunctionList sysTime:sysSpentTime){
							sysTime.setId(UUID.randomUUID().toString());
						}
						List<FunctionList> datasInDB = functionListManager.getFunctionList(appId);
						if(datasInDB.size()>0){
							List<FunctionList> datasForInsert = new ArrayList<FunctionList> ();//存放td_log_functionlist表里面没有的
							List<FunctionList> datasForUpdate = new ArrayList<FunctionList> ();//存放td_log_functionlist表里面有，但是时间更新了的
							for(FunctionList sysTime:sysSpentTime){
								String status = "notequal";
								for(FunctionList beanInDB:datasInDB){
									String result = sysTime.equal(beanInDB);
									if("notequal".equals(result)){
										
									}else if("timediff".equals(result)){
										 datasForUpdate.add(beanInDB);
										 status = "timediff";
										 break;
									}else if("equal".equals(result)){
										status = "equal";
										 break;
									}
								}
								if("notequal".equals(status)){
									datasForInsert.add(sysTime);
								}
							}
							functionListManager.insertSysTime(datasForInsert);
							functionListManager.updateSysTime(datasForUpdate);
						}else{
							functionListManager.insertSysTime(sysSpentTime);
						}
					}
					
				}
				return "统计成功";
			}catch(SQLException e){
				return "统计失败"+e.getMessage();
			}
			
		}
		public @ResponseBody
		File getTemplate(String fileName) {
			File file = new File(templateDir + "/" + fileName);
			return file;
		}
		
		
		public @ResponseBody String functionListbatchInput( MultipartFile uploadFileName,ModelMap model){
			String fileNameOrig = uploadFileName.getOriginalFilename();
		
		try {
			
		    String fileName = "functionListbatchInput"+System.currentTimeMillis()+".xlsx";
			File _file = new File(this.functionListDir + "/" + fileName);
			
			uploadFileName.transferTo(_file);
			Map map = util.batchImportFunctionList(this.functionListDir + "/", fileName,fileNameOrig);

			if (map != null) {
				model.addAttribute("msg", map.get("msg").toString()); 
			}
			return map.get("msg").toString();
		} catch (Exception ex) {
			model.addAttribute("errMsg", StringUtil.exceptionToString(ex));
			return StringUtil.exceptionToString(ex);
		}}
		
		
}
