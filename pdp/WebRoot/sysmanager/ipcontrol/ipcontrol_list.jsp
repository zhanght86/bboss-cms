<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：IP限制列表
作者：侯婷婷
版本：1.0
日期：2014-03-17
 --%>	

<div id="customContent">
<pg:empty actual="${datas}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${datas}">
   <pg:pager scope="request"  data="datas" desc="false" isList="false" containerid="custombackContainer" selector="customContent">
		<pg:param name="controluser" />
		<pg:param name="filtertype" />
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
			<div id="changeColor">		
					<table width="100%" border="0" cellpadding="0" cellspacing="0"
						class="stable" id="tb">
						<pg:header>
						 <th>
								<input type="checkBox" name="checkBoxAll"
									onClick="checkAll('checkBoxAll','id')" />
							</th>
							<th width="20%">
								用户登录名
							</th>
							<th width="30%">
								IP范围
							</th>
							<th width="10%">
								限制类型
							</th>
							<th width="40%">
								备注
						   </th>
						</pg:header>
						<pg:notify>
							<div class="nodata">
								<img
									src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>" />
							</div>
						</pg:notify>
						<pg:list>
							<tr>
							  <td class="td_center">
									<input onClick="checkOne('checkBoxAll','id')" type="checkbox"
										name="id" value="<pg:cell colName="id" defaultValue=""/>">
								</td>
								<td>
									<pg:cell colName="controluser" defaultValue="" />
								</td>
								<td>
									<pg:cell colName="ip" defaultValue="" />
								</td>
								<td>
									<pg:equal colName="filtertype" value="1">黑名单</pg:equal>
									<pg:equal colName="filtertype" value="0">白名单</pg:equal>
								</td>
								<td>
									<pg:cell colName="ipdesc" defaultValue="" />
								</td>
							</tr>
						</pg:list>
					</table>
				 </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="10" sizescope="5,10,20,50,100"/></div>

    </pg:pager>
    </pg:notempty>
</div>		

