<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
%>
<html><head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<title></title>
<script src="../inc/js/func.js"></script>
<script src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/include/security.js"></script>
<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<script language="javascript">
function queryUser(){	
	if(validate()){
		$.secutiry.dosubmit("form1",
						"newdoc_list.jsp?flag=query",
								"forDocList",
								"${pageContext.request.contextPath}");
		//form1.action="newdoc_list.jsp?flag=query";
		//form1.target="forDocList";
		//form1.submit();	
	}
}

//查询所有
function queryUserAll(){
	//form1.action="newdoc_list.jsp";
	//form1.target="forDocList";
	//form1.submit();	
	
	$.secutiry.dosubmit("form1",
						"newdoc_list.jsp",
								"forDocList",
								"<%=rootpath%>");
}

//默认光标停留在文档标题输入框
function document.onreadystatechange(){
	if (document.readyState!="complete")
		 return;
	document.all.form1.title.focus();
}
function validate(){
	if(form1.all.createTimeBegin.value != "" && form1.all.createTimeEnd.value != "" && form1.all.createTimeBegin.value > form1.all.createTimeEnd.value){
		alert("结束时间必须大于起始时间");
		return false;
	}
	return true;
}
function commonQuery(){	
	//form1.action="newdoc_queryFrame.jsp";
	//form1.target="forQuery";
	//form1.submit();	
	$.secutiry.dosubmit("form1",
						"newdoc_queryFrame.jsp",
								"forQuery",
								"<%=rootpath%>");
}
</script>
</head>
<body topmargin="0" rightmargin="0">
<form id="form1" name="form1" method="post" action="">

<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td width="5" valign="top" bgcolor="#ffffff"><img src="../images/querybox_left.gif" width="5" height="62"></td>
    <td style="background:url(../images/querybox_bg.gif) repeat-x top"><table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" style="margin-top:5px">
	<tr>
		<td width="12%" height='25' align="right" valign='middle'>
			文档标题：		</td>
		<td width="17%" height='25' align="left" valign='middle'>
			<input name="title" type="text" size="16"  class="cms_text">	  	</td>
		<td width="9%" height='25' align="right" valign='middle'>
			发稿人：		</td>
		<td width="19%" height='25' align="left" valign='middle'>
			<select name="userid" class="cms_select" style="width:118">
				<option value="">--请选择发稿人--</option>
			</select>	  	</td>
		<td width="9%" height='25' align="right" valign='middle'>
			作者：		</td>
	  	<td width="16%" height='25' align="left" valign='middle'>
	  		<input name="author" type="text" size="16"  class="cms_text">	 	</td>
		<td width="18%"  height='25'>&nbsp;	</td>
	</tr>
	<tr>
		<td height='25' valign='middle' align="right">
			文档状态：		</td>
		<td height='25' valign='middle' align="left">
			<select name="status" class="cms_select" style="width:107">
				<option value="">--请选择状态--</option>
			</select>		</td>
		<td width="9%" height='25' align="right" valign='middle'>
			文档类型：		</td>
		<td width="19%" height='25' align="left" valign='middle'>
			<select name="doctype" class="cms_select" style="width:118">
				<option value="">--请选择类型--	</option>
				<option value="0">普通文档</option>
				<option value="1">外部链接</option>
				<option value="3">聚合文档</option>
			</select>	 	</td>
		<td height='25' valign='middle' align="right">
			摘要：		</td>
	 	<td height='25' valign='middle' align="left">
	 		<input name="docAbstract" type="text" size="16"  class="cms_text">	 	</td>																	                                  			
		
		<td height='25'>&nbsp;</td>
	</tr>
	
	<tr>
		<td height='25' valign='middle' align="right">
			文档来源：		</td>
		<td height='25' valign='middle' align="left">
			<span class="cms_title_blue">
       			<select name="docsorid" class="cms_select" style="width:107">
       				<option value="">--请选择来源--</option>
         			<%
             			DocumentManagerImpl dmi = new DocumentManagerImpl();
						List dslist=dmi.getDocSourceList();
						request.setAttribute("dslist",dslist);
					%>
         			<pg:list requestKey="dslist">
         				<option value="<pg:cell colName="docsource_id"/>">
							<pg:cell colName="srcname"/> 
					</pg:list>
       			</select>
       		</span>     	</td>
		<td height='25' valign='middle' align="right">
			关键词：		</td>
		<td height='25' valign='middle' align="left">
			<input name="keyword" type="text" size="18"  class="cms_text">		</td>
		<td height='25' valign='middle' align="right">
			文档内容：		</td>
	  	<td height='25' valign='middle' align="left">
	  		<input name="content" type="text" size="16"  class="cms_text">		</td>
		<td height='25' colspan="2"><a style="cursor:hand;color:'#9A2F2F';  padding-top:5px"  class="cms_button" onClick="commonQuery()">普通查询</a></td>
	</tr>
	
	<tr>
		<td width="12%" height='25' align="right" valign='middle'>
			 撰写开始时刻：		</td>
		<td height='25' align="left" valign='middle'>
			<input name="createTimeBegin" type="text" size="16"  class="cms_text">
			<input type="button" value="." onClick="showdatetime(document.all('createTimeBegin'))"/>		</td>
		<td width="9%" height='25' align="right" valign='middle'>
			结束时刻：		</td>
		<td height='25' align="left" valign='middle'>
			<input name="createTimeEnd" type="text" size="18"  class="cms_text">
			<input type="button" value="." onClick="showdatetime(document.all('createTimeEnd'))"/>		</td>
		<td width="9%" height='25' align="right" valign='middle'>
			文档级别：		</td>
		<td height="25">
			<select name="docLevel" class="cms_select" style="width:107">
			<option value="">--请选择级别--</option>
			<%
				List doclevellist = null;
				doclevellist = dmi.getDocLevelList();
				request.setAttribute("doclevellist",doclevellist);
			%>
			<pg:list requestKey="doclevellist">
				<option value="<pg:cell colName="id"/>">
					<pg:cell colName="name"/> 
			</pg:list>
			</select>		</td>
		<td height='25' colspan="2">
			<input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">
			<input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUserAll()">		</td>
	</tr> 
</table></td>
    <td width="6" valign="top" bgcolor="#ffffff"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr>
</table>

</form>
</body>
</html>