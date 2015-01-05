<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ include file="/common/jsp/css.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<title>业务类别管理</title>
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
											<th>业务类别码：</th>
											<td><input id="businessCode" name="businessCode" type="text" value="" class="w120"/></td>
											<th>业务类别名：</th>
											<td>
												<input id="businessName" name="businessName" type="text" value="" class="w120" />
											</td>
											<th>父节点：</th>
											<td><select class="easyui-combotree" id='parentBusinessType' name="parentId" required="false"
												style="width: 120px;"></td>
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
			<div class="title_box">
				<div class="rightbtn">
				<a href="javascript:void" class="bt_small" id="addButton" onclick="javascript:add()"><span>新增</span></a>
				<!--  
				<a href="javascript:void" class="bt_small" id="delBatchButton" onclick="javascript:batchDelete()"><span>批量删除</span></a>
				-->
				</div>
				
			</div>
			<div id="custombackContainer" >
			
			</div>
		</div>
	</body>
	
	<script type="text/javascript">
		$(document).ready(function () {
			queryList();
			$("#CKA").click(function() {
	        	$("#tb tr:gt(1)").each(function() {
	            	$(this).find("#CK").get(0).checked = $("#CKA").get(0).checked;
	            });
	        });
			
			$("#parentBusinessType").combotree({
				url:"showComboxBusinessTree.page"
			});
		});
				
		function queryList(){	
			$("#custombackContainer").load(
						"queryListPage.page #customContent", {
							businessCode : $("#businessCode").val(),
							businessName : $("#businessName").val(),
							parentId:$("#parentBusinessType").combobox("getValue")
						}, function() {
							loadjs()
						});
		}
		
		function doreset() {
			$("#reset").click();
		}
		
		function add() {
			$.dialog({
				id : 'add',
				title : '新增业务类别',
				width : 450,
				height : 320,
				content : 'url:' + "<%=request.getContextPath()%>/workflow/businesstype/businessTypeAdd.jsp"
			});
		}

		function del(businessId) {
			$.dialog.confirm('确定要删除记录吗？删除后将不可恢复', function() {
				$.ajax({
					type : "POST",
					url : "delete.page",
					data : {
						"businessId" : businessId
					},
					dataType : 'json',
					async : false,
					beforeSend : function(XMLHttpRequest) {
						XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
					success : function(data) {
						alert(data);
						queryList();
					}
				});
			});
		}

		function batchDelete() {

			var idString = "";
			$("#tb tr:gt(0)").each(function() {
				if ($(this).find("#CK").get(0).checked == true) {
					idString = idString + $(this).find("#id").val() + ",";
				}
			});

			if (idString == "") {
				$.dialog.alert('请选择需要删除的记录！');
				return;
			}

			$.dialog.confirm('确定要删除记录吗？，删除后将不可恢复', function() {
				$.ajax({
					type : "POST",
					url : "batchDelete.page",
					data : {
						"idString" : idString
					},
					dataType : 'json',
					async : false,
					beforeSend : function(XMLHttpRequest) {
						XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
					success : function(data) {
						queryList();
					}
				});
			});
		}
		
		function modify(businessId) {
			$.dialog({
				id : 'add',
				title : '修改业务类别资料',
				width : 450,
				height : 320,
				content : 'url:' + "<%=request.getContextPath()%>/workflow/businesstype/queryDataModify.page?businessId="
						+ businessId 
			});
		}
	</script>
</html>
	