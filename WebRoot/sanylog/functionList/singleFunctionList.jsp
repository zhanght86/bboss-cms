<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html2/js/validate_class.js"></script>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
   $(document).ready(function() {
	      if("${msg}"!=""){
	    	$.dialog.alert("${msg}");
	    }
	       
   });    
   
   function CloseDlg(){
	   api.close();
   }
   function confirm() {	
	   var type = "${param.type}";
	   if("modify"==type){
		   var id = $("#id").val();
		   var estimateOper = $("#estimateOper").val();
		   var estimateUser = $("#estimateUser").val();
		   $.ajax({
		 	 	type: "POST",
				url : "functionListSingleModify.page",
				data :{id:id,estimateOper:estimateOper,estimateUser:estimateUser},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if (data == "修改成功") {
						W.$.dialog.alert(data, function() {
							W.queryList();
							W.queryAppInfo();
							api.close();
						}, api);
					} else {
						W.$.dialog.alert(data, function() {
						}, api);
					}
				}	
			 });
	   }else{
		   var appName = $("#appName").val();
		   var functionName = $("#functionName").val();
		   var functionCode = $("#functionCode").val();
		   var estimateOper = $("#estimateOper").val();
		   var estimateUser = $("#estimateUser").val();
		   $.ajax({
		 	 	type: "POST",
				url : "functionListSingleIncrement.page",
				data :{appName:appName,functionName:functionName,functionCode:functionCode,estimateOper:estimateOper,estimateUser:estimateUser},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					if (data == "新增成功") {
						W.$.dialog.alert(data, function() {
							W.queryList();
							W.queryAppInfo();
							api.close();
						}, api);
					} else {
						W.$.dialog.alert(data, function() {
						}, api);
					}
				}	
			 });
	   }
		
	   	
	}
</script>
</head>
	<body>
		<form  method="post" id="glform" name="glform" >
			<div class="form_box">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        

      <pg:list autosort="false" requestKey="datas">
           <tr style="display: none;">
   		   <td>id</td>
   		        <td><input type="text" id="id" value="<pg:cell colName="id"/>"></td>
   		   </tr>
   		   <tr>
   		   <td>应用名称</td>
   		        <%-- <td><input type="text" id="appName" class="required" value="<pg:cell colName="appName"/>">&nbsp;&nbsp;<font color="red">*</font></td> --%>
   		        <td><pg:cell colName="appName"/></td>
   		   </tr>
   		   <tr>     
   		   <td>功能名称</td>
                <td><input type="text" id="functionName" class="required"  value="<pg:cell colName="functionName"/>">&nbsp;&nbsp;<font color="red">*</font></td>
                </tr>
   		   <tr> 
   		   <td>功能编号</td>
                <td><input type="text" id="functionCode" class="required"  value="<pg:cell colName="functionCode"/>">&nbsp;&nbsp;<font color="red">*</font></td>  
                </tr>
   		   <tr> 
   		   <td>预计使用频次(次/月)</td>
        		<td><input type="text" id="estimateOper" class="posInt" value="<pg:cell colName="estimateOper" />"></td>  
        		</tr>
   		   <tr> 
   		   <td>预计使用人数(次/月)</td>
        		<td><input type="text" id="estimateUser" class="posInt" value="<pg:cell colName="estimateUser" />"></td>  
        		</tr>
   		   <tr> 
   		   <td>耗费工时(小时)</td>
        		<td><pg:cell colName="timeSpent" /></td>  
        		
          </tr>
	 </pg:list>
					
					</table>		

			</div>
			<tr><td>
					<a href="javascript:void" class="bt_2"  onclick="confirm()"><span>保存</span>
					<a href="javascript:void" class="bt_2"  onclick="CloseDlg()"><span>取消</span></a>
					</td></tr>
		</form>
		
	</body>
</html>
