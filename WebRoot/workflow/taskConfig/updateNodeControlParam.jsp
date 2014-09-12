<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改节点控制参数信息</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">


</script>
</head>
	
<body>
<div class="form">
	<form id="updateNodeFrom" name="updateNodeFrom" method="post" >
		<pg:beaninfo requestKey="nodeControlParam">
			<table border="0" cellpadding="0" cellspacing="0" class="table2" width="100%" >
			
				<input type="hidden" id="NODE_KEY" name="NODE_KEY" value="<pg:cell colName="NODE_KEY"/>"/>
				<input type="hidden" id="NODE_ID" name="NODE_ID" value="<pg:cell colName="NODE_ID"/>"/>
				<input type="hidden" id="ID" name="ID" value="<pg:cell colName="ID"/>"/>
				<input type="hidden" id="business_id" name="business_id" value="${business_id }"/>
				<input type="hidden" id="business_type" name="business_type" value="${business_type }"/>
				<input type="hidden" id="process_key" name="process_key" value="${process_key }"/>
				
				<tr >
					<th width="100px" >节点KEY：</th>
					<td width="400px"><pg:cell colName="NODE_KEY"/></td>
				</tr>
				
				<tr >
					<th width="100px" >节点名称：</th>
					<td width="400px"><pg:cell colName="NODE_NAME"/></td>
				</tr>
				
				<tr>
					<th width="100px" >处理工时：</th>
					<td width="400px">
					<input type="text" value="<pg:cell colName="DURATION_NODE"/>"  id="DURATION_NODE"
					name="DURATION_NODE" style="width: 50px;" onkeyup="chkPrice(this);" 
					onblur="chkLast(this)" onpaste="javascript: return false;"/> (小时)
					</td>
				</tr>
				
				<tr >
					<th width="100px" >节点描述：</th>
					<td width="400px"><textarea rows="8" cols="50" id="NODE_DESCRIBE" name="NODE_DESCRIBE"  
							class="w120" style="width: 280px;font-size: 12px;height:40px;" maxlength="200"><pg:cell colName="NODE_DESCRIBE"/></textarea></td>
				</tr>
				
				<tr >
					<th width="100px" >业务控制类：</th>
					<td width="400px">
					<input type="text" id="BUSSINESSCONTROLCLASS" name="BUSSINESSCONTROLCLASS" value="<pg:cell colName="BUSSINESSCONTROLCLASS"/>" style="width: 280px;"/>
					</td>
				</tr>
				
				<tr >
					<th width="100px" >待办URL：</th>
					<td width="400px">
					<input type="text" id="TASK_URL" name="TASK_URL" value="<pg:cell colName="TASK_URL"/>" style="width: 280px;"/>
					</td>
				</tr>
				
				<tr>
					<th width="100px" >控制变量：</th>
					<td width="400px">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>
								<pg:notempty colName="IS_VALID">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_VALID" id="IS_VALID" value="1" <pg:equal colName="IS_VALID" value="1">checked </pg:equal>/>是否有效 
								</pg:notempty>
								<pg:empty colName="IS_VALID">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_VALID" id="IS_VALID" value="1"/>是否有效 
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_EDIT">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDIT" id="IS_EDIT" value="1" <pg:equal colName="IS_EDIT" value="1">checked </pg:equal>/>可修改&nbsp;(当前节点的信息"选择处理人","串/并行切换"等是否能被修改)
								</pg:notempty>
								<pg:empty colName="IS_EDIT">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDIT" id="IS_EDIT" value="1" />可修改&nbsp;(当前节点的信息"选择处理人","串/并行切换"等是否能被修改)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_EDITAFTER">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDITAFTER" id="IS_EDITAFTER" value="1" <pg:equal colName="IS_EDITAFTER" value="1">checked </pg:equal>/>可修改后续节点&nbsp;(当前节点是否有修改后续节点的权限)
								</pg:notempty>
								<pg:empty colName="IS_EDITAFTER">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_EDITAFTER" id="IS_EDITAFTER" value="1" />可修改后续节点&nbsp;(当前节点是否有修改后续节点的权限)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_AUTO">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTO" id="IS_AUTO_BOX" value="1" <pg:equal colName="IS_AUTO" value="1">checked </pg:equal>/>自动审批&nbsp;(当前节点自动通过,不用判断前后节点审批人是否一致)
								</pg:notempty>
								<pg:empty colName="IS_AUTO">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTO" id="IS_AUTO_BOX" value="1" />自动审批&nbsp;(当前节点自动通过,不用判断前后节点审批人是否一致)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_AUTOAFTER">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTOAFTER" id="IS_AUTOAFTER" value="1" <pg:equal colName="IS_AUTOAFTER" value="1">checked </pg:equal>/>后续节点自动审批&nbsp;(当前节点自动通过,条件是前后节点审批人必须一致)
								</pg:notempty>
								<pg:empty colName="IS_AUTOAFTER">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_AUTOAFTER" id="IS_AUTOAFTER" value="1" />后续节点自动审批&nbsp;(当前节点自动通过,条件是前后节点审批人必须一致)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_RECALL">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_RECALL" id="IS_RECALL" value="1" <pg:equal colName="IS_RECALL" value="1">checked </pg:equal>/>可被撤回&nbsp;(当前节点是否能被撤回的权限)
								</pg:notempty>
								<pg:empty colName="IS_RECALL">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_RECALL" id="IS_RECALL" value="1" />可被撤回&nbsp;(当前节点是否能被撤回的权限)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_CANCEL">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_CANCEL" id="IS_CANCEL" value="1" <pg:equal colName="IS_CANCEL" value="1">checked </pg:equal>/>可驳回&nbsp;(当前节点是否有驳回的权限)
								</pg:notempty>
								<pg:empty colName="IS_CANCEL">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_CANCEL" id="IS_CANCEL" value="1" />可驳回&nbsp;(当前节点是否有驳回的权限)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_DISCARD">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_DISCARD" id="IS_DISCARD" value="1" <pg:equal colName="IS_DISCARD" value="1">checked </pg:equal>/>可废弃&nbsp;(当前节点是否有废弃的权限)
								</pg:notempty>
								<pg:empty colName="IS_DISCARD">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_DISCARD" id="IS_DISCARD" value="1" />可废弃&nbsp;(当前节点是否有废弃的权限)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_COPY">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_COPY" id="IS_COPY" value="1" <pg:equal colName="IS_COPY" value="1">checked </pg:equal>/>可抄送&nbsp;(当前节点是否为抄送)
								</pg:notempty>
								<pg:empty colName="IS_COPY">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_COPY" id="IS_COPY" value="1" />可抄送&nbsp;(当前节点是否为抄送)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_MULTI">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_MULTI" id="IS_MULTI" value="1" 
									<pg:equal colName="IS_MULTI" value="1">checked </pg:equal>
									<pg:equal colName="IS_MULTI_DEFAULT" value="1">checked disabled </pg:equal>
									/>多实例&nbsp;(多实例/单实例切换)
								</pg:notempty>
								<pg:empty colName="IS_MULTI">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_MULTI" id="IS_MULTI" value="1" />多实例&nbsp;(多实例/单实例切换)
								</pg:empty>
								</td>
							</tr>
							<tr>
								<td>
								<pg:notempty colName="IS_SEQUENTIAL">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_SEQUENTIAL" id="IS_SEQUENTIAL" value="1" <pg:equal colName="IS_SEQUENTIAL" value="1">checked </pg:equal>/>串行&nbsp;(多实例串/并行切换)
								</pg:notempty>
								<pg:empty colName="IS_SEQUENTIAL">
									<input type="checkbox" name="<pg:cell colName='NODE_KEY'/>_IS_SEQUENTIAL" id="IS_SEQUENTIAL" value="1" />串行&nbsp;(多实例串/并行切换)
								</pg:empty>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				
			</table>			
		</pg:beaninfo>
		
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

function dosubmit(){
	 
	$.dialog.confirm('确定设置信息吗？', function(){
		
		$.ajax({
	 	 	type: "POST",
			url : "<%=request.getContextPath()%>/workflow/config/submitNodeContralParam.page",
			data: formToJson("#updateNodeFrom"),
			dataType : 'json',
			async:false,
			beforeSend: function(XMLHttpRequest){
				XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
			success : function(data){
				if(data=="success"){
					api.close();
					// 模板配置的显示
					W.$("#DURATION_NODE"+$("#NODE_KEY").val()).text($("#DURATION_NODE").val());
					W.$("#NODE_DESCRIBE"+$("#NODE_KEY").val()).text($("#NODE_DESCRIBE").val());
					W.$("#BUSSINESSCONTROLCLASS"+$("#NODE_KEY").val()).text($("#BUSSINESSCONTROLCLASS").val());
					W.$("#TASK_URL"+$("#NODE_KEY").val()).text($("#TASK_URL").val());
				}else{
					$.dialog.alert("修改节点控制参数出错"+data,function(){},api);
				}
			}	
		 });
	},function(){}); 
}

</script>
</html>
