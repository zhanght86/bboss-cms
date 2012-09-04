<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程待办配置</title>
<%@ include file="/common/jsp/css.jsp"%>

<style type="text/css">
.a_bg_color {
	color: white;
	cursor: hand;
	background-color: #191970
}
</style>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
$(document).ready(function() {
	var taskId ;
	taskId = $("#taskId").val();
	if(taskId!=''){
		$(".chooseSpan").html("");
	}else{
		$(".paramValueInput").attr("style","display:block");
		$(".paramValueSpan").html("");
		
	}
  });
  
var api = frameElement.api;
function openChooseUsers(node_key){
	//alert(node_key);
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?users="+$("#"+node_key+"_users_id").val()+"&node_key="+node_key+"&user_realnames="+$("#"+node_key+"_users_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
	
}

function openChooseGroups(node_key){
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseGroupPage.page?groups="
				+ $("#" + node_key + "_groups_id").val()
				+ "&node_key="
				+ node_key
				+ "&group_realnames="
				+ $("#" + node_key + "_groups_name").val();
		$.dialog({
			id : 'nodeInfoIframe',
			title : '选择用户组',
			width : 700,
			height : 400,
			content : 'url:' + url
		});
	}

function doCandidataSubmit(){
	$.ajax({
		url: "${submitCandidataMethod}",
		type: "post",
		
		data: formToJson("#submitCandidataForm"),			
		datatype:"json",			
		success: function(data){
		    alert(data);
		  }
		
		});
}
function doParamSubmit(){
	$.ajax({
		url: "${sumitParamMethod}",
		type: "post",
		
		data: formToJson("#submitParamForm"),			
		datatype:"json",			
		success: function(data){
		    alert(data);
		  }
		
		});
}
</script>
</head>
<body class="easyui-layout">
	<input type="hidden" value="${processKey }" id="processKey"
		name="processKey" />
	<input type="hidden" value="${orgId }" id="orgId" name="orgId" />
	<input type="hidden" value="${taskId }" id="taskId" name="taskId" />
	<input type="hidden" value="${submitMethod }" id="submitMethod"
		name="submitMethod" />
	<div region="center"
		title="${processDef.DEPLOYMENT_NAME_ }>><span id='orgNameSpan'></span>节点配置"
		style="overflow: hidden;">

		<div class="easyui-layout" fit="true" style="background: #ccc;">
			<div region="center" style="height: 465px;">
				<pg:beaninfo requestKey="processDef">
					<%@ include file="viewProcessPic.jsp"%>
				</pg:beaninfo>
			</div>
			<div region="south" split="true" style="height: 400px;">
				<div class="tabbox" id="tabbox">
					<ul class="tab" id="menu0">
						<li><a href="javascript:void(0)" class="current"
							onclick="setTab(0,0)"><span>待办配置</span></a></li>
						<li><a href="javascript:void(0)" onclick="setTab(0,1)"><span>参数配置</span></a></li>
					</ul>
				</div>
				<div id="main0">
					<ul id="tab1" style="display: block;">
						<div id="nodeconfig">
								<div id="nodeinfolistContainer">
									
									<form name="submitCandidataForm" id="submitCandidataForm" method="post">
										<table width="100%" border="0" cellpadding="0" cellspacing="0"
											class="stable" id="tb">
											<pg:header>
												<th>节点KEY</th>
												<th>节点名称</th>
												<th>待办人</th>
												<th>待办组</th>
											</pg:header>
											<pg:list autosort="false"
												requestKey="activitiNodeCandidateList">
												<input type="hidden"
													id="<pg:cell colName='node_key'/>_users_id"
													name="candidate_users_id"
													value="<pg:cell colName='candidate_users_id'></pg:cell>" />
												<input type="hidden"
													id="<pg:cell colName='node_key'/>_groups_id"
													name="candidate_groups_id"
													value="<pg:cell colName='candidate_groups_id'></pg:cell>" />
												<input type="hidden" id="node_key" name="node_key"
													value="<pg:cell colName='node_key'/>" />
												<input type="hidden" name="business_id"
													value="<pg:cell colName='business_id'/>" />
												<input type="hidden" name="business_type"
													value="<pg:cell colName='business_type'/>" />
												<tr>
													<td><pg:cell colName="node_key"></pg:cell></td>
													<td><pg:cell colName="node_name"></pg:cell></td>
													<td><input type="text"
														id="<pg:cell colName='node_key'/>_users_name"
														name="candidate_users_name"
														value="<pg:cell colName='candidate_users_name'></pg:cell>"
														class="input1 w200" /> 
														<span class="chooseSpan">
															<pg:equal colName="is_edit_candidate" value="0">
																<a href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')">选择</a></td>
															</pg:equal>
															<pg:equal colName="is_edit_candidate" value="1">
															</pg:equal>
														</span>
													<td><input type="text"
														id="<pg:cell colName='node_key'/>_groups_name"
														name="candidate_groups_name"
														value="<pg:cell colName='candidate_groups_name'></pg:cell>"
														class="input1 w200" /> 
														<span class="chooseSpan">
															<pg:equal colName="is_edit_candidate" value="0">
																<a href="javascript:openChooseGroups('<pg:cell colName="node_key"/>')">选择</a></td>
															</pg:equal>
															<pg:equal colName="is_edit_candidate" value="1">
															</pg:equal>
														</span>
												</tr>
											</pg:list>
										</table>
										<pg:null actual="${taskId }" >
											<table id="table3">
												<tr>
													<td align="right"><a href="javascript:void(0)"
														class="bt_1" id="addButton" onclick="doCandidataSubmit()"><span>确定</span></a></td>
												</tr>
											</table>
										</pg:null>
										</form>
								</div>
						</div>
					</ul>
					<ul id="tab2">
						<div id="paramconig">
							<form name="submitParamForm" id="submitParamForm" method="post">
							<div class="title_box">
								<strong>节点参数配置</strong>
							</div>
							<div id="nodevariableContainer">
								<input type="hidden" id="business_id" name="business_id"
									value="${business_id }" /> <input type="hidden"
									id="business_type" name="business_type"
									value="${business_type }" /> <input type="hidden"
									id="process_key" name="process_key" value="${process_key }" />
								<div id="nodevariableContent">
									<table width="100%" border="0" cellpadding="0" cellspacing="0"
										class="stable" id="tb">
										<pg:header>
											<th>所属节点KEY</th>
											<th>参数名称</th>
											<th>参数值</th>
											<th>参数描述</th>
										</pg:header>
										<pg:list autosort="false" requestKey="nodevariableList">
											<tr>
												<td width="150"><input type="hidden" value="<pg:cell colName='node_key'/>" name="node_key"/><pg:cell colName="node_key"></pg:cell></td>
												<td width="300"><input type="hidden" value="<pg:cell colName='param_name'/>" name="param_name"/><pg:cell colName="param_name"></pg:cell></td>
												<td width="300">
												<pg:equal colName="is_edit_param" value="1">
													<input type="text" value="<pg:cell colName='param_value'/>" name="param_value" class="paramValueInput" style="display:none" readonly/>
												</pg:equal>
												<pg:equal colName="is_edit_param" value="0">
													<input type="text" value="<pg:cell colName='param_value'/>" name="param_value" class="paramValueInput" style="display:none"/>
												</pg:equal>
												<span class="paramValueSpan"><pg:cell colName="param_value"></pg:cell></span></td>
												<td><pg:cell colName="param_des"></pg:cell></td>
											</tr>
										</pg:list>
									</table>
								</div>
							</div>
							<pg:null actual="${taskId }" >
								<table id="table3">
									<tr>
										<td align="right"><a href="javascript:void(0)"
											class="bt_1" id="addButton" onclick="doParamSubmit()"><span>确定</span></a></td>
									</tr>
								</table>
					</pg:null>
					</form>
						</div>
					</ul>
					
				</div>
				
			</div>
		</div>
	</div>

</body>
</html>