<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：浏览统计计数数据查询页面
	作者：gw_hel
	版本：1.0
	日期：2012-08-27
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>浏览统计数据</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
	
	//页面加载时查询列表数据
	$(document).ready(function() {
	 	queryList1(); 
	  });
	
	//查询浏览统计列表数据(页面加载时会加载这个方法，页面调用查询时也会调用)
	 function queryList1() {	
		   var appId = "${param.siteId}";
			var year = $("#year").val();
			var month = $("#month").val();
		   	$("#custombackContainer").load("showOperCounterRankByMonth.page #customContent", { appId:appId, year:year,month:month}, function(){loadjs()});
	}
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
										<th>年份：</th>
										<td>
											<select id="year" name="year" class="w120">
												<option value="2012">2012</option>
												<option value="2013">2013</option>
												<option value="2014">2014</option>
												<option value="2015">2015</option>
												<option value="2016">2016</option>
											<select>
										</td>
										<th>月份：</th>
										<td>
											<select id="month" name="month" class="w120">
											    <option value=""></option>
												<option value="01">01</option>
												<option value="02">02</option>
												<option value="03">03</option>
												<option value="04">04</option>
												<option value="05">05</option>
												<option value="06">06</option>
												<option value="07">07</option>
												<option value="80">08</option>
												<option value="09">09</option>
												<option value="10">10</option>
												<option value="11">11</option>
												<option value="12">12</option>
											<select>
										</td>
									 <td><a href="javascript:void(0)" class="bt_1"
											id="queryButton" onclick="queryList1()"><span>查询</span> </a> <a
											href="javascript:void(0)" class="bt_2" id="resetButton"
											onclick="doreset()"><span>重置</span> </a> <input type="reset"
											id="reset" style="display: none" /></td>
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
			<strong>操作月统计数据</strong>
		</div> 
		<div id="custombackContainer"></div>
	</div>
</body>
</html>
