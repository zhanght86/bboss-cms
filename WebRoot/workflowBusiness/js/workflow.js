
//页面提交
function submitForm(formid,flag){
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
		$.dialog.alert("表单跳转页面（action）不能为空！");
	}

	if($("input[name='operateType']").length!=0){
		var check = $("input[name='operateType']:checked").val();
		if(!check){
			$.dialog.alert("请选择处理结果！");
			return ;
		}

		if(check == 'turnTo'){
			var duser = $("#delegateUser").val();
			if(!duser||duser==''){
				$.dialog.alert("请选择转办人！");
				return ;
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
				return ;
			}
		}
	}

	$(formid).form('submit', {
		onSubmit:function(){
			blockUI();
			if(!$("form").attr("subimt")){//防止重复提交
				$("form").attr("subimt","true");
			}else{
				return false;
			}
		},
		success:function(result){
			unblockUI();
			if(flag){
				if(typeof  submitBusinessReturn == "function"){
					submitBusinessReturn(result);
				 }
			}else{
			  if(typeof  submitReturn == "function"){
				submitReturn(result);
			  }
			}
		}
	});	
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