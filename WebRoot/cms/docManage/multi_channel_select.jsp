<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%
	String resTypeId = request.getParameter("resTypeId");
	String siteid = request.getParameter("siteid");
	
	String taskType = request.getParameter("taskType");
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = "";
	String chnlTree = "ChnlTree";
	if(siteid==null || siteid.length()==0){
		chnlTree = "SiteChnlSelTreeOfTask";
	}else{
		sitename = siteManager.getSiteInfo(siteid).getName();
	}
%>
<html>
<head>    
  <title>:::多频道选择::::::</title>
  <script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  <link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
  <link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
  <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<body name="testbody" class="contentbodymargin" scrolling="no">
<div id="contentborder">
<script language="javascript">
  	
</script> 
<form name="form2" method="post">
<table class="table" width="100%" border="0" cellpadding="0" cellspacing="1">
  <tr  align="left">
     <td  align="left"> 
   	 <tree:tree tree="channel_org_tree"
    	           node="channel_org_tree.node"
    	           imageFolder="../../cms/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   mode="static-dynamic"
    			   >   
    			   <tree:param name="siteid"/> 
    			   <tree:param name="taskType"/>                     
                   <tree:checkbox name="channelInfo" recursive="true"/>
					<%
							String name = "站点频道树";
							String temp_id = "ResourceManager";
							if(siteid != null && siteid.length()!=0){
					        	temp_id = "site:"+siteid;
					        	name ="站点("+sitename+")下的频道";
					        }
				    %>
    			   <tree:treedata treetype="<%=chnlTree%>"
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
<script language="javascript">
	function getHtml()
	{
		var body = document.all.item("testbody");
		alert(body);
		alert(body.innerHtml);
	}
</script>