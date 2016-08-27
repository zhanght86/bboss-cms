<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*,com.frameworkset.platform.cms.flowmanager.*,com.frameworkset.platform.cms.channelmanager.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userId = accesscontroler.getUserID();
	
	com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
 	cmsManager.init(request,session,response,accesscontroler);
 	String currentSiteid = cmsManager.getSiteID();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>归档文档查询</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript">
	function queryUser()
	{	
		//查询
		form1.action = "<%=rootpath%>/cms/docManage/garbageListFrame.jsp?flag=query";
		form1.target = "garbageListFrame";
		form1.submit();	
	}
	function queryUserAll()
	{	
		//查询所有
		form1.action = "<%=rootpath%>/cms/docManage/garbageListFrame.jsp";
		form1.target = "garbageListFrame";
		form1.submit();
		form1.reset();	
	}
	//默认光标停留在文档标题输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete") return;
		document.all.form1.title.focus();
	}
	function selChnl(){
		var reVlaue = openWin('multi_channel_select_frame.jsp?siteid=<%=currentSiteid%>',400,500);	
		if(reVlaue != undefined){
			document.all.channelNames.value = reVlaue.split(":")[0];
			document.all.channelIds.value = reVlaue.split(":")[1];
		}
	}
</script>
		<style type="text/css">
		body {
			background-color: #ffffff;
		}
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
	<body topmargin="1" rightmargin="1" scroll=no leftmargin="1">
		<form id="form1" name="form1" method="post" action="" >
			<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="5" valign="top" bgcolor="#ffffff"><img src="../images/querybox_left.gif" width="5" height="62"></td>
    <td  style="background:url(../images/querybox_bg.gif) repeat-x top">
	<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0"  class="query_table">
		<tr>
			<td colspan="7" height="40">
				&nbsp; 当前位置：回收站&nbsp; >> &nbsp;回收管理
			</td>
		</tr>
										<tr>
											<td align="right">
												文档标题：											</td>
											<td>
												<input name="title" type="text" size="16"  class="cms_text">
								  	  	  </td>
									  	  	<td align="right">
												文档流程：											</td>
											<td align="left">
												<select name="flowId" class="cms_select" style="width:107">
													<option value="">
														--请选择流程--
													</option>
													<% 
														FlowManager fm = new FlowManagerImpl();
														List flowlist = fm.getFlowList2();
														request.setAttribute("dslist", flowlist);
													%>
													<pg:list requestKey="dslist">
													<option value="<pg:cell colName="flowid"/>">
													<pg:cell colName="flowName" />
													</pg:list>
											  </select>
									  	  </td>
										  <td align="right">
												前一个状态：										  </td>
										  <td align="left" valign='middle'>
												<select name="preStatus" class="cms_select" style="width:107">
													<option value="">
														--请选择状态--
													</option>
													<%
													  List list = null;
													  DocumentManagerImpl dmi = new DocumentManagerImpl();
													  list = dmi.getStatusList();
													  request.setAttribute("dslist", list);
													%>
													<pg:list requestKey="dslist">
														<option value="<pg:cell colName="status_id"/>">
															<pg:cell colName="status_name" />
													</pg:list>
												</select>
										  </td>
										  				
										</tr>	
										<tr>
											<td align="right">所属频道：</td>
				                            <td colspan=3 align="left">
				                                <input name="channelNames" type="text" size="35"  class="cms_text" disabled="disabled">
				                                <input name="channelIds" type="hidden" size="16"  class="cms_text" >
				                                <input name="chnlSel" type="button" onClick="selChnl()" value="...">                        
			                              </td>
										  	<%if(accesscontroler.isAdmin()){%>
											<td align="right">
												 回 收 人：											</td>
											<td width="12%" align="left" valign='middle'>
												<select name="garbager" class="cms_select" style="width:107">
												<option value="">-选回收人-</option>
												<option value="1">本人</option>
												<option value="2">所有</option>
												</select>
										  </td>
											<%}else{%>
											<td></td>
											<td></td>
											<%}%>
											<td rowspan="2" align="center" valign='middle'>
												<div align="right">
													<input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">
													<input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUserAll()">
												</div>
										  </td>
										</tr>
	    </table>
	
	
	</td>
    <td width="6" valign="top" bgcolor="#ffffff"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr>
</table>

		</form>
	</body>
</html>



