<%
/**
 * 
 * <p>Title: 机构可访问税务机关权限</p>
 *
 * <p>Description: 机构可访问税务机关权限设置页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl" %>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	
	String resTypeId = request.getParameter("resTypeId");
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String currOrgId = request.getParameter("currOrgId");
	String isBatch = request.getParameter("isBatch");
	
%>

<%
	request.setAttribute("rootName", RequestContextUtils.getI18nMessage("sany.pdp.organization.tree.name", request));
%>
<html>
<head>    
  <title>属性容器</title>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
  <%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<script language="Javascript">
function saveReadorgname(){
	var checkValues = "";
	var un_checkValues = "";
	var arr = document.getElementsByName("checkboxValue");
	if(arr){
		for(var i = 0; i < arr.length; i++){
			if(arr[i].checked){
				if(checkValues==""){
					checkValues = arr[i].value;
				}else{
					checkValues += "," + arr[i].value;
				}
			}else{
				if(un_checkValues==""){
					un_checkValues = arr[i].value;
				}else{
					un_checkValues += "," + arr[i].value;
				}
			}
		}
	}
	document.Form1.checkValues.value = checkValues;
	document.Form1.un_checkValues.value = un_checkValues;
	var url = "saveTreeRoleresop_handle.jsp?resTypeId=<%=resTypeId%>&currRoleId=<%=currRoleId%>&role_type=<%=role_type%>&opId=readorgname&currOrgId=<%=currOrgId%>&isBatch=<%=isBatch%>";
	document.Form1.target = "saveres";
	document.Form1.action = url;
	document.Form1.submit();
}
</script>
</head>
<body class="contentbodymargin"  scroll="no">
<form name="Form1"  method="post" >
<DIV align="center">
<tr>
<td>
<input name="checkValues" value="" type="hidden" />
<input name="un_checkValues" value="" type="hidden" />
<a class="bt_1" onclick="saveReadorgname()" /><span><pg:message code="sany.pdp.common.operation.save"/></span></a>
</td>
</tr>
</DIV>

<div id="" style="width:100%;height:530;overflow:auto">
<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
  <tr class="tr" >
     <td  class="td">
    <tree:tree tree="role_org_tree"
    	           node="role_org_tree.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="" 
    			   mode="static-dynamic"
    			   >                         
                   <tree:param name="resTypeId"/>
                   <tree:param name="currRoleId"/>
                   <tree:param name="role_type"/>
                   <tree:param name="currOrgId"/>
                   <tree:param name="isBatch"/>
                   
				   <tree:checkbox recursive="true" name="checkboxValue"/>
				   
    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.SetReadorgnameTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="${rootName}"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   refreshNode="true"  
    	                     	                   
    	                   />
    	</tree:tree>
	</td>				  
  </tr>  
</table>
</div>
<iframe name="saveres" width="0" height="0"></iframe>
</form>
</body>
</html>

