<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>

   <cm:request setAttribute="pagePath" value="/sanyhrm/personnel/dimission/auditing"/>
 <cm:request setAttribute="h1_title" value="待办信息：调岗流程"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>三一集团人力资源管理系统:${h1_title}</title>
<%@ include file="/sanymbp/common/jsp/head.jsp"%>
</head>
<body>
	<div data-role="page" class="type-interior">
		<div data-role="header" data-position="fixed" class="h1_1">
			<%@ include file="/sanymbp/common/jsp/top.jsp"%>
			<ul class="navbar1">
				<li class="li2"><a data-ajax="false"
					href="${ctx}/sanyhrm/personnel/dimission/index.page?taskId=${param.taskId}"><span>申请内容</span></a></li>
				<li class="li1"><a data-ajax="false"
					href="${ctx}/sanyhrm/personnel/dimission/auditing.page?taskId=${param.taskId}"><span>流程信息</span></a></li>
			</ul>
		</div>
		<div data-role="content" class="div1">
			<div id="message" style="display: none;"></div>
			<form id="dimissionForm" data-ajax="false"
				action="${ctx}/sanyhrm/personnel/dimission/submitDimissionTask.page"
				method="post" data-ajax="false" class="ui-body-c">
				<input type="hidden" name="taskId" value="${param.taskId}" />
				<div data-role="fieldcontain">
					<fieldset data-role="controlgroup" data-type="horizontal">
						<legend>处理方式：</legend>
						<input type="radio" name="operateType" id="operateType-a"
							checked="checked" value="post" /> <label for="operateType-a">通过</label>
						<input type="radio" name="operateType" id="operateType-b"
							value="back" /> <label for="operateType-b">驳回</label> <input
							type="radio" name="operateType" id="operateType-c" value="del" />
						<label for="operateType-c">废弃</label>
					</fieldset>
				</div>
				<div data-role="fieldcontain" class="backSelect">
					<fieldset data-role="controlgroup" data-type="horizontal">
						<legend>驳回节点：</legend>
						<select id="backTaskName" name="backTaskName"
							data-native-menu="false">
							<pg:list requestKey="backWfNodeList">
								<option value="<pg:cell colName="wfNodeCode"/>"
									describing='<pg:cell colName="describing"/>'><pg:cell colName="name"/></option>
							</pg:list>
						</select>
					</fieldset>
				</div>
				<div data-role="fieldcontain" class="divSelect1">
					<fieldset data-role="controlgroup" data-type="horizontal">
						<legend>驳回处理人：</legend>
						<select id="ignoreTargetCandidateUsers"
							name="ignoreTargetCandidateUsers" data-native-menu="false"></select>
					</fieldset>
				</div>
				<div data-role="fieldcontain" class="backSelect">
					<fieldset data-role="controlgroup" data-type="horizontal">
						<legend>返回方式：</legend>
						<input type="checkbox" name="isBackIgnoreSourceTransition"
							id="isBackIgnoreSourceTransition" value="10" checked="checked" />
						<label for="isBackIgnoreSourceTransition">驳回的节点通过后直接返回本节点</label>
					</fieldset>
				</div>
				<div data-role="fieldcontain">
					<label for="nodeMsg">处理意见:</label>
					<textarea cols="40" rows="16" name="nodeMsg" id="nodeMsg"></textarea>
				</div>
				<div>
					<button type="submit" data-theme="b" name="submit"
						value="submit-value">提交</button>
				</div>
			</form>
			<h3>流程节点信息：</h3>
			<table class="table1" border="0" cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<th>时间</th>
						<th>节点名称</th>
						<th>处理人</th>
					</tr>
				</thead>
				<tbody>
					<pg:list requestKey="workflowLogList" >
						<tr>
							<td><pg:cell colName="CREATE_TIME_NA_"/></td>
							<td><pg:cell colName="NODE_NAME"/></td>
							<td><pg:cell colName="OPERATION_PERSON_NA_"/></td>
						</tr>
						<tr>
							<td colspan="3"><pg:cell colName="MESSAGE_"/>:
									<pg:empty colName="DESCRIBING">无</pg:empty>
									<pg:notempty colName="DESCRIBING"><pg:cell colName="DESCRIBING"/></pg:notempty>
							</td>
						</tr>
					</pg:list>
				</tbody>
			</table>
		</div>
		<%@ include file="/sanymbp/common/jsp/footer.jsp"%>
	</div>
</body>
</html>