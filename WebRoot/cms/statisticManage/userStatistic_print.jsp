<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%@ page import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.customform.*"%>
<%@ page import="com.frameworkset.common.tag.contextmenu.*"%>
<%@ include file="../../sysmanager/include/global1.jsp"%>
<%@ include file="../../sysmanager/base/scripts/panes.jsp"%>

<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	String maxPageItems = request.getParameter("maxPageItems");
    if(maxPageItems==null || maxPageItems==""){
    	maxPageItems = "10";
    }
    int maxPageItems_int = Integer.parseInt(maxPageItems);
	String startDate=request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	if(startDate==null){
		startDate="";
	}
	if(endDate==null){
		endDate="";
	}
	//System.out.println(startDate+endDate);
	int i=1;
%>
<html>
<head>
<title>
	CMS:::统计功能
</title>
<link  href="../inc/css/cms.css" rel="stylesheet" type="text/css"></link>
<script language="javascript" src="../inc/js/func.js"></script>
<script language="javascript" src=src="../../sysmanager/include/pager.js" type="text/javascript"></script>
<script type="text/javascript">
/****************************************************************/
function doPrintPreview()
	{
		//打印预览
		//dividePage();
		//WB.ExecWB(7,1);
		//cancelDividePage();
		//window.preview();
	}
	
function doPrint()
	{
		//打印当前页
		//document.all("noPrintTd").style.visibility = "hidden";
		window.print();
		//document.all("noPrintTd").style.visibility = "visible";
	}

</script>
<style type="text/css">
body {
	background-color: #ffffff;
scrollbar-face-color: #C9D1E4; 
scrollbar-shadow-color: #6B74B7; 
scrollbar-highlight-color: white; 
scrollbar-3dlight-color: #E100E1; 
scrollbar-darkshadow-color:#E100E1; 
scrollbar-arrow-color:#003492; 
scrollbar-base-color: #E100E1; 
scrollbar-track-color: #E9EDF3; 
COLOR: #000000;
}
</style>
<STYLE MEDIA="print">
	#navmenu {display: none}
	#article {width: 6.5in}
	#article {height: 9.2in}
	#x1 {display: none}
	#x2 {display: none}
	#x3 {display: none}
	#divideFlagTitle {page-break-before:always}
</STYLE>
</head>
<body topmargin="2" rightmargin="0" leftmargin="1" righttmargin="1">
<form name="form1" action="" method="post" >

<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
    <pg:listdata dataInfo="UserStatisticList" keyName="UserStatisticList"/>
		                    <!--分页显示开始,分页标签初始化-->
		                    <pg:pager maxPageItems="<%=maxPageItems_int%>" scope="request" data="UserStatisticList" isList="true">
		                    <pg:param name="startDate" value="<%=startDate%>"/>
		                    <pg:param name="endDate" value="<%=endDate%>"/>
		                    <pg:param name="maxPageItems" value="<%=maxPageItems%>"/>
	<tr id="trTitle" >
		<td valign="absMiddle" align="center" height="10" colspan="9"><h3>用户统计信息打印</h3></td>
	</tr>
	<tr id="trTime" >
		<td align="left" height="19" colspan="9">&nbsp;&nbsp;开始时间:<%=startDate%>
		&nbsp;&nbsp;&nbsp;结束时间:<%=endDate%>
		&nbsp;&nbsp;&nbsp;
<!--		&nbsp;&nbsp;&nbsp;<a onclick="javascript:doPrintPreview()" id="x2" style="cursor:hand;">[打印预览]</a>-->
		&nbsp;&nbsp;&nbsp;<a onclick="javascript:doPrint()" id="x3" style="cursor:hand;">[打印]</a>
		&nbsp;&nbsp;&nbsp;<a onclick="javascript:window.close();" id="x3" style="cursor:hand;">[关闭]</a>
		</td>
	</tr>
	<tr >
		<td width="5%"></td>
		<td width="10%">
			用户名</td>
		<td width="15%" >
			真实姓名	</td>
		<td width="10%">
			当前状态	</td>
		<td width="20%">
			所属角色	</td>
		<td width="10%">
			录稿篇数	</td>
		<td width="10%">
			审稿篇数	</td>			
		<td width="10%">
			发稿篇数	</td>		
		<td width="10%">
			录稿总字数</td>
		
	</tr>
	<!--检测当前页面是否有记录-->
	<pg:notify>
	<tr height="18px" class="labeltable_middle_tr_01">
		<td colspan=100 align='center'>暂时没有记录</td>
	</tr>
	</pg:notify>			      
			      			    
	<!--list标签循环输出每条记录-->			      
	<pg:list>
	<tr>
	    <td><%=i++%></td>
		<td >
			<pg:cell colName="userName" defaultValue=""/></td>
		<td  >
			<pg:cell colName="userRealName" defaultValue=""/>	</td>
		<td >
			<pg:equal colName="userStatus" value="0">已删除</pg:equal>
			<pg:equal colName="userStatus" value="1">新申请</pg:equal>
			<pg:equal colName="userStatus" value="2">已注册</pg:equal>
			<pg:equal colName="userStatus" value="3">停用</pg:equal>
		</td>
		<td >
			<pg:cell colName="role" defaultValue=""/>	</td>
		<td >		    		
				<pg:cell colName="writeDocs" defaultValue=""/>			
		</td>
		<td >						
				<pg:cell colName="auditDocs" defaultValue=""/>			
		</td>		
		<td >					
				<pg:cell colName="publishDocs" defaultValue=""/>			
		</td>		
		<td >
			<pg:cell colName="totalWords" defaultValue=""/></td>		
	</tr>
	</pg:list>	
</table>
</pg:pager>
</form>
<iframe height="0" width="0" name="operIframe"></iframe>
</body>
</html>
