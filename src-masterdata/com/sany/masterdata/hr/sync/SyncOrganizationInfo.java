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

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.sany.masterdata.hr.dao.TdSmOrganizationDao;
import com.sany.masterdata.hr.entity.TdSmOrganization;
import com.sany.masterdata.hr.entity.orginfo.ArrayOfOrgInfo;
import com.sany.masterdata.hr.entity.orginfo.OrgInfo;
import com.sany.masterdata.hr.webservices.client.OrgInfoClient;

/**
 * 同步机构数据
 * @author caix3
 * @since 2012-3-22
 */
public class SyncOrganizationInfo {

    private static Logger logger = Logger.getLogger(SyncOrganizationInfo.class);

    /**
     * 同步所有机构数据
     */
    public void syncAllData() {

        syncExpandDataByDate(null, null);

        // 生成组织树结构级
        OrgTreeLevel.run();
    }

    /**
     * 同步24小时内增量数据
     */
    public void syncExpandData() {

        Calendar calendar = Calendar.getInstance();
        Date enDate = calendar.getTime();
        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 26);
        Date stDate = calendar.getTime();

        syncExpandDataByDate(stDate, enDate);

        // 生成组织树结构级
        OrgTreeLevel.run();
    }

    /**
     * 同步指定时间段的增量数据
     * @param stDate 起始时间
     * @param enDate 结束时间
     */
    public void syncExpandDataByDate(Date stDate, Date enDate) {

        OrgInfoClient client = new OrgInfoClient();
        TdSmOrganizationDao dao = new TdSmOrganizationDao();

        // 分页获取用户数据
        int index = 0;
        int pageSize = client.getPageSize();
        for (int i = 0; i == index; i += pageSize) {
            try {
                TransactionManager tm = new TransactionManager();
                tm.begin(TransactionManager.RW_TRANSACTION);
                ArrayOfOrgInfo orgInfoList = client.getData(stDate, enDate, i, i + pageSize);
                index += orgInfoList.getOrgInfo().size();

                // 插入数据库
                for (OrgInfo temp : orgInfoList.getOrgInfo()) {
                    dao.saveOrUpdate(getTdSmOrganization(temp));
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

        OrgInfoClient client = new OrgInfoClient();

        // 分页获取用户数据
        int index = 0;
        int pageSize = client.getPageSize();
        for (int i = 0; i == index; i += pageSize) {
            try {

                // 回写数据库
                TransactionManager tm = new TransactionManager();
                PreparedDBUtil pre = new PreparedDBUtil();
                ArrayOfOrgInfo orgInfoList = client.getData(null, null, i, i + pageSize);
                index += orgInfoList.getOrgInfo().size();

                try {
                    tm.begin(TransactionManager.RW_TRANSACTION);
                    pre.setBatchOptimize(true);
                    pre.preparedInsert("insert into td_sm_organization (org_id, org_sn, org_name, parent_id, orgnumber, "
                            + "remark3, remark5, org_level, org_xzqm) " + "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");

                    for (OrgInfo temp : orgInfoList.getOrgInfo()) {
                        // 插入机构表
                        pre.setString(1, temp.getOrgId().getValue());
                        pre.setString(2, temp.getOrgId().getValue());
                        pre.setString(3, temp.getOrgName().getValue());
                        if (temp.getOrgName().getValue().trim().equals("三一集团")) {
                            pre.setString(4, "0");
                        } else {
                            pre.setString(4, temp.getParentId().getValue());
                        }
                        pre.setString(5, temp.getOrgId().getValue());
                        pre.setString(6, temp.getUFlag().getValue());
                        pre.setString(7, temp.getOrgName().getValue());
                        pre.setString(8, temp.getOrgNumber().getValue());
                        pre.setString(9, "31" + temp.getOrgId().getValue());

                        pre.addPreparedBatch();
                    }

                    pre.executePreparedBatch();
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

        // 生成组织树结构级
        OrgTreeLevel.run();
    }

    /**
     * 生成所需entity
     * @param temp
     * @return TdSmOrganization
     * @throws SQLException
     * @throws NumberFormatException
     */
    private TdSmOrganization getTdSmOrganization(OrgInfo temp) throws NumberFormatException, SQLException {

        TdSmOrganization tdSmOrganization = new TdSmOrganization();

        tdSmOrganization.setOrgId(temp.getOrgId().getValue());
        tdSmOrganization.setOrgName(temp.getOrgName().getValue());
        tdSmOrganization.setOrgnumber(temp.getOrgId().getValue());
        tdSmOrganization.setRemark3(temp.getUFlag().getValue());
        tdSmOrganization.setRemark5(temp.getOrgName().getValue());
        tdSmOrganization.setOrgLevel(temp.getOrgNumber().getValue());
        tdSmOrganization.setOrgXzqm("31" + temp.getOrgId().getValue());
        if (temp.getOrgName().getValue().trim().equals("三一集团")) {
            tdSmOrganization.setParentId("0");
        } else {
            tdSmOrganization.setParentId(temp.getParentId().getValue());
        }

        return tdSmOrganization;
    }

    public static void main(String[] args) {
        SyncOrganizationInfo test = new SyncOrganizationInfo();
        test.initialData();
    }
}
