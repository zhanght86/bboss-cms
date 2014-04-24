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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.frameworkset.util.CollectionUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.util.ListInfo;
import com.sany.application.entity.WfApp;
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
            WfApp wfApp, ModelMap model) {
    	
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
    public String updateAppInfo(String appInfoId, ModelMap model){
    	
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
    public String viewAppInfo(String appInfoId, ModelMap model){
    	
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
    public @ResponseBody String validateWfApp(WfApp wfApp){
    	
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
				// TODO Auto-generated catch block
				logger.error(e);
				return e.getMessage();
			}
		}else{
			return "save data is null";
		}
		
		return "success";
	}
	
	public @ResponseBody String deleteAppInfo(String appInfoId){
		
		String actionResult = "fail";
		
		if(StringUtils.isNotEmpty(appInfoId)){
			appcreateService.deleteWfAppById(appInfoId);
			actionResult = "success";
		}
		
		return actionResult;
	}
	
}
