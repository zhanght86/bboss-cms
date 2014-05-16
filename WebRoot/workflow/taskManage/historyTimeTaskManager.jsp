<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：历史任务管理
	作者：谭湘
	版本：1.0
	日期：2014-05-07
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>历史任务管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#wait").hide();
       		
    queryList();   
           
});
       
//加载历史任务列表数据  
function queryList(){	
	var proc_inst_id_ = $("#proc_inst_id_").val();
	var proc_def_id_ = $("#proc_def_id_").val();
		
    $("#historyTimeContainer").load("queryHistoryTimeTaskData.page #customContent", 
    	{proc_inst_id_:proc_inst_id_, proc_def_id_:proc_def_id_},function(){loadjs();});
	}
	
function doreset(){
	$("#reset").click();
}
    

</script>
</head>

<body>
<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
	<sany:menupath />
		
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
									<td><input id="proc_inst_id_" name="proc_inst_id_" type="text" class="w120"/></td>
									<td style="text-align:right">
										<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span><pg:message code="sany.pdp.common.operation.search"/></span></a>
										<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span><pg:message code="sany.pdp.common.operation.reset"/></span></a>
										<input type="hidden" name="proc_def_id_" id="proc_def_id_" />
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
			<a href="#" class="bt_small" id="delBatchButton"><span><pg:message code="sany.pdp.common.batch.delete"/></span></a>
		</div>
					
		<strong>历史任务列表</strong>
			<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
		</div>
			
		<div id="historyTimeContainer" style="overflow:auto"></div>
	</div>
</div>
</body>
</html>
