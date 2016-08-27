<%@page import="com.frameworkset.util.StringUtil"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程定义XML展示</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/include/syntaxhighlighter/styles/SyntaxHighlighter.css"></link>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shCore.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJava.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushXml.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/include/syntaxhighlighter/shBrushJScript.js"></script>

<body>
<pg:empty actual="${processXML}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 

<div class="form">
	<div class="mcontent">
		<div id="searchblock">
			<form id="viewForm" name="viewForm">
					<div class="shadow">
						<div class='info'>
							<p>
								<div id='detail'>
									<a href="<%=request.getContextPath()%>/workflow/repository/downProcessXMLandPicZip.page?processKey=${processKey}&version=${version}" >下载</a>
									<table border='1' cellspacing='0' width='100%' id='resultsTable'>
										<tbody>
											<tr><td><pre name='code' class='xml'><pg:cell actual="${processXML}" htmlEncode='true'/></pre></td></tr>
										</tbody>
									</table>
								</div>
							</p>
						</div>
					</div>
			</form>
		</div>
  	</div>	
  		
</div>
</body>
<script type="text/javascript">

var trHtml ="";

if(!$.browser.msie) {
		
	dp.SyntaxHighlighter.ClipboardSwf = '${pageContext.request.contextPath}/include/syntaxhighlighter/clipboard.swf';
	dp.SyntaxHighlighter.HighlightAll('code');
}else {
	
	
}	

</script>

</script>
</head>
</html>
