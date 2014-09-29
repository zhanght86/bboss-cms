<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：流程实例列表
作者：谭湘
版本：1.0
日期：2014-05-08
 --%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程实例信息</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<%-- 
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/demo/selectgrid/styleSheet/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/demo/selectgrid/styleSheet/style.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/demo/selectgrid/Script/jquery-ui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/demo/selectgrid/Script/jquery.multiselect.js"></script>
--%>
<script type="text/javascript">

$(document).ready(function() {
	<%--
	$("#s1").multiselect({
		selectedList: 3,
		show: ["blind", 200],
		hide: ["blind", 200]
	});
	--%>
	$("#wait").hide();
	
	queryList();
			
	$('#logicDelBatchButton').click(function() {
		logicDelInst();
    });
	
	$('#physicalDelBatchButton').click(function() {
		physicalDelInst();
    });
	
	$('#stBatchButton').click(function() {  
		startInst();	 		  
    });
	
	$('#upBatchButton').click(function() {  
		upBatchButton();	 		  
    });
	
	$("#businessType").combotree({
	   url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
	});
	
});

// 逻辑删除实例
function logicDelInst(){
	var ids="";
	
	$("#tb tr:gt(0)").each(function() {
		if ($(this).find("#CK").get(0).checked == true) {
             ids=ids+$(this).find("#PROC_INST_ID_").val()+",";
        }
    });
	
    if(ids==""){
       $.dialog.alert('请选择需要删除的流程！');
       return false;
    }
    
  	//逻辑删除，要判断是否已经删除过
	var isDelete = "";//流程完成，删除才有备注，用来判断是否能删除
	$("#tb tr:gt(0)").each(function() {
		if ($(this).find("#CK").get(0).checked == true) {
			if ($(this).find("#END_TIME").val() != '') {
				isDelete = "false";
				return;
			}
	     }
	});
	    
	if (isDelete == "false") {
	    $.dialog.alert('已完成的流程不能删除');
	    return;
	}
    
    var url="<%=request.getContextPath()%>/workflow/repository/delProcessInstance.jsp?ids="+ids
    		+"&processKey=${processKey}";
    $.dialog({ id:'iframeNewId', title:'填写删除原因',width:400,height:200, content:'url:'+url});  
}

//物理删除实例
function physicalDelInst(){
	var ids="";
	
	$("#tb tr:gt(0)").each(function() {
		if ($(this).find("#CK").get(0).checked == true) {
             ids=ids+$(this).find("#PROC_INST_ID_").val()+",";
        }
    });
	
    if(ids==""){
       $.dialog.alert('请选择需要删除的流程！');
       return false;
    }
    
	$.dialog.confirm('确定要删除记录吗？删除后将不可恢复', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/repository/delInstancesForPhysics.page",
			data :{"processInstIds":ids},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(data){
				if(data=="success"){
		 			modifyQueryData();
				}else{
					alert("流程实例物理删除出错："+data);
				}
			}	
		 });
	},function(){});    
}

// 开始实例
function startInst(){
	var processKey = $("#wf_key").val();
	
	var url = "<%=request.getContextPath()%>/workflow/repository/toStartProcessInst.page?processKey="
			+ processKey;
	
	$.dialog({ title:'流程节点参数配置-'+processKey,width:1000,height:620, content:'url:'+url,maxState:true});     
	 
}

// 升级
function upBatchButton() {
	$.dialog.confirm('确定升级？', function(){
    	$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/repository/upgradeInstances.page",
			data :{"processKey":'${processKey}'},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("流程实例升级出错："+data);
				}else {
					modifyQueryData();
					close();	
				}
			}	
		 });
     },function(){
     		
     });
}

// 查询数据
function queryList(){	
	var wf_key = $("#wf_key").val();
	var wf_Inst_Id = $("#wf_Inst_Id").val();
	var wf_start_time1 = $("#wf_start_time1").val();
	var wf_start_time2 = $("#wf_start_time2").val();
	var wf_end_time1 = $("#wf_end_time1").val();
	var wf_end_time2 = $("#wf_end_time2").val();
	var wf_state = $("#wf_state").val();
	var wf_business_key = $("#wf_business_key").val();
	var wf_version = $("#wf_version").combobox('getValues');
	var businessTypeId = $("#businessType").combotree('getValue');
	var wf_app_name = $("#wf_app_name").val();
	
	$("#instanceContainer").load("<%=request.getContextPath()%>/workflow/repository/queryProcessIntsByKey.page? #customContent", 
		{"wf_key":wf_key,"wf_Inst_Id":wf_Inst_Id,"wf_start_time1":wf_start_time1,"wf_start_time2":wf_start_time2,
		"wf_end_time1":wf_end_time1,"wf_end_time2":wf_end_time2,"wf_state":wf_state,"wf_versions":wf_version,
		"wf_business_key":wf_business_key,"businessTypeId":businessTypeId,"wf_app_name":wf_app_name},
		function(){loadjs();});
}

function modifyQueryData(){
	$("#instanceContainer").load("<%=request.getContextPath()%>/workflow/repository/queryProcessIntsByKey.page?"+$("#querystring").val()+" #customContent",function(){loadjs()});
}

//查看流程实例详情
function viewDetailInfo(processInstId) {
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewTaskDetailInfo.page?processInstId="+processInstId;
	$.dialog({ title:'明细查看-'+$("#wf_key").val(),width:1100,height:620, content:'url:'+url});
}

//流程实例挂起
function suspendProcess(processInstId){
	$.dialog.confirm('确定将实例挂起？', function(){
    	$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/repository/suspendProcessInst.page",
			data :{"processInstId":processInstId},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("流程实例挂起出错："+data);
				}else {
					modifyQueryData();
					close();	
				}
			}	
		 });
     },function(){
     		
     });
}

//流程实例激活
function activateProcess(processInstId){
	$.dialog.confirm('确定将实例激活？', function(){
    	$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/repository/activateProcessInst.page",
			data :{"processInstId":processInstId},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data != 'success') {
					alert("流程实例激活出错："+data);
				}else {
					modifyQueryData();
					close();	
				}
			}	
		 });
     },function(){
     		
     });
}

function doreset(){
	$("#wf_version").combobox("setValues","");
   	$("#reset").click();
}
	
</script>
</head>
	
<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
	<pg:empty actual="${processKey}">
		<sany:menupath menuid="processInsts"/>
	</pg:empty>	
	
		<div id="rightContentDiv">
			
			<div id="searchblock">
			
				<div class="search_top">
					<div class="right_top"></div>
					<div class="left_top"></div>
				</div>
				
				<div class="search_box">
					<form id="queryForm" name="queryForm">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="left_box"></td>
								<td>
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
										<tr>
											<th>流程实例ID：</th>
											<td><input id="wf_Inst_Id" name="wf_Inst_Id" type="text" class="w120" /></td>
											<th>状态：</th>
											<td>
												<select id="wf_state" name="wf_state" class="select1" style="width: 125px;">
												    <option value="0" selected>全部</option>
													<option value="1">进行中</option>
													<option value="2">挂起</option>
													<option value="3">完成</option>
												</select>
											</td>
											<th>开启时间：</th>
											<td><input id="wf_start_time1" name="wf_start_time1" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
												 ~<input id="wf_start_time2" name="wf_start_time2" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
											</td>
											<td rowspan="3" style="text-align:right">
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
												<input type="reset" id="reset" style="display:none"/>
											</td>
										</tr>
										<tr>
											<th>流程key：</th>
											<td>
												<input id="wf_key" name="wf_key" type="text" class="w120" value="${processKey}"
												<pg:notempty actual="${processKey}" > disabled</pg:notempty>/>
											</td>
											<th>业务主题：</th>
											<td>
												<input id="wf_business_key" name="wf_business_key" type="text" class="w120" />
											</td>
											<th>结束时间：</th>
											<td><input id="wf_end_time1" name="wf_end_time1" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
												 ~<input id="wf_end_time2" name="wf_end_time2" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
											</td>
										</tr>
										<tr>
											<th>版本：</th>
											<td>
												<select id="wf_version" name="wf_version" class="easyui-combobox" style="width: 120px;" editable="false" panelHeight="100px;" multiple="multiple">
													<pg:list actual="${versionList}" >            
														<option value="<pg:cell colName="VERSION_"/>" ><pg:cell colName="VERSION_"/> </option>
													</pg:list>  
												</select>
												<%--
												<select name="" multiple="multiple" style="width:125px" id="s1">
													<pg:list actual="${versionList}" >            
														<option value="<pg:cell colName="VERSION_"/>" ><pg:cell colName="VERSION_"/> </option>
													</pg:list>  
												</select>
												--%>
											</td>
											<th>业务类型：</th>
											<td>
												<select class="easyui-combotree" id='businessType' name="businessType" required="false"
														style="width: 120px;">
											</td>
											<pg:empty actual="${processKey}" >
												<th>应用：</th>
												<td>
													<input id="wf_app_name" name="wf_app_name" type="text" class="w120" />
												</td>
											</pg:empty>
										</tr>
									</table>
								</td>
								<td class="right_box"></td>
							</tr>
						</table>
					</form>
				</div>
				
				<div class="search_bottom">
					<div class="right_bottom"></div>
					<div class="left_bottom"></div>
				</div>
			</div>
			
			<div class="title_box">
				<div class="rightbtn">
					<pg:notempty actual="${processKey}" >
						<a href="#" class="bt_small" id="stBatchButton"><span>开启</span></a>
						<a href="#" class="bt_small" id="upBatchButton"><span>升级</span></a>
					</pg:notempty>
					<a href="#" class="bt_small" id="logicDelBatchButton"><span>废弃流程</span></a>
					<a href="#" class="bt_small" id="physicalDelBatchButton"><span>物理删除流程</span></a>
				</div>
					
				<strong>实例列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
			</div>
			
			<div id="instanceContainer" style="overflow:auto"></div>
		</div>
	</div>
</body>
</html>
