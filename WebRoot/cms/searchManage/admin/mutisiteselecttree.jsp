<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%
		AccessControl accesscontroler = AccessControl.getInstance();
    	if(!accesscontroler.checkAccess(request, response))
    		return;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>    
    <title>多选站点树</title>    
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">    
    <link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
  	<link rel="stylesheet" type="text/css" href="../../sysmanager/css/windows.css">
  	<link href="../../inc/css/cms.css" rel="stylesheet" type="text/css"> 
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
  </head>
  
  <script language="javascript">
  	function selectSites()
  	{	
  		var sites = document.getElementsByName("sites");
  		var selectSites = "";
  		for(var i=0;i<sites.length;i++)
  		{
  			if(sites[i].checked == true)
  			{
  				selectSites = selectSites + sites[i].value + ",";
  			}
  		}
  		window.returnValue = selectSites;
		window.close();
  	}
  </script>
  
  <body>
   <table height="90%"  width="100%" background="../images/left_center_bg.jpg"><tr ><td  width="100%" height="100%" valign=top>
    <table>
        <tr><td align="left"> 
         <tree:tree tree="MutisiteSelect_tree"
    	           node="MutisiteSelect_tree.node"
    	           imageFolder="/cms/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="" 
    			   dynamic="false"> 
    			   <tree:checkbox name="sites"/>
    			   <tree:treedata treetype="MutisiteSelect"
    	                   scope="request"
    	                   rootid="0"
    	                   rootName="选择索引站点"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="false"
    	                   />    	          
    	</tree:tree>
         </td></tr>
    </table>	
	</td></tr></table>
	<table height="10%"  width="100%" background="../images/left_center_bg.jpg">
			<tr class="tr">
				<td align="center">
					<input type="button" value="确定" class="cms_button" onClick="selectSites()">
					<input type="button" value="返回" class="cms_button" onClick="window.close()">
				</td>
			</tr>
	</table>
  </body>	
</html>
