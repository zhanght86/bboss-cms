
<%@page import="com.frameworkset.platform.config.ConfigManager"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@	page import="java.util.*,
				com.frameworkset.platform.sysmgrcore.entity.*,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.security.authentication.EncrpyPwd,
				com.frameworkset.platform.security.*,
				java.sql.Date"%>
<%	
	//action start
			AccessControl control = AccessControl.getInstance();
			control.checkManagerAccess(request,response);
			//是否出现  "是否税管员" 复选框
			boolean istaxmanager = ConfigManager.getInstance().getConfigBooleanValue("istaxmanager", false);
			UserManager userManager = SecurityDatabase.getUserManager();
			String userId=request.getParameter("userId");
			
			User user = new User();
			user = userManager.getUserById(userId);
			String userName = user.getUserName();
			
			//保存  更新操作
			if(request.getParameter("isSave") != null)
			{
				
				user.setUserPassword(request.getParameter("userPassword"));
				user.setUserRealname(request.getParameter("userRealname"));
				user.setUserSn(new Integer(request.getParameter("userSn")));
				
				if(!"NaN".equals(request.getParameter("userSex")))
					user.setUserSex(request.getParameter("userSex"));
				String userIsvalid =  request.getParameter("userIsvalid");
				if(userIsvalid != null && !"NaN".equals(userIsvalid))
				{
					try
					{
						user.setUserIsvalid(new Integer(userIsvalid));
					}
					catch(Exception e)
					{
					   user.setUserIsvalid(new Integer(2)) ;
					}
				}
				
				user.setUserHometel(request.getParameter("homePhone"));
				user.setUserMobiletel1(request.getParameter("mobile"));
				user.setUserPostalcode(request.getParameter("postalCode"));
				user.setRemark2(request.getParameter("shortMobile"));
				user.setUserEmail(request.getParameter("mail"));
				user.setUserMobiletel2(request.getParameter("userMobiletel2"));
				user.setRemark1(request.getParameter("remark1"));
				user.setRemark3(request.getParameter("remark3"));
				user.setRemark4(request.getParameter("remark4"));
				user.setRemark5(request.getParameter("remark5"));
				
				if(!"NaN".equals(request.getParameter("userType")))
				{
					user.setUserType(request.getParameter("userType"));
				}
	
				user.setUserPinyin(request.getParameter("userPinyin"));
	
				user.setUserWorktel(request.getParameter("userWorktel"));
	
				user.setUserFax(request.getParameter("userFax"));
	
				user.setUserOicq(request.getParameter("userOicq"));
				
				if (request.getParameter("userBirthday") != null
						&& request.getParameter("userBirthday").length() > 0)
				{
					user.setUserBirthday(Date.valueOf(request.getParameter("userBirthday")));
				}
	
				user.setUserAddress(request.getParameter("userAddress"));
	
				user.setUserIdcard(request.getParameter("userIdcard"));
				if (request.getParameter("userRegdate") != null
						&& request.getParameter("userRegdate").length() > 0)
				{
					user.setUserRegdate(Date.valueOf(request.getParameter("userRegdate")));
				}
				
				if(request.getParameter("istaxmanager")==null)
				{
					user.setIstaxmanager(0);
				}
				else
				{
					user.setIstaxmanager(Integer.valueOf(request.getParameter("istaxmanager")).intValue());
				}
				userManager.updateUser(user);
				
			}else
			{
				user = userManager.getUserById(userId);
			}
			
			//密码恢复
			if(request.getParameter("dispersePwd") != null)
			{
				user.setUserPassword("123456");
				userManager.updateUserPassword(user);
			}
	//action end
		
	String qstring = request.getParameter("qstring");
	if(qstring == null)
	{
		qstring  = (String)request.getAttribute("qstring");
	}
	
	if(qstring == null)
	{
		qstring = "";
	}
	String qqstring = java.net.URLEncoder.encode(qstring);

	try
	{
		String pwd = EncrpyPwd.decodePassword(user.getUserPassword());
		user.setUserPassword(pwd);
	}
	catch(Exception e)
	{
		
	}
	request.setAttribute("currUser",user);
%>

<html>
	<head>
		<title>离散用户【<%=userName%>】基本信息</title>
		<base target="_self">
		<script language="JavaScript" src="../../scripts/calender_date.js" ></script>
		<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>
		<script language="javascript" src="../../scripts/selectTime.js"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		
	</head>

<script language="JavaScript">

var q = "<%=qstring%>";
function trim(string){
  var temp="";
  string = ''+string;
  splitstring = string.split(" ");
  for(i=0;i<splitstring.length;i++){
    temp += splitstring[i];
  } 
  return temp;
 }
//离散用户保存成功提示
var isSave = "<%=request.getParameter("isSave")%>";
if(isSave == "true"){
	alert("<pg:message code='sany.pdp.common.operation.success'/>");
}
//离散用户密码恢复提示
var dispersePwd = "<%=request.getParameter("dispersePwd")%>";
if(dispersePwd == "2"){
	alert("<pg:message code='sany.pdp.common.operation.success'/>");
}

function storeUser1()
{

	document.userInfo.userName.disabled = false;
	
	
	if (validateForm(userInfo))
	{

		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
		
    		$.dialog.alert("<pg:message code='sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.loginname.null'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
    		return false;
    	} 
    	
	    if(document.userInfo.remark3.checked){
	    	document.userInfo.remark3.value="是";
	    }
		else{ 
			document.userInfo.remark3.value="否";
		}	
	    document.userInfo.target="saveDisperse";
		document.userInfo.action="userInfo_handle.jsp?isSave=true";
		document.userInfo.submit();
		document.userInfo.userName.disabled = true;
		
	}

}

function resetbutton()
{
}

 function goback(e,userId)
{		
	document.location.href="../user/discreteUserList.jsp?<%=qstring%>";
}
function defaultpass()//恢复用户初始密码
{
		var userId= document.forms[0].userId.value;
		//alert(userId);
		document.userInfo.target="saveDisperse";
		//document.userInfo.action="../user/userManager.do?method=defaultpass&userName="+userId;
		document.userInfo.action="userInfo_lisan.jsp?dispersePwd=2&userName="+userId;
		document.userInfo.submit();
		document.userInfo.userPassword.value="123456";
	
}

function winclose()
{
	window.returnValue = "ok";
	window.close();
	
}

</script>

	<body class="info"><CENTER><br>
		<form name="userInfo" method="post" action="">
			<pg:beaninfo requestKey="currUser">
			<input type="hidden" name="remark11" value="<pg:cell colName="remark1"  defaultValue=""/>" />
			<input type="hidden" name="remark31" value="<pg:cell colName="remark3"  defaultValue=""/>" />
			<table width="100%" height="25" border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
					<th>
							 <pg:message code="sany.pdp.user.login.name"/>：
						</th>
					  <td height="25">
					  	
						<input type="text" name="userName" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" maxlength="200" disabled=true ></td>

						
						<th>
							 <pg:message code="sany.pdp.user.real.name"/>：
						</th>
					  <td height="25">
						<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100">					</td>
						
					</tr>
					
					<tr>
						<th>
							 <pg:message code="sany.pdp.password"/>(<pg:message code="sany.pdp.default.password"/>123456)：
						</th>
					  <td height="25">
						<input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>" validator="string" cnname="口令" maxlength="40"></td>
						<th>
							 <pg:message code="sany.pdp.identity.card"/>：
						</th>
						<td height="25">
							<input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" validator="intNull" cnname="身份证号码" maxlength="18">
						</td>
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.company.telephone"/>：
						</th>
					  <td height="25">
						<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"  cnname="单位电话" maxlength="13"></td>
						<th>
							 <pg:message code="sany.pdp.sex"/>：
						</th>
					  <td height="25">					  
					    <%if("1".equals(request.getParameter("userId"))){%>
					    <dict:select type="sex" name="userSex" expression="{userSex}" disabled="true"/>
						<%}else{%>
						<dict:select type="sex" name="userSex" expression="{userSex}"/>
						<%}%>		
					</tr>
					<tr>
						<th>
							<pg:message code="sany.pdp.family.telephone"/>：
						</th>
						<td height="25">
							<input type="text" name="homePhone" value="<pg:cell colName="userHometel"  defaultValue=""/>"  cnname="家庭电话" maxlength="13">
						</td>
						<th>
							 <pg:message code="sany.pdp.email"/>：
						</th>
						<td height="25">
							<input type="text" name="mail" value="<pg:cell colName="userEmail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40">
						</td>
					</tr>

				<tr>
					<th><pg:message code="sany.pdp.moblie.telephone"/>1：</th>
					<td height="25">
								<input type="text" name="mobile" value="<pg:cell colName="userMobiletel1"  defaultValue=""/>" cnname="移动电话" maxlength="13"></td>
					<th> <pg:message code="sany.pdp.moblie.telephone"/>1<pg:message code="sany.pdp.address"/>：</th>
					<td height="25">
								<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100"></td>
					
			 	</tr>
			
			<tr><th> <pg:message code="sany.pdp.moblie.telephone"/>2：</th>
				<td height="25">
					<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>"  cnname="移动电话2" maxlength="13"></td>
				<th> <pg:message code="sany.pdp.moblie.telephone"/>2<pg:message code="sany.pdp.address"/>：</th>
				<td height="25">
							<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100"></td>
				
		   </tr>
		   <tr>
		   <th> <pg:message code="sany.pdp.sys.org.post"/>：</th>
				<td height="25">
							<input disabled="true" type="text" name="ou" value="<pg:cell colName="ou"  defaultValue=""/>" validator="stringNull" cnname="组织机构" maxlength="100"></td>
		   <th> <pg:message code="sany.pdp.spell"/>：</th>
				<td height="25">
							<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" validator="stringNull" cnname="拼音" maxlength="100"></td>
				
	       </tr>   
					<tr>
						<th>
							 <pg:message code="sany.pdp.user.type"/>：
						</th>
						<td height="25">
							<%if("1".equals(request.getParameter("userId"))){%>
							<dict:select type="userType" name="userType" expression="{userType}" disabled="true" textValue="<%=user.getUserType()%>" />
							<%}else{%>
							<dict:select type="userType" name="userType" expression="{userType}" textValue="<%=user.getUserType()%>" />
							<%}%>
						</td>
						<th>
							 <pg:message code="sany.pdp.post.code"/>：
						</th>
						<td height="25">
							<input type="text" name="postalCode" value="<pg:cell colName="userPostalcode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7">
						</td>
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.fax"/>：
						</th>
						<td height="25">
							<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="stringNull" cnname="传真" maxlength="40">
						</td>
						<th>
							 OICQ：
						</th>
						<td height="25">
							<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13">
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.birthday"/>：
						</th>
						<td height="25">
							<input type="text" name="userBirthday" onclick="showdate(document.all('userBirthday'))" readonly="true" value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40">
						</td>
						<th>
							 <pg:message code="sany.pdp.user.address"/>：
						</th>
						<td height="25">
							<input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" validator="stringNull" cnname="用户地址" maxlength="200">
						</td>
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.login.count"/>：
						</th>
						<td height="25">
							<input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" validator="intNull" readonly="true" cnname="登录次数" maxlength="40">
						</td>
						<th>
							 <pg:message code="sany.pdp.role.organization.check"/>：
						</th>
						<td height="25">
						<%if("1".equals(request.getParameter("userId"))){%>
						<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" disabled="true" />
						<%}else{%>
						<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" />
						<%}%>
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.register.date"/>：
						</th>
						<td height="25">
							<input type="text" name="userRegdate"  readonly="readOnly" value="<pg:cell colName="userRegdate"  defaultValue=""  />" validator="stringNull" cnname="注册日期" maxlength="40">
						</td>
						<th>
							 <pg:message code="sany.pdp.short.mobile"/>：
						</th>
						<td height="25">
							<input type="text" name="shortMobile" value="<pg:cell colName="remark2"  defaultValue=""/>" validator="stringNull"  cnname="手机短号码" maxlength="40">
						</td>
						
				</tr>
				<tr>
						<th>
							 <pg:message code="sany.pdp.user.sort.number"/>：
						</th>
						<td height="25">
							<input type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="" />" validator="stringNull" cnname="用户排序号" maxlength="40">
						</td>
						<td height="25" class="detailtitle" colspan="2">
							<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr><td width='10%'>
							<input type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
							</td><td width='150px'><div class='label'><pg:message code="sany.pdp.is.userinfo.secrecy"/></div></td></tr>
							</table>
						</td>
    					
				</tr>
						</table>
						</td>
				</tr>					
				<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />				
				<input type="hidden" name="qstring" value="<%=qqstring%>" >
			  </table>
			</pg:beaninfo>
			
			<div class="btnarea" >
				<a class="bt_1" onClick="storeUser1()"><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
				<a class="bt_1" onClick="defaultpass()"><span><pg:message code="sany.pdp.reset.password"/></span></a>
				<a class="bt_2" onclick="closeDlg();"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
			</div>
		
		</form>

<iframe name="saveDisperse" border="0" width="0" height="0"></iframe>
	</CENTER></body>
</html>
