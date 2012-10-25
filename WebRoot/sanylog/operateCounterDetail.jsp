<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>



<%--
	
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>操作日志详细</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/html2/js/common.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
<%-- $(document).ready(function() {
	//$('#tb').click(function() {
	//	checkErrorLogDetail(${param.batchId});
    //});
	showWriteOffDetail(${param.batchId});
}); 
function checkErrorLogDetail(batchId){
	var url='<%=request.getContextPath()%>/sfa/wirte/checkErrorLogDetail.page?batchId='+batchId;
	var title='日志明细';
	//alert(url);
	$.dialog({ id:'iframeNewId', title:title,width:650,height:450, content:'url:'+url}); 
}

function errorLogDetail(id){
    var url='<%=request.getContextPath()%>/sfa/wirte/checkErrorLogDetail.page?id='+id;
	$.dialog({title:'冲销导入日志消息详情',width:550,height:350, content:'url:'+url});
}

function showWriteOffDetail(batchId){
	
   	$("#custombackContainer").load("showWriteOffDetail.page #customContent", 
   	{ batchId:batchId}, function(){loadjs()});
} --%>
</script>
</head>
<body>
	<div id="customContent">
		<div class="title_box">
			<strong>操作日志明细</strong>
		</div>
		<div id="changeColor" style="width: 1150px; overflow: scroll;">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="stable" id="tb">
				<pg:header>

					<th>应用名称</th> 	
		       		<th>模块名称</th>
		       		<th>页面名称</th>
		       		<th>页面URL</th>
		       		<th>来源页面</th>
		       		<th>浏览器类型</th>


				</pg:header>
				<pg:list  requestKey="operateCounterDetail">
					<tr>
				<td><pg:cell colName="appName"/></td>    
                <td><pg:cell colName="moduleName"/></td> 
                <td><pg:cell colName="pageName"/></td>    
        		<td><pg:cell colName="pageURL" maxlength="50" replace="..."/></td>  
        		<td><pg:cell colName="referer" maxlength="50" replace="..."/></td>                          
        		<td><pg:cell colName="browserType"  /></td>
					</tr>
				</pg:list>
				<pg:header>

					<th>操作人</th>
		       		<th>操作类型</th>
		       		<th>操作时间</th>
		       		<th>操作内容</th>
		       		<th>操作IP</th>


				</pg:header>
				<pg:list  requestKey="operateCounterDetail">
					<tr>
				 <td><pg:cell colName="operator" /></td>  
        		<td><pg:cell colName="operation" /></td>                             
        		<td><pg:cell colName="operTime" dateformat="yyyy-MM-dd  HH:mm:ss" /></td>
        		<td><pg:cell colName="operContent" /></td>    
        		<td><pg:cell colName="operateIp" /></td>                             
					</tr>
				</pg:list>
			</table>
		</div>
	</div>
	<div id = "custombackContainer"></div>
</body>
</html>