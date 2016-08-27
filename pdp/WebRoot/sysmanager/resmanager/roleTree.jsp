

<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ include file="../base/scripts/panes.jsp"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>

<%@ page import="org.frameworkset.web.servlet.support.RequestContextUtils"%>

<%
	request.setAttribute("rootName", RequestContextUtils.getI18nMessage("sany.pdp.role.tree.name", request));
%>
<SCRIPT LANGUAGE="JavaScript"> 
	var api = frameElement.api, W = api.opener;
	
	function okadd(){	
		selectNode = document.all.item("userinfo");
		
	if(selectNode==null){
		$.dialog.alert('<pg:message code="sany.pdp.to.choose.roles"/>'); return;
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
 		$.dialog.alert('<pg:message code="sany.pdp.to.choose.roles"/>'); return;
 	}    
 	
 		W.document.getElementById("${param.tag}").value = ret;
 	
 		api.close();
	}
	
	
</SCRIPT>
<head>    
  <title>请选择角色</title>

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
    			   dynamic="false">  			                           
				   <tree:radio name="userinfo"/>
				   
    			   <tree:treedata treetype="com.frameworkset.platform.menu.ChargeRoleTree"
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
