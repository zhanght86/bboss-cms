<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%String orgId=request.getParameter("orgId");
%>
<html>
<head>    
 <title>属性容器</title>
 <script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
 
 <script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
 <SCRIPT language="javascript">
	
	var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
	function getOrgInfo(e,orgId){
	jsAccessControl.setBackColor(e);
	document.all("selectId").value = orgId;
	}
	function createorg()
	{
		getPropertiesContent().location.href="../user/userInfo1.jsp?orgId=<%=orgId%>";   
		
	}
	function back()
	{
		getPropertiesContent().location.href="<%=request.getContextPath()%>/orgmanager/org.do?method=getOrgInfo&orgId=<%=orgId%>";  
		
	}
	
</SCRIPT>
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">

<body class="contentbodymargin" scroll="no">

<div id="contentborder" align="center">
    <form name = "userForm" method="post" action="">	
    <input type="hidden" name="orgId" value ="<%=request.getParameter("orgId")%>">
	<table width="100%" height="25" border="0" cellpadding="0" cellspacing="1" class="thin" >
	<tr><td height='40' class="detailtitle" align=center colspan=4><b>直属用户列表</b></td></tr>
		<pg:listdata dataInfo="OrgSubUserList" keyName="OrgSubUserList"/>
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="18"
				  scope="request"  
				  data="OrgSubUserList" 
				  isList="false">
			      <tr class="labeltable_middle_td">
			      <!--设置分页表头-->
			 			      
			      	<td class="headercolor" height='30'>登录名</td>	<input class="text" type="hidden" name="selectId">
			      	<td class="headercolor" height='30'>登录密码</td>
			      	<td class="headercolor" height='30'>用户名</td>
			      	<td class="headercolor" height='30'>性别</td>
			      	
			      </tr>
			      <pg:param name="orgId"/>
			     		      
				  <!--检测当前页面是否有记录-->
		       	  <pg:notify>
			      <tr height="18px" >
			      	<td class="detailcontent" colspan=100 align='center'>暂时没有用户</td>
			      </tr>
			      </pg:notify>			      
			      			    
			      <!--list标签循环输出每条记录-->			      
			      <pg:list>	
			      		<tr class="labeltable_middle_tr_01" onMouseOver="this.className='mousestyle1'" onMouseOut="this.className= 'mousestyle2'" )" >	      				
								
			 				<td class="tablecells" align=center><pg:cell colName="userName" defaultValue=""/></td>
							<td class="tablecells" align=center>******</td>
							<td class="tablecells" align=center><pg:cell colName="userRealname" defaultValue=""/></td>																											    					    							    				    		
					  		<td class="tablecells" align=center >
					  		<pg:equal colName="userSex" value="F">女</pg:equal>
					  		<pg:equal colName="userSex" value="M">男</pg:equal>																										    					    							    				    		
					  		<pg:equal colName="userSex" value="-1">未知</pg:equal>
					  		</td>	
					  	</tr>			      		
			      </pg:list>
			   <tr height="18px" >
			      	<td class="detailcontent" colspan=3 align='center'><pg:index/><input type="hidden" name="queryString" value="<pg:querystring/>"></td>
			      	<td class="detailcontent" align='left'>
			      	
				 		<input type="button" value="新增用户" class="input"
								onclick="createorg()">
						<input type="button" value="返回" class="input"
								onclick="back()">		
					</td>
			      </tr>   			   	      
		</pg:pager>
		
	</table>	
</form>	
</div>
<%@ include file="../sysMsg.jsp"%>
</body>

</html>
