package com.sany.workflow.business.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 统一业务单实体bean
 *
 * @author luoh19
 * @version 2015年1月21日,v1.0
 */
public class CommonBusinessBean implements Serializable{
    
    private static final long serialVersionUID = 2352476725676555346L;

    private String orderId;// 业务ID

    private String orderNo;// 业务单号

    private String orderType;// 业务类型

    private String orderTypeName;// 业务类型名称

    private String orderState;// 业务订单状态

    private String orderStateName;// 业务订单状态名称
    
    private String orderParam;

    private String applyPerson; // 申请人

    private String applyPersonName;

    private String applyPersonWorknumber;

    private String createPerson; // 创建人

    private String createPersonName;

    private String createPersonWorknumber;

    private String processId; // 流程ID

    private String processKey; // 流程key

    private String title; // 业务内容

    private String dealTitle; // 处理内容

    private String dealState;// 处理状态

    private String dealStateName;

    private String dealPerson; // 当前处理人【流程冗余】

    private String dealPersonName;

    private String dealPersonWorknumber;    

    private String approvePerson; // 审批人【流程冗余】

    private String approvePersonName;

    private String approvePersonWorknumber;

    private Date createDatetime; // 创建时间

    private Date limitDatetime; // 处理期限
    
    private Date modifyDatetime; // 修改时间

    private Date endDatetime; // 结束时间
    
    public String getOrderId() {
        return orderId;
    }

    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    
    public String getOrderNo() {
        return orderNo;
    }

    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    
    public String getOrderType() {
        return orderType;
    }

    
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    
    public String getOrderTypeName() {
        return orderTypeName;
    }

    
    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    
    public String getOrderState() {
        return orderState;
    }

    
    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    
    public String getOrderStateName() {
        return orderStateName;
    }

    
    public void setOrderStateName(String orderStateName) {
        this.orderStateName = orderStateName;
    }

    
    public String getApplyPerson() {
        return applyPerson;
    }

    
    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    
    public String getApplyPersonName() {
        return applyPersonName;
    }

    
    public void setApplyPersonName(String applyPersonName) {
        this.applyPersonName = applyPersonName;
    }

    
    public String getApplyPersonWorknumber() {
        return applyPersonWorknumber;
    }

    
    public void setApplyPersonWorknumber(String applyPersonWorknumber) {
        this.applyPersonWorknumber = applyPersonWorknumber;
    }

    
    public String getCreatePerson() {
        return createPerson;
    }

    
    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    
    public String getCreatePersonName() {
        return createPersonName;
    }

    
    public void setCreatePersonName(String createPersonName) {
        this.createPersonName = createPersonName;
    }

    
    public String getCreatePersonWorknumber() {
        return createPersonWorknumber;
    }

    
    public void setCreatePersonWorknumber(String createPersonWorknumber) {
        this.createPersonWorknumber = createPersonWorknumber;
    }

    
    public String getProcessId() {
        return processId;
    }

    
    public void setProcessId(String processId) {
        this.processId = processId;
    }

    
    public String getProcessKey() {
        return processKey;
    }

    
    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    
    public String getTitle() {
        return title;
    }

    
    public void setTitle(String title) {
        this.title = title;
    }

    
    public String getDealTitle() {
        return dealTitle;
    }

    
    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    
    public String getDealState() {
        return dealState;
    }

    
    public void setDealState(String dealState) {
        this.dealState = dealState;
    }

    
    public String getDealStateName() {
        return dealStateName;
    }

    
    public void setDealStateName(String dealStateName) {
        this.dealStateName = dealStateName;
    }
    
    public String getDealPerson() {
        return dealPerson;
    }

    
    public void setDealPerson(String dealPerson) {
        this.dealPerson = dealPerson;
    }

    
    public String getDealPersonName() {
        return dealPersonName;
    }

    
    public void setDealPersonName(String dealPersonName) {
        this.dealPersonName = dealPersonName;
    }

    
    public String getDealPersonWorknumber() {
        return dealPersonWorknumber;
    }

    
    public void setDealPersonWorknumber(String dealPersonWorknumber) {
        this.dealPersonWorknumber = dealPersonWorknumber;
    }

    
    public String getApprovePerson() {
        return approvePerson;
    }

    
    public void setApprovePerson(String approvePerson) {
        this.approvePerson = approvePerson;
    }

    
    public String getApprovePersonName() {
        return approvePersonName;
    }

    
    public void setApprovePersonName(String approvePersonName) {
        this.approvePersonName = approvePersonName;
    }

    
    public String getApprovePersonWorknumber() {
        return approvePersonWorknumber;
    }

    
    public void setApprovePersonWorknumber(String approvePersonWorknumber) {
        this.approvePersonWorknumber = approvePersonWorknumber;
    }

    
    public Date getCreateDatetime() {
        return createDatetime;
    }

    
    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getOrderParam() {
        return orderParam;
    }


    public void setOrderParam(String orderParam) {
        this.orderParam = orderParam;
    }


	public Date getLimitDatetime() {
		return limitDatetime;
	}


	public void setLimitDatetime(Date limitDatetime) {
		this.limitDatetime = limitDatetime;
	}


	public Date getModifyDatetime() {
		return modifyDatetime;
	}


	public void setModifyDatetime(Date modifyDatetime) {
		this.modifyDatetime = modifyDatetime;
	}


	public Date getEndDatetime() {
		return endDatetime;
	}


	public void setEndDatetime(Date endDatetime) {
		this.endDatetime = endDatetime;
	}
}
