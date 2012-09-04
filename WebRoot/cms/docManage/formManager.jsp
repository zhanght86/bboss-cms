<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<style type="text/css">
		.STYLE1 {color: #0000FF}
		.STYLE2 {color: #000099}
		.style3 {
			font-size: 14px;
			font-weight: bold;
			color: #3300FF;
		}
		.operStyle{
		width:17;
		height:16;
		}
</style>
</head>
<body leftmargin="3">
<div style="margin-bottom:5px; margin-top:3px">
  <input name="Submit" type="submit" class="cms_button" value="新增">
  <input name="Submit" type="submit" class="cms_button" value="删除"></div>
<table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">
  <tr  class="cms_report_tr">
    <td height="30"><input type="checkbox" name="checkbox" value="checkbox" class="checkbox"></td>
    <td class="cms_title_blue">表单模版标题</td>
    <td class="cms_title_blue">说明</td>
  </tr>
  <tr>
    <td><input type="checkbox" name="checkbox" value="checkbox" class="checkbox"></td>
    <td>通用表单</td>
    <td>通用</td>
  </tr>
  <tr>
    <td height="32"><input type="checkbox" name="checkbox" value="checkbox" class="checkbox"></td>
    <td>xxx站点表单</td>
    <td>用于xxx站点</td>
  </tr>
</table>
<p class="cms_title_blue">&nbsp;</p>

</html>