package com.frameworkset.platform.security.service.entity;

import java.io.Serializable;
import java.util.Date;

import com.frameworkset.orm.annotation.PrimaryKey;

public class CommonUser implements Serializable {
	/**
	 * USER_ID              NUMBER(22)               NOT NULL,
  USER_SN              NUMBER(22)               NOT NULL,
  USER_NAME            VARCHAR2(200 BYTE)       NOT NULL,
  USER_PASSWORD        VARCHAR2(200 BYTE)       NOT NULL,
  USER_REALNAME        VARCHAR2(100 BYTE)       NOT NULL,
  USER_PINYIN          VARCHAR2(100 BYTE),
  USER_SEX             VARCHAR2(100 BYTE),
  USER_HOMETEL         VARCHAR2(100 BYTE),
  USER_WORKTEL         VARCHAR2(100 BYTE),
  USER_WORKNUMBER      VARCHAR2(100 BYTE),
  USER_MOBILETEL1      VARCHAR2(100 BYTE),
  USER_MOBILETEL2      VARCHAR2(100 BYTE),
  USER_FAX             VARCHAR2(100 BYTE),
  USER_OICQ            VARCHAR2(100 BYTE),
  USER_BIRTHDAY        DATE,
  USER_EMAIL           VARCHAR2(100 BYTE),
  USER_ADDRESS         VARCHAR2(200 BYTE),
  USER_POSTALCODE      VARCHAR2(10 BYTE),
  USER_IDCARD          VARCHAR2(50 BYTE),
  USER_ISVALID         NUMBER(22),
  USER_REGDATE         DATE,
  USER_LOGINCOUNT      NUMBER(22),
  USER_TYPE            VARCHAR2(100 BYTE),
  REMARK1              VARCHAR2(100 BYTE),
  REMARK2              VARCHAR2(100 BYTE),
  REMARK3              VARCHAR2(100 BYTE),
  REMARK4              VARCHAR2(100 BYTE),
  REMARK5              VARCHAR2(100 BYTE),
  PAST_TIME            DATE,
  DREDGE_TIME          VARCHAR2(50 BYTE),
  LASTLOGIN_DATE       DATE,
  WORKLENGTH           VARCHAR2(50 BYTE),
  POLITICS             VARCHAR2(100 BYTE),
  ISTAXMANAGER         NUMBER(1),
  LOGON_IP             VARCHAR2(15 BYTE),
  CERT_SN              VARCHAR2(50 BYTE),
  PASSWORD_UPDATETIME  TIMESTAMP(6),
  PASSWORD_DUALTIME    NUMBER(10)
	 */	
	@PrimaryKey(pkname="td_sm_user")
	private Integer user_id;
	private Integer user_sn;
	private String user_name;
	private String user_password;
	private String password_text;
	private String user_realname;
	private String user_sex;
	private String user_hometel;
	private String user_worktel;
	private String user_worknumber;
	private String user_mobiletel1;
	private String user_mobiletel2;
	private String user_fax;
	private String user_oicq;
	private Date user_birthday;
	private String user_email;
	private String user_address;
	private String user_postalcode;
	private String user_idcard;
	/**
	 *  开通	2
		申请	1
		停用	3
		删除	0
	 */
	private Integer user_isvalid=1;
	private Date user_regdate;
	/**
	 * 系统用户	0
	域用户	1
	会员用户	2
	 */
	private Integer user_type=2;
	private String remark1;
	private String remark2;
	private String remark3;
	private String remark4;
	private String remark5;
	private String worklength;
	private String cert_sn;
	private String org_id;
	private Date update_time;
	public CommonUser() {
		// TODO Auto-generated constructor stub
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getUser_sn() {
		return user_sn;
	}
	public void setUser_sn(Integer user_sn) {
		this.user_sn = user_sn;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getPassword_text() {
		return password_text;
	}
	public void setPassword_text(String password_text) {
		this.password_text = password_text;
	}
	public String getUser_realname() {
		return user_realname;
	}
	public void setUser_realname(String user_realname) {
		this.user_realname = user_realname;
	}
	public String getUser_sex() {
		return user_sex;
	}
	public void setUser_sex(String user_sex) {
		this.user_sex = user_sex;
	}
	public String getUser_hometel() {
		return user_hometel;
	}
	public void setUser_hometel(String user_hometel) {
		this.user_hometel = user_hometel;
	}
	public String getUser_worktel() {
		return user_worktel;
	}
	public void setUser_worktel(String user_worktel) {
		this.user_worktel = user_worktel;
	}
	public String getUser_worknumber() {
		return user_worknumber;
	}
	public void setUser_worknumber(String user_worknumber) {
		this.user_worknumber = user_worknumber;
	}
	public String getUser_mobiletel1() {
		return user_mobiletel1;
	}
	public void setUser_mobiletel1(String user_mobiletel1) {
		this.user_mobiletel1 = user_mobiletel1;
	}
	public String getUser_mobiletel2() {
		return user_mobiletel2;
	}
	public void setUser_mobiletel2(String user_mobiletel2) {
		this.user_mobiletel2 = user_mobiletel2;
	}
	public String getUser_fax() {
		return user_fax;
	}
	public void setUser_fax(String user_fax) {
		this.user_fax = user_fax;
	}
	public String getUser_oicq() {
		return user_oicq;
	}
	public void setUser_oicq(String user_oicq) {
		this.user_oicq = user_oicq;
	}
	public Date getUser_birthday() {
		return user_birthday;
	}
	public void setUser_birthday(Date user_birthday) {
		this.user_birthday = user_birthday;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_address() {
		return user_address;
	}
	public void setUser_address(String user_address) {
		this.user_address = user_address;
	}
	public String getUser_postalcode() {
		return user_postalcode;
	}
	public void setUser_postalcode(String user_postalcode) {
		this.user_postalcode = user_postalcode;
	}
	public String getUser_idcard() {
		return user_idcard;
	}
	public void setUser_idcard(String user_idcard) {
		this.user_idcard = user_idcard;
	}
	public Integer getUser_isvalid() {
		return user_isvalid;
	}
	public void setUser_isvalid(Integer user_isvalid) {
		this.user_isvalid = user_isvalid;
	}
	public Date getUser_regdate() {
		return user_regdate;
	}
	public void setUser_regdate(Date user_regdate) {
		this.user_regdate = user_regdate;
	}
	public Integer getUser_type() {
		return user_type;
	}
	public void setUser_type(Integer user_type) {
		this.user_type = user_type;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	public String getRemark4() {
		return remark4;
	}
	public void setRemark4(String remark4) {
		this.remark4 = remark4;
	}
	public String getRemark5() {
		return remark5;
	}
	public void setRemark5(String remark5) {
		this.remark5 = remark5;
	}
	public String getWorklength() {
		return worklength;
	}
	public void setWorklength(String worklength) {
		this.worklength = worklength;
	}
	public String getCert_sn() {
		return cert_sn;
	}
	public void setCert_sn(String cert_sn) {
		this.cert_sn = cert_sn;
	}
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

}
