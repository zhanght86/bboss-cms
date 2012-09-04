<%@page contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="com.frameworkset.platform.cms.container.Template" %>
<%@page import="java.util.*" %>
<%@page import="java.util.*" %>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.*" %>
<%@page import="com.frameworkset.platform.sysmgrcore.web.struts.form.*" %>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.*" %>
<%@page import="com.frameworkset.platform.sysmgrcore.exception.*" %>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	//AccessControl control = AccessControl.getInstance();
	//control.checkAccess(request,response);	
	response.setHeader("Cache-Control", "no-cache"); 
   response.setHeader("Pragma", "no-cache"); 
   response.setDateHeader("Expires", -1);  
    response.setDateHeader("max-age", 0);
	UserInfoForm user = (UserInfoForm)request.getAttribute("currUser");
	int i = 0;
%>
<html>
<head>
	<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
	<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<SCRIPT language="JavaScript" SRC="../../sysmanager/user/validateForm.js"></SCRIPT>
	<script src="../inc/js/func.js"></script>
	<tab:tabConfig/>
	<title>用户信息修改</title>

<script language="javascript">
function storeUser()
{	
		if(document.UserInfoForm.userIdcard.value != "")
	    {
	        	var value = document.UserInfoForm.userIdcard.value ;
	        	
	        	if(!/(^[^\$\*&'=<#>%\^@~\s]+$)|(^$)/.test(value))
	        	{
	        		alert("按要求输入 “身份证号码”的数据！\n数据必须为：合法的字符串。除『$*&'=<#>%^@~ 』");
	        		return false ;
	        	}
	        
	        	var len = document.UserInfoForm.userIdcard.value.length;
	        	if(len != 15 && len != 18)
	        	{
	        		alert("身份证号码的位数只能是15位或18位!");
	        		return false ;
	        	}
	    }
	    
		if (validateForm(UserInfoForm))
		{
			document.UserInfoForm.userType.disabled=false;
		  	if(document.UserInfoForm.remark3.checked)
	    		document.UserInfoForm.remark3.value="是";
			else document.UserInfoForm.remark3.value="否";   	
			document.UserInfoForm.target = "saveuser";
			document.UserInfoForm.action="../userManage/userManager.do?method=updateUser";
			document.UserInfoForm.submit();	
		}
}

function trangePassword()
{
		var passWord_value=document.UserPasswordUpdate.newPassword1.value;
      if(document.UserPasswordUpdate.oldPassword.value.length<1){
          alert("请输入旧密码！");
          return;
      }
              
      if(document.UserPasswordUpdate.newPassword1.value.length<1){
   		   alert("新密码不能为空，请重新输入！");
   		   return;
        }
      if(document.UserPasswordUpdate.newPassword2.value.length<1){
   		   alert("请确认新密码！");
   		   return;
        }  
     	if(passWord_value.search(/[\\\/\|:\*\?<>"']/g)!=-1)
		{
			alert("密码不能有\\/|:*?<>\"'!等特殊字符");
			return false;
		}
      if(document.UserPasswordUpdate.newPassword1.value!=document.UserPasswordUpdate.newPassword2.value){
   		   alert("新密码两次输入不相等,请重新输入！");
   		   return;
        }
      if(document.UserPasswordUpdate.userPassword.value!=document.UserPasswordUpdate.oldPassword.value){
          alert("旧密码不对！");
          return
      }  
      
      if (validateForm(UserInfoForm) ){
	      document.UserPasswordUpdate.target = "saveuser";      
	      document.UserPasswordUpdate.action="../userManage/userManager.do?method=updateUserPassword";
	      document.UserPasswordUpdate.submit();	           
      }
}  
	function back(){
		window.close();
	}
</script>
</head>
<body  bgcolor="#F7F8FC" >

	
		<tab:tabContainer id="user-info-update" selectedTabPaneId="user-baseInfo">
			<tab:tabPane id="user-baseInfo" tabTitle="用户基本信息设置">
			    <form name="UserInfoForm" method="post">
					<pg:beaninfo requestKey="currUser">
						<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
						<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
						<input type="hidden" name="userName" value="<pg:cell colName="userName"  defaultValue=""/>" />
						<input type="hidden" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>" />
						<input type="hidden" name="userIsvalid" value="<pg:cell colName="userIsvalid"  defaultValue=""/>" />
						<input type="hidden" name="userRegdate" value="<pg:cell colName="userRegdate"  defaultValue=""/>" />						
						<input type="hidden" name="dredgeTime" value="<pg:cell colName="dredgeTime"  defaultValue="" />" />
						<input type="hidden" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue=""/>" />
						<input type="hidden" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>" />
		
						<table width="80%" border="0" cellpadding="0" cellspacing="0" class=""  align=center>
							<tr>
								<td height="25" class="detailtitle"  colspan="2" align=left>
										登陆名:			
										<pg:cell colName="userName"  defaultValue=""/> 
								</td>						
								<td height="25" colspan="2" align=left>
								</td>				
								</tr>
							
							<tr>						
								<td height="25" class="detailtitle">
											 真实名称: </td>
								<td height="25">
									<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100"><span class="STYLE1">*</span>	
								</td>
								<td height="25" class="detailtitle">
											 身份证号码:</td>
									<td height="25">
									<input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>"  maxlength="18">
								</td>					
							</tr>
							
							<tr>
										<td height="25" class="detailtitle">
											 单位电话:</td>						
								<td height="25">
									<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"  validator="intNull" cnname="单位电话" maxlength="13"></td>
								<td height="25" class="detailtitle">
											 性别:</td>
								<td height="25">					  					    
									    <dict:select type="sex" name="userSex" expression="{userSex}" />					    
								</tr>
							
							<tr>
										<td height="25" class="detailtitle">
											 家庭电话:</td>
								<td height="25">
											<input type="text" name="homePhone" value="<pg:cell colName="homePhone"  defaultValue=""/>"  validator="intNull" cnname="家庭电话" maxlength="13"></td>				
								<td height="25" class="detailtitle">
											 电子邮件:</td>
								<td height="25">
											<input type="text" name="mail" value="<pg:cell colName="mail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40"></td>
							</tr>
							
							<tr>
								<td height="25" class="detailtitle">
											 移动电话1:
										</td>
								<td height="25">
											<input type="text" name="mobile" value="<pg:cell colName="mobile"  defaultValue=""/>" validator="intNull" cnname="移动电话" maxlength="13"></td>
								<td height="25" class="detailtitle"> 移动电话1归属地:</td>
								<td height="25">
											<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100"></td>
							</tr>
							
							<tr><td height="25" class="detailtitle"> 移动电话2</td>
								<td height="25">
									<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" validator="intNull" cnname="移动电话2" maxlength="13"></td>
								<td height="25" class="detailtitle"> 移动电话2归属地:</td>
								<td height="25">
											<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100"></td>
						   </tr>
						   <tr>
						   <td height="25" class="detailtitle"> 组织机构:</td>
								<td height="25">
											<input type="text" readonly="true" name="ou" value="<pg:cell colName="ou"  defaultValue=""/>" validator="stringNull" cnname="组织机构" maxlength="100"></td>
						   <td height="25" class="detailtitle"> 拼音:</td>
								<td height="25">
											<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" validator="stringNull" cnname="拼音" maxlength="100"></td>
					       </tr>   
						   <tr><td height="25" class="detailtitle"> 用户类型:</td>
							   <td height="25">
									<dict:select type="userType" name="userType" expression="{userType}" />
							   </td>
							   <td height="25" class="detailtitle"> 邮政编码:</td>
							   <td height="25">
											<input type="text" name="postalCode" value="<pg:cell colName="postalCode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7"></td>	
						   </tr>
									<tr>
										<td height="25" class="detailtitle">
											 传真:
										</td>
										<td height="25">
											<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="intNull" cnname="传真" maxlength="40">
										</td>
										<td height="25" class="detailtitle">
											 OICQ:
										</td>
										<td height="25">
											<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13">
										</td>
									</tr>
									<tr>
										<td height="25" class="detailtitle">
											 生日:
										</td>
										<td height="25">
											<input type="text" name="userBirthday" onclick="showdate(document.all('userBirthday'))" readonly="true" value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40">
										</td>
										<td height="25" class="detailtitle">
											 用户地址:
										</td>
										<td height="25">
											<input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" validator="stringNull" cnname="用户地址" maxlength="200">
										</td>
									</tr>
									<tr>
										<td height="25" class="detailtitle">
											 登录次数:
										
										</td>
										<td height="25">
											 <pg:cell colName="userLogincount"  defaultValue="0"/>
										</td>
										<td height="25" class="detailtitle">
											 用户排序号:
										
										</td>
										<td height="25">
											<input type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="" />" validator="intNull" cnname="用户排序号" maxlength="40">
										</td>
									</tr>
									<tr>
										<td height="25" class="detailtitle">
											 </STRONG>手机短号码:
										
										</td>
										<td height="25">
											<input type="text" name="shortMobile" value="<pg:cell colName="shortMobile"  defaultValue=""/>" validator="intNull"  cnname="手机短号码" maxlength="40">
										</td>
										<td height="25" class="detailtitle">
											
										</td>
										<td height="25">
										</td>
									</tr>
									<tr>
									<td height="25" class="detailtitle" colspan="4">
								
										<input type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal> value="on">
										个人信息是否保密
									
									</td>	
									</tr>	
									<tr>
										<td height="25" colspan="4" class="detailtitle" >当前状态:
										
										    <pg:equal colName="userIsvalid" value="0">已删除</pg:equal>
											<pg:equal colName="userIsvalid" value="1">新申请</pg:equal>
											<pg:equal colName="userIsvalid" value="2">已开通</pg:equal>
											<pg:equal colName="userIsvalid" value="3">停用</pg:equal>							
										</td>						   				
									</tr>
									<tr>
									  <td height="25" colspan="4" class="detailtitle" >
											注册日期:
											<pg:cell colName="userRegdate"  defaultValue="" />
										</td>
									</tr>
									<tr>
									  <td height="25" colspan="4" class="detailtitle" >
										开通日期:
										<pg:cell colName="dredgeTime"  defaultValue="" />
										</td>
									</tr>			
						</table>
		  		</pg:beaninfo>
		</form>

		<table width="80%"  cellpadding="2" cellspacing="0" align="center">
			<tr>
				<td>

				<div align="center">
					<input name="resave" type="button" class="cms_button" value="保存" onClick="storeUser()">
					<input name="resave" type="button" class="cms_button" value="关闭" onClick="back()">									
				</div>
						
				</td>
			</tr>
		</table>
	</tab:tabPane>
	
	
<!--	tab ---------------------------------------------------------------------------------------------------->
	<tab:tabPane id="user-password-update" tabTitle="用户密码修改">
	 <form name="UserPasswordUpdate" method="post">
	   <pg:beaninfo requestKey="currUser">
	      <input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />		
		  <input type="hidden" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>" />
	   </pg:beaninfo>
	  <table width="50%" border="0" cellpadding="3" cellspacing="0" align='center'>
	     <tr>
	        <td height='30' class="tablecells" nowrap="nowrap">
	            旧密码：
	        </td>
	        <td height='25' class="tablecells" nowrap="nowrap"> 
			   <INPUT type="password" name="oldPassword" maxlength="20" />
			</td>
	     </tr>
	     <tr>
	        <td height='30' class="tablecells" nowrap="nowrap">
	          新密码：
	        </td>
	        <td height='25' class="tablecells" nowrap="nowrap">  
				<INPUT type="password" name="newPassword1" maxlength="20" />(1-20位有效字符)
			</td>
	     </tr>
	     <tr>
	        <td height='30' class="tablecells" nowrap="nowrap">
	          新密码确认：
	        </td>
	        <td height='25' class="tablecells" nowrap="nowrap">  
				<INPUT type="password" name="newPassword2" maxlength="20" />(1-20位有效字符)
			</td>
	     </tr>
	   	
	      <tr></tr> 
	      <tr></tr>  
	     <tr>
	        <td colspan=2 align='center' height='25' class="tablecells" nowrap="nowrap">
				<input name="ok" type="button" class="cms_button" value="确定" onClick="trangePassword()">
				<input name="resave" type="button" class="cms_button" value="关闭" onClick="back()">
			</td>
	     </tr>
	  </table>
	 </form>
	</tab:tabPane>
  </tab:tabContainer>	
  <iframe name="saveuser" width="0" height="0"></iframe>
</body>
</html>