/*
 * @(#)SyncOrgInfo.java
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.greatwall.MdmService;
import com.sany.greatwall.domain.MdmPosition;
import com.sany.masterdata.utils.MDPropertiesUtil;

/**
 * 同步机构数据
 * @author caix3
 * @since 2012-3-22
 */
public class SyncJobInfo {

    private static final String JOB_SAVE_SQL = "insert into td_sm_job (job_name, job_id) values (?, ?)";
    
    private static final String JOBORG_SAVE_SQL = "insert into td_sm_orgjob (org_id, job_sn, job_id) values (?, ?, ?)";
    
    private static final String JOB_UPDATE_SQL = "update td_sm_job set job_name=? where job_id=?";
    
    private static final String JOBORG_UPDATE_SQL = "update td_sm_orgjob set org_id=?, job_sn=? where job_id=?";
    private static final String JOBORG_CLEAR_SQL = "delete from  td_sm_orgjob where org_id=? and job_id=?";
    
    private static final Integer BATCH_LIMIT = 1000;
    
    private static Logger logger = Logger.getLogger(SyncJobInfo.class);

    private MdmService mdmService;
    
    private ConfigSQLExecutor executor;
    
    /**
     * 同步所有机构数据
     */
    public void syncAllData() {
        logger.info("Sync job info started...");
        try {
            List<MdmPosition> jobList = mdmService.getPositionList("19000101", "99000101", "1", "99999999");
            Set<String> orgKeySet = new HashSet<String>(executor.queryList(String.class, "selectTdSmJobKey"));
            
            TransactionManager tm = new TransactionManager();
            PreparedDBUtil jobSavePre = new PreparedDBUtil();
            PreparedDBUtil jobOrgSavePre = new PreparedDBUtil();
            PreparedDBUtil jobUpdatePre = new PreparedDBUtil();
            PreparedDBUtil jobOrgUpdatePre = new PreparedDBUtil();
            try {
                tm.begin();
                
                int saveSize = 0;
                int updateSize = 0;
                jobSavePre.preparedInsert(JOB_SAVE_SQL);
                jobOrgSavePre.preparedInsert(JOBORG_SAVE_SQL);
                jobUpdatePre.preparedUpdate(JOB_UPDATE_SQL); 
                jobOrgUpdatePre.preparedUpdate(JOBORG_SAVE_SQL);
                
                for (MdmPosition temp : jobList) {
                    if (orgKeySet.contains(temp.getPositionId())) {
                        updateSize ++;
                        addPreBatch(jobUpdatePre, jobOrgUpdatePre, temp);
                        if (updateSize > BATCH_LIMIT) {
                            updateSize = 0;
                            jobUpdatePre.executePreparedBatch();
                            jobUpdatePre = new PreparedDBUtil();
                            jobUpdatePre.preparedUpdate(JOB_UPDATE_SQL);
                            jobOrgUpdatePre.executePreparedBatch();
                            jobOrgUpdatePre = new PreparedDBUtil();
                            jobOrgUpdatePre.preparedUpdate(JOBORG_SAVE_SQL);
                        }
                    } else {
                        saveSize ++;
                        addPreBatch(jobSavePre, jobOrgSavePre, temp);
                        if (saveSize > BATCH_LIMIT) {
                            saveSize = 0;
                            jobSavePre.executePreparedBatch();
                            jobSavePre = new PreparedDBUtil();
                            jobSavePre.preparedUpdate(JOB_SAVE_SQL);
                            jobOrgSavePre.executePreparedBatch();
                            jobOrgSavePre = new PreparedDBUtil();
                            jobOrgSavePre.preparedUpdate(JOBORG_SAVE_SQL);
                        }
                    }
                }
                
                if (saveSize > 0) { 
                    jobSavePre.executePreparedBatch(); 
                    jobOrgSavePre.executePreparedBatch();
                };
                if (updateSize > 0) { 
                    jobUpdatePre.executePreparedBatch(); 
                    jobOrgUpdatePre.executePreparedBatch();
                };
                
                tm.commit();
                
            } catch (Throwable e) {
               
                logger.error("initialData error", e);
            }
            finally
            {
            	tm.release();
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        logger.info("Sync job info finished...");
    }
    
    private void addPreBatch(PreparedDBUtil jobPre, PreparedDBUtil jobOrgPre, MdmPosition temp) throws Exception {
        
        jobPre.setString(1, temp.getPositionText());
        jobPre.setString(2, temp.getPositionId());
        jobPre.addPreparedBatch();
        
        if (temp.getOrgId() != null) {
        	SQLExecutor.delete(JOBORG_CLEAR_SQL, temp.getOrgId(),temp.getPositionId());
            jobOrgPre.setString(1, temp.getOrgId());
            jobOrgPre.setString(2, temp.getPositionRank());
            jobOrgPre.setString(3, temp.getPositionId());
            jobOrgPre.addPreparedBatch();
        }
    }

    public static void main(String[] args) {
        SyncJobInfo test = (SyncJobInfo) MDPropertiesUtil.getBeanObject("masterdata.hr.syncJobInfo");
        test.syncAllData();
    }
}
