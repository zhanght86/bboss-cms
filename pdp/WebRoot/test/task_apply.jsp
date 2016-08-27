<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%
    AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request,response);
%>
<%--
	描述：物料领用测试
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>物料领用申请</title>
		<%@ include file="/common/jsp/css.jsp"%>
	</head>
	<body>
		<div class="form_box">
			<form name="applyForm" method="post">
			<!--  class="collapsible"  收缩 -->
			<input type="hidden" value="${object.id }" name="id"/>
			<fieldset>
				<legend>物料领用申请</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=200px>标题：</th>
						<td width=400px align="left">
							<input id="title" name="title" type="text" value="${object.title }"
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="100" /><font color="red">*</font>
						</td>
					</tr>
					<tr>
						<th width=200px >
							申请人：
						</th>
						<td width=400px align="left">
							<input  type="text" value="<%=accesscontroler.getUserName()%>"
								class="w120 input_default easyui-validatebox" required="true" readOnly
								maxlength="10" /><font color="red">*</font>
							<input type="hidden" name="apply_name" id="apply_name" value="<%=accesscontroler.getUserAccount()%>"/>
						</td>
						<th width=200px>
							申请时间：
						</th>
						<td width=400px align="left">
							<input id="apply_time" name="apply_time" type="text" value="<pg:cell actual='${object.apply_time}' dateformat='yyyy-MM-dd'/>"
								class="w120 input_default easyui-validatebox" required="true"
								maxlength="20" /><font color="red">*</font>
						</td>
	
						</tr>
						<tr>
						<th width=200px >
							物料名称：
						</th>
						<td width=400px align="left">
							<input id="materiel_name" name="materiel_name" type="text" value="${object.materiel_name }"
								class="w120" maxlength="100" />
						</td>

						<th width=200px >
							领用数量：
						</th>
						<td width=400px align="left">
							<input id="materiel_num" name="materiel_num" type="text" value="${object.materiel_num }"
								class="w120" maxlength="10" />
						</td>
						
						
					</tr>
				</table>		
			</fieldset>
			<div class="btnarea" >
				<a href="javascript:void(0)" class="bt_1" id="addButton" onclick="dosubmit()"><span>提交</span></a>
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="doreset()"><span>重置</span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
	</body>
<script language="javascript">

   function dosubmit()
   {
		document.applyForm.action="../test/apply.page";
		document.applyForm.submit();
   	 }
function doreset(){
	$("#reset").click();
}
</script>