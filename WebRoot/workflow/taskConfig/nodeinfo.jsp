<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<div class="tabbox" id="tabbox">
	<ul class="tab" id="menu0">
		<li><a href="javascript:void(0)" class="current"
			onclick="setTab(0,0)"><span>待办配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(0,1)"><span>参数配置</span></a></li>
	</ul>
</div>

<div id="main0">
	
	
	<ul id="tab1" style="display: block;">
		<div>
			<pg:notempty actual="${activitiNodeNotExist}"><div align="left">${activitiNodeNotExist}</div></pg:notempty>
			
			<form name="submitForm" id="submitForm" method="post">
				
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<th>节点KEY</th>
						<th>节点名称</th>
						<th>待办人</th>
						<th>待办组</th>
						<!--<th>通知模板</th>-->
						<th>处理工时/小时</th>
						<!-- <th>提醒次数</th>-->
						<th>操作</th>
					</pg:header>
					<pg:list autosort="false" requestKey="activitiNodeCandidateList">
					<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
					<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
						<input type="hidden"
							id="<pg:cell colName='node_key'/>_users_id" name="candidate_users_id"
							value="<pg:cell colName='candidate_users_id'></pg:cell>" />
						<input type="hidden"
							id="<pg:cell colName='node_key'/>_groups_id" name="candidate_groups_id"
							value="<pg:cell colName='candidate_groups_id'></pg:cell>" />
						<input type="hidden" id="node_id" name="node_id" value="<pg:cell colName='id'/>"/>
						<input type="hidden" name="business_id" value="<pg:cell colName='business_id'/>"/>
						<input type="hidden" name="business_type" value="<pg:cell colName='business_type'/>"/>
						<tr>
							<td><pg:cell colName="node_key"></pg:cell></td>
							<td><pg:cell colName="node_name"></pg:cell></td>
							<td><input type="text"
								id="<pg:cell colName='node_key'/>_users_name" name="candidate_users_name"
								value="<pg:cell colName='candidate_users_name'></pg:cell>"
								 class="input1 w200" /><a
								href="javascript:openChooseUsers('<pg:cell colName="node_key"/>')">选择</a></td>
							<td><input type="text"
								id="<pg:cell colName='node_key'/>_groups_name" name="candidate_groups_name"
								value="<pg:cell colName='candidate_groups_name'></pg:cell>"
								 class="input1 w200" /><a
								href="javascript:openChooseGroups('<pg:cell colName="node_key"/>')">选择</a></td>
							<!-- 
							<td>
								<input type="hidden"
									id="<pg:cell colName='messagetempleid'/>_messagetempleid" name="messagetempleid"
									value="<pg:cell colName='messagetempleid'></pg:cell>" />
									
								<input type="hidden"
									id="<pg:cell colName='emailtempleid'/>_emailtempleid" name="emailtempleid"
									value="<pg:cell colName='emailtempleid'></pg:cell>" />
									
								<input type="text" id="<pg:cell colName='messagetempleid'/>_messagetitle" name="messagetitle" 
									value="<pg:cell colName="messagetempletitle"/>" class="input1 w150"/>
								<a href="javascript:openMessTemple('<pg:cell colName="messagetempleid"/>','0')">短信</a>
								
								<br/>
								<input type="text" id="<pg:cell colName='emailtempleid'/>_eamiltitle" name="eamiltitle" 
									value="<pg:cell colName="emailtempletitle"/>" class="input1 w150"/>
								<a href="javascript:openMessTemple('<pg:cell colName="emailtempleid"/>','1')">邮件</a>
							</td>
							 
							<td >
								<input type="text" value="<pg:cell colName="duration_node"/>" name="duration_node" 
								style="width: 40px;" onkeyup="chkPrice(this);" onblur="chkLast(this)" onpaste="javascript: return false;"/>
								<select id="simpleDate" name="simpleDate">
									<pg:notempty colName="simpleDate">
										<option value="D" <pg:equal colName="simpleDate" value="D">selected</pg:equal>>D(日)</option>
										<option value="H" <pg:equal colName="simpleDate" value="H">selected</pg:equal>>H(时)</option>
										<option value="m" <pg:equal colName="simpleDate" value="m">selected</pg:equal>>m(分)</option>
									</pg:notempty>
									<pg:empty colName="simpleDate">
										<option value="D" >D(日)</option>
										<option value="H" selected>H(时)</option>
										<option value="m" >m(分)</option>
									</pg:empty>
								</select>
							</td>
							-->
							<td >
								<input type="text" value="<pg:cell colName="duration_node"/>" name="duration_node" style="width: 50px;" onkeyup="chkPrice(this);" onblur="chkLast(this)" onpaste="javascript: return false;"/>
							</td>
							<!-- 
							<td >
								<input type="text" name="noticenum" style="width: 50px;" value="<pg:notequal colName="noticenum" value="0"><pg:cell colName="noticenum"/></pg:notequal>" onpaste="javascript: return false;"
									onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" 
									onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}">
							</td>
							 -->
							<td>
								可修改
								<select name="is_edit_candidate">
									<pg:equal colName="is_edit_candidate" value="0">
										<option value="0" selected>是</option>
										<option value="1">否</option>
									</pg:equal>
									<pg:equal colName="is_edit_candidate" value="1">
										<option value="0">是</option>
										<option value="1" selected>否</option>
									</pg:equal>
								</select>&nbsp;&nbsp; &nbsp;       |&nbsp;&nbsp;&nbsp;      
								有效
								<select name="is_valid">
									<pg:equal colName="is_valid" value="0">
										<option value="0" selected>是</option>
										<option value="1">否</option>
									</pg:equal>
									<pg:equal colName="is_valid" value="1">
										<option value="0">是</option>
										<option value="1" selected>否</option>
									</pg:equal>
								</select>
							</td>
						</tr>
						</pg:notin>
					</pg:list>
				</table>
				
				<div class="btnarea">
					<a href="javascript:void(0)" class="bt_1" id="addButton"
						onclick="doCandidateSubmit()"><span>确定</span></a> 
				</div>
			</form>
			
		</div>
	</ul>

	<ul id="tab2">
		<div id="paramconig">
			<div class="title_box">
				<div class="rightbtn">
					<a href="javascript:loadVariableResource()" class="bt_small" id="addButton"><span>加载配置资源</span></a>
					<a href="javascript:loadClearVariableResource()" class="bt_small" id="addButton"><span>加载配置资源清除原所有数据</span></a>
					<a href="javascript:addTr()" class="bt_small" id="addButton"><span>新增</span></a>
				</div>
				<strong>节点参数配置</strong>
			</div>
			<div id="nodevariableContainer">
				<form name="submitParamForm" id="submitParamForm" action="test2.page" method="post">
				<input type="hidden" id="business_id" name="business_id" value="${business_id }"/>
				<input type="hidden" id="business_type" name="business_type" value="${business_type }"/>
				<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
				<div id="nodevariableContent">
				</div>
				<div class="btnarea">
					<a href="javascript:void(0)" class="bt_1" id="addButton"
						onclick="doParamSubmit()"><span>确定</span></a> 
				</div>
				</form>
			</div>
		</div>
	</ul>
</div>
<div id="sp" style="display:none">
		<div style="color:#99BBE8;background:#fafafa;padding:5px;">选择节点</div>
		<pg:list autosort="false" requestKey="nodeInfoList">
			<input type="radio"  name="lang" value="<pg:cell colName='id'/>"/><span><pg:cell colName="node_name"></pg:cell></span><br/>
		</pg:list>
</div>
<script type="text/javascript">
function setUser(node_key,users_id,users_name){
		$("#"+node_key+"_users_id").val(users_id);
		$("#"+node_key+"_users_name").val(users_name);
}

function setGroup(node_key,groups_id,groups_name){
		$("#"+node_key+"_groups_id").val(groups_id);
		$("#"+node_key+"_groups_name").val(groups_name);
}

var api = frameElement.api;
function openChooseUsers(node_key){
	//alert(node_key);
	var url = "<%=request.getContextPath()%>/workflow/config/toChooseUserPage.page?users="+$("#"+node_key+"_users_id").val()+"&node_key="+node_key+"&user_realnames="+$("#"+node_key+"_users_name").val();
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:650, content:'url:'+url}); 
	
}

function openMessTemple(templeId,templeType) {
	var title="";
	if (templeType == '0') {
		title = "短信模板";
	}else{
		title = "邮件模板";
	}
	var url = "<%=request.getContextPath()%>/workflow/taskConfig/chooseMessageTemple.jsp?templeType="+templeType+"&templeId="+templeId;
	$.dialog({ id:'nodeInfoIframe', title:title,width:900,height:500, content:'url:'+url});
}

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

			url: "updateActivitiNodeCandidate.page",
			
			type: "post",
			
			data: formToJson("#submitForm"),			
			dataType:"json",			
			success: function(data){
			    alert(data);
			  }
			
			});
	}
	
	function doParamSubmit(){
		$.ajax({

			url: "submitNodeVariable.page",
			
			type: "post",
			
			data: formToJson("#submitParamForm"),			
			dataType:"json",			
			success: function(data){
			    alert(data);
			  }
			
			});
	}
	
	
	function loadNodevariableData(){
		$("#nodevariableContent").load("queryNodevariable.page?business_id="+$("#business_id").val()+"&business_type="+$("#business_type").val()+"&processKey="+$("#process_key").val());
	}
	
	function loadVariableResource(){
		$("#nodevariableContent").load("loadVariableResource.page?business_id="+$("#business_id").val()
		+"&business_type="+$("#business_type").val()+"&processKey="+$("#process_key").val());
	}
	
	function loadClearVariableResource(){
		$("#nodevariableContent").load("loadVariableResource.page?isClear=true&business_id="+$("#business_id").val()
		+"&business_type="+$("#business_type").val()+"&processKey="+$("#process_key").val());
	}
	
	function addTr(){
		var businessId = $("#business_id").val();
		var businessType = $("#business_type").val();
		var trHtml = "<tr><td><select name='node_id' id='node_id'>";
		<pg:list  requestKey="nodeInfoList">
			trHtml+="<option value='<pg:cell colName="id"/>'><pg:cell colName="node_name"/></option>";
		</pg:list>
		trHtml+="</select></td>";
		trHtml+="<td><input type='text' class='input1 w20' name='param_name' class='checkClass'/></td>";
		trHtml+="<td><input type='text' class='input1 w20' name='param_value'/></td>";
		trHtml+="<td><input type='text' class='input1 w200' name='param_des'/></td>";
		trHtml+="<td><select name='is_edit_param'><option value='0' selected>是</option><option value='1'>否</option></select></td>";
		trHtml+="<td><a href='javascript:void(0);' class='bt'>删除</a></td></tr>";
		//trHtml+="<input type='hidden' name='business_id' value='"+businessId+"'/>";
		//trHtml+="<input type='hidden' name='business_type' value='"+businessType+"'/>";
		$("#tb1").append(trHtml);
		$(".bt").click(function(){
			$(this).parent('td').parent('tr').remove();
		});
	}
	
	$(document).ready(function() {
		loadNodevariableData();
		
		if($("#taskId").val()){
			$("#tabbox").attr("style","display:none");
		};
		
	   });
	
	function chkPrice(obj){
		obj.value = obj.value.replace(/[^\d.]/g,""); 
		//必须保证第一位为数字而不是. 
		obj.value = obj.value.replace(/^\./g,""); 
		//保证只有出现一个.而没有多个. 
		obj.value = obj.value.replace(/\.{2,}/g,"."); 
		//小数点后面保留一位小数
		obj.value = obj.value.replace(/\.\d\d/g,"."); 
		//保证.只出现一次，而不能出现两次以上 
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
	} 
	
	function chkLast(obj){ 
		// 如果出现非法字符就截取掉 
		if(obj.value.substr((obj.value.length - 1), 1) == '.') {
			obj.value = obj.value.substr(0,(obj.value.length - 1)); 
		}
	}
</script>
</html>
