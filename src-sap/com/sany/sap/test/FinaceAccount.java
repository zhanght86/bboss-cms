/*
 * @(#)AppBom.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
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
package com.sany.sap.test;


public class FinaceAccount {
	  private String id ;	  
	  private String docNo;        //清帐凭证 docno
	  private String compCode;     //公司代码 compcode
	  private String accountYear;  //会计年度 accountyear
	  private String logId;

	  private String userName;     //用户名
	  private String createDate;   //创建时间
	  private Integer fileName;    
		  
	  public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDocNo() {
		return docNo;
	}
	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}
	public String getCompCode() {
		return compCode;
	}
	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}
	public String getAccountYear() {
		return accountYear;
	}
	public void setAccountYear(String accountYear) {
		this.accountYear = accountYear;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public Integer getFileName() {
		return fileName;
	}
	public void setFileName(Integer fileName) {
		this.fileName = fileName;
	}
	  public String getLogId() {
			return logId;
		}
		public void setLogId(String logId) {
			this.logId = logId;
		}
	  
}
