<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：流程统计
	作者：谭湘
	版本：1.0
	日期：2014-08-13
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程统计</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">

$(document).ready(function() {

	$("#wait").hide();
	
	queryList();
	
	$("#businessType").combotree({
	   	url:"<%=request.getContextPath()%>/workflow/businesstype/showComboxBusinessTree.page"
	});
           	
});
       
//加载流程统计列表数据  
function queryList(){
	var app = $("#app").val();
	var count_start_time = $("#count_start_time").val();
	var count_end_time = $("#count_end_time").val();
	var businessType = $("#businessType").combotree('getValue');
	
    $("#processCountContainer").load("<%=request.getContextPath()%>/workflow/statistics/queryProcessCountData.page #processCountContent", 
    	{"app":app, "count_start_time":count_start_time,"count_end_time":count_end_time,"businessType":businessType},
    	function(){loadjs();});
}

// 流程统计明细
function viewDetailInfo (processKey,processName) {
	var count_start_time = $("#count_start_time").val();
	var count_end_time = $("#count_end_time").val();
	var url="<%=request.getContextPath()%>/workflow/statistics/queryProcessDetailData.page?processKey="
			+processKey+"&count_start_time="+count_start_time+"&count_end_time="+count_end_time;
	$.dialog({ title:'流程明细('+processName+')',width:1100,height:620, content:'url:'+url});
}

// 流程效率分析
function efficiencyAnalyse(processKey,processName){
	var count_start_time = $("#count_start_time").val();
	var count_end_time = $("#count_end_time").val();
	var url="<%=request.getContextPath()%>/workflow/statistics/toProcessAnalyse.page?processKey="
			+processKey+"&count_start_time="+count_start_time+"&count_end_time="+count_end_time;
	$.dialog({ title:'流程效率分析('+processName+')',width:500,height:300, content:'url:'+url});
}

function doreset(){
	$("#reset").click();
}

</script>
</head>

<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
		<sany:menupath menuid="processCount"/>
		
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
											<th>所属应用：</th>
											<td><input id="app" name="app" type="text" class="w120"/></td>
											<th>业务类型：</th>
											<td>
												<select class="easyui-combotree" id='businessType' name="businessType" required="false"
													style="width: 120px;">
											</td>
											<th>统计日期：</th>
											<td>
												<input id="count_start_time" name="count_start_time" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd'})" class="w120" value="${count_start_time}"/>
												 ~<input id="count_end_time" name="count_end_time" type="text"
												 onclick="new WdatePicker({dateFmt:'yyyy-MM-dd'})" class="w120" value="${count_end_time}"/>
											</td>
											<td style="text-align:center" >
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
				</div>
					
				<strong>流程统计列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
			</div>
			
			<div id="processCountContainer" style="overflow:auto"></div>
		</div>
	</div>
</body>
</html>
