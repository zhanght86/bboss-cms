<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	AccessControl accesscontroler = AccessControl.getInstance();
    accesscontroler.checkAccess(request, response);
    String userid = accesscontroler.getUserID();
    
	String docid = request.getParameter("docid");
	String channelname = "";//request.getParameter("channelName");
	String siteid = "";//request.getParameter("siteid");
	String channelId = "";//request.getParameter("channelId");
	
	DocumentManager dmi = new DocumentManagerImpl();
	ChannelManager cm = new ChannelManagerImpl();
	SiteManager siteManager = new SiteManagerImpl();

	Document document = dmi.getDoc(docid);
	channelId = String.valueOf(document.getChanel_id());
	Channel channel = cm.getChannelInfo(channelId);
	channelname = channel.getName();
	siteid = String.valueOf(channel.getSiteId());
	
	//在返工文档编辑时传来rebound，在审核时传来audit，其他出来为空(返工文档是有任务id的)
   	String flag =  request.getParameter("flag");
   	int taskid = 0;
   	String taskidStr="";
   	if(flag!=null && flag.equals("rebound")){
   		taskid = dmi.hasTask(Integer.parseInt(userid),Integer.parseInt(docid),2);
   	}
   	else if(flag!=null && flag.equals("audit")){
   		taskid = dmi.hasTask(Integer.parseInt(userid),Integer.parseInt(docid),1);
   		taskidStr = request.getParameter("taskidStr");
   	}
	
	String status = String.valueOf(document.getStatus());
	
	String sitename = siteManager.getSiteInfo(siteid).getName();
	String docsource = String.valueOf(document.getDocsource_id());
	String tid = String.valueOf(document.getDetailtemplate_id());
	String doc_level_id = String.valueOf(document.getDoc_level());
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<link href="../inc/css/cms.css" rel="stylesheet" type="text/css">
<script src="../inc/js/func.js"></script>
<style type="text/css">
<!--
	.STYLE7 {color:#003398; font-size: 12px;}
	#Layer1 {
		position:absolute;
		left:0px;
		top:0px;
		width:113px;
		height:127px;
		z-index:1;
	}
	.STYLE9 {color: #FF6600}

-->
</style>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CMS 文档管理</title>
</head>
<script language="javascript">
		
	//返回
	function back() {
		window.close();
	}
	//选择图片新闻的概览图片
	var fileFlag;
	function chooseImage(){
		fileFlag = "pic";
		openWin("../channelManage/chooseImageFrameset.jsp",520,580);
	}
	function setImage(uri){
		if(fileFlag == "pic")
			document.all("picpath").value  = uri;
		if(fileFlag == "media")
			document.all("mediapath").value  = uri;
	}
	//选择多媒体文件
	function choosemedia()
	{
		fileFlag = "media";
		openWin("../channelManage/chooseImageFrameset.jsp?fileFlag=media",520,580);
	}
	//焦点放在第一个text
	function init(){
		myform.title.focus();
	}
	//光标是停在文本框文字的最后
	function focusend() 
	{ 
		var e = event.srcElement; 
		var r =e.createTextRange(); 
		r.moveStart("character",e.value.length); 
		r.collapse(true); 
		r.select(); 
	}
	function publishPreview(){
		myform.method="post";
		myform.action="previewPubDocHandle.jsp?docId=<%=docid%>";
		myform.submit();
	}
	function seeAdvice(channelId,docid,taskid){
		openWin("<%=rootpath%>/cms/docManage/see_audit_advice.jsp?channelId=<%=channelId%>&docid=<%=docid%>&taskid=<%=taskid%>" ,400,550);
	}
	function delive(){
		saveform("0");
		//flag为1表示新稿文档的称颂,为2表示返工文档的呈送
		openWin("<%=rootpath%>/cms/docManage/doc_AuditorList.jsp?flag=2&channelId=<%=channelId%>&docid=<%=docid%>&taskid=<%=taskid%>");
		var str = window.dialogArguments.location.href;
		var end = str.indexOf("?");
		var strArray;
		if(end != -1)
			strArray= str.slice(0,end);
		else
			strArray = str;
		window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
		window.close();
	}
	function agree(){
		var re;
		//参数auditFlag为审核意见，0表不同意，1表同意
		re = openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=<%=taskidStr%>&auditFlag=1",400,550);
		if(re == "cf"){ 
			var str = window.dialogArguments.location.href;
			var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					strArray= str.slice(0,end);
				else
					strArray = str;
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();
		}
	}
	function disagree(){
		var re;
		var addComment = window.confirm("要增加审核意见吗？");
		if(addComment==false){
			re = "cf";
			myform.method="post";
			//参数auditFlag为审核意见，0表不同意，1表同意
			myform.action = "<%=rootpath%>/cms/docManage/auditDocHandle.jsp?idStr=<%=taskidStr%>&auditFlag=0";
			myform.submit();
		}
		else{
			re = openWin("<%=rootpath%>/cms/docManage/audit_add_comment.jsp?idStr=<%=taskidStr%>&auditFlag=0",400,550);
		}
		if(re == "cf"){
			var str = window.dialogArguments.location.href;
			var end = str.indexOf("?");
				var strArray;
				if(end != -1)
					strArray= str.slice(0,end);
				else
					strArray = str;
			window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
			window.close();	
		}
	}
	function changSelect(o)
	{
	   document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
	   document.all.inputdocsource.focus();
	   document.all.inputdocsource.select();
	}	
</script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"  bgcolor="#F3F4F9"  onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onLoad="init()">
<script language="javascript">
	document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
</script>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form target="updatelinks" action="update_document.jsp" method="post"  name="myform">
	<input name="doctype" value="1" type=hidden>
   	<input name="docid" value="<%=docid%>" type=hidden>
	<input name="siteid" value="<%=siteid%>" type=hidden>
  	<input name="channelName" value="<%=channelname%>" type=hidden>
  	<input name="channelId" value="<%=channelId%>" type=hidden>
  	<input name="isnewdocsource" value="0" type="hidden"><!--是否新稿源-->
	<input type="hidden" name="parentDetailTpl" value="1"/>
<table width="99%" border="0" align="center" cellpadding="0" cellspacing="0">
			            		<tr>
					            	<td height="47" valign="top" background="../images/top_tile_short.jpg">
									<table width="100%" border="0" cellpadding="0" cellspacing="0">
					            			<tr>
					                  			<td width="200"><img src="../images/logo.jpg" width="200" height="50" /></td>
					                		    <td>内容编辑 [<%=sitename%> 站点  :  <%=channelname%> 频道]</td>
					            			    <td width="53"><img src="../images/Gather_top_LeftDot.jpg" width="174" height="50"></td>
					            			</tr>
				              		  </table>
</td>
					            </tr>
			            		<tr>
					            	<td height="18">
					            		<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
					                		<tr background="../images/Gather_Content_topbg.gif">
												<td width="4%" align="right" background="../images/Gather_Content_topbg.gif">
					                  				<img src="../images/ico_point.gif" width="25" height="25">					                  			</td>
				                  			  <td colspan="3" background="../images/Gather_Content_topbg.gif" class="cms_title_blue">
											  <table width="100%" border="0" cellpadding="0" cellspacing="0" background="../images/Gather_Content_topbg.gif">
														<tr>
															<td width="100" height="35" class="cms_title_blue">
															文档信息															</td>
															<td></td>
														</tr>
												</table>											  </td>
					                		</tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
							                  	<td height="23" colspan="3">
							                  		<table border="0" cellpadding="0" cellspacing="0">
									                    <tr >
									                    	<td><strong>文档类型:</strong></td>
									                      	<td>
										                      	<div align="center">
										                      		<img src="../images/doc_link.gif" width="20" height="20">										                      	</div>									                      	</td>
									                      	<td>外部链接</td>
									                    </tr>
						                  		</table>  </td>
											</tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
												<td width="12%" align="right"><strong>链接标题:</strong></td>
							                  	<td width="43%">
							                  		<span class="cms_title_blue">
								                    	<label>
								                    		<input name="title" type="text" class="cms_text" size="60" value="<%=document.getTitle()%>" readonly>
								                    	</label>
							                  		</span>
							                  		<span class="STYLE7">
							                  			<label>
							                  				<span class="red_star">*</span>							                  			</label>
							                  		</span>												</td>
												<td width="40%" height="23">
							                  		<strong>标题颜色:</strong>
							                  		<span>
							                  			<input name="titlecolor" value="<%=document.getTitlecolor()%>" readonly type="text" class="cms_text" style="color:<%=document.getTitlecolor()%>" size="7" maxlength="50">
									  		  </span>												</td>
							                </tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
												<td align="right"><strong>显示标题:</strong></td>
							                  	<td height="23">
							                  		<input name="subtitle" type="text" class="cms_text" size="60" value="<%=document.getSubtitle()%>" readonly>
							                    	<span class="STYLE7">
														<label>
															<span class="red_star">*</span>														</label>
													</span>												</td>
												<td height="23">
												<strong>文档来源:</strong>
													<span class="cms_title_blue">
													<input type="text" style="width:100px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource" readonly><span style="width:18px;border:0px solid red;">
													<select name="docsource_id" style="margin-left:-100px;width:118px;" onChange="changSelect(this)" id="docsource_id" disabled>
															<%
															  List dslist=null;
															  DocumentManagerImpl dmimpl=new DocumentManagerImpl();
															  dslist=dmimpl.getDocSourceList();
															  request.setAttribute("dslist",dslist);
															%>
															<pg:list requestKey="dslist">
																<option value="<pg:cell colName="docsource_id"/>"  
																	<pg:equal colName="docsource_id" value="<%=docsource%>">selected</pg:equal>>
																	<pg:cell colName="srcname" defaultValue="" />
																</option>
															</pg:list>
														</select>
														</span>
													</span>												</td>
							                </tr>
					                		<tr>
							                	<td height="26">&nbsp;</td>
												<td align="right"><strong>关 键 词:</strong></td>
							                  	<td height="23">
							                  		<span class="black">
							                  			<span class="cms_title_blue"> 
							                  				<input name="keywords" type="text" class="cms_text" size="60" value="<%=document.getKeywords()%>" readonly>
							                  			</span>							                  		</span>												</td>
												<td height="23">
											  		<label><strong>编稿时间:</strong>										  			<span class="black">
										  				<span class="cms_title_blue">
															<input name="docwtime" type="text" class="cms_text" value="<%=document.getDocwtime()%>" size="20" readonly="true">
															<!--<input type="button" class="input" value="时间" onClick="showdatetime(document.all('docwtime'))">-->
														</span>														</span>													</label>							                  	</td>
							                </tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
												<td align="right"><strong>作&nbsp;&nbsp;&nbsp;&nbsp;者:</strong></td>
							                  	<td height="23">
												  <span class="cms_title_blue">
														<input name="author" type="text" class="cms_text" size="22" value="<%=document.getAuthor()%>" readonly>
												  </span>
													&nbsp;&nbsp;&nbsp;&nbsp;<strong>级别:</strong>
													<span class="cms_title_blue">
														<select name="doc_level" class="cms_select" disabled>
															<%
															  List doclevellist = null;
															  DocumentManager impl = new DocumentManagerImpl();
															  doclevellist = impl.getDocLevelList();
															  request.setAttribute("doclevellist",doclevellist);
															%>
															<pg:list requestKey="doclevellist">
																<option value="<pg:cell colName="id"/>"<pg:equal colName="id" value="<%=doc_level_id%>">selected</pg:equal>>
																<pg:cell colName="name"/> 
															</pg:list>
														</select>&nbsp;										</span>							                  </td>
												<td>
												<strong>主题图片:</strong>
													<input type="text" name="picpath" value="<%=document.getPicPath()%>" class="cms_text" readonly size="30"/>
													<!--<input type="button" name="addpic" onClick="chooseImage()" value="选择图片"/>-->
												</td>
							                </tr>
											<tr>
												<td height="26">&nbsp;								                	</td>
												<td align="right"><strong>摘要内容:</strong></td>
												<td >
													<textarea name="docabstract" cols="60" rows="5" class="cms_textarea" readonly><%=document.getDocabstract()%></textarea>			
												</td>
											</tr>
					                		<tr>
							                	<td height="26">&nbsp;</td>
												<td align="right"><strong>链接 URL:</strong></td>
							                  	<td height="23">
							                  		<input name="content" type="text" class="cms_text" size="60" value="<%=document.getContent()%>" readonly>
							                    	<span class="red_star">*</span>												</td>
												<td>
													<strong>打开目标:</strong>						                    	  <span class="cms_title_blue">
									                    <select name="linktarget" class="cms_select" disabled>
										                    <option value="_blank" <%if(document.getLinktarget().equals("_blank")){out.print("selected");}%>>_blank</option>
										                    <option value="_self"  <%if(document.getLinktarget().equals("_self")){out.print("selected");}%>>_self</option>
										                    <option value="_top"   <%if(document.getLinktarget().equals("_top")){out.print("selected");}%>>_top</option>
										                    <option value="_parent" <%if(document.getLinktarget().equals("_parent")){out.print("selected");}%>>_parent</option>
									                    </select>
							                    	</span>							                    </td>
							                </tr>
				              		</table>					              	</td>
					            </tr>
			            		<tr>
			              			<td  width="766" height="33"  align="center" valign="middle">
			              	 			<input type="button" value="返回" class="cms_button" onClick="back()">			                   		</td>
			              		</tr>
			            		<!--来源于别的网站才有父站点-->
			          		</table>
			          		<script type="text/javascript">
  document.all.inputdocsource.value=document.all.docsource_id.options(document.all.docsource_id.selectedIndex).text;
  </script>  
</form>
<div style="display:none">
	<iframe name="updatelinks" width="0" height="0"></iframe>
</div>
</body>
</html>