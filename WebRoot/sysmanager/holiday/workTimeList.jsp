<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">

<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="workTimeList">
        <pg:header>
            <th style="display:none">id</th>
       	 	<th>名称</th> 
       		<th>开始时间</th>
       		<th>结束时间</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list autosort="false" requestKey="datas" >
   		<tr>     
   		        <td style="display:none"><pg:cell colName="id"/></td>
   		        <td><input type="text"  value = '<pg:cell colName="name"/>'/></td>
                <td><input type="text"  value = '<pg:cell colName="startTime"/>' onclick="WdatePicker({dateFmt:'HH:mm'})"/></td>
                <td><input type="text"  value = '<pg:cell colName="endTime"/>' onclick="WdatePicker({dateFmt:'HH:mm'})"/></td>
                <td><a href="javascript:void(0)" onclick="confirm(this)"><span>保存</span></a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteTime(this)"><span>删除</span></a></td>
                <td></td>
        </tr>
	 </pg:list>
    </table>
    </div>
</div>		
