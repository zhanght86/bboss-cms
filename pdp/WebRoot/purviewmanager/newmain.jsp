<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>权限管理首页</title>
		<%@ include file="/common/jsp/css.jsp"%>
		 <script type="text/javascript">
		

		$(document).ready(function() {
			
	    	 
	 	    
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
	<sany:menupath menuid="purviewmanager"/>
 
		<div class="tabbox">
		<ul class="tab" id="menu1">
			     <li><a href="javascript:void(0)" class="current" onclick='setTab(1,0)'><span><pg:message code="sany.pdp.user.organization.manage"/></span></a></li>
			     
			     <li><a href="javascript:void(0)" onclick='setTab(1,1,{frameid:"tab1iframe",framesrc:"rolemanager/role.jsp"})'><span><pg:message code="sany.pdp.role.manage"/></span></a></li>
			     <li><a href="javascript:void(0)" onclick='setTab(1,2,{frameid:"tab2iframe",framesrc:"jobmanager/jobinfo.jsp"})'><span><pg:message code="sany.pdp.job.manage"/></span></a></li>
			     <li><a href="javascript:void(0)" onclick='setTab(1,3,{frameid:"tab3iframe",framesrc:"groupmanager/group_main.jsp"})'><span><pg:message code="sany.pdp.user.group.manage"/></span></a></li>
			     <li><a href="javascript:void(0)" onclick='setTab(1,4,{frameid:"tab4iframe",framesrc:"resmanager/res_main.jsp"})'><span><pg:message code="sany.pdp.resource.manage"/></span></a></li>
		</ul>
		</div>
		<div id="main1">
		<ul  id="tab1" style="display:block;">
			<iframe id="tab0iframe"  frameborder="0" width="100%"  src="userorgmanager/org/org_main.jsp"  height="95%" ></iframe>	
		</ul>
		<ul id="tab2" style="display: none;">
					<iframe id="tab1iframe"  frameborder="0" width="100%"  height="95%" ></iframe>
		</ul>
		<ul id="tab3" style="display: none;">
				<iframe id="tab2iframe"  frameborder="0" width="100%"  height="95%" ></iframe>
		</ul>
		
		<ul id="tab4" style="display: none;">
			<iframe id="tab3iframe"  frameborder="0" width="100%"  height="95%" ></iframe>
		</ul>
		
		 <ul id="tab5" style="display: none;">
			<iframe id="tab4iframe"  frameborder="0" width="100%"  height="95%" ></iframe>
		</ul>
		
		
	</body>
	</html>
