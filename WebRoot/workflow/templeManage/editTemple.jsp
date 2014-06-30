<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：维护模板
作者：谭湘
版本：1.0
日期：2014-06-10
 --%>	

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>维护模板</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/include/ckeditor/ckeditor.js"></script>

</head>
	
<body>
<div class="form">
	<pg:beaninfo requestKey="templeBean">
	<form id="editTempleFrom" name="editTempleFrom" method="post" >
		<input type="hidden" name="templeId" id="templeId" value="<pg:cell colName="templeId" />"/>
		
		<table border="1" cellpadding="0" cellspacing="0" class="table2" width="100%" >
			<tr >
				<th width="100px"><strong>模板标题：</strong></th>
				<td width="600px" colspan="2">
					<input type="text" name="templeTitle" id="templeTitle" value="<pg:cell colName="templeTitle" />"/>
				</td>
			</tr>
			<tr>
				<th width="100px"><strong>模板类型：</strong></th>
				<td width="600px" colspan="2">
					<select id="templeType" name="templeType" class="select1" onchange="showTr(this.value)">
						<option value="0" <pg:equal colName="templeType" value="0"> selected </pg:equal>>短信</option>
						<option value="1" <pg:equal colName="templeType" value="1"> selected </pg:equal>>邮件</option>
					</select>
				</td>
			</tr>
			
			<tr class="messtr">
				<th width="100px" rowspan="2" ><strong>模板内容：</strong></th>
				<td width="700px" rowspan="2" >
					<textarea rows="8" cols="50" id="messtempleContent" name="messtempleContent" 
					style="width: 700px;font-size: 12px;height:400px;" 
					><pg:cell colName="templeContent" /></textarea>
				</td>	
				<td width="200px" align="center">	
					<strong>模板字段 </strong>
				</td>
			</tr>
			<tr class="messtr">
				<td >	
					<select id="" name="" class="select1" size="24" style="width: 100%; " ondblclick="javaScript:insertAtCursor(document.getElementById('messtempleContent'),this.value);">
				 		<pg:list actual="${paramsList}">
							<option value="<pg:cell colName='value'/>" ><pg:cell colName="name" /> <pg:cell colName="value" /></option>
				 		</pg:list>
					</select>
				</td>
			</tr>
			
			<tr class="emailtr" style="display:none;">
				<th width="100px" rowspan="2" ><strong>模板内容：</strong></th>
				<td width="700px" rowspan="2" >
					<textarea rows="8" cols="50" id="emailtempleContent" name="emailtempleContent" 
					><pg:cell colName="templeContent" /></textarea>
				</td>	
				<td width="200px" align="center">	
					<strong>模板字段 </strong>
				</td>
			</tr>
			<tr class="emailtr" style="display:none;">
				<td >	
					<select id="" name="" class="select1" size="24" style="width: 100%; " ondblclick="javaScript:insertAtCursorForCkeditor(this.value);">
						<pg:list actual="${paramsList}">
							<option value="<pg:cell colName='value'/>" ><pg:cell colName="name" /> <pg:cell colName="value" /></option>
				 		</pg:list>
					</select>
				</td>
			</tr>
		</table>			
		
		<div class="btnarea" >
			<pg:equal actual="${state}" value="0">
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
			</pg:equal>
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
			<input type="reset" id="reset" style="display: none;" />
		</div>
	</form>
	</pg:beaninfo>
</div>
</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
var editor;

$(document).ready(function() {
	CKEDITOR.replace( 'emailtempleContent', { height:'300px',width:'700px' } );
	
	showTr('${templeBean.templeType}');
	
	CKEDITOR.on( 'instanceReady', function( ev ) {
		editor = ev.editor;
		// state = 0 (新增、修改) = 1 (明细查看)
		if ('${state}' == '1') {
			editor.setReadOnly(true);
		}else {
			editor.setReadOnly(false);
		}
	});
	
});

// ckeditor在光标后追加字符串
function insertAtCursorForCkeditor(myValue) {
	
	if ( editor.mode == 'wysiwyg' ){
		editor.insertText( '\$\{'+myValue+'\}' );
	}
	
} 

//js在光标后追加字符串
function insertAtCursor(myField, myValue) {
	var myValue = '\$\{'+myValue+'\}';
	//IE support
	if (document.selection) {
		myField.focus();
		sel = document.selection.createRange();
		sel.text = myValue;
		sel.select();
	}
	//MOZILLA/NETSCAPE support 
	else if (myField.selectionStart || myField.selectionStart == '0') {
		var startPos = myField.selectionStart;
		var endPos = myField.selectionEnd;
		// save scrollTop before insert www.keleyi.com
		var restoreTop = myField.scrollTop;
		myField.value = myField.value.substring(0, startPos) + myValue + myField.value.substring(endPos, myField.value.length);
		
		if (restoreTop > 0) {
			myField.scrollTop = restoreTop;
		}
		
		myField.focus();
		myField.selectionStart = startPos + myValue.length;
		myField.selectionEnd = startPos + myValue.length;
		
	} else {
		myField.value += myValue;
		myField.focus();
	}
} 

function showTr(myValue) {
	//邮件模板
	if (myValue == '1'){
		$(".messtr").hide();
		$(".emailtr").show();
		
	}else if (myValue == '0'){//短信模板
		$(".messtr").show();
		$(".emailtr").hide();
		
	}
	
	// state = 0 (新增、修改) = 1 (明细查看)
	if ('${state}' == '1') {
		$(".select1").attr("disabled","disabled");
		$("#templeTitle").attr("disabled","disabled");
		$("#messtempleContent").attr("disabled","disabled");
	}
}

function dosubmit(){
	var templeTitle = $("#templeTitle").val();
	var templeType = $("#templeType").val();
	var templeContent = "";
	
	//邮件模板
	if (templeType == '1') {
		templeContent = editor.getData();
	}else {// 短信模板
		templeContent = $("#messtempleContent").text();
	}
	
	if ($.trim(templeTitle) == ""){
		alert("模板标题不能为空！");
		return;
	}	 
	
	if ($.trim(templeContent) == "") {
		alert("模板内容不能为空！");
		return;
	}
		
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/templeManage/saveTemple.page",
		data :{"templeTitle":templeTitle,"templeId":$("#templeId").val(),
			"templeContent":$.trim(templeContent),"templeType":templeType},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			XMLHttpRequest.setRequestHeader("RequestType", "ajax");
		},
		success : function(data){
			if(data=="success"){
				api.close();
	 			W.queryList();
			}else{
				$.dialog.alert("模板保存出错:"+data,function(){},api);
			}
		}	
	 });
}

</script>
</html>
