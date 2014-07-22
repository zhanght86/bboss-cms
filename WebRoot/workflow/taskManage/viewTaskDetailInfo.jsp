<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看任务详情</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	
<body>
<div class="">
	<div class="mcontent">
		<div id="searchblock">
			<form id="viewForm" name="viewForm">
				<pg:beaninfo requestKey="processInst">
					<fieldset >
						<legend><strong>流程信息</strong></legend>
								<%--流程定义table--%>
								<table border="0" cellpadding="0" cellspacing="0" class="table3" >
									<tr >
										<th rowspan="2" width="100"><strong>定义信息：</strong></th>
										<th width="350"><strong>定义ID</strong></th>
										<th width="100"><strong>流程KEY</strong></th>
										<th width="100"><strong>流程名称</strong></th>
										<th width="150"><strong>版本</strong></th>
									</tr>
									<tr >
										<td width="350"><pg:cell colName="PROC_DEF_ID_"/></td>
										<td width="100"><pg:cell colName="KEY_"/></td>
										<td width="100"><pg:cell colName="NAME_"/></td>
										<td width="150"><pg:cell colName="VERSION_"/></td>
									</tr>
								</table>
								
								<%--流程实例table--%>
								<table border="0" cellpadding="0" cellspacing="0" class="table3">
									<tr >
										<th 
										<pg:notempty colName="SUPER_PROCESS_INSTANCE_ID_" >
										rowspan="4" 
										</pg:notempty>
										<pg:empty colName="SUPER_PROCESS_INSTANCE_ID_" >
										rowspan="2" 
										</pg:empty>
										
										width="100"><strong>实例信息：</strong></th>
										<th width="350"><strong>实例ID</strong></th>
										<th width="100"><strong>实例状态</strong></th>
										<th width="100"><strong>发起人</strong></th>
										<th width="150"><strong>发起时间</strong></th>
										<th width="150"><strong>结束时间</strong></th>
									</tr>
									<tr >
										<td width="350"><pg:cell colName="PROC_INST_ID_"/></td>
										<td width="100">
											<pg:notnull colName="END_TIME_" >
												结束
											</pg:notnull>
											<pg:null colName="END_TIME_" >
												<pg:equal colName="SUSPENSION_STATE_" value="1">
													进行中
												</pg:equal>
												<pg:equal colName="SUSPENSION_STATE_" value="2">
													挂起
												</pg:equal>
											</pg:null>
										</td>
										<td width="100"><pg:cell colName="START_USER_ID_NAME"/></td>
										<td width="150"><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
										<td width="150">
											<pg:notnull colName="END_TIME_" >
												<pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/>
											</pg:notnull>
											<pg:null colName="END_TIME_" >
												&nbsp;
											</pg:null>
										</td>
									</tr>
									<pg:notempty colName="SUPER_PROCESS_INSTANCE_ID_" >
									<tr >
										<th width="350"><strong>父实例ID</strong></th>
										<th width="100"><strong>实例状态</strong></th>
										<th width="100"><strong>发起人</strong></th>
										<th width="150"><strong>发起时间</strong></th>
										<th width="150"><strong>结束时间</strong></th>
									</tr>
									<tr>
										<td width="350">
											<a href="javascript:void(0)" 
												onclick="javascript:W.viewDetailInfo('<pg:cell colName="SUPER_PROCESS_INSTANCE_ID_" />')">
			          				 			<pg:cell colName="SUPER_PROCESS_INSTANCE_ID_" />
			        						</a>
			        					</td>
										<td rowspan="2" width="100">
											<pg:notnull colName="SUPER_END_TIME_" >
												结束
											</pg:notnull>
											<pg:null colName="SUPER_END_TIME_" >
												<pg:equal colName="SUPER_SUSPENSION_STATE_" value="1">
													进行中
												</pg:equal>
												<pg:equal colName="SUPER_SUSPENSION_STATE_" value="2">
													挂起
												</pg:equal>
											</pg:null>
										</td>
										<td rowspan="2" width="100"><pg:cell colName="SUPER_START_USER_ID_NAME"/></td>
										<td rowspan="2" width="150"><pg:cell colName="SUPER_START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>
										<td rowspan="2" width="150">
											<pg:notnull colName="SUPER_END_TIME_" >
												<pg:cell colName="SUPER_END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/>
											</pg:notnull>
											<pg:null colName="SUPER_END_TIME_" >
												&nbsp;
											</pg:null>
										</td>
									</tr>
									</pg:notempty>
								</table>
								
								<%--当前节点table--%>
								<pg:notempty actual="${processInst.taskList}">
									<table border="0" cellpadding="0" cellspacing="0" class="table3">
										<tr >
											<th rowspan="<pg:size actual="${processInst.taskList}" increament="1"/>" width="100"><strong>当前节点信息：</strong></th>
											<th width="350"><strong>节点名称</strong></th>
											<th colspan="3" width="377"><strong>处理人</strong></th>
											<th width="150"><strong>签收人</strong></th>
										</tr>
										
										<pg:notempty colName="taskList" >
											<pg:list colName="taskList">
											<tr >
												<td width="350"><pg:cell colName="NAME_"/></td>
												<td colspan="3" width="377">
													<pg:notempty colName="USER_ID_NAME" >
														<pg:cell colName="USER_ID_NAME"/>
													</pg:notempty>
													<pg:empty colName="USER_ID_NAME" >
														&nbsp;
													</pg:empty>
												</td>
												<td width="150">
													<pg:notempty colName="ASSIGNEE_NAME" >
														<pg:cell colName="ASSIGNEE_NAME"/>
													</pg:notempty>
													<pg:empty colName="ASSIGNEE_NAME" >
														&nbsp;
													</pg:empty>
												</td>
											</tr>
											</pg:list>
										</pg:notempty>
									</table>
								</pg:notempty>
					</fieldset>
				</pg:beaninfo>
				
				<fieldset >
					<legend><strong>执行图</strong></legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0" >
						<tr>
					 		<td>
					 			<a href="javascript:void(0)" class="bt_small" id="reBack" onclick="reBack()"><span>轨迹回放</span></a>
					 		</td>
					 	</tr>
					 	<tr>
					 		<td align="center">
								<img id="pic" src="${pageContext.request.contextPath}/workflow/repository/getProccessActivePic.page?processInstId=${processInst.PROC_INST_ID_}" />
							</td>
						</tr>
					</table>
				</fieldset>
				
				<fieldset >
					<legend><strong>处理记录</strong></legend>
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">
					<pg:header>
				       	<th>节点名</th>
				       	<th>任务到达时间</th>
				       	<th>任务签收时间</th>
				       	<th>任务处理时间</th>
				       	<th>处理工时</th>
				       	<th>耗时</th>
				       	<th>是否预警</th>
       					<th>是否超时</th>
				       	<th>处理人</th>
				       	<th>签收人</th>
				       	<th>操作内容</th>
				      </pg:header>	
					<pg:list requestKey="taskHistorList">
					   	<tr height="25px">
					    	<td><pg:cell colName="ACT_NAME_" /></td>     
					    	<td><pg:cell colName="START_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>  
					    	<td><pg:cell colName="CLAIM_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>   
					    	<td><pg:cell colName="END_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>  
					    	<td><pg:cell colName="DURATION_NODE" /></td>   
					    	<td><pg:cell colName="DURATION_" /></td>  
					    	<td>
				       			<pg:notempty colName="isAlertTime" >
						           <pg:equal colName="isAlertTime" value="0">
						           		未预警
						           </pg:equal>
						           <pg:equal colName="isAlertTime" value="1">
						           		<span style="color: red;">已预警</span>
						           	</pg:equal>
						        </pg:notempty>
						       	<pg:empty colName="isAlertTime" >
						       		未预警
						       	</pg:empty>
						    </td>
				       		<td>
				       			<pg:notempty colName="isOverTime" >
						           <pg:equal colName="isOverTime" value="0">
						           		未超时
						           </pg:equal>
						           <pg:equal colName="isOverTime" value="1">
						           		<span style="color: red;">已超时</span>
						           	</pg:equal>
						        </pg:notempty>
						       	<pg:empty colName="isOverTime" >
						       		未超时
						       	</pg:empty>
						    </td>
					    	<td>
					    		<pg:null colName="delegateTaskList" >
							    	<pg:null colName="wfEntrust">
					       				<pg:cell colName="USER_ID_NAME"/>
					       			</pg:null>
					       			<pg:notnull colName="wfEntrust">
					       				<pg:cell colName="wfEntrust" property="create_user_name"/>委托给
					       				<pg:cell colName="wfEntrust" property="entrust_user_name"/>
					       			</pg:notnull>
						    	 </pg:null>
						    	 <pg:notnull colName="delegateTaskList" >
							     	<pg:list colName="delegateTaskList" >
									  <pg:cell colName="FROM_USER_NAME"/>转办给<pg:cell colName="TO_USER_NAME"/>
									  [<pg:cell colName="CHANGETIME"/>]
									  <br/>
							       </pg:list>
						         </pg:notnull>	
					    	</td>  
					    	<td>
								<pg:cell colName="ASSIGNEE_NAME"/>
							</td>  
					    	<td>
					    	 <pg:empty colName="DELETE_REASON_" >
					    	 	<pg:equal colName="ACT_TYPE_" value="startEvent">流程开启</pg:equal>
					    	 	<pg:equal colName="ACT_TYPE_" value="endEvent">流程结束</pg:equal>
					    		<pg:cell colName="ACT_NAME_" />
					    	 </pg:empty>
					    	 <pg:notempty colName="DELETE_REASON_" >
					    		<pg:cell colName="DELETE_REASON_" />
					    	 </pg:notempty>
					    	</td>  
					    </tr>
					 </pg:list>
				</table>
				</fieldset>
				
				<fieldset >
					<legend><strong>参数信息</strong></legend>
					
					<%@ include file="nodeVariableInfo.jsp"%>	
						
				</fieldset>
			</form>
		</div>
  	</div>	
  		
	<div class="btnarea" >
		<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
	</div>	
</div>
</body>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
	 	    
});

//轨迹回放
function reBack(){
	
}
</script>
</head>
</html>
