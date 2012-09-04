<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%
	String roleId = request.getParameter("userId");
	session.setAttribute("currRoleId",roleId);
	//System.out.println("========="+roleId);
	session.setAttribute("role_type","user");
	
%>
<html>
<head>    
  <title>属性容器</title>
  <script language="javascript" src="../../sysmanager/scripts/changeViewCms.js" type="text/javascript"></script>
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
  <link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css"> 
  
<body class="contentbodymargin" scroll="no">
<div id="contentborder">


<table class="table" width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">

  <tr class="tr">
    <td width="25%" class="td">
    <select  class="select" name="select" onChange="changeViewCms('select')">
      <option selected>--请选择资源--</option>
      <option value="site" link="userresSiteTree.jsp">站点资源</option>
      <option value="channel" link="userresChannelTree.jsp">频道资源</option>
      <option value="column" link="resColumnTree.jsp">菜单资源</option>
      <option value="site" link="userresResourcTree.jsp">资源类资源</option>
      <option value="orgunit" link="resOrgTree.jsp">机构资源</option>	
   </select>    
    </td>
    </tr>
   <tr height="100%" width="100%">
  	<td height="100%" width="100%">
  		<iframe id="resource_bridge" frameborder="0" name="resource_bridge" src="" height="100%" width="100%" />
  	</td>
  </tr>
</table>

</div>
</body>
</html>
