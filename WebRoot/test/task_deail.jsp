<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<div class="form_box">
	<fieldset>
		<legend>物料领用申请详细信息</legend>
		<table border="0" cellspacing="0" cellpadding="0" width="100%"
			class="table5">
			<tr>
				<th width=200px>标题：</th>
				<td width=400px align="left">${object.title }</td>
			</tr>
			<tr>
				<th width=200px>申请人：</th>
				<td width=400px align="left">${object.apply_name }</td>
				<th width=200px>申请时间：</th>
				<td width=400px align="left">${object.apply_time }</td>

			</tr>
			<tr>
				<th width=200px>物料名称：</th>
				<td width=400px align="left">${object.materiel_name }</td>

				<th width=200px>领用数量：</th>
				<td width=400px align="left">${object.materiel_num }</td>


			</tr>
		</table>
	</fieldset>
</div>