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
	DocumentManager docManager = new DocumentManagerImpl();
	String userId = accesscontroler.getUserID();
	String operate = request.getParameter("operate");
	String redict = "";
	String link = "归档管理";
	if(operate.equals("pigeonholeQuery")){
		redict = "pigeonholeListFrame.jsp?operate=" + operate;
		link = "归档管理";
	}else if(operate.equals("toPigeonhole")){
		redict = "publishedDocListFrame.jsp?operate=" + operate;
		link = "发布管理";
	}
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
		<script language="JavaScript" src="../../sysmanager/scripts/selectTime.js" type="text/javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
		<script type="text/javascript" src="../../public/datetime/calender_date.js" language="javascript"></script>
		<script language="javascript">
	function queryUser()
	{	

	var startTime = document.all.startTime.value;
	var endTime = document.all.endTime.value;
	if(endTime.length>0 && startTime.length>0)
	if(endTime<startTime)
	{
		alert("结束时间不能小于开始时间!");
		return false;
	}
	//bug432:结束时间不能小于开始时间,weida

		//查询
		form1.action = "<%=redict%>&flag=query";
		form1.target = "pigeonholeListFrame";
		form1.submit();	
	}
	function queryUserAll()
	{	
		//查询所有
		form1.action = "<%=redict%>";
		form1.target = "pigeonholeListFrame";
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
			<table width="100%" height="100%" border="0" cellpadding="0" align="center" cellspacing="0">
				<tr>
				  <td width="5" valign="top" bgcolor="#ffffff"><img src="../images/querybox_left.gif" width="5" height="62"></td>
				  <td  style="background:url(../images/querybox_bg.gif) repeat-x top">
				  <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="query_table">
                            <tr>
								<td colspan="7" height="40">
									&nbsp; 当前位置：归档管理&nbsp; >> &nbsp;<%=link%>
								</td>
							</tr>
                            <tr>
                              <td height='30' align="right">文档标题：</td>
                              <td height='30' align="left" valign='middle'><input name="title" type="text" size="16"  class="cms_text">                              </td>
                              <td height='30' align="center"> 文档流程： </td>
                              <td height='30' align="left" valign='middle'><select name="flowId" class="cms_select" style="width:107">
                                  <option value=""> --请选择流程-- </option>
                                  <% 
														FlowManager fm = new FlowManagerImpl();
														List flowlist = fm.getFlowList2();
														request.setAttribute("dslist", flowlist);
													%>
                                  <pg:list requestKey="dslist">
                                    <option value="<pg:cell colName="flowid"/>                    
                                    ">
                                    <pg:cell colName="flowName" />
                                  </pg:list>
                                </select>                             
                               </td>
                              <%if(operate.endsWith("pigeonholeQuery") && (accesscontroler.isAdmin() || docManager.isPigeonholeManager(Integer.parseInt(userId)))){%>
                              <td height='30' align="right" valign='middle'>归 档 人：</td>
                              <td height='30' align="left" valign='middle'><select name="pigeonholer" class="cms_select" style="width:90">
                                  <option value="">-选归档人-</option>
                                  <option value="1">本人</option>
                                  <option value="2">所有</option>
                                </select>                              </td>
                              <%}%>
                            </tr>
                            <tr>                            
                              <td height="30" align="right" valign="middle">开始时间：</td>
                              <td align="left" valign='middle'><input name="startTime" type="text" size="16"  class="cms_text">
                                  <input name="button" type="button" onClick="showdate(document.all('startTime'))" value="..."/>                              </td>
                              <td height="30" align="right" valign="middle">结束时间：</td>
                              <td align="left" valign='middle'><input name="endTime" type="text" size="16"  class="cms_text">
                                  <input name="button" type="button" onClick="showdate(document.all('endTime'))" value="..."/>                              </td>
                              <td height='30' rowspan=2 colspan=2 valign='bottom'>
                                  <input name="search" type="button" class="cms_button" value="查询" onClick="queryUser()">&nbsp;&nbsp;
                              <input name="searchall" type="button" class="cms_button" value="显示所有" onClick="queryUserAll()"></td>
                            </tr>
                            <tr>
                            	<td height='30' align="right" valign='middle'>所属频道：</td>
                              	<td height='30' align="left" colspan=3 valign='middle'>
                                 <input name="channelNames" type="text" size="60"  class="cms_text" disabled="disabled">
                                 <input name="channelIds" type="hidden" size="16"  class="cms_text" >
                                 <input name="chnlSel" type="button" onClick="selChnl()" value="...">                        
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



