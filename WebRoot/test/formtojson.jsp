<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">




<html>
<head>
<%@ include file="/common/jsp/css.jsp"%>


<SCRIPT LANGUAGE="JavaScript">	

$(document).ready(function() {
	
   });
   
function doCandidateSubmit(){
	var obj = formToJson("#submitForm");
	//var str=obj.toJSONString();
	var str = JSON.stringify(obj) 

	$("#aa").val(str);	 

	$.ajax({

		url: "/smc-desktop/workflow/config/test3.page",
		
		type: "post",
		
		data: formToJson("#submitForm1"),			
		datatype:"json",			
		success: function(data){
		    alert(data);
		  }
		
		});
}

</SCRIPT>
	</head>
	<body class="easyui-layout">
	<div region="north" split="true" title="组织结构"
		style="height: 150px; padding1: 1px; overflow: hidden;">
		<div id="contentborder" align="center">
			<form name="submitForm1" id="submitForm1" method="post">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="stable" id="tb">
					

					<tr 
							class="labeltable_middle_td"
							>
							<td height='20' align=left class="tablecells"><input id="aa" name="aa" /></td>
							
						</tr>
						
					
				</table>
			</form>
			<form name="submitForm" id="submitForm" method="post">
				

				
				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					class="stable" id="tb">
					

					<tr 
							class="labeltable_middle_td"
							>
							<td height='20' align=left class="tablecells"><input id="a" name="a" value=""/></td>
							<td height='20' align=left class="tablecells"><input id="b" name="b" value=""/></td>
							<td height='20' align=left class="tablecells"><input id="c" name="c" value=""/></td>
						</tr>
						<tr 
							class="labeltable_middle_td"
							>
							<td height='20' align=left class="tablecells"><input id="a" name="a" value=""/></td>
							<td height='20' align=left class="tablecells"><input id="b" name="b" value=""/></td>
							<td height='20' align=left class="tablecells"><input id="c" name="c" value=""/></td>
						</tr>
						<tr 
							class="labeltable_middle_td"
							>
							<td height='20' align=left class="tablecells"><input id="a" name="a" value=""/></td>
							<td height='20' align=left class="tablecells"><input id="b" name="b" value=""/></td>
							<td height='20' align=left class="tablecells"><input id="c" name="c" value=""/></td>
						</tr>
						
						<tr 
							class="labeltable_middle_td"
							>
							<td height='20' align=left class="tablecells"><input id="a" value="" type="button" onclick="doCandidateSubmit()" name="aaaa"/></td>
							
						</tr>
					
				</table>
				
			</form>
					</div>

	</div>

			
	</body>
</html>
