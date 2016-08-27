<%--
	描述：站点视频展示排行榜页面
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
		  <pg:list requestKey="videosLadder" needClear="false">
		  <ul class="v">
                  	<li><a target='_blank' href="<pg:cell colName="docpuburl"/>"><img src="<pg:sitedomain/>/<pg:cell colName="picPath"/>" width="128" height="80"/></a></li>
                     <li class="v_title"><a target='_blank' href="<pg:cell colName="docpuburl"/>"><pg:cell colName="title"/></a></li>
			</ul>
		 </pg:list>
	</body>
</html>
		
