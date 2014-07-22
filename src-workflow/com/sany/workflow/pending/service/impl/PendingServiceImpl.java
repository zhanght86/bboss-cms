package com.sany.workflow.pending.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.sany.workflow.pending.bean.App;
import com.sany.workflow.pending.bean.SysTitle;
import com.sany.workflow.pending.service.PendingService;

public class PendingServiceImpl implements PendingService {
	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;
	@Override
	public List<SysTitle> getAllSysTitleByUsed(String used,String subscribe,String userId) throws Exception {
		
		return executor.queryList(SysTitle.class, "getAllSysTitleByUsed", used,subscribe,userId);
	}
	@Override
	public List<SysTitle> getAllSysTitleForScribe(String userId) throws Exception {
		
		return executor.queryList(SysTitle.class, "getAllSysTitleForScribe", userId);
	}
	@Override
	public List<Map<String, String>> getSingleSub(String appId, String userId)
			throws Exception {
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		 executor.queryByNullRowHandler(new NullRowHandler() {

			@Override
			public void handleRow(Record origine) throws Exception {
				Map<String,String> map = new HashMap<String,String>();
				map.put("userId", origine.getString("USER_ID"));
				map.put("appId", origine.getString("APP_ID"));
				map.put("pendingSubscribe", origine.getString("PENDING_SUBSCRIBE"));
				list.add(map);
			}
		}, "getSingleSub", appId,userId);
		 
		 return list;
	}
	@Override
	public void updateSingleSub(String appId, String userId, String pendingType)
			throws Exception {
		executor.update("updateSingleSub",pendingType,  appId,  userId);
	}
	@Override
	public void insertSingleSub(String appId, String userId, String pendingType)
			throws Exception {
		executor.insert("insertSingleSub",  appId,  userId,  pendingType);
	}
	@Override
	public List<App> getAllPendingApp() throws Exception {
		
		return executor.queryList(App.class, "getAllPendingApp");
	}
	@Override
	public List<App> getAllPendingAppByUserId(String userId) throws Exception {
		
		return executor.queryList(App.class, "getAllPendingAppByUserId",userId);
	}
	@Override
	public SysTitle getSysTitleById(String id) throws Exception {
		
		List<SysTitle> list = executor.queryList(SysTitle.class, "getSysTitleById", id);
		
		return (null != list && list.size()>0)?list.get(0):null;
	}

}
