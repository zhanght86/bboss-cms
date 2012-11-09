<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
	描述：增加台账信息设置
	作者：许石玉
	版本：1.0
	日期：2012-02-08
	 --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>浏览Demo</title>
		<%@ include file="/common/jsp/css-lhgdialog.jsp"%>
		
	</head>
	<body>
		<div class="form_box">
			<form id="editForm" name="editForm" method="post">
			<pg:beaninfo requestKey="demo">
			<fieldset>
				<legend>浏览Demo信息</legend>
				<table border="0" cellpadding="0" cellspacing="0" class="table4">
					<tr>
						<th width=85px >
							ID：
						</th>
						<td width=140px>
							<pg:cell colName="id"/>
							
						</td>
						<th width=85px >
							名称：
						</th>
						<td width=140px>							
							<pg:cell colName="name"/>
						</td>
	
						</tr>
						
				
				</table>
			</fieldset>
			</pg:beaninfo>
			<div class="btnarea" >
				
				<a href="javascript:void(0)" class="bt_2" id="resetButton" onclick="closeDlg()"><span>退出</span></a>
				<input type="reset" id="reset" style="display: none;" />
			</div>
			</form>
		</div>
	</body>
<script language="javascript">
var api = frameElement.api, W = api.opener;
   function closeDlg()
   {
	   api.close();
   }
   
</script>