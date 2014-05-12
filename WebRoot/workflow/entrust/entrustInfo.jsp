<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新增委托待办</title>
<%@ include file="/common/jsp/css.jsp"%>
<style type="text/css">
<!--
  body{
  	font-size:14px;
  	line-height:25px;
  }
  td{
  	font-size:14px;
  	line-height:25px;
  }
  th{
  	font-size:14px;
  	line-height:25px;
  }

-->
</style>
</head>
<script type="text/javascript">

</script>
<body >
	<div class="form_box">
		<form id="addForm" name="addForm" method="post">
			<fieldset>
				<legend>委托待办信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4" width="100%">
				   <pg:beaninfo requestKey="wfEntrust" >
					<tr>
						<th>委托人：</th>
						<td width="70%"><pg:cell colName="entrust_user" defaultValue="" /></td>
					</tr>
					<tr>
						<th>归属人：</th>
						<td><pg:cell colName="create_user" defaultValue="" /></td>
					</tr>
					<tr id="secret_tr">
						<th>委托开始时间：</th>
						<td id="secret_td" style="width:220px;"><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr id="re_secret_tr">
						<th>委托结束时间：</th>
						<td style="width:220px;"><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr id="re_secret_tr">
						<th>创建时间：</th>
						<td style="width:220px;"><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					</pg:beaninfo>
					<pg:beaninfo requestKey="entrustRelation" >
					<tr>
						<th>委托类型：</th>
						<td><pg:cell colName="entrust_type" defaultValue="" /></td>
					</tr>
					<tr>
						<th vAlign="top">委托流程：</th>
						<td>
						<table border="0" cellpadding="0" cellspacing="0" width="80%" class="table3">
							<tr>
							    <td>序号</td>
								<td>流程</td>
								<td>应用</td>
								<td>业务类别</td>
							</tr>
							<pg:list requestKey="entrustProcRelationList">
							<tr>
							    <td><pg:rowid increament="1" offset="false"/></td>
								<td><pg:cell colName="procdef_name" defaultValue="" /></th>
								<td><pg:cell colName="wf_app_name" defaultValue="" /></th>
								<td><pg:cell colName="business_name" defaultValue="" /></th>
							</tr>
							</pg:list>
						</table>
						</td>
					</tr>
					<tr>
						<th>描述：</th>
						<td><pg:cell colName="entrust_desc" defaultValue="" /></td>
					</tr>
					</pg:beaninfo>
				</table>
			</fieldset>
			<div class="btnarea">
				<a href="javascript:void(0)" class="bt_2" id="resetButton"
					onclick="closeDlg()"><span>退出</span></a>
			</div>
		</form>
	</div>
</body>

