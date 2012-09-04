/*
 * @(#)SyncJobInfo.java
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

import java.util.Calendar;
import java.util.Date;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.masterdata.hr.dao.TdSmJobDao;
import com.sany.masterdata.hr.dao.TdSmOrgJobDao;
import com.sany.masterdata.hr.entity.TdSmJob;
import com.sany.masterdata.hr.entity.TdSmOrgJob;
import com.sany.masterdata.hr.entity.jobinfo.ArrayOfJobInfo;
import com.sany.masterdata.hr.entity.jobinfo.JobInfo;
import com.sany.masterdata.hr.webservices.client.JobInfoClient;

/**
 * 同步岗位数据
 * @author caix3
 * @since 2012-3-22 下午4:50:11
 */
public class SyncJobInfo {

    private Logger logger = Logger.getLogger(SyncJobInfo.class);

    /**
     * 同步所有岗位数据
     */
    public void syncAllData() {

        syncExpandDataByDate(null, null);
    }

    /**
     * 同步26小时内增量数据
     */
    public void syncExpandData() {

        Calendar calendar = Calendar.getInstance();
        Date enDate = calendar.getTime();
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 26);
        Date stDate = calendar.getTime();

        syncExpandDataByDate(stDate, enDate);
    }

    /**
     * 获取指定时间段内增量数据
     */
    public void syncExpandDataByDate(Date stDate, Date enDate) {

        JobInfoClient client = new JobInfoClient();
        TdSmOrgJobDao tdSmOrgJobDao = new TdSmOrgJobDao();
        TdSmJobDao tdSmJobDao = new TdSmJobDao();

        // 分页获取用户数据
        int index = 0;
        int pageSize = client.getPageSize();
        for (int i = 0; i == index; i += pageSize) {
            try {
                TransactionManager tm = new TransactionManager();
                tm.begin(TransactionManager.RW_TRANSACTION);
                ArrayOfJobInfo jobInfoList = client.getData(stDate, enDate, i, i + pageSize);
                index += jobInfoList.getJobInfo().size();

                // 插入数据库
                for (JobInfo temp : jobInfoList.getJobInfo()) {
                    tdSmJobDao.saveOrUpdate(getTdSmJob(temp));
                    tdSmOrgJobDao.saveOrUpdate(getTdSmOrgJob(temp));
                }
                tm.commit();
            } catch (Throwable e) {
                logger.error(e);
            }
        }
    }

    /**
     * 初始化同步操作，速度较快，全量插入，不判重
     */
    public void initialData() {

        JobInfoClient client = new JobInfoClient();

        // 分页获取用户数据
        int index = 0;
        int pageSize = client.getPageSize();
        for (int i = 0; i == index; i += pageSize) {
            try {

                // 回写数据库
                TransactionManager tm = new TransactionManager();
                PreparedDBUtil pre1 = new PreparedDBUtil();
                PreparedDBUtil pre2 = new PreparedDBUtil();
                ArrayOfJobInfo jobInfoList = client.getData(null, null, i, i + pageSize);
                index += jobInfoList.getJobInfo().size();

                try {
                    tm.begin(TransactionManager.RW_TRANSACTION);
                    pre1.setBatchOptimize(true);
                    pre2.setBatchOptimize(true);
                    pre1.preparedInsert("insert into td_sm_job (job_id, job_name, job_desc) values (?, ?, ?)");
                    pre2.preparedInsert("insert into td_sm_orgjob (job_id, org_id, job_sn) values (?, ?, ?)");

                    for (JobInfo temp : jobInfoList.getJobInfo()) {

                        // 插入岗位表
                        pre1.setString(1, temp.getJobId().getValue());
                        pre1.setString(2, temp.getJobName().getValue());
                        pre1.setString(3, temp.getRemarks().getValue());
                        pre1.addPreparedBatch();

                        // 插入岗位机构表
                        pre2.setString(1, temp.getJobId().getValue());
                        pre2.setString(2, temp.getOrgId().getValue());
                        pre2.setString(3, temp.getJobId().getValue());
                        pre2.addPreparedBatch();
                    }

                    pre1.executePreparedBatch();
                    pre2.executePreparedBatch();
                    tm.commit();

                } catch (Throwable e) {
                    try {
                        tm.rollback();
                    } catch (RollbackException e1) {
                        logger.error("transaction manager roll back error", e1);
                    }
                    logger.error("initialData error", e);
                }

            } catch (Throwable e) {
                logger.error(e);
            }
        }
    }

    /**
     * getTdSmJob
     * @param temp
     * @return TdSmJob
     */
    private TdSmJob getTdSmJob(JobInfo temp) {

        TdSmJob tdSmJob = new TdSmJob();

        tdSmJob.setJobId(temp.getJobId().getValue());
        tdSmJob.setJobName(temp.getJobName().getValue());
        tdSmJob.setJobDesc(temp.getRemarks().getValue());

        return tdSmJob;
    }

    /**
     * getTdSmOrgJob
     * @param temp
     * @return TdSmOrgJob
     */
    private TdSmOrgJob getTdSmOrgJob(JobInfo temp) {

        TdSmOrgJob tdSmOrgJob = new TdSmOrgJob();

        tdSmOrgJob.setJobId(temp.getJobId().getValue());
        tdSmOrgJob.setOrgId(temp.getOrgId().getValue());

        return tdSmOrgJob;
    }

    public static void main(String[] args) {
        SyncJobInfo test = new SyncJobInfo();
        test.initialData();
    }
}
