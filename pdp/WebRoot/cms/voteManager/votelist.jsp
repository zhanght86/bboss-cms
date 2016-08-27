<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>

<html>
	<head>
<%
	String path_= request.getContextPath();
    String basePath = request.getScheme() + "://"	+ request.getServerName() + ":" +
    request.getServerPort() + path_+ "/"; 
%>
		<title>选项查看</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css"/>
		<script language="javascript" src="../inc/js/func.js"></script>

     </head>
    
<script language="JavaScript" src="../include/pager.js" type="text/javascript"></script>
<link href="<%=basePath%>cms/inc/css/cms.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=basePath%>sysmanager/scripts/selectTime.js" ></script>
<script  src="<%=basePath%>public/datetime/calender.js" language="javascript"></script>
<script  src="<%=basePath%>public/datetime/calender_date.js" language="javascript"></script>
     <SCRIPT language="javascript">
     //复选框全部选中
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
	
	function newqs()
	{
		var w=window.showModalDialog('questionnairenew.jsp?channel=<%=request.getParameter("channel")%>',window,'dialogWidth:800px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
	 	if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	     if(w=='ok')
	     {
	 	   location.reload(); 
	 	  }
	 	}   
	
	}
	
	function newq()
	{
		var w=window.showModalDialog('questionnaire.jsp?channel=<%=request.getParameter("channel")%>',window,'dialogWidth:800px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
	 	if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	     if(w=='ok')
	     {
	 	   location.reload(); 
	 	  }
	 	}   
	
	}

	function modifynew(id){
		var w=window.showModalDialog('questionnairenew.jsp?id='+id+'&channel=<%=request.getParameter("channel")%>',window,'dialogWidth:800px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
	 	if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	       {
	 	     location.reload();
	 	   } 
	 	}   
	}
	
	function modify(id){
		var w=window.showModalDialog('questionnaire.jsp?id='+id+'&channel=<%=request.getParameter("channel")%>',window,'dialogWidth:800px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
	 	if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	       {
	 	     location.reload();
	 	   } 
	 	}   
	}
	
	function getResult(id){
		window.open('seeresult_bz.jsp?titleid='+id+'&opid=1','window','toolbar=no,left=100,top=50,width=600,scrollbars=yes,height=700,menubar=no,systemMenu=no');   
	}
	function audit(id){
	//window.showModalDialog('answerlist.jsp?id='+id,window,'dialogWidth:500px;dialogHeight:800px;center:yes;status:no;scroll:yes;help:no');
		window.open('answerlist.jsp?id='+id,'window','toolbar=no,left=100,top=50,width=700,scrollbars=yes,height=540,menubar=no,systemMenu=no');
	 }
	  function view(id)
	 {
	    window.showModalDialog('voteListView.jsp?id='+id,window,'dialogWidth:800px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
	 }
	  
	  function exportExcel(id){
		  window.location.href="<%=request.getContextPath()%>/voteMobile/newExportExcel.freepage?titleId="+id;
		 return;
	  }
	
   </SCRIPT>
  <%
   	
    	AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request,response);
		
		CMSManager cmsM = new CMSManager();
	    cmsM.init(request,session,response,accesscontroler);
		int siteID = Integer.parseInt(cmsM.getSiteID());
  String deptID = accesscontroler.getChargeOrgId();
  String deptName = accesscontroler.getChargeOrgName();
  String login_user_name = accesscontroler.getUserAccount();
  int length = 0;
  String siteNameRole = cmsM.getCurrentSite().getName()+"站点管理员";
  com.frameworkset.platform.security.authorization.AuthRole[] roles = accesscontroler.getAllRoleofUser(login_user_name);
  String str = "";
  boolean flag = false;
  for(int i=1;roles != null && i<roles.length;i++)
  {
    str = roles[i].getRoleName();
    if(str.equals("administrator")||str.equals(siteNameRole)||str.equals("网上调查管理员"))
    {
      flag = true;
      break;
    }
  }
  if(flag)
  {
    deptName = "admin";
  }
  if((request.getParameter("deptID")==null)||(request.getParameter("deptName")==null))
  {%>
    <script>
       document.location.href="votelist.jsp?deptID=<%=deptID%>&deptName=<%=deptName%>&channel=<%=request.getParameter("channel")%>";
    </script>
  <%}
%>
     <body topmargin="1" rightmargin="1" scroll=auto leftmargin="1">
      
     
	<form name="voteList" method="post">
	<input name="channelid" id="channelid" type="hidden" >
	
	<table width="100%" border="0" bgcolor="#F3F4F9" cellspacing="0">
	<td  style="background:url(<%=basePath%>cms/images/querybox_bg.gif) repeat-x top" >
		<table width="100%" border="0" cellpadding="0" align="center" cellspacing="0"   class="query_box">
				<tr>
					<td>
						<table width="100%" border="0">
							<tr>
								<td height="23">&nbsp; 当前位置：<%=request.getParameter("channel")%></td>
								</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="query_table">
										<tr>
											<td height='30' align="right">问卷标题： </td>
											<td height='30' align="center" valign='middle' width="16">
											<div align="left">
											<%
											String surveyTitle=request.getParameter("surveyTitle");
											%>
											<input type="hidden" name="deptID" value="<%=deptID%>"/>
											<input type="hidden" name="deptName" value="<%=deptName%>"/>
											<input type="hidden" name="channel" value="<%=request.getParameter("channel")%>"/>
											<input name="surveyTitle" id="surveyTitle" type="text" size="16"  class="cms_text" value="<% if(surveyTitle!=null&&!surveyTitle.equals("")) out.print(surveyTitle);%>">
											</div></td>
											<!-- <td height='30' align="right">所属频道:</td>
											<td height='30' align="left" valign='middle'>-->
											<input name="channelName" id="channelName" type="hidden" style="width:100px;" readonly="true" onclick="return selChannel()"/>
											<!-- </td>-->
										  <td height='30' align="right">是否审核：</td>
										  <td height='30' align="left" valign='middle'>	
											 <input name="active" id="active" type="radio" value="1"/>审核
  											 <input name="active" id="active" type="radio" value="0" />未审核
  											 <input name="active" id="active" type="radio" value="2" checked/>全部
										 </td>												  										  										 
									    </tr>
										<tr>

											<td height='30' align="right">
												 创建时间从：											</td>
											<td height='30' valign='middle' align="left">
												<div align="left">
												<%
												String createDateFrom=request.getParameter("createDateFrom");
												%>
													<input name="createDateFrom" id="createDateFrom" type="text" size="16"  class="cms_text" value="<% if(createDateFrom!=null&&!createDateFrom.equals("")) out.print(createDateFrom);%>">
												<input name="button" type="button" onClick="showdate(document.all('createDateFrom'))" value="..."/></div>											</td>
											<td width="8%" height='30' align="right">
												 到:											</td>
											<td width="20%" height='30' align="center" valign='middle'>
												<div align="left">
												<%
												String createDateTo=request.getParameter("createDateTo");
												%>
												 <input name="createDateTo" id="createDateTo" type="text" size="16"  class="cms_text" value="<% if(createDateTo!=null&&!createDateTo.equals("")) out.print(createDateTo);%>">
												 <input name="button" type="button" onClick="showdate(document.all('createDateTo'))" value="..."/></div>										  </td>
											<td height='30' colspan="2">
												<div align="right">
													<a style="cursor:hand" onClick="return cleanpart();"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">查询</div></a>
  													<a style="cursor:hand" onClick="return clean();"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">显示所有</div></a>
													</div></td>
										</tr>
								  </table>
				  </td>
				</tr>
		  </table>
	</td>
  </tr>
  <tr ><td colspan="2" height="2"></td></tr>
</table>
  	
	<table id="tag_attach_list" width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" id="docListTable">
			<tr>
				<td height='25' colspan="9" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
					<div  class="DocumentOperT">问卷操作：</div>
						<%
					 if(flag ||(request.getParameter("channel").equals("网上调查")
							 &&accesscontroler.checkPermission("dchmanager","create","titleManager"))
							 ||(request.getParameter("channel").equals("网上测评")
									 &&accesscontroler.checkPermission("wscpManager","create","wscpTitleManager")))
					 {
		            %>
		           <!--  
		           	gw_tanx 20150204
		           <a style="cursor:hand" onClick="newqs()">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">新增问卷</div></a> -->
					<a style="cursor:hand" onClick="newq()">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">新增问卷</div></a>
					<%
					}
					if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","delete","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","delete","wscpTitleManager")))
					// if(accesscontroler.checkPermission("dchmanager","delete","titleManager"))
					 {
		            %>
					<a style="cursor:hand"onclick="return myAction('delete')">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">删除问卷</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","audit","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","audit","wscpTitleManager")))
					// if(accesscontroler.checkPermission("dchmanager","audit","titleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="surveyActivate()">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">审核</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","stopaudit","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","stopaudit","wscpTitleManager")))
					 //if(accesscontroler.checkPermission("dchmanager","stopaudit","titleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="surveyUnActivate()">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">取消审核</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","top","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","top","wscpTitleManager")))
					 //if(accesscontroler.checkPermission("dchmanager","top","titleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="return myAction('settop')">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">问卷置顶</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","stoptop","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","stoptop","wscpTitleManager")))
					 //if(accesscontroler.checkPermission("dchmanager","stoptop","titleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="return myAction('canceltop')">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">取消置顶</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","cancellook","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","cancellook","wscpTitleManager")))
					// if(accesscontroler.checkPermission("dchmanager","cancellook","titleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="return myAction('cancellook')">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">取消查看</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","look","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","look","wscpTitleManager")))
					 //if(accesscontroler.checkPermission("dchmanager","look","titleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="return myAction('look')">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">恢复查看</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","cleartitle","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","cleartitle","wscpTitleManager")))
					 //if(accesscontroler.checkPermission("dchmanager","cleartitle","titleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="return myAction('clearvote')">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">投票清零</div></a>
					<%
					 }
					 if(flag ||request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","copy","wscpTitleManager"))
					 {
		            %>
					<a style="cursor:hand" onClick="return myAction('copy')">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">复制测评</div></a>
					<%
					 }
					 if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","questionlist","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","questionlist","wscpTitleManager")))
					 //if(accesscontroler.checkPermission("dchmanager","questionlist","titleManager"))
					 {
		            %>
					<a style="cursor:hand" href="questionList.jsp?channel=<%=request.getParameter("channel")%>">
					<div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">问题列表</div></a>
					<%}%>
					<!-- <a style="cursor:hand" href="vote_tag.jsp"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">发布预览</div></a>-->
				</td>
		    </tr> 
		</table>
		
		<table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable"><!--分页显示开始,分页标签初始化-->
         				<pg:listdata dataInfo="com.frameworkset.platform.cms.votemanager.Votelist" keyName="Votelist" />
						
						<pg:pager maxPageItems="30" scope="request" data="Votelist" isList="false">
							<!--检测当前页面是否有记录--><!--list标签循环输出每条记录-->
							<TR class="cms_report_tr">
								<!--设置分页表头-->
								<td class="headercolor"  width="2%" align="left"></td>
								<TD width="2%" align="center" >
								<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')" value="on" class="checkbox">
</TD>                            
								<TD class="headercolor" width="9%">问卷号</TD>
								<TD class="headercolor" width="7%">置顶</TD>	
								<TD class="headercolor"  >问卷标题</TD>
								<TD class="headercolor" >所属部门</TD>
								<TD class="headercolor" width="5%">审核</TD>
								<TD class="headercolor" width="5%">查看</TD>
								<TD class="headercolor" width="5%">创建时间</TD>
								<TD class="headercolor" width="5%">创建人</TD>	
								<TD class="headercolor" width="10%">操作</TD>	
												
							</TR>
							<pg:param name="id"/>
							<pg:param name="name" />
							<pg:param name="siteid" />
							<pg:param name="state" />							
							<pg:param name="deptID"/>
							<pg:param name="deptName"/>
							<pg:param name="foundername" />
							<pg:param name="channelid"/>
							<pg:param name="surveyTitle"/>
							<pg:param name="active"/>
							<pg:param name="createDateFrom"/>
							<pg:param name="createDateTo"/>
							<pg:param name="channel"/>
								
							<pg:list>	
							<tr class="cms_data_tr" onMouseOver="high(this)" onMouseOut="unhigh(this)" >
								<td class="tablecells" nowrap="nowrap" width="3%">
									<div align="left"><img src="../images/note.gif" width="16" height="15"></div>
								</td>
								<td class="tablecells" align=center height='30'>
								<INPUT type="checkbox" name="checkBoxOne"  onclick="checkOne('checkBoxAll','checkBoxOne')" value='<pg:cell colName="id" defaultValue=""/>' >
								</td>
								<td class="tablecells" nowrap="true" >
										<pg:cell colName="id" defaultValue="" />
								</td>
								<td class="tablecells" nowrap="nowrap" >
									<pg:equal colName="isTop" value="1"><font color="#ff0000">置顶</font></pg:equal>

								</td>
								<td class="tablecells" nowrap="true" title="<pg:cell colName="name" defaultValue=""/>" onClick="view('<pg:cell colName='id' defaultValue=''/>')" style="cursor:hand">
										  <pg:cell colName="name" maxlength="20" replace="..." defaultValue=""/>
								</td>
									
								<td class="tablecells" nowrap="true" >
										<pg:cell colName="depart_name" defaultValue="" />
								</td>
								<td class="tablecells" nowrap="nowrap" >
									<pg:equal colName="active" value="0">否</pg:equal>
									<pg:equal colName="active" value="1">是</pg:equal>
								</td>
								<td class="tablecells" nowrap="nowrap" >
									<pg:equal colName="islook" value="1">否</pg:equal>
									<pg:equal colName="islook" value="0">是</pg:equal>
								</td>
								<td class="tablecells" nowrap="true" >
										<pg:cell colName="foundDate" defaultValue="" />
								</td>
								<td class="tablecells" nowrap="true" >
										<pg:cell colName="foundername" defaultValue="" />
								</td>
								<td class="tablecells" nowrap="true" >
								<%
								 boolean ff = false;
					               if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","edit","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","edit","wscpTitleManager")))
					              // if(accesscontroler.checkPermission("dchmanager","edit","titleManager"))
					               {
					               ff = true;
		                       %>
								<%-- 
									gw_tanx 20150204
								<a style="cursor:hand;color:#0000cc" href="javascript:void(0)" onclick="modifynew('<pg:cell colName='id' defaultValue=''/>')"/>
								修改问卷</a>
								 --%>
								<a style="cursor:hand;color:#0000cc" href="javascript:void(0)" onclick="modify('<pg:cell colName='id' defaultValue=''/>')"/>
								修改问卷</a>
								
								<%
								}
								   if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","browse","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","browse","wscpTitleManager")))
					               //if(accesscontroler.checkPermission("dchmanager","browse","titleManager"))
					               {
					               ff = true;
		                       %>
		                       |
								<a style="cursor:hand;color:#0000cc" href="javascript:void(0)" onclick="havealook('<pg:cell colName='id' defaultValue=''/>')"/>
								预览</a>
								
								<%
								}
								if(flag ||(request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager","count","titleManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager","count","wscpTitleManager")))
					              //if(accesscontroler.checkPermission("dchmanager","count","titleManager"))
					               {
					                ff = true;
		                       %>|
								<a style="cursor:hand;color:#0000cc" href="javascript:void(0)" onclick="getResult('<pg:cell colName='id' defaultValue=''/>')"/>
								统计</a>
								<%}
								if(!ff)
								{
								 %>无权限<%
								}
								%>|
								<a style="cursor:hand;color:#0000cc" href="javascript:void(0)" onclick="exportExcel('<pg:cell colName='id' defaultValue=''/>')"/>
								导出提交答案</a>
								</td>
																
							</tr>
							</pg:list>
						<tr class="labeltable_middle_tr_01">
						<td colspan=13 ><div class="Data_List_Table_Bottom"> 
							 共
							<pg:rowcount />
							 条记录
							<pg:index />				
							</div>  
						</td>
						
					    </tr>
					     
							<P align="center"><input name="queryString" value="<pg:querystring/>" type="hidden"></P>
						</pg:pager>

				  </table>
				  <input type="hidden" value="" name="actioinType">
				</form><P align="center">
		</P></body>
</html>

<SCRIPT language="javascript">
function myAction(type){
	obj = document.getElementsByName("checkBoxOne");
	var c =0;
	var titleids="-1";
	for (i=0;i<obj.length;i++){
		if (obj[i].checked){
		 
		
			c++;
			break;
		}
		
	}
	
	for (i=0;i<obj.length;i++){
		
		if (obj[i].checked){
		
		titleids+= ","+obj[i].value;		
		}
	}

	if (c == 0){
		alert("请你选择！");
		return;
	}
	
	if (type=='modify'){
		document.forms[0].actioinType.value = "modify";
		document.voteList.submit();
	}
	if (type=='delete'){
		if(!confirm("是否要删除选项？"))
			return;
		var url = "operationOfVote.jsp?actionType=delete&titleID="+titleids;
		var w=window.showModalDialog(url,window,'');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}
	}
	if (type=='settop'){
		titleids = titleids.split(",");
		if(titleids.length>2)
		{
		  alert("只能置顶一个！");
		  return false;
		}
		var url = "operationOfVote.jsp?actionType=settop&titleID="+titleids[1];
		var w=window.showModalDialog(url,window,'');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}

	}
	if(type=='canceltop')
	{
		titleids = titleids.split(",");
		var url = "operationOfVote.jsp?actionType=canceltop&titleID="+titleids[1];
		var w=window.showModalDialog(url,window,'');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}
		//document.voteList.action='<%=request.getContextPath()%>/cms/voteManager/votelist.jsp?actionType=canceltop&titleID='+titleids[1];
		//document.voteList.submit();
	}
	if (type=='cancellook'){
		var url = "operationOfVote.jsp?actionType=cancellook&sids="+titleids;
		var w=window.showModalDialog(url,window,'');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}
	}
	if (type=='look'){
		var url = "operationOfVote.jsp?actionType=look&sids="+titleids;
		var w=window.showModalDialog(url,window,'');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}
	}
	if(type=='clearvote')
	{
	  if(confirm("您确定要清空所选调查的投票结果？"))
	  {
	    // titleids = titleids.split(",");
	     var url = "operationOfVote.jsp?actionType=clearvote&titleID="+titleids;
		 var w=window.showModalDialog(url,window,'');
		 if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	  }
	  }
	}
	if (type=='copy'){
		var copyTitleIds = titleids.split(',');
		if(copyTitleIds.length>2)
		{
			alert("只能选择一条记录！");
			return false;
		}
		else
		{
			titleids = copyTitleIds[1];
		}
		var w=window.showModalDialog('copyOfQuestionnaire.jsp?id='+titleids+'&channel=<%=request.getParameter("channel")%>',window,'dialogWidth:800px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}
	}
}

function havealook(id){
	window.showModalDialog('surveyPreview.jsp?id='+id,window,'dialogWidth:500px;dialogHeight:800px;center:yes;status:no;scroll:yes;help:no');
	//if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("flag").value.length>0||
	     //  document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	     //  document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	     //  {
	     //    cleanpart();
	     //  }  
}

function selChannel(){
	var ret = window.showModalDialog('channel_tree_frame.jsp?siteid=<%=siteID%>',window,'dialogWidth:500px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
	ret = ret.split(":");
	if(ret.length>=2)
	{
	  document.all("channelName").value = ret[1];
	  document.all("channelid").value = ret[2];
	}
}

function clean(){
    document.all("surveyTitle").value= "";
	document.all("channelName").value = "";
	document.all("channelid").value = "";
	document.all("createDateFrom").value = "";
	document.all("createDateTo").value = "";
	document.all("active")[2].checked = true;
	document.voteList.submit();
}

function cleanpart(){
	document.voteList.action="votelist.jsp?actionType=search";
	
	var createDateFrom = document.all.createDateFrom.value;
	var createDateTo = document.all.createDateTo.value;
	if(createDateTo.length>0 && createDateFrom.length>0)
	if(createDateTo<createDateFrom)
	{
		alert("结束时间不能小于开始时间!");
		return false;
	}
	//bug432:结束时间不能小于开始时间,weida
	
	document.voteList.submit();
}
	function surveyActivate(){
		var sids = "-1";
		
		var obj = document.getElementsByName("checkBoxOne");
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				sids += ","+obj[i].value;
			}
		}
		if (sids=='-1'){
			alert("请作选择!");
			return;
		}
		if (!confirm("是否要审核这些问题?"))
			return;
		var url = "operationOfVote.jsp?actionType=active&sids="+sids;
		var w=window.showModalDialog(url,window,'');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}
	}
	
	function surveyUnActivate(){
		var sids = "-1";
		
		var obj = document.getElementsByName("checkBoxOne");
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				sids += ","+obj[i].value;
			}
		}
		if (sids=='-1'){
			alert("请作选择!");
			return;
		}
		if (!confirm("是否要取消审核这些问题?"))
			return;
		var url = "operationOfVote.jsp?actionType=unactive&sids="+sids;
		var w=window.showModalDialog(url,window,'');
		if(document.all("surveyTitle").value.length>0||document.all("channelName").value.length>0||document.all("deptID").value.length>0||
	       document.all("channelid").value.length>0||document.all("createDateFrom").value.length>0||
	       document.all("createDateTo").value.length>0||document.all("active")[2].checked != true)
	       {
	         if(w=='ok')
	           cleanpart();
	       }
	     else{
	       if(w=='ok')
	 	     location.reload(); 
	 	}
		//document.voteList.action='<%=request.getContextPath()%>/cms/voteManager/votelist.jsp?actionType=unactive&sids='+sids;
		//document.voteList.submit();
	}
	
</script>