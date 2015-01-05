<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<div id="customContent">
<pg:equal actual="${datas.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${datas.totalSize}"  value="0">
   <pg:pager scope="request"  data="datas" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align="center" style="display:none">&nbsp;&nbsp;</th>
       		<th style="display:none">区域编码</th>
       		<th>区域名称</th>
       		<th>区域描述</th>
       		<th>默认区域</th>
       		<th>创建时间</th>
       		<th>创建人</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list >
   		<tr>
	        <td class="td_center" style="display:none">
                <input type="radio" name="areaInfoRadio" value='<pg:cell colName="areaId"/>'/>&nbsp;&nbsp;<pg:rowid increament="1" offset="false"/>
            </td> 
            <td style="display:none"><pg:cell colName="areaId" /></td>
    		<td><pg:cell colName="areaName" /></td>
    		<td><pg:cell colName="areaDesc" /></td>
    		<td>
    		    <pg:equal colName="areaDefault" value="0">非默认区域</pg:equal>
				<pg:equal colName="areaDefault"  value="1">默认区域</pg:equal>
    		</td>
    		<td><pg:cell colName="createTime" dateformat="yyyy-MM-dd HH:mm:ss" /></td>
    		<td><pg:cell colName="creator"  /></td>
    		
       		<td>
       			<a href="javascript:void" onclick="editArea('<pg:cell colName="areaId"/>','<pg:cell colName="areaDefault"/>');">区域信息</a>&nbsp;&nbsp;|&nbsp;&nbsp;
       			<a href="javascript:void" onclick="editOrgList('<pg:cell colName="areaId"/>','<pg:cell colName="areaName" />');">所辖部门</a>&nbsp;&nbsp;|&nbsp;&nbsp;
       			<a href="javascript:void" onclick="editHoliday('<pg:cell colName="areaId"/>','<pg:cell colName="areaName" />');">假日安排</a>&nbsp;&nbsp;|&nbsp;&nbsp;
       			<a href="javascript:void" onclick="queryWorkDate('<pg:cell colName="areaId"/>','<pg:cell colName="areaName" />');">工作时间设置</a>
       		</td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal >

</div>		
