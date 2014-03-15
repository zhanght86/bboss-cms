<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@page import="org.frameworkset.esb.datareuse.util.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<title>数据源查询</title>
<%@ include file="/common/jsp/commonCssScript.jsp"%>
<%@ include file="/common/jsp/jqureyEasyui.jsp"%>
<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>
<!-- 将js放到独立文件 -->	
<script type="text/javascript"	src="datasource.js"></script>

<style type="text/css">
	input {
		width:250px;
	}
	select
	{
		width: 252px;
	}
</style>
<script type="text/javascript">
	
	$(document).ready(function(){
		<%
			if(request.getParameter("close")!=null)
			{	
		%>
			parent.selectCallBack();
		<%
			}else{
		%>
			//加载公用初始化js
			commonInit();	
			bindFormData();
			changedConnectionType();			
		<%
			}
		%>	
	});
	
	function selectCallBack(param_table_name, object_type){		
		$("#param_table_name").val(param_table_name);
		$("#object_type").val(object_type);		
		JqueryDialog.Close();
	}
	
	

	function closeSelf(){
		parent.closeWindow();
	}

	
	function bindFormData(){
		<pg:beaninfo requestKey="obj">
		
		$("#ds_name").val('<pg:cell colName="ds_name"/>');
		$("#connection_type").val('<pg:cell colName="connection_type"/>');
		$("#db_type").val('<pg:cell colName="db_type"/>');
		$("#driver").val('<pg:cell colName="driver"/>');
		$("#initial_connections").val('<pg:cell colName="initial_connections"/>');
		$("#jdbc_url").val('<pg:cell colName="jdbc_url"/>');
		$("#jndi_name").val('<pg:cell colName="jndi_name"/>');
		$("#ext_jndi_name").val('<pg:cell colName="ext_jndi_name"/>');
		$("#maximum_size").val('<pg:cell colName="maximum_size"/>');
		$("#minimum_size").val('<pg:cell colName="minimum_size"/>');
		$("#password").val('<pg:cell colName="password"/>');
		$("#remark").val('<pg:cell colName="remark"/>');
		$("#show_sql").val('<pg:cell colName="show_sql"/>');

		$("#use_pool").val('<pg:cell colName="use_pool"/>');
		$("#username").val('<pg:cell colName="username"/>');
		$("#validation_query").val('<pg:cell colName="validation_query"/>');

		if ('<pg:cell colName="status"/>' != '<%=Constants.BaseDataStatus.NEW.getValue()%>'){
			$(".workStatus").show();
			
			$("#workStatus").html('<pg:cell colName="workStatus"/>');
			$("#numactive").html('<pg:cell colName="numactive"/>');
			$("#numidle").html('<pg:cell colName="numidle"/>');
			$("#maxnumactive").html('<pg:cell colName="maxnumactive"/>');

			$(".workStatus span:contains('-1')").html('外部数据源，无法监测');

			if ('<pg:cell colName="status"/>' == '<%=Constants.BaseDataStatus.USED.getValue()%>'){
				if ('<pg:cell colName="workStatus"/>' == 'start'){
					$("#stopButton").show();
					$("#updateButton").hide();
				}
				else {
					$("#stopButton").hide();
					$("#updateButton").show();
				}
			}
			else {
				$("#stopButton").hide();
				$("#updateButton").hide();
			}			
		}
		else {
			$(".workStatus").hide();
		}

		</pg:beaninfo>

		
	}
	
</script>

</head>


<body>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td valign="top">
		<table width="98%" border="0" align="left" cellpadding="10"
			cellspacing="0">
			<tr>
				<td valign="top">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" id= "valid_tb">
					<tr>
						<td>
					<form action="update.page" id="theForm" method="post">					
						<table width="100%" border="0" cellpadding="0" cellspacing="0"
							class="Ctable">
							<tr>
								<td colspan="4" class="taobox">数据源修改</td>
							</tr>
							
							<tr>															
								<td nowrap="nowrap" class="c2">数据源名称：</td>
								<td><input style="width: 100%" readonly="true" id="ds_name" name="ds_name" type="text" />									
								</td>
								<td class="c2" id="_jndi_name">JNDI名称：</td>
								<td id="td_jndi_name">
									<input class="input_default" style="width: 100%" 
										id="jndi_name" name="jndi_name" type="text"
										/>	
								</td>								
										
							</tr>
							
							<tr >
								<td class="c2">连接方式：</td>								
								<td>
									<select id="connection_type" name="connection_type" onchange="changedConnectionType();" style="width: 100%">
										<option value="custom_jdbc">自定义JDBC连接串</option>
										<option value="externaljndi">外部JNDI数据源</option>
									</select>
								</td>
								<td class="c2" nowrap="nowrap" id="td_type">数据库类型：</td>
								<td id="db_type_td">
									<select id="db_type" name="db_type" onchange="db_type_cg()" style="width: 100%">
										<option value="oracle">Oracle</option>
										<option value="sqlserver">SQL Server</option>
										<option value="db2">IBM DB2</option>
										<option value="mysql">MySQL</option>
									</select>
								</td>	
								
							</tr>
							
							<tr>
							  <td class="c2" nowrap="nowrap" style="display:none" id="_ext_jndi_name" >外部JNDI名称：</td>
								<td id="td_ext_jndi_name" style="display:none">
									<input class="input_default easyui-validatebox" style="width: 100%" 
										id="ext_jndi_name" name="ext_jndi_name" type="text"
										validType="Caracters" required="true" missingMessage="此项必填!"
										/>	
								</td>
							</tr>
							
							<tr class="custom_jdbc_content">
								<td class="c2">数据库驱动：</td>				
								<td colspan="3">
									<input class="input_default" style="width: 100%;"
										id="driver" name="driver" type="text"
										/>	
								</td>
							</tr>
							<tr class="custom_jdbc_content">
								<td class="c2">JDBC连接串：</td>							
								<td colspan="3">
									<input class="input_default" style="width: 100%;"
										id="jdbc_url" name="jdbc_url" type="text"
										/>	
								</td>
							</tr>
							
							<tr class="custom_jdbc_content">
								<td class="c2">用户名：</td>								
								<td>
									<input class="input_default" style="width: 100%"
										id="username" name="username" type="text"
										/>	
								</td>
								
								<td class="c2">密码：</td>
								<td>
									<input class="input_default" style="width: 100%"
										id="password" name="password" type="password"
										/>	
								</td>
							</tr>
							
							<tr class="custom_jdbc_content">
								<td class="c2">使用连接池：</td>								
								<td>
									<select id="use_pool" name="use_pool" onchange="use_pool_cg()">
										<option value="1">是</option>		
										<option value="0">否</option>								
									</select>
								</td>
								
								<td class="c2">初始连接数：</td>
								<td>
									<input class="input_default" style="width: 100%"
										id="initial_connections" name="initial_connections" type="text"
										 />	
								</td>
							</tr>
							
							<tr class="custom_jdbc_content">
								<td class="c2">最小连接数：</td>								
								<td>
									<input class="input_default" style="width: 100%"
											id="minimum_size" name="minimum_size" type="text"
											/>										
								</td>
								
								<td class="c2" nowrap="nowrap">最大连接数：</td>
								<td>
									<input class="input_default" style="width: 100%"
											id="maximum_size" name="maximum_size" type="text"
											/>	
								</td>
							</tr>
							
							<tr class="">
								<td class="c2" nowrap="nowrap">打印SQL调试信息：</td>								
								<td>
									<select id="show_sql" name="show_sql" >
										<option value="0">否</option>
										<option value="1">是</option>										
									</select>									
								</td>
								
								<td class="c2">只读模式：</td>
								<td>
									<select id="read_only" name="read_only">										
										<option value="1">是</option>
										<option value="0">否</option>										
									</select>	
								</td>
							</tr>
							
							<tr class="">
								<td class="c2">连接测试SQL：</td>								
								<td colspan="3">
									<textarea name="validation_query" id="validation_query" style="width:100%"></textarea>								
								</td>								
							</tr>
							<tr class="">
								<td class="c2">备注：</td>								
								<td colspan="3">
									<textarea  name="remark" id="remark" style="width:100%"></textarea>						
								</td>								
							</tr>
							
							<tr class="workStatus">
								<td class="c2">工作状态</td>								
								<td>
									<span id="workStatus" style="padding-right:10px;"></span>		
									<input type="button" id="stopButton" style="width:60px;" value="停止运行" class="button_1" onclick="stopDynamicDatasource()"  title="先停止数据源运行，才可保存数据源信息">		
								</td>
								
								<td class="c2" nowrap="nowrap">当前活动连接数:</td>
								<td>
									<span id="numactive"></span>		
								</td>
							</tr>
							<tr class="workStatus">
								<td class="c2">空闲连接数:</td>								
								<td>
									<span id="numidle"></span>								
								</td>
								
								<td class="c2" nowrap="nowrap">活动高峰值:</td>
								<td>
									<span id="maxnumactive"></span>		
								</td>
							</tr>
							
							<tr>
								<td align="center" colspan="4">
									
									<button type="button" class="button"  onclick="save();" id="updateButton">保存</button>
									
									<input type="button"  class="button" value="测试" onclick="test()"/>
												
									<button type="button" class="button" onclick="closeSelf();">取消</button>
								</td>
							</tr>
						</table>	
						</form>
										
						</td>
					</tr>					
				
				</table>
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</body>
</html>
