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
<%@ page import="com.frameworkset.platform.comm.common.imp.*"%>
<%	
	AccessControl accesscontroler = AccessControl.getInstance();
	accesscontroler.checkAccess(request, response);
	
	response.setHeader("Cache-Control", "no-cache"); 
	response.setHeader("Pragma", "no-cache"); 
	response.setDateHeader("Expires", -1);  
	response.setDateHeader("max-age", 0);
	
	HdTotal total = new HdTotal();
	
	String maxPageItems = request.getParameter("maxPageItems");
    if(maxPageItems==null || maxPageItems==""){
    	maxPageItems = "20";
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
%>
<html>
<head>
<title>
	CMS:::统计功能
</title>
<link  href="../inc/css/cms.css" rel="stylesheet" type="text/css"></link>
<script language="javascript" src="../inc/js/func.js"></script>
<script language="javascript" src="/sysmanager/include/pager.js" type="text/javascript"></script>
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
<body topmargin="2" rightmargin="0" leftmargin="1" righttmargin="1" scroll="auto">
<form name="form1" action="" method="post" >
<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" bordercolor="#B7CBE4" class="Datalisttable" id="docListTable">
    <pg:listdata dataInfo="HdUserStatisticList" keyName="HdUserStatisticList" />
		                    <!--分页显示开始,分页标签初始化-->
		                    <pg:pager maxPageItems="<%=maxPageItems_int%>" scope="request" data="HdUserStatisticList" isList="false">
		                    <pg:param name="startDate" value="<%=startDate%>"/>
		                    <pg:param name="endDate" value="<%=endDate%>"/>
		                    <pg:param name="maxPageItems" value="<%=maxPageItems%>"/>
		                    <pg:param name="user_name"/>
	<tr>

		<td nowrap class="cms_report_tr" width="100">
			用户名</td>
		<td nowrap class="cms_report_tr" width="100">
			真实姓名	</td>
		<td nowrap class="cms_report_tr">
			当前状态	</td>
		<td nowrap style="color:red" class="cms_report_tr">县长信箱</td><td nowrap>未处理</td><td nowrap>处理中</td><td nowrap>已处理</td><td nowrap>废件</td>
		<td nowrap style="color:red" class="cms_report_tr">效能投诉</td><td nowrap>未处理</td><td nowrap>处理中</td><td nowrap>已处理</td><td nowrap>废件</td>
		<td nowrap style="color:red" class="cms_report_tr">投资咨询</td><td nowrap>未处理</td><td nowrap>处理中</td><td nowrap>已处理</td><td nowrap>废件</td>
		<td nowrap style="color:red" class="cms_report_tr">网上投诉</td><td nowrap>未处理</td><td nowrap>处理中</td><td nowrap>已处理</td><td nowrap>废件</td>
		<td nowrap style="color:red" class="cms_report_tr">申请公开</td><td nowrap>未处理</td><td nowrap>已处理</td>
		<td nowrap style="color:red" class="cms_report_tr">公众留言</td><td nowrap>未处理</td><td nowrap>处理中</td><td nowrap>已处理</td><td nowrap>废件</td>
		<td nowrap style="color:red" class="cms_report_tr">咨询投诉</td><td nowrap>未处理</td><td nowrap>已处理</td>
		<td nowrap style="color:red" class="cms_report_tr">在线办理</td><td nowrap>未处理</td><td nowrap>已处理</td>
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
			<pg:cell colName="userName" defaultValue=""/></td>
		<td  >
			<pg:cell colName="userRealName" defaultValue=""/>	</td>
		<td >
			<pg:equal colName="userStatus" value="0">已删除</pg:equal>
			<pg:equal colName="userStatus" value="1">新申请</pg:equal>
			<pg:equal colName="userStatus" value="2">已注册</pg:equal>
			<pg:equal colName="userStatus" value="3">停用</pg:equal>
		</td>
		<td style="color:red">县长信箱</td>
		<td><%=total.getCountByEmail("县长信箱","0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByEmail("县长信箱","1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByEmail("县长信箱","2",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByEmail("县长信箱","3",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td style="color:red">效能投诉</td>
		<td><%=total.getCountByEmail("效能投诉","0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByEmail("效能投诉","1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByEmail("效能投诉","2",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByEmail("效能投诉","3",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td style="color:red">投资咨询</td>
		<td><%=total.getCountByTzzx("投资咨询","0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByTzzx("投资咨询","1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByTzzx("投资咨询","2",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByTzzx("投资咨询","3",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td style="color:red">网上投诉</td>
		<td><%=total.getCountByZfts("网上投诉","0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByZfts("网上投诉","1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByZfts("网上投诉","2",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByZfts("网上投诉","3",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td style="color:red">申请公开</td>
		<td><%=total.getCountBySqgk("申请公开","0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountBySqgk("申请公开","1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td style="color:red">公众留言</td>
		<td><%=total.getCountByLeavewords("公众留言","0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByLeavewords("公众留言","1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByLeavewords("公众留言","2",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountByLeavewords("公众留言","3",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td style="color:red">咨询投诉</td>
		<td><%=total.getCountBySpZxts("","0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountBySpZxts("","1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td style="color:red">在线办理</td>
		<td><%=total.getCountBySpZxbl("0",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
		<td><%=total.getCountBySpZxbl("1",dataSet.getString("role"),dataSet.getString("org_id"),dataSet.getString("user_id"),startDate,endDate)%></td>
	</tr>
	</pg:list>	
</table>
<table align=center width="100%">
	<tr height="18px" class="labeltable_middle_tr_01">
		<td colspan=100 align='center'> <pg:index/></td>			      	          
	</tr>
</table>
</pg:pager>
</form>
<iframe height="0" width="0" name="operIframe"></iframe>
</body>
</html>