<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<div id="customContent">
<pg:equal actual="${businessTypeList.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${businessTypeList.totalSize}"  value="0">
   <pg:pager scope="request"  data="businessTypeList" desc="true" isList="false" containerid="custombackContainer" selector="customContent">
	
	<pg:param name="businessId"/>
	<pg:param name="businessCode"/>
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
			
       		<th>业务类别码</th>
       		<th>业务类别名</th>
       		<th>父节点</th>
       		<th>状态</th>
       		<th>备注</th>
       		<th>操作</th>
       	</pg:header>	

      <pg:list autosort="false">
   		<tr>
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="businessId" />"/>
                    <input id="id" type="hidden" name="id" value="<pg:cell colName="businessId" />"/></td>
                <td><pg:cell colName="businessCode"/></td>
                <td><pg:cell colName="businessName"/></td> 
                <td><pg:cell colName="parent_name"/></td>
                <td>
                	<pg:equal colName="useFlag" value="0">失效</pg:equal>
                	<pg:equal colName="useFlag" value="1">启用</pg:equal>
                </td>
                <td><pg:cell colName="remark"/></td>
                <td class="td_center">
                	<a href="javascript:void(0)" id="del" onclick="javascript:del('<pg:cell colName="businessId" />')">删除</a>
                	<a href="javascript:void(0)" id="del" onclick="javascript:modify('<pg:cell colName="businessId" />')">修改</a>
                </td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal >
</div>		
