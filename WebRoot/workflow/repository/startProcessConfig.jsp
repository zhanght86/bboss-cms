<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%--
描述：开启流程实例配置
作者：谭湘
版本：1.0
日期：2014-07-24
 --%>	

<div class="tabbox">
	<ul class="tab" id="menu1">
		<li><a href="javascript:void(0)" class="current" onclick="setTab(1,0)"><span>处理人配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(1,1)"><span>控制变量配置</span></a></li>
		<li><a href="javascript:void(0)" onclick="setTab(1,2)"><span>参数配置</span></a></li>
	</ul>
</div>

<div id="main1" >
	<ul id="tab1" style="display:block;">
		<div id="handlerConfig" >
			<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
				<pg:header>
					<th>节点KEY</th>
					<th>节点名称</th>
					<th>待办人</th>
					<th>待办组</th>
					<th>节点类型</th>
				</pg:header>
				
				<input type="hidden" id="processKey" name="processKey" value="${processKey}"/>
				
				<pg:list requestKey="nodeConfigList">
					<pg:notin colName="node_type" scope="startEvent,endEvent,serviceTask">
						<tr class="replaceTr">
						
							<input type="hidden" id="<pg:cell colName='node_key'/>_users_id" 
								name="candidate_users_id" value="<pg:cell colName='candidate_users_id'/>" />
							<input type="hidden" id="<pg:cell colName='node_key'/>_groups_id" 
								name="candidate_groups_id" value="<pg:cell colName='candidate_groups_id'/>" />
							<input type="hidden" name="node_id" value="<pg:cell colName='id'/>"/>
							<input type="hidden" name="node_key" value="<pg:cell colName='node_key'/>" />
							<td><pg:cell colName="node_key"></pg:cell></td>
							<td><pg:cell colName="node_name"></pg:cell></td>
							<td>
								<input type="text" class="input1 w200" readonly
									id="<pg:cell colName='node_key'/>_users_name" 
									name="candidate_users_name" value="<pg:cell colName='candidate_users_name'/>" />
								<a href="javascript:openChooseUsers('<pg:cell colName='node_key'/>')">选择</a>
								<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','1')">清空</a>
							</td>
							<td>
								<input type="text" class="input1 w200" readonly
									id="<pg:cell colName='node_key'/>_groups_name" 
									name="candidate_groups_name" value="<pg:cell colName='candidate_groups_name'/>"/>
								<a href="javascript:openChooseGroups('<pg:cell colName='node_key'/>')">选择</a>
								<a href="javascript:emptyChoose('<pg:cell colName="node_key"/>','2')">清空</a>
							</td>
							<td >
								<span id="<pg:cell colName='node_key'/>_nodeTypeName"><pg:cell colName="nodeTypeName"/></span>
							</td>
						</tr>
					</pg:notin>
				</pg:list>
			</table>
		</div>
	</ul>
	
	<ul id="tab2" style="display: none;">
		<div id="controlParamConfig" >
			
			<%@ include file="nodeControlParam.jsp"%>
			
		</div>
	</ul>

	<ul id="tab3" style="display: none;">
		<div id="paramConfig">
			<div class="title_box">
				<div class="rightbtn">
					<a href="javascript:addTr()" class="bt_small" id="addButton"><span>新增</span></a>
				</div>
			</div>
			<div id="nodevariableContainer">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb3">
					<pg:header>
						<th>所属节点</th>
						<th>参数名称</th>
						<th>参数值</th>
						<th>参数描述</th>
						<th>操作</th>
					</pg:header>
				</table>
			</div>
		</div>
	</ul>
</div>

