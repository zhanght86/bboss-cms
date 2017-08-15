/*
 * @(#)TdSmOrgUserDao.java
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

import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.masterdata.hr.entity.TdSmOrgUser;

/**
 * TdSmOrgUserDao.java
 * @author caix3
 * @since 2012-3-22
 */
public class TdSmOrgUserDao extends DaoHandler {

    private Logger logger = Logger.getLogger(TdSmOrgUserDao.class);

    /**
     * save a new user mapping organization info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int save(TdSmOrgUser tdSmOrgUser) {

        int response = 0;
        try {
            executor.insertBean("tdSmOrgUserSave", tdSmOrgUser);
        } catch (Exception e) {
            logger.error("save TdSmOrgUser error", e);
        }
        return response;
    }

    /**
     * batch save
     * @param list
     * @return
     */
    public int batchsave(List<TdSmOrgUser> list) {

        int response = 0;
       
        try {
           
            executor.insertBeans("tdSmOrgUserSave", list);
          
        } catch (Exception e) {
            logger.error("save TdSmOrgUser error", e);
        }
        return response;
    }
    
    /**
     * save or update user mapping organization info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int saveOrUpdate(TdSmOrgUser tdSmOrgUser) {

        try {
            TdSmOrgUser temp = executor.queryObjectBean(TdSmOrgUser.class, "tdSmOrgUserSelect", tdSmOrgUser);
            if (temp == null) {
                return save(tdSmOrgUser);
            } else {
                return update(tdSmOrgUser);
            }
        } catch (Exception e) {
            logger.error("saveOrUpdate TdSmOrgUser error", e);
        }
        return 0;
    }

    /**
     * update user mapping organization info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int update(TdSmOrgUser tdSmOrgUser) {

        int response = 0;
        try {
            executor.updateBean("tdSmOrgUserUpdate", tdSmOrgUser);
            response = 1;
        } catch (Throwable e) {
            logger.error("update TdSmOrgUser error", e);
        }
        return response;
    }
    
}
