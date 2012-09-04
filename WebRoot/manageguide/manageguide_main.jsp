<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<html>
	<head>
		<title>管控指标</title>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/common/css/button.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/common/css/css.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/common/css/table.css">
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/common/css/input.css">
		<script language="JavaScript" src="<%=basePath %>/include/pager.js" type="text/javascript">
</script>
		<script language="javascript"
			src="<%=basePath %>/sysmanager/scripts/selectTime.js">
</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/scripts/jquery-1.4.4.min.js">
</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/manageguide/js/manageguide-frame.js">
</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/manageguide/js/checkBox.js">
</script>
		<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/public/datetime/calender_date.js">
</script>

		<script type="text/javascript">
var basePath = "<%=basePath%>";
function clearInfos() {
	var tableInfoForm = document.getElementById("tableInfoForm");
	tableInfoForm.controlCode.value = "";
	tableInfoForm.controlName.value = "";
	tableInfoForm.controlType.value = "";
}
function query() {
	var tableInfoForm = document.getElementById("tableInfoForm");
	tableInfoForm.action = "manageguide_list.jsp";
	tableInfoForm.target = "queryList";
	tableInfoForm.submit();
}
function add_manageguide() {
	var wHeight = window.screen.height*0.5;
	var wWidth = window.screen.width*0.5;
	var top =wHeight*0.5;
	var left=wWidth*0.5;
	var param = 'height=' + wHeight + ',' + 'width=' + wWidth
	+ ',top='+top+', left='+left+',status=no,toolbar=no,menubar=no,location=no,resizable=yes,scrollbars=yes';
	window.open('add_manageguide.jsp', '新增管控指标',param);
}
function closeWindow() {
	JqueryDialog.Close();
}
</script>
	</head>

	<body onload="query();">
		<form name="manageListFrameForm" id="manageListFrameForm"
			method="post" action="">
		</form>
		<form name="tableInfoForm" id="tableInfoForm" action="" method="post">
			<table width="99%" border="0" cellpadding="0" cellspacing="0" style="margin-top:5px;">
				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="Ctable">

							<tr>
								<td colspan="4" class="taobox">
									管控指标
								</td>
							</tr>
							
							<tr>
								<td width="15%" class="c2">
									指标编号：
								</td>
								<td width="35%">
									<input id="controlCode" class="input6" name="controlCode"
										type="text"/>
								</td>
								<td width="15%" class="c2">
									指标名称：
								</td>
								<td width="35%">
									<input id="controlName" name="controlName" type="text"
										class="input6"/>
								</td>
							</tr>
							<tr>
								<td width="15%" class="c2">
									指标分类：
								</td>
								<td width="25%">
							<s:action id="getControType" name="selectaction!getControType" />
							<s:select name="controlType"
								cssClass="select4" id="controlType"
								headerKey="" headerValue="全部"
								list="#getControType.nameValuePairs">
							</s:select>										
								</td>
								<td colspan="2" align="center">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center">

									<button type="button" onclick="query()" class="button">查询</button>
									<button type="button" onclick="clearInfos()" class="button">清空</button>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="4" align="right">
						<input name="button" type="button" onclick="add_manageguide()"
							class="addbutton1" value="添加" />
						<input name="Submit223" type="button"
							onclick="update_manageguide()" class="xbutton1" value="修改" />
						<input name="Submit2222" type="button" onclick="del_manageguide()"
							class="sbutton1" value="删除" />
					</td>
				</tr>
			</table>
		</form>
		<table width="99%" border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td colspan="4">
									<iframe src="" name="queryList" id="queryList" 
									border="0" frameborder="0" framespacing="0"
							marginheight="0" marginwidth="0" 
										width="100%" height="480px" />
								
					</td>
				</tr>
		</table>
	</body>
</html>

