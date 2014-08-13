/*
 * @(#)ActivitiRepository.java
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
package com.sany.application.action;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.fiyu.edit.TimeStamp;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;
import com.sany.application.entity.WfApp;
import com.sany.application.entity.WfPic;
import com.sany.application.service.AppcreateService;

/**
 * @author liud44
 * @since 2012-3-22 下午6:03:09
 */
public class AppcreateAction {
	
	private static Logger logger = Logger.getLogger(AppcreateAction.class);
	
	
	private AppcreateService appcreateService;

	public String index(WfApp wfApp,ModelMap model) {
				
		return "path:index";
	}
	
	/**
     * 分页查询列表
     * @param offset
     * @param pagesize
     * @return
     */
    public String queryListPage(@PagerParam(name = PagerParam.OFFSET) long offset,
            @PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
            WfApp wfApp, ModelMap model) throws Exception {
    	
        ListInfo listInfo = appcreateService.findListPage(offset, pagesize, wfApp);
        
        model.addAttribute("appcreateList", listInfo);
        
        return "path:queryListPage";
        
    }
    
    /**
     * 更新应用
     * @param appInfoId
     * @param model
     * @return
     */
    public String updateAppInfo(String appInfoId, ModelMap model) throws Exception {
    	
    	if(StringUtils.isNotEmpty(appInfoId)){
    		WfApp wfApp = appcreateService.queryWfAppById(appInfoId);
    		model.put("wfApp", wfApp);
    	}
    	
    	return "path:updateAppInfo";
    }
    
    /**
     * 查看应用
     * @param appInfoId
     * @param model
     * @return
     */
    public String viewAppInfo(String appInfoId, ModelMap model) throws Exception {
    	
    	if(StringUtils.isNotEmpty(appInfoId)){
    		WfApp wfApp = appcreateService.queryWfAppById(appInfoId);
    		model.put("wfApp", wfApp);
    	}
    	
    	return "path:viewAppInfo";
    }
	
    /**
     * 验证是否有重复的应用
     * @param wfApp
     * @return
     */
    public @ResponseBody String validateWfApp(WfApp wfApp) throws Exception {
    	
    	String validateResult = "fail";
    	
    	String wfAppId = "";
    	
    	if(wfApp != null){

    		if(StringUtils.isNotEmpty(wfApp.getId())){
    			
        		wfAppId = wfApp.getId();
        		
        		wfApp.setId("");
        	}
    		
    		if(StringUtils.isNotEmpty(wfApp.getOld_system_secret())
    				&& StringUtils.isNotEmpty(wfAppId)){
    			
    			WfApp secretWfApp = new WfApp();
    			secretWfApp.setId(wfAppId);
    			secretWfApp.setSystem_secret(EncrpyPwd.encodePassword(wfApp.getOld_system_secret()));
    			
    			List<WfApp> dataList = appcreateService.queryWfApp(secretWfApp);
    			
    			if(CollectionUtils.isEmpty(dataList)){
    				return "secretFail";
    			}
    		}
    		
        	WfApp validateWfApp = new WfApp();
        	
        	validateWfApp.setSystem_id(wfApp.getSystem_id());
        	
        	validateWfApp.setSystem_name(wfApp.getSystem_name());
        	
        	List<WfApp> dataList = appcreateService.queryWfApp(validateWfApp);
        	
        	if(CollectionUtils.isEmpty(dataList)){
        		
        		validateResult = "success";
        	}else{
        		if(StringUtils.isNotEmpty(wfAppId)){
        			
        			if(dataList.size()==1 && wfAppId.equals(dataList.get(0).getId())){
        				
        				validateResult = "success";
        			}
        		}
        	}
        	
    	}
    	
    	return validateResult;
    }
    
	/**
	 * 保存应用
	 * @param wfApp
	 * @return
	 */
	public @ResponseBody String saveWfApp(WfApp wfApp){
		
		if(wfApp != null){
			try {
				if(StringUtils.isNotEmpty(wfApp.getId())){
					wfApp.setUpdate_person(AccessControl.getAccessControl().getUserName());
				}else{
					wfApp.setCreator((AccessControl.getAccessControl().getUserName()));
				}
				appcreateService.saveWfApp(wfApp);
			} catch (Exception e) {
				logger.error(e);
				return e.getMessage();
			}
		}else{
			return "save data is null";
		}
		
		return "success";
	}
	
	public @ResponseBody String deleteAppInfo(String appInfoId) throws Exception {
		
		String actionResult = "fail";
		
		if(StringUtils.isNotEmpty(appInfoId)){
			appcreateService.deleteWfAppById(appInfoId);
			actionResult = "success";
		}
		
		return actionResult;
	}
	
	/*   图片上传    */
	public @ResponseBody String uploadAppPic( MultipartFile uploadFileName,ModelMap model,HttpServletRequest request)throws Exception{
		String fileNameOrig = uploadFileName.getOriginalFilename();
	    String id = UUID.randomUUID().toString();
	    WfPic pic = new WfPic(id,fileNameOrig,uploadFileName);
		try {
			String contextPath= request.getSession().getServletContext().getRealPath("");
			File _file = new File(contextPath+"\\application\\app_images\\" + fileNameOrig);
			
			uploadFileName.transferTo(_file);
			appcreateService.insertPic(pic);
			
			return "导入成功";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "导入失败";
		}
	}
	
	/*   跳转到图片选择页面    */
	public String picRefApp(String appInfoId, ModelMap model,HttpServletRequest request)throws Exception{
		List<WfPic> list = appcreateService.getAllWfPicNoContent();
		List<String> pic_selected = appcreateService.getAppSelectedPic(appInfoId);
		String pic_selected_name = "no";
		if(null != pic_selected&&pic_selected.size()>0){
			if(null != pic_selected.get(0)&&!"".equals(pic_selected.get(0))){
				pic_selected_name = pic_selected.get(0);
			}
		}
		String contextPath= request.getSession().getServletContext().getRealPath("");
		File file=new File(contextPath+File.separatorChar+"application"+File.separatorChar+"app_images" ); 
		String files[]=file.list();  
		//前台没有的图片，后台传过去
		if(null != list && list.size()>0){
			for(int i=0;i<list.size();i++){
				String id = list.get(i).getId();
				String name = list.get(i).getName();
				boolean flag = false;
				for(int j=0;j<files.length;j++){
					if(name.equals(files[j])){
						flag = true;
						break;
					}
				}
				if(!flag){
					appcreateService.getWfPicById(id,contextPath+File.separatorChar+"application"+File.separatorChar+"app_images" +File.pathSeparatorChar+ name);
					
				}
			}
			model.addAttribute("datas", list);
			model.addAttribute("appInfoId", appInfoId);
			model.addAttribute("picSelected",pic_selected_name );
		}
		return "path:selectPic";
	}
	/*   保存图片选择    */
	@SuppressWarnings("unchecked")
	public @ResponseBody String selectPicForApp(String appId ,String picName) throws Exception{
		appcreateService.updatePicSelected(appId,picName);
		return "success";
	}
	/** 获取口令 gw_tanx
	 * @param model
	 * @return
	 * 2014年7月29日
	 */
	public @ResponseBody String getSystemSecret(ModelMap model) throws Exception{

		return java.util.UUID.randomUUID().toString();

	}
	
	/** 判断是否删除应用
	 * @param model
	 * @return
	 * @throws Exception
	 * 2014年7月29日
	 */
	public @ResponseBody String isDeleteApp(String appInfoId, ModelMap model) throws Exception{

		return appcreateService.isDeleteApp(appInfoId);

	}
	
	
	
	/*   前台没有图片，后台传到前台    */
	@SuppressWarnings("unchecked")
	public @ResponseBody String findPic(String picName,HttpServletRequest request) throws Exception{
		String contextPath= request.getSession().getServletContext().getRealPath("");		
		appcreateService.getWfPicByName( picName,  contextPath+"\\application\\app_images\\" + picName);
		return "success";
	}
	
	
	public static void main(String []args0){
		File file=new File("D:\\apache-maven-3.0.4\\bin"); 
		String test[];  
		test=file.list();  
		for(int i=0;i<test.length;i++) 
		{   
			System.out.println(test[i]); 
			}
	}
}
