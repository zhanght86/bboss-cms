<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">

<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
       	 	<th>部门编码</th> 
       		<th>部门名称</th>
       		<th>操作</th>
       		
       	</pg:header>	

      <pg:list autosort="false" requestKey="datas">
   		<tr>     
   		        <td ><pg:cell colName="orgId"/></td>
                <td><pg:cell colName="orgName"/></td>
                <td><a href="#" onclick="deleteOrg('<pg:cell colName="areaId"/>','<pg:cell colName="orgId"/>')"><span>删除</span></a></td>
                
        </tr>
	 </pg:list>
    </table>
    </div>
</div>		
