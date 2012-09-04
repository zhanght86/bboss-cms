<%
/**
 * 
 * <p>Title: 岗位机构设置权限授予页面</p>
 *
 * <p>Description: 设置"岗位机构设置"权限给用户</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	String resTypeId = request.getParameter("resTypeId");
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String currOrgId = request.getParameter("currOrgId");
%>
<html>
<head>
<title>导航器内容</title>
<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
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
	var url = "saveTreeRoleresop_handle.jsp?resTypeId=<%=resTypeId%>&currRoleId=<%=currRoleId%>&role_type=<%=role_type%>&opId=jobset&currOrgId=<%=currOrgId%>";
	//alert(url);
	document.Form1.target = "saveres";
	document.Form1.action = url;
	document.Form1.submit();
}
</script>
</head>

<body class="contentbodymargin" scroll="no">
<DIV align="center">
<tr>
<td>
<input name="saveset" value="保存" type="button" onclick="saveReadorgname();" class="input" />
<input name="sx" value="刷新" class="input" type="button" onclick="parent.window.location.href = parent.window.location.href;" />
</td>
</tr>
</DIV>
<div id="contentborder" style="width:100%;height:530;overflow:auto">
<form name="Form1" method="post">
	<table >
        <tr><td>
        <input name="checkValues" value="" type="hidden" />
		<input name="un_checkValues" value="" type="hidden" />
         <tree:tree tree="job_tree"
    	           node="test_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target=""
    			   dynamic="false"
    			   >                         
                   <tree:param name="resTypeId"/>
                   <tree:param name="currRoleId"/>
                   <tree:param name="role_type"/>
                   <tree:param name="currOrgId"/>
                   
				   <tree:checkbox name="checkboxValue"/>

    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.ResJobTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="岗位列表"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
    </form>
    
</div>
<iframe name="saveres" width="0" height="0"></iframe>
</body>
</html>
