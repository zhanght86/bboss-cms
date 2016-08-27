<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>定期跟踪</title>
		<link rel="stylesheet" type="text/css"
			href="<%=basePath %>/common/css/button.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath %>/common/css/css.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath %>/common/css/table.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath %>/common/css/input.css">
		<script language="JavaScript" src="<%=basePath %>/include/pager.js"
			type="text/javascript">
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
<style type="text/css">
body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,blockquote,iframe{
	padding:0; margin:0;
}
table { 
	margin:0 auto;
	padding:0;
}
.input_length {
    width:73%;
}
</style>
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
	tableInfoForm.action = "scheme_list.jsp";
	tableInfoForm.target = "queryList";
	tableInfoForm.submit();
}
function add_manageguide() {
	JqueryDialog.Open('新增方案', 'add_scheme.jsp', 598, 308);
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
									定期跟踪
								</td>
							</tr>
							
							<tr>
								<td width="15%" class="c2">
									方案编号：
								</td>
								<td width="35%">
									<input id="controlCode" class="input_out" name="controlCode"
										type="text"
										onfocus="this.className='input_on';this.onmouseout=''"
										onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
										onmousemove="this.className='input_move'"
										onmouseout="this.className='input_out'" />
								</td>
								<td width="15%" class="c2">
									方案名称：
								</td>
								<td width="35%">
									<input id="controlName" name="controlName" type="text"
										class="input_out"
										onfocus="this.className='input_on';this.onmouseout=''"
										onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
										onmousemove="this.className='input_move'"
										onmouseout="this.className='input_out'" />
								</td>
							</tr>
							<tr>
								<td width="15%" class="c2">
									开始时间：
								</td>
								<td width="25%">
									<input id="controlName" name="controlName" type="text"
										class="input_out"
										onfocus="this.className='input_on';this.onmouseout=''"
										onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
										onmousemove="this.className='input_move'"
										onmouseout="this.className='input_out'" />									
								</td>
							<td width="15%" class="c2">
									结束时间：
								</td>
								<td width="35%">
									<input id="controlName" name="controlName" type="text"
										class="input_out"
										onfocus="this.className='input_on';this.onmouseout=''"
										onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
										onmousemove="this.className='input_move'"
										onmouseout="this.className='input_out'" />
								</td>
							</tr>
							<tr>
								<td colspan="4" align="center">

									<button type="button" onclick="query()" class="sbutton3">
										查询
									</button>
									&nbsp;&nbsp;
									<button type="button" onclick="clearInfos()" class="sbutton3">
										清空
									</button>
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
						<input name="Submit2222" type="button" onclick="del_manageguide()"
							class="sbutton3" value="模板下载" />
						<input name="Submit2222" type="button" onclick="del_manageguide()"
							class="sbutton3" value="导入人员" />														
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

