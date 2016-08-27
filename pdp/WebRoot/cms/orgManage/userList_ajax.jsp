<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.menu.OrgUserTree"%>
<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.entity.User,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.platform.sysmgrcore.manager.UserManager,java.util.*,
	com.frameworkset.common.poolman.DBUtil,
	com.frameworkset.platform.config.ConfigManager"%>
<%
	String orgId = request.getParameter("orgId");
	String oid = request.getParameter("oid");
	String classType = request.getParameter("classType");
	UserManager userManager = SecurityDatabase.getUserManager();
	//本机构下已有的用户
	List thisUser = new ArrayList();
	thisUser = userManager.getOrgUserList(oid);
	request.setAttribute("thisUser", thisUser);
	//其他机构下的用户
	if(orgId != null && !orgId.equals("")){
		List allUser = new ArrayList();
		allUser = userManager.getOrgUserList(orgId);
		request.setAttribute("allUser", allUser);
	}
	//离散用户
	if("lisan".equals(request.getParameter("classType"))){
		List allUser = userManager.getDicList();
		request.setAttribute("allUser", allUser);
	}
%>
<html>
<head>
<title>属性容器</title>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">

<script language="javascript">

function addone(name,value,n){
	for(var i=n;i>=0;i--){
		if(value==document.all("userIds").options[i].value){
			return;
		}
	}
	var op=new Option(name,value);
    document.all("userIds").add(op);
}

function addUser(){
	//调入单个用户
	var userIds = "";
	var n = document.all("userIds").options.length-1;
	var m=0;
	for(var i=0;i<document.all("allist").options.length;i++){
		op = document.all("allist").options[i];
		if(op.selected){
			userIds = op.value;
            m++;
		}
	}
	//增加不选择任何用户的判断
	//baowen.liu 2008-5-4
	if(m<=0){
	alert("请选择一个用户！");
	return false;
	}
	
	if(document.all("allist").options.length < 1){
		alert("没有用户可选");
		return;
	}
	var orgId = "<%=oid%>";
	var flag = "1";
	var classType = "<%=classType%>";
	document.OrgUserForm.target="saveUser";
	<%
		if(ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true) && !"lisan".equals(classType)){
	%>
		outMsg = "是否保留所选用户与该机构的关系！（保留点确定；删除点取消）";
        if (confirm(outMsg)){
			document.OrgUserForm.action="<%=request.getContextPath()%>/sysmanager/user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>&hold=true";
		}else{
			document.OrgUserForm.action="<%=request.getContextPath()%>/sysmanager/user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>";
		}
	<%
		}else{
	%>
			document.OrgUserForm.action="<%=request.getContextPath()%>/sysmanager/user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>";
	<%
		}
	%>
	document.OrgUserForm.submit();
}

function addall(){
	//调入所有用户
	var userIds = "";
	var flag1 = false;
	
	if(document.all("allist").options.length < 1){
		alert("没有用户可选");
		return;
	}
	
	var n = document.all("userIds").options.length-1;
	var p = document.all("allist").options.length-1
	for(var i = 0; i < document.all("allist").options.length; i++){
		var op = document.all("allist").options[i];
		if(flag1){
			userIds += "," + op.value;
		}else{
			userIds += op.value;
			flag1 = true;
		}
		//addone(op.text,op.value,n);
	}
	var orgId = "<%=oid%>";
	var flag = "1";
	var classType = "<%=classType%>";
	document.OrgUserForm.target="saveUser";
	<%
		if(ConfigManager.getInstance().getConfigBooleanValue("sys.user.enablemutiorg", true) && !"lisan".equals(classType)){
	%>
		outMsg = "是否保留所选用户与该机构的关系！（保留点确定；删除点取消）";
        if (confirm(outMsg)){
			document.OrgUserForm.action="<%=request.getContextPath()%>/sysmanager/user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>&hold=true";
		}else{
			document.OrgUserForm.action="<%=request.getContextPath()%>/sysmanager/user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>";
		}
	<%
		}else{
	%>
			document.OrgUserForm.action="<%=request.getContextPath()%>/sysmanager/user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>";
	<%
		}
	%>
	document.OrgUserForm.submit();
}

function deleteall(){
	alert("删除用户--");
}

function deleteuser(){
	alert("删除用户");
}

function okadd(){
	//alert("调入用户成功！");
	window.returnValue = "ok";
	parent.window.close();
}


</script>
		
</head>
		
	<body class="contentbodymargin" onunload="window.returnValue = 'ok'">
		<div id="contentborder" align="center">
			<center>
				<form name="OrgUserForm" action="" method="post">
					<table width="80%" border="0" cellpadding="0" cellspacing="1" class="table">
					<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" width="40%" align="center">
								机构下可选用户
							</td>
							<td width="20%" class="td" align="center">
								&nbsp;
							</td>
							<td width="40%" align="center" class="td">
								已存在该机构下的用户
							</td>
						</tr>
						<tr class="tabletop">
						    <td width="40%" align="center">&nbsp;</td>
						    <td width="20%" align="center">&nbsp;</td>
						    <td width="40%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" align="center" >
								<select class="select" name="allist"  style="width:90%" onDBLclick="addUser()" size="18">
									<pg:list requestKey="allUser">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname"/>(<pg:cell colName="userName"/>)
										</option>
									 </pg:list>
								</select>
							</td>

							<td align="center" class="td">
								<table class="table" width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr class="tr">
										<td align="center" class="td">
											<input name="button1" type="button" class="input" value="&gt;" onclick="addUser()">
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
									<!--
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
											<input name="button4" type="button" class="input" value="&lt;" onclick="deleteuser()">
		 								</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									-->
								</table>
							</td>
							<td class="td" align="center" >
								<select class="select" name="userIds"  style="width:90%" size="18">
									<pg:list requestKey="thisUser">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname"/>(<pg:cell colName="userName"/>)
										</option>
									 </pg:list>
								</select>

							</td>

						</tr>
						<tr class="tabletop">
						    <td  align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td colspan="3" class="td" align="center">
								

							</td>
						</tr>
						
					    <tr class="tr">
						    <td colspan="3" class="td" align="center">
							    
							    <input name="exit" type="button" class="input" value="返回" onclick="parent.window.close();">
    						</td>
					    </tr>
					</table>
				</form>
			</center>
		</div>
	</body>
	<iframe name="saveUser" width="" height=""></iframe>
</html>