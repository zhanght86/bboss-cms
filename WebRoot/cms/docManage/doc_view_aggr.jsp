<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.cms.sitemanager.*,com.frameworkset.platform.cms.templatemanager.*
,com.frameworkset.platform.cms.container.Template,com.frameworkset.platform.cms.documentmanager.*"%>
<%@page import="com.frameworkset.platform.security.AccessControl,java.util.List"%>
<%@page import="com.frameworkset.platform.cms.channelmanager.*"%>
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
	//获取所选频道的细缆模板ID
	
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

	//get文档的相关文档的信息
	String partId = "";
	String partName = "";
	String partTitle = "";
	String partChlName = "";//频道名称
	String[] doclists =  dmi.getAggrDocList2Str(docid);
	for(int i=0;i<doclists.length;i++)
	{
		String[] tem = doclists[i].split("№");
		partId += tem[0] + "№";
		partName += tem[1] + "№";
		partTitle += tem[2] + "№";
		partChlName += tem[3] + "№";
	}
%>
<%!/**
   * 针对HTML的特殊字符转义
   * @param value String
   * @return String
   */
  String filterStr(String value) {
    if (value == null) {
      return (null);
    }
    char content[] = new char[value.length()];
    value.getChars(0, value.length(), content, 0);
    StringBuffer result = new StringBuffer(content.length + 50);
    for (int i = 0; i < content.length; i++) {
      switch (content[i]) {
        case '<':
          result.append("&lt;");
          break;
        case '>':
          result.append("&gt;");
          break;
        case '&':
          result.append("&amp;");
          break;
        case '"':
          result.append("&quot;");
          break;
        case '\'':
          result.append("&#39;");
          break;
        case '\\':
          result.append("\\\\");
          break;
        default:
          result.append(content[i]);
      }
    }
	return (result.toString());
  }
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
	//初始化相关文档table表的数据
	function initRelatedDoc()
	{
		docxz.document.all("buttons").parentElement.style.display = "none";
		var part_id = "<%=partId%>";
		var part_name = "<%=filterStr(partName)%>";
		var part_title = "<%=filterStr(partTitle)%>";
		var part_chlName = "<%=partChlName%>";
		
		if(part_id!=""&&part_name!="")
		{
			var ids = new Array();
			var names = new Array();
			var titles = new Array();
			var chlNames = new Array();
			ids = part_id.split("№");
			names = part_name.split("№");
			titles = part_title.split("№");
			chlNames = part_chlName.split("№");
			var tableatt = docxz.document.all("chndoclist");
			for(var i=0;i<ids.length-1;i++)
			{
				var row1 = tableatt.insertRow(i+1);
				var td1 = docxz.document.createElement("td");
				var td2 = docxz.document.createElement("td");
				var td3 = docxz.document.createElement("td");
				var td4 = docxz.document.createElement("td");
				var td5 = docxz.document.createElement("td");

				td1.innerHTML="<input type='checkBox' name='id' value="+(i+1)+" hideFocus=true onclick='checkOne(id)'/>";		 
				var docid = "<input type='hidden' name='docid' value='"+ids[i]+"' />";
				//var channelId = "<input type='hidden' name='channelId' value='"+obj[1]+"' />";
				td2.innerHTML = docid + names[i];
				td3.innerHTML = chlNames[i];
				td4.innerHTML = "<input type='text' name=aggrtitle value="+titles[i]+" />";
				var up = "<a href='#' onclick='upRow(this)'><u>上移</u></a>&nbsp;";
				var down = "<a href='#' onclick='downRow(this)'><u>下移</u></a>";
				td5.innerHTML = "&nbsp;";
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
				row1.appendChild(td4);
				row1.appendChild(td5);
			}
		}
	}
	//初始化相关table
	function document.onreadystatechange()
	{
		if (document.readyState!="complete") 
			return;
		initRelatedDoc();
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
	//响应rodio事件
	function changtpl(type)
	{
		if(type==0)
			document.all.detailtemplate_id.disabled = false;
		else
			document.all.detailtemplate_id.disabled = true;
	}
	//预览模板
	function preview()
	{
		var tplId = document.all.detailtemplate_id.value;
		window.open("../templateManage/template_previewbyid.jsp?tplId=" + tplId + "&siteId=<%=siteid%>");
	}	
</script>
<script type="text/javascript" src="../../public/datetime/calender.js" language="javascript"></script>
<!--下面的代码 使得按 F5会刷新modal页面-->
<base target="_self">
<body  bottommargin="0"   bgcolor="#F3F4F9"  onkeydown="if(event.keyCode==116){reload.click()}" rightmargin="0" leftmargin="0" topmargin="0" onload="init()">
<script language="javascript">
	document.write("<a id='reload' href='" + document.location.href + "' style='display:none'>reload...</a>");
</script>
<!--上面的代码 使得按 F5会刷新modal页面-->
<form target="updatelinks" action="update_document.jsp" method="post"  name="myform">
	<input name="doctype" value="3" type=hidden><!--聚合文档-->
   	<input name="docid" value="<%=docid%>" type=hidden>
	<input name="siteid" value="<%=siteid%>" type=hidden>
  	<input name="channelName" value="<%=channelname%>" type=hidden>
  	<input name="channelId" value="<%=channelId%>" type=hidden>
	<input type="hidden" name="titlecolor" value="<%=document.getTitlecolor()%>" /><!--标题颜色-->
	<input name="subtitle" type="hidden" value="<%=document.getSubtitle()%>"/><!--显示标题-->
	<input name="author" value="不详" type="hidden"/><!--作者-->
	<input name="isnewdocsource" value="0" type="hidden"><!--是否新稿源-->
	<!--input name="detailtemplate_id" type="hidden"/><!--发布模板-->
	<input name="content" type="hidden"/><!--content-->
	<!--  <input type="hidden" name="parentDetailTpl" value="1"/>-->
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
			            		<tr>
					            	<td height="47" colspan="3" valign="top" background="../images/top_tile_short.jpg">
					            		<table width="100%" border="0" cellpadding="0" cellspacing="0">
					            			<tr>
					                  			<td width="200"><img src="../images/logo.jpg" width="200" height="50" /></td>
					                		    <td>内容编辑 [<%=sitename%> 站点  :  <%=channelname%> 频道]</td>
					            			    <td width="53"><img src="../images/Gather_top_LeftDot.jpg" width="174" height="50" /></td>
					            			</tr>
					              		</table>				            	  </td>
					            </tr>
			            		<tr>
					            	<td height="18" colspan="3">
					            		<table width="100%" border="0" cellpadding="0" cellspacing="0">
					                		<tr background="../images/Gather_Content_topbg.gif">
					                  			<td width="4%" height="35" align="right" background="../images/Gather_Content_topbg.gif"><img src="../images/ico_point.gif" width="25" height="25"></td>
					                  			<td class="cms_title_blue">
					                  				<table width="100%" border="0" cellpadding="0" cellspacing="0" background="../images/Gather_Content_topbg.gif">
					                      				<tr>
					                        				<td width="100" height="35" class="cms_title_blue">文档信息填写</td>
					                        				<td><div style="text-align:right">(<span class="red_star">*</span> 
						                        				为必填项
						                        				<img src="../images/tan.gif" width="11" height="11">)</div></td>
					                      				</tr>
				                    			  </table>				                    		  </td>
				                		  </tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
							                  	<td>
							                  		<table border="0" cellpadding="0" cellspacing="0">
									                    <tr>
									                    	<td align="right"><strong>文档类型:</strong></td>
									                      	<td>
										                      	<div align="center">
								                      		<img src="../images/doc_aggr.gif" width="20" height="20">										                      	</div>									                      	</td>
									                      	<td>聚合文档</td>
								                      </tr>
						                  		  </table>							                    </td>
							                </tr>
							                <tr>
							                	<td height="26">&nbsp;</td>
							                  	<td height="23"><strong>链接标题:
							                  		<span class="cms_title_blue">
								                    	<label>						                    		</label>
							                  		</span></strong><span class="cms_title_blue"><label>
							                  		<input name="title" type="text" class="cms_text" size="60" value="<%=document.getTitle()%>" onfocus="focusend()">
								                    	</label>
							                  		</span>
						                  		  <span class="STYLE7">
							                  			<label>
							                  				<span class="red_star">*</span>							                  			</label>
						                  		  </span>
							                  		&nbsp;&nbsp;<span><strong>文档来源:</strong>
														<span>
						                  					<span class="black">
						                  						<span class="STYLE7">
						                  							<span class="red_star">
						                  								<span class="cms_title_blue">
						                  								<input type="text" style="width:100px;height:21px;font-size:10pt;" id="inputdocsource" name="inputdocsource" readonly><span style="width:18px;border:0px solid red;">
						                  								<select name="docsource_id"  style="margin-left:-100px;width:118px;" onChange="changSelect(this)" id="docsource_id" disabled>
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
						                  								</span>						                  							</span>						                  						</span>						                  					</span>						                  				</span>
						                  			</span>											  </td>
							                </tr>
							               
					                		<tr>
							                	<td height="26">&nbsp;</td>
							                  	<td height="23">
							                  		<span class="black">
							                  			<strong>关 键 词:</strong>						                  			<span class="cms_title_blue"><input name="keywords" type="text" class="cms_text" size="26" value="<%=document.getKeywords()%>">
							                  			</span>
&nbsp;&nbsp;							                  		</span>
													级别:
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
														</select>
&nbsp;												  </span>
											  		
											  		<label>
											  			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
														<strong>&nbsp;编稿时间:</strong>										  			<span class="black">
											  				<span class="cms_title_blue">
																<input name="docwtime" type="text" dateformat="yyyy-MM-dd HH:mm:ss" class="cms_text" value="<%=document.getDocwtime()%>" size="20" readonly="true" >
															</span>
														</span>													</label>						                  	  </td>
							                </tr>
							                <tr height="23">
							                <td>&nbsp;</td>
							                <td colspan="3"><strong>发布模板:</strong>
							                <span class="cms_title_blue">
																			<input type="radio" name="selecttpl" value="0" checked="true" onClick="changtpl(0)" disabled>
																			<select name="detailtemplate_id" class="cms_select" style="width:130px" disabled>
																				<%
																				  List list=null;
																				  TemplateManager tmi=new TemplateManagerImpl();
																				  Template template=new Template();
																				  list=tmi.getTemplateInfoListofSite(Integer.parseInt(siteid), 2);
																				  request.setAttribute("tpllist",list);
																				%>
																				<pg:list requestKey="tpllist">
																					<option value="<pg:cell colName="templateId"/>"<pg:equal colName="templateId" value="<%=tid%>">selected</pg:equal>>
																					<pg:cell colName="name"/> 
																				</pg:list>
																			</select>
																			
																		</span>		
							               
							                
							                <input type="radio" name="selecttpl" value="1" onClick="changtpl(1)" disabled>
																		<input type="hidden" name="parentDetailTpl"/>
																		<%
																		ChannelManager chnl = new ChannelManagerImpl();
																		Template tp = chnl.getDetailTemplateOfChannel(channelId);
																		String tname = (tp==null?"还没有设置":tp.getName());
																		%>
																		继承所属频道的细览模板(<span class="red_star"><%=tname%></span>)	  
																		<!--<a href="#" onClick="preview()" ><u>预览</u></a>-->
							                </td>
							                </tr>
							                <tr>
							                	<td height="26">&nbsp;												</td>
											  <td><strong>摘要内容:</strong>												  <textarea name="docabstract" cols="70" rows="5" class="cms_textarea"><%=document.getDocabstract()%></textarea></td>
							                </tr>
					                		<tr>
												<td height="26">&nbsp;</td>
												<td height="23" colspan="2">
													<!--<img align=absMiddle border=0 height=12 src="../images/text-correlated.gif" width=14 >
													<a id=tab2 href="#" onclick=tabClick(2) ><u>[相关文档]</u></a>-->												</td>
									        </tr>
				              		  </table>					              	</td>
					            </tr>
			            		
								<tr>
									<td  width="95%" height="22" colspan="3"  align="center" valign="top">
										<div>

											<input type="hidden" name="doclist">
											<input type="hidden" name="titles">
											<br/><iframe id="docxz" src="../channelManage/chn_doc_xzlist.jsp?flag=11&siteid=<%=siteid%>" frameborder="0" scrolling="auto" width="95%" height="300">
											</iframe>
										</div>									</td>
								</tr>
								<tr>
									<td  height="23">
										<div align="center">
											<input type="button" value="返回" class="cms_button" onclick="back()">
										</div>									</td>
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