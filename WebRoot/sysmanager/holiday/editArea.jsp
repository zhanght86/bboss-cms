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

var areaId = '${param.areaId}';

   $(document).ready(function() {
	   querySingleArea();
   });  
   function querySingleArea(){
	   $.ajax({
	 	 	type: "POST",
			url : "querySingleArea.page",
			data :{areaId:areaId},
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data) {
					$("#areaId").val(data.areaId);
					$("#areaName").val(data.areaName);
					$("#creator").val(data.creator);
					$("#createTime").val(data.createTime);
					$("#areaDesc").html(data.areaDesc);
				} 
			}	
		 });
   }
   
   function CloseDlg(){
	   api.close();
   }
   function confirm() {	
	   var areaId = $("#areaId").val();
	   var areaName = $("#areaName").val();
	   var areaDesc = $('#areaDesc').html();
	    $.ajax({
	 	 	type: "POST",
			url : "updateArea.page",
			data :{areaId:areaId,areaName:areaName,areaDesc:areaDesc},
			dataType : '',
			async:false,
			beforeSend: function(XMLHttpRequest){
				 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
				},
			success : function(data){
				if (data == "success") {
					W.$.dialog.alert("修改成功", function() {
						W.queryList();
						api.close();
					}, api);
				} else {
					W.$.dialog.alert("修改失败", function() {
					}, api);
				}
			}	
		 }); 
	   	
	}
   
   
</script>
</head>
	<body>
		<form  method="post" id="glform" name="glform" ></form>
			<div class="form_box"></div>
			
		  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
	   		   <tr>
		   		   <td>区域编码</td>
		   		   <td><input type="text" id="areaId"  value='${param.areaId}' readonly="readonly"/></td>
	   		   </tr>
	   		   <tr>
		   		   <td>区域名称</td>
		   		   <td><input type="text" id="areaName"  value='${param.areaName}'/></td>
	   		   </tr>
	   		   <tr>
		   		   <td>创建人</td>
		   		   <td><input type="text" id="creator"  value='${param.creator}' readonly="readonly"/></td>
	   		   </tr>
	   		   <tr>
		   		   <td>创建时间</td>
		   		   <td><input type="text" id="createTime"  value='${param.createTime}' readonly="readonly"/></td>
	   		   </tr>
	   		   <tr>     
		   		   <td>区域描述</td>
		           <td><textarea rows="3" cols="20" id='areaDesc' >${param.areaDesc}</textarea></td>
	           </tr>
	           
			   
		</table>
			        <a href="#" class="bt_2"  onclick="confirm()"><span>保存</span></a>
					<a href="#" class="bt_2"  onclick="CloseDlg()"><span>取消</span></a>
	</body>
</html>
