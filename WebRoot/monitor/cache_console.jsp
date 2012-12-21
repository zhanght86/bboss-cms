<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>



<%--
	
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- <title>操作日志详细</title>-->
<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
<script type="text/javascript">
</script>
</head>
<body>
	<div id="customContent">
	    <sany:menupath />
		<div class="title_box">
			<strong>系统缓存管理</strong>
			<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('ALL')"><span>全部清除</span> </a> 
		</div>
		<div id="changeColor" style="width: 1150px; overflow: auto;">
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="stable" id="tb">
				
				<tr>
				<th width="20%">缓存类别</th>
				<th width="80%">操作</th>
				</tr>
				<tr>
				<td>机构缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('org')"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>部门管理员缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('orgadmin')"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>字典缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('dict')"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>权限缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('permission')"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>角色缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('role')"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>用户组缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('group')"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>CMS站点缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('site')"><span>清除</span> </a> 
				</td>
				</tr>
				<tr>
				<td>CMS频道缓存</td>
				<td >
				<a  href="javascript:void(0)" class="bt_1 sp"
											id="queryButton" onclick="clear('channel')"><span>清除</span> </a> 
				</td>
				</tr>
			</table>
			
		</div>
	</div>
	<div id = "custombackContainer"></div>
</body>
</html>