<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String channelName = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	String flag = request.getParameter("flag");
	SiteManager siteManager = new SiteManagerImpl();
	String sitename = siteManager.getSiteInfo(siteid).getName();
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/treeview.css">
		<link rel="stylesheet" type="text/css" href="<%=rootpath%>/sysmanager/css/windows.css">
		<title>内容管理主框架</title>
<script src="../inc/js/func.js"></script>
<script language="JavaScript" src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script language="javascript">
	var arr = new Array();
	var channelId = <%=channelId%>;
	var docSiteId = <%=siteid%>;
	function getArr(){
		return arr;
	}
 	function checkAll(totalCheck,checkName){	//复选框全部选中
	   var selectAll = document.getElementsByName(totalCheck);
	   var o = document.getElementsByName(checkName);
	
	   var docid = document.getElementsByName("docid");
	   var docListTable = document.all.docListTable;
	   var tablerows = docListTable.rows;
	   var j = 0;

	   if(selectAll[0].checked==true){
		   for (var i=0; i<o.length; i++){
	      	  if(!o[i].disabled){
	      	  	o[i].checked=true;
	      	  }
			  var obj = new Array();
			  obj[0] = docid[i].value;
			  obj[1] = channelId;
			  obj[2] = (tablerows[i+1].cells)[2].innerText;
			  obj[3] = docSiteId;
			  arr[j] = obj;
			  j++;
		   }
	   }else{
		   for (var i=0; i<o.length; i++){
	   	  	  o[i].checked=false;
			  obj = new Array();
			  arr = new Array();
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
			checkAll(totalCheck,checkName);//
		}else{
			selectAll[0].checked=false;
		}
	}		
	
</script>
<style type="text/css">
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

	<body leftmargin="0" topmargin="2" rightmargin="0">
		<form name="form1" target="listdoc" action="" method="post">
			<input type="hidden" name="chlName" value="<%=channelName%>"/>
			<table width="100%" border="0" align=center cellpadding="3" cellspacing="0" id="docListTable" class="Datalisttable">
				
				<pg:listdata dataInfo="SelectDocList" keyName="SelectDocList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="SelectDocList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td width="3%" height="30" align=center>
							<input type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">					  </td>
						<td width="12%">
							文档类型				</td>
						<td width="42%">
							文档标题
						</td>
						<!--<td width="8%">
							所属频道
						</td>-->
						<td width="10%">
							状态						</td>
						<td width="10%">
							发稿人
						</td>
						<td width="10%">
							作者						</td>
						<td width="15%">
							发稿日期						</td>
					</tr>
				
					<pg:param name="channelId" />
					<pg:param name="channelName" />
					<pg:param name="siteid" />
					<pg:param name="flag" />
					<pg:param name="title" />
					<pg:param name="status" />
					<pg:param name="userid" />
					<pg:param name="doctype" />
					<pg:param name="createTimeBegin" />
					<pg:param name="createTimeEnd" />
					<pg:param name="docsorid" />
					<pg:param name="keyword" />
					<pg:param name="author" />
					<pg:param name="docAbstract" />
					<pg:param name="content" />
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>
								该列表没有任何记录
							</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<pg:list>

						<tr class="cms_data_tr" onMouseOver="high(this)" onMouseOut="unhigh(this)" id="<pg:cell colName="document_id" defaultValue=""/>">
							<input name="docid" value="<pg:cell colName="document_id" defaultValue=""/>" type=hidden>
							<input name="subtitle" value="<pg:cell colName="subtitle" defaultValue=""/>" type=hidden>
							<input name="channelName" value="<pg:cell colName="channelName" defaultValue=""/>" type=hidden>
							<td class="tablecells" align=center>
								<input hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="document_id" defaultValue=""/>">
							</td>

							<td class="tablecells" align=center>
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
<!--							<td class="tablecells" nowrap="nowrap" style="cursor:hand" <pg:equal colName="doctype" value="0">onClick="viewDoc(<pg:cell colName="document_id" defaultValue=""/>,form1,'listdoc')"</pg:equal>>-->
							<td class="tablecells">
								<pg:cell colName="subtitle" defaultValue="" />
							</td>
							<!--<td class="tablecells" nowrap="nowrap" width="16%">

								<%=channelName%>
							</td>-->
							<td class="tablecells" width="10%">

								<pg:cell colName="statusname" defaultValue="" />
							</td>

							<td class="tablecells" width="10%">
								<pg:cell colName="username" defaultValue="" />
							</td>
							<td class="tablecells"  width="10%">
								<pg:cell colName="author" defaultValue="" />
							</td>
							<td class="tablecells" width="10%">
								<pg:cell colName="docwtime" defaultValue="" dateformat="yyyy/MM/dd HH:mm:ss" />
							</td>
						</tr>
					</pg:list>
					<tr height="18px" class="labeltable_middle_tr_01">
						<td colspan=10 align='center'>
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
		<iframe height="0" width="0" name="listdoc"></iframe>
</body>
</html>