<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
	描述：流程授权管理
	版本：1.0
	日期：2014-05-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程授权管理</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
     
     function pageInit(){
    	 
    	 queryList();
    	 
    	 $("#queryForm").find("input[type=text]").each(function() {
    		 var thisVal=$(this).val();
    	     //判断文本框的值是否为空，有值的情况就隐藏提示语，没有值就显示
    	     if(thisVal!="" && thisVal!="输入域账号或者姓名"){
    	         $(this).removeAttr("style");
    	         $(this).attr("style","width:175px;");
    	      }else{
    	    	 $(this).val("输入域账号或者姓名");
    	    	 $(this).removeAttr("style");
      	       	 $(this).attr("style","width:175px;color:#BCBCBC;");
    	      }
    	     //聚焦型输入框验证 
    	     $(this).focus(function(){
    	    	 var val=$(this).val();
    	    	 if(val!="" && val == "输入域账号或者姓名"){
    	    		 $(this).val("");
        	         $(this).removeAttr("style");
        	         $(this).attr("style","width:175px;");
        	     }
    	      }).blur(function(){
    	        var val=$(this).val();
    	        if(val!=""){
    	             
    	        }else{
    	        	$(this).val("输入域账号或者姓名");
      	    	 	$(this).removeAttr("style");
         	       	$(this).attr("style","width:175px;color:#BCBCBC;");
    	        } 
    	      });

    	 });
     }
       
     function queryList(){	

    	 var entrust_user = $("#entrust_user").val() == "输入域账号或者姓名"? "" : $("#entrust_user").val();
		 var create_user = $("#create_user").val() == "输入域账号或者姓名" ? "" : $("#create_user").val();
		 var sts = $("#sts").val();
    	 $("#custombackContainer").load("entrustList.page #customContent", {entrust_user:entrust_user, create_user:create_user, sts:sts},function(){loadjs();});
	 }
	 function containSpecial( s ) {  
		  var containSpecial = RegExp(/[(\ )(\~)(\!)(\@)(\#)(\$)(\%)(\^)(\&)(\+)(\=)(\[)(\])(\{)(\})(\|)(\\)(\;)(\:)(\')(\")(\,)(\/)(\<)(\>)(\?)(\)]+/);      
	      return ( containSpecial.test(s) );      
	 }
	 
	 //增加
	 function addEntrustInfo() {
		  $.dialog({
			  id : 'add',
			  title : '新增流程授权',
			  width : 550,
			  height : 500,
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
			  $.dialog.alert("请先选择一条记录查看",function(){});
			  
			  return;
		  }
		  $.dialog({
			  id : 'update',
			  title : '流程授权修改',
			  width : 550,
			  height : 500,
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
					  $.dialog.alert("流程授权修改为"+sts, function() {
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
	 function viewEntrustInfo(entrustInfoId) {
		
		  if(entrustInfoId==null || entrustInfoId == ""){
			  
			  var entrustInfoRadio = $('input[name="entrustInfoRadio"]:checked');
			  
			  if(entrustInfoRadio != null && entrustInfoRadio.length > 0){
				  
				  entrustInfoId = $('input[name="entrustInfoRadio"]:checked').val();
			  }else{
				  $.dialog.alert("请先选择一条记录查看",function(){});
				  
				  return;
			  }
		  }
		 
		  $.dialog({
			  id : 'view',
			  title : '查看流程授权信息',
			  width : 550,
			  height : 500,
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
			  $.dialog.alert("请先选择一条记录修改",function(){});
			  return;
		  }
		  
		$.dialog.confirm('确定删除？', function(){
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
	     },function(){
	     		
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
											<th>授权给：</th>
											<td><input id="entrust_user" name="entrust_user" type="text" class="w120" style="width:175px;" /></td>
											<th>
												授权人：
											</th>
											<td>
												<input id="create_user" name="create_user" type="text" class="w120" style="width:175px;" 
													value="<%=AccessControl.getAccessControl().getUserName() %>" <%if(!AccessControl.getAccessControl().isAdmin()){ %>readonly<%} %> />
											</td>
											<th>状态:</th>
											<td>
												<select name="sts" id="sts" onchange="queryList();" style="width:120px;" >     
													<option value="有效" selected>有效</option>
													<option value="失效">失效</option>
													<option value="">全部</option>  
												</select> 
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
				<a href="javascript:void(0)" class="bt_small" id="addButton" onclick="javascript:addEntrustInfo();"><span>新增</span></a>
				<a href="javascript:void(0)" class="bt_small" id="viewButton" onclick="javascript:viewEntrustInfo('');"><span>查看</span></a>
				<a href="javascript:void(0)" class="bt_small" id="updateButton" onclick="javascript:updateEntrustInfo();"><span>修改</span></a>
				<a href="javascript:void(0)" class="bt_small" id="delBatchButton" onclick="javascript:deleteEntrustInfo()"><span>删除</span></a>
				</div>
				
				<strong>流程授权列表</strong>				
			</div>
			<div id="custombackContainer"  style="overflow:auto">
			
			</div>
		</div>
	</body>
</html>
