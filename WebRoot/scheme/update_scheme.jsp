<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>编辑方案</title>
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/scripts/jquery-1.4.4.min.js">
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/scheme/js/scheme-update.js">	
		</script>
		<link href="<%=basePath %>/common/css/datePicker.css" rel="stylesheet" type="text/css" />
		<script language="javascript" src="<%=basePath %>/sysmanager/scripts/selectTime.js"></script>
		<script language="JavaScript" src="<%=request.getContextPath()%>/public/datetime/calender_date.js"></script>				
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/button.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/css.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/table.css">
		<link rel="stylesheet" type="text/css"
			href="<%=basePath%>/common/css/input.css">
	<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>					
		<style type="text/css">
.input_lengtht {
	width: 73%;
}
</style>
		<script type="text/javascript">
var basePath = "<%=basePath%>";
function query() {
	var schemeId = '<s:property value="tdPwSchemeTemp.schemeId"/>';
	var tableInfoForm = document.getElementById("update_scheme_form");
	tableInfoForm.action = "<%=basePath%>/scheme/personal_list.jsp?schemeId="+schemeId;
	tableInfoForm.target = "queryListSub";
	tableInfoForm.submit();
}
   	function showdivImpo() 
	{ 
   		   if (trim($("#tdPwSchemeTemp_schemeId").val())==""){
   			   alert("请输入方案编号后再进行人员的导入！");
   			   return;
   		   }
           JqueryDialog.Open('人员信息导入', basePath+"/scheme/upLoad.jsp?schemeId="+$("#tdPwSchemeTemp_schemeId").val()+"&cache="+ new Date().getTime(), 500, 200);			
	} 
	function trim(str) { //删除左右两端的空格
　　return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str){ //删除左边的空格
　　return str.replace(/(^\s*)/g,"");
}
function rtrim(str){ //删除右边的空格
　　return str.replace(/(\s*$)/g,"");
}
</script>
	</head>
	<body onload="query();">
		<div>
			<form action="" method="post" id="update_scheme_form"
				name="update_scheme_form">
				<table width="100%" id="listtable" border="0" cellspacing="0"
					cellpadding="0" class="Ctable">
					<tr>
						<td width="50%">
							<table width="100%" id="listtable" border="0" cellspacing="0"
								cellpadding="0" class="Ctable">

								<tr>
									<td colspan="4" class="taobox">
										定期跟踪
									</td>
								</tr>
								<tr>
									<td width="20%" class="c2" align="right" nowrap="nowrap">
										方案编号
									</td>
									<td width="80%" class="table_block_td" align="left"
										nowrap="nowrap">
										<s:hidden name="tdPwSchemeTemp.sequenceId"></s:hidden>
										<s:textfield name="tdPwSchemeTemp.schemeId"
											cssClass="input_out" readonly="true"
											onfocus="this.className='input_on';this.onmouseout=''"
											onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
											onmousemove="this.className='input_move'"
											onmouseout="this.className='input_out'">
										</s:textfield>
										<span id="userIdSpan" class="span_display"></span>
									</td>
								</tr>
								<tr>
									<td width="10%" class="c2" align="right" nowrap="nowrap">
										方案名称
									</td>
									<td width="40%" class="table_block_td" align="left"
										nowrap="nowrap">
										<s:textfield cssClass="input_length"
											name="tdPwSchemeTemp.schemeName" cssClass="input_out"
											onfocus="this.className='input_on';this.onmouseout=''"
											onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
											onmousemove="this.className='input_move'"
											onmouseout="this.className='input_out'">
										</s:textfield>
										<span id="userNameSpan" class="span_display"></span>
									</td>

								</tr>
								<tr>
									<td width="3%" class="c2" align="right" nowrap="nowrap">
										开始时间
									</td>
									<td width="3%" class="table_block_td" align="left"
										nowrap="nowrap">
										<s:textfield cssClass="input_length"
											name="tdPwSchemeTemp.startTime" cssClass="input_out"
											onfocus="this.className='input_on';this.onmouseout=''"
											onclick="showdate(this)" readonly="true"
											onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
											onmousemove="this.className='input_move'"
											onmouseout="this.className='input_out'">
										</s:textfield>
									</td>
								</tr>
								<tr>
									<td width="3%" class="c2" align="right" nowrap="nowrap">
										结束时间
									</td>
									<td width="3%" class="table_block_td" align="left"
										nowrap="nowrap">
										<s:textfield cssClass="input_length"
											name="tdPwSchemeTemp.endTime" cssClass="input_out"
											onclick="showdate(this)" readonly="true"
											onfocus="this.className='input_on';this.onmouseout=''"
											onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
											onmousemove="this.className='input_move'"
											onmouseout="this.className='input_out'">
										</s:textfield>
									</td>
								</tr>
								<tr>
									<td width="3%" class="c2" align="right" style="height: 52px;"
										nowrap="nowrap">
										方案说明
									</td>
									<td width="3%" class="table_block_td" align="left">
										<s:textarea rows="6" cssStyle="width:70%;height:52px;"
											name="tdPwSchemeTemp.schemeExplain" cssClass="input_out"
											onfocus="this.className='input_on';this.onmouseout=''"
											onblur="this.className='input_off';this.onmouseout=function(){this.className='input_out'};"
											onmousemove="this.className='input_move'"
											onmouseout="this.className='input_out'">
										</s:textarea>
									</td>
								</tr>
								<tr>
									<td width="3%" colspan="2" align="center" nowrap="nowrap">
										<span style="text-align: center;"> 
										<input type="button"
												name="addScheme2" onclick="updateScheme()"
												class="button" value="保 存" /> 
										<input type="button" name="tt" onclick="schemeReset()" class="button" value="重 置" /> 
										<input name="Sub" type="button" onclick="showdivImpo()" class="button" value="导入人员"/>
										</span>
									</td>
								</tr>
							</table>
						</td>
						<td width="50%">

							<iframe src="" name="queryListSub" id="queryListSub" border="0" style="margin-top:12px;"
								frameborder="0" framespacing="0" marginheight="0"
								marginwidth="0" height="298px" width="100%" />

						</td>
					</tr>
				</table>
			</form>
		</div>
	</body>
</html>


