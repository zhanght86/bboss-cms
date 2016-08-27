<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>

<%@page import="com.frameworkset.util.StringUtil"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	String defaultValue = request.getParameter("defaultValue");
	if(defaultValue == null)
		defaultValue = "";
%>

<%
	request.setAttribute("rootName", RequestContextUtils.getI18nMessage("sany.pdp.organization.tree.name", request));
%>

<SCRIPT LANGUAGE="JavaScript"> 
	var api = frameElement.api, W = api.opener;

	function okadd(){	
		selectNode = document.all.item("jobinfo");
		
	if(selectNode==null){
 		$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.view"/>'); return;
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
 		$.dialog.alert('<pg:message code="sany.pdp.purviewmanager.rolemanager.role.view"/>'); return;
 	}    
 	
	var temps = ret.split("$");
	var ids  = temps[0].split(":");
	var names  = temps[1].split(":");	
	var orgid = ids[0];
	var orgname = names[0];
	var jobid = ids[1];
	var jobname = names[1];
	
	var returnValue = orgname + ":" + jobname + "," + orgid + ":" + jobid;
	
	W.document.getElementById("${param.tag1}").value = returnValue.substring(0,returnValue.indexOf(","));
 	W.document.getElementById("${param.tag2}").value = returnValue.substring(returnValue.indexOf(",")+1,returnValue.length);
	
	api.close();
	
	//window.returnValue = ret;      
	//window.returnValue = orgname + ":" + jobname + "," + orgid + ":" + jobid;   
	//window.close();
	
	}
	
	
</SCRIPT>
<head>    
  <title>请选择机构和岗位</title>

 
<body class="contentbodymargin"  scroll="no">
<div class="btnarea" >
	<a href="javascript:void(0)" class="bt_1" id="add" name="add" onclick="okadd()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a>
	<a href="javascript:void(0)" class="bt_2" id="cancel" name="cancel" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.cancel"/></span></a>
</div>


<div id="contentborder" style="width:100%;height:495;overflow:auto">

    <table >
        <tr><td>
         <tree:tree tree="org_tree_tran"
    	           node="org_tree_tran.node"
    	           imageFolder="/sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="abc"
    			   mode="static-dynamic">  
    			   <tree:param name="defaultValue"/>
    			   <tree:param name="displayNameInput"/>
    			   <tree:param name="displayNameInput1"/>
    			   <tree:param name="displayValueInput"/>
    			   <tree:param name="displayValueInput1"/>    			                           
				   <tree:radio name="jobinfo" defaultValue="<%=defaultValue%>"/>
				   
    			   <tree:treedata treetype="com.frameworkset.platform.menu.ChargeOrgJobTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="${rootName}"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
 

 
</body>
