<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<% 
	//日志ID
	String logId = request.getParameter("logId");
	
	String operTable = request.getParameter("operTable")==null?"":request.getParameter("operTable");
	String detailContent = request.getParameter("detailContent")==null?"":request.getParameter("detailContent");
%>
<html>
	<head>
		<title>日志明细查询</title>
		<base target="_self"/>
	<script type="text/javascript">
		function resetwindow()
		{
			document.detailForm.action = window.location.href;	
			document.detailForm.target = "";
			
			document.detailForm.submit();
			
		}
		
		function queryDetail(){
			document.detailForm.target = "hiddenIframe";
			document.detailForm.action = "logDetailList.jsp";
			resetwindow();
		}
		
		function resetValue(){
			document.all.operTable.value = "";
			document.all.detailContent.value = "";
		}
	</script>
	</head>
<body>
<div style="height: 10px">&nbsp;</div>
<div class="mcontent">
	<div id="searchblock">
		<div  class="search_top">
	 		<div class="right_top"></div>
	 		<div class="left_top"></div>
	 	</div>
	 	<div class="search_box" >
	 		<form name="detailForm" method="post">
	 			<table width="98%" border="0" cellspacing="0" cellpadding="0">
	 				<tr>
      					<td class="left_box"></td>
      					<td>
      						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="table2">
      							<tr>
      								<th width="15%"><pg:message code="sany.pdp.sysmanager.log.data.operation.table"/>：</th>
      								<td><input name="operTable" type="text" value="<%=operTable %>" class="w120" style="width: 70%"/></td>
      							</tr>
      							<tr>
      								<th width="15%"><pg:message code="sany.pdp.sysmanager.log.data.operation.content"/>：</th>
      								<td>
      									<input name="detailContent" type="text" value="<%=detailContent %>"  class="w120" style="width: 70%"/>
      									&nbsp;
      									<a href="javascript:void(0)" class="bt_1" id="search"  onclick="queryDetail()"><span><pg:message code="sany.pdp.common.operation.search" /></span> </a>
			      						<a href="javascript:void(0)" class="bt_2" id="Submit22" onclick="resetValue()"><span><pg:message code="sany.pdp.common.operation.reset" /></span> </a>
      								</td>
      							</tr>
      						</table>
      					</td>
      					<td class="right_box"></td>
      				</tr>	
	 			</table>
	 		</form>
	 	</div>
	 	<div class="search_bottom">
			<div class="right_bottom"></div>
			<div class="left_bottom"></div>
		</div>
	</div>
	
	<div id="changeColor">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.LogDetailSearchList" keyName="LogDetailSearchList" />
		<pg:pager maxPageItems="12" scope="request" data="LogDetailSearchList" isList="false">
			<pg:param name="operTable"/>
			<pg:param name="detailContent"/>
			<pg:param name="logId"/>
			
			<pg:equal actual="${LogDetailSearchList.itemCount}" value="0" >
				<div class="nodata">
				<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
			</pg:equal>
			<pg:notequal actual="${LogDetailSearchList.itemCount}"  value="0">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
					<pg:header>
						<th><pg:message code="sany.pdp.sysmanager.log.data.operation.table"/></th>
						<th><pg:message code="sany.pdp.sysmanager.log.data.operation.primarykey"/></th>
						<th><pg:message code="sany.pdp.sysmanager.log.data.operation.content"/></th>
						<th><pg:message code="sany.pdp.role.operation.type"/></th>
					</pg:header>
					<pg:list>
						<tr>
							<td><pg:cell colName="operTable" defaultValue="" /></td>
							<td><pg:cell colName="operKeyID" defaultValue="" /></td>
							<td><pg:cell colName="detailContent" defaultValue="" /></td>
							<td>
								<pg:equal colName="operType" value="0"><pg:message code="sany.pdp.common.operation.null"/></pg:equal>
								<pg:equal colName="operType" value="1"><pg:message code="sany.pdp.common.add"/></pg:equal>
								<pg:equal colName="operType" value="2"><pg:message code="sany.pdp.common.batch.delete"/></pg:equal>
								<pg:equal colName="operType" value="3"><pg:message code="sany.pdp.common.operation.modfiy"/></pg:equal>
								<pg:equal colName="operType" value="4"><pg:message code="sany.pdp.common.operation.other"/></pg:equal>	
							</td>
						</tr>
					</pg:list>
				</table>
				<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
			</pg:notequal>
		</pg:pager>
	</div>
</div>

<iframe name="hiddenFrame" width="0" height="0"></iframe>
</body>
</html>
