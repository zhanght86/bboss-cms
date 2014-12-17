<%
/**
 * <p>Title: 机构转移页面</p>
 * <p>Description: 机构转移页面</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-3-17
 * @author da.wei
 * @version 1.0
 **/
 %>
 
<%     
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0); 
%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%@page import="com.frameworkset.util.StringUtil"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%
 //得到上个页面传入的orgName参数 2008-3-20 baowen.liu
	 String remark5 = request.getParameter("orgName");//机构orgName
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request,response);
	String orgId = StringUtil.replaceNull(request.getParameter("orgId"));
	boolean isAdmin = control.isAdmin();
//判断操作是否成功
	String isok =  (String)request.getAttribute("isOk");

//以下代码供左边树刷新使用
	String action = StringUtil.replaceNull(request.getParameter("action"));
	String updateorgId =  StringUtil.replaceNull(request.getParameter("updateorgId"));
	String updateparentId =  StringUtil.replaceNull(request.getParameter("updateparentId"));
%>
<SCRIPT LANGUAGE="JavaScript"> 

var api = frameElement.api, W = api.opener;

function okadd(){	
 if(selectNode==null)
 {
 	$.dialog.alert('<pg:message code="sany.pdp.to.choose.organization"/>'); return;
 }  
 //document.all.divProcessing.style.display = "block";
 //document.getElementById("tranSaveButton").disabled = true;
 //document.getElementById("tranBackButton").disabled = true;

 document.Form1.action = "orgtran_do.jsp?orgId=<%=orgId%>&remark5=<%=remark5%>&parentId="+selectNode;
 document.Form1.target = "hiddenFrame";
 document.Form1.submit();          
}

function alertfun(msg)
{
	
	$.dialog.alert(msg);
}
</SCRIPT>
<html>
	<head>
		<title>机构转移</title>
		<link rel="stylesheet" type="text/css" href="../../css/treeview.css">
	</head>
	<body class="contentbodymargin" scroll="auto">
		<div id="" align="center">
			<form name="Form1" action="" method="post">			
				<table>
					<tr>
						<td>
							<pg:message code="sany.pdp.to.choose.organization"/>
							<% 
								if(isAdmin){//不是拥有超级管理员角色的用户不能将机构转移到根节点
							%>
							<tree:tree 
							tree="org_tree_tran" 
							node="org_tree_tran.node" 
							imageFolder="/sysmanager/images/tree_images/" 
							collapse="true" 
							includeRootNode="true" 
							href="aa.jsp" 
							target="abc"
							mode="static-dynamic"
							>
										
								<tree:param name="parentId" />
								<tree:param name="orgId" />
								<tree:treedata 
								treetype="com.frameworkset.platform.menu.CurrentorgAndLisanTree" 
								scope="request" 
								rootid="0" 
								rootName="机构管理" 
								rootNameCode="sany.pdp.organization.manage"
								expandLevel="1" 
								showRootHref="true"
								needObserver="false" />

							</tree:tree>
							<% 
								}else{
							%>
							<tree:tree 
							tree="org_tree_tran" 
							node="org_tree_tran.node" 
							imageFolder="/sysmanager/images/tree_images/" 
							collapse="true" 
							includeRootNode="true" 
							href="aa.jsp" 
							target="abc"
							mode="static-dynamic"
							>
										
								<tree:param name="parentId" />
								<tree:param name="orgId" />
								<tree:treedata 
								treetype="com.frameworkset.platform.menu.CurrentorgAndLisanTree" 
								scope="request" 
								rootid="0" 
								rootName="机构管理" 
								rootNameCode="sany.pdp.organization.manage"
								expandLevel="1" 
								showRootHref="false"
								needObserver="false" />

							</tree:tree>
							<% 
								}
							%>
						</td>
					</tr>
				</table>
				<br>
				<table>
					<tr>
						<td>
							<a class="bt_1" onclick="okadd()"><span><pg:message code="sany.pdp.common.operation.ok"/></span></a>
							
						</td>
					</tr>
				</table>
		
		<div style="display:none">
			<iframe src="" id="abc" name="abc" style="width:0;height:0">
		</div>
		</form>
		</div>
		<div id=divProcessing style="width:200px;height:30px;position:absolute;left:200px;top:300px;display:none">
			<table border=0 cellpadding=0 cellspacing=1 bgcolor="#000000" width="100%" height="100%">
				<tr>
					<td bgcolor=#3A6EA5>
						<marquee align="middle" behavior="alternate" scrollamount="5">
							<font color=#FFFFFF>...处理中...请等待...</font>
						</marquee>
					</td>
				</tr>
			</table>
		</div>
	</body>
	<iframe name="hiddenFrame" width=0 height=0></iframe>
</html>


