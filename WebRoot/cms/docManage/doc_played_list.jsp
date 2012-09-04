<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.countermanager.*"%>
<%@ page import="org.frameworkset.web.servlet.support.WebApplicationContextUtils"%>
<%@ page import="com.frameworkset.platform.cms.countermanager.CounterManager"%>

<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String docId = request.getParameter("docId");
	
	String userName = request.getParameter("userName");
	String ipAddress =  request.getParameter("ipAddress");
	String subTimeBgin = request.getParameter("subTimeBgin");
	String subTimeEnd = request.getParameter("subTimeEnd");
	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>引用文档列表</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<script src="../inc/js/func.js"></script>
		
		<script language="javascript">
			function checkAll(totalCheck,checkName){	//复选框全部选中
			   var selectAll = document.getElementsByName(totalCheck);
			   var o = document.getElementsByName(checkName);
			   if(selectAll[0].checked==true){
				   for (var i=0; i<o.length; i++){
			      	  if(!o[i].disabled){
			      	  	o[i].checked=true;
			      	  }
				   }
			   }else{
				   for (var i=0; i<o.length; i++){
			   	  	  o[i].checked=false;
			   	   }
			   }
			}
			//单个选中复选框
			function checkOne(totalCheck,checkName){
			   var selectAll = document.getElementsByName(totalCheck);
			   var o = document.getElementsByName(checkName);
				var cbs = true;
				for (var i=0;i<o.length;i++){
					if(!o[i].disabled){
						if (o[i].checked==false){
							cbs=false;
						}
					}
				}
				if(cbs){
					selectAll[0].checked=true;
				}else{
					selectAll[0].checked=false;
				}
			}
			//判断是否有选择
			function haveSelect(elName){
				var isSelect = false;
				var ch = document.getElementsByName(elName);
				for (var i=0;i<ch.length;i++) {
					if (ch[i].checked){
						isSelect=true;
						break;
					}
				}
				return isSelect;
			}	
		</script>
	</head>
	<body topmargin="2" scroll=no leftmargin="0">
		<form name="commentDocForm" method="post">
			<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="Datalisttable">
				<pg:listdata dataInfo="com.frameworkset.platform.cms.countermanager.DocPlayedList" keyName="DocPlayedList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="12" scope="request" data="DocPlayedList" isList="false">
				<tr class="cms_report_tr">
					<td width="4%" height="30" align=center>
						<input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
					</td>
					<td>点播用户</td>
					<td>视频地址</td>
					<td>用户IP</td>
					<td>播放时间</td>
				</tr>
				<pg:param name="docId" />
				<pg:param name="userName" />
				<pg:param name="ipAddress" />
				<pg:param name="subTimeBgin" />
				<pg:param name="subTimeEnd" />
				
				<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该视频暂时没有被播放过
							</td>
						</tr>
				  </pg:notify>

				<!--list标签循环输出每条记录-->
				<pg:list>
				<tr onMouseOver="high(this)" onMouseOut="unhigh(this)">
					<td height="30" align=center>
						<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName='hitId' />">
					</td>
					<td>
						<pg:cell colName="hitUser" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="videoPath" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="hitIP" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="hitTime" defaultValue="" />
					</td>
				</tr>
				</pg:list>
					<tr class="labeltable_middle_tr_01">
						<td colspan=8 align='center' height="18px">
							共
							<pg:rowcount />
							条记录
							<pg:index />
						</td>
					</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
					<tr></tr>
				</pg:pager>
		  </table>
		</form>
		<div height="0" width="0" style="display:none">
			<iframe name="docComIframe"></iframe>
		</div>
	</body>	
</html>