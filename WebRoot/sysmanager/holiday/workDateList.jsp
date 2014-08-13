<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">

<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
        <th style="display:none">ID</th>
        <th style="display:none">区域Id</th>
       		<th>名称</th>
       		<th>描述</th>
       		<th>起止时间</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list autosort="false" requestKey="datas">
   		<tr>     
   		        <td  style="display:none"><pg:cell colName="id"/></td>
   		        <td  style="display:none"><pg:cell colName="areaId"/></td>
   		        <td ><pg:cell colName="name"/></td>
                <td><pg:cell colName="periodDesc"/></td>
                <td><pg:cell colName="startDate"/>&nbsp;&nbsp;至&nbsp;&nbsp;<pg:cell colName="endDate"/></td>
                <td><a href="#" onclick="editWorkDate('<pg:cell colName="id"/>')"><span>编辑工作日期</span></a>
                <%-- |&nbsp;&nbsp;<a href="#" onclick="editWorkTime('<pg:cell colName="id"/>')"><span>编辑工作时间</span></a> --%>
                </td>
        </tr>
	 </pg:list>
    </table>
    </div>
</div>		
