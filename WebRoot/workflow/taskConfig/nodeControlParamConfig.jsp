<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流流程配置-节点控制参数配置</title>
<%@ include file="/common/jsp/css.jsp"%>
<style type="text/css">
  .table6 td{
	border:0;  
  }
</style>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>

</head>
<body>
<form name="contralForm" id="contralForm" method="post">
			
	<input type="hidden" id="business_id" name="business_id" value="${business_id }"/>
	<input type="hidden" id="business_type" name="business_type" value="${business_type }"/>
	<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
	
	<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb2">
		<pg:header>
			<th>节点KEY</th>
			<th>节点名称</th>
			<th>处理工时<br/>(小时)</th>
			<th>节点描述</th>
			<th>待办URL</th>
			<th>业务处理类</th>
			<th>控制参数</th>
		</pg:header>
	
		<pg:list requestKey="contralParamList">
			<tr class="replaceTr">
				<td>
				<input type="hidden" id="NODE_ID" name="NODE_ID" value="<pg:cell colName="NODE_ID"/>"/>
				<input type="hidden" name="NODE_KEY" id="NODE_KEY" value="<pg:cell colName="NODE_KEY"/>"/><pg:cell colName="NODE_KEY"/>
				</td>
				<td >
					<pg:cell colName="NODE_NAME"/>
				</td>
				<td >
					<input type="text" name="DURATION_NODE" value="<pg:cell colName="DURATION_NODE"/>" class="input1 w50" onkeyup="chkPrice(this);" onblur="chkLast(this)" onpaste="javascript: return false;"/>
				</td>
				<td>
					<textarea rows="4" cols="80" id="NODE_DESCRIBE" name="NODE_DESCRIBE"
					 style="width: 200px;font-size: 12px;height:50px;" maxlength="200"><pg:cell colName="NODE_DESCRIBE"/></textarea>
				</td>
				<td>
					<textarea rows="4" cols="80" id="TASK_URL" name="TASK_URL"
					 style="width: 200px;font-size: 12px;height:50px;" maxlength="200"><pg:cell colName="TASK_URL"/></textarea>
				</td>
				<td>
					<textarea rows="4" cols="80" id="BUSSINESSCONTROLCLASS" name="BUSSINESSCONTROLCLASS" maxlength="200"
					style="width: 200px;font-size: 12px;height:50px;" ><pg:cell colName="BUSSINESSCONTROLCLASS"/></textarea>
				</td>
				<td>
					<table border="0" cellpadding="0" cellspacing="0" class="table6" >
						<tr>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_VALID" id="IS_VALID" value="1" <pg:equal colName="IS_VALID" value="1">checked</pg:equal>/>是否有效</td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDIT" id="IS_EDIT" value="1" <pg:equal colName="IS_EDIT" value="1">checked</pg:equal>/>可修改 </td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_CANCEL" id="IS_CANCEL" value="1" <pg:equal colName="IS_CANCEL" value="1">checked</pg:equal>/>可驳回</td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDITAFTER" id="IS_EDITAFTER" value="1" <pg:equal colName="IS_EDITAFTER" value="1">checked</pg:equal>/>可修改后续节点</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_RECALL" id="IS_RECALL" value="1" <pg:equal colName="IS_RECALL" value="1">checked</pg:equal>/>可被撤回 </td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_COPY" id="IS_COPY" value="1" <pg:equal colName="IS_COPY" value="1">checked</pg:equal>/>可抄送</td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_DISCARD" id="IS_DISCARD" value="1" <pg:equal colName="IS_DISCARD" value="1">checked</pg:equal>/>可废弃</td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTOAFTER" id="IS_AUTOAFTER" value="1" <pg:equal colName="IS_AUTOAFTER" value="1">checked</pg:equal>/>后续节点自动审批</td>
						</tr>
						<tr>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_DISCARDED" id="IS_DISCARDED" value="1" <pg:equal colName="IS_DISCARDED" value="1">checked</pg:equal>/>可被废弃 </td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_MULTI" id="IS_MULTI" value="1" 
								<pg:equal colName="IS_MULTI" value="1">checked</pg:equal> 
								<pg:equal colName="IS_MULTI_DEFAULT" value="1">checked disabled </pg:equal>
								/>多实例
							</td>
							<td><input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_SEQUENTIAL" id="IS_SEQUENTIAL" value="1" 
								<pg:equal colName="IS_SEQUENTIAL" value="1">checked</pg:equal> />串行
							</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					
				</td>
			</tr>
		</pg:list>
	</table>
	
	<div class="btnarea">
		<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="doContralFormSubmit()"><span>确定</span></a> 
	</div>
		
</form>

</body>
</html>

<script language="javascript">

$(document).ready(function() {
	
});

function doContralFormSubmit(){
		
	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/config/submitNodeContralParam.page",
		data: formToJson("#contralForm"),
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			XMLHttpRequest.setRequestHeader("RequestType", "ajax");
		},
		success : function(data){
			if(data=="success"){
				$.dialog.alert("节点控制参数保存成功",function(){});
			}else{
				$.dialog.alert("保存节点控制参数出错"+data,function(){});
			}
		}	
	 });
}

function chkPrice(obj){
	obj.value = obj.value.replace(/[^\d.]/g,""); 
	//必须保证第一位为数字而不是. 
	obj.value = obj.value.replace(/^\./g,""); 
	//保证只有出现一个.而没有多个. 
	obj.value = obj.value.replace(/\.{2,}/g,"."); 
	//小数点后面保留一位小数
	obj.value = obj.value.replace(/\.\d\d/g,"."); 
	//保证.只出现一次，而不能出现两次以上 
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$","."); 
} 

function chkLast(obj){ 
	// 如果出现非法字符就截取掉 
	if(obj.value.substr((obj.value.length - 1), 1) == '.') {
		obj.value = obj.value.substr(0,(obj.value.length - 1)); 
	}
}

function updateNodeControlParam(nodekey){
	 var url="<%=request.getContextPath()%>/workflow/config/toUpdateNodeControlParam.page?"
			 +"process_key=${process_key}&business_id=${business_id}&business_type=${business_type}"
			 +"&taskKey="+nodekey+"&pageType=fromTemplet";
	$.dialog({ id:'iframeNewId', title:'设置节点控制参数信息',width:580,height:600, content:'url:'+url});  
}

</script>

