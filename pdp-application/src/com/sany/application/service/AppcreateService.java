package com.sany.application.service;

import java.io.File;
import java.util.List;

import org.frameworkset.web.token.AppValidateResult;

import com.frameworkset.util.ListInfo;
import com.sany.application.entity.WfApp;
import com.sany.application.entity.WfPic;

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
    public AppValidateResult validateAppSecret(String appid,String secret)  throws Exception;
    
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
    /**
     * 保存图片
     * @param wfApp
     */
    public void insertPic(WfPic pic) throws Exception;
    /**
     * 获取所有图片没有内容
     * @param wfApp
     */
    public List<WfPic> getAllWfPicNoContent()throws Exception;
    /**
     * 获取单个图片含内容
     * @param wfApp
     */
    public File getWfPicById(String id,String path)throws Exception;
    /**
     * 获取应用已经选择的图片
     * @param wfApp
     */
    public List<String> getAppSelectedPic(String appInfoId)throws Exception;
    /**
     * 更新应用的图片
     * @param wfApp
     */
   public void updatePicSelected(String appId,String picName)throws Exception;
    /** 判断是否删除应用
     * @param appInfoId
     * @throws Exception
     * 2014年7月29日
     */
    public String isDeleteApp(String appInfoId) throws Exception;
    /**
     * 获取单个图片含内容
     * @param wfApp
     */
    
    public File getWfPicByName(String picName, String path) throws Exception ;
}
