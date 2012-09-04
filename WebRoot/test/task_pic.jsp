<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div>
 		<img src="pic.jsp" style="position:absolute; left:0px; top:0px;">
 		<!-- 给执行的节点加框 -->
 		<div style="position:absolute; border:1px solid red;left:${coordinateObj.x-1 }px;top:${coordinateObj.y-1 }px;width:${coordinateObj.width }px;height:${coordinateObj.height }px;"></div>
 </div>