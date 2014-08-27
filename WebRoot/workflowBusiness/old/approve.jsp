<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@include file="/page/util/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link href="${ctx}/newpage/stylesheet/basic.css" rel="stylesheet"
	type="text/css" />
<link href="${ctx}/newpage/sstylesheet/dossier.css" rel="stylesheet"
	type="text/css" />

</head>
<body>
	<pg:equal actual="${pagetype}" value="1">
		<tr>
			<th>处理人：</th>
			<td><a href="javascript:showtab()">选择流程节点处理人 </a></td>
		</tr>
	</pg:equal>
	<pg:notin actual="${pagetype}" scope="1,6">
		<tr>
			<th width="95">当前节点：</th>
			<td>${coordinateObj.properties.name}</td>
		</tr>
		<tr>
			<th>当前处理人：</th>
		<pg:list requestKey="actList">
			<pg:equal colName="actId" value="${coordinateObj.id}">
				<td><pg:cell colName="realName" /></td>
			</pg:equal>
		</pg:list>
		</tr>
	</pg:notin>

	<pg:equal actual="${pagetype}" value="5">
		<tr id="recalltr" style="display: none;">
			<th>处理结果：</th>
			<td><input name="operateType" type="radio" value="recall" />撤回</td>
		</tr>
	</pg:equal>

	<pg:in actual="${pagetype}" scope="2,3,4">
		<tr>
			<th>处理结果：</th>
			<td>
			<input name="operateType" type="radio" value="pass" />通过
			<pg:equal actual="${pagetype}" value="3">
					<input name="operateType" type="radio" value="reject" />驳回 
					<input name="operateType" type="radio" value="turnTo" />转办 
			</pg:equal> 
			<input name="operateType" type="radio" value="toEnd" />废弃
			</td>
		</tr>
		<tr id="rejectto" style="display: none">
				<th>驳回到</th>
			<td ><select name="rejectToActId" id="rejectToActId">
							<pg:list requestKey="hisActList">
								<option value="<pg:cell colName="actId" />">
									<pg:cell colName="actName" />
								</option>
							</pg:list>
					</select><input type="hidden" name="toActName" value="" />
					&nbsp;&nbsp;<input type="checkbox" name="isReturn" value="1" />驳回的节点通过后直接返回本节点
				</td>
			</tr>
			<tr id="delegateTr" style="display: none">
					<th>转办</th>
					<td ><input type="hidden" name="delegateUser" id="delegateUser" value="" />
					<input type="text" name="delegateUserName" id="delegateUserName" value="" size="50"/>
					<a href="javascript:delegateUsers('')" name="delegateUsersa">[选择]</a>
					<span class="requiredstar" style="color: red;display: none;">*</span>
					</td>
			</tr>
	</pg:in>
	<pg:notin actual="${pagetype}" scope="5,6">
		<tr>
			<th>处理意见:</th>
			<td><textarea name="remark" id="remark" class="textarea2"></textarea></td>
		</tr>
	</pg:notin>
	<!--  
			<tr>
				<th  width="15%">相关附件</th>
				<td  width="85%"><a class="bt_small"><span>添加附件</span></a></td>
			</tr>
			-->

	<pg:notequal actual="${pagetype}" value="1">
		<tr>
			<th>审批记录：</th>
			<td><table width="100%" border="0" cellpadding="0"
					cellspacing="0" class="sany_table2">
					<tbody>
						<tr>
							<th width="33%">时间</th>
							<th width="14%">节点名</th>
							<th width="13%">操作者</th>
							<th width="22%">操作</th>
							<th width="18%">备注</th>
						</tr>
						<pg:list requestKey="proLogList">
							<tr>
								<td><pg:cell colName="operateDate" /></td>
								<td><pg:cell colName="taskName" /></td>
								<td><pg:cell colName="operateName" /></td>
								<td><pg:cell colName="operateDescription" /></td>
								<td><pg:cell colName="operateRemark" /></td>
							</tr>
						</pg:list>

					</tbody>
				</table></td>
		</tr>
	</pg:notequal>
</body>
</html>