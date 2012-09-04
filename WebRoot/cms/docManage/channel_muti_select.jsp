<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%
	String resTypeId = request.getParameter("resTypeId");
	String srcchannelid = request.getParameter("channelid");
	String siteid = request.getParameter("siteid");

	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();  
	
	if(srcchannelid != null){
		session.removeAttribute("srcchannelid");
		session.setAttribute("srcchannelid",srcchannelid);
	}else{
		srcchannelid = (String)session.getAttribute("srcchannelid");
		session.removeAttribute("srcchannelid");
	}
	String docidStr = request.getParameter("docidStr");	
	String datalist = "MoveDocTree";  //有新增文档权限的频道才显示出来
%>
<html>

<head>    
  <title>文档转发：：</title>
  <script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  <link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
  <link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
  <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
</head>
<body class="contentbodymargin" scrolling="no">
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
    			   href=""
    			   target="abc"
				   dynamic="false"
    			   >                         
                   <tree:param name="resTypeId"/>
                   <tree:param name="docidStr"/>
                   <tree:param name="siteid"/>
				   <tree:param name="channelid"/>
				   <tree:param name="flag"/>
                   <tree:checkbox recursive="true"  name="channelInfo"/>
					<%
				        	String temp_id = "site:"+siteid;
				        	String name ="站点("+sitename+")下的频道";
				    %>
    			   <tree:treedata treetype="<%=datalist%>"
    	                   scope="request"
    	                   rootid="siteChnlTree"  
    	                   rootName="站点频道树"
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