/*
 * @(#)TestMain.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.appbom.action;

import java.util.List;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.appbom.entity.AppBom;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AppBomController controller= new AppBomController();
		ConfigSQLExecutor executor_ = new ConfigSQLExecutor("com/frameworkset/platform/appbom/service/appbom.xml");
		TransactionManager tm = new TransactionManager();
		try {
			List<AppBom> beans_ = SQLExecutor.queryList(AppBom.class, "select * from td_app_bom where bm<='0003'");
			tm.begin();
			//beans_.setApp_name_en("SAP2");
			executor_.deleteBeans("deleteByKeys", beans_);
			executor_.insertBeans("batchsave", beans_);
			tm.commit();
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String updatebatch(AppBom bean) {
		TransactionManager tm = new TransactionManager();
		try {
			
			tm.begin();
			tm.commit();
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "path:index";
	}

}
