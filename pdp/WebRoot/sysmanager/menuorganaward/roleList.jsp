<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.platform.config.model.AuthorTableInfo,
		com.frameworkset.platform.security.AccessControl,
		com.frameworkset.platform.security.authorization.AuthRole,
		com.frameworkset.platform.sysmgrcore.authorization.AppAuthorizationTable,
		com.frameworkset.common.poolman.DBUtil,
		com.frameworkset.platform.config.ConfigManager"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%
	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
	<head>
		<title>属性容器</title>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">
var f = false;//true为已选定,反之



function changeRowColor(obj) {
   if(obj.flag == "true")
   {
   		obj.flag = "false";
   		obj.style.removeAttribute("backgroundColor");  
	    obj.style.color = 'black';
   		
   }
   else
   {
   
   		obj.flag = "true";
   		 
   		obj.style.background='yellow';   //把点到的那一行变希望的颜色; 
   		obj.style.color = 'white';
   		
   		



   }
   subForm();






}

function selectAll(){
	var rowsCount = document.all.table1.rows.length;
	
	if(!f){
		for(var i=0;i<rowsCount;i++){
			var row = document.all.table1.rows[i];
			if(row!=null && row.id!=null && row.id!="" && row.flag != null){
				row.flag = "true";


				row.style.background='yellow';   //把点到的那一行变希望的颜色; 
	   			row.style.color = 'white';

			}
		}
		f = true;
	}else{
		for(var i=0;i<rowsCount;i++){
			var row = document.all.table1.rows[i];
			if(row!=null && row.id!=null && row.id!="" && row.flag != null){
				row.flag = "false";
				row.style.removeAttribute("backgroundColor");  
	    		row.style.color = 'black';
			}
		}
		f = false;
	}
	
	subForm();
}

function subForm(){
	var str = "";
	var rowsCount = document.all.table1.rows.length;
	
	for(var i=0;i<rowsCount;i++)
	{
		var row = document.all.table1.rows[i];

		if(row.flag == "true"){
			str += row.id + ":";
			
		}
	}
		roleList.action = "orgChooseTree.jsp?idString="+str+"&type=role";
		roleList.target="orgChooseTree";
		roleList.submit();



		roleList.action = "menuTree.jsp?idString="+str+"&type=role";
		roleList.target="menuTree";
		roleList.submit();




}










</SCRIPT>		
<body>
	<form name="roleList" target="" method="post" >      				
		<table id="table1" width="100%" cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE"  class="thin">
   			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.RoleList" keyName="RoleList" />
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="20" scope="request" data="RoleList" isList="false">
			<tr>
			<!--设置分页表头-->
				<td class="headercolor" ondblclick="selectAll()">角色名称</td>
				<td class="headercolor" ondblclick="selectAll()">角色描述</td>				
			</tr>
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
				<tr id="<pg:cell colName="roleId" defaultValue="" />" 
				<%if(accesscontroler.checkPermission(dataSet.getString("roleId"),"resopset","role"))
				{%>
					onclick="changeRowColor(this);" flag="false"
					<%}%>
					 onmouseover="this.className='mouseover'"
					onmouseout="this.className= 'mouseout'">
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="roleName" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" > 
						<pg:cell colName="roleDesc" defaultValue="" />
					</td>
				</tr>
			</pg:list>
			<input name="queryString" value="<pg:querystring/>" type="hidden">
		</pg:pager></table>
		<table cellspacing="0" id="table2" cellpadding="0" border="0" width="100%">
			<tr>
				<td colspan="3">
				 	 <pg:index />
				</td>
			</tr>
		</table>
	</form>
</body>
</html>


