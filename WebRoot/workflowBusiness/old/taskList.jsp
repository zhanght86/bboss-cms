<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<link href="stylesheet/main.css" rel="stylesheet" type="text/css" />
<%@ include file="/common/jsp/css.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false&skin=sany"></script>
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
function openAuditDlg(url){
    var autidUrl=ctx+url;
    $.dialog({ id:'iframeNewId', title:'',width:1160,height:650, content:'url:'+autidUrl,lock: true, currentwindow:window});
} 
//查找
function queryData(){
	$("#custombackContainer").load("${pageContext.request.contextPath}/html/targetTask.page?businessType=${businessType} #customContent",formToJson("#queryForm"),
	 function(){loadjs()}); 
}
</script>
<%int i=1; %>
<div id="customContent" align="center">
<pg:empty actual="${datas}" >
	<div class="nodata">
	<img src="${pageContext.request.contextPath}/html/images/no_data.jpg"/></div>
</pg:empty> 
<pg:notempty actual="${datas}">
   <pg:pager scope="request"  data="datas" desc="true" isList="false" >  
	<pg:param name="subjectNo"/>
	<pg:param name="subjectName"/>
	<pg:param name="sort"/>
	<pg:param name="businessType"/>
	
	
	<!-- 加入 class="tableOutline" 可控制表格宽度，滚动条展示 -->
	<div id="custombackContainer">
	<div id="changeColor">
	 <table width="100%" border="0" cellpadding="0" cellspacing="0" class="stable" id="tb">
        <pg:header>
        	<th  width="40">序号</th> 	
       		<th width="60">发送人</th>
       		<th>内容</th>
       		<th width="80" align="center">处理</th>
       	</pg:header>	
      <pg:list autosort="false">
   		<tr>
	        <td align="center"><%=i++ %></td>
            <td align="center"><pg:cell colName="sender"/></td>
            <td align="center"><pg:cell colName="title"/></td>  
            <td align="center"><a href="javascript:openAuditDlg('<pg:cell colName="url"/>?taskId=<pg:cell colName="taskId"/>')" >处理</a></td>
        </tr>
	 </pg:list>
    </table>
    </div>
	<div class="pages"><input type="hidden" value="<pg:querystring/>" id="querystring"/><pg:index tagnumber="5" sizescope="10,20,50,100"/></div>
</div>
    </pg:pager>
</pg:notempty>
</div>		
