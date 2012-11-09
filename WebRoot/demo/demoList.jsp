<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：台账列表信息设置
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="customContent">
<div class="nodata">${errormsg}</div>
<pg:equal actual="${demos.totalSize}" value="0" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:equal> 
<pg:notequal actual="${demos.totalSize}"  value="0">
   <pg:pager scope="request"  data="demos" desc="false" isList="false" containerid="custombackContainer" selector="customContent">
	<pg:param name="name"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor" style="overflow: auto;">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th align=center><input id="CKA" name="CKA" type="checkbox" 
							onClick="checkAll('CKA','CK')"></th>
			<th>ID</th>
       		<pg:title sort="true" type="th" align="center" colName="name" title="名称"/>
       		<th>操作</th>
       	</pg:header>	
      <pg:list>

   		<tr onDblClick="viewDemo('<pg:cell colName="id" />')">
   		        <td class="td_center">
                    <input id="CK" type="checkbox" name="CK" onClick="checkOne('CKA','CK')" value="<pg:cell colName="id" />"/>
                 <td><pg:cell colName="id" /></td>  
                <td><pg:cell colName="name"/></td>        		
                <td class="td_center"><a href="javascript:void(0)" id="editDemo" onclick="editDemo('<pg:cell colName="id" />')">编辑</a></td>    
                 
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="5,10,20,50,100"/></div>

    </pg:pager>
    </pg:notequal>
</div>		
