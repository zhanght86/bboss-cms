<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
	描述：修改DEMO信息设置
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>修改Demo</title>
		<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
		
	</head>
	<body>
		<div class="form_box">
			<form id="editForm" name="editForm" method="post">
			<pg:beaninfo requestKey="demo">
			<fieldset>
				<legend>修改Demo信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=85px >
							名称：
						</th>
						<td width=140px>
							<input type="hidden" name="id" id="id" value="<pg:cell colName="id"/>"/>
							<input id="name" name="name" type="text" value="<pg:cell colName="name"/>"
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="10" /><font color="red">*</font>
						</td>
						
	
						</tr>
						
				
				</table>
			</fieldset>
			</pg:beaninfo>
			<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span>修改</span></a>
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span></a>
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span>退出</span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
	</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
   function closeDlg()
   {
	   api.close();
   }
   function dosubmit()
   {
		
		$.ajax({
		   type: "POST",
			url : "updateDemo.page",
			data :formToJson("#editForm"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
					var validated = $("#editForm").form('validate');
			      	if (validated){
			      		blockUI();	
			      		XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			      	}
			      	else
			      	{			      		
			      		return false;
			      	}				 	
				},
			success : function(responseText){
				//去掉遮罩
				unblockUI();
				if(responseText=="success"){
					$.dialog.alert("修改记录成功",function(){	
							W.modifyQueryData();
							api.close();
					},api);													
				}else{
					$.dialog.alert(responseText,function(){},api);
				}
			}
		  });
   	 }
function doreset(){
	$("#reset").click();
}
</script>