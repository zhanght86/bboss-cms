<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.UserManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.RoleManager" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Role" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.User,com.frameworkset.platform.security.AccessControl" %>
<%@ page import="com.frameworkset.platform.config.*" %>
<%	
	//Integer userId = (Integer) session.getAttribute("currUserId");
	AccessControl accessControl = AccessControl.getInstance();
    accessControl.checkAccess(request,response);
    
    String uid = accessControl.getUserID();
	String orgId = request.getParameter("orgId");
	UserManager userManager = SecurityDatabase.getUserManager();
	User user = userManager.getUserById(uid);
	String userName = user.getUserRealname();
%>
<html>
<head>    
<title>用户【<%=userName%>】角色授予</title>
  <script language="javascript" src="<%=request.getContextPath()%>/include/dragdiv.js"></script>
  <script language="Javascript">
  function roleChangebox(){
	var tablesFrame= document.getElementsByName("orgjoblist");
	//var roleIdValue = document.all.jobrolelist.value;
	var roleIdValue = document.all("jobrolelist").value;
	tablesFrame[0].src = "../sysmanager/user/org_job.jsp?userId=<%=uid%>&roleId=" + roleIdValue;
}
  </script>
</head>
<body class="contentbodymargin" scroll="no" onload="loadDragDiv();">


<center>
<form name="OrgJobForm" action="" method="post" >
<input name="userId" value="<%=uid%>" type="hidden">


<table width="80%" border="0" cellpadding="0" cellspacing="1">


  <%
  RoleManager roleManager = SecurityDatabase.getRoleManager();
  String everyoneRolename = AccessControl.getEveryonegrantedRoleName();
  Role role = roleManager.getRoleByName(everyoneRolename);
  if(role != null)
  {
  %>
  <tr class="tabletop" cols="3">
    <td colspan="3"><font color="red">
	<pg:message code="sany.pdp.personcenter.person.role.title" arguments="<%=everyoneRolename%>" /></font></td>
  </tr>
  <tr class="tabletop">
    <td align="center">&nbsp;</td>
    
    <td align="center">&nbsp;</td>
    
    <td align="center">&nbsp;</td>
  </tr>
  <%
  }
  %>
  <tr class="tabletop">
    <td align="center"><%=user.getUserRealname()%>(<%=user.getUserName()%>)<pg:message code="sany.pdp.personcenter.person.role.authorize" /></td>
    
    <td align="center"><pg:message code="sany.pdp.personcenter.person.role.belong.userpost" /></td>
    
    <td align="center"><pg:message code="sany.pdp.personcenter.person.role.belong.userpost.begin" /><font color="#CC9900"><pg:message code="sany.pdp.sys.org" /></font>-<font color="#996600"><pg:message code="sany.pdp.sys.post" /></font></td>
  </tr>
  <tr >
    <td align="center" >
    <div class="win" id="dd_1" align="left">
     <select name="roleId"  multiple style="width:98%" size="15" >
		 <pg:list requestKey="existRole">
			<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>			
		</pg:list>
	 </select>
	 </div>
	</td>
	<td align="center">
	<div class="win" id="dd_2" align="left"><!-- 需要实现缩放的div层 -->
	<select name="jobrolelist" multiple style="width:98%" size="15"   onchange="roleChangebox()"><!-- HTML元素 -->
				  <pg:list requestKey="jobRole">
				  	<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>
	</select>
	</div>
 </td>
 <td align="center">
    <div class="win" id="dd_3" align="left">
 	<select name="userjobList" multiple style="width:98%" size="15">
		
	</select>	
 	</div>
 </td>
	
  </tr>
 <tr class="tabletop">
 	<td align="center">
 	<%if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole",true))
  {
  %><pg:message code="sany.pdp.personcenter.person.role.authorize.org" /><%}%>
  </td>
    
    <td  align="center"><%if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole",true))
  {
  %><pg:message code="sany.pdp.personcenter.person.role.authorize.usergroup" /><%}%></td>
    
    <td width="40%" align="center">&nbsp;</td>
  </tr>
 <tr>
 <td  align="center" > 
	<%if(ConfigManager.getInstance().getConfigBooleanValue("enableorgrole",true))
  {
  %><div class="win" id="dd_4" align="left">
     <select name="orgrolelist"  multiple style="width:98%" size="15" disabled="true">   
				  <pg:list requestKey="orgRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	</select>
	</div>
<%}%>
	</td>
	
	<td  align="center" ><%if(ConfigManager.getInstance().getConfigBooleanValue("enablergrouprole",true))
  {
  %><div class="win" id="dd_5" align="left">
     <select name="grouprolelist"  multiple style="width:98%" size="15" disabled="true">   
				  <pg:list requestKey="groupRole">
					<option value="<pg:cell colName="roleId"/>"><pg:cell colName="roleName"/></option>
				  </pg:list>			
	</select>
	</div>
	<%}%>
	</td>
 
 <td align="center">&nbsp;</td>
 </tr>
</table>
<pg:beaninfo requestKey="userRoleForm">
<input type="hidden"  name="orgId" value="<pg:cell colName="orgId"  defaultValue=""/>"/>
<input type="hidden"  name="uid" value="<pg:cell colName="userId"  defaultValue=""/>"/>
</pg:beaninfo>
</form>
</center>
<div id=divProcessing style="width:200px;height:25px;position:absolute;left:245px;top:298px;display:none">
	<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="98%" height="98%">
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
<iframe name="orgjoblist" src="../sysmanager/user/org_job.jsp?<pg:dtoken element="param"/>" width=0 height=0></iframe>
<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>


