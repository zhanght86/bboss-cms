<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.GroupManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Group" %>
<%
	String orgid = request.getParameter("orgId");
	List list = (List)request.getAttribute("existUser");
	if(list == null){
		String groupId = (String) session.getAttribute("currGroupId");		
		
		GroupManager groupManager = SecurityDatabase.getGroupManager();
		UserManager userManager = SecurityDatabase.getUserManager();	
		
		Group group = groupManager.getGroupByID(groupId);
		if (group != null) {
			List existUser = userManager.getUserList(group);			
			if (existUser == null) {
				existUser = new ArrayList();
			}			
			request.setAttribute("existUser", existUser);
		}
	}
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="sysmanager/css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="sysmanager/css/tab.winclassic.css">
		<SCRIPT LANGUAGE="JavaScript"> 
function addRole(){	
      var n=document.all("userIds").options.length-1;
   	 	
   for(var i=0;i<document.all("allist").options.length;i++){
   var op=document.all("allist").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
}
function addone(name,value,n){

   for(var i=n;i>=0;i--){
		if(value==document.all("userIds").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("userIds").add(op);
   
}
function deleteall(){
	for (var m=document.all("userIds").options.length-1;m>=0;m--)
    document.all("userIds").options[m]=null
}
      
function addall(){
	var n=document.all("userIds").options.length-1;
	var p=document.all("allist").options.length-1;		  
     for(var i=0;i<document.all("allist").options.length;i++){
     var op=document.all("allist").options[i];
     addone(op.text,op.value,n);  
   }
}
function deleterole(){
 for (var m=document.all("userIds").options.length-1;m>=0;m--)
	      if(document.all("userIds").options[m].selected)
         document.all("userIds").options[m]=null;
}
function okadd(){
var all = document.all("allist").options.length;
if(all ==0){
	alert("您没有选择机构或者机构可选人员为空");
	return;
}
var len=document.all("userIds").options.length;
	for (var i=0;i<len;i++){	      
         document.all("userIds").options[i].selected="true";
         }
         document.forms[0].action="groupmanager.do?method=storeUserList";
         document.forms[0].submit();
}
</SCRIPT>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<center>
				<form name="OrgJobForm" action="" method="post">
					<table width="80%" border="0" cellpadding="0" cellspacing="1" class="table">
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" width="40%" align="center">
								可选用户
							</td>
							<td width="20%" class="td" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center" class="td">
								已授予用户
							</td>
						</tr>
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" align="right">
								<select class="select" name="allist" multiple style="width:100%" onDBLclick="addRole()" size="18">
									<pg:list requestKey="allUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select>
							</td>

							<td align="center" class="td">
								<table class="table" width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr class="tr">
										<td align="center" class="td">
											<input name="button1" type="button" class="input" value="&gt;" onclick="addRole()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button2" type="button" class="input" value="&gt;&gt;" onclick="addall()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button3" type="button" class="input" value="&lt;&lt;" onclick="deleteall()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<input name="button4" type="button" class="input" value="&lt;" onclick="deleterole()">
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td class="td">
								<select class="select" name="userIds" multiple style="width:100%" onDBLclick="deleterole()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select>

							</td>

						</tr>
						<tr class="tr">
							<td colspan="3" class="td" align="center">&nbsp;
							</td>
						</tr>
						<tr class="tr">
							<td colspan="3" class="td" align="center">
								<input name="add" type="button" class="input" value="确定" onclick="okadd()">

							</td>
						</tr>
					</table>
					<pg:beaninfo requestKey="userRoleForm">
						<input type="hidden" name="orgId" value="<%=orgid%>" />
						<input type="hidden" name="uid" value="<pg:cell colName="userId"  defaultValue=""/>" />
					</pg:beaninfo>
				</form>
			</center>
		</div>
	</body>
</html>

