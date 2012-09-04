<%
/**
 * 
 * <p>Title: 用户组添加资源授予页面</p>
 *
 * <p>Description: 用户组添加资源授予操作页面</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: chinacreator</p>
 * @Date 2006-9-15
 * @author gao.tang
 * @version 1.0
 */
 %>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%
	String resTypeId = request.getParameter("resTypeId");
	String currRoleId = request.getParameter("currRoleId");
	String role_type = request.getParameter("role_type");
	String currOrgId = request.getParameter("currOrgId");
	String isBatch = request.getParameter("isBatch");
	
%>
<html>
	<head>
		<title>属性容器</title>
		<script language="JavaScript" src="changeView.js" type="text/javascript"></script>
<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
<script language="Javascript">
function saveReadorgname(){
	var checkValues = "";
	var un_checkValues = "";
	var arr = document.getElementsByName("checkboxValue");
	if(arr){
		for(var i = 0; i < arr.length; i++){
			if(arr[i].checked){
				if(checkValues==""){
					checkValues = arr[i].value;
				}else{
					checkValues += "," + arr[i].value;
				}
			}else{
				if(un_checkValues==""){
					un_checkValues = arr[i].value;
				}else{
					un_checkValues += "," + arr[i].value;
				}
			}
		}
	}
	document.Form1.checkValues.value = checkValues;
	document.Form1.un_checkValues.value = un_checkValues;
	var url = "saveTreeRoleresop_handle.jsp?resTypeId=<%=resTypeId%>&currRoleId=<%=currRoleId%>&role_type=<%=role_type%>&opId=usergroupset&currOrgId=<%=currOrgId%>&isBatch=<%=isBatch%>";
	document.Form1.target = "saveres";
	document.Form1.action = url;
	document.Form1.submit();
}
</script>
	</head>
	<body class="contentbodymargin" scroll="no">
	<form name="Form1"  method="post" >
		<DIV align="center">
		<tr>
		<td>
		<input name="checkValues" value="" type="hidden" />
		<input name="un_checkValues" value="" type="hidden" />
		<input name="sav" value="保存" type="button" onclick="saveReadorgname()" class="input" />
		
<input name="sx" value="刷新" class="input" type="button" onclick="parent.window.location.href = parent.window.location.href;" />
		</td>
		</tr>
		</DIV>
		<div id="contentborder" style="width:100%;height:530;overflow:auto">

				
					<table class="table" width="80%" border="0" cellpadding="0" cellspacing="1">

						<tr class="tr">
							<td class="td">

								<tree:tree tree="res_groupTree_tree" 
								node="res_groupTree_tree.node" 
								imageFolder="../../sysmanager/images/tree_images/" 
								collapse="true" includeRootNode="true" 
								href="" 
								target=""
								mode="static">
								<tree:param name="resTypeId"/>
			                   <tree:param name="currRoleId"/>
			                   <tree:param name="role_type"/>
			                   <tree:param name="currOrgId"/>
			                   <tree:param name="isBatch"/>

								<tree:checkbox recursive="true" partuprecursive="true"  name="checkboxValue"/>

								<tree:treedata treetype="com.frameworkset.platform.sysmgrcore.purviewmanager.menu.GroupTree" 
								scope="request" 
								rootid="0" 
								rootName="用户组" 
								expandLevel="1" 
								showRootHref="false" 
								needObserver="false" 
								refreshNode="false"
								/>
								</tree:tree>
							</td>
						</tr>
					</table>
				

		</div>
<iframe name="saveres" width="0" height="0"></iframe>
</form>
	</body>
</html>

