<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>查看流程信息</title>
		<%@ include file="/common/jsp/css.jsp"%>
		<script type="text/javascript">
		var api = frameElement.api,W = api.opener;

		$(document).ready(function() {
			var name = "${processDef.NAME_}";
	    	 $("#custombackContainer").load("queryProcessHisVer.page #customContent", {processName:name}); 
	       });
		
		function viewProcessInfo(deploymentId) {
			var url="<%=request.getContextPath()%>/workflow/repository/viewHisProcessInfo.page?deploymentId="+deploymentId;
			W.$.dialog({ title:'查看历史流程信息',width:1100,height:560, content:'url:'+url,parent:api,currentwindow:this});   
		}
		
		function refeProcessInfo(DEPLOYMENT_ID_) {
			$.ajax({
		 	 	type: "POST",
				url : "refeProcessInfo.page",
				data :{"deploymentId":DEPLOYMENT_ID_},
				dataType : 'json',
				async:false,
				beforeSend: function(XMLHttpRequest){
					 	XMLHttpRequest.setRequestHeader("RequestType", "ajax");
					},
				success : function(data){
					$("#DEPLOYMENT_ID_").text(data.deployment_ID_);
					$("#CATEGORY_").text(data.category_);
					$("#NAME_").text(data.name_);
					$("#KEY_").text(data.key_);
					$("#VERSION_").text(data.version_);
					$("#DEPLOYMENT_NAME_").text(data.deployment_NAME_);
					$("#DEPLOYMENT_TIME_").text(data.deployment_TIME_STRING_);
					$("#RESOURCE_NAME_").text(data.resource_name_);
					$("#DGRM_RESOURCE_NAME_").text(data.dgrm_resource_name_);
					
					document.getElementById("pic").src = "getProccessPic.page?processId="+data.id_;
				}	
			 });
		}
		
		function makeOneCheck(checkBox) {
			var checkBoxs = document.getElementsByName("CK");
			for (var i=0; i<checkBoxs.length; i++) {
				if (checkBoxs[i].checked && checkBoxs[i].value != checkBox.value) {
					checkBoxs[i].checked = false;
				}
			}
		}
		
		function setTab(m, n) {
			 var lit = document.getElementById("menu"+m).getElementsByTagName("a");
			 var lim = document.getElementById("main"+m).getElementsByTagName("ul");
			 for (i=0;i<lit.length;i++) {
				 lit[i].className=i==n?"current":"";
				 lim[i].style.display=i==n?"block":"none";
			 }
		}
		</script>
	</head>
	<body>
	<div class="form">
		<div class="tabbox">
		<ul class="tab" id="menu1">
			     <li><a href="javascript:void(0)" class="current" onclick="setTab(1,0)"><span><pg:message code="sany.pdp.workflow.detail.info"/></span></a></li>
			     <li><a href="javascript:void(0)" onclick="setTab(1,1)"><span><pg:message code="sany.pdp.workflow.history.info"/></span></a></li>
		</ul>
		</div>
		<div id="main1">
		<ul  id="tab1" style="display:block;">
			<div class="mcontent">
			<div id="searchblock">
				<pg:beaninfo requestKey="processDef">
				<form id="viewForm" name="viewForm">
				<fieldset>
				<legend><pg:message code="sany.pdp.workflow.operation.workflow.info"/></legend>
					<table border="0" cellpadding="0" cellspacing="0" class="table4">
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
		</ul>
		<ul id="tab2" style="display: none;">
					<div id="custombackContainer">
					</div>		
		</ul>
		<ul id="tab3" style="display: none;">
					<pg:beaninfo requestKey="processDef">
					<%@ include file="viewProcessPic.jsp"%>	
					</pg:beaninfo>
		</ul>
		<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
				</div>	
		</div>
</div>
	</body>
	</html>
