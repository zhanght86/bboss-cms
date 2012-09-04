<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	String siteid = request.getParameter("siteid");
	String channelid = request.getParameter("channelid");
	
	//ChannelManager chnl = new ChannelManagerImpl();
	//String siteid = String.valueOf(chnl.getChannelInfo(channelid).getSiteId());
	
	String docName = request.getParameter("docName");
	String chnlName = request.getParameter("chnlName");
	String citeUserName = request.getParameter("citeUserName");
	String srcChnlName = request.getParameter("srcChnlName");
	String citeTimeBgin = request.getParameter("citeTimeBgin");
	String citeTimeEnd = request.getParameter("citeTimeEnd");
	String queryFlag = request.getParameter("queryFlag");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>引用文档列表</title>
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
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
			function withdrawCitedDoc(){
				if(haveSelect("ID")){
					//将选中的文档id拼成字符串
					var all = document.getElementsByName("ID");
					var docidStr="";
					for(var i=0;i<all.length;i++){
						if(all[i].checked == true){
							docidStr = docidStr + all[i].value + ",";
						}
					}
					var urls = "<%=rootpath%>/cms/docManage/withdrawCitedHandle.jsp?siteid=<%=siteid%>&channelid=<%=channelid%>&docids=" + docidStr;
					window.urlstr = urls;
					showModalDialog("../doing_iframe.html",window,"dialogWidth:"+(screen.availWidth-200)+"px;dialogHeight:"+(screen.availHeight-150)+"px;help:no;scroll:auto;status:no");
					
				}
				else{
					alert("至少选择一篇引用文档");
				}
			}	
			function citeDoc(){
				//打开模态窗口选择引用文档
				openWin("<%=rootpath%>/cms/channelManage/channel_doc_xz.jsp?flag=3&channelid=<%=channelid%>&siteid=<%=siteid%>",800,550);
			}
			function citechnl(){
				openWin("multi_channel_select_frame.jsp?taskType=cite&channelid=<%=channelid%>&siteid=<%=siteid%>",400,500);	
			}	
		</script>

	</head>
	<body topmargin="2" scroll=no leftmargin="0">
		<form name="citeDocForm" method="post">
			<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="Datalisttable">
				<tr>
					<td colspan=9  height="25" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div class="DocumentOperT">操作：</div>
					<%if(accesscontroler.checkPermission(channelid, AccessControl.DELETE_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)){
					%>
					<a style="cursor:hand" onClick="withdrawCitedDoc()"><div class="DocumentOper"><img
									class="operStyle" src="../images/audity.gif">取消引用</div></a>
					<%}
					if(accesscontroler.checkPermission(channelid, AccessControl.ADD_PERMISSION,
								AccessControl.CHANNEL_RESOURCE)){
					%>
					<a style="cursor:hand" onClick="citeDoc()"><div class="DocumentOper"><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">文档引用</div></a>
					<a style="cursor:hand" onClick="citechnl()"><div class="DocumentOper"><img 
									class="operStyle" src="../../sysmanager/images/plan.gif">频道引用</div></a>
				  <%}%>				  
				  </td>
				</tr>
				<pg:listdata dataInfo="CitedDocumentList" keyName="CitedDocumentList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="10" scope="request" data="CitedDocumentList" isList="false">
				<tr class="cms_report_tr">
					<td width="4%" height="30" align=center>
						<input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
					</td>
					<td width="22%">文档名称</td>
					<td width="8%">文档状态</td>
					<td width="10%">源站点名</td>
					<td width="10%">源频道名</td>
					<td width="10%">当前频道名</td>
					<td width="10%">引用人</td>
					<td width="8%">引用类型</td>
					<td width="16%">引用日期</td>
				</tr>
				<pg:param name="channelid" />
				<pg:param name="siteid" />
				<pg:param name="docName" />
				<pg:param name="chnlName" />
				<pg:param name="citeUserName" />
				<pg:param name="srcChnlName" />
				<pg:param name="citeTimeBgin" />
				<pg:param name="citeTimeEnd" />
				<pg:param name="queryFlag" />
				<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该频道暂时没有引用文档
							</td>
						</tr>
				  </pg:notify>

				<!--list标签循环输出每条记录-->
				<pg:list>
				<%
					int citeType = dataSet.getInt("citeType");
					String citeTypeStr = citeType==0?"文档引用":"频道引用";
				%>
				<tr onMouseOver="high(this)" onMouseOut="unhigh(this)">
					<td height="30" align=center>
						<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<pg:cell colName="docid" defaultValue=""/>">
					</td>
					<td onClick="">
						<pg:cell colName="docName" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="statusName" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="srcSiteName" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="srcChannelName" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="curChannelName" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="citeUserName" defaultValue="" />
					</td>
					<td>
						<%=citeTypeStr%>
					</td>
					<td>
						<pg:cell colName="citeTime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
					</td>
				</tr>
				</pg:list>
					<tr class="labeltable_middle_tr_01">
						<td colspan=9 align='center' height="18px">
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
			<iframe name="citedDocIframe"></iframe>
		</div>
	</body>	
</html>