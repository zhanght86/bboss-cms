<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--
	描述：浏览统计计数数据查询页面
	作者：gw_hel
	版本：1.0
	日期：2012-08-27
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>浏览统计数据</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<link href="fusion/prettify.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" src="fusion/FusionCharts.js"></script>
<script type="text/javascript" src="fusion/prettify.js"></script>
<script type="text/javascript" src="fusion/json2.js"></script>
<script type="text/javascript">
	
	//页面加载时查询列表数据
	$(document).ready(function() {
	 	queryList(); 
	  });
	
	//查询浏览统计列表数据
	 function queryList() {	
	 	var siteId = "${siteId}";
	 	var startTime = "${startTime}";
	 	var endTime = "${endTime}";
	   	$("#custombackContainer").load("showPageBrowserCounterGatherList.freepage #customContent", { siteId:siteId, startTime:startTime, endTime:endTime }, function(){loadjs()});
	}
</script>
</head>
<body>	

	<div id="custombackContainer"></div>
  		
	<table width="100%" >
		<tr>
			<td align="center"  width="30%">
				<fieldset>
					<legend>访问量概况：</legend>
						<table width="100%" height="230" border="0" cellspacing="0" cellpadding="0">
							<th width="30%">&nbsp;</th>
							<th width="30%">PV</th>
       						<th width="30%">IP</th>
       						<pg:list requestKey="browserVisitInfoList" needClear="false">
								<tr>
       								<td><pg:cell colName="name"/>： </td><td><pg:cell colName="pv"/>&nbsp;<pg:cell colName="topPV"/></td><td><pg:cell colName="ip"/>&nbsp;<pg:cell colName="topIP"/></td>
       							</tr>
							</pg:list>
						</table>
				</fieldset>
			</td>
			<td align="center" >
				<div style="height: 14px">&nbsp;</div>
				<div id="queryContent_${type}"></div>
				<script type="text/javascript">
					var chart = new FusionCharts("fusion/Line.swf", "ChartId", "850", "250", "0", "0");
       				chart.setXMLUrl("<%=request.getContextPath()%>/counter/${browserCounterForwordPage}?type=${type}&siteId=${param.siteId}&startTime=${startTime}&endTime=${endTime}");
       				chart.render("queryContent_${type}");
  				</script>
			</td>
		</tr>
		</table>
		
		<br/>
		
		<table width="100%" border="0">
			<tr>
				<td align="center" colspan="2">
					<div id="queryContent_${type}_${type}"></div>
					<script type="text/javascript">
						var chart = new FusionCharts("fusion/Pie2D.swf", "ChartId", "600", "250", "0", "0");
	    				chart.setXMLUrl("<%=request.getContextPath()%>/counter/${browserTypeForwordPage}?type=${type}&siteId=${param.siteId}&startTime=${startTime}&endTime=${endTime}");
	    				chart.render("queryContent_${type}_${type}");
					</script>
				</td>
				<td align="center">
					<div id="queryContent_${type}_${type}_${type}"></div>
					<script type="text/javascript">
						var chart = new FusionCharts("fusion/Pie2D.swf", "ChartId", "600", "250", "0", "0");
	    				chart.setXMLUrl("<%=request.getContextPath()%>/counter/${browserIPForwordPage}?type=${type}&siteId=${param.siteId}&startTime=${startTime}&endTime=${endTime}");
	    				chart.render("queryContent_${type}_${type}_${type}");
					</script>
				<td>
			</tr>
		</table>
	
</body>
</html>
