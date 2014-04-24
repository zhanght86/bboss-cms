<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="wfAppContent">
<pg:equal actual="${appcreateList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${appcreateList.totalSize}"  value="0">
   <pg:pager scope="request"  data="appcreateList" desc="true" isList="false" containerid="wfAppContentPage" selector="wfAppContent">
	
	<pg:param name="system_id"/>
	<pg:param name="system_name"/>
	<pg:param name="app_mode_type"/>
	
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="notesList">
        <pg:header>
            <th>操作</th>
        	<th>应用编号</th>
			<th>应用名称</th>	
			<th>待办URL</th>
			<th>应用URL</th>
			<th>应用类型</th>							
			<th>创建人</th>
			<th>创建时间</th>
			<th>修改人</th>	
			<th>修改时间</th>
       	</pg:header>
  
      	<pg:list autosort="false" >
   			<tr>
   				<td align="right">
   					<input type="radio" name="appInfoRadio" value='<pg:cell colName="id"/>'/>&nbsp;&nbsp;<pg:rowid increament="1" offset="false"/>
   				</td>
   				<td align="center"><pg:cell colName="system_id" defaultValue="" /></td>
				<td align="center"><pg:cell colName="system_name" defaultValue="" /></td>
				<td align="center"><pg:cell colName="todo_url" defaultValue="" /></td>
				<td align="center"><pg:cell colName="app_url" defaultValue="" /></td>
				<td align="center"><pg:cell colName="app_mode_type" defaultValue="" /></td>
				<td align="center"><pg:cell colName="creator" defaultValue="" /></td>
				<td align="center"><pg:cell colName="create_date" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="" /></td>
				<td align="center"><pg:cell colName="update_person" defaultValue="" /></td>
				<td align="center"><pg:cell colName="update_date" dateformat="yyyy-MM-dd HH:mm:ss" defaultValue="" /></td>
        	</tr>
	 	</pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
    </pg:pager>
</pg:notequal>
</div>
