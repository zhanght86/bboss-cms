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
    <title>库表的列单选树</title>    
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
  	function selectColumns()
  	{	
  		var columnValues = document.getElementsByName("columns");
  		//var columns = document.getElementsByName("columns");
  		var selectColumns = "";
  		//var columnSelected = "";
  		for(var i=0;i<columnValues.length;i++)
  		{
  			if(columnValues[i].checked == true)
  			{
  				//columnSelected = columnValues[i].value;
  				//columnSelected = columnSelected.substring(0,columnSelected.indexOf("("))
  				selectColumns = selectColumns + columnValues[i].value + ",";
  			}
  		}
  		window.returnValue = selectColumns;
		window.close();
  	}
  </script>
  
  <%  
  String table_name = request.getParameter("table_name");
  if(table_name.equals(null) || table_name==null){
  	table_name = "";
  }
  String db_name = request.getParameter("db_name");
  if(db_name.equals(null) || db_name==null){
  	db_name = "";
  }
  session.removeAttribute("table_name");
  session.setAttribute("table_name",table_name);
  session.removeAttribute("db_name");
  session.setAttribute("db_name",db_name);
  %>
  
  <body>
   <table height="90%"  width="100%" background="../images/left_center_bg.jpg"><tr ><td  width="100%" height="100%" valign=top>
    <table>
        <tr><td align="left"> 
         <tree:tree tree="ColumnSelect_tree"
    	           node="ColumnSelect_tree.node"
    	           imageFolder="/cms/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="" 
    			   dynamic="false"> 
    			   
    			   <tree:radio name="columns"/>
    			   
    			   <tree:treedata treetype="ColumnSelect"
    	                   scope="request"
    	                   rootid="0"
    	                   rootName="选择列"
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
					<input type="button" value="确定" class="cms_button" onClick="selectColumns()">
					<input type="button" value="返回" class="cms_button" onClick="window.returnValue = 'nothing done';window.close();">
				</td>
			</tr>
	</table>
  </body>	
</html>
