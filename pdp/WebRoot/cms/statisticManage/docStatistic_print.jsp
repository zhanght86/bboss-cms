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
	
	//当前的时间
	String riqi ;
	
	java.text.SimpleDateFormat   df=new   java.text.SimpleDateFormat("yyyy-MM-dd");   
   
    
    Date currentDate = new Date(); 
    /**当前时间*/ 
    riqi = df.format(currentDate); 
    
    String maxPageItems = request.getParameter("maxPageItems");
    if(maxPageItems==null || maxPageItems==""){
    	maxPageItems = "10";
    }
    int maxPageItems_int = Integer.parseInt(maxPageItems);
	String startDate=request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	if(startDate==null){
		startDate=riqi;
	}
	if(endDate==null){
		endDate=riqi;
	}
	String userId=request.getParameter("userId");
	//flag=1表示查询录稿数，flag=2表示查询审稿数，flag=3表示查询发稿数
	String flag=request.getParameter("flag");
	if(flag==null){
		flag="";
	}	
	if(userId==null){
		userId="";
	}
	//得到频道ID
	String channelId=request.getParameter("channelId");
	if(channelId==null){
		channelId="";
	}
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
<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" bordercolorlight="#000000" class="Datalisttable" id="docListTable">
    <pg:listdata dataInfo="DocStatisticList" keyName="DocStatisticList"/>
		                    <!--分页显示开始,分页标签初始化-->
	<pg:pager maxPageItems="<%=maxPageItems_int%>" scope="request" data="DocStatisticList" isList="true">
		                    <pg:param name="startDate" value="<%=startDate%>"/>
		                    <pg:param name="endDate" value="<%=endDate%>"/>
		                    <pg:param name="userId" value="<%=userId%>"/>
		                    <pg:param name="flag" value="<%=flag%>"/>
		                    <pg:param name="channelId" value="<%=channelId%>"/>
		                    <pg:param name="maxPageItems" value="<%=maxPageItems%>"/>
	<tr id="trTitle" >
		<td valign="absMiddle" align="center" height="10" colspan="9"><h3>文档统计信息打印</h3></td>
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
		<td width="5%">
		</td>
		<td width="25%">
			文档标题</td>
		<td width="8%" >
			文档来源	</td>
		<td width="7%">
			文档状态	</td>
		<td width="10%">
			所属频道	</td>
		<td width="20%">
			 撰写时间	</td>		
		<td width="10%">
			作者	</td>
		<td width="10%">
			录稿人</td>
		<td width="5%">
			字数</td>		
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
			<pg:cell colName="title" defaultValue=""/>
			</td>
		<td  >
			<pg:cell colName="sourceName" defaultValue=""/>	</td>		
		<td >
			<pg:cell colName="status" defaultValue=""/>	</td>
		<td >
			<pg:cell colName="channelName" defaultValue=""/>	</td>		
		<td >
			<pg:cell colName="writeTime" defaultValue=""/>	</td>
		<td >
			<pg:cell colName="author" defaultValue=""/></td>
		<td >
			<pg:cell colName="createuserName" defaultValue=""/></td>
		<td >
			<pg:cell colName="wordsNum" defaultValue=""/></td>		
		
	</tr>
	</pg:list>	
	<%
		//得到新稿数和已发数
		Integer newNum = (Integer)request.getAttribute("newNum");
		Integer pubNum = (Integer)request.getAttribute("pubNum");
		Integer otherNum = (Integer)request.getAttribute("otherNum");
		if(newNum==null){
			newNum=new Integer(0);
		}
		if(pubNum==null){
			pubNum=new Integer(0);	
			//System.out.println("yyf");	
		}
		if((newNum.intValue()!=0) || (pubNum.intValue()!=0)){
			%>
			<tr>
				<td colspan=9>
				总稿数<pg:rowcount/>篇，其中新稿数<%=newNum.toString()%>篇，已发数<%=pubNum.toString()%>篇，其他(包括待审、待发、返工等)<%=otherNum.toString()%>篇
	   			</td>
	   		</tr>
	   	<%
		}
		%>
</table>
</pg:pager>
</form>
</body>
</html>