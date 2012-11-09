<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/jsp/css.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<title>应用系统模块管理</title>
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
											<th>系统编号：</th>
											<td><input id="appId" name="appId" type="text" value="" class="w120"/></td>
											<th>系统名称：</th>
											<td>
												<input id="appName" name="appName" type="text" value="" class="w120" />
											</td>
										</tr>
										<tr>
											<th>模块编号：</th>
											<td><input id="moduleId" name="moduleId" type="text" value="" class="w120"/></td>
											<th>模块名称：</th>
											<td>
												<input id="moduleName" name="moduleName" type="text" value="" class="w120" />
											</td>
											<td style="text-align:right">
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList()"><span>查询</span>
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
			<div id="custombackContainer" >
			
			</div>
		</div>
	</body>
	
	<script type="text/javascript">
		$(document).ready(function () {
			queryList();
		});
				
		
		function queryList() {
			$("#custombackContainer").load(
					"getModuleList.page #customContent",
					{
						appId : $("#appId").val(),
						appName : $("#appName").val(),
						moduleId: $("#moduleId").val(),
						moduleName: $("#moduleName").val()
					}, function() {
						loadjs()
					});
		}

		function doreset() {
			$("#reset").click();
		}
		
		function modify(autoId, type) {
			$.dialog.confirm('确定要修改记录吗？', function() {
				$.ajax({
					type : "POST",
					url : "modifyModuleInfo.page",
					data : {
						autoId : autoId,
						type : type
					},
					dataType : 'json',
					async : false,
					beforeSend : function(XMLHttpRequest) {
						XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
					success : function(data) {
						if(data == "success")
						{	
							$.dialog.confirm("保存成功",
									function(){},
									function(){},"","确认");
						}
						else
						{
							$.dialog.confirm(data,
									function(){},
									function(){},"","保存失败");							
						}
						queryList();
					}
				});
			});
		}
	</script>
</html>
	