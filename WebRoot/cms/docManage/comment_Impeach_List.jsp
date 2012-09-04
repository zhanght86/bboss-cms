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
	
	String commentId = request.getParameter("commentId");
	String comment = request.getParameter("comment");	
	
	DocCommentManager ddm = new DocCommentManagerImpl();
	String action = request.getParameter("action");
	
	try{
		if("delete".equals(action)){
			String[] impeachIds = request.getParameterValues("ID");
			ddm.deleteImpeachInfos(impeachIds);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
%>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>举报信息列表</title>
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
			function deleteImpeachs(){
				if(haveSelect("ID")){
					commentDocForm.action = "comment_impeach_list.jsp?commentId=<%=commentId%>&comment=<%=comment%>&action=delete";
					commentDocForm.submit();
				}
				else{
					alert("至少选择一篇引用文档");
				}
			}	
			function seeImpeachInfo(commentId){
				//alert(commentId);
				var re = openWin("impeachInfoList.jsp?commentId="+commentId,400,550);
			}
			function sendEmail(email,impeacher,impeachId,reasonDsp){
				var re = openWin("email_impeach_reply.jsp?email=" + email + "&impeachId="+impeachId+"&toName="+impeacher+"&commentId=<%=commentId%>"+"&reasonDsp="+reasonDsp ,400,550);
				if(re = "cf"){
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
		</script>
	</head>
	<base target="_self">
	<body topmargin="2" scroll=no leftmargin="0">
		<form name="commentDocForm" method="post" >
			<table width="99%" border="0" align="center" cellpadding="3" cellspacing="0" class="Datalisttable">
				<tr>
					<td colspan=8  height="25" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7"><div class="DocumentOperT">操作：</div>
						<a style="cursor:hand" onClick="deleteImpeachs()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">删除</div></a>
						<a style="cursor:hand" onClick="window.close()"><div class="DocumentOper"><img 
										class="operStyle" src="../../sysmanager/images/plan.gif">返回</div></a>
					</td>
				</tr>
				<pg:listdata dataInfo="CommentImpeachList" keyName="CommentImpeachList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="12" scope="request" data="CommentImpeachList" isList="false">
				<tr class="cms_report_tr">
					<td width="4%" height="30" align=center>
						<input   class="checkbox" type="checkBox" hideFocus=true name="checkBoxAll" onClick="checkAll('checkBoxAll','ID')">
					</td>
					<td width="20%">评论内容</td>
					<td width="6%">举报人</td>
					<td width="13%">邮箱</td>
					<td width="5%">原因</td>
					<td width="26%">描述</td>
					<td width="5%">回复</td>
					<td width="20%">举报日期</td>
				</tr>
				<pg:param name="commentId" />
				<pg:param name="comment" />
				<pg:notify>
						<tr  class="labeltable_middle_tr_01">
							<td colspan=100 align='center' height="18px">
								该评论暂时没有举报信息
							</td>
						</tr>
				  </pg:notify>

				<!--list标签循环输出每条记录-->
				<pg:list>
				<%
						int id = dataSet.getInt("id");					
						String impeacher = dataSet.getString("impeacher");
						String email = dataSet.getString("email");
						String description = dataSet.getString("description");
						int reason = dataSet.getInt("reason");
						int repled = dataSet.getInt("repled");
						
						String reasonDsp = "";
						switch(reason){
							case 0: 
								reasonDsp = "反动";
								break;
							case 1:
								reasonDsp = "色情";
								break;
							case 2:
								reasonDsp = "暴力";
								break;
							case 3:
								reasonDsp = "侵权";
								break;
							case 4:
								reasonDsp = "其他";
								break;
						}
						String repledStr ="未回";
						if(repled == 1)
							repledStr = "已回";
				%>
				<tr onMouseOver="high(this)" onMouseOut="unhigh(this)">
					<td height="30" align=center>
						<input   class="checkbox" hideFocus onClick="checkOne('checkBoxAll','ID')" type="checkbox" name="ID" value="<%=id%>">
					</td>
					<td style="word-break:break-all;word-wrap:break-word;" align="left">
						<%=comment.length()>200?comment.substring(0,200)+"...":comment%>
					</td>
					<td align="left" style="word-break:break-all;word-wrap:break-word;">
						<%=impeacher%> 
					</td>
					<td align="left" style="word-break:break-all;word-wrap:break-word;">
						<a onclick="sendEmail('<%=email%>','<%=impeacher%>','<%=id%>','<%=reasonDsp%>')" style="cursor:hand"><%=email%></a>
					</td>
					<td align="left" style="word-break:break-all;word-wrap:break-word;">
						<%=reasonDsp%>
					</td>
					<td style="word-break:break-all;word-wrap:break-word;" align="left">
						<%=description%>
					</td>
					<td style="word-break:break-all;word-wrap:break-word;" align="left">
						<%=repledStr%>
					</td>
					<td align="left">
						<pg:cell colName="comtime" dateformat="yyyy/MM/dd HH:mm:ss" defaultValue="" />
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