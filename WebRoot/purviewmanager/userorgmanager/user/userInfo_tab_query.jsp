<%
/*
 * <p>Title: 用户基本信息页面</p>
 * <p>Description: 用户基本信息页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: chinacreator</p>
 * @Date 2008-3-22
 * @author liangbing.tao
 * @version 1.0
 */
%>


<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager"%>				
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager" %>
<%@ page import="com.frameworkset.platform.config.ResourceInfoQueue" %>
<%@ page import="com.frameworkset.platform.config.model.ResourceInfo" %>
<%@ page import="java.util.List"%>
<%@ page import="com.frameworkset.platform.security.authentication.EncrpyPwd"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.config.ConfigManager"%>




<%     
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
    
	String currOrgId = (String) request.getParameter("orgId");
	String userId = (String)request.getParameter("userId");
	
	String userName = null;
	User user = null;
	UserManager userManager = SecurityDatabase.getUserManager();
	
	if(userId != null)
	{
		 userId = request.getParameter("userId");
		 user = userManager.getUserById(userId);
     	 userName= user.getUserRealname();
     	 //取用户信息的时候对密码解密
     	 user.setUserPassword(user.getUserPassword());
	}	
	
	request.setAttribute("currUser",user);
	
	String ou="";
	OrgManager orgMgr = SecurityDatabase.getOrgManager();
	List list = orgMgr.getOrgList(user);
	boolean flag = false;
	for(int i=0;i<list.size();i++){
		Organization o = (Organization)list.get(i);
		if(flag)
			ou += "," + o.getOrgName();
		else
		{
			ou += o.getOrgName();
			flag = true;
		}
	}
	
	String restext = (String)request.getParameter("restext");
	String resvalue = (String)request.getParameter("resvalue");
	String restext1 = (String)request.getParameter("restext1");
	String resvalue1 = (String)request.getParameter("resvalue1");
	String resna = (String)request.getParameter("resna");
	String resna1 = (String)request.getParameter("resna1");
	
	//恢复密码
	boolean renewPwdState = false;
	String actionType = request.getParameter("actionType");
	if(actionType != null && "renewUserPwd".equals(actionType)){
		User renewUser = userManager.getUserById(request.getParameter("renewUserId"));
		renewUser.setUserPassword("123456");
		renewPwdState = userManager.updateUserPassword(renewUser);
	}
	
	//是否出现  "是否税管员" 复选框
	boolean istaxmanager = ConfigManager.getInstance().getConfigBooleanValue("istaxmanager", false);
%>
<html>
	<head>
		<title>用户[<%=userName%>]基本信息</title>
	    <tab:tabConfig />
	    <script language="JavaScript" src="../../scripts/calender_date.js" ></script>
		<script language="JavaScript" src="../../scripts/common.js" type="text/javascript"></script>
		<script language="javascript" src="../../scripts/selectTime.js"></script>
		<script src="<%=request.getContextPath()%>/include/validateForm_<pg:locale/>.js"></script>
		<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>


<script language="JavaScript">
var api = frameElement.api, W = api.opener;

//用户密码恢复是否成功提示信息
var renewPwdState = "<%=renewPwdState%>";
if(renewPwdState=="true"){
	W.$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.user.password.resume"/>');
}
function trim(string){
  var temp="";
  string = ''+string;
  splitstring = string.split(" ");
  for(i=0;i<splitstring.length;i++){
    temp += splitstring[i];
  } 
  return temp;
 }
function storeUser()
{
	if (validateForm(UserInfoForm) )
	{
		var userName= document.forms[0].userName.value;
		if (trim(userName).length == 0 ){
    		W.$.dialog.alert("<pg:message code='sany.pdp.input.username'/>！"); 
    		return false;
    	}
    	if(document.UserInfoForm.remark3.checked){
    		document.UserInfoForm.remark3.value="是";
    	}else{ 
			document.UserInfoForm.remark3.value="否";
		}
		if(document.UserInfoForm.istaxmanager.checked){
			document.UserInfoForm.istaxmanager.value="1";
		}else{
			document.UserInfoForm.istaxmanager.value="0";
		}	
   		document.UserInfoForm.target = "userInfo";
   		
   		document.all("resave").disabled = true;
   		document.all("calc").disabled = true;
   		document.all("default").disabled = true;
   		document.all("Submit32").disabled = true;
   		//divProcessing.style.display="";
   		
   		
		document.UserInfoForm.action="updateUser.jsp?userId=<%=userId%>&istaxmanager="+document.UserInfoForm.istaxmanager.value;
		document.UserInfoForm.submit();
	}
}
function defaultpass()//恢复用户初始密码
{
	var userId= document.forms[0].userId.value;
	//alert(userId);
	document.UserInfoForm.target="userInfo";
	UserInfoForm.userPassword.value="123456";
	document.UserInfoForm.action="userInfo_tab.jsp?renewUserId="+userId+"&actionType=renewUserPwd";
	document.UserInfoForm.submit();	
}

function closed()
{
	window.returnValue = "ok";
	window.close();
}

function window_onload() 
{
	//notice.style.display="none";  
}



//查看用户权限信息------------------------------------------------------------------		
function queryroleRes(name)
		{	
		 if(name == "resname"){
			//if(document.roleresList.restype.value.length < 1 && document.roleresList.resname.value.length < 1){
			//	alert("资源类型名称和资源名称必须输入一个!!!");
			//	return;
		//	}
			
			for(var i=1;i<document.all("restype").options.length;i++){
			var op=document.all("restype").options[i];
			if(op.selected){
			var restext = op.text;
			var resvalue = op.value;
		
			   }
			}
			var resna = document.roleresList.resname.value;
			var tablesFrame= document.getElementsByName("userrole");
			tablesFrame[0].src = "userrole_popedom.jsp?userId=<%=userId%>&restype=" + resvalue + "&resname=" + resna;
		  }else{
		 // if(document.userresList.restype1.value.length < 1 && document.userresList.resname1.value.length < 1){
		//		alert("资源类型名称和资源名称必须输入一个!!!");
			//	return;
		//	}
			for(var i=1;i<document.all("restype1").options.length;i++){
			var opt=document.all("restype1").options[i];
			if(opt.selected){
			var restext1 = opt.text;
			var resvalue1 = opt.value;
		    }
			} 
			var resna1 = document.userresList.resname1.value;
			var tablesFrame= document.getElementsByName("userself");
			tablesFrame[0].src = "userself_popedom.jsp?userId=<%=userId%>&restype1="+resvalue1+"&resname1="+resna1;
		  }
		}
		function queryroleResall(name1)
		{	
		if(name1 == "resname"){
		    document.roleresList.resname.value = "";
			document.all("restype").options[0].selected = true ;
			//查找用户全部角色资源
			var tablesFrame= document.getElementsByName("userrole");
			tablesFrame[0].src = "userrole_popedom.jsp?userId=<%=userId%>";
			}
			else{
			document.userresList.resname1.value = "";
			document.all("restype1").options[0].selected = true ;
			//查找用户全部自身资源
			var tablesFrame= document.getElementsByName("userself");
			tablesFrame[0].src = "userself_popedom.jsp?userId=<%=userId%>";
			}
		}

</script>


<body class="contentbodymargin" scroll="no" onload="window_onload();renewPassword()" >
	<div style="height: 10px">&nbsp;</div>
		<div align="center">
<div class="tabbox" id="tabbox">
	<ul class="tab" id="menu0">
		<li><a href="javascript:void(0)" class="current"
			onclick="setTab(0,0)"><span><pg:message code="sany.pdp.sys.info"/></span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(0,1)"><span><pg:message code="sany.pdp.user.role.authority"/></span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(0,2)"><span><pg:message code="sany.pdp.user.authority.self"/></span></a></li>
	</ul>
</div>
<div id="main0">
		<ul id="tab1" style="display: block;">
		<form name="UserInfoForm" method="post">
		
		
		<pg:beaninfo requestKey="currUser">
		<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<table border="0" cellpadding="0" cellspacing="0" class="table4">
			
			<tr>
				<th> <pg:message code="sany.pdp.user.login.name"/>：</th>						
				<td height="25">
					<input type="text" name="userName" onchange="checkUser()" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" disabled="true" >
					<span class="STYLE1">*</span>				
				</td>
				<th> <pg:message code="sany.pdp.user.real.name"/>：</th>					
				<td height="25">
					<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100" readonly="true"><span class="STYLE1">*</span>
				</td>											  
			</tr>
			
			<tr>						
				<th> <pg:message code="sany.pdp.password"/>：</th>
				<td height="25">
					<input type="password" name="userPassword" readonly  value="<pg:cell colName="userPassword"  defaultValue="123456"/>" validator="string" cnname="口令"  maxlength="40"  readonly="true"><span class="STYLE1">*缺省口令为123456</span>
				</td>
				<th> <pg:message code="sany.pdp.identity.card"/>：</th>
					<td height="25">
					<input type="text" readonly="true" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" validator="intNull" cnname="身份证号码" maxlength="18">
				</td>
										
			</tr>
			
			<tr><th> <pg:message code="sany.pdp.company.telephone"/>：</th>					
				<td height="25">
					<input type="text" readonly="true" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>"  validator="phone" cnname="单位电话" maxlength="13"></td>
				<th> <pg:message code="sany.pdp.sex"/>：</th>
				<td height="25">					  				
				<dict:select type="sex" name="userSex" expression="{userSex}" disabled="true"/>
				</td>
			</tr>
			
			<tr><th> <pg:message code="sany.pdp.family.telephone"/>：</th>
				<td height="25">
							<input readonly="true" type="text" name="homePhone" value="<pg:cell colName="userHometel"  defaultValue=""/>"  validator="phone" cnname="家庭电话" maxlength="13"></td>				
				<th> <pg:message code="sany.pdp.email"/>：</th>
				<td height="25">
							<input readonly="true" type="text" name="mail" value="<pg:cell colName="userEmail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40"></td>
				
			</tr>
			
			<tr>
				<th> <pg:message code="sany.pdp.moblie.telephone"/>1：</th>
				<td height="25">
							<input readonly="true" type="text" name="mobile" value="<pg:cell colName="userMobiletel1"  defaultValue=""/>" validator="phone" cnname="移动电话" maxlength="13"></td>
				<th> <pg:message code="sany.pdp.moblie.telephone"/>1<pg:message code="sany.pdp.address"/>：</th>
				<td height="25">
							<input readonly="true" type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100"></td>
				
			</tr>
			
			<tr><th> <pg:message code="sany.pdp.moblie.telephone"/>2：</th>
				<td height="25">
					<input type="text" readonly="true" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" validator="phone" cnname="移动电话2" maxlength="13"></td>
				<th> <pg:message code="sany.pdp.moblie.telephone"/>2<pg:message code="sany.pdp.address"/>：</th>
				<td height="25">
							<input type="text" readonly="true" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100"></td>
				
		   </tr>
		   <tr>
		   <th> <pg:message code="sany.pdp.workflow.organization"/>：</th>
				<td height="25">
							<input type="text" name="ou" readonly="true" value="<%=ou%>" validator="stringNull" cnname="组织机构" maxlength="100"></td>
		   <th> <pg:message code="sany.pdp.complete.spell"/>：</th>
				<td height="25">
							<input type="text" name="userPinyin" readonly="true" value="<pg:cell colName="userPinyin"  defaultValue=""/>" validator="stringNull" cnname="拼音" maxlength="100"></td>
				
	       </tr>   
		   <tr><th> <pg:message code="sany.pdp.user.type"/>：</th>
			   <td height="25">
					<dict:select type="userType" name="userType" expression="{userType}" disabled="true"  />
					<input type=hidden name="userType" value="<pg:cell colName="userType" defaultValue="0"/>">
			   </td>					
			   <th> <pg:message code="sany.pdp.post.code"/>：</th>
			   <td height="25">
							<input type="text" readonly="true" name="postalCode" value="<pg:cell colName="userPostalcode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7"></td>						
			   
		   </tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.fax"/>：
						</th>
						<td height="25">
							<input readonly="true" type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="phone" cnname="传真" maxlength="40">
						</td>
						<th>
							 OICQ：
						</th>
						<td height="25">
							<input readonly="true" type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13">
						</td>
						
						</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.birthday"/>：
						</th>
						<td height="25">
							<input readonly="true" type="text" name="userBirthday"  readonly="true" value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40">
						</td>
						<th>
							 <pg:message code="sany.pdp.user.address"/>：
						</th>
						<td height="25">
							<input readonly="true" type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" validator="stringNull" cnname="用户地址" maxlength="200">
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.login.count"/>：
						</th>
						<td height="25">
							<input readonly="true" type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" validator="intNull" readonly cnname="登录次数" maxlength="40">
						</td>
						<th>
							 <pg:message code="sany.pdp.role.organization.check"/>：
						</th>
						<td height="25">
							<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" disabled="true" />
							<input type=hidden name="userIsvalid" value="<pg:cell colName="userIsvalid" defaultValue="0"/>">
						
													
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.register.date"/>：
						</th>
						<td height="25">
							<input type="text" name="userRegdate" onclick="showdate(document.all('userRegdate'))" readonly="true" value="<pg:cell colName="userRegdate"  defaultValue="" />" validator="stringNull" cnname="注册日期" maxlength="40" disabled>
							<input type=hidden name="userRegdate" value="<pg:cell colName="userRegdate"  defaultValue="" />"/>
							
						</td>
						<th>
							 <pg:message code="sany.pdp.short.mobile"/>：
						</th>
						<td height="25">
							<input type="text" readonly="true" name="shortMobile" value="<pg:cell colName="remark2"  defaultValue=""/>" validator="phone"  cnname="手机短号码" maxlength="40">
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.user.sort.number"/>：
						</th>
						<td height="25">
							
							<input type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="" />" validator="stringNull"  maxlength="40" readOnly=true>
							
						</td>
						<td height="25" class="detailtitle" colspan="2">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr><td width='10%'>
						<input disabled type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
						</td><td width='150px'><div class='label'><pg:message code="sany.pdp.is.userinfo.secrecy"/></div></td>
						</tr>
						</table>
						</td>
					</tr>
					<tr>
						<th>
							 上次修改密码时间:
						</th>
						<td height="25" >
							<pg:cell colName="passwordUpdatetime"  dateformat="yyyy-MM-dd HH:mm:ss"/>							
						</td>
						<th>
							 密码过期时间:
						</th>
						<td height="25" >
							<pg:cell colName="passwordExpiredTime"  dateformat="yyyy-MM-dd HH:mm:ss"/>							
						</td>
					</tr>
					<tr>
						<th>
							 设置密码有效期
						</th>
						<td height="25" >
							
							<pg:cell colName="passwordDualedTime"  defaultValue="-1" />天
							
						</td>
						<th>
							<pg:message code="sany.pdp.user.worknumber"/>：
						</th>
						<td height="25" >
							
							<input type="text" name="userWorknumber" value="<pg:cell colName="userWorknumber"  defaultValue="" />" validator="stringNull" maxlength="40" readOnly=true>
							
						</td>
						
					</tr>
					<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />
					<input name="orgId" value="<%=currOrgId%>" type="hidden" height="0">
		</table>
		
		  </pg:beaninfo>
		</form>

		<div class="btnarea" >
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
		</div>
	</ul>
<!-- --------------------------------------------------------------------------------------------->
	<ul id="tab2">
	<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
					<form name="roleresList" method="post" >
					<input name="userId" value="<%=userId%>" type="hidden">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="table2">
         				<tr >
           					<td height='30' valign='middle' align="center"><pg:message code="sany.pdp.role.resource.type.name"/>：<select name="restype" id="restype">
           					 <%
	                          int count = 0;
	                          ResourceManager rsManager = new ResourceManager();
                              ResourceInfoQueue restypeall=rsManager.getResourceInfoQueue();
                              int restypelength =restypeall.size();
	                          for(int i=0;i<restypelength;i++)
	                            
	                          {    
	                                ResourceInfo restypeinfo=restypeall.getResourceInfo(i);
	                                String id = restypeinfo.getId();
	                                String name = restypeinfo.getName();
	                                //if(restypeinfo.isAuto())
	                                //{
		                                if(restext != null && resvalue != null && !restext.equals("undefined") && !resvalue.equals("undefined") && count == 0 && restext.equals(name) && resvalue.equals(id) ){
		                                    count++;
			                        	%>
			                           <option value='<%=resvalue%>' selected><%=restext%></option>
			                           <%}
			                           else 
			                           {
			                           %>
		                               <option value='<%=id%>'><%=name%></option>
		                               <%}
		                             //}
								}%>
           					 </td>
           					<td height='30'valign='middle' align="center"><pg:message code="sany.pdp.role.resource.name"/>：
           					<%
           					if(resna == null){
           					%>
           					<input type="text" name="resname" >
           					<%}
           					else{
           					%>
           					<input type="text" value='<%=resna%>' name="resname" >
           					<%}%>
           					</td>
           					<td height='30' valign='middle' align="center" colspan="2">
           						<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick='queryroleRes("resname")'><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
								<a href="javascript:void(0)" class="bt_1" id="queryButton1" onclick='queryroleResall("resname")'><span><pg:message code="sany.pdp.common.operation.search.all"/></span></a>
           				   </td>
           				 
           				</tr>
           				
					</table>
								</td>
								<td class="right_box"></td>
							</tr>
					</table>
				</form>
				</div>
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
				</div>
				<iframe name="userrole" src="userrole_popedom.jsp?userId=<%=userId%>" style="width:100%" height="100%" scrolling="no" frameborder="0" marginwidth="1" marginheight="1"></iframe>
      </ul>
      
 <!-- --------------------------------------------------------------------------------------------->
	<ul id="tab3">
	<div id="searchblock">
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				<div class="search_box">
			<form name="userresList" method="post" >
					<input name="userId" value="<%=userId%>" type="hidden">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
         				<tr >
           					<td height='30'valign='middle' align="center"><pg:message code="sany.pdp.role.resource.type.name"/>：<select name="restype1" id="restype1">
           					<%
	                          int count1 = 0;
	                          ResourceManager rsManager1 = new ResourceManager();
                              ResourceInfoQueue restypeall1=rsManager1.getResourceInfoQueue();
                              int restypelength1 =restypeall1.size();
	                          for(int i=0;i<restypelength1;i++)
	                            
	                          {
	                                ResourceInfo restypeinfo1=restypeall1.getResourceInfo(i);
	                                String id1 = restypeinfo1.getId();
	                                String name1 = restypeinfo1.getName();
	                             //if(restypeinfo1.isAuto())
	                             //   {
	                                	if(restext1 != null && resvalue1 != null && !restext1.equals("undefined") && !resvalue1.equals("undefined") && count1 == 0 && restext1.equals(name1) && resvalue1.equals(id1)){
		                            	count1++;
				                        %>
				                        <option value='<%=resvalue1%>' selected><%=restext1%></option>
				                        <%}
				                        else
				                        {
				                        %>
			                            <option value='<%=id1%>'><%=name1%></option>
			                            <%}
	                               // }		                            
                            }%>
                          </select>
           					 </td>
           					<td height='30'valign='middle' align="center"><pg:message code="sany.pdp.role.resource.name"/>：
           					<%
           					if(resna1 == null){
           					%>
           					<input type="text" name="resname1" >
           					<%}
           					else{
           					%>
           					<input type="text" value='<%=resna1%>' name="resname1" >
           					<%}%>
           					</td>
           					<td height='30'valign='middle' align="center">
           						<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick='queryroleRes("resname1")'><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
								<a href="javascript:void(0)" class="bt_1" id="queryButton1" onclick='queryroleResall("resname1")'><span><pg:message code="sany.pdp.common.operation.search.all"/></span></a>
           						</td>
           				
           				 
           				</tr>
           				
					</table>
					</td>
					<td class="right_box"></td>
					</tr>
					</table>
	</form>
				</div>
		<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
		</div>	
</div>
				<iframe name="userself" src="userself_popedom.jsp?userId=<%=userId%>" style="width:100%" height="100%" scrolling="no" frameborder="0" marginwidth="1" marginheight="1"></iframe>
</ul>
      </div>

</div>
<iframe name="userInfo" width="0" height="0"></iframe>
<script>
//恢复密码
function renewPassword(){
	var renewPaw = "<%=request.getParameter("renewPaw")%>";
	if(renewPaw == "true"){
		$.dialog.alert('<pg:message code="sany.pdp.userorgmanager.user.password.resume"/>');
	}
}
</script>
</body> 
</html>
