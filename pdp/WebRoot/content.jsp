<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ include file="/common/jsp/accessControl.jsp"%>

<%--
描述：Tab页与页面
作者：刘剑峰
版本：1.0
日期：2011-06-01
 --%>
 
 
 <%
  	
 %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8"/>
		<title></title>
		<%@ include file="/common/jsp/commonCssScript.jsp"%>
		<%@ include file="/common/jsp/jqureyEasyui.jsp"%>
		<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>			

		<script type="text/javascript">
			
			function addTab(title, url){				
				if ($('#tt').tabs('exists', title)){
					$('#tt').tabs('select', title);
				} 
				else {
					var content = '<iframe scrolling="auto" frameborder="0" src="'+url+'" style="width:100%;height:100%;"></iframe>';

					$('#tt').tabs('add',{
						title:title,
						content:content,
						closable:true
					});
				}
			}
		</script>
	</head>

	<body class="easyui-layout">	
		<div id="warpperDiv"  region="center" >
			<div id="tt" class="easyui-tabs" fit="true" border="false">
				<div title="工作台">
					<iframe scrolling="auto" frameborder="0" src="jf.page" style="width:100%;height:100%;"></iframe>
				</div>
			</div>
		</div>
	</body>
	
</html>

