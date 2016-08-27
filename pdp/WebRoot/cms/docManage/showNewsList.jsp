<%--
	描述：站点最新新闻展示列表页面
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
		<pg:equal actual="${newsList.totalSize}" value="0" >
			<div style="text-align: center;padding-top:10px;">
				<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
			</pg:equal> 
		<pg:notequal actual="${newsList.totalSize}"  value="0">
		   <pg:pager scope="request"  data="newsList" desc="true" isList="false" >		
				<pg:param name="startTime"/>
				<pg:param name="endTime"/>
				<pg:param name="keywords"/>
				<pg:param name="siteId"/>
				<pg:param name="channelId"/>
				
				  <pg:list autosort="false">
				     	<li>
				     		<pg:cell colName="docsource_name"/>：</span>
				     		<pg:notequal colName="doctype" value="${DOCUMENT_AGGRATION}">
				           	<a title='<pg:cell colName="title"/>' target='_blank' href='<pg:cell colName="docpuburl"/>' class='linkTagClass '>
				           	<pg:equal colName='titlecolor' value='#000000'>
				           		<pg:cell colName="title"/> 
				           	</pg:equal>
				           	<pg:notequal colName='titlecolor' value='#000000'>
				           		<font color="<pg:cell colName='titlecolor'/>"><pg:cell colName="title"/></font>
				           	</pg:notequal>
				           	<pg:equal colName="isNew" value="1">
				           		<pg:notequal colName="newPicPath" value="">
				           			<img border="0" src="<pg:sitedomain/>/<pg:cell colName='newPicPath'/>"/>
				           		</pg:notequal>
				           	</pg:equal>
				           	</a>
				           	&nbsp;<font color="blue">点击数：<pg:cell colName='count'/></font>
				           	</pg:notequal>
				           	<pg:equal colName="doctype" value="${DOCUMENT_AGGRATION}">
				           		<Strong><pg:cell colName="title"/>:</Strong>
				     			<pg:list colName="compositeDocs" >
				     			  <a title='<pg:cell colName="title"/>' target='_blank' href='<pg:cell colName="docpuburl"/>' class='linkTagClass '>
						           	<pg:equal colName='titlecolor' value='#000000'>
						           		<pg:cell colName="title"/>
						           	</pg:equal>
						           	<pg:notequal colName='titlecolor' value='#000000'>
						           		<font color="<pg:cell colName='titlecolor'/>"><pg:cell colName="title"/></font>
						           	</pg:notequal>
						           	<pg:equal colName="isNew" value="1">
						           		<pg:notequal colName="newPicPath" value="">
						           			<img border="0" src="<pg:sitedomain/>/<pg:cell colName='newPicPath'/>"/>
						           		</pg:notequal>
						           	</pg:equal>
						           	</a>
						           	&nbsp;<font color="blue">点击数：<pg:cell colName='count'/></font>
				     			</pg:list>
				     		</pg:equal>
				     		
				       		&nbsp;&nbsp;&nbsp;&nbsp;<pg:cell colName="docwtime" dateformat="yyyy-MM-dd"/>
				       </li>
				 </pg:list>
				 <br/>
				 <br/>
				<div class="pages">
					<pg:index tagnumber="10" export="000000010" numberend=">" numberpre="<"/>
				</div>
		   </pg:pager>
		  </pg:notequal>
	</body>
</html>
		
