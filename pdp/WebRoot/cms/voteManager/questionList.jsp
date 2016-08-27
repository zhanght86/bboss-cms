<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="com.frameworkset.platform.cms.votemanager.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.*"%>

<html>
	<head>
		<title>选项查看</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="../inc/css/cms.css" rel="stylesheet" type="text/css"/>
		<script language="javascript" src="../inc/js/func.js"></script>

  </head>
  <%
        AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request,response);
  	if (request.getParameter("qids")!=null&&!"".equals(request.getParameter("qids"))){
    	VoteManager voteMgr  = new VoteManagerImpl();
  		if ("delete".equals(request.getParameter("actionType"))){
  			if(voteMgr.delQuestions(request.getParameter("qids"))==1){
  				%><script language="javascript">alert("删除成功!");</script><%
  			}
  		}
  		if ("active".equals(request.getParameter("actionType"))){
  			if(voteMgr.activateQuestions(request.getParameter("qids"),accesscontroler.getUserAccount(),accesscontroler.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request))==1){
  				%><script language="javascript">alert("审核成功!");</script><%
  			}
  		}
  		if ("unactive".equals(request.getParameter("actionType"))){
  			if(voteMgr.unactivateQuestions(request.getParameter("qids"),accesscontroler.getUserAccount(),accesscontroler.getUserName(),com.frameworkset.util.StringUtil.getClientIP(request))==1){
  				%><script language="javascript">alert("取消审核成功!");</script><%
  			}
  		}
    	if ("settop".equals((String)request.getParameter("actionType"))){
    		if(voteMgr.setQuestionTop((String)request.getParameter("qids"))!=1){
    			%><SCRIPT language="javascript">alert("置顶失败！");</script><%
    		}
    	}
  	}
  %>
    <%
   	
    	
		
		CMSManager cmsM = new CMSManager();
	    cmsM.init(request,session,response,accesscontroler);
		int siteID = Integer.parseInt(cmsM.getSiteID());
  String deptID = accesscontroler.getChargeOrgId();
  String deptName = accesscontroler.getChargeOrgName();
  String login_user_name = accesscontroler.getUserAccount();
  int length = 0;
  length = accesscontroler.getAllRoleofUser(login_user_name).length;
  String str = "";
  boolean flag = false;
  for(int i=1;i<length;i++)
  {
    str = accesscontroler.getAllRoleofUser(login_user_name)[i].getRoleName();
    if(str.equals("administrator")||str.equals("网上调查管理员"))
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
       document.location.href="questionList.jsp?deptID=<%=deptID%>&deptName=<%=deptName%>&channel=<%=request.getParameter("channel")%>";
    </script>
  <%}
%>
  <body>
  <form name="questionList" method="post" action="<%=request.getContextPath()%>/cms/voteManager/questionList.jsp" >
  	<input name="actionType" type="hidden" />
  	<input name="qids" type="hidden" />
  	<input name="deptID" type="hidden" value="<%=deptID%>"/>
  	<input name="deptName" type="hidden" value="<%=deptName%>"/>
  	<input name="channel" type="hidden" value="<%=request.getParameter("channel")%>"/>
  	
	<table width="100%" border="0" bgcolor="#F3F4F9" cellspacing="0">
  		<tr>
  		<td width="47%"  height="25" >
  			问卷名称：<input name="surveyName" id="surveyName" type="text" style="width:200px;"/>
  		</td>
  		<td   height="25"  colspan="2" >
  			是否审核：<input name="active" id="active" type="radio" value="1"/>审核
  			<input name="active" id="active" type="radio" value="0" />未审核
  			<input name="active" id="active" type="radio" value="2" checked/>全部
  		</td></tr>
  		<TR>
  		<td width="47%"  height="25" >
  			问题描述：<input name="questionTitle" id="questionTitle" type="text" style="width:200px;"/>
  		</td>
  		<td   height="25">
  			回答方式：<input name="style" id="style" type="radio" value="0"/>单选
  			<input name="style" id="style" type="radio" value="1" />多选
  			<input name="style" id="style" type="radio" value="2" />自由回答
  			<input name="style" id="style" type="radio" value="3" checked/>全部
  		</td>
  		<TD>
  		<a style="cursor:hand" onClick="return query();"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">查询</div></a>
  		<a style="cursor:hand" onClick="return clean();"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">显示所有</div></a>
  		</TD>
  		
  		</tr>
  	</table>
  	
	<table id="tag_attach_list" width="100%" border="1" align=center cellpadding="3" cellspacing="0" bordercolor="#B7CBE4"  class="Datalisttable" id="docListTable">
			<tr>
				<td height="25" colspan="9" background="../images/data_list_tHeadbg.jpg" style="text-align:left; background:url(../images/data_list_tHeadbg.jpg) repeat-y center #B7BDD7">
					<div  class="DocumentOperT">问题操作：</div>
					<!-- <a style="cursor:hand" onClick="return setTop()"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">问题置顶</div></a>
					<a style="cursor:hand" onClick="questionDelete()"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">删除</div></a>-->
					<a style="cursor:hand" onClick="questionActivate()"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">审核</div></a>
					<a style="cursor:hand" onClick="questionUnActivate()"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">取消审核</div></a>
					<a style="cursor:hand" onClick="return2()"><div class="DocumentOper"><img src="../images/new_doc.gif" class="operStyle">返回</div></a>
				</td>
		    </tr> 
		</table>
    <table width="100%" border="0" cellpadding="5" cellspacing="0" class="Datalisttable">
 				<pg:listdata dataInfo="CmsQuestionList" keyName="CmsQuestionList" />
				<!--分页显示开始,分页标签初始化-->
				<pg:pager maxPageItems="30" scope="request" data="CmsQuestionList" isList="false">
				<tr  class="cms_report_tr">
					<!--设置分页表头-->
					<td class="headercolor"  width="3%" align="left">全选</td>
					<td class="headercolor" width="5%" align="left">
						<P align="left">
							<INPUT type="checkbox" name="checkBoxAll" onclick="checkAll('checkBoxAll','checkBoxOne')">
						</P>
					</td>
					<td class="headercolor"  width="10%" align="left">问题编号</td>
					<td class="headercolor"  width="5%" align="left">置顶</td>
					<td class="headercolor"              align="left">问题描述</td>
					<td class="headercolor"              align="left">问卷名称</td>
					<td class="headercolor"  width="10%" align="left">回答方式</td>
					<td class="headercolor"  width="10%" align="left">答复次数</td>
					<td class="headercolor"  width="5%" align="left">审核</td>
					<td class="headercolor"  width="5%" align="left"></td>
				</tr>
				
				
					<pg:param name="id" />
					<pg:param name="title" />
					<pg:param name="surveyName" />
					<pg:param name="active" />
					<pg:param name="questionTitle" />
					<pg:param name="style" />
					<pg:param name="deptName" />
					<pg:param name="deptID" />
					
				<!--list标签循环输出每条记录-->
				<pg:list>
				<tr class="cms_data_tr" onMouseOver="high(this)" onMouseOut="unhigh(this)" >
					<td class="tablecells" nowrap="nowrap" width="3%">
						<div align="left"><img src="../images/note.gif" width="16" height="15"></div>
					</td>
					<td class="tablecells" nowrap="true">
					<P align="left">
						<INPUT type="checkbox" name="checkBoxOne" value='<pg:cell colName="id" defaultValue=""/>' >
					</P>
					</td>
					<td class="tablecells" nowrap="nowrap" >
						<pg:cell colName="id" defaultValue="" />
					</td>
					<td class="tablecells" nowrap="nowrap" >
						<pg:equal colName="isTop" value="1"><font color="#ff0000">置顶</font></pg:equal>
					</td>
					<td class="tablecells" nowrap="nowrap" title="<pg:cell colName="title" defaultValue=""/>">
						<pg:cell colName="title" defaultValue="" maxlength="15" replace="..."/>
					</td>
					<td class="tablecells" nowrap="nowrap" title="<pg:cell colName="surveyName" defaultValue=""/>">
						<pg:cell colName="surveyName" defaultValue="" maxlength="15" replace="..."/>
					</td>
					<td class="tablecells" nowrap="nowrap"  >
						<pg:equal colName="style" value="0">单选</pg:equal>
						<pg:equal colName="style" value="1">多选</pg:equal>
						<pg:equal colName="style" value="2">自由回答</pg:equal>
					</td>
					<td class="tablecells" nowrap="nowrap"  >
						<pg:cell colName="votecount" defaultValue="0" />
					</td>
					<td class="tablecells" nowrap="nowrap" >
						<pg:equal colName="active" value="0">否</pg:equal>
						<pg:equal colName="active" value="1">是</pg:equal>
					</td>
					<TD class="tablecells" nowrap="nowrap" >
					<a style="cursor:hand;color:#0000cc" href="javascript:void(0)" onClick="seeIP('<pg:cell colName="id" defaultValue=""/>')">IP记录</a>
					</TD>
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
</FORM>
			
  </body>
    
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
	function return2()
	{
	  document.location.href="votelist.jsp";
	}
	function questionUpdate(id){
		window.open('questionnaire.jsp?qid='+id,'mywindow','toolbar=no,left=100,top=0,width=700,scrollbars=yes,height=600,menubar=no,systemMenu=no');
	}
	
	function seeIP(id)
	{
		window.open('seeIP.jsp?qid='+id,'mywindow','toolbar=no,left=100,top=100,width=600,scrollbars=yes,height=530,menubar=no,systemMenu=yes');
	}
	
	function  questionDelete(){
			
		document.all("qids").value = "-1";
		
		var obj = document.getElementsByName("checkBoxOne");
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				document.all("qids").value += ","+obj[i].value;
			}
		}
		if (document.all("qids").value=='-1'){
			alert("请作选择!");
			return;
		}
		if (!confirm("是否要删除这些问题?"))
			return;
		document.all("actionType").value ="delete";
		document.questionList.submit();
	}
	
	function questionActivate(){
		document.all("qids").value = "-1";
		
		var obj = document.getElementsByName("checkBoxOne");
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				document.all("qids").value += ","+obj[i].value;
			}
		}
		if (document.all("qids").value=='-1'){
			alert("请作选择!");
			return;
		}
		if (!confirm("是否要审核这些问题?"))
			return;
			
		document.all("actionType").value ="active";
		document.questionList.submit();
	}
	
	function questionUnActivate(){
		document.all("qids").value = "-1";
		
		var obj = document.getElementsByName("checkBoxOne");
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				document.all("qids").value += ","+obj[i].value;
			}
		}
		if (document.all("qids").value=='-1'){
			alert("请作选择!");
			return;
		}
		if (!confirm("是否要取消审核这些问题?"))
			return;
		document.all("actionType").value ="unactive";
		document.questionList.submit();
	}
	
	function setTop(){
		document.all("qids").value = "";
		
		var obj = document.getElementsByName("checkBoxOne");
		for (var i=0;i<obj.length;i++){
			if (obj[i].checked){
				document.all("qids").value = obj[i].value;
				break;
			}
		}
		if (document.all("qids").value==""){
			alert("请作选择!");
			return;
		}
		
		document.all("actionType").value ="settop";
		document.questionList.submit();
	}
	
function query(){
		
		document.questionList.submit();
	}
	
function clean(){
	document.all("surveyName").value = "";
	document.all("active")[2].checked = true;
	document.questionList.submit();
}
</script>
</html>
