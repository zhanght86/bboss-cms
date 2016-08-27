<%@ page session="true" language="java" contentType="text/html; charset=utf-8"%>

<%@page import="com.frameworkset.platform.security.AccessControl"%>

<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>
<%
String planCode = (String)request.getAttribute("planCode");
String moduleCode = (String)request.getAttribute("moduleCode");
String noIndex = (String)request.getAttribute("noIndex");
AccessControl control = AccessControl.getAccessControl();
String userId = control.getUserAccount();

%>

<!DOCTYPE html>
<html>
<head>
<title><pg:message code="sany.gsp.common.title" /></title>
<%@ include file="/sanygsp/common/header.jsp"%>
<script src="${ctx}/sanygsp/js/jquery.mobile.simpledialog2.min.js"></script>
<link href="${ctx}/sanygsp/css/jquery.mobile.simpledialog.min.css" rel="stylesheet" type="text/css"></link>

<style media="screen" type="text/css">
.ui-page {
	-webkit-backface-visibility: hidden;
}
</style>

<script type="text/javascript">
	jQuery(document).ready(function() { 

		 //表单提交 通过
		 $("#approve").bind("click", function() {
			 
			 $("#approveForm").submit();
			 
		  });
	} )

</script>

<script type="text/javascript">
<!--

$(document).delegate('#reject', 'click', function() {
	<pg:equal length="request:rejectList" value="1">
		
		if(confirm("确实要驳回"))//驳回
			{
				<pg:list requestKey="rejectList" position="0">
					$("#approveForm #pass").val("<pg:cell colName='nodeCode'/>");
				</pg:list>									
				$("#approveForm").submit();
			}
			return false;
			
	</pg:equal>
	
	
	<pg:notequal length="request:rejectList" value="1">
	  // NOTE: The selector can be whatever you like, so long as it is an HTML element.
	  //       If you prefer, it can be a member of the current page, or an anonymous div
	  //       like shown.
	 $('<div>').simpledialog2({
	    mode: 'blank',
	    headerText: '请选择驳回节点',
	    headerClose: true,
	    dialogAllow: true,
	    dialogForce: true,
	    blankContent : 
			"<form method='post' id='approveForm2' name='approveForm2' data-ajax='false' action='${ctx}/sanygsp/approve.page'>"	+
// 				+"<table>"+
					"<fieldset data-role='controlgroup'>"+
						<pg:list requestKey="rejectList" >
					     	"<input type='radio' name='pass' id='pass<pg:rowid increament="1"/>' value='<pg:cell colName="nodeCode"/>'/>"+
					     	"<label for='pass<pg:rowid increament="1"/>'><pg:cell colName="nodeName"/></label>"+
						</pg:list>		
					"</fieldset>"+
				"<input type='hidden' name='userId' id='userId' value='<%=userId%>'/>"+
				"<input type='hidden' name='planCode' id='planCode' value='<%=planCode %>'/>"+
				"<input type='hidden' name='planItemCode' id='planItemCode' value='<%=noIndex%>'/>"+
				"<input type='hidden' name='opinion' id='opinion' value='"+$("#approveForm #opinion").val()+"'/>"+	
// 				"</table>"+
				"<a id='rejectButton' data-role='button' href='#' onClick='formSubmit2()'>驳回</a>"+
				"<a rel='close' data-role='button' href='#'>关闭</a>"
			+"</form>"
	  });
		</pg:notequal>
	})
--> 
</script>

</head>
<body>
	<div data-role="page">

		<div data-role="header">
			<a href="javascript:void(0)" data-rel="back" id="backlink" data-icon="back">
				<pg:message code="sany.gsp.common.backlink" />
			</a>
			<h1><pg:message code="sany.gsp.tpp.approve.process" /></h1>
			<a href="${ctx}/sanygsp/showPendingList.page" id="mainPage" 
				data-ajax="false" data-role="button"><pg:message code="sany.all.mobile.pending.index" />
			</a>
		</div>

		<div data-role="content">
			<div data-role="collapsible" data-collapsed="false" data-mini="true"
				data-iconpos="right">
				<h3><pg:message code="sany.gsp.tpp.aprove.record" /></h3>
				<pg:list requestKey="doneVos">
					<div data-role="fieldcontain">
						<label><pg:cell colName="userName"/></label>
						<label><pg:cell colName="realDate" dateformat="yyyy-MM-dd HH:mm"/></label>						
						<label><pg:cell colName="boContent"/></label>
					</div>
				</pg:list>				
			</div>

			<div data-role="collapsible" data-collapsed="false" data-mini="true"data-iconpos="right">

				<h3><pg:message code="sany.gsp.tpp.aprove.record" /></h3>
				<form method="post" id="approveForm" name="approveForm" data-ajax="false" action="${ctx}/sanygsp/approve.page">
					<input type="hidden" name="userId" id="userId" value="<%=userId%>" />
					<input type="hidden" name="planCode" id="planCode"	value="<%=planCode %>" />
					<input type="hidden" name="planItemCode" id="planItemCode" value="<%=noIndex%>" /> 
					<input type="hidden" name="pass" id="pass" value="true" />
					<textarea type="hidden" name="opinion" id="opinion"><pg:size requestKey="rejectList"/></textarea>
				</form>
			</div>

		</div>

		<div data-role="footer" id="footer" data-position="fixed" data-theme="a">

			<div data-role="navbar">
				<ul>
					<li><a href="javascript:void(0)" data-icon="check" id="approve" data-role="button" data-iconpos="right"><pg:message code="sany.gsp.pending.approve.pass" /></a></li>
					<li><a href="javascript:void(0)" data-icon="delete" id="reject" data-role="button" data-iconpos="right"><pg:message code="sany.gsp.pending.approve.reject" /></a></li>
				</ul>
			</div>
			<!-- /navbar -->
		</div>

	</div>
</body>
<script type="text/javascript">
 
function formSubmit2(){
	 		  
	 if(typeof($("#approveForm2  input:checked").val())=='undefined'){
		 alert("<pg:message code='sany.gsp.tpp.prompt.selectonenode' />");
		 return;
		}
	 
	 $("#approveForm2").submit();
}

</script>
</html>

