package com.sany.application.service;

import java.util.List;

import com.frameworkset.util.ListInfo;
import com.sany.application.entity.WfApp;

public interface AppcreateService {
	
	
	/**
     * 分页读取应用
     * @param offset
     * @param pagesize
     * @param condition
     * @return
     */
    public ListInfo findListPage(long offset, int pagesize, WfApp condition) throws Exception;
    
    /**
     * 根据ID加载应用
     * @param wfAppId
     * @return
     */
    public WfApp queryWfAppById(String wfAppId) throws Exception;
    
    /**
     * 查询应用列表
     * @param wfAppId
     * @return
     */
    public List<WfApp> queryWfApp(WfApp wfApp) throws Exception;
    
    /**
     * 验证应用口令
     */
    public Boolean validateWfAppSecret(WfApp wfApp) throws Exception;
    
    /**
     * 验证应用口令
     */
    public Boolean validateAppSecret(String appid,String secret)  throws Exception;
    
    /**
     * 删除应用
     * @param wfAppId
     */
    public void deleteWfAppById(String wfAppId) throws Exception;
    
    /**
     * 保存应用
     * @param wfApp
     */
    public void saveWfApp(WfApp wfApp) throws Exception;
    
}
