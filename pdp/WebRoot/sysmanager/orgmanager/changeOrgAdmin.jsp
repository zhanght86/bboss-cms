<%
/**
 * 
 * <p>Title: 机构部门管理员设置页面</p>
 *
 * <p>Description: 机构部门管理员设置页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: bboss</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
 %>
 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ include file="../base/scripts/panes.jsp"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ page import="java.util.List,
				com.frameworkset.platform.sysmgrcore.manager.UserManager,
				com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
				com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase,
				com.frameworkset.platform.sysmgrcore.entity.User,
				com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(pageContext);
	
	String orgId = request.getParameter("orgId");	//选择的机构
	String orgId1 = request.getParameter("orgId1");	//设置机构管理员的机构
	String roleId = "3";	//部门管理员角色的ID:3
	
	UserManager userManager = SecurityDatabase.getUserManager();
	OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
	List allUser = userManager.getOrgUserList(orgId);	//获取当前机构的用户列表
	List existUser = orgAdministrator.getAdministorsOfOrg(orgId1);	//获取机构的本级部门管理员
	
	//两个列表都要屏蔽当前自身用户
	if((allUser!=null) && (allUser.size()>0))
	{
		for(int i=0;i<allUser.size();i++)
		{
			if(accessControl.getUserID().toString() == ((User)allUser.get(i)).getUserId().toString()
				|| accessControl.getUserID().toString().equals(((User)allUser.get(i)).getUserId().toString()))
			{
				allUser.remove(i);
			}
		}
	}
	if((existUser!=null) && (existUser.size()>0))
	{
		for(int i=0;i<existUser.size();i++)
		{
			if(accessControl.getUserID().toString() == ((User)existUser.get(i)).getUserId().toString()
				|| accessControl.getUserID().toString().equals(((User)existUser.get(i)).getUserId().toString()))
			{
				existUser.remove(i);
			}
		}
	}
	
	request.setAttribute("allUser", allUser);
	request.setAttribute("existUser", existUser);
%>
<html>
	<head>
		<title>部门管理员角色授予用户</title>
		<script language="JavaScript" src="../scripts/common.js" type="text/javascript"></script>
	<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
	<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">
		<SCRIPT LANGUAGE="JavaScript"> 
		
		function closethisdog()
		{
			window.returnValue = "okay";
			window.close();
		}
		
		function addUser(){	
      		var n=document.all("userIds").options.length-1;   	 	
      		var selectUsers = "";
   			for(var i=0;i<document.all("allist").options.length;i++){
   				var op=document.all("allist").options[i];
   				if(op.selected)
   				{
   				
   					if(addone(op.text,op.value,n))
   					{
	   					if(selectUsers == "")
	   					{
	   						selectUsers = op.value;   						
	   					}
	   					else
	   					{
	   						selectUsers += "," + op.value;
	   					}
	   				}
   					
   				}
  			}
  			if(selectUsers != "" )
  			{
  				send_request("<%=rootpath%>/sysmanager/orgmanager/changeOrgAdmin_do.jsp?userId="+selectUsers+"&orgId=<%=orgId%>&tag=add");
  			}
  			
  			//changebox();
		}
		function addone(name,value,n){
   			for(var i=n;i>=0;i--){
				if(value==document.all("userIds").options[i].value){
		  			return false;
				}
			}
   			var op=new Option(name,value);
   			document.all("userIds").add(op);
   			return true;
   			
   			
		}
		function deleteall(){
			var selectUsers = "";
			for (var m=document.all("userIds").options.length-1;m>=0;m--)
			{
				if(selectUsers == "")
				{
					selectUsers = document.all("userIds").options[m].value;   						
				}
				else
				{
					selectUsers += "," + document.all("userIds").options[m].value;
				}
				
				document.all("userIds").options[m]=null;//删除一个去掉一个option
			}
			if(selectUsers != "" )
			{
				send_request("<%=rootpath%>/sysmanager/orgmanager/changeOrgAdmin_do.jsp?userId="+selectUsers+"&orgId=<%=orgId%>&tag=delete");
			}
    			
    		//changebox();
		}      
		function addall(){
		
			var n=document.all("userIds").options.length-1;
			var p=document.all("allist").options.length-1;		  
			var selectUsers = "";
     		for(var i=document.all("allist").options.length-1;i>=0;i--){
     			var op=document.all("allist").options[i];
     			if(addone(op.text,op.value,n))  
   				{	
   					if(selectUsers == "")
   					{
   						selectUsers = op.value;   						
   					}
   					else
   					{
   						selectUsers += "," + op.value;
   					}
   				}
   			}
   			if(selectUsers != "" )
  			{
  				send_request("<%=rootpath%>/sysmanager/orgmanager/changeOrgAdmin_do.jsp?userId="+selectUsers+"&orgId=<%=orgId%>&tag=add");
  			}
   			//changebox();
		}
		function deleterole(){
			var selectUsers = "";
 			for (var m=document.all("userIds").options.length-1;m>=0;m--)
 			{
	      		if(document.all("userIds").options[m].selected)
	      		{
		      		if(selectUsers == "")
   					{
   						selectUsers = document.all("userIds").options[m].value;;   						
   					}
   					else
   					{
   						selectUsers += "," + document.all("userIds").options[m].value;;
   					}
	      			
	      			document.all("userIds").options[m]=null;
	      		}
	      	}
	      	if(selectUsers != "")
	      	{
	      		send_request("<%=rootpath%>/sysmanager/orgmanager/changeOrgAdmin_do.jsp?userId="+selectUsers+"&orgId=<%=orgId%>&tag=delete");
	      	}
         			
         	//changebox();
		}

//---------------------------------------
		var http_request = false;
		//function send_request(url){
		//	http_request = false;
		//	//开始初始化XMLHttpRequest对象
		//	if(window.XMLHttpRequest){//Mozilla
		//		http_request = new XMLHttpRequest();
		//		if(http_request.overrideMimeType){//设置MIME类别
		//			http_request.overrideMimeType("text/xml");						
		//		}
		//	}
		//	else if(window.ActiveXObject){//IE
		//		try{
		//			http_request = new ActiveXObject("Msxml2.XMLHTTP");
		//		}catch(e){
		//			try{
		//				http_request = new ActiveXObject("Microsoft.XMLHTTP");							
		//			}catch(e){
		//			}
		//		}
		//	}
		//	if(!http_request){
		//		alert("不能创建XMLHttpRequest对象");
		//		return false;
		//	}
		//	http_request.onreadystatechange = processRequest;
		//	http_request.open("GET",url,true);
		//	http_request.send(null);
		//}
		
		function send_request(url){
			document.all.divProcessing.style.display = "block";
			document.OrgAdminForm.action = url;
			document.OrgAdminForm.target = "hiddenFrame";
			document.OrgAdminForm.submit();
		}
			
		function processRequest(){
			if(http_request.readyState == 4){
				//alert(http_request.status);
				if(http_request.status == 200){
					//alert(http_request.responseText);
				}
				else{
					alert("对不起，服务器错误");
				}
			}
		}
			
		function changebox(tag, userIds){				 
		   	 var len=document.all("userIds").options.length;		
    		 var roleId="<%=roleId%>";
    		 var orgId="<%=orgId%>";
    		 var userIds=new Array(len)
    		 for (var i=0;i<len;i++){	      
    		   userIds[i]=document.all("userIds").options[i].value;
     		 }    
     		 //需要修改        		
			 send_request('<%=rootpath%>/sysmanager/orgmanager/changeOrgAdmin_do.jsp?roleId='+roleId+'&userId='+userIds+'&orgId='+orgId);
		}
</SCRIPT>
	<body class="contentbodymargin">
		<div id="contentborder" align="center">
			<center>
				<form name="OrgAdminForm" action="" method="post">
					<table class="table">
						<tr>
						 	<td align=center nowrap>
						 		<font color=red>以下操作列表都不显示当前用户<font>
						 		<br>
						 	</td>
						</tr>
					</table>
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table">
						<tr class="tabletop">
						    <td width="45%" align="center"></td>
						    <td width="10%" align="center">&nbsp;</td>
						    <td width="45%" align="center">&nbsp;</td>
						<tr class="tr">
							<td class="td" width="45%" align="center">
								可选用户
							</td>
							<td width="10%" class="td" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center" class="td">
								已授予用户
							</td>
						</tr>
						<tr class="tabletop">
						    <td width="45%" align="center">&nbsp;</td>
						    <td width="10%" align="center">&nbsp;</td>
						    <td width="45%" align="center">&nbsp;</td>
						  </tr>
						<tr class="tr">
							<td class="td" align="center" >
								<select class="select" name="allist" multiple style="width:100%" onDBLclick="addUser()" size="18">
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
							<td class="td" align="center" >
								<select class="select" name="userIds" multiple style="width:100%" onDBLclick="deleterole()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />(<pg:cell colName="userName" />)
										</option>
									</pg:list>
								</select>
							</td>
						</tr>
					</table>
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table">
					<tr class="tr">
						<td align="center" class="td">
							<br>
							<input name="button5" type="button" class="input" value="关闭" onclick="closethisdog()">
						</td>
					</tr>
					</table>
					<input type="hidden" name="orgId" value="<%=orgId%>" />
					<input type="hidden" name="orgId1" value="<%=orgId1%>">
				</form>
			</center>
		</div>
	<div id=divProcessing style="width:200px;height:30px;position:absolute;left:120px;top:360px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
	    <tr>
		    <td bgcolor=#3A6EA5>
			    <marquee align="middle" behavior="alternate" scrollamount="5">
				    <font color=#FFFFFF>...处理中...请等待...</font>
				</marquee>
			</td>
		</tr>
	</table>
	</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>	
</html>
