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
	  });
	
	//查询浏览统计列表数据
	 function queryList() {	
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		if(null==startTime||""==startTime){
			$.dialog.alert("请输入开始时间！");
			return;
		}
        if(null==endTime||""==endTime){
        	$.dialog.alert("请输入结束时间！");
			return;
		}
	   	$("#custombackContainer").load("<%=request.getContextPath()%>/sanylog/pageList/showPageCounterList.page #customContent", { startTime:startTime, endTime:endTime}, function(){loadjs()});
	}
	
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
	 function openMaintainPage(){
	      var url='';
	  	  title="上线功能数维护";
	  	  url='<%=request.getContextPath()%>/sanylog/pageList/maintainPage.page';
	  		$.dialog({ id:'iframeNewId', title:title,width:400,height:550, content:'url:'+url});
	  	
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
										<th>统计时间：</th>
										<td><input  id="startTime"  name="startTime" class="Wdate" type="text" onclick="WdatePicker()" />
													&nbsp;&nbsp;---&nbsp;&nbsp;
												<input id="endTime" name="endTime"  class="Wdate" type="text" onclick="WdatePicker()" /></td>
										
										<!-- <th>被访问页面地址：</th>
										<td><input id="pageURL" name="pageURL" type="text"
											value="" class="w120" /></td> -->
										<th>&nbsp;</th>
										<td>
										<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span>查询</span> </a> 
										<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span> </a> 
										<input type="reset" id="reset" style="display: none" />
										<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="openMaintainPage()"><span>上线功能数维护</span> </a>
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
			<strong>页面浏览统计汇总数据</strong>
		</div>
		<div id="custombackContainer"></div>
	</div>
</body>
</html>
