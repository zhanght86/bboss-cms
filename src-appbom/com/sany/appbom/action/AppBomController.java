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
package com.sany.appbom.action;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.sany.appbom.entity.AppBom;
import com.sany.appbom.entity.AppBomCondition;
import com.sany.appbom.service.AppBomServiceImpl;

public class AppBomController {
	
	private AppBomServiceImpl service;

	public AppBomServiceImpl getService() {
		return service;
	}

	public void setService(AppBomServiceImpl service) {
		this.service = service;
	}
	public String index(){
		return "path:index";
	}
	
	public String indexFixed(){
		return "path:indexFixed";
	}

	public @ResponseBody String delete(AppBom bean,ModelMap model) {
		service.delete(bean);
		return "success";
	}	
	public @ResponseBody String deletebatch(String ids,ModelMap model) {
		String idKey[]=ids.split(",");
		List<AppBom>beans =new ArrayList<AppBom>();
		AppBom bom=null;
		for(String id:idKey){
			bom=new AppBom();
			bom.setId(id);
			beans.add(bom);
		}
		service.deletebatch(beans);
		return "success";
	}

	public String appPre(ModelMap model) {
		
		return "path:addPre";
	}
	
	public String updatePre(String id, ModelMap model) {
		try {
			if(id==null||"".equals(id)){
				model.addAttribute("errmsg", "没有选择需修改台账记录！");
				return "path:updatePre";
			}
			Map idmap = new HashMap();
			idmap.put("id", id);
			AppBom bean=service.uniqueResult(idmap);
			model.addAttribute("appbom", bean);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errmsg", e.getMessage());
			return "path:updatePre";
		}

		return "path:updatePre";
	}
	public String viewBom(String id, ModelMap model) {
		try {
			if(id==null||"".equals(id)){
				model.addAttribute("errmsg", "没有选择需查看的台账记录！");
				return "path:viewBom";
			}
			Map idmap = new HashMap();
			idmap.put("id", id);
			AppBom bean=service.uniqueResult(idmap);
			model.addAttribute("appbom", bean);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errmsg", e.getMessage());
			return "path:viewBom";
		}

		return "path:viewBom";
	}
	public @ResponseBody String update(AppBom bean, ModelMap model) {
		boolean result=service.update(bean);
		if(! result){
			return "error";
		}
		return "success";

	}
	
	public @ResponseBody String addBom(AppBom bean, ModelMap model) {
		boolean result=service.addBom(bean);
		if(! result){
			return "error";
		}
		return "success";
	}
	
	public String queryListAppBom(@PagerParam(name = PagerParam.SORT, defaultvalue = "bm") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			AppBomCondition appcondition, ModelMap model){
		ListInfo datas=null;
		String app_name=null,app_name_en=null,bm=null;
	
		if(appcondition!=null){
			if(appcondition.getApp_name()!=null&&!"".equals(appcondition.getApp_name())){
				app_name=appcondition.getApp_name();
				appcondition.setApp_name("%"+appcondition.getApp_name()+"%");
			}
			if(appcondition.getApp_name_en()!=null&&!"".equals(appcondition.getApp_name_en())){
				app_name_en=appcondition.getApp_name_en();
				appcondition.setApp_name_en("%"+appcondition.getApp_name_en()+"%");
			}
			appcondition.setSortKey(sortKey);
			appcondition.setSortDESC(desc);
		}
		datas=service.queryListInfoBean(offset, pagesize, appcondition);					
		model.addAttribute("datas", datas);	
		return "path:getAllListAppBom";
	}
	
	public String queryListAppBomFixed(@PagerParam(name = PagerParam.SORT, defaultvalue = "bm") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) long offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			AppBomCondition appcondition, ModelMap model){
		ListInfo datas=null;
		String app_name=null,app_name_en=null,bm=null;
	
		if(appcondition!=null){
			if(appcondition.getApp_name()!=null&&!"".equals(appcondition.getApp_name())){
				app_name=appcondition.getApp_name();
				appcondition.setApp_name("%"+appcondition.getApp_name()+"%");
			}
			if(appcondition.getApp_name_en()!=null&&!"".equals(appcondition.getApp_name_en())){
				app_name_en=appcondition.getApp_name_en();
				appcondition.setApp_name_en("%"+appcondition.getApp_name_en()+"%");
			}
			appcondition.setSortKey(sortKey);
			appcondition.setSortDESC(desc);
		}
		datas=service.queryListInfoBean(offset, pagesize, appcondition);					
		model.addAttribute("datas", datas);	
		return "path:getAllListAppBomFixed";
	}
	/**
	 * 判断Bm是否重复
	 * @param 
	 * @param 
	 * @return
	 */
	public @ResponseBody String CheckBmExist(String bm,String id,ModelMap model) {
		
		try {
			Map map=new HashMap();
			map.put("id",id);
			map.put("bm", bm);
			int cnt=service.checkBmExist(map);
			if(cnt==0){
				return "0";
			}else{
				return "1";
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "1";
	}
	/**
	 * 判断导出是否有记录
	 * @param 
	 * @param 
	 * @return
	 */
	public @ResponseBody String queryCntByCondition(AppBomCondition appcondition,ModelMap model) {
		
		try {
			if(appcondition!=null){
				if(appcondition.getApp_name()!=null&&!"".equals(appcondition.getApp_name())){
					appcondition.setApp_name("%"+appcondition.getApp_name()+"%");
				}
				if(appcondition.getApp_name_en()!=null&&!"".equals(appcondition.getApp_name_en())){
					appcondition.setApp_name_en("%"+appcondition.getApp_name_en()+"%");
				}
			}
			int cnt=service.queryCntByCondition(appcondition);
			if(cnt==0){
				return "0";
			}else{
				return "1";
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "1";
	}
	/*
	 * 导出Excel
	 */
	public void  exportExcel(AppBomCondition appcondition,ModelMap model,
			HttpServletRequest request,HttpServletResponse response) {
		
		List<AppBom> beans = null;
		String app_name=null,app_name_en=null,bm=null;
		
		if(appcondition!=null){
			if(appcondition.getApp_name()!=null&&!"".equals(appcondition.getApp_name())){
				app_name=appcondition.getApp_name();
				appcondition.setApp_name("%"+appcondition.getApp_name()+"%");
			}
			if(appcondition.getApp_name_en()!=null&&!"".equals(appcondition.getApp_name_en())){
				app_name_en=appcondition.getApp_name_en();
				appcondition.setApp_name_en("%"+appcondition.getApp_name_en()+"%");
			}
		}
		beans=service.queryListBean(appcondition);
		InputStream in=null;
		try{
			Integer excelType=appcondition.getExcelType();
			Workbook  workbook=null;
			if(excelType==1){
				in =AppBomController.class.getResourceAsStream("appbomExcelModel.xlsx");
				 workbook = new XSSFWorkbook(in);
				 service.setExcelData(workbook,false, beans);
				 sendFile( request, response,  "appbomExcel.xlsx",workbook,0);
			}
			else{
				in =AppBomController.class.getResourceAsStream("appbomExcelModel.xls");
				 workbook = new HSSFWorkbook(in);
				 service.setExcelData(workbook,true, beans);
				 sendFile( request, response,  "appbomExcel.xls",workbook,0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			 if (in != null) {
	                try {
	                    in.close();
	                }
	                catch (IOException e) {
	                    throw new RuntimeException(e.getMessage(), e);
	                }
	            }
		}
	}
	
	public static void sendFile(HttpServletRequest request, 
			HttpServletResponse response, String filename,
			Workbook workbook,long fileSize) throws Exception {
        OutputStream out = null;
        try {
        	if(workbook == null)
        		return;
        	out = response.getOutputStream();
        	

            response.setHeader("Content-Disposition", "attachment; filename=" + new String(filename.getBytes(),"ISO-8859-1").replaceAll(" ", "-"));
            response.setHeader("Accept-Ranges", "bytes");
            workbook.write(out);
            out.flush();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	throw e;
        }
        finally {
            try
			{
            	if(out != null)
            		out.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
        }
    }
	

	
}
