<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
	描述：开启流程实例
	作者：gw_tanx
	版本：1.0
	日期：2012-05-13
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>开启流程实例</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<style type="text/css">
.a_bg_color{
	color: white;
    cursor:hand;
	background-color: #191970
}
</style>
</head>

<body class="easyui-layout">
<form name="submitForm" id="submitForm" method="post">
	
	<div region="west" split="true" title="模板类型" style="width: 220px; padding1: 1px; overflow: hidden;">
		<div class="easyui-accordion" fit="true" border="false">
		
			<div id="org_tree" title="组织结构" selected="true" style="overflow: auto;"></div>
			<div id="bussinesstree" title="业务类型" style="padding: 10px; overflow: auto;"></div>
			<div title="通用配置" style="padding: 10px; overflow: auto;">
				<a href="javascript:query('','0','通用')" name="0">通用配置</a>
			</div>
		</div>
	</div>
	
	<div region="center" style="overflow: hidden;" >
		<div class="easyui-layout" fit="true" style="background: #ccc;">
			<div region="center" style="height: 80px;" title="配置信息" split="false">
				<div>
					<table border="0" cellpadding="0" cellspacing="0" class="table2" >
						<tr>
							<th width="60">业务主题:</th>
							<td width="400"><input type="text" id="businessKey" name="businessKey" value=""/></td>
						</tr>
					</table>
				</div>
				
				<div id="nodeInfoConfig">
				
				</div>

			</div>
			
			<div region="south" split="false" style="height:60px;">
				<div class="btnarea">
					<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="doCandidateSubmit()"><span>确定</span></a> 
				</div>
			</div>
		</div>
	</div>
</form>
	
</body>

<script language="javascript">
var api = frameElement.api, W = api.opener;
// 选择用户
function openChooseUsers(node_key){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?users="+$("#"+node_key+"_users_id").val()+"&node_key="+node_key+"&user_realnames="+$("#"+node_key+"_users_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
}
// 选择组
function openChooseGroups(node_key){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseGroupPage.page?groups="
		+ $("#" + node_key + "_groups_id").val()
		+ "&node_key="
		+ node_key
		+ "&group_realnames="
		+ $("#" + node_key + "_groups_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户组',width:700,height:400, content:'url:'+url}); 
}

function doCandidateSubmit(){
	$.ajax({

		url: "<%=request.getContextPath()%>/workflow/repository/startPorcessInstance.page",
		
		type: "post",
		
		data: formToJson("#submitForm"),			
		dataType:"json",			
		success: function(data){
			if (data != 'success') {
				alert("流程实例开启出错："+data);
			}else {
				W.queryList();
				api.close();	
			}
		  }
	});
}

//添加模板
function query(businessId,businessType,treename){
	
	$("a").removeClass("a_bg_color");
	$("a[name='"+businessId+"']").addClass("a_bg_color"); 
	
	var url = "<%=request.getContextPath()%>/workflow/repository/getConfigTempleInfo.page?"
		+"processKey=${process_key}&business_type="+businessType+"&business_id="+businessId;
	$("#nodeInfoConfig").load(url,function(){loadjs()});
	
}

//清空选择
function emptyChoose(id,type){
	if (type=='1') {//清空用户
		$("#"+id+"_users_id").val('');
		$("#"+id+"_users_name").val('');
	}else {//清空组
		$("#"+id+"_groups_id").val('');
		$("#"+id+"_groups_name").val('');
	}
}

//新增参数配置行
function addTr(){
	var trHtml = "<tr class='replaceTr'><td><select name='node_id' id='node_id'>";
	<pg:list requestKey="nodeInfoList">
		trHtml+="<option value='<pg:cell colName="id"/>'><pg:cell colName="node_name"/></option>";
	</pg:list>
	trHtml+="</select></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='param_name' class='checkClass'/></td>";
	trHtml+="<td><input type='text' class='input1 w20' name='param_value'/></td>";
	trHtml+="<td><input type='text' class='input1 w200' name='param_des'/></td>";
	trHtml+="<td><a href='javascript:void(0);' class='bt'>删除</a></td></tr>";
	$("#tb3").append(trHtml);
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
}

$(document).ready(function() {
	
	$("#org_tree").load("../taskConfig/task_config_org_tree.jsp");
	$("#bussinesstree").load("../businesstype/businessTypeTree.jsp");
	
	$(".bt").click(function(){
		$(this).parent('td').parent('tr').remove();
	});
	
	query('',0,'');
	
});
</script>
</html>