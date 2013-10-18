<%
	/**
	  * 站内文档查询
	  */
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.security.*,java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.docsourcemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.CMSManager"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	CMSManager cmsManager = new CMSManager();
	cmsManager.init(request,session,accesscontroler);
	Site site = cmsManager.getCurrentSite();

	//String channelname = request.getParameter("channelName");
	String siteid = site.getSiteId() + "";//request.getParameter("siteid");
	//String channelId = request.getParameter("channelId");
	//SiteManager siteManager = new SiteManagerImpl();
	String sitename = site.getName();
%>
<html><head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>内容管理主框架</title>
		<script src="../inc/js/func.js"></script>
		<script src="${pageContext.request.contextPath}/include/jquery-1.4.2.min.js"></script>
		<script src="${pageContext.request.contextPath}/include/security.js"></script>
		<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
		<script language="javascript"><!--
		function queryUser()
		{	
			//查询
			if(validate()){
				//form1.action="<%=rootpath%>/cms/docManage/siteDoc_list.jsp?flag=query&siteid=<%=siteid%>"
				//form1.target="sitedocList";
			//	form1.submit();	
				
					$.secutiry.dosubmit("form1",
						"<%=rootpath%>/cms/docManage/siteDoc_list.jsp?flag=query&siteid=<%=siteid%>",
								"sitedocList",
								"<%=rootpath%>");
			}
		}
		function queryUserAll()
		{	//查询所有
			//form1.action="<%=rootpath%>/cms/docManage/siteDoc_list.jsp?siteid=<%=siteid%>"
			//form1.target="sitedocList";
			//form1.submit();	
			$.secutiry.dosubmit("form1",
						"<%=rootpath%>/cms/docManage/siteDoc_list.jsp?siteid=<%=siteid%>",
								"sitedocList",
								"<%=rootpath%>");
		}
		//默认光标停留在文档标题输入框
		function document.onreadystatechange(){
			if (document.readyState!="complete") return;
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
			//form1.action="<%=rootpath%>/cms/docManage/queryFrame.jsp?siteid=<%=siteid%>"
			//form1.target="forQuery";
			//form1.submit();	
			$.secutiry.dosubmit("form1",
						"<%=rootpath%>/cms/docManage/queryFrame.jsp?siteid=<%=siteid%>",
								"forQuery",
								"<%=rootpath%>");
		}
--></script>
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
	<body leftmargin="0" topmargin="0" rightmargin="0">
		<form id="form1" name="form1" method="post" action="">
			<table width="100%" height="100%" border="0" cellpadding="0" align="center" cellspacing="0">
				<tr>
				  <td width="5" valign="top" bgcolor="#ffffff"><img src="../images/querybox_left.gif" width="5" height="62"></td>
					<td  style="background:url(../images/querybox_bg.gif) repeat-x top"><table width="100%" border="0" cellpadding="4" cellspacing="0">
							<tr>
								<td height="23">&nbsp; 当前位置:
									【<%=sitename%>
									站点】文档查询
								</td>
								<td align="right">&nbsp;
								<td>							
							</tr>
						</table>	
						
						<table width="100%" border="0" align="center" cellpadding="2" cellspacing="0" class="query_table">
                          <tr>
                            <td height='24' align="right"> 文 档 标 题 ： </td>
                            <td height='0' align="left" valign='middle'><div align="left">
                                <input name="title" type="text" size="16"  class="cms_text">
                            </div></td>
                            <td height='0' align="right"> 发 稿 人： </td>
                            <td height='0' align="left" valign='middle'><select name="userid" class="cms_select" style="width:118">
                                <option value="">--请选择发稿人--</option>
                                <%					  List list = null;
													  DocumentManagerImpl dmi = new DocumentManagerImpl();
													  list = dmi.getDistributeListOfSite(siteid);
													  request.setAttribute("dblist", list);
													%>
                                <pg:list requestKey="dblist">
                                  <option value="<pg:cell colName="user_id"/>">
                                  <pg:cell colName="user_name" />
                                </pg:list>
                              </select>                            </td>
                            <td height='0' align="right"> 作 &nbsp;&nbsp;&nbsp;者： </td>
                            <td height='0' align="left" valign='middle'><input name="author" type="text" size="16"  class="cms_text">                            </td>
                            <td rowspan="3" align="left" valign='middle'></td>
                          </tr>
                          <tr>
                            <td height='24' align="right"> 文 档 状 态 ： </td>
                            <td height='0' align="left" valign='middle'><select name="status" class="cms_select" style="width:107">
                                <option value="">--请选择状态--</option>
                                <%
                                					  List statuslist = dmi.getStatusList();
													  request.setAttribute("dslist", statuslist);
													%>
                                <pg:list requestKey="dslist">
                                  <option value="<pg:cell colName="status_id"/>">
                                  <pg:cell colName="status_name" />
                                </pg:list>
                              </select>                            </td>
                            <td height='0' align="right"> 文档类型： </td>
                            <td height='0' align="center" valign='middle'><div align="left">
                                <select name="doctype" class="cms_select" style="width:118">
                                  <option value="">--请选择类型-- </option>
                                  <option value="0">普通文档</option>
                                  <option value="1">外部链接</option>
                                  <option value="3">聚合文档</option>
                                </select>
                            </div></td>
                            <td height='0' align="right"> 摘 &nbsp;&nbsp;&nbsp;要： </td>
                            <td height='0' align="left" valign='middle'><input name="docAbstract" type="text" size="16"  class="cms_text">                            </td>
                          </tr>
                          <tr>
                            <td height='24' align="right"> 文 档 来 源 ： </td>
                            <td align="left" valign='middle'><span class="cms_title_blue">
                              <select name="docsorid" class="cms_select" style="width:107">
                                <option value="">--请选择来源--</option>
                                <%
                          							List dslist=null;
						  							dslist=dmi.getDocSourceList();
						  							request.setAttribute("dslist",dslist);
													%>
                                <pg:list requestKey="dslist">
                                  <option value="<pg:cell colName="docsource_id"/>">
                                  <pg:cell colName="srcname"/>
                                </pg:list>
                              </select>
                            </span> </td>
                            <td align="right"> 关&nbsp;键&nbsp;词： </td>
                            <td align="left" valign='middle'><input name="keyword" type="text" size="18"  class="cms_text">                            </td>
                            <td align="right"> 文档内容： </td>
                            <td align="left" valign='middle'><input name="content" type="text" size="16"  class="cms_text">                            </td>
                          </tr>
                          <tr>
                            <td width="0" height='24' align="right"> 撰写开始时刻： </td>
                            <td width="0" height='0' align="left" valign='middle'><input name="createTimeBegin" type="text" size="16"  class="cms_text">
                                <input name="button" type="button" onClick="showdate(document.all('createTimeBegin'))" value="..."/>
                                <!--									  			<input name="button3" type="button" onClick="selectTime('form1.createTimeBegin',0)" value=".">											-->                            </td>
                            <td width="0" height='0' align="right"> 结束时刻： </td>
                            <td width="0" height='0' align="left" valign='middle'><input name="createTimeEnd" type="text" size="18"  class="cms_text">
                                <input name="button" type="button" onClick="showdate(document.all('createTimeEnd'))" value="..."/>
                                <!--							      	      		<input name="button" type="button" onClick="selectTime('form1.createTimeEnd',0)" value=".">											-->                            </td>
                            <td width="0" height='0' align="right"> 文档级别： </td>
                            <td width="0" height="0"><select name="docLevel" class="cms_select" style="width:107">
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
                              </select>                            </td>
                            <td height='0' align="center" valign='middle'><div align="right">
                                <input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">
                                <input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUserAll()">
                            </div></td>
                          </tr>
                        </table></td>
				    <td width="6" valign="top" bgcolor="#ffffff"><img src="../images/querybox_right.gif" width="6" height="62"></td>
				</tr>
		  </table>
		</form>
</body>
</html>


