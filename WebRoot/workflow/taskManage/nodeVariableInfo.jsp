<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--流程参数table--%>
<table border="0" cellpadding="0" cellspacing="0" class="table3">
	<tr >
		<th rowspan="<pg:size actual='${instVariableList}' increament="1"/>" width="100"><strong>流程参数信息：</strong></th>
		<th width="150"><strong>参数名称</strong></th>
		<th width="100"><strong>参数类型</strong></th>
		<th width="100"><strong>BYTEARRAY_ID_</strong></th>
		<th width="100"><strong>DOUBLE_</strong></th>
		<th width="100"><strong>LONG_</strong></th>
		<th width="150"><strong>TEXT_</strong></th>
		<th width="100"><strong>TEXT2_</strong></th>
	</tr>
		
<pg:notempty actual="${instVariableList}" >
<pg:list actual="${instVariableList}"  >
	<tr >
		<td width="150"><pg:cell colName="NAME_"/></td>
		<td width="95"><pg:cell colName="TYPE_"/></td>
		<td width="100">
			<pg:notempty colName="BYTEARRAY_ID_" >
				<pg:cell colName="BYTEARRAY_ID_"/>
			</pg:notempty>
			<pg:empty colName="BYTEARRAY_ID_" >&nbsp;</pg:empty>
		</td>
		<td width="100">
			<pg:notempty colName="DOUBLE_" >
				<pg:cell colName="DOUBLE_"/>
			</pg:notempty>
			<pg:empty colName="DOUBLE_" >&nbsp;</pg:empty>
		</td>
		<td width="100">
			<pg:notempty colName="LONG_" >
				<pg:cell colName="LONG_"/>
			</pg:notempty>
			<pg:empty colName="LONG_" >&nbsp;</pg:empty>
		</td>
		<td width="150">
			<pg:notempty colName="TEXT_" >
				<pg:cell colName="TEXT_"/>
			</pg:notempty>
			<pg:empty colName="TEXT_" >&nbsp;</pg:empty>
		</td>
		<td width="150">
			<pg:notempty colName="TEXT2_" >
				<pg:cell colName="TEXT2_"/>
			</pg:notempty>
			<pg:empty colName="TEXT2_" >&nbsp;</pg:empty>
		</td>
	</tr>
</pg:list>
</pg:notempty>
</table>

<%--节点控制参数table--%>
<table border="0" cellpadding="0" cellspacing="0" class="table3">
	<tr >
		<th rowspan="<pg:size actual='${controlParamList}' increament="1"/>" width="100"><strong>节点控制参数信息：</strong></th>
		<th width="100"><strong>节点KEY</strong></th>
		<th width="160"><strong>节点描述</strong></th>
		<th width="300"><strong>待办URL</strong></th>
		<th width="500"><strong>控制参数</strong></th>
	</tr>
		
<pg:notempty actual="${controlParamList}" >
<pg:list actual="${controlParamList}"  >
	<tr >
		<td width="100"><pg:cell colName="NODE_KEY"/></td>
		<td width="160"><pg:cell colName="NODE_DESCRIBE"/></td>
		<td width="300"><pg:cell colName="TASK_URL"/></td>
		<td width="500">
			<input type="checkbox" disabled <pg:equal colName="IS_VALID" value="1">checked </pg:equal> />是否有效 
			
			<input type="checkbox" disabled <pg:equal colName="IS_EDIT" value="1">checked </pg:equal> />可修改
			
			<input type="checkbox" disabled <pg:equal colName="IS_EDITAFTER" value="1">checked </pg:equal>/>可修改后续节点
			
			<input type="checkbox" disabled <pg:equal colName="IS_AUTO" value="1">checked </pg:equal>/>自动审批
			
			<input type="checkbox" disabled <pg:equal colName="IS_AUTOAFTER" value="1">checked </pg:equal>/>后续节点自动审批
			
			<input type="checkbox" disabled <pg:equal colName="IS_RECALL" value="1">checked </pg:equal>/>可被撤回
			
			<input type="checkbox" disabled <pg:equal colName="IS_CANCEL" value="1">checked </pg:equal>/>可驳回
			
			<input type="checkbox" disabled <pg:equal colName="IS_DISCARD" value="1">checked </pg:equal>/>可废弃
			
			<input type="checkbox" disabled <pg:equal colName="IS_COPY" value="1">checked </pg:equal>/>可抄送
			
			<input type="checkbox" disabled <pg:equal colName="IS_MULTI" value="1">checked </pg:equal>/>多实例
			
			<input type="checkbox" disabled <pg:equal colName="IS_SEQUENTIAL" value="1">checked </pg:equal>/>串行
		</td>
	</tr>
</pg:list>
</pg:notempty>
</table>
							
<%--当前节点参数table--%>
<pg:notempty actual="${taskVariableMap}" >
<table border="0" cellpadding="0" cellspacing="0" class="table3">
	<tr >
		<th rowspan="<pg:cell actual="${variableRownum}"/>" width="100"><strong>当前节点参数：</strong></th>
		<th width="150"><strong>节点名称</strong></th>
		<th width="150"><strong>EXECUTIONID</strong></th>
		<th width="150"><strong>PARENTID</strong></th>
		<th width="100"><strong>参数名称</strong></th>
		<th width="100"><strong>参数类型</strong></th>
		<th width="100"><strong>BYTEARRAY_ID_</strong></th>
		<th width="100"><strong>DOUBLE_</strong></th>
		<th width="100"><strong>LONG_</strong></th>
		<th width="150"><strong>TEXT_</strong></th>
		<th width="100"><strong>TEXT2_</strong></th>
	</tr>
			
	
	<pg:map actual="${taskVariableMap}">
		<tr >
			<td rowspan="<pg:cell actual="${instanceRownum}"/>" width="150" ><pg:mapkey/></td>
					
		<pg:list >
		<pg:equal length="variableList" value="1">
			<td width="150"><pg:cell colName="ID_"/></td>
			<td width="150"><pg:cell colName="PARENT_ID_"/></td>
				
		<pg:list colName="variableList" position="0">
			<td width="100"><pg:cell colName="NAME_"/></td>
			<td width="100"><pg:cell colName="TYPE_"/></td>
			<td width="100">
				<pg:empty colName="DOUBLE_">&nbsp;</pg:empty>
				<pg:notempty colName="DOUBLE_"><pg:cell colName="DOUBLE_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="BYTEARRAY_ID_">&nbsp;</pg:empty>
				<pg:notempty colName="BYTEARRAY_ID_"><pg:cell colName="BYTEARRAY_ID_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="LONG_">&nbsp;</pg:empty>
				<pg:notempty colName="LONG_"><pg:cell colName="LONG_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="TEXT_">&nbsp;</pg:empty>
				<pg:notempty colName="TEXT_"><pg:cell colName="TEXT_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="TEXT2_">&nbsp;</pg:empty>
				<pg:notempty colName="TEXT2_"><pg:cell colName="TEXT2_"/></pg:notempty>
			</td>
		</pg:list>
		</tr>
		</pg:equal>
		
		<pg:upper length="variableList" value="1">
			<td width="150" rowspan="<pg:size colName="variableList"/>"><pg:cell colName="ID_"/> </td>
			<td width="150" rowspan="<pg:size colName="variableList"/>"><pg:cell colName="PARENT_ID_"/></td>
					
		<pg:list colName="variableList" position="0">
			<td width="100"><pg:cell colName="NAME_"/></td>
			<td width="100"><pg:cell colName="TYPE_"/></td>
			<td width="100">
				<pg:empty colName="BYTEARRAY_ID_">&nbsp;</pg:empty>
				<pg:notempty colName="BYTEARRAY_ID_"><pg:cell colName="BYTEARRAY_ID_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="DOUBLE_">&nbsp;</pg:empty>
				<pg:notempty colName="DOUBLE_"><pg:cell colName="DOUBLE_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="LONG_">&nbsp;</pg:empty>
				<pg:notempty colName="LONG_"><pg:cell colName="LONG_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="TEXT_">&nbsp;</pg:empty>
				<pg:notempty colName="TEXT_"><pg:cell colName="TEXT_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="TEXT2_">&nbsp;</pg:empty>
				<pg:notempty colName="TEXT2_"><pg:cell colName="TEXT2_"/></pg:notempty>
			</td>
		</pg:list>
		</tr>
			
		<pg:list colName="variableList" start="1">
		<tr>
			<td width="100"><pg:cell colName="NAME_"/></td>
			<td width="100"><pg:cell colName="TYPE_"/></td>
			<td width="100">
				<pg:empty colName="BYTEARRAY_ID_">&nbsp;</pg:empty>
				<pg:notempty colName="BYTEARRAY_ID_"><pg:cell colName="BYTEARRAY_ID_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="DOUBLE_">&nbsp;</pg:empty>
				<pg:notempty colName="DOUBLE_"><pg:cell colName="DOUBLE_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="LONG_">&nbsp;</pg:empty>
				<pg:notempty colName="LONG_"><pg:cell colName="LONG_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="TEXT_">&nbsp;</pg:empty>
				<pg:notempty colName="TEXT_"><pg:cell colName="TEXT_"/></pg:notempty>
			</td>
			<td width="100">
				<pg:empty colName="TEXT2_">&nbsp;</pg:empty>
				<pg:notempty colName="TEXT2_"><pg:cell colName="TEXT2_"/></pg:notempty>
			</td>
		</tr>
		</pg:list>
		</pg:upper>
		</pg:list> 
	
	</pg:map> 
</table>
</pg:notempty>