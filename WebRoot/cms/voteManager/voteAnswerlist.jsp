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
	
	function audit(id){
	//window.showModalDialog('answerlist.jsp?id='+id,window,'dialogWidth:500px;dialogHeight:800px;center:yes;status:no;scroll:yes;help:no');
		window.open('answerlist.jsp?id='+id,'window','toolbar=no,left=100,top=50,width=700,scrollbars=yes,height=540,menubar=no,systemMenu=no');
	 }
	 
	 function view(id)
	 {
	    window.showModalDialog('voteListView.jsp?id='+id,window,'dialogWidth:800px;dialogHeight:600px;center:yes;status:no;scroll:yes;help:no');
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
       document.location.href="voteAnswerlist.jsp?deptID=<%=deptID%>&deptName=<%=deptName%>&channel=<%=request.getParameter("channel")%>";
    </script>
  <%}
   %>
     <body topmargin="1" rightmargin="1" scroll=auto leftmargin="1">
      
     
	<form name="voteList" method="post">
	<input name="channelid" id="channelid" type="hidden" >
	<input name="channel" id="channel" type="hidden" value="<%=request.getParameter("channel")%>">
	
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
								<TD class="headercolor" width="7%">审核</TD>
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
								<td class="tablecells" nowrap="true" >
										<pg:cell colName="foundDate" defaultValue="" />
								</td>
								<td class="tablecells" nowrap="true" >
										<pg:cell colName="foundername" defaultValue="" />
								</td>
								<td class="tablecells" nowrap="true" >
								<%
					               if(flag || (request.getParameter("channel").equals("网上调查")&&accesscontroler.checkPermission("dchmanager1","adviceaudit","answerManager"))||(request.getParameter("channel").equals("网上测评")&&accesscontroler.checkPermission("wscpManager_1","adviceaudit","wscpAnswerManager")))
					               //if(accesscontroler.checkPermission("dchmanager1","adviceaudit","answerManager"))
					               {
		                       %>
								<a style="cursor:hand;color:#0000cc" href="javascript:void" onclick="audit('<pg:cell colName='id' defaultValue=''/>')"/>
								意见审核</a>
								<%}else{%>无权限<%}%>
								</td>							
							</tr>
							</pg:list>
						<tr class="labeltable_middle_tr_01">
						<td colspan=10 ><div class="Data_List_Table_Bottom"> 
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
	document.voteList.action="voteAnswerlist.jsp?actionType=search";
	
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
	
</script>