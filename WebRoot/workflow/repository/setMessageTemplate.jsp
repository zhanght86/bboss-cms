<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：设置消息模板
作者：谭湘
版本：1.0
日期：2014-06-23
 --%>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>设置消息模板</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">


</script>
</head>
	
<body>
<div class="form">
	<form id="setTemplateForm" name="setTemplateForm" method="post" >
		
		<table border="0" cellpadding="0" cellspacing="0" class="table2" width="100%" >
				<input type="hidden" id="noticeId" value="${templateMap.NOTICEID}" />
				<input type="hidden" id="processkey" value="${processKey}" />
			<tr >
				<td align="center">
				
					<input type="hidden" id="messagetempleid" name="messagetempleid" value="${templateMap.MESSAGETEMPLEID}" />
					
					<input type="text" id="messagetitle" name="messagetitle" value="${templateMap.MESSAGETEMPLETITLE }" class="input1 w150" readonly/>
					
					<a href="javascript:openMessTemple('${templateMap.MESSAGETEMPLEID }','0')">短信</a>
				</td>
			</tr>
			<tr>
				<td align="center">
					<input type="hidden" id="emailtempleid" name="EMAILTEMPLEID" value="${templateMap.EMAILTEMPLEID}" />
					
					<input type="text" id="eamiltitle" name="eamiltitle" value="${templateMap.EMAILTEMPLETITLE}" class="input1 w150" readonly/>
					
					<a href="javascript:openMessTemple('${templateMap.EMAILTEMPLEID}','1')">邮件</a>
				</td>
			</tr>
		</table>			
		
		<div class="btnarea" >
			<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span><pg:message code="sany.pdp.common.ok"/></span></a>
			<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.close"/></span></a>
			<input type="reset" id="reset" style="display: none;" />
		</div>
	</form>
</div>
</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;

function openMessTemple(templeId,templeType) {
	var title="";
	if (templeType == '0') {
		title = "短信模板";
	}else{
		title = "邮件模板";
	}
	var url = "<%=request.getContextPath()%>/workflow/taskConfig/chooseMessageTemple.jsp?templeType="+templeType+"&templeId="+templeId;
	$.dialog({ id:'templateIframe', title:title,width:900,height:500, content:'url:'+url});
}

function dosubmit(){
	
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/repository/saveMessageTemplate.page",
		data :{"messagetempleid":$("#messagetempleid").val(),"processKey":$("#processkey").val(),
			"emailtempleid":$("#emailtempleid").val(),"noticeId":$("#noticeId").val()},
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			XMLHttpRequest.setRequestHeader("RequestType", "ajax");
		},
		success : function(data){
			if(data=="success"){
				api.close();
	 			W.modifyQueryData();
			}else{
				$.dialog.alert("流程消息提醒设置出错："+data,function(){},api);
			}
		}	
	 });
}

$(document).ready(function() {
	
});
	
</script>
</html>
