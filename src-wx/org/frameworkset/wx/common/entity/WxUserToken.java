package org.frameworkset.wx.common.entity;

public class WxUserToken {
	private String userAccount;
	private String errorcode;
	private String errormsg;
	public WxUserToken() {
		// TODO Auto-generated constructor stub
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

}
