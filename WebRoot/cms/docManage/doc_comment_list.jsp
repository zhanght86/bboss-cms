<%@ page contentType="text/html;charset=UTF-8" import="java.util.*" language="java"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	String docId = request.getParameter("docId");
	
	String docComment = request.getParameter("docComment");
	String userName = request.getParameter("userName");
	String subTimeBgin = request.getParameter("subTimeBgin");
	String subTimeEnd = request.getParameter("subTimeEnd");
	String queryFlag = request.getParameter("queryFlag");
	String status = request.getParameter("status");
	String hasImpeach = request.getParameter("hasImpeach");
	
	DocCommentManager ddm = new DocCommentManagerImpl();
	
	
	
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
			function deleteComments(){
				if(haveSelect("ID")){
					commentDocForm.action = "docCommentHandle.jsp?action=delete";
					commentDocForm.target = "docComIframe";
					commentDocForm.submit();
				}
				else{
					alert("至少选择一篇引用文档");
				}
			}	
			function publishComments(){
				if(haveSelect("ID")){
					commentDocForm.action = "docCommentHandle.jsp?action=publish";
					commentDocForm.target = "docComIframe";
					commentDocForm.submit();
				}
				else{
					alert("至少选择一篇引用文档");
				}
			}	
			function addOneComment(){
				//打开模态窗口增加一条评论
				var re = openWin("add_one_comment.jsp?docId="+<%=docId%>,400,600);
				if(re=="cf"){
					var str = document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						strArray= str.slice(0,end);
					else
						strArray = str;
					document.location.href = strArray+"?"+document.all.queryString.value;
				}
			}	
			function seeDocComment(docTitle,userName,subtime,docComment,userIP,commentId){
				var re = openWin("see_doc_comment.jsp?docTitle="+docTitle+"&userName="+userName+"&subtime="+subtime+"&docComment="+docComment+"&userIP="+userIP+"&commentId="+commentId,400,550);
				if(re=="cf"){
					var str = document.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
						strArray= str.slice(0,end);
					else
						strArray = str;
					document.location.href = strArray+"?"+document.all.queryString.value;
				}
			}
			function withdrawPubComments(){
				if(haveSelect("ID")){
					commentDocForm.action = "docCommentHandle.jsp?action=withdrawPub";
					commentDocForm.target = "docComIframe";
					commentDocForm.submit();
				}
				else{
					alert("至少选择一篇引用文档");
				}
			}	
			function openComments(){
				commentDocForm.action = "docCommentHandle.jsp?action=open&docId=<%=docId%>";
				commentDocForm.target = "docComIframe";
				commentDocForm.submit();
			}
			function closeComments(){
				commentDocForm.action = "docCommentHandle.jsp?action=close&docId=<%=docId%>";
				commentDocForm.target = "docComIframe";
				commentDocForm.submit();
			}
			function seeImpeachInfo(commentId,comment){
				//alert(commentId);
				//alert(comment);
				var re = openWin("parent_comment_impeach.jsp?commentId="+commentId+"&comment="+comment,800,700);
			}
		</script>
	</head>
	<body topmargin="2" scroll=no leftmargin="0">
		<form name="commentDocForm" method="post">
			<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="Datalisttable">
				<tr>
					<td colspan=8  height="25" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div class="DocumentOperT">操作：</div>
						<%int comswitch = ddm.getDocCommentSwitch(docId,"doc");
						  if(comswitch == 1){%>
						<a style="cursor:hand" onClick="openComments()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">开通评论</div></a>
						<%}else if(comswitch == 0){%>
						<a style="cursor:hand" onClick="closeComments()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">关闭评论</div></a>
						<%}%>
						<a style="cursor:hand" onClick="addOneComment()"><div class="DocumentOper"><img
										class="operStyle" src="../images/audity.gif">增加评论</div></a>
						<a style="cursor:hand" onClick="deleteComments()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">删除评论</div></a>
						<a style="cursor:hand" onClick="publishComments()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">发布</div></a>
						<a style="cursor:hand" onClick="withdrawPubComments()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">撤发</div></a>
					</td>
				</tr>
				<pg:listdata dataInfo="DocCommentList" keyName="DocCommentList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="12" scope="request" data="DocCommentList" isList="false">
				<tr class="cms_report_tr">
					<td width="4%" height="30" align=center>
						<input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
					</td>
					<td width="23%">文档名称</td>
					<td width="27%">评论内容</td>
					<td width="10%">用户名</td>
					<td width="10%">用户IP</td>
					<td width="5%">评论状态</td>
					<td width="5%">举报</td>
					<td width="20%">发表日期</td>
				</tr>
				<pg:param name="docId" />
				<pg:param name="docComment" />
				<pg:param name="userName" />
				<pg:param name="subTimeBgin" />
				<pg:param name="subTimeEnd" />
				<pg:param name="queryFlag" />
				<pg:param name="status" />
				<pg:param name="hasImpeach" />
				<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该文档暂时没有评论
							</td>
						</tr>
				  </pg:notify>

				<!--list标签循环输出每条记录-->
				<pg:list>
				<%
						String docTitle = dataSet.getString("docTitle");
						String oneUserName = dataSet.getString("userName");
						String userIP = dataSet.getString("userIP");
						Date subtime = dataSet.getDate("subTime");
						String oneDocComment = dataSet.getString("comment");
						int commentId = dataSet.getInt("commentId");
						int oneStatus = dataSet.getInt("status");
						int alarm = dataSet.getInt("alarm");
						int impeachFlag = dataSet.getInt("impeachFlag");
						
						String statusDsp = "";
						switch(oneStatus){
							case 0: 
								statusDsp = "未发";
								break;
							case 1:
								statusDsp = "已发";
								break;
							case 2:
								statusDsp = "撤发";
								break;
						}
				%>
				<tr onMouseOver="high(this)" onMouseOut="unhigh(this)">
					<td height="30" align=center>
						<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<%=commentId%>">
					</td>
					<td>
						<pg:cell colName="docTitle" defaultValue="" />
					</td>
					<td onClick="seeDocComment('<%=docTitle%>','<%=oneUserName%>','<%=subtime%>','<pg:cell colName="docComment"/>','<%=userIP%>','<%=commentId%>')" style="cursor:hand;<%=alarm==1?"color:red":""%>">
						<pg:cell colName="docComment" defaultValue="" maxlength="20" replace="..." />
					</td>
					<td>
						<pg:cell colName="userName" defaultValue="" />
					</td>
					<td>
						<pg:cell colName="userIP" defaultValue="" />
					</td>
					<td>
						<%=statusDsp%>
					</td>
					<td style="<%=impeachFlag==1?"color:red;":""%>"><%=impeachFlag==1?"<a style=\"cursor:hand\" onclick=\"seeImpeachInfo('" + commentId + "','" + oneDocComment + "')\">举报</a>":""%></td>
					<td>
						<pg:cell colName="subTime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
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