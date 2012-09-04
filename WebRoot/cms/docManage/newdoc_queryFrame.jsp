<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title>内容管理主框架</title>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
<script language="javascript">
	function queryUser(){	
		//查询
		form1.action="newdoc_list.jsp?flag=query"
		form1.target="forDocList";
		form1.submit();	
	}
	function queryUserAll()
	{	//查询所有
		form1.action="newdoc_list.jsp"
		form1.target="forDocList";
		form1.submit();	
		form1.reset();
	}
	//默认光标停留在文档标题输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete")
			return;
		document.all.form1.title.focus();
	}
	function advancedQuery(){
		form1.action="newdoc_advancedQueryFrame.jsp"
		form1.target="forQuery";
		form1.submit();	
	} 
	function selChnl(){
		var reVlaue = openWin("multi_channel_select_frame.jsp?taskType=audit&siteid=",400,500);	
		if(reVlaue != undefined){
			document.all.channelNames.value = reVlaue.split(":")[0];
			document.all.channelIds.value = reVlaue.split(":")[1];
		}
	}
</script>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>
</head>
<body>
<form id="form1" name="form1" method="post">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="5" valign="top"><img src="../images/querybox_left.gif" width="5" height="62"></td>
    <td  style="background:url(../images/querybox_bg.gif) repeat-x top">
	<table width="100%" border="0" cellpadding="3" cellspacing="0" style="margin-top:5px"   class="query_table">
		<tr>
			<td height="23" colspan="7">&nbsp; 当前位置:个人工作台 &nbsp;>> &nbsp;新稿文档</td>
		</tr>
		<tr>
		<td width="15%" height='30' align="right" nowrap="nowrap">
			文档标题：		</td>
		<td width="15%" height='30' align="left">
			<input name="title" type="text" size="16"  class="cms_text">		</td>
		<td width="15%" align="right"> 关&nbsp;键&nbsp;词： </td>
        <td width="15%" align="left"><input name="keyword" type="text" size="18"  class="cms_text">
		<td width="15%" align="right"> 摘 &nbsp;&nbsp;&nbsp;要： </td>
        <td width="25%" height='0' align="left"><input name="docAbstract" type="text" size="16"  class="cms_text"></td>
	<tr>
		<td align="right" nowrap="nowrap">
			文档类型：		</td>
		<td width="17%" height='30' align="center">
			<select name="doctype" class="cms_select" style="width:120">
				<option value="">--请选择类型--</option>
				<option value="0">普通文档</option>
				<option value="1">外部链接</option>
				<option value="3">聚合文档</option>
			</select>		</td>
		<td align="right">
			文档级别：		</td>
		<td height='30' align="left">
			<select name="docLevel" class="cms_select">
				<option value="">--请选择文档级别--</option>
				<%
				DocumentManagerImpl dmi = new DocumentManagerImpl();
				List doclevellist = null;
				doclevellist = dmi.getDocLevelList();
				request.setAttribute("doclevellist",doclevellist);
				%>
				<pg:list requestKey="doclevellist">
				<option value="<pg:cell colName="id"/>">
				<pg:cell colName="name"/>
				</pg:list>
			</select>		</td>
		<td align="right">创建时间：</td>
        <td align="left"><input name="creatTime" type="text" size="16"  class="cms_text">
        <input name="button" type="button" onClick="showdate(document.all('creatTime'))" value="..."/>                </td>
	</tr>
	<tr>
		<td align="right" nowrap="nowrap">所属频道：</td>
        <td align="left" colspan="5">
            <input name="channelNames" type="text" size="45"  class="cms_text" disabled="disabled">
            <input name="channelIds" type="hidden" size="16"  class="cms_text" >
            <input name="chnlSel" type="button" onClick="selChnl()" value="...">
            &nbsp;&nbsp;
			<input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">&nbsp;&nbsp;
			<input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUserAll()"></td>
        </tr>
</table>
	</td>
    <td width="6" valign="top"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr>
</table>
</form>
</body>
</html>