//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemember\\SiteMember.java

package com.frameworkset.platform.cms.sitemember;

import java.util.Date;


/**
 * 网站会员信息
 */
public class SiteMember implements java.io.Serializable
{
    
    /**
     * 真实名称
     */
    private String realname;
    
    /**
     * 性别:男,女,未知
     */
    private String sex;
    
    /**
     * 加入时间
     */
    private Date attendTime;
    
    /**
     * 帐号
     */
    private String account;
    
    /**
     * 口令
     */
    private String password;
    
    /**
     * 生日
     */
    private Date birthdate;
    
    /**
     * 工作
     */
    private String work;
    
    /**
     * 状态:0-待审核
     *          1-开通
     *          2-停用
     */
    private int status;
    
    /**
     * 会员级别:在数据字典中定义
     */
    private int level;
    
    /**
     * 会员邮箱
     */
    private String email;
    
    /**
     * 会员学位
     */
    private String degree;
    
    /**
     * 会员地址
     */
    private String address;
    
    /**
     * 会员电话
     */
    private String telephone;
    
    /**
     * 会员手机
     */
    private String mobile;
    
    /**
     * 密码问题
     */
    private String question;
    
    /**
     * 密码答案
     */
    private String answer;
    
    /**
     * 会员登录次数
     */
    private int loginTimes;
    
    /**
     * 国别
     */
    private String country;
    /**
	 * 服务类别
	 */
	private String serviceType;
	/**
	 * 地市代码
	 */
	private String citycode;   
	/**
	 * 会员中文描述
	 */
	private String userchs;
	/**
	 * 邮政编码
	 */
	private String userPostalcode;
	
	
	
    public java.util.List theDocComment;
    
    /**
     * @since 2006.12
     */
    public SiteMember() 
    {
     
    }
    
    /**
     * Access method for the realname property.
     * 
     * @return   the current value of the realname property
     */
    public String getRealname() 
    {
        return realname;     
    }
    
    /**
     * Sets the value of the realname property.
     * 
     * @param aRealname the new value of the realname property
     */
    public void setRealname(String aRealname) 
    {
        realname = aRealname;     
    }
    
    /**
     * Access method for the sex property.
     * 
     * @return   the current value of the sex property
     */
    public String getSex() 
    {
        return sex;     
    }
    
    /**
     * Sets the value of the sex property.
     * 
     * @param aSex the new value of the sex property
     */
    public void setSex(String aSex) 
    {
        sex = aSex;     
    }
    
    /**
     * Access method for the attendTime property.
     * 
     * @return   the current value of the attendTime property
     */
    public Date getAttendTime() 
    {
        return attendTime;     
    }
    
    /**
     * Sets the value of the attendTime property.
     * 
     * @param aAttendTime the new value of the attendTime property
     */
    public void setAttendTime(Date aAttendTime) 
    {
        attendTime = aAttendTime;     
    }
    
    /**
     * Access method for the account property.
     * 
     * @return   the current value of the account property
     */
    public String getAccount() 
    {
        return account;     
    }
    
    /**
     * Sets the value of the account property.
     * 
     * @param aAccount the new value of the account property
     */
    public void setAccount(String aAccount) 
    {
        account = aAccount;     
    }
    
    /**
     * Access method for the password property.
     * 
     * @return   the current value of the password property
     */
    public String getPassword() 
    {
        return password;     
    }
    
    /**
     * Sets the value of the password property.
     * 
     * @param aPassword the new value of the password property
     */
    public void setPassword(String aPassword) 
    {
        password = aPassword;     
    }
    
    /**
     * Access method for the birthdate property.
     * 
     * @return   the current value of the birthdate property
     */
    public Date getBirthdate() 
    {
        return birthdate;     
    }
    
    /**
     * Sets the value of the birthdate property.
     * 
     * @param aBirthdate the new value of the birthdate property
     */
    public void setBirthdate(Date aBirthdate) 
    {
        birthdate = aBirthdate;     
    }
    
    /**
     * Access method for the work property.
     * 
     * @return   the current value of the work property
     */
    public String getWork() 
    {
        return work;     
    }
    
    /**
     * Sets the value of the work property.
     * 
     * @param aWork the new value of the work property
     */
    public void setWork(String aWork) 
    {
        work = aWork;     
    }
    
    /**
     * Access method for the status property.
     * 
     * @return   the current value of the status property
     */
    public int getStatus() 
    {
        return status;     
    }
    
    /**
     * Sets the value of the status property.
     * 
     * @param aStatus the new value of the status property
     */
    public void setStatus(int aStatus) 
    {
        status = aStatus;     
    }
    
    /**
     * Access method for the level property.
     * 
     * @return   the current value of the level property
     */
    public int getLevel() 
    {
        return level;     
    }
    
    /**
     * Sets the value of the level property.
     * 
     * @param aLevel the new value of the level property
     */
    public void setLevel(int aLevel) 
    {
        level = aLevel;     
    }
    
    /**
     * Access method for the email property.
     * 
     * @return   the current value of the email property
     */
    public String getEmail() 
    {
        return email;     
    }
    
    /**
     * Sets the value of the email property.
     * 
     * @param aEmail the new value of the email property
     */
    public void setEmail(String aEmail) 
    {
        email = aEmail;     
    }
    
    /**
     * Access method for the degree property.
     * 
     * @return   the current value of the degree property
     */
    public String getDegree() 
    {
        return degree;     
    }
    
    /**
     * Sets the value of the degree property.
     * 
     * @param aDegree the new value of the degree property
     */
    public void setDegree(String aDegree) 
    {
        degree = aDegree;     
    }
    
    /**
     * Access method for the address property.
     * 
     * @return   the current value of the address property
     */
    public String getAddress() 
    {
        return address;     
    }
    
    /**
     * Sets the value of the address property.
     * 
     * @param aAddress the new value of the address property
     */
    public void setAddress(String aAddress) 
    {
        address = aAddress;     
    }
    
    /**
     * Access method for the telephone property.
     * 
     * @return   the current value of the telephone property
     */
    public String getTelephone() 
    {
        return telephone;     
    }
    
    /**
     * Sets the value of the telephone property.
     * 
     * @param aTelephone the new value of the telephone property
     */
    public void setTelephone(String aTelephone) 
    {
        telephone = aTelephone;     
    }
    
    /**
     * Access method for the mobile property.
     * 
     * @return   the current value of the mobile property
     */
    public String getMobile() 
    {
        return mobile;     
    }
    
    /**
     * Sets the value of the mobile property.
     * 
     * @param aMobile the new value of the mobile property
     */
    public void setMobile(String aMobile) 
    {
        mobile = aMobile;     
    }
    
    /**
     * Access method for the question property.
     * 
     * @return   the current value of the question property
     */
    public String getQuestion() 
    {
        return question;     
    }
    
    /**
     * Sets the value of the question property.
     * 
     * @param aQuestion the new value of the question property
     */
    public void setQuestion(String aQuestion) 
    {
        question = aQuestion;     
    }
    
    /**
     * Access method for the answer property.
     * 
     * @return   the current value of the answer property
     */
    public String getAnswer() 
    {
        return answer;     
    }
    
    /**
     * Sets the value of the answer property.
     * 
     * @param aAnswer the new value of the answer property
     */
    public void setAnswer(String aAnswer) 
    {
        answer = aAnswer;     
    }
    
    /**
     * Access method for the loginTimes property.
     * 
     * @return   the current value of the loginTimes property
     */
    public int getLoginTimes() 
    {
        return loginTimes;     
    }
    
    /**
     * Sets the value of the loginTimes property.
     * 
     * @param aLoginTimes the new value of the loginTimes property
     */
    public void setLoginTimes(int aLoginTimes) 
    {
        loginTimes = aLoginTimes;     
    }
    
    /**
     * Access method for the country property.
     * 
     * @return   the current value of the country property
     */
    public String getCountry() 
    {
        return country;     
    }
    
    /**
     * Sets the value of the country property.
     * 
     * @param aCountry the new value of the country property
     */
    public void setCountry(String aCountry) 
    {
        country = aCountry;     
    }

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCitycode() {
		return citycode;
	}

	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getUserchs() {
		return userchs;
	}

	public void setUserchs(String userchs) {
		this.userchs = userchs;
	}

	public String getUserPostalcode() {
		return userPostalcode;
	}

	public void setUserPostalcode(String userPostalcode) {
		this.userPostalcode = userPostalcode;
	}
}
