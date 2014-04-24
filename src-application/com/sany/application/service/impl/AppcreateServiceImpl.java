package com.sany.application.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.frameworkset.util.CollectionUtils;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.util.ListInfo;
import com.sany.application.entity.WfApp;
import com.sany.application.service.AppcreateService;

public class AppcreateServiceImpl implements AppcreateService {
	
	private static Logger logger = Logger.getLogger(AppcreateServiceImpl.class);
	
	private ConfigSQLExecutor executor;

	@Override
	public ListInfo findListPage(long offset, int pagesize, WfApp condition) {
		// TODO Auto-generated method stub
		ListInfo listInfo = null;
        try {
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
        } catch (Exception e) {
            logger.error(e);
        }

        return listInfo;
	}
	
	/**
     * 根据ID加载应用
     * @param wfAppId
     * @return
     */
    public WfApp queryWfAppById(String wfAppId){
    	
    	WfApp wfApp = new WfApp();
    	
    	wfApp.setId(wfAppId);
    	
    	List<WfApp> wfAppList = null;
    	
    	try {
    		wfAppList = executor.queryListBean(WfApp.class, "selectWfAppList", wfApp);
    	} catch (Exception e) {
            logger.error(e);
        }
    	if(!CollectionUtils.isEmpty(wfAppList)){
    		
    		return wfAppList.get(0);
    	}else{
    		return null;
    	}
    	
    }
    
    /**
     * 查询应用列表
     */
    public List<WfApp> queryWfApp(WfApp wfApp){
    	
    	List<WfApp> wfAppList = null;
    	
    	try {
    		wfAppList = executor.queryListBean(WfApp.class, "selectWfAppList", wfApp);
    		
    	} catch (Exception e) {
    		
            logger.error(e);
        }
    	return wfAppList;
    }
    
    /**
     * 验证应用口令
     */
    public Boolean validateWfAppSecret(WfApp wfApp){
    	
    	List<WfApp> wfAppList = null;
    	
    	WfApp validateWfApp = new WfApp();
    	
    	if(StringUtils.isNotEmpty(wfApp.getId()) && StringUtils.isNotEmpty(wfApp.getSystem_secret())){
    		
    		validateWfApp.setId(wfApp.getId());
    		
    		String secret = EncrpyPwd.encodePassword(wfApp.getSystem_secret());

    		validateWfApp.setSystem_secret(secret);

    		try{
        		wfAppList = executor.queryListBean(WfApp.class, "selectWfAppList", wfApp);
        		
        		if(wfAppList.size()>0){
        			return true;
        		}
        	} catch (Exception e) {
        		
                logger.error(e);
            }
    	}
    	return false;
    }
    
    /**
     * 删除应用
     * @param wfAppId
     */
    public void deleteWfAppById(String wfAppId){
    	
    	if(StringUtils.isNotEmpty(wfAppId)){
    		
    		WfApp wfApp = new WfApp();
        	
        	wfApp.setId(wfAppId);
        	
        	try {
        		executor.deleteBean("deleteWfApp", wfApp);
        	} catch (Exception e) {
                logger.error(e);
            }
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


