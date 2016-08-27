/**
 * SmsNewOperatoraddsubCodeServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.sany.sms;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.StringHolder;


public class SmsNewOperatoraddsubCodeServiceLocator extends
        org.apache.axis.client.Service implements
        SmsNewOperatoraddsubCodeService {

    public SmsNewOperatoraddsubCodeServiceLocator() {
    }

    public SmsNewOperatoraddsubCodeServiceLocator(
            org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SmsNewOperatoraddsubCodeServiceLocator(java.lang.String wsdlLoc,
            javax.xml.namespace.QName sName)
            throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SmsNewOperatoraddsubCode
    
    private java.lang.String SmsNewOperatoraddsubCode_address = SMSSender.config.getSmsurl();

    public java.lang.String getSmsNewOperatoraddsubCodeAddress() {
        return SmsNewOperatoraddsubCode_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SmsNewOperatoraddsubCodeWSDDServiceName = "SmsNewOperatoraddsubCode";

    public java.lang.String getSmsNewOperatoraddsubCodeWSDDServiceName() {
        return SmsNewOperatoraddsubCodeWSDDServiceName;
    }

    public void setSmsNewOperatoraddsubCodeWSDDServiceName(java.lang.String name) {
        SmsNewOperatoraddsubCodeWSDDServiceName = name;
    }

    public SmsNewOperatoraddsubCode_PortType getSmsNewOperatoraddsubCode()
            throws javax.xml.rpc.ServiceException {
        java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SmsNewOperatoraddsubCode_address);
        } catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSmsNewOperatoraddsubCode(endpoint);
    }

    public SmsNewOperatoraddsubCode_PortType getSmsNewOperatoraddsubCode(
            java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SmsNewOperatoraddsubCodeSoapBindingStub _stub = new SmsNewOperatoraddsubCodeSoapBindingStub(
                    portAddress, this);
            _stub.setPortName(getSmsNewOperatoraddsubCodeWSDDServiceName());
            return _stub;
        } catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSmsNewOperatoraddsubCodeEndpointAddress(
            java.lang.String address) {
        SmsNewOperatoraddsubCode_address = address;
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface)
            throws javax.xml.rpc.ServiceException {
        try {
            if (SmsNewOperatoraddsubCode_PortType.class
                    .isAssignableFrom(serviceEndpointInterface)) {
                SmsNewOperatoraddsubCodeSoapBindingStub _stub = new SmsNewOperatoraddsubCodeSoapBindingStub(
                        new java.net.URL(SmsNewOperatoraddsubCode_address),
                        this);
                _stub.setPortName(getSmsNewOperatoraddsubCodeWSDDServiceName());
                return _stub;
            }
        } catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException(
                "There is no stub implementation for the interface:  "
                        + (serviceEndpointInterface == null ? "null"
                                : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation. If this service has
     * no port for the given interface, then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName,
            Class serviceEndpointInterface)
            throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("SmsNewOperatoraddsubCode".equals(inputPortName)) {
            return getSmsNewOperatoraddsubCode();
        } else {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:SmsNewOperatoraddsubCode",
                "SmsNewOperatoraddsubCodeService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports
                    .add(new javax.xml.namespace.QName(
                            "urn:SmsNewOperatoraddsubCode",
                            "SmsNewOperatoraddsubCode"));
        }
        return ports.iterator();
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(java.lang.String portName,
            java.lang.String address) throws javax.xml.rpc.ServiceException {

        if ("SmsNewOperatoraddsubCode".equals(portName)) {
            setSmsNewOperatoraddsubCodeEndpointAddress(address);
        } else { // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(
                    " Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
     * Set the endpoint address for the specified port name.
     */
    public void setEndpointAddress(javax.xml.namespace.QName portName,
            java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

    public static String sendSms(String content,String tel) throws Exception {
       
           
            // 参数itsm_sms itsm_sms831

            String account = new String(SMSSender.config.getUser()); // 接口账号用户名
            String password = new String(MD5.crypt(SMSSender.config.getPassword()));// 接口密码

            String orgeh_level1 = SMSSender.config.getFirstDeptId();
            String firstDeptId = orgeh_level1; // 一级部门编码 IT总部 50109849
            String secondDeptId = SMSSender.config.getSecondDeptId(); // 二级部门编码
            String thirdDeptId = SMSSender.config.getThirdDeptId(); // 三级部门编码

           
            String subCode = new String(SMSSender.config.getSMS_SUBCODE());

          return sendSms( account,  password,  firstDeptId,  secondDeptId,
          		 thirdDeptId, content, tel, subCode );
    }
    
    public static String sendSms(String account, String password, String firstDeptId, String secondDeptId,
    		String thirdDeptId,String content,String tel,String subCode ) throws Exception {
        try {
            SmsNewOperatoraddsubCodeServiceLocator locator = new SmsNewOperatoraddsubCodeServiceLocator();
            SmsNewOperatoraddsubCode_PortType pt = locator
                    .getSmsNewOperatoraddsubCode();
            // 参数itsm_sms itsm_sms831

          

            MtNewMessage message = new MtNewMessage();
            message.setContent(content); // 短信内容
            message.setPhoneNumber(tel); // 接受短信的号码

           

            StringHolder sendResMsg = new StringHolder();
            StringHolder errMsg = new StringHolder();

            // 发送短信
            try {
                pt.sendSms(account, password, firstDeptId, secondDeptId,
                        thirdDeptId, message, subCode, sendResMsg, errMsg);
            } catch (RemoteException e) {
                throw e;
            }
            // 获取回复短信
            try {
                pt.getSms(account, password, firstDeptId, secondDeptId,
                        thirdDeptId, sendResMsg, errMsg);
            } catch (RemoteException e) {
            	  throw e;
            }

            if (errMsg.value.length() == 0 && sendResMsg != null
                    && sendResMsg.value != null)
                return "sendResMsg:" + sendResMsg.value;
            else if (errMsg.value.equalsIgnoreCase("-1")) {
                return "输入参数不正确，请检查账户，密码，等输入参数是否为空";
            } else if (errMsg.value.equalsIgnoreCase("-2")) {
                return "请检查用户名，密码是否正确及部门名称是否与短信平台匹配";
            } else if (errMsg.value.equalsIgnoreCase("-3")) {
                return "账户已经超过每日发送短信限制数量（当账户被限制每日发送量时有用）";
            } else if (errMsg.value.equalsIgnoreCase("-4")) {
                return "客户端ip地址不正确（当需要ip校验时）";
            } else if (errMsg.value.equalsIgnoreCase("-5")) {
                return "smsId与数据库重复（下发短信时，如果smsId 由客户端传入，该参数不能重复）";
            } else if (errMsg.value.equalsIgnoreCase("-6")) {
                return "内容含有非法关键字，请检查下发内容";
            } else if (errMsg.value.equalsIgnoreCase("-7")) {
                return "对应的号码下发失败，下发号码为空或其他错误，导致该号码发送失败";
            } else if (errMsg.value.equalsIgnoreCase("-8")) {
                return "访问频率过快";
            } else if (errMsg.value.equalsIgnoreCase("-9")) {
                return "提交号码数量超过最大限制";
            }
        } catch (Exception e) {
        	  throw e;
        }
        return "success";
    }
    
    
    public static void main(String[] args) throws Exception {
    	sendSms("短信内容++","13319589069");
//        try {
//            SmsNewOperatoraddsubCodeServiceLocator locator = new SmsNewOperatoraddsubCodeServiceLocator();
//            SmsNewOperatoraddsubCode_PortType pt = locator
//                    .getSmsNewOperatoraddsubCode();
//            // 参数itsm_sms itsm_sms831
//
//            String account = new String("itsm_sms"); // 接口账号用户名
//            String password = new String(MD5.crypt("itsm_sms831"));// 接口密码
//
//            String firstDeptId = new String("50020097"); // 一级部门编码 IT总部 50109849
//            String secondDeptId = new String(""); // 二级部门编码
//            String thirdDeptId = new String(""); // 三级部门编码
//
//            MtNewMessage message = new MtNewMessage();
//            message.setContent("短信内容++"); // 短信内容
//            message.setPhoneNumber("13319589069"); // 接受短信的号码
//
//            String subCode = new String("18");
//
//            StringHolder sendResMsg = new StringHolder();
//            StringHolder errMsg = new StringHolder();
//
//            // 发送短信
//            try {
//                pt.sendSms(account, password, firstDeptId, secondDeptId,
//                        thirdDeptId, message, subCode, sendResMsg, errMsg);
//            } catch (RemoteException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            // 获取回复短信
//            try {
//                pt.getSms(account, password, firstDeptId, secondDeptId,
//                        thirdDeptId, sendResMsg, errMsg);
//            } catch (RemoteException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//
//            if (errMsg.value.length() == 0 && sendResMsg != null
//                    && sendResMsg.value != null)
//                System.out.println("sendResMsg:" + sendResMsg.value);
//            else if (errMsg.value.equalsIgnoreCase("-1")) {
//                System.out.println("输入参数不正确，请检查账户，密码，等输入参数是否为空");
//            } else if (errMsg.value.equalsIgnoreCase("-2")) {
//                System.out.println("请检查用户名，密码是否正确及部门名称是否与短信平台匹配");
//            } else if (errMsg.value.equalsIgnoreCase("-3")) {
//                System.out.println("账户已经超过每日发送短信限制数量（当账户被限制每日发送量时有用）");
//            } else if (errMsg.value.equalsIgnoreCase("-4")) {
//                System.out.println("客户端ip地址不正确（当需要ip校验时）");
//            } else if (errMsg.value.equalsIgnoreCase("-5")) {
//                System.out.println("smsId与数据库重复（下发短信时，如果smsId 由客户端传入，该参数不能重复）");
//            } else if (errMsg.value.equalsIgnoreCase("-6")) {
//                System.out.println("内容含有非法关键字，请检查下发内容");
//            } else if (errMsg.value.equalsIgnoreCase("-7")) {
//                System.out.println("对应的号码下发失败，下发号码为空或其他错误，导致该号码发送失败");
//            } else if (errMsg.value.equalsIgnoreCase("-8")) {
//                System.out.println("访问频率过快");
//            } else if (errMsg.value.equalsIgnoreCase("-9")) {
//                System.out.println("提交号码数量超过最大限制");
//            }
//
//        } catch (ServiceException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }
}
