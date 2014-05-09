package com.sany.application.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.util.ListInfo;
import com.sany.application.entity.WfApp;
import com.sany.application.service.AppcreateService;

public class AppcreateServiceImpl implements AppcreateService {
	
	private ConfigSQLExecutor executor;

	@Override
	public ListInfo findListPage(long offset, int pagesize, WfApp condition) throws Exception {
		// TODO Auto-generated method stub
		ListInfo listInfo = null;

    	if (StringUtils.isNotEmpty(condition.getSystem_name())) {
    		condition.setSystem_name("%" + condition.getSystem_name() + "%");
        }
        if (StringUtils.isNotEmpty(condition.getSystem_id())) {
            condition.setSystem_id("%" + condition.getSystem_id() + "%");
        }
        if (StringUtils.isNotEmpty(condition.getApp_mode_type())) {
        	condition.setApp_mode_type("%" + condition.getApp_mode_type() + "%");
        }
        if (StringUtils.isNotEmpty(condition.getApp_mode_type_nonexist())){
        	condition.setApp_mode_type_nonexist("%" + condition.getApp_mode_type_nonexist() + "%"); 
        }
        listInfo = executor
                .queryListInfoBean(WfApp.class, "selectWfAppList", offset, pagesize, condition);

        return listInfo;
	}
	
	/**
     * 根据ID加载应用
     * @param wfAppId
     * @return
	 * @throws Exception 
     */
    public WfApp queryWfAppById(String wfAppId) throws Exception{
    	
    	
    	try {
    		return executor.queryObject(WfApp.class, "selectWfApp", wfAppId);
    	} catch (Exception e) {
            throw e;
            
        }
    	
    	
    }
    
    /**
     * 查询应用列表
     */
    public List<WfApp> queryWfApp(WfApp wfApp) throws Exception {
    	
    	List<WfApp> wfAppList = null;
    	
		wfAppList = executor.queryListBean(WfApp.class, "selectWfAppList", wfApp);
    	
    	return wfAppList;
    }
    
    /**
     * 验证应用口令
     */
    public Boolean validateWfAppSecret(WfApp wfApp) throws Exception {
    	
    	List<WfApp> wfAppList = null;
    	
    	WfApp validateWfApp = new WfApp();
    	
    	if(StringUtils.isNotEmpty(wfApp.getId()) && StringUtils.isNotEmpty(wfApp.getSystem_secret())){
    		
    		validateWfApp.setId(wfApp.getId());
    		
    		String secret = EncrpyPwd.encodePassword(wfApp.getSystem_secret());

    		validateWfApp.setSystem_secret(secret);

    		wfAppList = executor.queryListBean(WfApp.class, "selectWfAppList", wfApp);
    		
    		if(wfAppList.size()>0){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 删除应用
     * @param wfAppId
     */
    public void deleteWfAppById(String wfAppId) throws Exception {
    	
    	if(StringUtils.isNotEmpty(wfAppId)){
    		
    		WfApp wfApp = new WfApp();
        	
        	wfApp.setId(wfAppId);
        
    		executor.deleteBean("deleteWfApp", wfApp);
    	}
    }
    
    /**
     * 保存应用
     * @param wfApp
     */
    public void saveWfApp(WfApp wfApp) throws Exception {
    	
    	if(wfApp!=null){
    		
    		if(StringUtils.isNotEmpty(wfApp.getSystem_secret())){
    			
    			wfApp.setSystem_secret_text(wfApp.getSystem_secret());
    			
    			wfApp.setSystem_secret(EncrpyPwd.encodePassword(wfApp.getSystem_secret()));
    		}else if(StringUtils.isNotEmpty(wfApp.getSystem_secret_text())){
    			
    			wfApp.setSystem_secret(EncrpyPwd.encodePassword(wfApp.getSystem_secret_text()));
    			
    		}
    	
    		if(StringUtils.isNotEmpty(wfApp.getId())){
    			
    			wfApp.setUpdate_date(new Timestamp(new Date().getTime()));
    			
    			executor.updateBean("updateWfApp", wfApp);
    		}else{
    			
    			wfApp.setCreate_date(new Timestamp(new Date().getTime()));
    			
    			wfApp.setId(UUID.randomUUID().toString());
    			
    			executor.insertBean("insertWfApp", wfApp);
    		}
    	}
    }

	@Override
	public Boolean validateAppSecret(String appid,String secret) throws Exception {
		secret = EncrpyPwd.encodePassword(secret);
		int result = executor.queryObject(int.class, "validateAppSecret", appid,secret);
		return result > 0;
	}
    
}


