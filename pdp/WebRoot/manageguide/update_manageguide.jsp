<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String basePath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>修改管控指标</title>
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/common/scripts/jquery-1.4.4.min.js">
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/manageguide/js/manageguide-update.js">
		</script>
		<link rel="stylesheet" type="text/css"
			href="<%=basePath %>/common/css/button.css">
			<link rel="stylesheet" type="text/css"
				href="<%=basePath %>/common/css/css.css">
				<link rel="stylesheet" type="text/css"
					href="<%=basePath %>/common/css/table.css">
					<link rel="stylesheet" type="text/css"
						href="<%=basePath %>/common/css/input.css">
		<style type="text/css">
.input_lengtht {
	width: 73%;
}
</style>
		<script type="text/javascript">
var basePath = "<%=basePath%>";
</script>
	</head>
	<body>
		<div>
			<form action="" method="post" id="add_manageguide_form"
				name="add_manageguide_form">
				<table width="100%" id="listtable" border="0" cellspacing="0"
					cellpadding="0" class="Ctable">
					<tr>
						<td width="20%" class="c2" align="right" nowrap="nowrap">
							指标名称
						</td>
						<td width="80%" class="table_block_td" align="left"
							nowrap="nowrap">
							<s:textfield name="tdPwControlIndicators.controlName"
								cssClass="input6">
							</s:textfield>
							<span id="userIdSpan" class="span_display"></span>
						</td>
					</tr>
					<tr>
						<td width="10%" class="c2" align="right" nowrap="nowrap">
							指标编号
						</td>
						<td width="40%" class="table_block_td" align="left"
							nowrap="nowrap">
							<s:textfield cssClass="input_length"
								name="tdPwControlIndicators.controlCode" cssClass="input6">
							</s:textfield>
							<span id="userNameSpan" class="span_display"></span>
						</td>

					</tr>
					<tr>
						<td width="3%" class="c2" align="right" nowrap="nowrap">
							指标分类
						</td>
						<td width="3%" class="table_block_td" align="left" nowrap="nowrap">
							<s:action id="getControType" name="selectaction!getControType" />
							<s:select name="tdPwControlIndicators.controlType"
								cssClass="select4" id="getControType"
								headerKey="-1" headerValue="-请选择-"
								list="#getControType.nameValuePairs">
							</s:select>
						</td>
					</tr>
					<tr>
						<td width="3%" class="c2" align="right" nowrap="nowrap">
							输出类型
						</td>
						<td width="3%" class="table_block_td" align="left" nowrap="nowrap">
							<s:action id="getControResType"
								name="selectaction!getControResType" />
							<s:select name="tdPwControlIndicators.controlResType"
								cssClass="select4" id="getControResType"
								headerKey="-1" headerValue="-请选择-"
								list="#getControResType.nameValuePairs">
							</s:select>
						</td>
					</tr>
					<tr>
						<td width="3%" class="c2" align="right" nowrap="nowrap">
							指标状态
						</td>
						<td width="3%" class="table_block_td" align="left" nowrap="nowrap">
							<s:bean id="statusType"
								name="com.frameworkset.platform.integralwarning.manageguide.select.SelectAction" />
							<s:radio list="#statusType.statusType"
								name="tdPwControlIndicators.isUsed" />

						</td>
					</tr>
					<tr>
						<td width="3%" class="c2" align="right" nowrap="nowrap">
							规则体
						</td>
						<td width="3%" class="table_block_td" align="left" nowrap="nowrap">
							<s:action id="getRuleContent" name="selectaction!getRuleContent" />
							<s:select name="tdPwControlIndicators.ruleId"
								cssClass="select4" id="getRuleContent"
								headerKey="-1" headerValue="-请选择-"
								list="#getRuleContent.nameValuePairs">
							</s:select>
						</td>
					</tr>
					<tr>
						<td width="3%" class="c2" align="right" nowrap="nowrap">
							规则说明
						</td>
						<td width="3%" class="table_block_td" align="left">
						<s:hidden name="tdPwControlIndicators.id"></s:hidden>
							<s:textarea  rows="6" cssStyle ="width:70%;height:52px;"
								name="tdPwControlIndicators.remark" cssClass="input_out">
							</s:textarea>	
							</td>
					</tr>
					<tr>
						<td width="3%" colspan="2" align="center" nowrap="nowrap">
							<span style="text-align: center;"> <input type="button"
									name="addManageGuide" onclick="manageGuideUpdate()" class="button"
									value="保 存" /> <input type="button" name="tt"
									onclick="manageGuideReset()" class="button" value="重 置" /> </span>
						</td>

					</tr>
				</table>

			</form>
		</div>
	</body>
</html>


