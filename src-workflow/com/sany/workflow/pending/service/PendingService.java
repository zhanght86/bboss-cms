package com.sany.workflow.pending.service;

import java.util.List;
import java.util.Map;

import com.sany.workflow.pending.bean.App;
import com.sany.workflow.pending.bean.SysTitle;

public interface PendingService {
	public List<SysTitle> getAllSysTitleByUsed(String used,String subscribe,String userId)throws Exception;
	
	public List<SysTitle> getAllSysTitleForScribe(String userId)throws Exception;
	
	public List<Map<String,String>> getSingleSub(String appId,String userId)throws Exception;
	
	
	public void updateSingleSub(String appId,String  userId,String pendingType)throws Exception;
	
	public void insertSingleSub(String appId,String  userId,String pendingType)throws Exception;
	
	public	List<App> getAllPendingApp()throws Exception;
	
	public	List<App>  getAllPendingAppByUserId(String userId)throws Exception;
	
	public SysTitle getSysTitleById(String id)throws Exception;
}
