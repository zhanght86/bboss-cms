<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>接口测试</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/jquery.ztree.core-3.5.js"></script>
<link  href="${pageContext.request.contextPath}/html/zTree/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
$(document).ready(function() {
	   getOrgTree()
});
function getNextTime (){
	 var orgId = $('#orgId').val();
	 var startTime = $('#startTime').val();
	 var interval = $('#interval').val();
	 
	 var address = "<%=request.getContextPath()%>/sysmanager/holiday/util/getNextTimeNode.freepage";
	   $.ajax({
	 	 	type: "POST",
			url : address,
			data :{orgId:orgId,startTime:startTime,interval:interval},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data) {
					$('#theNextTime').html(data);
				} 
			}	
		 }); 
 }
function getOrgTree(){
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/sysmanager/holiday/util/getOrgTree.freepage",
		data :{},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			$.fn.zTree.init($("#treeDemo"), setting, data);
			$("#treediv").show();
		}	
	 }); 
}
function orgClick(event, treeId, treeNode, clickFlag) {
	
	$("#orgId").val(treeNode.orgId);
	//$("#orgName").val(treeNode.name);
}	
var setting = {
		data: {
			key: {
				title:"orgId"
			},
			simpleData: {
				enable: true
			}
		},
		callback: {
			onClick: orgClick
		}
	};
</script>
</head>
	<body>
	
	<div style="width:400px;height:400px;float:left;">
	<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="table2">
					<tr>
						<th>部门ID：</th>
						<td>
							<input id="orgId" name="orgId" type="text"/>
						</td>
						</tr>
					<tr>
						<th >开始时间：</th>
						<td ><input id="startTime" name="startTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							type="text" value=""  /></td>
					</tr>
					<tr>		
							
						<th >工作耗时：</th>
						<td ><input id="interval" name="interval" type="text" /></td>
						
					</tr>
					<tr>
					<th><a href="#" class="bt_small" id="addButton" onclick="javascript:getNextTime()"><span>获取完成时间:</span></a></th>
					<td id="theNextTime"></td>
					</tr>
				</table>
			</div>
			<div  id="treediv" style="width:400px;height:400px;float:left;display:none">
			<fieldset>
			  <legend>组织机构树</legend>
		      <ul id="treeDemo" class="ztree"></ul>
			</fieldset>
		</div>
		
		<div style="width:200px;height:400px;float:left;"  >
		<table width="100%" border="1px" cellspacing="0" cellpadding="0">
				<tr>
						<th>返回代码</th>
						<td>含义</td>
				</tr>
				<tr>
				       <th>1</th>
				       <td>部门编码为空或格式错误</td>
				</tr>
				<tr>
				       <th>2</th>
				       <td>开始时间为空或格式错误</td>
				</tr>
				<tr>
				       <th>3</th>
				       <td>工作耗时为空或格式错误</td>
				</tr>
				
				<tr>
				       <th>5</th>
				       <td>没有工作日设置</td>
				</tr>
				<tr>
				       <th>6</th>
				       <td>没有工作时间设置</td>
				</tr>
				<tr>
				       <th>7</th>
				       <td>没有区域设置</td>
				</tr>
				<tr>
				       <th>8</th>
				       <td>工作耗时要小于596523</td>
				</tr>
		</table>
					</div>
					
	</body>
</html>
