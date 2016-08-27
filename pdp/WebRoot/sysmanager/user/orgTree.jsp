<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<html>
<head>    
  <title>选择机构</title>
  <script language="JavaScript"> 
  function addorg(){	
  	var chkbx = document.all("orgIdName");
	//没有复选框
  	if(!chkbx){  	
  		alert("您没有选择机构");
  		return;
  	}
	var n=document.all("orgIdName").length;
	
	var backstring = "";
	var issel = 0;
	for(var i= 0;i<n;i++){
		if(chkbx[i].checked){
			issel = 1;
		}
	}
	//只有一个复选框
	if(!n){
		if(document.all("orgIdName").checked){
			issel = 1;
		}
	}
	
	for(var i= 0;i<n;i++){
		if(chkbx[i].checked){
			var tmp = chkbx[i].value;
			backstring =backstring+tmp+"&";
			
		}
	}
	
	//alert(backstring);
	opener.document.forms[0].orgName.value=backstring;
	
	opener.document.forms[0].orgName.focus;
	opener. changebox();
	window.close();
	
}
</script>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
<body class="contentbodymargin">
<div id="contentborder">
<center>
<form name="OrgJobForm" action="" method="post" >
<table width="80%" border="0" cellpadding="0" cellspacing="1">
  <tr class="tabletop">
    <td width="40%" align="center">可选机构</td>    
  </tr>
  <tr >
     <td >
     
    <tree:tree tree="user_org_tree111"
    	           node="user_org_tree111.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target=""
    			   dynamic="false"
    			   >                         
                   <tree:param name="uid"/>
                   <tree:checkbox name="orgIdName"  />                   

    			   <tree:treedata treetype="OrgTreeCheckbox"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   refreshNode="false"
    	                   />

    	</tree:tree>
	</td>			 
				  
  </tr>
  <tr>
  <td align="center" >
  <input name="add" type="button" class="input" value="确定" onclick="addorg()">
  </td>
  </tr>
  
</table>
</form>
</center>
</div>
</body>
</html>

