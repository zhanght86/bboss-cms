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
	String	temp_id = "siteChnlTree";
    String  name = "站点频道树";
    
    String siteid = request.getParameter("siteid");
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>相关文档的选择</title>
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
</head>
<body class="contentbodymargin" scrolling="no">
<div id="contentborder">
    <table >
        <tr><td>
         <tree:tree tree="chnxz_tree"
    	           node="chnxz.node"
    	           imageFolder="../../sysmanager/images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   nowrap="true"
				   href=""
    			   target="chn_doc_list"
    			   mode="static-dynamic">
				   <tree:radio name="channelInfo" />
				   <%
							name = "站点频道树";
							temp_id = "ResourceManager";
							if(siteid != null && siteid.length()!=0){
					        	temp_id = "site:"+siteid;
					        	name ="站点("+sitename+")下的频道";
					        }
				    %>
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