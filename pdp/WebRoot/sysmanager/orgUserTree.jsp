<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%

%>
<SCRIPT LANGUAGE="JavaScript"> 
	function okadd(){	
		selectNode = document.all.item("userinfo");
		
	if(selectNode==null){
 		alert("请选择机构和用户"); return;
 	}    
 	var ret ;
 	
 	if(selectNode.length)
 	{
 		for(var i = 0;  i < selectNode.length; i ++)
 		{
 			if(selectNode[i].checked)
 			{
 				ret = selectNode[i].value;
 			}
 			
 		}
 	}
 	else if(selectNode)
 	{
 		if(selectNode.checked)
		{
			ret = selectNode.value;
		}
 	}
 	if(!ret)
 	{
 		alert("请选择机构和用户"); return;
 	}    
	window.returnValue = ret;   
	window.close();
	}
	
	
</SCRIPT>
<head>    
  <title>请选择机构下的用户</title>


<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">   
<body class="contentbodymargin"  scroll="no">
<div align="center">
<tr>
<td>
<input name="add" type="button" class="input" value="确定" onclick="okadd()" >
<input name="exit" type="button" class="input" value="取消" onclick="parent.window.close();">
</td>
</tr>
</div>
<div id="contentborder" style="width:100%;height:495;overflow:auto">

    <table >
        <tr><td>
         <tree:tree tree="org_tree_tran"
    	           node="org_tree_tran.node"
    	           imageFolder="/sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="false"
    			   href=""
    			   target="abc"
    			   mode="static-dynamic">  			                           
				   <tree:radio name="userinfo"/>
				   
    			   <tree:treedata treetype="com.frameworkset.platform.menu.ChargeUserTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="用户"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
    
</div>
 

 
</body>
