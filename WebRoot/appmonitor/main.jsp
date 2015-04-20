<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：Portal临控主页面
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>Portal监控管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript">
  $(document).ready(function() {
  		init();
  	 	queryData();
 	    $('#queryButton').click(function() {
          	queryData();
          });
          
           $.ajax({
      	  type: "POST",
	      url : "<%=request.getContextPath()%>/appmonitor/getAllAppInfo.page",
	      
	      dataType : 'json',
	      async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					var htm="<option value=''></option>";
					var options = $("#appId_query"); 
						$.each(data,function(i,o){
								htm += "<option value='"+o.appId+"'>"+o.appName+"</option>"
							});			 	
			 		options.html(htm);
			 		
				}	
			  });
	      
  })
  /*
  function queryList(){
  	var monitorTimeBegin=$("#monitorTimeBegin_query").val();
	var monitorTimeEnd=$("#monitorTimeEnd_query").val();
  	$("#custombackContainer").load("queryList.page #customContent",function(){loadjs()})
  	$("#custombackContainer2").load("queryReportList.page #customContent",function(){loadjs()})
  }*/
  	function queryData()
	{	
		var appId=$("#appId_query").val();
		var monitorTimeBegin=$("#monitorTimeBegin_query").val();
		var monitorTimeEnd=$("#monitorTimeEnd_query").val();
		var useTime=$("#useTime_query").val();
		var sign=$("#sign_query").val();
		$("#custombackContainer").load("queryList.page #customContent",
		{appId:appId,monitorTimeBegin:monitorTimeBegin,monitorTimeEnd:monitorTimeEnd,useTime:useTime,sign:sign},function(){loadjs()});
		
		$("#custombackContainer2").load("queryReportList.page #customContent",{monitorTimeBegin:monitorTimeBegin,monitorTimeEnd:monitorTimeEnd},function(){loadjs()});
		
	}
	function doreset(){
		$("#reset").click();
		init();
	}
	function init(){
		var dt=new Date();
		
		var month=parseInt(dt.getMonth())+1;
		if(month<10){
		 month='0'+month;
		}
		var date=parseInt(dt.getDate());
		if(date<10){
		 date='0'+date;
		}
		$("#monitorTimeBegin_query").val(dt.getFullYear()+'-'+month+'-'+date+' 00:00:00');
		$("#monitorTimeEnd_query").val(dt.getFullYear()+'-'+month+'-'+date+' 23:59:59');

	}
 </script>
</head>
	<body>
		<div class="mcontent">
			<sany:menupath menuid="portalmonitor"/>
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
											<th>
												应用系统：
											</th>
											<td >
												<select id="appId_query" name="appId"
													class="select1" style="width:125px;">
												</select>
											</td>
											<th>
												检测时间：
											</th>
											<td>
												<input id="monitorTimeBegin_query" name="monitorTimeBegin" type="text"
													value="" onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" /> 至 
												<input id="monitorTimeEnd_query" name="monitorTimeEnd" type="text"
													value="" onclick="new WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="w120" />${monitorTimeEnd}
											</td>
											<th>
												取值时长：
											</th>
											<td>
											<select id="sign_query" name="appId"
													class="select1" style="width:40px;">
													<option value="">
														
													</option>
													<option value=">=">
														>=
													</option>
													<option value="<=">
														<=
													</option>
													<option value="=">
														=
													</option>

												</select>
												<input id="useTime_query" name="useTime" type="text"
													value="" class="w120" style="width:60px;"/>毫秒
											</td>
											<th>
												&nbsp;
											</th>
											<td>
												<a href="javascript:void(0)" class="bt_1" id="queryButton"><span>查询</span>
												</a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span>
												</a>
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
			<div id="nodata">
				<table width="100%">
					<tr>
						<td width="62%" valign="top">
						<div id="custombackContainer"></div>
						</td>
						<td width="2%"></td>
						<td width="36%" valign="top">
						<div id="custombackContainer2"></div>
						</td>
					</tr>
				</table>		
			</div>
		</div>
	</body>
</html>
