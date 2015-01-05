<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.dictionary.Data" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityConstants" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManager" %>
<%@ page import="com.frameworkset.platform.dictionary.DictManagerImpl" %>
<%@ page import="org.frameworkset.spi.BaseSPIManager" %>
<%@ page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
    //????????------------------------------------------------
    AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
    String did = request.getParameter("did");
    String typeName = request.getParameter("typeName");	
    String orgId = request.getParameter("orgId");
	did = did==null?"":did;
	typeName = typeName==null?"":typeName;	
	orgId = orgId==null?"":orgId;
    String dicttype_name = "";
    Data dtype = null;
    if(did != null){
		DictManager dictManager = new DictManagerImpl();
		dtype = dictManager.getDicttypeById(did);
		dicttype_name = dtype.getName();
		typeName = dtype.getDescription().trim();
	}
    String rootId = did + ":root:root";
    String rootName = dicttype_name;
%>
<html>
<head>
<title></title>

<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../css/tab.winclassic.css">

</head>
<body>
<!--
href="org_dictdataTree.jsp" 
 -->
<p align="center"><font size=2><strong><%=typeName%>(<%=dtype.getDataTableName()%>)</strong></font>  
<div id="contentborder">
	
    <table cellspacing="1" cellpadding="0" border="0"  width=100%  >
        <tr><td>
         <tree:tree tree="org_dictdata_tree"     			   
    			   node="org_dictdata_tree.node" 
    	           imageFolder="../../images/tree_images/" 
    	           collapse="true"
    			   includeRootNode="true"    	
    			   href="javascript:void"   			   
    			   target=""
    			   mode="static-dynamic"
    			   >         
    			   <tree:param name="orgId"/>
    			   <tree:param name="did"/>
    			   <tree:param name="typeName"/>                            
                   <tree:checkbox recursive="true" partuprecursive="true" name="dictdataValue"/> 
    			   <tree:treedata treetype="com.frameworkset.platform.dictionary.PowreDictdataTree" 
    	                   scope="request"
    	                   rootid="<%=rootId%>"  
    	                   rootName="<%=typeName%>"
    	                   expandLevel="1"
    	                   checkboxValue="true"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   refreshNode="false" 
    	                   />
    	</tree:tree>    	
       </td></tr>
    </table>
</div>
</body>

</html>
<iframe name="hiddenFrame" width=0 height=0></iframe>

