<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>流程授权查看</title>
<%@ include file="/common/jsp/css.jsp"%>
<style type="text/css">
<!--
  body{
  	font-size:12px;
  	line-height:25px;
  }
  td{
  	font-size:12px;
  	line-height:25px;
  }
  th{
  	font-size:12px;
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
				<legend>流程授权信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4" width="100%">
				   <pg:beaninfo requestKey="wfEntrust" >
					<tr>
						<th>被授权人：</th>
						<td width="70%"><pg:cell colName="entrust_user_name" defaultValue="" /></td>
					</tr>
					<tr>
						<th>授权人：</th>
						<td><pg:cell colName="create_user_name" defaultValue="" /></td>
					</tr>
					<tr id="secret_tr">
						<th>授权开始时间：</th>
						<td id="secret_td" style="width:220px;"><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr id="re_secret_tr">
						<th>授权结束时间：</th>
						<td style="width:220px;"><pg:cell colName="start_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					<tr id="re_secret_tr">
						<th>创建时间：</th>
						<td style="width:220px;"><pg:cell colName="create_date" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
					</tr>
					</pg:beaninfo>
					<pg:beaninfo requestKey="entrustRelation" >
					<tr>
						<th vAlign="top">授权流程：</th>
						<td>
						<pg:equal colName="entrust_type" value="全部委托"><font color="red" id="selectProcdefMsg">未选择流程默认授权所有流程</font></pg:equal>
						<pg:equal colName="entrust_type" value="选择流程委托">
						<table border="0" cellpadding="0" cellspacing="0" width="80%" class="stable">
							<tr>
							    <th>序号</th>
								<th>流程名称</th>
								<th>应用系统</th>
							</tr>
							<pg:list requestKey="entrustProcRelationList">
							<tr>
							    <td><pg:rowid increament="1" offset="false"/></td>
								<td><pg:cell colName="procdef_name" defaultValue="" /></td>
								<td><pg:cell colName="wf_app_name" defaultValue="" /></td>
							</tr>
							</pg:list>
						</table>
						</pg:equal>
						</td>
					</tr>
					<tr>
						<th>描述：</th>
						<td><pg:cell colName="entrust_desc" defaultValue="" /></td>
					</tr>
					<tr>
						<td colspan='2'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="red">注意：授权不支持多级授权。举例，张三授权给李四，同个时间段李四授权给王五，这时需要张三审批的流程是不能转交给王五审批，还是李四审批。</font></td>
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

