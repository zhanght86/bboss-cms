
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

	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);

	String channelName = request.getParameter("channelName");
	String siteid = request.getParameter("siteid");
	String channelId = request.getParameter("channelId");
	String flag = request.getParameter("flag");
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
	//新增文档
	function adddoc() {
		form1.action="<%=rootpath%>/cms/docManage/doc_gather.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>";
		form1.submit();
	}
	//呈送、发布、归档、回收操作，传来的参数分别为deliver、publish、pigeonhole、garbage
	function operateDoc(operType){
		var tagetJsp = "operate_doc.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&operType="+operType;
		var chinesOperType;
		var isSelect = false;
	    var outMsg;
	    for (var i=0;i<form1.elements.length;i++) {
			var e = form1.elements[i];
			
			if (e.name == 'ID'){
				if (e.checked){
			       		isSelect=true;
			       		break;
			    }
			}
	    }
	    if (isSelect){
	        if (confirm("你确定要进行" + operType + "吗？")){
	        	form1.action=tagetJsp;
	        	form1.target="operIframe";
				form1.submit();
		 		return true;
			}
	    }else{
	    	alert("请至少要选择一篇文档！");
	    	return false;
	   }
  		return false;
	}
	
	function high(ob){
		ob.bgColor="#1199FF";
	}
	function unhigh(ob){
		ob.bgColor="";
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

	<body topmargin="3" rightmargin="0">
		<form name="form1" action="" method="post">
			<table width="100%" align=center cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" id="docListTable">
				<tr>
					<td height='30' colspan="2" nowrap align="left" valign='middle' class="cms_title_blue">
						文档操作：
					</td>
					<td height='30' colspan="7" align="left" valign='middle'>
						<a style="cursor:hand" onClick="openWin('<%=rootpath%>/cms/docManage/doc_gather.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50)"><u><img 
						src="../images/new_doc.gif"class="operStyle">内容采集</u></a>
						<a style="cursor:hand" onClick="operateDoc('呈送')"><u><img 
									class="operStyle" src="../images/ico_point.gif">呈送</u></a> 
						<a style="cursor=hand" onClick="operateDoc('发布')"><u><img 
									class="operStyle" src="../images/folder.gif">发布</u></a> 
						<a style="cursor:hand" onClick="operateDoc('归档')"><u><img 
									class="operStyle" src="../images/trash.gif"</u>归档</a> 
						<a style="cursor:hand" onClick="operateDoc('回收')"><u><img 
									class="operStyle" src="../images/trash_small.gif">回收</u></a>
					</td>
				</tr>
				<pg:listdata dataInfo="DocumentList" keyName="DocumentList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="DocumentList" isList="false">
					<tr class="cms_report_tr">
						<!--设置分页表头-->

						<td width="5%" height="30" align=center>
							<input type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">					  </td>
						<td width="9%">
							文档<br>类型
						</td>
						<td width="42%">
							文档标题
						</td>
						<td width="8%">
							所属频道
						</td>
						<td width="8%">
							状态
						</td>
						<td width="10%">
							发稿人
						</td>
						<td width="8%">
							作者
						</td>
						<td width="10%">
							发稿日期
						</td>
					</tr>
				
					<pg:param name="channelId" />
					<pg:param name="channelName" />
					<pg:param name="siteid" />
					<pg:param name="flag" />
					<pg:param name="title" />
					<pg:param name="status" />
					<pg:param name="userid" />
					<pg:param name="doctype" />
					<!--检测当前页面是否有记录-->
					<pg:notify>
						<tr height="18px" class="labeltable_middle_tr_01">
							<td colspan=100 align='center'>
								该频道暂时没有文档
							</td>
						</tr>
					</pg:notify>

					<!--list标签循环输出每条记录-->
					<pg:list>

						<tr class="cms_data_tr" id="<pg:cell colName="document_id" defaultValue=""/>">
							<input name="docid" value="<pg:cell colName="document_id" defaultValue=""/>" type=hidden>
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

							</td>
							<td class="tablecells" nowrap="nowrap" onMouseOver="high(this)" onMouseOut="unhigh(this)">
								<pg:equal colName="doctype" value="0">
									<a style="cursor:hand"
										onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50)"><pg:cell
											colName="subtitle" defaultValue="" /></a>
								</pg:equal>
								<pg:equal colName="doctype" value="1">
									<a style="cursor:hand"
										onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_links_update.jsp?siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>&docid=<pg:cell colName="document_id" defaultValue=""/>',screen.availWidth-20,screen.availHeight-50)"><pg:cell
											colName="subtitle" defaultValue="" /></a>
								</pg:equal>
								<pg:equal colName="doctype" value="2">
									<a style="cursor:hand"
										onclick="openWin('<%=rootpath%>/cms/docManage/doc_gather_filedownload_update.jsp?docid=<pg:cell colName="document_id" defaultValue=""/>&siteid=<%=siteid%>&channelName=<%=channelName%>&channelId=<%=channelId%>',screen.availWidth-20,screen.availHeight-50)"><pg:cell
											colName="subtitle" defaultValue="" /></a>
								</pg:equal>

							</td>
							<td class="tablecells" nowrap="nowrap" width="16%">

								<%=channelName%>
							</td>
							<td class="tablecells" nowrap="nowrap" width="10%">

								<pg:cell colName="statusname" defaultValue="" />
							</td>

							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:cell colName="username" defaultValue="" />
							</td>
							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:cell colName="author" defaultValue="" />
							</td>
							<td class="tablecells" nowrap="nowrap" width="10%">
								<pg:cell colName="createTime" defaultValue="" />
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
					<input name="queryString" value="<pg:querystring/>" type="hidden">
				</pg:pager>
		  </table>
		</form>
		<iframe height="0" width="0" name="operIframe"></iframe>
	</body>
</html>


