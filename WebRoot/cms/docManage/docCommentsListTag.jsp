<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.docCommentManager.*"%>
<%
	String docId = request.getParameter("docId");
	//docId = docId==null?"33430":docId;    //测试用
	System.out.println("docId"+docId);
	DocumentManager dm = new DocumentManagerImpl();
	String docTitle = dm.getDoc(docId).getSubtitle();
	DocCommentManager dcm = new DocCommentManagerImpl();
	int allComments = dcm.getCommentCount(Integer.parseInt(docId.trim()));
	int filteredComments = dcm.getFilteredCommentCount(Integer.parseInt(docId.trim()));
	int hotComments = dcm.getHotCommentCount(Integer.parseInt(docId.trim()));
	//System.out.println("allComments:" + allComments);
	//System.out.println("filteredComments:" + filteredComments);
	//System.out.println("hotComments:" + hotComments);
%>
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache, must-revalidate">
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<meta http-equiv="content-script-type" content="text/javascript"> 
	<meta name="keywords" content="留言板">
	<meta name="description" content="留言板">
    <title>留言板</title>
    <link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
    <script src="../inc/js/func.js"></script>
	<tab:tabConfig/>
	<script language="javascript">
	function up(commentId,commentStr){
		myForm.action = "addCommentHandleDefault.jsp?flag=up&srcCommentId=" + commentId  + "&docComment=" + commentStr + "&docId=<%=docId%>";
		myForm.submit();
	}
	function responseComment(commentId,comment){
		var re = openWin("responseComment.jsp?srcCommentId=" + commentId + "&srcComment=" + comment + "&docId=<%=docId%>",450,300);
		if(re=="cf"){
			//var str = document.location.href;
			//var end = str.indexOf("?");
			//var strArray;
			//if(end != -1)
			//	strArray= str.slice(0,end);
			//else
			//	strArray = str;
			//document.location.href = strArray+"?"+document.all.queryString.value;
			document.location.reload();
		}
	}
	function displaySpan(ob1,ob2){
		ob2.style.display="";
	}
	function undisplaySpan(ob1,ob2){
		ob2.style.display="none";
	}
	function addbookmark(){
		window.external.addFavorite(parent.document.location.href  ,'留言板_<%=docTitle%>');
	}
	function impeach(commentId,comment){
		openWin("commentImpeach.jsp?commentId=" + commentId + "&comment=" + comment,450,500);
	}
	</script>
  </head>
  
  <body scroll="auto">
	<tab:tabContainer id="doc-comment" selectedTabPaneId="foo-channel">
		<tab:tabPane id="doc-comment-filtered" tabTitle="已过滤留言">
			<table width="95%"  align="center" cellspacing="3"> 
			  <%
			  	session.removeAttribute("docCommentFiltered");
			  	session.setAttribute("docCommentFiltered","docCommentFiltered");
			  %>
			  <pg:listdata dataInfo="com.frameworkset.platform.cms.docCommentManager.DocCommentList" keyName="DocCommentList" />
			  <!--分页显示开始,分页标签初始化-->
			  <pg:pager maxPageItems="5" scope="request" data="DocCommentList" isList="false">
			  <pg:param name="docId" />
			  <pg:notify>
					<tr  class="labeltable_middle_tr_01">
						<td colspan=100 align='center' height="18px">
							暂时没有评论!
						</td>
					</tr>
			  </pg:notify>
			  <pg:param name="docId" />
			 <!--  <tr class="labeltable_middle_tr_01">
					<td align='left' height="18px" width="40%">
						本新闻共
						<pg:rowcount />
						条留言
					</td>
					<td align='ritht' height="18px">
						<pg:index />
					</td>
			  </tr>-->
			  <!--list标签循环输出每条记录-->
			  <pg:list> 
			  <%
					String userName = dataSet.getString("userName");
					String userIP = dataSet.getString("userIP"); 
					int commentId = dataSet.getInt("commentId"); 
					String comment = dataSet.getString("docComment");
					int srcCommentId = dataSet.getInt("srcCommentId");
					String displayStr = "网友";
					if(userName.length()>0){
						if(userIP.length()>0)                //系统管理员不记录IP
							displayStr =  displayStr + ":" + userName;
						else
							displayStr = "系统管理员";
					}else{
						userIP = userIP.length()>0?userIP.substring(0,userIP.lastIndexOf(".")) + ".*":"";
						displayStr = displayStr + " IP:" + userIP;
					}
			  %>
			  <tr class="cms_report_tr">
				<td  height="30" align="left"  colspan=2>
					<pg:cell colName="subTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue=""/>&nbsp;&nbsp;&nbsp;
					<%=displayStr%>
				</td>
			  </tr>
			  <tr>
				<td  colspan=2  style="word-break:break-all;word-wrap: break-word">
					<table width="100%" border="0">
					  <tr>
						<td align="left">&nbsp;&nbsp;&nbsp;&nbsp; <pg:cell colName="comment" defaultValue="" /></td>
					  </tr>
					  <%if(srcCommentId != 0){	
						  //获取原贴信息
						  DocComment  docComment = dcm.getCommentByComId(srcCommentId);
					  		String srcComment = docComment.getDocComment();
					  		String srcUserName = docComment.getUserName();
					  		String srcUserIP = docComment.getUserIP();
					  		String srcDisplayStr = "网友";
							if(srcUserName.length()>0){
								if(srcUserIP.length()>0)                //系统管理员不记录IP
									srcDisplayStr =  srcDisplayStr + ":" + srcUserName;
								else
									srcDisplayStr = "系统管理员";
							}else{
								srcUserIP = srcUserIP.length()>0?srcUserIP.substring(0,srcUserIP.lastIndexOf(".")) + ".*":"";
								srcDisplayStr = srcDisplayStr + " IP:" + srcUserIP;
							}
					  %>
					  <tr>
					  	<td align="center">
						  	<table width="100%" border="0">
						  		<tr>
						  			<td>[原贴] <%=srcDisplayStr%></td>
						  		</tr>
						  		<tr bgcolor="#FFFFF0">
						  			<td>&nbsp;&nbsp;<%=srcComment%></td>
						  		</tr>
						  	</table>
					    </td>
					  </tr>
					  <%}%>
					  <tr>
						<td align="right">
							<a style="cursor:hand" onClick="impeach('<%=commentId%>','<%=comment%>')">举报</a> 
							<span  onMouseOver="displaySpan(upSpan11<%=commentId%>,upSpan12<%=commentId%>)" onMouseOut="undisplaySpan(upSpan11<%=commentId%>,upSpan12<%=commentId%>)">
								<span id="upSpan11<%=commentId%>" name="upSpan11<%=commentId%>">
									<a style="cursor:hand" onClick="up(<%=commentId%>,'顶')">顶</a>
								</span>
								<span style="position:absolute;z-index:1;background-color:#FFF0F0;layer-background-color:#FFF0F0;display:none" id="upSpan12<%=commentId%>" name="upSpan12<%=commentId%>">
									<table>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId%>,'顶')">顶</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId%>,'不好说')">不好说</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId%>,'反对')">反对</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId%>,'不知所云')">不知所云</a></td></tr>
									</table>
								</span> 
							</span> 
							<a style="cursor:hand" onClick="responseComment('<%=commentId%>','<%=comment.length()>20?comment.substring(0,20)+"...":comment%>')">回复</a> 
							<a style="cursor:hand" onClick="addbookmark()">收藏</a>
						</td>
					  </tr>
					</table>
				</td>
			  </tr>
			  </pg:list>
			  <tr class="labeltable_middle_tr_01">
					<td align='left' height="18px" width="40%">
						本新闻共
						<pg:rowcount />
						条留言
					</td>
					<td align='ritht' height="18px">
						<pg:index />
					</td>
			  </tr>
			  <input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
			  </pg:pager>
			  <tr><td colspan="2" height="10"></td></tr>
		  </table>
		</tab:tabPane>
		<tab:tabPane id="doc-comment-all" tabTitle="全部留言">
			<table width="95%"  align="center" cellspacing="3"> 
			  <%
			  	session.removeAttribute("docCommentPublished"); 
			  	session.setAttribute("docCommentPublished","docCommentPublished");
			  %>
			  <pg:listdata dataInfo="com.frameworkset.platform.cms.docCommentManager.DocCommentList" keyName="DocCommentList" />
			  <!--分页显示开始,分页标签初始化-->
			  <pg:pager maxPageItems="5" scope="request" data="DocCommentList" isList="false">
			  <pg:param name="docId" />
			  <pg:notify>
					<tr  class="labeltable_middle_tr_01">
						<td colspan=100 align='center' height="18px">
							暂时没有评论!
						</td>
					</tr>
			  </pg:notify>
			  <pg:param name="docId" />
			  <!-- <tr class="labeltable_middle_tr_01">
					<td align='left' height="18px" width="40%">
						本新闻共
						<pg:rowcount />
						条留言
					</td>
					<td align='ritht' height="18px">
						<pg:index />
					</td>
			  </tr>-->
			  <!--list标签循环输出每条记录-->
			  <pg:list> 
			  <%
					String userName1 = dataSet.getString("userName");
					String userIP1 = dataSet.getString("userIP"); 
					int commentId1 = dataSet.getInt("commentId"); 
					String comment1 = dataSet.getString("docComment");
					int srcCommentId1 = dataSet.getInt("srcCommentId");
					String displayStr1 = "网友";
					if(userName1.length()>0){
						if(userIP1.length()>0)                //系统管理员不记录IP
							displayStr1 =  displayStr1 + ":" + userName1;
						else
							displayStr1 = "系统管理员";
					}else{
						userIP1 = userIP1.length()>0?userIP1.substring(0,userIP1.lastIndexOf(".")) + ".*":"";
						displayStr1 = displayStr1 + " IP:" + userIP1;
					}
			  %>
			  <tr class="cms_report_tr">
				<td  height="30" align="left"  colspan=2>
					<pg:cell colName="subTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue=""/>&nbsp;&nbsp;&nbsp;
					<%=displayStr1%>
				</td>
			  </tr>
			  <tr>
				<td  colspan=2  style="word-break:break-all;word-wrap: break-word">
					<table width="100%" border="0">
					  <tr>
						<td align="left">&nbsp;&nbsp;&nbsp;&nbsp; <pg:cell colName="comment" defaultValue="" /></td>
					  </tr>
					  <%if(srcCommentId1 != 0){					//获取原贴信息
						  DocComment  docComment = dcm.getCommentByComId(srcCommentId1);
					  		String srcComment = docComment.getDocComment();
					  		String srcUserName = docComment.getUserName();
					  		String srcUserIP = docComment.getUserIP();
					  		String srcDisplayStr = "网友";
							if(srcUserName.length()>0){
								if(srcUserIP.length()>0)                //系统管理员不记录IP
									srcDisplayStr =  srcDisplayStr + ":" + srcUserName;
								else
									srcDisplayStr = "系统管理员";
							}else{
								srcUserIP = srcUserIP.length()>0?srcUserIP.substring(0,srcUserIP.lastIndexOf(".")) + ".*":"";
								srcDisplayStr = srcDisplayStr + " IP:" + srcUserIP;
							}
					  %>
					  <tr>
					  	<td align="center">
						  	<table width="100%" border="0">
						  		<tr>
						  			<td>[原贴] <%=srcDisplayStr%></td>
						  		</tr>
						  		<tr bgcolor="#FFFFF0">
						  			<td>&nbsp;&nbsp;<%=srcComment%></td>
						  		</tr>
						  	</table>
					    </td>
					  </tr>
					  <%}%>
					  <tr>
						<td align="right">
							<a style="cursor:hand" onClick="impeach('<%=commentId1%>','<%=comment1%>')">举报</a>  
							<span  onMouseOver="displaySpan(upSpan21<%=commentId1%>,upSpan22<%=commentId1%>)" onMouseOut="undisplaySpan(upSpan21<%=commentId1%>,upSpan22<%=commentId1%>)">
								<span id="upSpan21<%=commentId1%>" name="upSpan21<%=commentId1%>">
									<a style="cursor:hand" onClick="up(<%=commentId1%>,'顶')">顶</a>
								</span>
								<span style="position:absolute;z-index:1;background-color:#FFF0F0;layer-background-color:#FFF0F0;display:none" id="upSpan22<%=commentId1%>" name="upSpan2<%=commentId1%>">
									<table>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId1%>,'顶')">顶</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId1%>,'不好说')">不好说</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId1%>,'反对')">反对</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId1%>,'不知所云')">不知所云</a></td></tr>
									</table>
								</span> 
							</span> 
							<a style="cursor:hand" onClick="responseComment('<%=commentId1%>','<%=comment1.length()>20?comment1.substring(0,20)+"...":comment1%>')">回复</a> 
							<a style="cursor:hand" onClick="addbookmark()">收藏</a>
						</td>
					  </tr>
					</table>
				</td>		
			  </tr>
			  </pg:list>
			  <tr class="labeltable_middle_tr_01">
					<td align='left' height="18px" width="40%">
						本新闻共
						<pg:rowcount />
						条留言
					</td>
					<td align='ritht' height="18px">
						<pg:index />
					</td>
			  </tr>
			  <input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
			  </pg:pager>
			  <tr><td colspan="2" height="10"></td></tr>
		  </table>
		</tab:tabPane>
		<tab:tabPane id="doc-comment-hot" tabTitle="热门留言">
			<table width="95%"  align="center" cellspacing="3"> 
			  <%
			  	session.removeAttribute("docCommentHot");
			  	session.setAttribute("docCommentHot","docCommentHot");
			  %>
			  <pg:listdata dataInfo="com.frameworkset.platform.cms.docCommentManager.DocCommentList" keyName="DocCommentList" />
			  <!--分页显示开始,分页标签初始化-->
			  <pg:pager maxPageItems="5" scope="request" data="DocCommentList" isList="false">
			  <pg:param name="docId" />
			  <pg:notify>
					<tr  class="labeltable_middle_tr_01">
						<td colspan=100 align='center' height="18px">
							暂时没有评论!
						</td>
					</tr>
			  </pg:notify>
			  <pg:param name="docId" />
			  <!-- <tr class="labeltable_middle_tr_01">
					<td align='left' height="18px" width="40%">
						本新闻共
						<pg:rowcount />
						条留言
					</td>
					<td align='ritht' height="18px">
						<pg:index />
					</td>
			  </tr>-->
			  <!--list标签循环输出每条记录-->
			  <pg:list> 
			  <%
					String userName2 = dataSet.getString("userName");
					String userIP2 = dataSet.getString("userIP"); 
					int commentId2= dataSet.getInt("commentId"); 
					String comment2 = dataSet.getString("comment");
					String displayStr2 = "网友";
					if(userName2.length()>0){
						if(userIP2.length()>0)                //系统管理员不记录IP
							displayStr2 =  displayStr2 + ":" + userName2;
						else
							displayStr2 = "系统管理员";
					}else{
						userIP2 = userIP2.length()>0?userIP2.substring(0,userIP2.lastIndexOf(".")) + ".*":"";
						displayStr2 = displayStr2 + " IP:" + userIP2;
					}
					int[] responseInfo = dcm.getResponseInfo(commentId2);
			  %>
			  <tr class="cms_report_tr">
				<td  height="30" align="left"  colspan=2>
					<pg:cell colName="subTime" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue=""/>&nbsp;&nbsp;&nbsp;
					<%=displayStr2%>
				</td>
			  </tr>
			  <tr>
			  	<td  colspan=2>
			  		<font color="#0033CC">&nbsp;&nbsp;[顶]</font><%=responseInfo[0]%><font color="#0033CC">人</font>&nbsp;<font color="#0033CC">[不好说]</font><%=responseInfo[1]%><font color="#0033CC">人</font>&nbsp;<font color="#0033CC">[反对]</font><%=responseInfo[2]%><font color="#0033CC">人</font>&nbsp;<font color="#0033CC">[不知所云]</font><%=responseInfo[3]%><font color="#0033CC">人</font>&nbsp;<font color="#0033CC">[回复]</font><%=responseInfo[4]%><font color="#0033CC">人</font>
			  	</td>
			  </tr>
			  <tr>
				<td  colspan=2  style="word-break:break-all;word-wrap: break-word">
					<table width="100%" border="0">
					  <tr>
						<td align="left">&nbsp;&nbsp;&nbsp;&nbsp; <pg:cell colName="comment" defaultValue="" /></td>
					  </tr>
					  <tr>
						<td align="right">
							<a style="cursor:hand" onClick="impeach('<%=commentId2%>','<%=comment2%>')">举报</a>  
							<span  onMouseOver="displaySpan(upSpan31<%=commentId2%>,upSpan32<%=commentId2%>)" onMouseOut="undisplaySpan(upSpan31<%=commentId2%>,upSpan32<%=commentId2%>)">
								<span id="upSpan31<%=commentId2%>" name="upSpan31<%=commentId2%>">
									<a style="cursor:hand" onClick="up(<%=commentId2%>,'顶')">顶</a>
								</span>
								<span style="position:absolute;z-index:1;background-color:#FFF0F0;layer-background-color:#FFF0F0;display:none" id="upSpan32<%=commentId2%>" name="upSpan32<%=commentId2%>">
									<table>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId2%>,'顶')">顶</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId2%>,'不好说')">不好说</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId2%>,'反对')">反对</a></td></tr>
										<tr><td><a style="cursor:hand" onClick="up(<%=commentId2%>,'不知所云')">不知所云</a></td></tr>
									</table>
								</span> 
							</span> 
							<a style="cursor:hand" onClick="responseComment('<%=commentId2%>','<%=comment2.length()>20?comment2.substring(0,20)+"...":comment2%>')">回复</a> 
							<a style="cursor:hand" onClick="addbookmark()">收藏</a>
						</td>
					  </tr>
					</table>
				</td>
			  </tr>
			  </pg:list>
			  <tr class="labeltable_middle_tr_01">
					<td align='left' height="18px" width="40%">
						本新闻共
						<pg:rowcount />
						条留言
					</td>
					<td align='ritht' height="18px">
						<pg:index />
					</td>
			  </tr>
			  <input id="queryString" name="queryString" value="<pg:querystring/>" type="hidden">
			  </pg:pager>
			  <tr><td colspan="2" height="10"></td></tr>
		  </table>
		</tab:tabPane>
	</tab:tabContainer>

	<form method="post" target="operIframe" name="myForm">
	</form>
  </body>
  <div style="display:none"><iframe name="operIframe"></iframe></div> 
</html>