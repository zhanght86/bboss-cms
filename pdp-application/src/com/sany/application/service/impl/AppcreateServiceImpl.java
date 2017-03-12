package com.sany.application.service.impl;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.frameworkset.security.ecc.SimpleKeyPair;
import org.frameworkset.web.token.AppValidateResult;
import org.frameworkset.web.token.Application;
import org.frameworkset.web.token.TokenHelper;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.frameworkset.util.ListInfo;
import com.sany.application.entity.WfApp;
import com.sany.application.entity.WfPic;
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
    		WfApp wfApp = executor.queryObject(WfApp.class, "selectWfApp", wfAppId);
    		SimpleKeyPair keypair = TokenHelper.getTokenService().getSimpleKey(wfApp.getSystem_id(), "RSA");
    		if(keypair != null)
    		{
    			wfApp.setPrivateKey(keypair.getPrivateKey());
    			wfApp.setPublicKey(keypair.getPublicKey());
    		}
    		return wfApp;
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
    			if(wfApp.getNeedsign() == 1)
    			{
    				TokenHelper.getTokenService().getSimpleKey(wfApp.getSystem_id(), "RSA");
    			}
    		}else{
    			
    			wfApp.setCreate_date(new Timestamp(new Date().getTime()));
    			
    			wfApp.setId(UUID.randomUUID().toString());
    			
    			executor.insertBean("insertWfApp", wfApp);
    			if(wfApp.getNeedsign() == 1)
    			{
    				TokenHelper.getTokenService().getSimpleKey(wfApp.getSystem_id(), "RSA");
    			}
    		}
    	}
    }

	@Override
	public AppValidateResult validateAppSecret(String appid,String secret) throws Exception {
		secret = EncrpyPwd.encodePassword(secret);
		AppValidateResult result = executor.queryObjectByRowHandler(new RowHandler<AppValidateResult>(){

			@Override
			public void handleRow(AppValidateResult rowValue, Record record)
					throws Exception {
				rowValue.setResult(true);
				Application app = new Application();
				app.setAppid(record.getString("SYSTEM_ID"));
				app.setSecret(record.getString("SYSTEM_SECRET"));
				app.setTicketlivetime(record.getLong("TICKETTIME"));
				
				int needsign = record.getInt("needsign");
				app.setSign(needsign == 1);
				rowValue.setApplication(app);
			}
			
		},AppValidateResult.class, "validateAppSecret", appid,secret);
		return result;
		
	}
	
	public WfApp getApplication(String appid) throws Exception{
		WfApp wfApp = executor.queryObject(WfApp.class, "selectWfAppCode", appid);
		return wfApp;
	}

	@Override
	public void insertPic(WfPic pic) throws Exception {
		executor.insertBean("insertPic", pic);
	}

	@Override
	public List<WfPic> getAllWfPicNoContent() throws Exception {
		return executor.queryList(WfPic.class, "getAllWfPicNoContent");
	}

	@Override
	public File getWfPicById(String id,final String path) throws Exception {

		return executor.queryTField( File.class,   new FieldRowHandler<File>() {

			@Override
			public File handleField(Record record) throws Exception {
				File f = new File(path);   
				if (f.exists())					 
					return f;
				record.getFile("content",f); 
				return f;  
			}  },"getWfPicById",id);
		
	}
	@Override
	public File getWfPicByName(String picName,final String path) throws Exception {

		return executor.queryTField( File.class,   new FieldRowHandler<File>() {

			@Override
			public File handleField(Record record) throws Exception {
				File f = new File(path);   
				if (f.exists())					 
					return f;
				record.getFile("content",f); 
				return f;  
			}  },"getWfPicByName",picName);
		
	}

	@Override
	public List<String> getAppSelectedPic(String appInfoId) throws Exception {
		
		return executor.queryList(String.class, "getAppSelectedPic",appInfoId);
	}

	@Override
	public void updatePicSelected(String appId, String picName)
			throws Exception {
		executor.update("updatePicSelected", picName,appId);
	}
    @Override
	public String isDeleteApp(String appInfoId) throws Exception {
		int result = executor.queryObject(int.class, "validateAppRelationIsExists", appInfoId);
		if (result > 0) {
			return "success";
		}else {
			return "fail";
		}
	}
}


