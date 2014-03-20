<%@ page session="true" language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>
<%@page import="com.qeweb.sanygsp.model.tpp.vo.TppPlanVO"%>
<%@page import="com.qeweb.sanygsp.model.tpp.vo.TppPlanDetailVO"%>
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
	
	$('#mainPage').live( 'tap', function() {
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
			<h1><pg:message code="sany.gsp.tpp.process.detail" /></h1>	
			<div data-type="horizontal" data-role="controlgroup" class="ui-btn-right ui-corner-all ui-controlgroup ui-controlgroup-horizontal">	
				<a href="${ctx}/sanygsp/approveView.page?planCode=${planCode}&noIndex=${noIndex}&moduleCode=${moduleCoe}" id="approvePage" 
					data-ajax="false" data-role="button"><pg:message code="sany.gsp.tpp.approve.process" />
				</a>
				<a href="${ctx}/sanygsp/showPendingList.page" id="mainPage" 
					data-ajax="false" data-role="button"><pg:message code="sany.all.mobile.pending.index" />
				</a>
			</div>
		</div>		
		
		<div data-role="content">
			
			<c:forEach items="${detailVos}" var="detailVo" varStatus="status">
			
			
			<div data-role="content" data-theme="b">
				<div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.material.code" />：</label>
					<label>${detailVo.materialCode}</label>
	            </div>
	            <div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.material.description" />：</label>
					<label>${detailVo.materialName}</label>
	            </div>
	             <div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.material.unit" />：</label>
					<label>${detailVo.materialUnit}</label>
	            </div>
	            <div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.material.amount" />：</label>
					<label>${detailVo.reqNum}</label>
	            </div>
	            <div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.greaterthan.50w" />：</label>
					<label>					
						<c:choose>
							<c:when test="${detailVo.reqNum>50}"><pg:message code="sany.gsp.tpp.yes" /></c:when>
							<c:otherwise><pg:message code="sany.gsp.tpp.no" /> </c:otherwise>
						</c:choose>
					 </label>
	            </div>
	             <div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.expected.arrival.time" />：</label>
					<label><fmt:formatDate value="${detailVo.wantDate}" pattern="yyyy-MM-dd"/></label>
	            </div>
				<div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.reference.provider" />：</label>
					<label>${detailVo.refSupplier}</label>
	            </div>
	            <div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.new" />：</label>
					<label>					
						<c:choose>
							<c:when test="${detailVo.refSupplier==1}"><pg:message code="sany.gsp.tpp.yes" /></c:when>
							<c:otherwise><pg:message code="sany.gsp.tpp.no" /></c:otherwise>
						</c:choose>
					</label>
	            </div>
	            <div data-role="fieldcontain"> 
					<label><pg:message code="sany.gsp.tpp.remark" />：</label>
					<label>${detailVo.remark}</label>
	            </div>
            </div>
            
            </c:forEach>
			      
		</div>

		<div data-role="footer" id="footer" class="footer-docs" data-theme="a" data-position="fixed">
			<p>&copy; 2012 ISANY</p>
		</div>
		
	</div>

</body>
</html>

