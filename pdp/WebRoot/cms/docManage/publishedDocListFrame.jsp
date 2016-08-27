<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%
//指针对当前站点
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
	String pigeonholer = request.getParameter("flag");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>已发文档列表</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
		<script language="javascript">
		function pigeonhole(){
			//将选中的文档id拼成字符串
			var all = document.getElementsByName("ID");
			var docidStr="";
			for(var i=0;i<all.length;i++){
				if(all[i].checked == true){
					docidStr = docidStr + all[i].value + ";";
				}
			}
			if(haveSelect('ID')){
				pigeonholeForm.action="pigeonholeHandle.jsp?siteId=<%=currentSiteid%>&docidStr="+docidStr;
				pigeonholeForm.target="pigeonholeFrame";
				pigeonholeForm.submit();
				return true;
			}else{
		    	alert("请至少要选择一篇文档！");
		    	return false;
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
					pigeonholeForm.action="docGarbageHandle.jsp?flag=recursive&siteId=<%=currentSiteid%>&docidStr="+docidStr;
					pigeonholeForm.target="pigeonholeFrame";
					pigeonholeForm.submit();
				}
			}else{
		    	alert("请至少要选择一篇文档！");
		   	}
		}
		function edit(docid,docTpye,docChannelName,docSiteid,docChannelid){
			if(confirm('您确定要修改编号为'+docid+'的文档？')){
			if(docTpye==0){
				openWin("doc_gather_update.jsp?siteid=" + docSiteid + "&channelName=" + docChannelName + "&channelId=" + docChannelid + "&docid=" + docid,screen.availWidth-20,screen.availHeight-50);
			}else if(docTpye==1){
				openWin("doc_gather_links_update.jsp?siteid=" + docSiteid + "&channelName=" + docChannelName + "&channelId=" + docChannelid + "&docid=" + docid,screen.availWidth-20,screen.availHeight-50);
			}else if(docTpye==2){
				openWin("doc_gather_filedownload_update?siteid=" + docSiteid + "&channelName=" + docChannelName + "&channelId=" + docChannelid + "&docid=" + docid,screen.availWidth-20,screen.availHeight-50);
			}else if(docTpye==3){
				openWin("doc_gather_aggr_update.jsp?siteid=" + docSiteid + "&channelName=" + docChannelName + "&channelId=" + docChannelid + "&docid=" + docid,screen.availWidth-20,screen.availHeight-50);
			}
		 }
		}
		//设置置顶
		function addArrangeDoc(docid,siteid,channelId)
		{
			openWin("<%=rootpath%>/cms/docManage/docArrange_add.jsp?siteid=" + siteid + "&channelid=" + channelId + "&docid=" + docid,555,240);
		}
		//取消置顶
		function delDocArrange(docid,siteid,channelId)
		{
			if(window.confirm("您确定要取消该文档的置顶吗？"))
			{
				document.forms[0].action = "<%=rootpath%>/cms/docManage/docArrange_del_submit.jsp?siteid=" + siteid + "&channelid=" + channelId + "&docid=" + docid;
				document.forms[0].target="pigeonholeFrame";
				document.forms[0].submit();
			}
		}
		//删除文档
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
				pigeonholeForm.action="document_del_submit.jsp?flag=recursive&siteId=<%=currentSiteid%>&docidStr=" + docidStr;
		        pigeonholeForm.target="pigeonholeFrame";
				pigeonholeForm.submit();
				}else
				{
				return false;
				}
			}else{
		    	alert("请选择要求删除的文档！");
		    	return false;
		   	}
		   	
		}
		//撤销发布
		function withdrawPublish(docId,siteId){
			pigeonholeForm.action = "<%=request.getContextPath()%>/cms/docManage/withdrawPublishHandle.jsp?siteId=" + siteId + "&docId="+docId;
			pigeonholeForm.target = "pigeonholeFrame";
			pigeonholeForm.submit();
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

	<body topmargin="1" rightmargin="1" scroll=auto leftmargin="1">
		<form name="pigeonholeForm" action="" method="post">
			<table width="100%" border="1"  cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
				<tr>
					<td height='25' colspan="9" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div class="DocumentOperT">文档操作：</div>
						<%if(accesscontroler.checkPermission(currentSiteid,AccessControl.ARCHIVE_PERMISSION,AccessControl.SITEDOC_RESOURCE)){%>
						<a style="cursor:hand" onClick="pigeonhole()"><div class="DocumentOper"><img 
							class="operStyle" src="../images/trash.gif">归档</div></a>
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
						<td style="width:3%" align=center height='30'>
							<input class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
						</td>
						<td width="8%" height='30'>文档类型</td>
						<td width="8%" height='30'>是否置顶</td>
						<td width="25%" height='30'>文档标题</td>
						<td width="8%" height='30'>所属频道</td>
						<td width="9%" height='30'>发稿人</td>
						<td width="10%" height='30'>流程</td>
						<td width="15%" height='30'>发布日期</td>
					</tr>
				
					<pg:param name="operate" />
					<pg:param name="channelIds" />
					<pg:param name="siteId" />
					<pg:param name="flag" />
					<pg:param name="title" />
					<pg:param name="flowId" />
					<pg:param name="pigeonholer" />
					
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								暂时没有归档文档
							</td>
						</tr>
					</pg:notify>
					<%
						ContextMenu contextmenu = new ContextMenuImpl();
					%>
					<!--list标签循环输出每条记录-->
					<pg:list>
						<%	
							int document_id = dataSet.getInt("document_id");
							int chanel_id = dataSet.getInt("chanel_id");
							int doctype = dataSet.getInt("doctype");
							String channelName = dataSet.getString("channelName");
							int siteid = dataSet.getInt("siteid");
							boolean isArrangeDoc = docManager.ArrangeDocExist(document_id+"");//是否是置顶							
							
							Menu menu = new Menu();
							menu.setIdentity("doc"+document_id);
							
							if(accesscontroler.checkPermission(chanel_id+"",AccessControl.UPARRANGE_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)||
								accesscontroler.checkPermission(siteid+"",AccessControl.UPARRANGE_PERMISSION,AccessControl.SITEDOC_RESOURCE)){
								if(!isArrangeDoc)
								{
									Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
									menuitem1.setName("设置置顶");
									menuitem1.setLink("javascript:addArrangeDoc('" + document_id + "','" + siteid + "','" + chanel_id + "')");
									menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_szzd.gif");
									menu.addContextMenuItem(menuitem1);
								}
								else
								{
									Menu.ContextMenuItem menuitem = new Menu.ContextMenuItem();
									menuitem.setName("取消置顶");
									menuitem.setLink("javascript:delDocArrange('" + document_id + "','" + siteid + "','" + chanel_id + "')");
									menuitem.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_szzd.gif");
									menu.addContextMenuItem(menuitem);
								}
							}

							if(accesscontroler.checkPermission(chanel_id+"",AccessControl.CHANNEL_UPDATEDOC_PERMISSION,AccessControl.CHANNELDOC_RESOURCE)||accesscontroler.checkPermission(siteid+"",AccessControl.CHANNEL_UPDATEDOC_PERMISSION,AccessControl.SITEDOC_RESOURCE)){
				
									Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
									menuitem2.setName("修改");
									menuitem2.setLink("javascript:edit('" + document_id + "','" + doctype + "','" + channelName + "','" + siteid + "','" + chanel_id + "')");
									menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_edit.gif");
									menu.addContextMenuItem(menuitem2);						
						
									contextmenu.addContextMenu(menu);
							}
							if(accesscontroler.checkPermission(chanel_id+"",AccessControl.WITHDRAWPUBLISH_MANAGER,AccessControl.CHANNELDOC_RESOURCE)
									||accesscontroler.checkPermission(siteid+"",AccessControl.WITHDRAWPUBLISH_MANAGER,AccessControl.SITEDOC_RESOURCE)){
				
									Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
									menuitem2.setName("撤发");
									menuitem2.setLink("javascript:withdrawPublish('" + document_id + "','" + siteid + "')");
									menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/rightMemu/doc_edit.gif");
									menu.addContextMenuItem(menuitem2);						
						
									contextmenu.addContextMenu(menu);
							}
						%>
						<tr class="cms_data_tr" id="<pg:cell colName="document_id" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
							<td class="tablecells" align=center height='25'>
								<input class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="document_id" defaultValue=""/>">
							</td>

							<td class="tablecells" align=center height='25'>
								<pg:equal colName="doctype" value="0">
									<div align="center">
										<img title="HTML文档" src="../images/doc_html.gif" width="20" height="20">
									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="1">
									<div align="center">
										<img title="外部链接类型文档" src="../images/doc_link.gif" width="20" height="20">
									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="2">
									<div align="center">
										<img title="下载文件类型" src="../images/doc_downloadfile.gif" width="15" height="16">
									</div>
								</pg:equal>
								<pg:equal colName="doctype" value="3">
									<div align="center">
										<img title="聚合文件类型" src="../images/doc_aggr.gif" width="15" height="16">
									</div>
								</pg:equal>
							</td>
							<td>
							<%if(isArrangeDoc){%><span class="red_star">置顶</span><%}%>
							</td>
							<td  id="doc<%=document_id%>" class="tablecells" align="left" height="25" style="cursor:hand" 								
									onClick="viewDoc(<pg:cell colName="document_id" defaultValue=""/>,pigeonholeForm,'pigeonholeFrame')" >
								<pg:cell colName="subtitle" defaultValue="" />
							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="channelName" defaultValue="" />
							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="username" defaultValue="" />
							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="flowName" defaultValue="" />
							</td>
							<td class="tablecells" nowrap="nowrap">
								<pg:cell colName="publishTime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
							</td>
						</tr>
					</pg:list>
					<%
						request.setAttribute("opdoc",contextmenu);
					%>
					<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
					<tr class="labeltable_middle_tr_01">
						<td colspan=10><div class="Data_List_Table_Bottom">
							共
							<pg:rowcount />
							条记录
							<pg:index /></div>					  </td>
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


