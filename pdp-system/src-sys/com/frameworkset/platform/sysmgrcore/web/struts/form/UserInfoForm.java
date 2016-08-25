//Created by MyEclipse Struts
// XSL source (default): platform:/plugin/com.genuitec.eclipse.cross.easystruts.eclipse_3.8.1/xslt/JavaClass.xsl

package com.frameworkset.platform.sysmgrcore.web.struts.form;



import java.io.Serializable;
import java.sql.Date;

/** 
 * MyEclipse Struts
 * Creation date: 03-09-2006
 * 
 * XDoclet definition:
 * @struts:form name="userInfoForm"
 */
public class UserInfoForm  implements Serializable {

	// --------------------------------------------------------- Instance Variables
    /** The composite primary key value. */
    private String userId;

    /** The value of the simple userSn property. */
    private Integer userSn;

    /** The value of the simple userName property. */
    private String userName;

    /** The value of the simple userPassword property. */
    private String userPassword;

    /** The value of the simple userRealname property. */
    private String userRealname;
    
    private String homePhone;
    private String mail;
    private String mobile;
    private String ou;//机构名字
    private String orgId;//机构ID
    private String postalCode;
    private String advQuery;//查询相关字段，需要排除绑定
    private boolean admin;
    private int curUserId;
    //add by 
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
	 * 工作电话
	 */
	private String userWorktel;
	
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
	private String userBirthday;

	
	/**
	 * 用户地址
	 */
	private String userAddress;

	
	/**
	 * 身份证号
	 */
	private String userIdcard;

	/**
	 * 是否有效，有效为1否则为0
	 */
	private int userIsvalid = 1;

	/**
	 * 注册日期
	 */
	private String userRegdate;

	/**
	 * 登录次数
	 */
	private int userLogincount;
	
	/**
	 * 用户类型
	 */
	private String userType;
	
//	private String userEmail;
	
	private String shortMobile;
	private String remark1;
	private String remark3;
	private String remark4;
	private String remark5;
	/**
	 * 会员过期时间
	 */
	private String past_Time;
	/**
	 * 会员开通时间
	 */
	private String dredgeTime;
	/**
	 * 密码取回问题
	 */
	private String question;
	
	/**
	 * 密码取回答案
	 */
	private String answer;
	
	/**
	 * 会员类型 1:企业   2:个人
	 */
	private int type;

	
	/**
	 * 企业名称
	 */
	private String  enterpriseName;
	/**
	 * 企业介绍
	 */
	private String  enterpriseIntro;
	/**
	 * 企业联系人
	 */
	private String enterpriseLinkman;
	/**
	 * 是否公开联系方式和资料
	 */
	private String enableopen;
	/**
	 * 服务类别
	 */
	private String serviceType;
	/**
	 * 会员角色
	 */
	private String memberRole;
	/**
	 * 服务资源
	 */
	private String res;
	/**
	 * 注册用户/收费用户
	 */
    private String ispay;        
    /**
     * 单位网址
     */
    private String network_address;    
    /**
     * 会员状态0:已停用，1：新申请，2：已开通
     */
    private String status;     
    /**
     * 备注
     */
    private String user_desc; 
    /**
     * 申请服务说明
     */
    private String serverexplain;
    /**
	 * 最后登陆网站时间
	 */
    private Date lastloginDate;
    //add end
    
    /**
	 * 工龄
	 */
	private String worklength;
	
	/**
	 * 政治面貌
	 */
	private String politics;
	
	/**
     * 标识机构是否是税管员
     * 0-不是
     * 1-是
     * 
     * ADDED BY BIAOPING.YIN 2007.11.15
     */
    private int istaxmanager = 0;
	
	public String getWorklength() {
		return worklength;
	}
	public void setWorklength(String worklength) {
		this.worklength = worklength;
	}
	public String getPolitics() {
		return politics;
	}
	public void setPolitics(String politics) {
		this.politics = politics;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public String getShortMobile() {
		return shortMobile;
	}
	public void setShortMobile(String shortMobile) {
		this.shortMobile = shortMobile;
	}
//	public String getUserEmail() {
//		return userEmail;
//	}
//	public void setUserEmail(String userEmail) {
//		this.userEmail = userEmail;
//	}
	/**
	 * @return 返回 userName。
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName 要设置的 userName。
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return 返回 userPassword。
	 */
	public String getUserPassword() {
		return userPassword;
	}
	/**
	 * @param userPassword 要设置的 userPassword。
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	/**
	 * @return 返回 userRealname。
	 */
	public String getUserRealname() {
		return userRealname;
	}
	/**
	 * @param userRealname 要设置的 userRealname。
	 */
	public void setUserRealname(String userRealname) {
		this.userRealname = userRealname;
	}
	/**
	 * @return 返回 userSn。
	 */
	public Integer getUserSn() {
		if ( userSn == null ) userSn = new Integer(0);
		return userSn;
	}
	/**
	 * @param userSn 要设置的 userSn。
	 */
	public void setUserSn(Integer userSn) {
		if ( userSn == null ) userSn = new Integer(0);
		this.userSn = userSn;
	}
	/**
	 * @return 返回 userId。
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId 要设置的 userId。
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	// --------------------------------------------------------- Methods



	/**
	 * @return 返回 homePhone。
	 */
	public String getHomePhone() {
		return homePhone;
	}
	/**
	 * @param homePhone 要设置的 homePhone。
	 */
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	/**
	 * @return 返回 mail。
	 */
	public String getMail() {
		return mail;
	}
	/**
	 * @param mail 要设置的 mail。
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	/**
	 * @return 返回 mobile。
	 */
	public String getMobile() {
		return mobile;
	}
	/**
	 * @param mobile 要设置的 mobile。
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	/**
	 * @return 返回 ou。
	 */
	public String getOu() {
		return ou;
	}
	/**
	 * @param ou 要设置的 ou。
	 */
	public void setOu(String ou) {
		this.ou = ou;
	}
	/**
	 * @return 返回 orgId。
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId 要设置的 orgId。
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	/**
	 * @return 返回 postalCode。
	 */
	public String getPostalCode() {
		return postalCode;
	}
	/**
	 * @param postalCode 要设置的 postalCode。
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	public String getUserFax() {
		return userFax;
	}
	public void setUserFax(String userFax) {
		this.userFax = userFax;
	}
	public String getUserIdcard() {
		return userIdcard;
	}
	public void setUserIdcard(String userIdcard) {
		this.userIdcard = userIdcard;
	}
	public int getUserIsvalid() {
		return userIsvalid;
	}
	public void setUserIsvalid(int userIsvalid) {
		this.userIsvalid = userIsvalid;
	}
	public int getUserLogincount() {
		return userLogincount;
	}
	public void setUserLogincount(int userLogincount) {
		this.userLogincount = userLogincount;
	}
	public String getUserMobiletel2() {
		return userMobiletel2;
	}
	public void setUserMobiletel2(String userMobiletel2) {
		this.userMobiletel2 = userMobiletel2;
	}
	public String getUserOicq() {
		return userOicq;
	}
	public void setUserOicq(String userOicq) {
		this.userOicq = userOicq;
	}
	public String getUserPinyin() {
		return userPinyin;
	}
	public void setUserPinyin(String userPinyin) {
		this.userPinyin = userPinyin;
	}
	public String getUserRegdate() {
		return userRegdate;
	}
	public void setUserRegdate(String userRegdate) {
		this.userRegdate = userRegdate;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserWorknumber() {
		return userWorknumber;
	}
	public void setUserWorknumber(String userWorknumber) {
		this.userWorknumber = userWorknumber;
	}
	public String getUserWorktel() {
		return userWorktel;
	}
	public void setUserWorktel(String userWorktel) {
		this.userWorktel = userWorktel;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
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
	public String getRemark3() {
		return remark3;
	}
	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}
	
	public String getEnableopen() {
		return enableopen;
	}
	public void setEnableopen(String enableopen) {
		this.enableopen = enableopen;
	}
	public String getEnterpriseIntro() {
		return enterpriseIntro;
	}
	public void setEnterpriseIntro(String enterpriseIntro) {
		this.enterpriseIntro = enterpriseIntro;
	}
	public String getEnterpriseLinkman() {
		return enterpriseLinkman;
	}
	public void setEnterpriseLinkman(String enterpriseLinkman) {
		this.enterpriseLinkman = enterpriseLinkman;
	}
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}

	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getDredgeTime() {
		return dredgeTime;
	}
	public void setDredgeTime(String dredgeTime) {
		this.dredgeTime = dredgeTime;
	}
	public String getMemberRole() {
		return memberRole;
	}
	public void setMemberRole(String memberRole) {
		this.memberRole = memberRole;
	}
	public String getRes() {
		return res;
	}
	public void setRes(String res) {
		this.res = res;
	}
	public String getIspay() {
		return ispay;
	}
	public void setIspay(String ispay) {
		this.ispay = ispay;
	}
	public String getNetwork_address() {
		return network_address;
	}
	public void setNetwork_address(String network_address) {
		this.network_address = network_address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUser_desc() {
		return user_desc;
	}
	public void setUser_desc(String user_desc) {
		this.user_desc = user_desc;
	}
	public String getPast_Time() {
		return past_Time;
	}
	public void setPast_Time(String past_Time) {
		this.past_Time = past_Time;
	}
	public String getServerexplain() {
		return serverexplain;
	}
	public void setServerexplain(String serverexplain) {
		this.serverexplain = serverexplain;
	}
	public String getEnterpriseName() {
		return enterpriseName;
	}
	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}
	public Date getLastloginDate() {
		return lastloginDate;
	}
	public void setLastloginDate(Date lastloginDate) {
		this.lastloginDate = lastloginDate;
	}
	public int getIstaxmanager() {
		return istaxmanager;
	}
	public void setIstaxmanager(int istaxmanager) {
		this.istaxmanager = istaxmanager;
	}
	public String getAdvQuery() {
		return advQuery;
	}
	public void setAdvQuery(String advQuery) {
		this.advQuery = advQuery;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public int getCurUserId() {
		return curUserId;
	}
	public void setCurUserId(int curUserId) {
		this.curUserId = curUserId;
	}
}