<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：历史任务管理
	作者：谭湘
	版本：1.0
	日期：2014-06-18
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>历史任务管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.css" />

<script type="text/javascript">

$(document).ready(function() {

	$("#wait").hide();
	
	$("#hiCopywait").hide();
       		
	queryList();   
	
	queryHiCopyList();
       		
    $('#delBatchButton').click(function() {
           delBatch();
    });
    
    $("#businessType").combotree({
   		url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
   	});
    
    <%-- 自动匹配用户名称--%>
	 var url="<%=request.getContextPath()%>/workflow/taskManage/getUserPageList.page";
		$("#queryForm #assignee").autocomplete(url , {
	         minChars:1,//自动完成激活之前填入的最小字符 
	         width:120,
	         max:20,// 最多显示多少条记录
	         dataType:"json",
	         // 扩展字段
	         extraParams: {    
	        	 assigneeName: function(){return $("#queryForm #assignee").val()}
	         },
	         scroll : true,//是否显示滚动条
	         matchContains: true, //包含匹配，就是data参数里的数据，是否只要包含文本框里的数据就显示 
	         cacheLength : 30, //缓存结果队列长度 
	         valuefiled:"user_realname",// 默认显示实体哪个属性
	         //下拉列表格式   
	         formatItem: function(row, i, max) {
	         	return "<I>"+row.user_realname+"("+row.user_name+")</I>";
	         },
	         //与这些相匹配
	         formatMatch: function(row, i, max) {
	              return row;
	         }
	     }).result(function(event,row,formatted){//通过result函数可进对数据进行其他操作
	     		$("#queryForm #assignee").val(row.user_name);
	     });
           	
});

//加载已抄送列表数据  
function queryHiCopyList(){
	
    $("#hiCopyContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryHiCopyTaskData.page #hiCopyContent", 
    	{"process_key":"${processKey}"},function(){loadjs();});
}

//加载实时任务列表数据  
function queryList(){
	var processIntsId = $("#processIntsId").val();
	var processKey = $("#processKey").val();
	var taskName = $("#taskName").val();
	var taskId = $("#taskId").val();
	var businessTypeId = $("#businessType").combotree('getValue');
	var businessKey = $("#businessKey").val();
	var createUser = $("#createUser").val();
	var entrustUser = $("#entrustUser").val();
	var appName = $("#appName").val();
	var assignee = $("#queryForm #assignee").val();
	
    $("#historyContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryHistoryTaskData.page #customContent", 
    	{"processIntsId":processIntsId, "processKey":processKey,"taskId":taskId,"taskName":taskName,
    	"businessTypeId":businessTypeId,"businessKey":businessKey,"createUser":createUser,
    	"entrustUser":entrustUser,"appName":appName, "assignee":assignee},
    	function(){loadjs();});
}

function modifyQueryData(){
	$("#historyContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryHistoryTaskData.page?"+$("#querystring").val()+" #customContent",function(){loadjs()});
}

//查看流程实例详情
function viewDetailInfo(processInstId) {
	var url="<%=request.getContextPath()%>/workflow/taskManage/viewTaskDetailInfo.page?processInstId="+processInstId;
	$.dialog({ title:'明细查看',width:1100,height:620, content:'url:'+url,maxState:true});
}
	
function doreset(){
	$("#reset").click();
}

</script>
</head>

<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
	<pg:empty actual="${processKey}">
		<sany:menupath menuid="historyTask"/>
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
											<th>任务ID：</th>
											<td><input id="taskId" name="taskId" type="text" class="w120"/></td>
											<th>任务名称：</th>
											<td><input id="taskName" name="taskName" type="text" class="w120"/></td>
											<th>业务类型：</th>
											<td>
												<select class="easyui-combotree" id='businessType' name="businessType" required="false"
														style="width: 120px;">
											</td>
											<th>被委托人：</th>
											<td>
												<input id="entrustUser" name="entrustUser" type="text" class="w120"/>
											</td>
											<td style="text-align:center" rowspan="3" >
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
												<input type="reset" id="reset" style="display:none"/>
											</td>
										</tr>
										<tr>
											<th>流程实例ID：</th>
											<td><input id="processIntsId" name="processIntsId" type="text" class="w120"/></td>
											<th>业务主题：</th>
											<td><input id="businessKey" name="businessKey" type="text" class="w120"/></td>
											<th>流程key：</th>
											<td><input id="processKey" name="processKey" type="text" class="w120" value="${processKey}"
												<pg:notempty actual="${processKey}" > disabled</pg:notempty>/>
											</td>
											<th>委托人：</th>
											<td>
												<input id="createUser" name="createUser" type="text" class="w120"
												<pg:false actual="${isAdmin}"> value="${currentAccount}" disabled</pg:false>
												/>
											</td>
										</tr>
										<tr>
											<th>处理人：</th>
											<td>
												<pg:true actual="${isAdmin}" evalbody="true">
													<pg:yes>
														<input id="assignee" name="assignee" type="text" class="w120" />
													</pg:yes>
													<pg:no>
														<input id="assignee" name="assignee" type="text"
															class="w120" value="${currentAccount}" disabled/>
													</pg:no>
												</pg:true>
											</td>
											
											<pg:empty actual="${processKey}">
												<th>应用：</th>
												<td><input id="appName" name="appName" type="text" class="w120"/></td>
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
				<div class="rightbtn"></div>
					
				<strong>历史任务列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
			</div>
			
			<div id="historyContainer" style="overflow:auto"></div>
			
			<%--已读抄送任务div --%>
			<div id="hiCopyDiv" >
				<!-- <div id="searchblock" >
					
						<div class="search_top">
							<div class="right_top"></div>
							<div class="left_top"></div>
						</div>
						
						<div class="search_box">
							<form id="hiCopyForm" name="hiCopyForm">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
								</table>
							</form>
						</div>
						
						<div class="search_bottom">
							<div class="right_bottom"></div>
							<div class="left_bottom"></div>
						</div>
					</div> -->
					
					<div class="title_box">
						<div class="rightbtn"></div>
							
						<strong>抄送任务列表</strong>
						<img id="hiCopywait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
					</div>
					
					<div id="hiCopyContainer" style="overflow:auto"></div>
			
			</div>
			
		</div>
	</div>
</body>
</html>
