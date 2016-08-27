<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.frameworkset.platform.cms.docsourcemanager.*"%>
<%
	DocsourceManager addm = new DocsourceManagerImpl();
	Docsource docsource = addm.getDsrcList(Integer.parseInt(request.getParameter("docId")));
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="../inc/js/func.js"></script>
<script language="javascript" >
function updatedocsrc(){
	if(docsrcupdate.srcname.value == "")
	{
		alert("请填写来源名称！");
		docsrcupdate.srcname.focus();
		return false;
	}
	docsrcupdate.action="docsource_ud.jsp?docId=<%=request.getParameter("docId")%>";
	docsrcupdate.submit();
}

function docsourcelist(){
	getPropertiesContent().location.href="docsource_list.jsp";
}
</script>
<style type="text/css">
<!--
body {
	background-color: ffffff;
}
.STYLE1 {color: #0000FF}
.STYLE2 {color: #000099}
-->
</style></head>

<body topmargin="3" rightmargin="0">
<form id="docsrcupdate" name="docsrcupdate" method="post" action="">
<table width="810" height="565" border="0"   cellpadding="0" align="center" cellspacing="0">
  <tr>
    <td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat"><table width="100%" border="0">
      <tr>
        <td height="23">&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:稿源管理</td>
        <td>&nbsp;</td>
      </tr>
      <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
      </tr>
	   <tr>
       <td >
	   
	      <div align="right">  </div></td>
       </tr>
    </table></td>
  </tr>
	<tr>
		<td  height="483" valign=top background="../images/tile.jpg" bgcolor="#FFFFFF">
		<table width="98%" border="0" align="center">
			<tr>
				<td width="10%">&nbsp;</td>
				<td width="40%">&nbsp;</td>
				<td width="10%">&nbsp;</td>
				<td width="48%">&nbsp;</td>
			</tr>

			<tr>
				<td width="10%"><div align="right">来源名称：</div></td>
				<td colspan="5"><input name="srcname" type="text" size="30" value="<%=docsource.getSRCNAME()%>">&nbsp;&nbsp;&nbsp;<span class="red_star">*</span></td>
			</tr>
			<tr>
				<td width="10%"><div align="right">来源链接：</div></td>
				<td colspan="5"><input name="srclink" type="text" size="30" value="<%=docsource.getSRCLINK()%>"></td>
			</tr>
			<tr>
				<td width="10%"><div align="right">来源说明：</div></td>
				<td colspan="5"><input name="srcdesc" type="text" size="70" value="<%=docsource.getSRCDESC()%>"></td>
			</tr>
			<tr>
        		<td colspan="5" align="left">
					<input type="button" name="submita" class="cms_button" value="提交" onClick="updatedocsrc()">
					<input type="button" name="submitc" class="cms_button" value="返回" onClick="docsourcelist()">
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
