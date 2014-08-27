		
		/**
		 * 工作流人员选择js
		 * 
		 * @author          fudk
		 * @company         SANY Heavy Industry Co, Ltd
		 * @creation date   2013-10-24
		 * @version         1.0
		 */
		

//选择处理人
function setTaskCandidateUsers(node_key) {
	var candidateName = $("#candidateName"+node_key).val();
	if(!candidateName) candidateName='';
	var url = $("#context").val()+"/workflowBusiness/chooseOrgUser.jsp?node_key="+node_key+"&candidateName="+candidateName+"&callBackFunc=updateAfterChoose";
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:900,height:480, content:'url:'+url}); 
}

//选择转办人
function delegateUsers(node_key){		
	var candidateName = $("#delegateUser").val();
	if(!candidateName) candidateName='';
	var url = $("#context").val() +"/workflowBusiness/chooseOrgUser.jsp?node_key=&candidateName="+candidateName+"&callBackFunc=updateAfterChoose"+"&index=1";
	$.dialog({ id:'nodeInfoIframe', title:'选择用户',width:900,height:480, content:'url:'+url}); 
}	

function updateAfterChoose(accouts,realnames,node_key){
	
	if(!node_key){
		if(accouts.indexOf(",") < 0){
			$("#delegateUser").val(accouts);
			$("#delegateUserName").val(realnames);
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

//加载页面时 让需要保存默认个人默认审批人员进行保存配置
function saveCandiInit(objs){
		var vs = objs.split(',');
		$("form").data("saveCandNode", vs);
}

//保存节点默认处理人
function saveCandi(node_key,userCodes,userNames){

	$.post(ctx + '/workflow/updateProccessActUser.page', {
		processKey : $("#processKey").val(),
		userId : $("form").data("user").userId,
		actId : $("#actId"+node_key).val(),
		candidateName:userCodes,
		realName : userNames
//		realName : encodeURIComponent(userNames)
	}, function(data) {
//		if (data.success == '0') {
//			alert(data.message);
//			return;
//		}
	}, 'json');
}

 function submitReturn(result){
	//去掉遮罩
		unblockUI();
		if(result.indexOf("提交失败")!=-1){//提交失败时不退出页面
			lhgdialog.alert(result);
			$("form").attr("subimt","");
		}else{
		    lhgdialog.alert(result,function(){closeP()});
		}
 }
 
 //var api = frameElement.api, W = api.opener;
//关闭并刷新父窗口列表 或 刷新首页代办
function closeP(){
	try{
		var api2 = frameElement.api;
	}catch(e){
	}
		if(api2==undefined){
			if(typeof gotoPage == "function"){
				gotoPage();
				return;
			}
		}
		var  W2 = api2.opener;
		if(typeof W2.queryList == "function"){
			W2.queryList();
		}else if(typeof W2.queryData == "function"){
			W2.queryData();
		}
		api2.close();
	
}