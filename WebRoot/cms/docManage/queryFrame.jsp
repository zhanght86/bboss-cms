<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*,com.frameworkset.platform.cms.util.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.SiteCacheManager"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.ChannelCacheManager"%>
<%@ page import="org.frameworkset.web.servlet.support.WebApplicationContextUtils"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String channelname = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();
	ChannelCacheManager cm = (ChannelCacheManager)SiteCacheManager.getInstance().getChannelCacheManager(siteid);
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="<%=rootpath%>/include/jquery-1.4.2.min.js"></script>
		<script src="<%=rootpath%>/include/security.js"></script>
		<script src="../inc/js/func.js"></script>
		
		<script language="javascript">
	function queryUser()
	{	
		//查询
		$.secutiry.dosubmit("form1",
				"<%=rootpath%>/cms/docManage/doc_list.jsp?flag=query&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>",
						"forDocList",
						"<%=rootpath%>");
		//form1.action="<%=rootpath%>/cms/docManage/doc_list.jsp?flag=query&siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		//form1.target="forDocList";
		//form1.submit();	
	}
	function queryUserAll()
	{	//查询所有
		//form1.action="<%=rootpath%>/cms/docManage/doc_list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		//form1.target="forDocList";
		//form1.submit();
		$.secutiry.dosubmitwithreset("form1",
				"<%=rootpath%>/cms/docManage/doc_list.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>",
						"forDocList",
						"<%=rootpath%>");
		//form1.reset();
	}
	//默认光标停留在文档标题输入框
	function document.onreadystatechange(){
		if (document.readyState!="complete") return;
		document.all.form1.title.focus();
	}
	function advancedQuery(){
		$.secutiry.dosubmit("form1",
				"<%=rootpath%>/cms/docManage/advancedQueryFrame.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>",
						"forQuery",
						"<%=rootpath%>");
		//form1.action="<%=rootpath%>/cms/docManage/advancedQueryFrame.jsp?siteid=<%=siteid%>&channelName=<%=channelname%>&channelId=<%=channelId%>"
		//form1.target="forQuery";
		//form1.submit();	
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
									站点
									<%
										String tmpchlid = cm.getChannel(channelId).getParentChannelId() + "";
										String outstr = "";
										while(!tmpchlid.equals("0"))
										{
											outstr = ">>" + cm.getChannel(tmpchlid).getDisplayName() + " 频道" + outstr;
											tmpchlid = cm.getChannel(tmpchlid).getParentChannelId() + "";
										}
									%>
									<%=outstr%>
									>>
									<%=channelname%>
									频道								</td>
								<td align="right" nowrap="nowrap">
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
													<input name="title" type="text" size="16"  class="cms_text">
												</div>										  </td>
											<td height='30' align="right">
												发稿人：											</td>
											<td height='30' align="left" valign='middle'>
												<select name="userid" class="cms_select" style="width:120">
													<option value="">
														--请选择发稿人--													</option>
													<%List list = null;
													  DocumentManager dmi = (DocumentManager)WebApplicationContextUtils.getWebApplicationContext().getBeanObject("documentManager");
													  list = dmi.getDistributeList(channelId);
													  request.setAttribute("dblist", list);
													%>
													<pg:list requestKey="dblist">
														<option value="<pg:cell colName="user_id"/>">
															<pg:cell colName="user_name" />
													</pg:list>
												</select>										  </td>
										  <td height='30' align="right">
											文档级别：									</td>
										  <td height='30' align="left" valign='middle' colspan="3">	
											<select name="docLevel" class="cms_select">
												<option value="">--请选择文档级别--</option>
												<%
													List doclevellist = null;
													doclevellist = dmi.getDocLevelList();
													request.setAttribute("doclevellist",doclevellist);
												%>
												<pg:list requestKey="doclevellist">
													<option value="<pg:cell colName="id"/>">
														<pg:cell colName="name"/> 
												</pg:list>
											</select>
											
											<td height='30' align="right">文档分类：</td>
											<td height='30' align="left" valign='middle' colspan="3">
												<select name="docClass" class="cms_select">
													<option value="">--请选择文档分类--</option>
													<%
														request.setAttribute("docClassList",CMSUtil.getDocClassList(siteid));
													%>
													<pg:list requestKey="docClassList">
														<option value="<pg:cell colName='class_name'/>">
															<pg:cell colName="class_name"/> 
														</option>
													</pg:list>
												</select>
											</td>
											
									    </tr>

										<tr>

											<td height='30' align="right">
												文档状态：											</td>
											<td height='30' valign='middle' align="left">
												<select name="status" class="cms_select" style="width:107">
													<option value="">
														--请选择状态--													</option>
													<%list = dmi.getStatusList();
													  request.setAttribute("dslist", list);
													%>
													<pg:list requestKey="dslist">
														<option value="<pg:cell colName="status_id"/>">
															<pg:cell colName="status_name" />
													</pg:list>
												</select>											</td>
											<td width="8%" height='30' align="right">
												文档类型:											</td>
											<td width="16%" height='30' align="center" valign='middle'>
												<div align="left">
													<select name="doctype" class="cms_select" style="width:120">
														<option value="">
															--请选择类型--														</option>
														<option value="0">
															普通文档														</option>
														<option value="1">
															外部链接														</option>
														<option value="3">
															聚合文档														</option>
													</select>
												</div>										  </td>
											<td width="8%" height='30' align="right">
												评论状态:											</td>
											<td height='30' colspan="3">
											<div align="left">
													<select name="iscomment" class="cms_select" style="width:120">
														<option value="">
															--请选择--														</option>
														<option value="0">
															未发布														</option>
													</select>
											</div>
											</td>
											<td colspan="4"><div align="center">
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
