<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
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
	    	List roleIds = new ArrayList();
        	
        	for(int i=0;i<ids.length;i++){
	        		roleIds.add(ids[i]);
        	}
        	//如果是用户,则要查出用户的角色ID与用户所属机构岗位下的ID
        	//if(type.equals("user")){
        		//String sql1 = " select role_id from td_sm_userrole where 1<>1 ";
        		//String sql2 = " select role_id from td_sm_orgjobrole where org_id in ("+
							 // " select org_id from td_sm_userjoborg t where 1<>1 ";
				//DBUtil db = new DBUtil();
				//for(int i=0;i<ids.length;i++){
					//sql1 += " or user_id = " + ids[i];
					//sql2 += " or user_id = " + ids[i];
				//}
				//try{
				//	String sql = sql1+" union "+sql2+") ";
					//System.out.println(sql);
					//db.executeSelect(sql);
					//for(int i=0;i<db.size();i++){
						//roleIds.add(db.getString(i,"role_id"));
					//}
				//}catch(Exception e){
					//e.printStackTrace();
				//}
        	//}
	    	request.setAttribute("roleIds",roleIds);
	    	request.setAttribute("role_type",type);
	    	request.setAttribute("resTypeId","column");
	    }	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../sysmanager/css/treeview.css">
	<body class="contentbodymargin" scroll="no">
		<div id="contentborder">
<form name="theform" id="theform" action="" method="post" >
		<input type="hidden" value="" id="treeids" name="treeids"/>
    <input type="hidden" value="" id="roleids" name="roleids"/>		
					<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
						
						
						<tr class="tr">
							<td class="td" width="40%" align="center">
								
							</td>
						</tr>
						<tr class="tr">
							<td class="td">

								<tree:tree tree="role_column_tree"
								 node="role_column_tree.node" 
								 imageFolder="../../sysmanager/images/tree_images/" 
								 collapse="true" includeRootNode="true" 
								 href="" 
								 target="operList" mode="static">
								 
									<tree:param name="roleIds"/>
									<tree:param name="role_type"/>
									<tree:param name="resTypeId" />
									<tree:checkbox recursive="true" partuprecursive="true" name="treeid"  />
									<tree:treedata 
									treetype="com.frameworkset.platform.menu.MenuResTree" 
									scope="request" rootid="0" 
									rootName="菜单树" 
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

