<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看任务详情</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	
<body>
<div class="form">
	<div class="mcontent">
		<div id="searchblock">
			<form id="viewForm" name="viewForm">
				<pg:beaninfo requestKey="processInst">
					<fieldset >
						<legend><strong>实例信息</strong></legend>
							<table border="0" cellpadding="0" cellspacing="0" class="table4">
								<tr height="25px">
									<th width=85px ><strong>定义ID：</strong></th>
									<td width=140px ><pg:cell colName="PROC_DEF_ID_"/></td>
									<th width=85px ><strong>流程KEY：</strong></th>
									<td width=140px ><pg:cell colName="KEY_"/></td>
									<th width=85px ><strong>流程名称：</strong></th>
									<td width=140px ><pg:cell colName="NAME_"/></td>
									<th width=85px ><strong>版本：</strong></th>
									<td width=140px ><pg:cell colName="VERSION_"/></td>
								</tr>
								<tr height="25px">
									<th width=85px ><strong>实例ID：</strong></th>
									<td width=140px ><pg:cell colName="PROC_INST_ID_"/></td>
									<th width=85px ><strong>父实例ID：</strong></th>
									<td width=140px ><pg:cell colName="SUPER_PROCESS_INSTANCE_ID_"/></td>
									<th width=85px ><strong>实例状态：</strong></th>
									<td width=140px >
										<pg:empty colName="SUSPENSION_STATE_" >
											流程结束
										</pg:empty>
										<pg:notempty colName="SUSPENSION_STATE_" >
											<pg:equal colName="SUSPENSION_STATE_" value="1">
												流程激活
											</pg:equal>
											<pg:equal colName="SUSPENSION_STATE_" value="2">
												流程挂起
											</pg:equal>
										</pg:notempty>
									</td>
									<th width=85px><strong>发起人：</strong></th>
									<td width=140px ><pg:cell colName="START_USER_ID_"/></td>
								</tr>
								<tr height="25px">
									<th width=85px><strong>发起时间：</strong></th>
									<td width=140px ><pg:cell colName="START_TIME_"/></td>
									<th width=85px ><strong>结束时间：</strong></th>
									<td width=140px ><pg:cell colName="END_TIME_"/></td>
									<pg:notempty colName="taskList" >
										<th width=85px ><strong>当前节点：</strong></th>
										<td width=140px >
											<pg:list colName="taskList">
									           	<pg:cell index="1" colName="NAME_"/></br>
									        </pg:list>
										</td>
									</pg:notempty>
									<pg:notempty colName="taskList" >
										<th width=85px ><strong>处理人：</strong></th>
										<td width=140px >
											<pg:list colName="taskList">
											   <pg:cell index="1" colName="USER_ID_"/></br>
									       	</pg:list>
										</td>
									</pg:notempty>
								</tr>
							</table>
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
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
				       	<th>节点名</th>
				       	<th>任务到达时间</th>
				       	<th>任务签收时间</th>
				       	<th>任务处理时间</th>
				       	<th>耗时</th>
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
					    	<td><pg:cell colName="DURATION_" /></td>  
					    	<td>
						    	<pg:empty colName="USER_ID_" >
						    		<pg:cell colName="ASSIGNEE_" />
						    	 </pg:empty>
						    	 <pg:notempty colName="USER_ID_" >
						    		<pg:cell colName="USER_ID_" />
						    	 </pg:notempty>
					    	</td>  
					    	<td><pg:cell colName="ASSIGNEE_" /></td>  
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
			</form>
		</div>
  	</div>	
  		
	<div class="btnarea" >
		<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
	</div>	
</div>
</body>
<script type="text/javascript">
$(document).ready(function() {

	 	    
});

//轨迹回放
function reBack(){
	
}
</script>
</head>
</html>
