<%@ include file="../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg" %>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree" %>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Restype" %>
<%@ page import="com.frameworkset.common.poolman.DBUtil" %>
<%
		String idString = request.getParameter("idString");
		String type = request.getParameter("type");
    	if(idString!=null && !idString.equals("") && type!=null && !type.equals("")){
    	
	    	String[] ids = idString.split(":");
	    	String resId = "";
	    	StringBuffer sql = new StringBuffer();
	    	sql.append(" select * from td_sm_roleresop t ")
	    		.append("where t.types='"+type+"' and restype_id = 'orgunit' and op_id = 'readorgname' ")
	    		.append(" and role_id in ( ");
	    	DBUtil db = new DBUtil();
	    	for(int i=0;i<ids.length;i++){
	    		if(i==ids.length-1) sql.append(ids[i]+" )");
	    		else sql.append(ids[i]+",");
	    	}
	    	List roleIdList = new ArrayList();
	    	try{
	    		db.executeSelect(sql.toString());
	    		for(int i=0;i<db.size();i++){
	    			roleIdList.add(db.getString(i,"res_id"));
	    		}
	    	}catch (Exception e){
	    		e.printStackTrace();
	    	}
	    	//List roleIds = new ArrayList();
	    	//for(int i=0;i<ids.length;i++){
	    		//roleIds.add(ids[i]);
	    	//}
	    	request.setAttribute("roleIds",roleIdList);
	    	request.setAttribute("role_type",type);
	    	request.setAttribute("resTypeId","orgunit");
	    	//System.out.println(sql);
    	}
%>
<html>
<head>    
  <title>属性容器</title>
  <script language="JavaScript" src="changeView.js" type="text/javascript"></script>
  <script language="javascript">
  	function choose(id){
  		alert(id);
  	}
  </script>
  <%@ include file="/include/css.jsp"%>
  <link rel="stylesheet" type="text/css" href="../css/treeview.css"> 
</head>
<body class="contentbodymargin" scroll="no">
<div id="contentborder">
<form name="theform" id="theform" action="" method="post" target="save">
	<input type="hidden" value="" id="treeids" name="treeids"/>
    <input type="hidden" value="" id="roleids" name="roleids"/>
<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
  
  <tr class="tr" >
     <td  class="td">
     <!-- href="/sysmanager/accessmanager/role/operList_orgs_ajax.jsp?resTypeId=orgunit"-->
     <tree:tree tree="role_org_tree"
    	           node="role_org_tree.node"
    	           imageFolder="../images/tree_images/"
    	           collapse="true"
    			   includeRootNode="true"
    			   target="operList"
    			   dynamic="false"
    			   mode="static"
    			   >                         
                   <tree:param name="roleIds"/>
					<tree:param name="role_type"/>
					<tree:param name="resTypeId" />
                   <tree:checkbox recursive="true"  name="treeid" partuprecursive="true"/>
    			   <tree:treedata treetype="com.frameworkset.platform.menu.OrgGrantUserOrRoleTree"
    	                   scope="request"
    	                   rootid="0"  
    	                   rootName="机构树"
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
</body>
<IFRAME name="save" marginheight="0" marginwidth="0"></IFRAME>
</html>

