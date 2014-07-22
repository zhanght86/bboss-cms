<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">

<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="subscribeList">
        <pg:header>
            <th>id</th>
       	 	<th>英文名称</th> 
       		<th>中文名称</th>
       		<th>待办类型</th>
       		<th style="display:none">valueUsed</th>
       		<th style="display:none">valueSub</th>
       		<th>开关状态</th>
       		<th>订阅状态</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list autosort="false" requestKey="datas" >
   		<tr id="<pg:cell colName="id"/>" >     
   		        <td ><pg:cell colName="id"/></td>
   		        <td><pg:cell colName="nameEN"/></td>
                <td><pg:cell colName="nameCH"/></td>
                <td align="center">
				<pg:equal colName="pendingType" value="1">GW</pg:equal>
				<pg:equal colName="pendingType"  value="2">本地</pg:equal>
				<pg:equal colName="pendingType" value="3">其它库</pg:equal>
				</td>
				<td style="display:none"><pg:cell colName="pendingUsed"/></td>
				<td style="display:none"><pg:cell colName="pendingSubscribe"/></td>
				<td align="center">
				<pg:equal colName="pendingUsed"  value="0">关闭</pg:equal>
				<pg:equal colName="pendingUsed" value="1">开启</pg:equal>
				</td>
				<td align="center">
				<pg:equal colName="pendingSubscribe"  value="0">未订阅</pg:equal>
				<pg:equal colName="pendingSubscribe" value="1">已订阅</pg:equal>
				<pg:empty colName="pendingSubscribe" >未订阅</pg:empty>
				</td>
                <td>
                <a href="#" onclick="confirm(this,'1')" id="<pg:cell colName="id"/>_sub" ><span>订阅</span></a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="#" onclick="confirm(this,'0')" id="<pg:cell colName="id"/>_cancel"><span>取消</span></a>
                </td>
                
        </tr>
	 </pg:list>
    </table>
    </div>
</div>		
