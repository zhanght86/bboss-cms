<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
	
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title></title>
		<%@ include file="/common/jsp/commonCssScript.jsp"%>				
		
		<style>
.list_outside_border {
	background-color: #999999;
	margin-top: 4px;
}

.title_td {
	background-color: #cfcfcf;
	height: 25px;
	text-align: center;
}

.content_td {
	background-color: #ffffff;
	height: 22px;
	text-align: center;
}

.STYLE1 {
	color: #FF0000
}

a:link {
	color: #FF0000;
	text-decoration: none;
}

a:visited {
	text-decoration: none;
	color: #FF0000;
}

a:hover {
	text-decoration: none;
	color: #FF0000;
}

a:active {
	text-decoration: none;
	color: #FF0000;
}

.stepLink {
	float:left;
	padding-left: 20px;
	cursor:hand;
}

.stepLink div{	
	padding-bottom: 5px;
	text-align:center;
}



</style>
	<script type="">
		var divwindow = window.parent;
		
		function showInTab(id, url, name){			
			divwindow.addTab(name, url);
		}

function showmessage(){			
			alert("你没有操作此功能的权限");
		}

		
	</script>
	
	</head>
	<body>
		<table width="95%" height="379" border="0" cellpadding="0"
			cellspacing="0">
			<tr>
				<td width="8" height="25">
					<img src="images/box/box_top_left.gif" />
				</td>
				<td height="25" background="images/box/box_top.gif">
					<img src="images/individual.gif" width="15" height="12" />
					欢迎使用
				</td>
				<td width="8" height="25">
					<img src="images/box/box_top_right.gif" />
				</td>
			</tr>
			<tr>
				<td background="images/box/box_left_mid.gif"></td>
				<td valign="top" height="100%">
				
			<table cellpadding="8">				
			
					<tr><td>
						<br />
						&nbsp;
						<br />
						&nbsp; 数据复用服务平台可以帮助系统管理员方便、快速的定义符合公安共享信息服务规范的web service服务接口。
					</td></tr>
					<tr><td>
						&nbsp;
						<img src="images/jf_07.jpg" width="16" height="16" />
						小提示：
						<br />
						&nbsp; 您可以通过
						<span class="STYLE1"><a href="datareuse/serviceintefacedes/serviceinterfacedes.jsp" target="_blank">服务接口描述</a>
						</span>功能查看具体的web service接口信息。
					</td></tr>
					<tr><td>
						&nbsp; 使用简介：复用服务接口的定义与发布需要以下4个步骤
					</td></tr>
					
					<tr><td>
					<%if(accesscontroler.checkPermission("datasourcMg","visible","column")) {%>
					
						<div class="stepLink"  onclick="showInTab('datasourcMg','${webAppPath}/datareuse/datasource/main.page', '数据源管理')">
							<div>
								<img
								src="images/left_menu/datasource.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>数据源管理</span>
							</div>
						</div>	
					<% } else{%>	
					<div class="stepLink"  onclick="showmessage()">
							<div>
								<img
								src="images/left_menu/datasource.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>数据源管理</span>
							</div>
						</div>	
					<%
					}
					%>	
						<div class="stepLink" ><img src="images/misc_arrow.gif" width="21" height="70" align="bottom"/></div>
		
						<%if(accesscontroler.checkPermission("codesetMg","visible","column")) {%>
						<div class="stepLink" onclick="showInTab('datasourcMg','${webAppPath}/datareuse/codeset/main.page', '数据对象管理')">
							<div>
								<img
								src="images/left_menu/codeset.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>数据对象管理</span>
							</div>
						</div>	
						<% } else{%>	
					<div class="stepLink"  onclick="showmessage()">
							<div>
								<img
								src="images/left_menu/codeset.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>数据对象管理</span>
							</div>
						</div>	
					<%
					}
					%>			
						
						<div class="stepLink" ><img src="images/misc_arrow.gif" width="21" height="70" align="bottom"/></div>
						
							<%if(accesscontroler.checkPermission("serviceReuseMg","visible","column")) {%>
						<div class="stepLink"  onclick="showInTab('datasourcMg','${webAppPath}/datareuse/servicereuse/main.page', '复用服务管理')">
							<div  style="text-align:center">
								<img
								src="images/left_menu/yingshe.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>映射成复用服务</span>
							</div>
						</div>
						<% } else{%>	
					<div class="stepLink"  onclick="showmessage()">
							<div  style="text-align:center">
								<img
								src="images/left_menu/yingshe.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>映射成复用服务</span>
							</div>
						</div>	
					<%
					}
					%>					
						
						<div class="stepLink" ><img src="images/misc_arrow.gif" width="21" height="70" align="bottom"/></div>
						<%if(accesscontroler.checkPermission("serviceMg","visible","column")) {%>
						<div class="stepLink"  onclick="showInTab('datasourcMg','${webAppPath}/uddi/servicemanage/main.page', '复用管理')">
							<div  style="text-align:center">
								<img
								src="images/left_menu/shenhe.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>服务审核发布</span>
							</div>
						</div>	
						<% } else{%>	
					<div class="stepLink"  onclick="showmessage()">
							<div  style="text-align:center">
								<img
								src="images/left_menu/shenhe.jpg" width="60" height="60"
								border="0"/>								
							</div>
							<div style="text-align:center">
								<span>服务审核发布</span>
							</div>
						</div>	
					<%
					}
					%>								
					</td></tr>
					
					
					<tr style="display:none"><td>
						&nbsp; 当以上步骤设置完毕以后，点击
						<span class="STYLE1"><a href="configRefresh.jsp">配置重新载入</a>
						</span>刷新系统控制表，即完成复用服务web service接口配置。
					</td></tr>
					<tr><td>
						&nbsp;
						<img src="images/jf_07.jpg" width="16" height="16" />
						小提示：
						<br />
						&nbsp; 您可以在
						<a href="datareuse/datamultiplexquery/query.jsp" target="_blank">服务测试</a>中测试配置的web
						service接口信息。（了解详情，请查看系统使用帮助）
						<br />
						<br />
					</td></tr>
				</table>
				</td>
				<td background="images/box/box_right_mid.gif"></td>
			</tr>
			<tr>
				<td width="8" height="8">
					<img src="images/box/box_left.gif" />
				</td>
				<td height="8" background="images/box/box_bottom.gif">
					<img src="images/box/box_bottom.gif" width="9" height="8" />
				</td>
				<td width="8" height="8">
					<img src="images/box/box_right.gif" />
				</td>
			</tr>
		</table>
	</body>
</html>


