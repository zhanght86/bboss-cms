<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.platform.cms.templatemanager.*"%>
<%@page import="java.lang.*"%>
<html>
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>::::::模板管理视图选择</title>
</head>

<base target="_self">
<body  bottommargin="0" onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0">
<form  method="post" enctype="multipart/form-data"  name="fm_site"  onsubmit="return check_form()">
  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" background="../images/toolbar_common_func_template_bg.jpg" bgcolor="#FFFFFF">
    <!--来源于别的网站才有父站点-->
    <tr >
      <td width="214" height="82" valign="top"><img src="../images/toolbar_common_func_template_logo.jpg" width="214" height="82"></td>
      <td valign="top">&nbsp;</td>
      <td width="48" valign="top"><img src="../images/toolbar_common_func_template_R.jpg" width="48" height="82"></td>
    </tr>
  </table>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><div style=" color:#543312; font-size:14px; padding:10px; background:#ECF1F6; border:1px dashed #ADC1D4; text-align:center">请选择您要操作的模板视图！<img src="../images/toolbar_common_func_template_T.gif" width="35" height="29" align="baseline"></div></td>
  </tr>
</table>
</form>

</body>
</html>
