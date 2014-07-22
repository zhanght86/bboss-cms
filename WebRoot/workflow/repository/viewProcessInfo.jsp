<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>查看流程信息</title>
		<%@ include file="/common/jsp/css.jsp"%>
		<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
		<script type="text/javascript">
		var api = frameElement.api,W = api.opener;

		$(document).ready(function() {
			
	    	 $("#custombackContainer").load("<%=request.getContextPath()%>/workflow/repository/queryProcessHisVer.page #customContent", {processKey:'${processDef.KEY_}',version:'${processDef.VERSION_}'});
	    	 
	    	 // 优先显示实时任务还是历史任务Tab页
	    	 if (${not empty tabNum}){
	    		 setTab(1,4,{frameid:"tab4iframe",framesrc:"<%=request.getContextPath()%>/workflow/taskManage/ontimeTaskManager.page?processKey=${processDef.KEY_}"});
	    	 }
	 	    
	    });
		
		function viewProcessInfo(processKey,version) {
			var url="<%=request.getContextPath()%>/workflow/repository/viewProcessInfo.page?processKey="+processKey + "&version="+version;
			$.dialog({ title:'查看流程信息-'+processKey+'-v' + version,width:1100,height:620, content:'url:'+url});   
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
		
		
		</script>
	</head>
	<body>
	<div class="">
		<div class="tabbox">
		<ul class="tab" id="menu1">
			     <li><a href="javascript:void(0)" class="current" onclick="setTab(1,0)"><span><pg:message code="sany.pdp.workflow.detail.info"/></span></a></li>
			     <li><a href="javascript:void(0)" onclick="setTab(1,1)"><span>版本信息</span></a></li>
			     <li><a href="javascript:void(0)" onclick='setTab(1,2,{frameid:"tab2iframe",framesrc:"${pageContext.request.contextPath}/workflow/repository/getProccessXMLByKey.page?processKey=${processDef.KEY_}&version=${processDef.VERSION_}"})'><span><pg:message code="sany.pdp.workflow.process.xml"/></span></a></li>
			     <li><a href="javascript:void(0)" onclick='setTab(1,3,{frameid:"tab3iframe",framesrc:"${pageContext.request.contextPath}/workflow/repository/toProcessInstance.page?processKey=${processDef.KEY_}"})'><span>流程实例</span></a></li>
			     <li><a href="javascript:void(0)" onclick='setTab(1,4,{frameid:"tab4iframe",framesrc:"${pageContext.request.contextPath}/workflow/taskManage/ontimeTaskManager.page?processKey=${processDef.KEY_}"})'><span>实时任务</span></a></li>
			     <li><a href="javascript:void(0)" onclick='setTab(1,5,{frameid:"tab5iframe",framesrc:"${pageContext.request.contextPath}/workflow/taskManage/historyTaskManager.page?processKey=${processDef.KEY_}"})'><span>历史任务</span></a></li>
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
							<th width=85px >ID：</th>
							<td width=140px id="DEPLOYMENT_ID_"><pg:cell colName="ID_"/></td>
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
							<td id="DEPLOYMENT_TIME_"><pg:cell colName="DEPLOYMENT_TIME_" dateformat="yyyy-MM-dd HH:mm:ss"/></td>

							<th width=85px ><pg:message code="sany.pdp.workflow.processdef.path"/>：</th>
							<td id="RESOURCE_NAME_"><pg:cell colName="RESOURCE_NAME_"/></td>
							
							<th width=85px ><pg:message code="sany.pdp.workflow.picture.resource.name"/>：</th>
							<td id="DGRM_RESOURCE_NAME_"><pg:cell colName="DGRM_RESOURCE_NAME_"/></td>
						</tr>
					</table>
				</fieldset>
				<fieldset id="viewResouceSet">
					<legend>资源信息</legend>
					<img id="pic" src="${pageContext.request.contextPath}/workflow/repository/getProccessPic.page?processId=<pg:cell colName="ID_"/>" />
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
				<iframe id="tab2iframe"  frameborder="0" width="100%"  height="630" ></iframe>
		</ul>
		
		<ul id="tab4" style="display: none;">
			<iframe id="tab3iframe"  frameborder="0" width="100%"  height="500" ></iframe>
		</ul>
		
		<ul id="tab5" style="display: none;">
			<iframe id="tab4iframe"  frameborder="0" width="100%"  height="630" ></iframe>
		</ul>
		
		<ul id="tab6" style="display: none;">
			<iframe id="tab5iframe"  frameborder="0" width="100%"  height="630" ></iframe>
		</ul>
		
		<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
				</div>	
		</div>
</div>
	</body>
	</html>
