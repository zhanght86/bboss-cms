<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：个人统计
	作者：谭湘
	版本：1.0
	日期：2014-08-13
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>个人统计</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">

$(document).ready(function() {
	
	var oRegion = document.getElementById("orgName"); 
	var oRegionList = document.getElementById("orgDivTree"); 
	var oClose = document.getElementById("tdClose"); 
	var bNoAdjusted = true; 
	oClose.onclick = function() { 
		$("#orgDivTree").hide(); 
	}; 
	oRegion.onfocus = function() { 
	   $("#orgDivTree").show();
	   if(bNoAdjusted){ 
	       bNoAdjusted = false; 
	       oRegionList.style.width = this.offsetWidth - 2; 
	       oRegionList.style.posTop = oRegionList.offsetTop + this.offsetHeight; 
	       oRegionList.style.posLeft = oRegionList.offsetLeft - this.offsetWidth -3; 
	    } 
	}; 

	$("#wait").hide();
	
	$("#org_tree").load("../taskConfig/task_config_common_org_tree.jsp");
	
});

function query(orgId,businessTypeOrg,treeName){
	$("#orgName").val(treeName);
	$("#orgId").val(orgId);
}
       
//加载实时任务列表数据  
function queryList(){
	var orgId = $("#orgId").val();
	var realName = $("#realName").val();
	var count_start_time = $("#count_start_time").val();
	var count_end_time = $("#count_end_time").val();
	
	if (realName == '' && orgId == '') {
		alert('【所在部门】与【姓名】不能同时为空');
		return;
	}
	
	if (count_start_time == '' || count_end_time == '') {
		alert('统计日期时间不能为空');
		return;
	}
	
    $("#personalCountContainer").load("<%=request.getContextPath()%>/workflow/statistics/queryPersonalCountData.page #personalCountContent", 
    	{"orgId":orgId, "realName":realName,"count_start_time":count_start_time,"count_end_time":count_end_time},
    	function(){loadjs();});
}

//个人统计明细
function viewDetailInfo (userName,realName) {
	var count_start_time = $("#count_start_time").val();
	var count_end_time = $("#count_end_time").val();
	var url="<%=request.getContextPath()%>/workflow/statistics/queryPersonalDetailData.page?userName="
			+userName+"&count_start_time="+count_start_time+"&count_end_time="+count_end_time;
	$.dialog({ title:'个人明细('+realName+')',width:1100,height:620, content:'url:'+url});
}

// 个人效率分析
function efficiencyAnalyse(realName,startNum,dealNum,delegateNum,entrustNum,entrustedNum,rejectNum,cancelNum,discardNum){
	var count_start_time = $("#count_start_time").val();
	var count_end_time = $("#count_end_time").val();
	var url="<%=request.getContextPath()%>/workflow/statistics/toPersonalAnalyse.page?startNum="
			+startNum+"&dealNum="+dealNum+"&delegateNum="+delegateNum+"&entrustNum="+entrustNum
			+"&entrustedNum="+entrustedNum+"&rejectNum="+rejectNum+"&cancelNum="+cancelNum
			+"&discardNum="+discardNum+"&count_start_time="+count_start_time
			+"&count_end_time="+count_end_time;
	$.dialog({ title:'个人效率分析('+realName+')',width:500,height:300, content:'url:'+url});
}

function doreset(){
	$("#reset").click();
}

</script>
</head>

<body>
	<div class="mcontent" style="width:98%;margin:0 auto;overflow:auto;">
	
		<sany:menupath menuid="personalCount"/>
		
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
											<th>所在部门：</th>
											<td>
												<input id="orgId" name="orgId" type="hidden" class="w200" />
												<input id="orgName" name="orgName" type="text" class="w200" readonly/>
												<div id="orgDivTree" style="display:none;height:200px;position:absolute;background-color: white; ">
													<table width="100%" style="border:1px solid gray;" cellpadding= "0" cellspacing="0"> 
				                                        <tr><td> 
															<div style="width:200px; height:200px; overflow:auto;" id="org_tree"></div>
				                                        </td></tr> 
				                                        <tr> 
				                                           <td align="right" id="tdClose" style="cursor: hand;"> Close </td> 
				                                        </tr> 
					                                </table> 
												</div>
											</td>
											<th>姓名：</th>
											<td><input id="realName" name="realName" type="text" class="w120"/></td>
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
					
				<strong>个人统计列表</strong>
				<img id="wait" src="<%=request.getContextPath()%>/common/images/wait.gif" />				
			</div>
			
			<div id="personalCountContainer" style="overflow:auto"></div>
		</div>
	</div>
</body>
</html>
