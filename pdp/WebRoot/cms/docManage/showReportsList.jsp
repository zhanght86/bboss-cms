<%--
	描述：站点专题展示列表页面
	作者：gw_hel
	版本：1.0
	日期：2012-08-07
--%>
<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<html>
	<head>
		<link href="<pg:sitedomain/>/css/report.css" rel="stylesheet" type="text/css" />
		<link href="<pg:sitedomain/>/css/main.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<pg:equal actual="${reportsList.totalSize}" value="0" >
			<div style="text-align: center;padding-top:10px;">
				<img src="<pg:sitedomain/>/images/no_data.jpg"/></div>
			</pg:equal> 
		<pg:notequal actual="${reportsList.totalSize}"  value="0">
		   <pg:pager scope="request"  data="reportsList" desc="true" isList="false" >
		
				<pg:param name="startTime"/>
				<pg:param name="endTime"/>
				<pg:param name="keywords"/>
				<pg:param name="siteId"/>
				<pg:param name="channelId"/>
				
				<ul class="report">
				  <pg:list autosort="false">
				     	<li>
				     		<div class="report_img"><img src="<pg:sitedomain/>/<pg:cell colName="outlinepicture"/>" width="116" height="76"  /></div>
				            <div class="report_title"><a target='_blank' href="<pg:cell colName="channelpuburl"/>"><pg:cell colName="name"/> </a></div>
				            <div class="date"><pg:cell colName="createTime" dateformat="yyyy-MM-dd"/></div>
				            <a target='_blank' href="<pg:cell colName="channelpuburl"/>"><pg:cell colName="channel_desc" maxlength="52" replace="..."/></a>
				       </li>
				 </pg:list>
				 </ul>
				 <br/>
				 <br/>
				<div class="pages">
					<pg:index tagnumber="5" export="000000010" numberend=">" numberpre="<"/>
				</div>
		   </pg:pager>
		 </pg:notequal>
	</body>
</html>
		
