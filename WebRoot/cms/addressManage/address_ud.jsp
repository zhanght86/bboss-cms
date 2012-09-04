<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.frameworkset.platform.cms.addressmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String addressBookId = request.getParameter("addressBookId");
	String userId = accesscontroler.getUserID();
	String personName = request.getParameter("personName");
	String personBirthday = request.getParameter("personBirthday");
	String personMobiletTel1 = request.getParameter("personMobileTel1");
	String personMobiletTel2 = request.getParameter("personMobileTel2");
	String personQICQ = request.getParameter("personQicq");
	String personFax = request.getParameter("personFax");
	String personWeb = request.getParameter("personWeb");
	String personEmail = request.getParameter("personEmail");
	String personRemark = request.getParameter("personRemark");
	String personHomeTel = request.getParameter("personHomeTel");
	String personPostalCode = request.getParameter("personPostalCode");
	String personHomeAddress = request.getParameter("personHomeAddress");
	String personCompany = request.getParameter("personCompany");
	String personDept = request.getParameter("personDept");
	String personWorkTel = request.getParameter("personWorkTel");
	String personDuty = request.getParameter("personDuty");
	String personCoAddress = request.getParameter("personCoAddress");
	
	Address adr = new Address();
	adr.setAddressBookId(addressBookId);
	adr.setUserId(userId);
	adr.setPersonName(personName);
	adr.setPersonBirthday(personBirthday);
	adr.setPersonMobileTel1(personMobiletTel1);
	adr.setPersonMobileTel2(personMobiletTel2);
	adr.setPersonQicq(personQICQ);
	adr.setPersonFax(personFax);
	adr.setPersonWeb(personWeb);
	adr.setPersonEmail(personEmail);
	adr.setPersonRemark(personRemark);
	adr.setPersonHomeTel(personHomeTel);
	adr.setPersonPostalCode(personPostalCode);
	adr.setPersonHomeAddress(personHomeAddress);
	adr.setPersonCompany(personCompany);
	adr.setPersonDept(personDept);
	adr.setPersonWorkTel(personWorkTel);
	adr.setPersonDuty(personDuty);
	adr.setPersonCoAddress(personCoAddress);
	
	boolean flag = false;
	try{
		flag = new AddressManagerImpl().updateAddress(adr);
	}catch(Exception e){
		e.printStackTrace();
	}
	if(flag){
%>
		<script language="javascript" >
			alert("更新成功!");
			parent.returnValue = true;
			parent.close();
		</script>
<%
	}else{
%>
		<script language="javascript" >
			alert("更新失败!");
		</script>
<%
	}
%>