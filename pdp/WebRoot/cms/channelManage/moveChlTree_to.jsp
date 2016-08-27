<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);

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
   	 <tree:tree tree="moveChl_to"
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
                   <tree:radio name="channelInfo"/>
					<%
				        	String temp_id = "site:"+siteid;
				    %>
    			   <tree:treedata treetype="MoveChlTree"
    	                   scope="request"
    	                   rootid="<%=temp_id%>"  
    	                   rootName="<%=sitename%>"
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