<%
	/**
	  * 直属用户列表
	  */
%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@page import="com.frameworkset.platform.security.AccessControl,
				com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
				com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	String orgId=request.getParameter("orgId");
	String curUserId = accessControl.getUserID();
	OrgAdministrator orgAdministrator = new OrgAdministratorImpl();
	orgId= orgId == null ? "" : orgId;
	boolean state = orgAdministrator.isOrgAdmin(curUserId,orgId);
	//System.out.println("isAdmin = " + state);
%>
<html>
<head>    
 <title>用户列表</title>
 <script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
 <script src="<%=request.getContextPath()%>/cms/inc/js/func.js"></script>
 <script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/include/pager.js" type="text/javascript"></script>
 <SCRIPT language="javascript">
	
	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	function getOrgInfo(e,orgId){
	jsAccessControl.setBackColor(e);
	document.all("selectId").value = orgId;
	}
	//新建子机构
	function createorg()
	{
		var urlstr ="<%=rootpath%>/orgmanager/org.do?method=newsubOrg&orgId=<%=orgId%>&iscms=iscms";
		openWin(urlstr,500,400);
	}
	//删除机构
	function deleteorg(){
		if(window.confirm("你确定要删除吗？(删除将连带删除子机构并且不可以再恢复)")){	
			var urlstr ="<%=rootpath%>/orgmanager/org.do?method=deleteOrg&orgId=<%=orgId%>&iscms=iscms";
			var hello = openWin(urlstr,0,0);
			if(hello)
			{
				alert("删除成功！");
			}
			
		}
	}
	//修改机构
	function modifyorg(){
		var urlstr ="<%=rootpath%>/orgmanager/org.do?method=modifyOrgInfo&orgId=<%=orgId%>&iscms=iscms";
		openWin(urlstr,500,400);
	}
	//角色授予
	function orgroles()
	{
		var urlstr ="<%=rootpath%>/orgmanager/orgrole.do?method=getRoleList&orgId=<%=orgId%>&iscms=iscms";
		openWin(urlstr,600,480);
		window.location.href=window.location.href;
	}
	//new 用户
	function createuser()
	{
		openWin("<%=request.getContextPath()%>/cms/orgManage/addUser.jsp?orgId=<%=orgId%>",780,580);
		//document.location.href = "userInfo.jsp?orgId=<%=orgId%>";
		
		window.document.location.href = window.document.location.href ;
	}
	//del 用户
	function deluser()
	{
		//openWin("userInfo.jsp?orgId=<%=orgId%>",780,580);
		var isSelect = false;
		var outMsg;
			
		for (var i=0;i<userForm.elements.length;i++) {
			var e = userForm.elements[i];
				
			if (e.name == 'checkBoxOne'){
				if (e.checked){
					isSelect=true;
					break;
				}
			}
		}
		if (isSelect){
			outMsg = "你确定要删除吗？(删除后是不可以再恢复的)。";
			if (confirm(outMsg)){
				userForm.target="theIframe"
				userForm.action="<%=request.getContextPath()%>/sysmanager/user/userManager.do?method=deleteUser&disuser=1&isRes=2";
				userForm.submit();
				return true;
			}
		}else{
			alert("至少要选择一条记录！");
			return false;
    }
	return false;
	}
	function back()
	{
		getPropertiesContent().location.href="<%=request.getContextPath()%>/orgmanager/org.do?method=getOrgInfo&orgId=<%=orgId%>";  
		
	}
	function orgRess(){
		openWin('<%=request.getContextPath()%>/cms/userManage/orgResFrame.jsp?orgId=<%=orgId%>',screen.availWidth-50,screen.availHeight-100);
	}
	
	//调入用户
	function folduser(){
		var win;
		win = window.showModalDialog("<%=request.getContextPath()%>/cms/orgManage/folduser_frame.jsp?orgId=<%=orgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
		if(win=="ok"){
			window.location.reload();
		}
	}
	
	//设置部门管理员
	function changeorgadmin(){
		var win;
		win = window.showModalDialog("<%=request.getContextPath()%>/sysmanager/orgmanager/orgAdmin.jsp?orgId=<%=orgId%>",window,"dialogWidth:"+(800)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
		if(win=="ok"){
			window.location.reload();
		}
	}
	
	//主机构
	var isMain = "<%=request.getParameter("isMain")%>";
	if(isMain=="main"){
		alert("删除不成功！当前机构是用户(<%=request.getParameter("userNameMain")%>)的主机构，当用户还有其他隶属机构时不能删除主机构，必须为用户指定一个主机构！");
	}
	var isSuccess = "<%=request.getParameter("isSuccess")%>";
	if(isSuccess=="1"){
		alert("删除用户成功！");
		var str = document.location.href;
		parent.window.document.location.href = str.substring(0,str.indexOf("isSuccess"));
	}
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">

<body class="contentbodymargin" scroll="auto">

  <%if(!"".equals(orgId)) { //当没有选择任何机构时给出提示信息 %>
<div id="contentborder" align="center">
    <form name = "userForm" method="post" action="">	
    <input type="hidden" name="orgId" value ="<%=request.getParameter("orgId")%>">
	<input type="hidden" name="delcmsuser" value="delcmsuser" /><!--用于标识CMS中的用户管理-->
	<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" >
		<tr style="align:left">
			<td height='40' align="left" colspan=8>
			<%
			 if(request.getParameter("resName")!=null)
			{
			%>
			<div align="left">&nbsp;<b>
			<%=request.getParameter("resName")==null?"":request.getParameter("resName")%></b>&nbsp;>>&nbsp;
			<%
			if(accessControl.checkPermission(orgId,"orgroleset",AccessControl.ORGUNIT_RESOURCE))
			{
			%>
			<a href="javascript:void(0)" onclick="orgroles()"><u>角色授予</u></a> | 
			<%
			}
			%>
			<%
			if(accessControl.checkPermission(orgId,"orgresset",AccessControl.ORGUNIT_RESOURCE))
			{
			%>
			<a href="javascript:void(0)" onclick="orgRess()"><u>权限授予</u></a> |
			<%
			}
			%>
			<%
			if(accessControl.checkPermission(orgId,"editsuborg",AccessControl.ORGUNIT_RESOURCE))
			{
			%>
			<a href="javascript:void(0)" onclick="modifyorg()"><u>修改机构</u></a> | 
			<%
			}
			%>
			<%
			if(accessControl.checkPermission(orgId,"deletesuborg",AccessControl.ORGUNIT_RESOURCE) && !state)
			{
			%>
			<a href="javascript:void(0)" onclick="deleteorg()"><u>删除机构</u></a> | 
			<%
			}
			%>
			<%
			if(accessControl.checkPermission(orgId,"addsuborg",AccessControl.ORGUNIT_RESOURCE))
			{
			%>
			<a href="javascript:void(0)" onclick="createorg()"><u>新建子机构</u></a> |
			<%
			}
			%>
			<%
			if(accessControl.checkPermission(orgId,"adduser",AccessControl.ORGUNIT_RESOURCE))
			{
			%>
			<a href="javascript:void(0)" onclick="createuser()"><u>新增用户</u></a> |
			<%
			}
			%>
			<!--
			<%
			if(accessControl.checkPermission(orgId,"deleteuser",AccessControl.ORGUNIT_RESOURCE))
			{
			%>
			<a href="javascript:void(0)" onclick="deluser();return false;"><u>彻底删除</u></a> |
			<%
			}
			%>
			-->
			<%
			if(accessControl.checkPermission(orgId,"adduser",AccessControl.ORGUNIT_RESOURCE))
			{
			%>
			<a href="javascript:void(0)" onclick="folduser()"><u>调入用户</u></a> 
			<%
			}
			%>
			<%
			//if(accessControl.checkPermission(orgId,"changeorgadmin",AccessControl.ORGUNIT_RESOURCE))
			//{
			%>
			<!-- <a href="javascript:void(0)" onclick="changeorgadmin()"><u>设置管理员</u></a></div>-->
			<%
			//}
			%>
			<%}%>
			<hr size="1">
			<div align="left">&nbsp;
			 <!--| 
			<a href="javascript:void(0)" onclick="createorg()"><u>添加用户</u></a>--></div>
			</td>
		</tr>
		<pg:listdata dataInfo="OrgSubUserList" keyName="OrgSubUserList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="15"
				  scope="request"  
				  data="OrgSubUserList" 
				  isList="false">
			      <tr class="cms_report_tr">
			      <!--设置分页表头-->
			 		<!-- <td class="headercolor" width="7"> -->
					<!-- 	<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')" width="10"> -->
					<!-- </td>	  -->    
			      	<td class="headercolor" height='30'>登录名</td>	<input class="text" type="hidden" name="selectId">
			      	
			      	<td class="headercolor" height='30'>用户名</td>
			      	<td class="headercolor" height='30'>性别</td>
			      	<td class="headercolor" height='30'>当前状态</td>
			      	<td class="headercolor" height='30'>用户类型</td>
			      	
			      </tr>
			      <pg:param name="orgId"/>
				  <pg:param name="resName"/>
			     		      
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td class="detailcontent" colspan=100 align='center'>暂时没有用户</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      <% //显示用户的身份
			                  //baowen.liu 2008-4-25
			                  String  userid =dataSet.getString("userId");
			                  //判断用户是否为当前部门的管理员
			                  boolean isOrgManager = orgAdministrator.isOrgAdmin(userid,orgId);
			                  //判断是否具有超级管理员角色
			                  boolean isRoleAdmin = accessControl.isAdminByUserid(userid);
			                  String isRoleAdminOrOrgManager = "";//是否拥有超级管理员角色与部门管理员角色
							if(isOrgManager && isRoleAdmin){
								isRoleAdminOrOrgManager = "(管理员；超级管理员角色)";
							}else if(isRoleAdmin){
								isRoleAdminOrOrgManager = "(超级管理员角色)";
							}else if(isOrgManager){
								isRoleAdminOrOrgManager = "(管理员)";
							}
				%>
			      		<tr class="cms_data_tr" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" )" >	      				
							<!-- <td class="tablecells" nowrap="nowrap" width="6"> -->
							<!-- 	<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="userId" defaultValue=""/>" width="10"> -->
							<!-- </td>	-->
			 				<td class="tablecells" align=center><pg:cell colName="userName" defaultValue=""/><%=isRoleAdminOrOrgManager%></td>
							
							<td class="tablecells" align=center><pg:cell colName="userRealname" defaultValue=""/></td>																											    					    							    				    		
					  		<td class="tablecells" align=center >
					  		<dict:itemname type="sex" expression="{userSex}" />
					  		<!--
					  		<pg:equal colName="userSex" value="F">女</pg:equal>
					  		<pg:equal colName="userSex" value="M">男</pg:equal>																										    					    							    				    		
					  		<pg:equal colName="userSex" value="-1">未知</pg:equal>
					  		-->
					  		</td>
					  		<td class="tablecells" align=center >
					  		 <dict:itemname type="isvalid" expression="{user_isvalid}" />
					  		 <!--
					  		 <pg:equal colName="user_isvalid" value="0">已删除</pg:equal>
					         <pg:equal colName="user_isvalid" value="1">新申请</pg:equal>
					         <pg:equal colName="user_isvalid" value="2">已开通</pg:equal>
					         <pg:equal colName="user_isvalid" value="3">停用</pg:equal>
					         -->
					  		</td>	
					  		<td class="tablecells" align=center >
					  		 <dict:itemname type="userType" expression="{userType}" />
					  		</td>
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="Data_List_Table_Bottom" colspan=7 align='center'>
					<pg:index/>
					<input type="hidden" name="queryString" value="<pg:querystring/>">
					</td>
			      	
			      </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>

 <%}else {out.println("<br><br><div align=center height=30><font size=3>请选择左边的一个机构进行操作！</font></div>");}%>
 
<IFRAME name="theIframe" width="0" height="0"></IFRAME>
<IFRAME name="delIframe" width="0" height="0"></IFRAME>
</body>

</html>