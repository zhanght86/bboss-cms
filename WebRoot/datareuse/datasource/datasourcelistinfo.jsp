<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/jsp/importAndTaglib.jsp"%>
<%@ include file="/common/jsp/accessControl.jsp"%>
<%@page import="org.frameworkset.esb.datareuse.util.*"%>
<div id="datasourceInfo"> 
				<table>
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
						  isList="false" containerid="datasourceInfoContainer" selector="datasourceInfo">
						<pg:param name="ds_name"/>
						<pg:param name="jdbc_url"/>						
									
						<form name="listForm" method="post">
						<table id="ds_list" class="Ctable">
							<tr>
								<td width="2%" class="c3"><input type="checkbox" name="checkbox"
											onclick="checkAll('checkBoxAll','checkItem');" value="checkbox" /></td>
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
							    	<input type="checkbox" name="checkItem" onClick="checkOne('checkBoxAll', this.name);"  
											value='<pg:cell colName="ds_name" />' />
								</td>
								<td>
									<pg:cell colName="ds_name" defaultValue=""/>
								</td>
								<td><pg:cell colName="connection_type" defaultValue=""/></td>
								<td><pg:cell colName="db_type" defaultValue=""/></td>
								<td><pg:cell colName="jndi_name" defaultValue=""/></td>
								<td ><div id="<pg:cell colName="ds_name" />"><pg:cell colName="status" defaultValue=""/></div></td>
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
</div>