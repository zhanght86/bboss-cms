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
	String url = "docStatistic_print.jsp?startDate="+startDate+"&endDate="+endDate;
	String userId=request.getParameter("userId");
	//flag=1表示查询录稿数，flag=2表示查询审稿数，flag=3表示查询发稿数
	String flag=request.getParameter("flag");
	if(flag==null){
		flag="";
	}	
	else{
		url = url+"&flag="+flag;
	}
	if(userId==null){
		userId="";
	}
	else{
		url=url+"&userId="+userId;
	}
	//得到频道ID
	String channelId=request.getParameter("channelId");
	if(channelId==null){
		channelId="";
	}
	else{
		url=url+"&channelId="+channelId;
	}
	url=url+"&maxPageItems="+maxPageItems_int;
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
function doprint(){
		form1.action="<%=url%>";
		form1.target="_blank";
		form1.submit();
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
}
</style>
</head>
<body topmargin="2" rightmargin="0" leftmargin="1" righttmargin="1">
<base target="_self">
<form name="form1" action="" method="post" >
<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
<!--   如果是从频道列表或用户列表打开此页面，则显示打印按扭-->
    <%if(!userId.equals("") || !channelId.equals("")){
    %>
    	<tr>
    		<td colspan=9 align=right><input name="print" type="button" class="cms_button" value="打印" onClick="doprint()"></td>
    	</tr>
    <%
      }
    %>
<!--    ************************************************-->

    <pg:listdata dataInfo="DocStatisticList" keyName="DocStatisticList"/>
		                    <!--分页显示开始,分页标签初始化-->
    <pg:pager maxPageItems="<%=maxPageItems_int%>" scope="request" data="DocStatisticList" isList="false" sortKey="status" desc="true">
		                    <pg:param name="startDate" value="<%=startDate%>"/>
		                    <pg:param name="endDate" value="<%=endDate%>"/>
		                    <pg:param name="userId" value="<%=userId%>"/>
		                    <pg:param name="flag" value="<%=flag%>"/>
		                    <pg:param name="channelId" value="<%=channelId%>"/>
                            <pg:param name="maxPageItems" value="<%=maxPageItems%>"/>
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
	}
	if(otherNum==null){
		otherNum=new Integer(0);		
	}
	if((newNum.intValue()!=0) || (pubNum.intValue()!=0) || (otherNum.intValue()!=0)){
		%>
		<tr>
			<td colspan=9>
				总稿<pg:rowcount/>篇，其中新稿<%=newNum.toString()%>篇，已发<%=pubNum.toString()%>篇，其他(包括待审、待发、返工等)<%=otherNum.toString()%>篇
			</td>
		</tr>
		<%
	}
	%>
	
	<tr class="cms_report_tr">

		<td width="25%">
			文档标题</td>
		<td width="8%" >
			文档来源	</td>
		<td width="7%">
			文档状态	</td>
		<td width="10%">
			所属频道	</td>
		<td width="25%">
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
		<td colspan=100 align='center'>没有找到记录</td>
	</tr>
	</pg:notify>			      
			      			    
	<!--list标签循环输出每条记录-->			      
	<pg:list>
	<tr>
		<td >
			<a href="javascript:void"  onclick="openWin('doc_view.jsp?docid=<pg:cell colName="documentId" defaultValue=""/>&channelName=<pg:cell colName="channelName" defaultValue=""/>&channelId=<pg:cell colName="channelId" defaultValue=""/>',screen.availWidth-100,screen.availHeight-100)" title="浏览"  target="_self"><pg:cell colName="title" defaultValue=""/></a>
			</td>
		<td  >
			<pg:cell colName="sourceName" defaultValue=""/>	</td>		
		<td >
			<pg:cell colName="status" defaultValue=""/>	</td>
		<td >
			<pg:cell colName="channelName" defaultValue=""/>	</td>		
		<td >
			<pg:cell colName="writeTime" defaultValue="" dateformat="yyyy-MM-dd hh:mm:ss"/>	</td>
		<td >
			<pg:cell colName="author" defaultValue=""/></td>
		<td >
			<pg:cell colName="createuserName" defaultValue=""/></td>
		<td >
			<pg:cell colName="wordsNum" defaultValue=""/></td>		
		
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