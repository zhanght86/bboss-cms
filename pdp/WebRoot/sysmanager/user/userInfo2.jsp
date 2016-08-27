
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%	

	UserInfoForm user = (UserInfoForm)request.getAttribute("currUser");
	if(user == null)
		user = new UserInfoForm();
		
		
	String qstring = request.getParameter("qstring");
	String userId = request.getParameter("userId");
	
	if(qstring != null){
		//qstring  = java.net.URLEncoder.encode(qstring);
	}
	if(qstring == null){
		qstring = "";
	}
	
	UserManager userManager = SecurityDatabase.getUserManager();
	User quser = userManager.getUserById(userId);
	if(quser == null){
		quser = new User();
	}
	String orginfo = "";//用户所属的org
	try {
				OrgManager orgManager = SecurityDatabase.getOrgManager();
				List orgList = orgManager.getOrgList(quser);
				if (orgList != null && orgList.size() > 0) {
					StringBuffer sb = new StringBuffer();
					boolean flags = true;
					for (int i = 0; i < orgList.size(); i++) {
						Organization o = (Organization) orgList.get(i);
						if (flags) {
							sb.append(o.getOrgName());
							flags = false;
						} else {
							sb.append("," + o.getOrgName());
						}
					}
					orginfo = sb.toString();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
	String jobinfo = "";//用户所属的岗位
	try {
	JobManager jobManager = SecurityDatabase.getJobManager();
	List jobList = jobManager.getJobList(quser);
	if (jobList != null && jobList.size() > 0) {
		StringBuffer sb = new StringBuffer();
		boolean flags = true;
		for (int i = 0; i < jobList.size(); i++) {
			Job j = (Job) jobList.get(i);
			if (flags) {
				sb.append(j.getJobName());
				flags = false;
			} else {
				sb.append("," + j.getJobName());
			}
		}
		jobinfo = sb.toString();
				}

	} catch (Exception e) {
		e.printStackTrace();
	}
	request.setAttribute("currUser",quser);
%>

<html>
	<head>
		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script language="javascript" src="../scripts/selectTime.js"></script>
		<SCRIPT language="JavaScript" SRC="validateForm.js"></SCRIPT>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>userInfo2</title>
	</head>

<script language="JavaScript">
var jsAccessControl = new JSAccessControl("#DAE0E9","#F6F8FB","#F6F8FB");
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
 
 function goback2(e,userId)
{		
	getPropertiesContent().location.href="../user/userquery_content.jsp?<%=qstring%>";
}

 function goback(e,userId)
{		
	document.location.href="../user/userquery_content_tab.jsp?<%=qstring%>";
}
</script>
<body class="info">
	<div style="height: 10px">&nbsp;</div>
	<div class="form_box">
		<pg:beaninfo requestKey="currUser">
			<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
			<input type="hidden" name="remark3" value="<pg:cell colName="remark3"  defaultValue=""/>" />
			<table border="0" cellpadding="0" cellspacing="0" class="table4" align="left" >
				<tr>
					<th width=110px>登陆名：</th>
					 <td width="140px">
						<input type="text" name="userName" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="登陆名" maxlength="200" disabled=true  class="w120" />
					 </td>
					<th width=110px>真实名称：</th>
					 <td width="140px">
						<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="真实名称" maxlength="100"  class="w120"  />
					</td>
			  </tr>
			  <tr>
			  	<th width=110px>口令：</th>
				<td width="140px">
					<input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>" validator="string" cnname="口令" maxlength="40"  class="w120"  />
				</td>
				<th width=110px>身份证号码：</th>
				<td width="140px">
					<input type="password" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" validator="intNull" cnname="身份证号码" maxlength="18"  class="w120"  />
				</td>
			</tr>
			<tr>
				<th width=110px>单位电话：</th>
				<td width="140px">
					<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>" validator="intNull" cnname="单位电话" maxlength="11"  class="w120"  />
				</td>
				<th width=110px>性别：</th>
				 <td width="140px">					  
				    <pg:equal colName="userSex" value="M"><input type="text" name="userSex" value="男" class="w120" /></pg:equal>
					<pg:equal colName="userSex" value="-1"><input type="text" name="userSex" value="未知" class="w120" /></pg:equal>
					<pg:equal colName="userSex" value="F"><input type="text" name="userSex" value="女" class="w120" /></pg:equal>		
				</td>		
			</tr>
			<tr>
				<th width=110px>家庭电话：</th>
				<td width="140px">
					<input type="text" name="homePhone" value="<pg:cell colName="userHometel"  defaultValue=""/>" validator="intNull" cnname="家庭电话" maxlength="11" class="w120"  />
				</td>
				<th width=110px>电子邮件：</th>
				<td width="140px">
					<input type="text" name="mail" value="<pg:cell colName="userEmail"  defaultValue=""/>" validator="emailNull" cnname="电子邮件" maxlength="40" class="w120"  />
				</td>
			</tr>
			<tr>
				<th width=110px> 移动电话1：</th>
				<td width="140px">
					<input type="text" name="mobile" value="<pg:cell colName="mobile"  defaultValue=""/>" validator="intNull" cnname="移动电话" maxlength="11" class="w120"  /></td>
				<th width=110px> 移动电话1归属地：</th>
				<td width="140px">
					<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" validator="stringNull" cnname="移动电话1归属地" maxlength="100" class="w120"  /></td>
			</tr>
			<tr>
				<th width=110px> 移动电话2：</th>
				<td width="140px">
					<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" validator="intNull" cnname="移动电话2" maxlength="11" class="w120" />
				</td>
				<td width=110px> 移动电话2归属地：</td>
				<td width="140px">
					<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" validator="stringNull" cnname="移动电话2归属地" maxlength="100" class="w120" />
				</td>
		   </tr>
		   <tr>
		   		<th width=110px> 组织机构：</th>
				<td width="140px">
					<input type="text" name="ou" value="<%=orginfo%>" readonly="true" class="w120"  />
				</td>
		   		<th width=110px> 所属岗位：</th>
		    	 <td width="140px">
					<input type="text" name="ju" value="<%=jobinfo%>" readonly="true" class="w120"  />
				</td>
	       </tr>   
			<tr>
				<th width=110px>用户类型：</th>
				<td width="140px">
			    	<pg:equal colName="userType" value="1"><input type="text" name="userType" value="OA用户" class="w120"  /></pg:equal>
					<pg:equal colName="userType" value="0"><input type="text" name="userType" value="非OA用户" class="w120"  /></pg:equal>
				</td>
				<th width=110px>邮政编码：</th>
				<td width="140px">
					<input type="text" name="postalCode" value="<pg:cell colName="userPostalcode"  defaultValue=""/>" validator="intNull" cnname="邮政编码" maxlength="7" class="w120"  />
				</td>
			</tr>
			<tr>
				<th width=110px>传真：</th>
				<td width="140px">
					<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" validator="stringNull" cnname="传真" maxlength="40" class="w120"  />
				</td>
				<th width=110px>OICQ：</td>
				<td width="140px">
					<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" validator="intNull" cnname="OICQ" maxlength="13" class="w120"  />
				</td>
			</tr>
			<tr>
				<th width=110px>生日：</th>
				<td width="140px">
					<input type="text" name="userBirthday"  value="<pg:cell colName="userBirthday"  defaultValue=""  />" validator="stringNull" cnname="生日" maxlength="40" class="w120" />
				</td>
				<th width=110px>用户地址：</th>
				<td width="140px">
					<input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" validator="stringNull" cnname="用户地址" maxlength="200" class="w120" />
				</td>
			</tr>
			<tr>
				<th width=110px>登录次数：</th>
				<td width="140px">
					<input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" validator="intNull" readonly="true" cnname="登录次数" maxlength="40" class="w120"  />
				</td>
				<th width=110px>是否有效：</th>
				<td width="140px">
					<pg:equal colName="userIsvalid" value="1"><input type="text" name="userSex" value="有效" class="w120"  /></pg:equal>
					<pg:equal colName="userIsvalid" value="0"><input type="text" name="userSex" value="无效" class="w120"  /></pg:equal>	
				</td>
			</tr>
			<tr>
				<th width=110px>注册日期：</th>
				<td width="140px">
					<input type="text" name="userRegdate" value="<pg:cell colName="userRegdate"  defaultValue=""  />" validator="stringNull" cnname="注册日期" maxlength="40" class="w120"  />
				</td>
				<th width=110px>手机短号码：</th>
				<td width="140px">
					<input type="text" name="shortMobile" value="<pg:cell colName="remark2"  defaultValue=""/>" validator="stringNull"  cnname="手机短号码" maxlength="40" class="w120"  />
				</td>
		  </tr>
		 <tr>
         	<th width=110px> 拼音：</th>
    	 	<td width="140px">
				<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" readonly="true"  class="w120"  />
			</td>
			<th width=110px> 个人信息是否保密：</th>
			<td width="140px">
				<input type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
			</td>
		</tr>						
			<input type="hidden" name="userId" value="<pg:cell colName="userId"  defaultValue=""/>" />				
		 </table>
			</pg:beaninfo>			
		</form>
	</body>
</html>