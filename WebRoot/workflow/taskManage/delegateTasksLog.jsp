<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>转派任务日志</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<!-- 
<script type='text/javascript' src="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/include/autocomplete/jquery.autocomplete.css" />
 -->
<script type="text/javascript">
var api = frameElement.api, W = api.opener;

$(document).ready(function() {
	
	$("#wait").hide();
	
	queryList();  
	
	//$("#processKey").focus().autocomplete('');
           	
});

//转派任务
function delegateTasks () {
	var url="<%=request.getContextPath()%>/workflow/taskManage/delegateTasks.jsp?processKey=${processKey}";
	$.dialog({ title:'转派任务',width:300,height:200, content:'url:'+url});
}

//加载实时任务列表数据  
function queryList(){
	var processKey = $("#processKey").val();
	var fromUser = $("#fromUser").val();
	var toUser = $("#toUser").val();
	var delegateTime_from = $("#delegateTime_from").val();
	var delegateTime_to = $("#delegateTime_to").val();
	
    $("#delegateContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryDelegateTasksLogData.page #delegateContent", 
    	{"processKey":processKey,"fromUser":fromUser,"toUser":toUser,"delegateTime_from":delegateTime_from,
    	"delegateTime_to":delegateTime_to},function(){ });
}

function modifyQueryData(){
	$("#delegateContainer").load("<%=request.getContextPath()%>/workflow/taskManage/queryDelegateTasksLogData.page?"+$("#querystring").val()+" #delegateContent",function(){loadjs()});
}

function doreset(){
	$("#reset").click();
}

</script>
</head>
	
<body>
<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
	<div id="rightContentDiv">
		
		<div id="searchblock">
		
			<div class="search_top">
				<div class="right_top"></div>
				<div class="left_top"></div>
			</div>
			
			<div class="search_box">
				<form id="delegateLogForm" name="delegateLogForm">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td class="left_box"></td>
							<td>
								<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
									<tr>
										<th>流程key：</th>
										<td><input id="processKey" name="processKey" type="text" class="w120" value="${param.processKey}"
										<pg:notempty actual="${param.processKey}" > disabled</pg:notempty>/></td>
										<th>转派人：</th>
										<td><input id="fromUser" name="fromUser" type="text" class="w120"/></td>
										<th>被转派人：</th>
										<td><input id="toUser" name="toUser" type="text" class="w120"/></td>
										<th>转派时间：</th>
										<td><input id="delegateTime_from" name="delegateTime_from" type="text"
											onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
											~<input id="delegateTime_to" name="delegateTime_to" type="text"
											onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />
										</td>
										<td style="text-align:center" rowspan="2" >
											<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
											<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
											<input type="reset" id="reset" style="display:none"/>
										</td>
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
				<a href="javascript:void" class="bt_small" onclick="delegateTasks();"><span>转派</span></a>
			</div>
				
			<strong>转派日志列表</strong>
			<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
		</div>
		
		<div id="delegateContainer" style="overflow:auto"></div>
	</div>
</div>
</body>

</html>
