<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	String resId2 = request.getParameter("resId2");
	String resTypeId2 = request.getParameter("resTypeId2");
	String resTypeName = request.getParameter("resTypeName");
	String title = request.getParameter("title");
	String resName2 = request.getParameter("resName2");
	String isBatch = request.getParameter("isBatch");
	String path = "res_org_userlist.jsp?resId2=" + resId2 + "&resTypeId2=" + resTypeId2 + "&resTypeName=" + resTypeName + "&title=" + title+"&resName2="+resName2+"&isBatch="+isBatch;
	String rootpath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织数</title>
<script language="javascript">
function action11(){
	getPropertiesContent().location.href="<%=rootpath%>/sysmanager/orgmanager/organsearch.jsp";   
}
function neworg()
{
	openWin("new_org.jsp",520,400);
}
</script>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">

</head>
<body class="contentbodymargin" scroll="auto">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
  <tr>
    <td align="center">&nbsp;</td>
   
  </tr>
</table>
    <table >
        <tr><td align="left">
         <tree:tree tree="org_tree"
    	           node="org_tree.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="<%=path%>" 
    			   target="res_user_list" 
    			   mode="static-dynamic"    			   
    			   > 
    			   
    			   <tree:param name="resId2"/>
                   <tree:param name="resTypeId2"/>
                   <tree:param name="resTypeName"/>
				   <tree:param name="title"/>
				   <tree:param name="resName2"/>
				   <tree:param name="isBatch"/>
				    <tree:param name="isGlobal"/>
    			    
    			   <tree:treedata treetype="com.frameworkset.platform.menu.CMSOrgChargeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="组织树"
    	                   rootNameCode="sany.pdp.organization.tree.name"
    	                   expandLevel="1"
    	                   showRootHref="true"
    	                   needObserver="false"
    	                   enablecontextmenu="false" 
    	                   />
					
    	</tree:tree>
         </td></tr>
    </table>
<script language="javascript">
</script>

</body>
</html>

