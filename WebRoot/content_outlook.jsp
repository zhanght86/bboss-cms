<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@page import="com.frameworkset.platform.framework.MenuHelper"%>
<%@page import="com.frameworkset.platform.framework.ModuleQueue"%>
<%@page import="com.frameworkset.platform.framework.Module"%>
<%@page import="com.frameworkset.platform.framework.Item"%>
<%@page import="com.frameworkset.platform.framework.ItemQueue"%>


<%--
描述：Tab页与页面
作者：刘剑峰
版本：1.0
日期：2011-06-01
 --%>


<%%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8" />
		<title></title>
		<%@ include file="/common/jsp/commonCssScript.jsp"%>
		<%@ include file="/common/jsp/jqureyEasyui.jsp"%>
		<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>

		<script type="text/javascript">
			function addTab(title, url){				
				if ($('#tt').tabs('exists', title)){
					$('#tt').tabs('select', title);
				} 
				else {
					var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';

					$('#tt').tabs('add',{
						title:title,
						content:content,
						closable:true
					});
				}
			}

//缓存二级菜单
var moduleItems = new Array();

var westmenus=new Array();

var oldItems;

function hasSubModule(itemsBean)
{
	if(itemsBean.listModules && itemsBean.listModules.length > 0)
		return true;
	return false;
}
function removeOld(){
	if(oldItems != null )
	{
		menubar_id = "menubar_" + oldItems.parentId;
		$("#"+menubar_id).hide();
		
	}
}


function addMenubars(menuid)
{
	var menudiv="<div class=\"easyui-accordion\" animate=\"false\" fit=\"true\" border=\"false\" id=\""+menuid+"\"></div>";
	$("#west_area").append(menudiv);
	westmenus[menuid] = menuid;
	if($.parser)	$.parser.parse("#west_area");
}
/**
 * 显示二级菜单
 * @param moduleId 一级菜单id
 */
function showitem(moduleId,moduleName) {
	if(oldItems != null && oldItems.parentName==moduleName)
	{
		return;
	}
	var menubar_id = "menubar_" + moduleId;
	
	if(westmenus[menubar_id])
	{
		removeOld();
		oldItems = moduleItems[moduleId];
		$("#" + menubar_id).show();
		$('#'+menubar_id).accordion('resize');
		$('#'+menubar_id).accordion('resize');
		return;
	}
	westmenus[menubar_id] = menubar_id;
	removeOld();
	$("#" + menubar_id).show();
	//如果本一级菜单没有点击过，通过Ajax从后台获取二级菜单数据，然后通过js缓存
	if (!moduleItems[moduleId]){	
		$.ajax({
			type : "post",
			dataType : "json",
			data : {
				"moduleId" : moduleId
			},
			url : "menu/queryItemsBean.page",
			cache : false,
			async : false,// ajax同步标志修改
			error : function() {
				window.parent.locaction="logout.jsp";				
			},
			success : function(data) {
				//将返回的数据放入js缓存
				moduleItems[moduleId] = data;				
			}
		});
	}
		
	//从js缓存中读取二级菜单信息
	var itemsBean = moduleItems[moduleId];	
	oldItems=itemsBean;
	if(itemsBean.listItems )
	{
		//$("#menudiv").accordion('remove',oldtitle);
		
		$("#"+menubar_id).accordion('add',{
			title:moduleName
		});
		
		//$("#"+menubar_id).accordion('getPanel'
		var a=$("#"+menubar_id).accordion('getPanel',moduleName);
		
	
	
		if (itemsBean.listItems.length > 0) {
			$(a).append("<ul id=\"tree"+moduleId+"\"></ul>");
			$("#tree"+moduleId).tree();
			$.each(itemsBean.listItems, function(i) {
				var name = this.name;
				var dataLink = this.pathU;
				var aid=this.id;
				$("#tree"+moduleId).tree('append',{
					parent:null,
					data:[{
						id:aid,
						text:"<span onclick=\"addTab('"+name+"','"+dataLink+"')\">"+this.name+"</span>",
						onClick:function(node){
						//$(this).tree('toggle', node.target);
						
						}
					}]
				});
				
			});
		}
	}
	if(itemsBean.listModules && itemsBean.listModules.length > 0) 
	{	
		$.each(itemsBean.listModules, function(i) {
			$("#"+menubar_id).accordion('add',{
				title:this.name
			});
			
			var a=$("#"+menubar_id).accordion('getPanel',this.name);
			var innerid = 	this.id;				
			if (!moduleItems[innerid]){
				
				$.ajax({
					type : "post",
					dataType : "json",
					data : {
						"moduleId" : innerid,
						"parentPath" : itemsBean.parentPath
					},
					url : "menu/queryItemsBean.page",
					cache : false,
					async : false,// ajax同步标志修改
					error : function() {
						window.parent.locaction="logout.jsp";				
					},
					success : function(data) {
						//将返回的数据放入js缓存	
											
						moduleItems[innerid] = data;				
					}
				});
			}
			var itemsBean_inner = moduleItems[innerid];
			$(a).append("<ul id=\"tree"+innerid+"\"></ul>");
			$("#tree"+innerid).tree(
			{
			onClick:function(node){
					$(this).tree('toggle', node.target);
					//alert('you dbclick '+node.text);
				},
			onBeforeExpand
			:function(node){
					//$(this).tree('toggle', node.target);
					//alert('you dbclick '+node.id);
					
					if(node.attributes.hasSon == false)
						return false;
					getTreeData(node);
				}});
			if (itemsBean_inner.listItems.length > 0) {
				$.each(itemsBean_inner.listItems, function(i) {
					var name = this.name;
					var dataLink = this.pathU;
					var aid=this.id;
					$("#tree"+innerid).tree('append',{
						parent:null,
						data:[{
							id:aid,
							text:"<span onclick=\"addTab('"+name+"','"+dataLink+"')\">"+this.name+"</span>"
							
						}]
					});
					
				});
			}
			
			if(itemsBean.listModules && itemsBean.listModules.length > 0) {
				
				$.each(itemsBean_inner.listModules, function(i) {
					var aid=this.id;
					var name = this.name;
					var dataLink = this.pathU;
					var onFun="addTab('"+name+"','"+dataLink+"')";
					var _parentPath = itemsBean_inner.parentPath;
					$("#tree"+innerid).tree('append',{
						parent:null,
						data:[{
							id:aid,
							text:this.name,							
							state:'closed',
							animate:false,
							attributes:{"hasSon":this.hasSon,"nodeType":this.type,"parentPath":_parentPath,"treeid":innerid}
						}]
					});
					
				});
			}
		});
	}

	
	

}

//定义已经加载了的节点
var clickpNode=new Array();
function getTreeData(node){
	
	var parentPath=node.attributes.parentPath;
	if(!clickpNode[node.id]){
		$.ajax({
				type : "post",
				dataType : "json",
				data : {
					"moduleId" : node.id,
					"parentPath" :parentPath
				},
				url : "menu/queryItemsBean.page",
				cache : false,
				async : false,// ajax同步标志修改
				error : function() {
					window.parent.locaction="logout.jsp";				
				},
				success : function(data) {
					//将返回的数据放入js缓存
					insertTreeNode(node,data);
					clickpNode[node.id]=node.id;			
				}
			});
	}		
}

function insertTreeNode(pnode,data){
	var pid=pnode.id;
	var treeid=pnode.attributes.treeid;
	if(data){
		//item不为空
		if(data.listItems &&data.listItems.length>0){
			
			$.each(data.listItems, function(i) {
					var aid=this.id;
					var name = this.name;
					var dataLink = this.pathU;
					var onFun="addTab('"+name+"','"+dataLink+"')";
					$("#tree"+treeid).tree('append',{
						parent:(pnode.target),
						data:[{
							id:aid,
							text:"<span onclick=\""+onFun+"\">"+this.name+"</span>"							
						}]
					});
					
				});
		
		}
		//module不为空
		if(data.listModules && data.listModules.length > 0) {
				
				$.each(data.listModules, function(i) {
					var aid=this.id;
					var name = this.name;
					var dataLink = this.pathU;
					var onFun="addTab('"+name+"','"+dataLink+"')";
					$("#tree"+treeid).tree('append',{
						parent:(pnode.target),
						data:[{
							id:aid,
							text:this.name,							
							state:'closed',
							animate:false,
							attributes:{"hasSon":this.hasSon,"nodeType":this.type,"parentPath":data.parentPath,"treeid":treeid}
						}]
					});
					
				});
			}
	}
}

	$(document).ready(function() {
		
		<%
		ModuleQueue moduleQueue = (ModuleQueue) request
													.getAttribute("moduleQueue");
		if(moduleQueue!=null&&moduleQueue.size()>0)
		out.print("showitem('"+moduleQueue.getModule(0).getId()+"','"+moduleQueue.getModule(0).getName()+"');");											
		%>	
												
	}	
	);
	
</script>
	</head>

	<body class="easyui-layout">
		<div region="west" split="true" id="west_area" title="导航菜单"
			style="width: 150; padding1: 1px; overflow: hidden;">
			<%
		
		if(moduleQueue!=null&&moduleQueue.size()>0)
		{
			for(int i = 0; i < moduleQueue.size();i ++)
			{
				Module m = moduleQueue.getModule(i);
				
			%>
				
				<div class="easyui-accordion" style="display: none;" animate="false" fit="true" border="false" id="<%="menubar_" + m.getId() %>"></div>
			<%
				
			}
		}
			String hasrootitems = (String)request.getAttribute("hasrootitems");
			if(hasrootitems != null&&hasrootitems.equals("true")){
			%>
				
				<div class="easyui-accordion" style="display: none;" animate="false" fit="true" border="false" id="<%="menubar_rootitems"%>"></div>
			<%
			}
			
			Item publicitem = (Item)request.getAttribute("publicitem");
			String isanypage = publicitem.getWorkspacecontentExtendAttribute("isany");
			if(isanypage == null)
				isanypage = "jf.jsp";
			String jfpage = MenuHelper.getRealUrl(request.getContextPath(),isanypage);
		%>	
		</div>
		<div id="warpperDiv" region="center">
			<div id="tt" class="easyui-tabs" fit="true" border="false">
				<div title="工作台">
					<iframe scrolling="auto" frameborder="0" src="<%=jfpage %>"
						style="width: 100%; height: 100%;"></iframe>
				</div>
			</div>
		</div>
	</body>

</html>
