<%
/**
 * <p>Title: 机构树</p>
 * <p>Description: 机构树</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
AccessControl accessControl = AccessControl.getInstance();
accessControl.checkManagerAccess(request,response);
 
%> 
 
 
 
 
 



<form name="orgtreeform" action="" method="post" style="height:550px;overflow:auto;" >

    <table >
        <tr><td align="left">
         <tree:tree tree="org_tree_userorgmanager"
    	           node="org_tree.node"
    	           imageFolder="../../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="false"
    			   href="../user/userquery_content_tab.jsp" 
    			   target="org_userlist" 
    			   mode="static-dynamic" jquery="true"
    			   >     			    
    			   <tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.PurviewManagerOrgTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
    	                   rootNameCode="sany.pdp.organization.tree.name"
    	                   expandLevel="1"
    	                   showRootHref="false"
    	                   needObserver="false"
    	                   enablecontextmenu="true" 
    	                   />					
    	</tree:tree>
         </td></tr>
    </table>
</form>
<iframe name="hiddenFrame" width=0 height=0 frameborder="0"></iframe>
 
 

