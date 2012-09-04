<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%
	String resTypeId = request.getParameter("resTypeId");
	
	String siteid = request.getParameter("siteid");

	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();  
	
	
	String datalist = "ChnlTree";  //有新增文档权限的频道才显示出来
%>
<html>
<head>    
  <title>频道树</title>
  <script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  <link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
  <link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
  <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<base target="_self">
<body  scrolling="no"  style="boder:0px">
<div id="contentborder11">
<form name="form2" method="post">
<table class="table" width="100%" border="0" cellpadding="0" cellspacing="1">
  <tr  align="left">
     <td  align="left"> 
					<%
				        	String temp_id = "site:"+siteid;
				        	String name ="站点("+sitename+")下的频道";
				    %>
   	 <tree:tree tree="channel_org_tree"
    	           node="channel_org_tree.node"
    	           imageFolder="../../cms/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   href=""
    			   target="abc"
    			   >                         
                   <tree:param name="resTypeId"/>
                   <tree:param name="docidStr"/>
                   <tree:param name="siteid"/>
				   <tree:param name="channelid"/>
				   <tree:param name="flag"/>
                   <tree:radio name="channelInfo"/>
    			   <tree:treedata treetype="<%=datalist%>"
    	                   scope="request"
    	                   rootid="<%=temp_id%>"
    	                   rootName="<%=name%>"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   refreshNode="true"
    	                   />
    	</tree:tree>
	</td>				  
  </tr>  
</table>
</form>
</div>
<div height=0 width=0 style="display:none">
	<iframe name="handleMoveFrame"> </iframe>
</div>
</body>

</html>