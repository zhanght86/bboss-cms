
/**
 * 工作流js
 * 
 * @author          fudk
 * @company         SANY Heavy Industry Co, Ltd
 * @creation date   2013-10-24
 * @version         1.0
 */

$(document).ready(function(){
	
	workflowaddInit();

});		
//工作流页面默认参数配置
function workflowaddInit(){
	
	var pagestate = $("#pagestate").val();
	
	//流程页面状态
	if (pagestate == '1'){//新增初始状态
		//第一个节点状态设置
		$("tr[id='protr1'] td:last").html('当前处理节点');
		
	}else if (pagestate == '2') {//暂存或驳回提交人节点，提交人查看状态
		
	}else if (pagestate == '3') {//审批中提交人查看状态
		
	}else if (pagestate == '4') {//审批中审批人查看状态
		
	}else if (pagestate == '5') {//审批中或暂存中其他人查看状态
		
	}else if (pagestate == '6') {//流程结束后查看状态
		
	}
}
//节点状态标记设置
function selectNode(actId,txt){	
	$("#zx"+actId).text(txt);
	if(txt=='执行中'){
		obj = $("#protr"+actId.substring(8,actId.length));
		obj.css("color","red");
	}
}
//选择处理人
function setCandidate(node_key){
	setTaskCandidateUsers(node_key);
}
//显示当前处理人
function queryAssignees(){
	if($("#assignees").attr("title") != $("form").data("assignees")){
		try{//查询人员名称列表并显示当前处理人，各系统自行实现
			showAssignees($("form").data("assignees"));
		}catch(error){}
	}
}
//显示撤回
function showRecall(){
	var canRecall = $("#canRecall"+$("form").data("coordinateObj").id).val();
	if(canRecall == '10' ){
		$("#recalltr").show();
	}else{
		$("#remark").parent().parent().hide();
	}
}
//流程暂存状态查看
function showTemp(){
	var user = $("#candidateName1").val();
	if(!user){
		workflowaddInit();
	}
}


//隐藏选择按钮
function showChooseUsers(){
	var pagestate =$("form").data("pagestate");
	var nowTaskId = $("form").data("coordinateObj").id;
	if(pagestate == '1'||pagestate == '2'){
		$("a[name=chooseUsersa]").show();
	}else{

		var objs = $("#protable").find("input[value="+nowTaskId+"]");
		if(objs.attr("nvl") == '10'){
			var con = parseInt(objs.attr("con"))-1;
			$("tr[id^=protr]:gt("+con+")").find("a").show();
		}
	}
}

//加载页面时 让需要选择的节点红色星号显示,objs为必选节点
function initliteRequiredStar(objs){
	var vs = objs.split(',');
	$("#tempnum").val(objs);
	var trs = $("#protable tr");
	for(var j=0;j<vs.length;j++){
		var req = vs[j];
		$(trs[req]).find(".requiredstar").show();
	}

}
//显示当前处理人
function workAssignees(userList){
	var userNames = '';
	if(userList){
		$.each(userList, function(i) { 
			userNames += ","+this.userName;
		});
		if(userNames.length>1){
			userNames = userNames.substring(1, userNames.length);
		}

	}
	$("#assignees").html(userNames);

}

/**
 * 修改节点审批人
 * userlist         用户数组 userlist = [{userCode:'00001',userName:'用户1'},{userCode:'00002',userName:'用户2'}]
 * nodeids        替换节点列表 nodeids =[2,3,4]
 */
function changeNodeUsers(userlist,nodeIds){
	if (userlist.length > 0 && nodeIds.length > 0) {
		var user ;
		$.each(userlist, function(i) {
			user = this;
			$.each(nodeIds, function(j) {
				changeNodeUser(user,this);
			})
		})
	}
}

/**
 * 修改节点审批人
 * user         用户数组 user = {userCode:'00001',userName:'用户1'}
 * nodeId        替换节点 
 */
function changeNodeUser(user,nodeId){
	var selectedUser = $("#candidateName"+nodeId).val();
	if(selectedUser&&selectedUser!=''){
		if(selectedUser.indexOf(user.userCode) <0){
			$("#candidateName"+nodeId).val(selectedUser +','+ user.userCode);
			$("#realName"+nodeId).val($("#realName"+nodeId).val()+ "," + user.userName);
			$("#realnames"+nodeId).html($("#realName"+nodeId).val()+ "," + user.userName);
		}
	}else{
		$("#candidateName"+nodeId).val(user.userCode);
		$("#realName"+nodeId).val(user.userName);
		$("#realnames"+nodeId).html(user.userName);
	}
}
/**
 * 设置节点审批人
 * user         用户数组 user = {userCode:'00001',userName:'用户1'}
 * nodeId        替换节点 
 */
function setNodeUser(user,nodeId){
	$("#candidateName"+nodeId).val(user.userCode);
	$("#realName"+nodeId).val(user.userName);
	$("#realnames"+nodeId).html(user.userName);
}

/**
 * 根据申请人，获取流程默认审批人
 * userId         
 * processKey        流程key 
 */
function changeUserCandidate(userId,processKey){
	alert(userId)
	var url = ctx + '/workflow/queryActUser.page';
	$.ajax({   
		type : "post",   
		dataType : "json",   
		data : { userId : userId, processKey:processKey},   
		url : url,   
		cache : false,   
		async : false,// ajax同步标志修改   
		error : function() {   
//			window.parent.locaction="logout.jsp";                  
		},   
		success : function(data) {   
			$.each(data, function(i) {
				var tid = this.actId.replace("usertask","");
				if(tid>1){
					$("#candidateName"+tid).val(this.candidateName);
					$("#realName"+tid).val(this.realName);
					$("#realnames"+tid).html(this.realName);
				}
			});

		}
	});
}

/**
 * 根据申请人，获取流程默认审批人
 * typeName         pass 通过，reject驳回，turnTo转办，toEnd废弃，recall撤回
 */
function hideOperateType(typeName){
	$("input[name=operateType][value="+typeName+"]").hide();
	$("#"+typeName+"Span").hide();
}

//页面提交
function submitForm(formid){
	if(formid&&formid!=''){
		formid = "#"+formid;
	}else{
		formid = "form";
	}
	// 如果页面需要验证，则在页面添加 validateData 函数提示自己的验证信息 验证成功返回 true 失败返回 false
	if(typeof validateData == "function"){
		if(!validateData()){
			return;
		}
	}
	if($(formid).attr("action") == ''){
		alert("表单跳转页面（action）不能为空！");
	}

	if($("input[name='operateType']").length!=0){
		var check = $("input[name='operateType']:checked").val();
		if(!check){
			alert("请选择处理结果！");
			return ;
		}

		if(check == 'turnTo'){
			var duser = $("#delegateUser").val();
			if(!duser||duser==''){
				alert("请选择转办人！");
				return ;
			}

		}

	}
	var trs = $("#protable tr");
	var tempnum = $("#tempnum").val();
	var vs = tempnum.split(',');
	if($.trim(tempnum) != ''){
		for(var j=0;j<vs.length;j++){
			var sid = vs[j];
//			var spanname = $.trim($(trs[sid]).find("span[id^=realnames]").text());
			var spanname = $.trim($(trs[sid]).find("input[name^=realName]").val());
			if(sid!='0'&&( spanname == '' || spanname == '无审批人')){
				var rid = parseInt(sid) ;
				var actName = $("tr[id='protr"+ rid +"'] td:eq(1)").text();
				alert("节点["+actName+"]不能为空！");
				return ;
			}
		}
	}

	$(formid).form('submit', {
		onSubmit:function(){
			blockUI()
			if(!$("form").attr("subimt")){//防止重复提交
				$("form").attr("subimt","true");
			}else{
				return false;
			}
		},
		success:function(result){
			unblockUI();
			if(typeof  submitReturn == "function"){
				submitReturn(result);
			}
		}
	});	
}