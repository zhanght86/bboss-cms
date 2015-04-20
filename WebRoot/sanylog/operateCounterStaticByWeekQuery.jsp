<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：浏览统计计数数据查询页面
	作者：qingl2
	版本：1.0
	日期：2012-08-27
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>浏览统计数据</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script language="JavaScript" src="fusion/FusionCharts.js"></script>
<script type="text/javascript" src="fusion/prettify.js"></script>
<script type="text/javascript" src="fusion/json2.js"></script>
<%@ include file="/sanylog/calender.jsp"%>
<script type="text/javascript">
	
	//页面加载时查询列表数据
	$(document).ready(function() {
	 	queryList(); 
	  });
	
	//查询浏览统计列表数据(页面加载时会加载这个方法，页面调用查询时也会调用)
	 function queryList() {	
	 	var appId = "${param.siteId}";
		var vtime = $("#vtime").val();
		var date = $("#date").val();
		vtime = date.substring(0,4)+"-"+vtime;
	   	$("#custombackContainer").load("showOperCounterRankByWeek.page #customContent", { appId:appId, vtime:vtime}, function(){loadjs()});
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
	 function outputExcel(){
		 var appId = "${param.siteId}";
		 var vtime = $("#vtime").val();
			var date = $("#date").val();
		var type = "td_log_operstatic_byweek";
		if(""==vtime||null==vtime){
			$.dialog.alert("请选择时间！");
		}else{
			var week  = date.substring(0,4)+"-"+vtime;
			document.getElementById("outputExcel").href="downloadExcel.page?time="+week+"&type="+type+"&appId="+appId;
		}
	 }
	 //重置查询条件
	 function doreset() {
   		$("#reset").click();
   	}
	 function clearweek(){
		 $("#inputdate_week").val("");
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
									<th>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
										<th>选择日期：</th>
											<td><input  id="date"  name="date"  type="text" onclick="WdatePicker({isShowWeek:true,onpicked:function(){$dp.$('vtime').value=$dp.cal.getP('W','W');}})" /></td> 	
										<th>您选择了第：</th>
										<td><input type="text" id="vtime" size="3" class="Wdate"/>&nbsp;&nbsp;周</td> 
											<!-- <td>
											<a href="javascript:void(0)" class="bt_1"	id="calendar_trigger_week" ><span>选择周</span> </a>
											<input id="inputdate_week" value=""/>
                               <a href="javascript:void(0)" class="bt_1" id="calendar_trigger_week" onclick="clearweek()"><span>清除</span> </a></td>  -->
									 <td><a href="javascript:void(0)" class="bt_1"
											id="queryButton" onclick="queryList()"><span>查询</span> </a> <a
											href="javascript:void(0)" class="bt_2" id="resetButton"
											onclick="doreset()"><span>重置</span> </a> <input type="reset"
											id="reset" style="display: none" /><a href="javascript:void(0)" class="bt_1"
											id="outputExcel" onclick="outputExcel()"><span>导出周统计报表</span> </a></td>
											
											
									</tr>
									
								</table>
							</td>
							<td class="right_box"></td>
						</tr>
					</table>
					<!-- 下面这段script代码必须放在form体的最后  
             loadcalendar方法的五个参数分别解释如下：
             1、日期显示文本框的ID号
             2、触发日历控件显示的控件ID号
             3、要显示的日期格式，%Y表示年，%m表示月，%d表示日
             4、是否带周显示，默认是不带
             5、是否带时间显示，默认是不带
             6、日历显示文字的语言，默认是中文 -->
        <!-- <script language="javascript" type="text/javascript">
            loadcalendar('inputdate_week', 'calendar_trigger_week', '%Y年%m月%d日', true, false, "cn");
        </script> -->
        <!-- 上面这段script代码必须放在form体的最后 -->
				</form>
				
			</div>
			 <div class="search_bottom">
				<div class="right_bottom"></div>
				<div class="left_bottom"></div>
			</div>
		</div>
		<div class="title_box">
			<strong>操作周统计数据</strong>
		</div> 
		<table width="100%" border="0">
			<tr>
				<td align="center" colspan="2">
					<div id="operCountCompare"></div>
					<script type="text/javascript">
					var vtime = $("#vtime").val();
					var date = $("#date").val();
					var week  = date.substring(0,4)+"-"+vtime;
						var chart = new FusionCharts("fusion/MSColumn2D.swf", "ChartId", "600", "250", "0", "0");
	    				chart.setXMLUrl("<%=request.getContextPath()%>/sanylog/operCountCompare.page?type=week&appId=${param.siteId}&time="+week);
	    				chart.render("operCountCompare");
					</script>
				</td>
				<td align="center">
					<div id="operUserCompare"></div>
					<script type="text/javascript">
					var vtime = $("#vtime").val();
					var date = $("#date").val();
					var week  = date.substring(0,4)+"-"+vtime;
						var chart = new FusionCharts("fusion/MSColumn2D.swf", "ChartId", "600", "250", "0", "0");
	    				chart.setXMLUrl("<%=request.getContextPath()%>/sanylog/operUserCompare.page?type=week&appId=${param.siteId}&time="+week);
	    				chart.render("operUserCompare");
					</script>
				<td>
			</tr>
		</table>
		<div id="custombackContainer"></div>
	</div>
</body>
</html>
