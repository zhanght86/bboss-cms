<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Restype" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkManagerAccess(request,response);
	String orgId = request.getParameter("orgId");
	String old_ids = orgId;
	
		
%>
<html>
<head>    
  <title>机构树</title>
  <script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../../css/treeview.css"> 
<body class="contentbodymargin" scroll="no">
<div id="contentborder">


<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
  
  <tr>
  	  <td align="center"><input type="button" value="确定" class="input"  onclick="saveit()"></td>
  </tr>
  <tr class="tr" >
     <td  class="td">
     
    <tree:tree tree="role_org_tree"
    	           node="role_org_tree.node"
    	           imageFolder="../../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="_self" 
    			   dynamic="false" 
    			   >                         
                   <tree:param name="resTypeId"/>
				   <tree:radio   name="ids"/>
    			   <tree:treedata treetype="com.frameworkset.platform.dictionary.AccreditOrgTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
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
</body>
<script>
     function saveit(){
         var arr = document.getElementsByName("ids");
         var old_ids = "<%=old_ids%>";
         var selected_value = "";
         for(var i=0;i<arr.length;i++){
             if(arr[i].checked){
                 selected_value = arr[i].value;
                 break;
             }
         }
         var id_name = selected_value.split(":");
         if(id_name.length>1){
             window.dialogArguments.form1.orgId.value = id_name[0];
             if(selected_value==old_ids){//当选择的ID,就是原来的ID,不刷新下面的字典数据页面
                 window.returnValue = "noRefresh";
             }else{//当选择的ID,不是原来的ID,刷新下面的字典数据页面
                 window.returnValue="ok";
             }
         }
         window.close();    
     }
     window.onload = function autoRun(){
         var ids = "<%=old_ids%>";
         var arr = document.getElementsByName("ids");
         for(var i=0;i<arr.length;i++){             
             if(arr[i].value == ids){
                 arr[i].checked=true;
                 break;
             }
         }
     }
     
</script>
</html>

