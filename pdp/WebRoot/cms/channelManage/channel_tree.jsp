<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ include file="/sysmanager/include/global1.jsp"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String channelName = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	String flag = request.getParameter("flag");
	String href = "../docManage/doc_list.jsp?channelName="+channelName+"&siteid="+siteid+"&channelId="+channelId;
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>相关文档的选择</title>
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
</head>
<body class="contentbodymargin" scrolling="no">
<div id="contentborder">
    <table >
        <tr><td>
        <%
        	String temp_id = "site:"+siteid;
			SiteManager sm = new SiteManagerImpl();
        	String name ="站点(" + sm.getSiteInfo(siteid).getName() + ")下的频道";
        	if("2".equals(flag) || "3".equals(flag) || "4".equals(flag)){
        		temp_id = "siteChnlTree";
        		name = "站点频道树";
        	}
        %>
         <tree:tree tree="chnxz_tree"
    	           node="chnxz.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   nowrap="true"
				   href="doc_list.jsp"
    			   target="chn_doc_list"
    			   dynamic="false"
    			   >
				   <tree:param name="siteid"/>
				   
				   <tree:treedata treetype="ChnlTree"
    	                   scope="request"
    	                   rootid="<%=temp_id%>"
    	                   rootName="<%=name%>" 
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
                           refreshNode="false"
    	                   />

    	</tree:tree>
         </td></tr>
    </table>
</div>
</body>
</html>