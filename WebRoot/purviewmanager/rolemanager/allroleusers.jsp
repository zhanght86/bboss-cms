<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>

<%--
描述：台账列表信息设置
作者：许石玉
版本：1.0
日期：2012-02-08
 --%>	

<div id="users">
<pg:empty actual="${allusers}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}<pg:message code='sany.pdp.common.list.nodata.path'/>"/></div>
</pg:empty> 
<pg:notempty actual="${allusers}">
   <pg:pager scope="request"  data="allusers" desc="false" isList="false" containerid="allusers" selector="users">
	<pg:param name="roleid"/>
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="changeColor" >
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
            <th>序号</th>
       		<th>已授予账号</th>
       		<th>用户工号</th>
       		<th>用户名称</th>
       		<th>用户性别</th>
       		<th>部门</th>
       		
       	</pg:header>	
      <pg:list>

   		<tr  >
   		        
                <td><pg:rowid increament="1" offset="false"/> </td>
                <td><span class="toolTip" title="<pg:cell colName="userName"/>"><pg:cell colName="userName"  /></span></td>
                 <td> <pg:cell colName="workNumber" /> </td>
                <td> <pg:cell colName="userRealname" /> </td>
        		<td><pg:case colName="userSex">
        			<pg:equal value="M">男</pg:equal>
        			<pg:equal value="F">女</pg:equal>
        			<pg:other>未知</pg:other>
        			</pg:case>
        		</td>    
        		<td><pg:cell colName="jobName" /></td>       
               
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><pg:index tagnumber="10" sizescope="5,10"/></div>

    </pg:pager>
    </pg:notempty>
</div>		
