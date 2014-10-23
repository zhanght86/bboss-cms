
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
		if($(this).val()=='reject'){
			$("input[name=toActName]").val($("select[id=rejectToActId] option:selected").text());
			$("#rejectto").show();
		}else{
			$("#rejectto").hide(); 
		}
		if($(this).val()=='turnTo'){
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
	
	var vs = objs.split(',');
	var trs = $("#protable tr");
	for(var j=0;j<vs.length;j++){
		var req = vs[j];
		$(trs[req]).find(".required").show();
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
function setCandidate(node_key) {
	var candidateName = $("#candidateName"+node_key).val();
	if(!candidateName) candidateName='';
	var url = ctx+"/workflowBusiness/chooseOrgUser.jsp?node_key="+node_key+"&candidateName="+candidateName+"&callBackFunc=updateAfterChoose";
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:480, content:'url:'+url}); 
}

//选择转办人
function delegateUsers(node_key){		
	var candidateName = $("#delegateUser").val();
	if(!candidateName) candidateName='';
	var url = ctx +"/workflowBusiness/chooseOrgUser.jsp?node_key=&candidateName="+candidateName+"&callBackFunc=updateAfterChoose"+"&index=1";
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:1000,height:480, content:'url:'+url}); 
}

// 选择处理人后的业务逻辑方法
function updateAfterChoose(accouts,realnames,node_key){
	
	if(!node_key){
		if(accouts.indexOf(",") < 0){
			if (accouts == userAccount) {
				alert('不能将任务转办给自己！');
			}else {
				$("#delegateUser").val(accouts);
				$("#delegateUserName").val(realnames);
			}
		}else{
			alert('只能选择1个转办人！')
		}
	}else{
		$("#candidateName"+node_key).val(accouts);
		$("#realName"+node_key).val(realnames);
		$("#realnames"+node_key).html(realnames);
		
		var candNodes = $("form").data("saveCandNode");
		if(candNodes){
			for(var j=0;j<candNodes.length;j++){
				if(node_key == candNodes[j]){
					saveCandi(node_key,accouts,realnames);
				}
			}
		}
	}
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
			alert("请选择处理结果！");
			return false;
		}

		if(check == 'turnTo'){
			var duser = $("#delegateUser").val();
			if(!duser||duser==''){
				alert("请选择转办人！");
				return false;
			}
		}
	}
	
	// 判断审批节点区域中处理人是否为空
	var trs = $("#protable tr");
	var vs = nodeStarNum.split(',');
	if($.trim(nodeStarNum) != ''){
		for(var j=0;j<vs.length;j++){
			var sid = vs[j];
			var spanname = $.trim($(trs[sid]).find("input[name^=realName]").val());
			if(sid!='0'&&( spanname == '' || spanname == '无审批人')){
				var rid = parseInt(sid) ;
				var actName = $("tr[id='protr"+ rid +"'] td:eq(0)").text();
				alert("节点["+actName+"]处理人不能为空！");
				return false;
			}
		}
	}
	
	return true;
}
