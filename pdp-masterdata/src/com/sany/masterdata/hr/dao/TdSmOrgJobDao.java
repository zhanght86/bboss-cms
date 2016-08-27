/*
 * @(#)TdSmOrgJobDao.java
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
package com.sany.masterdata.hr.dao;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.sany.masterdata.hr.entity.TdSmOrgJob;

/**
 * TdSmOrgJobDao.java
 * @author caix3
 * @since 2012-3-22
 * @version
 */
public class TdSmOrgJobDao extends DaoHandler {

    private Logger logger = Logger.getLogger(TdSmOrgJobDao.class);

    /**
     * save a new organization map job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int save(TdSmOrgJob tdSmOrgJob) {

        int response = 0;
        try {
            if (!tdSmOrgJob.getJobId().trim().equals("") && !tdSmOrgJob.getOrgId().trim().equals("")) {
                tdSmOrgJob.setJobSn(new Long(DBUtil.getNextStringPrimaryKey("td_sm_job")));
                executor.insertBean("tdSmOrgJobSave", tdSmOrgJob);
            }
        } catch (Exception e) {
            logger.error("save TdSmOrgJob error", e);
        }
        return response;
    }

    /**
     * save or update organization map job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int saveOrUpdate(TdSmOrgJob tdSmOrgJob) {

        try {
            TdSmOrgJob temp = executor.queryObjectBean(TdSmOrgJob.class, "tdSmOrgJobSelect", tdSmOrgJob);
            if (temp == null) {
                return save(tdSmOrgJob);
            } else {
                return update(tdSmOrgJob);
            }
        } catch (Exception e) {
            logger.error("saveOrUpdate TdSmOrgJob error", e);
        }
        return 0;
    }

    /**
     * update organization map job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int update(TdSmOrgJob tdSmOrgJob) {

        int response = 0;
        try {
            if (!tdSmOrgJob.getJobId().trim().equals("") && !tdSmOrgJob.getOrgId().trim().equals("")) {
                executor.updateBean("tdSmOrgJobUpdate", tdSmOrgJob);
                response = 1;
            }
        } catch (Throwable e) {
            logger.error("update TdSmOrgJob error", e);
        }
        return response;
    }
}
