<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<html>
<head>    
  <title>属性容器</title>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<center>
<form name="OrgJobForm" action="" method="post" >
<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
<input type="hidden" name="isRecursive" value=""> 
  
  
  <tr class="tr" >
     <td class="td" width="40%" align="left">
        <!--input type="checkbox" id="ischecked" name="ischecked" onclick="setParam()" value="true"/><font style="font-size:12px">递归显示用户</font-->
     </td> 
  <tr class="tr" >
     <td  class="td">
     
     <tree:tree tree="group_org_tree"
    	           node="group_org_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href="userList_ajax.jsp"
    			   
    			   target="userList"
    			   >                         
                   <tree:param name="uid"/>
    			   <tree:treedata treetype="OrgTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   
    	                   />
    	</tree:tree>
	</td>
	 				  
  </tr>  
</table>
</form>
</center>
</div>
<script>
    function setParam(){
        if(document.all("ischecked").checked==true)
            document.all("isRecursive").value = "true" ;
        
    }
    function getParam(){
        return "../../groupmanager.do?method=getUserList&isRecursive=true";
    }
    
    function xxx(){
        alert(11)
    }
</script>
</body>
</html>

