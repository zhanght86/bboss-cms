package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class UserJobs implements Serializable{
	String userName;
	String userRealname;
	String userMobiletel1;
	String jobName;
	String orgName;
	Integer userId;
	String userType;
	String userEmail;
	String org_Name;
	String userSex;
	String user_isvalid;//用户状态
	String user_regdate;//注册时间
	String dredge_time;//开通时间
	String fettle;//岗位状态
	Date jobStartTime;//上岗时间
	String orgId;//递归查询时用户所属机构
	Integer istaxmanager;
	/**
     * 密码修改时间
     */
    private Timestamp passwordUpdatetime;
    
    private Timestamp passwordExpiredTime; 

	// TODO 注意，增加新的字段属性时，请手工在 db.UserManagerImpl.getUserList(Organization org)
	// 中增加相应的项

	/**
	 * @return the passwordExpiredTime
	 */
	public Timestamp getPasswordExpiredTime() {
		return passwordExpiredTime;
	}

	/**
	 * @param passwordExpiredTime the passwordExpiredTime to set
	 */
	public void setPasswordExpiredTime(Timestamp passwordExpiredTime) {
		this.passwordExpiredTime = passwordExpiredTime;
	}

	/**
	 * @return the passwordUpdatetime
	 */
	public Timestamp getPasswordUpdatetime() {
		return passwordUpdatetime;
	}

	/**
	 * @param passwordUpdatetime the passwordUpdatetime to set
	 */
	public void setPasswordUpdatetime(Timestamp passwordUpdatetime) {
		this.passwordUpdatetime = passwordUpdatetime;
	}
	public String getFettle() {
		return fettle;
	}
	public void setFettle(String fettle) {
		this.fettle = fettle;
	}
	public Date getJobStartTime() {
		return jobStartTime;
	}
	public void setJobStartTime(Date jobStartTime) {
		this.jobStartTime = jobStartTime;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getUserMobiletel1() {
		return userMobiletel1;
	}
	public void setUserMobiletel1(String userMobiletel1) {
		this.userMobiletel1 = userMobiletel1;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRealname() {
		return userRealname;
	}
	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getOrg_Name() {
		return org_Name;
	}
	public void setOrg_Name(String org_Name) {
		this.org_Name = org_Name;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	
	public String getUser_isvalid() {
		return user_isvalid;
	}
	public void setUser_isvalid(String user_isvalid) {
		this.user_isvalid = user_isvalid;
	}
	
	public String getUser_regdate() {
		return user_regdate;
	}
	public void setUser_regdate(String user_regdate) {
		this.user_regdate = user_regdate;
	}
	
	public String getDredge_time() {
		return dredge_time;
	}
	public void setDredge_time(String dredge_time) {
		this.dredge_time = dredge_time;
	}
	public Integer getIstaxmanager() {
		return istaxmanager;
	}
	public void setIstaxmanager(Integer istaxmanager) {
		this.istaxmanager = istaxmanager;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
}
