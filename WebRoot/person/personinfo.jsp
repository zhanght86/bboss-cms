<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl
				,com.frameworkset.platform.config.ConfigManager"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	request.setAttribute("personInfo", RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.info", request));
	request.setAttribute("viewResource", RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.resource.view", request));
	request.setAttribute("viewPost", RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.post.view", request));
	request.setAttribute("viewRole", RequestContextUtils.getI18nMessage("sany.pdp.personcenter.person.role.view", request));
%>
				
<%
	AccessControl accesscontroler = AccessControl.getAccessControl();
    
    String userId = accesscontroler.getUserID();
    
    
   User user = new UserManagerImpl().getUserById(userId);
   //String pwd = EncrpyPwd.decodePassword(user.getUserPassword());
	//user.setUserPassword(pwd);
		
    request.setAttribute("userinfo",user);
    Organization mainorg = accesscontroler.getChargeOrg();
    
    java.util.List secondOrgs = (java.util.List)accesscontroler.getUserObjectAttribute("secondOrgs");
    String mainorgname = "";
    if(mainorg != null)
    {
    	mainorgname = mainorg.getRemark5();
    	
    }
   for(int i = 0; secondOrgs != null && i < secondOrgs.size(); i ++)
    {
   	Organization secondorg = (Organization)secondOrgs.get(i);
   	if(mainorgname.equals(""))
   	{
   		mainorgname = secondorg.getRemark5();
   	}
   	else
  	{
   		mainorgname = "," + secondorg.getRemark5();
   	}
    }
	
	String rootpath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>


<script language="JavaScript">
function ReSizeiFrame(iframe)
{
if(iframe && !window.opera)
{
   //iframe.style.display = "block";
   if(iframe.contentDocument && iframe.contentWindow.document.documentElement.scrollHeight)
   {
    iframe.height = iframe.contentWindow.document.documentElement.scrollHeight;//设置iframe在火狐下高度
   }
   else if (iframe.Document && iframe.Document.body.scrollHeight)
   {
    iframe.height = iframe.Document.body.scrollHeight;//设置iframe在ie下的高度

   }
}
}
function storeUser()
{
	document.UserInfoForm.userSex.disabled=false;
	document.UserInfoForm.userType.disabled=false;
	if(document.UserInfoForm.remark3.checked)document.UserInfoForm.remark3.value='<pg:message code="sany.pdp.common.yes"/>';
	else document.UserInfoForm.remark3.value='<pg:message code="sany.pdp.common.no"/>';
	//if(document.UserInfoForm.istaxmanager.checked)document.UserInfoForm.istaxmanager.value = 1;
	//else document.UserInfoForm.istaxmanager.value = 0;
   	document.UserInfoForm.isvalid.disabled=false;
   	//document.UserInfoForm.istaxmanager.disabled=false;
	document.UserInfoForm.action="<%=rootpath%>/person/userInfoHandle.jsp?action=storeUser";
	UserInfoForm.submit();
	//window.parent.frames[1].location.reload();
}
</SCRIPT>
<title><pg:message code="sany.pdp.personcenter.person.info"/></title>
<tab:tabConfig />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body ENCTYPE="multipart/form-data" style="height: 100%;">
<div class="mcontent">
<sany:menupath menuid="personuserinfomodify"/>
	<tab:tabContainer id="userinfo-bar-container"
		selectedTabPaneId="userInfo" skin="sany">
		<tab:tabPane id="userInfo" tabTitle="${personInfo}">
			<pg:beaninfo requestKey="userinfo">
				<form id="UserInfoForm" method="post" name="UserInfoForm" target="saveInfoFrame">
					<input type="hidden" name="userId"
						value="<pg:cell colName="userId" defaultValue=""/>" />
					<table width="100%" border="0"	cellpadding="0" cellspacing="0" class="table4">
									<tr>
										<th width="20%"><pg:message code="sany.pdp.personcenter.person.loginname"/><input type="hidden" name="userSn" 
											value="<pg:cell colName="userSn" defaultValue=""/>" />
										</th>
										<td width="30%"><input type="text" name="userName"
											value="<pg:cell colName="userName" defaultValue=""/>"
											readonly="true" class="w_70"></td>
										<th width="15%"><pg:message code="sany.pdp.personcenter.person.realname"/></th>
										<td ><input type="text" name="userRealname"
											value="<pg:cell colName="userRealname" defaultValue=""/>"
											labelClass="label" class="w_70" onlyDate="false">
										</td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.view.password"/></th>
										<td><input type="password" name="password"
											value="<pg:cell colName="userPassword" defaultValue=""/>"
											readonly="true" labelClass="label" class="w_70">
										</td>
										<th><pg:message code="sany.pdp.personcenter.person.idcard"/></th>
										<td><input type="text" name="userIdcard"
											value="<pg:cell colName="userIdcard" defaultValue=""/>"
											validator="intNull" cnname="<pg:message code='sany.pdp.personcenter.person.idcard'/>" maxlength="18"
											class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.worktel"/></th>
										<td><input type="text" name="userWorktel"
											value="<pg:cell colName="userWorktel" defaultValue=""/>"
											validator="intNull" cnname="<pg:message code='sany.pdp.personcenter.person.worktel'/>" maxlength="13"
											class="w_70"></td>
										<th><pg:message code="sany.pdp.personcenter.person.sex"/></th>
										<td><dict:select type="sex" name="userSex"
												expression="{userSex}" style="width:70%" /></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.hometel"/></th>
										<td><input type="text" name="homePhone"
											value="<pg:cell colName="userHometel" defaultValue=""/>"
											validator="intNull" cnname="<pg:message code='sany.pdp.personcenter.person.hometel'/>" maxlength="13"
											class="w_70"></td>
										<th><pg:message code="sany.pdp.personcenter.person.email"/></th>
										<td><input type="text" name="mail"
											value="<pg:cell colName="userEmail" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.mobiletel1"/></th>
										<td><input type="text" name="mobile"
											value="<pg:cell colName="userMobiletel1" defaultValue=""/>"
											validator="intNull" cnname="<pg:message code='sany.pdp.personcenter.person.mobiletel1'/>" maxlength="13"
											class="w_70"></td>
										<th><pg:message code="sany.pdp.personcenter.person.mobiletel1.location"/></th>
										<td><input type="text" name="remark4"
											value="<pg:cell colName="remark4" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.mobiletel2"/></th>
										<td><input type="text" name="userMobiletel2"
											value="<pg:cell colName="userMobiletel2" defaultValue=""/>"
											validator="intNull" cnname="<pg:message code='sany.pdp.personcenter.person.mobiletel2'/>" maxlength="13"
											class="w_70"></td>
										<th><pg:message code="sany.pdp.personcenter.person.mobiletel2.location"/></th>
										<td><input type="text" name="remark5"
											value="<pg:cell colName="remark5" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.organization"/></th>
										<td><input type="text" name="ou"
											value="<%=mainorgname%>" readonly="true"
											labelClass="label" class="w_70"></td>
										<th><pg:message code="sany.pdp.personcenter.person.pinyin"/></th>
										<td><input type="text" name="userPinyin"
											value="<pg:cell colName="userPinyin" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.usertype"/></th>
										<td><dict:select type="userType" name="userType"
												disabled="true" expression="{userType}"
												style="width:70%" /></td>
										<th><pg:message code="sany.pdp.personcenter.person.postalCode"/></th>
										<td><input type="text" name="postalCode"
											maxlength="6"
											value="<pg:cell colName="userPostalcode" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.fax"/></th>
										<td><input type="text" name="userFax"
											value="<pg:cell colName="userFax" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
										<th>OICQ</th>
										<td><input type="text" name="userOicq"
											maxlength="16"
											value="<pg:cell colName="userOicq" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.birthday"/></th>
										<td><input type="text" name="userBirthday"
											value="<pg:cell colName="userBirthday" defaultValue="" />"
											class="Wdate" onclick="WdatePicker()"
											readonly="true" labelClass="label" class="w_70">
										</td>
										<th><pg:message code="sany.pdp.personcenter.person.address"/></th>
										<td><input type="text" name="userAddress"
											value="<pg:cell colName="userAddress" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.logincount"/></th>
										<td><input type="text" name="userLogincount"
											value="<pg:cell colName="userLogincount" defaultValue="0"/>"
											readonly="true" labelClass="label" class="w_70">
										</td>
										<th><pg:message code="sany.pdp.personcenter.person.isvalid"/></th>
										<td><dict:select type="isvalid" disabled="true"
												name="isvalid" expression="{userIsvalid}"
												style="width:70%" /></td>
									</tr>



									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.regdate"/></th>
										<td><input type="text" name="userRegdate"
											value="<pg:cell colName="userRegdate" defaultValue=""/>"
											readonly="true" labelClass="label" class="w_70">
										</td>
										<th><pg:message code="sany.pdp.personcenter.person.shortmobile"/></th>
										<td><input type="text" name="shortMobile"
											value="<pg:cell colName="remark2" defaultValue=""/>"
											labelClass="label" class="w_70"></td>
									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.secrecy"/></th>
										<td colspan="3"><input type="checkbox"
											name="remark3" style="border: 0;background: none;"
											value="<pg:cell colName="remark3" defaultValue=""/>"
											<pg:equal colName="remark3" value="<pg:message code='sany.pdp.common.yes'/>">checked</pg:equal>>
										</td>

									</tr>
									<tr>
										<th><pg:message code="sany.pdp.personcenter.person.worknumber"/></th>
										<td><input type="text"
											name="userWorknumber"
											value="<pg:cell colName="userWorknumber"  defaultValue="" />"
											validator="stringNull" cnname="<pg:message code='sany.pdp.personcenter.person.worknumber'/>" maxlength="40"
											readOnly=true class="w_70"></td>
										<td></td><td></td>

									</tr>
								</table>
					<div class="btnarea" >
								<a href="javascript:void(0)" class="bt_1" onclick="storeUser()"><span><pg:message code="sany.pdp.common.ok"/></span></a> 
								<a href="javascript:void(0)"	class="bt_2" onclick="UserInfoForm.reset();"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
					</div>	
					<table width="100%" align="center" border="0" cellpadding="0"
						cellspacing="0">
						<IFRAME name="saveInfoFrame" id="saveInfoFrame" height="0"
							width="0"></IFRAME>
					</table>
				</form>
			</pg:beaninfo>
		</tab:tabPane>
		<tab:tabPane id="res_list" tabTitle="${viewResource}" lazeload="true">
			<% String temppath = rootpath+"/sysmanager/resmanager/res_queryframe.jsp?isUserRes=is&typeName=USER" ;%>
			<tab:iframe id="res_list_iframe" src="<%=temppath%>" frameborder="0"
				width="100%" height="100%"></tab:iframe>
		</tab:tabPane>

		<%
	if(ConfigManager.getInstance().getConfigBooleanValue("enablejobfunction", false))
	{
	%>
		<tab:tabPane id="job_list" tabTitle="${viewPost}"  lazeload="true">
			<% String temppath = rootpath+"/person/userjob_list.jsp" ;%>
			<tab:iframe id="job_list_iframe" src="<%=temppath%>" frameborder="0" 
				width="100%" height="100%" ></tab:iframe>
		</tab:tabPane>
		<%
	}
	%>

		<tab:tabPane id="role_list" tabTitle="${viewRole}" lazeload="true">
			<% String temppath = rootpath+"/person/userrole_do.jsp" ;%>
			<tab:iframe id="role_list_iframe" src="<%=temppath%>" frameborder="0"
				width="100%" height="100%" ></tab:iframe>
		</tab:tabPane>

	</tab:tabContainer>


</div>
</body>
</html>

