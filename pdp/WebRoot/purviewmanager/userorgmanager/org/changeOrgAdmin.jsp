<%
/**
 * <p>Title: 机构管理员设置页面</p>
 * <p>Description: 机构管理员设置页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%@ page
	import="java.util.List
		   ,com.frameworkset.platform.sysmgrcore.manager.UserManager
		   ,com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator
		   ,com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl
		   ,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
		   ,com.frameworkset.platform.sysmgrcore.entity.User
		   ,com.frameworkset.platform.security.AccessControl"%>
<%AccessControl accessControl = AccessControl.getInstance();
			accessControl.checkManagerAccess(request,response);

			String orgId = request.getParameter("orgId"); //选择的机构
			String orgId1 = request.getParameter("orgId1"); //设置机构管理员的机构
			String roleId = "3"; //部门管理员角色的ID:3

			UserManager userManager = SecurityDatabase.getUserManager();
			OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
			List allUser = userManager.getOrgUserList(orgId); //获取当前机构的用户列表
			List existUser = orgAdministrator.getAdministorsOfOrg(orgId1); //获取机构的本级部门管理员

			//两个列表都要屏蔽当前自身用户
			if ((allUser != null) && (allUser.size() > 0)) {
				for (int i = 0; i < allUser.size(); i++) {
					if (accessControl.getUserID().toString() == ((User) allUser
							.get(i)).getUserId().toString()
							|| accessControl.getUserID().toString().equals(
									((User) allUser.get(i)).getUserId()
											.toString())) {
						allUser.remove(i);
					}
				}
			}
			if ((existUser != null) && (existUser.size() > 0)) {
				for (int i = 0; i < existUser.size(); i++) {
					if (accessControl.getUserID().toString() == ((User) existUser
							.get(i)).getUserId().toString()
							|| accessControl.getUserID().toString().equals(
									((User) existUser.get(i)).getUserId()
											.toString())) {
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
		<SCRIPT LANGUAGE="JavaScript"> 
		var api = parent.frameElement.api, W = api.opener;
		function closethisdog()
		{
			window.returnValue = "okay";
			window.close();
		}
		
		function addUser(){	
      		var n=document.all("userIds").options.length-1;   	 	
      		
   			for(var i=0;i<document.all("allist").options.length;i++){
   				var op=document.all("allist").options[i];
   				if(op.selected)
   				{
   					addone(op.text,op.value,n);
   				}
  			}
   			changebox();
		}
		function addone(name,value,n){
   			for(var i=n;i>=0;i--){
				if(value==document.all("userIds").options[i].value){
		  			return false;
				}
			}
   			var op=new Option(name,value);
   			document.all("userIds").add(op);
   			
		}
		function deleteall(){
			if(!'<%=orgId1%>'){
				W.$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return;
			}
			var length = document.all("userIds").options.length;
			var i = 0;
			var userId = new Array(length);
			for (var m=length-1;m>=0;m--)
			{
				userId[i++] = document.all("userIds").options[m].value;
				
				document.all("userIds").options[m]=null;//删除一个去掉一个option
			}
			
			if(i > 0)
			{
				send_request("changeOrgAdmin_do.jsp?userId="+userId+"&orgId=<%=orgId1%>&tag=delete");
			}
			else
				{
				W.$.dialog.alert("<pg:message code='sany.pdp.personcenter.person.select.one'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return ;
				}
    		
		}  
		
		function addall(){
		
			var n=document.all("userIds").options.length-1;
			var p=document.all("allist").options.length-1;		  
			
     		for(var i=0;i<document.all("allist").options.length;i++){
     			var op=document.all("allist").options[i];
     			addone(op.text,op.value,n);
   			}
   			changebox();
		}
		
		function deleterole(){
			if(!'<%=orgId1%>'){
				W.$.dialog.alert("<pg:message code='sany.pdp.afterchooseoption'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return;
			}
			var leng = document.all("userIds").options.length;
			var i = 0;
			var userId = new Array();
 			for (var m=leng-1;m>=0;m--)
 			{
	      		if(document.all("userIds").options[m].selected)
	      		{
	      			userId[i++] = document.all("userIds").options[m].value;
	      			document.all("userIds").options[m]=null;
	      		}
	      	}
 			
	      	if(i > 0)
	      	{
	      		send_request("changeOrgAdmin_do.jsp?userId="+userId+"&orgId=<%=orgId1%>&tag=delete");
	      	}
	      	else
	      	{
					W.$.dialog.alert("<pg:message code='sany.pdp.personcenter.person.select.one'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
					return ;
			}
		}
		
		function changebox(){				 
			var len=document.all("userIds").options.length;			  	 	
		    var userId=new Array(len);
		    var orgId = '<%=orgId1%>';
		    for (var i=0;i<len;i++){	      
		    	userId[i]=document.all("userIds").options[i].value;
		    }  
		    if(len > 0)
		    {     
		    			
		    	send_request("changeOrgAdmin_do.jsp?userId="+userId+"&orgId=<%=orgId1%>&tag=add");
			}
			else
			{
				W.$.dialog.alert("<pg:message code='sany.pdp.personcenter.person.select.one'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
				return ;
			}
		}

//---------------------------------------
		//var http_request = false;
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
				
					http_request = false;
					if(window.XMLHttpRequest){//Mozilla
							http_request = new XMLHttpRequest();
							if(http_request.overrideMimeType){//??MIME??
								http_request.overrideMimeType("text/xml");						
								}
							}
							else if(window.ActiveXObject){//IE
								try{
									http_request = new ActiveXObject("Msxml2.XMLHTTP");
								}catch(e){
								try{
									http_request = new ActiveXObject("Microsoft.XMLHTTP");							
								}catch(e){
								}
								}
							}
							if(!http_request){
								return false;
							}
							http_request.onreadystatechange = processRequest;
							http_request.open("GET",url,true);
							http_request.send(null);
							}
			
	function processRequest(){
		if(http_request.readyState == 4)
		{				
			if(http_request.status == 200)
			{		
				W.$.dialog.alert("<pg:message code='sany.pdp.common.operation.success'/>!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			else
			{
				W.$.dialog.alert("处理失败，请联系管理!",function(){},null,"<pg:message code='sany.pdp.common.alert'/>");
			}
			//document.all("button1").disabled = false;
			//document.all("button2").disabled = false;
			//document.all("button3").disabled = false;
			//document.all("button4").disabled = false;
			
			//divProcessing.style.display="none";
			
		}else
		{
			//document.all("button1").disabled = true;
		//	document.all("button2").disabled = true;
		//	document.all("button3").disabled = true;
			//document.all("button4").disabled = true;
			
			//divProcessing.style.display="";
			
		}	
	}
			
		
</SCRIPT>
	<body class="contentbodymargin">
		<div id="" align="center">
			<center>
				<form name="OrgAdminForm" action="" method="post">
					<table width="100%" border="0" cellpadding="0" cellspacing="1" class="table">
						<tr class="tabletop">
							<td width="45%" align="center"></td>
							<td width="10%" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center">
								&nbsp;
							</td>
						<tr class="tr">
							<td class="td" width="45%" align="center">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.can"/>
							</td>
							<td width="10%" class="td" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center" class="td">
								<pg:message code="sany.pdp.purviewmanager.rolemanager.role.authorize.to.user.had"/>
							</td>
						</tr>
						<tr class="tabletop">
							<td width="45%" align="center">
								&nbsp;
							</td>
							<td width="10%" align="center">
								&nbsp;
							</td>
							<td width="45%" align="center">
								&nbsp;
							</td>
						</tr>
						<tr class="tr">
							<td class="td" align="center">
								<select class="select" name="allist" multiple style="width:100%" onDBLclick="addUser()" size="18">
									<pg:list requestKey="allUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />
											(
											<pg:cell colName="userName" />
											)
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
									<tr class="tr">
										<td align="center" class="td">
											<a class="bt_2"  onclick="deleteall()"><span>&lt;&lt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											<a class="bt_2"  onclick="deleterole()"><span>&lt;</span></a>
										</td>
									</tr>
									<tr class="tr">
										<td align="center" class="td">
											&nbsp;
										</td>
									</tr>
								</table>
							</td>
							<td class="td" align="center">
								<select class="select" name="userIds" multiple style="width:100%" onDBLclick="deleterole()" size="18">
									<pg:list requestKey="existUser" needClear="false">
										<option value="<pg:cell colName="userId"/>">
											<pg:cell colName="userRealname" />
											(
											<pg:cell colName="userName" />
											)
										</option>
									</pg:list>
								</select>
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
							<font color=#FFFFFF>...处理中...请等待...</font>
					</td>
				</tr>
			</table>
		</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>

