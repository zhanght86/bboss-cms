

<%@page contentType="text/html; charset=UTF-8"%>

<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.util.*" %>
<%@page import="com.frameworkset.platform.cms.sitemanager.*" %>
<%@page import="com.frameworkset.platform.cms.flowmanager.*" %>
<%@page import="com.frameworkset.platform.cms.templatemanager.*" %>
<%@page import="com.frameworkset.common.poolman.DBUtil" %>
<%@page import="com.frameworkset.platform.cms.container.Template" %>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl,
				com.frameworkset.platform.sysmgrcore.entity.Organization,
				com.frameworkset.platform.sysmgrcore.entity.RoleType,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl" %>
<%@page import="java.util.*" %>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.RoleTypeManager" %>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>


<%    
    response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);


	AccessControl control = AccessControl.getInstance();
	control.checkAccess(request,response);	
	
	OrgAdministratorImpl orgAdministratorImpl = new OrgAdministratorImpl();
	//得到当前登陆用户的所在机构列表
	String userId = control.getUserID();
	OrgManagerImpl orgManagerImpl = new OrgManagerImpl();
	List userOrgList = orgManagerImpl.getOrgListOfUser(userId);
	
	
	String userType = request.getParameter("userType");
	if(userType==null||userType.equals("")){//默认为内部用户
	   userType="0";
	}
	
	String QueryuserName = null;
	if(request.getParameter("userName")==null){
		QueryuserName = "";
	}else{
		QueryuserName = request.getParameter("userName");
	}
	
	String QueryroleName = null;
	if(request.getParameter("roleName")==null){
		QueryroleName = "";
	}else{
		QueryroleName = request.getParameter("roleName");
	}
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
	<script src="../inc/js/func.js"></script>
	<tab:tabConfig/>
	<title>用户管理</title>

<script language="javascript">
	var isAdminOrSelf = "<%=request.getParameter("isAdminOrSelf")%>";
	if(isAdminOrSelf == "true")
	{
		alert("不能对系统管理员或部门管理员或自己进行操作！");
		window.location.href="main.jsp";
	}
	
	function changeType(){
	   
		form1.atcion="main.jsp?userType="+form1.userType.value;
		form1.submit();
		
		//window.location.href = "main.jsp?userType="+form1.userType.value;
		
	}

	function queryUser(id)
	{
		form1.action="main.jsp?id="+id;
		form1.submit();	 
	}
	
	function addUser(){
		openWin('../userManage/addUser.jsp?userType='+form1.userType.value,screen.availWidth-200,screen.availHeight-200);
		window.location.href = window.location.href;
	}
	
	 function execute(dealType){
	 	var isSelect = false;
	    var outMsg;
		var status = document.all.item("intervalType").value;
		
		//alert(status); 
	    for (var i=0;i<form1.elements.length;i++) {
			var e = form1.elements[i];
				
			if (e.name == 'checkBoxOne'){
				if (e.checked){
		       		isSelect=true;
		       		
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		if(status=="-1"){
					alert("请选择操作项！");
					return;
				}
	    		outMsg = "你确定要执行所选择的操作吗？";
	        	if (confirm(outMsg)){
					form1.action="../userManage/userManager.do?method=userStatus&status="+status;
					form1.submit();
		 			return true;
				}
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;	
	 }
	
	 function checkAll(totalCheck,checkName){	
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	
	 function onShowUserInfo(userId){
	 	form1.action=openWin("../userManage/userManager.do?method=getUser&userId="+userId,screen.availWidth-300,screen.availHeight-100);
		form1.submit();
	 
	 }
   //-----------------------------------------------------------------------------------------------------------------------------------------	
   function checkAll2(totalCheck,checkName){	
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
	   	   }
	   }
	}
	function queryRole()
	{	
		form2.action="../userManage/main.jsp"
		form2.submit();	
	}
	function selectuser(roleId)
	{	
		form2.action="../userManage/selectuser.jsp?roleId="+roleId;
		form2.submit();	
	}
	function purviewRole(roleId)
	{	
		//form2.action="openWin('../userManage/resFrame.jsp?roleId='+roleId,screen.availWidth-600,screen.availHeight-300)";
		form2.action="../userManage/userResFrame.jsp?userId="+roleId;
		form2.submit();	
	}
	function deleteRole(dealType) {
	    var isSelect = false;
	    var outMsg;
		    
	    for (var i=0;i<form2.elements.length;i++) {
			var e = form2.elements[i];
				
			if (e.name == 'ID'){
				if (e.checked){
		       		isSelect=true;
		       		
		       		break;
			    }
			}
	    }
	    if (isSelect){
	    	if (dealType==1){
	    		outMsg = "你确定要删除选定角色吗？(删除后是不可以再恢复的)。";
	        	if (confirm(outMsg)){
					form2.action="../userManage/deleteRole.jsp";
					form2.submit();
		 			return true;
				}
			} 
	    }else{
	    	alert("至少要选择一条记录！");
	    	return false;
	    }
		return false;
	}
	
	function popedomSet(roleId){
		if(roleId=="1"){
			alert("此角色是系统管理员，不能进行此项操作！");
			return false;
		}else if(roleId=="3"){
			alert("此角色是机构管理员，不能进行此项操作！");
			return false;
		}else{
			openWin('resFrame.jsp?roleId='+roleId,screen.availWidth-50,screen.availHeight-100);
			return true;
		}
	}
	
	function grantRole(userId)
	{
		var flag = openWin("../../sysmanager/user/userRole.do?method=getRoleList&userId=" + userId ,screen.availWidth-300,screen.availHeight-100) ;
		if(flag == "ok")
		{
			window.location.href = window.location.href;
		}
	}
  
</script>
</head>
<body  bgcolor="#F7F8FC" >

	<table width="90%" border="0" cellpadding="0" cellspacing="0" align="center">
	    <tr height="10">
	       <td>
	       </td>
	    </tr>
		<tr >
			<td colspan="2" class="topDIV"  align="left" valign="top">
					<FONT color="#8080c0">用户权限管理</FONT>
				</td>
		</tr>
		<tr height="20">
	       <td>
	       </td>
	    </tr>
		<tr>
			<td colspan="2">
				<tab:tabContainer id="user-manage-update" selectedTabPaneId="user-manage">
					<tab:tabPane id="user-manage" tabTitle="用户管理">
					 <FORM id="form1" name="form1" method="post" onSubmit="">
					    <table  width="100%" height="30" >
					          <tr>
					          	<td align="left">
									用户类型：
									<dict:select extend="onchange=changeType()" defaultValue="<%=userType%>" type="userType" name="userType" expression="{userType}"  />
									
									<!--
					          		<select name="userType" class="cms_select" onChange="changeType()" >
						                <option value="0" >
						                    系统用户
					                   </option>
						               <option value="1" <%if(userType.equals("1")){%>selected<%}%>>
						                    外部会员
					                   </option>					                   
					                </select>
					                -->					                
					          	</td>
					          </tr>
						      <tr>
						      
						         <td  align="right">
						             <FONT color="#8080c0">查询::::</FONT>用户名：
						             <input name="userName" type="text" size="20" value="<%=QueryuserName%>">
	                                 <input name="search" type="button" class="cms_button" value="查询" onClick="queryUser('6')">
<!--	                                 &nbsp;&nbsp;&nbsp;&nbsp;-->
<!--	                                 <input type="Button" name="add"  class="cms_button" value="添加用户" onClick="addUser()">-->
	                             </td>
	                           </tr>
	                           <tr>
	                           		<td  height="10" align=right >
						  			    <a style="cursor:hand" onClick="queryUser('7')">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">全部用户</div></a>
										<a style="cursor:hand" onClick="queryUser('2')">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">已开通</div></a>
										<a style="cursor:hand" onClick="queryUser('1')">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">新申请</div></a>
										<a style="cursor:hand" onClick="queryUser('3')">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">已停用</div></a>
										<a style="cursor:hand" onClick="queryUser('0')">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">已删除</div></a>
						  			    <a style="cursor:hand" onClick="queryUser('0')">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">离散用户</div></a>						  			    
						  			    <a style="cursor:hand" onClick="addUser()">						  			   
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">添加用户</div></a>
										
										</td>
	                           </tr>
						    </table>
					    
					    
					    <hr width="100%">
						<table width="100%" border="0" cellpadding="3" cellspacing="0" class="Templateedittable">
					    <pg:listdata dataInfo="User_List" keyName="User_List"/>
		                    <!--分页显示开始,分页标签初始化-->
		                    <pg:pager maxPageItems="10" scope="request" data="User_List" isList="false">
		                    <pg:param name="userName"/>
		                    <pg:param name="id"/>
		                    <pg:param name="userType"/>
							<tr>
								<td width="8%" height="24"   bgcolor="#EDEFF6">
								<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')">
								全选</td>
								<td width="10%" height="24"   bgcolor="#EDEFF6">用户名</td>
								<td width="10%" height="24"   bgcolor="#EDEFF6">当前状态</td>
								<td width="12%" height="24"   bgcolor="#EDEFF6">真实姓名</td>
								<td width="15%" height="24"   bgcolor="#EDEFF6">组织机构</td>
								<td width="13%" height="24"   bgcolor="#EDEFF6">申请时间</td>
								<td width="13%" height="24"   bgcolor="#EDEFF6">开通时间</td>
								<td width="4%" height="24"   bgcolor="#EDEFF6">权限</td>
								<td width="4%" height="24"   bgcolor="#EDEFF6">角色</td>
								<td width="7%" height="24"   bgcolor="#EDEFF6">隶属机构</td>
								<td width="4%" height="24"   bgcolor="#EDEFF6">编辑</td>								
							</tr>
							
		                    <!--检测当前页面是否有记录-->
		       	            <pg:notify>
			                <tr height="18px" class="labeltable_middle_tr_01">
			      	          <td colspan=100 align='center'>暂时没有用户</td>
			                </tr>
			               </pg:notify>			      
			      			    
			               <!--list标签循环输出每条记录-->			      
			                <pg:list>	
			                
			               <% //显示用户的身份
			                  //baowen.liu 2008-4-25
			                  String  userid =dataSet.getString("userId");
			                  boolean isOrgManager = orgAdministratorImpl.isOrgAdmin(userid,"");
			                  boolean isRoleAdmin = control.isAdminByUserid(userid);
			                  String isRoleAdminOrOrgManager = "";//是否拥有超级管理员角色与部门管理员角色
							if(isOrgManager && isRoleAdmin){
								isRoleAdminOrOrgManager = "(管理员；超级管理员角色)";
							}else if(isRoleAdmin){
								isRoleAdminOrOrgManager = "(超级管理员角色)";
							}else if(isOrgManager){
								isRoleAdminOrOrgManager = "(管理员)";
							}
			                %>
						     <tr class="cms_data_tr" id="<pg:cell colName="userId" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
						      
						        <td height='25' class="tablecells" >
						        <P align="left">
						        <input onClick="" type="checkbox" name="checkBoxOne" value="<pg:cell colName="userId" defaultValue=""/>">
						        </P>
						        </td>		      				
<!--							    <td height='25' class="tablecells" nowrap="nowrap">-->
<!--							    	<pg:cell colName="userName" defaultValue=""/>-->
<!--							    </td>-->

							    <td align=left onclick="var isOk = openWin('../userManage/userManager.do?method=getUser&userId=<pg:cell colName="userId" defaultValue=""/>',screen.availWidth-300,screen.availHeight-200); if(isOk=='ok'){changeType();}" style="cursor:hand">
								    <a class="BlackLine"  class="Black">
								    	<pg:cell colName="userName" defaultValue=""/> 
								    		<%=isRoleAdminOrOrgManager%>
								    </a>	
							    </td>
							    
							    <td height='25' class="tablecells">		
							    	 <dict:itemname type="isvalid" expression="{userIsvalid}" />	    
							    	 <!--     
							         <pg:equal colName="userIsvalid" value="0">已删除</pg:equal>
							         <pg:equal colName="userIsvalid" value="1">新申请</pg:equal>
							         <pg:equal colName="userIsvalid" value="2">已开通</pg:equal>
							         <pg:equal colName="userIsvalid" value="3">停用</pg:equal>
							         -->
							     </td>
							    <td height='25' class="tablecells"><pg:cell colName="userRealname" defaultValue=""/></td>
							    <td height='25' class="tablecells"><pg:cell colName="orgs" defaultValue=""/></td>
							    <td height='25' class="tablecells"><pg:cell colName="user_Regdate" defaultValue=""/></td>
							    <td height='25' class="tablecells"><pg:cell colName="dredgeTime" defaultValue=""/></td>
							    
							    <td height='25' class="tablecells">
							    <%
				  			    boolean userressetTag = false;
				  			    for(int i=0;i<userOrgList.size();i++)
				  			    {
				  			    	Organization org = (Organization)userOrgList.get(i);
				  			    	if(control.checkPermission(org.getOrgId(),"userresset",AccessControl.ORGUNIT_RESOURCE))
				  			    	{
				  			    		userressetTag = true;
				  			    		break;
				  			    	}
				  			    }
								if(control.isAdmin() || userressetTag)
								{
								//onClick="openWin('../userManage/userResFrame.jsp?userId=<pg:cell colName="userId" defaultValue="" />',screen.availWidth-50,screen.availHeight-100)"
								%>	
							    	<a style="cursor:hand" onClick="openWin('../userManage/userResFrame.jsp?userId=<pg:cell colName="userId" defaultValue="" />',screen.availWidth-50,screen.availHeight-100)">
							    	<IMG src="../images/new_doc.gif" border="0">
								<%
								}
								%>
							    </td>
							    
							    <td height='25' class="tablecells">
							    <%
				  			    boolean userrolesetTag = false;
				  			    for(int i=0;i<userOrgList.size();i++)
				  			    {
				  			    	Organization org = (Organization)userOrgList.get(i);
						  			if(control.checkPermission(org.getOrgId(),"userroleset",AccessControl.ORGUNIT_RESOURCE))
				  			    	{
				  			    		userrolesetTag = true;
				  			    		break;
				  			    	}
				  			    }
								if(control.isAdmin() || userrolesetTag)
								{
								%>
						    		<a style="cursor:hand" onClick="grantRole(<pg:cell colName="userId" defaultValue="" />)">
						    		<IMG src="../images/new_doc.gif" border="0">
							    <%
								}
								%>
							    </td>
							    
							    <td height='25' class="tablecells">
							    <%
				  			    boolean userorgsetTag = false;
				  			    for(int i=0;i<userOrgList.size();i++)
				  			    {
				  			    	Organization org = (Organization)userOrgList.get(i);
						  			if(control.checkPermission(org.getOrgId(),"userorgset",AccessControl.ORGUNIT_RESOURCE))
				  			    	{
				  			    		userorgsetTag = true;
				  			    		break;
				  			    	}
				  			    }
								if(control.isAdmin() || userorgsetTag)
								{
								%>
						    		<a style="cursor:hand" onClick="openWin('userOrg_iframe.jsp?userId=<pg:cell colName="userId" defaultValue=""/>',screen.availWidth-300,screen.availHeight-100)">
						    		<IMG src="../images/new_doc.gif" border="0">
						    	<%
								}
								%>
							    </td>
							    
							    <td height='25' class="tablecells">
							    <%
				  			    boolean edituserTag = false;
				  			    for(int i=0;i<userOrgList.size();i++)
				  			    {
				  			    	Organization org = (Organization)userOrgList.get(i);
						  			if(control.checkPermission(org.getOrgId(),"edituser",AccessControl.ORGUNIT_RESOURCE))
				  			    	{
				  			    		edituserTag = true;
				  			    		break;
				  			    	}
				  			    }
								if(control.isAdmin() || edituserTag)
								{
								%>  
						    		<a style="cursor:hand" onClick="var isOk = openWin('../userManage/userManager.do?method=getUser&userId=<pg:cell colName="userId" defaultValue=""/>',screen.availWidth-300,screen.availHeight-100); if(isOk=='ok'){changeType();}">
						    		<IMG src="../images/new_doc.gif" border="0">
						    	<%
								}
								%>
							    </td>
							    
					  	      </tr>			      		
			                </pg:list>	
			                </table>
			                <table align=center width="100%">
			                <tr height="18px" class="labeltable_middle_tr_01">
			      	          <td colspan=4 align='center'><pg:index/></td>			      	          
			                </tr>
			                 <tr height="18px" class="labeltable_middle_tr_01">
			      	          <td colspan=4 align='right'>操作：
			      	          	<select name="intervalType" class="cms_select">
						                <option value="-1" >
						                    请选择
					                   </option>
					                   
					                   	<%
						  			    boolean useropenaccountTag = false;
						  			    for(int i=0;i<userOrgList.size();i++)
						  			    {
						  			    	//Organization org = (Organization)userOrgList.get(i);
						  			    	if(control.checkPermission(userOrgList.get(i).toString(),"useropenaccount",AccessControl.ORGUNIT_RESOURCE))
						  			    	{
						  			    		useropenaccountTag = true;
						  			    		break;
						  			    	}
						  			    }
										if(control.isAdmin() || useropenaccountTag)
										{
										%>
						               <option value="2" >
						                    开通帐号
					                   </option>
					                   <%
										}
										%>
					                   
					                   <%
						  			    boolean usercloseaccountTag = false;
						  			    for(int i=0;i<userOrgList.size();i++)
						  			    {
						  			    	//Organization org = (Organization)userOrgList.get(i);
						  			    	if(control.checkPermission(userOrgList.get(i).toString(),"usercloseaccount",AccessControl.ORGUNIT_RESOURCE))
						  			    	{
						  			    		usercloseaccountTag = true;
						  			    		break;
						  			    	}
						  			    }
										if(control.isAdmin() || usercloseaccountTag)
										{
										%>
					                   <option value="3" >
						                    停用账号
					                   </option>
					                   <%
										}
										%>
					                   
					                   <%
						  			    boolean cmsdeleteuserTag = false;
						  			    for(int i=0;i<userOrgList.size();i++)
						  			    {
						  			    	//Organization org = (Organization)userOrgList.get(i);
						  			    	if(control.checkPermission(userOrgList.get(i).toString(),"cmsdeleteuser",AccessControl.ORGUNIT_RESOURCE))
						  			    	{
						  			    		cmsdeleteuserTag = true;
						  			    		break;
						  			    	}
						  			    }
										if(control.isAdmin() || cmsdeleteuserTag)
										{
										%>
					                   <option value="0" >
						                    删除用户
					                   </option>
					                   <%
										}
										%>
					                   
					                   <%
						  			    boolean deleteuserTag = false;
						  			    for(int i=0;i<userOrgList.size();i++)
						  			    {
						  			    	//Organization org = (Organization)userOrgList.get(i);
						  			    	if(control.checkPermission(userOrgList.get(i).toString(),"deleteuser",AccessControl.ORGUNIT_RESOURCE))
						  			    	{
						  			    		deleteuserTag = true;
						  			    		break;
						  			    	}
						  			    }
										if(control.isAdmin() || deleteuserTag)
										{
										%>
					                   <option value="4" >
						                    彻底删除
					                   </option>
					                    <%
										}
										%>
					                   
					                </select>
									<input name="search" type="button" class="cms_button" value="执行" onClick="javascript:execute(1); return false;">
			      	          </td>			      	          
			                </tr>   
			    	    </table>
					   </pg:pager>
					   </FORM>
					</tab:tabPane>
<!-- -------------------------------------------------------------------------------------------------------------------------------------------	-->
					<tab:tabPane id="group-manage" tabTitle="机构管理" lazeload="true">
						<tab:iframe id="groupmanage" src="../orgManage/org_main.jsp" frameborder="0" scrolling="yes" width="100%" height="520">
						</tab:iframe>
					</tab:tabPane>
					<tab:tabPane id="res-manage" tabTitle="资源管理" lazeload="true">
						<tab:iframe id="resmanage" src="../resManager/res_main.jsp" frameborder="0" scrolling="yes" width="100%" height="520">
						</tab:iframe>
					</tab:tabPane>
<!-------------------------------------------------------------------------------------------------------------------------------->
					<tab:tabPane id="role-manage" tabTitle="角色管理">
					  <FORM id="form2" name="form2" method="post" onSubmit="">
						 <table  width="100%" height="30" >
						      <tr>
						      
						         <td  align="right">
						             <FONT color="#8080c0">查询::::</FONT>角色名：
						             <input name="roleName" type="text" size="20" class="cms_text" value=<%=QueryroleName%>>
	                                 <input name="search" type="button" class="cms_button" value="查询" onClick="queryRole()">
	                             </td>
	                           </tr>
	                           <tr>
	                           		<td  height="10" align=right >
	                           			
	                           			<%
										ResourceManager resManager = new ResourceManager();
										String resId = resManager.getGlobalResourceid(AccessControl.ROLE_RESOURCE);
										if (resId != null && !resId.equals("") && control.checkPermission(resId,
	                       							"add", AccessControl.ROLE_RESOURCE))
										{
										%>
						  			    <a style="cursor:hand" onClick="openWin('../userManage/addRole.jsp?flag=0',screen.availWidth-600,screen.availHeight-300)">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">新建角色</div></a>
										<%}%>
										
										<%
										resManager = new ResourceManager();
										resId = resManager.getGlobalResourceid(AccessControl.ROLE_RESOURCE);
										if (resId != null && !resId.equals("") && control.checkPermission(resId,
	                       							"cmsdelete", AccessControl.ROLE_RESOURCE))
										{
										%>
										<a style="cursor:hand" onClick="javascript:deleteRole(1); return false;">
						  			    <div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">删除</div></a>
						  			    <%}%>

						 			</td>
	                           </tr>
						    </table>
					
						<hr width="100%">
						<table width="100%" border="0" cellpadding="3" cellspacing="0" class="Templateedittable">
						    <pg:listdata dataInfo="InnerRoleList" keyName="InnerRoleList" />
							<!--分页显示开始,分页标签初始化-->
							<pg:pager maxPageItems="10" scope="request" data="InnerRoleList" isList="false">
							<tr>
								<td width="10%" height="24"   bgcolor="#EDEFF6">
									<input type="checkBox" name="checkBoxAll2" onClick="checkAll2('checkBoxAll2','ID')">全选
								</td>
								<td width="15%" height="24"   bgcolor="#EDEFF6">名称</td>
								<td width="25%" height="24"   bgcolor="#EDEFF6">描述</td>
								<td width="15%" height="24"   bgcolor="#EDEFF6">角色类型</td>
								<td width="10%" height="24"   bgcolor="#EDEFF6">用户成员</td>
								<td width="10%" height="24"   bgcolor="#EDEFF6">权限</td>
							</tr>
							<pg:param name="roleId" />
							<pg:param name="roleName" />
							<!--检测当前页面是否有记录-->
							<pg:notify>
								<tr height="18px" class="labeltable_middle_tr_01">
									<td colspan=100 align='center'>
										暂时没有角色
									</td>
								</tr>
							</pg:notify>	
								
								<!--list标签循环输出每条记录-->
							<pg:list>
								<pg:param name="roleId"/>
								<tr class="cms_data_tr" id="<pg:cell colName="roleId" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
									<td height='25' class="tablecells" nowrap="nowrap">
										<%
											String idValue = dataSet.getString("roleId");
											
										%>
										<input onClick="" type="checkbox" name="ID" value="<pg:cell colName="roleId" defaultValue=""/>" <%if("1".equals(idValue) || "2".equals(idValue) || "3".equals(idValue) || "4".equals(idValue)){%>disabled="true"<%}%>>
									</td>
									<td height='25' class="tablecells" nowrap="nowrap">

										<a style="cursor:hand" onClick="openWin('../userManage/addRole.jsp?roleId=<pg:cell colName="roleId" defaultValue="" />&flag=1',screen.availWidth-600,screen.availHeight-300)">
						  			    <pg:cell colName="roleName" defaultValue="" />
						  			    </a>
									</td>
									<td height='25' class="tablecells" nowrap="nowrap">
										<pg:cell colName="roleDesc" defaultValue="" maxlength="25" replace="..."/>
									</td>
									<%
										 //增加角色类型的名称
										 //baowen.liu 2008-4-25
										String roleTypeId = dataSet.getString("roleType");
						                RoleTypeManager rtm = new RoleTypeManager();
						                RoleType roleType=rtm.getRoleType(roleTypeId);
						                String roleTypeName = roleType.getTypeName();		
										
									%>
									<td>
										<%=roleTypeName%>
									</td>									
									<td height='25' class="tablecells" nowrap="nowrap">
										<a style="cursor:hand" onClick="selectuser('<pg:cell colName="roleId" defaultValue=""/>')">
						  			   	查看
						  			    </a>
									</td>
									
									<%
										String roleId = dataSet.getString("roleId");
										if (roleId != null && !roleId.equals("") && control.checkPermission(roleId,
	                       							"cmspermission", AccessControl.ROLE_RESOURCE))
										{
									%>
									<td height='25' class="tablecells" nowrap="nowrap">
						  			    <a style="cursor:hand" onClick="popedomSet(<pg:cell colName="roleId" defaultValue="" />)">
						  			  	<IMG src="../images/new_doc.gif" border="0">
						  			    </a>
									</td>
									<%}else{%><td></td><%}%>
									
								</tr>
							</pg:list>
							<tr height="18px" class="labeltable_middle_tr_01">
								<td colspan=6 align='center'>
							<pg:index />
								</td>
							</tr>
							<input name="queryString" value="<pg:querystring/>" type="hidden">
						</pg:pager>						
					  </table>	
					  </FORM>
					</tab:tabPane>
				</tab:tabContainer>			
			</td>
		</tr>
  </table>	

<iframe name="exeman" width="0" height="0" style="display:none"></iframe>
</body>
</html>
