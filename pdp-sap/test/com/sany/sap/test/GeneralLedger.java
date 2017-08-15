/*
 * @(#)AppBom.java
 * 
 * Copyright @ 2001-2011 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于bbossgroups有限公司机密的和私有信息，不得泄露。
 * 并且只能由bbossgroups有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.sany.sap.test;

import java.util.Date;

public class GeneralLedger {
	private String id;
	private String logId;
	private String docNo;
	private Date docDate;
	private Date pstngDate;
	private String period;
	private String compCode;
	private String docType;
	private String currency;
	private Double exchRate;
	private String ldgrp;
	private String refDocNo;
	private Integer numPage;
	private String headerTxt;
	private String userName;
	private String newBS;
	private String hkont;
	private String umskz;
	private Integer transType;
	private Double amtDocCur;
	private String costCenter;
	private String orderId;
	private String salesOrd;
	private String paoBjnr;
	private String busArea;
	private String funcArea;
	private String profitCtr;
	private String allocNmbr;
	private Date blineDate;
	private String assetNo;
	private String itemText;
	private String partPrctr;
	private String tradeId;
	private String append;
	private String refKey1;
	private String refKey2;
	private String refKey3;
	
	private String createUser;
	private Date createDate;
	private String fileName;
	private String errorMessages;
	private String itemNoAcc;
	private String busAct ="RFBU";
	private String spglind;
	private String voucherNo;
	
	public String getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(String errorMessages) {
		this.errorMessages = errorMessages;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getDocDate() {
		return docDate;
	}

	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}

	public Date getPstngDate() {
		return pstngDate;
	}

	public void setPstngDate(Date pstngDate) {
		this.pstngDate = pstngDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getExchRate() {
		return exchRate;
	}

	public void setExchRate(Double exchRate) {
		this.exchRate = exchRate;
	}

	public String getLdgrp() {
		return ldgrp;
	}

	public void setLdgrp(String ldgrp) {
		this.ldgrp = ldgrp;
	}

	public String getRefDocNo() {
		return refDocNo;
	}

	public void setRefDocNo(String refDocNo) {
		this.refDocNo = refDocNo;
	}

	public Integer getNumPage() {
		return numPage;
	}

	public void setNumPage(Integer numPage) {
		this.numPage = numPage;
	}

	public String getHeaderTxt() {
		return headerTxt;
	}

	public void setHeaderTxt(String headerTxt) {
		this.headerTxt = headerTxt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNewBS() {
		return newBS;
	}

	public void setNewBS(String newBS) {
		this.newBS = newBS;
	}

	public String getHkont() {
		return hkont;
	}

	public void setHkont(String hkont) {
		this.hkont = hkont;
	}

	public String getUmskz() {
		return umskz;
	}

	public void setUmskz(String umskz) {
		this.umskz = umskz;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public Double getAmtDocCur() {
		return amtDocCur;
	}

	public void setAmtDocCur(Double amtDocCur) {
		this.amtDocCur = amtDocCur;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSalesOrd() {
		return salesOrd;
	}

	public void setSalesOrd(String salesOrd) {
		this.salesOrd = salesOrd;
	}

	public String getPaoBjnr() {
		return paoBjnr;
	}

	public void setPaoBjnr(String paoBjnr) {
		this.paoBjnr = paoBjnr;
	}

	public String getBusArea() {
		return busArea;
	}

	public void setBusArea(String busArea) {
		this.busArea = busArea;
	}

	public String getFuncArea() {
		return funcArea;
	}

	public void setFuncArea(String funcArea) {
		this.funcArea = funcArea;
	}

	public String getProfitCtr() {
		return profitCtr;
	}

	public void setProfitCtr(String profitCtr) {
		this.profitCtr = profitCtr;
	}

	public String getAllocNmbr() {
		return allocNmbr;
	}

	public void setAllocNmbr(String allocNmbr) {
		this.allocNmbr = allocNmbr;
	}

	public Date getBlineDate() {
		return blineDate;
	}

	public void setBlineDate(Date blineDate) {
		this.blineDate = blineDate;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

	public String getItemText() {
		return itemText;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	public String getPartPrctr() {
		return partPrctr;
	}

	public void setPartPrctr(String partPrctr) {
		this.partPrctr = partPrctr;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getAppend() {
		return append;
	}

	public void setAppend(String append) {
		this.append = append;
	}

	public String getRefKey1() {
		return refKey1;
	}

	public void setRefKey1(String refKey1) {
		this.refKey1 = refKey1;
	}

	public String getRefKey2() {
		return refKey2;
	}

	public void setRefKey2(String refKey2) {
		this.refKey2 = refKey2;
	}

	public String getRefKey3() {
		return refKey3;
	}

	public void setRefKey3(String refKey3) {
		this.refKey3 = refKey3;
	}
	public String getItemNoAcc() {
		return itemNoAcc;
	}
	public void setItemNoAcc(String itemNoAcc) {
		this.itemNoAcc = itemNoAcc;
	}
	
	public String getBusAct() {
		return busAct;
	}

	public void setBusAct(String busAct) {
		this.busAct = busAct;
	}

	public String getSpglind() {
		return spglind;
	}

	public void setSpglind(String spglind) {
		this.spglind = spglind;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	
}
