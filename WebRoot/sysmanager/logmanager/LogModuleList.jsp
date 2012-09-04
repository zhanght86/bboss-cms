<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>

<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ include file="/common/jsp/csscontextmenu-lhgdialog.jsp"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.text.DateFormat,java.text.SimpleDateFormat,java.util.Calendar,java.util.Date"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	//当前的时间
	String riqi;
	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
			"yyyy-MM-dd");
	Date currentDate = new Date();
	/**当前时间*/
	riqi = df.format(currentDate);
	String operUser = request.getParameter("operUser");
	String type = request.getParameter("type");
	String oper = request.getParameter("oper");
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	operUser = operUser == null ? "" : operUser;
	type = type == null ? "" : type;
	oper = oper == null ? "" : oper;
	startDate = startDate == null ? "" : startDate;
	endDate = endDate == null ? "" : endDate;
	if (startDate == null || startDate == "") {
		startDate = riqi;
	}
	if (endDate == null || endDate == "") {
		endDate = riqi;
	}
	String rootpath = request.getContextPath();
%>
<html >
      

<head>
		<title>属性容器</title>
<tab:tabConfig/>		
<script language="JavaScript" src="<%=request.getContextPath()%>/sysmanager/jobmanager/common.js" type="text/javascript"></script>		
<script language="javascript" src="../scripts/selectTime.js"></script>

<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js" ></script>
<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="JavaScript">
	
	
		
	
		
	function logState(status,id){
		outMsg = '<pg:message code="sany.pdp.sysmanager.log.module.status.modfiy.confirm"/>';
		$.dialog.confirm(outMsg, function() {
			LogModuleForm.action="<%=rootpath%>/sysmanager/logmanager/submitLog_do.jsp?status="+ status +"&id="+id+"&flag=1";
			LogModuleForm.submit();
		}, function() {
			document.location = document.location;
		});
	}		
	
</script>
<body>
	<div class="mcontent">
		<div class="title_box">
			<strong><pg:message code="sany.pdp.sysmanager.log.data.list"/></strong>
		</div>
		
		<div id="changeColor">
			<pg:listdata dataInfo="LogModuleList" keyName="LogModuleList" />
			<pg:pager maxPageItems="15" scope="request" data="LogModuleList" isList="false">
				<pg:equal actual="${LogModuleList.itemCount}" value="0" >
					<div class="nodata">
					<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
				</pg:equal>
				<pg:notequal actual="${LogModuleList.itemCount}"  value="0">
					<table width="98.5%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
						<pg:header>
							<th><pg:message code="sany.pdp.sysmanager.log.module.name"/></th>
							<th><pg:message code="sany.pdp.sysmanager.log.module.description"/></th>
							<th><pg:message code="sany.pdp.sysmanager.log.module.operation.log"/></th>
						</pg:header>
						<form name="LogModuleForm" action="" method="post">
						<pg:list>
							<tr>
								<td><pg:cell colName="LOGMODULE" defaultValue="" /></td>
								<td><pg:cell colName="MODULE_DESC" defaultValue="" /></td>
								<td>
									<INPUT type="checkbox" name="status" 
									<pg:equal colName="STATUS" value="0">checked
									</pg:equal>
									<pg:equal colName="STATUS" value="1">
									</pg:equal>
									onClick="logState('<pg:cell colName="STATUS" defaultValue=""/>','<pg:cell colName="id" defaultValue="" />')" value="<pg:cell colName="STATUS" defaultValue="0" />">
									<pg:equal colName="STATUS" value="0"><pg:message code="sany.pdp.sysmanager.log.module.operation.log.yes"/>
									</pg:equal>
									<pg:equal colName="STATUS" value="1"><pg:message code="sany.pdp.sysmanager.log.module.operation.log.no"/>
									</pg:equal>
								</td>
							</tr>
						</pg:list>
					</table>
					<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
				</pg:notequal>
			</pg:pager>
		</div>
	</div>	
	
	<iframe src="" name="deleteLogs" width=0 height=0 style="display:none"></iframe>
</body>
</html>

