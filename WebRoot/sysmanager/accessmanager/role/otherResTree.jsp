<%@ include file="../../include/global1.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../../base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.ResManager,com.frameworkset.platform.resource.ResourceManager"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.frameworkset.platform.sysmgrcore.entity.Restype" %>
<%
	String resTypeId = request.getParameter("resTypeId");
	//ResManager resManager = SecurityDatabase.getResourceManager();
	//Restype resType = new Restype();
	//resType.setRestypeId("0");
	//List list = resManager.getChildResTypeList(resType);
	
	ResourceManager resManager = new ResourceManager();
	
	List list = resManager.getResourceInfos();
	if (list == null)
		list = new ArrayList();
	request.setAttribute("resTypeList", list);
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="javascript">
  	function changeView(){
  		var len =document.all("select").options.length;
  		for(var i=0;i<len;i++){
   			var op=document.all("select").options[i];
   			if(op.selected)
   				var s = op.value;
  		}
  		if(s == "orgunit"){
  			document.location.href="resOrgTree.jsp?resTypeId="+s;
  		}
  		else if(s == "column"){
  			document.location.href="resColumnTree.jsp?resTypeId="+s;
  		}
  		else if(s == "job"){
  			document.location.href="resOrgJobTree.jsp?resTypeId="+s;
  		}
  		else if(s == "user"){
  			document.location.href="resOrgJobUserTree.jsp?resTypeId="+s;
  		}
  		else
  			document.location.href="otherResTree.jsp?resTypeId="+s;
  	}
  </script>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
	<body class="contentbodymargin">
		<div id="contentborder">
			<center>
				<form action="" method="post">
					<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">
						<tr class="tr">
							<td width="25%" class="td" align="center">
								
							</td>
						</tr>
						<tr class="tr">
							<td width="25%" class="td">
								<select class="select" name="select" onChange="changeView()">
									<option>
										--请选择资源--
									</option>
									
									<pg:list requestKey="resTypeList" needClear="false">
									<pg:equal colName="auto" value="true">
										<option value="<pg:cell colName="id"/>"
										<%
								        	//String type = "other";								        	
							        		String restypeId = dataSet.getString(Integer.parseInt(rowid),"id");
							        		if(restypeId.equals(resTypeId))
							        		{
							        			out.println("selected");							        			
							        		}
								        %>
										>
											<pg:cell colName="name" />
										</option>
										</pg:equal>

									</pg:list>									
									
								</select>
							</td>
						</tr>
						<tr class="tr">
							<td class="td" width="40%" align="center">
								
							</td>
						</tr>
						<tr class="tr">
							<td class="td">

								<tree:tree tree="other_res_tree" node="other_res_tree.node" imageFolder="../../images/tree_images/" collapse="true" includeRootNode="true" href="/sysmanager/accessmanager/role/operList.jsp" target="operList">
									<tree:param name="resTypeId"/>

									<tree:treedata treetype="OtherTree" scope="request" rootid="<%=resTypeId%>" rootName="资源树" expandLevel="1" 
									showRootHref="false" 
									needObserver="false" 
									refreshNode="false"
									/>
								</tree:tree>
							</td>
						</tr>
					</table>
				</form>
			</center>
		</div>
	</body>
</html>

