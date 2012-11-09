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
   	{ batchId:batchId}, function(){loadjs()});browserCounterDetail
} 
<pg:header>
        	<th style="display: none">应用名称</th> 	
       		<th style="display: none">模块名称</th>
       		<th style="display: none">页面名称</th>
       		<th>浏览页面地址</th>
       		<th>浏览来源地址</th>
       		<th style="display: none">浏览器类型</th>
       		<th style="display: none">用户</th>
       		<th style="display: none">用户IP地址</th>
       		<th style="display: none">浏览时间</th>
       		<th>功能编号</th>
       		<th>功能路径</th>
       	</pg:header>	

      <pg:list requestKey="browserCounterDetail">
   		<tr>
   		        <td style="display: none"><pg:cell colName="siteName"/></td>
                <td style="display: none"><pg:cell colName="channelName"/></td>
                <td style="display: none"><pg:cell colName="docName"/></td>  
        		<td><pg:cell colName="pageURL" maxlength="40" replace="..."/></td>  
        		<td><pg:cell colName="referer" maxlength="40" replace="..."/></td>  
        		<td style="display: none"><pg:cell colName="browserType" /></td>  
        		<td style="display: none"><pg:cell colName="browserUser" /></td>  
        		<td style="display: none"><pg:cell colName="browserIp" /></td>  
        		<td style="display: none"><pg:cell colName="browserTime"  dateformat="yyyy-MM-dd  HH:mm:ss"/></td>
        		<td><pg:cell colName="moduleCode"/></td>
        		<td><pg:cell colName="modulePath"/></td>
        </tr>
        </pg:list>
--%>
</script>
</head>
<body>
	<div id="customContent">
		<div class="title_box">
			<strong>浏览日志明细</strong>
		</div>
		<div id="changeColor" style="width: 1150px; overflow: scroll;">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="stable" id="tb">
				
        <tr>
        <td><pg:header>
        <th>浏览页面地址</th>
        </pg:header>
        </td>
        <td>
        <pg:list requestKey="browserCounterDetail">
        <pg:cell colName="pageURL" maxlength="80" replace="..."/>
        </pg:list>
        </td>
        </tr>
        <tr>
        <td><pg:header>
        <th>浏览来源地址</th>
        </pg:header>
        </td>
        <td>
        <pg:list requestKey="browserCounterDetail">
        <pg:cell colName="referer" maxlength="80" replace="..."/>
        </pg:list>
        </td>
        </tr>
        <tr>
        <td><pg:header>
        <th style="display: none">功能编号</th>
        </pg:header>
        </td>
        <td>
        <pg:list requestKey="browserCounterDetail">
        <pg:cell colName="moduleCode" maxlength="80" replace="..."/>
        </pg:list>
        </td>
        </tr>
        <tr>
        <td><pg:header>
        <th style="display: none">功能路径</th>
        </pg:header>
        </td>
        <td>
        <pg:list requestKey="browserCounterDetail">
        <pg:cell colName="modulePath" maxlength="80" replace="..."/>
        </pg:list>
        </td>
        </tr>
			</table>
		</div>
	</div>
	<div id = "custombackContainer"></div>
</body>
</html>