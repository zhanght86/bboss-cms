/*
 * @(#)hrSyncTask.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
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
package com.sany.masterdata.task;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;

import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdminCache;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;
import com.frameworkset.platform.sysmgrcore.purviewmanager.CacheManager;
import com.sany.masterdata.hr.sync.SyncJobInfo;
import com.sany.masterdata.hr.sync.SyncOrganizationInfo;
import com.sany.masterdata.hr.sync.SyncUserInfo;

/**
 * 人员主数据同步
 * @author caix3
 * @version 2012-3-30,v1.0 
 */
public class HrSyncTask {

    private static Logger logger = Logger.getLogger(HrSyncTask.class);

    private SyncUserInfo syncUserInfo;
    private SyncOrganizationInfo syncOrganizationInfo;
    private SyncJobInfo syncJobInfo;
    
    public String syncAllData() {
        try {
            logger.info("Sync HR master data start...");
            syncOrganizationInfo.syncAllData();
            syncJobInfo.syncAllData();
            syncUserInfo.syncAllData();
            
            logger.info("Sync HR master data finish...");
        } catch (Exception e) {
            logger.error("", e);
            return e.getMessage();
        }
        try {
//        	 logger.info("Refresh HR master user cache data ...");
//			UserCacheManager.getInstance().refresh();
//			logger.info("Refresh HR master organization cache data ...");
//			OrgCacheManager.getInstance().reset();
//			logger.info("Refresh HR master organization administrators cache data ...");
//			OrgAdminCache.getOrgAdminCache().reset();
        	Event<String> event = new EventImpl<String>(CacheManager.refreshMasterData,CacheManager.cacheRefreshEvent);
    		
    		/**
    		 * 事件以同步方式传播
    		 */
    		
    		EventHandle.sendEvent(event);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "success";
    }
}
