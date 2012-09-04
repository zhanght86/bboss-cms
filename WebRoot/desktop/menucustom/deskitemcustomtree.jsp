<%@ page contentType="text/html; charset=UTF-8"%>
<%@page
	import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
	<%--
	描述：桌面窗口的树结构
	作者：qian.wang
	版本：1.0
	日期：2011-09-20
	 --%>
<html>
	<head>
		<title>属性容器</title>

		<script type='text/javascript'
			src='<%=request.getContextPath() %>/include/jquery-1.4.2.min.js'
			language='JavaScript'></script>
		<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>
		<script language="JavaScript" src="changeView.js"
			type="text/javascript"></script>
		<%@ include file="/include/css.jsp"%>
		<link rel="stylesheet" type="text/css" href="../css/treeview.css">
		
         <link href="${pageContext.request.contextPath}/common/css/button.css"
	rel="stylesheet" type="text/css" />
		
		<script>
		function blockUI(msgContent){
	if (!msgContent){
		msgContent = '正在处理，请稍候...';
	}
	$("<div class=\"datagrid-mask\"></div>").css({display:"block", width:"100%",height:$(window).height()}).appendTo("body");
	$("<div class=\"datagrid-mask-msg\"></div>").html(msgContent).appendTo("body").css({display:"block",left: ($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
	}
	
	/**
	* 去掉遮罩层
	* @auther 刘剑峰
	* @date 2011-04-15 
	*/
				function unblockUI(){
					$(".datagrid-mask-msg").remove();
					$(".datagrid-mask").remove();
				}
			$(document).ready(function() {
				//commonInit();	
			});
		
			
			function resetAll(){
			  $.post("../../desktop/resetall.page",function(data){
			    if(data == "ok")
						alert("重置成功!");
				else 		
						alert("重置失败!");
			  });
			
			}
			
			
			
		</script>
	</head>
	<body class="contentbodymargin" scroll="auto">
			<div >

               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button onclick="resetAll()"  class="button" style="width:150">重置所有窗口</button>
				<table class="table" width="100%" border="0" cellpadding="0" 
					cellspacing="1">


					<tr class="tr">
						<td class="td" width="40%" align="center">

						</td>
						<td class="td" width="60%" align="center"></td>
					</tr>
					<tr class="tr">
						<td class="td" width="40%">

							<tree:tree tree="role_column_tree" node="role_column_tree.node"
								imageFolder="/sysmanager/images/tree_images" collapse="true"
								includeRootNode="false" mode="static"
								href="../../desktop/getMenuCustom.page" target="sizewin">
								
								<tree:treedata
									treetype="com.frameworkset.platform.esb.datareuse.common.action.DeskTopMenuCustomTree"
									scope="request" rootid="0" rootName="菜单管理" expandLevel="1"
									showRootHref="false" sortable="false" needObserver="false"
									refreshNode="false" enablecontextmenu="false" />

							</tree:tree>
						</td>
					</tr>
				</table>
				
				
			
			</div>

	</body>
</html>

