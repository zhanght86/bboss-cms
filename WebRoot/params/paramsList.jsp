<%--
	描述：参数管理
	作者：gw_hel
	版本：1.0
	日期：2012-08-07
--%>
<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>参数设置</title>
<script language="javascript">
	//增加一行
	function addRow() {
		 var tr = $("#tb1 tr").eq(0).clone();
		
		 tr.show();
         tr.appendTo("#tb");
	}
	
	//删除被指定的行
	function deleteRow() {
		var codes="";
		
    	$("#tb tr:gt(0)").each(function() {
		 
    	if ($(this).find("#CK").get(0).checked == true) {
    			$(this).remove();
    		} 
   		 });
	    
	}
	
	function doSubmit() {
		
	
		$("#paramForm").form('submit', {
		    "url": "saveParams.page",
		    onSubmit:function(){		
		    },
		    success:function(responseText){	
		    	if(responseText=="success"){
					$.dialog.alert("保存参数成功");													
				}else{
					$.dialog.alert("保存出错");
				}
		    }
		});	
	}
</script>
</head>
<body>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="right">
					<a href="#" onclick="addRow()" class="bt_2" ><span>+</span></a> 
					<a href="#" onclick="deleteRow()" class="bt_2" ><span>-</span></a>
					<a href="#" onclick="doSubmit()" class="bt_1" ><span>保存</span></a>
				</td>
			</tr>
		</table>
		 <table width="100%" border="0" cellpadding="0" cellspacing="0"  id="tb1">
		 	<tr style="display:none">
		   		    <td class="td_center"><input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')"/></td>
		        	<td><input id="name" name="name" type="text"  class="W120" style="width:100%"/></td>  
		        	<td><input id="value" name="value" type="text" class="W120" style="width:100%" /></td>
		        	<input id="paramid" name="paramid" type="hidden" value="${paramId}"/>
		        	<input id="param_type" name="param_type" type="hidden" value="${paramType}"/>  
		        </tr>
		  </table>
		  
		   <form id="paramForm" name="paramForm">
			<input id="handler" name="handler" type="hidden" value="${handler}"/>
			<input id="nodeId" name="nodeId" type="hidden" value="${paramId}"/>
			<input id="nodeType" name="nodeType" type="hidden" value="${paramType}"/>
			<div id="changeColor">
			 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
		        <tr>
		            <th align=center width="40px"><input id="CKA" name="CKA" type="checkbox" 
									onClick="checkAll('CKA','CK')"></th>
		       		<th>参数名称</th>
		       		<th>参数值</th>
		       		<input id="rn" name="rn" type="hidden" value="0" />
		        	<input id="paramid" name="paramid" type="hidden" value="${paramId}"/> 
		        	<input id="param_type" name="param_type" type="hidden" value="${paramType}"/>
		       	</tr>	
		       	
		
		      <pg:list requestKey="paramsList">
		   		<tr>
		   		    <td class="td_center"><input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="paramid" />"/></td>
		        	<td><input id="name" name="name" type="text" value="<pg:cell colName='name' />" class="W120" style="width:100%"/></td>  
		        	<td><input id="value" name="value" type="text" value="<pg:cell colName='value' />" class="W120" style="width:100%" /></td> 
		        	<input id="rn" name="rn" type="hidden" value="<pg:cell colName='rn' />" />
		        	<input id="paramid" name="paramid" type="hidden" value="<pg:cell colName='paramid' />"/> 
		        	<input id="param_type" name="param_type" type="hidden" value="<pg:cell colName='param_type' />"/>
		        </tr>
			 </pg:list>
		    </table>
		    </div>
		    </form>
</body>
<html>

