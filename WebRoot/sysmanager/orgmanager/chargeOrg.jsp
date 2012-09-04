<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
		String displayNameInput = request.getParameter("displayNameInput");
		String displayValueInput = request.getParameter("displayValueInput");
		String displayNameInput1 = request.getParameter("displayNameInput1");
		String displayValueInput1 = request.getParameter("displayValueInput1");
		//System.out.println("displayNameInput...."+displayNameInput);
		//System.out.println("displayValueInput...."+displayValueInput);
		//System.out.println("displayNameInput1...."+displayNameInput1);
		//System.out.println("displayValueInput1...."+displayValueInput1);
		
		String defaultValue = request.getParameter("defaultValue");
		if(defaultValue == null)
			defaultValue = "";
		//String jobId = request.getParameter("jobId");
		
%>
<SCRIPT LANGUAGE="JavaScript"> 
	function okadd(){	
		selectNode = document.all.item("jobinfo");
		//alert(selectNode);
		
	if(selectNode==null){
 		alert("请选择主管处室！"); return;
 	}    
 	var ret ;
 	
 	if(selectNode.length)
 	{
 		for(var i = 0;  i < selectNode.length; i ++)
 		{
 			if(selectNode[i].checked)
 			{
 				//alert(selectNode[i].value);
 				ret = selectNode[i].value;
 			}
 			
 		}
 	}
 	else if(selectNode)
 	{
 		if(selectNode.checked)
		{
			//alert(selectNode.value);
			ret = selectNode.value;
		}
 	}
 	if(!ret)
 	{
 		alert("请选择主管处室！"); return;
 	}    
 	

	
	//father.getId():son.getJobId()$father.getName():son.getJobName()
	var temps = ret.split("$");
	var ids  = temps[0].split(":");
	var names  = temps[1].split(":");	
	var orgid = ids[0];
	var orgname = names[0];
	var jobid = ids[1];
	var jobname = names[1];
	
	         parent.window.opener.document.all.item("<%=displayNameInput%>").value = orgname;
	         parent.window.opener.document.all.item("<%=displayValueInput%>").value = orgid;
	    
	     	 parent.window.opener.document.all.item("<%=displayNameInput1%>").value = jobname;
	         parent.window.opener.document.all.item("<%=displayValueInput1%>").value = jobid;
	     
     parent.window.close();

         
	}
	
	
</SCRIPT>
<head>    
  <title>主管处室选择</title>


<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../css/tab.winclassic.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/treeview.css">
<%@ include file="/include/css.jsp"%>
<link rel="stylesheet" type="text/css" href="../sysmanager/css/contentpage.css">
<link rel="stylesheet" type="text/css" href="../sysmanager/css/tab.winclassic.css">   
<body class="contentbodymargin"  scroll="no">
<div id="contentborder" align="left">

    <table >
        <tr><td>
       请选择主管处室和岗位
         <tree:tree tree="org_tree_tran"
    	           node="org_tree_tran.node"
    	           imageFolder="/sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="abc"
    			   dynamic="false">  
    			   <tree:param name="defaultValue"/>
    			   <tree:param name="displayNameInput"/>
    			   <tree:param name="displayNameInput1"/>
    			   <tree:param name="displayValueInput"/>
    			   <tree:param name="displayValueInput1"/>    			                           
				   <tree:radio name="jobinfo" defaultValue="<%=defaultValue%>"/>
				   
    			   <tree:treedata treetype="ChargeOrgJobTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
    <br>
   <table><tr><td>
    <input name="add" type="button" class="input" value="确定" onclick="okadd()" >
    <input name="exit" type="button" class="input" value="放弃" onclick="parent.window.close();">
    
     </td></tr></table>
</div>
 

 
</body>


