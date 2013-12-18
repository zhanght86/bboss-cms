<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
	描述：部署流程
	作者：gw_hel,gw_caim
	版本：1.0
	日期：2012-03-20
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title></title>
		<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
	</head>
	<body>
		<div class="form">
			<form id="deployProcessFrom" name="deployProcessFrom" method="post"  enctype="multipart/form-data">
			<input type="hidden" id="businessTypeId" name="businessTypeId"/>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=110px >
							<pg:message code="sany.pdp.workflow.deploy.name"/>：
						</th>
						<td width="140px">
							<input id="NAME_" name="NAME_" type="text" value=""
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="10" /><font color="red">*</font>
						</td>
					</tr>
					<tr>
						<th width="110px" >
							<pg:message code="sany.pdp.workflow.operation.choose.deploy.resource"/>：
						</th>
						<td width="140px" >
							<input type="file" name="processDef" id="processDef"  style="height: 20px; width: 180px"/>
						</td>
					</tr>
					<tr>
						<th width="110px" >
							<pg:message code="sany.pdp.workflow.operation.choose.parameter.resource"/>：
						</th>
						<td width="140px" >
							<input type="file" name="paramFile" id="paramFile"  style="height: 20px; width: 180px"/>
						</td>
					</tr>
					<tr>
						<th><pg:message code="sany.pdp.workflow.business.type"/></th>
						<td>
							<select class="easyui-combotree" id='businessType' name="businessType" required="false"
									style="width: 120px;">
						</td>
					</tr>
					<tr>
						<th width="110px">
							<!--pg:message code="sany.pdp.workflow.operation.config.task.again"/-->
							清除待办处理人和节点参数：
						</th>
						<td>
							<input type="checkbox" name="needConfigCheckbox" id="needConfigCheckbox" />
							<input type="hidden" name="needConfig" id="needConfig" value="0"/>
						</td>
					</tr>
					
					<tr>
						<th width="110px">
							流程实例升级策略：
						</th>
						<td>
							<input type="radio" name="upgradepolicy" value="0" checked/>保持旧版本
							<input type="radio" name="upgradepolicy" value="1"/>升级旧版本实例到新版本
							<input type="radio" name="upgradepolicy" value="2"/>删除旧版本流程实例
						</td>
					</tr>
				</table>			
			<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit2()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
				<!-- <a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span></a> -->
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
	</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
function checkBM(){
	var bm=$("#bm").val();
	var id="";
	var isValid=false;
 	$.ajax({
		   type: "POST",
			url : "CheckBmExist.page",
			data :{"bm":bm,"id":id},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if(data=="0")
					isValid=true;
				else
					isValid=false;
			}
		  });
		  return isValid;		  
}

  function dosubmit2()
  {
	  if($("#NAME_").val()==''){
		  alert("请输入部署名称");
		  return;
	  }
	  if($("#processDef").val()==''){
		 alert("请选择部署资源");
	 	 return;
	  };
	  if($('#businessType').combotree('getValue')==''){
		 alert("请选择所属业务类型");
	  	return;
	  };
	  $('#businessTypeId').val($('#businessType').combotree('getValue'));
	  if(document.getElementsByName("needConfigCheckbox")[0].checked){
		  $("#needConfig").val(1);
	  }else{
		  $("#needConfig").val(0);
	  }
	  $("#deployProcessFrom").form('submit', {
		    "url": "deployProcess.page",
		    onSubmit:function(){			
				//显示遮罩							
				blockUI();	
		    },
		    success:function(responseText){	
		    	//去掉遮罩	
				unblockUI();
				if(responseText=="success"){
				$.dialog.alert("部署成功",function(){	
							W.modifyQueryData();
							api.close();
					},api);													
				}else{
					$.dialog.alert("部署异常--"+responseText,function(){},api);
				}
		    }
		});	
  }


   function dosubmit()
   {
	   /*
		if(!checkBM()){
			
			$.dialog.alert("编码已存在，请重新录入!",function(){return;},api);
			return;
		}
	   */
		$.ajax({
		   type: "POST",
			url : "deployProcess.page",
			data :formToJson("#deployProcessFrom"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					var validated = $("#deployProcessFrom").form('validate');
			      	if (validated){
			      		blockUI();	
			      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			      	}
			      	else
			      	{			      		
			      		return false;
			      	}				 	
				},
			success : function(responseText){
				//去掉遮罩
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("新增记录成功",function(){	
							W.modifyQueryData();
							api.close();
					},api);													
				}else{
					$.dialog.alert("新增出错",function(){},api);
				}
			}
		  });
   	 }
function doreset(){
	$("#reset").click();
}

$(document).ready(function() {	
	$("#businessType").combotree({
		url:"../businesstype/showComboxBusinessTree.page"
		});
});
</script>