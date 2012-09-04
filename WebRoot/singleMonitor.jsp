<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" import="java.util.List"%>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Enumeration,
				com.frameworkset.common.poolman.util.JDBCPoolMetaData"%>
<%@page import="com.frameworkset.platform.remote.Utils"%>
<%@page import="org.jgroups.blocks.GroupRequest"%>
<%@page import="java.util.Iterator"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>				
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response,false);
	String userAccount = accessControl.getUserAccount();
	
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>poolman连接池使用情况与配置信息</title>
		<link rel="stylesheet" type="text/css" href="sysmanager/css/windows.css">
		<tab:tabConfig/>	
		<script src="../inc/js/func.js"></script>
		</head>

	<body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<tab:tabContainer id="log-bar-containerr">
	<% 
		//List poollist = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		Enumeration enum_ = dbUtil.getAllPoolnames();
		while(enum_.hasMoreElements()){
			String poolname = (String)enum_.nextElement();
			JDBCPoolMetaData metadata = DBUtil.getPool(poolname).getJDBCPoolMetadata();
		String title = "数据库："+poolname+" 的链接情况";
	%>
	<tab:tabPane id="<%=poolname %>" tabTitle="<%=title%>" >
	
	<form  name="LogForm"  method="post">
	<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="1" class="thin">
					<tr>
					<table>
					<tr><td colspan="3">数据库：<%=poolname %> 的链接情况</td>
					</tr>
					<tr class="tr">
						
						<td width="16%" height="25" class="detailtitle" align="right">空闲连接：</td>
						<td height="25">
						<%=DBUtil.getNumIdle(poolname)%>
						</td>
						</tr>
						
						<tr class="tr">
						<td width="16%" height="25" class="detailtitle" align="right">正在使用连接：</td>
						<td height="25" >
						<%=DBUtil.getNumActive(poolname)%>
						</td>
						</tr>
						
						<tr class="tr">
						<td width="16%" height="25" class="detailtitle" align="right">使用连接高峰值：</td>
						<td height="25" >
						<%=DBUtil.getMaxNumActive(poolname)%>
						</td>
						</tr>
					</table>
					
					
						<tr>
						<table border="1">
						<caption>数据库：<%=poolname %>的配置信息</caption>
						<tr>
						<th>配置属性名</th>
						<th>属性对应值</th>
						<th>描述</th>
						</tr>
						
						<tr>
						<td>driver</td>
						<td height="25"><%=metadata.getDriver() %></td>
						<td>数据库驱动</td>
						</tr>
						
						<tr>
						<td>url</td>
						<td height="25">
						<%if(userAccount.equals("admin")){ %><%=metadata.getURL() %><%}else{ %>
						******
						<%} %>
						</td>
						<td>数据库链接地址</td>
						</tr>
						
						<tr>
						<td>username</td>
						<td height="25">
						<%if(userAccount.equals("admin")){ %><%=metadata.getUserName() %>
						<%}else{ %>
						******
						<%} %></td>
						<td>数据库用户名</td>
						</tr>
						
						<tr>
						<td>password</td>
						<td height="25"><%if(userAccount.equals("admin")){ %><%=metadata.getUserName() %><%}else{ %>
						******
						<%} %></td>
						<td>数据库密码</td>
						</tr>
						
						
						<tr>
						<td>loadmetadata</td>
						<td height="25"><%=metadata.getLoadmetadata() %></td>
						<td>是否加载数据库源数据</td>
						</tr>
						
						<tr>
						<td>txIsolationLevel</td>
						<td height="25"><%=metadata.getTxIsolationLevel() %></td>
						<td>事务分离级别</td>
						</tr>
						
						<tr>
						<td>initialConnections</td>
						<td height="25"><%=metadata.getInitialConnections() %></td>
						<td>初始链接数,缺省为1</td>
						</tr>
						
						<tr>
						<td>minimumSize</td>
						<td height="25"><%=metadata.getMinimumSize() %></td>
						<td>最小空闲链接数,缺省为0，根据配置的不同可改为maximumSize的一半，如果maximumSize为200则minimumSize配为100</td>
						</tr>
						
						<tr>
						<td>maximumSize</td>
						<td height="25"><%=metadata.getMaximumSize() %></td>
						<td>允许的最大链接数,缺省值为整数的最大值 </td>
						</tr>
						
						<tr>
						<td>skimmerFrequency</td>
						<td height="25"><%=metadata.getSkimmerFrequency() %></td>
						<td>回收空闲链接操作间隔时间,毫秒（ms）为单位，缺省为4200,000毫秒，也就是4200秒</td>
						</tr>
						
						<tr>
						<td>connectionTimeout</td>
						<td height="25"><%=metadata.getConnectionTimeout() %></td>
						<td>链接最大空闲时间,毫秒为单位，缺省为1200 ,000毫秒，当系统进程回收空闲链接时，超过最大空闲时间的链接将被回收</td>
						</tr>
						
						<tr>
						<td>shrinkBy</td>
						<td height="25"><%=metadata.getShrinkBy() %></td>
						<td>每次回收的链接数,回收进程每次最多回收的空闲链接数，缺省值是5</td>
						</tr>
						
						<tr>
						<td>maxWait</td>
						<td height="25"><%=metadata.getMaxWait() %>&nbsp;微妙</td>
						<td>获取链接等待超时时间,单位：毫秒，缺省等待时间为30000毫秒也就是30秒。当应用程序请求链接时，链接池中所有的链接都处于使用状态，并且已经达到最大连接数时，该请求就会处于等待状态，如果等待的时间超过30秒（maxWait配置的值）时，系统将抛出请求链接超时异常。</td>
						</tr>
						
						<tr>
						<td>validationQuery</td>
						<td height="25"><%=metadata.getValidationQuery() %></td>
						<td>校验sql语句。应用程序从链接池申请链接时，连接池对申请的链接执行校验sql语句，如果执行成功，则标识该链接是有效的，直接返回给应用程序，否则将该连接直接从链接池中销毁，并且重新到池中获取新的链接
						如此反复执行，直接获取到有效的连接为止
						</td>
						</tr>
						</table>
						</tr>
						
						
						<tr>
						<td>
						*********************************************************************************
						</td>
						
						</tr>
						
			
			  </table>
			  </form>
			  
			  </tab:tabPane>
			  <% 
				}	
			%>
	</tab:tabContainer>
	</div>
				</body>
</html>
