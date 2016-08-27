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
<title>操作统计数据</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
function setFrame2(){
	var src = document.getElementById("frame2").src;
	if(null==src||""==src){
		document.getElementById("frame2").src="operateCounterStaticByDayQuery.jsp?siteId="+${param.siteId};
	}
	
}
function setFrame3(){
	var src = document.getElementById("frame3").src;
	if(null==src||""==src){
		document.getElementById("frame3").src="operateCounterStaticByWeekQuery.jsp?siteId="+${param.siteId};
	}
}
function setFrame4(){
	var src = document.getElementById("frame4").src;
	if(null==src||""==src){
		document.getElementById("frame4").src="operateCounterStaticByMonthQuery.jsp?siteId="+${param.siteId};
	}
}
function setFrame5(){
	var src = document.getElementById("frame5").src;
	if(null==src||""==src){
		document.getElementById("frame5").src="operateCounterStaticByYearQuery.jsp?siteId="+${param.siteId};
	}
}
function setFrame6(){
	var src = document.getElementById("frame6").src;
	if(null==src||""==src){
		document.getElementById("frame6").src="operateCounterHistoryCompare.jsp?siteId="+${param.siteId};
	}
}
</script>
</head>
<body>
	<div class="mcontent">
		<div class="tabbox" >
			<ul class="tab" id="menu2">
				<li><a href="javascript:void(0)" class="current" onclick="setTab(2,0)"><span>应用系统操作记录</span></a></li>
				
				  <li><a href="javascript:void(0)" onclick="setFrame2();setTab(2,1)"><span>模块操作量日统计排名</span></a></li>
				  <li><a href="javascript:void(0)" onclick="setFrame3();setTab(2,2)"><span>模块操作量本周统计排名</span></a></li>
				<li><a href="javascript:void(0)"  onclick="setFrame4();setTab(2,3)"><span>模块操作量月统计排名</span></a></li>
				<li><a href="javascript:void(0)"  onclick="setFrame5();setTab(2,4)"><span>模块操作量年统计排名</span></a></li> 
				 <li><a href="javascript:void(0)"  onclick="setFrame6();setTab(2,5)"><span>历史数据比较</span></a></li> 
			</ul>
		</div>
		<div id="main2">
			<ul  id="tab1" style="display:block;">
				<%-- <jsp:include page="operateCounterQuery.jsp?siteId=${param.siteId}"  flush="true"/> --%>
				<iframe id="frame1" src="operateCounterQuery.jsp?siteId=${param.siteId}"  frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
			 
			 <ul id="tab2" style="display: none;">
				<iframe id="frame2"   frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
			<ul id="tab3" style="display: none;">
				<iframe id="frame3"   frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
			 <ul id="tab4" style="display: none;">
			 <iframe id="frame4"  frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
			<ul id="tab5" style="display: none;">
			<iframe id="frame5"   frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
			 <ul id="tab5" style="display: none;">
			<iframe id="frame6"   frameborder="0" width="100%"  height="630" ></iframe>
			</ul>
		</div>
	</div>
</body>
</html>
