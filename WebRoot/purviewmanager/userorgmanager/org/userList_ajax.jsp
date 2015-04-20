<%
/*
 * <p>Title: 用户调入操作的用户列表页面</p>
 * <p>Description: 用户调入操作的用户列表页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-21
 * @author liangbing.tao
 * @version 1.0
 */
 %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.sysmgrcore.purviewmanager.db.FunctionDB"%>

<%@ page
	import="com.frameworkset.platform.security.AccessControl,
	com.frameworkset.platform.menu.OrgUserTree,
	com.frameworkset.platform.sysmgrcore.entity.Job,
	com.frameworkset.platform.sysmgrcore.entity.User,
	com.frameworkset.platform.sysmgrcore.manager.JobManager,
	com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
	com.frameworkset.platform.sysmgrcore.manager.UserManager,java.util.*,
	com.frameworkset.common.poolman.DBUtil"%>
	
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
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
	String orgpath =  orgId == null ?"请选择调动用户所在机构":"调动用户所在机构:"+FunctionDB.buildOrgPath(orgId);
	
	//离散用户
	if("lisan".equals(request.getParameter("classType"))){
		List allUser = userManager.getDicList();
		request.setAttribute("allUser", allUser);
	}
%>
<html>
<head>
	<title>属性容器</title>
	<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
	<script language="javascript">
	var api = parent.frameElement.api, W = api.opener;
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
		
		for(var i=0;i<document.all("allist").options.length;i++){
			op = document.all("allist").options[i];
			
			if(op.selected){
				userIds = op.value;
				//addone(op.text,op.value,n);
			}
		}
		
		if(document.all("allist").options.length < 1){
			$.dialog.alert("<pg:message code='sany.pdp.no.chooseuser'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			return;
		}
		var orgId = "<%=oid%>";
		var flag = "1";
		var classType = "<%=classType%>";
		document.OrgUserForm.target="saveUser";
		//document.all.divProcessing.style.display = "block";
		//document.all.button1.disabled = true;
		//document.all.button2.disabled = true;
		//document.all.exit.disabled = true;
		document.OrgUserForm.action="../user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>";
		document.OrgUserForm.submit();
	}
	
	function addall(){
		//调入所有用户
		var userIds = "";
		var flag1 = false;
		
		if(document.all("allist").options.length < 1){
			$.dialog.alert("<pg:message code='sany.pdp.no.chooseuser'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
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
		//document.all.divProcessing.style.display = "block";
		//document.all.button1.disabled = true;
		//document.all.button2.disabled = true;
	//	document.all.exit.disabled = true;
		document.OrgUserForm.target="saveUser";
		document.OrgUserForm.action="../user/saveOrgUser.jsp?userId=" + userIds + "&orgId=" + orgId + "&flag=" + flag + "&classType=" + classType +"&CurorgId=<%=orgId%>";
		document.OrgUserForm.submit();
	}
	function alertfun(msg1,msg2)
	{
		$.dialog.alert(msg1,function(){},null,msg2);
	}
	
	function deleteall(){
		$.dialog.alert("<pg:message code='sany.pdp.delete.user'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	}
	
	function deleteuser(){
		$.dialog.alert("<pg:message code='sany.pdp.delete.user'/>",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
	}
	
	function okadd(){
		$.dialog.alert("<pg:message code='sany.pdp.common.success'/>！",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
		window.returnValue = "ok";
		parent.window.close();
	}
	</script>		
</head>
		
<body class="contentbodymargin" onunload="window.returnValue = 'ok'">
	<div class="titlebox">
					<SPAN class=location><A href="javascript:void(0)"><%=orgpath %></A></SPAN>
	</div>
	<div id="" align="center">
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
							<pg:message code="sany.pdp.choose.user"/>
						</td>
						<td width="20%" class="td" align="center">
							&nbsp;
						</td>
						<td width="40%" align="center" class="td">
							<pg:message code="sany.pdp.exist.user"/>
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
											<a class="bt_2"  onclick="addUser()"><span>&gt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a class="bt_2"  onclick="addall()"><span>&gt;&gt;</span></a>
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
						
					</table>
				</form>
			</center>
		</div>
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:190px;top:380px;display:none">
			<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
				<tr>
					<td bgcolor=#3A6EA5>
						<marquee align="middle" behavior="alternate" scrollamount="5">
							<font color=#FFFFFF><pg:message code="sany.pdp.common.operation.processing"/></font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
		<iframe width="0" height="0" frameborder=0 noResize scrolling="false" marginWidth=0 name="saveUser"></iframe>
	</body>
</html>
