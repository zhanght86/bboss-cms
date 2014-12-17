<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="com.frameworkset.platform.sysmgrcore.entity.User"%>
<%@page import="com.frameworkset.platform.sysmgrcore.manager.db.UserManagerImpl"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>
<%
/*
 * <p>Title: 用户历史任职情况</p>
 * <p>Description: 用户历史任职情况</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: bboss</p>
 * @Date 2008-9-9
 * @author gao.tang
 * @version 1.0
 */
 %>
 
<% 
	AccessControl control = AccessControl.getInstance();
	control.checkManagerAccess(request, response);
	
	String userId = request.getParameter("userId");
	User user = new UserManagerImpl().getUserById(userId);
%>
<html>
<head>
	<base target="_self">
	<title>用户<%out.print(user.getUserName()+"【"+user.getUserRealname()+"】"); %>历史任职情况</title>
	<%@ include file="/include/css.jsp"%>
</head>
<body>
<br/>
<form name="com.frameworkset.goform" method="post">
</form>
	<fieldset>
	<LEGEND align=left><strong>&nbsp;用户<%out.print(user.getUserName()+"【"+user.getUserRealname()+"】"); %>的历史任职情况&nbsp;</strong></LEGEND>
	<table cellspacing="1" cellpadding="0" border="0" bordercolor="#EEEEEE" width=100% class="thin">
		<pg:listdata dataInfo="com.frameworkset.platform.sysmgrcore.web.tag.UserHisJobQuery" keyName="userHisJobQuery" />
		<!--分页显示开始,分页标签初始化-->
		<pg:pager maxPageItems="15" scope="request" data="userHisJobQuery" isList="false">
		
		<tr>
			<td height='20' class="headercolor">历史任职税务机构</td>
			<td height='20' class="headercolor">历史任职岗位</td>
			<td height='20' class="headercolor">任职时间</td>
			<td height='20' class="headercolor">离职时间</td>
		</tr>
		<pg:param name="userId"/>
		<!--检测当前页面是否有记录-->
		<pg:notify>
			<tr height='25' class="labeltable_middle_tr_01">
				<td colspan="100" align='center'>
					没有记录
				</td>
			</tr>
		</pg:notify>
		<!--list标签循环输出每条记录-->
		<pg:list>
			<tr>
				<td  height='20' align="left" class="tablecells">
					<pg:cell colName="orgid" defaultValue="" /> <pg:cell colName="remark5" defaultValue="" />
				</td>
				<td  height='20' align="left" class="tablecells">
					<pg:cell colName="jobid" defaultValue="" /> <pg:cell colName="jobname" defaultValue="" />
				</td>
				<td  height='20' align="left" class="tablecells">
					<pg:cell colName="JOB_STARTTIME" defaultValue="未知时间"  dateformat="yyyy-MM-dd HH:mm:ss" />
				</td>
				<td  height='20' align="left" class="tablecells">
					<pg:cell colName="JOB_QUASHTIME"  defaultValue="未知时间"  dateformat="yyyy-MM-dd HH:mm:ss" />
				</td> 
			</tr>
		</pg:list>
		<tr height="18px" class="labeltable_middle_tr_01">
			<td colspan=4 align='center' nowrap>
				 <pg:index custom="true" />
				<input type="hidden" name="querystring" value="<pg:querystring/>">
			</td>								 
	    </tr>
		</pg:pager>
		</table>
		</fieldset>

</body>
</html>
