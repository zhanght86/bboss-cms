<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>应用系统情况浏览</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>

</head>
<body>
	<sany:menupath />
	<div class="mcontent">
		<div class="tabbox" >
			<ul class="tab" id="menu2">
				<li><a href="javascript:void(0)" class="current" onclick="setTab(2,0)"><span>应用系统概况</span></a></li>
				<li><a href="javascript:void(0)" onclick="setTab(2,1)"><span>应用系统模块概况</span></a></li>
			</ul>
		</div>
		<div id="main2">
			<ul  id="tab1" style="display:block;">
				<jsp:include page="appManager.jsp"  flush="true"/>
			</ul>
			<ul id="tab2" style="display: none;">
				<iframe src="moduleManager.jsp"  frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
		</div>
	</div>
</body>
</html>
