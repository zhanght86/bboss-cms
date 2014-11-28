
/**
 * 工作流js
 * 
 * @author          fudk
 * @company         SANY Heavy Industry Co, Ltd
 * @creation date   2013-10-24
 * @version         1.0
 */

$(document).ready(function(){
	
	//如果是驳回按钮，显示驳回至节点
	$("input[name=operateType]").click( function () { 
		var op = $(this).val();
		if(op=='reject'){
			$("input[name=toActName]").val($("select[id=rejectToActId] option:selected").text());
			$("#rejectto").show();
		}else{
			$("#rejectto").hide(); 
		}
		if(op=='turnto' || op == 'turnTo'){
			$("#delegateTr").show();
		}else{
			$("#delegateTr").hide(); 
		}
	}); 
	
	//选择拨回到某个节点，将节点Key参数赋值
	$("#rejectToActId").change( function() {
		$("input[name=toActName]").val($("select[id=rejectToActId] option:selected").text());
	}); 

	InitWorkflowPage();
	
});	

// 显示节点状态
function showNodeStatus(){	
	
	//获取已处理任务数
	var hitaskSzie = $("#hiTaskSzie").val();
	
	for (var i = 1;i <= hitaskSzie; i++) {
		var hiActId = $("#hiActId"+i).val();
		$("#zx"+hiActId).text('已执行');
	}
	
	//设置当前节点状态--执行中
	$("#zx"+taskKey).text('执行中');
	//根据当前任务节点key的Tr
	var obj = $("#protr"+taskKey.substring(8,taskKey.length));
	obj.css("color","red");
}

//加载页面时 让需要选择的节点红色星号显示,objs为必选节点
function initliteRequiredStar(objs){
	nodeStarNum = objs;// 存储页面节点序号
	
	$(".required").hide();
	
	var vs = objs.split(',');
	for(var j=0;j<vs.length;j++){
		var req = vs[j];
		$("#protr"+req).find(".required").show();
	}
}

//判断空节点
function isNodeNull(){
	
	var listSize = $("#actListSize").val();
	for (var i = 1;i <= listSize; i++) {
		if ($("#candidateName"+i).val() == '') {
			$("#realnames"+i).html("无审批人");// 前台显示的值
		}
	}
}


// 初始化工作流审批页面
function InitWorkflowPage(){
	
	// 申请页面，暂存后再申请页面，第一节点处理人为当前登录处理人
	if(pagestate == '1' || pagestate == '2') {
		$("#realnames1").html(userName);// 前台显示的值
		$("#candidateName1").val(userAccount);//保存到后台需要的值
		$("#realName1").val(userName);//保存到后台需要的值
		$("tr[id='protr1'] td:last").html('当前处理节点');
		$("tr[id='protr1']").css("color","red");
	}
	
	//判断空节点，赋值未‘无审批人’
	isNodeNull();
	
	//如果允许修改，显示审批人修改按钮
	showChooseUsers();

	//列表显示执行中节点
	showNodeStatus();
	
}

//隐藏选择按钮
function showChooseUsers(){
	//获取当前任务节点key
	var nowTaskId = "";
	
	// 流程开启时默认第一个任务key
	if (pagestate == 1 || pagestate == 2) {
		nowTaskId = "usertask1";
	}else {
		nowTaskId = taskKey;
	}
	
	//根据当前任务节点key获取节点顺位数
	var objs = $("#protable").find("input[value="+nowTaskId+"]");
	var currentTaskNum = parseInt(objs.attr("con"));
	//获取所有节点个数
	var listSize = $("#actListSize").val();
	
	// 当前节点能修改后续节点权限
	if($("#isEditAfter"+currentTaskNum).val() == 1){
		
		for (var i = currentTaskNum+1;i <= listSize; i++) {
			// 后续节点是否能被修改
			if ($("#isEdit"+i).val() == 1) {
				$("#protr"+i).find("a").show();
			}
		}
	}
}

//选择处理人
function setCandidate(node_key){
	//处理人id/域账号/工号
	var candidateName = $("#candidateName"+node_key).val();
	//处理人中文名
	var candidateCNName = $("#candidateCNName"+node_key).val();
	//处理人名称+部门名称
	var realName = $("#realName"+node_key).val();
	//处理部门id
	var candidateOrgId = $("#candidateOrgId"+node_key).val();
	//处理部门名称
	var candidateOrgName = $("#candidateOrgName"+node_key).val();
	
	var url = encodeURI(ctx+"/workflowBusiness/choose/toChooseUserPage.page?processKey="+$('#processKey').val()
			+"&candidateName="+candidateName
			+"&candidateCNName="+candidateCNName
			+"&candidateOrgId="+candidateOrgId
			+"&candidateOrgName="+candidateOrgName
			+"&realName="+realName
			+"&nodekey="+node_key
			+"&callBackFunc=updateAfterChoose");
	$.dialog({ id:'nodeInfoIframe', title:'选择处理人',width:1000,height:650, content:'url:'+url}); 
	
}

//选择转办人
function delegateUsers(node_key){
	
	var url = ctx+"/workflowBusiness/choose/toChooseUserPage.page?processKey="+$('#processKey').val()
	+"&callBackFunc=updateAfterChoose&index=1";
	
	$.dialog({ id:'nodeInfoIframe', title:'选择转办人',width:1000,height:650, content:'url:'+url}); 
}

/** 选择处理人后的业务逻辑方法
 * @param accouts 处理人id
 * @param realnames 处理人名称
 * @param allnames 处理人名称+部门名称
 * @param orgids 部门id
 * @param orgnames 部门名称
 * @param node_key 节点数，如‘1,2,3’
 */
function updateAfterChoose(accouts,realnames,allnames,orgids,orgnames,node_key){
	
	if(!node_key){
		// 转办判断
		if(accouts.indexOf(",") < 0){
			if (accouts == userAccount) {
				$.dialog.alert('不能将任务转办给自己！',function(){});
			}else {
				$("#delegateUser").val(accouts);
				$("#delegateUserName").val(realnames);
			}
		}else{
			$.dialog.alert('只能选择1个转办人！',function(){});
		}
	}else{
		
		$("#candidateName"+node_key).val(accouts);
		$("#candidateCNName"+node_key).val(realnames);
		$("#realName"+node_key).val(allnames);
		$("#candidateOrgId"+node_key).val(orgids);
		$("#candidateOrgName"+node_key).val(orgnames);
		
		$("#realnames"+node_key).html(allnames);
		
	}
	
	isNodeNull();
}

/**
 * 修改节点审批人
 * userlist         用户数组 userlist = [{userCode:'00001',userName:'用户1'},{userCode:'00002',userName:'用户2'}]
 * nodeids        替换节点列表 nodeids =[2,3,4]
 */
function changeNodeUsers(userlist,nodeIds){
	if (userlist.length > 0 && nodeIds.length > 0) {
		
		for (var i = 0;i< nodeIds.length;i++){
			
			var nodeId = nodeIds[i];
			
			for (var j = 0; j< userlist.length;j++) {
				var user = userlist[j];
				
				if (j == 0) {
					$("#candidateName"+nodeId).val('');
					$("#candidateName"+nodeId).val(user.userCode);
					$("#realName"+nodeId).val(user.userName);
					$("#realnames"+nodeId).html(user.userName);
				}else {
					var selectedUser = $("#candidateName"+nodeId).val();
					$("#candidateName"+nodeId).val(selectedUser +','+ user.userCode);
					$("#realName"+nodeId).val($("#realName"+nodeId).val()+ "," + user.userName);
					$("#realnames"+nodeId).html($("#realName"+nodeId).val());
				}
			}
		}
	}
}

//检查页面元素
function checkoutPageElement(){
	// 判断处理功能区域是否为空
	if($("input[name='operateType']").length!=0){
		var check = $("input[name='operateType']:checked").val();
		if(!check){
			$.dialog.alert("请选择处理结果！",function(){});
			return false;
		}

		if(check == 'turnto' || check == 'turnTo'){
			var duser = $("#delegateUser").val();
			if(!duser||duser==''){
				$.dialog.alert("请选择转办人！",function(){});
				return false;
			}
		}
	}
	
	// 判断审批节点区域中处理人是否为空
	var vs = nodeStarNum.split(',');
	if($.trim(nodeStarNum) != ''){
		for(var j=0;j<vs.length;j++){
			var sid = vs[j];
			var spanname = $.trim($("#protr"+sid +" #realName"+sid).val());
			if(sid!='0'&&( spanname == '' || spanname == '无审批人')){
				var actName = $("#protr"+sid +" #actName"+sid).val();
				$.dialog.alert("节点["+actName+"]处理人不能为空！",function(){});
				return false;
			}
		}
	}
	
	return true;
}

// 查看抄送节点明细
function viewCopyTaskInfo(actid) {
	
	var url = ctx+"/workflowBusiness/business/getUserReaderCopyTasks.page?actinstid="+actid;
	
	$.dialog({ id:'nodeInfoIframe', title:'已阅明细',width:500,height:400, content:'url:'+url}); 

}
