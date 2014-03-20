<%@ page session="true" language="java"	contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title><pg:message code="sany.gsp.common.title" /></title>
<%@ include file="/sanygsp/common/header.jsp"%>
<script type="text/javascript">
<!--
$(document).bind("mobileinit", function(){
});
$(
function() {
	
	$('#approvePage').live( 'tap', function() {
 		$.mobile.loadingMessageTextVisible = true;
 		$.mobile.showPageLoadingMsg();
		} );
	
	$('#detailPage').live( 'tap', function() {
 		$.mobile.loadingMessageTextVisible = true;
 		$.mobile.showPageLoadingMsg();
		} );
		
} );
--> 
</script>
</head>
<body>
	<div data-role="page">

		<div data-role="header" data-position="fixed">
			<a href="#" data-rel="back" id="backlink" data-icon="back"> 
				<pg:message code="sany.gsp.common.backlink" />
			</a>
			<h1><pg:message code="sany.gsp.tpp.digest" /></h1>
			<div data-type="horizontal" data-role="controlgroup" class="ui-btn-right ui-corner-all ui-controlgroup ui-controlgroup-horizontal">  	          
			   	<a href="${ctx}/sanygsp/approveView.page?boId=${tppPlanVO.code}&noIndex=${noIndex}&moduleCode=${moduleCode}"					 
					id="approvePage" data-ajax="false" data-role="button"><pg:message code="sany.gsp.tpp.approve.process" /></a>
			    
				<a href="${ctx}/sanygsp/detail.page?planCode=${tppPlanVO.code}&noIndex=${noIndex}&moduleCode=${moduleCode}"
					id="detailPage" data-ajax="false" data-role="button" ><pg:message code="sany.gsp.tpp.process.detail" /></a>
					
					
			</div>
		</div>

		<div data-role="content">

			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.plancode" />：</label> <label>${tppPlanVO.code}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.pending.process.theme" />：</label> <label>${tppPlanVO.title}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.submitter" />：</label> <label>${tppPlanVO.reqUserName}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.company" />：</label> <label>${tppPlanVO.orgName}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.contact" />：</label> <label>${tppPlanVO.telephone}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.workerno" />：</label> <label>${tppPlanVO.workNumber}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.department" />：</label> <label>${tppPlanVO.department}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.factory" />：</label> <label>${tppPlanVO.plantName}</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.submit.date" />：</label> <label><fmt:formatDate value="${tppPlanVO.reqDateTime}" pattern="yyyy-MM-dd"/></label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.plantype" />：</label> 
				<label>
				<c:choose>
					<c:when test="${tppPlanVO.planType=='0'}">
						<pg:message code="sany.gsp.tpp.tempplan" />
					</c:when>
					<c:otherwise>
						<pg:message code="sany.gsp.tpp.partplan" />
					</c:otherwise>
				 </c:choose>
				</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.tpp.urgent.plan" />：</label> 
				<label>
				<c:choose>
					<c:when test="${tppPlanVO.isUrgency=='0'}">
						<pg:message code="sany.gsp.tpp.no" />
					</c:when>
					<c:otherwise>
						<pg:message code="sany.gsp.tpp.yes" />
					</c:otherwise>
				 </c:choose>					
				</label>
			</div>
			<div data-role="fieldcontain">
				<label><pg:message code="sany.gsp.budget.amount" />：</label> <label>${tppPlanVO.planMoney}</label>
			</div>

		</div>

		<div data-role="footer" id="footer" class="footer-docs" data-theme="a"	data-position="fixed">
			<p>&copy; 2012 ISANY</p>
		</div>

	</div>

</body>
</html>

