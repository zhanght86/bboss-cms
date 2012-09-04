<%@ page session="false" contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="com.frameworkset.common.poolman.DBUtil"%>
<%@page import="com.frameworkset.platform.security.AccessControl"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Enumeration,
				com.frameworkset.common.poolman.util.JDBCPoolMetaData"%>
<%@page import="com.frameworkset.platform.remote.Utils"%>
<%@page import="org.jgroups.blocks.GroupRequest"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.jgroups.stack.IpAddress"%>
<%@page import="org.jgroups.util.Rsp"%>
<%@ taglib prefix="tab" uri="/WEB-INF/tabpane-taglib.tld" %>				
<%
	//System.out.println("ip = " + accessControl.getlocalIpAdd());
	
	boolean isCluster = Utils.clusterstarted();
	//System.out.println("是否启用了集群 ： " + isCluster);
	%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>poolman连接池使用情况与配置信息</title>
		<link rel="stylesheet" type="text/css" href="sysmanager/css/windows.css">
		<tab:tabConfig/>	
		<script src="../inc/js/func.js"></script>
		<script type="text/javascript" language="Javascript">
		function flushBotton(){
			document.location = document.location;
		}
		
		window.setInterval("flushButton()", 30000);
		</script>
		</head>

	<body class="contentbodymargin" onload="" scroll="no">
	<div style="width:100%;height:100%;overflow:auto">
	<form  name="LogForm"  method="post">
	<table width="100%" height="" border="0" cellpadding="0" cellspacing="1" class="thin">
	<caption>集群所有服务器数据库链接使用情况表<div align="right"><input type="button" class="input" value="刷新页面" onclick="flushBotton()"></div></caption>
	<tr class="tr">
		
		<th height="25" class="detailtitle">数据库名称</th>
		<th height="25" class="detailtitle">空闲连接</th>
		<th height="25" class="detailtitle">正在使用连接</th>
		<th height="25" class="detailtitle">使用连接高峰值</th>
		<th height="25" class="detailtitle">允许的最大链接数</th>
		<th height="25" class="detailtitle">最小链接数</th>
	</tr>
	<%
	if(Utils.clusterstarted()){
		
		//Iterator iterator = Utils.getAppservers().iterator();
		
		
		//String[] ipPort = new String[vector.size()];
	 	//获取集群中每台服务器的链接池的链接使用情况
	    //Map<ip:port,Map<dbname,Object[idleconnections,usedconnections,maxusedconnections]>>
		Map map = Utils.getDataSourceStatus();
		//服务器ip于端口
		Vector appServers = Utils.getAppservers();
		Set servers = map.entrySet();
		//System.out.println("servers.size() = " + servers.size());
		//Iterator it = servers.iterator();
		int serversCount = 1;
		for(int appC = 0; appC < appServers.size(); appC++){
			IpAddress ipAddress = (IpAddress)appServers.get(appC);
			String ipPort = ipAddress.toString();
			Map objMap = (Map)(((Rsp)map.get(ipAddress)).getValue());
		//}
		//while(it.hasNext())
		//{
		//	Map.Entry entry = (Map.Entry)it.next();
		//	String ipPort = entry.getKey().toString();
		//	Rsp rsp = (Rsp)entry.getValue();
		
		//	Map objMap = (Map)rsp.getValue();
			if(objMap == null){
				out.println("该服务器退出~~~·");
				break;
			}
			Set poolmans = objMap.entrySet();
			Iterator itObjMap = poolmans.iterator();
			%>
			<tr>
			<td colspan="6" height="25" class="detailtitle"><strong>server<%=serversCount++ %>-----<%=ipPort %></strong></td>
			</tr>
			<%
			while(itObjMap.hasNext()){
				Map.Entry entryPool = (Map.Entry)itObjMap.next();
				String poolName = entryPool.getKey().toString();
				Object[] object = (Object[])entryPool.getValue();
			//}	
			//for(int j = 0; j < poolnames.length; j++){
			//	Object[] object = (Object[])objMap.get(poolnames[j]);
				%>

		<tr class="tr">
		<td height="25" class="detailtitle"><%=poolName %></td>
		<td height="25" class="detailtitle"><%=object[0] %></td>
		<td height="25" class="detailtitle"><%=object[1] %></td>
		<td height="25" class="detailtitle"><%=object[2] %></td>
		<td height="25" class="detailtitle"><%=object[3] %></td>
		<td height="25" class="detailtitle"><%=object[4] %></td>
		</tr>
	
				<%
			}
		}
	}
%>
</table>
	</form>
</div>
				</body>
</html>
