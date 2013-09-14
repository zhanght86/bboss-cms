<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>


<%--  
	描述：浏览统计计数数据查询页面   <jsp:include page='browserCounterQuery.jsp?siteId=${param.siteId}'  flush='true' />
	作者：qingl2   
	版本：1.0
	日期：2012-08-25
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">
function setFrame1(){
	var src = document.getElementById("frame1").src;
	if(null==src||""==src){
		document.getElementById("frame1").src="browserCounterQuery.jsp?siteId="+${param.siteId};
	}
	
}
function setFrame2(){
	var src = document.getElementById("frame2").src;
	if(null==src||""==src){
		document.getElementById("frame2").src="browserCounterGatherQuery.jsp?siteId="+${param.siteId};
	}
}
function setFrame3(){
	var src = document.getElementById("frame3").src;
	if(null==src||""==src){
		document.getElementById("frame3").src="pageCounterQuery.jsp?siteId="+${param.siteId};
	}
}
function setFrame4(){
	var src = document.getElementById("frame4").src;
	if(null==src||""==src){
		document.getElementById("frame4").src="browserCounterView.jsp?siteId="+${param.siteId};
	}
}
</script>
<title>浏览统计数据</title>
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>

</head>
<body>
	<div class="mcontent">
		<div class="tabbox" >
			<ul class="tab" id="menu2">
				<li><a href="javascript:void(0)" onclick="setFrame1();setTab(2,0)"><span>应用访问记录</span></a></li>
				<li><a href="javascript:void(0)" onclick="setFrame2();setTab(2,1)"><span>应用访问页面汇总</span></a></li>
				<li><a href="javascript:void(0)"  onclick="setFrame4();setTab(2,2)"><span>应用访问概况</span></a></li>
				<li><a href="javascript:void(0)" class="current" onclick="setTab(2,3)"><span>页面使用情况统计</span></a></li>
			</ul>
		</div>
		<div id="main2">
			<ul id="tab1" style="display:none;">
				<iframe   frameborder="0" width="100%"  height="630"  flush="true" id="frame1"></iframe>
			</ul>
			<ul id="tab2" style="display: none;">
				<iframe  frameborder="0" width="100%"  height="630"  flush="false"  id="frame2"></iframe>
			</ul>
			<ul id="tab3" style="display:none;">
			<iframe   frameborder="0" width="100%"  height="1000"  flush="true" id="frame4"></iframe>
				<%-- <jsp:include page="browserCounterView.jsp?siteId=${param.siteId}"  flush="true"/> --%>
			</ul>
			<ul id="tab4" style="display:block;">
				<iframe frameborder="0" width="100%"  height="630"  flush="true" id="frame3" src="pageCounterQuery.jsp"></iframe>
			</ul>
		</div>
	</div>
</body>
</html>
