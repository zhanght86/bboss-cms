<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>组织机构树</title>
<script language="javascript">
function sub()
{
	//将选中的文档id拼成字符串
	var all = document.getElementsByName("ID");
	var orgid = "";
	var orgname = "";
	for(var i=0;i<all.length;i++){
		if(all[i].checked == true){
			orgid = orgid + all[i].value.split(":")[0] + ",";
			orgname = orgname + all[i].value.split(":")[1] + ",";		
		}
	}
	if(haveSelect('ID')){
		window.opener.document.all.item("orgname").value = orgname;
		window.opener.document.all.item("orgids").value = orgid;
		window.close();
	}else{
		alert("请选择角色！");
	}
}
</script>
<script src="../inc/js/func.js"></script>
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../../sysmanager/css/tab.winclassic.css">

</head>
<body class="contentbodymargin" scroll="auto">
  <form>
    <table >
        <tr><td align="left">
         <tree:tree tree="org_tree"
    	           node="org_tree.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   dynamic="false"
    			   > 
    			   <tree:checkbox name="ID" recursive="true"/> 
    			   <tree:treedata treetype="CMSOrgChargeTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="组织树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="true" 
    	                   />
					
    	</tree:tree>
         </td></tr>
    </table>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table" >
  		<tr>
    		<td align="center">
    		<input type="button" value="确定" name="con" onclick="sub()"> 
    		&nbsp; 
    		<input type="button" value="返回" name="back" onclick="window.close()">
    		</td>  
  		</tr>
	</table>
</form>
<script language="javascript">
</script>

</body>
</html>
