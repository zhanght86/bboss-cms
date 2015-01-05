<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
	
	//页面加载时查询列表数据
	$(document).ready(function() {
		queryAppInfo();
	 	queryList(); 
	  });
	
	//查询浏览统计列表数据(页面加载时会加载这个方法，页面调用查询时也会调用)
	 function queryList() {	
		var appId = $("#appId").val();
		var functionCode = $("#functionCode").val();
		var functionName = $("#functionName").val();
		
	   	$("#custombackContainer").load("showPageList.page #customContent", { appId:appId, functionCode:functionCode,functionName:functionName}, function(){loadjs()});
	}
	//查询相应的模块
	function queryAppInfo() {
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/sanylog/pageList/getAllApp.page",
			data :{},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				$.each(data,function(i,o){
					document.getElementById("appId").options.add(new Option(data[i].appName, data[i].appId));
				});	
			}	
		 });
	}
	function modifyQueryData()
	{
		$("#custombackContainer").load("showFunctionList.page?"+$("#querystring").val()+" #customContent",function(){loadjs()});
	}
	function deleteRecord(id){
		$.dialog.confirm("是否要删除？",function(){
			$.ajax({
		 	 	type: "POST",
				url : "deleteRecord.page",
				data :{id:id},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					$.dialog.alert(data);
					queryAppInfo();
					modifyQueryData();
				}	
			 });
			
			
		},function(){ //取消按钮回调函数
				})
	}
	function modifyOrIncrementRecord(id,type){
		var url='<%=request.getContextPath()%>/sanylog/pageList/modifyOrIncrementRecord.page?id='+id+'&type='+type;
		if("modify"==type){
			var title='页面清单修改';
		}else{
			var title='功能清单新增';
		}
		$.dialog({ id:'iframeNewId', title:title,width:450,height:330, content:'url:'+url}); 
	}
	
	function checkBrowserDetail(browserId){
	      var url='';
	  	  title="浏览记录明细";
	  	  url='<%=request.getContextPath()%>/sanylog/checkBrowserDetail.page?browserId='+browserId;
	  		$.dialog({ id:'iframeNewId', title:title,width:1200,height:550, content:'url:'+url});
	  	
 }
	function batchInput(){
		var url='<%=request.getContextPath()%>/sanylog/pageList/pageListbatchInput.jsp';
		var title='清单导入';
		$.dialog({ id:'iframeNewId', title:title,width:700,height:330, content:'url:'+url}); 
		
	}
	 //重置查询条件
	 function doreset() {
   		$("#reset").click();
   	}
	 
	 function staticSpentTime(){
		 $.ajax({
		 	 	type: "POST",
				url : "<%=request.getContextPath()%>/sanylog/pageList/staticSpentTime.page",
				data :{},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					$.dialog.alert(data);
					queryList();
				}	
			 });
	 }
	 
	 function templateDownload(){
		 var appId = $("#appId").val();
		 if(null==appId||""==appId){
			 $.dialog.alert("请选择系统！");
			 return;
		 }else{
				document.getElementById("templateButton").href="<%=request.getContextPath()%>/sanylog/pageList/downloadExcel.page?appId="+appId;

		 }
	 }
</script>
</head>
<body>
	<div class="mcontent">
	<sany:menupath />
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
										<th>项目名称：</th>
										<td>
											<select id="appId" name="appId"  maxlength="40"><!-- class="w120" -->
												<option value="">&nbsp;&nbsp;&nbsp;无限制&nbsp;&nbsp;&nbsp;</option>
											<select>
										</td>
										<th >功能路径：</th>
										<td ><input id="functionName" name="functionName"
											type="text" value="" class="w120" /></td>
									 <th >功能编码：</th>
										<td ><input id="functionCode" name="functionCode"
											type="text" value="" class="w120" /></td> 	
										<td><a href="javascript:void(0)" class="bt_1"
											id="queryButton" onclick="queryList()"><span>查询</span> </a> <a
											href="javascript:void(0)" class="bt_2" id="resetButton"
											onclick="doreset()"><span>重置</span> </a> <input type="reset"
											id="reset" style="display: none" /></td>
											<td></td>
											<td></td>
									</tr>
									
								</table>
							</td>
							<td class="right_box"></td>
						</tr>
					</table>
				</form>
			</div>
			<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void(0)" class="bt_1" id="staticSpentTime" onclick="staticSpentTime()"><span>工时统计</span> </a>
				<a href="javascript:void" class="bt_small" id="templateButton"  onclick="templateDownload()"><span>模板下载</span></a>
				<a href="javascript:void" class="bt_small" id="SubjectInputButton" onclick="batchInput()"><span>功能清单导入</span></a>
				</div>
			</div>
		</div>
		<div class="title_box">
			<strong>页面清单列表</strong>
		</div>
		<div id="custombackContainer"></div>
	</div>
</body>
</html>
