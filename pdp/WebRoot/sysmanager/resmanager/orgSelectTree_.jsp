<% 
  response.setHeader("Cache-Control", "no-cache"); 
  response.setHeader("Pragma", "no-cache"); 
  response.setDateHeader("Expires", -1);  
  response.setDateHeader("max-age", 0); 
%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
/**
 * 
 * <p>Title: 选择机构</p>
 *
 * <p>Description: 选择机构</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: bboss</p>
 * @Date 2008-11-4
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<%
	AccessControl accessControl = AccessControl.getInstance();
	if(!accessControl.checkManagerAccess(request, response)){
		return;
	}
	String restypeId = request.getParameter("restypeId");
%>

<html>
<head>
	<title></title>
	<%@ include file="/include/css.jsp"%>
	<script type="text/javascript" language="Javascript">
		function okadd(){	
			selectNode = document.all.item("orgid");
		
			if(selectNode==null){
 				alert("请选择机构"); return;
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
 				alert("请选择机构"); return;
 			}    
			window.returnValue = ret; 
			window.close();
		}
	
	</script>
</head>

<body class="contentbodymargin"  scroll="no">
<div align="center">
<tr>
<td>
<input name="add" type="button" class="input" value="确定" onclick="okadd()" >
    <input name="exit" type="button" class="input" value="取消" onclick="parent.window.close();">
</td>
</tr>
</DIV>
<div id="contentborder" style="width:100%;height:495;overflow:auto">

    <table >
    	<%if(restypeId != null && !"".equals(restypeId)){ %>
    	<tr>
    	<td><input name="orgid" type="radio" value="orgunit:orgunit:global"/> 机构全局资源</td>
    	</tr>
    	<%} %>
        <tr><td>
         <tree:tree tree="selectorgnode"
    	           node="selectorgnode.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="abc"
    			   mode="static-dynamic">  			                           
				   <tree:radio name="orgid"/>
				   
				   
    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.OrgTree"
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
</div>
</body>

</html>
