 
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.util.*,com.frameworkset.platform.sysmgrcore.entity.*,com.frameworkset.platform.sysmgrcore.web.struts.form.*"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.OrgManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.JobManager" %>
<%@ page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@ page import="com.frameworkset.platform.config.ConfigManager" %>
<%	
	UserInfoForm user = (UserInfoForm)request.getAttribute("currUser");
	if(user == null)
		user = new UserInfoForm();
		
	 boolean showidcard = ConfigManager.getInstance().getConfigBooleanValue("user.showidcard", true);
	    request.setAttribute("showidcard", showidcard);	
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
	if(quser.getRemark3()==null||quser.getRemark3().equals(""))
	quser.setRemark3("否");
	
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
		<title>userInfo4</title>
		
	 
</head>

<script language="JavaScript">
var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
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

<body>
	<div style="height: 10px">&nbsp;</div>
	<div class="form_box">
		<pg:beaninfo requestKey="currUser">
		<table border="0" cellpadding="0" cellspacing="0" class="table4" align="left" >
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.loginname"/>：</th>
				<td width="140px"><input type="text" name="userName" onchange="checkUser()" value="<pg:cell colName="userName"  defaultValue=""/>" validator="string" cnname="<pg:message code='sany.pdp.personcenter.person.loginname'/>" maxlength="200" readonly="true" class="w120" /></td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.realname"/>：</th>
				<td width="140px"><input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" validator="string" cnname="<pg:message code='sany.pdp.personcenter.person.realname'/>" maxlength="100" readonly="true" class="w120" /></td>
			</tr>
			<tr>
			<pg:true actual="${showidcard }" evalbody="true">
				<pg:yes>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.view.password"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="password" name="userIdcard" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
				<th width=110px ><pg:message code="sany.pdp.personcenter.person.idcard"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userIdcard" value="******" readonly="true" class="w120"></pg:equal>
				</td>
				</pg:yes>
				<pg:no>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.view.password"/>：</th>
				<td width="140px" colspan="3">
					<pg:equal colName="remark3" value="否"><input type="password" name="userPassword" value="<pg:cell colName="userPassword"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="password" name="userIdcard" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
				
				</pg:no>
			</pg:true>	
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.worktel"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userWorktel" value="******" readonly="true" class="w120"/></pg:equal>						
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.sex"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否">					  
				    	<pg:equal colName="userSex" value="M"><input type="text" name="userSex" value="<pg:message code='sany.pdp.personcenter.person.sex.male'/>" readonly="true" class="w120" /></pg:equal>
						<pg:equal colName="userSex" value="-1"><input type="text" name="userSex" value="<pg:message code='sany.pdp.sys.unknow'/>" readonly="true" class="w120" /></pg:equal>
						<pg:equal colName="userSex" value="F"><input type="text" name="userSex" value="<pg:message code='sany.pdp.personcenter.person.sex.female'/>" readonly="true" class="w120" /></pg:equal>				
			    	</pg:equal>					    	
			    	<pg:equal colName="remark3" value="是">				
				    	<input type="text" name="userSex" value="******" readonly="true" class="w120"/>
			    	</pg:equal>					    	
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.hometel"/>：</th>
				<td width="140px">
						<pg:equal colName="remark3" value="否"><input type="text" name="homePhone" value="<pg:cell colName="userHometel"  defaultValue=""/>" readonly="true" class="w120"/></pg:equal>
						<pg:equal colName="remark3" value="是"><input type="text" name="homePhone" value="******" readonly="true" class="w120"/></pg:equal>			
					</td>
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.email"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="mail" value="<pg:cell colName="userEmail"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="mail" value="******" readonly="true" class="w120" /></pg:equal>			
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.mobiletel1"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="mobile" value="<pg:cell colName="userMobiletel1"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
				   	<pg:equal colName="remark3" value="是"><input type="text" name="mobile" value="******" readonly="true" class="w120" /></pg:equal>			
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.mobiletel1.location"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
				    <pg:equal colName="remark3" value="是"><input type="text" name="remark4" value="******" readonly="true" class="w120 /></pg:equal>			
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.mobiletel2"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userMobiletel2" value="******" readonly="true" class="w120" /></pg:equal>							
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.mobiletel2.location"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="remark5" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.organization"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="ou" value="<%=orginfo%>" readonly="true" class="w120" /></pg:equal>
				     <pg:equal colName="remark3" value="是"><input type="text" name="ou" value="******" readonly="true" class="w120" /></pg:equal>			
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.post"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="ju" value="<%=jobinfo%>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="ju" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.usertype"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否">
						<pg:equal colName="userType" value="1"><input type="text" name="userType" value="<pg:message code='sany.pdp.personcenter.person.usertype.outer'/>" readonly="true" class="w120" /></pg:equal>
						<pg:equal colName="userType" value="0"><input type="text" name="userType" value="<pg:message code='sany.pdp.personcenter.person.usertype.inner'/>" readonly="true" class="w120" /></pg:equal>		
						<pg:equal colName="userType" value="2"><input type="text" name="userType" value="" readonly="true" class="w120" /></pg:equal>						
					</pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userType" value="******" readonly="true" class="w120" /></pg:equal>				
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.postalCode"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="postalCode" value="<pg:cell colName="userPostalcode"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userPostalcode" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.fax"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userFax" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
				<th width=110px>OICQ：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userOicq" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.birthday"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userBirthday"  value="<pg:cell colName="userBirthday"  defaultValue=""  />" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userBirthday" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.address"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userAddress" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.logincount"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userLogincount" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.isvalid"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否">					  
			    		<pg:equal colName="userIsvalid" value="1"><input type="text" name="userSex" value="<pg:message code='sany.pdp.personcenter.person.isvalid.yes'/>" readonly="true" class="w120" /></pg:equal>
						<pg:equal colName="userIsvalid" value="0"><input type="text" name="userSex" value="<pg:message code='sany.pdp.personcenter.person.isvalid.no'/>" readonly="true" class="w120" /></pg:equal>
						<pg:equal colName="userIsvalid" value="2"><input type="text" name="userSex" value="" readonly="true" class="w120" /></pg:equal>
					</pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userIsvalid" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.regdate"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userRegdate" value="<pg:cell colName="userRegdate"  defaultValue=""  />" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userRegdate" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.shortmobile"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="shortMobile" value="<pg:cell colName="remark2"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="shortMobile" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
			</tr>
			<tr>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.pinyin"/>：</th>
				<td width="140px">
					<pg:equal colName="remark3" value="否"><input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" readonly="true" class="w120" /></pg:equal>
					<pg:equal colName="remark3" value="是"><input type="text" name="userPinyin" value="******" readonly="true" class="w120" /></pg:equal>
				</td>
				<th width=110px><pg:message code="sany.pdp.personcenter.person.worknumber"/>：</th>
				<td width="140px">
					<input type="text" name="userWorknumber" value="<pg:cell colName="userWorknumber"  defaultValue="" />" validator="stringNull" cnname="<pg:message code='sany.pdp.personcenter.person.worknumber'/>" maxlength="40" readOnly=true class="w120" />
				</td>
			</tr>
		</table>
		</pg:beaninfo>
	</div>
	</body>
</html>