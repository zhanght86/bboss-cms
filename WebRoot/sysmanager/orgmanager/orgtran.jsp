<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
AccessControl control = AccessControl.getInstance();
control.checkAccess(request,response);
String orgId = StringUtil.replaceNull(request.getParameter("orgId"));
//判断操作是否成功
String isok =  (String)request.getAttribute("isOk");

//以下代码供左边树刷新使用
String action = StringUtil.replaceNull(request.getParameter("action"));
String updateorgId =  StringUtil.replaceNull(request.getParameter("updateorgId"));
String updateparentId =  StringUtil.replaceNull(request.getParameter("updateparentId"));
%>
<SCRIPT LANGUAGE="JavaScript"> 
var ok = <%=isok%>;
	if(ok=="1"){
		alert("错误，不能转移！");
	} 
	else if(ok=="2")
	{
		alert("转移机构成功！");
	}
var action="<%=action%>";
if(action!="")
getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=0&collapse=0&request_scope=session&selectedNode=<%=updateorgId%>";
function okadd(){	
	if(selectNode==null){
		alert("请选择转移到的机构"); return;
	}  
	Form1.action="../../purviewmanager/userorgmanager/org/orgtran_do.jsp?orgId=<%=orgId%>&parentId="+selectNode;
	Form1.submit();  
	//window.location.href="<%=rootpath%>/orgmanager/org.do?method=tranOrg&orgId=<%=orgId%>&parentId="+selectNode;    
                   
}
function updateAfter(){
 	getNavigatorContent().location.href ="<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=0&expand=0&request_scope=session&selectedNode=<%=updateorgId%>"; 
 	
}
function back(){	
     
 	window.location.href="<%=rootpath%>/orgmanager/org.do?method=getOrgInfo&orgId=<%=orgId%>";    
                   
}

function flashSelf(orgId,seOrg){
	window.location.href = "<%=rootpath%>/sysmanager/orgmanager/orgtran.jsp?orgId="+seOrg;
	getNavigatorContent().location.href = "<%=rootpath%>/sysmanager/orgmanager/navigator_content.jsp?anchor=0&collapse=0&request_scope=session&selectedNode="+seOrg;
}

</SCRIPT>
<head>    
  <title>机构转移</title>


<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">   
<body class="contentbodymargin" onLoad="<%=action.equals("tran")?"updateAfter()":""%>" scroll="no">
<form name="Form1" target="abc" action="" method="post" >
<div id="contentborder" align="center">
    <table >
        <tr><td>
        选择转移到的机构
         <tree:tree tree="org_tree_tran"
    	           node="org_tree_tran.node"
    	           imageFolder="/sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="aa.jsp"
    			   target="abc"
    			   mode="static"
    			   > 
    			                           
                   <tree:param name="parentId"/>
				   <tree:param name="orgId"/>
    			   <tree:treedata treetype="com.frameworkset.platform.menu.CurrentorgAndLisanTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构管理"
    	                   expandLevel="1"
    	                   showRootHref="true"
    	                   needObserver="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
    <br>
   <table><tr><td>
    <input name="add" type="button" class="input" value="确定" onclick="okadd()" >
     <input name="Submit" type="button" class="input" value="返回" onclick="back()">                  
     </td></tr></table>
</div>
</form>
<div style="display:none">
	<iframe src="" id="abc" name="abc" style="width:0;height:0">
</div>
</body>


