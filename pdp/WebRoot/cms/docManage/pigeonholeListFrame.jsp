<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String userid = accesscontroler.getUserID();
	request.setAttribute("userId",userid);
	
	com.frameworkset.platform.cms.CMSManager cmsManager  = new com.frameworkset.platform.cms.CMSManager();
 	cmsManager.init(request,session,response,accesscontroler);
 	String currentSiteid = cmsManager.getSiteID();
 	request.setAttribute("currentSiteid",currentSiteid);
 	
 	DocumentManager docManager = new DocumentManagerImpl();
	
	String operate = request.getParameter("operate");
	String channelIds = request.getParameter("channelIds");
	String flowId = request.getParameter("flowId");
	String title = request.getParameter("title");
	String flag = request.getParameter("flag");
	String pigeonholer = request.getParameter("pigeonholer");
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");	
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>归档文档列表</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript">
		//导出
		function exportDoc()
		{
			//将选中的文档id拼成字符串
			var all = document.getElementsByName("ID");
			var docidStr="";
			for(var i=0;i<all.length;i++){
				if(all[i].checked == true){
					docidStr = docidStr + all[i].value + ",";
				}
			}
			if(haveSelect('ID')){
				pigeonholeForm.action="doc_export.jsp?siteid=<%=currentSiteid%>&docids=" + docidStr;
		        pigeonholeForm.target="pigeonholeFrame";
				pigeonholeForm.submit();
				//打开模态窗口选择目的频道
				//window.open("doc_export.jsp?batchFlag=1&siteid=<%=currentSiteid%>&docids=" + docidStr,"newwin","");
			}else{
		    	alert("请至少要选择一篇文档！");
		   	}
		}
		function garbage(){
			//将选中的文档id拼成字符串
			var all = document.getElementsByName("ID");
			var docidStr="";
			for(var i=0;i<all.length;i++){
				if(all[i].checked == true){
					docidStr = docidStr + ":" + all[i].value + ";";
				}
			}
			if(haveSelect('ID')){
				if(window.confirm("您确定要回收吗？")){
					pigeonholeForm.action="docGarbageHandle.jsp?docidStr="+docidStr;
					pigeonholeForm.target="pigeonholeFrame";
					pigeonholeForm.submit();
				}
			}else{
		    	alert("请至少要选择一篇文档！");
		   	}
		}
		function delDoc()
		{
			//将选中的文档id拼成字符串
			var all = document.getElementsByName("ID");
			var docidStr="";
			for(var i=0;i<all.length;i++){
				if(all[i].checked == true){
					docidStr = docidStr + all[i].value + ";";
				}
			}
			if(haveSelect('ID')){
			   if(confirm('您确定要删除所选的文档？')){
				//pigeonholeForm.action="document_del_submit.jsp?docidStr=" + docidStr;	需要传站点id,weida
			 pigeonholeForm.action="document_del_submit.jsp?docidStr=" + docidStr + "&siteId=" + <%= currentSiteid %>;
		        pigeonholeForm.target="pigeonholeFrame";
				pigeonholeForm.submit();
				}
				else
				{
				return false;
				}
			}else{
		    	alert("请选择要求删除的文档！");
		    	return false;
		   	}
		}
		function recover(docId,flowId,channelId,siteId){
			openWin("<%=rootpath%>/cms/docManage/doc_statusList.jsp?docId=" + docId + "&flowId=" + flowId + "&channelId=" + channelId + "&siteId=" + siteId);
		}
		</script>
		<style type="text/css">
		body {
			background-color: #fff;
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

	<body topmargin="1" rightmargin="1" scroll=auto leftmargin="1">
		<form name="pigeonholeForm" action="" method="post">
			<table width="100%" border="1"  cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
				<tr>
					<td  height='25' colspan="9" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div  class="DocumentOperT">文档操作：</div>
						<%if(accesscontroler.checkPermission(currentSiteid,AccessControl.EXPORTDOC_PERMISSION,AccessControl.SITEDOC_RESOURCE)){%>
						<a style="cursor:hand" onClick="exportDoc()"><div class="DocumentOper"><img 
							class="operStyle" src="../../sysmanager/images/rightMemu/tpl_reimport.gif">批量下载</div></a>
						<%}
						if(accesscontroler.checkPermission(currentSiteid,AccessControl.TRASH_PERMISSION,AccessControl.SITECHANNEL_RESOURCE)){%>
						<a style="cursor:hand" onClick="garbage()"><div class="DocumentOper"><img 
							class="operStyle" src="../images/trash_small.gif">回收</div></a>
						<%}
						if(accesscontroler.checkPermission(currentSiteid,AccessControl.DELETE_PERMISSION,AccessControl.SITECHANNEL_RESOURCE)){%>
						<a style="cursor:hand" onClick="delDoc()"><div class="DocumentOper"><img 
							class="operStyle" src="../images/trash_small.gif">删除</div></a>	
						<%}%>				
					</td>
				</tr>

				<pg:listdata dataInfo="PigeonholeDocList" keyName="PigeonholeDocList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="PigeonholeDocList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->
						<td width="5%" height='30' align=center style="width:3%">
					  <input  class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')"></td>
						<td width="8%" height='30'>文档类型</td>
						<td width="8%" height='30'>是否置顶</td>
						<td width="30%" height='30'>文档标题</td>
						<td width="10%" height='30'>所属频道</td>
						<td width="8%" height='30'>发稿人</td>
						<td width="10%" height='30'>流程</td>
						<td width="8%" height='30'>归档人</td>
						<td width="13%" height='30'>归档日期</td>
					</tr>
				
					<pg:param name="operate" />
					<pg:param name="channelIds" />
					<pg:param name="flag" />
					<pg:param name="title" />
					<pg:param name="flowId" />
					<pg:param name="pigeonholer" />
					<pg:param name="startTime" />
					<pg:param name="endTime" />
					
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								暂时没有归档文档							</td>
						</tr>
					</pg:notify>

					<%
						ContextMenu contextmenu = new ContextMenuImpl();
					%>
					<!--list标签循环输出每条记录-->
					<pg:list>
						<%	
							int flowid = dataSet.getInt("flowId");
							int document_id = dataSet.getInt("document_id");
							int chanel_id = dataSet.getInt("chanel_id");
							boolean isArrangeDoc = docManager.ArrangeDocExist(document_id+"");//是否是置顶						
							
							Menu menu = new Menu();
							menu.setIdentity("doc"+document_id);
												
							Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
							menuitem1.setName("恢复");
							menuitem1.setLink("javascript:recover('" + document_id + "','" + flowid + "','" + chanel_id + "','" + currentSiteid + "')");
							menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
							menu.addContextMenuItem(menuitem1);
							
							contextmenu.addContextMenu(menu);
						%>
						<tr class="cms_data_tr" id="<pg:cell colName="document_id" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
							<td class="tablecells"><input class="checkbox" hidefocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="document_id" defaultValue=""/>">							</td>

							<td class="tablecells" height='25'>
								<pg:equal colName="doctype" value="0">
									<div align="center">
										<img title="HTML文档" src="../images/doc_html.gif" width="20" height="20">									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="1">
									<div align="center">
										<img title="外部链接类型文档" src="../images/doc_link.gif" width="20" height="20">									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="2">
									<div align="center">
										<img title="下载文件类型" src="../images/doc_downloadfile.gif" width="15" height="16">									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="3">
									<div align="center">
										<img title="聚合文件类型" src="../images/doc_aggr.gif" width="15" height="16">									</div>
							  </pg:equal>							</td>
							<td>
							<%if(isArrangeDoc){%><span class="red_star">置顶</span><%}%>
							</td>
							<td id="doc<%=document_id%>"  class="tablecells" height="25" style="cursor:hand" 
									onMouseOver="high(this)" onMouseOut="unhigh(this)" 
									onClick="viewDoc(<pg:cell colName="document_id" defaultValue=""/>
							,pigeonholeForm,'pigeonholeFrame')" >
								<pg:cell colName="subtitle" defaultValue="" />
							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="channelName" defaultValue="" />							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="username" defaultValue="" />							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="flowName" defaultValue="" />							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="pigeonholePerfomer" defaultValue="" />							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="pigeonholeTime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />							</td>
						</tr>
					</pg:list>
					<%
						request.setAttribute("opdoc",contextmenu);
					%>
					<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
					<tr class="labeltable_middle_tr_01">
						<td  height="18px" colspan=10><div class="Data_List_Table_Bottom">
							共
							<pg:rowcount />
							条记录
					  <pg:index />	</div>					</td>
					</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
					<tr></tr>
				</pg:pager>
		  </table>
		</form>
		<div heigth="0" width="0" style="display:none">
		  	<iframe name="pigeonholeFrame"><iframe/>
		</div>
	</body>
</html>


