
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
s
<%@ page import="com.frameworkset.platform.resource.ResourceManager
				,com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase
				,com.frameworkset.platform.sysmgrcore.manager.UserManager,com.frameworkset.platform.sysmgrcore.entity.User
				,com.frameworkset.platform.config.ConfigManager
				,com.frameworkset.platform.security.AccessControl"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String userId = request.getParameter("userId");
	
	ResourceManager resManager = new ResourceManager();
	
	List list = resManager.getResourceInfos();
	
	//加开关，如果不允许超级管理员之外的用户进行菜单授权，当前用户又不是超级管理员，则去掉菜单项，请注意，是超级管理员，重复，不是啰嗦！
	boolean state = ConfigManager.getInstance().getConfigBooleanValue("enablecolumngrant", true) && !accesscontroler.getUserID().equals("1");
	
	if(list == null)
		list = new ArrayList();
	request.setAttribute("resTypeList",list);
	session.setAttribute("role_type","user");
	session.setAttribute("currRoleId",userId);
%>
<html>
<head>    
  <title>属性容器</title>
  <script language="JavaScript" src="../../scripts/changeView.js" type="text/javascript"></script>
<%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
  
<body class="contentbodymargin" scroll="no">
<div id="contentborder">


<table class="table" width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
	
  <tr class="tr">
    <td width="25%" class="td"><select  class="select" name="select" onChange="changeView('select')">
      <option selected>--请选择资源--</option>
	  <pg:list requestKey="resTypeList" needClear="false">
	      <pg:equal colName="auto" value="true">
		      <pg:equal colName="id" value="column">
		      	<%if(!state){%>
		      	<option value="<pg:cell colName="id"/>" link="<pg:cell colName="resource"/>">	
		      		<pg:cell colName="name"/>
				</option>
				<%}%>
		      </pg:equal>
		      <pg:notequal colName="id" value="column">	
		      	<option value="<pg:cell colName="id"/>" link="<pg:cell colName="resource"/>">
		      		<pg:cell colName="name"/>
				</option>
		      </pg:notequal>
		  </pg:equal>					
	  </pg:list>

    </select></td>
    </tr>
     
    <tr height="100%" width="100%"><td height="100%" width="100%">
    <iframe id="resource_bridge" frameborder="0" name="resource_bridge" src="" height="100%" width="100%"/></td></tr>
  
</table>

</div>
</body>
</html>
