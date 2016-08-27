<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,
		com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl,
		com.frameworkset.platform.sysmgrcore.manager.OrgAdministrator"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%
	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String uId = accesscontroler.getUserID();
	ResourceManager resManager = new ResourceManager();
    String resId = resManager.getGlobalResourceid(AccessControl.ORGUNIT_RESOURCE);
	String curOrgId = request.getParameter("orgId");
	if(curOrgId == null)
		curOrgId = (String)request.getAttribute("orgId");
	String reFlush = "false";
	if (request.getAttribute("reFlush") != null) {
		reFlush = "true";
	}
	
	Integer currUserId = (Integer)session.getAttribute("currUserId");
	if(currUserId == null){
		currUserId = Integer.valueOf("-1");
	}
	//String curOrgId = (String)session.getAttribute("orgId");
	String desc = (String)request.getParameter("pager.desc");	
	String intervalType = (String)request.getParameter("intervalType");
	String taxmanager = (String)request.getParameter("taxmanager");
	String ischecked="";
	if((String)request.getAttribute("ischecked")==null){
		ischecked ="";
	}else{
		ischecked =(String)request.getAttribute("ischecked");
	}
	
	String QueryuserName = null;
	String QueryuserRealname = null;
	if(request.getParameter("userName")==null){
		QueryuserName="";
	}else{
		QueryuserName = request.getParameter("userName");
	}
	if(request.getParameter("userRealname")==null){
		QueryuserRealname="";
	}else{
		QueryuserRealname = request.getParameter("userRealname");
	}
	OrgAdministrator oam = new OrgAdministratorImpl();
%>
<html>
	<head>
		<title>属性容器</title>
		<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
		<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
		<%@ include file="/include/css.jsp"%>
		<STYLE>
			body {	font-size: 12px; font-family:Arial, Helvetica, sans-serif; color:#393F63; line-height:20px; margin:2px;
			    SCROLLBAR-HIGHLIGHT-COLOR: #ffffff;
				SCROLLBAR-SHADOW-COLOR: #ffffff;
				SCROLLBAR-3DLIGHT-COLOR: #B6D2EF; 
				SCROLLBAR-ARROW-COLOR: #ffffff;
				SCROLLBAR-TRACK-COLOR: #E0EEFB;
				SCROLLBAR-DARKSHADOW-COLOR: #B4CEDF;
				SCROLLBAR-FACE-COLOR: #B9D6F0;
			}
		</STYLE>
<script language="JavaScript" src="common.js" type="text/javascript"></script>
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<SCRIPT language="javascript">



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
var f = false;
function selectAll(){
	var rowsCount = document.all.table1.rows.length;
	
	if(!f){
		for(var i=0;i<rowsCount;i++){
			var row = document.all.table1.rows[i];
			if(row!=null && row.id!=null && row.id!="" && row.flag != null){
				row.flag = "true";

				//#191970
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
		userList.action = "orgChooseTree.jsp?idString="+str+"&type=user";
		userList.target="orgChooseTree";
		userList.submit();
		//userList.action = "saveOrgGrant.jsp?idString="+str+"&type=user";
		//userList.target="saveOrgGrant";
		//userList.submit();
		userList.action = "menuTree.jsp?idString="+str+"&type=user";
		userList.target="menuTree";
		userList.submit();
		//userList.action = "saveMenuGrant.jsp?idString="+str+"&type=user";
		//userList.target="saveMenuGrant";
		//userList.submit();
	
}
function sortBy(byName){
	var desc = "false";

	if ( "<%=desc%>"  == desc ) desc = "true";
	userList.action="../user/orgByUserList.jsp?orgId=<%=curOrgId%>&pager.offset=0&pager.sortKey="+byName+"&pager.desc="+desc;
	userList.submit();
}
</SCRIPT>		
<body>
	<form name="userList" target="orgChooseTree" method="post" >
		<input type="hidden" name="orgId" value="<%=curOrgId%>"/>         				
		<table cellspacing="1" id="table1" cellpadding="0" border="0" bordercolor="#EEEEEE" width="100%" class="thin">
   			<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserListSn" keyName="UserListSn" />
			<!--分页显示开始,分页标签初始化-->
			<pg:pager maxPageItems="5" scope="request" data="UserListSn" isList="false">
			<tr>
			<!--设置分页表头-->
				<td class="headercolor" ondblclick="selectAll()">用户登陆名</td>
				<td class="headercolor" ondblclick="selectAll()">用户实名</td>
				<td class="headercolor" ondblclick="selectAll()">是否税管员</td>
				<td class="headercolor" ondblclick="selectAll()">用户类别 </td>							
				
			</tr>
			<pg:param name="orgId" />
			<pg:param name="userName" />
			<pg:param name="userRealname" />
			<pg:param name="intervalType" />
			<pg:param name="istaxmanager" />
		<!--检测当前页面是否有记录-->
			<pg:notify>
				<tr height="18px" class="labeltable_middle_tr_01">
					<td colspan=100 align='center'>
						暂时没有用户
					</td>
				</tr>
			</pg:notify>
	<!--list标签循环输出每条记录-->
			<pg:list>
				<%
					String userId = dataSet.getInt("userId")+"";
					String isAdminStr = "";
					boolean isadmin = true;
					if(accesscontroler.isAdminByUserid(userId)){
						isAdminStr = "超级管理员";
						isadmin = true;
					}else if(oam.isOrgAdmin(userId,curOrgId)){
						isAdminStr = "部门管理员";
						isadmin = false;
					}else if(accesscontroler.isAdminByUserid(userId) && oam.isOrgAdmin(userId,curOrgId)){
						isAdminStr = "超级管理员";
						isadmin = true;
					}else{
						isadmin = false;
					}
					
					if(isadmin){
				%>
				<TR style="color: white" onmouseover="this.className='mouseover'" onmouseout="this.className= 'mouseout'" >
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="userName" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="userRealname" defaultValue="" />
					</td>
					<td>
						<pg:equal colName="istaxmanager" value="1"><font color="#0000FF">税管员</font></pg:equal>
						<pg:equal colName="istaxmanager" value="0"><font color="#0000FF">非税管员</font></pg:equal>
						<pg:equal colName="istaxmanager" value=""><font color="#0000FF">非税管员</font></pg:equal>
					</td>
					<td class="tablecells" nowrap="nowrap" > 
						<font color="#0000FF"><dict:itemname type="userType" expression="{userType}" />(<%=isAdminStr%>)</font> 	
    				</td>
				</TR>
				<%  }else{ %>
			    	<TR style="color: white" id="<pg:cell colName="userId" defaultValue="" />"
						onclick="changeRowColor(this);"
						flag="false" onmouseover="this.className='mouseover'"
						onmouseout="this.className= 'mouseout'">
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="userName" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap">
						<pg:cell colName="userRealname" defaultValue="" />
					</td>
					<td>
						<pg:equal colName="istaxmanager" value="1">税管员</pg:equal>
						<pg:equal colName="istaxmanager" value="0">非税管员</pg:equal>
						<pg:equal colName="istaxmanager" value="">非税管员</pg:equal>
					</td>
					<td class="tablecells" nowrap="nowrap" > 
						<dict:itemname type="userType" expression="{userType}" />
					</td>
				</TR>
			  <%  }%>
				</pg:list>
					<input name="queryString" value="<pg:querystring/>" type="hidden">
		</pg:pager></table>
		<table cellspacing="0" id="table2" cellpadding="0" border="0" width="100%">
			<TR>
				<td colspan="3">
				 	 <pg:index />
				</td>
			</TR>
		</table>
	</form>
</body>
<center>
</html>


