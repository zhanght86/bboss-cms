<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>选择流程</title>
<%@ include file="/common/jsp/common-css-lhgdialog.jsp"%>
<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/html3/js/tab.js"></script>

<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
	
	$("#bussinesstree").load("../workflowdemo/businessDemoTree.jsp");
	
});

//开启流程实例
function toStartProc() {
	
	var processKey = $("#bussinesstree input[name=processkey]:checked").val();
	
	W.toStartProc(processKey);
	
	closeDlg();
}

</script>
</head>

<body>

<div id="bussinesstree" title="业务类型流程树" style="padding: 10px; overflow: auto; height: 300px;"></div>

<div align="center">
	<a href="javascript:void(0)" class="bt_sany" onclick="toStartProc();">确定</a>
	<a href="javascript:void(0)" class="bt_sany" onclick="closeDlg();"><pg:message code="sany.pdp.common.operation.close"/></a>
</div>	
</body>
</html>
