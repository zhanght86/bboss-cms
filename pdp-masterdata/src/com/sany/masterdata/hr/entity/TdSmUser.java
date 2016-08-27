/*
 * @(#)TdSmUser.java
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
package com.sany.masterdata.hr.entity;

import java.util.Date;

/**
 * user info entity
 * @author caix3
 * @since 2012-03-22
 */
public class TdSmUser {

    /** The composite primary key value. */

    private Integer userId;

    /** The value of the simple userSn property. */
    private Integer userSn;

    /** The value of the simple userName property. */
    private String userName;

    /** The value of the simple userPassword property. */
    private String userPassword;

    /** The value of the simple userRealname property. */
    private String userRealname;

    /**
     * 工号
     */
    private String userWorknumber;

    /**
     * 拼音
     */
    private String userPinyin;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 家庭电话
     */
    private String userHometel;

    /**
     * 工作电话
     */
    private String userWorktel;

    /**
     * 移动电话1
     */
    private String userMobiletel1;

    /**
     * 移动电话2
     */
    private String userMobiletel2;

    /**
     * 传真
     */
    private String userFax;

    /**
     * OICQ
     */
    private String userOicq;

    /**
     * 生日
     */
    private Date userBirthday;

    /**
     * Email
     */
    private String userEmail;

    /**
     * 用户地址
     */
    private String userAddress;

    /**
     * 邮政编码
     */
    private String userPostalcode;

    /**
     * 身份证号
     */
    private String userIdcard;

    /**
     * 是否有效，有效为1否则为0
     */
    private Integer userIsvalid;

    /**
     * 注册日期
     */
    private Date userRegdate;

    /**
     * 登录次数
     */
    private Integer userLogincount;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 备用1
     */
    private String remark1;

    /**
     * 备用2 手机短号码字段
     */
    private String remark2;

    /**
     * 备用3
     */
    private String remark3;

    /**
     * 备用4
     */
    private String remark4;

    /**
     * 备用5
     */
    private String remark5;

    private Date pastTime;

    /**
     * 会员开通时间
     */
    private String dredgeTime;

    /**
     * 用户最后登陆日期
     */
    private Date lastloginDate;

    /**
     * 工龄
     */
    private String worklength;

    /**
     * 政治面貌
     */
    private String politics;

    private int istaxmanager;

    private String logonIp;

    private String certSn;

    public String getCertSn() {
        return certSn;
    }

    public String getDredgeTime() {
        return dredgeTime;
    }

    public int getIstaxmanager() {
        return istaxmanager;
    }

    public Date getLastloginDate() {
        return lastloginDate;
    }

    public String getLogonIp() {
        return logonIp;
    }

    public Date getPastTime() {
        return pastTime;
    }

    public String getPolitics() {
        return politics;
    }

    public String getRemark1() {
        return remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public String getRemark4() {
        return remark4;
    }

    public String getRemark5() {
        return remark5;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public Date getUserBirthday() {
        return userBirthday;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserFax() {
        return userFax;
    }

    public String getUserHometel() {
        return userHometel;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserIdcard() {
        return userIdcard;
    }

    public Integer getUserIsvalid() {
        return userIsvalid;
    }

    public Integer getUserLogincount() {
        return userLogincount;
    }

    public String getUserMobiletel1() {
        return userMobiletel1;
    }

    public String getUserMobiletel2() {
        return userMobiletel2;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserOicq() {
        return userOicq;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserPinyin() {
        return userPinyin;
    }

    public String getUserPostalcode() {
        return userPostalcode;
    }

    public String getUserRealname() {
        return userRealname;
    }

    public Date getUserRegdate() {
        return userRegdate;
    }

    public String getUserSex() {
        return userSex;
    }

    public Integer getUserSn() {
        return userSn;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserWorknumber() {
        return userWorknumber;
    }

    public String getUserWorktel() {
        return userWorktel;
    }

    public String getWorklength() {
        return worklength;
    }

    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }

    public void setDredgeTime(String dredgeTime) {
        this.dredgeTime = dredgeTime;
    }

    public void setIstaxmanager(int istaxmanager) {
        this.istaxmanager = istaxmanager;
    }

    public void setLastloginDate(Date lastloginDate) {
        this.lastloginDate = lastloginDate;
    }

    public void setLogonIp(String logonIp) {
        this.logonIp = logonIp;
    }

    public void setPastTime(Date pastTime) {
        this.pastTime = pastTime;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    public void setRemark5(String remark5) {
        this.remark5 = remark5;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserFax(String userFax) {
        this.userFax = userFax;
    }

    public void setUserHometel(String userHometel) {
        this.userHometel = userHometel;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserIdcard(String userIdcard) {
        this.userIdcard = userIdcard;
    }

    public void setUserIsvalid(Integer userIsvalid) {
        this.userIsvalid = userIsvalid;
    }

    public void setUserLogincount(Integer userLogincount) {
        this.userLogincount = userLogincount;
    }

    public void setUserMobiletel1(String userMobiletel1) {
        this.userMobiletel1 = userMobiletel1;
    }

    public void setUserMobiletel2(String userMobiletel2) {
        this.userMobiletel2 = userMobiletel2;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserOicq(String userOicq) {
        this.userOicq = userOicq;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserPinyin(String userPinyin) {
        this.userPinyin = userPinyin;
    }

    public void setUserPostalcode(String userPostalcode) {
        this.userPostalcode = userPostalcode;
    }

    public void setUserRealname(String userRealname) {
        this.userRealname = userRealname;
    }

    public void setUserRegdate(Date userRegdate) {
        this.userRegdate = userRegdate;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setUserSn(Integer userSn) {
        this.userSn = userSn;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserWorknumber(String userWorknumber) {
        this.userWorknumber = userWorknumber;
    }

    public void setUserWorktel(String userWorktel) {
        this.userWorktel = userWorktel;
    }

    public void setWorklength(String worklength) {
        this.worklength = worklength;
    }

}
