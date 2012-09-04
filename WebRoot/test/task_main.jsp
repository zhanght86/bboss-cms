<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程处理</title>
<%@ include file="/common/jsp/css.jsp"%>
<link href="stylesheet/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery-1.5.2.min.js"></script>
<script type="text/javascript" src="js/commontool.js"></script>	
</head>
<body>
<div class="mcontent">
<div id="order_detail" style="display:block">
        <div class="tabbox">
          <ul class="tab" id="menu0">
            <li><a href="#" class="current" onclick="setTab(0,0)"><span>审核</span></a></li>
            <li><a href="#" onclick="setTab(0,1)"><span>领料申请单详细信息</span></a></li>
            <li><a href="#" onclick="setTab(0,2)"><span>流程图</span></a></li>
            </ul>
        </div>
        <div id="main0">
            <ul style="display:block;">
            	<%@ include file="task_history.jsp"%>
            	<pg:equal actual="${isDetail}" value="false">
	            	<%@ include file="task_step1.jsp"%>
                </pg:equal>
            </ul>
            <ul>
                <%@ include file="task_deail.jsp" %>
            </ul>
            <ul>
            	<div style="position:absolute;">
 		<img src="pic.jsp" style=" position:absolute;left:-600px; top:-10px;">
 		<!-- 给执行的节点加框 -->
 		<div style=" position:absolute;border:1px solid red;left:${coordinateObj.x-601 }px;top:${coordinateObj.y-11 }px;width:${coordinateObj.width }px;height:${coordinateObj.height }px;"></div>
 </div>
            </ul>
        </div>
	</div>
        </div>
</body>
</html>