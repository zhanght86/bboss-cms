/**
 * SmsNewOperatoraddsubCode_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sany.sms;

public interface SmsNewOperatoraddsubCode_PortType extends java.rmi.Remote {
    public void sendSms(java.lang.String account, java.lang.String password, java.lang.String firstDeptId, java.lang.String secondDeptId, java.lang.String thirdDeptId, MtNewMessage message, java.lang.String subCode, javax.xml.rpc.holders.StringHolder sendResMsg, javax.xml.rpc.holders.StringHolder errMsg) throws java.rmi.RemoteException;
    public void getSms(java.lang.String account, java.lang.String password, java.lang.String firstDeptId, java.lang.String secondDeptId, java.lang.String thirdDeptId, javax.xml.rpc.holders.StringHolder resMsg, javax.xml.rpc.holders.StringHolder errMsg) throws java.rmi.RemoteException;
    public void getReport(java.lang.String account, java.lang.String password, java.lang.String firstDeptId, java.lang.String secondDeptId, java.lang.String thirdDeptId, MtNewMessage[] message, javax.xml.rpc.holders.StringHolder reportMsg, javax.xml.rpc.holders.StringHolder errMsg) throws java.rmi.RemoteException;
    public void batchSendSms(java.lang.String account, java.lang.String password, java.lang.String firstDeptId, java.lang.String secondDeptId, java.lang.String thirdDeptId, MtNewMessage[] messages, java.lang.String subCode, javax.xml.rpc.holders.StringHolder sendResMsg, javax.xml.rpc.holders.StringHolder errMsg) throws java.rmi.RemoteException;
}
