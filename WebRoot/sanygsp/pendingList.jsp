<%@ page session="true" language="java" contentType="text/html; charset=utf-8"%>
<%@ include file="/sanymbp/common/jsp/taglibs.jsp"%>

<!DOCTYPE html>
<html>
<head>
<title><pg:message code="sany.gsp.common.title" /></title>
<%@ include file="/sanygsp/common/header.jsp"%>
<style media="screen" type="text/css">
.ui-page {
	-webkit-backface-visibility: hidden;
}
</style>

<script type="text/javascript">
<!--
$(document).bind("mobileinit", function(){
});
$(
function() {
	
	$('#viewPlanDetail').live( 'tap', function() {
 		$.mobile.loadingMessageTextVisible = true;
 		$.mobile.showPageLoadingMsg();
		} );
		
} );
--> 
</script>
</head>

<body>
	<div data-role="page">

		<div data-role="header">
			<a href="#" data-rel="back" id="backlink" data-icon="back"> <pg:message	code="sany.gsp.common.backlink" /></a>
			<h1><pg:message	code="sany.gsp.pending.approve" /></h1>
			<a href="${ctx}/sanymbp/index.page" data-ajax="false"
				id="mainpage" data-icon="home" data-iconpos="notext"
				data-direction="reverse" class="ui-btn-right jqm-home"
				data-ajax="false"><pg:message code="sany.all.mobile.pending.index" />
			</a>
		</div>

		<div data-role="content">

			<ul data-role="listview">
			<pg:notempty actual="${pendingList}">
				<pg:list requestKey="pendingList">
				<li>
					<a data-ajax="false" id="viewPlanDetail" data-transition="pop" 	href="${ctx}/sanygsp/getPendingDigest.page?planCode=<pg:cell colName="planCode"/>&noIndex=<pg:cell colName="noIndex"/>&moduleCode=<pg:cell colName="moduleCode"/>">
						<h3><pg:cell colName="submitter"/>  <pg:cell colName="receiveDate"/>  <pg:message code='sany.gsp.tpp.plancode'/>: <pg:cell colName="planCode"/></h3>		
						<h3>
							<pg:message	code="sany.gsp.pending.process.theme" />：<pg:cell colName="topic"/>
						</h3>
					</a>
				</li>
				</pg:list>
				</pg:notempty>
				<pg:empty actual="${pendingList}">
						<h3>
							<pg:message	code="sany.gsp.pending.process.nopending" />
						</h3>
						</pg:empty>
				
				
			</ul>

		</div>

		<div data-role="footer" id="footer" class="footer-docs" data-theme="a" data-position="fixed">
			<p>&copy; 2012 ISANY</p>
		</div>

	</div>

</body>
</html>

