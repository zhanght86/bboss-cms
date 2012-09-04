/*
 * @(#)hrSyncTask.java
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
package com.sany.masterdata.task;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.manager.db.OrgCacheManager;
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

    /**
     *初始化 
     */
    public void initialData() {
        
        Long stateTime = System.currentTimeMillis();
        
        logger.info("initial organization info all data start...");
        SyncOrganizationInfo syncOrganizationInfo = new SyncOrganizationInfo();
        syncOrganizationInfo.initialData();
        logger.info("Sync organization info all data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
        logger.info("initial job info all data  start...");
        SyncJobInfo syncJobInfo = new SyncJobInfo();
        syncJobInfo.initialData();
        logger.info("initial job info all data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
        logger.info("initialuser info all data  start...");
        SyncUserInfo syncUserInfo = new SyncUserInfo();
        syncUserInfo.initialData();
        logger.info("initial user info all data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
    }
    
    /**
     *同步全量数据 
     */
    public void syncAllData() {
        
        Long stateTime = System.currentTimeMillis();
        
        logger.info("Sync organization info all data start...");
        SyncOrganizationInfo syncOrganizationInfo = new SyncOrganizationInfo();
        syncOrganizationInfo.syncAllData();
        logger.info("Sync organization info all data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
        logger.info("Sync job info all data  start...");
        SyncJobInfo syncJobInfo = new SyncJobInfo();
        syncJobInfo.syncAllData();
        logger.info("Sync job info all data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
        logger.info("Sync user info all data  start...");
        SyncUserInfo syncUserInfo = new SyncUserInfo();
        syncUserInfo.syncAllData();
        logger.info("Sync user info all data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
    }
    
    /**
     * 同步增量数据
     */
    public void syncExpandData() {
        
        Long stateTime = System.currentTimeMillis();
        
        logger.info("Sync organization info expand data start...");
        SyncOrganizationInfo syncOrganizationInfo = new SyncOrganizationInfo();
        syncOrganizationInfo.syncExpandData();
        logger.info("Sync organization info expand data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
        logger.info("Sync job info expand data  start...");
        SyncJobInfo syncJobInfo = new SyncJobInfo();
        syncJobInfo.syncExpandData();
        logger.info("Sync job info expand data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");
        
        logger.info("Sync user info expand data  start...");
        SyncUserInfo syncUserInfo = new SyncUserInfo();
        syncUserInfo.syncExpandData();
        logger.info("Sync user info expand data  end..., spend time " + (System.currentTimeMillis() - stateTime)
                / 1000 + "s");;
                
        
    }
    
    /**
     * 同步全量数据，并刷新内存中的组织机构数据
     */
    public void syncAllDaAndReset() {
        
        syncAllData();
        
        // 刷新内存中的组织机构数据
        OrgCacheManager.getInstance().reset();
    }
    
    /**
     * 同步增量数据，并刷新内存中的组织机构数据
     */
    public void syncExDaAndReset() {
        
        syncExpandData();
        
        // 刷新内存中的组织机构数据
        OrgCacheManager.getInstance().reset();
    }
    
    public static void main(String[] args) {
        HrSyncTask test = new HrSyncTask();
        test.syncAllData();
    }
}
