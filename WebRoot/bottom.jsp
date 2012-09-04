<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<% String path = request.getContextPath();%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>底边条</title>
<link href="<%=path%>/common/css/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=path%>/common/scripts/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=path%>/common/scripts/bottom.js"></script>
</head>
<body  style="overflow: hidden;">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="bottom">
  <tr>
    <td width="5" background="<%=path%>/common/images/bottom_02.gif"><img src="<%=path%>/common/images/bottom_01.gif" width="5" height="28" /></td>
    <td align="left" background="<%=path%>/common/images/bottom_04.gif"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td width="14"><img src="<%=path%>/common/images/bottom_03.gif" width="14" height="28" /></td>
        <td width="10">&nbsp;</td>
        <td nowrap="nowrap"><table width="700" id="stateTitle" border="0" cellspacing="2" cellpadding="2" style="margin-left:10px;">
            <tr>
              <td width="4"><img src="<%=path%>/common/images/bottom_07.gif" width="4" height="7" /></td>
              <td width="193" id ="calloutNum">红色预警(10)</td>
              <td width="2"><img src="<%=path%>/common/images/bottom_06.gif" width="2" height="16" /></td>
              <td width="193" id="callInNum">橙色预警(5)</td>
              <td width="2"><img src="<%=path%>/common/images/bottom_06.gif" width="2" height="16" /></td>
              <td width="286" id ="callTime">黄色预警(0)</td>
              <td width="2"><img src="<%=path%>/common/images/bottom_06.gif" width="2" height="16" /></td>
              <td width="320" id="LoginTime">蓝色预警(5)</td>
              <td width="2"><img src="<%=path%>/common/images/bottom_06.gif" width="2" height="16" /></td>
              <td width="197" id="compeleteNum">签收(2)</td>
              <td width="2"><img src="<%=path%>/common/images/bottom_06.gif" width="2" height="16" /></td>
              <td width="197" id="compeleteNum">待反馈(2)</td>
              <td width="2"><img src="<%=path%>/common/images/bottom_06.gif" width="2" height="16" /></td>
              <td width="197" id="compeleteNum">己处理(0)</td>
            </tr>
        </table></td>
      </tr>
    </table></td>
    <td width="50" align="center" background="<%=path%>/common/images/bottom_04.gif">&nbsp;</td>
    <td width="5" align="right" background="<%=path%>/common/images/bottom_04.gif"><img src="<%=path%>/common/images/bottom_05.gif" width="5" height="28" /></td>
  </tr>
</table>
</body>
</html>


 

