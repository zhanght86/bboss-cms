<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.resource.ResourceManager"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkManagerAccess(request,response);
	String selectNode = "";
	selectNode = request.getParameter("did")==null?"":request.getParameter("did");
%>

<%
	 request.setAttribute("dictType", RequestContextUtils.getI18nMessage("sany.pdp.dictmanager.dict.type", request));
%>

<html>
<head>
<title>字典管理</title>

<script language="javaScript" src="js/dictTreeQuery.js"></script>
<script type="text/javascript" language="JavaScript">
	function refreshDict(dictId){
		document.dictForm.action = "refreshDict.jsp?dictId=" + dictId;
		document.dictForm.target = "hiddenFrame";
		document.dictForm.submit();
	}
</script>

</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<form action="" name="dictForm" target="hiddenFrame" method="post">
    <table>
        <tr><td>
         <tree:tree tree="dict_tree"
    	           node="dict_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"    			   
    			   href="properties_info_toolbar.jsp"    			   
    			   target="base_properties_toolbar"
    			   dynamic="false"
    			   >                                            

    			   <tree:treedata treetype="com.frameworkset.platform.dictionary.DictTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="${dictType}"
    	                   expandLevel="1"
    	                   showRootHref="true"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   enablecontextmenu="true"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
    </form>
</div>
</body>
<script>
    var obj;
    if("<%=selectNode%>"!=""){	 
	    document.all.item("<%=selectNode%>").className = "selectedTextAnchor";
	    selectNode = "<%=selectNode%>";
    } 
</script>
<iframe name="hiddenFrame" width="0" height="0"></iframe>
</html>

