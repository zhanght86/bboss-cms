<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.*"%>
<%@page import="com.frameworkset.platform.security.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String docid = request.getParameter("docid");	
	String queryFlag = request.getParameter("queryFlag");
	String versionNum = request.getParameter("versionNum");
	String docVerTimeBgin = request.getParameter("docVerTimeBgin");
	String docVerTimeEnd = request.getParameter("docVerTimeEnd");
	String docVerUser = request.getParameter("docVerUser");
	String docVerLable = request.getParameter("docVerLable");
	
%>
<html>
	<head>
		<base target=_self><!-- use for submit to self-->
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<title>.::::::::::文档版本管理::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::..</title>
		<script src="../inc/js/func.js"></script>
		<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
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
	//
	function manageDocVer(jspurl,docid,version)
	{
		openWin(jspurl+"?docid="+docid+"&version="+version,800,550);
	}
	function seeDescription(docid,version,opername){
		openWin("see_version_description.jsp?docid=" + docid +"&version=" + version + "&opername=" + opername,400,500);
	}
	</script>
	
	</head>

	<body topmargin="2" rightmargin="0">
		<form target="managedocversion" name="manageDocVerform" action="" method="post">
			<table class="Datalisttable" width="100%" height="100%" border="0" align=center cellpadding="3" cellspacing="0" bgcolor="#FFFFFF" id="docVerListTable">
<!--				<tr>-->
<!--					<td height='30' colspan="6" nowrap align="left" valign='middle' class="cms_title_blue">-->
<!--						文档版本管理：-->
<!--					</td>-->
<!--				</tr>-->
				<pg:listdata dataInfo="DocumentVerList" keyName="DocumentVerList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="DocumentVerList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

<!--						<td style="width:5%" align=center height='30'>-->
<!--							<input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">-->
<!--						</td>-->
						<td width="42%" height='30'>
							文档标题
						</td>
						<td width="10%" height='30'>
							版本号
						</td>
						<td width="10%" height='30'>
							版本标签
						</td>
						<td width="14%" height='30'>
							版本保存人
						</td>
						<td width="*" height='30'>
							保存时间
						</td>
<!--						<td width="*" height='30'>-->
<!--							操作-->
<!--						</td>-->
					</tr>
					
					<pg:param name="docid" />
					<pg:param name="queryFlag" />
					<pg:param name="versionNum" />
					<pg:param name="docVerTimeBgin" />
					<pg:param name="docVerTimeBgin" />
					<pg:param name="docVerUser" />
					<pg:param name="docVerUser" />
					
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该文档暂时没有版本
							</td>
						</tr>
					</pg:notify>
					<%
						ContextMenu contextmenu = new ContextMenuImpl();
					%>
					<!--list标签循环输出每条记录-->
					<pg:list>
						<%	
							int version = dataSet.getInt("version");
							String verDiscription = dataSet.getString("verDiscription");
							String verLable = dataSet.getString("verLable");
							String subtitle = dataSet.getString("subtitle");
							String username = dataSet.getString("username");																
							
							Menu menu = new Menu();
							menu.setIdentity("doc"+version);
												
							Menu.ContextMenuItem menuitem1 = new Menu.ContextMenuItem();
							menuitem1.setName("删除版本");
							menuitem1.setLink("javascript:manageDocVer('del_doc_version.jsp','" + docid + "','" + version + "')");
							menuitem1.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
							menu.addContextMenuItem(menuitem1);
				
							Menu.ContextMenuItem menuitem2 = new Menu.ContextMenuItem();
							menuitem2.setName("恢复版本");
							menuitem2.setLink("javascript:manageDocVer('resume_doc_version.jsp','" + docid + "','" + version + "')");
							menuitem2.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
							menu.addContextMenuItem(menuitem2);	
							
							Menu.ContextMenuItem menuitem3 = new Menu.ContextMenuItem();
							menuitem3.setName("查看注释");
							menuitem3.setLink("javascript:seeDescription('" + docid + "','" + version + "','" + username + "')");
							menuitem3.setIcon(request.getContextPath() +"/sysmanager/images/actions.gif");
							menu.addContextMenuItem(menuitem3);					
				
							contextmenu.addContextMenu(menu);
						%>
						<tr class="cms_data_tr" id="<pg:cell colName="document_id" defaultValue=""/>" onMouseOver="high(this)" onMouseOut="unhigh(this)">
							<input name="docid" value="<pg:cell colName="document_id" defaultValue=""/>" type=hidden>
							<input name="version" value="<pg:cell colName="version" defaultValue=""/>" type=hidden>
<!--							<td class="tablecells" align=center height='30' width="5%">-->
<!--								<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="document_id" defaultValue=""/>">-->
<!--							</td>-->
							
							<td id="doc<%=version%>" class="tablecells" height="30" width="42%" style="cursor:hand"  onClick="viewDoc(<pg:cell colName="document_id" defaultValue=""/>,manageDocVerform,'managedocversion')">
								<pg:cell colName="subtitle" defaultValue="" />
							
							</td>
							<td class="tablecells" width="10%">
								<pg:cell colName="version" defaultValue="" />
							</td>
							<td class="tablecells" width="10%">
								<pg:cell colName="verLable" defaultValue="" />
							</td>
							<td class="tablecells"  width="10%">
								<pg:cell colName="username" defaultValue="" />
							</td>
							<td class="tablecells"  width="*">
								<pg:cell colName="opTime"  dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
							</td>
<!--							<td class="tablecells" nowrap="nowrap" align="center" width="*">-->
<!--								<a style="cursor:hand" onClick="manageDocVer('del_doc_version.jsp','<pg:cell colName="document_id" defaultValue=""/>','<pg:cell colName="version" defaultValue=""/>')"><u>删除版本</u></a>-->
<!--								<a style="cursor:hand" onClick="manageDocVer('resume_doc_version.jsp','<pg:cell colName="document_id" defaultValue=""/>','<pg:cell colName="version" defaultValue=""/>')"><u>恢复版本</u></a>-->
<!--							</td>-->
						</tr>
					</pg:list>
					<%
						request.setAttribute("opdoc",contextmenu);
					%>
					<pg:contextmenu context="opdoc" enablecontextmenu="true" scope="request"/>
					<tr class="labeltable_middle_tr_01">
						<td colspan=10 align='center'  height="18px"><div class="Data_List_Table_Bottom">
							共
							<pg:rowcount />
							条记录
							<pg:index /></div>
						</td>
					</tr>
					<input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
					<tr></tr>
				</pg:pager>

		  </table>
		</form>
		<iframe height="0" width="0" name="managedocversion"></iframe>
	</body>
</html>


