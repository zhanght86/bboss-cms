<%
	/**
	  * 直属用户列表
	  */
%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%
	String orgId=request.getParameter("orgId");

	String resId2 = request.getParameter("resId2");
	String resTypeId2 = request.getParameter("resTypeId2");
	String resTypeName = request.getParameter("resTypeName");
	String title = request.getParameter("title");
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
			openWin(urlstr,0,0);
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
	}
	//new 用户
	function createuser()
	{
		openWin("<%=request.getContextPath()%>/cms/orgManage/userInfo.jsp?orgId=<%=orgId%>",780,580);
		//document.location.href = "userInfo.jsp?orgId=<%=orgId%>";
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
				userForm.action="<%=request.getContextPath()%>/sysmanager/user/userManager.do?method=deleteUser&disuser=1";
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
		openWin('<%=request.getContextPath()%>/cms/userManage/orgResFrame.jsp?orgId=<%=orgId%>',screen.availWidth-100,screen.availHeight-100);
	}
</SCRIPT>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/cms/inc/css/cms.css" rel="stylesheet" type="text/css">

<body class="contentbodymargin" scroll="auto">

<div id="contentborder" align="center">
    <form name = "userForm" method="post" action="">	
    <input type="hidden" name="orgId" value ="<%=request.getParameter("orgId")%>">
	<input type="hidden" name="delcmsuser" value="delcmsuser" /><!--用于标识CMS中的用户管理-->
	<table width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" >
		<tr style="align:left">
			<td height='40' align="left" colspan=5>
			<%
			if(request.getParameter("resName")!=null)
			{
			%>
			<div align="left">&nbsp;<b>
			<%=request.getParameter("resName")==null?"":request.getParameter("resName")%></b>&nbsp;>>&nbsp;
			<b>用户列表</b>
			</div>
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
			 		<!--td class="headercolor" width="6">
						<input type="checkBox" name="checkBoxAll" onClick="checkAll('checkBoxAll','checkBoxOne')" width="10">
					</td-->	      
			      	<td class="headercolor" height='30'>登录名</td>	<input class="text" type="hidden" name="selectId">
			      	<td class="headercolor" height='30'>用户名</td>
			      	<td class="headercolor" height='30'>性别</td>
					<td class="headercolor" height='30'>权限授予</td>
			      	
			      </tr>
			      <pg:param name="orgId"/>
				  <pg:param name="resName"/>
				  <pg:param name="resId2"/>
				  <pg:param name="resTypeId2"/>
				  <pg:param name="resTypeName"/>
				  <pg:param name="title"/>
			     		      
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td class="detailcontent" colspan=100 align='center'>暂时没有用户</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      		<tr class="cms_data_tr" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" )" >	      				
							<!--td class="tablecells" nowrap="nowrap" width="6">
								<input type="checkBox" name="checkBoxOne" onClick="checkOne('checkBoxAll','checkBoxOne')" value="<pg:cell colName="userId" defaultValue=""/>" width="10">
							</td-->	
			 				<td class="tablecells" align=center><pg:cell colName="userName" defaultValue=""/></td>
							
							<td class="tablecells" align=center><pg:cell colName="userRealname" defaultValue=""/></td>																
							<td class="tablecells" align=center >
								<pg:equal colName="userSex" value="F">女</pg:equal>
								<pg:equal colName="userSex" value="M">男</pg:equal>								<pg:equal colName="userSex" value="-1">未知</pg:equal>
					  		</td>
							<%
					  		String userId=dataSet.getString("userId");
					  		if(AccessControl.checkPermissionByUserID(userId,resId2,"write",resTypeId2)){
					  		%>
							<td  onclick="openWin('operList_user_tab.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>&resTypeName=<%=resTypeName%>&title=<%=title%>&resId=<pg:cell colName="userId" defaultValue=""/>&username=<pg:cell colName="userRealname" defaultValue=""/>',screen.availWidth-250,screen.availHeight-150)">
									<IMG src="../images/point_tag.gif" border="0"  style="cursor:hand">
							</td>
							<%
							}
							else
							{
							%>
							<td onclick="openWin('operList_user_tab.jsp?resId2=<%=resId2%>&resTypeId2=<%=resTypeId2%>&resTypeName=<%=resTypeName%>&title=<%=title%>&resId=<pg:cell colName="userId" defaultValue=""/>&username=<pg:cell colName="userRealname" defaultValue=""/>',screen.availWidth-250,screen.availHeight-150)">
									<IMG src="../images/new_doc.gif" border="0"  style="cursor:hand">
							</td>
							<%}%>
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="Data_List_Table_Bottom" colspan=5 align='center'>
					<pg:index/>
					<input type="hidden" name="queryString" value="<pg:querystring/>">
					</td>
			      	
			      </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>
</body>

</html>