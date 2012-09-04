<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%AccessControl accesscontroler = AccessControl.getInstance();
			accesscontroler.checkAccess(request, response);
			String channelname = request.getParameter("channelName");
			String siteid = request.getParameter("siteid");
			String channelId = request.getParameter("channelId");
			SiteManager siteManager = new SiteManagerImpl();
			String sitename = siteManager.getSiteName(siteid);
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
		form1.action="<%=rootpath%>/cms/channelManage/DocList.jsp?flag=1&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		form1.submit();	
	}
	function queryUserAll()
	{	//查询所有
		form1.action="<%=rootpath%>/cms/channelManage/DocList.jsp?flag=2&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		form1.submit();	
	}
	//默认光标停留在文档标题输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete") return;
		document.all.form1.title.focus();
	}
</script>
		<style type="text/css">
		body {
			background-color: #003399;
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
	<body topmargin="3" rightmargin="0">
		<form id="form1" name="form1" method="post" action="" target="forDocList">
			<table width="810" height="565" border="0" cellpadding="0" align="center" cellspacing="0">
				<tr>
					<td height="82" valign=top background="../images/pagebar.jpg" style="background-repeat:no-repeat">
						<table width="100%" border="0">
							<tr>
								<td height="23">
									&nbsp;&nbsp;&nbsp;&nbsp; 当前位置:
									<%=sitename%>
									站点 >>
									<%=channelname%>
									频道
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									&nbsp;
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
							<tr>
								<td>
									<div align="right">
										| <a href="/creatorcms/cms/main.jsp">欢迎页</a>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td height="483" valign=top background="images/tile.jpg" bgcolor="#FFFFFF">
						<table class="mhwz_table" width="98%" align=center>
							<tr>
								<td>
									<table width="100%" border="0" align="center" class="">
										<tr>
											<td width="11%" height='30' align="center" valign='middle'>
												文档标题：
											</td>
											<td width="11%" height='30' align="center" valign='middle'>
												<div align="left">
													<input name="title" type="text" size="16"  class="cms_text">
												</div>
											</td>
											<td width="11%" height='30' align="center" valign='middle'>
												发稿人：
											</td>
											<td width="11%" height='30' align="left" valign='middle'>

												<select name="userid" class="cms_select" style="width:120">
													<option value="">
														--请选择发稿人--
													</option>
													<%List list = null;
													  DocumentManagerImpl dmi = new DocumentManagerImpl();
													  list = dmi.getDistributeList(channelId);
													  request.setAttribute("dblist", list);
													%>
													<pg:list requestKey="dblist">
														<option value="<pg:cell colName="user_id"/>">
															<pg:cell colName="user_name" />
													</pg:list>
												</select>
											</td>
											<td width="11%" height='30' align="center" valign='middle'>

											</td>
											<td width="39%" height='30' align="center" valign='middle'>

											</td>
											<td width="3%" height='30' align="center" valign='middle'>
												&nbsp;
											</td>
											<td width="3%" height='30' align="left" valign='middle'>
												&nbsp;
											</td>
										</tr>

										<tr>

											<td height='30' valign='middle' align="center">
												文档状态：
											</td>
											<td height='30' valign='middle' align="left">
												<select name="status" class="cms_select" style="width:107">
													<option value="">
														--请选择状态--
													</option>
													<%list = dmi.getStatusList();
													  request.setAttribute("dslist", list);
													%>
													<pg:list requestKey="dslist">
														<option value="<pg:cell colName="status_id"/>">
															<pg:cell colName="status_name" />
													</pg:list>
												</select>
											</td>
											<td width="11%" height='30' align="center" valign='middle'>
												文档类型:
											</td>
											<td width="39%" height='30' align="center" valign='middle'>
												<div align="left">
													<select name="doctype" class="cms_select" style="width:120">
														<option value="">
															--请选择类型--
														</option>
														<option value="0">
															普通文档
														</option>
														<option value="1">
															外部链接
														</option>
														<option value="2">
															外部文件
														</option>
													</select>
												</div>
											</td>
											<td height='30' colspan="4" align="center" valign='middle'>
												<div align="right">
													<input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">
													<input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUserAll()">
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>


