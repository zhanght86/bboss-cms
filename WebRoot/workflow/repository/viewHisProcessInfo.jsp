<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title></title>
		<link href="stylesheet/common.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="js/jquery-1.5.2.min.js"></script>
		<script type="text/javascript" src="js/commontool.js"></script>	
		<%@ include file="/common/jsp/css.jsp"%>
		<script type="text/javascript">
		</script>
	</head>
	<body>
			<div class="mcontent">
			<div id="searchblock">
				<pg:beaninfo requestKey="hisProcessDef">
				<form id="viewForm" name="viewForm">
				<fieldset>
				<legend><pg:message code="sany.pdp.workflow.operation.workflow.info"/></legend>
					<table border="0" cellpadding="0" cellspacing="0"
						class="table4">
						<tr height="25px">
							<th width=85px ><pg:message code="sany.pdp.workflow.deploy.id"/>：</th>
							<td width=140px id="DEPLOYMENT_ID_"><pg:cell colName="DEPLOYMENT_ID_"/></td>
							<th width=85px ><pg:message code="sany.pdp.workflow.deploy.catalogue"/>：</th>
							<td width=140px id="CATEGORY_">
								<pg:cell colName="CATEGORY_"/>
							</td>
							<th width=70px><pg:message code="sany.pdp.common.name"/>：</th>
							<td width=140px id="NAME_">
								<pg:cell colName="NAME_"/>
							</td>
						</tr>
						<tr height="25px">	
							<th ><pg:message code="sany.pdp.workflow.process.key"/>：</th>
							<td id="KEY_">
								<pg:cell colName="KEY_"/>
							</td>

							<th width=85px ><pg:message code="sany.pdp.workflow.deploy.version"/>：</th>
							<td id="VERSION_"><pg:cell colName="VERSION_"/></td>
							
							<th width=85px ><pg:message code="sany.pdp.workflow.deploy.name"/>：</th>
							<td id="DEPLOYMENT_NAME_"><pg:cell colName="DEPLOYMENT_NAME_"/></td>
						</tr>
						<tr height="25px">	
							<th ><pg:message code="sany.pdp.workflow.deploy.time"/>：</th>
							<td id="DEPLOYMENT_TIME_"><pg:cell colName="DEPLOYMENT_TIME_" dateformat="yyyy-MM-dd hh:mm:ss"/></td>

							<th width=85px ><pg:message code="sany.pdp.workflow.resource.name"/>：</th>
							<td id="RESOURCE_NAME_"><pg:cell colName="RESOURCE_NAME_"/></td>
							
							<th width=85px ><pg:message code="sany.pdp.workflow.picture.resource.name"/>：</th>
							<td id="DGRM_RESOURCE_NAME_"><pg:cell colName="DGRM_RESOURCE_NAME_"/></td>
						</tr>
					</table>
				</fieldset>
				<fieldset id="viewResouceSet">
					<legend>资源信息</legend>
					<img id="pic" src="getProccessPic.page?processId=<pg:cell colName="ID_"/>" />
				</fieldset>
				</form>
				</pg:beaninfo>
			</div>
  	</div>	
		<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
		</div>	
	</body>
