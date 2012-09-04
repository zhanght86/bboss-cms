<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript">
	function queryUser()
	{	
		//查询
		form1.action="list.jsp?flag=query&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		form1.target="forDocList2";
		form1.submit();	
	}
	function queryUserAll()
	{	//查询所有
		form1.action="list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		form1.target="forDocList2";
		form1.submit();
		form1.reset();
	}
	//默认光标停留在文档标题输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete") return;
		document.all.form1.title.focus();
	}
	function advancedQuery(){
		form1.action="advancedQueryFrame.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		form1.target="forQuery2";
		form1.submit();	
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
    <td width="5" valign="top"><img src="../images/querybox_left.gif" width="5" height="62"></td>
    <td  style="background:url(../images/querybox_bg.gif) repeat-x top">
	<table width="100%" height="100%" border="0" cellpadding="0" align="center" cellspacing="0"   class="query_box">
				<tr>
					<td>
						<table width="100%" border="0">
							<tr>
								<td height="23">&nbsp; 当前位置:
									<%=sitename%>
									站点 >>
									<%=channelname%>
									频道								</td>
								<td align="right">
									<a style="cursor:hand; color:'#9A2F2F';  padding-top:5px"  class="cms_button" onClick="advancedQuery()">高级查询</a>
							<td>							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="query_table">
										<tr>
											<td height='30' align="right">
												文档标题：											</td>
											<td height='30' align="center" valign='middle'>
												<div align="left">
													<input name="title" type="text" class="cms_text">
												</div>										  </td>
											<td height='30' align="right">
												发稿人：											</td>
											<td height='30' align="left" valign='middle'>
												<select name="userid" class="cms_select" style="width:120">
													<option value="">
														--请选择发稿人--													</option>
													<%List list = null;
													  DocumentManagerImpl dmi = new DocumentManagerImpl();
													  list = dmi.getDistributeList(channelId);
													  request.setAttribute("dblist", list);
													%>
													<pg:list requestKey="dblist">
														<option value="<pg:cell colName="user_id"/>">
															<pg:cell colName="user_name" />
													</pg:list>
												</select>										  </td>
										  <td height='30' cols="2" align="right">
											&nbsp;									</td>
									    </tr>

										<tr>

											<td height='30' align="right">
												作者：											</td>
											<td height='30' valign='middle' align="left">
												<input type="text" name="author" class="cms_text" />											</td>
											<td width="8%" height='30' align="right">
												摘要：										</td>
											<td width="16%" height='30' align="center" valign='middle'>
												<input type="text" name="docAbstract" class="cms_text"/>									  </td>
											<td height='30' colspan="2">
												<div align="right">
													<input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">
													<input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUserAll()">
<!--													<input name="searchall" type="button" class="cms_button" value="高级查询" onClick="advancedQuery()">-->
												</div>										  </td>
										</tr>
								  </table>
				  </td>
				</tr>
		  </table>
	
	
	</td>
    <td width="6" valign="top"><img src="../images/querybox_right.gif" width="6" height="62"></td>
  </tr>
</table>

		</form>
	</body>
</html>


