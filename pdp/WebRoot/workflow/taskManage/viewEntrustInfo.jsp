<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看委托信息</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	
<body>
<div class="">
<pg:empty actual="${entrustList}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${entrustList}" >
	<div class="mcontent">
		<div id="searchblock">
			<form id="viewForm" name="viewForm">
					<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table3" id="tb">
					<pg:header>
				       	<th>应用系统</th>
				       	<th>流程名称</th>
				       	<th>授权人</th>
				       	<th>被授权人</th>
				       	<th>授权开始时间</th>
				       	<th>授权结束时间</th>
				       	<th>创建时间</th>
				       	<th>状态</th>
				      </pg:header>	
					<pg:list requestKey="entrustList">
					   	<tr height="25px">
					   		<td>
					    		<pg:empty colName="wf_app_name">
					    			所有应用
					    		</pg:empty>
					    		<pg:notempty colName="wf_app_name">
					    			<pg:cell colName="wf_app_name" />
					    		</pg:notempty>
					    	</td>    
					    	<td>
					    		<pg:empty colName="procdef_name">
					    			所有流程
					    		</pg:empty>
					    		<pg:notempty colName="procdef_name">
					    			<pg:cell colName="procdef_name" />
					    		</pg:notempty>
					    	</td>     
					    	<td><pg:cell colName="create_user"/></td>  
					    	<td><pg:cell colName="entrust_user"/></td>  
					    	<td><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss"/></td>   
					    	<td><pg:cell colName="end_date" dateformat="yyyy-MM-dd HH:mm:ss"/></td>  
					    	<td><pg:cell colName="create_date" dateformat="yyyy-MM-dd HH:mm:ss"/></td>  
					    	<td><pg:cell colName="sts"/></td>
					    </tr>
					 </pg:list>
				</table>
			</form>
		</div>
  	</div>
  	
	<div class="btnarea" >
		<a href="javascript:void(0)" class="bt_2" id="closeButton" onclick="closeDlg()"><span><pg:message code="sany.pdp.common.operation.exit"/></span></a>
	</div>
</pg:notempty>	
</div>
</body>
<script type="text/javascript">
var api = frameElement.api, W = api.opener;
$(document).ready(function() {
});
</script>
</head>
</html>
