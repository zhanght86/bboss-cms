<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程待办配置</title>
<%@ include file="/common/jsp/css.jsp"%>
<style type="text/css">
.a_bg_color{
	color: white;
    cursor:hand;
	background-color: #191970
}
</style>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
	function query(businessId,businessType,treename) {
		$("#orgNameSpan").html(treename+">>");
		$("a").removeClass("a_bg_color");
		if(businessType!='0')
		$("a[name='"+businessId+"']").addClass("a_bg_color"); 
		$("#business_id").val(businessId);
		$("#business_type").val(businessType);
		var processKey = $('#key').val();
		var taskKey = $('#taskKey').val();
		$("#nodeconfig").load(
				"showActivitiNodeInfo.page?taskKey=" + taskKey + "&processKey="
						+ processKey + "&business_id=" + businessId+"&business_type="+businessType);
	}
	$(document).ready(function(){
		  $("#org_tree").load("../taskConfig/task_config_org_tree.jsp");
		  $("#bussinesstree").load("../businesstype/businessTypeTree.jsp");
	});
	
</script>
</head>
<body>
<div class="titlebox"><span class="location">您的当前位置：工作流&gt;<a href="../repository/index.page">工作流管理</a>&gt;流程配置</span></div>
<div class="easyui-layout" style="width:100%;height:650px;">

	<input type="hidden" value="${submitMethod }" id="submitMethod" name="submitMethod"/>
	<div region="west" split="true" title="待办类型"
		style="width: 220px; padding1: 1px; overflow: hidden;">
		<div class="easyui-accordion" fit="true" border="false">
			<div id="org_tree" title="组织结构" selected="true" style="overflow: auto;">
				
			</div>
			<div id="bussinesstree" title="业务类型" style="padding: 10px; overflow: auto;">
				
				
			</div>
			<div title="通用配置" style="padding: 10px; overflow: auto;">
				<a href="javascript:query('','0','通用')" name="0">通用配置</a>
			</div>
		</div>

	</div>
	<div region="center" title="${processDef.DEPLOYMENT_NAME_ }>><span id='orgNameSpan'></span>节点配置" style="overflow: hidden;">

		<div class="easyui-layout" fit="true" style="background: #ccc;">
			<div region="center" style="height: 465px;">
				<pg:beaninfo requestKey="processDef">
					<%@ include file="viewProcessPic.jsp"%>
				</pg:beaninfo>
			</div>
			<div region="south" split="true" style="height: 400px;">
				<div id="nodeconfig">
					<pg:beaninfo requestKey="processDef">
						<fieldset>
							<legend>流程信息</legend>
							<table border="0" cellpadding="0" cellspacing="0" class="table4">
								<tr height="25px">
									<th width=85px>部署ID：</th>
									<td width=140px id="DEPLOYMENT_ID_"><pg:cell
											colName="DEPLOYMENT_ID_" /></td>
									<th width=85px>目录：</th>
									<td width=140px id="CATEGORY_"><pg:cell
											colName="CATEGORY_" /></td>
									<th width=70px>名称：</th>
									<td width=140px id="NAME_"><pg:cell colName="NAME_" /></td>
								</tr>
								<tr height="25px">
									<th>键：</th>
									<td id="KEY_"><pg:cell colName="KEY_" /></td>

									<th width=85px>版本：</th>
									<td id="VERSION_"><pg:cell colName="VERSION_" /></td>

									<th width=85px>部署名称：</th>
									<td id="DEPLOYMENT_NAME_"><pg:cell
											colName="DEPLOYMENT_NAME_" /></td>
								</tr>
								<tr height="25px">
									<th>部署时间：</th>
									<td id="DEPLOYMENT_TIME_"><pg:cell
											colName="DEPLOYMENT_TIME_" dateformat="yyyy-MM-dd hh:mm:ss" /></td>

									<th width=85px>资源名称：</th>
									<td id="RESOURCE_NAME_"><pg:cell colName="RESOURCE_NAME_" /></td>

									<th width=85px>图片资源名称：</th>
									<td id="DGRM_RESOURCE_NAME_"><pg:cell
											colName="DGRM_RESOURCE_NAME_" /></td>
								</tr>
							</table>
						</fieldset>
					</pg:beaninfo>
				</div>
			</div>
		</div>
	</div>
	</div>
</body>
</html>