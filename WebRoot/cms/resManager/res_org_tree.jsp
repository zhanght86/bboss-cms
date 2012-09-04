<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织机构树</title>
<script language="javascript">

</script>
<script src="../inc/js/func.js"></script>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">

</head>
<body class="contentbodymargin" scroll="auto">

<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
  <tr>
	<td>&nbsp;</td>
  </tr>
</table>
    <table >
        <tr><td align="left">
         <tree:tree tree="org_tree"
    	           node="org_tree.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="operList_tab.jsp" 
    			   target="res_org_list" 
    			   dynamic="false"
    			   >
				   
				   <tree:param name="resId2"/>
                   <tree:param name="resTypeId2"/>
                   <tree:param name="resTypeName"/>
				   <tree:param name="title"/>

    			   <tree:treedata treetype="CMSOrgChargeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="组织树"
    	                   expandLevel="1"
    	                   showRootHref="false"
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
