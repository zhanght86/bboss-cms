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
	
	//本周的起始时间
	String start_weekDate;
	java.text.SimpleDateFormat   df=new   java.text.SimpleDateFormat("yyyy-MM-dd");   
    Calendar   calendar=Calendar.getInstance();   
    Calendar   cpcalendar=(Calendar)calendar.clone(); 
        
    Date currentDate = new Date(); 
    /**当前时间*/ 
    String riqi = df.format(currentDate); 
    //得到本周一的时间
    cpcalendar.set(Calendar.DAY_OF_WEEK,   Calendar.MONDAY); 
    start_weekDate = df.format(new Date(cpcalendar.getTimeInMillis()));
    
    String maxPageItems = request.getParameter("maxPageItems");
    if(maxPageItems==null || maxPageItems==""){
    	maxPageItems = "10";
    }
    int maxPageItems_int = Integer.parseInt(maxPageItems);    
	String startDate=request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	
	if(startDate==null){
		startDate=start_weekDate;
	}
	if(endDate==null){
		endDate=riqi;
	}
	//System.out.println(startDate+endDate);
	
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
}
</style>
</head>
<body topmargin="2" rightmargin="0" leftmargin="1" righttmargin="1">
<form name="form1" action="" method="post" >
<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
    <pg:listdata dataInfo="ChannelStatisticList" keyName="ChannelStatisticList"/>
		                    <!--分页显示开始,分页标签初始化-->
		                    <pg:pager maxPageItems="<%=maxPageItems_int%>" scope="request" data="ChannelStatisticList" isList="false">
		                    <pg:param name="startDate" value="<%=startDate%>"/>
		                    <pg:param name="endDate" value="<%=endDate%>"/>
		                    <pg:param name="maxPageItems" value="<%=maxPageItems%>"/>
	<tr class="cms_report_tr">

		<td width="25%">
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
		<td >
			<pg:cell colName="siteName" defaultValue=""/></td>
		<td  >
			<pg:cell colName="channelName" defaultValue=""/>	</td>		
		<td >
			<pg:equalandupper colName="writeNum" value="1">			
<!--				<a href="docStatistic_list.jsp?startDate=<%=startDate%>&endDate=<%=endDate%>&channelId=<pg:cell colName="channelId" defaultValue=""/>&flag=1" title="查看"  target="_self" ><pg:cell colName="writeNum" defaultValue=""/></a>-->
					<a href="#" onClick="openWin('docStatistic_list.jsp?startDate=<%=startDate%>&endDate=<%=endDate%>&channelId=<pg:cell colName="channelId" defaultValue=""/>&flag=1',screen.availWidth-200,screen.availHeight-200)" title="查看"  target="_self" ><pg:cell colName="writeNum" defaultValue=""/>
			</pg:equalandupper>
			<pg:equal colName="writeNum" value="0">			
				<pg:cell colName="writeNum" defaultValue=""/>
			</pg:equal>
		</td>
		<td >
			<pg:equalandupper colName="auditNum" value="1">			
<!--				<a href="docStatistic_list.jsp?startDate=<%=startDate%>&endDate=<%=endDate%>&channelId=<pg:cell colName="channelId" defaultValue=""/>&flag=2" title="查看"  target="_self"><pg:cell colName="auditNum" defaultValue=""/></a>-->
					<a href="#" onClick="openWin('docStatistic_list.jsp?startDate=<%=startDate%>&endDate=<%=endDate%>&channelId=<pg:cell colName="channelId" defaultValue=""/>&flag=2',screen.availWidth-200,screen.availHeight-200)" title="查看"  target="_self"><pg:cell colName="auditNum" defaultValue=""/>
			</pg:equalandupper>
			<pg:equal colName="auditNum" value="0">			
				<pg:cell colName="auditNum" defaultValue=""/>
			</pg:equal>
		</td>
		<td >
			<pg:equalandupper colName="publishNum" value="1">			
<!--				<a href="docStatistic_list.jsp?startDate=<%=startDate%>&endDate=<%=endDate%>&channelId=<pg:cell colName="channelId" defaultValue=""/>&flag=3" title="查看"  target="_self"><pg:cell colName="publishNum" defaultValue=""/></a>-->
				<a href="#" onClick="openWin('docStatistic_list.jsp?startDate=<%=startDate%>&endDate=<%=endDate%>&channelId=<pg:cell colName="channelId" defaultValue=""/>&flag=3',screen.availWidth-200,screen.availHeight-200)" title="查看"  target="_self"><pg:cell colName="publishNum" defaultValue=""/></a>
			</pg:equalandupper>
			<pg:equal colName="publishNum" value="0">			
				<pg:cell colName="publishNum" defaultValue=""/>
			</pg:equal>
		</td>		
		<td >
			<pg:cell colName="totalWords" defaultValue=""/></td>		
	</tr>
	</pg:list>	
</table>
<table align=center width="100%">
	<tr height="18px" class="labeltable_middle_tr_01">
		<td colspan=4 align='center'><pg:index/></td>			      	          
	</tr>
</table>
</pg:pager>
</form>
<iframe height="0" width="0" name="operIframe"></iframe>
</body>
</html>