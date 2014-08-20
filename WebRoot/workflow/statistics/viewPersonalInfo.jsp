<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>查看个人统计明细</title>
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
	 
	 <pg:param name="count_start_time" />
	 <pg:param name="count_end_time" />
	 <pg:param name="userName" />
   
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor1">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
			<th>流程名称</th>
	       	<th>业务主题</th>
	       	<th>处理节点</th>
	       	<th>节点工时</th>
	       	<th>处理工时</th>
	       	<th>预警时间</th>
	       	<th>超时时间</th>
	       	<th>备注</th>
       	</pg:header>	

	    <pg:list requestKey="listInfo">
	    	<pg:equalandlower length="dealTaskList" value="1">
	    		<tr>
			    	<td> <pg:cell colName="processName"/></td>  
			    	<td><pg:cell colName="businessKey"/></td>  
			    	<pg:notempty colName="dealTaskList" >
							<pg:list colName="dealTaskList" position="0" >
					           	<td><pg:cell colName="nodeName"/></td>
					           	<td><pg:cell colName="durationNode"/></td>
					           	<td><pg:cell colName="duration" /></td>
					           	<td>
						           	<pg:notempty colName="alertTime" >
					       				<span 
					       				<pg:equal colName="isAlertTime" value="1">style=" color: red";</pg:equal>>
							          		<pg:cell colName="alertTime" dateformat="yyyy-MM-dd HH:mm:ss"/>
							          	</span>
							        </pg:notempty>
		        				</td>
		        				<td>
		        					<pg:notempty colName="overTime" >
					       				<span 
					       				<pg:equal colName="isOverTime" value="1">style=" color: red";</pg:equal>>
							           		<pg:cell colName="overTime" dateformat="yyyy-MM-dd HH:mm:ss"/>
							           	</span>
							        </pg:notempty>
		        				</td>
		        				<td><pg:cell colName="remark" /></td>
				           	</pg:list>
			        </pg:notempty>
			    </tr>
	    	</pg:equalandlower>
	    	<pg:upper length="dealTaskList" value="1">
			   	<tr>
			    	<td rowspan="<pg:size colName="dealTaskList"/>"> <pg:cell colName="processName"/></td>  
			    	<td rowspan="<pg:size colName="dealTaskList"/>"><pg:cell colName="businessKey"/></td>  
			    	<pg:notempty colName="dealTaskList" >
							<pg:list colName="dealTaskList" position="0" >
					           	<td><pg:cell colName="nodeName"/></td>
					           	<td><pg:cell colName="durationNode"/></td>
					           	<td><pg:cell colName="duration" /></td>
					           	<td>
						           	<pg:notempty colName="alertTime" >
					       				<span 
					       				<pg:equal colName="isAlertTime" value="1">style=" color: red";</pg:equal>>
							          		<pg:cell colName="alertTime" dateformat="yyyy-MM-dd HH:mm:ss"/>
							          	</span>
							        </pg:notempty>
		        				</td>
		        				<td>
		        					<pg:notempty colName="overTime" >
					       				<span 
					       				<pg:equal colName="isOverTime" value="1">style=" color: red";</pg:equal>>
							           		<pg:cell colName="overTime" dateformat="yyyy-MM-dd HH:mm:ss"/>
							           	</span>
							        </pg:notempty>
		        				</td>
		        				<td><pg:cell colName="remark" /></td>
				           	</pg:list>
			        </pg:notempty>
			    </tr>
			    <pg:list colName="dealTaskList" start="1">
			        <tr>
			           	<td><pg:cell colName="nodeName"/></td>
			           	<td><pg:cell colName="durationNode" /></td>
			           	<td><pg:cell colName="duration" /></td>
		           		<td>
				           	<pg:notempty colName="alertTime" >
			       				<span 
			       				<pg:equal colName="isAlertTime" value="1">style=" color: red";</pg:equal>>
					          		<pg:cell colName="alertTime" dateformat="yyyy-MM-dd HH:mm:ss"/>
					          	</span>
					        </pg:notempty>
	        			</td>
	        			<td>
        					<pg:notempty colName="overTime" >
			       				<span 
			       				<pg:equal colName="isOverTime" value="1">style=" color: red";</pg:equal>>
					           		<pg:cell colName="overTime" dateformat="yyyy-MM-dd HH:mm:ss"/>
					           	</span>
					        </pg:notempty>
	        			</td>
	        			<td><pg:cell colName="remark" /></td>
			        </tr>
		        </pg:list>
		    </pg:upper>
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
