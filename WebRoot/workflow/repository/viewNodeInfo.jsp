<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：节点控制参数
作者：谭湘
版本：1.0
日期：2014-07-24
 --%>	

<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
	//这是扩展Firefox下moveRows()这个方法，否则moveRows()是不被firefox支持
	if (typeof Element != 'undefined') Element.prototype.moveRow = function (sourceRowIndex, targetRowIndex) {//执行扩展操作
	   if (!/^(table|tbody|tfoot|thead)$/i.test(this.tagName) || sourceRowIndex === targetRowIndex) return false;
	   var pNode = this;
	   if (this.tagName == 'TABLE') pNode = this.getElementsByTagName('tbody')[0]; 
	   //firefox会自动加上tbody标签，所以需要取tbody，直接table.insertBefore会error
	   var sourceRow = pNode.rows[sourceRowIndex], targetRow = pNode.rows[targetRowIndex];
	   if (sourceRow == null || targetRow == null) return false;
	   var targetRowNextRow = sourceRowIndex > targetRowIndex ? false : getTRNode(targetRow, 'nextSibling');
	   if (targetRowNextRow === false) pNode.insertBefore(sourceRow, targetRow); //后面行移动到前面，直接insertBefore即可
	   else {//移动到当前行的后面位置，则需要判断要移动到的行的后面是否还有行，有则insertBefore，否则appendChild
	       if (targetRowNextRow == null) pNode.appendChild(sourceRow);
	       else pNode.insertBefore(sourceRow, targetRowNextRow);
	   }
	}
	//Firefox下表格里面的空白，回车也算一个节点，所以需要过滤一下节点类型。
	function getTRNode(nowTR, sibling) { while (nowTR = nowTR[sibling]) if (nowTR.tagName == 'TR') break; return nowTR; }
    
});

function makeOneCheck(obj) {
	var radios = $(".FIRST_USERNODE");
	for (var i=0; i<radios.length; i++) {
		radios[i].checked = false;
	}
	$(obj).get(0).checked = true;
}

function setIsCopy(obj,nodekey) {
	if (obj.value != '0') {
		$("#"+nodekey+"_isCopyShow").attr("checked",true);
	}else {
		$("#"+nodekey+"_isCopyShow").attr("checked",false);
	}
	
}

function saveSort(){
	var FIRST_USERNODE_NUM = 0;
	/**
	$(".FIRST_USERNODE").each(function() {
		if ($(this).get(0).checked == true) {
			FIRST_USERNODE_NUM ++;
        }
    });
	
	if (FIRST_USERNODE_NUM > 1) {
		W.$.dialog.alert("只能设置一个默认撤回节点，保存失败",function(){},api);
		return;
	}
	*/
	
	var ORDER_NUM = $(".ORDER_NUM");
	ORDER_NUM.each(function(i){
       $(this).val(i);
	 });
	
   	$.ajax({
 	 	type: "POST",
		url : "<%=request.getContextPath()%>/workflow/repository/saveNodeOrderNum.page",
		data: formToJson("#nodesortForm"),		
		dataType : 'json',
		async:false,
		beforeSend: function(XMLHttpRequest){
			 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
			},
		success : function(data){
			if (data != 'success') {
				W.$.dialog.alert("保存出错："+data,function(){},api);
			}else {
				W.$.dialog.alert("保存成功",function(){},api);
			}
		}
	 });
}

var contents = {
error : "移动按钮异常",
ico_top : "↑",
ico_bottom : "↓",
button_top : "<INPUT type=\"button\" value=\"↑\" onclick=\"trMove(this)\">",
button_bottom : "<INPUT type=\"button\" value=\"↓\" onclick=\"trMove(this)\">"
}

var trMove = function(obj) {
	var thisTr = obj.parentNode.parentNode;
	var localIndex = thisTr.rowIndex;
	var trArr = document.getElementById("tab").rows;
	if(obj.value == contents.ico_top) {
	    
	  if(localIndex != 1)
		document.getElementById("tab").moveRow(localIndex,localIndex-1);
	} else if(obj.value == contents.ico_bottom){
	    if(localIndex != trArr.length-1)
		document.getElementById("tab").moveRow(localIndex,localIndex+1);
	} else {
		alert(contents.error);
	}
}

var tabHide = function(start) {
var tab = document.getElementById("tab");
var trs = tab.rows;
var firstTr = trs[start];
var lasterTR = trs[trs.length - 1];
}

function trMoveTofirst(obj){
  var thisTr = obj.parentNode.parentNode;
  var localIndex = thisTr.rowIndex;
  if(localIndex != 1)
  document.getElementById("tab").moveRow(localIndex,1);
  
}
function trMoveToend(obj){
  var thisTr = obj.parentNode.parentNode;
  var localIndex = thisTr.rowIndex;
  var trArr = document.getElementById("tab").rows;
  if(localIndex != trArr.length-1)
  document.getElementById("tab").moveRow(localIndex,trArr.length-1);
}
</script>

<form id="nodesortForm" method="post" action="">
	<table width="100%" border="0" cellpadding="0" cellspacing="0" id="tab" class="stable">
		<pg:header>
			<th>默认撤<br/>回节点</th>
			<th>抄送节点</th>
			<th>节点KEY</th>
			<th>节点名称</th>
			<th>操作</th>
			<th>节点描述</th>
			<th>处理工时<br/>(小时)</th>
			<th>待办URL</th>
			<th>控制参数</th>
		</pg:header>
			
		<pg:list requestKey="nodeList">
			<tr >
				<td class="td_center"><input type="radio" class="FIRST_USERNODE" name="<pg:cell colName='NODE_KEY'/>_FIRST_USERNODE" value="1"
				id="FIRST_USERNODE" onclick="makeOneCheck(this)"
				<pg:equal colName="FIRST_USERNODE" value="1">checked</pg:equal> />
				<input type="hidden" name="ID" value="<pg:cell colName="ID"/>"/>
				<input type="hidden" name="NODE_KEY" value="<pg:cell colName="NODE_KEY"/>"/>
				<input type="hidden" name="ORDER_NUM" class="ORDER_NUM" value="<pg:cell colName="ORDER_NUM"/>"/></td>
				<td >
					<pg:equal colName="NODE_TYPE" value="userTask">
						<select id="IS_COPY" name="<pg:cell colName="NODE_KEY"/>_IS_COPY" onchange="setIsCopy(this,'<pg:cell colName="NODE_KEY"/>')">
							<option value="0" <pg:equal colName="IS_COPY" value="0">selected</pg:equal>>普通节点</option>
							<option value="1" <pg:equal colName="IS_COPY" value="1">selected</pg:equal>>抄送节点</option>
							<option value="2" <pg:equal colName="IS_COPY" value="2">selected</pg:equal>>通知节点</option>
						</select>
					</pg:equal>
					<pg:notequal colName="NODE_TYPE" value="userTask">
						<select id="IS_COPY" name="IS_COPY" >
							<option value="0" selected>普通节点</option>
						</select>
					</pg:notequal>
				</td>
				<td class="td_center"><pg:cell colName="NODE_KEY"/></td>
				<td ><pg:cell colName="NODE_NAME"/></td>
				<td >
			         <INPUT type="button" value="顶" onclick="trMoveTofirst(this)" />
			         <INPUT type="button" value="↑"  onclick="trMove(this)" />
			         <INPUT type="button" value="↓"  onclick="trMove(this)" />
			         <INPUT type="button" value="底" onclick="trMoveToend(this)" />
		        </td>
				<td >
					<span class="toolTip" title="<pg:cell colName="NODE_DESCRIBE"/>"><pg:cell colName="NODE_DESCRIBE" maxlength="8" replace="..."/></span>
				</td>
				<td ><pg:cell colName="DURATION_NODE"/></td>
				<td >
					<span class="toolTip" title="<pg:cell colName="TASK_URL"/>"><pg:cell colName="TASK_URL" maxlength="20" replace="..."/></span>
				</td>
				<pg:equal colName="NODE_TYPE" value="userTask">
				<td >
					<pg:notempty colName="IS_VALID">
						<input type="checkbox" disabled <pg:equal colName="IS_VALID" value="1">checked </pg:equal> />是否有效 
					</pg:notempty>
					<pg:empty colName="IS_VALID">
						<input type="checkbox" disabled/>是否有效 
					</pg:empty>
					
					<pg:notempty colName="IS_EDIT">
						<input type="checkbox" disabled <pg:equal colName="IS_EDIT" value="1">checked </pg:equal> />可修改
					</pg:notempty>
					<pg:empty colName="IS_EDIT">
						<input type="checkbox" disabled/>可修改
					</pg:empty>
					
					<pg:notempty colName="IS_EDITAFTER">
						<input type="checkbox" disabled <pg:equal colName="IS_EDITAFTER" value="1">checked </pg:equal>/>可修改后续节点
					</pg:notempty>
					<pg:empty colName="IS_EDITAFTER">
						<input type="checkbox" disabled />可修改后续节点
					</pg:empty>
					
					<pg:notempty colName="IS_AUTOAFTER">
						<input type="checkbox" disabled <pg:equal colName="IS_AUTOAFTER" value="1">checked </pg:equal>/>处理人一致自动审批
					</pg:notempty>
					<pg:empty colName="IS_AUTOAFTER">
						<input type="checkbox" disabled />处理人一致自动审批
					</pg:empty>
					
					<br/>
					
					<pg:notempty colName="IS_RECALL">
						<input type="checkbox" disabled <pg:equal colName="IS_RECALL" value="1">checked </pg:equal>/>可被撤回
					</pg:notempty>
					<pg:empty colName="IS_RECALL">
						<input type="checkbox" disabled />可被撤回
					</pg:empty>
					
					<pg:notempty colName="IS_CANCEL">
						<input type="checkbox" disabled <pg:equal colName="IS_CANCEL" value="1">checked </pg:equal>/>可驳回
					</pg:notempty>
					<pg:empty colName="IS_CANCEL">
						<input type="checkbox" disabled />可驳回
					</pg:empty>
					
					<pg:notempty colName="IS_DISCARD">
						<input type="checkbox" disabled <pg:equal colName="IS_DISCARD" value="1">checked </pg:equal>/>可废弃
					</pg:notempty>
					<pg:empty colName="IS_DISCARD">
						<input type="checkbox" disabled />可废弃
					</pg:empty>
					
					<pg:notempty colName="IS_MULTI">
						<input type="checkbox" disabled <pg:equal colName="IS_MULTI" value="1">checked </pg:equal>/>多实例
					</pg:notempty>
					<pg:empty colName="IS_MULTI">
						<input type="checkbox" disabled />多实例
					</pg:empty>
					
					<pg:notempty colName="IS_SEQUENTIAL">
						<input type="checkbox" disabled <pg:equal colName="IS_SEQUENTIAL" value="1">checked </pg:equal>/>串行
					</pg:notempty>
					<pg:empty colName="IS_SEQUENTIAL">
						<input type="checkbox" disabled />串行
					</pg:empty>
					
					<input type="checkbox"  id="<pg:cell colName="NODE_KEY"/>_isCopyShow" <pg:equal colName="IS_COPY" value="1">checked</pg:equal>/>可抄送
				</td>
				</pg:equal>
				<pg:notequal colName="NODE_TYPE" value="userTask"><td>&nbsp;</td></pg:notequal>
			</tr>
		</pg:list>
	</table>
	
	<a href="javascript:void(0)" class="bt_2" id="saveSort" onclick="saveSort()"><span>保存</span></a>
</form>
