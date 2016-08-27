/*
 * @(#)TdSmOrganizationDao.java
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

import com.frameworkset.common.poolman.DBUtil;
import com.sany.masterdata.hr.entity.TdSmOrganization;

/**
 * TdSmOrganizationDao.java
 * @author caix3
 * @since 2012-3-21
 */
public class TdSmOrganizationDao extends DaoHandler {

    private Logger logger = Logger.getLogger(TdSmOrganizationDao.class);

    /**
     * get all organization information
     * @return List<TdSmOrganization>
     */
    public List<TdSmOrganization> findOrgList() {

        List<TdSmOrganization> orgList = null;
        try {
            orgList = executor.queryList(TdSmOrganization.class, "tdSmOrgAllData");
        } catch (Exception e) {
            logger.error("get organization list error", e);
        }
        return orgList;
    }
    
    /**
     * save a new organization info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int save(TdSmOrganization tdSmOrganization) {

        int response = 0;
        try {
            tdSmOrganization.setOrgSn(Integer.parseInt(DBUtil.getNextStringPrimaryKey("td_sm_organization")));
            executor.insertBean("tdSmOrgSave", tdSmOrganization);
        } catch (Exception e) {
            logger.error("save TdSmOrganization error", e);
        }
        return response;
    }

    /**
     * save or update organization info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int saveOrUpdate(TdSmOrganization tdSmOrganization) {

        try {
            TdSmOrganization temp = executor.queryObjectBean(TdSmOrganization.class, "tdSmOrgSelect", tdSmOrganization);
            if (temp == null) {
                return save(tdSmOrganization);
            } else {
                return update(tdSmOrganization);
            }
        } catch (Exception e) {
            logger.error("saveOrUpdate TdSmOrganization error", e);
        }
        return 0;
    }

    /**
     * update organization info
     * @param tdSmUser
     * @return 0: error 1: success
     */
    public int update(TdSmOrganization tdSmOrganization) {

        int response = 0;
        try {
            executor.updateBean("tdSmOrgUpdate", tdSmOrganization);
            response = 1;
        } catch (Throwable e) {
            logger.error("update TdSmOrganization error", e);
        }
        return response;
    }
}
