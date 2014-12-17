<%
/*
 * <p>Title: 用户信息查看页面</p>
 * <p>Description: 用户信息查看页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-24
 * @author baowen.liu
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

<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.*" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="java.util.*"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%     

	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkManagerAccess(request,response);
	
		 String userId = request.getParameter("userId");
		 UserManager userManager = SecurityDatabase.getUserManager();
		 User user = userManager.getUserById(userId);
     	 String userName= user.getUserRealname();
	     request.setAttribute("currUser", user);
	     
	     String userAccount = user.getUserName();
	    
	
	//得到用户所在机构的列表.
	
	OrgManager orgManager  = SecurityDatabase.getOrgManager();
	List list  = orgManager.getOrgList(user);
	
	String orgNames ="";
	
	if(list.size() > 0)
	{
		for(int i=0 ; i<list.size();i++)
		{
			Organization org = (Organization) list.get(i);
			orgNames += org.getOrgName() + ",";		
		}
	}
	if(orgNames.length()!=0){
		orgNames = orgNames.substring(0,orgNames.length()-1);
	}
	
	 
%>
<html>
	<head>
		<title>用户[<%=userName%>]基本信息</title>
	   

		<script language="JavaScript" src="common.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../html/js/commontool.js"></script>
	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
	<script language="JavaScript">	
		function back()
			{
				
				window.close();
			}
	</script>

</head>




<body class="contentbodymargin" scroll="no" onload="">
		<div id="" align="center">

	
		<form name="UserInfoForm" method="post">
		
		
		<pg:beaninfo requestKey="currUser">
		<input type="hidden" name="remark1" value="<pg:cell colName="remark1"  defaultValue=""/>" />
		<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="table2">
			
			<tr>
				<th> <pg:message code="sany.pdp.user.login.name"/>：</th>						
				<td height="25">
					<input type="text" readonly name="userName"  value="<pg:cell colName="userName"  defaultValue=""/>" >
					<font color="red">*</font>
				</td>
				<th> <pg:message code="sany.pdp.user.real.name"/>：</th>						
				<td height="25">
					<input type="text" name="userRealname" value="<pg:cell colName="userRealname"  defaultValue=""/>" readonly>
					<font color="red">*</font>
				</td>											  
			</tr>
			
			<tr>						
				<th> <pg:message code="sany.pdp.password"/>：</th>
				<td height="25">
					<input type="password" name="userPassword" readonly  value="<pg:cell colName="userPassword"  defaultValue="123456"/>" readonly>
					<font color="red">*<pg:message code="sany.pdp.default.password"/> 123456</font>
				</td>
				<th> <pg:message code="sany.pdp.identity.card"/>：</th>
					<td height="25">
					<input type="text" name="userIdcard" value="<pg:cell colName="userIdcard"  defaultValue=""/>" readonly>
				</td>
										
			</tr>
			
			<tr><th> <pg:message code="sany.pdp.company.telephone"/>：</th>					
				<td height="25">
					<input type="text" name="userWorktel" value="<pg:cell colName="userWorktel"  defaultValue=""/>" readonly></td>
				<th> <pg:message code="sany.pdp.sex"/>：</th>
				<td height="25">					  					    
				<%if("1".equals(request.getParameter("userId"))){%>
				<dict:select type="sex" name="userSex" expression="{userSex}" disabled="true"/>
				<%}else{%>
				<dict:select type="sex" name="userSex" expression="{userSex}"  disabled="true"/>
				<%}%>
				</td>
			</tr>
			
			<tr><th> <pg:message code="sany.pdp.family.telephone"/>：</th>
				<td height="25">
							<input type="text" name="homePhone" value="<pg:cell colName="userHometel"  defaultValue=""/>" readonly></td>				
				<th> <pg:message code="sany.pdp.email"/>：</th>
				<td height="25">
							<input type="text" name="mail" value="<pg:cell colName="userEmail"  defaultValue=""/>" readonly></td>
				
			</tr>
			
			<tr>
				<th> <pg:message code="sany.pdp.moblie.telephone"/>1：</th>
				<td height="25">
							<input type="text" name="mobile" value="<pg:cell colName="userMobiletel1"  defaultValue=""/>" readonly></td>
				<th> <pg:message code="sany.pdp.moblie.telephone"/>1<pg:message code="sany.pdp.address"/>：</th>
				<td height="25">
							<input type="text" name="remark4" value="<pg:cell colName="remark4"  defaultValue=""/>" readonly></td>
				
			</tr>
			
			<tr><th> <pg:message code="sany.pdp.moblie.telephone"/>2：</th>
				<td height="25">
					<input type="text" name="userMobiletel2" value="<pg:cell colName="userMobiletel2"  defaultValue=""/>" readonly></td>
				<th> <pg:message code="sany.pdp.moblie.telephone"/>2<pg:message code="sany.pdp.address"/>：</th>
				<td height="25">
							<input type="text" name="remark5" value="<pg:cell colName="remark5"  defaultValue=""/>" readonly ></td>
				
		   </tr>
		   <tr>
		   <th> <pg:message code="sany.pdp.workflow.organization"/>：</th>
				<td height="25">
							<input type="text" name="ou" readonly="true" value="<%=orgNames%>" readonly ></td>
		   <th> <pg:message code="sany.pdp.spell"/>：</th>
				<td height="25">
							<input type="text" name="userPinyin" value="<pg:cell colName="userPinyin"  defaultValue=""/>" readonly></td>
				
	       </tr>   
		   <tr><th> <pg:message code="sany.pdp.user.type"/>：</th>
			   <td height="25">
			   		<%if(accesscontroler.isAdmin(userAccount) || "1".equals(userId) || userId.equals(accesscontroler.getUserID())){%>
					<dict:select type="userType" name="userType" expression="{userType}" disabled="true"  />
					<%}else{%>
					<dict:select type="userType" name="userType" expression="{userType}" disabled="true" />
					<%}%>
			   </td>					
			   <th> <pg:message code="sany.pdp.post.code"/>：</th>
			   <td height="25">
							<input type="text" name="postalCode" value="<pg:cell colName="userPostalcode"  defaultValue=""/>" readonly></td>						
			   
		   </tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.fax"/>：
						</th>
						<td height="25">
							<input type="text" name="userFax" value="<pg:cell colName="userFax"  defaultValue=""/>" readonly>
						</td>
						<th>
							 OICQ：
						</th>
						<td height="25">
							<input type="text" name="userOicq" value="<pg:cell colName="userOicq"  defaultValue=""/>" readonly>
						</td>
						
						</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.birthday"/>：
						</th>
						<td height="25">
							<input type="text" name="userBirthday"  readonly="true" value="<pg:cell colName="userBirthday"  defaultValue=""  />" readonly>
						</td>
						<th>
							 <pg:message code="sany.pdp.user.address"/>：
						</th>
						<td height="25">
							<input type="text" name="userAddress" value="<pg:cell colName="userAddress"  defaultValue=""/>" readonly>
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.login.count"/>：
						</th>
						<td height="25">
							<input type="text" name="userLogincount" value="<pg:cell colName="userLogincount"  defaultValue="0"/>"  readonly >
						</td>
						<th>
							 <pg:message code="sany.pdp.role.organization.check"/>：
						</th>
						<td height="25">
							<%if(accesscontroler.isAdmin(userAccount) || "1".equals(userId) || userId.equals(accesscontroler.getUserID())){%>
							<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" disabled="true" />
							<%}else{%>
							<dict:select type="isvalid" name="userIsvalid" expression="{userIsvalid}" disabled="true"/>
						<%}%>							
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.register.date"/>：
						</th>
						<td height="25">
							<input type="text" name="userRegdate"  readonly="true" value="<pg:cell colName="userRegdate"  defaultValue="" />" readonly >
						</td>
						<th>
							 <pg:message code="sany.pdp.short.mobile"/>：
						</th>
						<td height="25">
							<input type="text" name="shortMobile" value="<pg:cell colName="remark2"  defaultValue=""/>" readonly >
						</td>
						
					</tr>
					<tr>
						<th>
							 <pg:message code="sany.pdp.user.sort.number"/>：
						</th>
						<td height="25">
							<input type="text" name="userSn" value="<pg:cell colName="userSn"  defaultValue="" />" readonly >
						</td>
						<td height="25" class="detailtitle" colspan="2">
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
						<tr><td width='10%'>
						<input disabled="true" type="checkbox" name="remark3" <pg:equal colName="remark3" value="是">checked</pg:equal>>
						</td><td width='150px'><div class='label'><pg:message code="sany.pdp.is.userinfo.secrecy"/></div></td>
						</tr>
						</table>
						</td>
					</tr>
		     </table>
		  </pg:beaninfo>
		</form>
        <div align="center">
	         <a class="bt_2" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
		</div>
	
		

</div>

</body> 
</html>
