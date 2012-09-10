<%--
	描述：站点视频展示列表页面
	作者：gw_hel
	版本：1.0
	日期：2012-08-07
--%>
<%@ page language="java"  pageEncoding="utf-8" session="false"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<html>
	<head>
		<link href="<pg:sitedomain/>/css/main.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<pg:equal actual="${videosList.totalSize}" value="0" >
			<div style="text-align: center;padding-top:10px;">
				<img src="<pg:sitedomain/>/images/no_data.jpg"/></div>
			</pg:equal> 
		<pg:notequal actual="${videosList.totalSize}"  value="0">
		   <pg:pager scope="request"  data="videosList" desc="true" isList="false" >
		
				<pg:param name="orderByTime"/>
				<pg:param name="orderByPlay"/>
				<pg:param name="orderByComment"/>
				<pg:param name="siteId"/>
				<pg:param name="channelId"/>
				
				<div class="videoList"> 
				<div class="items" style="width:580px;">
				  <pg:list autosort="false">
				  <ul class="v">
                    	<li><a target='_blank' href="<pg:cell colName="docpuburl"/>"><img src="<pg:sitedomain/>/<pg:cell colName="picPath"/>" width="120" height="80"/></a></li>
                        <li class="v_title"><a target='_blank' href="<pg:cell colName="docpuburl"/>"><pg:cell colName="title" maxlength="18" replace=".."/></a></li>
                        <li></li>
                        <li><span class="ico__statplay"></span><span class="num"><pg:cell colName="playedCount"/></span><span class="ico__statcomment"></span><span class="num"><pg:cell colName="cmCount"/></span></li>
					</ul>
				 </pg:list>
				 </div>
				</div>
				<br/>
				<div class="pages">
					<pg:index tagnumber="5" export="000000010" numberend=">" numberpre="<"/>
				</div>
		   </pg:pager>
		 </pg:notequal>
	</body>
</html>
		
