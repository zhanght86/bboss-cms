/*
 * @(#)SyncUserInfo.java
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.authentication.EncrpyPwd;
import com.sany.masterdata.hr.dao.TdSmOrgUserDao;
import com.sany.masterdata.hr.dao.TdSmUserDao;
import com.sany.masterdata.hr.dao.TdSmUserJobOrgDao;
import com.sany.masterdata.hr.entity.TdSmOrgUser;
import com.sany.masterdata.hr.entity.TdSmUser;
import com.sany.masterdata.hr.entity.TdSmUserJobOrg;
import com.sany.masterdata.hr.entity.userinfo.ArrayOfUserInfo;
import com.sany.masterdata.hr.entity.userinfo.UserInfo;
import com.sany.masterdata.hr.webservices.client.UserInfoClient;

/**
 * 同步人员数据
 * @author caix3
 * @since 2012-03-20
 */
public class SyncUserInfo {

    private static final String[] SEX = {"", "M", "F"};

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static Logger logger = Logger.getLogger(SyncUserInfo.class);
    private static final String usepassword ;
    static
    {
    	usepassword = EncrpyPwd.encodePassword("123456");
    }

    /**
     * 同步所有人员数据
     */
    public void syncAllData() {

        syncExpandDataByDate(null, null);
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
    }

    /**
     * 同步所有人员数据
     */
    public void syncExpandDataByDate(Date stDate, Date enDate) {

        UserInfoClient client = new UserInfoClient();
        TdSmUserDao tdSmUserDao = new TdSmUserDao();
        TdSmOrgUserDao tdSmOrgUserDao = new TdSmOrgUserDao();
        TdSmUserJobOrgDao tdSmUserJobOrgDao = new TdSmUserJobOrgDao();

        // 分页获取用户数据
        int index = 0;
        int pageSize = client.getPageSize();
        for (int i = 0; i == index; i += pageSize) {
            try {
                TransactionManager tm = new TransactionManager();
                tm.begin(TransactionManager.RW_TRANSACTION);
                ArrayOfUserInfo userInfoList = client.getData(stDate, enDate, i, i + pageSize);
                index += userInfoList.getUserInfo().size();

                // 插入数据库
                for (UserInfo temp : userInfoList.getUserInfo()) {
                    tdSmUserDao.saveOrUpdate(getTdSmUesr(temp));
                    tdSmOrgUserDao.saveOrUpdate(getTdSmOrgUser(temp));
                    tdSmUserJobOrgDao.saveOrUpdate(getTdSmUserJobOrg(temp));
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

        UserInfoClient client = new UserInfoClient();
        TdSmUserDao tdSmUserDao = new TdSmUserDao();
        TdSmOrgUserDao tdSmOrgUserDao = new TdSmOrgUserDao();
        TdSmUserJobOrgDao tdSmUserJobOrgDao = new TdSmUserJobOrgDao();

        // 分页获取用户数据
        int index = 0;
        int pageSize = client.getPageSize();
        List<TdSmUser> userList = null;
        List<TdSmOrgUser> orgUserList = null;
        List<TdSmUserJobOrg> userJobOrgList = null;
        for (int i = 0; i == index; i += pageSize) {
            try {
                ArrayOfUserInfo userInfoList = client.getData(null, null, i, i + pageSize);
                index += userInfoList.getUserInfo().size();

                userList = new ArrayList<TdSmUser>();
                orgUserList = new ArrayList<TdSmOrgUser>();
                userJobOrgList = new ArrayList<TdSmUserJobOrg>();

                for (UserInfo temp : userInfoList.getUserInfo()) {
                    userList.add(getTdSmUesr(temp));
                    orgUserList.add(getTdSmOrgUser(temp));
                    userJobOrgList.add(getTdSmUserJobOrg(temp));
                }

                tdSmUserDao.batchSave(userList);
                tdSmOrgUserDao.batchsave(orgUserList);
                tdSmUserJobOrgDao.batchSave(userJobOrgList);

            } catch (Throwable e) {
                logger.error(e);
            }
        }
    }

    /**
     * 生成需要的人员部门关系实例
     * @param userinfo
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    private TdSmOrgUser getTdSmOrgUser(UserInfo userInfo) {

        TdSmOrgUser tdSmOrgUser = new TdSmOrgUser();

        tdSmOrgUser.setOrgId(userInfo.getOrgeh().getValue());
        tdSmOrgUser.setUserId(Integer.parseInt(userInfo.getUserId().getValue()));

        return tdSmOrgUser;
    }

    /**
     * 生成需要的人员信息字段实例
     * @param userinfo
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    private TdSmUser getTdSmUesr(UserInfo userInfo) throws SQLException {

        TdSmUser tdSmUser = new TdSmUser();

        tdSmUser.setUserId(Integer.parseInt(userInfo.getUserId().getValue()));
        tdSmUser.setUserSn(Integer.parseInt(userInfo.getUserId().getValue()));
        tdSmUser.setUserName(userInfo.getUsrid5().getValue().trim().toLowerCase());
        tdSmUser.setUserPassword(usepassword);
        tdSmUser.setUserRealname(userInfo.getNachn().getValue() + userInfo.getVorna().getValue());
        tdSmUser.setUserWorknumber(userInfo.getUserId().getValue());
        tdSmUser.setUserMobiletel1(userInfo.getUsrid3().getValue());

        String workTel = userInfo.getUsrid4().getValue().trim();
        if (workTel.equals("")) {
            workTel = userInfo.getUsrid3().getValue();
        }
        tdSmUser.setUserWorktel(workTel);

        tdSmUser.setUserIdcard(userInfo.getIcnum().getValue());
        tdSmUser.setUserIsvalid(2);
        tdSmUser.setUserLogincount(0);
        tdSmUser.setUserType("1");

        if (!userInfo.getGesch().getValue().trim().equals("")) {
            tdSmUser.setUserSex(SEX[Integer.parseInt(userInfo.getGesch().getValue())]);
        }

        try {
            if (!userInfo.getDat01().getValue().trim().equals("")) {
                tdSmUser.setUserRegdate(SDF.parse(userInfo.getDat01().getValue().trim()));
            }

            if (!userInfo.getGbdat().getValue().trim().equals("")) {
                tdSmUser.setUserBirthday(SDF.parse(userInfo.getGbdat().getValue().trim()));
            }
        } catch (ParseException e) {
            logger.warn(e);
        }

        return tdSmUser;
    }

    /**
     * 生成需要的人员岗位部门关系字段实例
     * @param userinfo
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    private TdSmUserJobOrg getTdSmUserJobOrg(UserInfo userInfo) {

        TdSmUserJobOrg tdSmUserJobOrg = new TdSmUserJobOrg();

        tdSmUserJobOrg.setOrgId(userInfo.getOrgeh().getValue());
        tdSmUserJobOrg.setUserId(Integer.parseInt(userInfo.getUserId().getValue()));
        tdSmUserJobOrg.setJobId(userInfo.getPlans().getValue());

        try {
            tdSmUserJobOrg.setJobStartTime(SDF.parse(userInfo.getZdate().getValue()));
        } catch (ParseException e) {
            logger.warn(e);
        }

        return tdSmUserJobOrg;
    }

    public static void main(String[] args) {
        SyncUserInfo test = new SyncUserInfo();
        test.initialData();
    }

}
