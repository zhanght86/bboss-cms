/*
 * @(#)TdSmUserJobOrgDao.java
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

import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.masterdata.hr.entity.TdSmUserJobOrg;

/**
 * TdSmUserJobOrgDao.java
 * @author caix3
 * @since 2012-3-23 上午11:18:01
 */
public class TdSmUserJobOrgDao extends DaoHandler {

    private Logger logger = Logger.getLogger(TdSmUserJobOrgDao.class);

    /**
     * save a new user mapping organization job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int save(TdSmUserJobOrg tdSmUserJobOrg) {

        int response = 0;
        try {
            executor.insertBean("tdSmUserJobOrgSave", tdSmUserJobOrg);
            response = 1;
        } catch (Exception e) {
            logger.error("save TdSmUserJobOrg error", e);
        }
        return response;
    }

    /**
     * batch save
     * @param userList
     * @return
     */
    public int batchSave(List<TdSmUserJobOrg> List) {
        
        int response = 0;
        TransactionManager tm = new TransactionManager();
        try {
            tm.begin(TransactionManager.RW_TRANSACTION);
            executor.insertBeans("tdSmUserJobOrgSave", List);
            tm.commit();
        } catch (Exception e) {
            logger.error("save TdSmUserJobOrg error", e);
        }
        return response;
    }
    
    /**
     * save or update user mapping organization job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int saveOrUpdate(TdSmUserJobOrg tdSmUserJobOrg) {

        try {
            TdSmUserJobOrg temp = executor
                    .queryObjectBean(TdSmUserJobOrg.class, "tdSmUserJobOrgSelect", tdSmUserJobOrg);
            if (temp == null) {
                return save(tdSmUserJobOrg);
            } else {
                return update(tdSmUserJobOrg);
            }
        } catch (Exception e) {
            logger.error("saveOrUpdate TdSmUserJobOrg error", e);
        }

        return 0;
    }

    /**
     * update user mapping organization job info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int update(TdSmUserJobOrg tdSmUserJobOrg) {

        int response = 0;
        try {
            executor.updateBean("tdSmUserJobOrgUpdate", tdSmUserJobOrg);
            response = 1;
        } catch (Throwable e) {
            logger.error("update TdSmUserJobOrg error", e);
        }
        return response;
    }
}
