<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：流程定义管理
	作者：尹标平
	版本：1.0
	日期：2012-03-23
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>委托代办管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
     
     function pageInit(){
    	 
    	 queryList();
     }
       
     function queryList(){	

    	 var entrust_user = $("#entrust_user").val();
		 var create_user = $("#create_user").val();
    	 $("#custombackContainer").load("entrustList.page #customContent", {entrust_user:entrust_user, create_user:create_user},function(){loadjs();});
	 }
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	 
	 //增加
	 function addEntrustInfo() {
		  $.dialog({
			  id : 'add',
			  title : '新增应用',
			  width : 550,
			  height : 420,
			  content : 'url:' + "<%=request.getContextPath()%>/workflow/entrust/entrustAdd.jsp"
		  });
	 }
		
	 //修改
	 function updateEntrustInfo() {
		
		 var entrustInfoId = "";
		 
		  var entrustInfoRadio = $('input[name="entrustInfoRadio"]:checked');
		  
		  if(entrustInfoRadio != null && entrustInfoRadio.length > 0){
			  
			  entrustInfoId = $('input[name="entrustInfoRadio"]:checked').val();
		  }else{
			  alert("请先选择一条记录查看");
			  
			  return;
		  }
		  $.dialog({
			  id : 'update',
			  title : '修改应用',
			  width : 550,
			  height : 420,
			  content : 'url:' + "<%=request.getContextPath()%>/workflow/entrust/entrustInfoModify.page?entrustInfoId=" + entrustInfoId
		  });
	 }
	 
	 function unUseEntrustInfo(entrustInfoId,sts){
		 
		 $.ajax({
			  type : "POST",
			  url : "unUseEntrustInfo.page",
			  data : {"entrustInfoId":entrustInfoId,"sts":sts},
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
					  $.dialog.alert("委托修改为"+sts, function() {
						  queryList();
					  }, frameElement.api);
				  } else {
					  $.dialog.alert(responseText, function() {
					  }, frameElement.api);
				  }
			  }
		  });
		 
	 }
		
	 //查看
	 function viewEntrustInfo() {
		
		 var entrustInfoId = "";
		 
		  var entrustInfoRadio = $('input[name="entrustInfoRadio"]:checked');
		  
		  if(entrustInfoRadio != null && entrustInfoRadio.length > 0){
			  
			  entrustInfoId = $('input[name="entrustInfoRadio"]:checked').val();
		  }else{
			  alert("请先选择一条记录查看");
			  
			  return;
		  }
		  $.dialog({
			  id : 'view',
			  title : '查看委托信息',
			  width : 550,
			  height : 420,
			  content : "url:" + "<%=request.getContextPath()%>/workflow/entrust/viewEntrustInfo.page?entrustInfoId=" + entrustInfoId
		  });
	 }
		
	 //删除
	 function deleteEntrustInfo() {
		  var entrustInfoId = "";
		  var entrustInfoRadio = $('input[name="entrustInfoRadio"]:checked');
		  if(entrustInfoRadio != null && entrustInfoRadio.length > 0){
			  entrustInfoId = $('input[name="entrustInfoRadio"]:checked').val();
		  }else{
			  alert("请先选择一条记录修改");
			  return;
		  }
		  $.ajax({
			  type : "POST",
			  url : "deleteEntrustInfo.page",
			  data : {"entrustInfoId":entrustInfoId},
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
</script>
</head>
	<body onload="pageInit();">
		<div class="mcontent">
			<sany:menupath menuid="workflowmanager" />
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
									<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
										<tr>
											<th>委托人：</th>
											<td><input id="entrust_user" name="entrust_user" type="text" class="w120" /></td>
											<th>
												归属人：
											</th>
											<td>
												<input id="create_user" name="create_user" type="text" class="w120" />
											</td>
											<th>&nbsp;</th>
											<td>
												&nbsp;
											</td>
											<th>
												&nbsp;
											</th>
											<td style="text-align:right">
												<a href="javascript:void(0)" class="bt_1" id="queryButton" onclick="queryList();"><span><pg:message code="sany.pdp.common.operation.search"/></span>
												</a>
												<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="cleanAll();"><span><pg:message code="sany.pdp.common.operation.reset"/></span>
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
				<a href="#" class="bt_small" id="addButton" onclick="javascript:addEntrustInfo();"><span>新增</span></a>
				<a href="#" class="bt_small" id="viewButton" onclick="javascript:viewEntrustInfo();"><span>查看</span></a>
				<a href="#" class="bt_small" id="updateButton" onclick="javascript:updateEntrustInfo();"><span>修改</span></a>
				<a href="#" class="bt_small" id="delBatchButton" onclick="javascript:deleteEntrustInfo()"><span>删除</span></a>
				</div>
				
				<strong>委托待办管理</strong>				
			</div>
			<div id="custombackContainer"  style="overflow:auto">
			
			</div>
		</div>
	</body>
</html>
