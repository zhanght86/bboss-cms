<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看流程统计明细</title>
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
	
<body>
<div class="">
<pg:empty actual="${listInfo}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${listInfo}" >
	<pg:pager scope="request"  data="listInfo" desc="true" isList="false" >
	 
	 <pg:param name="processKey" />
	 <pg:param name="count_start_time" />
	 <pg:param name="count_end_time" />
   
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
			<th>业务主题</th>
	       	<th>流程启动人</th>
	       	<th>是否办结</th>
	       	<th>耗时</th>
	       	<th>转办次数</th>
	       	<th>委托次数</th>
	       	<th>废弃次数</th>
	       	<th>撤销次数</th>
	       	<th>驳回次数</th>
       	</pg:header>	

	    <pg:list requestKey="listInfo">
		   	<tr>
		    	<td><pg:cell colName="businessKey"/></td>  
		    	<td><pg:cell colName="startUserName"/></td>  
		    	<td>
		    		<pg:null colName="endTime">否</pg:null>
		    		<pg:notnull colName="endTime">是</pg:notnull>
		    	</td>   
		    	<td><pg:cell colName="duration"/></td>  
		    	<td><pg:cell colName="delegateNum"/></td>  
		    	<td><pg:cell colName="entrustNum"/></td>
		    	<td><pg:cell colName="discardNum"/></td>  
		    	<td><pg:cell colName="cancelNum"/></td>  
		    	<td><pg:cell colName="rejectNum"/></td>
		    </tr>
		 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>

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
