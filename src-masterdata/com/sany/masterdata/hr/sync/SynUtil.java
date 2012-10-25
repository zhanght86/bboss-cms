/*
 * @(#)SynUtil.java
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
package com.sany.masterdata.hr.sync;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.SQLParams;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.sany.masterdata.task.HrSyncTask;

/**
 * @author yinbp
 * @since 2012-3-27 上午9:43:02
 */
public class SynUtil {

//    public static void main(String[] args) {
////        SyncJobInfo test = new SyncJobInfo();
////        test.syncAllData();
////        SyncOrganizationInfo test_ = new SyncOrganizationInfo();
////        test_.syncAllData();
////        SyncUserInfo test__ = new SyncUserInfo();
////        test__.syncAllData();
//    	HrSyncTask task = new HrSyncTask();
////    	task.syncAllData();
////    	task.initialData();
//
//    }
	static class User 
	{
		String name;
		String password;
	}
	public static void encryptUserPassword() throws SQLException
	{
		final Map<String,String> users = new HashMap<String,String>();
		SQLExecutor.queryByNullRowHandler(new NullRowHandler(){
			
			@Override
			public void handleRow(Record arg0) throws Exception {			
				
				users.put(arg0.getString("USER_NAME"), arg0.getString("USER_PASSWORD"));
				
			}}, "select USER_NAME,USER_PASSWORD from td_sm_user ");
		if(users.size() > 0)
		{
			List<SQLParams> params_list = new ArrayList<SQLParams>();
			Iterator<String> its = users.keySet().iterator();
			int i = 0;
			while(its.hasNext())
			{
				String userName = its.next();
				String password = users.get(userName);
				SQLParams params = new SQLParams (); 
				params.addSQLParam("USER_NAME", userName, SQLParams.STRING);
				params.addSQLParam("USER_PASSWORD", EncrpyPwd.encodePassword(password), SQLParams.STRING);
				params_list.add(params);
				i ++;
				if(i == 1000)
				{
					SQLExecutor.updateBeans("update td_sm_user set USER_PASSWORD=#[USER_PASSWORD] where USER_NAME=#[USER_NAME] ", params_list);
					params_list.clear();
					i = 0;
					
				}
				
			}
			if(params_list.size() > 0)
			{
				SQLExecutor.updateBeans("update td_sm_user set USER_PASSWORD=#[USER_PASSWORD] where USER_NAME=#[USER_NAME] ", params_list);
				params_list.clear();
				i = 0;
			}
		}
		
	}
	
	public static void main(String[] args) throws SQLException
	{
		initMasterData() ;
	}
	
	public static void initMasterData() throws SQLException
	{
		  HrSyncTask task = new HrSyncTask();
		  task.initialData();
		
	}
}
