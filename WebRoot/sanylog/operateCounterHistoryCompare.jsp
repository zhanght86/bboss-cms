<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：浏览统计计数数据查询页面
	作者：qingl2
	版本：1.0
	日期：
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script language="JavaScript" src="fusion/FusionCharts.js"></script>
<script type="text/javascript" src="fusion/prettify.js"></script>
<script type="text/javascript" src="fusion/json2.js"></script>
<script type="text/javascript">
	var appId;
	//页面加载时查询列表数据
	$(document).ready(function() {
	 	//queryList();
	 	appId=${param.siteId};
	  });
	
	
	//查询相应的模块
	function queryModuleInfo(appId) {
	  if (appId != null && appId != "") {
	  	$.ajax({
	 	 	type: "POST",
			url : "../sanylog/getModuleBySiteId.page",
			data :{"appId":appId},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				$.each(data,function(i,o){
					document.getElementById("channelId").options.add(new Option(data[i].moduleName, data[i].moduleId));
				});	
			}	
		 });
	  }
	}
	//查询频道信息(这个可以用来查询本站点下面的模块名称，要做适当的改动)
	function queryChannelInfo(siteId) {
	  if (siteId != null && siteId != "") {
	  	$.ajax({
	 	 	type: "POST",
			url : "../channel/getChannelBySiteId.page",
			data :{"siteId":siteId},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				$.each(data,function(i,o){
					document.getElementById("channelId").options.add(new Option(data[i].name, data[i].channelId));
				});	
			}	
		 });
	  }
	}
	 
	 //重置查询条件
	 function doreset() {
   		$("#reset").click();
   	}
	 
	 function queryChart(){
		 var startDate = $("#startDate").val();
		 var endDate = $("#endDate").val();
		 var startWeek = $("#startWeek").val();
		 var vtime1 = $("#vtime1").val();
		 startWeek = startWeek.substring(0,4)+"-"+vtime1;
		 var endWeek = $("#endWeek").val();
		 var vtime2 = $("#vtime2").val();
		 endWeek = endWeek.substring(0,4)+"-"+vtime2;
		 var startMonth = $("#startMonth").val();
		 var endMonth = $("#endMonth").val();
		 var startYear = $("#startYear").val();
		 var endYear = $("#endYear").val();
		 var compareType = $("#compareType").val();
		 if((null!=startDate&&""!=startDate)||(null!=endDate&&""!=endDate)){
			 var tableName = "TD_LOG_OPERSTATIC_BYDAY";//TD_LOG_OPERSTATIC_BYDAY   TD_LOG_OPER_BYDAY_CHART
			  getChart(tableName,compareType,startDate,endDate);
		 }else if((null!=startWeek&&""!=startWeek)||(null!=endWeek&&""!=endWeek)){
			 var tableName = "TD_LOG_OPERSTATIC_BYWEEK";//TD_LOG_OPERSTATIC_BYDAY   TD_LOG_OPER_BYWEEK_CHART
			  getChart(tableName,compareType,startWeek,endWeek);
		 }else if((null!=startMonth&&""!=startMonth)||(null!=endMonth&&""!=endMonth)){
			 var tableName = "TD_LOG_OPERSTATIC_BYMONTH";//TD_LOG_OPERSTATIC_BYMONTH   TD_LOG_OPER_BYMONTH_CHART
			  getChart(tableName,compareType,startMonth,endMonth);
		 }else if((null!=startYear&&""!=startYear)||(null!=endYear&&""!=endYear)){
			 var tableName = "TD_LOG_OPERSTATIC_BYYEAR";//TD_LOG_OPERSTATIC_BYYEAR   TD_LOG_OPER_BYYEAR_CHART
			  getChart(tableName,compareType,startYear,endYear);
		 }else{
			 $.dialog.alert("查询条件不能为空！");
		 }
	 }
	 function getChart(tableName,compareType,startTime,endTime){
		 var chart = new FusionCharts("fusion/MSLine.swf", "ChartId", "900", "250", "0", "0");
			chart.setXMLUrl("<%=request.getContextPath()%>/sanylog/historyCompare.page?tableName="+tableName+"&appId="+appId+"&compareType="+compareType+"&startTime="+startTime+"&endTime="+endTime);
			chart.render("historyCompare");
	 }
</script>
</head>
<body>
	<div class="mcontent">
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
								<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table2">
									<tr>
									<th>按日对比：</th>
										<th>开始日期：</th>
										<td>
											<td><input  id="startDate"  name="startDate" class="Wdate" type="text" onclick="WdatePicker()" /></td> 	
										</td>
										<th>结束日期：</th>
										<td>
											<td><input  id="endDate"  name="endDate" class="Wdate" type="text" onclick="WdatePicker()" /></td> 	
										</td>
										<th>按周对比：</th>
										<td>开始周数：</td>
										<td>
											<td><input  id="startWeek"  name="startWeek" class="Wdate"  type="text" onclick="WdatePicker({isShowWeek:true,onpicked:function(){$dp.$('vtime1').value=$dp.cal.getP('W','W');}})" />
											&nbsp;--&nbsp; <input type="text" id="vtime1" size="2" class="Wdate"/> &nbsp;周
											</td> 	
										</td>
										<th>结束周数：</th>
										<td>
											<td><input  id="endWeek"  name="endWeek" class="Wdate"  type="text" onclick="WdatePicker({isShowWeek:true,onpicked:function(){$dp.$('vtime2').value=$dp.cal.getP('W','W');}})" /> 	
										&nbsp;--&nbsp;<input type="text" id="vtime2" size="2" class="Wdate"/>&nbsp;周</td>
										</td>
										</tr>
										</table>
										<table width="100%" border="0" cellpadding="0" cellspacing="0"
									class="table2">
										<tr>
										<th>按月对比：</th>
										<th>开始月份：</th>
										<td>
											<td><input  id="startMonth"  name="startMonth" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" /></td> 	
										</td>
										<th>结束月份：</th>
										<td>
											<td><input  id="endMonth"  name="endMonth" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM'})" /></td> 	
										</td>
										<th>按年对比：</th>
										<th>开始年份：</th>
										<td>
											<td><input  id="startYear"  name="startYear" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" /></td> 	
										</td>
										<th>结束年份：</th>
										<td>
											<td><input  id="endYear"  name="endYear" class="Wdate" type="text" onclick="WdatePicker({dateFmt:'yyyy'})" /></td> 	
										</td>
										<th>比较类型：</th>
										<td>
											<select id="compareType" name="compareType" class="w120">
												<option value="count">使用次数</option>
												<option value="user">使用人数</option>
											<select>
										</td>
									 <td><a href="javascript:void(0)" class="bt_1"
											id="queryButton" onclick="queryChart()"><span>查询</span> </a> <a
											href="javascript:void(0)" class="bt_2" id="resetButton"
											onclick="doreset()"><span>重置</span> </a> <input type="reset"
											id="reset" style="display: none" /></td>
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
			<strong>历史数据比较</strong>
		</div> 
		<table width="100%" border="0">
			<tr>
				<td align="center" colspan="2">
					<div id="historyCompare"></div>
					<%-- <script type="text/javascript">
					    var time = $("#vtime").val();
						var chart = new FusionCharts("fusion/MSColumn2D.swf", "ChartId", "600", "250", "0", "0");
	    				chart.setXMLUrl("<%=request.getContextPath()%>/sanylog/operCountCompare.page?type=day&appId=${param.siteId}&time="+time);
	    				chart.render("operCountCompare");
					</script> --%>
				</td>
				<%-- <td align="center">
					<div id="operUserCompare"></div>
					<script type="text/javascript">
					    var time = $("#vtime").val();
						var chart = new FusionCharts("fusion/MSColumn2D.swf", "ChartId", "600", "250", "0", "0");
	    				chart.setXMLUrl("<%=request.getContextPath()%>/sanylog/operUserCompare.page?type=day&appId=${param.siteId}&time="+time);
	    				chart.render("operUserCompare");
					</script>
				<td> --%>
			</tr>
		</table>
		<div id="custombackContainer"></div>
	</div>
</body>
</html>
