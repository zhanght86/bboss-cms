<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	request.setAttribute("rootName", RequestContextUtils.getI18nMessage("sany.pdp.organization.user.tree.name", request));
%>
<SCRIPT LANGUAGE="JavaScript"> 
	var api = frameElement.api, W = api.opener;
	
	function okadd(){	
		
		selectNode = document.all.item("userinfo");
		
	if(selectNode==null){
		$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.orguser.select"/>'); return;
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
 		$.dialog.alert('<pg:message code="sany.pdp.sysmanager.resource.orguser.select"/>'); return;
 	}    
 	
 	W.document.getElementById("${param.tag}").value = ret;
 	
 	api.close();
 	
	//window.returnValue = ret;   
	//window.close();
	}
	
	
</SCRIPT>
<head>    
  <title>请选择机构下的用户</title>

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
    			   
    			  
				   <tree:radio name="userinfo"/>
				   
    			   <tree:treedata treetype="com.frameworkset.platform.menu.ChargeUserTree"
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
