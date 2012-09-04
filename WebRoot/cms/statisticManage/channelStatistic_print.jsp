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
		//window.printpreview();
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
    <pg:listdata dataInfo="ChannelStatisticList" keyName="ChannelStatisticList"/>
		                    <!--分页显示开始,分页标签初始化-->
		                    <pg:pager maxPageItems="<%=maxPageItems_int%>" scope="request" data="ChannelStatisticList" isList="true">
		                    <pg:param name="startDate" value="<%=startDate%>"/>
		                    <pg:param name="endDate" value="<%=endDate%>"/>
		                    <pg:param name="maxPageItems" value="<%=maxPageItems%>"/>
	<tr id="trTitle" >
		<td valign="absMiddle" align="center" height="10" colspan="7"><h3>频道统计信息打印</h3></td>
	</tr>
	<tr id="trTime" >
		<td align="left" height="19" colspan="7">&nbsp;&nbsp;开始时间:<%=startDate%>
		&nbsp;&nbsp;&nbsp;结束时间:<%=endDate%>
		&nbsp;&nbsp;&nbsp;
<!--		&nbsp;&nbsp;&nbsp;<a onclick="javascript:doPrintPreview()" id="x2" style="cursor:hand;">[打印预览]</a>-->
		&nbsp;&nbsp;&nbsp;<a onclick="javascript:doPrint()" id="x3" style="cursor:hand;">[打印]</a>
		&nbsp;&nbsp;&nbsp;<a onclick="javascript:window.close();" id="x3" style="cursor:hand;">[关闭]</a>
		</td>
	</tr>
	<tr >
		<td width="5%"></td>
		<td width="20%">
			站点名称</td>
		<td width="20%" >
			频道名称	</td>		
		<td width="15%">
			录入篇数	</td>	
		<td width="10%">
			审稿篇数</td>	
		<td width="15%">
			发稿篇数	</td>		
		<td width="15%">
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
			<pg:cell colName="siteName" defaultValue=""/></td>
		<td  >
			<pg:cell colName="channelName" defaultValue=""/>	</td>		
		<td >					
				<pg:cell colName="writeNum" defaultValue=""/>			
		</td>
		<td >					
				<pg:cell colName="auditNum" defaultValue=""/>			
		</td>
		<td >					
				<pg:cell colName="publishNum" defaultValue=""/>			
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