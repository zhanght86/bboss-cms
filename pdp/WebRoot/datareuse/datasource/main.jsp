<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@ page import="org.frameworkset.web.servlet.support.RequestContext" %>
<%@page import="org.frameworkset.esb.datareuse.util.*"%>


 <%
 	//状态下拉框
 	request.setAttribute("statusDropList", Constants.BaseDataStatus.values());
 
	//状态下拉框
	request.setAttribute("statusDropList", Constants.BaseDataStatus.values());

	Map statusDropMap = new LinkedHashMap();
	for(Constants.BaseDataStatus status:  Constants.BaseDataStatus.values()){
		statusDropMap.put(status.getValue(), status.getText());
	}
	request.setAttribute("statusDropMap", statusDropMap);
 	
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<title>数据源查询</title>
<%@ include file="/common/jsp/commonCssScript.jsp"%>
<%@ include file="/common/jsp/jqureyEasyui.jsp"%>

<%@ include file="/common/scripts/dialog/dialog.include.jsp"%>

<script type="text/javascript">	
	//停用状态的文字
	var unusedStatusText = "<%=Constants.BaseDataStatus.UNUSED.getText()%>";
	
	//启用状态的文字
	var usedStatusText = "<%=Constants.BaseDataStatus.USED.getText()%>";

	//新增状态 
	var newStatusText = "<%=Constants.BaseDataStatus.NEW.getText()%>";
	
	$(document).ready(function(){
		addInputCss();
		initStatus();
	});

	function closeWindow() {
		JqueryDialog.Close();
	}
	
	function selectCallBack(){
		JqueryDialog.Close();
		var querystring = $("#querystring").val();
		url = "${webAppPath}/datareuse/datasource/main.page?"+querystring;
		window.navigate(url); 
	}
	function add(){		
		//window.location.href = "${webAppPath}/datareuse/datasource/addDatasource.page";	
		var url = "${webAppPath}/datareuse/datasource/add.page";		
		JqueryDialog.Open('新增数据源', url, 800, 522);	

	}
	
	function startDatasource(){
       var cks = $("#ds_list input[name='checkItem']").filter("input:checked");
      
       
	   var ds_name = cks.eq(0).val();
		if (cks.length == 1) {
			var datasourcestatus = $.trim($("#"+ds_name).text());
			
			if(datasourcestatus == usedStatusText){
			  $.messager.alert("提示信息","该数据源已经在启用中！");
			}else{ 
				$.messager.confirm('提示对话框', "确定启用该数据源？", function(r){
					if (r){
						 var url = "${webAppPath}/datareuse/datasource/updatadatasourcestatus.page";
						 $.post(url,{"ds_name":ds_name,"status":"<%=Constants.BaseDataStatus.USED.getValue()%>"},function(result){
							  if(result == "ok"){
								  $.messager.alert("提示信息","该数据源已经成功启用！");
								  $("#"+ds_name).text(usedStatusText);							 
							  }else {
							    	$.messager.alert("提示信息","启动失败，异常如下："+result);
							   }
						});					  
					}
				});			
			}
		}
		else
		{
			$.messager.alert("提示信息","只能选择一个数据源!");
		}
	}
	function stopDatasource(){
	    var cks = $("#ds_list input[name='checkItem']").filter("input:checked");
		var ds_name = cks.eq(0).val();
		
		
		if (cks.length == 1) {
			var datasourcestatus = $.trim($("#"+ds_name).text());
			if(datasourcestatus == newStatusText){
			  $.messager.alert("提示信息","该数据源没有启用！");
			}
			else if(datasourcestatus == unusedStatusText){
			  $.messager.alert("提示信息","该数据源已经在停用中！");
			}else{ 
			
				$.messager.confirm('提示对话框', "确定停止该数据源？", function(r){
					if (r){
						 var url = "${webAppPath}/datareuse/datasource/updatadatasourcestatus.page";
						 $.post(url
							,{"ds_name":ds_name,"status":"<%=Constants.BaseDataStatus.UNUSED.getValue()%>"} ,function(result){
							    if(result == "ok"){
								    $.messager.alert("提示信息","该数据源已经成功停用！");
								    $("#"+ds_name).text(unusedStatusText);								    
							    }else {
							    	$.messager.alert("提示信息","启动失败，异常如下："+result);
							    }
						  });						  
					}
				});			  
			}
		}
		else
		{
			$.messager.alert("提示信息","一次只能选择一个数据源!");
		}
	
	}
	
	function edit(){		
		var cks = $("#ds_list input[name='checkItem']").filter("input:checked");
	
		if (cks.length == 1) {
		    var ds_name = cks.eq(0).val();
		   // var datasourceStatus = $.trim($("#"+ds_name).text());
            
			/**   
		    if(datasourceStatus == unusedStatusText){
		     $.messager.alert("提示信息","停用中的数据源不能修改!");
		     return;
		    }
		    */
			var url = "${webAppPath}/datareuse/datasource/edit.page?ds_name="+encodeURIComponent(cks.eq(0).val());
			JqueryDialog.Open('修改数据源', url, 800, 520);	
		}
		else
		{
			$.messager.alert("提示信息","一次只能选择一个数据源!");
		}
	}	
	
	
	
	function deleteDatasources()
	{   
		var cks = $("#ds_list input[name='checkItem']").filter("input:checked");
		if (cks.length == 1) {
		   var ds_name = cks.eq(0).val();
		   var datasourcestatus = $.trim($("#"+ds_name).text());
		   
		   if(datasourcestatus == usedStatusText){
		     $.messager.alert("提示信息","启用中的数据源不能删除！");
		   }else if(datasourcestatus == newStatusText){
			   $.messager.confirm('提示对话框', "确定删除该数据源？", function(r){
					if (r){
						var url = "${webAppPath}/datareuse/datasource/deleteDatasources.page";
				        $.post(url,{"ds_name":ds_name},function(result){				         
					       if(result == "OK"){					      
							    var querystring = $("#querystring").val();
							    url = "${webAppPath}/datareuse/datasource/main.page?"+querystring;
							    window.navigate(url);
					     	} else {					   
								$.messager.alert("提示信息","无法删除，此数据源已经建立了级联关系!");
						   }				  
				        });	
					}
			   });
					  		   
	       }
		   else {
		      $.messager.alert("提示信息","停用中的数据源不能删除！");
		   }
		}
		else if(cks.length<1)
		{
			$.messager.alert('提示信息','请选择一个数据源!');			
		}
		else{
		   $.messager.alert('提示信息','一次只能选择一个数据源!');	
		}
	}
	
	function checkboxAllClick(check){
		var checkItems = document.getElementsByName('checkItem');		
		for(i=0; i<checkItems.length; i++){
			checkItems[i].checked = check;
		}
	}
	
	function clearForm(){		
		$("#jdbc_url").val('');
		$("#ds_name").val('');
		$("#status").val('');
	}
	
	function initStatus(){
		$("#status").val("${status}");	 
	}
</script>

</head>


<body>
<table>
	<tr>
		<td valign="top">
		<table>
			<tr>
				<td valign="top">
				<table>
					<tr>
						<td>
						
						<form name="queryForm" action="main.page" method="post">
						<table class="Ctable">
							<tr>
								<td colspan="4" class="taobox">数据源查询</td>
							</tr>
							<tr>
								<td width="15%" class="c2">数据源名称：</td>
								<td width="25%"><input id="ds_name" class="input_default"
									name="ds_name" type="text"
									value='<common:request parameter="ds_name"/>' /></td>
								<td width="15%" class="c2">JDBC连接串：</td>
								<td width="25%"><input id="jdbc_url" class="input_default"
									name="jdbc_url" type="text"
									value='<common:request parameter="jdbc_url"/>' />									
								</td>
							</tr>
							<tr>
							    <td width="15%" class="c2">数据源状态：</td>
								<td width="25%">
								 <select id="status" name="status" style="width:75%;">									
								 	<option value="">-请选择-</option>
									 <pg:list requestKey="statusDropList">
										<option value="<pg:cell colName="value" defaultValue=""/>">
											<pg:cell colName="text" defaultValue=""/>
										</option>
									</pg:list>
								 </select>									
								</td>
								<td></td>
								<td></td>
							</tr>
							
							
							<tr>
								<td align="center" colspan="4">
								<button type="submit" class="button">查询</button>
		
								<button type="button" class="button" onclick="clearForm();">清空</button>
								</td>
							</tr>							
						</table>
						</form>
						</td>
					</tr>					
					<tr>
						<td colspan="5" align="right">
							<input type="button" class="addbutton1" value="添加" onclick="add();" />
							<input type="button" class="editbutton1" value="修改" onclick="edit()" />
							<input type="button" class="deletebutton1" value="删除"  onclick="deleteDatasources()"/>
							<input type="button" class="startbutton" value="启用" onclick="startDatasource()" />
							<input type="button" class="stopbutton" value="停用" onclick="stopDatasource()" />
						</td>
					</tr>
					<div id="deletedialog" style="display:none"  style="padding:5px;width:300px;height:100px;">此操作不可回复，确定删除此数据源？</div>
					<tr>
						<td>
					
					<!--分页显示开始,分页标签初始化-->
					<pg:pager scope="request" data="pagedata" 
						  isList="false">
						<pg:param name="ds_name"/>
						<pg:param name="jdbc_url"/>						
						<pg:param name="status"/>						
									
						<form name="listForm" method="post">
						<table id="ds_list" class="Ctable">
							<tr>
								<td width="2%" class="c3"><input type="checkbox" name="checkbox" onclick="checkboxAllClick(this.checked)" 
									value="checkbox" /></td>
								<td class="c3" width="10%">数据源名称</td>
								<td class="c3" width="10%">连接方式</td>
								<td class="c3" width="10%">数据库类型</td>
								<td class="c3" width="10%">JNDI名称</td>															
								<td class="c3" width="10%">状态</td>															
								<td class="c3" width="20%">JDBC连接串</td>					
								<td width="15%" class="c3">备注</td>
							</tr>
						<pg:list>
							<tr height="18px" class="cms_data_tr"
								onmouseover="this.style.background='#E9EEF4'"
								onMouseOut="this.style.background=''">
								<td>
									<input type="checkbox" name="checkItem" value='<pg:cell colName="ds_name" />'/>
								</td>
								<td>
									<pg:cell colName="ds_name" defaultValue=""/>
								</td>
								<td nowrap="nowrap">
								<pg:equal colName="connection_type" value="custom_jdbc" >自定义JDBC连接串</pg:equal>
								<pg:equal colName="connection_type" value="externaljndi">外部JNDI数据源</pg:equal>
								</td>
								<td><pg:cell colName="db_type" defaultValue=""/></td>
								<td><pg:cell colName="jndi_name" defaultValue=""/></td>
								<td >
									<span id="<pg:cell colName="ds_name" />">
										<pg:convert convertData="statusDropMap" colName="status" defaultValue=""/>
									</span>
								</td>
								<td><pg:cell colName="jdbc_url" defaultValue=""/></td>							
								<td><pg:cell colName="remark" defaultValue="" maxlength="50"/></td>								
							</tr>							
						</pg:list>
							<tr>
								<td height="20" class="pagefoot"  colspan="7" align="center" class="pagefoot">						
									<pg:index/>		
									<input id="querystring" type="hidden" value='<pg:querystring/>'/>					
								</td>
							</tr>
						</table>
						</form>
					</pg:pager>
										
						</td>
					</tr>					
				</table>
				<table>
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
