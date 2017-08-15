/*
 * @(#)TdSmJobDao.java
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
package com.sany.masterdata.hr.dao;

import org.apache.log4j.Logger;

import com.sany.masterdata.hr.entity.TdSmJob;

/**
 * TdSmJobDao.java
 * @author caix3
 * @since 2012-3-22
 */
public class TdSmJobDao extends DaoHandler {

    private Logger logger = Logger.getLogger(TdSmJobDao.class);

    /**
     * save a new job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int save(TdSmJob tdSmJob) {

        int response = 0;
        try {
            executor.insertBean("tdSmJobSave", tdSmJob);
        } catch (Exception e) {
            logger.error("save TdSmJob error", e);
        }
        return response;
    }

    /**
     * save or update job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int saveOrUpdate(TdSmJob tdSmJob) {

        try {
            TdSmJob temp = executor.queryObjectBean(TdSmJob.class, "tdSmJobSelect", tdSmJob);
            if (temp == null) {
                return save(tdSmJob);
            } else {
                return update(tdSmJob);
            }
        } catch (Exception e) {
            logger.error("saveOrUpdate TdSmJob error", e);
        }
        return 0;
    }

    /**
     * update job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int update(TdSmJob tdSmJob) {

        int response = 0;
        try {
            executor.updateBean("tdSmJobUpdate", tdSmJob);
            response = 1;
        } catch (Throwable e) {
            logger.error("update TdSmJob error", e);
        }
        return response;
    }
}
