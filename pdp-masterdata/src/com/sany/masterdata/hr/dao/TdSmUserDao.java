/*
 * @(#)TdSmUserDao.java
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

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.masterdata.hr.entity.TdSmUser;

/**
 * TdSmUserDao.java
 * @author caix3
 * @since 2012-03-21
 */
public class TdSmUserDao extends DaoHandler {

    private Logger logger = Logger.getLogger(TdSmUserDao.class);

    /**
     * get user id by user work number
     * @param workNumber
     * @return user id
     * @throws Exception
     */
    public Integer getUserId(String workNumber) throws Exception {

        Integer response = 0;
        try {
            List<TdSmUser> temp = searchByWorkNumber(workNumber);
            if (temp.size() == 0) {
                response = new Long(DBUtil.getNextPrimaryKey("td_sm_user")).intValue();
            } else if (temp.size() == 1) {
                response = temp.get(0).getUserId();
            } else {
                throw new Exception("select TdSmUser error, user work number[" + workNumber + "] has " + temp.size()
                        + " result");
            }
        } catch (Exception e) {
            throw e;
        }
        return response;
    }

    /**
     * save a new user info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int save(TdSmUser tdSmUser) {

        int response = 0;
        try {
            executor.insertBean("tdSmUserSave", tdSmUser);
        } catch (Exception e) {
            logger.error("save TdSmUser error", e);
        }
        return response;
    }

    /**
     * batch save
     * @param userList
     * @return
     */
    public int batchSave(List<TdSmUser> userList) {
        
        int response = 0;
  
        try {
      
            executor.insertBeans("tdSmUserSave", userList);
         
        } catch (Exception e) {
            logger.error("save TdSmUser error", e);
        }
        return response;
    }
    
    /**
     * save or update a new user info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int saveOrUpdate(TdSmUser tdSmUser) {

        try {
            List<TdSmUser> temp = searchByWorkNumber(tdSmUser.getUserWorknumber());
            if (temp.size() == 0) {
                return save(tdSmUser);
            } else if (temp.size() == 1) {
                return update(tdSmUser);
            } else {
                throw new Exception("select TdSmUser error, user work number[" + tdSmUser.getUserWorknumber()
                        + "] has " + temp.size() + " result");
            }

        } catch (Exception e) {
            logger.error("saveOrUpdate TdSmUser error", e);
        }
        return 0;
    }

    /**
     * Search by user work number
     * @param userWorkNumber
     * @return List<TdSmUser>
     * @throws SQLException
     */
    public List<TdSmUser> searchByWorkNumber(String userWorkNumber) throws SQLException {

        return executor.queryList(TdSmUser.class, "tdSmUserSelectByWorkNumber", userWorkNumber);
    }

    /**
     * update a new user info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int update(TdSmUser tdSmUser) {

        int response = 0;
        try {
            executor.updateBean("tdSmUserUpdate", tdSmUser);
            response = 1;
        } catch (Exception e) {
            logger.error("update TdSmUser error", e);
        }
        return response;
    }
}
