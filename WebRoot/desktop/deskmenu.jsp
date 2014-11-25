<%@ page contentType="text/html; charset=UTF-8"%>
<%@page
	import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/treetag.tld" prefix="tree"%>
<%
String customtype = request.getParameter("customtype");
String url = "updatedeskmenu.page";
String cleardeskmenu = "cleardeskmenu.page";
if(customtype != null && customtype.equals("default"))
{
	url = "updatedefaultdeskmenu.page";
	cleardeskmenu = "cleardefaultdeskmenu.page";
}

%>
<html>
	<head>
		<title>属性容器</title>
		<pg:config enablecontextmenu="false"/>
	
		
		<%@ include file="/include/css.jsp"%>
		
		
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
			function save(){
				$("#myform").form('submit', {
				    "url": "<%=url%>",
				    onSubmit:function(){			
						//显示遮罩							
						blockUI();	
				    },
				    success:function(text){	
				    	
				    	//去掉遮罩	
						unblockUI();
						$.messager.alert("提示对话框" , "设置成功");	
						 window.parent.frames[1].location.href = window.parent.frames[1].location.href ; 
				    }
				});	
			}
			
			function clearmenus(){
				$("#myform").form('submit', {
				    "url": "<%=cleardeskmenu%>",
				    onSubmit:function(){			
						//显示遮罩							
						blockUI();	
				    },
				    success:function(text){	
				    	
				    	//去掉遮罩	
						unblockUI();
						$.messager.alert("提示对话框" , "清除成功");
						var menupaths = document.getElementsByName("menupath");
						for(var i=0;i<menupaths.length;i++)
						   menupaths[i].checked = false;
						     
						try{
						   window.parent.frames[1].location.href = window.parent.frames[1].location.href ; 
						  }
						  catch(e)
						  {
						  	//alert(e);
						  }
				    }
				});	
			}
			
			
			
			
			
		</script>
	</head>
	<body class="contentbodymargin" scroll="no">
		<form name="myform" method="post" id="myform">
			<div id="contentborder">


				<table class="table" width="80%" border="0" cellpadding="0"
					cellspacing="1">


					<tr class="tr">
						<td class="td" width="40%" align="center">

						</td>
					</tr>
					<tr class="tr">
						<td class="td">

							<tree:tree tree="role_column_tree" node="role_column_tree.node"
								imageFolder="/sysmanager/images/tree_images" collapse="true"
								includeRootNode="false" mode="static-dynamic">
								<tree:param name="customtype" />
								<tree:checkbox name="menupath" />
								<tree:treedata
									treetype="com.frameworkset.platform.esb.datareuse.common.action.DeskTopMenuTree"
									scope="request" rootid="0" rootName="菜单管理" expandLevel="1"
									showRootHref="false" sortable="false" needObserver="false"
									refreshNode="false" enablecontextmenu="false" />

							</tree:tree>
						</td>
					</tr>
				</table>
				<table width="100%">
					<tr>
						<td align="center">
							<input type="button" value="保存" onclick="save()" />
							<input type="button" value="清除" onclick="clearmenus()" />
						</td>
					</tr>
				</table>

			</div>

		</form>
	</body>
</html>
