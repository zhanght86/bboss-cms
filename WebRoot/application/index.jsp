<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl"%>


<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl,com.frameworkset.common.poolman.DBUtil,com.frameworkset.common.poolman.PreparedDBUtil" %>
<%@ page import="com.frameworkset.platform.config.ConfigManager" %>

<html >
<head>				

		<title>属性容器</title>
		<%@ include file="/common/jsp/css.jsp"%>
		<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	<script language="JavaScript" src="common.js" type="text/javascript"></script>
	<SCRIPT language="javascript">	
	
	function cleanAll(){
		$("#system_name").val("");
		$("#system_id").val("");
		$("#app_mode_type").val("");
		$("#app_mode_type").val("");
		
	}
	
	function pageInit(){
		
		queryList();
		
	}
	
	//查找
	function queryList(){
		
		$("#wfAppContentPage").load("<%=request.getContextPath()%>/application/queryListPage.page #wfAppContent", 
		formToJson("#queryForm"), function(){loadjs()});
	
	}
	
	//验证增加的表单
	function validateSaveAppInfo(){
		
		if($("#system_name").val() == ""){
			alert("应用名称不能为空");
			return false;
		}
		if($("#system_id").val() == ""){
			alert("应用编码不能为空");
			return false;
		}
		
		if($("#app_mode_type").val() == ""){
			alert("请选择应用类型");
			return false;
		}
		if($("#todo_url").val() == ""){
			alert("待办URL不能为空");
			return false;
		}
		if($("#wf_manage_url").val() == ""){
			alert("应用URL不能为空");
			return false;
		}
		return true;
	}	
	
	//增加
	function saveAppInfo() {
		$.dialog({
			id : 'add',
			title : '新增应用',
			width : 450,
			height : 320,
			content : 'url:' + "<%=request.getContextPath()%>/application/appTypeAdd.jsp"
		});
	}
	
	//修改
	function updateAppInfo() {
		
		var appInfoId = "";
		var appInfoRadio = $('input[name="appInfoRadio"]:checked');
		if(appInfoRadio != null && appInfoRadio.length > 0){
			appInfoId = $('input[name="appInfoRadio"]:checked').val();
		}else{
			alert("请先选择一条记录修改");
			return;
		}
		$.dialog({
			id : 'update',
			title : '修改应用',
			width : 450,
			height : 320,
			content : 'url:' + "<%=request.getContextPath()%>/application/updateAppInfo.page?appInfoId="+appInfoId
		});
	}
	
	//查看
	function viewAppInfo() {
		
		var appInfoId = "";
		var appInfoRadio = $('input[name="appInfoRadio"]:checked');
		if(appInfoRadio != null && appInfoRadio.length > 0){
			appInfoId = $('input[name="appInfoRadio"]:checked').val();
		}else{
			alert("请先选择一条记录查看");
			return;
		}
		$.dialog({
			id : 'update',
			title : '查看应用',
			width : 450,
			height : 320,
			content : 'url:' + "<%=request.getContextPath()%>/application/viewAppInfo.page?appInfoId="+appInfoId
		});
	}
	
	//删除
	function deleteAppInfo() {
		var appInfoId = "";
		var appInfoRadio = $('input[name="appInfoRadio"]:checked');
		if(appInfoRadio != null && appInfoRadio.length > 0){
			appInfoId = $('input[name="appInfoRadio"]:checked').val();
		}else{
			alert("请先选择一条记录修改");
			return;
		}
		$.ajax({
			type : "POST",
			url : "deleteAppInfo.page",
			data : {"appInfoId":appInfoId},
			dataType : 'json',
			async : false,
			beforeSend : function(XMLHttpRequest) {
			
				blockUI();
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(responseText) {
				//去掉遮罩
				unblockUI();
				
				if (responseText == "success") {
					$.dialog.alert("删除成功", function() {
						queryList();
					}, frameElement.api);
				} else {
					$.dialog.alert(responseText, function() {
					}, frameElement.api);
				}
			}
		});
	}
	
	//上传应用图片
	function uploadPic() {
		var appName = "";
		var appInfoId = "";
		var appInfoRadio = $('input[name="appInfoRadio"]:checked');
		if(appInfoRadio != null && appInfoRadio.length > 0){
			appInfoId = $('input[name="appInfoRadio"]:checked').val();
			appName = document.getElementById(appInfoId).childNodes[2].innerHTML;
		}else{
			alert("请先选择一条记录查看");
			return;
		}
		$.dialog({
			id : 'uploadPic',
			title : appName+'上传应用图片',
			width : 450,
			height : 320,
			content : 'url:' + "<%=request.getContextPath()%>/application/uploadPic.jsp?appInfoId="+appInfoId
		});
	}
	
	</SCRIPT>	
</head>
<body onload="pageInit();">
	<sany:menupath menuid="userquery"/>

	<div class="mcontent">
		<div id="searchblock">
			<div  class="search_top" >
	    		<div class="right_top"></div>
	    		<div class="left_top"></div>
     		</div>
     		<div class="search_box">
     			<form name="queryForm" id="queryForm" method="post" >
     				<table width="100%" border="0" cellspacing="0" cellpadding="0">
     					<tr>
	      					<td class="left_box"></td>
	      					<td>
	      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
	      							<tr>
	      								<th>应用名称：</th>
	      								<td><input type="text" name="system_name" id="system_name" class="w120" /></td>
	      								<th>应用编号：</th>
	      								<td><input type="text" name="system_id" id="system_id" class="w120" /></td>
	      								<th>应用类型：</th>
										<td><select id='app_mode_type' name="app_mode_type" required="false"
											style="width: 120px;">
											<option value=""></option>
											<option value="中央库应用">中央库应用</option>
						    				<option value="独立库应用">独立库应用</option>
						    				<option value="第三方应用">第三方应用</option>
											</select>										
										</td> 
	      								<td>
	      									<a href="javascript:void(0)" class="bt_1" id="search" onclick="queryList();"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      							<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="cleanAll();"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
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
			<a href="#" class="bt_small" id="addButton" onclick="javascript:saveAppInfo();"><span>新增</span></a>
			<a href="#" class="bt_small" id="viewButton" onclick="javascript:viewAppInfo();"><span>查看</span></a>
			<a href="#" class="bt_small" id="updateButton" onclick="javascript:updateAppInfo();"><span>修改</span></a>
			<a href="#" class="bt_small" id="delBatchButton" onclick="javascript:deleteAppInfo()"><span>删除</span></a>
			<a href="#" class="bt_small" id="uploadPicButton" onclick="javascript:uploadPic()"><span>上传应用图片</span></a>
			</div>
			<strong>应用列表</strong>
		</div>
		<div id="wfAppContentPage" style="overflow: auto;"></div>
	</div>

</body>
</html>
